package com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * Description: 创建白名单配置实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
public class WifiWhitelistConfigDTO {
    /**
     * 白名单列表
     */
    @JSONField(name = "wifiWhitelist")
    private List<WifiWhitelistDTO> wifiWhiteList;

    public WifiWhitelistConfigDTO() {

    }

    public WifiWhitelistConfigDTO(List<WifiWhitelistDTO> wifiWhiteList) {
        this.wifiWhiteList = wifiWhiteList;
    }

    /**
     * 获取白名单
     * @return List<WifiWhitelistDTO>
     */
    public List<WifiWhitelistDTO> getWifiWhiteList() {
        if (Objects.nonNull(wifiWhiteList)) {
            return wifiWhiteList;
        } else {
            return new ArrayList<>();
        }

    }

    public void setWifiWhiteList(List<WifiWhitelistDTO> wifiWhiteList) {
        this.wifiWhiteList = wifiWhiteList;
    }

}
