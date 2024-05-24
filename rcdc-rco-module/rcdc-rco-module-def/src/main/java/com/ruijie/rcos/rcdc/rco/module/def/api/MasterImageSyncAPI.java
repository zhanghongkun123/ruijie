package com.ruijie.rcos.rcdc.rco.module.def.api;


import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbCrossClusterSyncImageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterCompleteImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterImageSyncPrepareRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
public interface MasterImageSyncAPI {


    /**
     * 主端准备
     * @param request request
     * @throws BusinessException 业务异常
     */
    void prepare(MasterImageSyncPrepareRequest request) throws BusinessException;


    /**
     * 主端开始镜像同步
     * @param request request
     * @throws BusinessException 业务异常
     */
    void sync(CbbCrossClusterSyncImageRequest request) throws BusinessException;


    /**
     * 主端完成镜像同步
     * @param request request
     * @throws BusinessException 业务异常
     */
    void complete(MasterCompleteImageSyncRequest request) throws BusinessException;
}
