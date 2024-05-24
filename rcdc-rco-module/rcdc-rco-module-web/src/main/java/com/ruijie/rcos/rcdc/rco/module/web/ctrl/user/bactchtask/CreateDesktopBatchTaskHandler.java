package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.desktop.CreateDesktopWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 
 * Description: 创建云桌面批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 * 
 * @author artom
 */
public class CreateDesktopBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CreateCloudDesktopRequest request;

    public CreateDesktopBatchTaskHandler(List<? extends BatchTaskItem> taskList, CreateCloudDesktopRequest request,
                                         BaseAuditLogAPI auditLogAPI) {
        super(taskList);
        this.request = request;
        this.auditLogAPI = auditLogAPI;
    }

    /**
     * 设置用户名
     * 
     * @param userName 用户名
     * @return this
     */
    public CreateDesktopBatchTaskHandler setUserName(String userName) {
        Assert.hasText(userName, "userName can not empty");
        this.userName = userName;
        return this;
    }

    @Override
    public CreateDesktopBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public CreateDesktopBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public CreateDesktopBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public CreateDesktopBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public CreateDesktopBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");
        try {
            request.setBatchTaskItem(taskItem);
            CreateDesktopWebResponse createResponse = cloudDesktopWebService.createDesktop(request, userName);
            desktopName = createResponse.getDesktopName();
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_ITEM_SUC_DESC).msgArgs(new String[] {userName, createResponse.getDesktopName()})
                    .build();
        } catch (BusinessException e) {
            LOGGER.error("创建云桌面失败，用户id=" + taskItem.getItemID().toString(), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_ITEM_FAIL_DESC, e, userName, e.getI18nMessage());
        } finally {
            processItemCount.incrementAndGet();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (processItemCount.get() == 1) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_SINGLE_SUC)
                        .msgArgs(new String[] {userName, desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_SINGLE_FAIL)
                        .msgArgs(new String[] {userName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_BATCH_RESULT);
        }
    }

    @Override
    public boolean supportProgressAware() {
        return true;
    }
}
