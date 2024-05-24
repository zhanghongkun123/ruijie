package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageVmMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.TerminalVmModeTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AllDesktopOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AbnormalTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopUsageIdArrRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.UserBindDesktopNumDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageDayEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 12:17
 *
 * @author zhangyichi
 */
public interface DesktopService {

    /**
     * 获取集群云桌面概要数据
     * @return 集群云桌面概要数据
     * @throws BusinessException 业务异常
     */
    AllDesktopOverviewDTO getAllDesktopOverview() throws BusinessException;

    /**
     * 获取所有子分组的id
     * @param parentGroupId 父分组id
     * @return 各级子分组的id，包括传入的id
     * @throws BusinessException 业务异常
     */
    List<UUID> getChildGroupId(UUID parentGroupId) throws BusinessException;

    /**
     * 根据云桌面id获取指定时间内桌面资源平均使用情况
     * 从云桌面资源使用率日统计表中获取
     * @param request 云桌面ID数组请求
     * @return 资源使用率列表
     * @throws BusinessException 业务异常
     */
    List<DesktopUsageDTO> listDesktopDayUsage(DesktopUsageIdArrRequest request) throws BusinessException;

    /**
     * 根据云桌面id获取指定时间内桌面资源平均使用情况
     * @param requestIdArr 云桌面ID数组
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<DesktopUsageDTO> 返回结果
     * @throws BusinessException 业务异常
     */
    List<DesktopUsageDTO> listDesktopUsageById(UUID[] requestIdArr, Date startTime, Date endTime) throws BusinessException;

    /**
     * 按列资源类型和表类型查询使用率异常的云桌面
     * @param resourceType 资源类型
     * @param abnormalType 异常类型
     * @return 使用率异常的云桌面
     * @throws BusinessException 业务异常
     */
    List<DesktopResourceUsageDayEntity> listAbnormalDesktop(ResourceTypeEnum resourceType, AbnormalTypeEnum abnormalType) throws BusinessException;

    /**
     * 校验绑定镜像的云桌面是否有运行中
     * @param imageId 镜像id
     * @param imageName 镜像名称
     * @throws BusinessException 业务异常
     */
    void validateImageTemplateDesktopNotRunning(UUID imageId,String imageName) throws BusinessException;


    /**
     * 根据终端虚机运行策略类型判断是否存在该桌面
     * @param terminalVmModeType 终端全局桌面运行策略
     * @return 是否存在
     */
    boolean existDesktopByTerminalGlobalVmMode(TerminalVmModeTypeEnum terminalVmModeType);

    /**
     * 根据桌面ID列表查询，按桌用户ID进行分组统计绑定的桌面数量
     *
     * @param deskIdList 桌面Id列表
     * @return List<UserBindDesktopNumDTO> 统计列表
     */
    List<UserBindDesktopNumDTO> listUserBindDesktopNum(List<UUID> deskIdList);
}
