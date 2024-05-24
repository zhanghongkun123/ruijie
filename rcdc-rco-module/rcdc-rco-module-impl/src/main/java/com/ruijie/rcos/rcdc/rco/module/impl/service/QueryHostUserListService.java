package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewHostUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewHostUserEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Description: 查询用户消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/10
 *
 * @author Jarman
 */
@Service
public class QueryHostUserListService extends AbstractPageQueryTemplate<RcoViewHostUserEntity> {

    private static final String ID = "id";

    @Autowired
    private RcoViewHostUserDAO rcoViewHostUserDAO;



    @Override
    protected Pageable getPageRequest(PageSearchRequest request, String defaultSortField, Direction defaultDirection) {
        Assert.notNull(defaultSortField, "请指定默认排序字段");
        Assert.notNull(defaultDirection, "请指定默认排序方式");
        int page = request.getPage();
        int limit = request.getLimit();
        List<Sort.Order> orderList = new ArrayList<>();
        // 传入没有指定排序字段，使用id和默认字段进行排序
        if (ArrayUtils.isEmpty(request.getSortArr())) {
            orderList.add(new Sort.Order(defaultDirection, defaultSortField));
            // 加入ID作为排序条件，防止分页数据重复
            orderList.add(new Sort.Order(Direction.DESC, ID));
            return PageRequest.of(page, limit, Sort.by(orderList));
        }
        // 有指定排序字段和排方式，需要加上id作为排序条件防止分页重复
        Arrays.stream(Objects.requireNonNull(request.getSortArr())).forEach(sortVO -> {
            Direction directionEnum = Direction.fromString(sortVO.getDirection().name());
            Sort.Order order = new Sort.Order(directionEnum, sortVO.getSortField());
            orderList.add(order);
        });
        orderList.add(new Sort.Order(Direction.DESC, ID));
        Sort sort = Sort.by(orderList);
        return PageRequest.of(page, limit, sort);
    }


    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("userName", "realName", "userDescription");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Direction.DESC);
    }

    @Override
    protected Page<RcoViewHostUserEntity> find(Specification<RcoViewHostUserEntity> specification, Pageable pageable) {
        if (specification == null) {
            return rcoViewHostUserDAO.findAll(pageable);
        }
        return rcoViewHostUserDAO.findAll(specification, pageable);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //父类实现了
    }
}
