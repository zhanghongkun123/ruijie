package com.ruijie.rcos.rcdc.rco.module.impl.datasync.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchDataSyncLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.entity.DataSyncLogEntity;

import java.util.Date;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/25 16:19
 *
 * @author coderLee23
 */
public interface DataSyncLogService {

    /**
     * 保存日志
     * 
     * @param context 日志内容
     */
    void saveDataSyncLog(String context);

    /**
     *
     * 分页查询 同步日志 信息
     *
     * @param searchDataSyncLogDTO 查询实体
     * @param pageable 分页实体
     * @return Page<DataSyncLogEntity>
     */
    Page<DataSyncLogEntity> pageDataSyncLog(SearchDataSyncLogDTO searchDataSyncLogDTO, Pageable pageable);


    /**
     * 根据过期时间删除日志
     * 
     * @param expireTime 过期时间
     */
    void deleteByExpireTime(Date expireTime);

    /**
     * 根据偏移量
     * 
     * @param offset 偏移量
     * @return DataSyncLogEntity
     */
    DataSyncLogEntity findOneByOffset(Long offset);


    /**
     * 根据偏移量
     *
     * @return Integer
     */
    Long count();
}
