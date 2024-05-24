package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月25日
 *
 * @author lanzf
 */
@ApiModel("备份恢复")
public class NetworkStrategyRecoverRequest {

    @ApiModelProperty(value = "要恢复网络策略的云平台ID", required = true)
    @NotNull
    private UUID platformId;

    @ApiModelProperty(value = "交换机ID", required = true)
    @NotNull
    private UUID vswitchId;

    public UUID getVswitchId() {
        return vswitchId;
    }

    public void setVswitchId(UUID vswitchId) {
        this.vswitchId = vswitchId;
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }
}
