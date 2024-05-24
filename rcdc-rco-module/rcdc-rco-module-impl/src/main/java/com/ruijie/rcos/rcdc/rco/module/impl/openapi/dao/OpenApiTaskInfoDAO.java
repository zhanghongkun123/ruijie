package com.ruijie.rcos.rcdc.rco.module.impl.openapi.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.openapi.entity.OpenApiTaskInfoEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/12
 *
 * @author xiejian
 */
public interface OpenApiTaskInfoDAO extends SkyEngineJpaRepository<OpenApiTaskInfoEntity, UUID> {

    /**
     * 根据task信息返回任务异常信息
     * @param taskId 任务id
     * @return 任务异常信息
     */
    OpenApiTaskInfoEntity findByTaskId(UUID taskId);

    /**
     * 根据业务id和任务状态返回任务列表
     * @param businessId 业务id
     * @param taskState 任务状态
     * @return 任务列表
     */
    List<OpenApiTaskInfoEntity> findByBusinessIdAndTaskState(UUID businessId, String taskState);

    /**
     * 根据业务类型和任务状态查找任务列表
     * @param action 任务类型
     * @param taskState 任务状态
     * @return 数量
     */
    Long countByActionAndTaskState(String action, String taskState);

    /**
     * 基于业务类型和任务状态查找任务列表
     * @param action 任务类型
     * @param taskState 任务状态
     * @return 任务列表
     */
    List<OpenApiTaskInfoEntity> findByActionAndTaskState(String action, String taskState);

    /**
     * 获取异步任务列表
     * @param taskState 任务状态
     * @return 列表
     */
    List<OpenApiTaskInfoEntity> findByTaskState(String taskState);
}
