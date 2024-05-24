package com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 *
 * Description: 创建白名单配置实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
public class WifiWhitelistDTO implements Comparable<WifiWhitelistDTO> {
    /**
     * wifi名称
     */
    @NotNull
    @TextShort
    private String ssid;

    /**
     * 当本条目属于列表的一份子时,
     * <code>index<code/> 是 列表中的下标
     */
    @NotNull
    @Range(min = "0", max = "3")
    private Integer index;

    public WifiWhitelistDTO() {

    }

    public WifiWhitelistDTO(String ssid, Integer index) {
        this.ssid = ssid;
        this.index = index;
    }


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

    @Override
    public int compareTo(WifiWhitelistDTO o) {
        if (this.index > o.index) {
            return 1;
        } else if (this.index.equals(o.index)) {
            return 0;
        } else {
            return -1;
        }
    }
}
