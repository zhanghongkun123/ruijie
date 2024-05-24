package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.role.IacGetRolePageRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description: 角色管理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月18日
 *
 * @author Ghang
 */
@Controller
@RequestMapping("/rco/user/roleManage")
public class RoleManageController {


    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    /**
     * 分页获取角色
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse getRolePage(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "PageWebRequest is null");

        IacGetRolePageRequest rolePageRequest = new IacGetRolePageRequest();
        rolePageRequest.setHasDefault(true);
        rolePageRequest.setHasSuperPrivilege(false);
        rolePageRequest.setPage(request.getPage());
        rolePageRequest.setLimit(request.getLimit());
        final DefaultPageResponse<IacRoleDTO> rolePage = baseRoleMgmtAPI.getRolePage(rolePageRequest);

        return DefaultWebResponse.Builder.success(rolePage);
    }
}
