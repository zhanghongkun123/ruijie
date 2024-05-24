package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.conversion;

import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:  <br>
 * Copyright: Copyright (c) 2023 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2023/12/5 <br>
 *
 * @author fang
 */
@Service
public class TerminalIdLabelConversion extends AbstractlIdLabelConversion<String> {

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    protected String getLabel(String id) throws BusinessException {
        CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(id);
        return terminalBasicInfoDTO.getTerminalName();
    }
}
