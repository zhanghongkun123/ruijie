package com.ruijie.rcos.rcdc.rco.module.impl.init.cpuarch;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOsFileMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetOsFileResultDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.osfile.CbbUpdateOsFileCpuArchDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.init.cpuarch.impl.IDVImageTemplateCpuArch;
import com.ruijie.rcos.rcdc.rco.module.impl.init.cpuarch.impl.VDIImageTemplateCpuArch;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;


/**
 * <br>
 * Description: 镜像文件初始化CPU架构
 * Copyright: Copyright (c) 2023 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2023.06.12 <br>
 *
 * @author lf
 */
@Service
public class OsFileCpuArchInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsFileCpuArchInitializer.class);

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private CbbOsFileMgmtAPI cbbOsFileMgmtAPI;

    @Autowired
    private IDVImageTemplateCpuArch idvImageTemplateCpuArch;

    @Autowired
    private VDIImageTemplateCpuArch vdiImageTemplateCpuArch;

    @Override
    public void safeInit() {
        LOGGER.info("OsFileCpuArchInitializer 开始");
        List<CbbGetOsFileResultDTO> osFileResultDTOList = cbbOsFileMgmtAPI.findByCpuArch(CbbCpuArchType.OTHER);
        LOGGER.info("镜像架构为OTHER的文件列表{}", JSON.toJSONString(osFileResultDTOList));
        osFileResultDTOList.forEach(osFileResultDTO -> {
            vdiImageTemplateCpuArch.checkAndUpdateCpuArchByImageFileName(osFileResultDTO.getImageFileName());
            idvImageTemplateCpuArch.checkAndUpdateCpuArchByImageFileName(osFileResultDTO.getImageFileName());
        } );
    }

    /**
     * 更新镜像文件的CPU架构
     * @param osFileId 镜像文件id
     * @param cpuArch cpu架构
     */
    private void updateOsFileCpuArch(UUID osFileId, CbbCpuArchType cpuArch) {
        CbbUpdateOsFileCpuArchDTO request = new CbbUpdateOsFileCpuArchDTO();
        request.setId(osFileId);
        request.setCpuArch(cpuArch);
        try {
            cbbOsFileMgmtAPI.updateOsFileCpuArch(request);
        } catch (BusinessException e) {
            LOGGER.info("更新镜像文件:[{}]的CPU架构异常", osFileId);
        }
    }

}
