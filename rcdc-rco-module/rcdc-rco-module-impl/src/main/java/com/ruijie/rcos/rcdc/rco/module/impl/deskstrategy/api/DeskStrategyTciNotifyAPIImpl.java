package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.api;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyTciNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopViewService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/3
 *
 * @author songxiang
 */

public class DeskStrategyTciNotifyAPIImpl implements DeskStrategyTciNotifyAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStrategyTciNotifyAPIImpl.class);

    private static final String COMPUTER_NAME = "computerName";

    private static final int DEFAULT_LIMIT = 1000;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CloudDesktopViewService cloudDesktopViewService;

    private static final ExecutorService SEND_FETCH_COMMAND_THREAD_POOL =
            ThreadExecutors.newBuilder("send_fetch_start_params_thread").maxThreadNum(20).queueSize(1).build();

    @Override
    public void notifyFetchStartParams(UUID strategyId) {
        // 根据策略ID查询对面的云桌面
        Assert.notNull(strategyId, "strategyId must not null");
        try {
            sendToAllTerminalOfStrategy(strategyId);
        } catch (Exception e) {
            LOGGER.error("修改TCI策略后，下发给所有的TCI公用终端失败,不影响后续的行为", e.getMessage(), e);
        }

    }

    @Override
    public void notifyFetchStartParams() {
        LOGGER.info("通知所有在线TCI公共终端获取启动参数");

        int page = 0;
        PageSearchRequest pageSearchRequest = new PageSearchRequest();
        pageSearchRequest.setIsAnd(true);
        pageSearchRequest.setPage(page);
        pageSearchRequest.setLimit(DEFAULT_LIMIT);
        pageSearchRequest.setMatchEqualArr(new MatchEqual[]{
            new MatchEqual("terminalPlatform", new String[]{CbbTerminalPlatformEnums.VOI.toString()}),
            new MatchEqual("idvTerminalModel", new String[]{IdvTerminalModeEnums.PUBLIC.toString()})
        });

        while (true) {
            Page<ViewUserDesktopEntity> pageResponse = cloudDesktopViewService.pageQuery(pageSearchRequest);
            List<ViewUserDesktopEntity> userDesktopList = pageResponse.getContent();

            if (CollectionUtils.isEmpty(userDesktopList)) {
                LOGGER.info("第[{}]页数据为空，结束处理", page);
                break;
            }

            // 异步通知SHINE
            List<String> terminalIdList = userDesktopList.stream().map(ViewUserDesktopEntity::getTerminalId).collect(Collectors.toList());
            asyncSendFetchCommand(terminalIdList);

            // 查询下一页数据
            pageSearchRequest.setPage(++page);
        }

        LOGGER.info("完成通知所有在线TCI公共终端获取启动参数");
    }

    private void sendToAllTerminalOfStrategy(UUID strategyId) {
        LOGGER.info("【修改TCI策略后，向TCI公共终端下发策略变更信息】策略ID:[{}]", strategyId);
        List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.getAllDesktopByStrategyId(strategyId);
        if (CollectionUtils.isEmpty(desktopList)) {
            return;
        }
        List<String> terminalIdList = desktopList.stream().filter(desktop -> //
                desktop.getTerminalPlatform() != null && //
                desktop.getIdvTerminalMode() != null && //
                desktop.getTerminalPlatform().equals(CbbTerminalPlatformEnums.VOI.name()) && //
                desktop.getIdvTerminalMode().equals(IdvTerminalModeEnums.PUBLIC.name()))//
                .map(CloudDesktopDTO::getTerminalId)//
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(terminalIdList)) {
            return;
        }

        asyncSendFetchCommand(terminalIdList);
    }

    private void asyncSendFetchCommand(List<String> terminalIdList) {
        SEND_FETCH_COMMAND_THREAD_POOL.execute(() -> {
            for (String terminalId : terminalIdList) {

                boolean isOnline = cbbTerminalOperatorAPI.isTerminalOnline(terminalId);
                if (isOnline) {
                    sendToTerminal(terminalId);
                } else {
                    LOGGER.info("【TCI公用终端桌面获取参数】，终端:[{}]离线,不发送策略信息", terminalId);
                }


            }
        });
    }

    @Override
    public void notifyDeskEditComputerName(UUID deskId, String computerName) {
        Assert.notNull(deskId, "deskId must not null");
        Assert.notNull(computerName, "computerName must not null");

        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);

            Boolean isMatchSend = isMatchSendCondition(cloudDesktopDetailDTO);

            if (isMatchSend) {
                sendEditComputerToTerminal(cloudDesktopDetailDTO.getTerminalId(), computerName);
            }
        } catch (Exception e) {
            LOGGER.error("通知下发编辑计算机桌面失败，不影响后续行为", e.getMessage(), e);
        }


    }


    @Override
    public void notifyDeskFetchStartParams(UUID deskId) {
        Assert.notNull(deskId, "deskId must not null");
        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);

            Boolean isMatchSend = isMatchSendCondition(cloudDesktopDetailDTO);

            if (isMatchSend) {
                sendToTerminal(cloudDesktopDetailDTO.getTerminalId());
            }
        } catch (Exception e) {
            LOGGER.error("通知TCI公共终端获取云桌面启动参数失败，不影响后续行为 " + e.getMessage(), e);
        }


    }



    private Boolean isMatchSendCondition(CloudDesktopDetailDTO cloudDesktopDetailDTO) throws BusinessException {

        if (cloudDesktopDetailDTO == null) {
            return false;
        }

        String terminalId = cloudDesktopDetailDTO.getTerminalId();

        if (terminalId == null) {
            LOGGER.warn("【TCI公用终端桌面获取参数】,终端id为空，不下发");
            return false;
        }

        if (StringUtils.isEmpty(cloudDesktopDetailDTO.getIdvTerminalModel())) {
            LOGGER.warn("【TCI公用终端桌面获取参数】,终端模式:idvTerminalModel为空，不下发");
            return false;
        }


        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.append(cloudDesktopDetailDTO.getTerminalPlatform(), CbbTerminalPlatformEnums.VOI.name());
        equalsBuilder.append(cloudDesktopDetailDTO.getIdvTerminalModel(), IdvTerminalModeEnums.PUBLIC.name());

        return equalsBuilder.isEquals();

    }

    private void sendToTerminal(String terminalId) {
        try {
            LOGGER.info("向终端[ " + terminalId + " ]发送【获取启动参数策略消息】");
            shineMessageHandler.requestContent(terminalId, ShineAction.SEND_FETCH_START_PARAM_COMMAND, new JSONObject());
        } catch (Exception e) {
            LOGGER.error("向终端[" + terminalId + "]发送【获取启动参数策略消息】失败", e);
        }
    }

    private void sendEditComputerToTerminal(String terminalId, String computerName) {
        try {
            LOGGER.info("向终端[ " + terminalId + " ]发送【编辑计算机名】:" + computerName);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(COMPUTER_NAME, computerName);
            shineMessageHandler.requestContent(terminalId, ShineAction.SEND_EDIT_COMPUTER_NAME, jsonObject);
        } catch (Exception e) {
            LOGGER.error("向终端[" + terminalId + "]发送【编辑计算机名】失败", e);
        }
    }
}
