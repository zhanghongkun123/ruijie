package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import java.util.Date;
import java.util.UUID;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacLastLoginInfoDTO;
import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/25 13:47
 *
 * @author linrenjian
 */
public class LoginInfoChangeRequestBaseDTO {

    /**
     * WEB 客户端的IP，
     */
    @NotBlank
    private String ip;

    /**
     * 用户名称
     */
    @NotBlank
    private String userName;


    @Nullable
    private Date lastLoginTerminalTime;

    /**
     * 转换为 登陆变更信息
     *
     * @param terminalId 桌面ID
     * @return 更新最后信息
     */
    public LoginInfoChangeRequestDTO buildNewLoginInfoChangeRequestDTO(String terminalId) {
        Assert.notNull(terminalId, "id 不能为null");

        LoginInfoChangeRequestDTO loginInfoChangeRequestDTO = new LoginInfoChangeRequestDTO();
        loginInfoChangeRequestDTO.setIp(ip);
        loginInfoChangeRequestDTO.setUserName(userName);
        loginInfoChangeRequestDTO.setLastLoginTerminalTime(lastLoginTerminalTime);
        loginInfoChangeRequestDTO.setTerminalId(terminalId);
        return loginInfoChangeRequestDTO;
    }


    /**
     * 转换为 用户最后更新信息
     *
     * @param id 用户ID
     * @return 更新最后信息
     */
    public IacLastLoginInfoDTO buildNewCbbLastLoginInfoDTO(UUID id) {
        Assert.notNull(id, "id 不能为null");

        IacLastLoginInfoDTO cbbLastLoginInfoDTO = new IacLastLoginInfoDTO();
        cbbLastLoginInfoDTO.setLastLoginTerminalIp(ip);
        cbbLastLoginInfoDTO.setLastLoginTerminalTime(lastLoginTerminalTime);
        cbbLastLoginInfoDTO.setId(id);
        return cbbLastLoginInfoDTO;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Nullable
    public Date getLastLoginTerminalTime() {
        return lastLoginTerminalTime;
    }

    public void setLastLoginTerminalTime(@Nullable Date lastLoginTerminalTime) {
        this.lastLoginTerminalTime = lastLoginTerminalTime;
    }
}
