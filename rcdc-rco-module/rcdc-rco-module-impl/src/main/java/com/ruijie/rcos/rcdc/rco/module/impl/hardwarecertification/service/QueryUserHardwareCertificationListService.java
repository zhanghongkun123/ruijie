package com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.service;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.dao.RcoViewUserHardwareCertificationDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.entity.RcoViewUserHardwareCertificationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.service.specification.UserHardCertificationPageQuerySpecification;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Description: 用户硬件特征码查询服务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
@Service("rcoQueryUserHardwareCertificationListService")
public class QueryUserHardwareCertificationListService extends AbstractPageQueryTemplate<RcoViewUserHardwareCertificationEntity> {

    @Autowired
    private RcoViewUserHardwareCertificationDAO rcoViewUserHardwareCertificationDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("userName", "terminalName", "macAddr", "featureCode");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //Doing nothing because of waiting someone to complete it...
    }

    @Override
    protected Page<RcoViewUserHardwareCertificationEntity> find(Specification<RcoViewUserHardwareCertificationEntity> specification,
                                                                Pageable pageable) {
        if (specification == null) {
            return rcoViewUserHardwareCertificationDAO.findAll(pageable);
        }
        return rcoViewUserHardwareCertificationDAO.findAll(specification, pageable);
    }

    @Override
    protected PageQuerySpecification<RcoViewUserHardwareCertificationEntity> buildSpecification(PageSearchRequest request) {
        if (StringUtils.isBlank(request.getSearchKeyword()) && ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            // 允许为空
            return null;
        } else {
            return new UserHardCertificationPageQuerySpecification(request.getSearchKeyword(), getSearchColumn(),
                    request.getMatchEqualArr(), request.getIsAnd());
        }
    }
}
