package com.ruijie.rcos.rcdc.rco.module.def.api;


import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoImageTemplateSnapshotDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.PublishSyncingImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlavePrepareImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlaveRollbackImageSyncRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
public interface SlaveImageSyncAPI {


    /**
     * 从端准备
     *
     * @param request request
     * @throws BusinessException 业务异常
     */
    void prepare(SlavePrepareImageSyncRequest request) throws BusinessException;


    /**
     * 从端发布镜像
     *
     * @param request request
     * @throws BusinessException 业务异常
     */
    void publish(PublishSyncingImageTemplateDTO request) throws BusinessException;


    /**
     * 回滚从端镜像
     *
     * @param request request
     * @throws BusinessException BusinessException
     */
    void rollback(SlaveRollbackImageSyncRequest request) throws BusinessException;


    /**
     * 获取和主端的差异快照集合
     *
     * @param masterSnapshotList 主端快照集合
     * @return 差异快照集合
     */
    List<RcoImageTemplateSnapshotDTO> getDiffSnapshotListCompareWithMaster(List<RcoImageTemplateSnapshotDTO> masterSnapshotList);
}
