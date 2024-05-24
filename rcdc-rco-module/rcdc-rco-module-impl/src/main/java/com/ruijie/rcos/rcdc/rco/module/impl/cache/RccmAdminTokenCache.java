package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/12/22 14:48
 *
 * @author yxq
 */
@Service
public class RccmAdminTokenCache {

    /**
     * 格式化key，拼接为“管理员名称_子系统”。如果子系统为空，则为“管理员名称_null”
     */
    private static final String KEY_FORMAT = "%s_%s";

    private static final Map<String, CacheInfo> CACHE = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(RccmAdminTokenCache.class);

    /**
     * 添加缓存信息
     *
     * @param adminName 管理员名称
     * @param subSystem 子系统
     * @param cacheInfo 缓存信息
     */
    public void addCache(String adminName, @Nullable SubSystem subSystem, CacheInfo cacheInfo) {
        Assert.hasText(adminName, "adminName must has text");
        Assert.notNull(cacheInfo, "cacheInfo must not be null");

        LOGGER.debug("管理员[{}]保存子系统[{}]新的token信息[{}]", adminName, subSystem, JSON.toJSONString(cacheInfo));
        CACHE.put(getFormattedKey(adminName, subSystem), cacheInfo);
    }

    /**
     * 获取缓存信息
     *
     * @param adminName 管理员名称
     * @param subSystem 子系统
     * @return 缓存信息
     */
    public CacheInfo getCache(String adminName, @Nullable SubSystem subSystem) {
        Assert.hasText(adminName, "adminName must has text");

        return CACHE.get(getFormattedKey(adminName, subSystem));
    }

    private String getFormattedKey(String adminName, SubSystem subSystem) {
        return String.format(KEY_FORMAT, adminName, subSystem);
    }

    /**
     * 缓存信息
     */
    public static class CacheInfo {

        /**
         * 回话id。由rcenter生成，管理员每次在rcenter登录都会生成一个新的sessionId
         */
        private String sessionId;

        /**
         * 身份中心生成的token信息
         */
        private String iacToken;

        public CacheInfo() {
        }

        public CacheInfo(String sessionId, String iacToken) {
            this.sessionId = sessionId;
            this.iacToken = iacToken;
        }

        public String getIacToken() {
            return iacToken;
        }

        public void setIacToken(String iacToken) {
            this.iacToken = iacToken;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }
}
