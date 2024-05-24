package com.ruijie.rcos.rcdc.rco.module.impl.service.specification;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.criteria.*;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryAppGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserQueryHelper;

/**
 * Description: 应用分组用户分页查询Specification
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/24
 *
 * @author zhengjingyong
 */
public class RcaAppGroupUserPageQuerySpecification extends PageQuerySpecification<RcoViewUserEntity> {

    private UUID appGroupId;

    private PageQueryAppGroupDTO pageQueryAppGroupDTO;

    public RcaAppGroupUserPageQuerySpecification(String searchKeyword, List<String> searchColumnList, MatchEqual[] matchEqualArr, Boolean isAnd) {
        super(searchKeyword, searchColumnList, matchEqualArr, isAnd);
    }

    @Override
    public Predicate toPredicate(Root<RcoViewUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Assert.notNull(root, "root is null");
        Assert.notNull(query, "query is null");
        Assert.notNull(cb, "cb is null");

        Predicate predicate = super.toPredicate(root, query, cb);
        Assert.notNull(predicate, "predicate is null");

        if (Objects.nonNull(pageQueryAppGroupDTO)) {
            pageQueryAppGroupDTO.setQuery(query);
            pageQueryAppGroupDTO.setBuilder(cb);
            return buildAppGroupPredicate(pageQueryAppGroupDTO, root, cb, predicate);
        }

        return predicate;
    }

    private Predicate buildAppGroupPredicate(PageQueryAppGroupDTO queryAppGroupDTO, Root<RcoViewUserEntity> root, CriteriaBuilder cb,
                                             Predicate predicate) {
        // 用户id条件
        // 构建where里的条件sql "select member_id from t_cbb_rca_group_member where group_id = ?1 and member_type = 'USER'"
        Predicate subQueryPredicate = buildUserSubQueryPredicate(queryAppGroupDTO, root);

        // 用户组id条件
        // 构建where里的条件sql"select member_id from t_cbb_rca_group_member where group_id = ?1 and member_type = 'USER_GROUP'"
        Predicate groupSubQueryPredicate = buildGroupSubQueryPredicate(queryAppGroupDTO, root);

        if (queryAppGroupDTO.isIn()) {
            // 如果是查询已分配到应用分组中部分的用户，需要将添加 and (用户in条件 or 用户组in条件）
            predicate.getExpressions().add(cb.or(subQueryPredicate, groupSubQueryPredicate));
        } else {
            // 如果是查询未分配到应用分组中部分的用户，需要将添加 and (用户not in条件 and 用户组not in条件）
            predicate.getExpressions().add(cb.and(subQueryPredicate, groupSubQueryPredicate));
        }
        return predicate;
    }

    private Predicate buildUserSubQueryPredicate(PageQueryAppGroupDTO pageQuery, Root<RcoViewUserEntity> root) {
        Subquery<UUID> subQuery = UserQueryHelper.buildAppGroupUserSubQuery(pageQuery, RcaEnum.GroupMemberType.USER);
        Expression<String> userIdExpression = root.get(UserQueryHelper.ID);
        return pageQuery.isIn() ? userIdExpression.in(subQuery) : userIdExpression.in(subQuery).not();
    }

    private Predicate buildGroupSubQueryPredicate(PageQueryAppGroupDTO pageQuery, Root<RcoViewUserEntity> root) {
        Subquery<UUID> groupIdSubQuery = UserQueryHelper.buildAppGroupUserSubQuery(pageQuery, RcaEnum.GroupMemberType.USER_GROUP);
        Expression<String> groupIdExpression = root.get(UserQueryHelper.GROUP_ID);
        return pageQuery.isIn() ? groupIdExpression.in(groupIdSubQuery) : groupIdExpression.in(groupIdSubQuery).not();
    }

    public UUID getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(UUID appGroupId) {
        this.appGroupId = appGroupId;
    }

    public PageQueryAppGroupDTO getPageQueryAppGroupDTO() {
        return pageQueryAppGroupDTO;
    }

    public void setPageQueryAppGroupDTO(PageQueryAppGroupDTO queryAppGroupDTO) {
        this.pageQueryAppGroupDTO = queryAppGroupDTO;
    }
}
