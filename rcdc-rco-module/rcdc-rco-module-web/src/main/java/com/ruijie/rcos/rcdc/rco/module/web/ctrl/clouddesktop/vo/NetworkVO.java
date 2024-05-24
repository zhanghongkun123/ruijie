package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.vo;

import java.util.UUID;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: NetworkVO Description
 * Copyright: Copyright (c) 2017
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/04/26
 *
 * @author chixin
 */
public class NetworkVO {

    @ApiModelProperty(value = "网络策略ID", required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "指定网络IP地址", required = false)
    @IPv4Address
    private String ip;

    @ApiModelProperty(value = "标签", required = false)
    @Nullable
    private String label;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Nullable
    public String getLabel() {
        return label;
    }

    public void setLabel(@Nullable String label) {
        this.label = label;
    }
}
