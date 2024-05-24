package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PermissionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.dto.PermissionFilterDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.dto.PermissionTreeDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo.PermissionTreeVO;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description:权限
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/13 16:56
 *
 * @author linrenjian
 */
@Api(tags = "权限模块")
@Controller
@RequestMapping("/rco/permission")
public class PermissionController {


    @Autowired
    private PermissionMgmtAPI basePermissionMgmtAPI;

    /**
     * 获取权限全部列表请求
     *
     * @return 获取结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取权限全部列表请求")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-获取权限全部列表请求"})})
    @EnableAuthority
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse getPermissionPage() throws BusinessException {
        List<IacPermissionDTO> basePermissionList = basePermissionMgmtAPI.listAllPermissionByServerModel();
        return CommonWebResponse.success(basePermissionList);
    }



    /**
     * 获取权限树权限全部列表请求
     * 获取结果
     * 
     * @return PermissionTreeVO
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取权限全部列表请求")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-获取权限全部列表请求"})})
    @RequestMapping(value = "tree", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse tree() throws BusinessException {
        // 查询根据服务器模式获取所有权限
        List<IacPermissionDTO> basePermissionList = basePermissionMgmtAPI.listAllPermissionByServerModel();
        // 创建权限过滤DTO对象
        PermissionFilterDTO permissionFilterDTO = new PermissionFilterDTO();
        // 构造返回权限树的一维数组
        PermissionTreeDTO[] permissionTreeDTOArr = permissionFilterDTO.buildPermissionTreeDTOArr(basePermissionList);
        // 设置到权限过滤DTO对象
        permissionFilterDTO.setPermissionTreeDTOArr(permissionTreeDTOArr);
        // 构建权限树VO结构
        List<PermissionTreeVO> resultList = permissionFilterDTO.buildPermissionTree(permissionFilterDTO);

        return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", resultList));
    }

}
