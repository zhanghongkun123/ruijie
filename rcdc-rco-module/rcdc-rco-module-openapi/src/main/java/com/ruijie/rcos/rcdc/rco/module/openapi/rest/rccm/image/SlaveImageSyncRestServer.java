package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.image;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ServerModel;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.PublishSyncingImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlaveCompareImageSnapshotRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlavePrepareImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlaveRollbackImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SlaveRollbackImageSyncResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description: 镜像同步从端处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
@OpenAPI
@Path("/v1/imagetemplates/slave")
@ServerModel(businessExKey = BusinessKey.RCDC_RCO_ONLY_VDI_MODEL_CAN_SYNC_IMAGE)
public interface SlaveImageSyncRestServer {

    /**
     * 从集群准备接口
     * @param request request
     * @throws BusinessException BusinessException
     */
    @POST
    @Path("/prepare")
    void prepare(SlavePrepareImageSyncRequest request) throws BusinessException;


    /**
     * 从集群发布镜像接口
     * @param request request
     * @throws BusinessException BusinessException
     */
    @POST
    @Path("/publish")
    void publish(PublishSyncingImageTemplateDTO request) throws BusinessException;


    /**
     * 从集群发布镜像接口
     * @param request request
     * @throws BusinessException BusinessException
     */
    @POST
    @Path("/rollback")
    void rollback(SlaveRollbackImageSyncRequest request) throws BusinessException;


    /**
     * 从集群发布镜像接口
     * @param request request
     * @throws BusinessException BusinessException
     * @return 处理结果
     */
    @POST
    @Path("/compare")
    SlaveRollbackImageSyncResponse compare(SlaveCompareImageSnapshotRequest request) throws BusinessException;
}
