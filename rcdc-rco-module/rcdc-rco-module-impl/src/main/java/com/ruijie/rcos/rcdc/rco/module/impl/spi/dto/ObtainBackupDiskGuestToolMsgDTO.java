package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

/**
 * Description: 获取备份盘
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年03月12日
 *
 * @author xwx
 */
public class ObtainBackupDiskGuestToolMsgDTO {
    //GT指定处理的目标，CDC的才接收处理
    private String target;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
