package com.ruijie.rcos.rcdc.rco.module.impl.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/28 11:18
 *
 * @author conghaifeng
 */
public class DeskTopConfigDTO {

    /**
     * 分组名称
     */
    @NotBlank
    @TextShort
    private String groupName;

    /**
     *
     * 云桌面关联镜像模板id
     */
    @Nullable
    private UUID cbbIdvDesktopImageId;

    /**
     * 云桌面关联策略模板id
     */
    @Nullable
    private UUID cbbIdvDesktopStrategyId;


    /**
     *
     * VOI云桌面关联镜像模板id
     */
    @Nullable
    private UUID cbbVoiDesktopImageId;

    /**
     * VOI云桌面关联策略模板id
     */
    @Nullable
    private UUID cbbVoiDesktopStrategyId;

    /**
     * IDV桌面用户配置策略
     */
    @Nullable
    private UUID cbbIdvUserProfileStrategyId;

    /**
     * VOI桌面用户配置策略
     */
    @Nullable
    private UUID cbbVoiUserProfileStrategyId;

    @Nullable
    private UUID idvSoftwareStrategyId;

    @Nullable
    private UUID voiSoftwareStrategyId;


    @Nullable
    public UUID getCbbIdvDesktopImageId() {
        return cbbIdvDesktopImageId;
    }

    public void setCbbIdvDesktopImageId(@Nullable UUID cbbIdvDesktopImageId) {
        this.cbbIdvDesktopImageId = cbbIdvDesktopImageId;
    }

    @Nullable
    public UUID getCbbIdvDesktopStrategyId() {
        return cbbIdvDesktopStrategyId;
    }

    public void setCbbIdvDesktopStrategyId(@Nullable UUID cbbIdvDesktopStrategyId) {
        this.cbbIdvDesktopStrategyId = cbbIdvDesktopStrategyId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Nullable
    public UUID getCbbVoiDesktopImageId() {
        return cbbVoiDesktopImageId;
    }

    public void setCbbVoiDesktopImageId(@Nullable UUID cbbVoiDesktopImageId) {
        this.cbbVoiDesktopImageId = cbbVoiDesktopImageId;
    }

    @Nullable
    public UUID getCbbVoiDesktopStrategyId() {
        return cbbVoiDesktopStrategyId;
    }

    public void setCbbVoiDesktopStrategyId(@Nullable UUID cbbVoiDesktopStrategyId) {
        this.cbbVoiDesktopStrategyId = cbbVoiDesktopStrategyId;
    }

    @Nullable
    public UUID getIdvSoftwareStrategyId() {
        return idvSoftwareStrategyId;
    }

    public void setIdvSoftwareStrategyId(@Nullable UUID idvSoftwareStrategyId) {
        this.idvSoftwareStrategyId = idvSoftwareStrategyId;
    }

    @Nullable
    public UUID getVoiSoftwareStrategyId() {
        return voiSoftwareStrategyId;
    }

    public void setVoiSoftwareStrategyId(@Nullable UUID voiSoftwareStrategyId) {
        this.voiSoftwareStrategyId = voiSoftwareStrategyId;
    }

    @Nullable
    public UUID getCbbIdvUserProfileStrategyId() {
        return cbbIdvUserProfileStrategyId;
    }

    public void setCbbIdvUserProfileStrategyId(@Nullable UUID cbbIdvUserProfileStrategyId) {
        this.cbbIdvUserProfileStrategyId = cbbIdvUserProfileStrategyId;
    }

    @Nullable
    public UUID getCbbVoiUserProfileStrategyId() {
        return cbbVoiUserProfileStrategyId;
    }

    public void setCbbVoiUserProfileStrategyId(@Nullable UUID cbbVoiUserProfileStrategyId) {
        this.cbbVoiUserProfileStrategyId = cbbVoiUserProfileStrategyId;
    }
}
