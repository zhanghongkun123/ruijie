package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
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
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchPushInstallPackageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewPushInstallPackageDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewPushInstallPackageEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryAppEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewPushInstallPackageService;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/04/05 16:09
 *
 * @author coderLee23
 */
@Service
public class ViewPushInstallPackageServiceImpl implements ViewPushInstallPackageService {

    @Autowired
    private ViewPushInstallPackageDAO viewPushInstallPackageDAO;

    @Override
    public Page<ViewPushInstallPackageEntity> pagePushInstallPackage(SearchPushInstallPackageDTO searchPushInstallPackageDTO, Pageable pageable) {
        Assert.notNull(searchPushInstallPackageDTO, "searchPushInstallPackageDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Specification<ViewPushInstallPackageEntity> specification = (root, query, criteriaBuilder) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(criteriaBuilder, "CriteriaBuilder is null");

            Predicate conjunction = criteriaBuilder.conjunction();

            UUID id = searchPushInstallPackageDTO.getId();
            // id 存在，则直接根据id查询，主要用于首页搜索的选中跳转使用
            if (Objects.nonNull(id)) {
                conjunction.getExpressions().add(criteriaBuilder.equal(root.get(DBConstants.ID), id));
            }

            // filterDeliveryGroupId 过滤已经添加的交付应用
            UUID filterDeliveryGroupId = searchPushInstallPackageDTO.getFilterGroupId();
            DataSourceTypeEnum dataSourceType = searchPushInstallPackageDTO.getDataSourceType();

            if (dataSourceType == DataSourceTypeEnum.DELIVERY_GROUP) {
                if (Objects.nonNull(filterDeliveryGroupId)) {
                    Subquery<ViewUamDeliveryAppEntity> subquery = query.subquery(ViewUamDeliveryAppEntity.class);
                    Root<ViewUamDeliveryAppEntity> subRoot = subquery.from(ViewUamDeliveryAppEntity.class);
                    subquery.select(subRoot);
                    subquery.where(criteriaBuilder.equal(root.get(DBConstants.ID), subRoot.get(DBConstants.APP_ID)),
                            criteriaBuilder.equal(subRoot.get(DBConstants.DELIVERY_GROUP_ID), filterDeliveryGroupId));
                    conjunction.getExpressions().add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));
                }
            }

            // 只支持可选已发布
            if (Objects.nonNull(dataSourceType)) {
                List<AppStatusEnum> statusList = Collections.singletonList(AppStatusEnum.PUBLISHED);
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.APP_STATUS)).value(statusList));
            }

            // appStatus 查询
            List<AppStatusEnum> appStatusList = searchPushInstallPackageDTO.getAppStatusList();
            if (!CollectionUtils.isEmpty(appStatusList)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.APP_STATUS)).value(appStatusList));
            }

            // 增加交付应用时只支持可选交付组同操作系统类型的
            OsPlatform osPlatform = searchPushInstallPackageDTO.getOsPlatform();
            if (Objects.nonNull(osPlatform)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.OS_PLATFORM)).value(osPlatform));
            }

            // appName采用模糊查询
            String appName = searchPushInstallPackageDTO.getAppName();
            if (StringUtils.hasText(appName)) {
                Predicate appNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(DBConstants.APP_NAME)),
                        DBConstants.LIKE + appName.toLowerCase() + DBConstants.LIKE);
                conjunction.getExpressions().add(appNamePredicate);
            }

            // 权限处理
            if (!Boolean.TRUE.equals(searchPushInstallPackageDTO.getHasAllPermission())) {
                Subquery<AdminDataPermissionEntity> subquery = query.subquery(AdminDataPermissionEntity.class);
                Root<AdminDataPermissionEntity> subRoot = subquery.from(AdminDataPermissionEntity.class);
                subquery.select(subRoot);
                subquery.where(criteriaBuilder.equal(root.get(DBConstants.ID).as(String.class), subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                        criteriaBuilder.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.UAM_APP),
                        criteriaBuilder.equal(subRoot.get(DBConstants.ADMIN_ID), searchPushInstallPackageDTO.getAdminId()));
                conjunction.getExpressions().add(criteriaBuilder.exists(subquery));
            }

            return conjunction;
        };

        return viewPushInstallPackageDAO.findAll(specification, pageable);
    }

}
