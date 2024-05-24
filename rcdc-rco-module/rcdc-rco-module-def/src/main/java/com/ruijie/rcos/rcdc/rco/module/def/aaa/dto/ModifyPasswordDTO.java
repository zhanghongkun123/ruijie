package com.ruijie.rcos.rcdc.rco.module.def.aaa.dto;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/6 9:47
 *
 * @author linrenjian
 */
public class ModifyPasswordDTO {
    @NotNull
    private UUID id;

    /**
     * 旧密码，并且为非加密形式
     */
    @NotBlank
    private String oldPwd;

    /**
     * 新密码，并且为非加密形式
     */
    @NotBlank
    private String newPwd;


    /**
     * 是否需要强制修改密码 默认不需要
     */
    @Nullable
    private Boolean needUpdatePassword = false;


    public Boolean getNeedUpdatePassword() {
        return needUpdatePassword;
    }

    public void setNeedUpdatePassword(Boolean needUpdatePassword) {
        this.needUpdatePassword = needUpdatePassword;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
