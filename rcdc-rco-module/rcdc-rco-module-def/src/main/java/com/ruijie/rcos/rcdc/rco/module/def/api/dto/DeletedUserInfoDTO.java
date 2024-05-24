package com.ruijie.rcos.rcdc.rco.module.def.api.dto;


import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: webclient用户信息DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-06-22
 *
 * @author lihengjing
 */
public class DeletedUserInfoDTO {
    @NotNull
    private String userName;

    public DeletedUserInfoDTO(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserDeletedDesktopInfoDTO{" +
                "userName='" + userName + '\'' +
                '}';
    }
}

