package com.ruijie.rcos.rcdc.rco.module.web.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.RemoteAssistMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.RemoteAssistStateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.RemoteAssistBussinessStateEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.RemoteAssistStateEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.RemoteAssistWindowActionEnums;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.RemoteAssistWindowTitleEnums;
import com.ruijie.rcos.rcdc.rco.module.web.dto.RemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.RemoteAssistStrategyService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 远程桌面策略实现类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/15
 *
 * @author zhiweiHong
 */
@Service
public class DesktopRemoteAssistServiceImpl implements RemoteAssistStrategyService {

    private static final String REMOTE_ASSIST = "remoteAssist";

    @Autowired
    private RemoteAssistMgmtAPI remoteAssistInquire;

    @Override
    public Boolean isNeedHandle(String component) {
        Assert.hasText(component, "component must not be null" );

        return REMOTE_ASSIST.equals(component);
    }

    @Override
    public RemoteAssistStateDTO queryState(RemoteAssistDTO remoteAssistDTO) throws BusinessException {
        Assert.notNull(remoteAssistDTO, "remoteAssistDTO can not be null");

        RemoteAssistRequest remoteAssistRequest = new RemoteAssistRequest();
        remoteAssistRequest.setDeskId(remoteAssistDTO.getBusinessId());
        remoteAssistRequest.setAdminId(remoteAssistDTO.getUserId());
        CloudDesktopRemoteAssistDTO assistInfo = remoteAssistInquire.queryRemoteAssistInfo(remoteAssistRequest);
        String state = assistInfo.getAssistState();

        RemoteAssistStateDTO assistStateDTO = new RemoteAssistStateDTO();
        assistStateDTO.setMessage(RemoteAssistStateEnum.getMessageByState(state));
        assistStateDTO.setAction(RemoteAssistWindowActionEnums.getActionByState(state));

        assistStateDTO.setTitle(RemoteAssistWindowTitleEnums.REMOTE_ASSIST.getTitle());
        assistStateDTO.setState(RemoteAssistBussinessStateEnum.NORMAL);
        return assistStateDTO;
    }

    @Override
    public CloudDesktopRemoteAssistDTO queryVncUrl(RemoteAssistDTO remoteAssistDTO) throws BusinessException {
        Assert.notNull(remoteAssistDTO, "remoteAssistDTO can not be null");

        RemoteAssistRequest remoteAssistRequest = new RemoteAssistRequest();
        remoteAssistRequest.setDeskId(remoteAssistDTO.getBusinessId());
        remoteAssistRequest.setAdminId(remoteAssistDTO.getUserId());

        CloudDesktopRemoteAssistDTO assistInfo = remoteAssistInquire.queryRemoteAssistInfo(remoteAssistRequest);
        assistInfo.setTitle(RemoteAssistWindowTitleEnums.REMOTE_ASSIST.getTitle());
        return assistInfo;
    }
}
