package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbAbortEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.maintenance.module.def.constants.StopTaskAction;
import com.ruijie.rcos.rcdc.maintenance.module.def.dto.CbbTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoMaintainStopTaskHandler;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * 
 * Description: 终止本地提取任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/20
 *
 * @author zhiweiHong
 */
@Service
public class AbortLocalTerminalImageExtractTask implements RcoMaintainStopTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbortLocalTerminalImageExtractTask.class);

    @Autowired
    private ImageTemplateAPI imageTemplateAPI;


    @Override
    public boolean isSupport(CbbTaskDTO cbbTaskDTO) {
        Assert.notNull(cbbTaskDTO, "cbbTaskDto can not be null");

        return StopTaskAction.ABORT_LOCAL_IMAGE_EXTRACT_EDIT.equals(cbbTaskDTO.getStopTaskName());
    }

    @Override
    public void handle(CbbTaskDTO cbbTaskDTO)  {
        Assert.notNull(cbbTaskDTO, "cbbTaskDto can not be null");

        CbbAbortEditImageTemplateDTO request = JSON.parseObject(cbbTaskDTO.getStopTaskParams(), CbbAbortEditImageTemplateDTO.class);
        try {
            imageTemplateAPI.abortLocalTerminalImageExtract(request.getImageTemplateId());
        } catch (BusinessException e) {
            LOGGER.error("终止本地编辑[{}]异常", request.getImageTemplateId(), e);
        }
    }
}
