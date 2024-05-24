package com.ruijie.rcos.rcdc.rco.module.impl.cache;



import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Description: 管理员在终端登录，会话信息缓存
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/18 21:31
 *
 * @author zhangyichi
 */
public class AdminLoginOnTerminalCache {

    private UUID adminId;

    private String adminName;

    private String terminalId;

    private LocalDateTime timestamp;

    public AdminLoginOnTerminalCache() {
    }

    public AdminLoginOnTerminalCache(IacAdminDTO adminDTO, String terminalId) {
        this.adminId = adminDTO.getId();
        this.adminName = adminDTO.getUserName();
        this.terminalId = terminalId;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getAdminId() {
        return adminId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getAdminName() {
        return adminName;
    }

    /**
     * 更新时间戳
     */
    synchronized void updateTimeStamp() {
        this.timestamp = LocalDateTime.now();
    }
}
