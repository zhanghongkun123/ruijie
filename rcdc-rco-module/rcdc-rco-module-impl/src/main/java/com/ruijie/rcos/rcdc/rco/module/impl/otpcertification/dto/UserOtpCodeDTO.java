package com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月08日
 *
 * @author zhanghongkun
 */
public class /**/UserOtpCodeDTO extends EqualsHashcodeSupport {

    /**
     * 用户名
     */
    @NotBlank
    private String userName;

    /**
     * 动态口令验证码
     */
    @NotBlank
    private String otpCode;

    /**
     * 动态口令验证码首次校验
     */
    @Nullable
    private Boolean firstCheck;

    /**
     * 用户ID
     * @return
     */
    @Nullable
    private UUID userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public Boolean getFirstCheck() {
        return firstCheck;
    }

    public void setFirstCheck(Boolean firstCheck) {
        this.firstCheck = firstCheck;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
