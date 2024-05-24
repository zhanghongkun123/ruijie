package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/14 16:40
 *
 * @author coderLee23
 */
public class CountCloudDesktopDTO {

    @NotEmpty
    private List<UUID> idList;

    @NotBlank
    private String osType;

    /**
     * 操作系统版本号
     */
    @NotNull
    private String osVersion;

    @Nullable
    private UUID imageTemplateId;

    @NotBlank
    private String cbbImageType;

    @NotBlank
    private String pattern;

    @NotNull
    private Boolean isDelete;

    public List<UUID> getIdList() {
        return idList;
    }

    public void setIdList(List<UUID> idList) {
        this.idList = idList;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @Nullable
    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(@Nullable UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public String getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(String cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
