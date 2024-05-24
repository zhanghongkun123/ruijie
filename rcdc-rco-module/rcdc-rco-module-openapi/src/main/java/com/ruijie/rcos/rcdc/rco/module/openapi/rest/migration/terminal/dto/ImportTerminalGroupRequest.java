package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto;

import com.ruijie.rcos.rcdc.rco.module.openapi.service.common.ServerBusinessName;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
@ServerBusinessName("终端组信息导入")
public class ImportTerminalGroupRequest {

    @NotNull
    private Long id;

    @NotBlank
    @TextShort
    private String name;

    @Nullable
    private Long parentGroup;

    @Nullable
    private String[] ssidArr;

    @Nullable
    private Boolean needApplyToSubgroup;

    @Nullable
    private Long imageTemplateId;

    @Nullable
    private ImportDesktopConfig desktopConfig;

    @Nullable
    public Boolean getNeedApplyToSubgroup() {
        return needApplyToSubgroup;
    }

    public void setNeedApplyToSubgroup(@Nullable Boolean needApplyToSubgroup) {
        this.needApplyToSubgroup = needApplyToSubgroup;
    }

    @Nullable
    public ImportDesktopConfig getDesktopConfig() {
        return desktopConfig;
    }

    public void setDesktopConfig(@Nullable ImportDesktopConfig desktopConfig) {
        this.desktopConfig = desktopConfig;
    }

    @Nullable
    public Long getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(@Nullable Long imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public Long getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(@Nullable Long parentGroup) {
        this.parentGroup = parentGroup;
    }

    @Nullable
    public String[] getSsidArr() {
        return ssidArr;
    }

    public void setSsidArr(@Nullable String[] ssidArr) {
        this.ssidArr = ssidArr;
    }
}
