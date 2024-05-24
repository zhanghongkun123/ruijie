package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

/**
 * 
 * Description: 清除桌面会话配置 的数据
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-12-30
 * 
 * @author zqj
 */
public class ClearDeskSessionDTO {

    /**
     * 云桌面ID
     */
    private UUID deskId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
