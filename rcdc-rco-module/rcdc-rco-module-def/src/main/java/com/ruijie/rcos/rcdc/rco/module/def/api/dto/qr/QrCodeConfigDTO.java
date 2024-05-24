package com.ruijie.rcos.rcdc.rco.module.def.api.dto.qr;


import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacQrCodeType;

/**
 * Description: 二维码配置信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-24 14:58:00
 *
 * @author zjy
 */
public class QrCodeConfigDTO {

    /**
     * 二维码类型
     **/
    private IacQrCodeType qrCodeType;

    /**
     * 排序号
     **/
    private Integer order;

    /**
     * 二维码开关，默认为关闭
     **/
    private Boolean openSwitch;

    /**
     * 二维码超时时间，单位毫秒，默认900000毫秒
     **/
    private Long expireTime;

    /**
     * 更多配置
     **/
    private String advanceConfig;

    /**
     * 内容前缀
     **/
    private String contentPrefix;

    public IacQrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(IacQrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getOpenSwitch() {
        return openSwitch;
    }

    public void setOpenSwitch(Boolean openSwitch) {
        this.openSwitch = openSwitch;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getAdvanceConfig() {
        return advanceConfig;
    }

    public void setAdvanceConfig(String advanceConfig) {
        this.advanceConfig = advanceConfig;
    }

    public String getContentPrefix() {
        return contentPrefix;
    }

    public void setContentPrefix(String contentPrefix) {
        this.contentPrefix = contentPrefix;
    }
}
