package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 批量修改IDV云桌面DNS请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年1月16日
 *
 * @author heruiyuan1
 */
public class EditIDVDesktopDnsWebRequest implements WebRequest {

    @ApiModelProperty(value = "云桌面ID数组", required = true)
    @NotEmpty
    @Size(min = 1)
    private UUID[] idArr;

    @ApiModelProperty(value = "是否自动获取DNS", required = true)
    @NotNull
    private Boolean autoDns;

    @ApiModelProperty(value = "首选DNS")
    @Nullable
    @IPv4Address
    private String dns;

    @ApiModelProperty(value = "备用DNS")
    @Nullable
    @IPv4Address
    private String dnsBack;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    public Boolean getAutoDns() {
        return autoDns;
    }

    public void setAutoDns(Boolean autoDns) {
        this.autoDns = autoDns;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getDnsBack() {
        return dnsBack;
    }

    public void setDnsBack(String dnsBack) {
        this.dnsBack = dnsBack;
    }
}

