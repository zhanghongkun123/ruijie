package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.response;

/**
 * Description: cms docking rest response
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7
 *
 * @author wjp
 */
public class VerifAdminRestServerResponse {

    private int authCode;

    private String adminName;

    private String password;

    private Long createTime;

    public VerifAdminRestServerResponse() {

    }

    public VerifAdminRestServerResponse(int authCode) {
        this.authCode = authCode;
    }

    public int getAuthCode() {
        return authCode;
    }

    public void setAuthCode(int authCode) {
        this.authCode = authCode;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
