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

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUserSyncDataDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserSyncDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUserSyncDataService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 11:57
 *
 * @author coderLee23
 */
@Service
public class ViewUserSyncDataServiceImpl implements ViewUserSyncDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewUserSyncDataServiceImpl.class);

    @Autowired
    private ViewUserSyncDataDAO viewUserSyncDataDAO;


    @Override
    public Page<ViewUserSyncDataEntity> pageUserSyncData(List<IacUserTypeEnum> userTypeList, Pageable pageable) {
        Assert.notEmpty(userTypeList, "userTypeList must not be null or empty");
        Assert.notNull(pageable, "pageable must not be null");

        Specification<ViewUserSyncDataEntity> specification = (root, query, cb) -> {
            Assert.notNull(root, "root must not be null");
            Assert.notNull(query, "query must not be null");
            Assert.notNull(cb, "CriteriaBuilder must not be null");

            Predicate conjunction = cb.conjunction();
            // 不同步访客用户
            conjunction.getExpressions().add(cb.in(root.get("userType")).value(userTypeList));

            return conjunction;
        };

        return viewUserSyncDataDAO.findAll(specification, pageable);
    }

    @Override
    public Optional<ViewUserSyncDataEntity> findById(UUID id) {
        Assert.notNull(id, "id must not be null");
        return viewUserSyncDataDAO.findById(id);
    }
}
