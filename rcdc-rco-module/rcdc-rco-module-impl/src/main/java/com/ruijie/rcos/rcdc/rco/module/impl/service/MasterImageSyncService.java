package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbCrossClusterSyncImageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterCompleteImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterImageSyncPrepareRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 主端准备动作
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
public interface MasterImageSyncService {


    /**
     * 准备阶段
     *
     * @param request request
     * @throws BusinessException BusinessException
     */
    void prepareStage(MasterImageSyncPrepareRequest request) throws BusinessException;


    /**
     * 主端开始镜像同步
     * @param request request
     * @throws BusinessException BusinessException
     */
    void sync(CbbCrossClusterSyncImageRequest request) throws BusinessException;

    /**
     * 主端完成镜像同步
     * @param request request
     * @throws BusinessException 业务异常
     */
    void complete(MasterCompleteImageSyncRequest request) throws BusinessException;
}
