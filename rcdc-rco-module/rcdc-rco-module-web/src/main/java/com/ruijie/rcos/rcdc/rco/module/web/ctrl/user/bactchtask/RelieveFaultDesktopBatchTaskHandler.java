package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;


import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskFaultInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CbbDeskFaultInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/03/23 16:08
 *
 * @author ljm
 */
public class RelieveFaultDesktopBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelieveFaultDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private DeskFaultInfoAPI deskFaultInfoAPI;

    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    private Map<UUID, String> idMap;

    private boolean isBatch = true;

    private String desktopName;

    private String userName;

    private String desktopType = Constants.CLOUD_DESKTOP;

    public RelieveFaultDesktopBatchTaskHandler(Map<UUID, String> idMap,
        Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.idMap = idMap;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    /**
     * 传入云桌面故障对象
     *
     * @param deskFaultInfoAPI 对象
     */
    public void setDesktopFaultInfoAPI(DeskFaultInfoAPI deskFaultInfoAPI) {
        Assert.notNull(deskFaultInfoAPI, "desktopFaultInfoAPI must not be null");
        this.deskFaultInfoAPI = deskFaultInfoAPI;
    }

    /**
     * 传入云桌面服务对象
     *
     * @param cloudDesktopMgmtAPI 云桌面对象
     */
    public void setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "BatchTaskItem不能为null");
        String computerId = idMap.get(taskItem.getItemID());
        String deskIdentification = computerId;
        UUID uuid = UUID.fromString(computerId);
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI
            .getDesktopDetailById(uuid);
        String localUserName = cloudDesktopDetailDTO.getUserName();
        String localDesktopName = cloudDesktopDetailDTO.getDesktopName();
        try {
            CbbDeskFaultInfoResponse response = deskFaultInfoAPI.findFaultInfoByDeskId(uuid);
            if (response != null && response.getCbbDeskFaultInfoDTO() != null) {
                deskFaultInfoAPI.relieveFaultForCloudDesk(uuid);
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_SUCCESS, localUserName, localDesktopName, desktopType);
            } else {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_FAULT_NULL);
            }

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_SUCCESS)
                .msgArgs(new String[]{localUserName, localDesktopName, desktopType}).build();

        } catch (BusinessException e) {
            LOGGER.error("取消云桌面报障异常：" + deskIdentification, e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_FAIL, e, userName, localDesktopName,
                    e.getI18nMessage(), desktopType);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_FAIL, e, localUserName,
                    localDesktopName, e.getI18nMessage(), desktopType);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量取消云桌面报障
        if (isBatch) {
            if (desktopType.equals(Constants.APP_CLOUD_DESKTOP)) {
                return buildDefaultFinishResult(successCount, failCount,
                        RcaBusinessKey.RCDC_RCO_APP_DESKTOP_RELIEVE_FAULT_RESULT);
            } else {
                return buildDefaultFinishResult(successCount, failCount,
                        UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_RESULT);
            }
        }

        // 取消单条云桌面报障
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder()
                .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_SUCCESS_RESULT)
                .msgArgs(new String[]{userName, desktopName, desktopType})
                .batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder()
                .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_RELIEVE_FAULT_FAIL_RESULT)
                .msgArgs(new String[]{userName, desktopName, desktopType})
                .batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }
}
