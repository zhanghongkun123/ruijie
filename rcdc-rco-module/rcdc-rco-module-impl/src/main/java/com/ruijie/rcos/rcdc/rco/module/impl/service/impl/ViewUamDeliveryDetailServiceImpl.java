package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.appcenter.module.def.CbbAppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.ProgressStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryAppDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryObjectDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamDeliveryDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryDetailEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUamDeliveryDetailService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/06 22:27
 *
 * @author coderLee23
 */
@Service
public class ViewUamDeliveryDetailServiceImpl implements ViewUamDeliveryDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewUamDeliveryDetailServiceImpl.class);

    @Autowired
    private ViewUamDeliveryDetailDAO viewUamDeliveryDetailDao;


    @Override
    public Page<ViewUamDeliveryDetailEntity> pageUamDeliveryAppDetail(SearchDeliveryAppDetailDTO searchDeliveryAppDetailDTO, Pageable pageable) {
        Assert.notNull(searchDeliveryAppDetailDTO, "cbbSearchDeliveryAppDetailDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Specification<ViewUamDeliveryDetailEntity> specification = (root, query, cb) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(cb, "CriteriaBuilder is null");
            // 用于添加所有查询条件
            Predicate conjunction = cb.conjunction();

            String cloudDesktopName = searchDeliveryAppDetailDTO.getCloudDesktopName();
            // appName 存在，则采用模糊查询
            if (StringUtils.hasText(cloudDesktopName)) {
                Predicate appNamePredicate = cb.like(cb.lower(root.get(DBConstants.CLOUD_DESKTOP_NAME)), "%" + cloudDesktopName.toLowerCase() + "%");
                conjunction.getExpressions().add(appNamePredicate);
            }

            List<CbbCloudDeskState> deskStateList = searchDeliveryAppDetailDTO.getDeskStateList();
            if (!CollectionUtils.isEmpty(deskStateList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.DESK_STATE)).value(deskStateList));
            }

            List<DeliveryStatusEnum> deliveryStatusList = searchDeliveryAppDetailDTO.getDeliveryStatusList();
            if (!CollectionUtils.isEmpty(deliveryStatusList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.DELIVERY_STATUS)).value(deliveryStatusList));
            }

            List<ProgressStatusEnum> progressStatusList = searchDeliveryAppDetailDTO.getProgressStatusList();
            if (!CollectionUtils.isEmpty(progressStatusList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.PROGRESS_STATUS)).value(progressStatusList));
            }


            // 处理支持 应用类型过滤
            conjunction.getExpressions().add(cb.equal(root.get(DBConstants.APP_ID), searchDeliveryAppDetailDTO.getAppId()));
            conjunction.getExpressions().add(cb.equal(root.get(DBConstants.DELIVERY_GROUP_ID), searchDeliveryAppDetailDTO.getDeliveryGroupId()));

            return conjunction;
        };

        return viewUamDeliveryDetailDao.findAll(specification, pageable);

    }

    @Override
    public Page<ViewUamDeliveryDetailEntity> pageUamDeliveryObjectDetail(SearchDeliveryObjectDetailDTO searchDeliveryObjectDetailDTO,
            Pageable pageable) {
        Assert.notNull(searchDeliveryObjectDetailDTO, "cbbSearchDeliveryObjectDetailDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Specification<ViewUamDeliveryDetailEntity> specification = (root, query, cb) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(cb, "CriteriaBuilder is null");
            // 用于添加所有查询条件
            Predicate conjunction = cb.conjunction();
            String appName = searchDeliveryObjectDetailDTO.getAppName();
            // appName 存在，则采用模糊查询
            if (StringUtils.hasText(appName)) {
                Predicate appNamePredicate = cb.like(cb.lower(root.get(DBConstants.APP_NAME)), "%" + appName.toLowerCase() + "%");
                conjunction.getExpressions().add(appNamePredicate);
            }

            List<AppTypeEnum> appTypeList = searchDeliveryObjectDetailDTO.getAppTypeList();
            if (!CollectionUtils.isEmpty(appTypeList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.APP_TYPE)).value(appTypeList));
            }

            List<DeliveryStatusEnum> deliveryStatusList = searchDeliveryObjectDetailDTO.getDeliveryStatusList();
            if (!CollectionUtils.isEmpty(deliveryStatusList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.DELIVERY_STATUS)).value(deliveryStatusList));
            }

            List<ProgressStatusEnum> progressStatusList = searchDeliveryObjectDetailDTO.getProgressStatusList();
            if (!CollectionUtils.isEmpty(progressStatusList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.PROGRESS_STATUS)).value(progressStatusList));
            }

            // 处理支持 应用类型过滤
            conjunction.getExpressions().add(cb.equal(root.get(DBConstants.CLOUD_DESKTOP_ID), searchDeliveryObjectDetailDTO.getCloudDesktopId()));
            conjunction.getExpressions().add(cb.equal(root.get(DBConstants.DELIVERY_GROUP_ID), searchDeliveryObjectDetailDTO.getDeliveryGroupId()));

            return conjunction;
        };

        return viewUamDeliveryDetailDao.findAll(specification, pageable);
    }

    @Override
    public ViewUamDeliveryDetailEntity getById(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        Optional<ViewUamDeliveryDetailEntity> viewUamDeliveryDetailEntity = viewUamDeliveryDetailDao.findById(id);
        if (!viewUamDeliveryDetailEntity.isPresent()) {
            LOGGER.error("ID为{}的交付详情不存在,请核实是否存在或删除！", id);
            throw new BusinessException(CbbAppCenterBusinessKey.RCDC_UAM_DELIVERY_DETAIL_NOT_EXIST);
        }
        return viewUamDeliveryDetailEntity.get();
    }
}
