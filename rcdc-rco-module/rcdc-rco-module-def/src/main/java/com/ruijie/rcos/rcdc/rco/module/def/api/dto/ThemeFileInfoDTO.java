package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: 主题策略文件信息DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-06-22
 *
 * @author lihengjing
 */
public class ThemeFileInfoDTO {
    @Nullable
    private String imagePath;

    @Nullable
    private String imageName;

    @Nullable
    private String md5;

    @NotNull
    private Boolean isDefault = Boolean.TRUE;

    @Nullable
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
    }

    @Nullable
    public String getImageName() {
        return imageName;
    }

    public void setImageName(@Nullable String imageName) {
        this.imageName = imageName;
    }

    @Nullable
    public String getMd5() {
        return md5;
    }

    public void setMd5(@Nullable String md5) {
        this.md5 = md5;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "ThemeFileInfoDTO{" +
                "imagePath='" + imagePath + '\'' +
                ", imageName='" + imageName + '\'' +
                ", md5='" + md5 + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
