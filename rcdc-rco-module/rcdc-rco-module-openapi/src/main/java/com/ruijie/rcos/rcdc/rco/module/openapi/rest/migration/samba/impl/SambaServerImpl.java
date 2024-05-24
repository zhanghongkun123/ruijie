package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.samba.impl;

import com.ruijie.rcos.base.sysmanage.module.impl.connector.response.SambaPasswordResponse;
import com.ruijie.rcos.base.sysmanage.module.impl.connector.samba.SambaRestClient;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.samba.SambaServer;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
@Service
public class SambaServerImpl implements SambaServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SambaServerImpl.class);

    @Autowired
    private SambaRestClient sambaRestClient;

    /**
     * 通过用户名获取密码
     *
     * @param username 用户名
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @Override
    public SambaPasswordResponse getPassword(String username) throws BusinessException {
        Assert.notNull(username, "username is not null");
        return sambaRestClient.getPassword(username);
    }
}
