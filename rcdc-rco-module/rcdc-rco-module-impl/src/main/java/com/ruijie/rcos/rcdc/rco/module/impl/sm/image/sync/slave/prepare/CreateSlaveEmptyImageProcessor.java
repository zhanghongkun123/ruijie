package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.slave.prepare;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbCreateEmptyImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageVmHost;
import com.ruijie.rcos.rcdc.rco.module.common.sm.AbstractTaskExecuteProcessor;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.SlavePrepareImageSyncDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static com.ruijie.rcos.rcdc.hciadapter.module.def.constants.Constants.DEFAULT_PLATFORM_ID;

/**
 * Description: 创建空的镜像模版
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
@Service
public class CreateSlaveEmptyImageProcessor extends AbstractTaskExecuteProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateSlaveEmptyImageProcessor.class);

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Autowired
    private PrepareStageContextResolver resolver;

    private static final String TASK_KEY = "create_empty_image";

    @Override
    protected StateTaskHandle.ProcessResult innerDoProcess(StateTaskHandle.StateProcessContext context, UUID taskId) throws BusinessException {


        SlavePrepareImageSyncDTO dto = resolver.resolveDTO(context);

        boolean isExist = imageTemplateMgmtAPI.existsImageTemplate(dto.getImageId());
        if (isExist) {
            return StateTaskHandle.ProcessResult.next();
        }

        CbbCreateEmptyImageTemplateDTO request = new CbbCreateEmptyImageTemplateDTO();
        BeanUtils.copyProperties(dto, request);
        request.setImageTemplateId(dto.getImageId());
        request.setTaskId(taskId);
        request.setImageSync(true);
        request.setCbbOsType(dto.getOsType());
        request.setCbbImageType(CbbImageType.VDI);
        request.setEnableMultipleVersion(true);
        request.setImageVmHost(ImageVmHost.SERVER);
        request.setDiskController(dto.getDiskController());
        if (Objects.isNull(dto.getPlatformId())) {
            request.setPlatformId(DEFAULT_PLATFORM_ID);
        }
        LOGGER.info("创建空白镜像请求[{}]", JSON.toJSONString(request));
        imageTemplateMgmtAPI.createEmptyImage(request);
        resolver.setIsFirstCreateFlag(context, true);
        return StateTaskHandle.ProcessResult.next();
    }



    @Override
    protected String getTaskKey() {
        return TASK_KEY;
    }
}
