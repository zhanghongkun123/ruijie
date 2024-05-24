package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbImageCdromDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateEditDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateStartDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ExternalImageVmState;
import com.ruijie.rcos.rcdc.codec.adapter.def.annotation.MaintainFilterAction;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.MenuType;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.StartEditImageVmRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 启动镜像模板
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 02:21
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.VDI_EDIT_IMAGE_START_VM_ACTION)
@MaintainFilterAction
public class StartEditImageVmHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<StartEditImageVmRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartEditImageVmHandlerSPIImpl.class);

    private static final String CREATE_AND_START_VM_THREAD_NAME = "create_and_start_vm";

    private static final String START_VM_THREAD_NAME = "start_vm";

    private static final Integer IN_MAINTENANCE = -2;

    private static final Integer DESK_RUNNING = -3;

    private static final Integer CANNOT_OPERATE = -4;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private VDIEditImageHelper helper;

    @Autowired
    private AdminLoginOnTerminalCacheManager adminCacheManager;

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Autowired
    private DesktopService desktopService;

    @Autowired
    private CmsUpgradeAPI cmsUpgradeAPI;

    @Autowired
    private AdminPermissionAPI adminPermissionAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    CbbResponseShineMessage getResponseMessage(CbbDispatcherRequest request, StartEditImageVmRequestDTO requestDTO, UUID adminId) {

        try {
            // 查询是否有启动VDI镜像模板权限
            boolean hasPermission = adminPermissionAPI.hasPermission(MenuType.IMAGE_TEMPLATE_START, adminId);
            if (!hasPermission) {
                LOGGER.info("启动VDI镜像模板,当前管理员{}没有启动镜像权限", adminId);
                // -5没有启动权限
                return ShineMessageUtil.buildResponseMessageWithContent(request, PermissionConstants.NO_START_PERMISSSION, "");
            }
        } catch (BusinessException e) {
            LOGGER.error("启动VDI镜像模板,当前管理员查询权限异常", e);
            // 没有启动权限
            return ShineMessageUtil.buildResponseMessageWithContent(request, Constants.FAILURE, "");
        }

        if (isInMaintenanceMode()) {
            return ShineMessageUtil.buildErrorResponseMessage(request, IN_MAINTENANCE);
        }
        CbbGetImageTemplateInfoDTO imageInfo;
        try {
            imageInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(requestDTO.getId());
        } catch (BusinessException e) {
            LOGGER.error("查询镜像模板信息异常", e);
            return buildErrorResponseMessage(request);
        }
        Assert.notNull(imageInfo, "imageInfo is null");

        // 查询传入的镜像是否允许被当前管理员操作
        try {
            helper.getImageEditingInfoIfPresent(requestDTO.getId(), adminId, request.getTerminalId());
        } catch (BusinessException e) {
            LOGGER.error("当前管理员不可操作目标镜像", e);
            return ShineMessageUtil.buildErrorResponseMessage(request, CANNOT_OPERATE);
        }

        Assert.notNull(requestDTO.getId(), "image id cannot be null!");
        Assert.notNull(requestDTO.getVmState(), "vmState cannot be null!");
        UUID imageTemplateId = requestDTO.getId();
        CbbOsType osType = imageInfo.getCbbOsType();
        try {
            inquireImageTemplateEditing(requestDTO, adminId, request.getTerminalId());

            if (requestDTO.getVmState() == ExternalImageVmState.EXISTS) {
                LOGGER.info("镜像虚机已存在，直接启动");
                ThreadExecutors.execute(this.getClass().getName() + START_VM_THREAD_NAME,
                    () -> startVm(imageTemplateId, adminId, request.getTerminalId(), osType));
                return buildResponseMessage(request, new Object());
            }

            LOGGER.info("镜像虚机不存在，创建并启动");
            ThreadExecutors.execute(this.getClass().getName() + CREATE_AND_START_VM_THREAD_NAME,
                () -> createAndStartVm(imageTemplateId, adminId, request.getTerminalId(), osType));
            return buildResponseMessage(request, new Object());
        } catch (Exception e) {
            LOGGER.error("启动镜像模板失败", e);
            return buildErrorResponseMessage(request);
        }
    }

    private void inquireImageTemplateEditing(StartEditImageVmRequestDTO requestDTO, UUID adminId, String terminalId) throws BusinessException {
        CbbImageTemplateEditDTO editRequest = new CbbImageTemplateEditDTO();
        editRequest.setImageTemplateId(requestDTO.getId());
        editRequest.setAdminId(adminId);
        editRequest.setTerminalId(terminalId);
        AdminLoginOnTerminalCache adminCache = adminCacheManager.getIfPresent(requestDTO.getAdminSessionId());
        Assert.notNull(adminCache, "登录信息为空！");
        editRequest.setAdminName(adminCache.getAdminName());
        cbbImageTemplateMgmtAPI.editImageTemplate(editRequest);
    }

    @Override
    StartEditImageVmRequestDTO resolveJsonString(String dataJsonString) {
        return JSONObject.parseObject(dataJsonString, StartEditImageVmRequestDTO.class);
    }

    private void createAndStartVm(UUID imageTemplateId, UUID adminId, String terminalId, CbbOsType osType) {
        try {
            List<CbbImageCdromDTO> cdromList = cmsUpgradeAPI.getCmIso(imageTemplateId, osType);
            CbbImageTemplateStartDTO imageTemplateStartDTO = new CbbImageTemplateStartDTO();
            imageTemplateStartDTO.setImageId(imageTemplateId);
            imageTemplateStartDTO.setCdromList(cdromList);
            imageTemplateStartDTO.setNeedExactMatchVgpu(true);
            cbbImageTemplateMgmtAPI.createAndRunVmForEditImageTemplate(imageTemplateStartDTO);
            successLog(imageTemplateId, adminId, terminalId);
        } catch (BusinessException e) {
            LOGGER.error("创建并启动虚机失败", e);
            failLog(e, imageTemplateId, adminId, terminalId);
        }
    }

    private void startVm(UUID imageTemplateId, UUID adminId, String terminalId, CbbOsType osType) {
        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        try {
            List<CbbImageCdromDTO> cdromList = cmsUpgradeAPI.getCmIso(imageTemplateId, osType);
            CbbImageTemplateStartDTO imageTemplateStartDTO = new CbbImageTemplateStartDTO();
            imageTemplateStartDTO.setImageId(imageTemplateId);
            imageTemplateStartDTO.setCdromList(cdromList);
            imageTemplateStartDTO.setNeedExactMatchVgpu(true);
            cbbImageTemplateMgmtAPI.startVm(imageTemplateStartDTO);
            successLog(imageTemplateId, adminId, terminalAddr);
        } catch (BusinessException e) {
            LOGGER.error("启动虚机失败", e);
            failLog(e, imageTemplateId, adminId, terminalAddr);
        }
    }

    private void successLog(UUID imageTemplateId, UUID adminId, String terminalMacAddr) {
        LOGGER.info("启动虚机成功");
        helper.recordLog(BusinessKey.RCDC_RCO_VDI_EDIT_IMAGE_START_VM_SUCCESS, adminId, imageTemplateId, terminalMacAddr);
    }

    private void failLog(BusinessException ex, UUID imageTemplateId, UUID adminId, String terminalMacAddr) {
        LOGGER.info("启动虚机失败");
        helper.recordLog(BusinessKey.RCDC_RCO_VDI_EDIT_IMAGE_START_VM_FAIL, ex, adminId, imageTemplateId, terminalMacAddr);
    }

    private boolean isInMaintenanceMode() {
        SystemMaintenanceState systemMaintenanceState = maintenanceModeMgmtAPI.getMaintenanceMode();
        if (systemMaintenanceState != SystemMaintenanceState.NORMAL) {
            LOGGER.info("当前系统维护模式状态[{}], VDI终端镜像编辑业务暂停！", systemMaintenanceState);
            return true;
        }
        return false;
    }
}
