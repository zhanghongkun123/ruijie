package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.common;

import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.entity.DesktopTempPermissionRelationEntity;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/24
 *
 * @author linke
 */
public class DesktopTempPermissionHelper {

    public static final String RELATED_ID = "relatedId";

    public static final String RELATED_TYPE = "relatedType";

    private static final String DESKTOP_TEMP_PERMISSION_ID = "desktopTempPermissionId";

    /**
     * 创建临时权限关联对象子查询
     *
     * @param desktopTempPermissionId 临时权限
     * @param type                    对象类型
     * @param query                   CriteriaQuery
     * @param cb                      CriteriaBuilder
     * @return Subquery<UUID>
     */
    public static Subquery<UUID> buildRelatedIdSubQuery(UUID desktopTempPermissionId, DesktopTempPermissionRelatedType type,
                                                        CriteriaQuery<?> query, CriteriaBuilder cb) {
        Assert.notNull(desktopTempPermissionId, "desktopTempPermissionId is null");
        Assert.notNull(type, "type is null");
        Assert.notNull(query, "query is null");
        Assert.notNull(cb, "cb is null");

        // subQuery: select relatedId from DesktopTempPermissionRelationEntity where desktopTempPermissionId = ?1 and relatedType = ?2
        // 子查询结果类型UUID
        Subquery<UUID> subQuery = query.subquery(UUID.class);
        // 子查询 查询DesktopPoolUserEntity
        Root<DesktopTempPermissionRelationEntity> subRoot = subQuery.from(DesktopTempPermissionRelationEntity.class);
        // 查询 select DesktopPoolUserEntity.relatedId
        subQuery.select(subRoot.get(RELATED_ID));

        // desktopTempPermissionId = ?1
        Predicate subIdPredicate = cb.equal(subRoot.get(DESKTOP_TEMP_PERMISSION_ID), desktopTempPermissionId);
        // relatedType = ?2
        Predicate subTypePredicate = cb.equal(subRoot.get(RELATED_TYPE), type);
        // where 条件组合
        subQuery.where(subIdPredicate, subTypePredicate);
        return subQuery;
    }
}
