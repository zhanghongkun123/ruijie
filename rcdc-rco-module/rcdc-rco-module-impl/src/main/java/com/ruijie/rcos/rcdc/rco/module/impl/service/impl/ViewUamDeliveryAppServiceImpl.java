package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.appcenter.module.def.CbbAppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchDeliveryAppDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamDeliveryAppDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryAppEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUamDeliveryAppService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/04 18:57
 *
 * @author coderLee23
 */
@Service
public class ViewUamDeliveryAppServiceImpl implements ViewUamDeliveryAppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewUamDeliveryAppServiceImpl.class);


    @Autowired
    private ViewUamDeliveryAppDAO viewUamDeliveryAppDao;

    @Override
    public Page<ViewUamDeliveryAppEntity> pageUamDeliveryApp(SearchDeliveryAppDTO searchDeliveryAppDTO, Pageable pageable) {
        Assert.notNull(searchDeliveryAppDTO, "cbbSearchDeliveryAppDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Specification<ViewUamDeliveryAppEntity> specification = (root, query, cb) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(cb, "CriteriaBuilder is null");
            // 用于添加所有查询条件
            Predicate conjunction = cb.conjunction();
            String appName = searchDeliveryAppDTO.getAppName();
            // appName 存在，则采用模糊查询
            if (StringUtils.hasText(appName)) {
                Predicate appNamePredicate = cb.like(cb.lower(root.get("appName")), "%" + appName.toLowerCase() + "%");
                conjunction.getExpressions().add(appNamePredicate);
            }

            List<AppTypeEnum> appTypeList = searchDeliveryAppDTO.getAppTypeList();
            if (!CollectionUtils.isEmpty(appTypeList)) {
                conjunction.getExpressions().add(cb.in(root.get("appType")).value(appTypeList));
            }


            List<DeliveryStatusEnum> deliveryStatusList = searchDeliveryAppDTO.getDeliveryStatusList();
            if (!CollectionUtils.isEmpty(deliveryStatusList)) {
                conjunction.getExpressions().add(cb.in(root.get("deliveryStatus")).value(deliveryStatusList));
            }

            // 处理支持 应用类型过滤
            conjunction.getExpressions().add(cb.equal(root.get("deliveryGroupId"), searchDeliveryAppDTO.getDeliveryGroupId()));

            return conjunction;
        };

        return viewUamDeliveryAppDao.findAll(specification, pageable);

    }

    @Override
    public ViewUamDeliveryAppEntity findById(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        Optional<ViewUamDeliveryAppEntity> viewUamDeliveryAppEntity = viewUamDeliveryAppDao.findById(id);
        if (!viewUamDeliveryAppEntity.isPresent()) {
            LOGGER.error("ID为{}的交付应用不存在,请核实是否存在或删除！", id);
            throw new BusinessException(CbbAppCenterBusinessKey.RCDC_UAM_DELIVERY_APP_NOT_EXIST);
        }
        return viewUamDeliveryAppEntity.get();
    }

    @Override
    public List<ViewUamDeliveryAppEntity> findAppListByGroupId(UUID groupId) throws BusinessException {
        Assert.notNull(groupId, "groupId must not be null");

        return viewUamDeliveryAppDao.findByDeliveryGroupIdOrderByAppName(groupId);
    }

    @Override
    public List<String> findAppNameListByGroupId(List<UUID> groupIdList) throws BusinessException {
        Assert.notEmpty(groupIdList, "groupIdList must not be null");
        return viewUamDeliveryAppDao.findDistinctAppNameList(groupIdList);
    }
}
