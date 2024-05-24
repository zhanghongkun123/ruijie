package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeConfigDTO;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-14 19:49
 *
 * @author wanglianyun
 */
public class ClientQrCodeConfigRespnose extends Result {

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
     * 内容前缀
     **/
    private String contentPrefix;

    /**
     * 更多配置
     **/
    private String advanceConfig;

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

    public String getContentPrefix() {
        return contentPrefix;
    }

    public void setContentPrefix(String contentPrefix) {
        this.contentPrefix = contentPrefix;
    }

    public String getAdvanceConfig() {
        return advanceConfig;
    }

    public void setAdvanceConfig(String advanceConfig) {
        this.advanceConfig = advanceConfig;
    }
}
