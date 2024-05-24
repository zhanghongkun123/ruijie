package com.ruijie.rcos.rcdc.rco.module.def.user.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月7日
 * 
 * @author wjp
 */
public class UserGroupOperNotifyDTO {

    @NotBlank
    private String oper;

    @NotNull
    private Long timestamp;

    @NotNull
    private UserGroupOperNotifyContentDTO userGroupOperNotifyContentDTO;

    @NotNull
    private UserOperSyncNotifyDTO userOperSyncNotifyDTO;

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public UserGroupOperNotifyContentDTO getUserGroupOperNotifyContentDTO() {
        return userGroupOperNotifyContentDTO;
    }

    public void setUserGroupOperNotifyContentDTO(UserGroupOperNotifyContentDTO userGroupOperNotifyContentDTO) {
        this.userGroupOperNotifyContentDTO = userGroupOperNotifyContentDTO;
    }

    public UserOperSyncNotifyDTO getUserOperSyncNotifyDTO() {
        return userOperSyncNotifyDTO;
    }

    public void setUserOperSyncNotifyDTO(UserOperSyncNotifyDTO userOperSyncNotifyDTO) {
        this.userOperSyncNotifyDTO = userOperSyncNotifyDTO;
    }
}
