package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.ruijie.rcos.gss.base.iac.module.dto.IacLoginUserDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WifiWhitelistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleGroupPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.request.DeleteWifiWhitelistRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.TerminalGroupHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbDeleteTerminalGroupDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
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
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

/**
 * Description: 删除单条终端升级包处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月08日
 *
 * @author nt
 */
public class DeleteTerminalGroupBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTerminalGroupBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserTerminalGroupMgmtAPI userTerminalGroupMgmtAPI;

    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    private PermissionHelper permissionHelper;

    private TerminalGroupHelper terminalGroupHelper;

    private SessionContext sessionContext;

    private String groupName;

    private WifiWhitelistAPI wifiWhitelistAPI;

    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    private IacLoginUserDTO adminDTO;


    public DeleteTerminalGroupBatchTaskHandler(CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI, PermissionHelper permissionHelper,
                                               BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI, UserTerminalMgmtAPI userTerminalMgmtAPI) {
        super(batchTaskItem);
        Assert.notNull(terminalGroupMgmtAPI, "terminalGroupMgmtAPI can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");
        Assert.notNull(permissionHelper, "permissionHelper can not be null");
        Assert.notNull(userTerminalMgmtAPI, "userTerminalMgmtAPI can not be null");

        this.auditLogAPI = auditLogAPI;
        this.terminalGroupMgmtAPI = terminalGroupMgmtAPI;
        this.permissionHelper = permissionHelper;
        this.userTerminalMgmtAPI = userTerminalMgmtAPI;
    }

    public void setAdminDTO(IacLoginUserDTO adminDTO) {
        this.adminDTO = adminDTO;
    }

    public void setWifiWhitelistAPI(WifiWhitelistAPI wifiWhitelistAPI) {
        this.wifiWhitelistAPI = wifiWhitelistAPI;
    }

    /**
     * 设置 userTerminalGroupMgmtAPI
     *
     * @param userTerminalGroupMgmtAPI userTerminalGroupMgmtAPI
     */
    public void setUserTerminalGroupMgmtAPI(UserTerminalGroupMgmtAPI userTerminalGroupMgmtAPI) {
        Assert.notNull(userTerminalGroupMgmtAPI, "userTerminalGroupMgmtAPI can not be null");
        this.userTerminalGroupMgmtAPI = userTerminalGroupMgmtAPI;
    }

    /**
     * 设置 sessionContext
     *
     * @param sessionContext sessionContext
     */
    public void setSessionContext(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext can not be null");
        this.sessionContext = sessionContext;
    }

    /**
     * 设置 terminalGroupHelper
     *
     * @param terminalGroupHelper terminalGroupHelper
     */
    public void setTerminalGroupHelper(TerminalGroupHelper terminalGroupHelper) {
        Assert.notNull(terminalGroupHelper, "terminalGroupHelper can not be null");
        this.terminalGroupHelper = terminalGroupHelper;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem不能为null");

        DeleteTerminalGroupBatchTaskItem deleteTaskItem = (DeleteTerminalGroupBatchTaskItem) taskItem;
        String localGroupName = taskItem.getItemID().toString();

        try {
            localGroupName = getTerminalGroupName(taskItem.getItemID());

            /** 获取删除前的组 */
            List<CbbTerminalGroupDetailDTO> terminalGroupDTOList = terminalGroupMgmtAPI.listTerminalGroup();
            // 是否拥有所有权限
            boolean isAllPermissions = permissionHelper.isAllGroupPermission(adminDTO.getId());

            // 没有全部终端组权限需增加终端组权限校验
            if (!isAllPermissions) {
                terminalGroupHelper.checkPermission(taskItem.getItemID(), terminalGroupDTOList, localGroupName, sessionContext);
            }

            CbbDeleteTerminalGroupDTO delRequest = new CbbDeleteTerminalGroupDTO();
            delRequest.setId(taskItem.getItemID());
            delRequest.setMoveGroupId(deleteTaskItem.getMoveGroupId());
            // 预先获取，将数据存在内存中
            List<UUID> deleteGroupIdList = getDeleteGroupIdList(deleteTaskItem.getItemID(), deleteTaskItem.getMoveGroupId(), terminalGroupDTOList);

            // 预先获取，白名单专属于IDV终端
            List<TerminalDTO> terminalList = getNeedNotifyTerminalList(deleteGroupIdList);
            LOGGER.info("获取通知终端集合：[{}]", JSONArray.toJSONString(terminalList));
            terminalGroupMgmtAPI.deleteTerminalGroup(delRequest);
            for (UUID id : deleteGroupIdList) {
                userTerminalGroupMgmtAPI.deleteTerminalGroupDesktopConfig(id);
            }
            permissionHelper.deleteAdminGroupPermissionList(deleteGroupIdList, RoleGroupPermissionType.TERMINAL_GROUP);
            
            // 当指定分组为总览时，通知改成未分组ID
            UUID moveTerminalGroupId = Optional.ofNullable(deleteTaskItem.getMoveGroupId()).orElse(CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID);
            DeleteWifiWhitelistRequest request = new DeleteWifiWhitelistRequest(taskItem.getItemID(), moveTerminalGroupId, terminalList);
            wifiWhitelistAPI.deleteWifiWhitelist(request);

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_DELETE_TERMINAL_GROUP_SUCCESS_LOG, localGroupName);

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_DELETE_TERMINAL_GROUP_RESULT_SUCCESS).msgArgs(new String[] {localGroupName}).build();
        } catch (BusinessException e) {
            LOGGER.error("delete terminal group[" + taskItem.getItemID() + "] fail", e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_DELETE_TERMINAL_GROUP_FAIL_LOG, e, localGroupName, e.getI18nMessage());
            String tip = terminalGroupHelper.getErrorTip(sessionContext, e, permissionHelper.isAllGroupPermission(sessionContext));
            throw new BusinessException(UserBusinessKey.RCDC_DELETE_TERMINAL_GROUP_RESULT_FAIL, e, localGroupName, tip);
        } finally {
            groupName = localGroupName;
        }
    }

    private List<TerminalDTO> getNeedNotifyTerminalList(List<UUID> groupIdList) {
        List<TerminalDTO> resultList = new ArrayList<>();
        for (UUID groupId : groupIdList) {
            List<TerminalDTO> terminalList =
                    userTerminalMgmtAPI.queryListByPlatformAndGroupIdAndState(CbbTerminalPlatformEnums.IDV, groupId, CbbTerminalStateEnums.ONLINE);
            if (!CollectionUtils.isEmpty(terminalList)) {
                resultList.addAll(terminalList);
            }
        }
        return resultList;
    }

    private List<UUID> getDeleteGroupIdList(UUID groupId, UUID moveGroupId, List<CbbTerminalGroupDetailDTO> terminalGroupDTOList) {
        List<UUID> deleteGroupIdList = new ArrayList<>();
        /** 只有移动到未分组，才删除组及底下组 */
        if (CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID.equals(moveGroupId)) {
            /** 从删除节点开始递归获取所有子孙节点进行递归删除 */
            for (CbbTerminalGroupDetailDTO dto : terminalGroupDTOList) {
                if (dto.getId().equals(groupId)) {
                    List<CbbTerminalGroupDetailDTO> parentGroupDTOList = new ArrayList<>();
                    parentGroupDTOList.add(dto);
                    getChildrenTreeNode(parentGroupDTOList, deleteGroupIdList, terminalGroupDTOList);
                    break;
                }
            }
        } else {
            deleteGroupIdList.add(groupId);
        }
        return deleteGroupIdList;
    }

    private void getChildrenTreeNode(List<CbbTerminalGroupDetailDTO> parentGroupDTOList, List<UUID> deleteGroupIdList,
            List<CbbTerminalGroupDetailDTO> terminalGroupDTOList) {
        List<CbbTerminalGroupDetailDTO> childrenDTOList = new ArrayList<>();

        for (CbbTerminalGroupDetailDTO parentGroupDTO : parentGroupDTOList) {
            deleteGroupIdList.add(parentGroupDTO.getId());
            for (CbbTerminalGroupDetailDTO childGroupDTO : terminalGroupDTOList) {
                if (parentGroupDTO.getId().equals(childGroupDTO.getParentGroupId())) {
                    childrenDTOList.add(childGroupDTO);
                }
            }
        }
        if (CollectionUtils.isEmpty(childrenDTOList)) {
            return;
        }
        getChildrenTreeNode(childrenDTOList, deleteGroupIdList, terminalGroupDTOList);
    }

    private String getTerminalGroupName(UUID id) {
        String localGroupName = id.toString();
        try {
            CbbTerminalGroupDetailDTO groupDTO = terminalGroupMgmtAPI.loadById(id);
            localGroupName = groupDTO.getGroupName();
        } catch (Exception e) {
            LOGGER.error("get terminal group label", e);
        }
        return localGroupName;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_DELETE_TERMINAL_GROUP_SUCCESS) //
                    .msgArgs(new String[] {groupName}) //
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(UserBusinessKey.RCDC_DELETE_TERMINAL_GROUP_FAIL) //
                    .msgArgs(new String[] {groupName}) //
                    .build();
        }
    }
}
