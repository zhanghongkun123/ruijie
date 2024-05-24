package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.conversion;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolDetailDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
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
 * @author chenli
 */
@Service
public class RcaPoolIdLabelConversion extends AbstractlIdLabelConversion<UUID> {

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Override
    protected String getLabel(UUID id) throws BusinessException {
        RcaAppPoolDetailDTO rcaAppPoolDetailDTO = rcaAppPoolAPI.getDetail(id);
        return rcaAppPoolDetailDTO.getName();
    }
}
