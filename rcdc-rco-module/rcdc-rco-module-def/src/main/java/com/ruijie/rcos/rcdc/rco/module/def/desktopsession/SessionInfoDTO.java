package com.ruijie.rcos.rcdc.rco.module.def.desktopsession;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 获取RDSMgr会话数返回结果
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月22日
 *
 * @author wangjie9
 */

public class SessionInfoDTO {

    @NotNull
    private UUID hostId;

    /**
     * 应用主机会话数
     */
    @NotNull
    private Integer count;

    /**
     * 用户列表
     */
    @Nullable
    private List<String> userList;

    /**
     * 应用主机上所有用户会话信息列表
     */
    @Nullable
    private List<RdsMgrDesktopSessionInfoDTO> userSessionInfoList;

    /**
     * OA上报即将在线的会话信息
     */
    @Nullable
    private List<RdsMgrDesktopSessionInfoDTO> needReportOnlineSessionInfoList;

    /**
     * OA上报即将离线的会话信息
     */
    @Nullable
    private List<RdsMgrDesktopSessionInfoDTO> needReportDestroySessionInfoList;


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public List<RdsMgrDesktopSessionInfoDTO> getUserSessionInfoList() {
        return userSessionInfoList;
    }

    public void setUserSessionInfoList(List<RdsMgrDesktopSessionInfoDTO> userSessionInfoList) {
        this.userSessionInfoList = userSessionInfoList;
    }

    public List<RdsMgrDesktopSessionInfoDTO> getNeedReportOnlineSessionInfoList() {
        return needReportOnlineSessionInfoList;
    }

    public void setNeedReportOnlineSessionInfoList(List<RdsMgrDesktopSessionInfoDTO> needReportOnlineSessionInfoList) {
        this.needReportOnlineSessionInfoList = needReportOnlineSessionInfoList;
    }

    public List<RdsMgrDesktopSessionInfoDTO> getNeedReportDestroySessionInfoList() {
        return needReportDestroySessionInfoList;
    }

    public void setNeedReportDestroySessionInfoList(List<RdsMgrDesktopSessionInfoDTO> needReportDestroySessionInfoList) {
        this.needReportDestroySessionInfoList = needReportDestroySessionInfoList;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }
}
