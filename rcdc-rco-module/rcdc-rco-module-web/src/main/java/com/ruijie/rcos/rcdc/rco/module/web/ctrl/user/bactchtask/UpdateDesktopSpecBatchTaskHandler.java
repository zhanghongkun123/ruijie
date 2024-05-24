package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDeskSpecRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.StrategyHardwareDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 *
 * Description: 修改云桌面规格批量任务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author linke
 */
public class UpdateDesktopSpecBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateDesktopSpecBatchTaskHandler.class);

    private static final Set<CbbCloudDeskState> CAN_UPDATE_STATE_SET = Sets.newHashSet(CbbCloudDeskState.CLOSE, CbbCloudDeskState.SLEEP,
            CbbCloudDeskState.RUNNING);

    private BaseAuditLogAPI auditLogAPI;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    private DeskSpecAPI deskSpecAPI;

    private CbbDeskSpecAPI cbbDeskSpecAPI;

    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private UUID desktopPoolId;

    private CloudDesktopWebService cloudDesktopWebService;

    private boolean isBatch = true;

    /**
     * 当批处理就一个任务时，日志记录使用
     */
    private String oneItemDesktopName;

    public UpdateDesktopSpecBatchTaskHandler(List<? extends BatchTaskItem> batchTaskItemList) {
        super(batchTaskItemList);
        Assert.notNull(batchTaskItemList, "batchTaskItemList is not null");
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.cbbVDIDeskMgmtAPI = SpringBeanHelper.getBean(CbbVDIDeskMgmtAPI.class);
        this.cbbVDIDeskStrategyMgmtAPI = SpringBeanHelper.getBean(CbbVDIDeskStrategyMgmtAPI.class);
        this.cbbVDIDeskDiskAPI = SpringBeanHelper.getBean(CbbVDIDeskDiskAPI.class);
        this.deskSpecAPI = SpringBeanHelper.getBean(DeskSpecAPI.class);
        this.cbbDeskSpecAPI = SpringBeanHelper.getBean(CbbDeskSpecAPI.class);
        this.cbbDesktopPoolMgmtAPI = SpringBeanHelper.getBean(CbbDesktopPoolMgmtAPI.class);
        this.cloudDesktopWebService = SpringBeanHelper.getBean(CloudDesktopWebService.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem is not null");
        UpdateDesktopSpecBatchTaskItem taskItem = (UpdateDesktopSpecBatchTaskItem) batchTaskItem;
        Assert.notNull(taskItem.getItemID(), "itemId is not null");

        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(taskItem.getItemID());
        String desktopName = cbbDeskDTO.getName();

        try {
            cloudDesktopWebService.checkThirdPartyDesktop(taskItem.getItemID(), UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_NO_CHANGE_THIRD_PARTY);
            oneItemDesktopName = desktopName;
            // 池变更规格引起的，池桌面已开启独立配置无需变更
            if (checkPoolDeskEnableCustom(cbbDeskDTO)) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_DESK_ENABLE_CUSTOM, desktopName);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_DESK_ENABLE_CUSTOM).msgArgs(new String[]{desktopName}).build();
            }
            // 池变更规格引起的，静态池桌面，非独立配置且非关机状态，跳过关机后会自动变更
            if (isNotCloseStaticPoolDesk(cbbDeskDTO)) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_DESK_STATIC_POOL_NOT_CLOSE, desktopName);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_DESK_STATIC_POOL_NOT_CLOSE).msgArgs(new String[]{desktopName}).build();
            }

            CbbUpdateDeskSpecRequest updateDeskSpecRequest = buildUpdateReq(taskItem, cbbDeskDTO);
            // 无需考虑待变更的，只比对现有真实的规格配置
            if (checkIsNotChange(updateDeskSpecRequest, cbbDeskDTO, taskItem)) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_NO_CHANGE_LOG, desktopName);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_NO_CHANGE_LOG).msgArgs(new String[]{desktopName}).build();
            }

            validateBeforeUpdate(cbbDeskDTO);
            LOGGER.info("编辑云桌面[{}]的规格:{}", updateDeskSpecRequest.getDeskId(), JSON.toJSONString(updateDeskSpecRequest));
            if (deskSpecAPI.updateDeskSpec(updateDeskSpecRequest)) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_SUC_LOG, desktopName);
            } else {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_NOT_CLOSE_SUC_LOG, desktopName);
            }

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_ITEM_SUC_DESC).msgArgs(new String[]{desktopName}).build();
        } catch (BusinessException e) {
            LOGGER.error("编辑云桌面的规格失败，云桌面id=" + taskItem.getItemID().toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_FAIL_LOG, desktopName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_ITEM_FAIL_DESC, e, desktopName, e.getI18nMessage());
        } finally {
            oneItemDesktopName = desktopName;
        }
    }

    private CbbUpdateDeskSpecRequest buildUpdateReq(UpdateDesktopSpecBatchTaskItem taskItem, CbbDeskDTO cbbDeskDTO) throws BusinessException {
        CbbUpdateDeskSpecRequest cbbUpdateReq = new CbbUpdateDeskSpecRequest();
        cbbUpdateReq.setDeskId(taskItem.getItemID());
        cbbUpdateReq.setCpu(checkNullGetValue(taskItem.getCpu(), cbbDeskDTO.getCpu()));
        cbbUpdateReq.setMemory(checkNullGetValue(taskItem.getMemory(), cbbDeskDTO.getMemory()));
        cbbUpdateReq.setEnableHyperVisorImprove(checkNullGetValue(taskItem.getEnableHyperVisorImprove(), cbbDeskDTO.getEnableHyperVisorImprove()));

        if (BooleanUtils.isTrue(taskItem.getEnableChangeVgpu())) {
            // 显卡详细参数构建
            cbbUpdateReq.setVgpuInfoDTO(deskSpecAPI.checkAndBuildVGpuInfo(cbbDeskDTO.getClusterId(), taskItem.getVgpuType(),
                    taskItem.getVgpuExtraInfo()));
        } else {
            cbbUpdateReq.setVgpuInfoDTO(cbbDeskDTO.getVgpuInfoDTO());
        }

        cbbUpdateReq.setPersonSize(checkNullGetValue(taskItem.getPersonSize(), cbbDeskDTO.getPersonSize()));
        if (cbbDeskDTO.getPersonSize() == 0 && cbbUpdateReq.getPersonSize() > 0) {
            cbbUpdateReq.setPersonDiskStoragePoolId(taskItem.getPersonDiskStoragePoolId());
        }

        if (BooleanUtils.isTrue(taskItem.getEnableChangeExtraDisk())) {
            cbbUpdateReq.setExtraDiskList(checkNullGetValue(taskItem.getExtraDiskList(), new ArrayList<>()));
        } else {
            // 取桌面现有的额外盘赋值
            List<CbbAddExtraDiskDTO> extraDiskList = cbbVDIDeskDiskAPI.listDeskExtraDisk(cbbUpdateReq.getDeskId());
            cbbUpdateReq.setExtraDiskList(checkNullGetValue(extraDiskList, new ArrayList<>()));
        }

        // 应用分层类型处理系统盘大小
        CbbDeskStrategyVDIDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(cbbDeskDTO.getStrategyId());
        if (deskStrategy.getPattern() == CbbCloudDeskPattern.APP_LAYER) {
            if (Objects.isNull(taskItem.getSystemSize())) {
                cbbUpdateReq.setSystemSize(cbbDeskDTO.getSystemSize());
            } else {
                cbbUpdateReq.setSystemSize(taskItem.getSystemSize() - Constants.SYSTEM_DISK_CAPACITY_INCREASE_SIZE);
            }
        } else {
            cbbUpdateReq.setSystemSize(checkNullGetValue(taskItem.getSystemSize(), cbbDeskDTO.getSystemSize()));
        }

        // 修改桌面规格独立配置标识，桌面池管控的无需修改为独立配置，
        cbbUpdateReq.setEnableCustom(Objects.isNull(desktopPoolId));
        return cbbUpdateReq;
    }

    private void validateBeforeUpdate(CbbDeskDTO cbbDeskDTO) throws BusinessException {
        // 检查桌面池类型：非桌面池触发的批量变更，动态池桌面不支持单独变更规格
        if (Objects.equals(cbbDeskDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC) && Objects.isNull(desktopPoolId)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_DYNAMIC_POOL_NOT_SUPPORT, cbbDeskDTO.getName());
        }
        if (!CAN_UPDATE_STATE_SET.contains(cbbDeskDTO.getDeskState())) {
            String deskStateI18n = LocaleI18nResolver.resolve(UserBusinessKey.I18N_DESK_STATE_PREFIX +
                    cbbDeskDTO.getDeskState().name().toLowerCase());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_DESK_STATE_NOT_SUPPORT, cbbDeskDTO.getName(), deskStateI18n);
        }
    }

    private boolean checkPoolDeskEnableCustom(CbbDeskDTO cbbDeskDTO) {
        // 池变更规格引起的，静态池桌面独立配置跳过
        return Objects.nonNull(desktopPoolId) && BooleanUtils.isTrue(cbbDeskDTO.getEnableCustom());
    }

    private boolean isNotCloseStaticPoolDesk(CbbDeskDTO cbbDeskDTO) {
        // 池变更规格引起的，静态池桌面，非独立配置且非关机状态，跳过关机后会自动变更
        return Objects.nonNull(desktopPoolId) && cbbDeskDTO.getDesktopPoolType() == DesktopPoolType.STATIC
                && Boolean.FALSE.equals(cbbDeskDTO.getEnableCustom()) && cbbDeskDTO.getDeskState() != CbbCloudDeskState.CLOSE;
    }

    private boolean checkIsNotChange(CbbUpdateDeskSpecRequest cbbUpdateReq, CbbDeskDTO cbbDeskDTO, UpdateDesktopSpecBatchTaskItem taskItem)
            throws BusinessException {
        if (BooleanUtils.isTrue(taskItem.getEnableChangeExtraDisk())) {
            if (!deskSpecAPI.isExtraDiskEquals(cbbVDIDeskDiskAPI.listDeskExtraDisk(cbbUpdateReq.getDeskId()), cbbUpdateReq.getExtraDiskList())) {
                return false;
            }
        }
        // 使用桌面真实的配置
        CbbDeskSpecDTO cbbDeskSpecDTO = cbbDeskSpecAPI.getById(cbbDeskDTO.getDeskSpecId());
        cbbDeskSpecDTO.setCpu(cbbDeskDTO.getCpu());
        cbbDeskSpecDTO.setMemory(cbbDeskDTO.getMemory());
        cbbDeskSpecDTO.setSystemSize(cbbDeskDTO.getSystemSize());
        cbbDeskSpecDTO.setPersonSize(cbbDeskDTO.getPersonSize());
        cbbDeskSpecDTO.setEnableHyperVisorImprove(cbbDeskDTO.getEnableHyperVisorImprove());
        cbbDeskSpecDTO.setVgpuInfoDTO(cbbDeskDTO.getVgpuInfoDTO());

        StrategyHardwareDTO strategyHardwareDTO = new StrategyHardwareDTO();
        BeanUtils.copyProperties(cbbUpdateReq, strategyHardwareDTO);
        return deskSpecAPI.specHardwareEquals(strategyHardwareDTO, cbbDeskSpecDTO);
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {

        updateDesktopPoolState();
        if (isBatch) {
            return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_BATCH_RESULT);
        }
        // 更新单条用户
        if (sucCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_ITEM_SUC_DESC)
                    .msgArgs(new String[] {oneItemDesktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_UPDATE_SPEC_SINGLE_RESULT_FAIL)
                    .msgArgs(new String[] {oneItemDesktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    /**
     * 获取值
     *
     * @param nullableObj   首选值
     * @param defaultObj    首选值为null时的默认值
     * @return Object
     */
    private <T> T checkNullGetValue(T nullableObj, T defaultObj) {
        return Optional.ofNullable(nullableObj).orElse(defaultObj);
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    private void updateDesktopPoolState() {
        if (Objects.isNull(desktopPoolId)) {
            return;
        }

        try {
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolId, CbbDesktopPoolState.AVAILABLE);
        } catch (Exception e) {
            LOGGER.error(String.format("更新桌面池[%s]信息失败", desktopPoolId), e);
        }
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public void setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        this.cloudDesktopWebService = cloudDesktopWebService;
    }
}
