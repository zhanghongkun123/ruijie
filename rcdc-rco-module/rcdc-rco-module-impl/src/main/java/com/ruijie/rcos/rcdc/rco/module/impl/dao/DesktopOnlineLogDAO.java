package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopOnlineLogEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 云桌面登录登出记录日志DAO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/20 11:06
 *
 * @author linke
 */
public interface DesktopOnlineLogDAO extends SkyEngineJpaRepository<DesktopOnlineLogEntity, UUID> {

    /**
     * 查询当前EST连接的在线数量用户
     *
     * @return 数量
     */
    @Query(value = "select count(distinct last_login_record.desktop_id)" +
            "from (select desktop_id, max(operation_time) last_login_time" +
            "      from t_rco_desktop_online_log" +
            "      where operation_type = 'EST_LOGIN' and desktop_id in (select desk_id from t_cbb_desk_info where desk_state = 'RUNNING')" +
            "      group by desktop_id) last_login_record" +
            "         left join (select desktop_id, max(operation_time) last_logout_time" +
            "                    from t_rco_desktop_online_log" +
            "                    where operation_type = 'EST_LOGOUT'" +
            "                    group by desktop_id) last_logout_record" +
            "                   on last_login_record.desktop_id = last_logout_record.desktop_id " +
            "where last_logout_record.last_logout_time < last_login_record.last_login_time " +
            "   or last_logout_record.last_logout_time is null ", nativeQuery = true)
    Long countCurrentOnlineDesktop();

    /**
     ** 查询超期的操作日志条数
     *
     * @param overdueTime 日志时间
     * @return  数量
     */
    Integer countByOperationTimeLessThan(Date overdueTime);

    /**
     ** 删除超期的操作日志条数
     * @param overdueTime 日志时间
     */
    @Transactional
    @Modifying
    void deleteByOperationTimeLessThan(Date overdueTime);
}
