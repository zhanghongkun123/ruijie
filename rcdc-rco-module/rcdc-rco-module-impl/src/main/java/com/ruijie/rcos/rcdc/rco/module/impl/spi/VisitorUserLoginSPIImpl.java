package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserLoginNoticeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.spi.UserLoginEventNoticeSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.response.LoginIdentityResponse;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;

/**
 * Description: 访客用户登录事件通知SPI接口实现类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/6
 *
 * @author lintingling
 */
@DispatcherImplemetion(Constants.LOGIN_VISITOR)
public class VisitorUserLoginSPIImpl implements UserLoginEventNoticeSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorUserLoginSPIImpl.class);

    @Override
    public DtoResponse<?> notify(UserLoginNoticeDTO request) {
        // 访客登录成功，一定是访客用户，无身份验证配置

        LOGGER.info("访客用户在访客登录通道成功后无需获取身份验证配置");
        return DtoResponse.success(new LoginIdentityResponse(null));
    }
}
