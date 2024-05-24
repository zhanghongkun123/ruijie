package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmConfigAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ListAlarmConfigRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.UpdateAlarmConfigRequest;
import com.ruijie.rcos.base.alarm.module.def.api.response.QueryAlarmConfigResponse;
import com.ruijie.rcos.base.alarm.module.def.dto.AlarmConfigDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.BaseQueryAlarmConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.BaseUpdateAlarmConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.vo.AlarmConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.validation.AlarmConfigValidation;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月17日
 *
 * @author xgx
 */
@Controller
@RequestMapping("rco/alarm/config")
@EnableCustomValidate(validateClass = AlarmConfigValidation.class)
public class AlarmConfigCtrl {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmConfigCtrl.class);

    @Autowired
    private BaseAlarmConfigAPI baseAlarmConfigAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 获取告警配置详情
     * 
     * @param baseQueryAlarmConfigWebRequest 告警查询请求对象
     * @return 告警配置信息
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/detail")
    public DefaultWebResponse query(BaseQueryAlarmConfigWebRequest baseQueryAlarmConfigWebRequest) throws BusinessException {
        Assert.notNull(baseQueryAlarmConfigWebRequest, "请求参数不能为空");

        QueryAlarmConfigResponse queryAlarmConfigResponse = baseAlarmConfigAPI.queryAlarmConfig(baseQueryAlarmConfigWebRequest.getId());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("【告警配置】获取告警配置成功：{}", JSONObject.toJSONString(queryAlarmConfigResponse));
        }

        AlarmConfigVO configVO = new AlarmConfigVO(queryAlarmConfigResponse);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("【告警配置】告警配置转换VO成功：{}", JSONObject.toJSONString(configVO));
        }

        return DefaultWebResponse.Builder.success(configVO);
    }

    /**
     * 编辑告警配置
     * 
     * @param baseUpdateAlarmConfigWebRequest 告警编辑请求对象
     * @return 响应
     * @throws AnnotationValidationException 注解校验异常
     * @throws BusinessException 业务异常
     */
    @EnableCustomValidate(validateMethod = "alarmConfigEditValidate")
    @RequestMapping(value = "/edit")
    public DefaultWebResponse edit(BaseUpdateAlarmConfigWebRequest baseUpdateAlarmConfigWebRequest)
            throws AnnotationValidationException, BusinessException {
        Assert.notNull(baseUpdateAlarmConfigWebRequest, "请求参数不能为空");

        UpdateAlarmConfigRequest updateAlarmConfigRequest = baseUpdateAlarmConfigWebRequest.turn2UpdateAlarmConfigRequest();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("【告警配置】转换API请求参数结束，api请求参数：{}", JSON.toJSONString(updateAlarmConfigRequest));
        }

        QueryAlarmConfigResponse queryAlarmConfigResponse = null;

        try {
            queryAlarmConfigResponse = baseAlarmConfigAPI.queryAlarmConfig(updateAlarmConfigRequest.getId());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("【告警配置】获取告警配置成功，告警配置：{}", JSON.toJSONString(queryAlarmConfigResponse));
            }
            baseAlarmConfigAPI.updateAlarmConfig(updateAlarmConfigRequest);
            LOGGER.debug("【告警配置】更新告警配置成功");
            auditLogAPI.recordLog(AlarmBusinessKey.BASE_ALARM_EDIT_ALARM_SUCCESS, queryAlarmConfigResponse.getAlarmName());
            return DefaultWebResponse.Builder.success(AlarmBusinessKey.BASE_ALARM_OPERATOR_SUCCEED, StringUtils.EMPTY);
        } catch (BusinessException e) {
            String alarmName =
                    queryAlarmConfigResponse == null ? baseUpdateAlarmConfigWebRequest.getId().toString() : queryAlarmConfigResponse.getAlarmName();
            LOGGER.error("【告警配置】更新告警配置失败", e);
            auditLogAPI.recordLog(AlarmBusinessKey.BASE_ALARM_EDIT_ALARM_FAIL, alarmName, e.getI18nMessage());
            throw e;
        }
    }

    /**
     * 分页获取告警配置列表
     * 
     * @param pageWebRequest 分页获取告警列表请求参数
     * @return 告警配置列表
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse list(PageWebRequest pageWebRequest) {
        Assert.notNull(pageWebRequest, "请求参数不能为空");

        ListAlarmConfigRequest listAlarmConfigRequest = new ListAlarmConfigRequest();
        BeanUtils.copyProperties(pageWebRequest, listAlarmConfigRequest);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("【告警配置】转换API请求参数结束，api请求参数：{}", JSON.toJSONString(listAlarmConfigRequest));
        }
        DefaultPageResponse<AlarmConfigDTO> defaultPageResponse = baseAlarmConfigAPI.listAlarmConfig(listAlarmConfigRequest);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("【告警配置】获取告警列表结束，响应：{}", JSON.toJSONString(defaultPageResponse));
        }
        return DefaultWebResponse.Builder.success(defaultPageResponse);
    }

}
