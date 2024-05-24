package com.ruijie.rcos.rcdc.rco.module.def.api.dto;


import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/24
 *
 * @author zqj
 */
public class UserLogoutDTO {

    @NotBlank
    private String tip;

    @NotBlank
    private String userName;

    public UserLogoutDTO(String tip, String userName) {
        this.tip = tip;
        this.userName = userName;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
