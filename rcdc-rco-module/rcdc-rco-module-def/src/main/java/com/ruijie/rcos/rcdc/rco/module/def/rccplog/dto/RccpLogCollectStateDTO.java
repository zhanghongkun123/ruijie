package com.ruijie.rcos.rcdc.rco.module.def.rccplog.dto;

import com.ruijie.rcos.rcdc.rco.module.def.rccplog.enums.RccpLogCollectState;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/22 14:27
 *
 * @author zhangyichi
 */
public class RccpLogCollectStateDTO {

    @NotNull
    private UUID hostId;

    @NotNull
    private RccpLogCollectState state;

    @Nullable
    private String logFileName;

    @Nullable
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public RccpLogCollectState getState() {
        return state;
    }

    public void setState(RccpLogCollectState state) {
        this.state = state;
    }

    @Nullable
    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(@Nullable String logFileName) {
        this.logFileName = logFileName;
    }
}
