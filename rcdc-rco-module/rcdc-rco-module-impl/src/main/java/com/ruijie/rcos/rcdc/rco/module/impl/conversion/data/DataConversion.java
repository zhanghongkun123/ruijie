package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
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
interface DataConversion {

    Logger LOGGER = LoggerFactory.getLogger(DataConversion.class);

    Integer MAX_LABEL_SHOW_NUM = 20;

    /**
     * 数据转换
     *
     * @param from 来源
     * @param to 目标
     */
    void conversion(ScheduleDataDTO<UUID, String> from, ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to);

    /**
     * 是否支持
     *
     * @param from 来源
     * @return 是否支持
     */
    boolean isSupport(ScheduleDataDTO<UUID, String> from);
}
