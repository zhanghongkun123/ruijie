package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author chenl
 */
public class ImportUserGroupDataRequest {


    @NotBlank
    private String id;

    @NotBlank
    @Size(max = 64)
    private String name;

    @NotBlank
    private String parentGroupId;

    @NotNull
    private AuthType authType;

    @NotBlank
    @Size(max = 64)
    private String parentGroupName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(@Nullable String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    @Nullable
    public String getParentGroupName() {
        return parentGroupName;
    }

    public void setParentGroupName(@Nullable String parentGroupName) {
        this.parentGroupName = parentGroupName;
    }
}
