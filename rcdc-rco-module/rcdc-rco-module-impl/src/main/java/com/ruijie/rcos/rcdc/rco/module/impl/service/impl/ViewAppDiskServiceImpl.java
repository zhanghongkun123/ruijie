package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.*;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DataSourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchAppDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.RequestSourceEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewAppDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.*;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewAppDiskService;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/04/05 16:10
 *
 * @author coderLee23
 */
@Service
public class ViewAppDiskServiceImpl implements ViewAppDiskService {


    @Autowired
    private ViewAppDiskDAO viewAppDiskDAO;

    @Override
    public Page<ViewAppDiskEntity> pageAppDisk(SearchAppDiskDTO searchAppDiskDTO, Pageable pageable) {
        Assert.notNull(searchAppDiskDTO, "searchAppDiskDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Specification<ViewAppDiskEntity> specification = (root, query, criteriaBuilder) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(criteriaBuilder, "CriteriaBuilder is null");

            Predicate conjunction = criteriaBuilder.conjunction();

            UUID id = searchAppDiskDTO.getId();
            // id 存在，则直接根据id查询，主要用于首页搜索的选中跳转使用
            if (Objects.nonNull(id)) {
                conjunction.getExpressions().add(criteriaBuilder.equal(root.get(DBConstants.ID), id));
            }

            // filterDeliveryGroupId 过滤已经添加的交付应用
            UUID filterDeliveryGroupId = searchAppDiskDTO.getFilterGroupId();
            DataSourceTypeEnum dataSourceType = searchAppDiskDTO.getDataSourceType();

            if (dataSourceType == DataSourceTypeEnum.DELIVERY_GROUP) {
                if (Objects.nonNull(filterDeliveryGroupId)) {
                    Subquery<ViewUamDeliveryAppEntity> subquery = query.subquery(ViewUamDeliveryAppEntity.class);
                    Root<ViewUamDeliveryAppEntity> subRoot = subquery.from(ViewUamDeliveryAppEntity.class);
                    subquery.select(subRoot);
                    subquery.where(criteriaBuilder.equal(root.get(DBConstants.ID), subRoot.get(DBConstants.APP_ID)),
                            criteriaBuilder.equal(subRoot.get(DBConstants.DELIVERY_GROUP_ID), filterDeliveryGroupId));
                    conjunction.getExpressions().add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));

                    // 存在交付组id，则需要获取类型相同的应用出来
                    Subquery<ViewUamDeliveryGroupEntity> groupSubquery = query.subquery(ViewUamDeliveryGroupEntity.class);
                    Root<ViewUamDeliveryGroupEntity> groupSubRoot = groupSubquery.from(ViewUamDeliveryGroupEntity.class);
                    groupSubquery.select(groupSubRoot);
                    groupSubquery.where(criteriaBuilder.equal(root.get(DBConstants.OS_TYPE), groupSubRoot.get(DBConstants.OS_TYPE)),
                            criteriaBuilder.equal(root.get(DBConstants.OS_VERSION), groupSubRoot.get(DBConstants.OS_VERSION)),
                            criteriaBuilder.equal(root.get(DBConstants.CBB_IMAGE_TYPE), groupSubRoot.get(DBConstants.CBB_IMAGE_TYPE)),
                            criteriaBuilder.equal(root.get(DBConstants.IMAGE_TEMPLATE_ID), groupSubRoot.get(DBConstants.IMAGE_TEMPLATE_ID)),
                            criteriaBuilder.equal(groupSubRoot.get(DBConstants.ID), filterDeliveryGroupId));
                    conjunction.getExpressions().add(criteriaBuilder.exists(groupSubquery));

                }
                // 只支持可选已发布
                List<AppStatusEnum> statusList = Collections.singletonList(AppStatusEnum.PUBLISHED);
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.APP_STATUS)).value(statusList));
            }

            // testId 过滤已经添加的测试应用
            if (dataSourceType == DataSourceTypeEnum.DELIVERY_TEST) {
                if (Objects.nonNull(filterDeliveryGroupId)) {
                    Subquery<ViewUamAppTestApplicationEntity> subquery = query.subquery(ViewUamAppTestApplicationEntity.class);
                    Root<ViewUamAppTestApplicationEntity> subRoot = subquery.from(ViewUamAppTestApplicationEntity.class);
                    subquery.select(subRoot);
                    subquery.where(criteriaBuilder.equal(root.get(DBConstants.ID), subRoot.get(DBConstants.APP_ID)),
                            criteriaBuilder.equal(subRoot.get(DBConstants.TEST_ID), filterDeliveryGroupId));
                    conjunction.getExpressions().add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));

                    // 存在交付组id，则需要获取类型相同的应用出来
                    Subquery<ViewUamAppTestEntity> testSubquery = query.subquery(ViewUamAppTestEntity.class);
                    Root<ViewUamAppTestEntity> testSubRoot = testSubquery.from(ViewUamAppTestEntity.class);
                    testSubquery.select(testSubRoot);
                    testSubquery.where(criteriaBuilder.equal(root.get(DBConstants.OS_TYPE), testSubRoot.get(DBConstants.OS_TYPE)),
                            criteriaBuilder.equal(root.get(DBConstants.OS_VERSION), testSubRoot.get(DBConstants.OS_VERSION)),
                            criteriaBuilder.equal(root.get(DBConstants.APP_SOFTWARE_PACKAGE_TYPE), testSubRoot.get(DBConstants.CBB_IMAGE_TYPE)),
                            criteriaBuilder.equal(root.get(DBConstants.IMAGE_TEMPLATE_ID), testSubRoot.get(DBConstants.IMAGE_TEMPLATE_ID)),
                            criteriaBuilder.equal(testSubRoot.get(DBConstants.ID), filterDeliveryGroupId));
                    conjunction.getExpressions().add(criteriaBuilder.exists(testSubquery));
                }

                // 可选已发布和待发布
                List<AppStatusEnum> statusList = Arrays.asList(AppStatusEnum.PRE_PUBLISH, AppStatusEnum.PUBLISHED);
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.APP_STATUS)).value(statusList));
            }

            // appStatus 查询
            List<AppStatusEnum> appStatusList = searchAppDiskDTO.getAppStatusList();
            if (!CollectionUtils.isEmpty(appStatusList)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.APP_STATUS)).value(appStatusList));
            }

            // 虚机类型查询
            List<CbbImageType> cbbImageTypeList = searchAppDiskDTO.getCbbImageTypeList();
            if (!CollectionUtils.isEmpty(cbbImageTypeList)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.CBB_IMAGE_TYPE)).value(cbbImageTypeList));
            }

            // 操作系统
            List<CbbOsType> cbbOsTypeList = searchAppDiskDTO.getCbbOsTypeList();
            if (!CollectionUtils.isEmpty(cbbOsTypeList)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).value(cbbOsTypeList));
            }

            List<CloudPlatformStatus> platformStatusList = searchAppDiskDTO.getPlatformStatusList();
            if (!CollectionUtils.isEmpty(platformStatusList)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.PLATFORM_STATUS)).value(platformStatusList));
            }

            // appName采用模糊查询
            String appName = searchAppDiskDTO.getAppName();
            if (StringUtils.hasText(appName)) {
                Predicate appNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(DBConstants.APP_NAME)),
                        DBConstants.LIKE + appName.toLowerCase() + DBConstants.LIKE);
                // 非空，则说明是 应用磁盘交付组或者是应用测试选择应用的操作无需过滤
                RequestSourceEnum requestSource = searchAppDiskDTO.getRequestSource();
                if (requestSource == RequestSourceEnum.APP_DISK) {
                    Predicate disjunction = criteriaBuilder.disjunction();
                    disjunction.getExpressions().add(appNamePredicate);
                    Predicate imageTempateNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(DBConstants.IMAGE_TEMPLATE_NAME)),
                            DBConstants.LIKE + appName.toLowerCase() + DBConstants.LIKE);

                    disjunction.getExpressions().add(imageTempateNamePredicate);

                    Predicate platformNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(DBConstants.PLATFORM_NAME)),
                            DBConstants.LIKE + appName.toLowerCase() + DBConstants.LIKE);
                    disjunction.getExpressions().add(platformNamePredicate);

                    conjunction.getExpressions().add(disjunction);
                } else {
                    conjunction.getExpressions().add(appNamePredicate);
                }
            }

            // 权限处理
            if (!Boolean.TRUE.equals(searchAppDiskDTO.getHasAllPermission())) {
                Subquery<AdminDataPermissionEntity> subquery = query.subquery(AdminDataPermissionEntity.class);
                Root<AdminDataPermissionEntity> subRoot = subquery.from(AdminDataPermissionEntity.class);
                subquery.select(subRoot);
                subquery.where(criteriaBuilder.equal(root.get(DBConstants.ID).as(String.class), subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                        criteriaBuilder.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.UAM_APP),
                        criteriaBuilder.equal(subRoot.get(DBConstants.ADMIN_ID), searchAppDiskDTO.getAdminId()));
                conjunction.getExpressions().add(criteriaBuilder.exists(subquery));
            }

            return conjunction;
        };

        return viewAppDiskDAO.findAll(specification, pageable);
    }

    @Override
    public long countByConditions(ConditionQueryRequest request) {
        Assert.notNull(request, "request can not be null");
        return viewAppDiskDAO.countByConditions(request);
    }

}
