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
 * @author ljm
 */
public class UserOperNotifyDTO {

    @NotBlank
    private String oper;

    @NotNull
    private Long timestamp;

    @NotNull
    private UserOperNotifyContentDTO notifyDto;

    @NotNull
    private UserOperSyncNotifyDTO userOperSyncDto;

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

    public UserOperNotifyContentDTO getNotifyDto() {
        return notifyDto;
    }

    public void setNotifyDto(UserOperNotifyContentDTO notifyDto) {
        this.notifyDto = notifyDto;
    }

    public UserOperSyncNotifyDTO getUserOperSyncDto() {
        return userOperSyncDto;
    }

    public void setUserOperSyncDto(UserOperSyncNotifyDTO userOperSyncDto) {
        this.userOperSyncDto = userOperSyncDto;
    }
}
