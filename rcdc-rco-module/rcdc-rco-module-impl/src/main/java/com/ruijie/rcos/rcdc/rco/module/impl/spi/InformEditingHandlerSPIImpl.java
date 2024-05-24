package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateEditingInfoDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageIdRequestDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 22:21
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.VDI_EDIT_IMAGE_INFORM_EDITING_ACTION)
public class InformEditingHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<VDIEditImageIdRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InformEditingHandlerSPIImpl.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private VDIEditImageHelper helper;

    @Override
    CbbResponseShineMessage getResponseMessage(CbbDispatcherRequest request, VDIEditImageIdRequestDTO requestDTO, UUID adminId) {
        // 查询传入的镜像是否允许被当前管理员操作
        CbbImageTemplateEditingInfoDTO imageTemplateEditingInfoDTO;
        try {
            imageTemplateEditingInfoDTO = helper.getImageEditingInfoIfPresent(requestDTO.getId(), adminId, request.getTerminalId());
            Assert.isTrue(imageTemplateEditingInfoDTO != null, "image template is not editing!");
        } catch (BusinessException e) {
            LOGGER.error("当前管理员不可操作目标镜像", e);
            return buildErrorResponseMessage(request);
        }

        return buildResponseMessage(request, new Object());
    }

    @Override
    VDIEditImageIdRequestDTO resolveJsonString(String dataJsonString) {
        return JSONObject.parseObject(dataJsonString, VDIEditImageIdRequestDTO.class);
    }
}
