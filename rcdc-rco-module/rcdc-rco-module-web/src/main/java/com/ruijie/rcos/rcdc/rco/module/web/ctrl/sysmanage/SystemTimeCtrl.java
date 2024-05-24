package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.NetworkAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.SystemTimeAPI;
import com.ruijie.rcos.base.sysmanage.module.def.common.Constant;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseNetworkDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto.BaseUpdateSystemTimeDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.enums.SystemTimeConfigType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.systemtime.BaseGetSystemTimeWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.systemtime.BaseUpdateSystemTimeWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description: 系统时间操作Controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月29日
 *
 * @author fyq
 */
@Controller
@RequestMapping("rco/systemConfig/systemTime")
public class SystemTimeCtrl {

    @Autowired
    private SystemTimeAPI systemTimeAPI;

    @Autowired
    private NetworkAPI networkAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 更新系统时间接口
     * 
     * @param webRequest 更新请求
     * @return 更新结果
     * @throws BusinessException 业务异常
     */
    protected DefaultWebResponse updateSystemTime(BaseUpdateSystemTimeWebRequest webRequest) throws BusinessException {

        Assert.notNull(webRequest, "请求参数不能为空");

        SystemTimeConfigType configType = webRequest.getType();

        if (configType == SystemTimeConfigType.NTP) {
            BaseNetworkDTO networkDTO = networkAPI.detailNetwork();
            if (null == networkDTO || !StringUtils.hasText(networkDTO.getDns())) {
                throw new BusinessException(
                        SysmanagerBusinessKey.BASE_SYS_MANAGE_SYSTEM_TIME_UPDATE_FAIL_BY_DNS_NOT_CONFIG);
            }

            return updateSystemTimeFromNtp(webRequest);
        } else {
            return updateSystemTimeBySet(webRequest);
        }
    }

    private DefaultWebResponse updateSystemTimeBySet(BaseUpdateSystemTimeWebRequest webRequest) throws BusinessException {

        try {
            BaseUpdateSystemTimeDTO dto = new BaseUpdateSystemTimeDTO(systemTimeAPI.updateSystemTime(webRequest.getTime()));

            SimpleDateFormat sdf = new SimpleDateFormat(Constant.YYYY_MM_DD_HH_MM_SS);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_SYSTEM_TIME_UPDATE,
                    sdf.format(new Date(webRequest.getTime())));

            return DefaultWebResponse.Builder.success(dto);

        } catch (BusinessException e) {
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_SYSTEM_TIME_UPDATE_FAIL, e.getI18nMessage());
            throw e;
        }
    }

    private DefaultWebResponse updateSystemTimeFromNtp(BaseUpdateSystemTimeWebRequest webRequest) throws BusinessException {


        try {
            BaseUpdateSystemTimeDTO dto = new BaseUpdateSystemTimeDTO(systemTimeAPI.updateSystemTimeFromNtp(webRequest.getNtpServer()));

            SimpleDateFormat sdf = new SimpleDateFormat(Constant.YYYY_MM_DD_HH_MM_SS);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_SYSTEM_TIME_UPDATE_FROM_NTP,
                    webRequest.getNtpServer(), sdf.format(new Date()));

            return DefaultWebResponse.Builder.success(dto);

        } catch (BusinessException e) {
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_SYSTEM_TIME_UPDATE_FROM_NTP_FAIL,
                    e.getI18nMessage());
            throw e;
        }
    }


    /**
     * 获取系统时间接口
     * 
     * @param webRequest 更新请求
     * @return 更新结果
     */
    @RequestMapping(value = "get")
    public DefaultWebResponse getSystemTime(BaseGetSystemTimeWebRequest webRequest) {

        Assert.notNull(webRequest, "请求不能为空");

        return DefaultWebResponse.Builder.success(System.currentTimeMillis());
    }
}
