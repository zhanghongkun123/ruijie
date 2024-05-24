package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.resquest;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 更新安装包描述
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月14日
 *
 * @author Ghang
 */
@ApiModel
public class DistributeFileEditWebRequest implements WebRequest {

    @ApiModelProperty
    @NotNull
    private UUID id;

    @TextMedium
    @Nullable
    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
