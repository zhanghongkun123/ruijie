package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 缓存请求云桌面操作参数，用于处理泽塔回调的接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/3/21
 *
 * @author Jarman
 */
@Service
public class DesktopOperateRequestCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopOperateRequestCache.class);

    /** key -> 桌面id ,value -> 请求参数对象 */
    private static final Map<UUID, DesktopRequestDTO> CACHE = new ConcurrentHashMap<>();

    /**
     * 添加缓存
     * 
     * @param id id
     * @param cache 缓存对象
     */
    public void addCache(UUID id, DesktopRequestDTO cache) {
        Assert.notNull(id, "id can not null");
        Assert.notNull(cache, "cache can not null");
        CACHE.put(id, cache);
    }

    /**
     * 移除缓存
     * 
     * @param id id
     */
    public void removeCache(UUID id) {
        Assert.notNull(id, "id can not null");
        CACHE.remove(id);
    }

    /**
     * 根据终端id删除缓存
     * 
     * @param terminalId 终端id
     */
    public void removeByTerminalId(String terminalId) {
        Assert.hasText(terminalId, "terminalId cannot empty");
        LOGGER.info("removeByTerminalId删除前，桌面操作缓存数：{}", CACHE.size());
        Iterator<Map.Entry<UUID, DesktopRequestDTO>> iterator = CACHE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, DesktopRequestDTO> entry = iterator.next();
            DesktopRequestDTO desktopRequestDTO = entry.getValue();
            CbbDispatcherRequest request = desktopRequestDTO.getCbbDispatcherRequest();
            if (request != null && terminalId.equals(request.getTerminalId())) {
                LOGGER.info("removeByTerminalId删除桌面终端[{}]缓存", terminalId);
                iterator.remove();
            }
        }
        LOGGER.info("removeByTerminalId删除后当前桌面操作缓存数：{}", CACHE.size());
    }

    /**
     * 获取缓存
     *
     * @param id id
     * @return 返回缓存对象
     */
    public DesktopRequestDTO getCache(UUID id) {
        Assert.notNull(id, "id can not null");
        return CACHE.get(id);
    }
}
