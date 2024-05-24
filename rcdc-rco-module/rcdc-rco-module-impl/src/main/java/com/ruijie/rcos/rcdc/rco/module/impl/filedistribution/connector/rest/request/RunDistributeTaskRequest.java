package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto.AppClientDistributeTaskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto.FtpConfigDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/23 13:42
 *
 * @author zhangyichi
 */
public class RunDistributeTaskRequest {

    @NotNull
    @JSONField(name = "taskArr")
    private AppClientDistributeTaskInfoDTO[] taskArr;

    private FtpConfigDTO ftpConfig;

    public AppClientDistributeTaskInfoDTO[] getTaskArr() {
        return taskArr;
    }

    public void setTaskArr(AppClientDistributeTaskInfoDTO[] taskArr) {
        this.taskArr = taskArr;
    }

    public FtpConfigDTO getFtpConfig() {
        return ftpConfig;
    }

    public void setFtpConfig(FtpConfigDTO ftpConfig) {
        this.ftpConfig = ftpConfig;
    }
}
