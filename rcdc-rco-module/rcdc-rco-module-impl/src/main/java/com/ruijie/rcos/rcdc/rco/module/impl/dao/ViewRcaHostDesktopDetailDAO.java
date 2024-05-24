package com.ruijie.rcos.rcdc.rco.module.impl.dao;

import com.ruijie.rcos.rcdc.rco.module.common.query.RcdcJpaRepository;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaHostDesktopEntity;
import com.ruijie.rcos.sk.pagekit.api.PageQueryDAO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * Description: 派生云主机查询云桌面明细视图
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月11日
 *
 * @author liuwc
 */
public interface ViewRcaHostDesktopDetailDAO extends RcdcJpaRepository<ViewRcaHostDesktopEntity, UUID>, PageQueryDAO<ViewRcaHostDesktopEntity> {

    /**
     * 根据主机id列表查询主机桌面详情
     *
     * @param hostIdList 主机id列表
     * @return 主机桌面详情列表
     */
    List<ViewRcaHostDesktopEntity> findAllByRcaHostIdIn(List<UUID> hostIdList);

    /**
     * 根据池Id查询所有数据列表
     *
     * @param poolId 池id
     * @return 映射列表
     */
    List<ViewRcaHostDesktopEntity> findAllByRcaPoolId(UUID poolId);


    /**
     * 根据主机id列表查询主机桌面详情
     *
     * @param hostId 主机id
     * @return 主机桌面详情列表
     */
    ViewRcaHostDesktopEntity findByRcaHostId(UUID hostId);

    /**
     * 计算配置冲突的桌面数量
     *
     * @param poolId poolId
     * @param imageTemplateId imageTemplateId
     * @return 和池镜像不一致的桌面数量
     */
    @Query("select count(id) from ViewRcaHostDesktopEntity where rcaPoolId = ?1 and imageTemplateId != ?2")
    int countConflictRcaDeskNum(UUID poolId, UUID imageTemplateId);



}
