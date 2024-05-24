package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.entity.ViewAuditPrintLogEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

/**
 * Description: 打印记录视图DAO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/26
 *
 * @author WuShengQiang
 */
public interface ViewAuditPrintLogDAO extends SkyEngineJpaRepository<ViewAuditPrintLogEntity, UUID> {

    /**
     * 用户维度统计打印报表
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户维度统计打印报表
     */
    @Query(value = "select a.user_id, a.user_name, a.print_page, a.print_count, b.total_print_count, "
            + "concat(round(cast(a.print_count as numeric) / cast(b.total_print_count as numeric)* 100, 2) , '%') print_count_percent from "
            + "(select cast(user_id as varchar), user_name, sum(print_page_count) print_page, count(id) print_count "
            + "from v_rco_audit_print_log_static t "
            + "where apply_type = 'PRINT' and print_state = 'SUCCESS' and print_time between :startTime and :endTime "
            + "group by user_id, user_name) a, "
            + "(select count(id) total_print_count "
            + "from v_rco_audit_print_log_static t "
            + "where apply_type = 'PRINT' and print_state = 'SUCCESS' and print_time between :startTime and :endTime) b", nativeQuery = true)
    List<Map<String, Object>> staticUserPrintLogByPrintTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 用户组维度统计打印报表
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户组维度统计打印报表
     */
    @Query(value = "select a.group_id, a.group_name, a.print_page, a.print_count, b.total_print_count, "
            + "concat(round(cast(a.print_count as numeric) / cast(b.total_print_count as numeric)* 100, 2) , '%') print_count_percent from "
            + "(select cast(group_id as varchar), group_name, sum(print_page_count) print_page, count(id) print_count "
            + "from v_rco_audit_print_log_static t "
            + "where apply_type = 'PRINT' and print_state = 'SUCCESS' and print_time between :startTime and :endTime "
            + "group by group_id, group_name) a, "
            + "(select count(id) total_print_count "
            + "from v_rco_audit_print_log_static t "
            + "where apply_type = 'PRINT' and print_state = 'SUCCESS' and print_time between :startTime and :endTime) b", nativeQuery = true)
    List<Map<String, Object>> staticUserGroupPrintLogByPrintTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 云桌面维度统计打印报表
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 云桌面维度统计打印报表
     */
    @Query(value = "select a.desktop_id, a.desktop_name, a.print_page, a.print_count, b.total_print_count, "
            + "concat(round(cast(a.print_count as numeric) / cast(b.total_print_count as numeric)* 100, 2) , '%') print_count_percent from "
            + "(select cast(desktop_id as varchar), desktop_name, sum(print_page_count) print_page, count(id) print_count "
            + "from v_rco_audit_print_log_static t "
            + "where apply_type = 'PRINT' and print_state = 'SUCCESS' and print_time between :startTime and :endTime "
            + "group by desktop_id, desktop_name) a, "
            + "(select count(id) total_print_count "
            + "from v_rco_audit_print_log_static t "
            + "where apply_type = 'PRINT' and print_state = 'SUCCESS' and print_time between :startTime and :endTime) b", nativeQuery = true)
    List<Map<String, Object>> staticDesktopPrintLogByPrintTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 终端维度统计打印报表
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 终端维度统计打印报表
     */
    @Query(value = "select a.terminal_id, a.terminal_name, a.print_page, a.print_count, b.total_print_count, "
            + "concat(round(cast(a.print_count as numeric) / cast(b.total_print_count as numeric)* 100, 2) , '%') print_count_percent from "
            + "(select cast(terminal_id as varchar), terminal_name, sum(print_page_count) print_page, count(id) print_count "
            + "from v_rco_audit_print_log_static t "
            + "where apply_type = 'PRINT' and print_state = 'SUCCESS' and print_time between :startTime and :endTime "
            + "group by terminal_id, terminal_name) a, "
            + "(select count(id) total_print_count "
            + "from v_rco_audit_print_log_static t "
            + "where apply_type = 'PRINT' and print_state = 'SUCCESS' and print_time between :startTime and :endTime) b", nativeQuery = true)
    List<Map<String, Object>> staticTerminalPrintLogByPrintTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
