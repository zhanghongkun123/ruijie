package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.def.distribution.request.SearchDistributeTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDistributeTaskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDistributeTaskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewDistributeTaskService;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/04 11:44
 *
 * @author coderLee23
 */
@Service
public class ViewDistributeTaskServiceImpl implements ViewDistributeTaskService {

    @Autowired
    private ViewDistributeTaskDAO viewDistributeTaskDAO;


    @Override
    public Page<ViewDistributeTaskEntity> pageDistributeTask(SearchDistributeTaskDTO searchDistributeTaskDTO, Pageable pageable) {
        Assert.notNull(searchDistributeTaskDTO, "searchDistributeTaskDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Specification<ViewDistributeTaskEntity> specification = (root, query, criteriaBuilder) -> {
            Assert.notNull(root, "root must not be null");
            Assert.notNull(query, "query must not be null");
            Assert.notNull(criteriaBuilder, "CriteriaBuilder must not be null");

            Predicate conjunction = criteriaBuilder.conjunction();

            String taskName = searchDistributeTaskDTO.getTaskName();
            if (StringUtils.hasText(taskName)) {
                Predicate taskNamPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(DBConstants.TASK_NAME)),
                        DBConstants.LIKE + taskName.toLowerCase() + DBConstants.LIKE);
                conjunction.getExpressions().add(taskNamPredicate);
            }

            // 权限处理
            if (!Boolean.TRUE.equals(searchDistributeTaskDTO.getHasAllPermission())) {
                Subquery<AdminDataPermissionEntity> subquery = query.subquery(AdminDataPermissionEntity.class);
                Root<AdminDataPermissionEntity> subRoot = subquery.from(AdminDataPermissionEntity.class);
                subquery.select(subRoot);
                subquery.where(criteriaBuilder.equal(root.get(DBConstants.ID).as(String.class), subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                        criteriaBuilder.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.FILE_DISTRIBUTION),
                        criteriaBuilder.equal(subRoot.get(DBConstants.ADMIN_ID), searchDistributeTaskDTO.getAdminId()));
                conjunction.getExpressions().add(criteriaBuilder.exists(subquery));
            }

            return conjunction;
        };

        return viewDistributeTaskDAO.findAll(specification, pageable);
    }
}
