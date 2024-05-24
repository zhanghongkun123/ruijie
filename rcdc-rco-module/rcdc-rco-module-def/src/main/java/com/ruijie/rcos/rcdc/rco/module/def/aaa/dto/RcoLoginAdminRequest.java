package com.ruijie.rcos.rcdc.rco.module.def.aaa.dto;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/4 11:28
 *
 * @author yxq
 */
public class RcoLoginAdminRequest {

    @NotNull
    private String userName;

    @NotNull
    private String rawPassword;

    /**
     * 是否只进行
     */
    @NotNull
    private Boolean onlyValidAdminPwd = Boolean.FALSE;

    /**
     * 客户端IP
     */
    @Nullable
    private String loginIp;

    @Nullable
    private SubSystem subSystem;

    public String getUserName() {
        return userName;
    }

    /**
     * 设置管理员名称
     *
     * @param userName 管理员名称
     * @return 当前对象
     */
    public RcoLoginAdminRequest setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    /**
     * 设置明文密码
     *
     * @param rawPassword 明文密码
     * @return 当前对象
     */
    public RcoLoginAdminRequest setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
        return this;
    }

    public Boolean getOnlyValidAdminPwd() {
        return onlyValidAdminPwd;
    }

    /**
     * 设置是否只校验密码
     *
     * @param onlyValidAdminPwd 是否只校验密码
     * @return 当前对象
     */
    public RcoLoginAdminRequest setOnlyValidAdminPwd(Boolean onlyValidAdminPwd) {
        this.onlyValidAdminPwd = onlyValidAdminPwd;
        return this;
    }

    @Nullable
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * 设置ip
     *
     * @param loginIp ip
     * @return 当前对象
     */
    public RcoLoginAdminRequest setLoginIp(@Nullable String loginIp) {
        this.loginIp = loginIp;
        return this;
    }

    @Nullable
    public SubSystem getSubSystem() {
        return subSystem;
    }

    /**
     * 设置子系统
     *
     * @param subSystem 子系统
     * @return 当前对象
     */
    public RcoLoginAdminRequest setSubSystem(@Nullable SubSystem subSystem) {
        this.subSystem = subSystem;
        return this;
    }
}
