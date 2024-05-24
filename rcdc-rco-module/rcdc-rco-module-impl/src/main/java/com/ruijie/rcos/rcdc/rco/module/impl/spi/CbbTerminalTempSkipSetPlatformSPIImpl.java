package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.rco.module.def.enums.TerminalTempSkipSetPlatformTypeEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalTempSkipSetPlatformSPI;

/**
 * Description: skip set platform
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/23
 *
 * @author xwx
 */
public class CbbTerminalTempSkipSetPlatformSPIImpl implements CbbTerminalTempSkipSetPlatformSPI {

    @Override
    public boolean checkTempSkipSetPlatformList(CbbShineTerminalBasicInfo terminalBasicInfo) {
        Assert.notNull(terminalBasicInfo, "terminalBasicInfo cant be null");

        String productType = terminalBasicInfo.getProductType();
        if (StringUtils.isEmpty(productType)) {
            return false;
        }
        return TerminalTempSkipSetPlatformTypeEnum.isInTerminalTempSkipSetPlatformList(productType);
    }
}
