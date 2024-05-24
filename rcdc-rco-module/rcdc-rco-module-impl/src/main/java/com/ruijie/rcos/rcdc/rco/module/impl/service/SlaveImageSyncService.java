package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.PublishSyncingImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.SlavePrepareImageSyncDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlaveRollbackImageSyncRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 从端准备动作
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
public interface SlaveImageSyncService {


    /**
     * 准备阶段
     *
     * @param request request
     * @throws BusinessException BusinessException
     */
    void prepareStage(SlavePrepareImageSyncDTO request) throws BusinessException;


    /**
     * 发布镜像模版
     *
     * @param request request
     * @throws BusinessException BusinessException
     */
    void publishStage(PublishSyncingImageTemplateDTO request) throws BusinessException;

    /**
     * 回滚从端镜像
     * @param request request
     * @throws  BusinessException BusinessException
     */
    void rollbackStage(SlaveRollbackImageSyncRequest request) throws BusinessException;
}
