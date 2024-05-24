package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.alarm.module.def.api.BaseEmailConfigAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.TestEmailConfigRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.UpdateEmailConfigRequest;
import com.ruijie.rcos.base.alarm.module.def.api.response.QueryEmailConfigResponse;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.BaseQueryEmailConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.BaseTestEmailConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.BaseUpdateEmailConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.validation.EmailConfigValidation;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月17日
 *
 * @author xgx
 */
@Controller
@RequestMapping("rco/alarm/mail")
@EnableCustomValidate(validateClass = EmailConfigValidation.class)
public class EmailConfigCtrl {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailConfigCtrl.class);

    @Autowired
    private BaseEmailConfigAPI baseEmailConfigAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    private static final String EMAIL_SPLIT_CHAR = ";";

    /**
     * 获取邮件配置详情
     * 
     * @param baseQueryEmailConfigWebRequest 查询请求参数
     * @return 邮件配置详情
     * @throws UnsupportedEncodingException 编码转换异常
     */
    @RequestMapping(value = "/detail")
    public DefaultWebResponse detail(BaseQueryEmailConfigWebRequest baseQueryEmailConfigWebRequest) throws UnsupportedEncodingException {
        Assert.notNull(baseQueryEmailConfigWebRequest, "请求参数不能为空");
        LOGGER.debug("【邮件配置】获取邮件配置开始");
        QueryEmailConfigResponse queryEmailConfigResponse = baseEmailConfigAPI.queryEmailConfig();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("【邮件配置】获取邮件配置成功：{}", JSON.toJSONString(queryEmailConfigResponse));
        }
        return DefaultWebResponse.Builder.success(queryEmailConfigResponse);
    }

    /**
     * 更新邮件配置
     * 
     * @param baseUpdateEmailConfigWebRequest 邮件更新请求参数
     * @throws BusinessException 业务异常
     * @return 响应
     */
    @RequestMapping(value = "/edit")
    @EnableCustomValidate(validateMethod = "validateEmailConfig")
    public DefaultWebResponse edit(BaseUpdateEmailConfigWebRequest baseUpdateEmailConfigWebRequest) throws BusinessException {
        Assert.notNull(baseUpdateEmailConfigWebRequest, "请求参数不能为空");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("【邮件配置】编辑邮件配置开始，请求参数：{}", JSON.toJSONString(baseUpdateEmailConfigWebRequest));
        }

        UpdateEmailConfigRequest updateEmailConfigRequest = new UpdateEmailConfigRequest();
        BeanUtils.copyProperties(baseUpdateEmailConfigWebRequest, updateEmailConfigRequest);
        String toMailAccounts = StringUtils.join(baseUpdateEmailConfigWebRequest.getToMailAccountArr(), EMAIL_SPLIT_CHAR);
        updateEmailConfigRequest.setToMailAccount(toMailAccounts);
        if (!ObjectUtils.isEmpty(baseUpdateEmailConfigWebRequest.getCopyToMailAccountArr())) {
            String copyMailAccounts = StringUtils.join(baseUpdateEmailConfigWebRequest.getCopyToMailAccountArr(), EMAIL_SPLIT_CHAR);
            updateEmailConfigRequest.setCopyToMailAccount(copyMailAccounts);
        }
        try {
            baseEmailConfigAPI.updateEmailConfig(updateEmailConfigRequest);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("【邮件配置】获取邮件配置成功");
            }
            auditLogAPI.recordLog(AlarmBusinessKey.BASE_ALARM_EDIT_EMAIL_CONFIG_SUCCESS);
            LOGGER.debug("【邮件配置】记录操作日志成功");
            return DefaultWebResponse.Builder.success(AlarmBusinessKey.BASE_ALARM_OPERATOR_SUCCEED, StringUtils.EMPTY);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(AlarmBusinessKey.BASE_ALARM_EDIT_EMAIL_CONFIG_FAIL, e.getI18nMessage());
            throw e;
        }
    }

    /**
     * 测试邮箱配置连通性
     *
     * @param baseTestEmailConfigWebRequest baseTestEmailConfigWebRequest
     * @return DefaultWebResponse
     * @throws BusinessException BusinessException
     */
    @RequestMapping(value = "/sendTestMail")
    public DefaultWebResponse sendTestMail(BaseTestEmailConfigWebRequest baseTestEmailConfigWebRequest)
            throws BusinessException {
        final TestEmailConfigRequest testEmailConfigRequest = new TestEmailConfigRequest();
        BeanUtils.copyProperties(baseTestEmailConfigWebRequest, testEmailConfigRequest);
        final String toMailAccounts =
                StringUtils.join(baseTestEmailConfigWebRequest.getToMailAccountArr(), EMAIL_SPLIT_CHAR);
        testEmailConfigRequest.setToMailAccount(toMailAccounts);

        if (!ObjectUtils.isEmpty(baseTestEmailConfigWebRequest.getCopyToMailAccountArr())) {
            final String copyMailAccounts =
                    StringUtils.join(baseTestEmailConfigWebRequest.getCopyToMailAccountArr(), EMAIL_SPLIT_CHAR);
            testEmailConfigRequest.setCopyToMailAccount(copyMailAccounts);
        }

        baseEmailConfigAPI.testEmailConfig(testEmailConfigRequest);
        return DefaultWebResponse.Builder.success(AlarmBusinessKey.BASE_ALARM_SEND_TEST_EMAIL_SUCCEED, StringUtils.EMPTY);
    }
}
