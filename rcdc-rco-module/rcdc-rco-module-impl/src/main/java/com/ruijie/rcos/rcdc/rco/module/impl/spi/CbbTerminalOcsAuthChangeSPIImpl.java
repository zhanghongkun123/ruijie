package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalOcsAuthChangeSPI;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.request.CbbTerminalOcsAuthChangeRequest;
import org.springframework.util.Assert;

/**
 * Description: 实现 CbbTerminalOcsAuthChangeSPIImpl
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.09.27
 *
 * @author LinHJ
 */
public class CbbTerminalOcsAuthChangeSPIImpl implements CbbTerminalOcsAuthChangeSPI {

    @Override
    public void notifyOcsAuthChange(CbbTerminalOcsAuthChangeRequest changeRequest) {
        Assert.notNull(changeRequest, "changeRequest can not be null");
    }
}
