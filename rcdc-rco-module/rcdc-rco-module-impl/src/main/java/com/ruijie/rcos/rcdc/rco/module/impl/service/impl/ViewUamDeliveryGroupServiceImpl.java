package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

import com.ruijie.rcos.rcdc.appcenter.module.def.CbbAppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.HomePageSearchDeliveryGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamDeliveryGroupDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryAppEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryGroupEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUamDeliveryGroupService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/12 19:35
 *
 * @author coderLee23
 */
@Service
public class ViewUamDeliveryGroupServiceImpl implements ViewUamDeliveryGroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewUamDeliveryGroupServiceImpl.class);

    @Autowired
    private ViewUamDeliveryGroupDAO viewUamDeliveryGroupDao;

    @Override
    public Page<ViewUamDeliveryGroupEntity> pageUamDeliveryGroup(SearchDeliveryGroupDTO searchDeliveryGroupDTO, Pageable pageable) {
        Assert.notNull(searchDeliveryGroupDTO, "searchDeliveryGroupDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");

        Specification<ViewUamDeliveryGroupEntity> specification = (root, query, cb) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(cb, "CriteriaBuilder is null");

            Predicate conjunction = cb.conjunction();

            // 根据 应用类型分组查询
            AppDeliveryTypeEnum appDeliveryType = searchDeliveryGroupDTO.getAppDeliveryType();
            if (Objects.nonNull(appDeliveryType)) {
                conjunction.getExpressions().add(cb.equal(root.get(DBConstants.APP_DELIVERY_TYPE), appDeliveryType));
            }

            // id 根据id查询，主要用于首页搜索跳转使用
            UUID id = searchDeliveryGroupDTO.getId();
            if (Objects.nonNull(id)) {
                Predicate idPredicate = cb.equal(root.get(DBConstants.ID), id);
                conjunction.getExpressions().add(idPredicate);
            }

            // 根据应用获取所有交付组
            UUID appId = searchDeliveryGroupDTO.getAppId();
            if (Objects.nonNull(appId)) {
                Subquery<ViewUamDeliveryAppEntity> subquery = query.subquery(ViewUamDeliveryAppEntity.class);
                Root<ViewUamDeliveryAppEntity> subRoot = subquery.from(ViewUamDeliveryAppEntity.class);
                subquery.select(subRoot);
                subquery.where(cb.equal(root.get(DBConstants.ID), subRoot.get(DBConstants.DELIVERY_GROUP_ID)),
                        cb.equal(subRoot.get(DBConstants.APP_ID), appId));
                conjunction.getExpressions().add(cb.exists(subquery));
            }

            // deliveryGroupName采用模糊查询
            String name = searchDeliveryGroupDTO.getName();
            if (StringUtils.hasText(name)) {
                Predicate appNamePredicate = cb.like(cb.lower(root.get(DBConstants.DELIVERY_GROUP_NAME)), "%" + name.toLowerCase() + "%");
                conjunction.getExpressions().add(appNamePredicate);
            }


            List<CbbImageType> cbbImageTypeList = searchDeliveryGroupDTO.getCbbImageTypeList();
            if (!CollectionUtils.isEmpty(cbbImageTypeList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.CBB_IMAGE_TYPE)).value(cbbImageTypeList));
            }

            // 权限处理
            if (!Boolean.TRUE.equals(searchDeliveryGroupDTO.getHasAllPermission())) {
                Subquery<AdminDataPermissionEntity> subquery = query.subquery(AdminDataPermissionEntity.class);
                Root<AdminDataPermissionEntity> subRoot = subquery.from(AdminDataPermissionEntity.class);
                subquery.select(subRoot);
                subquery.where(cb.equal(root.get(DBConstants.ID).as(String.class), subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                        cb.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.DELIVERY_GROUP),
                        cb.equal(subRoot.get(DBConstants.ADMIN_ID), searchDeliveryGroupDTO.getAdminId()));
                conjunction.getExpressions().add(cb.exists(subquery));
            }



            return conjunction;
        };

        return viewUamDeliveryGroupDao.findAll(specification, pageable);
    }

    @Override
    public Page<ViewUamDeliveryGroupEntity> pageUamDeliveryGroup(HomePageSearchDeliveryGroupDTO homePageSearchDeliveryGroupDTO, Pageable pageable) {
        Assert.notNull(homePageSearchDeliveryGroupDTO, "homePageSearchDeliveryGroupDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");

        Specification<ViewUamDeliveryGroupEntity> specification = (root, query, cb) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(cb, "CriteriaBuilder is null");

            Predicate conjunction = cb.conjunction();

            // 根据 应用类型分组查询
            AppDeliveryTypeEnum appDeliveryType = homePageSearchDeliveryGroupDTO.getAppDeliveryType();
            if (Objects.nonNull(appDeliveryType)) {
                conjunction.getExpressions().add(cb.equal(root.get(DBConstants.APP_DELIVERY_TYPE), appDeliveryType));
            }

            String deliveryGroupName = homePageSearchDeliveryGroupDTO.getDeliveryGroupName();
            // deliveryGroupName采用模糊查询
            if (StringUtils.hasText(deliveryGroupName)) {
                Predicate appNamePredicate =
                        cb.like(cb.lower(root.get(DBConstants.DELIVERY_GROUP_NAME)), "%" + deliveryGroupName.toLowerCase() + "%");
                conjunction.getExpressions().add(appNamePredicate);
            }

            // 权限处理
            if (!Boolean.TRUE.equals(homePageSearchDeliveryGroupDTO.getHasAllPermission())) {
                Subquery<AdminDataPermissionEntity> subquery = query.subquery(AdminDataPermissionEntity.class);
                Root<AdminDataPermissionEntity> subRoot = subquery.from(AdminDataPermissionEntity.class);
                subquery.select(subRoot);
                subquery.where(cb.equal(root.get(DBConstants.ID).as(String.class), subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                        cb.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.DELIVERY_GROUP),
                        cb.equal(subRoot.get(DBConstants.ADMIN_ID), homePageSearchDeliveryGroupDTO.getAdminId()));
                conjunction.getExpressions().add(cb.exists(subquery));
            }

            return conjunction;
        };

        return viewUamDeliveryGroupDao.findAll(specification, pageable);
    }

    @Override
    public ViewUamDeliveryGroupEntity findById(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        Optional<ViewUamDeliveryGroupEntity> viewUamDeliveryGroupEntityOptional = viewUamDeliveryGroupDao.findById(id);
        if (!viewUamDeliveryGroupEntityOptional.isPresent()) {
            LOGGER.error("ID为{}的交付组不存在,请核实是否存在或删除！", id);
            throw new BusinessException(CbbAppCenterBusinessKey.RCDC_UAM_DELIVERY_GROUP_NOT_EXIST);
        }
        return viewUamDeliveryGroupEntityOptional.get();
    }
}
