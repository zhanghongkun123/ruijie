package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Description:自动进入虚机缓存
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/18
 *
 * @author zjy
 */
@Service
public class AutoStartVmCache {

    /**
     * key -> 终端id,value -> 终端id
     * 设置30分钟后过期
     */
    private static final Cache<String, String> CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES).build();

    /**
     * 记录终端id
     *
     * @param terminalId 终端id
     * @Date 2022/1/18 15:46
     * @Author zjy
     **/
    public void addCache(String terminalId) {
        Assert.hasText(terminalId, "terminalId can not null");
        CACHE.put(terminalId, terminalId);
    }

    /**
     * 移除终端id
     *
     * @param terminalId 终端id
     * @Date 2022/1/18 15:49
     * @Author zjy
     **/
    public void deleteCache(String terminalId) {
        Assert.hasText(terminalId, "terminalId can not null");
        CACHE.invalidate(terminalId);
    }

    /**
     * 获取终端id
     *
     * @param terminalId 终端id
     * @return 返回值
     * @Date 2022/1/18 15:51
     * @Author zjy
     **/
    public String getCache(String terminalId) {
        Assert.hasText(terminalId, "terminalId can not null");
        return CACHE.getIfPresent(terminalId);
    }


}
