package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTypeSupportOsVersionDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ImageTypeSupportOsVersionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageTypeSupportOsVersionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageTypeSupportOsVersionService;

/**
 * Description: 镜像类型支持操作系统版本配置表
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/15
 *
 * @author ypp
 */

@Service
public class ImageTypeSupportOsVersionServiceImpl implements ImageTypeSupportOsVersionService {

    @Autowired
    private ImageTypeSupportOsVersionDAO imageTypeSupportOsVersionDAO;

    @Override
    public List<ImageTypeSupportOsVersionDTO> getByImageTypeAndOsType(CbbImageType cbbImageType, CbbOsType cbbOsType) {
        Assert.notNull(cbbImageType, "cbbImageType must not be null");
        Assert.notNull(cbbOsType, "cbbOsType must not be null");
        
        List<ImageTypeSupportOsVersionEntity> imageTypeSupportOsVersionDTOList =
                imageTypeSupportOsVersionDAO.findByCbbImageTypeAndOsType(cbbImageType, cbbOsType);

        return convertToDto(imageTypeSupportOsVersionDTOList);

    }

    @Override
    public List<ImageTypeSupportOsVersionDTO> findAllImageTypeSupportOsVersionConfig() {
        
        List<ImageTypeSupportOsVersionEntity> imageTypeSupportOsVersionDTOList = imageTypeSupportOsVersionDAO.findAll();
        return convertToDto(imageTypeSupportOsVersionDTOList);

    }

    @Override
    public boolean hasImageSupportOsVersion(@Nullable List<ImageTypeSupportOsVersionDTO> imageTypeSupportOsVersionDTOList,
            ImageTypeSupportOsVersionDTO imageTypeSupportOsVersionDTO) {
        Assert.notNull(imageTypeSupportOsVersionDTO, "imageTypeSupportOsVersionDTO must not be null");

        // 未配置镜像类型支持操作系统版本数据则默认支持
        if (CollectionUtils.isEmpty(imageTypeSupportOsVersionDTOList)) {
            return true;
        }

        // 镜像操作系统版本为空时，默认支持
        if (StringUtils.isBlank(imageTypeSupportOsVersionDTO.getOsVersion())) {
            return true;
        }

        List<ImageTypeSupportOsVersionDTO> imageTypeSupportOsTypeDTOList =
                imageTypeSupportOsVersionDTOList.stream().filter(item -> item.getCbbImageType() == imageTypeSupportOsVersionDTO.getCbbImageType()
                        && item.getOsType() == imageTypeSupportOsVersionDTO.getOsType()).collect(Collectors.toList());

        // 没有配置对应操作系统类型，则默认支持
        if (CollectionUtils.isEmpty(imageTypeSupportOsTypeDTOList)) {
            return true;
        }

        // 有配置镜像类型支持的操作系统版本数据，仅支持配置的操作系统版本
        return imageTypeSupportOsTypeDTOList.stream().anyMatch(item -> item.getOsVersion().equals(imageTypeSupportOsVersionDTO.getOsVersion()));
    }

    private List<ImageTypeSupportOsVersionDTO> convertToDto(List<ImageTypeSupportOsVersionEntity> imageTypeSupportOsVersionDTOList) {
        if (CollectionUtils.isEmpty(imageTypeSupportOsVersionDTOList)) {
            return new ArrayList<>();
        }

        return imageTypeSupportOsVersionDTOList.stream().map(item -> {
            ImageTypeSupportOsVersionDTO imageTypeSupportOsVersionDTO = new ImageTypeSupportOsVersionDTO();
            BeanUtils.copyProperties(item, imageTypeSupportOsVersionDTO);
            return imageTypeSupportOsVersionDTO;
        }).collect(Collectors.toList());
    }
}
