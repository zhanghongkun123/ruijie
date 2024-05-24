package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * Description: 文件分发任务暂存状态
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/22 11:16
 *
 * @author zhangyichi
 */
public enum FileDistributionStashTaskStatus {

    NONE,
    QUEUED,
    RUNNING,
    STASHED,
    PRE_CANCEL,
    CANCELING,
    REBUILDING
}
