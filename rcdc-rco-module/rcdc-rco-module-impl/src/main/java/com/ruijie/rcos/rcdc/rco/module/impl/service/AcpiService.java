package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.DeskDiskExpectDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: AcpiUtils服务类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/6
 *
 * @author zwf
 */
public interface AcpiService {
    /**
     * 获取ACPI
     *
     * @param userEntity        userEntity
     * @param userDesktopEntity userDesktopEntity
     * @param deskVDI           VDI桌面信息
     * @return acpi
     * @throws BusinessException BusinessException
     */
    String genAcpiPara(@Nullable RcoViewUserEntity userEntity, UserDesktopEntity userDesktopEntity,
                       CbbDeskDTO deskVDI) throws BusinessException;

    /**
     * 获取ACPI
     *
     * @param userEntity        userEntity
     * @param userDesktopEntity userDesktopEntity
     * @param deskVDI           VDI桌面信息
     * @param deskDiskExpectDetailDTO deskDiskExpectDetailDTO
     * @return acpi
     * @throws BusinessException BusinessException
     */
    String genAcpiPara(@Nullable IacUserDetailDTO userEntity, UserDesktopEntity userDesktopEntity,
                       CbbDeskDTO deskVDI, @Nullable DeskDiskExpectDetailDTO deskDiskExpectDetailDTO) throws BusinessException;

    /**
     * 获取UPM启动机制的code
     *
     * @param rcoDeskInfoEntity 桌面信息
     * @param restorePointId    镜像快照点ID
     * @param userType          用户类型
     * @param desktopPoolType   池桌面类型
     * @return UPM启动机制的code
     */
    Integer getUpmPolicyEnable(@Nullable RcoDeskInfoEntity rcoDeskInfoEntity, UUID restorePointId,
                               IacUserTypeEnum userType, @Nullable DesktopPoolType desktopPoolType);
}
