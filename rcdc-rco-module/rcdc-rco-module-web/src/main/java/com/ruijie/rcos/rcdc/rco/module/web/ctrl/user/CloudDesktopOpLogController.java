package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.DeskopOpLogPageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDesktopOpLogDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.TimePageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.validation.CloudDesktopOpLogValidation;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * 
 * Description: 云桌面操作日志WEB控制器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 * 
 * @author artom
 */
@Controller
@RequestMapping("/rco/user/cloudDesktopOpLog")
@EnableCustomValidate(validateClass = CloudDesktopOpLogValidation.class)
public class CloudDesktopOpLogController {
    
    @Autowired
    private CbbDesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    /**
     * *查询
     *
     * @param request 页面请求参数
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse list(TimePageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        DeskopOpLogPageRequest pageReq = new DeskopOpLogPageRequest(request);
        if (!permissionHelper.isAllLogPermission(sessionContext)) {
            appendDesktopIdMatchEqual(pageReq, sessionContext);
        }
        pageReq.setStartTime(request.getStartTime());
        pageReq.setEndTime(request.getEndTime());
        DefaultPageResponse<CbbDesktopOpLogDetailDTO> resp = desktopOpLogMgmtAPI.pageQuery(pageReq);
        return DefaultWebResponse.Builder.success(resp);
    }

    private void appendDesktopIdMatchEqual(PageSearchRequest request, SessionContext sessionContext)
        throws BusinessException {
        UUID[] uuidArr = permissionHelper.getDesktopIdArr(sessionContext);
        request.appendCustomMatchEqual(new MatchEqual("desktopId", uuidArr));
    }
    
}
