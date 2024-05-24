package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.conversion;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Description:  <br>
 * Copyright: Copyright (c) 2023 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2023/12/5 <br>
 *
 * @author fang
 */
@Service
public class DeskPoolIdLabelConversion extends AbstractlIdLabelConversion<UUID> {

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Override
    protected String getLabel(UUID id) throws BusinessException {
        CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(id);
        return desktopPoolDTO.getName();
    }
}
