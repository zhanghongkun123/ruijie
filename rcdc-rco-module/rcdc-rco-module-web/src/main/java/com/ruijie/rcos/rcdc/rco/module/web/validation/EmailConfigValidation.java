package com.ruijie.rcos.rcdc.rco.module.web.validation;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import com.ruijie.rcos.base.alarm.module.def.util.MailValidateUtil;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.BaseUpdateEmailConfigWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 邮件配置校验器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月19日
 *
 * @author xgx
 */
@Service
public class EmailConfigValidation {
    /**
     * 更新邮箱参数校验
     * 
     * @param baseUpdateEmailConfigWebRequest 请求参数
     * @throws BusinessException 业务异常
     */
    public void validateEmailConfig(BaseUpdateEmailConfigWebRequest baseUpdateEmailConfigWebRequest) throws BusinessException {
        Assert.notNull(baseUpdateEmailConfigWebRequest, "请求参数不能为空");
        String[] toMailAccountArr = baseUpdateEmailConfigWebRequest.getToMailAccountArr();
        validateEmails(toMailAccountArr);
        String[] copyToMailAccountArr = baseUpdateEmailConfigWebRequest.getCopyToMailAccountArr();
        if (!ObjectUtils.isEmpty(copyToMailAccountArr)) {
            validateEmails(copyToMailAccountArr);
        }
    }

    private void validateEmails(String[] mailAccountArr) throws BusinessException {
        for (String mailAccount : mailAccountArr) {
            MailValidateUtil.validateEmail(mailAccount);
        }
    }
}
