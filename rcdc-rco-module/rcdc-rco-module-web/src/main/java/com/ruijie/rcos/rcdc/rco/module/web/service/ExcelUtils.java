package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.userprofile.TextExcelField;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.InputStream;

/**
 * Description: Excel工具
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/22
 *
 * @author Jarman
 */
public class ExcelUtils {

    private ExcelUtils() {
        throw new IllegalStateException("ExcelUtils Utility class");
    }


    /** 用户组模板标识 */
    private static final String USER_GROUP_TEMPLATE = "user_group_template";

    /** 纯IDV模式下用户组模板标识 */
    private static final String USER_GROUP_TEMPLATE_ONLY_FOR_IDV = "user_group_template_only_for_idv";

    /** 用户模板标识 */
    private static final String USER_TEMPLATE = "user_template";

    /** 纯IDV模式下用户模板标识 */
    private static final String USER_TEMPLATE_ONLY_FOR_IDV = "user_template_only_for_idv";

    /** pc终端模板标识 */
    private static final String COMPUTER_TEMPLATE = "computer_template";

    /** 用户模板标识 */
    private static final String VDIDESK_TEMPLATE = "vdidesk_template";

    /** 用户-mac绑定模板标识 */
    private static final String USER_MAC_BINDING_TEMPLATE = "user_mac_binding_template";

