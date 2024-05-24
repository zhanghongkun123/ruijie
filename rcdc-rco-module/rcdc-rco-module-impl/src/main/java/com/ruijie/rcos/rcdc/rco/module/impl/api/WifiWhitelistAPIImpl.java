package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WifiWhitelistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.request.DeleteWifiWhitelistRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.request.SaveWifiWhitelistRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.response.GetWifiWhitelistResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.WifiWhitelistDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.WifiWhitelistEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 *
 * Description: wifi 白名单实现类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
public class WifiWhitelistAPIImpl implements WifiWhitelistAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(WifiWhitelistAPIImpl.class);

    @Autowired
    private WifiWhitelistDAO wifiWhitelistDAO;

    @Autowired
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Override
    public void updateWifiWhitelist(SaveWifiWhitelistRequest request) {
        Assert.notNull(request, "request can not be null");

        // 获取待更新的组Id列表
        List<UUID> needUpdateGroupIdList = getNeedUpdateGroupIdList(request.getTerminalGroupId(), request.getNeedApplyToSubgroup());
        batchUpdateWhitelist(request.getWifiWhiteList(), needUpdateGroupIdList);

        GetWifiWhitelistResponse response = new GetWifiWhitelistResponse();
        response.setWifiWhiteList(request.getWifiWhiteList());
        //由于更新无线白名单 只需要通知IDV  底层已经是DIV  不需要修改
        List<String> terminalIdList = getNotifyTerminalIdList(needUpdateGroupIdList);
        notifyOnlineIdvAsync(response, terminalIdList);
    }

    @Override
    public void createWifiWhitelist(@NonNull SaveWifiWhitelistRequest request) {
        Assert.notNull(request, "request can not be null");

        List<WifiWhitelistDTO> wifiWhiteList = request.getWifiWhiteList();
        // 批量新增
        batchInsertWhitelist(wifiWhiteList, request.getTerminalGroupId());
    }

    @Override
    public void deleteWifiWhitelist(@NonNull DeleteWifiWhitelistRequest request) {
        Assert.notNull(request, "request can not be null");
        request.verify();

        wifiWhitelistDAO.deleteByTerminalGroupId(request.getOldTerminalGroupId());
        List<TerminalDTO> needNotifyTerminalList = request.getNeedNotifyTerminalList();
        if (CollectionUtils.isEmpty(needNotifyTerminalList)) {
            // 如果终端集合为空，直接返回
            return;
        }

        GetWifiWhitelistResponse getWifiWhitelistResponse = getWifiWhitelistByTerminalGroupId(request.getMoveTerminalGroupId());
        List<String> terminalIdList = needNotifyTerminalList.stream().filter(item -> isTerminalOnline(item.getTerminalState()))
                .map(TerminalDTO::getId).collect(Collectors.toList());
        LOGGER.info("获取目标终端分组[{}]", JSONArray.toJSONString(terminalIdList));
        notifyOnlineIdvAsync(getWifiWhitelistResponse, terminalIdList);
    }

    @Override
    public GetWifiWhitelistResponse getWifiWhitelistByTerminalGroupId(@NonNull UUID terminalGroupId) {
        Assert.notNull(terminalGroupId, "terminalGroupId can not be null");
        List<WifiWhitelistEntity> wifiWhitelistEntityList = wifiWhitelistDAO.findByTerminalGroupIdOrderByIndexAsc(terminalGroupId);

        GetWifiWhitelistResponse result = new GetWifiWhitelistResponse();
        if (CollectionUtils.isEmpty(wifiWhitelistEntityList)) {
            return result;
        }
        // 存在白名单时，转换返回
        for (WifiWhitelistEntity entity : wifiWhitelistEntityList) {
            result.addWifiWhitelist(entity.getSsid(), entity.getIndex());
        }
        return result;
    }

    @Override
    public void notifyIdvIfTargetGroupHasWhitelist(@NonNull UUID targetGroupId, @NonNull String terminalId) throws BusinessException {
        Assert.notNull(targetGroupId, "targetGroupId can not be null");
        Assert.notNull(terminalId, "terminalId can not be null");

        GetWifiWhitelistResponse wifiWhitelistResponse = getWifiWhitelistByTerminalGroupId(targetGroupId);
        LOGGER.info("获取目标分组白名单信息:[{}]", JSON.toJSONString(wifiWhitelistResponse));
        TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);
        LOGGER.info("获取当前终端信息:[{}]", JSON.toJSONString(terminalDTO));
        // 如果终端在线异步通知白名单更换
        boolean isNeedNotifyTerminal = Objects.nonNull(terminalDTO) && isTerminalOnline(terminalDTO.getTerminalState());
        if (isNeedNotifyTerminal) {
            notifyOnlineIdvAsync(wifiWhitelistResponse, Collections.singletonList(terminalDTO.getId()));
        }
    }


    /**
     * 判断终端是否在线
     *
     * @param state 终端状态
     * @return true 在线 ，false 离线
     */
    private Boolean isTerminalOnline(CbbTerminalStateEnums state) {
        return CbbTerminalStateEnums.ONLINE == state;
    }



    /**
     * 异步通知在线IDV白名单信息
     *
     * @param getWifiWhitelistResponse 白名单实体
     * @param terminalIdList 在线终端ID列表
     */
    private void notifyOnlineIdvAsync(GetWifiWhitelistResponse getWifiWhitelistResponse, List<String> terminalIdList) {
        if (CollectionUtils.isEmpty(terminalIdList)) {
            LOGGER.info("终端集合为空，无需通知");
            return;
        }
        terminalIdList = terminalIdList.stream().distinct().collect(Collectors.toList());
        LOGGER.info("批量发送更新白名单命令给终端，终端集合为[{}]", terminalIdList);
        List<String> finalTerminalIdList = terminalIdList;
        String whitelistObj = JSON.toJSONString(getWifiWhitelistResponse);
        ThreadExecutors.submit("notifyTerminalWifiWhitelist", () -> {
            for (String terminalId : finalTerminalIdList) {

                CbbShineMessageRequest request = CbbShineMessageRequest.create(ShineAction.UPDATE_WIFI_WHITELIST, terminalId);
                request.setContent(whitelistObj);
                LOGGER.info("发送白名单消息给终端[{}],白名单消息为:[{}],request实体为:[{}]", terminalId, whitelistObj, JSON.toJSONString(request));
                CbbShineMessageResponse response = null;
                try {
                    response = messageHandlerAPI.syncRequest(request);
                    LOGGER.info("shine的回应为[code:{},content:{}]", response.getCode(), response.getContent());
                } catch (Exception e) {
                    LOGGER.error("向终端[{}]下发更新白名单请求失败,{}", terminalId, e);
                }
            }
        });
    }

    /**
     * 获取需要通知的终端Id，实际上获取的是终端MAC地址
     *
     * @param groupIdList 需要获取终端的分组ID集合
     * @return 终端MAC地址集合。可能返回空集合
     */
    private List<String> getNotifyTerminalIdList(List<UUID> groupIdList) {
        List<String> resultList = new ArrayList<>();
        for (UUID groupId : groupIdList) {
            List<String> terminalIdList = getTerminalIdByGroupId(groupId);
            if (!CollectionUtils.isEmpty(terminalIdList)) {
                resultList.addAll(terminalIdList);
            }
        }
        return resultList;
    }

    /**
     * 根据终端分组Id获取终端集合
     *
     * @param groupId 终端组Id
     * @return 终端Mac地址集合
     */
    private List<String> getTerminalIdByGroupId(UUID groupId) {
        //由于更新无线白名单 只需要通知IDV   顶层要求已经是DIV  不需要修改
        List<TerminalDTO> terminalList =
                userTerminalMgmtAPI.queryListByPlatformAndGroupIdAndState(CbbTerminalPlatformEnums.IDV, groupId, CbbTerminalStateEnums.ONLINE);
        return terminalList.stream().map(TerminalDTO::getId).collect(Collectors.toList());
    }



    /**
     * 根据传入的组集合，批量更新白名单
     *
     * @param wifiWhitelist 白名单列表
     * @param updateGroupIdList 待更新的组Id列表
     */
    private void batchUpdateWhitelist(List<WifiWhitelistDTO> wifiWhitelist, List<UUID> updateGroupIdList) {

        LOGGER.info("批量更新终端组的白名单，批量更新组为[{}]", updateGroupIdList);
        // 批量新增
        for (UUID groupId : updateGroupIdList) {
            // 删除之前的白名单
            wifiWhitelistDAO.deleteByTerminalGroupId(groupId);
            batchInsertWhitelist(wifiWhitelist, groupId);
        }
    }

    /**
     * 批量增加白名单信息
     *
     * @param wifiWhiteList 白名单信息
     * @param terminalGroupId 对应终端分组Id
     */
    private void batchInsertWhitelist(List<WifiWhitelistDTO> wifiWhiteList, UUID terminalGroupId) {
        if (CollectionUtils.isEmpty(wifiWhiteList)) {
            // 如果传入的集合为空,认为关闭白名单配置。
            return;
        }
        List<WifiWhitelistEntity> wifiWhitelistEntityList =
                wifiWhiteList.stream().map(item -> new WifiWhitelistEntity(item, terminalGroupId)).collect(Collectors.toList());
        wifiWhitelistDAO.saveAll(wifiWhitelistEntityList);
    }

    /**
     * 获取需要更新的groupId集合
     *
     * @param groupId 当前修改的groupId
     * @param needApplyToSubgroup 判断是否需要下发到子组，如果需要，查询当前groupId的子组id
     * @return 返回groupId，如果需要下发子组，返回列表中会附带子组Id
     */
    private List<UUID> getNeedUpdateGroupIdList(UUID groupId, Boolean needApplyToSubgroup) {
        Assert.notNull(groupId, "groupId must not be null");
        Assert.notNull(needApplyToSubgroup, "needApplyToSubgroup must not be null");

        List<UUID> resultList = new ArrayList<>();
        resultList.add(groupId);
        // 判断是否需要获取子组信息,未分组下不存在子组，所以忽略
        boolean isNeedLoadSubGroup = needApplyToSubgroup && !CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID.equals(groupId);
        if (isNeedLoadSubGroup) {
            List<CbbTerminalGroupDetailDTO> terminalGroupDtoList = terminalGroupMgmtAPI.listTerminalGroup();
            getSubGroupIdList(resultList, terminalGroupDtoList, Collections.singletonList(groupId));
        }

        return Collections.unmodifiableList(resultList);
    }

    /**
     * 递归获取子组ID集合
     *
     * @param childIdList 子组Id集合存储位置
     * @param groupDataList TerminalGroupDTO集合，数据列
     * @param parentIds 父Id集合，查询条件
     */
    private void getSubGroupIdList(List<UUID> childIdList, List<CbbTerminalGroupDetailDTO> groupDataList, List<UUID> parentIds) {
        Assert.isTrue(!CollectionUtils.isEmpty(groupDataList), "groupDataList must be existed");
        Assert.isTrue(!CollectionUtils.isEmpty(parentIds), "parentIds must be existed");
        Assert.notNull(childIdList, "childIdList can not be null");

        List<UUID> childGroupIdList = new ArrayList<>();
        for (CbbTerminalGroupDetailDTO dto : groupDataList) {
            if (parentIds.contains(dto.getParentGroupId())) {
                childGroupIdList.add(dto.getId());
            }
        }
        if (childGroupIdList.isEmpty()) {
            return;
        }
        childIdList.addAll(childGroupIdList);
        getSubGroupIdList(childIdList, groupDataList, childGroupIdList);
    }

}
