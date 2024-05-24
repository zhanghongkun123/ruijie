package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

/**
 * Description: 加域消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/19
 *
 * @author Jarman
 */
public class AdGuestToolMsgDTO {

    private int code;

    private String message;

    private BodyMessage content;

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

    public BodyMessage getContent() {
        return content;
    }

    public void setContent(BodyMessage content) {
        this.content = content;
    }

    /**
     * 消息体
     */
    public static class BodyMessage {
        private String domain;

        private String ip;

        private String account;

        private String accountPassword;

        private boolean autoJoin;

        private String adOu;

        private boolean isDomainUser;

        /** ad域用户权限 */
        private String adUserAuthority;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getAccountPassword() {
            return accountPassword;
        }

        public void setAccountPassword(String accountPassword) {
            this.accountPassword = accountPassword;
        }

        public boolean isAutoJoin() {
            return autoJoin;
        }

        public void setAutoJoin(boolean autoJoin) {
            this.autoJoin = autoJoin;
        }

        public String getAdOu() {
            return adOu;
        }

        public void setAdOu(String adOu) {
            this.adOu = adOu;
        }

        public boolean isDomainUser() {
            return isDomainUser;
        }

        public void setDomainUser(boolean domainUser) {
            isDomainUser = domainUser;
        }

        public String getAdUserAuthority() {
            return adUserAuthority;
        }

        public void setAdUserAuthority(String adUserAuthority) {
            this.adUserAuthority = adUserAuthority;
        }

    }
}
