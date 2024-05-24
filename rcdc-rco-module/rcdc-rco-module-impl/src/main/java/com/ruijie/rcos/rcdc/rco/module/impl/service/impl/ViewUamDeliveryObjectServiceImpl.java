package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.criteria.Predicate;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
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
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamDeliveryObjectDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryObjectEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUamDeliveryObjectService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/05 14:41
 *
 * @author coderLee23
 */
@Service
public class ViewUamDeliveryObjectServiceImpl implements ViewUamDeliveryObjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewUamDeliveryObjectServiceImpl.class);

    @Autowired
    private ViewUamDeliveryObjectDAO viewUamDeliveryObjectDAO;

    @Override
    public Page<ViewUamDeliveryObjectEntity> pageUamDeliveryObject(SearchDeliveryObjectDTO searchDeliveryObjectDTO, Pageable pageable) {
        Assert.notNull(searchDeliveryObjectDTO, "cbbSearchDeliveryObjectDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Specification<ViewUamDeliveryObjectEntity> specification = (root, query, cb) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(cb, "CriteriaBuilder is null");
            // 用于添加所有查询条件
            String appName = searchDeliveryObjectDTO.getCloudDesktopName();
            Predicate conjunction = cb.conjunction();

            // appName 存在，则采用模糊查询
            if (StringUtils.hasText(appName)) {
                Predicate appNamePredicate = cb.like(cb.lower(root.get(DBConstants.CLOUD_DESKTOP_NAME)), "%" + appName.toLowerCase() + "%");
                conjunction.getExpressions().add(appNamePredicate);
            }

            List<CbbCloudDeskState> deskStateList = searchDeliveryObjectDTO.getDeskStateList();
            if (!CollectionUtils.isEmpty(deskStateList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.DESK_STATE)).value(deskStateList));
            }

            List<DeliveryStatusEnum> deliveryStatusList = searchDeliveryObjectDTO.getDeliveryStatusList();
            if (!CollectionUtils.isEmpty(deliveryStatusList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.DELIVERY_STATUS)).value(deliveryStatusList));
            }
            List<CloudPlatformStatus> platformStatusList = searchDeliveryObjectDTO.getPlatformStatusList();
            if (!CollectionUtils.isEmpty(platformStatusList)) {
                conjunction.getExpressions().add(cb.in(root.get(DBConstants.PLATFORM_STATUS)).value(platformStatusList));
            }

            // 处理支持 应用类型过滤
            conjunction.getExpressions().add(cb.equal(root.get(DBConstants.DELIVERY_GROUP_ID), searchDeliveryObjectDTO.getDeliveryGroupId()));
            return conjunction;
        };

        return viewUamDeliveryObjectDAO.findAll(specification, pageable);
    }

    @Override
    public ViewUamDeliveryObjectEntity findById(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        Optional<ViewUamDeliveryObjectEntity> viewUamDeliveryObjectEntity = viewUamDeliveryObjectDAO.findById(id);
        if (!viewUamDeliveryObjectEntity.isPresent()) {
            LOGGER.error("ID为{}的交付对象不存在,请核实是否存在或删除！", id);
            throw new BusinessException(CbbAppCenterBusinessKey.RCDC_UAM_DELIVERY_OBJECT_NOT_EXIST);
        }
        return viewUamDeliveryObjectEntity.get();
    }

    @Override
    public List<ViewUamDeliveryObjectEntity> findByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null");
        return viewUamDeliveryObjectDAO.findByCloudDesktopId(deskId);
    }

    @Override
    public List<ViewUamDeliveryObjectEntity> findByCloudDesktopIdAndAppDeliveryType(UUID deskId, AppDeliveryTypeEnum appDeliveryType) {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(appDeliveryType, "appDeliveryType must not be null");
        return viewUamDeliveryObjectDAO.findByCloudDesktopIdAndAppDeliveryType(deskId, appDeliveryType);
    }

    @Override
    public Boolean existsByAppDeliveryTypeAndCloudDesktopIdIn(AppDeliveryTypeEnum appDeliveryType, List<UUID> cloudDesktopIdList) {
        Assert.notNull(appDeliveryType, "appDeliveryType must not be null");
        Assert.notEmpty(cloudDesktopIdList, "cloudDesktopIdList must not be empty");
        return viewUamDeliveryObjectDAO.existsByAppDeliveryTypeAndCloudDesktopIdIn(appDeliveryType, cloudDesktopIdList);
    }

}
