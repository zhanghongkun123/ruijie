package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

/**
 * Description: ShineLoginResponseDTO
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年02月11日
 *
 * @author zjy
 */
public class ShineAutoStartVmDTO {

    private String userName;

    private String password;

    private Boolean isStartVm;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getStartVm() {
        return isStartVm;
    }

    public void setStartVm(Boolean startVm) {
        isStartVm = startVm;
    }
}