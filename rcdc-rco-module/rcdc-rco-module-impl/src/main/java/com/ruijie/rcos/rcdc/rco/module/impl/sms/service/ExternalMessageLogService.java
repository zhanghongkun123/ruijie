package com.ruijie.rcos.rcdc.rco.module.impl.sms.service;

import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageBusinessType;

import java.util.Date;

/**
 * Description: ExternalMessageLogService
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author TD
 */
public interface ExternalMessageLogService {

    /**
     ** 查询超期的操作日志条数
     *
     * @param overdueTime 日志时间
     * @return  数量
     */
    Integer countBySendTimeLessThan(Date overdueTime);

    /**
     ** 删除超期的操作日志条数
     * @param overdueTime 日志时间
     */
    void deleteBySendTimeLessThan(Date overdueTime);

    /**
     * 根据消息类型、关联对象获取当天成功消息总数
     * @param relatedType 消息类型
     * @param relatedTarget 关联对象
     * @return 消息总数
     */
    Long countCurrentDayMessageSuccessTotalNumber(MessageBusinessType relatedType, String relatedTarget);
}
