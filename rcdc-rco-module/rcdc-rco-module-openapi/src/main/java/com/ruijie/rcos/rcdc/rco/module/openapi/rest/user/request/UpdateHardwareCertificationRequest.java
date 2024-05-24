package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacUserHardwareCertificationStateEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.BatchTaskRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月24日
 *
 * @author zdc
 */
public class UpdateHardwareCertificationRequest extends BatchTaskRequest {

    /**
     * 绑定关系id
     */
    @NotNull
    UUID bindingId;

    /**
     * 修改状态
     */
    @NotNull
    IacUserHardwareCertificationStateEnum state;

    public UUID getBindingId() {
        return bindingId;
    }

    public void setBindingId(UUID bindingId) {
        this.bindingId = bindingId;
    }

    public IacUserHardwareCertificationStateEnum getState() {
        return state;
    }

    public void setState(IacUserHardwareCertificationStateEnum state) {
        this.state = state;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateHardwareCertificationRequest that = (UpdateHardwareCertificationRequest) o;
        return Objects.equals(bindingId, that.bindingId) && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindingId, state);
    }
}
