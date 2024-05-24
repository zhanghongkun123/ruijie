package com.ruijie.rcos.rcdc.rco.module.web.service.impl;

import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.ConnectVmStateDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryVmVncURLResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.RemoteAssistStateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.RemoteAssistBussinessStateEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.RemoteAssistWindowActionEnums;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.RemoteAssistWindowTitleEnums;
import com.ruijie.rcos.rcdc.rco.module.web.dto.RemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.RemoteAssistStrategyService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
public class EditAppRemoteAssistServiceImpl implements RemoteAssistStrategyService {

    private static final String EDIT_APP = "editApp";

    private static final String LEFT_BRACKET = "[";

    private static final String RIGHT_BRACKET = "]";

    private static final Integer REMOTE_AVAILABLE = 1;

    private static final Integer REMOTE_UNAVAILABLE = 0;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    @Override
    public Boolean isNeedHandle(String component) {
        Assert.hasText(component, "component must not be null");

        return EDIT_APP.equals(component);
    }

    @Override
    public RemoteAssistStateDTO queryState(RemoteAssistDTO remoteAssistDTO) throws BusinessException {
        Assert.notNull(remoteAssistDTO, "remoteAssistDTO can not be null");

        RemoteAssistStateDTO assistStateDTO = new RemoteAssistStateDTO();

        ConnectVmStateDTO connectVmStateDTO = cbbAppSoftwarePackageMgmtAPI.queryTempVmState(remoteAssistDTO.getBusinessId());
        if (connectVmStateDTO.getAppSoftwarePackageState() == AppStatusEnum.EDITING) {
            assistStateDTO.setTitle(RemoteAssistWindowTitleEnums.EDIT_APP.getTitle());
            assistStateDTO.setAvailable(connectVmStateDTO.isVncAvailable() ? REMOTE_AVAILABLE :
                    REMOTE_UNAVAILABLE);
            assistStateDTO.setState(RemoteAssistBussinessStateEnum.convertRemoteState(false));
        } else {
            assistStateDTO = buildRemoteAssistDTOForClose(UserBusinessKey.RCDC_RCO_IMAGE_FINISH_EDIT);
        }
        return assistStateDTO;
    }

    @Override
    public CloudDesktopRemoteAssistDTO queryVncUrl(RemoteAssistDTO remoteAssistDTO) throws BusinessException {
        Assert.notNull(remoteAssistDTO, "remoteAssistDTO can not be null");
        CloudDesktopRemoteAssistDTO assistInfo = new CloudDesktopRemoteAssistDTO();

        AssistInfoDTO appAssistInfoDTO = new AssistInfoDTO();
        final CbbQueryVmVncURLResultDTO queryAppVncConnect = cbbAppSoftwarePackageMgmtAPI.queryVncConnect(remoteAssistDTO.getBusinessId());
        BeanUtils.copyProperties(queryAppVncConnect, appAssistInfoDTO);
        appAssistInfoDTO.setSsl(queryAppVncConnect.getIsCrypto());
        assistInfo.setAssistInfo(appAssistInfoDTO);

        AppSoftwarePackageDTO packageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(remoteAssistDTO.getBusinessId());
        String title = StringUtils.join(RemoteAssistWindowTitleEnums.EDIT_APP.getTitle(),
                LEFT_BRACKET, packageDTO.getName(),
                RIGHT_BRACKET);
        assistInfo.setTitle(title);
        return assistInfo;
    }

    private RemoteAssistStateDTO buildRemoteAssistDTOForClose(String message) {
        RemoteAssistStateDTO assistStateDTO = new RemoteAssistStateDTO();
        assistStateDTO.setTitle(RemoteAssistWindowTitleEnums.EDIT_APP.getTitle());
        assistStateDTO.setAction(RemoteAssistWindowActionEnums.CLOSE);
        assistStateDTO.setMessage(LocaleI18nResolver.resolve(message));
        return assistStateDTO;
    }
}
