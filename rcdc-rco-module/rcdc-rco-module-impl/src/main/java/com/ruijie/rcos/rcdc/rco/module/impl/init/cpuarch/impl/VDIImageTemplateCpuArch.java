package com.ruijie.rcos.rcdc.rco.module.impl.init.cpuarch.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.impl.init.cpuarch.AbstractImageTemplateCpuArch;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年06月14日
 *
 * @author lf
 */
@Service
public class VDIImageTemplateCpuArch extends AbstractImageTemplateCpuArch {

    private static final Logger LOGGER = LoggerFactory.getLogger(VDIImageTemplateCpuArch.class);

    @Autowired
    private ComputerClusterServerMgmtAPI clusterMgmtAPI;

    private List<CbbImageType> cbbImageTypeList = Collections.singletonList(CbbImageType.VDI);

    @Override
    public CbbCpuArchType checkAndUpdateCpuArchByImageFileName(String imageFileName) {
        Assert.hasText(imageFileName, "imageFileName is empty");

        CbbCpuArchType imageCpuArchType;
        List<CbbImageTemplateDTO> imageList = getImageList(imageFileName, cbbImageTypeList);
        if (imageList.isEmpty()) {
            imageCpuArchType = CbbCpuArchType.OTHER;
        } else {
            imageCpuArchType = getCpuArchByImageList(imageList);
        }

        // 更新镜像的CPU架构
        if (CbbCpuArchType.OTHER != imageCpuArchType) {
            updateImageTemplateCpuArch(imageList, imageCpuArchType);
        }
        return imageCpuArchType;
    }


    private CbbCpuArchType getCpuArchByImageList(List<CbbImageTemplateDTO> imageList) {
        List<UUID> clusterIdList = imageList.stream().map(CbbImageTemplateDTO::getClusterId).collect(Collectors.toList());
        if (clusterIdList.isEmpty()) {
            return CbbCpuArchType.OTHER;
        }

        Map<CbbCpuArchType, UUID> cpuArchGroupMap = new HashMap<>();
        clusterIdList.forEach(clusterId -> {
            CbbCpuArchType clusterCpuArch;
            try {
                clusterCpuArch = getComputerClusterCpuArch(clusterId);
                cpuArchGroupMap.put(clusterCpuArch, clusterId);
            } catch (BusinessException e) {
                LOGGER.info("获取集群:[{}]的CPU架构异常", clusterId);
            }
        });

        // 只有ARM，没有X86
        if (cpuArchGroupMap.containsKey(CbbCpuArchType.ARM) && !cpuArchGroupMap.containsKey(CbbCpuArchType.X86_64)) {
            return CbbCpuArchType.ARM;
        }

        // 只有X86，没有ARM
        if (cpuArchGroupMap.containsKey(CbbCpuArchType.X86_64) && !cpuArchGroupMap.containsKey(CbbCpuArchType.ARM)) {
            return CbbCpuArchType.X86_64;
        }

        return CbbCpuArchType.OTHER;
    }

    /**
     * 获取集群的CPU架构
     * @param clusterId 集群ID
     * @return 集群CPU架构
     * @throws BusinessException 业务异常
     */
    private CbbCpuArchType getComputerClusterCpuArch(UUID clusterId) throws BusinessException {
        PlatformComputerClusterDTO computerClusterDTO = clusterMgmtAPI.getComputerClusterInfoById(clusterId);
        return CbbCpuArchType.convert(computerClusterDTO.getArchitecture());
    }

}
