package com.ruijie.rcos.rcdc.rco.module.web.service;

import static org.junit.Assert.assertEquals;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;

import mockit.*;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/6/5
 *
 * @author nt
 */
@RunWith(SkyEngineRunner.class)
public class ExcelUtilsTest {

    /**
     * testIsUserTemplateGetRowIsNull
     *
     * @param sheet sheet
     */
    @Test
    public void testIsUserTemplateGetRowIsNull(@Mocked Sheet sheet) {

        new MockUp<Sheet>() {
            @Mock
            public Row getRow(int rowNum) {
                // for test
                return null;
            }
        };

        boolean isUserTemplate = ExcelUtils.isUserTemplate(sheet);
        assertEquals(false, isUserTemplate);
    }

    /**
     * testIsUserTemplateGetRowIsNull
     *
     * @param sheet sheet
     */
    @Test
    public void testIsUserTemplate(@Mocked Sheet sheet, @Mocked Row row, @Injectable Cell cell) {

        new MockUp<Sheet>() {
            @Mock
            public Row getRow(int rowNum) {
                // for test
                return row;
            }
        };

        new MockUp<CellUtil>() {
            @Mock
            public Cell getCell(Row row, int index) {
                return cell;
            }
        };

        new Expectations() {
            {
                cell.getStringCellValue();
                result = "user_template";
            }
        };

        boolean isUserTemplate = ExcelUtils.isUserTemplate(sheet);
        assertEquals(true, isUserTemplate);
    }


}
