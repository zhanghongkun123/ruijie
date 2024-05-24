package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public interface RcoSoftwareDAO extends SkyEngineJpaRepository<RcoSoftwareEntity, UUID> {

    /**
     * 通过软件分组ID查找软件
     * @param id 软件分组ID
     * @return 软件对象列表
     */
    List<RcoSoftwareEntity> findByGroupId(UUID id);

    /**
     * 通过软件分组ID列表查找软件
     * @param groupIds 软件分组ID
     * @return 软件对象列表
     */
    List<RcoSoftwareEntity> findByGroupIdIn(Iterable<UUID> groupIds);

    /**
     * 通过软件ID列表查找软件
     * @param ids 软件ID
     * @param parentIds 软件ID
     * @return 软件对象列表
     */
    List<RcoSoftwareEntity> findByIdInOrParentIdIn(Iterable<UUID> ids, Iterable<UUID> parentIds);


    /**
     *
     * @param id 软件id
     * @param sort 排序规则
     * @return 软件列表
     */
    List<RcoSoftwareEntity> findByGroupId(UUID id, Sort sort);

    /**
     *
     *
     * @param idArr 软件id数组
     * @param targetGroupId 目前软件组
     */
    @Modifying()
    @Query("update RcoSoftwareEntity o set o.groupId = :targetGroupId, version = version + 1 " +
            "where (o.id in :ids or o.parentId in :ids) and version = version")
    void updateSoftwareGroupId(@Param("ids")Iterable<UUID> idArr, @Param("targetGroupId") UUID targetGroupId);

    /**
     * 删除目录下的软件列表
     * @param parentId parentId
     */
    void deleteByParentId(UUID parentId);


    /**
     * 通过软件ID列表查找软件
     * @param ids 软件ID
     * @return 软件对象列表
     */
    List<RcoSoftwareEntity> findByIdIn(Iterable<UUID> ids);

    /**
     *
     * @param ids 软件id
     * @return 软件总数
     */
    Integer countByIdIn(Iterable<UUID> ids);

    /**
     * 通过软件名称查找软件
     * @param name 软件名称
     * @param topLevelFile topLevelFile
     * @return 软件对象
     */
    RcoSoftwareEntity findByNameAndTopLevelFile(String name, Boolean topLevelFile);

    /**
     * 查询MD5是否已存在
     * @param fileCustomMd5 文件特征码
     * @param topLevelFile topLevelFile
     * @return 软件实体
     */
    RcoSoftwareEntity findByFileCustomMd5AndTopLevelFile(String fileCustomMd5, Boolean topLevelFile);

    /**
     * 查询软件个数
     * @return 软件总数
     */
    long countByParentIdIsNull();
}
