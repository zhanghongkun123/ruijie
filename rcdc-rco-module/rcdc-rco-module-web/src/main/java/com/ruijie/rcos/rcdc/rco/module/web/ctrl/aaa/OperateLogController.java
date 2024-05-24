package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa;

import com.ruijie.rcos.gss.base.iac.module.dto.IacLoginUserDTO;
import com.ruijie.rcos.gss.log.module.def.dto.BaseOperateLogDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OperateLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月27日
 * 
 * @author zhuangchenwu
 */
@Controller
@RequestMapping("/rco/operateLog")
public class OperateLogController {

    public static final String OPERATOR_ID = "operatorId";

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private PermissionHelper permissionHelper;


    @Autowired
    private OperateLogAPI operateLogAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    /**
     * 获取操作日志列表请求
     * 
     * @param request 请求参数
     * @param sessionContext session上下文信息
     * @return 获取结果
     * @throws BusinessException 异常
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DefaultWebResponse getOperateLogPage(PageQueryRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(request);
        // 审计 与超级管理员查看全部 系统管理员 只能看自己的日志
        if (permissionHelper.roleIsSysAdmin(sessionContext) || !permissionHelper.isAllLogPermission(sessionContext)) {
            IacLoginUserDTO loginUserInfo = baseAdminMgmtAPI.getLoginUserInfo();
            builder.eq(OPERATOR_ID, loginUserInfo.getId());
        }
        PageQueryResponse<BaseOperateLogDTO> response = operateLogAPI.operateLogList(builder);
        return DefaultWebResponse.Builder.success(response);
    }

}
