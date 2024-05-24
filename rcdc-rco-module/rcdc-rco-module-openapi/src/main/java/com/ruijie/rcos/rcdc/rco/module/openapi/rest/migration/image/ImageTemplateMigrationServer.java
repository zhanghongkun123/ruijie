package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image;

import javax.ws.rs.*;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.*;
import com.ruijie.rcos.rcdc.task.ext.module.def.dto.StateMachineConfigAndBatchTaskItemDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.taskkit.flow.api.TaskExecuteResult;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年04月07日
 *
 * @author xgx
 */
@OpenAPI
@Path("/v1/migration/imageTemplate")
public interface ImageTemplateMigrationServer {
    /**
     * 创建镜像模版
     * 
     * @param createImageTemplateDTO 请求对象
     * @throws BusinessException 业务异常
     */
    @Path("/create")
    @POST
    void createImageTemplate(@NotNull CreateImageTemplateDTO createImageTemplateDTO) throws BusinessException;

    /**
     * 创建驱动安装记录
     * 
     * @param createImageTemplateDriverInstallRecordDTO 请求对象
     * @throws BusinessException 业务异常
     */
    @Path("/drive/create")
    @POST
    void createDriveInstallRecord(CreateImageTemplateDriverInstallRecordDTO createImageTemplateDriverInstallRecordDTO) throws BusinessException;

    /**
     * 分页查询镜像列表
     * 
     * @param request 请求对象
     * @return 镜像列表
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/list")
    PageQueryResponse<CbbImageTemplateDTO> pageQuery(PageServerRequest request) throws BusinessException;

    /**
     * 获取镜像模版信息
     * 
     * @param id 镜像模版名称
     * @return 镜像模版信息
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/{id}")
    CbbGetImageTemplateInfoDTO getDetail(@NotNull @PathParam("id") String id) throws BusinessException;

    /**
     * 编辑镜像
     * 
     * @param id 镜像id
     * @param editImageTemplateDTO 请求参数
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("/{id}")
    void editImageTemplate(@NotNull @PathParam("id") String id, EditImageTemplateDTO editImageTemplateDTO) throws BusinessException;

    /**
     * 发布镜像
     * 
     * @param id 镜像ID
     * @param taskDTO 任务对象
     * @throws BusinessException 异常
     */
    @PUT
    @ApiAction("publish")
    @Path("/{id}")
    void publishImageTemplate(@NotNull @PathParam("id") String id, StateMachineConfigAndBatchTaskItemDTO taskDTO) throws BusinessException;

    /**
     * 发布镜像
     * 
     * @param id 镜像ID
     * @param taskDTO 任务对象
     * @throws BusinessException 异常
     * @return 任务信息
     */
    @PUT
    @ApiAction("getTask")
    @Path("/{id}")
    TaskExecuteResult getTask(@NotNull @PathParam("id") String id, QueryImageOperateTaskDTO taskDTO) throws BusinessException;

    /**
     * 查询镜像的sunny安装状态
     *
     * @param oldImageId 镜像id
     * @return 任务结果
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/{imageId}/sunny/status")
    SunnyInstallResponse getSunnyStatus(@NotNull @PathParam("imageId") String oldImageId) throws BusinessException;


}
