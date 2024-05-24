package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 获取用户信息DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/21
 *
 * @author WuShengQiang
 */
public class ObtainUserInfoDTO {

    private int code;

    private String message;

    private Content content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * 内容
     */
    public static class Content {

        /**
         * 用户类型
         */
        @NotNull
        @JSONField(name = "userType")
        private IacUserTypeEnum userType;


        /**
         * 密码
         */
        @NotNull
        @JSONField(name = "password")
        private String password;

        /**
         * 用户名
         */
        @NotNull
        @JSONField(name = "name")
        private String userName;


        /**
         * 是否同步账号
         */
        @Nullable
        @JSONField(name = "isChangeUsername")
        private Boolean changeUserName;


        /**
         * 是否自动登录
         */
        @Nullable
        @JSONField(name = "isAutoLogon")
        private Boolean autoLogin;


        /**
         * 用户组
         */
        @Nullable
        @JSONField(name = "usergroup")
        private String userGroup;


        public IacUserTypeEnum getUserType() {
            return userType;
        }

        public void setUserType(IacUserTypeEnum userType) {
            this.userType = userType;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Nullable
        public Boolean getChangeUserName() {
            return changeUserName;
        }

        public void setChangeUserName(@Nullable Boolean changeUserName) {
            this.changeUserName = changeUserName;
        }

        @Nullable
        public Boolean getAutoLogin() {
            return autoLogin;
        }

        public void setAutoLogin(@Nullable Boolean autoLogin) {
            this.autoLogin = autoLogin;
        }

        @Nullable
        public String getUserGroup() {
            return userGroup;
        }

        public void setUserGroup(@Nullable String userGroup) {
            this.userGroup = userGroup;
        }
    }
}
