package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.rco.module.def.enums.TerminalTempSkipAuthTypeEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalTempSkipAuthListSPI;

/**
 * Description: 终端先跳过授权，后面再做校验
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月09日
 *
 * @author xwx
 */
public class CbbTerminalTempSkipAuthListSPIImpl implements CbbTerminalTempSkipAuthListSPI {

    @Override
    public boolean checkTempSkipAuthList(CbbShineTerminalBasicInfo terminalBasicInfo) {
        Assert.notNull(terminalBasicInfo, "terminalBasicInfo cant be null");

        String productType = terminalBasicInfo.getProductType();
        if (StringUtils.isEmpty(productType)) {
            return false;
        }
        return TerminalTempSkipAuthTypeEnum.isInTerminalTempSkipAuthList(productType);
    }

}
