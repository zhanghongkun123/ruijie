package com.ruijie.rcos.rcdc.rco.module.impl.util;


import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * 
 * Description: 导出excel工具类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/15
 *
 * @author zhiweiHong
 */
public class ExportUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportUtils.class);

    private ExportUtils()  { }

    /**
     * 生成excel文档
     * 
     * @param data 生成excel数据
     * @param outputFilePath 输出路径
     * @param headerClass 头部信息
     * @throws Exception Exception
     */
    public static void generateExcel(List<? extends Object> data, String outputFilePath, Class<? extends Object> headerClass) throws Exception {
        Assert.notNull(data, "data is not null");
        Assert.hasText(outputFilePath, "outputFilePath is not null");
        Assert.notNull(headerClass, "headerClass is not null");

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
        writerExcel(exList, outputFilePath);
    }

    /**
     * 根据类获取导出excel的header
     *
     * @param headerClass headerClass
     * @return List
     */
    public static List generateHeader(Class<? extends Object> headerClass) {
        Assert.notNull(headerClass, "headerClass is not null");
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

    private static void writerExcel(List<List> list, String outputFilePath) throws Exception {
        Assert.notNull(list, "list is not null");
        Assert.notNull(outputFilePath, "outputFilePath is not null");

        // 创建Workbook
        try (XSSFWorkbook book = new XSSFWorkbook(); FileOutputStream os = new FileOutputStream(outputFilePath)) {
            // 创建sheet
            XSSFSheet sheet = book.createSheet("Sheet1");
            for (int i = 0; i < list.size(); i++) {
                List childList = list.get(i);
                // 创建行
                XSSFRow row = sheet.createRow(i);
                // 创建单元格
                sheet.setColumnWidth((short) 0, (short) (35.7 * 100));// n为列高的像素数
                sheet.setColumnWidth((short) 1, (short) (35.7 * 100));
                sheet.setColumnWidth((short) 2, (short) (35.7 * 120));
                sheet.setColumnWidth((short) 3, (short) (35.7 * 120));
                sheet.setColumnWidth((short) 4, (short) (35.7 * 120));
                sheet.setColumnWidth((short) 5, (short) (35.7 * 120));
                sheet.setColumnWidth((short) 6, (short) (35.7 * 120));
                sheet.setColumnWidth((short) 7, (short) (35.7 * 120));
                XSSFCell[] cellArr = new XSSFCell[childList.size()];
                for (int j = 0; j < childList.size(); j++) {
                    String value = String.valueOf(childList.get(j));
                    // 设置第i行第j列为Cells[j]单元格
                    cellArr[j] = row.createCell(j);
                    // 设置单元格的值
                    cellArr[j].setCellValue(new XSSFRichTextString(value));
                }
            }
            book.write(os);
            os.flush();
        }
    }
}
