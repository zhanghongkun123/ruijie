package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.TerminalTotalOnlineTimeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.TerminalOnlineTimeRecordDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.TerminalOnlineTimeRecordEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;

/**
 * Description:  统计终端在线时长业务类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/5
 *
 * @author xiao'yong'deng
 */
public interface TerminalOnlineTimeRecordService {

    /**
     * 查询终端运行记录数据
     * @param terminalId 终端id
     * @return 终端运行记录
     */
    TerminalOnlineTimeRecordEntity findByTerminalId(String terminalId);

    /**
     * 获取终端在线时长记录信息
     * @param macAddr mac地址
     * @return 终端运行记录
     */
    TerminalOnlineTimeRecordEntity findByMacAddr(String macAddr);

    /**
     * 保留终端在线信息，存在则更新，不存在则修改
     * @param terminalOnlineTimeRecordDTO 终端在线信息dto
     */
    void saveTerminalOnlineTimeRecord(TerminalOnlineTimeRecordDTO terminalOnlineTimeRecordDTO);

    /**
     * 统计各类型终端在线时间
     * @param platforms 终端类型
     * @return 返回总计数据
     */
    List<TerminalTotalOnlineTimeDTO> findTotalOnlineTime(List<String> platforms);

    /**
     * 终端在线更新在线时长表
     * @param terminalOnlineTimeRecordDTO  上线信息
     * @throws BusinessException 异常信息
     */
    void doUpdateByOnline(TerminalOnlineTimeRecordDTO terminalOnlineTimeRecordDTO) throws BusinessException;

    /**
     * 终端离线更新在线时长表
     * @param terminalId  终端id
     * @param hasServerOnline  服务器是否在线
     */
    void handleTerminalOfflineTime(String terminalId, Boolean hasServerOnline);

    /**
     * 定时器处理终端在线总时长
     */
    void handleTerminalOnlineTimeByQuartz();
}
