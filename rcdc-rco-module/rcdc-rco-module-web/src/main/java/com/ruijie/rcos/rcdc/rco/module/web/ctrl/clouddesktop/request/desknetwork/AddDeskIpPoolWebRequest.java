package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desknetwork;

import java.util.UUID;
import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 创建IP池请求 
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月19日
 * 
 * @author zouqi
 */
public class AddDeskIpPoolWebRequest implements WebRequest {
    
    /**
     * 网络策略ID
     */
    @NotNull
    private UUID deskNetworkId;
    
    /**
     * IP地址池 开始IP
     */
    @NotBlank
    @IPv4Address
    private String ipStart;
    
    /**
     * IP地址池 结束IP
     */
    @NotBlank
    @IPv4Address
    private String ipEnd;

    public UUID getDeskNetworkId() {
        return deskNetworkId;
    }

    public void setDeskNetworkId(UUID deskNetworkId) {
        this.deskNetworkId = deskNetworkId;
    }

    public String getIpStart() {
        return ipStart;
    }

    public void setIpStart(String ipStart) {
        this.ipStart = ipStart;
    }

    public String getIpEnd() {
        return ipEnd;
    }

    public void setIpEnd(String ipEnd) {
        this.ipEnd = ipEnd;
    }
    
}
