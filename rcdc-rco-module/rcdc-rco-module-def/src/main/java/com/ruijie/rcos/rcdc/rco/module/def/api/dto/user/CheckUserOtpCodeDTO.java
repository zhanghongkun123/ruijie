package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 校验动态口令码请求体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/5
 *
 * @author WuShengQiang
 */
public class CheckUserOtpCodeDTO {

    /**
     * 用户名
     */
    @NotBlank
    @TextShort
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

    @Nullable
    public Boolean getFirstCheck() {
        return firstCheck;
    }

    public void setFirstCheck(@Nullable Boolean firstCheck) {
        this.firstCheck = firstCheck;
    }

    @Nullable
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(@Nullable UUID userId) {
        this.userId = userId;
    }
}
