package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: IP网段限制DTO类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/27 22:56
 *
 * @author yxq
 */
public class IpLimitDTO {

    @Nullable
    private UUID id;

    /**
     * 限制网段开始IP
     */
    @IPv4Address
    @NotBlank
    private String ipStart;

    /**
     * 限制网段终止IP
     */
    @IPv4Address
    @NotBlank
    private String ipEnd;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
