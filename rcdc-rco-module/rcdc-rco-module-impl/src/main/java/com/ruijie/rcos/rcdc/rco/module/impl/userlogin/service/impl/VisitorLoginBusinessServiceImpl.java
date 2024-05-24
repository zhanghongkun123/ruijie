package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.common.RcoInvalidTimeHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 终端访客用户登录
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/09
 *
 * @author linke
 */
@Service("visitorLoginTemplateService")
public class VisitorLoginBusinessServiceImpl extends AbstractLoginBusinessServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorLoginBusinessServiceImpl.class);

    @Autowired
    protected RcoViewUserDAO rcoViewUserDAO;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserService userService;

    @Autowired
    RcoInvalidTimeHelper rcoInvalidTimeHelper;

    @Override
    public String getKey() {
        return ShineAction.VISITOR_LOGIN;
    }

    @Override
    public ShineLoginDTO getShineLoginDTO(String data) throws Exception {
        Assert.hasText(data, "data不能为null");

        return shineMessageHandler.parseObject(data, ShineLoginDTO.class);
    }

    @Override
    public int processLogin(String terminalId, @Nullable IacUserDetailDTO userDetailDTO) throws BusinessException {
        Assert.hasText(terminalId, "terminalId不能为null");

        if (userDetailDTO == null) {
            LOGGER.info("数据库中不存在用户");
            return LoginMessageCode.USERNAME_OR_PASSWORD_ERROR;
        }
        // 检查是否为访客用户，不是访客不允许登录
        if (IacUserTypeEnum.VISITOR != userDetailDTO.getUserType()) {
            LOGGER.info("不允许在终端[{}]用非访客账号用于访客登录", terminalId, userDetailDTO.getUserName());
            return LoginMessageCode.NOT_ALLOW_LOGIN_FOR_NOT_VISITOR;
        }
        // 检查该访客是否处于禁用状态
        if (userDetailDTO.getUserState() == IacUserStateEnum.DISABLE) {
            LOGGER.info("用户[{}]已禁用，不可登录", userDetailDTO.getUserName());
            return LoginMessageCode.AD_ACCOUNT_DISABLE;
        }
        return CommonMessageCode.SUCCESS;
    }

    @Override
    public String getLoginEvent() {
        return Constants.LOGIN_VISITOR;
    }

    @Override
    public ShineLoginResponseDTO generateResponse(@Nullable IacUserDetailDTO userDetailDTO) {

        return new ShineLoginResponseDTO();
    }
}
