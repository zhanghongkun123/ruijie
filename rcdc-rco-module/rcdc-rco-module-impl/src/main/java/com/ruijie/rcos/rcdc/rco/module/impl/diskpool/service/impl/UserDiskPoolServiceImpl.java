package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.service.impl;

import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.AttachDiskMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.UserDiskPoolDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.UserDiskPoolEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.service.UserDiskPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 用户与磁盘池关系服务类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/15
 *
 * @author TD
 */
@Service
public class UserDiskPoolServiceImpl  extends AbstractPageQueryTemplate<UserDiskPoolEntity> implements UserDiskPoolService {

    private static final String RELATED_ID = "relatedId";

    private static final String RELATED_TYPE = "relatedType";

    @Autowired
    private UserDiskPoolDAO userDiskPoolDAO;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private UserService userService;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("name", "platformName");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        // 暂不实现
    }

    @Override
    protected Page<UserDiskPoolEntity> find(Specification<UserDiskPoolEntity> specification, Pageable pageable) {
        if (specification == null) {
            return userDiskPoolDAO.findAll(pageable);
        }
        return userDiskPoolDAO.findAll(specification, pageable);
    }

    @Override
    public DefaultPageResponse<UserDiskPoolEntity> pageQueryUserDiskPool(IacUserDetailDTO cbbUserDetailDTO, PageSearchRequest request,
                                                                         Boolean isAddGroup) {
        Assert.notNull(cbbUserDetailDTO, "pageDiskPoolByUserId cbbUserDetailDTO can not be null");
        Assert.notNull(request, "pageDiskPoolByUserId request can not be null");
        Assert.notNull(isAddGroup, "pageDiskPoolByUserId isAddGroup can not be null");
        DefaultDataSort defaultDataSort = getDefaultDataSort();
        Assert.notNull(defaultDataSort, "默认排序信息不能为null");
        String defaultSortField = defaultDataSort.getSortField();
        Assert.hasText(defaultSortField, "默认排序信息不能为空");
        // 构建Pageable
        Pageable pageable = getPageRequest(request, defaultSortField, defaultDataSort.getDirection());
        Page<UserDiskPoolEntity> diskPoolPage = userDiskPoolDAO.findAll(new PageQuerySpecification<UserDiskPoolEntity>(request.getSearchKeyword(),
                getSearchColumn(), request.getMatchEqualArr(),
                request.getIsAnd()) {
            @Override
            public Predicate toPredicate(Root<UserDiskPoolEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Assert.notNull(root, "pageQueryUserDiskPool root is null");
                Assert.notNull(query, "pageQueryUserDiskPool query is null");
                Assert.notNull(cb, "pageQueryUserDiskPool cb is null");

                Predicate predicate = super.toPredicate(root, query, cb);
                Assert.notNull(predicate, "predicate is null");

                // 添加用户组或用户条件
                Predicate paramPredicate;
                if (BooleanUtils.toBoolean(isAddGroup)) {
                    Predicate groupPredicate1 = cb.equal(root.get(RELATED_ID), cbbUserDetailDTO.getGroupId());
                    Predicate groupPredicate2 = cb.equal(root.get(RELATED_TYPE), IacConfigRelatedType.USERGROUP);
                    paramPredicate = cb.and(groupPredicate1, groupPredicate2);
                } else {
                    Predicate userPredicate1 = cb.equal(root.get(RELATED_ID), cbbUserDetailDTO.getId());
                    Predicate userPredicate2 = cb.equal(root.get(RELATED_TYPE), IacConfigRelatedType.USER);
                    paramPredicate = cb.and(userPredicate1, userPredicate2);
                }


                predicate.getExpressions().add(paramPredicate);
                return predicate;
            }
        }, pageable);
        return DefaultPageResponse.Builder.success(request.getLimit(), (int) diskPoolPage.getTotalElements(),
                diskPoolPage.getContent().toArray(new UserDiskPoolEntity[0]));
    }

    @Override
    public void deactivateDisk(UUID desktopId, UUID diskId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId can not be null");
        Assert.notNull(diskId, "diskId can not be null");
        // 卸载磁盘
        cbbVDIDeskDiskAPI.deactivateDisk(desktopId, diskId);
    }

    @Override
    public GuesttoolMessageContent buildFailMessage(UUID deskId , Integer code) {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(code, "code must not be null");

        GuesttoolMessageContent guesttoolMessageContent = new GuesttoolMessageContent();
        guesttoolMessageContent.setCode(code);
        guesttoolMessageContent.setMessage(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_NOT_EXIST_PERSONAL_DISK,
                String.valueOf(deskId)));
        guesttoolMessageContent.setContent(StringUtils.EMPTY);

        return guesttoolMessageContent;
    }

    @Override
    public void setUserInfoForAttachDiskMessage(AttachDiskMessageDTO attachDiskMessageDTO, @Nullable UUID userId) {
        Assert.notNull(attachDiskMessageDTO, "attachDiskMessageDTO must not be null");

        if (userId != null) {
            RcoViewUserEntity userInfo = userService.getUserInfoById(userId);
            if (userInfo != null) {
                attachDiskMessageDTO.setUserName(userInfo.getUserName());
                attachDiskMessageDTO.setUserType(userInfo.getUserType());
            }
        }
    }

    @Override
    public DefaultPageResponse<UserDiskPoolEntity> pageQueryAdGroupDiskPool(UUID adGroupId, PageSearchRequest request) {
        Assert.notNull(adGroupId, "pageDiskPoolByUserId cbbUserDetailDTO can not be null");
        Assert.notNull(request, "pageDiskPoolByUserId request can not be null");
        DefaultDataSort defaultDataSort = getDefaultDataSort();
        Assert.notNull(defaultDataSort, "默认排序信息不能为null");
        String defaultSortField = defaultDataSort.getSortField();
        Assert.hasText(defaultSortField, "默认排序信息不能为空");
        // 构建Pageable
        Pageable pageable = getPageRequest(request, defaultSortField, defaultDataSort.getDirection());
        Page<UserDiskPoolEntity> diskPoolPage = userDiskPoolDAO.findAll(new PageQuerySpecification<UserDiskPoolEntity>(request.getSearchKeyword(),
                getSearchColumn(), request.getMatchEqualArr(),
                request.getIsAnd()) {
            @Override
            public Predicate toPredicate(Root<UserDiskPoolEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Assert.notNull(root, "pageQueryUserDiskPool root is null");
                Assert.notNull(query, "pageQueryUserDiskPool query is null");
                Assert.notNull(cb, "pageQueryUserDiskPool cb is null");

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
        return DefaultPageResponse.Builder.success(request.getLimit(), (int) diskPoolPage.getTotalElements(),
                diskPoolPage.getContent().toArray(new UserDiskPoolEntity[0]));
    }
}
