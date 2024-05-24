package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.deskoperate.CbbRestoreDeskRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskFaultInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CbbDeskFaultInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.AppCenterHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 * 
 * Description: 云桌面还原批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 * 
 * @author artom
 */
public class RevertDesktopBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevertDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private DeskFaultInfoAPI deskFaultInfoAPI;

    private Boolean enableStoreSystemDisk;

    private String desktopType = Constants.CLOUD_DESKTOP;

    @ApiModelProperty(value = "是否同时还原D盘，并删除数据")
    @Nullable
    private Boolean needRestoreDataTemplateDisk;

    private AppCenterHelper appCenterHelper;


    @Nullable
    public Boolean getNeedRestoreDataTemplateDisk() {
        return needRestoreDataTemplateDisk;
    }

    public void setNeedRestoreDataTemplateDisk(@Nullable Boolean needRestoreDataTemplateDisk) {
        this.needRestoreDataTemplateDisk = needRestoreDataTemplateDisk;
    }

    /**
     * 传入云桌面故障对象
     *
     * @param deskFaultInfoAPI 对象
     */
    public void setDesktopFaultInfoAPI(DeskFaultInfoAPI deskFaultInfoAPI) {
        Assert.notNull(deskFaultInfoAPI, "desktopFaultInfoAPI must not be null");
        this.deskFaultInfoAPI = deskFaultInfoAPI;
    }

    /**
     * 设置是否保留原镜像数据
     * 
     * @param enableStoreSystemDisk 保留原镜像数据与否
     * @return 任务
     */
    public RevertDesktopBatchTaskHandler setEnableStoreSystemDisk(Boolean enableStoreSystemDisk) {
        this.enableStoreSystemDisk = enableStoreSystemDisk;
        return this;
    }

    public RevertDesktopBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.appCenterHelper = SpringBeanHelper.getBean(AppCenterHelper.class);
    }

    @Override
    public RevertDesktopBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public RevertDesktopBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public RevertDesktopBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        Assert.notNull(cloudDesktopOperateAPI, "cloudDesktopOperateAPI must not be null");
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
        return this;
    }

    @Override
    public RevertDesktopBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        Assert.notNull(cbbIDVDeskOperateAPI, "Param [cbbIDVDeskOperateAPI] must not be null");
        this.cbbIDVDeskOperateAPI = cbbIDVDeskOperateAPI;
        return this;
    }

    @Override
    public RevertDesktopBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        // 不需要设置
        return null;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");
        UUID id = taskItem.getItemID();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(id);
        String tmpUserName = cloudDesktopDetailDTO.getUserName();
        String tmpDesktopName = cloudDesktopDetailDTO.getDesktopName();
        boolean isRestoreSystemDisk = osTypeSupportRestoreDisk(cloudDesktopDetailDTO.getDesktopImageType()) ? enableStoreSystemDisk : false;
        CbbCloudDeskType deskType = null;
        try {
            deskType = CbbCloudDeskType.valueOf(cloudDesktopDetailDTO.getDesktopCategory());
            //校验桌面是否在测试中
            appCenterHelper.checkTestingDesk(id);
            LOGGER.info("准备下发还原{}类型云桌面[id={}]命令", deskType.name(), id);
            switch (deskType) {
                case IDV:
                    cbbIDVDeskOperateAPI.restoreDeskIDV(new CbbRestoreDeskRequest(id, isRestoreSystemDisk, this.needRestoreDataTemplateDisk));
                    break;
                case VDI:
                    checkBeforeRestore(cloudDesktopDetailDTO);
                    cloudDesktopOperateAPI.restore(new CbbRestoreDeskRequest(id, isRestoreSystemDisk));
                    break;
                case THIRD:
                    throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_FAIL_THIRD_PARTY, cloudDesktopDetailDTO.getDesktopName());
                default:
                    throw new BusinessException("不支持的云桌面类型:{" + deskType + "}");
            }
            CbbDeskFaultInfoResponse response = deskFaultInfoAPI.findFaultInfoByDeskId(id);
            if (response != null && response.getCbbDeskFaultInfoDTO() != null) {
                LOGGER.info("还原桌面同时取消报障，桌面{}", id);
                deskFaultInfoAPI.relieveFaultForCloudDesk(id);
            }
            // IDV还原云桌面，记录是否还原D盘的审计日志
            if (deskType == CbbCloudDeskType.IDV) {
                if (needRestoreDataTemplateDisk != null && needRestoreDataTemplateDisk) {
                    auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_SUC_LOG_RESTORE_DATA, tmpUserName, tmpDesktopName);
                } else {
                    auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_SUC_LOG_RESTORE_ONLY_SYSTEM, tmpUserName, tmpDesktopName);
                }
            }

            if (isRestoreSystemDisk) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_SUC_LOG_STORE_SYSTEM_DISK, tmpUserName, tmpDesktopName, desktopType);
            } else {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_SUC_LOG, tmpUserName, tmpDesktopName, desktopType);
            }
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_ITEM_SUC_DESC).msgArgs(tmpUserName, tmpDesktopName, desktopType)
                    .build();
        } catch (BusinessException e) {
            LOGGER.error("发送云桌面还原命令失败，云桌面id=" + id.toString(), e);
            if (isRestoreSystemDisk) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_FAIL_LOG_STORE_SYSTEM_DISK, e, tmpUserName, tmpDesktopName,
                        e.getI18nMessage(), desktopType);
            } else {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_FAIL_LOG, e, tmpUserName, tmpDesktopName,
                        e.getI18nMessage(), desktopType);
            }
            // IDV还原云桌面，记录是否还原D盘的审计日志
            if (deskType == CbbCloudDeskType.IDV) {
                if (needRestoreDataTemplateDisk != null && needRestoreDataTemplateDisk) {
                    auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_FAIL_LOG_RESTORE_DATA, tmpUserName, tmpDesktopName,
                            e.getI18nMessage());
                } else {
                    auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_FAIL_LOG_RESTORE_ONLY_SYSTEM, tmpUserName, tmpDesktopName,
                            e.getI18nMessage());
                }
            }

            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_ITEM_FAIL_DESC, e, tmpUserName, tmpDesktopName,
                    e.getI18nMessage(), desktopType);
        } finally {
            userName = tmpUserName;
            desktopName = tmpDesktopName;
            processItemCount.incrementAndGet();
        }
    }

    /**
     * 当前仅支持win7和win10, win11还原保留磁盘
     * 
     * @param osType
     * @return
     */
    private boolean osTypeSupportRestoreDisk(CbbOsType osType) {
        return CbbOsType.isWin7UpOS(osType);
    }

    private void checkBeforeRestore(CloudDesktopDetailDTO desktopDetailDTO) throws BusinessException {
        if (Objects.equals(desktopDetailDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC.name())) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_DYNAMIC_POOL_NOT_SUPPORT, desktopDetailDTO.getDesktopName());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (processItemCount.get() == 1) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_SINGLE_SUC)
                        .msgArgs(new String[] {userName, desktopName, desktopType}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_SINGLE_FAIL)
                        .msgArgs(new String[] {userName, desktopName, desktopType}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            if (desktopType.equals(Constants.APP_CLOUD_DESKTOP)) {
                return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCO_APP_DESKTOP_REVERT_BATCH_RESULT);
            } else {
                return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_REVERT_BATCH_RESULT);
            }
        }
    }

}
