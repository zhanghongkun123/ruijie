package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desknetwork;

import java.util.UUID;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 检查命名唯一性
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月19日
 * 
 * @author zouqi
 */
public class CheckNetworkDuplicationWebRequest implements WebRequest {

    /**
     * 业务网络ID
     */
    @ApiModelProperty(value = "网络策略id")
    @Nullable
    private UUID networkId;

    @ApiModelProperty(value = "网络策略名称", required = true)
    @TextMedium
    @TextName
    @NotBlank
    private String deskNetworkName;

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public String getDeskNetworkName() {
        return deskNetworkName;
    }

    public void setDeskNetworkName(String deskNetworkName) {
        this.deskNetworkName = deskNetworkName;
    }
    
}
