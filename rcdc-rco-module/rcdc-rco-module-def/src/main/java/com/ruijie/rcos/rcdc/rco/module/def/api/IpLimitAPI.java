package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IpLimitStrategyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/27 21:40
 *
 * @author yxq
 */
public interface IpLimitAPI {

    /**
     * 编辑VDI IP网段限制策略信息
     * @param ipLimitStrategyDTO 编辑的信息
     * @throws BusinessException 业务异常
     */
    void updateIpLimit(IpLimitStrategyDTO ipLimitStrategyDTO) throws BusinessException;

    /**
     * 获取VDI IP网段限制策略信息
     *
     * @return 限制策略信息
     */
    IpLimitStrategyDTO getIpLimitStrategy();

    /**
     * IP限制是否开启(有deskId优先查询云桌面策略配置)
     *
     * @param deskId 云桌面ID
     * @return 是否开启
     * @throws BusinessException 业务异常
     */
    Boolean isIpLimitEnable(@Nullable UUID deskId) throws BusinessException;


    /**
     * Ip是否被限制(有deskId优先查询云桌面策略配置)
     *
     * @param terminalIp 终端IP
     * @param deskId     云桌面ID
     * @return 是否被限制
     * @throws BusinessException 业务异常
     */
    boolean isIpLimited(String terminalIp, @Nullable UUID deskId) throws BusinessException;

    /**
     * Ip是否被限制(有deskId优先查询云桌面策略配置)
     *
     * @param terminalIp 终端IP
     * @param deskDTO     云桌面
     * @return 是否被限制
     * @throws BusinessException 业务异常
     */
    boolean isIpLimited(String terminalIp, @Nullable CbbDeskDTO deskDTO) throws BusinessException;

    /**
     * Ip是否被限制(有deskStrategyId优先查询云桌面策略配置)
     *
     * @param poolType poolType
     * @param terminalIp     终端IP
     * @param deskStrategyId 云桌面策略ID
     * @return 是否被限制
     * @throws BusinessException 业务异常
     */
    boolean isIpLimitedByDeskStrategy(CbbDesktopPoolType poolType, String terminalIp, UUID deskStrategyId) throws BusinessException;
}
