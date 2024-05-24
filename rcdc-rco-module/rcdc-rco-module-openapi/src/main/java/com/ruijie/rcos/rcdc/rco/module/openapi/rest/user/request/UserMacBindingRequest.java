package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.BatchTaskRequest;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.Size;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月22日
 *
 * @author zdc
 */
public class UserMacBindingRequest extends BatchTaskRequest {

    /**
     * 用户名
     */
    @NotBlank
    private String userName;

    /**
     * 终端mac
     */
    @NotBlank
    private String terminalMac;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserMacBindingRequest that = (UserMacBindingRequest) o;
        return userName.equals(that.userName) && terminalMac.equals(that.terminalMac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, terminalMac);
    }
}
