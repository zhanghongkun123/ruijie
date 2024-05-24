package com.ruijie.rcos.rcdc.rco.module.def.api.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: GT密码通知体
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/03/18
 *
 * @author zhengjingyong
 */
public class UserPasswordGtInfoDTO {

    @NotNull
    private Integer code;

    @NotNull
    private String message;

    @Nullable
    private UserPasswordGtInfoDTO.Content content;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserPasswordGtInfoDTO.Content getContent() {
        return content;
    }

    public void setContent(UserPasswordGtInfoDTO.Content content) {
        this.content = content;
    }


    /**
     * 内容
     */
    public static class Content {
        /**
         * 密码
         */
        @Nullable
        private String password;

        /**
         * 用户名
         */
        @Nullable
        private String userName;


        /**
         * 是否同步账号
         */
        @Nullable
        @JSONField(name = "is_change_username")
        private Boolean changeUserName;


        /**
         * 是否自动登录
         */
        @Nullable
        @JSONField(name = "is_auto_login")
        private Boolean autoLogin;


        /**
         * 用户组
         */
        @Nullable
        @JSONField(name = "usergroup")
        private String userGroup;


        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Nullable
        public String getUserName() {
            return userName;
        }

        public void setUserName(@Nullable String userName) {
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
