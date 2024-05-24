package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Description: 定时任务-桌面池对象
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/6
 *
 * @author wuShengQiang
 */
@Service
public class DesktopPoolConversion extends AbstractDataConversion {

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Override
    UUID[] getIdArrayFromScheduleData(ScheduleDataDTO<UUID, String> from) {
        return from.getDesktopPoolArr();
    }

    @Override
    void setDataToScheduleDataDTO(ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to, List<IdLabelEntry> idLabelEntryList) {
        to.setDesktopPoolArr(idLabelEntryList.toArray(new IdLabelEntry[] {}));
    }

    @Override
    public IdLabelEntry obtainIdLabelEntryBy(Object id) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail((UUID) id);
        IdLabelEntry idLabelEntry = new IdLabelEntry();
        idLabelEntry.setId(desktopPoolDTO.getId());
        idLabelEntry.setLabel(desktopPoolDTO.getName());
        return idLabelEntry;
    }

    @Override
    public boolean isSupport(ScheduleDataDTO<UUID, String> from) {
        Assert.notNull(from, "from can not be null");
        return !ObjectUtils.isEmpty(from.getDesktopPoolArr());
    }
}
