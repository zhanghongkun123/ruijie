package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.slave.prepare;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbConfigVmForEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuModelType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.common.sm.AbstractTaskExecuteProcessor;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.SlavePrepareImageSyncDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.VgpuExtraInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: 配置镜像模版
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
@Service
public class ConfigImageProcessor extends AbstractTaskExecuteProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigImageProcessor.class);

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Autowired
    private PrepareStageContextResolver resolver;

    private static final String TASK_KEY = "config_image";

    @Override
    protected StateTaskHandle.ProcessResult innerDoProcess(StateTaskHandle.StateProcessContext context, UUID taskId) throws BusinessException {

        SlavePrepareImageSyncDTO dto = resolver.resolveDTO(context);

        CbbConfigVmForEditImageTemplateDTO request = new CbbConfigVmForEditImageTemplateDTO();
        request.setCpuCoreCount(dto.getVmCpuCoreCount());
        request.setImageTemplateId(dto.getImageId());
        request.setEnableNested(dto.getEnableNested());
        request.setEnableSoftwareDecode(dto.getEnableSoftwareDecode());
        request.setEnableVirtualEmulation(dto.getEnableVirtualEmulation());
        request.setMemorySize(dto.getVmMemorySize());
        request.setComputerName(dto.getComputerName());
        request.setVgpuInfoDTO(buildVGpuInfoDTO(dto.getVgpuType().toString(), JSON.parseObject(dto.getVgpuExtraInfo(), VgpuExtraInfoDTO.class)));
        request.setDiskSize(Objects.isNull(dto.getPublishSnapshot()) ? dto.getSystemDiskSize() : dto.getPublishSnapshot().getSystemDiskSize());
        request.setForceExpand(true);


        LOGGER.info("配置镜像模版[{}]", JSON.toJSONString(request));
        imageTemplateMgmtAPI.configVmForEditImageTemplate(request);
        return StateTaskHandle.ProcessResult.next();
    }

    @Override
    protected Boolean isSkipProcess(StateTaskHandle.StateProcessContext context) throws BusinessException {
        // 首次创建无需配置镜像模版
        return resolver.isFirstCreateFlag(context);
    }

    private static VgpuInfoDTO buildVGpuInfoDTO(String vgpuType, VgpuExtraInfoDTO vgpuExtraInfoDTO) {

        VgpuInfoDTO vgpuInfoDTO = new VgpuInfoDTO();
        Long vgpuSize = vgpuExtraInfoDTO.getGraphicsMemorySize();
        if (StringUtils.isEmpty(vgpuType) || VgpuType.QXL.name().equals(vgpuType) || Objects.isNull(vgpuSize)) {
            return vgpuInfoDTO;
        }
        vgpuInfoDTO.setVgpuType(VgpuType.valueOf(vgpuType));
        VgpuExtraInfo vgpuExtraInfo = new VgpuExtraInfo();
        vgpuExtraInfo.setGraphicsMemorySize(vgpuSize);
        vgpuExtraInfo.setVgpuModelType(Objects.isNull(vgpuExtraInfoDTO.getVgpuModelType()) ? VgpuModelType.G : vgpuExtraInfoDTO.getVgpuModelType());
        vgpuExtraInfo.setModel(vgpuExtraInfoDTO.getModel());
        vgpuExtraInfo.setParentGpuModel(vgpuExtraInfoDTO.getParentGpuModel());
        vgpuInfoDTO.setVgpuExtraInfo(vgpuExtraInfo);
        return vgpuInfoDTO;
    }

    @Override
    protected String getTaskKey() {
        return TASK_KEY;
    }
}
