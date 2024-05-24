package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import org.springframework.lang.Nullable;

/**
 * Description: 修改服务器VIP后处理一键安装DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/21 14:23
 *
 * @author yxq
 */
public class ChangeVipConfigOneClickDTO {

    /**
     * 旧的VIP
     */
    @Nullable
    private String oldVip;

    public String getOldVip() {
        return oldVip;
    }

    public void setOldVip(String oldVip) {
        this.oldVip = oldVip;
    }

}
