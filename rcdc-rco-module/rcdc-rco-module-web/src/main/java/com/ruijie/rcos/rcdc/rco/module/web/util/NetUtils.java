package com.ruijie.rcos.rcdc.rco.module.web.util;

import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.net.InetAddress;
import java.util.Objects;

/**
 * Description: 网络工具
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/26 10:10
 *
 * @author clone
 */
public class NetUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtils.class);

    /**
     * ping主机联通性测试
     * @param ip IP地址
     * @param timeout 超时时间
     * @return 返回bool/是否可达
     */
    public static boolean ping(String ip , @Nullable int timeout) {
        Assert.notNull(ip , "ip can not be null");
        try {
            return InetAddress.getByName(ip).isReachable(timeout);
        } catch (Exception e) {
            LOGGER.error("net ping failed", e);
            return false;
        }
    }
}
