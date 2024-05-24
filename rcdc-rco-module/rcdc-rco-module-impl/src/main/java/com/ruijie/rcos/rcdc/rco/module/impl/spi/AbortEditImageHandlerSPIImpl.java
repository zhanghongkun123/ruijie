package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.UUID;

import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.MenuType;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageIdRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 放弃编辑镜像
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 11:42
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.VDI_EDIT_IMAGE_GIVE_UP_ACTION)
public class AbortEditImageHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<VDIEditImageIdRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbortEditImageHandlerSPIImpl.class);

    private static final String ABORT_EDIT_IMAGE_THREAD_NAME = "abort_edit_image";

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private VDIEditImageHelper helper;

    @Autowired
    private AdminPermissionAPI adminPermissionAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;


    @Override
    CbbResponseShineMessage getResponseMessage(CbbDispatcherRequest request, VDIEditImageIdRequestDTO requestDTO, UUID adminId) {

        try {
            // 查询是否有放弃VDI镜像模板的权限 -5 没有编辑权限
            boolean hasPermission = adminPermissionAPI.hasPermission(MenuType.IMAGE_TEMPLATE_GIVE_UP, adminId);
            if (!hasPermission) {
                LOGGER.info("放弃VDI镜像模板,当前管理员{}没有编辑权限", adminId);
                return ShineMessageUtil.buildResponseMessageWithContent(request, PermissionConstants.NO_EDIT_PERMISSSION, "");
            }
        } catch (BusinessException e) {
            LOGGER.error("放弃VDI镜像模板,当前管理员查询权限异常", e);
            return ShineMessageUtil.buildResponseMessageWithContent(request, Constants.FAILURE, "");
        }
        // 查询传入的镜像是否允许被当前管理员操作
        try {
            helper.getImageEditingInfoIfPresent(requestDTO.getId(), adminId, request.getTerminalId());
        } catch (BusinessException e) {
            LOGGER.error("当前管理员不可操作目标镜像", e);
            return buildErrorResponseMessage(request);
        }

        UUID imageTemplateId = requestDTO.getId();
        ThreadExecutors.execute(this.getClass().getName() + ABORT_EDIT_IMAGE_THREAD_NAME,
            () -> abortEditImage(imageTemplateId, adminId, request.getTerminalId()));
        return buildResponseMessage(request, new Object());
    }

    @Override
    VDIEditImageIdRequestDTO resolveJsonString(String dataJsonString) {
        return JSONObject.parseObject(dataJsonString, VDIEditImageIdRequestDTO.class);
    }

    private void abortEditImage(UUID imageId, UUID adminId, String terminalId) {
        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        try {
            cbbImageTemplateMgmtAPI.abortEditImageTemplate(imageId);

            LOGGER.info("放弃镜像成功");
            helper.recordLog(BusinessKey.RCDC_RCO_VDI_EDIT_IMAGE_ABORT_SUCCESS, adminId, imageId, terminalAddr);
        } catch (BusinessException e) {
            LOGGER.error("放弃镜像失败", e);
            helper.recordLog(BusinessKey.RCDC_RCO_VDI_EDIT_IMAGE_ABORT_FAIL, e, adminId, imageId, terminalAddr);
        }
    }
}
