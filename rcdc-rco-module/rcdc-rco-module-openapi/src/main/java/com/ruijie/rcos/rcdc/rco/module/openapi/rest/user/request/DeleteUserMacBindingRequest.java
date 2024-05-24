package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.BatchTaskRequest;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月22日
 *
 * @author zdc
 */
public class DeleteUserMacBindingRequest extends BatchTaskRequest {

    /**
     * 绑定关系id
     */
    @NotNull
    UUID bindingId;

    public UUID getBindingId() {
        return bindingId;
    }

    public void setBindingId(UUID bindingId) {
        this.bindingId = bindingId;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeleteUserMacBindingRequest that = (DeleteUserMacBindingRequest) o;
        return Objects.equals(bindingId, that.bindingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindingId);
    }
}
