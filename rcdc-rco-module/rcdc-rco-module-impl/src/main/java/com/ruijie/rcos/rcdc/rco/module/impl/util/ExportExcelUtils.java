package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: 导出用户数据到Excel
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/04/12
 *
 * @author guoyongxin
 */
public class ExportExcelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcelUtils.class);

    private static final  Short DEFAULT_COLUMNS_WIDTH = 7000;

    private ExportExcelUtils() {
    }

    /**
     * 生成excel文档
     *
     * @param data           生成excel数据
     * @param outputFilePath 输出路径
     * @param headerClass    头部信息
     * @param sheetName sheet名字
     * @param infoArr 附加信息
     * @throws Exception Exception
     */
    public static void generateTotalExcel(List<? extends Object> data, String outputFilePath,
                                        Class<? extends Object> headerClass, String sheetName, String[] infoArr) throws Exception {
        Assert.notNull(data, "data is not null");
        Assert.hasText(outputFilePath, "outputFilePath is not null");
        Assert.notNull(headerClass, "headerClass is not null");
        Assert.notNull(sheetName, "sheetName must not be null");
        Assert.notNull(infoArr, "infoArr must not be null");
        // 创建Workbook
        try (XSSFWorkbook book = new XSSFWorkbook(); FileOutputStream os = new FileOutputStream(outputFilePath)) {
            Sheet sheet = book.createSheet(sheetName);
            // 创建单元格
            Field[] fieldArr = headerClass.getDeclaredFields();
            int filedIndex = 0;
            for (Field field : fieldArr) {
                field.setAccessible(true);
                ExcelHead annotation = field.getAnnotation(ExcelHead.class);
                if (Objects.isNull(annotation)) {
                    continue;
                }
                sheet.setColumnWidth(filedIndex, DEFAULT_COLUMNS_WIDTH);
                filedIndex++;
            }

            int sheetNum = 0;
            for (String info : infoArr) {
                Row row = sheet.createRow(sheetNum);
                Cell cell = row.createCell(0);
                cell.setCellValue(info);
                sheetNum++;
            }

            generateExcel(data, outputFilePath, headerClass, sheet, sheetNum);

            book.write(os);
            os.flush();
        }
    }

    /**
     * 生成excel文档
     *
     * @param data 生成excel数据
     * @param outputFilePath 输出路径
     * @param headerClass 头部信息
     * @param sheet 头部信息
     * @param sheetNum sheetNum
     */
    public static void generateExcel(List<? extends Object> data, String outputFilePath, Class<? extends Object> headerClass,
                                     Sheet sheet, int sheetNum) {
        Assert.notNull(data, "data is not null");
        Assert.hasText(outputFilePath, "outputFilePath is not null");
        Assert.notNull(headerClass, "headerClass is not null");
        Assert.notNull(sheet, "sheet is not null");

        List<List> exList = new ArrayList<>();
        exList.add(generateHeader(headerClass));

        data.stream().forEach(item -> {
            List dataList = new ArrayList();
            Class clz = item.getClass();
            Field[] fieldArr = clz.getDeclaredFields();
            for (Field field : fieldArr) {
                field.setAccessible(true);
                ExcelHead annotation = field.getAnnotation(ExcelHead.class);
                if (Objects.isNull(annotation)) {
                    continue;
                }
                String header = annotation.value();
                if (StringUtils.isNotBlank(header)) {
                    try {
                        dataList.add(field.get(item));
                    } catch (IllegalAccessException e) {
                        LOGGER.info("反射excel字段失败，field为{}", field.getName());
                    }
                }
            }
            exList.add(dataList);
        });
        writerExcel(exList, sheet, sheetNum);
    }

    private static List generateHeader(Class<? extends Object> headerClass) {
        List resultList = new ArrayList();
        Field[] fieldArr = headerClass.getDeclaredFields();
        for (Field field : fieldArr) {
            field.setAccessible(true);
            ExcelHead annotation = field.getAnnotation(ExcelHead.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            String header = annotation.value();
            header = LocaleI18nResolver.resolve(header);
            resultList.add(header);
        }
        return resultList;
    }

    private static void writerExcel(List<List> list, Sheet sheet, int sheetNum) {
        for (int i = 0; i < list.size(); i++) {
            List childList = list.get(i);
            // 创建行
            Row row = sheet.createRow(i + sheetNum);
            Cell[] cellArr = new Cell[childList.size()];
            for (int j = 0; j < childList.size(); j++) {
                String value = String.valueOf(childList.get(j));
                // 设置第i行第j列为Cells[j]单元格
                cellArr[j] = row.createCell(j);
                // 设置单元格的值
                cellArr[j].setCellValue(value);
            }
        }

    }
}
