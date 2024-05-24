package com.ruijie.rcos.rcdc.rco.module.def.api;

import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DataSyncLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchDataSyncLogDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/22 12:11
 *
 * @author coderLee23
 */
public interface DataSyncLogAPI {

    /**
     * 
     * 分页查询 同步日志 信息
     *
     *
     * @param searchDataSyncLogDTO 查询实体
     * @param pageable 分页实体
     * @return DefaultPageResponse<DataSyncLogDTO>
     */
    DefaultPageResponse<DataSyncLogDTO> pageDataSyncLog(SearchDataSyncLogDTO searchDataSyncLogDTO, Pageable pageable);

}
