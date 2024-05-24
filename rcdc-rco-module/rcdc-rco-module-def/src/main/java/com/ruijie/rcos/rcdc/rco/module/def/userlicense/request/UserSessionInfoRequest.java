package com.ruijie.rcos.rcdc.rco.module.def.userlicense.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 用户会话信息请求对象
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
public class UserSessionInfoRequest {

    /**
     * 会话数量
     */
    @NotNull
    private Integer count;

    /**
     * 会话详情列表
     */
    @Nullable
    private List<UserSessionDTO> sessionInfoList;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UserSessionInfoRequest() {
    }

    public UserSessionInfoRequest(Integer count, @Nullable List<UserSessionDTO> sessionInfoList) {
        this.count = count;
        this.sessionInfoList = sessionInfoList;
    }

    public UserSessionInfoRequest(@Nullable List<UserSessionDTO> sessionInfoList) {
        this.sessionInfoList = sessionInfoList;
        if (sessionInfoList == null) {
            this.sessionInfoList = new ArrayList<>();
        }
        this.count = this.sessionInfoList.size();
    }

    @Nullable
    public List<UserSessionDTO> getSessionInfoList() {
        return sessionInfoList;
    }

    public void setSessionInfoList(@Nullable List<UserSessionDTO> sessionInfoList) {
        this.sessionInfoList = sessionInfoList;
    }
}
