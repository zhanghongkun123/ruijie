package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月16日
 *
 * @author xgx
 */
@Service
public class DataConversionChain {
    @Autowired
    private List<DataConversion> dataConversionList;

    /**
     * 数据转换
     * 
     * @param scheduleDataDTO dto
     * @return idLabel DTO
     */
    public ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> conversion(ScheduleDataDTO<UUID, String> scheduleDataDTO) {
        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> toScheduleDataDTO = new ScheduleDataDTO<>();
        dataConversionList.stream().forEach(dataConversion -> {
            if (dataConversion.isSupport(scheduleDataDTO)) {
                dataConversion.conversion(scheduleDataDTO, toScheduleDataDTO);
            }
        });
        return toScheduleDataDTO;
    }
}
