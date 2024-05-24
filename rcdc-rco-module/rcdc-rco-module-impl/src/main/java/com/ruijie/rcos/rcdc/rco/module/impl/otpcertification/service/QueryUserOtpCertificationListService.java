package com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.service;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dao.RcoViewUserOtpCertificationDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.entity.RcoViewUserOtpCertificationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Description: 动态口令查询服务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author lihengjing
 */
@Service("rcoQueryUserOtpCertificationListService")
public class QueryUserOtpCertificationListService extends AbstractPageQueryTemplate<RcoViewUserOtpCertificationEntity> {

    @Autowired
    private RcoViewUserOtpCertificationDAO rcoViewUserOtpCertificationDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("userName", "userGroupName");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("updateTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //Doing nothing because of waiting someone to complete it...
    }

    @Override
    protected Page<RcoViewUserOtpCertificationEntity> find(Specification<RcoViewUserOtpCertificationEntity> specification,
                                                           Pageable pageable) {
        if (specification == null) {
            return rcoViewUserOtpCertificationDAO.findAll(pageable);
        }
        return rcoViewUserOtpCertificationDAO.findAll(specification, pageable);
    }
}
