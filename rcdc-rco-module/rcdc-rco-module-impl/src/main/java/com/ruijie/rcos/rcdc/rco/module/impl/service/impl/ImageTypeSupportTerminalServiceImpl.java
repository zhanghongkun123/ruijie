package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ImageTypeSupportTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageTypeSupportTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageTypeSupportTerminalService;

/**
 * Description: 镜像类型支持终端型号配置
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/15
 *
 * @author ypp
 */
@Service
public class ImageTypeSupportTerminalServiceImpl implements ImageTypeSupportTerminalService {

    @Autowired
    private ImageTypeSupportTerminalDAO imageTypeSupportTerminalDAO;



    @Override
    public boolean hasImageSupportTerminal(@Nullable String productType, CbbImageType cbbImageType, CbbOsType cbbOsType) {
        Assert.notNull(cbbImageType, "cbbImageType must not be null");
        Assert.notNull(cbbOsType, "cbbOsType must not be null");


        List<ImageTypeSupportTerminalEntity> imageTypeSupportTerminalEntityList =
                imageTypeSupportTerminalDAO.findByCbbImageTypeAndOsType(cbbImageType, cbbOsType);

        return compareConfig(imageTypeSupportTerminalEntityList, productType);
    }


    @Override
    public boolean hasImageSupportTerminal(@Nullable List<ImageTypeSupportTerminalEntity> imageTypeSupportTerminalEntityList,
            ImageTypeSupportTerminalEntity imageTypeSupportTerminalEntity) {
        Assert.notNull(imageTypeSupportTerminalEntity, "imageTypeSupportTerminalEntity must not be null");

        // 未配置默认都支持
        if (CollectionUtils.isEmpty(imageTypeSupportTerminalEntityList)) {
            return true;
        }

        List<ImageTypeSupportTerminalEntity> imageSupportTerminalEntityList = imageTypeSupportTerminalEntityList.stream()
                .filter(item -> item.getCbbImageType().equals(imageTypeSupportTerminalEntity.getCbbImageType())
                        && item.getOsType().equals(imageTypeSupportTerminalEntity.getOsType()))
                .collect(Collectors.toList());


        return compareConfig(imageSupportTerminalEntityList, imageTypeSupportTerminalEntity.getProductType());
    }

    @Override
    public List<ImageTypeSupportTerminalEntity> findAll() {
        return imageTypeSupportTerminalDAO.findAll();
    }

    private boolean compareConfig(List<ImageTypeSupportTerminalEntity> imageTypeSupportTerminalEntityList, @Nullable String productType) {

        // 未配置默认都支持
        if (CollectionUtils.isEmpty(imageTypeSupportTerminalEntityList)) {
            return true;
        }

        // 有配置，但是目标终端类型为空认为不支持
        if (StringUtils.isBlank(productType)) {
            return false;
        }


        return imageTypeSupportTerminalEntityList.stream().anyMatch(item -> item.getProductType().equals(productType));
    }


}
