package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDeskSpecRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.EditAppPoolSpecRequest;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Description: 变更应用池规格批任务处理器
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月05日
 *
 * @author zhengjingyong
 */
public class RcaUpdateAppPoolSpecBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaUpdateAppPoolSpecBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private EditAppPoolSpecRequest editAppPoolSpecRequest;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    private DeskSpecAPI deskSpecAPI;

    private RcaAppPoolBaseDTO appPoolBaseDTO;

    public RcaUpdateAppPoolSpecBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator,
                                                BaseAuditLogAPI auditLogAPI, RcaAppPoolAPI rcaAppPoolAPI,
                                                EditAppPoolSpecRequest editAppPoolSpecRequest) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.rcaAppPoolAPI = rcaAppPoolAPI;
        this.editAppPoolSpecRequest = editAppPoolSpecRequest;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");

        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(taskItem.getItemID());
        String desktopName = cbbDeskDTO.getName();

        CbbUpdateDeskSpecRequest specRequest = new CbbUpdateDeskSpecRequest();
        specRequest.setDeskId(cbbDeskDTO.getDeskId());
        specRequest.setCpu(editAppPoolSpecRequest.getCpu());
        specRequest.setMemory(editAppPoolSpecRequest.getMemory());
        specRequest.setEnableHyperVisorImprove(cbbDeskDTO.getEnableHyperVisorImprove());
        VgpuInfoDTO vgpuInfoDTO = editAppPoolSpecRequest.getVgpuInfoDTO();
        if (vgpuInfoDTO == null) {
            vgpuInfoDTO = new VgpuInfoDTO();
        }
        // 显卡详细参数构建
        specRequest.setVgpuInfoDTO(deskSpecAPI.checkAndBuildVGpuInfo(cbbDeskDTO.getClusterId(),
                vgpuInfoDTO.getVgpuType(), vgpuInfoDTO.getVgpuExtraInfo()));

        specRequest.setPersonSize(checkNullGetValue(editAppPoolSpecRequest.getPersonSize(), cbbDeskDTO.getPersonSize()));
        if (cbbDeskDTO.getPersonSize() == 0 && editAppPoolSpecRequest.getPersonSize() != null &&
                editAppPoolSpecRequest.getPersonSize() > 0) {
            specRequest.setPersonDiskStoragePoolId(editAppPoolSpecRequest.getPersonDiskStoragePoolId());
        }
        // 应用池没有额外盘规格
        specRequest.setExtraDiskList(new ArrayList<>());
        specRequest.setSystemSize(checkNullGetValue(editAppPoolSpecRequest.getSystemSize(), cbbDeskDTO.getSystemSize()));
        // 应用池无需修改为个性配置
        specRequest.setEnableCustom(Boolean.FALSE);
        specRequest.setEnableHyperVisorImprove(editAppPoolSpecRequest.getEnableHyperVisorImprove());

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
        try {
            rcaAppPoolAPI.updateAppPoolState(editAppPoolSpecRequest.getAppPoolId(), RcaEnum.PoolState.AVAILABLE);
        } catch (Exception e) {
            LOGGER.error(String.format("更新应用池[%s]信息失败", editAppPoolSpecRequest.getAppPoolId()), e);
        }
        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_SUCCESS, appPoolBaseDTO.getName());

        return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_RESULT);
    }

    public void setCbbVDIDeskMgmtAPI(CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI) {
        this.cbbVDIDeskMgmtAPI = cbbVDIDeskMgmtAPI;
    }

    public void setCbbVDIDeskDiskAPI(CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI) {
        this.cbbVDIDeskDiskAPI = cbbVDIDeskDiskAPI;
    }

    public void setCbbVDIDeskStrategyMgmtAPI(CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI) {
        this.cbbVDIDeskStrategyMgmtAPI = cbbVDIDeskStrategyMgmtAPI;
    }

    public void setAppPoolBaseDTO(RcaAppPoolBaseDTO appPoolBaseDTO) {
        this.appPoolBaseDTO = appPoolBaseDTO;
    }

    private <T> T checkNullGetValue(T nullableObj, T defaultObj) {
        return Optional.ofNullable(nullableObj).orElse(defaultObj);
    }

    public void setDeskSpecAPI(DeskSpecAPI deskSpecAPI) {
        this.deskSpecAPI = deskSpecAPI;
    }

    private boolean checkIsNotChange(CbbUpdateDeskSpecRequest specRequest, CbbDeskDTO cbbDeskDTO) throws BusinessException {
        return Objects.equals(specRequest.getCpu(), cbbDeskDTO.getCpu())
                && Objects.equals(specRequest.getMemory(), cbbDeskDTO.getMemory())
                && Objects.equals(specRequest.getPersonSize(), cbbDeskDTO.getPersonSize())
                && Objects.equals(specRequest.getSystemSize(), cbbDeskDTO.getSystemSize())
                && deskSpecAPI.isVgpuInfoEquals(specRequest.getVgpuInfoDTO(), cbbDeskDTO.getVgpuInfoDTO())
                && Objects.equals(specRequest.getEnableHyperVisorImprove(), cbbDeskDTO.getEnableHyperVisorImprove());
    }

}
