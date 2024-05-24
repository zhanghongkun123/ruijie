package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import static org.junit.Assert.assertEquals;

import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import java.util.ArrayList;
import java.util.List;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * Description: QueryCabinetServiceImpl单元测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/7 19:02
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class QueryCabinetServiceImplTest {

    @Tested
    private QueryCabinetServiceImpl queryCabinetService;

    @Injectable
    private CabinetDAO cabinetDAO;

    /**
     * 测试getSearchColumn
     */
    @Test
    public void testGetSearchColumn() {
        List<String> columnList = new ArrayList<>();
        columnList.add("name");
        assertEquals(columnList, queryCabinetService.getSearchColumn());
    }

    /**
     * 测试getDefaultDataSort
     */
    @Test
    public void testGetDefaultDataSort() {
        DefaultDataSort sort = queryCabinetService.getDefaultDataSort();
        assertEquals("createTime", sort.getSortField());
        assertEquals(Sort.Direction.DESC, sort.getDirection());
    }

    /**
     * 测试mappingField()
     */
    @Test
    public void testMappingField() {
        new Verifications() {
            {
                queryCabinetService.mappingField((EntityFieldMapper) any);
                times = 1;
            }
        };
    }

    /**
     * 测试find,Specification为null
     *
     * @param pageable mock对象
     * @param page mock对象
     */
    @Test
    public void testFindSpecificationIsNull(
        @Mocked Pageable pageable,
        @Mocked Page<CabinetEntity> page) {

        new Expectations() {
            {
                cabinetDAO.findAll(pageable);
                result = page;
            }
        };
        Page<CabinetEntity> page1 = queryCabinetService.find(null, pageable);
        assertEquals(page, page1);

        new Verifications() {
            {
                cabinetDAO.findAll(pageable);
                times = 1;
            }
        };
    }

    /**
     * 测试find,Specification不为null
     *
     * @param specification mock对象
     * @param pageable mock对象
     * @param page mock对象
     */
    @Test
    public void testFindSpecificationNotNull(
        @Mocked Specification<CabinetEntity> specification,
        @Mocked Pageable pageable,
        @Mocked Page<CabinetEntity> page) {

        new Expectations() {
            {
                cabinetDAO.findAll(specification, pageable);
                result = page;
            }
        };
        Page<CabinetEntity> page1 = queryCabinetService.find(specification, pageable);
        assertEquals(page, page1);

        new Verifications() {
            {
                cabinetDAO.findAll(pageable);
                times = 0;
                cabinetDAO.findAll(specification, pageable);
                times = 1;
            }
        };
    }

}
