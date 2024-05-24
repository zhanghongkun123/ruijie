package com.ruijie.rcos.rcdc.rco.module.impl.dto;


import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.DiskPoolUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserQueryHelper;

/**
 * Description: 分页查询DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/29
 * 
 * @author TD
 */
public class PageQueryPoolDTO {

    private UUID poolId;

    private IacConfigRelatedType relatedType = IacConfigRelatedType.USER;

    private CriteriaQuery<?> query;

    private CriteriaBuilder builder;

    private PageSearchRequest request;

    private Class subTable;

    private boolean isIn;

    private String poolIdColumnName;

    /**
     * 是否添加组查询
     */
    private boolean isAddGroup;

    /**
     * 是否添加NOT IN查询
     */
    private boolean isNotIn = Boolean.FALSE;

    public PageQueryPoolDTO(UUID poolId, String poolIdColumnName, PageSearchRequest request, boolean isIn) {
        Assert.notNull(poolId, "poolId can not be null");
        Assert.notNull(poolIdColumnName, "poolIdColumnName can not be null");
        Assert.notNull(request, "request can not be null");
        this.poolId = poolId;
        this.poolIdColumnName = poolIdColumnName;
        this.request = request;
        this.isIn = isIn;
        this.subTable = poolIdColumnName.equals(UserQueryHelper.DESKTOP_POOL_ID) ? DesktopPoolUserEntity.class : DiskPoolUserEntity.class;
    }

    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    public IacConfigRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(IacConfigRelatedType relatedType) {
        this.relatedType = relatedType;
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

    public String getPoolIdColumnName() {
        return poolIdColumnName;
    }

    public void setPoolIdColumnName(String poolIdColumnName) {
        this.poolIdColumnName = poolIdColumnName;
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
