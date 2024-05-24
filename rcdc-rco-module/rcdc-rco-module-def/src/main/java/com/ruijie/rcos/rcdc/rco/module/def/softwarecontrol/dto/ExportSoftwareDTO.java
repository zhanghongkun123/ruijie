package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto;


import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.constants.SoftwareControlConstants;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Description: 导出实体类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/17
 *
 * @author lihengjing
 */
public class ExportSoftwareDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportSoftwareDTO.class);

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_GROUP_NAME)
    private String softwareGroupName;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_GROUP_TYPE)
    private String softwareGroupType;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_GROUP_DESC)
    private String softwareGroupDesc;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_NAME)
    private String softwareName;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_DESC)
    private String softwareDesc;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_DIGITAL_SIGN)
    private String digitalSign;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_DIGITAL_SIGN_FLAG)
    private String digitalSignFlag;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_PRODUCT_NAME)
    private String productName;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_PRODUCT_NAME_FLAG)
    private String productNameFlag;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_PROCESS_NAME)
    private String processName;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_PROCESS_NAME_FLAG)
    private String processNameFlag;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_ORIGINAL_FILE_NAME)
    private String originalFileName;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_ORIGINAL_FILE_NAME_FLAG)
    private String originalFileNameFlag;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_FILE_CUSTOM_MD5)
    private String fileCustomMd5;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_FILE_CUSTOM_MD5_FLAG)
    private String fileCustomMd5Flag;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_FILE_IS_DIRECTORY)
    private String directoryFlag;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_FILE_PARENT_ID)
    private String parentId;

    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_FILE_SOFTWARE_ID)
    private String softwareId;

    /**
     * 厂商数字签名(黑名单)
     */
    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_BLACK_DIGITAL_SIGN_FLAG)
    private String digitalSignBlackFlag;

    /**
     * 产品名称(黑名单)
     */
    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_BLACK_PRODUCT_NAME_FLAG)
    private String productNameBlackFlag;

    /**
     * 进程名(黑名单)
     */
    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_BLACK_PROCESS_NAME_FLAG)
    private String processNameBlackFlag;

    /**
     * 原始文件名(黑名单) 
     */
    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_BLACK_ORIGINAL_FILE_NAME_FLAG)
    private String originalFileNameBlackFlag;

    /**
     * 自定义md5值(黑名单) 
     */
    @ExcelHead(SoftwareControlConstants.RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_BLACK_FILE_CUSTOM_MD5_FLAG)
    private String fileCustomMd5BlackFlag;

    public ExportSoftwareDTO(SoftwareDTO softwareDTO, @Nullable SoftwareGroupDTO softwareGroupDTO) {
        Assert.notNull(softwareDTO, "softwareDTO is not null");
        if (softwareGroupDTO != null) {
            this.softwareGroupName = softwareGroupDTO.getName();
            this.softwareGroupDesc = softwareGroupDTO.getDescription();
        }
        this.softwareName = softwareDTO.getName();
        this.softwareDesc = softwareDTO.getDescription();
        BeanUtils.copyProperties(softwareDTO, this);
        if (softwareDTO.getDirectoryFlag() != null && softwareDTO.getDirectoryFlag()) {
            this.setSoftwareId(softwareDTO.getId().toString());
        }
        this.setParentId(softwareDTO.getParentId() != null ? softwareDTO.getParentId().toString() : null);
        if (SoftwareControlConstants.SOFTWARE_IMPORT_SOFTWARE_NULL_NAME.equals(softwareDTO.getName().trim())) {
            try {
                format(null);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                LOGGER.info("格式化ExportSoftwareDTO类失败，失败原因是{}", e);
            }
        } else {
            try {
                format(softwareDTO);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                LOGGER.info("格式化ExportSoftwareDTO类失败，失败原因是{}", e);
            }
        }
    }

    private void format(SoftwareDTO softwareDTO) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<? extends ExportSoftwareDTO> softwareClazz = this.getClass();
        Field[] fieldArr = softwareClazz.getDeclaredFields();
        for (Field field : fieldArr) {
            field.setAccessible(true);
            if (Objects.isNull(field.get(this))) {
                field.set(this, SoftwareControlConstants.SOFTWARE_INFO_FIELD_EXPORT_NULL);
            }
            if (softwareDTO == null) {
                continue;
            }
            if (field.getName().endsWith(SoftwareControlConstants.SOFTWARE_INFO_FIELD_END_STRING)) {
                Class clazz = softwareDTO.getClass();
                Method method = clazz.getDeclaredMethod(SoftwareControlConstants.SOFTWARE_INFO_FIELD_GET_METHOD + captureName(field.getName()));
                Boolean isTrue = (Boolean) method.invoke(softwareDTO);
                if (isTrue != null) {
                    field.set(this, isTrue ? SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE
                            : SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_FALSE);
                }
            }
        }
    }

    /**
     * 将字符串的首字母转大写
     *
     * @param str 需要转换的字符串
     * @return
     */
    private String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] strArr = str.toCharArray();
        strArr[0] -= 32;
        return String.valueOf(strArr);
    }

    public String getSoftwareGroupName() {
        return softwareGroupName;
    }

    public void setSoftwareGroupName(String softwareGroupName) {
        this.softwareGroupName = softwareGroupName;
    }

    public String getSoftwareGroupType() {
        return softwareGroupType;
    }

    public void setSoftwareGroupType(String softwareGroupType) {
        this.softwareGroupType = softwareGroupType;
    }

    public String getSoftwareGroupDesc() {
        return softwareGroupDesc;
    }

    public void setSoftwareGroupDesc(String softwareGroupDesc) {
        this.softwareGroupDesc = softwareGroupDesc;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getSoftwareDesc() {
        return softwareDesc;
    }

    public void setSoftwareDesc(String softwareDesc) {
        this.softwareDesc = softwareDesc;
    }

    public String getDigitalSign() {
        return digitalSign;
    }

    public void setDigitalSign(String digitalSign) {
        this.digitalSign = digitalSign;
    }

    public String getDigitalSignFlag() {
        return digitalSignFlag;
    }

    public void setDigitalSignFlag(String digitalSignFlag) {
        this.digitalSignFlag = digitalSignFlag;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameFlag() {
        return productNameFlag;
    }

    public void setProductNameFlag(String productNameFlag) {
        this.productNameFlag = productNameFlag;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessNameFlag() {
        return processNameFlag;
    }

    public void setProcessNameFlag(String processNameFlag) {
        this.processNameFlag = processNameFlag;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getOriginalFileNameFlag() {
        return originalFileNameFlag;
    }

    public void setOriginalFileNameFlag(String originalFileNameFlag) {
        this.originalFileNameFlag = originalFileNameFlag;
    }

    public String getFileCustomMd5() {
        return fileCustomMd5;
    }

    public void setFileCustomMd5(String fileCustomMd5) {
        this.fileCustomMd5 = fileCustomMd5;
    }

    public String getFileCustomMd5Flag() {
        return fileCustomMd5Flag;
    }

    public void setFileCustomMd5Flag(String fileCustomMd5Flag) {
        this.fileCustomMd5Flag = fileCustomMd5Flag;
    }

    public String getDirectoryFlag() {
        return directoryFlag;
    }

    public void setDirectoryFlag(String directoryFlag) {
        this.directoryFlag = directoryFlag;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(String softwareId) {
        this.softwareId = softwareId;
    }

    public String getDigitalSignBlackFlag() {
        return digitalSignBlackFlag;
    }

    public void setDigitalSignBlackFlag(String digitalSignBlackFlag) {
        this.digitalSignBlackFlag = digitalSignBlackFlag;
    }

    public String getProductNameBlackFlag() {
        return productNameBlackFlag;
    }

    public void setProductNameBlackFlag(String productNameBlackFlag) {
        this.productNameBlackFlag = productNameBlackFlag;
    }

    public String getProcessNameBlackFlag() {
        return processNameBlackFlag;
    }

    public void setProcessNameBlackFlag(String processNameBlackFlag) {
        this.processNameBlackFlag = processNameBlackFlag;
    }

    public String getOriginalFileNameBlackFlag() {
        return originalFileNameBlackFlag;
    }

    public void setOriginalFileNameBlackFlag(String originalFileNameBlackFlag) {
        this.originalFileNameBlackFlag = originalFileNameBlackFlag;
    }

    public String getFileCustomMd5BlackFlag() {
        return fileCustomMd5BlackFlag;
    }

    public void setFileCustomMd5BlackFlag(String fileCustomMd5BlackFlag) {
        this.fileCustomMd5BlackFlag = fileCustomMd5BlackFlag;
    }
}
