package com.ruijie.rcos.rcdc.rco.module.web.validation;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.alarm.module.def.condition.AlarmCondition;
import com.ruijie.rcos.base.alarm.module.def.condition.AlarmConditionConverter;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.BaseUpdateAlarmConfigWebRequest;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.validation.BeanValidationUtil;

/**
 * Description: 告警配置校验器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月26日
 *
 * @author xgx
 */
@Service
public class AlarmConfigValidation {
    /**
     * 告警配置校验器
     * 
     * @param baseUpdateAlarmConfigWebRequest 请求参数
     * @throws AnnotationValidationException 参数校验异常
     */
    public void alarmConfigEditValidate(BaseUpdateAlarmConfigWebRequest baseUpdateAlarmConfigWebRequest) throws AnnotationValidationException {
        Assert.notNull(baseUpdateAlarmConfigWebRequest, "请求参数不能为空");
        AlarmType alarmType = baseUpdateAlarmConfigWebRequest.getAlarmIdLabelRequest().getId();
        AlarmCondition alarmCondition = AlarmConditionConverter.conversion(alarmType, JSONObject.toJSONString(baseUpdateAlarmConfigWebRequest));
        BeanValidationUtil.validateBean(alarmCondition.getClass(), alarmCondition);
    }
}
