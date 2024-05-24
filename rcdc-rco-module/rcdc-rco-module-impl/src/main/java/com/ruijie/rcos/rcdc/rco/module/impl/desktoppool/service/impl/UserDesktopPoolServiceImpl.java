package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.UserDesktopPoolDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.UserDesktopPoolEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.UserDesktopPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/11
 *
 * @author linke
 */
@Service
public class UserDesktopPoolServiceImpl extends AbstractPageQueryTemplate<UserDesktopPoolEntity> implements UserDesktopPoolService {

    private static final String RELATED_ID = "relatedId";

    private static final String RELATED_TYPE = "relatedType";

    private static final String PLATFORM_NAME = "platformName";

    @Autowired
    private UserDesktopPoolDAO userDesktopPoolDAO;

    @Override
    public DefaultPageResponse<DesktopPoolDTO> pageQueryUserDesktopPool(IacUserDetailDTO cbbUserDetailDTO, PageSearchRequest request) {
        Assert.notNull(cbbUserDetailDTO, "cbbUserDetailDTO is null");
        Assert.notNull(request, "request is null");

        DefaultDataSort defaultDataSort = getDefaultDataSort();
        Assert.notNull(defaultDataSort, "默认排序信息不能为null");
        String defaultSortField = defaultDataSort.getSortField();
        Assert.hasText(defaultSortField, "默认排序信息不能为空");
        // 构建Pageable
        Pageable pageable = getPageRequest(request, defaultSortField, defaultDataSort.getDirection());

        Page<UserDesktopPoolEntity> desktopPoolPage = userDesktopPoolDAO.findAll(new PageQuerySpecification<UserDesktopPoolEntity>(
                request.getSearchKeyword(), getSearchColumn(), request.getMatchEqualArr(), request.getIsAnd()) {
            @Override
            public Predicate toPredicate(Root<UserDesktopPoolEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Assert.notNull(root, "root is null");
                Assert.notNull(query, "query is null");
                Assert.notNull(cb, "cb is null");

                Predicate predicate = super.toPredicate(root, query, cb);
                Assert.notNull(predicate, "predicate is null");

                // 添加用户组和用户条件
                Predicate userPredicate1 = cb.equal(root.get(RELATED_ID), cbbUserDetailDTO.getId());
                Predicate userPredicate2 = cb.equal(root.get(RELATED_TYPE), IacConfigRelatedType.USER);
                Predicate userPredicate = cb.and(userPredicate1, userPredicate2);

                Predicate groupPredicate1 = cb.equal(root.get(RELATED_ID), cbbUserDetailDTO.getGroupId());
                Predicate groupPredicate2 = cb.equal(root.get(RELATED_TYPE), IacConfigRelatedType.USERGROUP);
                Predicate groupPredicate = cb.and(groupPredicate1, groupPredicate2);

                predicate.getExpressions().add(cb.or(userPredicate, groupPredicate));
                return predicate;
            }
        }, pageable);

        DefaultPageResponse<DesktopPoolDTO> pageResponse = new DefaultPageResponse<>();
        if (CollectionUtils.isEmpty(desktopPoolPage.getContent())) {
            pageResponse.setTotal(desktopPoolPage.getTotalElements());
            pageResponse.setItemArr(new DesktopPoolDTO[0]);
            return pageResponse;
        }
        pageResponse.setTotal(desktopPoolPage.getTotalElements());
        pageResponse.setItemArr(desktopPoolPage.getContent().stream().map(this::convert2DesktopPoolDTO).toArray(DesktopPoolDTO[]::new));
        return pageResponse;
    }

    @Override
    public DefaultPageResponse<DesktopPoolDTO> pageQueryAdGroupDesktopPool(UUID adGroupId, PageSearchRequest request) {
        Assert.notNull(adGroupId, "adGroupId is null");
        Assert.notNull(request, "request is null");

        DefaultDataSort defaultDataSort = getDefaultDataSort();
        Assert.notNull(defaultDataSort, "默认排序信息不能为null");
        String defaultSortField = defaultDataSort.getSortField();
        Assert.hasText(defaultSortField, "默认排序信息不能为空");
        // 构建Pageable
        Pageable pageable = getPageRequest(request, defaultSortField, defaultDataSort.getDirection());

        Page<UserDesktopPoolEntity> desktopPoolPage = userDesktopPoolDAO.findAll(new PageQuerySpecification<UserDesktopPoolEntity>(
                request.getSearchKeyword(), getSearchColumn(), request.getMatchEqualArr(), request.getIsAnd()) {
            @Override
            public Predicate toPredicate(Root<UserDesktopPoolEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Assert.notNull(root, "root is null");
                Assert.notNull(query, "query is null");
                Assert.notNull(cb, "cb is null");

                Predicate predicate = super.toPredicate(root, query, cb);
                Assert.notNull(predicate, "predicate is null");

                // 添加AD域组条件
                Predicate groupPredicate1 = cb.equal(root.get(RELATED_ID), adGroupId);
                Predicate groupPredicate2 = cb.equal(root.get(RELATED_TYPE), IacConfigRelatedType.AD_GROUP);
                Predicate groupPredicate = cb.and(groupPredicate1, groupPredicate2);

                predicate.getExpressions().add(groupPredicate);
                return predicate;
            }
        }, pageable);

        DefaultPageResponse<DesktopPoolDTO> pageResponse = new DefaultPageResponse<>();
        if (CollectionUtils.isEmpty(desktopPoolPage.getContent())) {
            pageResponse.setTotal(desktopPoolPage.getTotalElements());
            pageResponse.setItemArr(new DesktopPoolDTO[0]);
            return pageResponse;
        }
        pageResponse.setTotal(desktopPoolPage.getTotalElements());
        pageResponse.setItemArr(desktopPoolPage.getContent().stream().map(this::convert2DesktopPoolDTO).toArray(DesktopPoolDTO[]::new));
        return pageResponse;
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("name", PLATFORM_NAME);
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {

    }

    @Override
    protected Page<UserDesktopPoolEntity> find(Specification<UserDesktopPoolEntity> specification, Pageable pageable) {
        if (specification == null) {
            return userDesktopPoolDAO.findAll(pageable);
        }
        return userDesktopPoolDAO.findAll(specification, pageable);
    }

    private DesktopPoolDTO convert2DesktopPoolDTO(UserDesktopPoolEntity entity) {
        DesktopPoolDTO desktopPoolDTO = new DesktopPoolDTO();
        BeanUtils.copyProperties(entity, desktopPoolDTO);
        if (Objects.nonNull(entity.getMemory())) {
            desktopPoolDTO.setMemory(CapacityUnitUtils.mb2Gb(entity.getMemory()));
        }
        desktopPoolDTO.setPoolType(entity.getPoolType());
        desktopPoolDTO.setPersonDisk(entity.getPersonSize());
        desktopPoolDTO.setSystemDisk(entity.getSystemSize());
        return desktopPoolDTO;
    }
}
