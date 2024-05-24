package com.ruijie.rcos.rcdc.rco.module.impl.openapi.service.impl;

import com.ruijie.rcos.rcdc.rco.module.impl.openapi.dao.OpenApiTaskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.openapi.entity.OpenApiTaskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.openapi.service.OpenApiTaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: OpenApiTaskInfoService
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月12日
 *
 * @author xiejian
 */
@Service
public class OpenApiTaskInfoServiceImpl implements OpenApiTaskInfoService {

    @Autowired
    private OpenApiTaskInfoDAO openApiTaskInfoDAO;

    @Override
    public void save(OpenApiTaskInfoEntity entity) {
        Assert.notNull(entity, "entity is not null");

        openApiTaskInfoDAO.save(entity);
    }

    @Override
    public OpenApiTaskInfoEntity findByTaskId(UUID taskId) {
        Assert.notNull(taskId, "taskId is not null");

        return openApiTaskInfoDAO.findByTaskId(taskId);
    }

    @Override
    public List<OpenApiTaskInfoEntity> findByBusinessIdAndTaskState(UUID businessId, String taskState) {
        Assert.notNull(businessId, "businessId is not null");
        Assert.notNull(taskState, "taskState is not null");

        return openApiTaskInfoDAO.findByBusinessIdAndTaskState(businessId, taskState);
    }

    @Override
    public Long findByActionAndTaskStateCount(String action, String taskState) {
        Assert.notNull(action, "action is not null");
        Assert.notNull(taskState, "taskState is not null");

        return openApiTaskInfoDAO.countByActionAndTaskState(action, taskState);
    }

    @Override
    public List<OpenApiTaskInfoEntity> findByActionAndTaskState(String action, String taskState) {
        Assert.notNull(action, "action is not null");
        Assert.notNull(taskState, "taskState is not null");
        return openApiTaskInfoDAO.findByActionAndTaskState(action, taskState);
    }

    @Override
    public List<OpenApiTaskInfoEntity> findByTaskState(String taskState) {
        Assert.notNull(taskState, "taskState is not null");
        return openApiTaskInfoDAO.findByTaskState(taskState);
    }
}
