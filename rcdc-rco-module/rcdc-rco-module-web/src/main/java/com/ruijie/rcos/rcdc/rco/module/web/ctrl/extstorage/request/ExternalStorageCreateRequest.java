package com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.request;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageProtocolTypeEnum;
import com.ruijie.rcos.sk.base.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 外置存储创建请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/5
 *
 * @author TD
 */
public class ExternalStorageCreateRequest {

    /**
     * 存储名称
     */
    @ApiModelProperty(value = "存储名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String name;

    /**
     * 存储描述
     */
    @ApiModelProperty(value = "存储描述")
    @Nullable
    @TextMedium
    private String description;

    /**
     * 存储类型：NFS、SAMBA
     */
    @ApiModelProperty(value = "存储类型", required = true)
    @NotNull
    private ExternalStorageProtocolTypeEnum protocolType;

    /**
     * 存储地址
     */
    @ApiModelProperty(value = "存储地址", required = true)
    @NotBlank
    @IPv4Address
    private String serverName;

    /**
     * 端口
     */
    @ApiModelProperty(value = "端口：SAMBA类型的外置存储必填（1-65535）")
    @Nullable
    @Range(min = "1", max = "65535")
    private Integer port;

    /**
     * 共享路径
     */
    @ApiModelProperty(value = "共享路径", required = true)
    @NotNull
    @TextMedium
    private String shareName;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称：SAMBA类型的外置存储必填")
    @Nullable
    private String userName;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码：SAMBA类型的外置存储必填")
    @Nullable
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public ExternalStorageProtocolTypeEnum getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ExternalStorageProtocolTypeEnum protocolType) {
        this.protocolType = protocolType;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nullable String userName) {
        this.userName = userName;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    @Nullable
    public Integer getPort() {
        return port;
    }

    public void setPort(@Nullable Integer port) {
        this.port = port;
    }
}
