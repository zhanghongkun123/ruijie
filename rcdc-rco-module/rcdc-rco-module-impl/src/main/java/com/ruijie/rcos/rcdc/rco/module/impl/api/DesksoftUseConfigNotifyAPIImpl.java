package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesksoftUseConfigNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesksoftUseConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CmcConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 16:58
 *
 * @author linrenjian
 */
public class DesksoftUseConfigNotifyAPIImpl implements DesksoftUseConfigNotifyAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesksoftUseConfigNotifyAPIImpl.class);

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    /***
     * 全局配置
     */
    @Autowired
    private GlobalParameterService globalParameterService;

    /**
     * GT 上报开关
     */
    private String rcdcGtDesksoftMsgStatusValue = null;

    /**
     * 终端通知处理线程池,分配1个线程数
     */
    private static final ThreadExecutor NOTICE_HANDLER_THREAD_POOL =
            ThreadExecutors.newBuilder(DesksoftUseConfigNotifyAPIImpl.class.getName()).maxThreadNum(1).queueSize(20).build();

    @Override
    public void updateConfig(String parameter) throws BusinessException {
        Assert.notNull(parameter, "parameter must not null");

        updateUserConfigNotNotifyDesk(parameter);

        notifyAllDesk(parameter);
    }


    @Override
    public void updateUserConfigNotNotifyDesk(String parameter) throws BusinessException {
        Assert.notNull(parameter, "parameter must not null");

        globalParameterService.updateParameter(Constants.RCDC_GT_DESKSOFT_MSG_STATUS, parameter);

        rcdcGtDesksoftMsgStatusValue = parameter;
    }

    @Override
    public void notifyAllDesk(String parameter) {
        Assert.notNull(parameter, "parameter must not null");

        // 查询所有在线云桌面
        List<ViewUserDesktopEntity> viewUserDesktopEntityList =
                viewDesktopDetailDAO.findAllByDeskStateAndIsDelete(CbbCloudDeskState.RUNNING.name(), false);

        LOGGER.info("viewUserDesktopEntityList length{}", viewUserDesktopEntityList.size());
        // 向在线终端发送新管理员密码
        NOTICE_HANDLER_THREAD_POOL.execute(() -> sentDesk(viewUserDesktopEntityList, parameter));

    }



    private void sentDesk(List<ViewUserDesktopEntity> viewUserDesktopEntityList, String parameter) {
        // 逐个向运行中的桌面发送消息
        for (ViewUserDesktopEntity viewUserDesktopEntity : viewUserDesktopEntityList) {
            // 构造请求
            DesksoftUseConfigDTO desksoftUseConfigDTO = new DesksoftUseConfigDTO();
            Boolean enableCmc = Boolean.valueOf(parameter);
            desksoftUseConfigDTO.setDesksoftMsgStatus(enableCmc);
            // 判断是否开启
            desksoftUseConfigDTO.setCmcStatus(enableCmc ? CmcConstants.CMC_ON : CmcConstants.CMC_OFF);
            // 构造GT 消息请求
            CbbGuesttoolMessageDTO guesttoolMessageDTO = buildSendMessageRequest(desksoftUseConfigDTO, GuesttoolMessageResultTypeEnum.SUCCESS);
            // 设置桌面ID
            guesttoolMessageDTO.setDeskId(viewUserDesktopEntity.getCbbDesktopId());
            // 设置终端ID
            guesttoolMessageDTO.setTerminalId(viewUserDesktopEntity.getTerminalId());
            try {
                guestToolMessageAPI.asyncRequest(guesttoolMessageDTO);
            } catch (Exception e) {
                LOGGER.error("CMC策略[" + viewUserDesktopEntity.getId() + "]变更，发送变更通知到云桌面[" + JSON.toJSONString(viewUserDesktopEntity) +
                        "]失败", e);
            }
            // 异步发送信息
        }

    }

    /**
     * 构造 请求
     * 
     * @param messageDTO
     * @return
     */
    private CbbGuesttoolMessageDTO buildSendMessageRequest(DesksoftUseConfigDTO messageDTO, GuesttoolMessageResultTypeEnum resultTypeEnum) {
        // 配置默认code、message、content值
        GuesttoolMessageContent messageContent = new GuesttoolMessageContent();
        // 成功标识
        messageContent.setCode(resultTypeEnum.getCode());
        messageContent.setMessage(resultTypeEnum.getMessage());
        messageContent.setContent(messageDTO);

        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setBody(JSON.toJSONString(messageContent));
        // PORT :171
        dto.setCmdId(GuestToolCmdId.NOTIFY_GT_DESKSOFT_MSG_STATUS_CMD_ID);
        // PORT 136
        dto.setPortId(GuestToolCmdId.NOTIFY_GT_DESKSOFT_MSG_STATUS_PORT_ID);

        return dto;
    }

    @Override
    public DesksoftUseConfigDTO getGlobalCmcStrategy() {
        if (rcdcGtDesksoftMsgStatusValue == null) {
            rcdcGtDesksoftMsgStatusValue = globalParameterService.findParameter(Constants.RCDC_GT_DESKSOFT_MSG_STATUS);
        }
        // 获取CMC 软件软件使用是否上报开关
        String parameter = rcdcGtDesksoftMsgStatusValue;
        DesksoftUseConfigDTO desksoftUseConfigDTO = new DesksoftUseConfigDTO();
        Boolean enableCmc = Boolean.valueOf(parameter);
        desksoftUseConfigDTO.setDesksoftMsgStatus(enableCmc);
        desksoftUseConfigDTO.setCmcStatus(enableCmc ? CmcConstants.CMC_ON : CmcConstants.CMC_OFF);
        return desksoftUseConfigDTO;
    }

    /**
     * 初始化配置
     */
    @PostConstruct
    public void initMethod() {
        rcdcGtDesksoftMsgStatusValue = globalParameterService.findParameter(Constants.RCDC_GT_DESKSOFT_MSG_STATUS);
    }
}
