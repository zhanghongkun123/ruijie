package com.ruijie.rcos.rcdc.rco.module.impl.service;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月13日
 *
 * @author zdc
 */
public interface RcoIacPasswordBlacklistService {

    /**
     * 明文密码加密后，调用iac接口判断是否弱密码
     * @param pwd 明文密码
     * @return 是否弱密码
     */
    Boolean isPasswordBlackList(String pwd);
}
