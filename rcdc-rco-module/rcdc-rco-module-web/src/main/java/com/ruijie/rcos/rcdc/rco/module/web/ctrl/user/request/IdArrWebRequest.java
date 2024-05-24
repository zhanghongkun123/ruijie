package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 只包含ID数组的请求对象
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月20日
 * 
 * @author zhuangaqiang
 */
public class IdArrWebRequest implements WebRequest {

    @ApiModelProperty(value = "用户ID组", required = true)
    @NotEmpty
    private UUID[] idArr;

    @ApiModelProperty(value = "password")
    @Nullable
    private String password;

    @ApiModelProperty(value = "confirmPwd")
    @Nullable
    private String confirmPwd;

    @Nullable
    private String passwordResetMode;

    @Nullable
    private Boolean shouldOnlyDeleteDataFromDb;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Nullable
    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(@Nullable String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    @Nullable
    public String getPasswordResetMode() {
        return passwordResetMode;
    }

    public void setPasswordResetMode(@Nullable String passwordResetMode) {
        this.passwordResetMode = passwordResetMode;
    }

    @Nullable
    public Boolean getShouldOnlyDeleteDataFromDb() {
        return shouldOnlyDeleteDataFromDb;
    }

    public void setShouldOnlyDeleteDataFromDb(@Nullable Boolean shouldOnlyDeleteDataFromDb) {
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
    }
}
