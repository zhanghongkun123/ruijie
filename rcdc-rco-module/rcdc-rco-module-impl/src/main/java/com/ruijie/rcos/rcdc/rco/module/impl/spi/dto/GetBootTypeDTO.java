package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.BootType;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/19 14:41
 *
 * @author yxq
 */
public class GetBootTypeDTO {

    private BootType startMode;

    public BootType getStartMode() {
        return startMode;
    }

    public void setStartMode(BootType startMode) {
        this.startMode = startMode;
    }
}
