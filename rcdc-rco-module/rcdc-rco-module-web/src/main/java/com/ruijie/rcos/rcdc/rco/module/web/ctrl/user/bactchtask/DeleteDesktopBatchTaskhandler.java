package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;


import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MoveDesktopToRecycleBinRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * Description: 删除云桌面批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 *
 * @author artom
 */
public class DeleteDesktopBatchTaskhandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDesktopBatchTaskhandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserProfileMgmtAPI userProfileMgmtAPI;

    private Boolean shouldOnlyDeleteDataFromDb;

    private String prefix;

    public DeleteDesktopBatchTaskhandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI, Boolean shouldOnlyDeleteDataFromDb) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
        this.prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);
    }

    public void setUserProfileMgmtAPI(UserProfileMgmtAPI userProfileMgmtAPI) {
        this.userProfileMgmtAPI = userProfileMgmtAPI;
    }

    @Override
    public DeleteDesktopBatchTaskhandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public DeleteDesktopBatchTaskhandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public DeleteDesktopBatchTaskhandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public DeleteDesktopBatchTaskhandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public DeleteDesktopBatchTaskhandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");
        UUID id = taskItem.getItemID();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(id);
        String tmpUserName = cloudDesktopDetailDTO.getUserName();
        String tmpDesktopName = cloudDesktopDetailDTO.getDesktopName();
        try {
            if (Objects.equals(cloudDesktopDetailDTO.getDeskType(), CbbCloudDeskType.THIRD.name())) {
                cloudDesktopMgmtAPI.deleteDesktopThirdParty(cloudDesktopDetailDTO.getId());
            } else if (Boolean.TRUE.equals(shouldOnlyDeleteDataFromDb)) {
                cloudDesktopMgmtAPI.deleteDeskFromDb(id);
            } else if (Objects.equals(cloudDesktopDetailDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC.name())) {
                // 动态池彻底删除
                cloudDesktopMgmtAPI.forceDeleteDesktop(cloudDesktopDetailDTO.getId());
            } else {
                MoveDesktopToRecycleBinRequest request = new MoveDesktopToRecycleBinRequest();
                request.setDesktopId(id);
                cloudDesktopMgmtAPI.moveDesktopToRecycleBin(request);
            }
            userProfileMgmtAPI.deleteFailCleanRequestById(id);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_SUC_LOG, tmpUserName, tmpDesktopName, prefix);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_ITEM_SUC_DESC).msgArgs(new String[] {tmpUserName, tmpDesktopName, prefix}).build();
        } catch (BusinessException e) {
            LOGGER.error("删除云桌面失败，云桌面id=" + id.toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_FAIL_LOG, e, tmpUserName, tmpDesktopName, e.getI18nMessage(), prefix);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_ITEM_FAIL_DESC, e, tmpUserName, tmpDesktopName, e.getI18nMessage(), prefix);
        } finally {
            userName = tmpUserName;
            desktopName = tmpDesktopName;
            processItemCount.incrementAndGet();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (processItemCount.get() == 1) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_SINGLE_SUC)
                        .msgArgs(new String[] {userName, desktopName, prefix}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_SINGLE_FAIL)
                        .msgArgs(new String[] {userName, desktopName, prefix}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return WebBatchTaskUtils.buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_BATCH_RESULT, prefix);
        }
    }

}
