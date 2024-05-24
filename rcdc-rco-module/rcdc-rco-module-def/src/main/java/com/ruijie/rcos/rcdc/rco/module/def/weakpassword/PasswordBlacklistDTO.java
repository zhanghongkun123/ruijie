package com.ruijie.rcos.rcdc.rco.module.def.weakpassword;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * 全局弱密码配置开关DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月30日
 *
 * @author TD
 */
public class PasswordBlacklistDTO {

    /**
     * 是否启用弱密码库
     */
    @NotNull
    private Boolean enablePasswordBlacklist;

    public Boolean getEnablePasswordBlacklist() {
        return enablePasswordBlacklist;
    }

    public void setEnablePasswordBlacklist(Boolean enablePasswordBlacklist) {
        this.enablePasswordBlacklist = enablePasswordBlacklist;
    }
}