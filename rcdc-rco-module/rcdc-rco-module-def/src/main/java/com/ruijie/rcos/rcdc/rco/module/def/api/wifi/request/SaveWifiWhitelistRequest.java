package com.ruijie.rcos.rcdc.rco.module.def.api.wifi.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvEditTerminalGroupRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.apache.commons.compress.utils.Lists;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * Description: 更新白名单请求实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
public class SaveWifiWhitelistRequest {
    /**
     * wifi白名单集合
     */
    @Nullable
    private List<WifiWhitelistDTO> wifiWhiteList;

    /**
     * 用户终端ID
     */
    @NotNull
    private UUID terminalGroupId;

    /**
     * 是否下发下组
     */
    @Nullable
    private Boolean needApplyToSubgroup;


    public SaveWifiWhitelistRequest() {

    }

    public SaveWifiWhitelistRequest(IdvEditTerminalGroupRequest request) {
        this.wifiWhiteList = request.getWifiWhitelistDTOList();
        this.terminalGroupId = request.getId();
        this.needApplyToSubgroup = request.isNeedApplyToSubgroup();
    }


    public SaveWifiWhitelistRequest(List<WifiWhitelistDTO> wifiWhiteList, UUID terminalGroupId) {
        this.wifiWhiteList = wifiWhiteList;
        this.terminalGroupId = terminalGroupId;
    }


    /**
     * 获取无线白名单
     * 
     * @return List<WifiWhitelistDTO>
     */
    public List<WifiWhitelistDTO> getWifiWhiteList() {
        if (Objects.isNull(this.wifiWhiteList)) {
            this.wifiWhiteList = Lists.newArrayList();
        }
        return this.wifiWhiteList;
    }

    public void setWifiWhiteList(List<WifiWhitelistDTO> wifiWhiteList) {
        this.wifiWhiteList = wifiWhiteList;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    /**
     * 获取是否应用下组
     * 
     * @return true - 是 false - 否
     */
    public Boolean getNeedApplyToSubgroup() {
        if (Objects.isNull(needApplyToSubgroup)) {
            // 默认不需要下发应用组
            needApplyToSubgroup = false;
        }
        return needApplyToSubgroup;
    }

    public void setNeedApplyToSubgroup(Boolean needApplyToSubgroup) {
        this.needApplyToSubgroup = needApplyToSubgroup;
    }
}
