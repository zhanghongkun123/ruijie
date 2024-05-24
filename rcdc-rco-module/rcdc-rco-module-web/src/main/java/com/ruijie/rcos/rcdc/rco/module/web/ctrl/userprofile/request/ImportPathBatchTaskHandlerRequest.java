package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

/**
 * Description: 导入批量创建路径请求对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/26
 *
 * @author WuShengQiang
 */
public class ImportPathBatchTaskHandlerRequest {

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    private String userName;

    public Iterator<? extends BatchTaskItem> getBatchTaskItemIterator() {
        return batchTaskItemIterator;
    }

    public void setBatchTaskItemIterator(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        this.batchTaskItemIterator = batchTaskItemIterator;
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public UserProfileMgmtAPI getUserProfileMgmtAPI() {
        return userProfileMgmtAPI;
    }

    public void setUserProfileMgmtAPI(UserProfileMgmtAPI userProfileMgmtAPI) {
        this.userProfileMgmtAPI = userProfileMgmtAPI;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}