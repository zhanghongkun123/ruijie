package com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/21 9:25
 *
 * @author yxq
 */
public class UserMacBindingDTO {

    @NotBlank
    private String userName;

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
        UserMacBindingDTO that = (UserMacBindingDTO) o;
        return Objects.equals(userName, that.userName) && Objects.equals(terminalMac, that.terminalMac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, terminalMac);
    }
}
