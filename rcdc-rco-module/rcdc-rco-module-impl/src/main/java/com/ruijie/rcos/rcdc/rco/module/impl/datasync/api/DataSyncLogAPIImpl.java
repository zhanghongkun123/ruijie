package com.ruijie.rcos.rcdc.rco.module.impl.datasync.api;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.DataSyncLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DataSyncLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchDataSyncLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.entity.DataSyncLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.service.DataSyncLogService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/22 12:16
 *
 * @author coderLee23
 */
public class DataSyncLogAPIImpl implements DataSyncLogAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSyncLogAPIImpl.class);

    @Autowired
    private DataSyncLogService dataSyncLogService;

    @Override
    public DefaultPageResponse<DataSyncLogDTO> pageDataSyncLog(SearchDataSyncLogDTO searchDataSyncLogDTO, Pageable pageable) {
        Assert.notNull(searchDataSyncLogDTO, "searchDataSyncLogDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<DataSyncLogEntity> dataSyncLogPage = dataSyncLogService.pageDataSyncLog(searchDataSyncLogDTO, pageable);
        Page<DataSyncLogDTO> dataSyncLogDTOPage = dataSyncLogPage.map(entity -> {
            DataSyncLogDTO dataSyncLogDTO = new DataSyncLogDTO();
            BeanUtils.copyProperties(entity, dataSyncLogDTO);
            return dataSyncLogDTO;
        });
        DefaultPageResponse<DataSyncLogDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(dataSyncLogDTOPage.stream().toArray(DataSyncLogDTO[]::new));
        defaultPageResponse.setTotal(dataSyncLogDTOPage.getTotalElements());
        return defaultPageResponse;
    }

}
