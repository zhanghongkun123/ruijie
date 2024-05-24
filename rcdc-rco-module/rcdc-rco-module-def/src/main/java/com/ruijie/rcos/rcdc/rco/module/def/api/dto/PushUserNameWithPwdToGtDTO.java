package com.ruijie.rcos.rcdc.rco.module.def.api.dto;


import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: 推送GT密码通知体
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/08/24
 *
 * @author zhengjingyong
 */
public class PushUserNameWithPwdToGtDTO {

    @NotNull
    private Integer code;

    @NotNull
    private String message;

    @Nullable
    private PushUserNameWithPwdToGtDTO.Content content;

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

    public PushUserNameWithPwdToGtDTO.Content getContent() {
        return content;
    }

    public void setContent(PushUserNameWithPwdToGtDTO.Content content) {
        this.content = content;
    }


    /**
     * 内容
     */
    public static class Content {
        /**
         * 用户名
         */
        @NotNull
        private String account;

        /**
         * 密码
         */
        @NotNull
        private String newPwd;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getNewPwd() {
            return newPwd;
        }

        public void setNewPwd(String newPwd) {
            this.newPwd = newPwd;
        }
    }
}
