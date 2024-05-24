package com.ruijie.rcos.rcdc.rco.module.web.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.advanced.request.BaseUpdateMailConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 邮件服务器配置入参检验对象
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/28
 *
 * @author liusd
 */

@Service
public class MailConfigCustomValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailConfigCustomValidation.class);

    /**
     * 校验创建邮件服务配置
     *
     * @param request 请求参数对象
     * @throws BusinessException 业务异常
     */
    public void updateMailConfigValidate(BaseUpdateMailConfigWebRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Boolean isEnable = request.getEnableSendMail();
        // 用户名不能是保留字
        if (Boolean.FALSE.equals(isEnable)) {
            return;
        }
        String serverAddress = request.getServerAddress();
        String fromMailAccount = request.getFromMailAccount();
        if (StringUtils.isBlank(serverAddress)) {
            LOGGER.error("serverAddress[{}]不能为空", serverAddress);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_SERVER_ADDRESS_NOT_BLANK);
        }
        if (StringUtils.isBlank(fromMailAccount)) {
            LOGGER.error("fromMailAccount[{}]不能为空", fromMailAccount);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_FROM_MAIL_ACCOUNT_NOT_BLANK);
        }
    }

}
