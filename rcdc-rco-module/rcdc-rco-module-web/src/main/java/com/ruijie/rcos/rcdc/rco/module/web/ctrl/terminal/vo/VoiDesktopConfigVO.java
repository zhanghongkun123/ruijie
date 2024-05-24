package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/21 16:51
 *
 * @author linrenjian
 */

public class VoiDesktopConfigVO {

    @NotNull
    @ApiModelProperty(value = "策略")
    private IdLabelEntry strategy;

    @NotNull
    @ApiModelProperty(value = "镜像")
    private IdLabelEntry image;

    /**
     * 软件管控策略ID
     */
    @Nullable
    private IdLabelEntry softwareStrategy;

    /**
     * 用户配置策略
     */
    @Nullable
    @ApiModelProperty(value = "用户配置策略")
    private IdLabelEntry userProfileStrategy;

    public IdLabelEntry getStrategy() {
        return strategy;
    }

    public void setStrategy(IdLabelEntry strategy) {
        this.strategy = strategy;
    }

    public IdLabelEntry getImage() {
        return image;
    }

    public void setImage(IdLabelEntry image) {
        this.image = image;
    }

    @Nullable
    public IdLabelEntry getSoftwareStrategy() {
        return softwareStrategy;
    }

    public void setSoftwareStrategy(@Nullable IdLabelEntry softwareStrategy) {
        this.softwareStrategy = softwareStrategy;
    }

    @Nullable
    public IdLabelEntry getUserProfileStrategy() {
        return userProfileStrategy;
    }

    public void setUserProfileStrategy(@Nullable IdLabelEntry userProfileStrategy) {
        this.userProfileStrategy = userProfileStrategy;
    }
}
