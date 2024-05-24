package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.AssistCertification;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.PrimaryCertification;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextMedium;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/21
 *
 * @author xiao'yong'deng
 */
public class CreateUserGroupRequest {

    @NotBlank
    @TextMedium
    private String name;

    @Nullable
    private UUID parentId;

    @Nullable
    private Long accountExpireDate;

    @Nullable
    private Integer invalidTime;

    @Nullable
    private PrimaryCertification primaryCertification;

    @Nullable
    private AssistCertification assistCertification;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(@Nullable UUID parentId) {
        this.parentId = parentId;
    }

    @Nullable
    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(@Nullable Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    @Nullable
    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(@Nullable Integer invalidTime) {
        this.invalidTime = invalidTime;
    }

    @Nullable
    public PrimaryCertification getPrimaryCertification() {
        return primaryCertification;
    }

    public void setPrimaryCertification(@Nullable PrimaryCertification primaryCertification) {
        this.primaryCertification = primaryCertification;
    }

    @Nullable
    public AssistCertification getAssistCertification() {
        return assistCertification;
    }

    public void setAssistCertification(@Nullable AssistCertification assistCertification) {
        this.assistCertification = assistCertification;
    }
}
