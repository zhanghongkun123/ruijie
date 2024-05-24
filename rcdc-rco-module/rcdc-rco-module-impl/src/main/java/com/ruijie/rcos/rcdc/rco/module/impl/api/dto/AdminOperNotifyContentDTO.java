package com.ruijie.rcos.rcdc.rco.module.impl.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年02月10日
 * 
 * @author ljm
 */
public class AdminOperNotifyContentDTO {

    @NotBlank
    private String adminName;

    @Nullable
    private String password;


    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
