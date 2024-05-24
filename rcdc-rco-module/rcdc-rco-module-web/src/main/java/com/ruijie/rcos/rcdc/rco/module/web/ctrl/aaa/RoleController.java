package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacGetAdminPageByRoleRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.role.IacGetRolePageRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PermissionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.role.GetRoleWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo.RoleVO;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.SessionContextRegistry;
import com.ruijie.rcos.rcdc.rco.module.web.validation.RoleCustomValidation;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.match.FuzzyMatch;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/13 19:10
 *
 * @author linrenjian
 */
@Api(tags = "角色模块")
@Controller
@RequestMapping("/rco/role")
@EnableCustomValidate(validateClass = RoleCustomValidation.class)
public class RoleController {

    /**
     *日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);


    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    /**
     * 角色API
     */
    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    /**
     * 日志
     */
    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    /**
     * RCDC 服务器模式权限API
     */
    @Autowired
    private PermissionMgmtAPI basePermissionMgmtAPI;

    /**
     * 会话
     */
    @Autowired
    private SessionContextRegistry sessionContextRegistry;


    @Autowired
    private PermissionHelper permissionHelper;



    /**
     * 获取角色列表请求
     *
     * @param pageQueryRequest 请求参数
     * @param sessionContext session信息
     * @return 获取结果
     * @throws BusinessException 异常
     */
    @ApiOperation("获取角色列表请求")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-获取角色列表请求"})})
    @EnableAuthority
    public CommonWebResponse getRolePage(PageQueryRequest pageQueryRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(pageQueryRequest, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        // 获取AAA角色列表查询结果
        IacGetRolePageRequest request = buildRoleListRequest(pageQueryRequest);
        // 不是超级管理员 不显示超级管理员
        if (!permissionHelper.isAdminName()) {
            request.setHasSuperPrivilege(false);
        }
        DefaultPageResponse<IacRoleDTO> rolePage = baseRoleMgmtAPI.getRolePage(request);
        // 角色VO
        List<RoleVO> roleVOList = new ArrayList<>();
        // 遍历角色
        IacRoleDTO[] baseRoleDTOSItemArr = rolePage.getItemArr();
        for (int i = 0; i < baseRoleDTOSItemArr.length; i++) {
            RoleVO roleVO = new RoleVO();
            IacRoleDTO baseRoleDTO = baseRoleDTOSItemArr[i];
            // 拷贝 baseRoleDTO 到 roleVO
            BeanUtils.copyProperties(baseRoleDTO, roleVO);
            // 根据角色ID查询管理员信息
            IacGetAdminPageByRoleRequest baseGetAdminPageByRoleRequest = new IacGetAdminPageByRoleRequest();
            baseGetAdminPageByRoleRequest.setRoleId(baseRoleDTO.getId());
            DefaultPageResponse<IacAdminDTO> adminPageByRole = baseAdminMgmtAPI.getAdminPageByRole(baseGetAdminPageByRoleRequest);
            // 关联的管理员数量
            roleVO.setAdminRelationCount(adminPageByRole.getTotal());
            roleVOList.add(roleVO);
        }
        DefaultPageResponse<RoleVO> resp = new DefaultPageResponse<>();
        RoleVO[] roleArr = roleVOList.toArray(new RoleVO[roleVOList.size()]);
        resp.setItemArr(roleArr);
        resp.setTotal(rolePage.getTotal());
        return CommonWebResponse.success(resp);
    }

    private IacGetRolePageRequest buildRoleListRequest(PageQueryRequest pageQueryRequest) {
        IacGetRolePageRequest request = new IacGetRolePageRequest();
        request.setPage(pageQueryRequest.getPage());
        request.setLimit(pageQueryRequest.getLimit());

        if (ArrayUtils.isNotEmpty(pageQueryRequest.getSortArr())) {
            Sort[] sortArr = Arrays.stream(pageQueryRequest.getSortArr()).map(sort -> {
                Sort convertSort = new Sort();
                // hasDefault需要转换成isDefault
                String fieldName = sort.getFieldName();
                if ("hasDefault".equals(sort.getFieldName())) {
                    fieldName = "isDefault";
                }
                convertSort.setSortField(fieldName);
                convertSort.setDirection(Sort.Direction.valueOf(sort.getDirection().name()));
                return convertSort;
            }).toArray(Sort[]::new);
            request.setSortArr(sortArr);
        }

        // 判断是否存在搜索条件
        Match[] matchArr = pageQueryRequest.getMatchArr();
        if (ArrayUtils.isNotEmpty(matchArr)) {
            for (Match match : matchArr) {
                if (match.getType() == Match.Type.FUZZY
                        && StringUtils.equals("roleName", ((FuzzyMatch) match).getFieldNameArr()[0])) {
                    request.setRoleName(String.valueOf(((FuzzyMatch) match).getValue()));
                }
            }
        }

        return request;
    }


    /**
     * 获取单个管理员信息请求
     *
     * @param request 请求参数
     * @return 获取结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("角色信息详情请求")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-角色信息详情请求"})})
    public CommonWebResponse<IacRoleDTO> detail(GetRoleWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        IacRoleDTO role = baseRoleMgmtAPI.getRole(request.getId());
        return CommonWebResponse.success(role);
    }
}
