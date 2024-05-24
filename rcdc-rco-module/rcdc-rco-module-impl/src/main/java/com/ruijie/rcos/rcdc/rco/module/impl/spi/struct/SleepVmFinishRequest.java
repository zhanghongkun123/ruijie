package com.ruijie.rcos.rcdc.rco.module.impl.spi.struct;

import java.util.UUID;

/**
 *
 * 桌面休眠通知
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年5月20日
 *
 * @author zjy
 */
public class SleepVmFinishRequest {

    private UUID deskId;

    public SleepVmFinishRequest(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
