package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserSyncDataEntity;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 11:54
 *
 * @author coderLee23
 */
public interface ViewUserSyncDataService {

    /**
     * 分页获取用户同步数据
     *
     * @param pageable 分页
     * @param userTypeList 用户类型列表
     * @return Page<ViewUserSyncDataEntity>
     */
    Page<ViewUserSyncDataEntity> pageUserSyncData(List<IacUserTypeEnum> userTypeList, Pageable pageable);


    /**
     * 根据id 获取用户数据
     *
     * @param id 唯一表示
     * @return ViewUserSyncDataEntity
     */
    Optional<ViewUserSyncDataEntity> findById(UUID id);

}
