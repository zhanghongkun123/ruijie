package com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;

/**
 * Description: 支持时间范围的pageSpec
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/4/11 3:35 下午
 *
 * @param <TerminalOpLogEntity> 数据库表Entity
 * @author zhouhuan
 */
public class TerminalOpLogPageSpec<TerminalOpLogEntity> extends PageQuerySpecification<TerminalOpLogEntity> {

    private Date startTime;

    private Date endTime;


    /**
     * @param searchKeyword
     * @param searchColumnList
     * @param matchEqualArr
     */
    public TerminalOpLogPageSpec(String searchKeyword, List<String> searchColumnList, MatchEqual[] matchEqualArr, Boolean isAnd) {
        super(searchKeyword, searchColumnList, matchEqualArr, isAnd);
    }

    @Override
    protected Predicate expandPredicate(Root<TerminalOpLogEntity> root, CriteriaBuilder cb) {
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
