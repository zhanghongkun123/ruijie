package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.comm.SpiCustomThreadPoolConfig;

/**
 * Description: 终端访客用户登录
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/2/13
 *
 * @author Jarman
 */
@DispatcherImplemetion(ShineAction.VISITOR_LOGIN)
@SpiCustomThreadPoolConfig(threadPoolName = "custom_user_login_thread_pool")
public class VisitorLoginSPIImpl extends AbstractLoginSPI implements CbbDispatcherHandlerSPI {
}
