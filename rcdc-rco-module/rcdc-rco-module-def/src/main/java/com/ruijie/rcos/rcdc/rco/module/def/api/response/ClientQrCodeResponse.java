package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacQrCodeLoginStatus;
import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeDTO;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-14 20:04
 *
 * @author wanglianyun
 */
public class ClientQrCodeResponse extends Result {

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
    private IacQrCodeLoginStatus status;

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

    public IacQrCodeLoginStatus getStatus() {
        return status;
    }

    public void setStatus(IacQrCodeLoginStatus status) {
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
