package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WifiWhitelistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvCreateTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvEditTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.IdvTerminalGroupDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.DeleteTerminalGroupBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.DeleteTerminalGroupBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.CreateIdvTerminalGroupWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.DelTerminalGroupWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.EditIdvTerminalGroupWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.ListTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common.UserConfigHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.IdLabelStringEntry;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.CheckGroupNameUniqueWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.TerminalGroupHelper;
import com.ruijie.rcos.rcdc.rco.module.web.wifi.vo.WifiWhitelistVO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
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
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Description: 终端分组管理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 *
 * @author nt
 */
@Api(tags = "IDV终端组管理")
@Controller
@RequestMapping("/rco/terminal/group/idv")
@EnableCustomValidate(enable = false)
public class IDVTerminalGroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IDVTerminalGroupController.class);

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
    private UserConfigHelper userConfigHelper;

    @Autowired
    private IacAdminMgmtAPI iacAdminMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 获取分组树形结构列表
     *
     * @param request 请求参数
     * @param sessionContext session信息
     * @return 直接下级分组列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("IDV|TCI终端分组树形结构列表")
    @RequestMapping(value = "list")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，IDV|TCI终端分组树形结构列表"})})
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
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，检验终端分组名称是否同级唯一"})})
    public DefaultWebResponse checkNameUnique(CheckGroupNameUniqueWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbTerminalGroupNameDuplicationDTO apiRequest =
                new CbbTerminalGroupNameDuplicationDTO(request.getId(), request.getParentGroupId(), request.getGroupName());
        boolean hasDuplication = terminalGroupMgmtAPI.checkUseGroupNameDuplication(apiRequest);

        return DefaultWebResponse.Builder.success(new CheckTerminalGroupNameUniqueContentVO(hasDuplication));
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
     * @apiIgnore
     * @api {POST} /rco/terminal/group/idv/create
     * @apiName /idv/create
     * @apiGroup /rco/terminal/group
     * @apiDescription 创建idv终端组
     * @apiParam (请求体字段说明) {IdvDesktopConfigVO} idvDesktopConfig 镜像模板
     * @apiParam (请求体字段说明) {IdLabelEntry} idvDesktopConfig.strategy 策略
     * @apiParam (请求体字段说明) {UUID} idvDesktopConfig.strategy.id
     * @apiParam (请求体字段说明) {String} idvDesktopConfig.strategy.label
     * @apiParam (请求体字段说明) {IdLabelEntry} idvDesktopConfig.image 镜像
     * @apiParam (请求体字段说明) {UUID} idvDesktopConfig.image.id
     * @apiParam (请求体字段说明) {String} idvDesktopConfig.image.label
     * @apiParam (请求体字段说明) {WifiWhitelistConfigVO} idvWifiWhitelistConfig wifi白名单信息
     * @apiParam (请求体字段说明) {List} idvWifiWhitelistConfig.wifiWhitelist 白名单列表
     * @apiParam (请求体字段说明) {String} idvWifiWhitelistConfig.wifiWhitelist.ssid
     * @apiParam (请求体字段说明) {Integer} idvWifiWhitelistConfig.wifiWhitelist.index
     * @apiParam (请求体字段说明) {String} groupName
     * @apiParam (请求体字段说明) {IdLabelEntry} parentGroup
     * @apiParam (请求体字段说明) {UUID} parentGroup.id
     * @apiParam (请求体字段说明) {String} parentGroup.label
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "groupName":"groupName",
     *                  "idvDesktopConfig":{
     *                  "image":{
     *                  "id":"3daf6fc8-28c8-4ba2-af39-89ff0e6d38d6",
     *                  "label":"label"
     *                  },
     *                  "strategy":{
     *                  "id":"3daf6fc8-28c8-4ba2-af39-89ff0e6d38d6",
     *                  "label":"label"
     *                  }
     *                  },
     *                  "idvWifiWhitelistConfig":{
     *                  "wifiWhitelist":[
     *                  {
     *                  "index":1,
     *                  "ssid":"ssid"
     *                  }
     *                  ]
     *                  },
     *                  "parentGroup":{
     *                  "id":"3daf6fc8-28c8-4ba2-af39-89ff0e6d38d6",
     *                  "label":"label"
     *                  }
     *                  }
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "message":"创建idv终端组[{0}]成功",
     *                    "msgArgArr":[],
     *                    "msgKey":"rcdc_rco_terminal_group_add_idv_terminal_group_success_log",
     *                    "status":"SUCCESS"
     *                    }
     * @apiErrorExample {json} 异常响应
     *                  {
     *                  "message":"创建idv终端组[{0}]失败，失败原因：{1}",
     *                  "msgArgArr":[],
     *                  "msgKey":"rcdc_rco_terminal_group_add_idv_terminal_group_fail_log",
     *                  "status":"ERROR"
     *                  }
     */
    /**
     * idv终端分子
     * 
     * @param request CreateIdvTerminalGroupWebRequest
     * @param sessionContext SessionContext
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("idv分组创建")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，idv分组创建"})})
    @EnableAuthority
    public CommonWebResponse createIdv(CreateIdvTerminalGroupWebRequest request,
                                       SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        //先校验UPM策略是否适合
        userConfigHelper.checkUserProfile(request.getIdvDesktopConfig());
        userConfigHelper.checkUserProfile(request.getVoiDesktopConfig());

        if (Objects.nonNull(request.getIdvWifiWhitelistConfig())) {
            request.getIdvWifiWhitelistConfig().verify();
        }

        IdvCreateTerminalGroupRequest createReq = CreateIdvTerminalGroupWebRequest.convertFor(request);
        UUID parentGroupId = request.getParentGroup() == null ? null : request.getParentGroup().getId();
        String groupName = request.getGroupName();
        try {
            UUID idvTerminalGroupId = userTerminalGroupMgmtAPI.createIdvTerminalGroup(createReq);
            saveRoleGroupPermission(sessionContext, groupName, parentGroupId);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_ADD_IDV_TERMINAL_GROUP_SUCCESS_LOG, groupName);
            return CommonWebResponse.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, idvTerminalGroupId);
        } catch (BusinessException ex) {
            LOGGER.error("create idv terminal group fail", ex);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_ADD_IDV_TERMINAL_GROUP_FAIL_LOG, groupName, ex.getI18nMessage());
            return CommonWebResponse.fail(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, new String[] {ex.getI18nMessage()});
        }
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

    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/group/idv/edit
     * @apiName /idv/edit
     * @apiGroup /rco/terminal/group
     * @apiDescription 编辑idv终端分组
     * @apiParam (请求体字段说明) {IdvDesktopConfigVO} idvDesktopConfig 镜像模板，网络策略，云桌面策略,DHCP
     * @apiParam (请求体字段说明) {IdLabelEntry} idvDesktopConfig.strategy 策略
     * @apiParam (请求体字段说明) {UUID} idvDesktopConfig.strategy.id
     * @apiParam (请求体字段说明) {String} idvDesktopConfig.strategy.label
     * @apiParam (请求体字段说明) {IdLabelEntry} idvDesktopConfig.image 镜像
     * @apiParam (请求体字段说明) {UUID} idvDesktopConfig.image.id
     * @apiParam (请求体字段说明) {String} idvDesktopConfig.image.label
     * @apiParam (请求体字段说明) {EditWifiWhitelistConfigVO} idvWifiWhitelistConfig 白名单配置信息
     * @apiParam (请求体字段说明) {Boolean} idvWifiWhitelistConfig.needApplyToSubgroup 是否应用下组
     * @apiParam (请求体字段说明) {List} idvWifiWhitelistConfig.wifiWhitelist 白名单列表
     * @apiParam (请求体字段说明) {String} idvWifiWhitelistConfig.wifiWhitelist.ssid
     * @apiParam (请求体字段说明) {Integer} idvWifiWhitelistConfig.wifiWhitelist.index
     * @apiParam (请求体字段说明) {UUID} id 终端分组id
     * @apiParam (请求体字段说明) {String} groupName 分组名称
     * @apiParam (请求体字段说明) {IdLabelEntry} parentGroup 父级分组id
     * @apiParam (请求体字段说明) {UUID} parentGroup.id
     * @apiParam (请求体字段说明) {String} parentGroup.label
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "groupName":"groupName",
     *                  "id":"6568542e-5e45-4087-8cee-56ae566df4e9",
     *                  "idvDesktopConfig":{
     *                  "image":{
     *                  "id":"6568542e-5e45-4087-8cee-56ae566df4e9",
     *                  "label":"label"
     *                  },
     *                  "strategy":{
     *                  "id":"6568542e-5e45-4087-8cee-56ae566df4e9",
     *                  "label":"label"
     *                  }
     *                  },
     *                  "idvWifiWhitelistConfig":{
     *                  "needApplyToSubgroup":false,
     *                  "wifiWhitelist":[
     *                  {
     *                  "index":1,
     *                  "ssid":"ssid"
     *                  }
     *                  ]
     *                  },
     *                  "parentGroup":{
     *                  "id":"6568542e-5e45-4087-8cee-56ae566df4e9",
     *                  "label":"label"
     *                  }
     *                  }
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "message":"操作成功",
     *                    "msgArgArr":[],
     *                    "msgKey":"rcdc_rco_module_operate_success",
     *                    "status":"SUCCESS"
     *                    }
     * @apiErrorExample {json} 异常响应
     *                  {
     *                  "message":"操作失败，失败原因：{}",
     *                  "msgArgArr":[],
     *                  "msgKey":"rcdc_rco_module_operate_fail",
     *                  "status":"ERROR"
     *                  }
     */
    /**
     * 编辑分组
     * 
     * @param request EditIdvTerminalGroupWebRequest
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("idv分组编辑")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，idv编辑终端组"})})
    @EnableAuthority
    public CommonWebResponse editIdv(EditIdvTerminalGroupWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        if (Objects.nonNull(request.getIdvWifiWhitelistConfig())) {
            request.getIdvWifiWhitelistConfig().verify();
        }

        IdvEditTerminalGroupRequest editReq = EditIdvTerminalGroupWebRequest.convertFor(request);
        LOGGER.info("edit idv terminal group, request info : {} ", request.toString());
        String groupName = getTerminalGroupName(request.getId());
        try {
            userTerminalGroupMgmtAPI.editIdvTerminalGroup(editReq);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_EDIT_IDV_TERMINAL_GROUP_SUCCESS_LOG, groupName);
            return CommonWebResponse.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            LOGGER.error("edit idv terminal group", e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_GROUP_EDIT_IDV_TERMINAL_GROUP_FAIL_LOG, groupName, e.getI18nMessage());
            return CommonWebResponse.fail(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, new String[] {e.getI18nMessage()});
        }
    }

    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/group/idv/delete
     * @apiName /idv/delete
     * @apiGroup /rco/terminal/group
     * @apiDescription 删除idv终端组
     * @apiParam (请求体字段说明) {UUID[]} idArr 分组id数组集合
     * @apiParam (请求体字段说明) {UUID} moveGroupId 目标分组ID
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "idArr":["a01737c5-ab95-4169-a9e5-dbba3f4cc460"],
     *                  "moveGroupId":"a01737c5-ab95-4169-a9e5-dbba3f4cc460"
     *                  }
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content":{
     *                    "taskDesc":"正在删除终端组",
     *                    "taskId":"c26372df-f6e0-4201-ad1c-83d105a747ff",
     *                    "taskName":"删除终端组",
     *                    "taskStatus":"PROCESSING"
     *                    },
     *                    "message":"",
     *                    "msgArgArr":[],
     *                    "msgKey":"rcdc_rco_module_operate_success",
     *                    "status":"SUCCESS"
     *                    }
     */
    /**
     * 终端删除
     * 
     * @param request DelTerminalGroupWebRequest
     * @param sessionContext SessionContext
     * @param builder BatchTaskBuilder
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("idv分组删除")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，idv删除终端组"})})

    @EnableAuthority
    public CommonWebResponse deleteIdv(DelTerminalGroupWebRequest request,
                                       SessionContext sessionContext, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");
        Assert.notNull(builder, "builder can not be null");

        verifyDelTerminalGroupWebRequest(request);


        return startDeleteTerminalGroupBatchTask(request, sessionContext, builder);
    }

    private void verifyDelTerminalGroupWebRequest(DelTerminalGroupWebRequest request) throws BusinessException {

        // 校验移动分组是否存在，总览为null
        if (request.getMoveGroupId() != null) {
            terminalGroupMgmtAPI.loadById(request.getMoveGroupId());
        }
        // 校验删除分组是否存在
        for (UUID id : request.getIdArr()) {
            terminalGroupMgmtAPI.loadById(id);
        }
    }

    private CommonWebResponse<BatchTaskSubmitResult> startDeleteTerminalGroupBatchTask(
            DelTerminalGroupWebRequest request, SessionContext sessionContext, BatchTaskBuilder builder) throws BusinessException {
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

    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/group/idv/detail
     * @apiName /idv/detail
     * @apiGroup /rco/terminal/group
     * @apiDescription 获取idv终端分组详情
     * @apiParam (请求体字段说明) {UUID} id
     * @apiParamExample {json} 请求体示例
     *                  {
     *                  "id":"032b5193-98bd-4c01-b401-b8c2b1e90a73"
     *                  }
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "content":{
     *                    "groupName": "groupName",
     *                    "id": "032b5193-98bd-4c01-b401-b8c2b1e90a73",
     *                    "idvDesktopConfig": {
     *                    "image": {
     *                    "id": "032b5193-98bd-4c01-b401-b8c2b1e90a73",
     *                    "label": "label"
     *                    },
     *                    "strategy": {
     *                    "id": "032b5193-98bd-4c01-b401-b8c2b1e90a73",
     *                    "label": "label"
     *                    }
     *                    },
     *                    "idvWifiWhitelistConfig":{
     *                    "wifiWhitelist": [
     *                    {
     *                    "index": 1,
     *                    "ssid": "ssid"
     *                    }
     *                    ],
     *                    },
     *                    "parentGroup": {
     *                    "id": "id",
     *                    "label": "label"
     *                    }
     *                    },
     *                    "message":"",
     *                    "msgArgArr":[],
     *                    "msgKey":"rcdc_rco_module_operate_success",
     *                    "status":"SUCCESS"
     *                    }
     * @apiErrorExample {json} 异常响应
     *                  {
     *                  "message":"操作失败，失败原因：{}",
     *                  "msgArgArr":[],
     *                  "msgKey":"rcdc_rco_module_operate_fail",
     *                  "status":"ERROR"
     *                  }
     */
    /**
     * 终端分组详情
     * 
     * @param request IdWebRequest
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("idv分组详情")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，idv分组详情"})})
    public CommonWebResponse<IdvTerminalGroupDetailVO> idvDetail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        try {
            IdvTerminalGroupDetailResponse detailResponse = userTerminalGroupMgmtAPI.idvTerminalGroupDetail(request.getId());
            return CommonWebResponse.success(buildIdvTerminalGroupDetailVO(detailResponse));
        } catch (BusinessException ex) {
            return CommonWebResponse.fail(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, new String[] {ex.getI18nMessage()});
        }
    }

    private IdvTerminalGroupDetailVO buildIdvTerminalGroupDetailVO(IdvTerminalGroupDetailResponse detailResponse) {

        IdvTerminalGroupDetailVO detailVO = new IdvTerminalGroupDetailVO();
        // 构造终端组信息
        LOGGER.info("构造终端组信息{}", detailVO);
        buildTerminalGroupVO(detailResponse, detailVO);
        // 构建IDV终端配置
        LOGGER.info("构建IDV终端配置", detailVO);
        buildIDVTerminalGroupConfigVO(detailResponse, detailVO);
        // 构建VOI终端配置
        LOGGER.info("构建VOI终端配置", detailVO);
        buildVOITerminalGroupConfigVO(detailResponse, detailVO);
        // 白名单相关填充
        WifiWhitelistConfigDTO wifiWhitelistConfigDTO = detailResponse.getWifiWhitelistConfigDTO();
        List<WifiWhitelistVO> whitelistVoList = wifiWhitelistConfigDTO.getWifiWhiteList().stream()
                .map(item -> new WifiWhitelistVO(item.getSsid(), item.getIndex())).collect(Collectors.toList());
        WifiWhitelistConfigVO whitelistConfigVO = new WifiWhitelistConfigVO();
        whitelistConfigVO.setWifiWhiteList(whitelistVoList);
        detailVO.setIdvWifiWhitelistConfig(whitelistConfigVO);
        return detailVO;
    }

    /**
     * 构建终端信息
     * 
     * @param detailResponse
     * @param detailVO
     * @return
     */
    private IdvTerminalGroupDetailVO buildTerminalGroupVO(IdvTerminalGroupDetailResponse detailResponse, IdvTerminalGroupDetailVO detailVO) {
        detailVO.setId(detailResponse.getId());
        detailVO.setGroupName(detailResponse.getName());
        IdLabelStringEntry idLabelEntry = new IdLabelStringEntry();
        if (detailResponse.getParentId() == null) {
            idLabelEntry.setId(TerminalGroupHelper.TERMINAL_GROUP_ROOT_ID);
            idLabelEntry.setLabel(TerminalGroupHelper.TERMINAL_GROUP_ROOT_NAME);
        } else {
            idLabelEntry.setId(detailResponse.getParentId().toString());
            idLabelEntry.setLabel(detailResponse.getParentName());
        }
        detailVO.setParentGroup(idLabelEntry);
        return detailVO;
    }

    /**
     * 构建IDV终端配置
     * 
     * @param detailResponse
     * @param detailVO
     * @return
     */
    private IdvTerminalGroupDetailVO buildIDVTerminalGroupConfigVO(IdvTerminalGroupDetailResponse detailResponse, IdvTerminalGroupDetailVO detailVO) {
        IdvDesktopConfigVO configVO = new IdvDesktopConfigVO();
        // IDV 镜像目标是否存在
        UUID imageId = detailResponse.getIdvDesktopImageId();
        if (imageId != null) {
            String imageName = detailResponse.getIdvDesktopImageName();
            UUID strategyId = detailResponse.getIdvDesktopStrategyId();
            String strategyName = detailResponse.getIdvDesktopStrategyName();
            configVO.setImage(IdLabelEntry.build(imageId, imageName));
            configVO.setStrategy(IdLabelEntry.build(strategyId, strategyName));
            if (detailResponse.getIdvSoftwareStrategyId() != null) {
                configVO.setSoftwareStrategy(IdLabelEntry.build(detailResponse.getIdvSoftwareStrategyId(),
                        detailResponse.getIdvSoftwareStrategyName()));
            }
            UUID userProfileStrategyId = detailResponse.getIdvUserProfileStrategyId();
            if (userProfileStrategyId != null) {
                configVO.setUserProfileStrategy(IdLabelEntry.build(userProfileStrategyId, detailResponse.getIdvUserProfileStrategyName()));
            }
        }
        detailVO.setIdvDesktopConfig(configVO);
        return detailVO;
    }

    /**
     * 构建VOI终端配置
     * 
     * @param detailResponse
     * @param detailVO
     * @return
     */
    private IdvTerminalGroupDetailVO buildVOITerminalGroupConfigVO(IdvTerminalGroupDetailResponse detailResponse, IdvTerminalGroupDetailVO detailVO) {
        IdvDesktopConfigVO voiConfigVO = new IdvDesktopConfigVO();
        // VOI 镜像目标是否存在
        UUID voiImageId = detailResponse.getVoiDesktopImageId();
        if (voiImageId != null) {
            String imageName = detailResponse.getVoiDesktopImageName();
            UUID strategyId = detailResponse.getVoiDesktopStrategyId();
            String strategyName = detailResponse.getVoiDesktopStrategyName();
            voiConfigVO.setImage(IdLabelEntry.build(voiImageId, imageName));
            voiConfigVO.setStrategy(IdLabelEntry.build(strategyId, strategyName));
            if (detailResponse.getVoiSoftwareStrategyId() != null) {
                voiConfigVO.setSoftwareStrategy(IdLabelEntry.build(detailResponse.getVoiSoftwareStrategyId(),
                        detailResponse.getVoiSoftwareStrategyName()));
            }
            UUID userProfileStrategyId = detailResponse.getVoiUserProfileStrategyId();
            if (userProfileStrategyId != null) {
                voiConfigVO.setUserProfileStrategy(IdLabelEntry.build(userProfileStrategyId, detailResponse.getVoiUserProfileStrategyName()));
            }
        }
        // 设置VOI 信息
        detailVO.setVoiDesktopConfig(voiConfigVO);
        return detailVO;
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
