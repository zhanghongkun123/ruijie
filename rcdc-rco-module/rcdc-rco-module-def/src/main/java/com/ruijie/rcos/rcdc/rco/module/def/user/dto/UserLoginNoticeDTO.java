package com.ruijie.rcos.rcdc.rco.module.def.user.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherKey;

import java.util.UUID;

/**
 * Description: 用户登录事件通知请求
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/6
 *
 * @author lintingling
 */
public class UserLoginNoticeDTO {

    @NotBlank
    @DispatcherKey
    private String dispatcherKey;

    @NotNull
    private UUID userId;

    public UserLoginNoticeDTO() {

    }

    public UserLoginNoticeDTO(String noticeEvent, UUID userId) {
        this.dispatcherKey = noticeEvent;
        this.userId = userId;
    }

    public String getDispatcherKey() {
        return dispatcherKey;
    }

    public void setDispatcherKey(String dispatcherKey) {
        this.dispatcherKey = dispatcherKey;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
