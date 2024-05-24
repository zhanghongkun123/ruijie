package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopLicenseStatEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description: 云桌面授权使用情况统计DAO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/14
 *
 * @author zjy
 */
public interface DesktopLicenseStatDAO extends SkyEngineJpaRepository<DesktopLicenseStatEntity, UUID> {

    /**
     * 获取指定时间内的云桌面平均使用率信息
     *
     * @param licenseType 证书类型
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param step        步长，包括（1 minutes, 5 minutes, 1 hours, 1 days)
     * @return 授权使用趋势数据
     */
    @Query(value = (
        "select" +
            " coalesce(a.atime) create_time, coalesce(b.total_count, 0) total_count, coalesce(b.used_count, 0) used_count," +
            " coalesce(b.vdi_used_count, 0) vdi_used_count, coalesce(b.idv_used_count, 0) idv_used_count, " +
            " coalesce(b.voi_used_count, 0) voi_used_count," +
            " coalesce(b.rca_used_count, 0) rca_used_count, coalesce(b.edu_voi_used_count, 0) edu_voi_used_count" +
        " from" +
            " (select tmpTime as atime from generate_series(" +
            " CAST( ?2 AS TIMESTAMP),CAST( ?3 AS TIMESTAMP),CAST( ?4 AS INTERVAL)) as tmpTime) a" +
        " left join t_rco_desktop_License_stat b on b.create_time = a.atime and b.license_type = ?1"), nativeQuery = true)
    List<Map<String, Object>> statsByLicenseTypeAndTime(String licenseType, Date startTime, Date endTime, String step);


    /**
     * 清理某个日期之前的统计数据
     *
     * @param createTime  清理时间
     * @return 清理行数
     */
    @Transactional
    @Modifying
    Integer deleteByCreateTimeBefore(Date createTime);
}
