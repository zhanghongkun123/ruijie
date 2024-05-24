package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;


import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UserDesktopBindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 绑定用户
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/22
 *
 * @author zqj
 */
public class BindUserBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BindUserBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private IacUserMgmtAPI cbbUserAPI;

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private String userName;

    private UUID deskId;


    public BindUserBatchTaskHandler(Iterator<? extends BatchTaskItem> taskList) {
        super(taskList);
    }

    public UserDesktopMgmtAPI getUserDesktopMgmtAPI() {
        return userDesktopMgmtAPI;
    }

    public void setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public IacUserMgmtAPI getCbbUserAPI() {
        return cbbUserAPI;
    }

    public void setCbbUserAPI(IacUserMgmtAPI cbbUserAPI) {
        this.cbbUserAPI = cbbUserAPI;
    }

    public CbbDeskMgmtAPI getCbbDeskMgmtAPI() {
        return cbbDeskMgmtAPI;
    }

    public void setCbbDeskMgmtAPI(CbbDeskMgmtAPI cbbDeskMgmtAPI) {
        this.cbbDeskMgmtAPI = cbbDeskMgmtAPI;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");
        UUID userId = taskItem.getItemID();

        IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(userId);
        String logName = userDetailDTO.getUserName();

        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);

        try {
            UserDesktopBindUserRequest bindUserRequest = new UserDesktopBindUserRequest();
            bindUserRequest.setUserId(userId);
            bindUserRequest.setDesktopId(deskId);
            userDesktopMgmtAPI.desktopBindUser(bindUserRequest);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_DESKTOP_BIND_USER_SUCCESS, cbbDeskDTO.getName(), logName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_DESKTOP_BIND_USER_SUCCESS).msgArgs(new String[]{cbbDeskDTO.getName(), logName})
                    .build();
        } catch (BusinessException e) {
            LOGGER.error("云桌面[{}]关联用户[{}]失败", cbbDeskDTO.getName(), logName, e);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_DESKTOP_BIND_USER_FAIL, e, cbbDeskDTO.getName(), logName,
                    e.getI18nMessage());
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_BIND_USER_FAIL, e,
                    cbbDeskDTO.getName(), logName, e.getI18nMessage());
        }


    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        return WebBatchTaskUtils.buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_BATCH_RESULT);
    }
}
