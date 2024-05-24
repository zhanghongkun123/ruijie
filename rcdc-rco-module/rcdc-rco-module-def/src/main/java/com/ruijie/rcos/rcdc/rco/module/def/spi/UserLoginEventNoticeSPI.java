package com.ruijie.rcos.rcdc.rco.module.def.spi;

import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserLoginNoticeDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherInterface;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;

/**
 * Description: 用户登录事件通知SPI接口
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/6
 *
 * @author lintingling
 */
@DispatcherInterface
public interface UserLoginEventNoticeSPI {

    /**
     * 消息通知
     *
     * @param request 请求参数
     * @return 返回身份验证
     */
    DtoResponse<?> notify(UserLoginNoticeDTO request);
}
