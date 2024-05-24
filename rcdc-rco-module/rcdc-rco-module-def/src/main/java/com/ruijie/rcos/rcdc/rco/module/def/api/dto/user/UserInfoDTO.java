package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import java.util.Date;
import java.util.UUID;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacLastLoginInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;



/**
 * Description: 普通用户信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-14 10:46:00
 *
 * @author zjy
 */
public class UserInfoDTO {

    private UUID uuid;

    private String userName;

    private String displayName;

    private UUID userGroupId;

    private IacUserStateEnum state;

    private IacUserTypeEnum userType;

    private Long accountExpireDate;

    private Integer invalidTime;

    private String description;

    private String invalidDescription;


    /**
     * 是否失效
     */
    private Boolean invalid;

    /**
     * 最后登出时间
     */
    private Date loginOutTime;


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 失效账户恢复时间
     */
    private Date invalidRecoverTime;



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

    private String password;

    /**
     * 转换为 用户最后更新信息
     * 
     * @param ip ip 信息
     * @return 最后更新信息
     */

    public IacLastLoginInfoDTO buildNewCbbLastLoginInfoDTO(String ip) {
        Assert.notNull(ip, "ip 不能为null");

        IacLastLoginInfoDTO cbbLastLoginInfoDTO = new IacLastLoginInfoDTO();
        cbbLastLoginInfoDTO.setLastLoginTerminalIp(ip);
        cbbLastLoginInfoDTO.setLastLoginTerminalTime(new Date());
        cbbLastLoginInfoDTO.setId(this.getUuid());
        return cbbLastLoginInfoDTO;
    }


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public IacUserStateEnum getState() {
        return state;
    }

    public void setState(IacUserStateEnum state) {
        this.state = state;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvalidDescription() {
        return invalidDescription;
    }

    public void setInvalidDescription(String invalidDescription) {
        this.invalidDescription = invalidDescription;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    public Date getLoginOutTime() {
        return loginOutTime;
    }

    public void setLoginOutTime(Date loginOutTime) {
        this.loginOutTime = loginOutTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getInvalidRecoverTime() {
        return invalidRecoverTime;
    }

    public void setInvalidRecoverTime(Date invalidRecoverTime) {
        this.invalidRecoverTime = invalidRecoverTime;
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

    public Boolean getNeedNotifyLoginTerminalChange() {
        return needNotifyLoginTerminalChange;
    }

    public void setNeedNotifyLoginTerminalChange(Boolean needNotifyLoginTerminalChange) {
        this.needNotifyLoginTerminalChange = needNotifyLoginTerminalChange;
    }
}
