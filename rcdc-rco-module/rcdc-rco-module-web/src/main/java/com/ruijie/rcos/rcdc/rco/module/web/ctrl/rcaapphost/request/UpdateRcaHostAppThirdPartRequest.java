package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.request;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 修改第三方主机纳管信息
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月10日
 *
 * @author liuwc
 */
public class UpdateRcaHostAppThirdPartRequest implements WebRequest {

    /**
     * 纳管应用主机ID
     */
    @NotNull
    private UUID id;

    // 云主机名称
    @NotBlank
    @TextName
    private String name;


    // 最大会话数
    @NotNull
    @Range(min = "0", max = "200")
    private Integer maxSessionCount;

    @Nullable
    // 应用主机管理员账号
    private String hostAuthName;

    @Nullable
    // 应用主机密码
    private String hostAuthCode;

    @Nullable
    @TextMedium
    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxSessionCount() {
        return maxSessionCount;
    }

    public void setMaxSessionCount(Integer maxSessionCount) {
        this.maxSessionCount = maxSessionCount;
    }

    @Nullable
    public String getHostAuthName() {
        return hostAuthName;
    }

    public void setHostAuthName(@Nullable String hostAuthName) {
        this.hostAuthName = hostAuthName;
    }

    @Nullable
    public String getHostAuthCode() {
        return hostAuthCode;
    }

    public void setHostAuthCode(@Nullable String hostAuthCode) {
        this.hostAuthCode = hostAuthCode;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}
