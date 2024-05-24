package com.ruijie.rcos.rcdc.rco.module.web.service.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbVmGraphicsDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.vm.VmIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.RemoteAssistStateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.RemoteAssistWindowTitleEnums;
import com.ruijie.rcos.rcdc.rco.module.web.dto.RemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.RemoteAssistStrategyService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: PC 小助手支持编辑镜像
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023.11.07
 *
 * @author linhj
 */
@Service
public class ImageEstSpiceRemoteAssistServiceImpl implements RemoteAssistStrategyService {

    private static final String IMAGE_EST_SPICE = "imageEstSpice";

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI vdiDeskMgmtAPI;

    @Override
    public Boolean isNeedHandle(String component) {
        Assert.hasText(component, "component must not be null");
        return IMAGE_EST_SPICE.equals(component);
    }

    @Override
    public RemoteAssistStateDTO queryState(RemoteAssistDTO remoteAssistDTO) {
        Assert.notNull(remoteAssistDTO, "remoteAssistDTO can not be null");
        return new RemoteAssistStateDTO();
    }

    @Override
    public CloudDesktopRemoteAssistDTO queryVncUrl(RemoteAssistDTO remoteAssistDTO) throws BusinessException {
        Assert.notNull(remoteAssistDTO, "remoteAssistDTO can not be null");

        CloudDesktopRemoteAssistDTO assistInfo = new CloudDesktopRemoteAssistDTO();
        final CbbImageTemplateDetailDTO imageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(remoteAssistDTO.getBusinessId());
        final CbbVmGraphicsDTO cbbVmGraphicsDTO =
                vdiDeskMgmtAPI.querySpiceById(new VmIdRequest(imageTemplateDetailDTO.getPlatformId(), imageTemplateDetailDTO.getTempVmId()));
        AssistInfoDTO estAssistInfoDTO = new AssistInfoDTO();
        estAssistInfoDTO.setIp(cbbVmGraphicsDTO.getAddress());
        estAssistInfoDTO.setPort(cbbVmGraphicsDTO.getPort());
        estAssistInfoDTO.setPassword(remoteAssistDTO.getBusinessId().toString());
        assistInfo.setAssistInfo(estAssistInfoDTO);
        assistInfo.setTitle(String.format("%s[%s]", RemoteAssistWindowTitleEnums.REMOTE_ASSIST.getTitle(),
                imageTemplateDetailDTO.getImageName()));
        return assistInfo;
    }
}
