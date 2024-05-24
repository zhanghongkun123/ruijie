package com.ruijie.rcos.rcdc.rco.module.impl.cmc.dto;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Description:  GuestTool 上报软件信息统计信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.11.05
 *
 * @author LinHJ
 */
public class CmcMessagePerformUtil {

    // 消费成功记录
    public static AtomicLong receiveSuccessCount = new AtomicLong(0);

    // 消费失败记录
    public static AtomicLong receiveFailCount = new AtomicLong(0);

    // 收到请求记录
    public static AtomicLong dealCount = new AtomicLong(0);

}
