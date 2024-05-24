package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.TerminalVmModeTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AllDesktopOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopNormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.CheckDesktopPortResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.AbnormalPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopOnlineSituationStatisticsRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopUsageIdArrRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetUserGroupOverviewRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ListDesktopByUserGroupPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AbnormalDesktopListResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DeskTopStatusResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DesktopOnlineSituationStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DesktopUsageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.ListDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.UserGroupOverviewResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;


/**
 * Description: 监控大屏，云桌面相关API
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 12:02
 *
 * @author zhangyichi
 */
public interface DesktopAPI {

    /**
     * 获取所有云桌面开机信息
     *
     * @return 云桌面开机信息响应
     * @throws BusinessException 异常
     */

    AllDesktopOverviewDTO getAllDesktopOverview() throws BusinessException;

    /**
     * 获取部门下所有云桌面一个月内各资源每天均值列表
     *
     * @param request 部门用户组ID
     * @return 部门云桌面总览
     * @throws BusinessException 业务异常
     */

    UserGroupOverviewResponse getUserGroupOverview(GetUserGroupOverviewRequest request) throws BusinessException;

    /**
     * 分页获取部门下云桌面列表
     *
     * @param request 分页请求
     * @return 云桌面列表
     * @throws BusinessException 业务异常
     */

    ListDesktopResponse listDesktopByUserGroup(ListDesktopByUserGroupPageRequest request) throws BusinessException;

    /**
     * 根据云桌面id获取桌面资源使用情况
     *
     * @param request 云桌面ID数组
     * @return 云桌面使用情况
     * @throws BusinessException 业务异常
     */

    DesktopUsageResponse getDesktopUsage(DesktopUsageIdArrRequest request) throws BusinessException;

    /**
     * 获取云桌面正太分布数据
     *
     * @param resourceType 请求资源类型
     * @return DesktopNormalDistributionDTO 返回
     * @throws BusinessException 业务异常
     */

    DesktopNormalDistributionDTO getDesktopNormalDistribution(ResourceTypeEnum resourceType) throws BusinessException;

    /**
     * 查询异常关注虚机列表
     *
     * @param request 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */

    AbnormalDesktopListResponse listAbnormalDesktop(AbnormalPageRequest request) throws BusinessException;

    /**
     * 校验绑定镜像的云桌面是否有运行中
     *
     * @param imageId   镜像id
     * @param imageName 镜像名称
     * @throws BusinessException 业务异常
     */
    void validateImageTemplateDesktopNotRunning(UUID imageId, String imageName) throws BusinessException;

    /**
     * 远程协助是否自动接受
     *
     * @param deskId 桌面id
     * @return true 自动接受
     * @throws BusinessException 业务异常
     */
    boolean isRemoteAssistAutoAgree(UUID deskId) throws BusinessException;

    /**
     * 是否是已请求远程协助
     * @param deskId 桌面id
     * @return ture/false
     */
    boolean isRequestRemoteAssist(UUID deskId);

    /**
     * 查询桌面连接信息，是否还存在连接使用桌面中
     *
     * @param desktopId 桌面id
     * @return ture存在连接连着桌面。false不存在连接。
     * @throws BusinessException 业务异常
     */
    boolean isAnyConnectedChannel(UUID desktopId) throws BusinessException;

    /**
     * 记录云桌面关机时间
     * @param deskId 云桌面id
     * @param shutOvertime 超时关机时长分钟
     */
    void recordDeskShutdownDate(UUID deskId, Integer shutOvertime);

    /**
     * 获取将要关机云桌面Id
     * @return 云桌面Id列表
     */
    List<UUID> getDeskShutdownIdList();

    /**
     * 清除云桌面关机时间
     * @param deskId 桌面id
     */
    void clearDeskShutdownDate(UUID deskId);

    /**
     * 重置云桌面关机时间
     * @param shutOvertime 超时关机时长分钟
     */
    void resetDeskShutdownDate(Integer shutOvertime);

    /**
     * 变更桌面所属云桌面分组
     *
     * @param deskId 桌面id
     * @param groupId 桌面组id
     * @throws BusinessException 业务异常
     */
    void removeDesktopByGroupId(UUID deskId, UUID groupId) throws BusinessException;

    /**
     * 统计云桌面状态数量
     * @return  返回云桌面状态数量
     */
    DeskTopStatusResponse statisticsDeskTopStatus();

    /**
     * 统计云桌面历史运行状态
     * @param request  云桌面历史情况统计请求
     * @return  返回统计结果
     * @throws BusinessException  业务异常
     */
    DesktopOnlineSituationStatisticsResponse statisticsDesktopHistoryOnlineSituation(DesktopOnlineSituationStatisticsRequest request)
        throws BusinessException;

    /**
     * 检测云桌面端口
     * @param desktopIp desktopIp
     * @param needCheckPortList needCheckPortList
     * @return  返回检测结果
     */
    CheckDesktopPortResultDTO checkDesktopPort(String desktopIp, List<Integer> needCheckPortList);

    /**
     * 根据终端虚机运行策略类型判断是否存在该桌面
     * @param terminalVmModeType 终端全局桌面运行策略
     * @return 是否存在
     */
    boolean existDesktopByTerminalGlobalVmMode(TerminalVmModeTypeEnum terminalVmModeType);

    /**
     * 校验计算机名称
     * @param deskId 桌面ID
     * @param computerName 计算机名称
     * @param osType 系统类型
     * @throws BusinessException 业务异常
     */
    void validateComputerName(UUID deskId, String computerName, CbbOsType osType) throws BusinessException;


    /**
     * 根据桌面ID列表查询，按用户ID进行分组统计，是否有用户绑定桌面数量>1
     *
     * @param deskIdList 桌面ID列表
     * @return true 存在有个用户绑定了多个桌面
     */
    boolean existUserBindMoreDesktop(List<UUID> deskIdList);

    /**
     * @param deskId 桌面id
     * @throws BusinessException 异常
     */
    void throwExceptionWhenNotExist(UUID deskId) throws BusinessException;

    /**
     * 根据多会话桌面id获取与其关联的用户id列表
     *
     * @param deskId 桌面id
     * @return 用户id列表
     */
    List<UUID> findUserIdByDeskId(UUID deskId);
}