package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.comm.SpiCustomThreadPoolConfig;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月08日
 *
 * @author zhanghongkun
 */
@DispatcherImplemetion(ShineAction.USER_OTP_CODE_LOGIN)
@SpiCustomThreadPoolConfig(threadPoolName = "custom_user_login_thread_pool")
public class UserOtpCodeLoginSPIImpl extends AbstractLoginSPI implements CbbDispatcherHandlerSPI {
}
