package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolComputerMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.service.tree.TreeBuilder;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.ListTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.TerminalGroupVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.PublicBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/02/05
 *
 * @author xiejian
 */
@Service
public class TerminalGroupHelper {

    public static final String TERMINAL_GROUP_ROOT_ID = "root";

    public static final String TERMINAL_GROUP_ROOT_NAME = "总览";

    @Autowired
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private DesktopPoolComputerMgmtAPI desktopPoolComputerMgmtAPI;

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalGroupHelper.class);

    /**
     * 构建用户组树形结构
     *
     * @param terminalGroupVOArr       要构建的数据源
     * @param filterGroupId            要过滤掉的组id
     * @param enableFilterDefaultGroup 是否过滤掉默认未分组
     * @return 返回树结构
     */
    public List<TerminalGroupVO> buildTerminalGroupTree(TerminalGroupVO[] terminalGroupVOArr,
                                                        @Nullable UUID filterGroupId, Boolean enableFilterDefaultGroup) {
        Assert.notNull(terminalGroupVOArr, "terminalGroupVOArr cannot null");
        Assert.notNull(enableFilterDefaultGroup, "enableFilterDefaultGroup cannot null");

        List<TerminalGroupVO> terminalGroupVOList = filterTerminalGroup(terminalGroupVOArr, filterGroupId,
                enableFilterDefaultGroup);
        return new TreeBuilder<>(terminalGroupVOList).build();
    }

    private List<TerminalGroupVO> filterTerminalGroup(TerminalGroupVO[] terminalGroupVOArr, UUID filterGroupId,
                                                      boolean enableFilterDefaultGroup) {
        List<TerminalGroupVO> terminalGroupVOList = new ArrayList<>();

        // 记录默认分组（未分组）
        TerminalGroupVO defaultGroupVO = null;
        for (TerminalGroupVO terminalGroupVO : terminalGroupVOArr) {
            if (filterGroupId != null && filterGroupId.toString().equals(terminalGroupVO.getId())) {
                // 过滤指定组
                continue;
            }
            if (enableFilterDefaultGroup && terminalGroupVO.getId()
                    .equals(CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID.toString())) {
                // 过滤默认组
                continue;
            }
            if (CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID.toString().equals(terminalGroupVO.getId())) {
                defaultGroupVO = terminalGroupVO;
                continue;
            }
            terminalGroupVOList.add(terminalGroupVO);
        }
        if (defaultGroupVO != null) {
            // 默认未分组置底
            terminalGroupVOList.add(defaultGroupVO);
        }

        return terminalGroupVOList;
    }

    /**
     * 生成异常提示
     *
     * @param sessionContext       sessionContext
     * @param e                    异常
     * @param isAllGroupPermission isAllGroupPermission
     * @return 提示信息
     */
    public String getErrorTip(SessionContext sessionContext, BusinessException e, boolean isAllGroupPermission) {
        Assert.notNull(sessionContext, "sessionContext can not be null");
        Assert.notNull(e, "BusinessException can not be null");
        Assert.notNull(e.getKey(), "BusinessException key can not be null");

        String tip = e.getI18nMessage();
        if (!isAllGroupPermission) {
            if (e.getKey().equals(PublicBusinessKey.RCDC_TERMINALGROUP_GROUP_NUM_EXCEED_LIMIT)) {
                tip = LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_TERMINALGROUP_GROUP_NUM_EXCEED_LIMIT_FOR_SYSADMIN, e.getI18nMessage());
            }
            if (e.getKey().equals(PublicBusinessKey.RCDC_TERMINALGROUP_SUB_GROUP_NUM_EXCEED_LIMIT)) {
                tip = LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_TERMINALGROUP_SUB_GROUP_NUM_EXCEED_LIMIT_FOR_SYSADMIN, e.getI18nMessage());
            }
            if (e.getKey().equals(PublicBusinessKey.RCDC_TERMINALGROUP_GROUP_NAME_DUPLICATE)) {
                tip = LocaleI18nResolver.resolve(
                        TerminalBusinessKey.RCDC_TERMINALGROUP_GROUP_NAME_DUPLICATE_FOR_SYSADMIN,
                        e.getI18nMessage());
            }
            if (e.getKey().equals(PublicBusinessKey.RCDC_DELETE_TERMINAL_GROUP_SUB_GROUP_HAS_DUPLICATION_WITH_MOVE_GROUP)) {
                tip = LocaleI18nResolver.resolve(
                        TerminalBusinessKey.RCDC_DELETE_TERMINAL_GROUP_SUB_GROUP_HAS_DUPLICATION_WITH_MOVE_GROUP_FOR_SYSADMIN, e.getI18nMessage());
            }
        }
        return tip;
    }


    /**
     * 获取孩子节点
     *
     * @param parentGroupDTOList   parentGroupDTOList
     * @param deleteGroupIdList    deleteGroupIdList
     * @param terminalGroupDTOList terminalGroupDTOList
     */
    public void getChildrenTreeNode(List<CbbTerminalGroupDetailDTO> parentGroupDTOList, List<UUID> deleteGroupIdList,
                                    List<CbbTerminalGroupDetailDTO> terminalGroupDTOList) {

        Assert.notNull(parentGroupDTOList, "parentGroupDTOList cannot null");
        Assert.notNull(deleteGroupIdList, "deleteGroupIdList cannot null");
        Assert.notNull(terminalGroupDTOList, "terminalGroupDTOList cannot null");

        List<CbbTerminalGroupDetailDTO> childrenDTOList = new ArrayList<>();
        // 遍历父节点
        for (CbbTerminalGroupDetailDTO parentGroupDTO : parentGroupDTOList) {
            deleteGroupIdList.add(parentGroupDTO.getId());
            terminalGroupDTOList.forEach(childGroupDTO -> {
                if (parentGroupDTO.getId().equals(childGroupDTO.getParentGroupId())) {
                    childrenDTOList.add(childGroupDTO);
                }
            });
        }
        if (CollectionUtils.isEmpty(childrenDTOList)) {
            return;
        }
        getChildrenTreeNode(childrenDTOList, deleteGroupIdList, terminalGroupDTOList);
    }

    /**
     * @param groupId              终端组编码
     * @param terminalGroupDTOList 终端组列表
     * @param localGroupName       终端组名称
     * @param sessionContext       会话信息
     * @throws BusinessException 业务异常
     */
    public void checkPermission(UUID groupId, List<CbbTerminalGroupDetailDTO> terminalGroupDTOList, String localGroupName,
                                SessionContext sessionContext) throws BusinessException {
        Assert.notNull(groupId, "groupId cannot null");
        Assert.notNull(terminalGroupDTOList, "terminalGroupDTOList cannot null");
        Assert.notNull(localGroupName, "localGroupName cannot null");
        Assert.notNull(sessionContext, "sessionContext cannot null");

        List<UUID> deleteGroupIdList = new ArrayList<>();
        StringBuilder tip = new StringBuilder();
        for (CbbTerminalGroupDetailDTO dto : terminalGroupDTOList) {
            if (dto.getId().equals(groupId)) {
                List<CbbTerminalGroupDetailDTO> parentGroupDTOList = new ArrayList<>();
                parentGroupDTOList.add(dto);
                getChildrenTreeNode(parentGroupDTOList, deleteGroupIdList, terminalGroupDTOList);
                break;
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(" 管理员ID:{}将删除的终端组集合为{}", sessionContext.getUserId(), JSON.toJSONString(deleteGroupIdList));
        }

        // 获取拥有权限的终端组
        ListTerminalGroupIdRequest listTerminalGroupIdRequest = new ListTerminalGroupIdRequest();
        listTerminalGroupIdRequest.setAdminId(sessionContext.getUserId());
        ListTerminalGroupIdResponse listTerminalGroupIdResponse = adminDataPermissionAPI.listTerminalGroupIdByAdminId(listTerminalGroupIdRequest);
        if (listTerminalGroupIdResponse == null) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_ADMIN_NOT_HAS_TERMINAL_GROUP_PERMISSION_FOR_MANAGE, sessionContext.getUserName(),
                    localGroupName);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("管理员ID:{}拥有的终端组集合为{}", sessionContext.getUserId(), //
                    JSON.toJSONString(listTerminalGroupIdResponse.getTerminalGroupIdList()));
        }
        for (UUID id : deleteGroupIdList) {
            if (!listTerminalGroupIdResponse.getTerminalGroupIdList().contains(id.toString())) {
                tip.append("," + getTerminalGroupName(id));
            }
        }
        String resultTip = tip.toString();
        if (StringUtils.isNotBlank(resultTip)) {
            String checkPermissionTip = StringUtils.substring(resultTip, 1);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_ADMIN_NOT_HAS_BELONG_TERMINAL_GROUP_PERMISSION_FOR_MANAGE,
                    sessionContext.getUserName(), localGroupName, checkPermissionTip);
        }

    }

    /**
     * 获取终端组名称
     *
     * @param id uuid
     * @return 名称
     */
    public String getTerminalGroupName(UUID id) {
        Assert.notNull(id, "id cannot null");

        String localGroupName = id.toString();
        try {
            CbbTerminalGroupDetailDTO groupDTO = terminalGroupMgmtAPI.loadById(id);
            localGroupName = groupDTO.getGroupName();
        } catch (Exception e) {
            LOGGER.error("get terminal group label", e);
        }
        return localGroupName;
    }

    /**
     * 添加池的分配信息
     *
     * @param request                参数
     * @param resultTerminalGroupArr resultTerminalGroupArr
     */
    public void addPoolAssignInfo(ListTerminalGroupRequest request, TerminalGroupVO[] resultTerminalGroupArr) {
        Assert.notNull(request, "request cannot null");
        Assert.notEmpty(resultTerminalGroupArr, "resultTerminalGroupArr cannot empty");
        List<TerminalGroupVO> groupVOList = Arrays.asList(resultTerminalGroupArr);
        if (CollectionUtils.isEmpty(groupVOList)) {
            return;
        }

        // 2.已经分配到池的组集合
        List<DesktopPoolComputerDTO> allAssignedGroupList = desktopPoolComputerMgmtAPI.
                listDeskPoolComputerByRelatedType(ComputerRelatedType.COMPUTER_GROUP);
        List<DesktopPoolComputerDTO> assignedGroupList =
                allAssignedGroupList.stream().filter(computerDTO -> Objects.equals(request.getDesktopPoolId(),
                        computerDTO.getDesktopPoolId())).collect(Collectors.toList());
        Set<String> assignedGroupSet = assignedGroupList.stream().map(item -> item.getRelatedId().toString()).collect(Collectors.toSet());

        List<DesktopPoolComputerDTO> disableGroupList =
                allAssignedGroupList.stream().filter(computerDTO ->
                        !Objects.equals(request.getDesktopPoolId(), computerDTO.getDesktopPoolId())).collect(Collectors.toList());
        Set<String> disableGroupSet = disableGroupList.stream().map(item -> item.getRelatedId().toString()).collect(Collectors.toSet());

        for (TerminalGroupVO terminalGroupVO : resultTerminalGroupArr) {
            terminalGroupVO.setAssigned(assignedGroupSet.contains(terminalGroupVO.getId()));
            terminalGroupVO.setDisabled(disableGroupSet.contains(terminalGroupVO.getId()) || terminalGroupVO.isDisabled());
        }
    }

    /**
     * 过滤池未关联的终端组
     *
     * @param request                请求
     * @param resultTerminalGroupArr resultTerminalGroupArr
     * @return 过滤后的用户组
     */
    public TerminalGroupVO[] filterPoolNotRelaGroupVOArr(ListTerminalGroupRequest request, TerminalGroupVO[] resultTerminalGroupArr) {
        Assert.notNull(request, "request cannot null");
        Assert.notEmpty(resultTerminalGroupArr, "resultTerminalGroupArr cannot empty");
        // 关联的终端组
        Set<String> relationUserGroup = new HashSet<>();
        if (Objects.nonNull(request.getDesktopPoolId())) {
            relationUserGroup = desktopPoolComputerMgmtAPI.getDesktopPoolRelationTerminalGroup(request.getDesktopPoolId());
        }
        List<TerminalGroupVO> hasPermissionDTOList = new ArrayList<>();
        for (TerminalGroupVO terminalGroupVO : resultTerminalGroupArr) {
            if (relationUserGroup.contains(terminalGroupVO.getId()) || TERMINAL_GROUP_ROOT_ID.equals(terminalGroupVO.getId())) {
                terminalGroupVO.setDisabled(false);
                hasPermissionDTOList.add(terminalGroupVO);
            }
        }
        return hasPermissionDTOList.toArray(new TerminalGroupVO[0]);
    }
}
