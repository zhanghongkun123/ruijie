package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.image;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbCrossClusterSyncImageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ServerModel;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterCompleteImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterImageSyncPrepareRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description: 镜像同步主端处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
@OpenAPI
@Path("/v1/imagetemplates/master")
@ServerModel(businessExKey = BusinessKey.RCDC_RCO_ONLY_VDI_MODEL_CAN_SYNC_IMAGE)
public interface MasterImageSyncRestServer {

    /**
     * 主集群准备接口
     *
     * @param request request
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/prepare")
    void prepare(MasterImageSyncPrepareRequest request) throws BusinessException;


    /**
     * 主集群准备接口
     *
     * @param request request
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/sync")
    void sync(CbbCrossClusterSyncImageRequest request) throws BusinessException;


    /**
     * 主集群完成镜像同步接口
     *
     * @param request request
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/complete")
    void complete(MasterCompleteImageSyncRequest request) throws BusinessException;


}
