package com.ruijie.rcos.rcdc.rco.module.impl.user.strategy;

import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperSyncNotifyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/26 15:03
 *
 * @author coderLee23
 */
public interface UserOperSyncNotifyStrategy {

    /**
     * 操作类型名称
     * 
     * @return 操作类型名称
     */
    String getOper();

    /**
     * 同步通知
     * 
     * @param cbbUserOperSyncNotifyRequest 请求对象
     * @throws BusinessException 业务异常
     */
    void syncNotifyUserChange(UserOperSyncNotifyDTO cbbUserOperSyncNotifyRequest) throws BusinessException;

}
