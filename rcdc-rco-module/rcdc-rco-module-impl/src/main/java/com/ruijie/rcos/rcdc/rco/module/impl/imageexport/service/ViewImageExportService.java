package com.ruijie.rcos.rcdc.rco.module.impl.imageexport.service;

import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ImageExportDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.imageexport.entity.ViewImageExportEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/19 16:21
 *
 * @author liuwang1
 */
@Service
public class ViewImageExportService extends AbstractPageQueryTemplate<ViewImageExportEntity> {

    @Autowired
    private ImageExportDAO imageExportDAO;

    @Override
    protected List<String> getSearchColumn() {
        return new ArrayList<>();
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("exportTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {

    }

    @Override
    protected Page<ViewImageExportEntity> find(Specification<ViewImageExportEntity> specification, Pageable pageable) {
        if (specification == null) {
            return imageExportDAO.findAll(pageable);
        }
        return imageExportDAO.findAll(specification, pageable);
    }
}
