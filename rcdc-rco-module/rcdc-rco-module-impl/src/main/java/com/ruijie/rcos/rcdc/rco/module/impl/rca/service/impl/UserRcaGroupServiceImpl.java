package com.ruijie.rcos.rcdc.rco.module.impl.rca.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserRcaGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserRcaGroupDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserRcaGroupEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rca.service.UserRcaGroupService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
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
import java.util.UUID;

/**
 * Description: 用户应用分组service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/08/11
 *
 * @author zhengjingyong
 */
@Service
public class UserRcaGroupServiceImpl extends AbstractPageQueryTemplate<UserRcaGroupEntity> implements UserRcaGroupService {

    private static final String MEMBER_ID = "memberId";

    private static final String MEMBER_TYPE = "memberType";

    @Autowired
    private UserRcaGroupDAO userRcaGroupDAO;

    @Override
    public DefaultPageResponse<UserRcaGroupDTO> pageQueryUserRcaGroup(IacUserDetailDTO cbbUserDetailDTO, PageSearchRequest request) {
        Assert.notNull(cbbUserDetailDTO, "cbbUserDetailDTO is null");
        Assert.notNull(request, "request is null");

        DefaultDataSort defaultDataSort = getDefaultDataSort();
        Assert.notNull(defaultDataSort, "默认排序信息不能为null");
        String defaultSortField = defaultDataSort.getSortField();
        Assert.hasText(defaultSortField, "默认排序信息不能为空");
        // 构建Pageable
        Pageable pageable = getPageRequest(request, defaultSortField, defaultDataSort.getDirection());

        Page<UserRcaGroupEntity> rcaGroupPage = getPage(cbbUserDetailDTO, null, request, pageable);

        DefaultPageResponse<UserRcaGroupDTO> pageResponse = new DefaultPageResponse<>();
        if (CollectionUtils.isEmpty(rcaGroupPage.getContent())) {
            pageResponse.setTotal(rcaGroupPage.getTotalElements());
            pageResponse.setItemArr(new UserRcaGroupDTO[0]);
            return pageResponse;
        }
        pageResponse.setTotal(rcaGroupPage.getTotalElements());
        pageResponse.setItemArr(rcaGroupPage.getContent().stream().map(this::convert2UserRcaGroupDTO).toArray(UserRcaGroupDTO[]::new));
        return pageResponse;
    }

    @Override
    public DefaultPageResponse<UserRcaGroupDTO> pageQueryAdGroupRcaGroup(UUID adGroupId, PageSearchRequest request) {
        Assert.notNull(adGroupId, "adGroupId is null");
        Assert.notNull(request, "request is null");

        DefaultDataSort defaultDataSort = getDefaultDataSort();
        Assert.notNull(defaultDataSort, "默认排序信息不能为null");
        String defaultSortField = defaultDataSort.getSortField();
        Assert.hasText(defaultSortField, "默认排序信息不能为空");
        // 构建Pageable
        Pageable pageable = getPageRequest(request, defaultSortField, defaultDataSort.getDirection());

        Page<UserRcaGroupEntity> rcaGroupPage = getPage(null, adGroupId, request, pageable);

        DefaultPageResponse<UserRcaGroupDTO> pageResponse = new DefaultPageResponse<>();
        if (CollectionUtils.isEmpty(rcaGroupPage.getContent())) {
            pageResponse.setTotal(rcaGroupPage.getTotalElements());
            pageResponse.setItemArr(new UserRcaGroupDTO[0]);
            return pageResponse;
        }
        pageResponse.setTotal(rcaGroupPage.getTotalElements());
        pageResponse.setItemArr(rcaGroupPage.getContent().stream().map(this::convert2UserRcaGroupDTO).toArray(UserRcaGroupDTO[]::new));
        return pageResponse;
    }

    private Page<UserRcaGroupEntity> getPage(IacUserDetailDTO cbbUserDetailDTO, UUID adGroupId, PageSearchRequest request, Pageable pageable) {
        return userRcaGroupDAO.findAll(new PageQuerySpecification<UserRcaGroupEntity>(
                request.getSearchKeyword(), getSearchColumn(), request.getMatchEqualArr(), request.getIsAnd()) {
            @Override
            public Predicate toPredicate(Root<UserRcaGroupEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Assert.notNull(root, "root is null");
                Assert.notNull(query, "query is null");
                Assert.notNull(cb, "cb is null");

                Predicate predicate = super.toPredicate(root, query, cb);
                Assert.notNull(predicate, "predicate is null");

                if (cbbUserDetailDTO != null) {
                    // 添加用户组和用户条件
                    Predicate userPredicate1 = cb.equal(root.get(MEMBER_ID), cbbUserDetailDTO.getId());
                    Predicate userPredicate2 = cb.equal(root.get(MEMBER_TYPE), RcaEnum.GroupMemberType.USER);
                    Predicate userPredicate = cb.and(userPredicate1, userPredicate2);

                    Predicate groupPredicate1 = cb.equal(root.get(MEMBER_ID), cbbUserDetailDTO.getGroupId());
                    Predicate groupPredicate2 = cb.equal(root.get(MEMBER_TYPE), RcaEnum.GroupMemberType.USER_GROUP);
                    Predicate groupPredicate = cb.and(groupPredicate1, groupPredicate2);

                    predicate.getExpressions().add(cb.or(userPredicate, groupPredicate));
                }

                if (adGroupId != null) {
                    // 添加AD域组条件
                    Predicate groupPredicate1 = cb.equal(root.get(MEMBER_ID), adGroupId);
                    Predicate groupPredicate2 = cb.equal(root.get(MEMBER_TYPE), RcaEnum.GroupMemberType.AD_SAFETY_GROUP);
                    Predicate groupPredicate = cb.and(groupPredicate1, groupPredicate2);

                    predicate.getExpressions().add(groupPredicate);
                }

                return predicate;
            }
        }, pageable);
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("name");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {

    }

    @Override
    protected Page<UserRcaGroupEntity> find(Specification<UserRcaGroupEntity> specification, Pageable pageable) {
        if (specification == null) {
            return userRcaGroupDAO.findAll(pageable);
        }
        return userRcaGroupDAO.findAll(specification, pageable);
    }

    private UserRcaGroupDTO convert2UserRcaGroupDTO(UserRcaGroupEntity entity) {
        UserRcaGroupDTO userRcaGroupDTO = new UserRcaGroupDTO();
        BeanUtils.copyProperties(entity, userRcaGroupDTO);
        return userRcaGroupDTO;
    }
}
