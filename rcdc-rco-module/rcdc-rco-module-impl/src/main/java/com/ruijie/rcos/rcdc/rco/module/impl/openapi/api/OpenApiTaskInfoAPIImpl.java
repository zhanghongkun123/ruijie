package com.ruijie.rcos.rcdc.rco.module.impl.openapi.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.dto.OpenApiTaskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.SaveOpenApiTaskInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.openapi.entity.OpenApiTaskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.openapi.service.OpenApiTaskInfoService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.*;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/12
 *
 * @author xiejian
 */
public class OpenApiTaskInfoAPIImpl implements OpenApiTaskInfoAPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(OpenApiTaskInfoAPIImpl.class);


    @Autowired
    private OpenApiTaskInfoService openApiTaskInfoService;

    @Override
    public void save(SaveOpenApiTaskInfoRequest request) {
        Assert.notNull(request, "request is not null");
        OpenApiTaskInfoEntity oldEntity = openApiTaskInfoService.findByTaskId(request.getTaskId());
        OpenApiTaskInfoEntity newEntity = new OpenApiTaskInfoEntity();
        if (!Objects.isNull(oldEntity)) {
            BeanUtils.copyProperties(oldEntity, newEntity);
            BeanUtils.copyProperties(request, newEntity);
        } else {
            BeanUtils.copyProperties(request, newEntity);
            newEntity.setCreateTime(new Date());
        }


        openApiTaskInfoService.save(newEntity);
    }

    @Override
    public OpenApiTaskInfoDTO findByTaskId(UUID taskId) {
        Assert.notNull(taskId, "taskId is not null");

        OpenApiTaskInfoEntity openApiTaskInfoEntity = openApiTaskInfoService.findByTaskId(taskId);
        OpenApiTaskInfoDTO dto = new OpenApiTaskInfoDTO();
        if (Objects.isNull(openApiTaskInfoEntity)) {
            return dto;
        }
        BeanUtils.copyProperties(openApiTaskInfoEntity, dto);
        return dto;
    }

    @Override
    public List<OpenApiTaskInfoDTO> findByBusinessIdAndTaskState(UUID businessId, String taskState) {
        Assert.notNull(businessId, "businessId is not null");
        Assert.notNull(taskState, "taskState is not null");

        List<OpenApiTaskInfoEntity> openApiTaskInfoEntityList = openApiTaskInfoService.findByBusinessIdAndTaskState(businessId, taskState);
        List<OpenApiTaskInfoDTO> dtoList = new ArrayList<>();
        if (Objects.isNull(openApiTaskInfoEntityList) || openApiTaskInfoEntityList.isEmpty()) {
            return dtoList;
        }
        openApiTaskInfoEntityList.forEach(openApiTaskInfoEntity -> {
            OpenApiTaskInfoDTO dto = new OpenApiTaskInfoDTO();
            BeanUtils.copyProperties(openApiTaskInfoEntity, dto);
            dtoList.add(dto);
        });

        return dtoList;
    }

    @Override
    public Long findByActionAndTaskStateCount(String action, String taskState) {
        Assert.notNull(action, "businessId is not null");
        Assert.notNull(taskState, "taskState is not null");
        return openApiTaskInfoService.findByActionAndTaskStateCount(action, taskState);
    }
}
