package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 解除应用主机和用户的会话绑定关系
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月26日
 *
 * @author liuwc
 */
public class RcaSessionUnbindUserRequest {

    @ApiModelProperty(value = "云主机id")
    @NotNull
    private UUID hostId;

    @ApiModelProperty(value = "用户id")
    @Nullable
    private UUID userId;

    @ApiModelProperty(value = "用户名称")
    @Nullable
    private String userName;

    @ApiModelProperty(value = "用户id列表")
    @Nullable
    private UUID[] idArr;

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    @Nullable
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(@Nullable UUID userId) {
        this.userId = userId;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nullable String userName) {
        this.userName = userName;
    }

    @Nullable
    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(@Nullable UUID[] idArr) {
        this.idArr = idArr;
    }
}