    /**
     * 解析Excel文件
     *
     * @param inputStream 文件输入流
     * @return 返回Excel第一个Sheet
     * @throws BusinessException 业务异常
     */
    public static Sheet parseExcelFile(InputStream inputStream) throws BusinessException {
        Assert.notNull(inputStream, "InputStream 不能为null");
        Workbook book;
        try {
            book = WorkbookFactory.create(inputStream);
        } catch (Exception e) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IMPORT_INVALID_FORMAT, e);
        }
        return book.getSheetAt(0);
    }

    /**
     * 获取单元格内容
     *
     * @param row excel行
     * @param userExcelConstants excel列定义
     * @return 返回单元格内容
     */
    public static String getCellValue(Row row, UserExcelField userExcelConstants) {
        Assert.notNull(row, "row不能为null");
        Assert.notNull(userExcelConstants, "UserExcelField 不能为null");

        Cell cell = CellUtil.getCell(row, userExcelConstants.getIndex());
        cell.setCellType(CellType.STRING);
        String value = cell.getStringCellValue();
        return StringUtils.isNotBlank(value) ? value.trim() : null;
    }

    /**
     * 获取单元格内容
     *
     * @param row excel行
     * @param computerExcelField excel列定义
     * @return 返回单元格内容
     */
    public static String getCellValue(Row row, ComputerExcelField computerExcelField) {
        Assert.notNull(row, "row不能为null");
        Assert.notNull(computerExcelField, "UserExcelField 不能为null");

        Cell cell = CellUtil.getCell(row, computerExcelField.getIndex());
        cell.setCellType(CellType.STRING);
        String value = cell.getStringCellValue();
        return StringUtils.isNotBlank(value) ? value.trim() : null;
    }

    /**
     * 获取单元格内容
     *
     * @param row excel行
     * @param importVDIDeskExcelField excel列定义
     * @return 返回单元格内容
     */
    public static String getCellValue(Row row, ImportVDIDeskExcelField importVDIDeskExcelField) {
        Assert.notNull(row, "row不能为null");
        Assert.notNull(importVDIDeskExcelField, "importVDIDeskExcelField 不能为null");

        Cell cell = CellUtil.getCell(row, importVDIDeskExcelField.getIndex());
        // 强制使用DataFormatter获取单元格的文本表示（避免单元格内容出现类似小数，出现精度丢失）
        String value = new DataFormatter().formatCellValue(cell);
        return value != null ? value.trim() : null;
    }

    /**
     * 获取单元格内容
     *
     * @param row excel行
     * @param excelField excel列定义
     * @return 返回单元格内容
     */
    public static String getCellValue(Row row, ExcelField excelField) {
        Assert.notNull(row, "row不能为null");
        Assert.notNull(excelField, "excelField 不能为null");

        Cell cell = CellUtil.getCell(row, excelField.getIndex());
        if (cell.getCellTypeEnum() != CellType.STRING) {
            cell.setCellType(CellType.STRING);
        }
        String value = cell.getStringCellValue();
        return value != null ? value.trim() : null;
    }

    /**
     * 判断是否为空行
     *
     * @param row 行
     * @return true 为null
     */
    public static boolean isNullRow(@Nullable Row row) {
        if (row == null) {
            return true;
        }
        for (UserExcelField userExcel : UserExcelField.values()) {
            Cell cell = CellUtil.getCell(row, userExcel.getIndex());
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断路径是否为空行
     *
     * @param row         行
     * @param excelFields 列对象类型 枚举类数组
     * @return true 为null
     */
    public static boolean isNullRow(@Nullable Row row, TextExcelField[] excelFields) {
        Assert.notNull(excelFields, "excelFields 不能为null");
        if (row == null) {
            return true;
        }
        for (TextExcelField excelField : excelFields) {
            Cell cell = CellUtil.getCell(row, excelField.getIndex());
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取单元格内容
     *
     * @param row        excel行
     * @param excelField excel列定义
     * @return 返回单元格内容
     */
    public static String getCellValue(Row row, TextExcelField excelField) {
        Assert.notNull(row, "row不能为null");
        Assert.notNull(excelField, "excelField 不能为null");

        Cell cell = CellUtil.getCell(row, excelField.getIndex());
        if (cell.getCellTypeEnum() != CellType.STRING) {
            cell.setCellType(CellType.STRING);
        }
        String value = cell.getStringCellValue();
        return value != null ? value.trim() : null;
    }

    /**
     * 判断是否为空行
     *
     * @param row 行
     * @param excelFields 列对象类型 枚举类数组
     * @return true 为null
     */
    public static boolean isNullRow(@Nullable Row row, ExcelField[] excelFields) {
        Assert.notNull(excelFields, "excelFields 不能为null");
        if (row == null) {
            return true;
        }
        for (ExcelField excelField : excelFields) {
            Cell cell = CellUtil.getCell(row, excelField.getIndex());
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验导入的模板是否为导入VDI云桌面模板
     *
     * @param sheet excel sheet
     * @return true 用户组模板
     */
    public static boolean isVDIDeskTemplate(Sheet sheet) {
        Assert.notNull(sheet, "sheet cannot null");
        String value = getTemplateType(sheet);
        return StringUtils.isNotBlank(value) && value.equals(VDIDESK_TEMPLATE);
    }

    /**
     * 校验导入的模板是否为导入用户组模板
     *
     * @param sheet excel sheet
     * @return true 用户组模板
     */
    public static boolean isUserGroupTemplate(Sheet sheet) {
        Assert.notNull(sheet, "sheet cannot null");
        String value = getTemplateType(sheet);
        return StringUtils.isNotBlank(value) && value.equals(USER_GROUP_TEMPLATE);
    }

    /**
     * 校验导入的模板是否为导入纯IDV模式下用户组模板
     *
     * @param sheet excel sheet
     * @return true 用户组模板
     */
    public static boolean isUserGroupTemplateOnlyForIDV(Sheet sheet) {
        Assert.notNull(sheet, "sheet cannot null");
        String value = getTemplateType(sheet);
        return StringUtils.isNotBlank(value) && value.equals(USER_GROUP_TEMPLATE_ONLY_FOR_IDV);
    }

    /**
     * 校验导入的模板是否为导入用户模板
     *
     * @param sheet excel sheet
     * @return true 用户模板
     */
    public static boolean isUserTemplate(Sheet sheet) {
        Assert.notNull(sheet, "sheet cannot null");
        String value = getTemplateType(sheet);
        return StringUtils.isNotBlank(value) && value.equals(USER_TEMPLATE);
    }

    /**
     * 校验导入的模板是否为导入纯IDV模式下用户模板
     *
     * @param sheet excel sheet
     * @return true 用户模板
     */
    public static boolean isUserTemplateOnlyForIDV(Sheet sheet) {
        Assert.notNull(sheet, "sheet cannot null");
        String value = getTemplateType(sheet);
        return StringUtils.isNotBlank(value) && value.equals(USER_TEMPLATE_ONLY_FOR_IDV);
    }

    /**
     * 校验导入的模板是否为用户-mac绑定模板
     *
     * @param sheet excel sheet
     * @return boolean
     */
    public static boolean isUserMacBindingTemplate(Sheet sheet) {
        Assert.notNull(sheet, "sheet cannot null");
        String value = getTemplateType(sheet);
        return StringUtils.isNotBlank(value) && value.equals(USER_MAC_BINDING_TEMPLATE);
    }

    /**
     * 校验导入的模板是否为导入PC终端模板
     *
     * @param sheet excel sheet
     * @return true 用户模板
     */
    public static boolean isComputerTemplate(Sheet sheet) {
        Assert.notNull(sheet, "sheet cannot null");
        String value = getTemplateType(sheet);
        return StringUtils.isNotBlank(value) && value.equals(COMPUTER_TEMPLATE);
    }

    /**
     * 获取excel模板第1行第1列单元格的值，识别模板类型
     */
    private static String getTemplateType(Sheet sheet) {
        Row row = sheet.getRow(0);
        if (row == null) {
            return StringUtils.EMPTY;
        }
        Cell cell = CellUtil.getCell(row, 0);
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }
}