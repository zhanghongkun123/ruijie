package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/10 19:54
 *
 * @author coderLee23
 */
public class GetTestSelectUserGroupDesktopWebRequest implements WebRequest {

    /**
     * 用户组id
     */
    @ApiModelProperty(value = " 用户组id")
    @NotNull
    private UUID userGroupId;

    /**
     * 规格：操作系统类型
     */
    @ApiModelProperty(value = "操作系统类型")
    @Nullable
    private CbbOsType osType;

    /**
     * 规格：镜像类型
     */
    @ApiModelProperty(value = "镜像类型")
    @Nullable
    private CbbImageType cbbImageType;

    /**
     * 规格：镜像类型
     */
    @ApiModelProperty(value = "桌面测试状态")
    @Nullable
    private DesktopTestStateEnum state;

    /**
     * 模糊查询
     */
    @ApiModelProperty(value = " 模糊查询")
    @Nullable
    private String searchName;

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    @Nullable
    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(@Nullable CbbOsType osType) {
        this.osType = osType;
    }

    @Nullable
    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(@Nullable CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    @Nullable
    public DesktopTestStateEnum getState() {
        return state;
    }

    public void setState(@Nullable DesktopTestStateEnum state) {
        this.state = state;
    }

    @Nullable
    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(@Nullable String searchName) {
        this.searchName = searchName;
    }
}
