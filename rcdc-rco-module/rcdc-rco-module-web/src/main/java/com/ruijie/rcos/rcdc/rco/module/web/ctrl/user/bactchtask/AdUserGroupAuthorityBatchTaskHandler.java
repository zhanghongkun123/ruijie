package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Iterator;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserEventNotifyAPI;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdUserGroupAuthorityEditDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
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
public class AdUserGroupAuthorityBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdUserGroupAuthorityBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private IacUserGroupMgmtAPI userGroupAPI;

    private IacAdUserAuthorityEnum adUserAuthority;

    private UserEventNotifyAPI userEventNotifyAPI;


    public AdUserGroupAuthorityBatchTaskHandler(Iterator<DefaultBatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI,
            IacUserGroupMgmtAPI userGroupAPI, IacAdUserAuthorityEnum adUserAuthority) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.userGroupAPI = userGroupAPI;
        this.adUserAuthority = adUserAuthority;
    }

    public void setUserEventNotifyAPI(UserEventNotifyAPI userEventNotifyAPI) {
        this.userEventNotifyAPI = userEventNotifyAPI;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_RESULT);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {

        Assert.notNull(item, "BatchTaskItem is null");

        IacAdUserGroupAuthorityEditDTO request = new IacAdUserGroupAuthorityEditDTO();
        request.setId(item.getItemID());
        request.setAdUserAuthority(adUserAuthority);
        try {
            // 修改AD域用户权限信息
            String userGroupName = userGroupAPI.editAdUserAuthority(request);
            // 发消息给GT，通知组下用户权限变更,异步任务
            userEventNotifyAPI.notifyUserGroupAuthorityChanged(item.getItemID(), adUserAuthority);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_SUCCESS, new String[] {userGroupName});
            LOGGER.info("groupId:{" + request.getId() + "}, 用户组修改成功");
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_SUCCESS).msgArgs(new String[] {userGroupName}).build();
        } catch (BusinessException e) {
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            LOGGER.info("groupId:{" + request.getId() + "}, 用户组修改成功");
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_FAIL, new String[] {exceptionMsg});
            throw new BusinessException(UserBusinessKey.RCDC_RCO_EDIT_AD_USER_GROUP_AUTHORITY_FAIL, e, exceptionMsg);
        }
    }
}
