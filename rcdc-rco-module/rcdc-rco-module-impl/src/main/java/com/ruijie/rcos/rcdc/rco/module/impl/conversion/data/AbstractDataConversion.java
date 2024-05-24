package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月24日
 *
 * @author zql
 */
public abstract class AbstractDataConversion implements DataConversion {

    @Override
    public void conversion(ScheduleDataDTO<UUID, String> from, ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to) {
        Assert.notNull(from, "from can not be null");
        Assert.notNull(to, "to can not be null");

        // 至多返回20条label记录，剩余则为空
        int limitSize = MAX_LABEL_SHOW_NUM;
        List<IdLabelEntry> idLabelEntryList = Lists.newArrayList();
        for (UUID id : getIdArrayFromScheduleData(from)) {
            if (limitSize > 0) {
                try {
                    IdLabelEntry idLabelEntry = obtainIdLabelEntryBy(id);
                    idLabelEntryList.add(idLabelEntry);
                    limitSize--;
                } catch (BusinessException e) {
                    LOGGER.error("查询ID为{}的基本信息发生异常{}", id, e.getI18nMessage());
                }

            } else {
                IdLabelEntry idLabelEntry = new IdLabelEntry();
                idLabelEntry.setId(id);
                idLabelEntry.setLabel(null);
                idLabelEntryList.add(idLabelEntry);
            }
        }
        setDataToScheduleDataDTO(to, idLabelEntryList);
    }

    /**
     * 获取定时任务的对象ID
     *
     * @param scheduleDataDTO
     * @return 任务对象的ID数组
     */
    abstract UUID[] getIdArrayFromScheduleData(ScheduleDataDTO<UUID, String> scheduleDataDTO);


    /**
     * 设置定时任务数据的对象数据
     *
     * @param to
     * @param idLabelEntryList
     */
    abstract void setDataToScheduleDataDTO(ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to, List<IdLabelEntry> idLabelEntryList);


    /**
     * 根据ID获取对应的id-label记录
     *
     * @param id id
     * @return id和label的对应关系
     * @throws BusinessException 异常
     */
    abstract IdLabelEntry obtainIdLabelEntryBy(Object id) throws BusinessException;
}
