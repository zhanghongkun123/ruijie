package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.batchtask;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppGroupAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaOneClientNotifyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupMemberDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.CheckDuplicateGroupNameRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.CreateAppGroupRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.RcaUpdateAppGroupBindRequest;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request.RcaGroupBindAppGroupWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request.RcaUpdateBindWebRequest;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 创建应用分组分配用户数据批处理handler
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/01/16
 *
 * @author zhengjingyong
 */
public class RcaCreateGroupAndBindBatchHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaCreateGroupAndBindBatchHandler.class);

    private RcaAppPoolAPI rcaAppPoolAPI;

    private RcaAppGroupAPI rcaAppGroupAPI;

    private RcaGroupMemberAPI rcaGroupMemberAPI;

    private BaseAuditLogAPI auditLogAPI;

    private UUID[] userGroupIdArr;

    private RcaOneClientNotifyAPI rcaOneClientNotifyAPI;

    private List<UUID> appGroupIdList = new ArrayList<>();

    public RcaCreateGroupAndBindBatchHandler(UUID[] userGroupIdArr, Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
        this.userGroupIdArr = userGroupIdArr;
    }


    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");

        RcaCreateGroupWithBindBatchTaskItem item = (RcaCreateGroupWithBindBatchTaskItem) taskItem;
        RcaGroupBindAppGroupWebRequest bindAppGroupRequest = item.getBindAppGroupRequest();

        UUID groupId = bindAppGroupRequest.getGroupId();
        String groupName = bindAppGroupRequest.getGroupName();
        String poolName = "";
        appGroupIdList.add(groupId);

        try {
            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(item.getPoolId());
            poolName = appPoolBaseDTO.getName();

            RcaGroupDTO groupDTO;
            if (groupId == null && !StringUtils.isEmpty(groupName)) {
                CheckDuplicateGroupNameRequest duplicateGroupNameRequest = new CheckDuplicateGroupNameRequest();
                duplicateGroupNameRequest.setAppPoolId(item.getPoolId());
                duplicateGroupNameRequest.setName(groupName);
                rcaAppGroupAPI.checkNameDuplication(duplicateGroupNameRequest);

                CreateAppGroupRequest createAppGroupRequest = new CreateAppGroupRequest();
                createAppGroupRequest.setDefaultGroup(Boolean.FALSE);
                createAppGroupRequest.setAppIdList(bindAppGroupRequest.getAppIdList());
                createAppGroupRequest.setAppPoolId(item.getPoolId());
                createAppGroupRequest.setName(groupName);
                groupDTO = rcaAppGroupAPI.createAppGroup(createAppGroupRequest);
            } else {
                groupDTO = rcaAppGroupAPI.getGroupDTO(groupId);
            }
            groupId = groupDTO.getId();
            groupName = groupDTO.getName();
            if (RcaEnum.PoolState.AVAILABLE != appPoolBaseDTO.getPoolState()) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_APP_POOL_UNAVAILABLE_UPDATE_BIND_FORBID,
                        groupDTO.getName(), poolName);
            }

            RcaUpdateBindWebRequest updateBindWebRequest = bindAppGroupRequest.getUpdateBindRequest();
            RcaUpdateAppGroupBindRequest updateBindRequest = new RcaUpdateAppGroupBindRequest();
            BeanUtils.copyProperties(updateBindWebRequest, updateBindRequest);
            updateBindRequest.setAppGroupId(groupId);
            // 如果不是超管需要把非权限内的用户组加回来
            checkGroupAuthAndAddDefaultGroup(bindAppGroupRequest.getGroupId(), updateBindRequest);
            rcaGroupMemberAPI.updateGroupMember(updateBindRequest);

            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_UPDATE_BIND_OBJ_TASK_ITEM_SUCCESS, poolName, groupName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_UPDATE_BIND_OBJ_TASK_ITEM_SUCCESS)
                    .msgArgs(new String[] {poolName, groupName}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_UPDATE_BIND_OBJ_TASK_ITEM_FAIL, poolName, groupName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_UPDATE_BIND_OBJ_TASK_ITEM_FAIL)
                    .msgArgs(new String[] {poolName, groupName, e.getI18nMessage()}).build();
        }
    }

    private void checkGroupAuthAndAddDefaultGroup(UUID appGroupId, RcaUpdateAppGroupBindRequest bindRequest) throws BusinessException {
        if (ArrayUtils.isEmpty(userGroupIdArr)) {
            return;
        }

        List<RcaGroupMemberDTO> groupMemberDTOList = rcaGroupMemberAPI.listGroupMember(appGroupId, RcaEnum.GroupMemberType.USER_GROUP);
        if (CollectionUtils.isEmpty(groupMemberDTOList)) {
            return;
        }
        List<UUID> appGroupBindUserGroupIdList = groupMemberDTOList.stream().map(RcaGroupMemberDTO::getMemberId).collect(Collectors.toList());
        List<UUID> selectedUserGroupIdList = Optional.ofNullable(bindRequest.getSelectedGroupIdList()).orElse(Lists.newArrayList());
        Set<UUID> authGroupIdSet = Sets.newHashSet(userGroupIdArr);
        // 分级分权，只选择了部分分组。将回填当前已经绑定的用户组
        selectedUserGroupIdList.addAll(appGroupBindUserGroupIdList.stream().filter(id -> !authGroupIdSet.contains(id)).collect(Collectors.toList()));
        selectedUserGroupIdList = selectedUserGroupIdList.stream().distinct().collect(Collectors.toList());
        bindRequest.setSelectedGroupIdList(selectedUserGroupIdList);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (!CollectionUtils.isEmpty(appGroupIdList)) {
            // 发送应用变更通知到OC
            try {
                rcaOneClientNotifyAPI.notifyAllOnlineUserAppChangedByAppGroupId(appGroupIdList.stream().distinct().collect(Collectors.toList()));
            } catch (Exception ex) {
                LOGGER.error("推送应用更新通知到用户发生异常，ex: ", ex);
            }
        }

        return buildDefaultFinishResult(successCount, failCount, RcaBusinessKey.RCDC_RCA_APP_POOL_GROUP_UPDATE_BIND_OBJ_TASK_RESULT);
    }


    public void setRcaAppPoolAPI(RcaAppPoolAPI rcaAppPoolAPI) {
        this.rcaAppPoolAPI = rcaAppPoolAPI;
    }


    public void setRcaAppGroupAPI(RcaAppGroupAPI rcaAppGroupAPI) {
        this.rcaAppGroupAPI = rcaAppGroupAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setRcaGroupMemberAPI(RcaGroupMemberAPI rcaGroupMemberAPI) {
        this.rcaGroupMemberAPI = rcaGroupMemberAPI;
    }

    public void setRcaOneClientNotifyAPI(RcaOneClientNotifyAPI rcaOneClientNotifyAPI) {
        this.rcaOneClientNotifyAPI = rcaOneClientNotifyAPI;
    }
}
