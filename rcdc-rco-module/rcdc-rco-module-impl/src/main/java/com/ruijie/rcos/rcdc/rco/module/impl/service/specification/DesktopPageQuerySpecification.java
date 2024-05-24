package com.ruijie.rcos.rcdc.rco.module.impl.service.specification;

import com.google.common.collect.Sets;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.common.DesktopTempPermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 云桌面分页查询Specification接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/08/02
 *
 * @author chenl
 */
public class DesktopPageQuerySpecification extends PageQuerySpecification<ViewUserDesktopEntity> {

    /**
     * 定时任务快照和备份的code
     */
    private final static Set<String> SCHEDULE_TYPE_CODE_SET = Sets.newHashSet(ScheduleTypeCodeConstants.CLOUD_DESK_CREATE_SNAPSHOT_TYPR_CODE,
            ScheduleTypeCodeConstants.CLOUD_DESK_CREATE_BACKUP_TYPR_CODE);

    private final static String PATTERN = "pattern";

    private final static String PERSON_SIZE = "personSize";

    private final static String CBB_DESKTOP_ID = "cbbDesktopId";

    private final static String IMAGE_USAGE = "imageUsage";

    private String scheduleTypeCode;

    private UUID desktopTempPermissionId;

    private ImageUsageTypeEnum imageUsage;

    public DesktopPageQuerySpecification(String searchKeyword, List<String> searchColumnList, MatchEqual[] matchEqualArr, Boolean isAnd) {
        super(searchKeyword, searchColumnList, matchEqualArr, isAnd);
    }

    @Override
    public Predicate toPredicate(Root<ViewUserDesktopEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Assert.notNull(root, "root is null");
        Assert.notNull(query, "query is null");
        Assert.notNull(cb, "cb is null");

        Predicate predicate = super.toPredicate(root, query, cb);
        Assert.notNull(predicate, "predicate is null");

        if (StringUtils.isNotEmpty(scheduleTypeCode) && SCHEDULE_TYPE_CODE_SET.contains(scheduleTypeCode)) {
            buildScheduleTaskDefaultExpressions(root, cb, predicate);
        }
        if (Objects.nonNull(desktopTempPermissionId)) {
            buildDesktopTempPermissionSubQueryExpressions(root, query, cb, predicate);
        }

        return predicate;
    }

    @Override
    protected Predicate buildLikePredicate(Root<ViewUserDesktopEntity> root, CriteriaBuilder cb) {
        if (StringUtils.isBlank(getSearchKeyword())) {
            // 没有传搜索关键字时返回null
            return null;
        }
        List<Predicate> predicateList = new ArrayList<>(getSearchColumnList().size());
        getSearchColumnList().forEach(item -> {
            StringBuilder sb = new StringBuilder();
            Expression<String> columnExpression = root.get(item).as(String.class);
            String searchKeyword = getSearchKeyword();
            if (Constants.DESKTOP_NAME.equals(item)) {
                searchKeyword = searchKeyword.toLowerCase();
                columnExpression = cb.lower(root.get(item).as(String.class));
            }
            sb.append("%").append(searchKeyword).append("%");
            predicateList.add(cb.like(columnExpression, sb.toString()));
        });
        Predicate[] predicateArr = new Predicate[predicateList.size()];
        return cb.or(predicateList.toArray(predicateArr));
    }

    private void buildScheduleTaskDefaultExpressions(Root<ViewUserDesktopEntity> root, CriteriaBuilder cb, Predicate rootPredicate) {
        Expression<String> patternExpression = root.get(PATTERN);
        Expression<Integer> personSizeExpression = root.get(PERSON_SIZE);
        // 添加默认过滤条件: where后 and (pattern != 'RECOVERABLE' or (pattern = 'RECOVERABLE' and person_size > 0))
        Predicate patternPredicate = cb.equal(patternExpression, CbbCloudDeskPattern.RECOVERABLE.name());
        Predicate personSizePredicate = cb.gt(personSizeExpression, 0);
        Predicate orPatternPersonSizePredicate = cb.and(patternPredicate, personSizePredicate);
        rootPredicate.getExpressions().add(cb.or(patternPredicate.not(), orPatternPersonSizePredicate));
    }

    private void buildDesktopTempPermissionSubQueryExpressions(Root<ViewUserDesktopEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                                               Predicate rootPredicate) {
        // subQuery: select relatedId from DesktopTempPermissionRelationEntity where desktopTempPermissionId = ?1 and relatedType = ?2
        Subquery<UUID> subQuery = DesktopTempPermissionHelper.buildRelatedIdSubQuery(desktopTempPermissionId,
                DesktopTempPermissionRelatedType.DESKTOP, query, cb);
        // 添加上面的子查询到主查询条件：cbbDesktopId in (subQuery)
        Expression<String> cbbDesktopIdExpression = root.get(CBB_DESKTOP_ID);
        Predicate subQueryPredicate = cbbDesktopIdExpression.in(subQuery);
        rootPredicate.getExpressions().add(subQueryPredicate);
    }

    public String getScheduleTypeCode() {
        return scheduleTypeCode;
    }

    public void setScheduleTypeCode(String scheduleTypeCode) {
        this.scheduleTypeCode = scheduleTypeCode;
    }

    public UUID getDesktopTempPermissionId() {
        return desktopTempPermissionId;
    }

    public void setDesktopTempPermissionId(UUID desktopTempPermissionId) {
        this.desktopTempPermissionId = desktopTempPermissionId;
    }

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }
}
