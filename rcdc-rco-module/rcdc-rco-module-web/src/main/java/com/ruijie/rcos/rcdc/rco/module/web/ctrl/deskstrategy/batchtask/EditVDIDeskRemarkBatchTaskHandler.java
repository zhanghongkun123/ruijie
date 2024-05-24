package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * Description: 批量编辑云桌面标签
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月28日
 *
 * @author linrenjian
 */

public class EditVDIDeskRemarkBatchTaskHandler extends AbstractBatchTaskHandler {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EditVDIDeskRemarkBatchTaskHandler.class);

    /**
     * VDI云桌面管理操作
     */
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;


    private BaseAuditLogAPI auditLogAPI;

    /**
     * 处理的记录数累计值
     */
    protected AtomicInteger processItemCount = new AtomicInteger(0);

    /**
     * 桌面名称
     */
    private String desktopName;

    /**
     * 桌面
     */
    private CloudDesktopWebService cloudDesktopWebService;

    public EditVDIDeskRemarkBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItems, BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItems);
        Assert.notNull(batchTaskItems, "batchTaskItems is not null");
        this.cbbVDIDeskMgmtAPI = SpringBeanHelper.getBean(CbbVDIDeskMgmtAPI.class);
        this.cloudDesktopWebService = SpringBeanHelper.getBean(CloudDesktopWebService.class);
        this.auditLogAPI = auditLogAPI;
    }


    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem is not null");
        EditVDIDeskRemarkBatchTaskItem taskItem = (EditVDIDeskRemarkBatchTaskItem) batchTaskItem;
        // 获取现在的桌面信息
        CloudDesktopDetailDTO desktopDTO = cloudDesktopWebService.obtainCloudDesktopResponse(taskItem.getItemID());
        //获取当前桌面名称
        String tmpDesktopName = desktopDTO.getDesktopName();
        try {
            checkBeforeUpdate(desktopDTO);
            // 更新云桌面标签
            cbbVDIDeskMgmtAPI.updateVDIDeskRemark(taskItem.getItemID(), taskItem.getRemark());
            // 更新成功
            auditLogAPI.recordLog(UserBusinessKey.RCO_EDIT_DESKTOP_REMARK_SUCCESS_LOG, tmpDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCO_EDIT_DESKTOP_REMARK_SUCCESS_LOG).msgArgs(new String[] {tmpDesktopName}).build();
        } catch (BusinessException e) {
            LOGGER.error("编辑云桌面[{}]标签失败", tmpDesktopName, e);
            auditLogAPI.recordLog(UserBusinessKey.RCO_EDIT_DESKTOP_REMARK_FAIL_LOG, e, tmpDesktopName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCO_EDIT_DESKTOP_REMARK_FAIL_LOG, e, tmpDesktopName, e.getI18nMessage());
        } finally {
            desktopName = tmpDesktopName;
            processItemCount.incrementAndGet();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (processItemCount.get() == 1) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCO_EDIT_DESKTOP_REMARK_SUCCESS_LOG)
                        .msgArgs(new String[] {desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            }
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCO_EDIT_DESKTOP_REMARK_FAIL_LOG_DESC)
                    .msgArgs(new String[] {desktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
        return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCO_EDIT_DESKTOP_REMARK_BATCH_RESULT);
    }

    private void checkBeforeUpdate(CloudDesktopDetailDTO desktopDTO) throws BusinessException {
        if (StringUtils.equals(desktopDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC.name())) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_REMARK_DYNAMIC_POOL_NOT_SUPPORT, desktopDTO.getDesktopName());
        }
    }
}
