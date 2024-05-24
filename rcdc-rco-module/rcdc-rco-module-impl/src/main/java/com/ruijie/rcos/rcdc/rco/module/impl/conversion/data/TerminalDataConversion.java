package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
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
class TerminalDataConversion implements DataConversion {

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;


    @Override
    public void conversion(ScheduleDataDTO<UUID, String> from, ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to) {
        Assert.notNull(from, "from can not be null");
        Assert.notNull(to, "to can not be null");

        // 至多返回20条label记录，剩余则为空
        int limitSize = MAX_LABEL_SHOW_NUM;
        List<GenericIdLabelEntry> idLabelEntryList = Lists.newArrayList();
        for (String one : from.getTerminalArr()) {
            if (limitSize > 0) {
                try {
                    CbbTerminalBasicInfoDTO cbbTerminalBasicInfoResponse =
                            cbbTerminalOperatorAPI.findBasicInfoByTerminalId(one);
                    GenericIdLabelEntry<String> idLabelEntry = new GenericIdLabelEntry<>();
                    idLabelEntry.setId(cbbTerminalBasicInfoResponse.getTerminalId());
                    idLabelEntry.setLabel(cbbTerminalBasicInfoResponse.getTerminalName());
                    idLabelEntryList.add(idLabelEntry);
                    limitSize--;
                } catch (BusinessException e) {
                    LOGGER.error("查询终端ID为{}的基本信息发生异常{}", one, e.getI18nMessage());
                }

            } else {
                GenericIdLabelEntry<String> idLabelEntry = new GenericIdLabelEntry<>();
                idLabelEntry.setId(one);
                idLabelEntry.setLabel(null);
                idLabelEntryList.add(idLabelEntry);
            }
        }
        to.setTerminalArr(idLabelEntryList.toArray(new GenericIdLabelEntry[] {}));
    }

    @Override
    public boolean isSupport(ScheduleDataDTO<UUID, String> from) {
        Assert.notNull(from, "from can not be null");
        return !ObjectUtils.isEmpty(from.getTerminalArr());
    }
}
