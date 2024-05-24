package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月16日
 *
 * @author xgx
 */
@Service
class TerminalGroupDataConversion extends AbstractDataConversion {

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Override
    UUID[] getIdArrayFromScheduleData(ScheduleDataDTO<UUID, String> from) {
        return from.getTerminalGroupArr();
    }

    @Override
    void setDataToScheduleDataDTO(ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to, List<IdLabelEntry> idLabelEntryList) {
        to.setTerminalGroupArr(idLabelEntryList.toArray(new IdLabelEntry[]{}));
    }


    @Override
    public IdLabelEntry obtainIdLabelEntryBy(Object id) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        CbbTerminalGroupDetailDTO terminalGroupDTO = cbbTerminalGroupMgmtAPI.loadById((UUID)id);
        IdLabelEntry idLabelEntry = new IdLabelEntry();
        idLabelEntry.setId(terminalGroupDTO.getId());
        idLabelEntry.setLabel(terminalGroupDTO.getGroupName());
        return idLabelEntry;
    }

    @Override
    public boolean isSupport(ScheduleDataDTO<UUID, String> from) {
        Assert.notNull(from, "from can not be null");
        return !ObjectUtils.isEmpty(from.getTerminalGroupArr());
    }
}
