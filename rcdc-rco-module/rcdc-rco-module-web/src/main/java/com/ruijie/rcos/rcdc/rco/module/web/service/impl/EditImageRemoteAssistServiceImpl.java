package com.ruijie.rcos.rcdc.rco.module.web.service.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryImageTemplateEditStateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryImageTemplateEditStateResultDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryVmVncURLResultDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbRemoteConnectState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
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
public class EditImageRemoteAssistServiceImpl implements RemoteAssistStrategyService {

    private static final String EDIT_IMAGE = "editImage";

    private static final String LEFT_BRACKET = "[";

    private static final String RIGHT_BRACKET = "]";

    private static final Integer REMOTE_AVAILABLE = 1;

    private static final Integer REMOTE_UNAVAILABLE = 0;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Override
    public Boolean isNeedHandle(String component) {
        Assert.hasText(component, "component must not be null");

        return EDIT_IMAGE.equals(component);
    }

    @Override
    public RemoteAssistStateDTO queryState(RemoteAssistDTO remoteAssistDTO) throws BusinessException {
        Assert.notNull(remoteAssistDTO, "remoteAssistDTO can not be null");
        final CbbQueryImageTemplateEditStateDTO queryImageTemplateEditStateDTO =
                new CbbQueryImageTemplateEditStateDTO(remoteAssistDTO.getBusinessId(), remoteAssistDTO.getUserId());
        final CbbQueryImageTemplateEditStateResultDTO editStateResultDTO =
                cbbImageTemplateMgmtAPI.queryImageTemplateEditState(queryImageTemplateEditStateDTO);
        RemoteAssistStateDTO assistStateDTO = new RemoteAssistStateDTO();
        if (editStateResultDTO.getImageState() == ImageTemplateState.EDITING) {
            assistStateDTO.setTitle(RemoteAssistWindowTitleEnums.EDIT_IMAGE.getTitle());
            assistStateDTO.setAvailable(editStateResultDTO.isVncAvailable() ? REMOTE_AVAILABLE :
                    REMOTE_UNAVAILABLE);
            assistStateDTO.setState(RemoteAssistBussinessStateEnum.convertRemoteState(editStateResultDTO.isInstallDriver()));

            if (CbbRemoteConnectState.ERROR.equals(editStateResultDTO.getRemoteConnectState())) {
                assistStateDTO = buildRemoteAssistDTOForClose(UserBusinessKey.RCDC_RCO_IMAGE_STATE_ERROR);
            }
        } else {
            assistStateDTO = buildRemoteAssistDTOForClose(UserBusinessKey.RCDC_RCO_IMAGE_FINISH_EDIT);
        }
        return assistStateDTO;
    }

    @Override
    public CloudDesktopRemoteAssistDTO queryVncUrl(RemoteAssistDTO remoteAssistDTO) throws BusinessException {
        Assert.notNull(remoteAssistDTO, "remoteAssistDTO can not be null");


        CloudDesktopRemoteAssistDTO assistInfo = new CloudDesktopRemoteAssistDTO();

        AssistInfoDTO assistInfoDTO = new AssistInfoDTO();
        final CbbQueryVmVncURLResultDTO cbbQueryVmVncURLResultDTO = cbbImageTemplateMgmtAPI.queryVmVncURL(remoteAssistDTO.getBusinessId());
        BeanUtils.copyProperties(cbbQueryVmVncURLResultDTO, assistInfoDTO);
        assistInfoDTO.setSsl(cbbQueryVmVncURLResultDTO.getIsCrypto());
        assistInfo.setAssistInfo(assistInfoDTO);

        CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(remoteAssistDTO.getBusinessId());
        String title = StringUtils.join(RemoteAssistWindowTitleEnums.EDIT_IMAGE.getTitle(),
                LEFT_BRACKET, cbbImageTemplateDetailDTO.getImageName(),
                RIGHT_BRACKET);

        assistInfo.setTitle(title);
        return assistInfo;
    }

    private RemoteAssistStateDTO buildRemoteAssistDTOForClose(String message) {
        RemoteAssistStateDTO assistStateDTO = new RemoteAssistStateDTO();
        assistStateDTO.setTitle(RemoteAssistWindowTitleEnums.EDIT_IMAGE.getTitle());
        assistStateDTO.setAction(RemoteAssistWindowActionEnums.CLOSE);
        assistStateDTO.setMessage(LocaleI18nResolver.resolve(message));
        return assistStateDTO;
    }
}

