package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.TerminalTotalOnlineTimeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.TerminalOnlineTimeRecordEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * Description: 终端在线时长dao层类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/5
 *
 * @author xiao'yong'deng
 */
public interface TerminalOnlineTimeRecordDAO extends SkyEngineJpaRepository<TerminalOnlineTimeRecordEntity, UUID> {

    /**
     * 获取在线终端组数量
     * @param hasOnline 是否在线
     * @return  在线总条数
     */
    int countByHasOnline(Boolean hasOnline);

    /**
     * * 根据终端id查询绑定的 TerminalOnlineTimeRecordEntity
     *
     * @param terminalId 终端id
     * @return 返回TerminalOnlineTimeRecordEntity对象
     */
    TerminalOnlineTimeRecordEntity findByTerminalId(String terminalId);

    /**
     * * 根据终端id查询绑定的 UserDesktopEntity
     *
     * @param macAddr 终端mac
     * @return 返回TerminalOnlineTimeRecordEntity对象
     */
    TerminalOnlineTimeRecordEntity findByMacAddr(String macAddr);

    /**
     * 统计各类型终端在线时间
     * @param platforms 终端类型
     * @return 返回总计数据
     */
    @Query(value = "select new com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.TerminalTotalOnlineTimeDTO(platform, sum(onlineTotalTime)) " +
        "from TerminalOnlineTimeRecordEntity where platform in (?1) group by platform")
    List<TerminalTotalOnlineTimeDTO> findTotalOnlineTimeByPlatform(List<String> platforms);
}
