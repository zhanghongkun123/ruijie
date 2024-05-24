package com.ruijie.rcos.rcdc.rco.module.impl.printer.service;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.dao.PrinterConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.entity.PrinterConfigEntity;
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
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/16
 *
 * @author chenjiehui
 */
@Service
public class QueryPrinterConfigServiceImpl extends AbstractPageQueryTemplate<PrinterConfigEntity> {

    @Autowired
    private PrinterConfigDAO printerConfigDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("configName", "printerModel");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        // Do nothing because of X and Y.
    }

    @Override
    protected Page<PrinterConfigEntity> find(Specification<PrinterConfigEntity> specification, Pageable pageable) {
        if (specification == null) {
            return printerConfigDAO.findAll(pageable);
        }
        return printerConfigDAO.findAll(specification, pageable);
    }
}
