package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ReleaseAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.api.response.QueryAlarmResponse;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AlarmAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.AlarmWebResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.CancelAlarmRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

/**
 * Description: 首页顶部轮播信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/28
 *
 * @author Jarman
 */
@Controller
@RequestMapping("/rco/user/alarm")
public class AlarmController {

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Autowired
    private AlarmAPI alarmAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;



    /**
     * 获取顶部轮播信息
     * 
     * @param request 默认请求参数对象
     * @return 返回告警轮播数据
     */
    @RequestMapping("list")
    public DefaultWebResponse getCarouselData(PageWebRequest request) {
        Assert.notNull(request, "PageWebRequest不能为null");
        DefaultPageResponse<AlarmWebResponse> response = alarmAPI.pageQuery(request);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 解除告警
     * 
     * @param request 请求参数
     * @return 返回成功或失败
     * @throws BusinessException 业务异常
     */
    @RequestMapping("cancel")
    public DefaultWebResponse cancelAlarm(CancelAlarmRequest request) throws BusinessException {
        Assert.notNull(request, "CancelAlarmRequest不能为null");
        String alarmContent = null;
        try {
            ReleaseAlarmRequest alarmRequest = new ReleaseAlarmRequest();
            alarmRequest.setId(request.getId());
            QueryAlarmResponse alarmResponse = baseAlarmAPI.queryAlarm(request.getId());
            alarmContent = alarmResponse.getAlarmContent();
            baseAlarmAPI.releaseAlarm(alarmRequest);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_MESSAGE_RELEASE_SUCCESS_LOG, new String[] {alarmContent});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_MESSAGE_RELEASE_FAIL_LOG, e, new String[]{alarmContent, e.getI18nMessage()});
            throw e;
        }
        return DefaultWebResponse.Builder.success();
    }
    
    /**
     * 查询云桌面报障列表
     * @param request 请求参数
     * @param sessionContext session信息
     * @return DefaultWebResponse 响应列表数据
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listDesktopFault")
    public DefaultWebResponse listDesktopFault(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "PageWebRequest不能为null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        PageSearchRequest pageRequest = new DeskPageSearchRequest(request);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            appendDesktopIdMatchEqual(pageRequest, sessionContext);
        }
        return DefaultWebResponse.Builder.success(userDesktopMgmtAPI.desktopFaultPageQuery(pageRequest));
    }

    private void appendDesktopIdMatchEqual(PageSearchRequest request, SessionContext sessionContext) throws BusinessException {
        UUID[] uuidArr = permissionHelper.getDesktopIdArr(sessionContext);
        request.appendCustomMatchEqual(new MatchEqual("cbbDesktopId", uuidArr));
    }

}
