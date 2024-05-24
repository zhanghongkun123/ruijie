package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbEditDeskPwdConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 修改桌面密码展示配置处理类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/11 11:38
 *
 * @author yxq
 */
public class EditDeskRootPwdConfigHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditDeskRootPwdConfigHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    private Boolean showRootPwd;


    public EditDeskRootPwdConfigHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "item can not be null");

        UUID deskId = item.getItemID();
        String desktopName = deskId.toString();

        try {
            desktopName = cloudDesktopMgmtAPI.getDesktopDetailById(deskId).getDesktopName();
            CbbEditDeskPwdConfigDTO editDeskPwdConfigDTO = new CbbEditDeskPwdConfigDTO();
            editDeskPwdConfigDTO.setDeskId(deskId);
            editDeskPwdConfigDTO.setShowRootPwd(showRootPwd);
            cloudDesktopMgmtAPI.editDeskRootPwdConfig(editDeskPwdConfigDTO);

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_ROOT_PWD_CONFIG_SUCCESS, desktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_EDIT_ROOT_PWD_CONFIG_SUCCESS).msgArgs(desktopName).build();
        } catch (BusinessException e) {
            LOGGER.error("修改云桌面[{}]密码显示配置失败", deskId, e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EDIT_ROOT_PWD_CONFIG_FAIL, e, desktopName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_EDIT_ROOT_PWD_CONFIG_FAIL, e, desktopName, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_EDIT_ROOT_PWD_CONFIG_BATCH_TASK_RESULT);
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public Boolean getShowRootPwd() {
        return showRootPwd;
    }

    public void setShowRootPwd(Boolean showRootPwd) {
        this.showRootPwd = showRootPwd;
    }

    public UserDesktopMgmtAPI getCloudDesktopMgmtAPI() {
        return cloudDesktopMgmtAPI;
    }

    public void setCloudDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
    }
}
