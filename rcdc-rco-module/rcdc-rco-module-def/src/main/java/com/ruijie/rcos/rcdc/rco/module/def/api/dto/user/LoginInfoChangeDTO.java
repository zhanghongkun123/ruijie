package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/25 14:03
 *
 * @author linrenjian
 */
public class LoginInfoChangeDTO {

    /**
     * 用户ID
     */

    private UUID id;

    /**
     * 是否需要通知用户登录信息变化
     */
    private Boolean needNotifyLoginTerminalChange;

    /**
     * 上一次登录IP
     */
    private String lastLoginTerminalIp;

    /**
     * 用户上一次登录终端时间
     */
    private Date lastLoginTerminalTime;



    /**
     * 是否需要通知登陆终端信息变化
     * @param ip IP
     */
    public void buildNeedNotifyLoginTerminalChange(@Nullable String ip) {
        // 全局策略提示开关已开启，查询用户的上一次登录信息进行比较
        if (StringUtils.hasText(this.getLastLoginTerminalIp()) && !this.getLastLoginTerminalIp().equals(ip)) {
            this.setNeedNotifyLoginTerminalChange(Boolean.TRUE);
        } else {
            this.setNeedNotifyLoginTerminalChange(Boolean.FALSE);
        }
    }



    public Boolean getNeedNotifyLoginTerminalChange() {
        return needNotifyLoginTerminalChange;
    }

    public void setNeedNotifyLoginTerminalChange(Boolean needNotifyLoginTerminalChange) {
        this.needNotifyLoginTerminalChange = needNotifyLoginTerminalChange;
    }

    public String getLastLoginTerminalIp() {
        return lastLoginTerminalIp;
    }

    public void setLastLoginTerminalIp(String lastLoginTerminalIp) {
        this.lastLoginTerminalIp = lastLoginTerminalIp;
    }

    public Date getLastLoginTerminalTime() {
        return lastLoginTerminalTime;
    }

    public void setLastLoginTerminalTime(Date lastLoginTerminalTime) {
        this.lastLoginTerminalTime = lastLoginTerminalTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
