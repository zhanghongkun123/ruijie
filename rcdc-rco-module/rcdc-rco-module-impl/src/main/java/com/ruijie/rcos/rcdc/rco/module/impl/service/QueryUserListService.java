package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryAppGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.specification.RcaAppGroupUserPageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.service.specification.UserPageQuerySpecification;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;


/**
 * Description: 查询用户消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/10
 *
 * @author Jarman
 */
@Service("rcoQueryUserListService")
public class QueryUserListService extends AbstractPageQueryTemplate<RcoViewUserEntity> {

    private static final String ID = "id";

    @Autowired
    private RcoViewUserDAO rcoViewUserDAO;

    /**
     * 根据桌面池ID分页查询在桌面池中或不在池中的用户列表
     *
     * @param desktopPoolId 桌面池ID
     * @param request request
     * @param isInPool isInPool
     * @return Page<RcoViewUserEntity>
     */
    public Page<RcoViewUserEntity> pageUserInOrNotInDesktopPool(UUID desktopPoolId, PageSearchRequest request, Boolean isInPool) {
        Assert.notNull(desktopPoolId, "desktopPoolId is null");
        Assert.notNull(request, "request is null");
        Assert.notNull(isInPool, "isInPool is null");

        Pageable pageable = buildPageable(request);

        PageQueryPoolDTO pageQuery = new PageQueryPoolDTO(desktopPoolId, UserQueryHelper.DESKTOP_POOL_ID, request, isInPool);
        UserPageQuerySpecification specification = new UserPageQuerySpecification(request.getSearchKeyword(),
                getSearchColumn(), request.getMatchEqualArr(), request.getIsAnd());
        specification.setDesktopPoolId(desktopPoolId);
        specification.setPageQueryPoolDTO(pageQuery);

        return rcoViewUserDAO.findAll(specification, pageable);
    }

    /**
     * 根据应用分组ID分页查询在中或不在分组中的用户列表
     *
     * @param appGroupId 应用分组id
     * @param request request
     * @param isInGroup 是否在组内
     * @return Page<RcoViewUserEntity>
     */
    public Page<RcoViewUserEntity> pageUserInOrNotInAppGroup(UUID appGroupId, PageSearchRequest request, Boolean isInGroup) {
        Assert.notNull(appGroupId, "appGroupId is null");
        Assert.notNull(request, "request is null");
        Assert.notNull(isInGroup, "isInPool is null");

        Pageable pageable = buildPageable(request);

        PageQueryAppGroupDTO pageQuery = new PageQueryAppGroupDTO(appGroupId, request, isInGroup);
        RcaAppGroupUserPageQuerySpecification specification = new RcaAppGroupUserPageQuerySpecification(request.getSearchKeyword(),
                getSearchColumn(), request.getMatchEqualArr(), request.getIsAnd());
        specification.setAppGroupId(appGroupId);
        specification.setPageQueryAppGroupDTO(pageQuery);

        return rcoViewUserDAO.findAll(specification, pageable);
    }

    /**
     * 根据池ID分页查询在池中或不在池中的用户列表
     *
     * @param pageQuery 查询条件
     * @return Page<RcoViewUserEntity>
     */
    public Page<RcoViewUserEntity> pageUserInOrNotInPool(PageQueryPoolDTO pageQuery) {
        Assert.notNull(pageQuery, "pageQuery is null");
        Assert.notNull(pageQuery.getRequest(), "request is null");
        PageSearchRequest request = pageQuery.getRequest();

        Pageable pageable = buildPageable(request);

        UserPageQuerySpecification specification = new UserPageQuerySpecification(request.getSearchKeyword(),
                getSearchColumn(), request.getMatchEqualArr(), request.getIsAnd());
        specification.setPageQueryPoolDTO(pageQuery);

        return rcoViewUserDAO.findAll(specification, pageable);
    }

    @Override
    protected Pageable getPageRequest(PageSearchRequest request, String defaultSortField, Sort.Direction defaultDirection) {
        Assert.notNull(defaultSortField, "请指定默认排序字段");
        Assert.notNull(defaultDirection, "请指定默认排序方式");
        int page = request.getPage();
        int limit = request.getLimit();
        List<Sort.Order> orderList = new ArrayList<>();
        // 传入没有指定排序字段，使用id和默认字段进行排序
        if (ArrayUtils.isEmpty(request.getSortArr())) {
            orderList.add(new Sort.Order(defaultDirection, defaultSortField));
            // 加入ID作为排序条件，防止分页数据重复
            return PageRequest.of(page, limit, Sort.by(orderList));
        }
        // 有指定排序字段和排方式，需要加上id作为排序条件防止分页重复
        Arrays.stream(Objects.requireNonNull(request.getSortArr())).forEach(sortVO -> {
            Sort.Direction directionEnum = Sort.Direction.fromString(sortVO.getDirection().name());
            Sort.Order order = new Sort.Order(directionEnum, sortVO.getSortField());
            orderList.add(order);
        });
        Sort sort = Sort.by(orderList);
        return PageRequest.of(page, limit, sort);
    }

    /**
     * 根据桌面临时权限ID分页查询关联的用户列表
     *
     * @param desktopTempPermissionId 临时权限ID
     * @param request                 request
     * @return Page<RcoViewUserEntity>
     */
    public Page<RcoViewUserEntity> pageUserBindDesktopTempPermission(UUID desktopTempPermissionId, PageSearchRequest request) {
        Assert.notNull(desktopTempPermissionId, "desktopTempPermissionId is null");
        Assert.notNull(request, "request is null");
        Pageable pageable = buildPageable(request);
        UserPageQuerySpecification specification = new UserPageQuerySpecification(request.getSearchKeyword(),
                getSearchColumn(), request.getMatchEqualArr(), request.getIsAnd());
        specification.setDesktopTempPermissionId(desktopTempPermissionId);

        return rcoViewUserDAO.findAll(specification, pageable);
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("userName", "realName", "userDescription");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Direction.DESC);
    }

    @Override
    protected Page<RcoViewUserEntity> find(Specification<RcoViewUserEntity> specification, Pageable pageable) {
        if (specification == null) {
            return rcoViewUserDAO.findAll(pageable);
        }
        return rcoViewUserDAO.findAll(specification, pageable);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //父类实现了
    }

    /**
     * 根据名称查询
     *
     * @param userName userName
     * @return RcoViewUserEntity
     */
    public RcoViewUserEntity getByUserName(String userName) {
        Assert.hasText(userName, "userName must not be null");
        return rcoViewUserDAO.findByUserName(userName);
    }
}
