package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryImageTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalWithImageDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalWithImageEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryImageTerminalListService;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;

/**
 * Description: 终端关于镜像信息视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月12日
 *
 * @author ypp
 */

@Service
public class QueryImageTerminalListServiceImpl extends  AbstractPageQueryTemplate<ViewTerminalWithImageEntity>
        implements QueryImageTerminalListService {

    @Autowired
    private ViewTerminalWithImageDAO viewTerminalWithImageDAO;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private final static String IMAGE_ID = "imageId";

    private final static String CPU_ARCH_TYPE = "cpuArchType";

    @Override
    public Page<ViewTerminalWithImageEntity> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "request must not be null");
        setSortCondition(request);
        return super.pageQuery(request, ViewTerminalWithImageEntity.class);
    }

    @Override
    public ViewTerminalWithImageEntity getTerminalWithImage(String imageId, String terminalId) {
        Assert.notNull(imageId, "imageId must not be null");
        Assert.hasText(terminalId, "terminalId must not be null");
        return viewTerminalWithImageDAO.findByImageIdAndTerminalId(imageId, terminalId);
    }


    private void setSortCondition(PageSearchRequest request) {
        if (ArrayUtils.isEmpty(request.getSortArr())) {
            List<Sort> sortList = new ArrayList<>();

            MatchEqual[] matchEqualsArr = request.getMatchEqualArr();
            Optional<MatchEqual> optional = Arrays.stream(matchEqualsArr).filter(item -> "imageId".equals(item.getName())).findFirst();
            if (optional.isPresent()) {
                Object[] imageIdValueArr = optional.get().getValueArr();

                if (Objects.nonNull(imageIdValueArr) && imageIdValueArr.length > 0) {

                    String imageId = String.valueOf(imageIdValueArr[0]);
                    List<CbbQueryImageTypeDTO> cbbQueryImageTypeList = cbbImageTemplateMgmtAPI.queryImageType(UUID.fromString(imageId));
                    if (!CollectionUtils.isEmpty(cbbQueryImageTypeList) && cbbQueryImageTypeList.get(0).getCbbImageType() == CbbImageType.VOI) {
                        buildSortArr("downloadState", Sort.Direction.ASC, sortList);
                        buildSortArr("downloadUpdateTime", Sort.Direction.DESC, sortList);
                    }
                }
            }
          
            buildSortArr("installDriveState", Sort.Direction.ASC, sortList);
            buildSortArr("terminalState", Sort.Direction.DESC, sortList);
            buildSortArr("terminalName", Sort.Direction.ASC, sortList);
            request.setSortArr((sortList.toArray(new Sort[0])));
        }
    }

    private void buildSortArr(String sortField, Sort.Direction asc,
            List<Sort> sortList) {
        Sort downloadStateSort = new Sort();
        downloadStateSort.setSortField(sortField);
        downloadStateSort.setDirection(asc);
        sortList.add(downloadStateSort);
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("downloadState", "installDriveState", "ip", "terminalNameUpper", "terminalState", "macAddr", "imageId", "productId");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("downloadState", org.springframework.data.domain.Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {

    }

    @Override
    protected Page<ViewTerminalWithImageEntity> find(Specification<ViewTerminalWithImageEntity> specification, Pageable pageable) {
        if (specification == null) {
            return viewTerminalWithImageDAO.findAll(pageable);
        }
        return viewTerminalWithImageDAO.findAll(specification, pageable);
    }

    private MatchEqual[] addMatchEqualArr(MatchEqual[] matchEqualsArr, String sortField, Object value) {
        MatchEqual matchEqual = new MatchEqual();
        matchEqual.setName(sortField);
        Object[] valueArr = {value};
        matchEqual.setValueArr(valueArr);
        List<MatchEqual> matchEqualList = new ArrayList<>(Arrays.asList(matchEqualsArr));
        matchEqualList.add(matchEqual);

        MatchEqual[] newMarchEqualArr = new MatchEqual[matchEqualList.size()];
        return matchEqualList.toArray(newMarchEqualArr);
    }
}
