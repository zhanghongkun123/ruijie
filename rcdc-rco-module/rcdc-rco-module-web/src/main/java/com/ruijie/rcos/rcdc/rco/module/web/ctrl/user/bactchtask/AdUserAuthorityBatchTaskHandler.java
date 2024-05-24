package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Iterator;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserEventNotifyAPI;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdUserAuthorityEditDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 修改ad域用户权限 批处理任务类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年06月18日
 *
 * @author zouqi
 */
public class AdUserAuthorityBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdUserAuthorityBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private IacUserMgmtAPI userAPI;

    private IacAdUserAuthorityEnum adUserAuthority;

    private UserEventNotifyAPI userEventNotifyAPI;

    private boolean isBatch = true;

    private String userName;

    public AdUserAuthorityBatchTaskHandler(Iterator<DefaultBatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI, IacUserMgmtAPI userAPI,
                                           IacAdUserAuthorityEnum adUserAuthority) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.userAPI = userAPI;
        this.adUserAuthority = adUserAuthority;
    }

    public void setUserEventNotifyAPI(UserEventNotifyAPI userEventNotifyAPI) {
        this.userEventNotifyAPI = userEventNotifyAPI;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_EDIT_AD_USER_AUTHORITY_RESULT);
        }
        // 更新单条用户
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_USER_UPDATE_AUTH_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {userName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_USER_UPDATE_AUTH_SINGLE_FAIL_RESULT)
                    .msgArgs(new String[] {userName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "BatchTaskItem is null");

        IacAdUserAuthorityEditDTO request = new IacAdUserAuthorityEditDTO();
        request.setId(item.getItemID());
        request.setAdUserAuthority(adUserAuthority);
        try {
            // 修改AD域用户权限信息
            String adUserName = userAPI.editAdUserAuthority(request);
            userEventNotifyAPI.notifyUserAuthorityChanged(item.getItemID(), adUserAuthority);
            userName = adUserName;
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_AUTHORITY_SUCCESS, new String[] {adUserName});
            LOGGER.info("userId:{" + request.getId() + "}, 用户修改成功");
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_AUTHORITY_SUCCESS).msgArgs(new String[] {adUserName}).build();
        } catch (BusinessException e) {
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            LOGGER.info("userId:{" + request.getId() + "}, 用户修改失败");
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_AUTHORITY_FAIL, new String[] {exceptionMsg});
            throw new BusinessException(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_AUTHORITY_FAIL, e, exceptionMsg);
        }
    }
}
