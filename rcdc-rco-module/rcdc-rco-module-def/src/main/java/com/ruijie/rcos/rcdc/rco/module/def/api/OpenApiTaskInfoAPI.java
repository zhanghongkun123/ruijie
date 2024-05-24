package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.openapi.dto.OpenApiTaskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.SaveOpenApiTaskInfoRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/14
 *
 * @author xiejian
 */
public interface OpenApiTaskInfoAPI {

    /**
     * 保存任务异常信息
     * @param request 请求
     */
    void save(SaveOpenApiTaskInfoRequest request);

    /**
     * 根据任务id查找异常信息
     * @param taskId 任务id
     * @return dto
     */
    OpenApiTaskInfoDTO findByTaskId(@NotNull UUID taskId);

    /**
     * 根据业务id和任务状态查找任务列表
     * @param businessId 业务id
     * @param taskState 任务状态
     * @return 任务列表
     */
    List<OpenApiTaskInfoDTO> findByBusinessIdAndTaskState(@NotNull UUID businessId, @NotNull String taskState);

    /**
     * 根据业务类型和任务状态查找任务列表
     * @param action 任务类型
     * @param taskState 任务状态
     * @return 数量
     */
    Long findByActionAndTaskStateCount(@NotNull String action, @NotNull String taskState);
}
