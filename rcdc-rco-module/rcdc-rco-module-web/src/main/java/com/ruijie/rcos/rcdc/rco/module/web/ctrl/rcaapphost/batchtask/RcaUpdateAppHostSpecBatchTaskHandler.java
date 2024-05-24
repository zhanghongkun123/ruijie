package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.batchtask;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDeskSpecRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.request.DeskSpecRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.ImageDeskSpecGpuCheckParamDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask.RcaUpdateAppPoolSpecBatchTaskHandler;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Description: 变更应用主机规格批任务处理器
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月25日
 *
 * @author zhengjingyong
 */
public class RcaUpdateAppHostSpecBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaUpdateAppPoolSpecBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private RcaHostAPI rcaHostAPI;

    private DeskSpecRequest deskSpecRequest;

    private DeskSpecAPI deskSpecAPI;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private RcaHostSessionAPI rcaHostSessionAPI;

    public RcaUpdateAppHostSpecBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator,
                                                BaseAuditLogAPI auditLogAPI, RcaAppPoolAPI rcaAppPoolAPI,
                                                RcaHostAPI rcaHostAPI, DeskSpecRequest deskSpecRequest) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.rcaAppPoolAPI = rcaAppPoolAPI;
        this.rcaHostAPI = rcaHostAPI;
        this.deskSpecRequest = deskSpecRequest;
    }

    public void setDeskSpecAPI(DeskSpecAPI deskSpecAPI) {
        this.deskSpecAPI = deskSpecAPI;
    }

    public void setCbbVDIDeskMgmtAPI(CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI) {
        this.cbbVDIDeskMgmtAPI = cbbVDIDeskMgmtAPI;
    }

    public void setCbbImageTemplateMgmtAPI(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI) {
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
    }

    public void setRcaHostSessionAPI(RcaHostSessionAPI rcaHostSessionAPI) {
        this.rcaHostSessionAPI = rcaHostSessionAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");

        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(taskItem.getItemID());
        String desktopName = cbbDeskDTO.getName();

        CbbDeskSpecDTO cbbDeskSpecDTO = deskSpecAPI.buildCbbDeskSpec(cbbDeskDTO.getClusterId(), deskSpecRequest);
        VgpuInfoDTO vgpuInfoDTO = cbbDeskSpecDTO.getVgpuInfoDTO();
        if (vgpuInfoDTO == null) {
            vgpuInfoDTO = new VgpuInfoDTO();
            cbbDeskSpecDTO.setVgpuInfoDTO(vgpuInfoDTO);
        }

        RcaHostDTO hostDTO = rcaHostAPI.getById(taskItem.getItemID());
        if (hostDTO.getPoolId() == null) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_FAIL_BY_NOT_POOL);
        }
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(hostDTO.getPoolId());
        if (RcaEnum.PoolType.DYNAMIC == appPoolBaseDTO.getPoolType()
                || RcaEnum.HostSessionType.MULTIPLE == appPoolBaseDTO.getSessionType()
                || RcaEnum.HostSourceType.THIRD_PARTY == appPoolBaseDTO.getHostSourceType()) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_FAIL_BY_NOT_POOL);
        } else {
            UUID hostSingleBindUser = rcaHostSessionAPI.getHostSingleBindUser(taskItem.getItemID(), RcaEnum.HostSessionBindMode.STATIC);
            if (hostSingleBindUser == null) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_FAIL_BY_NOT_POOL);
            }
        }

        // 检查vGpu
        checkDeskVgpu(cbbDeskDTO, cbbDeskSpecDTO);

        CbbUpdateDeskSpecRequest specRequest = new CbbUpdateDeskSpecRequest();
        specRequest.setDeskId(cbbDeskDTO.getDeskId());
        specRequest.setCpu(cbbDeskSpecDTO.getCpu());
        specRequest.setMemory(cbbDeskSpecDTO.getMemory());
        specRequest.setEnableHyperVisorImprove(cbbDeskSpecDTO.getEnableHyperVisorImprove());
        // 显卡详细参数构建
        specRequest.setVgpuInfoDTO(deskSpecAPI.checkAndBuildVGpuInfo(cbbDeskDTO.getClusterId(),
                vgpuInfoDTO.getVgpuType(), vgpuInfoDTO.getVgpuExtraInfo()));

        specRequest.setPersonSize(checkNullGetValue(cbbDeskSpecDTO.getPersonSize(), cbbDeskDTO.getPersonSize()));
        if (cbbDeskDTO.getPersonSize() == 0 && cbbDeskSpecDTO.getPersonSize() != null &&
                cbbDeskSpecDTO.getPersonSize() > 0) {
            specRequest.setPersonDiskStoragePoolId(cbbDeskSpecDTO.getPersonDiskStoragePoolId());
        }
        // 应用主机没有额外盘规格
        specRequest.setExtraDiskList(new ArrayList<>());
        specRequest.setSystemSize(checkNullGetValue(cbbDeskSpecDTO.getSystemSize(), cbbDeskDTO.getSystemSize()));
        // 单独变更的主机，需要修改为自定义规格，不受应用池管控
        specRequest.setEnableCustom(Boolean.TRUE);
        if (cbbDeskSpecDTO.getEnableHyperVisorImprove() != null) {
            specRequest.setEnableHyperVisorImprove(cbbDeskSpecDTO.getEnableHyperVisorImprove());
        }

        if (checkIsNotChange(specRequest, cbbDeskDTO)) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_NO_CHANGE_LOG, desktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_NO_CHANGE_LOG)
                    .msgArgs(new String[] {desktopName}).build();
        }

        try {
            LOGGER.info("编辑云主机[{}]的规格:{}", specRequest.getDeskId(), JSON.toJSONString(specRequest));
            cbbVDIDeskMgmtAPI.updateDeskSpec(specRequest);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_ITEM_SUCCESS, appPoolBaseDTO.getName(), desktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_ITEM_SUCCESS)
                    .msgArgs(new String[] {appPoolBaseDTO.getName(), desktopName}).build();
        } catch (BusinessException e) {
            LOGGER.error("编辑云主机[{}]的规格失败", taskItem.getItemID().toString(), e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_ITEM_FAIL,
                    appPoolBaseDTO.getName(), desktopName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_ITEM_FAIL)
                    .msgArgs(new String[] {appPoolBaseDTO.getName(), desktopName, e.getI18nMessage()})
                    .build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_RESULT);
    }

    private <T> T checkNullGetValue(T nullableObj, T defaultObj) {
        return Optional.ofNullable(nullableObj).orElse(defaultObj);
    }


    private boolean checkIsNotChange(CbbUpdateDeskSpecRequest specRequest, CbbDeskDTO cbbDeskDTO) throws BusinessException {
        return Objects.equals(specRequest.getCpu(), cbbDeskDTO.getCpu())
                && Objects.equals(specRequest.getMemory(), cbbDeskDTO.getMemory())
                && Objects.equals(specRequest.getPersonSize(), cbbDeskDTO.getPersonSize())
                && Objects.equals(specRequest.getSystemSize(), cbbDeskDTO.getSystemSize())
                && deskSpecAPI.isVgpuInfoEquals(specRequest.getVgpuInfoDTO(), cbbDeskDTO.getVgpuInfoDTO())
                && Objects.equals(specRequest.getEnableHyperVisorImprove(), cbbDeskDTO.getEnableHyperVisorImprove());
    }

    private void checkDeskVgpu(CbbDeskDTO cbbDeskDTO, CbbDeskSpecDTO cbbDeskSpecDTO) throws BusinessException {
        // 校验显卡支持情况
        ImageDeskSpecGpuCheckParamDTO checkParamDTO = new ImageDeskSpecGpuCheckParamDTO();
        checkParamDTO.setImageId(cbbDeskDTO.getImageTemplateId());
        checkParamDTO.setClusterId(cbbDeskDTO.getClusterId());
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(cbbDeskDTO.getImageTemplateId());
        checkParamDTO.setImageEditionId(imageTemplateDetail.getLastRecoveryPointId());

        VgpuInfoDTO vgpuInfoDTO = cbbDeskSpecDTO.getVgpuInfoDTO();
        if (vgpuInfoDTO != null) {
            checkParamDTO.setVgpuInfoDTO(vgpuInfoDTO);
            deskSpecAPI.checkGpuSupportByImageAndSpec(checkParamDTO);
        }
    }

}
