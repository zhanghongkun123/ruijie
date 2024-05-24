package com.ruijie.rcos.rcdc.rco.module.impl.datasync.constant;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/21 11:39
 *
 * @author coderLee23
 */
public interface DataSyncConstant {

    /**
     * 批量同步用户数据线程组
     */
    String BATCH_SYNC_USER_DATA_THREAD = "batch_sync_user_data_thread";

    /**
     * 批量同步用户组数据线程组
     */
    String BATCH_SYNC_USER_GROUP_DATA_THREAD = "batch_sync_user_group_data_thread";

    /**
     * 线程最大数量
     */
    Integer MAX_THREAD_NUM = 10;

    /**
     * 线程最大数量
     */
    Integer QUEUE_SIZE = 1000;

    /**
     * t_sk_global_parameter表存放云桌面日志保留天数键值
     * */
    String DESK_OP_LOG_RETAIN_DAY = "desk_op_log_retain_day";

}
