package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageTypeSupportTerminalEntity;

import java.util.List;

/**
 * Description: 镜像类型支持终端型号配置
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/15
 *
 * @author ypp
 */
public interface ImageTypeSupportTerminalService {

    /**
     * 镜像是否支持该终端
     * 
     * @param productType 终端型号
     * @param cbbImageType 镜像类型
     * @param cbbOsType 操作系统类型
     * @return boolean
     */
    boolean hasImageSupportTerminal(String productType, CbbImageType cbbImageType, CbbOsType cbbOsType);

    /**
     * 镜像是否支持该终端
     * 
     * @param imageTypeSupportTerminalEntityList 镜像支持终端列表
     * @param imageTypeSupportTerminalEntity 目标数据
     * @return boolean
     */
    boolean hasImageSupportTerminal(List<ImageTypeSupportTerminalEntity> imageTypeSupportTerminalEntityList,
            ImageTypeSupportTerminalEntity imageTypeSupportTerminalEntity);

    /**
     * 查询所有配置
     * 
     * @return List<ImageTypeSupportTerminalEntity>
     */
    List<ImageTypeSupportTerminalEntity> findAll();
}
