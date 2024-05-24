package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: 定时任务-磁盘池转换对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/08/02
 *
 * @author lyb
 */
@Service
public class DiskPoolConversion extends AbstractDataConversion {

    @Autowired
    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    @Override
    UUID[] getIdArrayFromScheduleData(ScheduleDataDTO<UUID, String> from) {
        return from.getDiskPoolArr();
    }

    @Override
    void setDataToScheduleDataDTO(ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to, List<IdLabelEntry> idLabelEntryList) {
        to.setDiskPoolArr(idLabelEntryList.toArray(new IdLabelEntry[] {}));
    }

    @Override
    public IdLabelEntry obtainIdLabelEntryBy(Object id) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        CbbDiskPoolDTO cbbDiskPoolDTO = cbbDiskPoolMgmtAPI.getDiskPoolDetail((UUID) id);
        IdLabelEntry idLabelEntry = new IdLabelEntry();
        idLabelEntry.setId(cbbDiskPoolDTO.getId());
        idLabelEntry.setLabel(cbbDiskPoolDTO.getName());
        return idLabelEntry;
    }

    @Override
    public boolean isSupport(ScheduleDataDTO<UUID, String> from) {
        Assert.notNull(from, "from can not be null");
        return !ObjectUtils.isEmpty(from.getDiskPoolArr());
    }
}
