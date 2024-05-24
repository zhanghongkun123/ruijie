package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTypeSupportOsVersionDTO;

/**
 * Description: 镜像类型支持操作系统版本配置表
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/15
 *
 * @author ypp
 */
public interface ImageTypeSupportOsVersionService {


    /**
     * 获取镜像类型支持的操作系统版本号
     *
     * @param cbbImageType 镜像类型
     * @param cbbOsType 操作系统
     * @return List<ImageTypeSupportOsVersionDTO>
     */
    List<ImageTypeSupportOsVersionDTO> getByImageTypeAndOsType(CbbImageType cbbImageType, CbbOsType cbbOsType);

    /**
     * 查询所有镜像支持操作系统版本配置
     * 
     * @return List<ImageTypeSupportOsVersionDTO>
     */
    List<ImageTypeSupportOsVersionDTO> findAllImageTypeSupportOsVersionConfig();

    /**
     * 镜像是否支持该操作系统版本
     * 
     * @param imageTypeSupportOsVersionDTOList 镜像支持操作系统列表
     * @param imageTypeSupportOsVersionDTO 镜像
     * @return boolean
     */
    boolean hasImageSupportOsVersion(@Nullable List<ImageTypeSupportOsVersionDTO> imageTypeSupportOsVersionDTOList,
                                     ImageTypeSupportOsVersionDTO imageTypeSupportOsVersionDTO);
}
