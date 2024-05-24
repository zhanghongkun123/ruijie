package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.ruijie.rcos.rcdc.license.module.def.dto.CbbLicenseClassifiedUsageDTO;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseDurationEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.entity.UserLicenseEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;

/**
 * Description: 用户授权记录持久化接口
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
public interface UserLicenseDAO extends SkyEngineJpaRepository<UserLicenseEntity, UUID>, PageQueryDAO<UserLicenseEntity> {

    /**
     * 根据授权持续类型与授权类型进行删除
     * @param duration 授权持续类型
     * @param licenseType 授权类型
     * @return 删除条数
     */
    int deleteByLicenseDurationAndLicenseType(CbbLicenseDurationEnum duration, String licenseType);

    /**
     * @return 按licenseDuration和licenseType分类后的授权使用情况
     */
    @Query(value = "select new com.ruijie.rcos.rcdc.license.module.def.dto.CbbLicenseClassifiedUsageDTO(t.licenseDuration, t.licenseType, " +
            "t.authMode, count(*)) from UserLicenseEntity t where t.licenseType != null  group by t.licenseDuration, t.licenseType, t.authMode")
    List<CbbLicenseClassifiedUsageDTO> getLicenseClassifiedUsage();

    /**
     * 根据授权持续类型与授权类型查询用户授权记录
     * @param licenseType 授权类型
     * @param licenseDuration 授权持续类型
     * @return 用户授权记录
     */
    List<UserLicenseEntity> findByLicenseTypeAndLicenseDuration(String licenseType, CbbLicenseDurationEnum licenseDuration);

    /**
     * 根据用户ID查询用户授权记录
     * @param userId 用户ID
     * @return 用户授权记录
     */
    Optional<UserLicenseEntity> findByUserId(UUID userId);
}
