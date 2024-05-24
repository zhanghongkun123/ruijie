package com.ruijie.rcos.rcdc.rco.module.impl.init.cpuarch.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ClusterMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.init.cpuarch.AbstractImageTemplateCpuArch;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
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
public class IDVImageTemplateCpuArch extends AbstractImageTemplateCpuArch {

    private static final Logger LOGGER = LoggerFactory.getLogger(IDVImageTemplateCpuArch.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ClusterMgmtAPI clusterMgmtAPI;

    @Autowired
    private ViewTerminalDAO viewTerminalDAO;

    private List<CbbImageType> cbbImageTypeList = Arrays.asList(CbbImageType.IDV, CbbImageType.VOI);

    @Override
    public CbbCpuArchType checkAndUpdateCpuArchByImageFileName(String imageFileName) {
        Assert.hasText(imageFileName, "imageFileName is empty");

        // 由于当前IDV不支持ARM，因此IDV镜像默认度是X86架构
        CbbCpuArchType imageCpuArch = CbbCpuArchType.X86_64;
        List<CbbImageTemplateDTO> imageList = getImageList(imageFileName, cbbImageTypeList);
        if (imageList.isEmpty()) {
            return imageCpuArch;
        }

        // 更新镜像的CPU架构
        updateImageTemplateCpuArch(imageList, imageCpuArch);
        return imageCpuArch;
    }
}
