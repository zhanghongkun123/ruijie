package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import java.util.Date;
import java.util.Objects;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/25 13:47
 *
 * @author linrenjian
 */
public class LoginInfoChangeRequestDTO extends LoginInfoChangeRequestBaseDTO {

    /**
     * 终端ID
     */
    @NotNull
    private String terminalId;

    /**
     * 构造 登陆时间
     *
     * @return 本身
     */
    public LoginInfoChangeRequestDTO buildLastLoginTerminalTime() {
        if (Objects.isNull(this.getLastLoginTerminalTime())) {
            this.setLastLoginTerminalTime(new Date());
        }
        return this;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

}
