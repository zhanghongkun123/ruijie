package com.ruijie.rcos.rcdc.rco.module.def.api.wifi.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.apache.commons.compress.utils.Lists;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 *
 * Description: 获取白名单回应
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
public class GetWifiWhitelistResponse extends DefaultResponse {

    private List<WifiWhitelistDTO> wifiWhiteList;

    /**
     * 获取无线白名单，如果wifiWhiteList为空，则返回空数组
     * @return List<WifiWhitelistDTO>
     */
    public List<WifiWhitelistDTO> getWifiWhiteList() {
        if (Objects.isNull(wifiWhiteList)) {
            this.wifiWhiteList = Lists.newArrayList();
        }
        return wifiWhiteList;
    }

    /**
     * 添加无线白名单集合
     * @param ssid  无线名称
     * @param index 顺序
     */
    public void addWifiWhitelist(String ssid, Integer index) {
        Assert.notNull(ssid,"ssid can not be null");
        Assert.notNull(index,"index can not be null");
        if (Objects.isNull(wifiWhiteList)) {
            this.wifiWhiteList = Lists.newArrayList();
        }
        this.wifiWhiteList.add(new WifiWhitelistDTO(ssid, index));
    }

    public void setWifiWhiteList(List<WifiWhitelistDTO> wifiWhiteList) {
        this.wifiWhiteList = wifiWhiteList;
    }
}
