package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;

import com.ruijie.rcos.rcdc.rca.module.def.dto.config.UserAuthorityOperationConfig;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 编辑应用池用户自定义策略
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月09日
 *
 * @author zhengjingyong
 */
public class RcaEditUserCustomStrategyWebRequest implements WebRequest {

    @ApiModelProperty(value = "应用池id", required = true)
    @NotNull
    private UUID poolId;

    @ApiModelProperty(value = "用户id列表", required = true)
    @NotEmpty
    private List<UUID> userIdList;

    @ApiModelProperty(value = "用户授权配置",required = true)
    @NotNull
    private UserAuthorityOperationConfig userAuthorityOperationConfig;

    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    public List<UUID> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<UUID> userIdList) {
        this.userIdList = userIdList;
    }

    public UserAuthorityOperationConfig getUserAuthorityOperationConfig() {
        return userAuthorityOperationConfig;
    }

    public void setUserAuthorityOperationConfig(UserAuthorityOperationConfig userAuthorityOperationConfig) {
        this.userAuthorityOperationConfig = userAuthorityOperationConfig;
    }
}

