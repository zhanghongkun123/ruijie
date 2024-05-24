package com.ruijie.rcos.rcdc.rco.module.def.desktopsession;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbRdsMgrDesktopSessionInfoDTO;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月22日
 *
 * @author wangjie9
 */
public class UpdateReportSessionSuccessDTO {

    /**
     * 云应用在线会话上报成功列表
     */
    @Nullable
    private List<CbbRdsMgrDesktopSessionInfoDTO> reportOnlineSessionSuccessList;

    /**
     * 云应用注销会话上报成功列表
     */
    @Nullable
    private List<CbbRdsMgrDesktopSessionInfoDTO> reportDestroySessionSuccessList;


    @Nullable
    public List<CbbRdsMgrDesktopSessionInfoDTO> getReportOnlineSessionSuccessList() {
        return reportOnlineSessionSuccessList;
    }

    public void setReportOnlineSessionSuccessList(@Nullable List<CbbRdsMgrDesktopSessionInfoDTO> reportOnlineSessionSuccessList) {
        this.reportOnlineSessionSuccessList = reportOnlineSessionSuccessList;
    }

    @Nullable
    public List<CbbRdsMgrDesktopSessionInfoDTO> getReportDestroySessionSuccessList() {
        return reportDestroySessionSuccessList;
    }

    public void setReportDestroySessionSuccessList(@Nullable List<CbbRdsMgrDesktopSessionInfoDTO> reportDestroySessionSuccessList) {
        this.reportDestroySessionSuccessList = reportDestroySessionSuccessList;
    }
}
