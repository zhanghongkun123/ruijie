package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.batchtask;

import java.util.*;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacQueryUserListPageDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaOneClientNotifyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.spi.RcaGroupMemberSPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import org.springframework.util.Assert;


import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppGroupAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * Description: 修改应用分组分配用户数据批处理handler
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/01/16
 *
 * @author zhengjingyong
 */
public class RcaUnbindAppGroupUserBatchHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaUnbindAppGroupUserBatchHandler.class);

    private RcaAppPoolAPI rcaAppPoolAPI;

    private RcaAppGroupAPI rcaAppGroupAPI;

    private RcaGroupMemberAPI rcaGroupMemberAPI;

    private BaseAuditLogAPI auditLogAPI;

    private IacUserMgmtAPI cbbUserAPI;

    private UUID appGroupId;

    private String userName = "";

    private Map<UUID, List<UUID>> groupIdWithUserIdMap;

    private RcaOneClientNotifyAPI rcaOneClientNotifyAPI;

    private RcaGroupMemberSPI rcaGroupMemberSPI;

    private List<UUID> deleteUserIdList;

    private UUID poolId;

    public RcaUnbindAppGroupUserBatchHandler(Iterator<? extends BatchTaskItem> iterator, RcaAppPoolAPI rcaAppPoolAPI,
                                             RcaAppGroupAPI rcaAppGroupAPI, RcaGroupMemberAPI rcaGroupMemberAPI, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.rcaAppPoolAPI = rcaAppPoolAPI;
        this.rcaAppGroupAPI = rcaAppGroupAPI;
        this.rcaGroupMemberAPI = rcaGroupMemberAPI;
        this.auditLogAPI = auditLogAPI;
        this.groupIdWithUserIdMap = new HashMap<>();
        this.deleteUserIdList = new ArrayList<>();
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");

        UUID userId = taskItem.getItemID();
        try {
            RcaGroupDTO groupDTO = rcaAppGroupAPI.getGroupDTO(appGroupId);
            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(groupDTO.getPoolId());
            if (RcaEnum.PoolState.AVAILABLE != appPoolBaseDTO.getPoolState()) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_APP_POOL_UNAVAILABLE_UPDATE_BIND_FORBID,
                        groupDTO.getName(), appPoolBaseDTO.getName());
            }
            poolId = appPoolBaseDTO.getId();

            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
            userName = userDetail.getUserName();

            // 仅解除个人类型绑定，分组绑定批处理完成执行
            UUID bindGroupId = rcaGroupMemberAPI.unbindUserWithoutGroup(appGroupId, userId);
            if (bindGroupId != null) {
                saveGroupWithUser(bindGroupId, userId);
            }
            deleteUserIdList.add(userId);

            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_USER_TASK_ITEM_SUCCESS, userName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_USER_TASK_ITEM_SUCCESS)
                    .msgArgs(new String[]{userName}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_USER_TASK_ITEM_FAIL,
                    userName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_USER_TASK_ITEM_FAIL)
                    .msgArgs(new String[]{userName, e.getI18nMessage()}).build();
        }
    }

    private synchronized void saveGroupWithUser(UUID userGroupId, UUID userId) {
        List<UUID> expiredUserIdList = groupIdWithUserIdMap.getOrDefault(userGroupId, new ArrayList<>());
        expiredUserIdList.add(userId);
        groupIdWithUserIdMap.put(userGroupId, expiredUserIdList);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (!groupIdWithUserIdMap.isEmpty()) {
            for (Map.Entry<UUID, List<UUID>> groupEntity : groupIdWithUserIdMap.entrySet()) {
                UUID userGroupId = groupEntity.getKey();
                try {
                    rcaGroupMemberAPI.unbindGroup(appGroupId, userGroupId);
                    List<UUID> excludeUserIdList = groupEntity.getValue();
                    // 组下用户可能会超过1000 ,分多次绑定用户
                    batchBindUser(appGroupId, userGroupId, excludeUserIdList);
                } catch (Exception ex) {
                    LOGGER.error("批量解除用户分配发生异常，应用组id: [{}], 用户组id:[{}]", appGroupId, userGroupId);
                }
            }
        }

        try {
            if (!CollectionUtils.isEmpty(deleteUserIdList)) {
                // 解除绑定后置处理
                rcaGroupMemberSPI.unbindUser(poolId, appGroupId, deleteUserIdList);
            }
        } catch (Exception ex) {
            LOGGER.error("推送应用更新通知到用户发生异常，ex: ", ex);
        }

        return buildDefaultFinishResult(successCount, failCount,
                RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_BATCH_DELETE_BIND_USER_TASK_RESULT);
    }
    
    private void batchBindUser(UUID appGroupId, UUID userGroupId, List<UUID> excludeUserIdList) throws BusinessException {
        // 组下用户可能会超过1000 ,分多次绑定用户
        IacPageResponse<IacUserDetailDTO> pageResult = pageQueryByGroupId(userGroupId, 0);
        // 总页数
        int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
        for (int page = 0; page < totalPage; page++) {
            // 前面已查过，不重复查
            if (page == 0) {
                bindUser(appGroupId, Arrays.asList(pageResult.getItemArr()), excludeUserIdList);
                continue;
            }
            pageResult = pageQueryByGroupId(userGroupId, page);
            if (pageResult.getTotal() == 0) {
                break;
            }
            bindUser(appGroupId, Arrays.asList(pageResult.getItemArr()), excludeUserIdList);
        }
    }
    
    private void bindUser(UUID appGroupId, List<IacUserDetailDTO> userDetailDTOList, List<UUID> excludeUserIdList) throws BusinessException {
        List<UUID> addUserIdList =
                userDetailDTOList.stream().filter(item -> item.getUserType() != IacUserTypeEnum.VISITOR && !excludeUserIdList.contains(item.getId()))
                        .map(IacUserDetailDTO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(addUserIdList)) {
            LOGGER.info("批量解除用户分配后，该用户组已无其他用户，跳过用户回填处理，应用组id: [{}]", appGroupId);
            return;
        }
        rcaGroupMemberAPI.batchBindUser(appGroupId, addUserIdList);
    }

    private IacPageResponse<IacUserDetailDTO> pageQueryByGroupId(UUID groupId, Integer page) throws BusinessException {
        IacQueryUserListPageDTO pageDTO = new IacQueryUserListPageDTO();
        pageDTO.setGroupId(groupId);
        pageDTO.setPage(page);
        pageDTO.setLimit(Constants.MAX_QUERY_LIST_SIZE);
        return cbbUserAPI.pageQueryUserListByGroupId(pageDTO);
    }

    public UUID getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(UUID appGroupId) {
        this.appGroupId = appGroupId;
    }

    public IacUserMgmtAPI getCbbUserAPI() {
        return cbbUserAPI;
    }

    public void setCbbUserAPI(IacUserMgmtAPI cbbUserAPI) {
        this.cbbUserAPI = cbbUserAPI;
    }

    public RcaOneClientNotifyAPI getRcaOneClientNotifyAPI() {
        return rcaOneClientNotifyAPI;
    }

    public void setRcaOneClientNotifyAPI(RcaOneClientNotifyAPI rcaOneClientNotifyAPI) {
        this.rcaOneClientNotifyAPI = rcaOneClientNotifyAPI;
    }

    public void setRcaGroupMemberSPI(RcaGroupMemberSPI rcaGroupMemberSPI) {
        this.rcaGroupMemberSPI = rcaGroupMemberSPI;
    }
}
