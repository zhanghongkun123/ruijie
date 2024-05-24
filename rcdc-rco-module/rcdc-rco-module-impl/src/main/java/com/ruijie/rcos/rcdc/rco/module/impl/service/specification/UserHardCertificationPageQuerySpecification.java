package com.ruijie.rcos.rcdc.rco.module.impl.service.specification;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.entity.RcoViewUserHardwareCertificationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 硬件特征码分页查询Specification接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/04/25
 *
 * @author yxq
 */
public class UserHardCertificationPageQuerySpecification extends PageQuerySpecification<RcoViewUserHardwareCertificationEntity> {

    public UserHardCertificationPageQuerySpecification(String searchKeyword, List<String> searchColumnList,
                                                       MatchEqual[] matchEqualArr, Boolean isAnd) {
        super(searchKeyword, searchColumnList, matchEqualArr, isAnd);
    }

    @Override
    protected Predicate buildLikePredicate(Root<RcoViewUserHardwareCertificationEntity> root, CriteriaBuilder cb) {
        if (StringUtils.isBlank(getSearchKeyword())) {
            // 没有传搜索关键字时返回null
            return null;
        }
        List<Predicate> predicateList = new ArrayList<>(getSearchColumnList().size());
        getSearchColumnList().forEach(item -> {
            StringBuilder sb = new StringBuilder();
            Expression<String> columnExpression = root.get(item).as(String.class);
            // mac地址不区分大小写
            if  (Constants.MAC_ADDR.equals(item)) {
                columnExpression = cb.lower(root.get(item).as(String.class));
                sb.append("%").append(getSearchKeyword().toLowerCase()).append("%");
            } else {
                sb.append("%").append(getSearchKeyword()).append("%");
            }

            predicateList.add(cb.like(columnExpression, sb.toString()));
        });
        Predicate[] predicateArr = new Predicate[predicateList.size()];
        return cb.or(predicateList.toArray(predicateArr));
    }
}
