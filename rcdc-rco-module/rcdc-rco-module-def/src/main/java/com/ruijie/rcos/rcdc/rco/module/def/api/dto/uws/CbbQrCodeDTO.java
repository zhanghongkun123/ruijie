package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;


import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeLoginStatus;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月16日
 *
 * @author xgx
 */
public class CbbQrCodeDTO {
    private String qrCode;

    private String content;

    private String clientId;

    private CbbQrCodeLoginStatus status;

    private Long expireTime;

    private String userData;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public CbbQrCodeLoginStatus getStatus() {
        return status;
    }

    public void setStatus(CbbQrCodeLoginStatus status) {
        this.status = status;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }
}
