package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WifiWhitelistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.DeleteTerminalGroupBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.DeleteTerminalGroupBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.CreateTerminalGroupWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.DelTerminalGroupWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.EditTerminalGroupWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.ListTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.TerminalGroupIdWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.CheckTerminalGroupNameUniqueContentVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.TerminalGroupDetailVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.TerminalGroupVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.IdLabelStringEntry;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.CheckGroupNameUniqueWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.TerminalGroupHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbEditTerminalGroupDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupNameDuplicationDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * Description: 终端分组管理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 *
 * @author nt
 */
@Api(tags = "云办公客户端终端组管理")
@Controller
@RequestMapping("/rco/terminal/group/pc")
@EnableCustomValidate(enable = false)
public class PcTerminalGroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PcTerminalGroupController.class);

    @Autowired
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private TerminalGroupHelper terminalGroupHelper;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private UserTerminalGroupMgmtAPI userTerminalGroupMgmtAPI;

    @Autowired
    private WifiWhitelistAPI wifiWhitelistAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacAdminMgmtAPI iacAdminMgmtAPI;

    /**
     * 获取分组树形结构列表
     *
     * @param request 请求参数
     * @param sessionContext session信息
     * @return 直接下级分组列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("云办公客户端获取分组树形结构列表")
    @RequestMapping(value = "list")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，获取分组树形结构列表"})})
    public DefaultWebResponse list(ListTerminalGroupRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        List<CbbTerminalGroupDetailDTO> terminalGroupDTOList = terminalGroupMgmtAPI.listTerminalGroup();

        if (CollectionUtils.isEmpty(terminalGroupDTOList)) {
            return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", Collections.emptyList()));
        }

        UUID filterGroupId = null;
        boolean enableFilterDefaultGroup = false;
        if (request != null) {
            filterGroupId = request.getFilterGroupId();
            if (request.getEnableFilterDefaultGroup() != null) {
                enableFilterDefaultGroup = request.getEnableFilterDefaultGroup();
            }
        }

        TerminalGroupVO[] terminalGroupVOArr = buildTerminalGroupVO(terminalGroupDTOList);

        /** 判断是否不需要权限控制，用于管理员分配组权限的时候展示完整树 */
        Boolean isNoPermission = Objects.requireNonNull(request).getNoPermission();
        TerminalGroupVO[] resultTerminalGroupArr;

        if (isNoPermission != null && isNoPermission) {
            resultTerminalGroupArr = terminalGroupVOArr;
            refreshNoPermissionInfo(resultTerminalGroupArr);
        } else {
            if (permissionHelper.isAllGroupPermission(sessionContext)) {
                resultTerminalGroupArr = terminalGroupVOArr;
                refreshNoPermissionInfo(resultTerminalGroupArr);
            } else {
                ListTerminalGroupIdRequest listTerminalGroupRequest = new ListTerminalGroupIdRequest();
                listTerminalGroupRequest.setAdminId(sessionContext.getUserId());
                ListTerminalGroupIdResponse listTerminalGroupIdResponse =
                        adminDataPermissionAPI.listTerminalGroupIdByAdminId(listTerminalGroupRequest);
                List<String> hasPermissionUUIDList = listTerminalGroupIdResponse.getTerminalGroupIdList();
                TerminalGroupVO[] hasPermissionArr = filterTerminalGroupVOArr(terminalGroupVOArr, hasPermissionUUIDList);
                resultTerminalGroupArr = findTreeTerminalGroupVOArr(hasPermissionArr, terminalGroupVOArr);
            }
        }

        List<TerminalGroupVO> resultList =
                terminalGroupHelper.buildTerminalGroupTree(resultTerminalGroupArr, filterGroupId, enableFilterDefaultGroup);

        return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", resultList));
    }

    private void refreshNoPermissionInfo(TerminalGroupVO[] terminalGroupVOArr) {
        for (TerminalGroupVO vo : terminalGroupVOArr) {
            vo.setDisabled(false);
        }
    }

    /**
     * 将terminal API的组DTO转换成前台展示的组VO，并且加入ROOT节点
     *
     * @param terminalGroupDTOList
     * @return
     */
    private TerminalGroupVO[] buildTerminalGroupVO(List<CbbTerminalGroupDetailDTO> terminalGroupDTOList) {
        List<TerminalGroupVO> voList = new ArrayList<>();
        for (CbbTerminalGroupDetailDTO dto : terminalGroupDTOList) {
            TerminalGroupVO vo = new TerminalGroupVO();
            vo.setId(dto.getId().toString());
            vo.setLabel(dto.getGroupName());
            vo.setEnableDefault(dto.getEnableDefault());
            vo.setDisabled(true);
            if (dto.getParentGroupId() == null) {
                vo.setParentId(TerminalGroupHelper.TERMINAL_GROUP_ROOT_ID);
            } else {
                vo.setParentId(dto.getParentGroupId().toString());
            }
            voList.add(vo);
        }
        voList.add(addRootTerminalGroupVO());
        return voList.toArray(new TerminalGroupVO[voList.size()]);
    }

    private TerminalGroupVO addRootTerminalGroupVO() {
        TerminalGroupVO vo = new TerminalGroupVO();
        vo.setId(TerminalGroupHelper.TERMINAL_GROUP_ROOT_ID);
        vo.setLabel(TerminalGroupHelper.TERMINAL_GROUP_ROOT_NAME);
        vo.setParentId(null);
        vo.setDisabled(true);
        vo.setEnableDefault(false);
        return vo;
    }

    private TerminalGroupVO[] filterTerminalGroupVOArr(TerminalGroupVO[] terminalGroupVOArr, List<String> hasPermissionUUIDList) {
        if (CollectionUtils.isEmpty(hasPermissionUUIDList)) {
            return new TerminalGroupVO[0];
        }

        List<TerminalGroupVO> hasPermissionDTOList = new ArrayList<>();
        for (TerminalGroupVO vo : terminalGroupVOArr) {
            for (String id : hasPermissionUUIDList) {
                if (vo.getId().equals(id)) {
                    vo.setDisabled(false);
                    hasPermissionDTOList.add(vo);
                    break;
                }
            }
        }

        return hasPermissionDTOList.toArray(new TerminalGroupVO[hasPermissionDTOList.size()]);
    }

    private TerminalGroupVO[] findTreeTerminalGroupVOArr(TerminalGroupVO[] hasPermissionTerminalGroupVOArr, TerminalGroupVO[] allTerminalGroupVOArr) {
        List<TerminalGroupVO> resultList = new ArrayList<>();
        for (TerminalGroupVO vo : hasPermissionTerminalGroupVOArr) {
            List<TerminalGroupVO> treeNodeList = new ArrayList<>();
            treeNodeList.add(vo);
            recursiveTree(treeNodeList, vo, allTerminalGroupVOArr);
            resultList.addAll(treeNodeList);
        }
        return resultList.toArray(new TerminalGroupVO[resultList.size()]);
    }

    private void recursiveTree(List<TerminalGroupVO> treeNodeList, TerminalGroupVO vo, TerminalGroupVO[] allTerminalGroupVOArr) {
        if (vo.getParentId() == null) {
            return;
        }

        TerminalGroupVO parentNode = null;
        for (TerminalGroupVO terminalGroupVO : allTerminalGroupVOArr) {
            if (terminalGroupVO.getId().equals(vo.getParentId())) {
                parentNode = terminalGroupVO;
                break;
            }
        }
        if (!treeNodeList.contains(parentNode)) {
            treeNodeList.add(parentNode);
        }
        recursiveTree(treeNodeList, parentNode, allTerminalGroupVOArr);
    }


    /**
     * 检验终端分组名称是否同级唯一
     *
     * @param request 请求参数
     * @return 是否唯一结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("检验终端分组名称是否同级唯一")
    @RequestMapping(value = "checkNameUnique")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，云办公客户端 检验终端分组名称是否同级唯一"})})
    public DefaultWebResponse checkNameUnique(CheckGroupNameUniqueWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbTerminalGroupNameDuplicationDTO apiRequest =
                new CbbTerminalGroupNameDuplicationDTO(request.getId(), request.getParentGroupId(), request.getGroupName());
        boolean hasDuplication = terminalGroupMgmtAPI.checkUseGroupNameDuplication(apiRequest);

        return DefaultWebResponse.Builder.success(new CheckTerminalGroupNameUniqueContentVO(hasDuplication));
    }

    /**
     * 获取终端分组编辑详情
     *
     * @param request 请求参数
     * @return 终端分组详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取终端分组详情")
    @RequestMapping(value = "detail")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，终端分组详情"})})
    public DefaultWebResponse detail(TerminalGroupIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbTerminalGroupDetailDTO terminalGroupDTO = terminalGroupMgmtAPI.loadById(request.getId());
        return DefaultWebResponse.Builder.success(buildGroupDetailVO(terminalGroupDTO));
    }

    private TerminalGroupDetailVO buildGroupDetailVO(CbbTerminalGroupDetailDTO terminalGroupDTO) {
        IdLabelStringEntry idLabelEntry = new IdLabelStringEntry();
        if (terminalGroupDTO.getParentGroupId() == null) {

            idLabelEntry.setId(TerminalGroupHelper.TERMINAL_GROUP_ROOT_ID);
            idLabelEntry.setLabel(TerminalGroupHelper.TERMINAL_GROUP_ROOT_NAME);
        } else {
            idLabelEntry.setId(terminalGroupDTO.getParentGroupId().toString());
            idLabelEntry.setLabel(terminalGroupDTO.getParentGroupName());
        }
        TerminalGroupDetailVO detailVO = new TerminalGroupDetailVO();
        detailVO.setId(terminalGroupDTO.getId());
        detailVO.setGroupName(terminalGroupDTO.getGroupName());
        detailVO.setParentGroup(idLabelEntry);
        return detailVO;
    }

    /**
     * 创建终端分组
     *
     * @param request 请求参数
     * @param sessionContext session信息
     * @return 结果响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分组创建")
    @RequestMapping(value = "create")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，分组创建"})})

    @EnableAuthority
    public DefaultWebResponse create(CreateTerminalGroupWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        String groupName = request.getGroupName();
        UUID parentGroupId = request.getParentGroup() == null ? null : request.getParentGroup().getId();
        CbbTerminalGroupDTO createReq = new CbbTerminalGroupDTO(groupName, parentGroupId);
        LOGGER.info("create terminal group, group label [{}], parent group id [{}] ", groupName, parentGroupId);
        try {
            // 创建终端组
            CbbTerminalGroupDetailDTO terminalGroup = terminalGroupMgmtAPI.createTerminalGroup(createReq);
            // 保存管理员数据权限关联表
            saveRoleGroupPermission(sessionContext, groupName, parentGroupId);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_ADD_TERMINAL_GROUP_SUCCESS_LOG, groupName);
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, terminalGroup.getId());
        } catch (BusinessException ex) {
            LOGGER.error("create terminal group fail", ex);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_ADD_TERMINAL_GROUP_FAIL_LOG, groupName, ex.getI18nMessage());
            String tip = terminalGroupHelper.getErrorTip(sessionContext, ex, permissionHelper.isAllGroupPermission(sessionContext));
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, ex, tip);
        }
    }

    /**
     * 编辑终端分组
     *
     * @param request 请求参数
     * @param sessionContext session信息
     * @return 请求结果响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑终端组")
    @RequestMapping(value = "edit")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，编辑终端组"})})

    @EnableAuthority
    public DefaultWebResponse edit(EditTerminalGroupWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        UUID parentId = request.getParentGroup() == null ? null : request.getParentGroup().getId();
        Assert.hasText("parentId", "parent group id can not be blank");
        CbbEditTerminalGroupDTO editReq = new CbbEditTerminalGroupDTO(request.getId(), request.getGroupName(), parentId);
        LOGGER.info("edit terminal group, request info : {} ", request.toString());
        String groupName = getTerminalGroupName(request.getId());
        try {
            terminalGroupMgmtAPI.editTerminalGroup(editReq);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_EDIT_TERMINAL_GROUP_SUCCESS_LOG, groupName);
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            LOGGER.error("edit terminal group", e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_EDIT_TERMINAL_GROUP_FAIL_LOG, groupName, e.getI18nMessage());
            String tip = terminalGroupHelper.getErrorTip(sessionContext, e, permissionHelper.isAllGroupPermission(sessionContext));
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, tip);
        }
    }

    /**
     * 删除终端分组
     *
     * @param request 请求参数
     * @param sessionContext session信息
     * @param builder builder
     * @return 请求结果响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除终端组")
    @RequestMapping(value = "delete")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，删除终端组"})})

    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> delete(DelTerminalGroupWebRequest request, SessionContext sessionContext,
            BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");
        Assert.notNull(builder, "builder不能为null");

        return startDeleteTerminalGroupBatchTask(request, sessionContext, builder);
    }


    private CommonWebResponse<BatchTaskSubmitResult> startDeleteTerminalGroupBatchTask(DelTerminalGroupWebRequest request,
            SessionContext sessionContext, BatchTaskBuilder builder) throws BusinessException {
        DeleteTerminalGroupBatchTaskItem taskItem = new DeleteTerminalGroupBatchTaskItem(request.getIdArr()[0],
                LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_DELETE_ITEM_NAME), request.getMoveGroupId());
        DeleteTerminalGroupBatchTaskHandler handler =
                new DeleteTerminalGroupBatchTaskHandler(terminalGroupMgmtAPI, permissionHelper, taskItem, auditLogAPI, userTerminalMgmtAPI);
        handler.setWifiWhitelistAPI(wifiWhitelistAPI);
        handler.setSessionContext(sessionContext);
        handler.setTerminalGroupHelper(terminalGroupHelper);
        handler.setUserTerminalGroupMgmtAPI(userTerminalGroupMgmtAPI);
        handler.setAdminDTO(iacAdminMgmtAPI.getLoginUserInfo());
        BatchTaskSubmitResult submitResult = builder.setTaskName(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_DELETE_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_DELETE_TASK_DESC).registerHandler(handler).start();

        return CommonWebResponse.success(submitResult);
    }

    private void saveRoleGroupPermission(SessionContext sessionContext, String groupName, UUID parentGroupId) throws BusinessException {
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            CbbTerminalGroupDTO request = new CbbTerminalGroupDTO();
            request.setGroupName(groupName);
            request.setParentGroupId(parentGroupId);
            CbbTerminalGroupDetailDTO terminalGroupDTO = terminalGroupMgmtAPI.getByName(request);
            UUID groupId = terminalGroupDTO.getId();
            permissionHelper.saveAdminGroupPermission(sessionContext, groupId, AdminDataPermissionType.TERMINAL_GROUP);
        }
    }

    private String getTerminalGroupName(UUID id) {
        String groupName = id.toString();
        try {
            CbbTerminalGroupDetailDTO groupDTO = terminalGroupMgmtAPI.loadById(id);
            groupName = groupDTO.getGroupName();
        } catch (Exception e) {
            LOGGER.error("get terminal group label", e);
        }
        return groupName;
    }


}
