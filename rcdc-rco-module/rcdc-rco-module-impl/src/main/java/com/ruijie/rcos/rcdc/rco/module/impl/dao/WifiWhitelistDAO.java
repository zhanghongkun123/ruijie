package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.WifiWhitelistEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @ClassName: WifiWhitelistDAO
 * @Description: 白名单数据交互类
 * @author: zhiweiHong
 * @date: 2020/8/19
 **/
public interface WifiWhitelistDAO extends SkyEngineJpaRepository<WifiWhitelistEntity, UUID> {
    /**
     * 根据terminalGroupId删除
     *
     * @param terminalGroupId 终端组ID
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByTerminalGroupId(UUID terminalGroupId);

    /**
     * 根据terminalGroupId查询白名单集合
     *
     * @param terminalGroupId 终端组ID
     * @return wifiWhitelist
     */
    List<WifiWhitelistEntity> findByTerminalGroupIdOrderByIndexAsc(UUID terminalGroupId);
}
