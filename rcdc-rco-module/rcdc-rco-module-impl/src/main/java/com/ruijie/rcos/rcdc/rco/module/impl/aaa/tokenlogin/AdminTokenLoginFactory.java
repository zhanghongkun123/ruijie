package com.ruijie.rcos.rcdc.rco.module.impl.aaa.tokenlogin;

import com.ruijie.rcos.rcdc.rco.module.def.enums.TokenSourceEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.aaa.tokenlogin.impl.CloudDockTokenLoginServiceImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.aaa.tokenlogin.impl.RccpTokenLoginServiceImpl;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/7/6
 *
 * @author chenjuan
 */
@Service
public class AdminTokenLoginFactory {

    @Autowired
    RccpTokenLoginServiceImpl rccpTokenLoginService;

    @Autowired
    CloudDockTokenLoginServiceImpl cloudDockTokenLoginService;

    /**
     * 获取token来源
     * @param tokenSourceEnum TokenSourceEnum
     * @return AbstractAdminTokenLoginService
     * @throws BusinessException 业务异常
     */
    public AbstractAdminTokenLoginService getTokenSource(TokenSourceEnum tokenSourceEnum) throws BusinessException {
        Assert.notNull(tokenSourceEnum, "tokenSourceEnum can not be null");

        if (tokenSourceEnum == TokenSourceEnum.RCCP) {
            return rccpTokenLoginService;
        }
        if (tokenSourceEnum == TokenSourceEnum.CLOUD_DOCK) {
            return cloudDockTokenLoginService;
        }
        if (tokenSourceEnum == TokenSourceEnum.UNKNOWN) {
            throw new BusinessException("tokenSource not supported.");
        }
        throw new BusinessException("tokenSource not supported.");
    }
}
