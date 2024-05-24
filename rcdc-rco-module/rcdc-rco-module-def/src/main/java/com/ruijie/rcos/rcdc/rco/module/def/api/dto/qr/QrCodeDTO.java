package com.ruijie.rcos.rcdc.rco.module.def.api.dto.qr;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeLoginStatus;

/**
 * Description: 二维码信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-21 14:58:00
 *
 * @author zjy
 */
public class QrCodeDTO {

    /**
     * 二维码
     **/
    private String qrCode;

    /**
     * 内容
     **/
    private String content;

    /**
     * 客户端唯一标识
     **/
    private String clientId;

    /**
     * 二维码状态
     **/
    private CbbQrCodeLoginStatus status;

    /**
     * 过期时间
     **/
    private Long expireTime;

    /**
     * 用户数据
     **/
    private String userData;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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
