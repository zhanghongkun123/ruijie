package com.ruijie.rcos.rcdc.rco.module.impl.openapi.service;

import com.ruijie.rcos.rcdc.rco.module.impl.openapi.entity.OpenApiTaskInfoEntity;

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
public interface OpenApiTaskInfoService {

    /**
     * 保存
     * @param entity 任务异常信息
     */
    void save(OpenApiTaskInfoEntity entity);

    /**
     * 基于ID查询任务异常信息
     * @param taskId 任务id
     * @return OpenApiTaskInfoEntity
     */
    OpenApiTaskInfoEntity findByTaskId(UUID taskId);

    /**
     * 基于业务id和任务状态查找任务列表
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
    Long findByActionAndTaskStateCount(String action, String taskState);

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
