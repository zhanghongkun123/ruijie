package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryTestDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamAppTestDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestApplicationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUamAppTestService;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/04/05 23:23
 *
 * @author coderLee23
 */
@Service
public class ViewUamAppTestServiceImpl implements ViewUamAppTestService {

    @Autowired
    private ViewUamAppTestDAO viewUamAppTestDAO;

    @Override
    public Page<ViewUamAppTestEntity> pageUamAppTest(SearchDeliveryTestDTO searchDeliveryTestDTO, Pageable pageable) {
        Assert.notNull(searchDeliveryTestDTO, "searchDeliveryTestDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Specification<ViewUamAppTestEntity> specification = (root, query, cb) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(cb, "CriteriaBuilder is null");

            Predicate conjunction = cb.conjunction();

            // id 根据id查询，主要用于首页搜索跳转使用
            UUID id = searchDeliveryTestDTO.getId();
            if (Objects.nonNull(id)) {
                Predicate idPredicate = cb.equal(root.get(DBConstants.ID), id);
                conjunction.getExpressions().add(idPredicate);
            }

            // 根据应用获取所有测试任务
            UUID appId = searchDeliveryTestDTO.getAppId();
            if (Objects.nonNull(appId)) {
                Subquery<ViewUamAppTestApplicationEntity> subquery = query.subquery(ViewUamAppTestApplicationEntity.class);
                Root<ViewUamAppTestApplicationEntity> subRoot = subquery.from(ViewUamAppTestApplicationEntity.class);
                subquery.select(subRoot);
                subquery.where(cb.equal(root.get(DBConstants.ID), subRoot.get(DBConstants.TEST_ID)),
                        cb.equal(subRoot.get(DBConstants.APP_ID), appId));
                conjunction.getExpressions().add(cb.exists(subquery));
            }

            // deliveryGroupName采用模糊查询
            String name = searchDeliveryTestDTO.getName();
            if (StringUtils.hasText(name)) {
                Predicate appNamePredicate = cb.like(cb.lower(root.get(DBConstants.NAME)), "%" + name.toLowerCase() + "%");
                conjunction.getExpressions().add(appNamePredicate);
            }

            List<CbbImageType> appSoftwarePackageTypeList = searchDeliveryTestDTO.getAppSoftwarePackageTypeList();
            if (!CollectionUtils.isEmpty(appSoftwarePackageTypeList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.APP_SOFTWARE_PACKAGE_TYPE)).value(appSoftwarePackageTypeList));
            }

            List<TestTaskStateEnum> stateList = searchDeliveryTestDTO.getStateList();
            if (!CollectionUtils.isEmpty(stateList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.STATE)).value(stateList));
            }

            // 权限处理
            if (!Boolean.TRUE.equals(searchDeliveryTestDTO.getHasAllPermission())) {
                Subquery<AdminDataPermissionEntity> subquery = query.subquery(AdminDataPermissionEntity.class);
                Root<AdminDataPermissionEntity> subRoot = subquery.from(AdminDataPermissionEntity.class);
                subquery.select(subRoot);
                subquery.where(cb.equal(root.get(DBConstants.ID).as(String.class), subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                        cb.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.DELIVERY_TEST),
                        cb.equal(subRoot.get(DBConstants.ADMIN_ID), searchDeliveryTestDTO.getAdminId()));
                conjunction.getExpressions().add(cb.exists(subquery));
            }

            return conjunction;
        };

        return viewUamAppTestDAO.findAll(specification, pageable);
    }

}
