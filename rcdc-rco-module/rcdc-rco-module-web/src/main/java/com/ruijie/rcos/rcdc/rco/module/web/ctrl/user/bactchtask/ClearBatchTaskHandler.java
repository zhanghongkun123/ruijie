package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Iterator;
import java.util.UUID;

import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
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
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description: 清空回收站
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 *
 * @author Ghang
 */
public class ClearBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClearBatchTaskHandler.class);

    private ImageUsageTypeEnum imageUsage;

    private String desktopType = Constants.CLOUD_DESKTOP;

    private BaseAuditLogAPI auditLogAPI;

    private UserRecycleBinMgmtAPI recycleBinMgmtAPI;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;


    public ClearBatchTaskHandler(UserRecycleBinMgmtAPI recycleBinMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                 BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.recycleBinMgmtAPI = recycleBinMgmtAPI;
    }

    public void setCbbVDIDeskMgmtAPI(CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI) {
        this.cbbVDIDeskMgmtAPI = cbbVDIDeskMgmtAPI;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (imageUsage == ImageUsageTypeEnum.APP) {
            return buildDefaultFinishResult(successCount, failCount, RcaBusinessKey.RCDC_RCO_RECYCLEBIN_APP_CLEAR_RESULT);
        } else {
            return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_RECYCLEBIN_CLEAR_RESULT);
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        return clear(taskItem.getItemID());
    }

    private DefaultBatchTaskItemResult clear(UUID id) throws BusinessException {
        String logName = obtainDeskName(id);
        try {
            if (imageUsage == ImageUsageTypeEnum.APP) {
                desktopType = Constants.APP_CLOUD_DESKTOP;
            }

            recycleBinMgmtAPI.delete(new IdRequest(id));
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_SUC_LOG, logName, desktopType, StringUtils.EMPTY);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_ITEM_SUC_DESC).msgArgs(new String[]{logName, desktopType, StringUtils.EMPTY}).build();
        } catch (BusinessException e) {
            LOGGER.error("清空回收站云桌面失败，云桌面id=" + id.toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_FAIL_LOG, logName, e.getI18nMessage(), desktopType, StringUtils.EMPTY);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_RECYCLEBIN_DELETE_ITEM_FAIL_DESC, e, logName, e.getI18nMessage(), desktopType, StringUtils.EMPTY);
        }
    }

    private String obtainDeskName(UUID deskId) {
        String resultName = deskId.toString();
        try {
            CbbDeskDTO deskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
            resultName = deskDTO.getName();
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面数据失败，桌面id[{}]", deskId, e);
        }
        return resultName;
    }

}
