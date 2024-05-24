package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月21日
 * 
 * @author zhuangchenwu
 */
public class LoginAdminWebRequest implements WebRequest {

    @ApiModelProperty(value = "管理员账号", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String pwd;

    @ApiModelProperty(value = "时间戳", required = true)
    @NotNull
    private Long timestamp;

    /**
     * 校验码
     */
    @Nullable
    private String captchaCode;

    /**
     * 校验码的key
     */
    @Nullable
    private String captchaKey;

    @Nullable
    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(@Nullable String captchaCode) {
        this.captchaCode = captchaCode;
    }

    @Nullable
    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(@Nullable String captchaKey) {
        this.captchaKey = captchaKey;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
