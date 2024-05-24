package com.ruijie.rcos.rcdc.rco.module.impl.rccm.service;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: RccmManageService
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/12
 *
 * @author lihengjing
 */
public interface RccmManageSmService {

    /**
     * 事务处理更新 统一登入开关
     * @param hasUnifiedLogin 开关
     * @param enableAssistAuth 辅助认证开关
     * @throws BusinessException BusinessException
     */
    void updateUnifiedLogin(Boolean hasUnifiedLogin, Boolean enableAssistAuth) throws BusinessException;
}
