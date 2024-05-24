package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;

/**
 * Description: 用户web客户端登录结果
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/28
 *
 * @author Jarman
 */
public class UserWebClientLoginResultDTO {

    private UUID id;

    private String userName;

    private String realName;

    private Date createTime;

    /**
     * 密码是否为弱密码
     */
    private Boolean needUpdatePwd;

    /**
     * 是否开启防爆功能
     */
    private Boolean preventsBruteForce;

    private IacUserStateEnum userState;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getNeedUpdatePwd() {
        return needUpdatePwd;
    }

    public void setNeedUpdatePwd(Boolean needUpdatePwd) {
        this.needUpdatePwd = needUpdatePwd;
    }

    public Boolean getPreventsBruteForce() {
        return preventsBruteForce;
    }

    public void setPreventsBruteForce(Boolean preventsBruteForce) {
        this.preventsBruteForce = preventsBruteForce;
    }

    public IacUserStateEnum getUserState() {
        return userState;
    }

    public void setUserState(IacUserStateEnum userState) {
        this.userState = userState;
    }
}