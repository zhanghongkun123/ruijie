package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DataSourceTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TaskGetGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TaskSearchGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.GetGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.TerminalGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalGroupDesktopRelatedDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalGroupDesktopRelatedEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryObjectEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewTerminalGroupDesktopRelatedService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/09 17:57
 *
 * @author coderLee23
 */
@Service
public class ViewTerminalGroupDesktopRelatedServiceImpl implements ViewTerminalGroupDesktopRelatedService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ViewTerminalGroupDesktopRelatedServiceImpl.class);


    @Autowired
    private ViewTerminalGroupDesktopRelatedDAO viewTerminalGroupDesktopRelatedDAO;

    @PersistenceContext(unitName = "datasourceRootDefaultEntityManager")
    private EntityManager entityManager;


    @Override
    public Page<ViewTerminalGroupDesktopRelatedEntity> pageTerminalGroupDesktopRelated(SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO,
            Pageable pageable) {
        Assert.notNull(searchGroupDesktopRelatedDTO, "searchGroupDesktopRelatedDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");

        Specification<ViewTerminalGroupDesktopRelatedEntity> specification = (root, query, criteriaBuilder) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(criteriaBuilder, "CriteriaBuilder is null");

            Predicate conjunction = criteriaBuilder.conjunction();

            // 只允许添加应用分层
            Predicate desktopTypePredicate = criteriaBuilder.equal(root.get(DBConstants.DESKTOP_TYPE), CbbCloudDeskPattern.PERSONAL);
            conjunction.getExpressions().add(desktopTypePredicate);

            // 只允许可用的桌面状态可选
            conjunction.getExpressions()
                    .add(criteriaBuilder.in(root.get(DBConstants.DESK_STATE)).value(CbbCloudDeskState.AVAILABLE_CLOUD_DESK_STATE));

            UUID imageTemplateId = searchGroupDesktopRelatedDTO.getImageTemplateId();
            if (Objects.nonNull(imageTemplateId)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.IMAGE_TEMPLATE_ID)).value(imageTemplateId));
            }

            // 忽略掉已经被使用的云桌面
            DataSourceTypeEnum dataSourceType = searchGroupDesktopRelatedDTO.getDataSourceType();
            if (dataSourceType == DataSourceTypeEnum.DELIVERY_GROUP) {
                if (Objects.nonNull(searchGroupDesktopRelatedDTO.getFilterGroupId())) {
                    Subquery<ViewUamDeliveryObjectEntity> subquery = query.subquery(ViewUamDeliveryObjectEntity.class);
                    Root<ViewUamDeliveryObjectEntity> subRoot = subquery.from(ViewUamDeliveryObjectEntity.class);
                    subquery.select(subRoot);
                    subquery.where(criteriaBuilder.equal(root.get(DBConstants.CLOUD_DESKTOP_ID), subRoot.get(DBConstants.CLOUD_DESKTOP_ID)),
                            criteriaBuilder.equal(subRoot.get(DBConstants.DELIVERY_GROUP_ID), searchGroupDesktopRelatedDTO.getFilterGroupId()));
                    conjunction.getExpressions().add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));
                }
            }

            if (dataSourceType == DataSourceTypeEnum.DELIVERY_TEST) {
                if (Objects.nonNull(searchGroupDesktopRelatedDTO.getFilterGroupId())) {
                    Subquery<ViewUamAppTestDesktopEntity> subQuery = query.subquery(ViewUamAppTestDesktopEntity.class);
                    Root<ViewUamAppTestDesktopEntity> subRoot = subQuery.from(ViewUamAppTestDesktopEntity.class);
                    subQuery.select(subRoot);
                    subQuery.where(criteriaBuilder.equal(root.get(DBConstants.CLOUD_DESKTOP_ID), subRoot.get(DBConstants.DESK_ID)),
                            criteriaBuilder.equal(subRoot.get(DBConstants.TEST_ID), searchGroupDesktopRelatedDTO.getFilterGroupId()));
                    conjunction.getExpressions().add(criteriaBuilder.not(criteriaBuilder.exists(subQuery)));
                }
            }

            List<CbbCloudDeskState> deskStateList = searchGroupDesktopRelatedDTO.getDeskStateList();
            if (!CollectionUtils.isEmpty(deskStateList)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.DESK_STATE)).value(deskStateList));
            }

            // 点击终端组，返回可选云桌面列表
            UUID id = searchGroupDesktopRelatedDTO.getGroupId();
            if (Objects.nonNull(id)) {
                Predicate idPredicate = criteriaBuilder.equal(root.get(DBConstants.TERMINAL_GROUP_ID), id);
                conjunction.getExpressions().add(idPredicate);
            }

            CbbOsType criteriaBuilderOsType = searchGroupDesktopRelatedDTO.getOsType();
            if (Objects.nonNull(criteriaBuilderOsType)) {
                Predicate osTypePredicate = criteriaBuilder.equal(root.get(DBConstants.OS_TYPE), criteriaBuilderOsType);
                conjunction.getExpressions().add(osTypePredicate);
            }

            CbbImageType criteriaBuilderImageType = searchGroupDesktopRelatedDTO.getCbbImageType();
            if (Objects.nonNull(criteriaBuilderImageType)) {
                Predicate criteriaBuilderbImageTypePredicate = criteriaBuilder.equal(root.get(DBConstants.CBB_IMAGE_TYPE), criteriaBuilderImageType);
                conjunction.getExpressions().add(criteriaBuilderbImageTypePredicate);
            }

            if (Objects.nonNull(criteriaBuilderOsType) || Objects.nonNull(criteriaBuilderImageType)) {
                // 存在应用磁盘，则只能筛选 win7 win10的操作系统
                conjunction.getExpressions()
                        .add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).value(CbbOsType.APP_SOFTWARE_PACKAGE_WINDOWS_OS_TYPE_LIST));
            } else {
                OsPlatform osPlatform = searchGroupDesktopRelatedDTO.getOsPlatform();
                //根据交付组的操作系统类型筛选匹配的桌面
                if (osPlatform == OsPlatform.WINDOWS) {
                    conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).value(CbbOsType.WINDOWS_OS_TYPE_LIST));
                }
                if (osPlatform == OsPlatform.LINUX) {
                    conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).
                            value(CbbOsType.APP_PUSH_PACKAGE_LINUX_OS_TYPE_LIST));
                }
            }

            Predicate disjunction = criteriaBuilder.disjunction();

            String searchName = searchGroupDesktopRelatedDTO.getSearchName();
            // terminalName 和 desktopName 采用模糊查询，忽略大小写
            if (StringUtils.hasText(searchName)) {

                Predicate terminalNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(DBConstants.TERMINAL_NAME)),
                        DBConstants.LIKE + searchName.toLowerCase() + DBConstants.LIKE);
                disjunction.getExpressions().add(terminalNamePredicate);

                Predicate desktopNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(DBConstants.DESKTOP_NAME)),
                        DBConstants.LIKE + searchName.toLowerCase() + DBConstants.LIKE);
                disjunction.getExpressions().add(desktopNamePredicate);
            }

            // 权限处理
            if (!Boolean.TRUE.equals(searchGroupDesktopRelatedDTO.getHasAllPermission())) {
                Subquery<AdminDataPermissionEntity> subquery = query.subquery(AdminDataPermissionEntity.class);
                Root<AdminDataPermissionEntity> subRoot = subquery.from(AdminDataPermissionEntity.class);
                subquery.select(subRoot);
                subquery.where(
                        criteriaBuilder.equal(root.get(DBConstants.TERMINAL_GROUP_ID).as(String.class), subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                        criteriaBuilder.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.TERMINAL_GROUP),
                        criteriaBuilder.equal(subRoot.get(DBConstants.ADMIN_ID), searchGroupDesktopRelatedDTO.getAdminId()));
                conjunction.getExpressions().add(criteriaBuilder.exists(subquery));
            }

            if (!disjunction.getExpressions().isEmpty()) {
                conjunction.getExpressions().add(disjunction);
            }

            return conjunction;
        };

        return viewTerminalGroupDesktopRelatedDAO.findAll(specification, pageable);
    }

    @Override
    public List<ViewTerminalGroupDesktopRelatedEntity> listTerminalGroupDesktopRelated(SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO) {
        Assert.notNull(searchGroupDesktopRelatedDTO, "searchGroupDesktopRelatedDTO must not be null");

        Specification<ViewTerminalGroupDesktopRelatedEntity> specification = (root, query, criteriaBuilder) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(criteriaBuilder, "CriteriaBuilder is null");

            Predicate conjunction = criteriaBuilder.conjunction();

            // 只允许添加应用分层
            Predicate desktopTypePredicate = criteriaBuilder.equal(root.get(DBConstants.DESKTOP_TYPE), CbbCloudDeskPattern.PERSONAL);
            conjunction.getExpressions().add(desktopTypePredicate);

            // 只允许可用的桌面状态可选
            conjunction.getExpressions()
                    .add(criteriaBuilder.in(root.get(DBConstants.DESK_STATE)).value(CbbCloudDeskState.AVAILABLE_CLOUD_DESK_STATE));

            // 应用磁盘只能选择相同的镜像模板
            UUID imageTemplateId = searchGroupDesktopRelatedDTO.getImageTemplateId();
            if (Objects.nonNull(imageTemplateId)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.IMAGE_TEMPLATE_ID)).value(imageTemplateId));
            }

            // 忽略掉已经被使用的云桌面
            DataSourceTypeEnum dataSourceType = searchGroupDesktopRelatedDTO.getDataSourceType();
            if (dataSourceType == DataSourceTypeEnum.DELIVERY_GROUP) {
                Subquery<ViewUamDeliveryObjectEntity> subquery = query.subquery(ViewUamDeliveryObjectEntity.class);
                Root<ViewUamDeliveryObjectEntity> subRoot = subquery.from(ViewUamDeliveryObjectEntity.class);
                subquery.select(subRoot);

                AppDeliveryTypeEnum appDeliveryType = searchGroupDesktopRelatedDTO.getAppDeliveryType();
                Predicate subConjunction = criteriaBuilder.conjunction();
                // 应用磁盘忽略掉被所有交付组已选【含自身交付组】
                subConjunction.getExpressions()
                        .add(criteriaBuilder.equal(root.get(DBConstants.CLOUD_DESKTOP_ID), subRoot.get(DBConstants.CLOUD_DESKTOP_ID)));

                // 应用交付类型
                subConjunction.getExpressions().add(criteriaBuilder.equal(subRoot.get(DBConstants.APP_DELIVERY_TYPE), appDeliveryType));
                // 推送安装包忽略掉被自身交付组已选。
                if (appDeliveryType == AppDeliveryTypeEnum.PUSH_INSTALL_PACKAGE) {
                    UUID filterGroupId = searchGroupDesktopRelatedDTO.getFilterGroupId();
                    subConjunction.getExpressions().add(criteriaBuilder.equal(subRoot.get(DBConstants.DELIVERY_GROUP_ID), filterGroupId));
                }

                subquery.where(subConjunction);
                conjunction.getExpressions().add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));
            }

            if (dataSourceType == DataSourceTypeEnum.DELIVERY_TEST) {
                Subquery<ViewUamAppTestDesktopEntity> subquery = query.subquery(ViewUamAppTestDesktopEntity.class);
                Root<ViewUamAppTestDesktopEntity> subRoot = subquery.from(ViewUamAppTestDesktopEntity.class);
                subquery.select(subRoot);
                Predicate subConjunction = criteriaBuilder.conjunction();
                subConjunction.getExpressions().add(criteriaBuilder.equal(root.get(DBConstants.CLOUD_DESKTOP_ID), subRoot.get(DBConstants.DESK_ID)));
                final Predicate subDisjunction = criteriaBuilder.disjunction();
                subDisjunction.getExpressions()
                        .add(criteriaBuilder.in(subRoot.get(DBConstants.TEST_STATE)).value(DesktopTestStateEnum.getProcessingStateList()));
                if (Objects.nonNull(searchGroupDesktopRelatedDTO.getFilterGroupId())) {
                    Predicate testIdPredicate =
                            criteriaBuilder.equal(subRoot.get(DBConstants.TEST_ID), searchGroupDesktopRelatedDTO.getFilterGroupId());
                    subDisjunction.getExpressions().add(testIdPredicate);
                }
                subConjunction.getExpressions().add(subDisjunction);
                subquery.where(subConjunction);
                conjunction.getExpressions().add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));
                final Predicate disjunction = criteriaBuilder.disjunction();

                disjunction.getExpressions().add(criteriaBuilder.equal(root.get(DBConstants.DESK_STATE), CbbCloudDeskState.RUNNING));
                disjunction.getExpressions().add(criteriaBuilder.equal(root.get(DBConstants.STATE), CbbTerminalStateEnums.ONLINE));
                conjunction.getExpressions().add(disjunction);
            }

            CbbOsType cbbOsType = searchGroupDesktopRelatedDTO.getOsType();
            if (Objects.nonNull(cbbOsType)) {
                Predicate osTypePredicate = criteriaBuilder.equal(root.get(DBConstants.OS_TYPE), cbbOsType);
                conjunction.getExpressions().add(osTypePredicate);
            }

            String osVersion = searchGroupDesktopRelatedDTO.getOsVersion();
            if (StringUtils.hasText(osVersion)) {
                Predicate osTypePredicate = criteriaBuilder.equal(root.get(DBConstants.OS_VERSION), osVersion);
                conjunction.getExpressions().add(osTypePredicate);
            }

            CbbImageType criteriaBuilderImageType = searchGroupDesktopRelatedDTO.getCbbImageType();
            if (Objects.nonNull(criteriaBuilderImageType)) {
                Predicate criteriaBuilderImageTypePredicate = criteriaBuilder.equal(root.get(DBConstants.CBB_IMAGE_TYPE), criteriaBuilderImageType);
                conjunction.getExpressions().add(criteriaBuilderImageTypePredicate);
            }

            if (Objects.nonNull(cbbOsType) || Objects.nonNull(criteriaBuilderImageType)) {
                // 存在应用磁盘，则只能筛选 win7 win10的操作系统
                conjunction.getExpressions()
                        .add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).value(CbbOsType.APP_SOFTWARE_PACKAGE_WINDOWS_OS_TYPE_LIST));
            } else {
                OsPlatform osPlatform = searchGroupDesktopRelatedDTO.getOsPlatform();
                //根据交付组的操作系统类型筛选匹配的桌面
                if (osPlatform == OsPlatform.WINDOWS) {
                    conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).value(CbbOsType.WINDOWS_OS_TYPE_LIST));
                }
                if (osPlatform == OsPlatform.LINUX) {
                    conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).
                            value(CbbOsType.APP_PUSH_PACKAGE_LINUX_OS_TYPE_LIST));
                }
            }

            // 点击终端组，返回可选云桌面列表,已包含权限筛选。
            List<UUID> terminalGroupIdList = searchGroupDesktopRelatedDTO.getGroupIdList();
            if (!CollectionUtils.isEmpty(terminalGroupIdList)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.TERMINAL_GROUP_ID)).value(terminalGroupIdList));
            }

            return conjunction;
        };

        return viewTerminalGroupDesktopRelatedDAO.findAll(specification);
    }

    @Override
    public List<TerminalGroupDesktopCountDTO> listTerminalGroupDesktopCount(GetGroupDesktopCountDTO getGroupDesktopCountDTO) {
        Assert.notNull(getGroupDesktopCountDTO, "getGroupDesktopCountDTO must not be null");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TerminalGroupDesktopCountDTO> query = criteriaBuilder.createQuery(TerminalGroupDesktopCountDTO.class);
        Root<ViewTerminalGroupDesktopRelatedEntity> root = query.from(ViewTerminalGroupDesktopRelatedEntity.class);

        Expression<?> groupByExp = root.get(DBConstants.TERMINAL_GROUP_ID);
        // 计算被其他交付组使用的数量
        DataSourceTypeEnum dataSourceType = getGroupDesktopCountDTO.getDataSourceType();
        AppDeliveryTypeEnum appDeliveryType = getGroupDesktopCountDTO.getAppDeliveryType();
        // 只有 应用磁盘交付组 才需要提示该云桌面被其他交付组使用【不可选状态】
        if (appDeliveryType == AppDeliveryTypeEnum.APP_DISK && dataSourceType == DataSourceTypeEnum.DELIVERY_GROUP) {
            Subquery<ViewUamDeliveryObjectEntity> subquery = query.subquery(ViewUamDeliveryObjectEntity.class);
            Root<ViewUamDeliveryObjectEntity> subRoot = subquery.from(ViewUamDeliveryObjectEntity.class);
            subquery.select(subRoot);
            subquery.where(criteriaBuilder.equal(root.get(DBConstants.CLOUD_DESKTOP_ID), subRoot.get(DBConstants.CLOUD_DESKTOP_ID)),
                    criteriaBuilder.equal(subRoot.get(DBConstants.APP_DELIVERY_TYPE), AppDeliveryTypeEnum.APP_DISK));

            Expression<Long> notUsedCount =
                    criteriaBuilder.sum(criteriaBuilder.<Long>selectCase().when(criteriaBuilder.exists(subquery), 0L).otherwise(1L));

            Expression<Long> usedCount =
                    criteriaBuilder.sum(criteriaBuilder.<Long>selectCase().when(criteriaBuilder.exists(subquery), 1L).otherwise(0L));

            query.multiselect(groupByExp, notUsedCount, usedCount);
        } else {
            query.multiselect(groupByExp, criteriaBuilder.count(root), criteriaBuilder.literal(0L));
        }


        Predicate conjunction = criteriaBuilder.conjunction();

        // 只允许添加应用分层
        Predicate desktopTypePredicate = criteriaBuilder.equal(root.get(DBConstants.DESKTOP_TYPE), CbbCloudDeskPattern.PERSONAL);
        conjunction.getExpressions().add(desktopTypePredicate);

        // 只允许可用的桌面状态可选
        conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.DESK_STATE)).value(CbbCloudDeskState.AVAILABLE_CLOUD_DESK_STATE));

        // 应用磁盘只能选择相同的镜像模板
        UUID imageTemplateId = getGroupDesktopCountDTO.getImageTemplateId();
        if (Objects.nonNull(imageTemplateId)) {
            conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.IMAGE_TEMPLATE_ID)).value(imageTemplateId));
        }

        UUID filterGroupId = getGroupDesktopCountDTO.getFilterGroupId();
        // 忽略掉已经被自身添加的云桌面
        if (dataSourceType == DataSourceTypeEnum.DELIVERY_GROUP) {
            if (Objects.nonNull(filterGroupId)) {
                Subquery<ViewUamDeliveryObjectEntity> subquery = query.subquery(ViewUamDeliveryObjectEntity.class);
                Root<ViewUamDeliveryObjectEntity> subRoot = subquery.from(ViewUamDeliveryObjectEntity.class);
                subquery.select(subRoot);
                subquery.where(criteriaBuilder.equal(root.get(DBConstants.CLOUD_DESKTOP_ID), subRoot.get(DBConstants.CLOUD_DESKTOP_ID)),
                        criteriaBuilder.equal(subRoot.get(DBConstants.DELIVERY_GROUP_ID), filterGroupId));
                conjunction.getExpressions().add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));
            }
        }

        if (dataSourceType == DataSourceTypeEnum.DELIVERY_TEST) {
            if (Objects.nonNull(filterGroupId)) {
                Subquery<ViewUamAppTestDesktopEntity> subQuery = query.subquery(ViewUamAppTestDesktopEntity.class);
                Root<ViewUamAppTestDesktopEntity> subRoot = subQuery.from(ViewUamAppTestDesktopEntity.class);
                subQuery.select(subRoot);
                subQuery.where(criteriaBuilder.equal(root.get(DBConstants.CLOUD_DESKTOP_ID), subRoot.get(DBConstants.DESK_ID)),
                        criteriaBuilder.equal(subRoot.get(DBConstants.TEST_ID), filterGroupId));
                conjunction.getExpressions().add(criteriaBuilder.not(criteriaBuilder.exists(subQuery)));
            }
        }

        CbbOsType cbbOsType = getGroupDesktopCountDTO.getOsType();
        if (Objects.nonNull(cbbOsType)) {
            Predicate osTypePredicate = criteriaBuilder.equal(root.get(DBConstants.OS_TYPE), cbbOsType);
            conjunction.getExpressions().add(osTypePredicate);
        }

        String osVersion = getGroupDesktopCountDTO.getOsVersion();
        if (StringUtils.hasText(osVersion)) {
            Predicate osTypePredicate = criteriaBuilder.equal(root.get(DBConstants.OS_VERSION), osVersion);
            conjunction.getExpressions().add(osTypePredicate);
        }

        CbbImageType criteriaBuilderImageType = getGroupDesktopCountDTO.getCbbImageType();
        if (Objects.nonNull(criteriaBuilderImageType)) {
            Predicate criteriaBuilderImageTypePredicate = criteriaBuilder.equal(root.get(DBConstants.CBB_IMAGE_TYPE), criteriaBuilderImageType);
            conjunction.getExpressions().add(criteriaBuilderImageTypePredicate);
        }

        if (Objects.nonNull(cbbOsType) || Objects.nonNull(criteriaBuilderImageType)) {
            // 存在应用磁盘，则只能筛选 win7 win10的操作系统
            conjunction.getExpressions()
                    .add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).value(CbbOsType.APP_SOFTWARE_PACKAGE_WINDOWS_OS_TYPE_LIST));
        } else {
            OsPlatform osPlatform = getGroupDesktopCountDTO.getOsPlatform();
            //根据交付组的操作系统类型筛选匹配的桌面
            if (osPlatform == OsPlatform.WINDOWS) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).value(CbbOsType.WINDOWS_OS_TYPE_LIST));
            }
            if (osPlatform == OsPlatform.LINUX) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).
                        value(CbbOsType.APP_PUSH_PACKAGE_LINUX_OS_TYPE_LIST));
            }
        }

        // 权限处理
        if (!Boolean.TRUE.equals(getGroupDesktopCountDTO.getHasAllPermission())) {
            Subquery<AdminDataPermissionEntity> subquery = query.subquery(AdminDataPermissionEntity.class);
            Root<AdminDataPermissionEntity> subRoot = subquery.from(AdminDataPermissionEntity.class);
            subquery.select(subRoot);
            subquery.where(
                    criteriaBuilder.equal(root.get(DBConstants.TERMINAL_GROUP_ID).as(String.class), subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                    criteriaBuilder.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.TERMINAL_GROUP),
                    criteriaBuilder.equal(subRoot.get(DBConstants.ADMIN_ID), getGroupDesktopCountDTO.getAdminId()));
            conjunction.getExpressions().add(criteriaBuilder.exists(subquery));
        }

        query.where(conjunction);
        query.groupBy(groupByExp);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Page<ViewTerminalGroupDesktopRelatedEntity> pageTerminalGroupDesktopRelated(
            TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO, Pageable pageable) {
        Assert.notNull(taskSearchGroupDesktopRelatedDTO, "taskSearchGroupDesktopRelatedDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");

        Specification<ViewTerminalGroupDesktopRelatedEntity> specification = (root, query, criteriaBuilder) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(criteriaBuilder, "CriteriaBuilder is null");

            Predicate conjunction = criteriaBuilder.conjunction();

            // 只允许可用的桌面状态可选
            conjunction.getExpressions()
                    .add(criteriaBuilder.in(root.get(DBConstants.DESK_STATE)).value(CbbCloudDeskState.AVAILABLE_CLOUD_DESK_STATE));

            // 默认支持windows、KYLIN、UOS操作系统
            conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).value(getSupportOsType()));

            List<CbbCloudDeskState> deskStateList = taskSearchGroupDesktopRelatedDTO.getDeskStateList();
            if (!CollectionUtils.isEmpty(deskStateList)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.DESK_STATE)).value(deskStateList));
            }
            List<CloudPlatformStatus> platformStatusList = taskSearchGroupDesktopRelatedDTO.getPlatformStatusList();
            if (!CollectionUtils.isEmpty(platformStatusList)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.PLATFORM_STATUS)).value(platformStatusList));
            }

            // 点击终端组，返回可选云桌面列表
            UUID id = taskSearchGroupDesktopRelatedDTO.getGroupId();
            if (Objects.nonNull(id)) {
                Predicate idPredicate = criteriaBuilder.equal(root.get(DBConstants.TERMINAL_GROUP_ID), id);
                conjunction.getExpressions().add(idPredicate);
            }

            Predicate disjunction = criteriaBuilder.disjunction();

            String searchName = taskSearchGroupDesktopRelatedDTO.getSearchName();
            // terminalName 和 desktopName 采用模糊查询，忽略大小写
            if (StringUtils.hasText(searchName)) {

                Predicate terminalNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(DBConstants.TERMINAL_NAME)),
                        DBConstants.LIKE + searchName.toLowerCase() + DBConstants.LIKE);
                disjunction.getExpressions().add(terminalNamePredicate);

                Predicate desktopNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(DBConstants.DESKTOP_NAME)),
                        DBConstants.LIKE + searchName.toLowerCase() + DBConstants.LIKE);
                disjunction.getExpressions().add(desktopNamePredicate);
            }

            // 权限处理
            if (!Boolean.TRUE.equals(taskSearchGroupDesktopRelatedDTO.getHasAllPermission())) {
                Subquery<AdminDataPermissionEntity> subquery = query.subquery(AdminDataPermissionEntity.class);
                Root<AdminDataPermissionEntity> subRoot = subquery.from(AdminDataPermissionEntity.class);
                subquery.select(subRoot);
                subquery.where(
                        criteriaBuilder.equal(root.get(DBConstants.TERMINAL_GROUP_ID).as(String.class), subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                        criteriaBuilder.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.TERMINAL_GROUP),
                        criteriaBuilder.equal(subRoot.get(DBConstants.ADMIN_ID), taskSearchGroupDesktopRelatedDTO.getAdminId()));
                conjunction.getExpressions().add(criteriaBuilder.exists(subquery));
            }

            if (!disjunction.getExpressions().isEmpty()) {
                conjunction.getExpressions().add(disjunction);
            }

            return conjunction;
        };

        return viewTerminalGroupDesktopRelatedDAO.findAll(specification, pageable);
    }

    @Override
    public List<TerminalGroupDesktopCountDTO> listTerminalGroupDesktopCount(TaskGetGroupDesktopCountDTO taskGetGroupDesktopCountDTO) {
        Assert.notNull(taskGetGroupDesktopCountDTO, "taskGetGroupDesktopCountDTO must not be null");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TerminalGroupDesktopCountDTO> query = criteriaBuilder.createQuery(TerminalGroupDesktopCountDTO.class);
        Root<ViewTerminalGroupDesktopRelatedEntity> root = query.from(ViewTerminalGroupDesktopRelatedEntity.class);

        Expression<?> groupByExp = root.get(DBConstants.TERMINAL_GROUP_ID);
        query.multiselect(groupByExp, criteriaBuilder.count(root), criteriaBuilder.literal(0L));

        Predicate conjunction = criteriaBuilder.conjunction();
        // 只允许可用的桌面状态可选
        conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.DESK_STATE)).value(CbbCloudDeskState.AVAILABLE_CLOUD_DESK_STATE));

        // 默认支持windows、KYLIN、UOS操作系统
        conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).value(getSupportOsType()));

        // 权限处理
        if (!Boolean.TRUE.equals(taskGetGroupDesktopCountDTO.getHasAllPermission())) {
            Subquery<AdminDataPermissionEntity> subquery = query.subquery(AdminDataPermissionEntity.class);
            Root<AdminDataPermissionEntity> subRoot = subquery.from(AdminDataPermissionEntity.class);
            subquery.select(subRoot);
            subquery.where(
                    criteriaBuilder.equal(root.get(DBConstants.TERMINAL_GROUP_ID).as(String.class), subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                    criteriaBuilder.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.TERMINAL_GROUP),
                    criteriaBuilder.equal(subRoot.get(DBConstants.ADMIN_ID), taskGetGroupDesktopCountDTO.getAdminId()));
            conjunction.getExpressions().add(criteriaBuilder.exists(subquery));
        }

        query.where(conjunction);
        query.groupBy(groupByExp);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<ViewTerminalGroupDesktopRelatedEntity> listTerminalGroupDesktopRelated(
            TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO) {
        Assert.notNull(taskSearchGroupDesktopRelatedDTO, "taskSearchGroupDesktopRelatedDTO must not be null");

        Specification<ViewTerminalGroupDesktopRelatedEntity> specification = (root, query, criteriaBuilder) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(criteriaBuilder, "CriteriaBuilder is null");

            Predicate conjunction = criteriaBuilder.conjunction();

            // 只允许可用的桌面状态可选
            conjunction.getExpressions()
                    .add(criteriaBuilder.in(root.get(DBConstants.DESK_STATE)).value(CbbCloudDeskState.AVAILABLE_CLOUD_DESK_STATE));

            // 默认支持windows、KYLIN、UOS操作系统
            conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.OS_TYPE)).value(getSupportOsType()));

            // 点击终端组，返回可选云桌面列表,已包含权限筛选。
            List<UUID> terminalGroupIdList = taskSearchGroupDesktopRelatedDTO.getGroupIdList();
            if (!CollectionUtils.isEmpty(terminalGroupIdList)) {
                conjunction.getExpressions().add(criteriaBuilder.in(root.get(DBConstants.TERMINAL_GROUP_ID)).value(terminalGroupIdList));
            }

            return conjunction;
        };

        return viewTerminalGroupDesktopRelatedDAO.findAll(specification);
    }

    private List<CbbOsType> getSupportOsType() {
        List<CbbOsType> supportOsTypeList = new ArrayList<>(CbbOsType.WINDOWS_OS_TYPE_LIST);
        supportOsTypeList.add(CbbOsType.KYLIN_64);
        supportOsTypeList.add(CbbOsType.UOS_64);
        return supportOsTypeList;
    }


}
