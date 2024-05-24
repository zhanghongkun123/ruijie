package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.comm.SpiCustomThreadPoolConfig;

/**
 * Description: 终端用户登录(非访客用户)
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author Jarman
 */
@DispatcherImplemetion(ShineAction.LOGIN)
@SpiCustomThreadPoolConfig(threadPoolName = "custom_user_login_thread_pool")
public class LoginSPIImpl extends AbstractLoginSPI implements CbbDispatcherHandlerSPI {
}
