package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbSetIDVDeskNetworkDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop.EditIDVDesktopNetworkWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
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
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 单台修改IDV云桌面网络配置任务处理器
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年1月16日
 *
 * @author heruiyuan1
 */
public class EditIDVDesktopNetworkBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditIDVDesktopNetworkBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private final EditIDVDesktopNetworkWebRequest editIDVDesktopNetworkWebRequest;

    private final CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    private final CloudDesktopWebService cloudDesktopWebService;

    private String desktopName = "";

    public EditIDVDesktopNetworkBatchTaskHandler(EditIDVDesktopNetworkWebRequest editIDVDesktopNetworkWebRequest,
                                                 BatchTaskItem batchTaskItem,
                                                 BaseAuditLogAPI auditLogAPI,
                                                 CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI,
                                                 CloudDesktopWebService cloudDesktopWebService) {
        super(batchTaskItem);
        this.editIDVDesktopNetworkWebRequest = editIDVDesktopNetworkWebRequest;
        this.auditLogAPI = auditLogAPI;
        this.cbbIDVDeskMgmtAPI = cbbIDVDeskMgmtAPI;
        this.cloudDesktopWebService = cloudDesktopWebService;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "taskItem is not null");
        Assert.notNull(batchTaskItem.getItemID(), "taskItemId is not null");

        UUID deskId = editIDVDesktopNetworkWebRequest.getId();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(deskId);
        String tmpDesktopName = cloudDesktopDetailDTO.getDesktopName();

        try {
            cloudDesktopWebService.checkThirdPartyDesktop(deskId, UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_THIRD_PARTY);
            LOGGER.info("准备下发编辑IDV云桌面[id={}]网络配置命令", deskId);

            CbbSetIDVDeskNetworkDTO cbbReq = new CbbSetIDVDeskNetworkDTO();
            cbbReq.setAutoDhcp(editIDVDesktopNetworkWebRequest.getAutoDhcp());
            cbbReq.setIpAddr(editIDVDesktopNetworkWebRequest.getIpAddr());
            cbbReq.setMask(editIDVDesktopNetworkWebRequest.getMask());
            cbbReq.setGateway(editIDVDesktopNetworkWebRequest.getGateway());
            cbbReq.setAutoDns(editIDVDesktopNetworkWebRequest.getAutoDns());
            cbbReq.setDns(editIDVDesktopNetworkWebRequest.getDns());
            cbbReq.setDnsBack(editIDVDesktopNetworkWebRequest.getDnsBack());
            cbbIDVDeskMgmtAPI.setIDVDeskNetwork(deskId, cbbReq);

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_SUC_LOG, tmpDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_SUC_DESC)
                    .msgArgs(new String[]{tmpDesktopName})
                    .build();
        } catch (BusinessException e) {
            LOGGER.error("发送编辑IDV云桌面网络配置命令失败，云桌面id=" + deskId, e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_FAIL_LOG, tmpDesktopName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_FAIL_DESC, e, tmpDesktopName, e.getI18nMessage());
        } finally {
            desktopName = tmpDesktopName;
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_RESULT_SUC) //
                    .msgArgs(new String[]{desktopName})
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_IDV_NETWORK_RESULT_FAIL) //
                    .msgArgs(new String[]{desktopName})
                    .build();
        }
    }


}
