package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.PrimaryCertification;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.AssistCertification;
import com.ruijie.rcos.sk.base.annotation.CellPhone;
import com.ruijie.rcos.sk.base.annotation.Email;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: 编辑用户请求
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 19:00
 *
 * @author zdc
 */
public class EditUserRequest {

    /**
     * 用户名
     */
    @NotNull
    @TextShort
    @TextName
    private String userName;

    /**
     * 主要认证策略
     */
    @Nullable
    private PrimaryCertification primaryCertification;

    /** 辅助认证 */
    @Nullable
    private AssistCertification assistCertification;

    /** 用户描述 */
    @Nullable
    @Size(max = 128)
    private String description;

    /** 手机号 */
    @Nullable
    @TextShort
    @CellPhone
    private String phoneNum;

    /** 邮箱 */
    @Nullable
    @Email
    private String email;

    /** 过期时间 */
    @Nullable
    @Range(min = "0")
    private Long accountExpireDate;

    /** 失效天数 */
    @Nullable
    @Range(min = "0", max = "1000")
    private Integer invalidTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Nullable
    public PrimaryCertification getPrimaryCertification() {
        return primaryCertification;
    }

    public void setPrimaryCertification(@Nullable PrimaryCertification primaryCertification) {
        this.primaryCertification = primaryCertification;
    }

    @Nullable
    public AssistCertification getAssistCertification() {
        return assistCertification;
    }

    public void setAssistCertification(@Nullable AssistCertification assistCertification) {
        this.assistCertification = assistCertification;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(@Nullable String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(@Nullable Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    @Nullable
    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(@Nullable Integer invalidTime) {
        this.invalidTime = invalidTime;
    }
}