package com.ruijie.rcos.rcdc.rco.module.impl.sms.dao;

import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageBusinessType;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessagePlatformType;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageStatus;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.entity.ExternalMessageLogEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * Description: ExternalMessageLogDAO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/14
 *
 * @author TD
 */
public interface ExternalMessageLogDAO extends SkyEngineJpaRepository<ExternalMessageLogEntity, UUID> {

    /**
     * 指定关联类型、目标对象、平台类型获取发送成功的消息总数
     * @param relatedType 关联类型
     * @param relatedTarget 目标对象
     * @param platformType 平台类型
     * @return 消息总数量
     */
    @Query(nativeQuery = true, value = "select count(1) from t_rco_external_message_log where related_type = ?1 " +
            "and related_target = ?2 and platform_type = ?3 and status = 'SUCCESS' and send_time >= current_date")
    long countCurrentDayMessageSuccessTotalNumber(String relatedType, String relatedTarget, String platformType);

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
    @Transactional
    @Modifying
    void deleteBySendTimeLessThan(Date overdueTime);
}
