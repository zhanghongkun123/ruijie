package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SetLogIntervalAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SyslogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SyslogScheduleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.SyslogSendCycleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SetLogIntervalRequest;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.EmptyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.set.EditLogIntervalWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.set.EditSyslogScheduleWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo.GetLogIntervalVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo.SyslogScheduleVO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

import io.swagger.annotations.ApiOperation;

/**
 * Description: 日志周期设置Controller类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/15
 *
 * @author zhiweiHong
 */
@Controller
@RequestMapping("/rco/logInterval")
public class LogIntervalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogIntervalController.class);

    @Autowired
    private SetLogIntervalAPI setLogIntervalAPI;

    @Autowired
    private CbbDesktopOpLogMgmtAPI userDesktopOpLogMgmtAPI;

    @Autowired
    private SyslogAPI syslogAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    /**
     * 设置日志周期
     *
     * @param request        CreateLogIntervalWebRequest
     * @return DefaultWebResponse
     * @throws BusinessException 当base组件抛出异常时反馈前端
     */
    @ApiOperation("设置日志周期")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限-设置日志周期", "增加syslog定时发送时间"})})
    @EnableAuthority
    public DefaultWebResponse edit(EditLogIntervalWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        SetLogIntervalRequest setLogIntervalRequest = new SetLogIntervalRequest();
        setLogIntervalRequest.setInterval(request.getInterval());

        EditSyslogScheduleWebRequest editSyslogScheduleWebRequest = request.getEditSyslogScheduleWebRequest();

        if (SyslogSendCycleEnum.MINUTE.equals(editSyslogScheduleWebRequest.getSendCycleType())
                && editSyslogScheduleWebRequest.getIntervalMinute() == null) {
            throw new BusinessException(AaaBusinessKey.RCDC_RCO_SET_LOG_INTERVAL_FAIL_LOG, "当日志周期为分钟间隔时,不允许间隔时间为空");
        }

        if (SyslogSendCycleEnum.DAY.equals(editSyslogScheduleWebRequest.getSendCycleType())
                && editSyslogScheduleWebRequest.getScheduleTime() == null) {
            throw new BusinessException(AaaBusinessKey.RCDC_RCO_SET_LOG_INTERVAL_FAIL_LOG, "当日志周期为天时,不允许发送时间为空");
        }

        try {

            SyslogScheduleDTO syslogScheduleDTO = new SyslogScheduleDTO();
            BeanUtils.copyProperties(editSyslogScheduleWebRequest, syslogScheduleDTO);
            syslogAPI.editSyslogSchedule(syslogScheduleDTO);
            setLogIntervalAPI.setLogInterval(setLogIntervalRequest);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_RCO_SET_LOG_INTERVAL_SUCCESS_LOG);

            return DefaultWebResponse.Builder.success(AaaBusinessKey.RCDC_RCO_SET_LOG_INTERVAL_SUCCESS_LOG, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑日志周期失败，失败原因：", e);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_RCO_SET_LOG_INTERVAL_FAIL_LOG, e.getI18nMessage());
            throw new BusinessException(AaaBusinessKey.RCDC_RCO_SET_LOG_INTERVAL_FAIL_LOG, e, e.getI18nMessage());
        }
    }


    /**
     * 获取设置详情
     *
     * @param request 空的request
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    @ApiVersions(value = {@ApiVersion(value = Version.V1_0_0, descriptions = {"获取日志保存周期"}),
            @ApiVersion(value = Version.V3_1_1, descriptions = {"增加syslog配置信息"})})
    public DefaultWebResponse detail(EmptyWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        int retainDay = userDesktopOpLogMgmtAPI.getDesktopOpLogRetainDays();
        GetLogIntervalVO getLogIntervalVO = new GetLogIntervalVO(retainDay);

        SyslogScheduleDTO syslogScheduleDTO = syslogAPI.findSyslogScheduleDetail();
        SyslogScheduleVO syslogScheduleVO = new SyslogScheduleVO();
        BeanUtils.copyProperties(syslogScheduleDTO, syslogScheduleVO);
        getLogIntervalVO.setSyslogScheduleVO(syslogScheduleVO);

        return DefaultWebResponse.Builder.success(getLogIntervalVO);
    }
}
