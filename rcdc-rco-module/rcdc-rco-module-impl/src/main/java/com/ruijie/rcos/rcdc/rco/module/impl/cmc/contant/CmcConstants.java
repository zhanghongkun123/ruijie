package com.ruijie.rcos.rcdc.rco.module.impl.cmc.contant;

/**
 * Description:  GuestTool 上报软件信息常量
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.11.05
 *
 * @author LinHJ
 */
public class CmcConstants {

    // 分割字符
    public final static String SPLIT_VERSION = "_&&_";

    // 默认线程配置
    public final static int DEFAULT_CHILD_THREAD_NUM = 1;

    // 默认线程消费队列大小
    public final static int DEFAULT_QUEUE_NUM = 1000;

    // 等待软件处理锁时间
    public final static int WAIT_SOFT_TIMES = 3 * 10;

    // 软件处理统计次数日志打印间隔时间
    public final static int LOG_PRINT_TIMES = 5 * 60 * 1000;

    // 软件锁前缀
    public final static String LOCK_DESKSOFT_MSG = "LOCK_DESKSOFT_MSG_";

    // 配置缓存更新时间
    public final static int UPDATE_SETTING_CACHE = 600 * 1000;

    // VDI 部署模式子线程数量
    public final static String CONFIG_VDI_CHILD_THREAD_NUM_KEY = "rcdc.cmc.child.thread.vdi.num";

    // RCM 部署模式子线程数量
    public final static String CONFIG_RCM_CHILD_THREAD_NUM_KEY = "rcdc.cmc.child.thread.rcm.num";
}
