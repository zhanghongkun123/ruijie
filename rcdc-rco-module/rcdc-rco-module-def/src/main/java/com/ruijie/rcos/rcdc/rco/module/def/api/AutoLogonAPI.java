package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.autologon.dto.AutoLogonDTO;
import com.ruijie.rcos.rcdc.rco.module.def.autologon.enums.AutoLogonEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 *
 * Description: 自动登录策略API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年06月01日
 *
 * @author TD
 */
public interface AutoLogonAPI {

    /**
     * 变更全局auto_logon：全局windows密码配置
     *
     * @param autoLogDTO 请求参数
     * @throws BusinessException 业务异常
     */
    void updateAutoLogonStrategy(AutoLogonDTO autoLogDTO) throws BusinessException;

    /**
     * 获取全局windows密码配置开关
     *
     * @param autoLogonEnum 获取类型
     * @return boolean 响应参数
     * @throws BusinessException 业务异常
     */
    boolean getGlobalAutoLogonStrategy(AutoLogonEnum autoLogonEnum);
}