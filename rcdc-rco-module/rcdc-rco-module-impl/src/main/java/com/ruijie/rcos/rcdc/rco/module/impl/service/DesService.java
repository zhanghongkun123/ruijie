package com.ruijie.rcos.rcdc.rco.module.impl.service;

/**
 * Description: AlarmService
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-02-23
 *
 * @author ljm
 */
public interface DesService {

    /**
     * bCrypt加密
     *
     * @param password 密码
     * @return 密文
     */
    String bCryptEncrypt(String password);


}
