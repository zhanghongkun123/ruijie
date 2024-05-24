package com.ruijie.rcos.rcdc.rco.module.impl.service.specification;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.common.DesktopTempPermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserQueryHelper;

/**
 * Description: 用户分页查询Specification
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/24
 *
 * @author linke
 */
public class UserPageQuerySpecification extends PageQuerySpecification<RcoViewUserEntity> {

    private UUID desktopPoolId;

    private PageQueryPoolDTO pageQueryPoolDTO;

    private UUID desktopTempPermissionId;

    public UserPageQuerySpecification(String searchKeyword, List<String> searchColumnList, MatchEqual[] matchEqualArr, Boolean isAnd) {
        super(searchKeyword, searchColumnList, matchEqualArr, isAnd);
    }

    @Override
    public Predicate toPredicate(Root<RcoViewUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Assert.notNull(root, "root is null");
        Assert.notNull(query, "query is null");
        Assert.notNull(cb, "cb is null");

        Predicate predicate = super.toPredicate(root, query, cb);
        Assert.notNull(predicate, "predicate is null");

        if (Objects.nonNull(pageQueryPoolDTO)) {
            pageQueryPoolDTO.setQuery(query);
            pageQueryPoolDTO.setBuilder(cb);
            if (Objects.nonNull(desktopPoolId)) {
                // 关联桌面池的用户查询
                return buildDesktopPoolPredicate(pageQueryPoolDTO, root, cb, predicate);
            }
            // 关联磁盘池的用户查询
            return buildPoolPredicate(pageQueryPoolDTO, root, cb, predicate);
        }

        if (Objects.nonNull(desktopTempPermissionId)) {
            // 关联临时权限的用户查询
            return buildDesktopTempPermissionPredicate(root, query, cb, predicate);
        }

        return predicate;
    }

    private Predicate buildDesktopPoolPredicate(PageQueryPoolDTO pageQueryPoolDTO, Root<RcoViewUserEntity> root, CriteriaBuilder cb,
                                                Predicate predicate) {
        // 用户id条件
        // 构建where里的条件sql "select related_id from t_rco_desktop_pool_user where desktop_pool_id = ?1 and related_type = 'USER'"
        Predicate subQueryPredicate = buildUserSubQueryPredicate(pageQueryPoolDTO, root);

        // 用户组id条件
        // 构建where里的条件sql"select related_id from t_rco_desktop_pool_user where desktop_pool_id = ?1 and related_type = 'USERGROUP'"
        Predicate groupSubQueryPredicate = buildGroupSubQueryPredicate(pageQueryPoolDTO, root);

        if (pageQueryPoolDTO.isIn()) {
            // 如果是查询已分配到池中部分的用户，需要将添加 and (用户in条件 or 用户组in条件）
            predicate.getExpressions().add(cb.or(subQueryPredicate, groupSubQueryPredicate));
        } else {
            // 如果是查询未分配到池中部分的用户，需要将添加 and (用户not in条件 and 用户组not in条件）
            predicate.getExpressions().add(cb.and(subQueryPredicate, groupSubQueryPredicate));
        }
        return predicate;
    }

    private Predicate buildPoolPredicate(PageQueryPoolDTO pageQueryPoolDTO, Root<RcoViewUserEntity> root, CriteriaBuilder cb,
                                                Predicate predicate) {
        // 用户id条件
        // 构建where里的条件sql "select related_id from t_rco_disk_pool_user where disk_pool_id = ?1 and related_type = 'USER'"
        Predicate subQueryPredicate = buildUserSubQueryPredicate(pageQueryPoolDTO, root);

        // 用户组id条件
        // 构建where里的条件sql"select related_id from t_rco_disk_pool_user where disk_pool_id = ?1 and related_type = 'USERGROUP'"
        Predicate groupSubQueryPredicate = buildGroupSubQueryPredicate(pageQueryPoolDTO, root);

        // 构建已分配到其他池的用户条件查询语句
        // 构建where里的条件sql"select related_id from t_rco_disk_pool_user where disk_pool_id != ?1 and related_type = 'USER'"
        Expression<String> userIdNotInExpression = root.get(UserQueryHelper.ID);
        Subquery<UUID> subQueryNotEqual = UserQueryHelper.buildPoolUserSubQueryNotEqualPoolId(pageQueryPoolDTO, IacConfigRelatedType.USER);
        Predicate userIdNotInPredicate = pageQueryPoolDTO.isIn() ? userIdNotInExpression.in(subQueryNotEqual).not() :
                userIdNotInExpression.in(subQueryNotEqual);

        if (pageQueryPoolDTO.isIn()) {
            // 如果是查询已分配到池中部分的用户，需要将添加 and (用户in条件 or 用户组in条件）
            predicate.getExpressions().add(cb.or(subQueryPredicate, groupSubQueryPredicate));
            // 磁盘池过滤掉已经添加到其他池的用户
            predicate.getExpressions().add(userIdNotInPredicate);
        } else {
            // 如果是查询未分配到池中部分的用户，需要将添加 and (用户not in条件 and 用户组not in条件）
            Predicate userAndGroupPredicate = cb.and(subQueryPredicate, groupSubQueryPredicate);
            // 查询未分配到池中部分的用户，要添加 or条件（那些已经分配到其他池的用户）
            predicate.getExpressions().add(cb.or(userAndGroupPredicate, userIdNotInPredicate));
        }
        return predicate;
    }

    private Predicate buildUserSubQueryPredicate(PageQueryPoolDTO pageQuery, Root<RcoViewUserEntity> root) {
        Subquery<UUID> subQuery = UserQueryHelper.buildPoolUserSubQuery(pageQuery, IacConfigRelatedType.USER);
        Expression<String> userIdExpression = root.get(UserQueryHelper.ID);
        return pageQuery.isIn() ? userIdExpression.in(subQuery) : userIdExpression.in(subQuery).not();
    }

    private Predicate buildGroupSubQueryPredicate(PageQueryPoolDTO pageQuery, Root<RcoViewUserEntity> root) {
        Subquery<UUID> groupIdSubQuery = UserQueryHelper.buildPoolUserSubQuery(pageQuery, IacConfigRelatedType.USERGROUP);
        Expression<String> groupIdExpression = root.get(UserQueryHelper.GROUP_ID);
        return pageQuery.isIn() ? groupIdExpression.in(groupIdSubQuery) : groupIdExpression.in(groupIdSubQuery).not();
    }

    private Predicate buildDesktopTempPermissionPredicate(Root<RcoViewUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                                          Predicate predicate) {
        Subquery<UUID> subQuery = DesktopTempPermissionHelper.buildRelatedIdSubQuery(desktopTempPermissionId,
                DesktopTempPermissionRelatedType.USER, query, cb);

        // 添加RcoViewUserEntity查询条件 id in (subQuery)
        Expression<String> userIdExpression = root.get(UserQueryHelper.ID);
        Predicate subQueryPredicate = userIdExpression.in(subQuery);
        predicate.getExpressions().add(subQueryPredicate);

        return predicate;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public PageQueryPoolDTO getPageQueryPoolDTO() {
        return pageQueryPoolDTO;
    }

    public void setPageQueryPoolDTO(PageQueryPoolDTO pageQueryPoolDTO) {
        this.pageQueryPoolDTO = pageQueryPoolDTO;
    }

    public UUID getDesktopTempPermissionId() {
        return desktopTempPermissionId;
    }

    public void setDesktopTempPermissionId(UUID desktopTempPermissionId) {
        this.desktopTempPermissionId = desktopTempPermissionId;
    }
}
