package com.ruijie.rcos.rcdc.rco.module.def.spi;

import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherInterface;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherKey;

/**
 * Description: 管理员操作信息通知
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/08/15
 *
 * @author lifeng
 */
public interface AdminActionMsgSPI {

    /**
     * 退出所有管理员
     * @throws BusinessException 异常
     */
    void notifyAllAdminLogout() throws BusinessException;
}
