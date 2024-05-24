package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Iterator;
import java.util.Objects;

import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 搬迁UserGroupController中ApplyDesktopBatchTaskHandler类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/31
 *
 * @author linke
 */
public class ApplyDesktopBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplyDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private IacUserMgmtAPI cbbUserAPI;

    private CbbDeskSpecAPI cbbDeskSpecAPI;

    private UserGroupDesktopConfigDTO groupDesktopConfig;

    public ApplyDesktopBatchTaskHandler(UserGroupDesktopConfigDTO groupDesktopConfig, Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
        this.groupDesktopConfig = groupDesktopConfig;
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.userDesktopMgmtAPI = SpringBeanHelper.getBean(UserDesktopMgmtAPI.class);
        this.cbbUserAPI = SpringBeanHelper.getBean(IacUserMgmtAPI.class);
        this.cbbDeskSpecAPI = SpringBeanHelper.getBean(CbbDeskSpecAPI.class);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_APPLY_DESKTOP_BATCH_RESULT);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        // 查询用户绑定的vdi桌面个数
        IacUserDetailDTO userDetailResponse = cbbUserAPI.getUserDetail(taskItem.getItemID());
        Assert.notNull(userDetailResponse, "userDetailResponse can not be null");
        String userName = userDetailResponse.getUserName();
        long vdiDesktopNum = userDesktopMgmtAPI.getUserVdiDesktopNum(taskItem.getItemID());

        if (vdiDesktopNum > 0) {
            LOGGER.debug("用户[{}]已配置了vdi云桌面，不应用分组的桌面配置", taskItem.getItemID());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_APPLY_DESKTOP_IGNORE).msgArgs(new String[]{userName}).build();
        }
        try {
            CreateDesktopResponse createResponse = createDesktop(userDetailResponse, taskItem);
            String desktopName = createResponse.getDesktopName();
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_APPLY_DESKTOP_SUCCESS_LOG, new String[]{userName, desktopName});
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_APPLY_DESKTOP_RESULT_SUCCESS).msgArgs(new String[]{userName, desktopName}).build();
        } catch (BusinessException e) {
            LOGGER.error("用户[" + userName + "]创建桌面失败", e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_APPLY_DESKTOP_FAIL_LOG, e, new String[]{userName, e.getI18nMessage()});
            throw new BusinessException(UserBusinessKey.RCDC_RCO_APPLY_DESKTOP_RESULT_FAIL, e, userName, e.getI18nMessage());
        }
    }

    private CreateDesktopResponse createDesktop(IacUserDetailDTO userDetail, BatchTaskItem taskItem) throws BusinessException {
        CreateCloudDesktopRequest createDesktopRequest = new CreateCloudDesktopRequest();
        createDesktopRequest.setUserId(userDetail.getId());

        createDesktopRequest.setStrategyId(groupDesktopConfig.getStrategyId());
        createDesktopRequest.setNetworkId(groupDesktopConfig.getNetworkId());
        createDesktopRequest.setDesktopImageId(groupDesktopConfig.getImageTemplateId());
        createDesktopRequest.setSoftwareStrategyId(groupDesktopConfig.getSoftwareStrategyId());
        createDesktopRequest.setUserProfileStrategyId(groupDesktopConfig.getUserProfileStrategyId());
        if (groupDesktopConfig.getClusterId() != null) {
            createDesktopRequest.setClusterId(groupDesktopConfig.getClusterId());
        }
        if (groupDesktopConfig.getPlatformId() != null) {
            createDesktopRequest.setPlatformId(groupDesktopConfig.getPlatformId());
        }
        if (Objects.nonNull(groupDesktopConfig.getDeskSpecId())) {
            createDesktopRequest.setDeskSpec(cbbDeskSpecAPI.getById(groupDesktopConfig.getDeskSpecId()));
        }
        createDesktopRequest.setBatchTaskItem(taskItem);
        return userDesktopMgmtAPI.create(createDesktopRequest);
    }
}
