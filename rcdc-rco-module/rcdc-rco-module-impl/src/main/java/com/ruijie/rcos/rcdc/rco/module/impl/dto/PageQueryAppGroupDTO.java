package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoRcaGroupMemberEntity;

/**
 * Description: 应用分组分页查询DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/12
 * 
 * @author zhengjingyong
 */
public class PageQueryAppGroupDTO {

    private UUID appGroupId;

    private RcaEnum.GroupMemberType memberType = RcaEnum.GroupMemberType.USER;

    private CriteriaQuery<?> query;

    private CriteriaBuilder builder;

    private PageSearchRequest request;

    private Class subTable;

    private boolean isIn;

    private String appGroupIdColumnName = "groupId";

    /**
     * 是否添加组查询
     */
    private boolean isAddGroup;

    /**
     * 是否添加NOT IN查询
     */
    private boolean isNotIn = Boolean.FALSE;

    public PageQueryAppGroupDTO(UUID appGroupId, PageSearchRequest request, boolean isIn) {
        Assert.notNull(appGroupId, "appGroupId can not be null");
        Assert.notNull(request, "request can not be null");
        this.appGroupId = appGroupId;
        this.request = request;
        this.isIn = isIn;
        this.subTable = RcoRcaGroupMemberEntity.class;
    }

    public UUID getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(UUID appGroupId) {
        this.appGroupId = appGroupId;
    }

    public RcaEnum.GroupMemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(RcaEnum.GroupMemberType memberType) {
        this.memberType = memberType;
    }

    public CriteriaQuery getQuery() {
        return query;
    }

    public void setQuery(CriteriaQuery query) {
        this.query = query;
    }

    public CriteriaBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(CriteriaBuilder builder) {
        this.builder = builder;
    }

    public PageSearchRequest getRequest() {
        return request;
    }

    public void setRequest(PageSearchRequest request) {
        this.request = request;
    }

    public boolean isIn() {
        return isIn;
    }

    public void setIn(boolean in) {
        isIn = in;
    }

    public Class getSubTable() {
        return subTable;
    }

    public void setSubTable(Class subTable) {
        this.subTable = subTable;
    }

    public String getAppGroupIdColumnName() {
        return appGroupIdColumnName;
    }

    public void setAppGroupIdColumnName(String appGroupIdColumnName) {
        this.appGroupIdColumnName = appGroupIdColumnName;
    }

    public boolean isAddGroup() {
        return isAddGroup;
    }

    public void setAddGroup(boolean addGroup) {
        isAddGroup = addGroup;
    }

    public boolean isNotIn() {
        return isNotIn;
    }

    public void setNotIn(boolean notIn) {
        isNotIn = notIn;
    }
}
