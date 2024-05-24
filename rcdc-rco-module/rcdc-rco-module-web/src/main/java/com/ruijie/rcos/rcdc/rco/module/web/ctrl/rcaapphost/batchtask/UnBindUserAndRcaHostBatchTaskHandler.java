package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 解除绑定用户和应用主机会话关系
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月23日
 *
 * @author liuwc
 */
public class UnBindUserAndRcaHostBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnBindUserAndRcaHostBatchTaskHandler.class);

    private UUID hostId;
    
    private RcaHostAPI rcaHostAPI;

    private IacUserMgmtAPI cbbUserAPI;

    private RcaHostSessionAPI rcaHostSessionAPI;
    
    private BaseAuditLogAPI auditLogAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    public UnBindUserAndRcaHostBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI, UUID hostId) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.hostId = hostId;
    }

    public void setRcaHostAPI(RcaHostAPI rcaHostAPI) {
        this.rcaHostAPI = rcaHostAPI;
    }

    public void setRcaHostSessionAPI(RcaHostSessionAPI rcaHostSessionAPI) {
        this.rcaHostSessionAPI = rcaHostSessionAPI;
    }

    public void setCbbUserAPI(IacUserMgmtAPI cbbUserAPI) {
        this.cbbUserAPI = cbbUserAPI;
    }

    public void setRcaAppPoolAPI(RcaAppPoolAPI rcaAppPoolAPI) {
        this.rcaAppPoolAPI = rcaAppPoolAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");

        UUID userId = taskItem.getItemID();
        RcaHostDTO rcaHostDTO = rcaHostAPI.getById(hostId);
        String hostName = rcaHostDTO.getName();
        String userName = "";

        try {
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
            userName = userDetail.getUserName();

            UUID hostSingleBindUser = rcaHostSessionAPI.getHostSingleBindUser(hostId, RcaEnum.HostSessionBindMode.STATIC);
            rcaHostSessionAPI.unbindUser(userId, hostId);
            if (RcaEnum.HostSourceType.VDI.equals(rcaHostDTO.getHostSourceType()) && hostSingleBindUser != null) {
                LOGGER.info("应用池[{}]VDI应用主机[{}]解除静态单会话绑定", rcaHostDTO.getPoolId(), rcaHostDTO.getId());
                rcaAppPoolAPI.regressSelfHostToPool(rcaHostDTO.getId(), rcaHostDTO.getPoolId());
            }
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_SUCCESS, hostName, userName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_SUCCESS).msgArgs(new String[]{hostName, userName}).build();
        } catch (BusinessException e) {
            LOGGER.error("应用主机解除绑定用户失败,主机id={}, 名称={}, 用户={}", hostId, hostName, userName);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_FAIL, hostName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_FAIL)
                    .msgArgs(new String[]{hostName, e.getI18nMessage()})
                    .build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCA_HOST_UNBIND_USER_SESSION_BATCH_RESULT);
    }

}
