package com.ruijie.rcos.rcdc.rco.module.impl.datasync.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchDataSyncLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.datasync.dao.DataSyncLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.entity.DataSyncLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.service.DataSyncLogService;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.Objects;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/25 16:20
 *
 * @author coderLee23
 */
@Service
public class DataSyncLogServiceImpl implements DataSyncLogService {

    @Autowired
    private DataSyncLogDAO dataSyncLogDAO;

    @Override
    public void saveDataSyncLog(String context) {
        Assert.hasText(context, "context must not be null or empty");
        DataSyncLogEntity dataSyncLogEntity = new DataSyncLogEntity();
        dataSyncLogEntity.setContent(context);
        dataSyncLogDAO.save(dataSyncLogEntity);
    }

    @Override
    public Page<DataSyncLogEntity> pageDataSyncLog(SearchDataSyncLogDTO searchDataSyncLogDTO, Pageable pageable) {
        Assert.notNull(searchDataSyncLogDTO, "searchDataSyncLogDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");

        Specification<DataSyncLogEntity> specification = (root, query, cb) -> {
            Assert.notNull(root, "root must not be null");
            Assert.notNull(query, "query must not be null");
            Assert.notNull(cb, "CriteriaBuilder must not be null");

            Predicate conjunction = cb.conjunction();

            Date startCreateTime = searchDataSyncLogDTO.getStartCreateTime();
            Date endCreateTime = searchDataSyncLogDTO.getEndCreateTime();
            if (Objects.nonNull(startCreateTime) && Objects.nonNull(endCreateTime)) {
                Predicate contentPredicate = cb.between(root.get("createTime"), startCreateTime, endCreateTime);
                conjunction.getExpressions().add(contentPredicate);
            }

            String content = searchDataSyncLogDTO.getContent();
            if (StringUtils.hasText(content)) {
                Predicate contentPredicate = cb.like(cb.lower(root.get("content")), "%" + content.toLowerCase() + "%");
                conjunction.getExpressions().add(contentPredicate);
            }

            return conjunction;
        };

        return dataSyncLogDAO.findAll(specification, pageable);
    }

    @Override
    public void deleteByExpireTime(Date expireTime) {
        Assert.notNull(expireTime, "expireTime must not be null");
        dataSyncLogDAO.deleteByCreateTimeLessThan(expireTime);
    }

    @Override
    public DataSyncLogEntity findOneByOffset(Long offset) {
        Assert.notNull(offset, "offset must not be null");
        return dataSyncLogDAO.findOneByOffset(offset);
    }

    @Override
    public Long count() {
        return dataSyncLogDAO.count();
    }

}
