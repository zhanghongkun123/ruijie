package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.NetworkType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 
 * Description: 编辑云桌面网络
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月3日
 * 
 * @author Administrator
 */
public class EditDesktopNetworkRequest extends IdRequest {
    @NotNull
    private UUID networkId;
    
    @Nullable
    private NetworkType networkType = NetworkType.STATIC ;
    
    @Nullable
    private String ip; //type = STATIC时有效
    
    public EditDesktopNetworkRequest(UUID id, UUID networkId) {
        super(id);
        this.networkId = networkId;
    }
    
    public EditDesktopNetworkRequest(UUID id, UUID networkId, String ip) {
        super(id);
        this.networkId = networkId;
        this.ip = ip;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
