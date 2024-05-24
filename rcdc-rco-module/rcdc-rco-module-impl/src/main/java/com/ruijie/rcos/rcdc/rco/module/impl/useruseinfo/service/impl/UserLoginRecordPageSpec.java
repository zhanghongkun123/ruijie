package com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.impl;


import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.entity.UserLoginRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.entity.ViewUserLoginRecordEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 用户登录分页条件
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/2 17:10
 *
 * @author zjy
 */
public class UserLoginRecordPageSpec extends PageQuerySpecification<ViewUserLoginRecordEntity> {

    private static final long serialVersionUID = -991607592191239197L;

    private Date startTime;

    private Date endTime;


    /**
     * @param searchKeyword
     * @param searchColumnList
     * @param matchEqualArr
     */
    public UserLoginRecordPageSpec(String searchKeyword, List<String> searchColumnList, MatchEqual[] matchEqualArr) {
        super(searchKeyword, searchColumnList, matchEqualArr, null);
    }

    @Override
    protected Predicate expandPredicate(Root<ViewUserLoginRecordEntity> root, CriteriaBuilder cb) {
        List<Predicate> predicateList = new ArrayList<>();
        if (startTime != null) {
            predicateList.add(cb.greaterThanOrEqualTo(root.get("createTime"), startTime));
        }
        if (endTime != null) {
            predicateList.add(cb.lessThanOrEqualTo(root.get("createTime"), endTime));
        }

        if (predicateList.isEmpty()) {
            //没有条件返回空
            return null;
        } else {
            return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}