package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.DesktopSessionLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDesktopPoolDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.UserConnectDesktopFaultLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.request.CreateConnectFaultLogRequest;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.request.StatisticDesktopPoolHistoryInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.response.StatisticDesktopPoolHistoryResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import java.util.UUID;

/**
 * Description: 桌面池报表相关API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/18 14:12
 *
 * @author yxq
 */
public interface DesktopPoolDashboardAPI {

    /**
     * 统计桌面池相关历史信息
     *
     * @param request 请求
     * @return 统计结果
     * @throws BusinessException 业务异常
     */
    StatisticDesktopPoolHistoryResponse statisticDesktopPoolUseHistory(StatisticDesktopPoolHistoryInfoRequest request) throws BusinessException;

    /**
     * 分页查询桌面池会话连接记录
     *
     * @param pageQueryRequest 分页查询条件
     * @return 分页查询结果
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<DesktopSessionLogDTO> pageDesktopPoolConnectHistory(PageQueryRequest pageQueryRequest) throws BusinessException;

    /**
     * 分页查询桌面池会话连接失败记录
     *
     * @param pageQueryRequest 分页查询条件
     * @return 分页查询结果
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<UserConnectDesktopFaultLogDTO> pageDesktopPoolConnectFailHistory(PageQueryRequest pageQueryRequest) throws BusinessException;

    /**
     * 获取当前时间桌面池的数量信息
     *
     * @param cbbDesktopPoolType 桌面池类型(VDI、第三方)
     * @param desktopPoolType 桌面池类型(动态、静态、普通)
     * @param deskPoolId      桌面池id
     * @return 桌面池的数量信息
     */
    StatisticDesktopPoolDesktopCountDTO getCurrentDesktopPoolInfo(CbbDesktopPoolType cbbDesktopPoolType,
                                                                  DesktopPoolType desktopPoolType, UUID deskPoolId);

    /**
     * 创建桌面池分配失败记录
     *
     * @param request 请求
     */
    void recordConnectFaultLog(CreateConnectFaultLogRequest request);

    /**
     * 修改会话连接表、连接失败表中的用户组名为新的名称
     *
     * @param userGroupId 用户组id
     * @param userGroupName 新的用户组名
     */
    void updateUserGroupName(UUID userGroupId, String userGroupName);

    /**
     * 修改会话连接表、连接失败表中的桌面池名称名为新的名称
     *
     * @param desktopPoolId 用户组id
     * @param desktopPoolName 新的桌面池名称
     */
    void updateDesktopPoolName(UUID desktopPoolId, String desktopPoolName);
}
