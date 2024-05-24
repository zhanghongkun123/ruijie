package com.ruijie.rcos.rcdc.rco.module.impl.init.cpuarch;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 *
 * Description: 镜像模板CPU架构更新
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/14
 *
 * @author lf
 */
public abstract class AbstractImageTemplateCpuArch {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImageTemplateCpuArch.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    /**
     * 填充镜像模板的CPU类型
     * @param imageFileName 镜像文件名称
     * @return CbbCpuArchType cpu架构
     */
    protected abstract CbbCpuArchType checkAndUpdateCpuArchByImageFileName(String imageFileName);

    protected void updateImageTemplateCpuArch(List<CbbImageTemplateDTO> imageList,
                                            CbbCpuArchType imageCpuArchType) {
        Assert.notEmpty(imageList, "imageList is empty");
        Assert.notNull(imageCpuArchType, "imageCpuArchType is empty");

        List<UUID> imageIdList = imageList.stream().map(CbbImageTemplateDTO::getId).collect(Collectors.toList());
        LOGGER.info("镜像idList:{}, 更新的CPU架构类型:{}", JSON.toJSONString(imageIdList), imageCpuArchType);
        cbbImageTemplateMgmtAPI.updateCpuArchByIdList(imageIdList, imageCpuArchType);
    }

    protected List<CbbImageTemplateDTO> getImageList(String imageFileName, List<CbbImageType> imageTypeList) {
        Assert.hasText(imageFileName, "imageFileName is empty");
        Assert.notEmpty(imageTypeList, "imageTypeList is empty");

        return cbbImageTemplateMgmtAPI.listByImageFileNameAndCbbImageType(
                imageFileName, imageTypeList);
    }

}
