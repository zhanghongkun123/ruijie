package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.batchtask;

import java.util.*;

import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MoveDesktopToRecycleBinRequest;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 批量删除应用主机Handler
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月11日
 *
 * @author liuwc
 */
public class DeleteRcaHostBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteRcaHostBatchTaskHandler.class);

    private Set<UUID> poolIdSet;

    private boolean isDesktopHost;

    private Boolean shouldOnlyDeleteDataFromDb;

    private String prefix;

    private RcaHostAPI rcaHostAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private UserProfileMgmtAPI userProfileMgmtAPI;

    private CloudDesktopWebService cloudDesktopWebService;

    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private RcaHostSessionAPI rcaHostSessionAPI;

    private final static Integer INITIAL_NUMBER = 0;

    public DeleteRcaHostBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI,
                                         boolean isDesktopHost) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.isDesktopHost = isDesktopHost;
        this.poolIdSet = new HashSet<>();
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        UUID hostId = batchTaskItem.getItemID();
        RcaHostDTO rcaHostDTO = rcaHostAPI.getById(hostId);

        if (rcaHostDTO.getPoolId() != null) {
            poolIdSet.add(rcaHostDTO.getPoolId());
        }

        // 镜像派生主机的删除
        if (isDesktopHost) {
            return removeDesktopHost(rcaHostDTO);
        }

        // 第三方云主机的删除
        return removeThirdPartyHost(rcaHostDTO);

    }

    private DefaultBatchTaskItemResult removeThirdPartyHost(RcaHostDTO rcaHostDTO) {
        UUID hostId = rcaHostDTO.getId();
        if (!Objects.isNull(rcaHostDTO.getPoolId())) {
            LOGGER.error("应用主机[{}]删除失败，应用主机已绑定池[{}}]", hostId, rcaHostDTO.getPoolId());
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_HAS_POOL_DELETE_FAIL
                    , rcaHostDTO.getName(), getPoolName(rcaHostDTO.getPoolId()));
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_HOST_HAS_POOL_DELETE_FAIL)
                    .msgArgs(new String[] {rcaHostDTO.getName(), getPoolName(rcaHostDTO.getPoolId())}).build();
        }

        if (!ObjectUtils.isEmpty(rcaHostDTO) && rcaHostDTO.getStatus() == RcaEnum.HostStatus.ONLINE &&
                rcaHostDTO.getSessionUsage() != null && !rcaHostDTO.getSessionUsage().equals(INITIAL_NUMBER)) {
            LOGGER.error("应用主机[{}]删除失败，应用主机正在使用", hostId);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_USAGE_DELETE_FAIL, rcaHostDTO.getName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_HOST_USAGE_DELETE_FAIL)
                    .msgArgs(new String[] {rcaHostDTO.getName()}).build();
        }
        try {
            rcaHostAPI.deleteRcaHostById(hostId, rcaHostDTO.getHostSourceType());
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_SUCCESS, rcaHostDTO.getName(), StringUtils.EMPTY);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_SUCCESS)
                    .msgArgs(new String[] {rcaHostDTO.getName(), StringUtils.EMPTY}).build();
        } catch (BusinessException e) {
            LOGGER.error("应用主机[{}]删除失败，异常原因：", hostId, e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_FAIL, e.getI18nMessage(), StringUtils.EMPTY);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_FAIL)
                    .msgArgs(new String[] {e.getI18nMessage(), StringUtils.EMPTY}).build();
        }
    }

    private DefaultBatchTaskItemResult removeDesktopHost(RcaHostDTO rcaHostDTO) {
        UUID deskId = rcaHostDTO.getId();
        try {
            MoveDesktopToRecycleBinRequest request = new MoveDesktopToRecycleBinRequest();
            request.setDesktopId(deskId);
            RcaHostDTO hostDTO = rcaHostAPI.getById(deskId);

            if (Boolean.TRUE.equals(shouldOnlyDeleteDataFromDb)) {
                // 需要将会话和绑定关系全部清除
                rcaHostSessionAPI.unbindAllUserByHostId(deskId);
                rcaHostSessionAPI.deleteAllByHostId(deskId);
                cloudDesktopMgmtAPI.deleteDeskFromDb(deskId);
            } else {
                // 检查主机是否有会话绑定，若有需要管理员手动解绑后再移除
                if (rcaHostSessionAPI.countByHostId(deskId) > 0) {
                    throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_UNBIND_HOST_FAIL_BY_HAS_SESSION, hostDTO.getName());
                }
                cloudDesktopMgmtAPI.forceDeleteDesktop(deskId);
            }
            userProfileMgmtAPI.deleteFailCleanRequestById(deskId);
            rcaHostAPI.deleteRcaHostById(deskId, rcaHostDTO.getHostSourceType());
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_SUCCESS, rcaHostDTO.getName(), prefix);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_SUCCESS)
                    .msgArgs(new String[]{rcaHostDTO.getName(), prefix}).build();

        } catch (BusinessException e) {
            LOGGER.error("删除应用主机桌面失败，云桌面id=" + deskId.toString(), e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_FAIL, e.getI18nMessage(), prefix);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_FAIL)
                    .msgArgs(new String[]{e.getI18nMessage(), prefix}).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (!isDesktopHost) {
            // 仅第三方主机删除需要重新从主机汇聚应用
            poolIdSet.stream().forEach(poolId -> {
                try {
                    rcaAppPoolAPI.mergeAppFromHost(poolId);
                    rcaAppPoolAPI.notifyTerminalAppChange(poolId);
                } catch (BusinessException e) {
                    LOGGER.error("刷新应用池[{}]应用失败", poolId, e);
                }
            });
        }
        prefix = Objects.isNull(prefix) ? StringUtils.EMPTY : prefix;
        return WebBatchTaskUtils.buildDefaultFinishResult(successCount, failCount, RcaBusinessKey.RCDC_RCA_APP_HOST_DELETE_TASK_RESULT, prefix);

    }

    public void setRcaHostAPI(RcaHostAPI rcaHostAPI) {
        this.rcaHostAPI = rcaHostAPI;
    }

    public void setRcaAppPoolAPI(RcaAppPoolAPI rcaAppPoolAPI) {
        this.rcaAppPoolAPI = rcaAppPoolAPI;
    }

    public void setUserProfileMgmtAPI(UserProfileMgmtAPI userProfileMgmtAPI) {
        this.userProfileMgmtAPI = userProfileMgmtAPI;
    }

    public void setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        this.cloudDesktopWebService = cloudDesktopWebService;
    }

    public void setCloudDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
    }

    public void setRcaHostSessionAPI(RcaHostSessionAPI rcaHostSessionAPI) {
        this.rcaHostSessionAPI = rcaHostSessionAPI;
    }

    private String getPoolName(UUID poolId) {
        try {
            return rcaAppPoolAPI.getAppPoolById(poolId).getName();
        } catch (BusinessException e) {
            LOGGER.error("获取应用池名称失败，e={}", e);
            // 获取不到应用池名称返回空null即可
            return null;

        }
    }

    /**
     * 设置是否是从库表删除
     *
     * @param shouldOnlyDeleteDataFromDb ture说明从库表强制删除
     */
    public void setShouldOnlyDeleteDataFromDb(@Nullable Boolean shouldOnlyDeleteDataFromDb) {
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
        this.prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);
    }
}
