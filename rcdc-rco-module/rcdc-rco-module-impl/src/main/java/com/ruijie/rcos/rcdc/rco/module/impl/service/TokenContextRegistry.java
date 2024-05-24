package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TokenContextDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年03月17日
 *
 * @author xiejian
 */
@Service
public class TokenContextRegistry {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenContextRegistry.class);

    private Map<UUID, TokenContextDTO> tokenMap = new ConcurrentHashMap<>();

    /**
     * 登录时保存token信息
     * @param token token信息
     */
    public void addToken(TokenContextDTO token) {
        Assert.notNull(token, "token is not null");
        tokenMap.put(token.getToken(), token);
    }

    /**
     * 登录时保存token信息
     * @param token token信息
     * @return TokenContextDTO
     */
    public TokenContextDTO findToken(UUID token) {
        Assert.notNull(token, "token is not null");
        return tokenMap.get(token);
    }

    /**
     * 退出时移除token信息
     * @param token token
     */
    public void removeToken(UUID token) {
        Assert.notNull(token, "token is not null");
        tokenMap.remove(token);
    }

    /**
     * 清除失效的token
     */
    public void cleanInvalidToken() {
        for (Map.Entry<UUID, TokenContextDTO> tokenEntry : tokenMap.entrySet()) {
            TokenContextDTO tokenEntryValue = tokenEntry.getValue();
            //判断时间是否失效
            if (tokenEntryValue.getInvalidDate().isBefore(LocalDateTime.now())) {
                removeToken(tokenEntryValue.getToken());
            }
        }
    }
}
