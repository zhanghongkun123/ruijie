package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.response;

import com.ruijie.rcos.rcdc.rca.module.def.enums.CollectLogState;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: 查看日志收集返回结果
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月22日
 *
 * @author liuwc
 */
public class GetRcaHostCollectLogStateWebResponse {

    @NotNull
    private CollectLogState state;

    @Nullable
    private String logFileName;

    @Nullable
    private String message;

    public CollectLogState getState() {
        return state;
    }

    public void setState(CollectLogState state) {
        this.state = state;
    }

    @Nullable
    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(@Nullable String logFileName) {
        this.logFileName = logFileName;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

}
