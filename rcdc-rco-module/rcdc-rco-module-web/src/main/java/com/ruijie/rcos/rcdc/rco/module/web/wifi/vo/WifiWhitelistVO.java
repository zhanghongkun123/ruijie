package com.ruijie.rcos.rcdc.rco.module.web.wifi.vo;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * Description: wifi白名单实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
public class WifiWhitelistVO {
    /**
     * wifi名称
     */
    @NotNull
    @TextShort
    @ApiModelProperty(value = "ssid")
    private String ssid;

    /**
     * 当本条目属于列表的一份子时,
     * <code>index<code/> 是 列表中的下标
     */
    @NotNull
    @Range(min = "0", max = "3")
    @ApiModelProperty(value = "排序")
    private Integer index;

    public WifiWhitelistVO(String ssid, Integer index) {
        this.ssid = ssid;
        this.index = index;
    }

    public WifiWhitelistVO()  { }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
