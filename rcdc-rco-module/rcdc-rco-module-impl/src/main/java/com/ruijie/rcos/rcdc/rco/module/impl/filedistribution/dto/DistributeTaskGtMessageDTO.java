package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description: 下发文件分发任务GT消息结构体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/23 09:55
 *
 * @author zhangyichi
 */
public class DistributeTaskGtMessageDTO {

    @JSONField(name = "task_info", alternateNames = "taskInfo")
    private DesktopDistributeTaskInfoDTO taskInfo;

    private FtpConfigDTO ftpConfig;

    public DesktopDistributeTaskInfoDTO getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(DesktopDistributeTaskInfoDTO taskInfo) {
        this.taskInfo = taskInfo;
    }

    public FtpConfigDTO getFtpConfig() {
        return ftpConfig;
    }

    public void setFtpConfig(FtpConfigDTO ftpConfig) {
        this.ftpConfig = ftpConfig;
    }
}
