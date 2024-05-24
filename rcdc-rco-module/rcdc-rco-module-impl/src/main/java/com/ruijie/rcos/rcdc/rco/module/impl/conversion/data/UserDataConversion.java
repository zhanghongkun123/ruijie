package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
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
class UserDataConversion extends AbstractDataConversion {

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;


    @Override
    UUID[] getIdArrayFromScheduleData(ScheduleDataDTO<UUID, String> from) {
        return from.getUserArr();
    }

    @Override
    void setDataToScheduleDataDTO(ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to, List<IdLabelEntry> idLabelEntryList) {
        to.setUserArr(idLabelEntryList.toArray(new IdLabelEntry[]{}));
    }

    @Override
    public IdLabelEntry obtainIdLabelEntryBy(Object id) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail((UUID)id);
        IdLabelEntry idLabelEntry = new IdLabelEntry();
        idLabelEntry.setId(userDetail.getId());
        idLabelEntry.setLabel(userDetail.getUserName());
        return idLabelEntry;
    }

    @Override
    public boolean isSupport(ScheduleDataDTO<UUID, String> from) {
        Assert.notNull(from, "from can not be null");
        return !ObjectUtils.isEmpty(from.getUserArr());
    }
}
