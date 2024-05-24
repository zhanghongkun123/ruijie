package com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.batchtask;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDelUserGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.base.iac.module.dto.IacLoginUserDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleGroupPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.request.DeleteUserGroupBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
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
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;


/**
 * Description: 删除用户分组批量任务处理器
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年12月12日
 *
 * @author wjp
 */
public class DeleteUserGroupBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserGroupBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    private UserDesktopConfigAPI userDesktopConfigAPI;

    private PermissionHelper permissionHelper;

    private SessionContext sessionContext;

    private UserGroupHelper userGroupHelper;

    private RcaGroupMemberAPI rcaGroupMemberAPI;

    private IacLoginUserDTO adminDTO;

    private boolean isBatch = true;

    private String groupName = "";

    public DeleteUserGroupBatchTaskHandler(DeleteUserGroupBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.cbbUserGroupAPI = request.getCbbUserGroupAPI();
        this.userDesktopConfigAPI = request.getUserDesktopConfigAPI();
        this.permissionHelper = request.getPermissionHelper();
        this.sessionContext = request.getSessionContext();
        this.userGroupHelper = request.getUserGroupHelper();
        this.rcaGroupMemberAPI = request.getRcaGroupMemberAPI();
    }

    public void setAdminDTO(IacLoginUserDTO adminDTO) {
        this.adminDTO = adminDTO;
    }

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        DeleteUserGroupBatchTaskItem deleteUserGroupBatchTaskItem = (DeleteUserGroupBatchTaskItem) taskItem;
        UUID groupId = deleteUserGroupBatchTaskItem.getItemID();
        String localGroupName = groupId.toString();
        UUID moveGroupId = deleteUserGroupBatchTaskItem.getMoveGroupId();
        try {
            IacDelUserGroupDTO cbbDelUserGroupDTO = new IacDelUserGroupDTO(groupId, moveGroupId);
            IacUserGroupDetailDTO[] cbbUserGroupDetailDTOArr = cbbUserGroupAPI.getAllUserGroup();
            IacUserGroupDetailDTO userGroupDTO = cbbUserGroupAPI.getUserGroupDetail(groupId);
            localGroupName = userGroupDTO.getName();

            // 是否拥有所有权限
            boolean isAllPermissions = permissionHelper.isAllGroupPermission(adminDTO.getId());

            // 没有全部终端组权限需增加终端组权限校验
            if (!isAllPermissions) {
                userGroupHelper.checkPermission(taskItem.getItemID(), Arrays.asList(cbbUserGroupDetailDTOArr), localGroupName, sessionContext);
            }

            cbbUserGroupAPI.deleteUserGroup(cbbDelUserGroupDTO);
            deleteGroupDesktopConfig(groupId, moveGroupId, cbbUserGroupDetailDTOArr);
            deleteRoleGroupPermission(groupId, moveGroupId, cbbUserGroupDetailDTOArr);
            rcaGroupMemberAPI.unbindAllAppGroup(groupId, RcaEnum.GroupMemberType.USER_GROUP, localGroupName);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DELETE_USER_GROUP_SUCCESS_LOG, new String[] {localGroupName});
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DELETE_USER_GROUP_SUCCESS_LOG).msgArgs(new String[] {localGroupName}).build();
        } catch (BusinessException e) {
            LOGGER.error("删除用户组失败", e);
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DELETE_USER_GROUP_FAIL_LOG, new String[] {localGroupName, exceptionMsg});
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DELETE_USER_GROUP_FAIL_LOG, e, localGroupName, exceptionMsg);
        } finally {
            groupName = localGroupName;
        }
    }

    private void deleteGroupDesktopConfig(UUID groupId, UUID moveGroupId, IacUserGroupDetailDTO[] cbbUserGroupDetailDTOArr) {
        List<UUID> deleteGroupIdList = findDeleteGroupIdList(groupId, moveGroupId, cbbUserGroupDetailDTOArr);
        deleteGroupIdList.stream().forEach(dto -> {
            userDesktopConfigAPI.deleteUserGroupDesktopConfig(dto, UserCloudDeskTypeEnum.IDV);
            userDesktopConfigAPI.deleteUserGroupDesktopConfig(dto, UserCloudDeskTypeEnum.VDI);
            // 删除VOI的云桌面配置
            userDesktopConfigAPI.deleteUserGroupDesktopConfig(dto, UserCloudDeskTypeEnum.VOI);
        });
    }

    private void deleteRoleGroupPermission(UUID groupId, UUID moveGroupId, IacUserGroupDetailDTO[] cbbUserGroupDetailDTOArr) {
        List<UUID> deleteGroupIdList = findDeleteGroupIdList(groupId, moveGroupId, cbbUserGroupDetailDTOArr);
        permissionHelper.deleteAdminGroupPermissionList(deleteGroupIdList, RoleGroupPermissionType.USER_GROUP);
    }

    private List<UUID> findDeleteGroupIdList(UUID groupId, UUID moveGroupId, IacUserGroupDetailDTO[] cbbUserGroupDetailDTOArr) {
        List<UUID> deleteGroupIdList = Lists.newArrayList();
        /** 只有移动到未分组，才删除组及底下组 */
        if (moveGroupId != null && moveGroupId.equals(IacUserGroupMgmtAPI.DEFAULT_USER_GROUP_ID)) {
            /** 从删除节点开始递归获取所有子孙节点进行递归删除 */
            List<IacUserGroupDetailDTO> parentGroupDTOList =
                    Arrays.stream(cbbUserGroupDetailDTOArr).filter(dto -> dto.getId().equals(groupId)).collect(Collectors.toList());
            getChildrenTreeNode(parentGroupDTOList, deleteGroupIdList, cbbUserGroupDetailDTOArr);
        } else {
            deleteGroupIdList.add(groupId);
        }
        return deleteGroupIdList;
    }

    private void getChildrenTreeNode(List<IacUserGroupDetailDTO> parentGroupDTOList, List<UUID> deleteGroupIdList,
                                     IacUserGroupDetailDTO[] cbbUserGroupDetailDTOArr) {
        deleteGroupIdList.addAll(parentGroupDTOList.stream().map(IacUserGroupDetailDTO::getId).collect(Collectors.toList()));
        List<IacUserGroupDetailDTO> childrenDTOList = Lists.newArrayList();
        parentGroupDTOList.stream().forEach(parentGroupDTO -> childrenDTOList.addAll(Arrays.stream(cbbUserGroupDetailDTOArr)
                .filter(dto -> parentGroupDTO.getId().equals(dto.getParentId())).collect(Collectors.toList())));
        if (!childrenDTOList.isEmpty()) {
            getChildrenTreeNode(childrenDTOList, deleteGroupIdList, cbbUserGroupDetailDTOArr);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量删除
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_USER_GROUP_DELETE_RESULT);
        }
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_USER_GROUP_DELETE_SUCCESS_RESULT)
                    .msgArgs(new String[] {groupName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_USER_GROUP_DELETE_FAIL_RESULT)
                    .msgArgs(new String[] {groupName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }
}
