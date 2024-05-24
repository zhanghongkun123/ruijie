package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.UUID;

import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbPublishImageTemplateDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.MenuType;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.PublishImageRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.PublishImageResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 发布镜像
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 15:27
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.VDI_EDIT_IMAGE_PUBLISH_ACTION)
public class PublishImageHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<PublishImageRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishImageHandlerSPIImpl.class);

    private static final String PUBLISH_IMAGE_THREAD_NAME = "publish_image";

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private VDIEditImageHelper helper;

    @Autowired
    private AdminPermissionAPI adminPermissionAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    CbbResponseShineMessage getResponseMessage(CbbDispatcherRequest request, PublishImageRequestDTO requestDTO, UUID adminId) {

        String terminalAddr = request.getTerminalId();
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", request.getTerminalId());
        }

        try {
            // 查询是否有发布VDI镜像模板权限 -5 没有发布权限
            boolean hasPermission = adminPermissionAPI.hasPermission(MenuType.IMAGE_TEMPLATE_PUBLISH, adminId);
            if (!hasPermission) {
                LOGGER.info("发布VDI镜像模板,当前管理员{}没有发布权限", adminId);
                return ShineMessageUtil.buildResponseMessageWithContent(request, PermissionConstants.NO_PUBLISH_PERMISSSION, "");
            }
        } catch (BusinessException e) {
            LOGGER.error("发布VDI镜像模板,当前管理员查询权限异常", e);
            return ShineMessageUtil.buildResponseMessageWithContent(request, Constants.FAILURE, "");
        }
        // 查询传入的镜像是否允许被当前管理员操作
        try {
            helper.getImageEditingInfoIfPresent(requestDTO.getId(), adminId, request.getTerminalId());
        } catch (BusinessException e) {
            LOGGER.error("当前管理员不可操作目标镜像", e);
            return buildResponseMessage(request, PublishImageResponseDTO.buildFailDTO(e.getI18nMessage()));
        }

        CbbPublishImageTemplateDTO publishRequest =
                new CbbPublishImageTemplateDTO(requestDTO.getId(), requestDTO.getChangeLog());
        try {
            cbbImageTemplateMgmtAPI.validatePublishImageTemplateRequest(publishRequest);
        } catch (BusinessException e) {
            LOGGER.error("发布镜像前校验不通过", e);
            helper.recordLogUpperMac(BusinessKey.RCDC_RCO_VDI_EDIT_IMAGE_PUBLISH_FAIL, e, adminId, publishRequest.getImageTemplateId(), terminalAddr);
            return buildResponseMessage(request, PublishImageResponseDTO.buildFailDTO(e.getI18nMessage()));
        }

        String finalTerminalAddr = terminalAddr;
        ThreadExecutors.execute(this.getClass().getName() + PUBLISH_IMAGE_THREAD_NAME,
            () -> publishImage(publishRequest, adminId, finalTerminalAddr));
        return buildResponseMessage(request, PublishImageResponseDTO.buildSuccessDTO());
    }

    @Override
    PublishImageRequestDTO resolveJsonString(String dataJsonString) {
        return JSONObject.parseObject(dataJsonString, PublishImageRequestDTO.class);
    }

    private void publishImage(CbbPublishImageTemplateDTO publishRequest, UUID adminId, String terminalMacAddr) {
        try {
            cbbImageTemplateMgmtAPI.publishImageTemplate(publishRequest);
            LOGGER.info("发布镜像成功");
            helper.recordLog(BusinessKey.RCDC_RCO_VDI_EDIT_IMAGE_PUBLISH_SUCCESS, adminId, publishRequest.getImageTemplateId(), terminalMacAddr);
        } catch (BusinessException e) {
            LOGGER.error("发布镜像失败", e);
            helper.recordLogUpperMac(BusinessKey.RCDC_RCO_VDI_EDIT_IMAGE_PUBLISH_FAIL, e, adminId, publishRequest.getImageTemplateId(),
                    terminalMacAddr);
        }
    }
}
