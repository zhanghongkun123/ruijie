package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.constants.SoftwareControlConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.SoftwareControlBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.dto.ImportSoftwareDTO;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.constants.SoftwareControlConstants.SOFTWARE_IMPORT_SOFTWARE_NULL_NAME;


/**
 * Description: 导入的软件数据行校验类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/18
 *
 * @author lihengjing
 */
public class SoftwareRowValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareRowValidator.class);

    /**
     * 记录校验数据不符合规范的国际化描述
     */
    List<String> errorList;

    /**
     * 数据行号
     */
    private Integer rowNum;

    private String softwareGroupName;

    private String softwareGroupType;

    private String softwareGroupDesc;

    private String softwareName;

    private String softwareDesc;

    private String digitalSign;

    private String digitalSignFlag;

    private String productName;

    private String productNameFlag;

    private String processName;

    private String processNameFlag;

    private String originalFileName;

    private String originalFileNameFlag;

    private String fileCustomMd5;

    private String fileCustomMd5Flag;

    private String directoryFlag;

    /**
     * 厂商数字签名(黑名单) 
     */
    private String digitalSignBlackFlag;

    /**
     * 产品名称(黑名单) 
     */
    private String productNameBlackFlag;

    /**
     * 进程名(黑名单) 
     */
    private String processNameBlackFlag;

    /**
     * 原始文件名(黑名单) 
     */
    private String originalFileNameBlackFlag;

    /**
     * 自定义md5值(黑名单) 
     */
    private String fileCustomMd5BlackFlag;


    protected SoftwareRowValidator(ImportSoftwareDTO software, List<String> errorList) {
        Assert.notNull(software, "行不能为null");
        Assert.notNull(errorList, "行不能为null");
        this.errorList = errorList;
        // row中的行数初始行为0，这里补偿+1
        this.rowNum = software.getRowNum() + 1;
        this.softwareGroupName = software.getSoftwareGroupName();
        this.softwareGroupType = software.getSoftwareGroupType().trim();
        this.softwareGroupDesc = software.getSoftwareGroupDesc();
        this.softwareName = software.getSoftwareName();
        this.softwareDesc = software.getSoftwareDesc();
        this.digitalSign = software.getDigitalSign();
        this.digitalSignFlag = software.getDigitalSignFlag().trim();
        this.productName = software.getProductName();
        this.productNameFlag = software.getProductNameFlag().trim();
        this.processName = software.getProcessName();
        this.processNameFlag = software.getProcessNameFlag().trim();
        this.originalFileName = software.getOriginalFileName();
        this.originalFileNameFlag = software.getOriginalFileNameFlag().trim();
        this.fileCustomMd5 = software.getFileCustomMd5();
        this.fileCustomMd5Flag = software.getFileCustomMd5Flag().trim();
        this.directoryFlag = software.getDirectoryFlag().trim();
        this.digitalSignBlackFlag = software.getDigitalSignBlackFlag().trim();
        this.productNameBlackFlag = software.getProductNameBlackFlag().trim();
        this.processNameBlackFlag = software.getProcessNameBlackFlag().trim();
        this.originalFileNameBlackFlag = software.getOriginalFileNameBlackFlag().trim();
        this.fileCustomMd5BlackFlag = software.getFileCustomMd5BlackFlag().trim();
    }


    /**
     * 行内容数据校验
     *
     */
    public void validateRowData() {
        String rowNumStr = String.valueOf(rowNum);

        validateSoftwareGroup(rowNumStr);
        if (!SOFTWARE_IMPORT_SOFTWARE_NULL_NAME.equals(softwareName)) {
            validateSoftwareName(rowNumStr);
            validateSoftwareInfo(rowNumStr);
            validateSoftwareBlackInfo(rowNumStr);
        }

    }


    private void validateSoftwareGroup(String rowNumStr) {
        if (StringUtils.hasText(softwareGroupName)) {
            // 软件分组名称不能超过SoftwareValidateRules.SOFTWARE_GROUP_NAME_SIZE个字符
            if (softwareGroupName.length() > SoftwareValidateRules.SOFTWARE_GROUP_NAME_SIZE) {
                LOGGER.error("软件分组名称长度不能超过{}个字符", SoftwareValidateRules.SOFTWARE_GROUP_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_GROUP_NAME_TOO_LENGTH,
                        new String[] {rowNumStr, softwareGroupName, String.valueOf(SoftwareValidateRules.SOFTWARE_GROUP_NAME_SIZE)}));
            }
            if (!ValidatorUtil.isTextName(softwareGroupName)) {
                // 软件分组名称格式错误
                LOGGER.error("软件分组名称[{}]格式错误", softwareGroupName);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_GROUP_NAME_INCORRECT,
                        new String[] {rowNumStr, softwareGroupName}));
            }
        }

        if (softwareGroupDesc.length() > SoftwareValidateRules.SOFTWARE_GROUP_DESC_SIZE) {
            // 软件分组描述不能超过SoftwareValidateRules.SOFTWARE_GROUP_DESC_SIZE个字符
            LOGGER.error("软件分组描述长度不能超过{}个字符", SoftwareValidateRules.SOFTWARE_GROUP_DESC_SIZE);
            errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_GROUP_DESC_TOO_LENGTH,
                    new String[] {rowNumStr, softwareGroupDesc, String.valueOf(SoftwareValidateRules.SOFTWARE_GROUP_DESC_SIZE)}));
        }
    }

    private void validateSoftwareName(String rowNumStr) {
        if (StringUtils.hasText(softwareName)) {
            if (softwareName.length() > SoftwareValidateRules.SOFTWARE_NAME_SIZE) {
                // 软件名称不能超过UserValidateRules.USER_NAME_SIZE个字符
                LOGGER.error("软件名称长度不能超过{}个字符", SoftwareValidateRules.SOFTWARE_NAME_SIZE);
            }
            if (!ValidatorUtil.isTextName(softwareName)) {
                // 软件名称格式错误
                LOGGER.error("软件名称[{}]格式错误", softwareName);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_NAME_INCORRECT,
                        new String[] {rowNumStr, softwareName}));
            }
        } else {
            // 软件名称不能为空
            LOGGER.error("软件名称[{}]不能为空", softwareName);
            errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_NAME_NOT_ALLOW_EMPTY,
                    new String[] {rowNumStr}));
        }
        if (softwareDesc.length() > SoftwareValidateRules.SOFTWARE_DESC_SIZE) {
            // 软件描述不能超过SoftwareValidateRules.SOFTWARE_DESC_SIZE个字符
            LOGGER.error("软件描述长度不能超过{}个字符", SoftwareValidateRules.SOFTWARE_DESC_SIZE);
            errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_DESC_TOO_LENGTH,
                    new String[] {rowNumStr, softwareDesc, String.valueOf(SoftwareValidateRules.SOFTWARE_DESC_SIZE)}));
        }
    }

    private void validateSoftwareInfo(String rowNumStr) {

        Boolean isDirFlag = SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(directoryFlag);
        if (!SoftwareValidateRules.DEFAULT_FLAGS.contains(directoryFlag)) {
            // 是否文件夹不能为空
            LOGGER.error("[{}]是否文件夹仅支持【是】、【否】两个选项", softwareName);
            errorList.add(LocaleI18nResolver.resolve(
                    SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_FILE_CUSTOM_DIRECTORY_FLAG_NOT_ALLOW_OTHER_WORDS,
                    new String[] {rowNumStr, softwareName, directoryFlag}));
        }

        if (!isDirFlag && !SoftwareValidateRules.DEFAULT_FLAGS.contains(digitalSignFlag)) {
            // 厂商数字签名不能为空
            LOGGER.error("[{}]厂商数字签名标记仅支持【是】、【否】两个选项", softwareName);
            errorList.add(
                    LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_DIGITAL_SIGN_FLAG_NOT_ALLOW_OTHER_WORDS,
                            new String[] {rowNumStr, softwareName, digitalSignFlag}));
        } else {
            if (!isDirFlag && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(digitalSignFlag) && !StringUtils.hasText(digitalSign)) {
                // 厂商数字签名标记是时，厂商数字签名信息不可为空
                LOGGER.error("[{}]厂商数字签名标记是时，厂商数字签名信息不可为空", softwareName);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_DIGITAL_SIGN_NOT_ALLOW_EMPTY,
                        new String[] {rowNumStr, softwareName, digitalSign}));
            }
            if (digitalSign.length() > SoftwareValidateRules.DIGITAL_SIGN_SIZE) {
                // 厂商数字签名不能超过SoftwareValidateRules.SOFTWARE_DESC_SIZE个字符
                LOGGER.error("厂商数字签名长度不能超过{}个字符", SoftwareValidateRules.DIGITAL_SIGN_SIZE);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_DIGITAL_SIGN_TOO_LENGTH,
                        new String[] {rowNumStr, digitalSign, String.valueOf(SoftwareValidateRules.DIGITAL_SIGN_SIZE)}));
            }
        }
        if (!isDirFlag && !SoftwareValidateRules.DEFAULT_FLAGS.contains(productNameFlag)) {
            // 产品名称不能为空
            LOGGER.error("[{}]产品名称标记仅支持【是】、【否】两个选项", softwareName);
            errorList.add(
                    LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_PRODUCT_NAME_FLAG_NOT_ALLOW_OTHER_WORDS,
                            new String[] {rowNumStr, softwareName, productNameFlag}));
        } else {
            if (!isDirFlag && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(productNameFlag) && !StringUtils.hasText(productName)) {
                // 产品名称标记是时，产品名称信息不可为空
                LOGGER.error("[{}]产品名称标记是时，产品名称信息不可为空", softwareName);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_PRODUCT_NAME_NOT_ALLOW_EMPTY,
                        new String[] {rowNumStr, softwareName}));
            }
            if (productName.length() > SoftwareValidateRules.PRODUCT_NAME_SIZE) {
                // 产品名称不能超过SoftwareValidateRules.SOFTWARE_DESC_SIZE个字符
                LOGGER.error("产品名称长度不能超过{}个字符", SoftwareValidateRules.PRODUCT_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_PRODUCT_NAME_TOO_LENGTH,
                        new String[] {rowNumStr, productName, String.valueOf(SoftwareValidateRules.PRODUCT_NAME_SIZE)}));
            }
        }
        if (!isDirFlag && !SoftwareValidateRules.DEFAULT_FLAGS.contains(processNameFlag)) {
            // 进程名不能为空
            LOGGER.error("[{}]进程名标记仅支持【是】、【否】两个选项", softwareName);
            errorList.add(
                    LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_PROCESS_NAME_FLAG_NOT_ALLOW_OTHER_WORDS,
                            new String[] {rowNumStr, softwareName, processNameFlag}));
        } else {
            if (!isDirFlag && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(processNameFlag) && !StringUtils.hasText(processName)) {
                // 进程名标记是时，进程名信息不可为空
                LOGGER.error("[{}]进程名标记是时，进程名信息不可为空", softwareName);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_PROCESS_NAME_NOT_ALLOW_EMPTY,
                        new String[] {rowNumStr, softwareName}));
            }
            if (processName.length() > SoftwareValidateRules.PROCESS_NAME_SIZE) {
                // 进程名不能超过SoftwareValidateRules.SOFTWARE_DESC_SIZE个字符
                LOGGER.error("进程名长度不能超过{}个字符", SoftwareValidateRules.PROCESS_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_PROCESS_NAME_TOO_LENGTH,
                        new String[] {rowNumStr, processName, String.valueOf(SoftwareValidateRules.PROCESS_NAME_SIZE)}));
            }
        }
        if (!isDirFlag && !SoftwareValidateRules.DEFAULT_FLAGS.contains(originalFileNameFlag)) {
            // 原始文件名不能为空
            LOGGER.error("[{}]原始文件名标记仅支持【是】、【否】两个选项", softwareName);
            errorList.add(LocaleI18nResolver.resolve(
                    SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_ORIGINAL_FILE_NAME_FLAG_NOT_ALLOW_OTHER_WORDS,
                    new String[] {rowNumStr, softwareName, originalFileNameFlag}));
        } else {
            if (!isDirFlag && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(originalFileNameFlag)
                    && !StringUtils.hasText(originalFileName)) {
                // 原始文件名标记是时，原始文件名信息不可为空
                LOGGER.error("[{}]原始文件名标记是时，原始文件名信息不可为空", softwareName);
                errorList.add(
                        LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_ORIGINAL_FILE_NAME_NOT_ALLOW_EMPTY,
                                new String[] {rowNumStr, softwareName}));
            }
            if (originalFileName.length() > SoftwareValidateRules.ORIGINAL_FILE_NAME_SIZE) {
                // 原始文件名不能超过SoftwareValidateRules.SOFTWARE_DESC_SIZE个字符
                LOGGER.error("原始文件名长度不能超过{}个字符", SoftwareValidateRules.ORIGINAL_FILE_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_ORIGINAL_FILE_NAME_TOO_LENGTH,
                        new String[] {rowNumStr, originalFileName, String.valueOf(SoftwareValidateRules.ORIGINAL_FILE_NAME_SIZE)}));
            }
        }
        if (!isDirFlag && !SoftwareValidateRules.DEFAULT_FLAGS.contains(fileCustomMd5Flag)) {
            // 文件特征码不能为空
            LOGGER.error("[{}]文件特征码标记仅支持【是】、【否】两个选项", softwareName);
            errorList.add(LocaleI18nResolver.resolve(
                    SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_FILE_CUSTOM_MD5_NOT_FLAG_ALLOW_OTHER_WORDS,
                    new String[] {rowNumStr, softwareName, fileCustomMd5Flag}));
        } else {
            if (SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(fileCustomMd5Flag) && !StringUtils.hasText(fileCustomMd5)) {
                // 文件特征码标记是时，文件特征码信息不可为空
                LOGGER.error("[{}]文件特征码标记是时，文件特征码信息不可为空", softwareName);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_FILE_CUSTOM_MD5_NOT_ALLOW_EMPTY,
                        new String[] {rowNumStr, softwareName}));
            }
            if (fileCustomMd5.length() > SoftwareValidateRules.FILE_CUSTOM_MD5_SIZE) {
                // 文件特征码不能超过SoftwareValidateRules.SOFTWARE_DESC_SIZE个字符
                LOGGER.error("文件特征码长度不能超过{}个字符", SoftwareValidateRules.FILE_CUSTOM_MD5_SIZE);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_FILE_CUSTOM_MD5_TOO_LENGTH,
                        new String[] {rowNumStr, fileCustomMd5, String.valueOf(SoftwareValidateRules.FILE_CUSTOM_MD5_SIZE)}));
            }
        }
        if (!validateSoftwareFieldMoreThanOne()) {
            LOGGER.error("[{}]软件信息项至少有一项为是，且不为空", softwareName);
            errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_INFO_MUST_MORE_THAN_ONE,
                    new String[] {rowNumStr, softwareName}));
        }
    }

    /**
     * 检查软件信息项至少为一 且勾选
     *
     * @return true 校验通过 false 校验不通过
     */
    private boolean validateSoftwareFieldMoreThanOne() {
        // mvp版本不做软件白名单运行查杀，所以不收集安装路径信息
        return (SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(directoryFlag)
                || (StringUtils.hasText(digitalSign) && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(digitalSignFlag))
                || (StringUtils.hasText(productName) && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(productNameFlag))
                || (StringUtils.hasText(processName) && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(processNameFlag))
                || (StringUtils.hasText(originalFileName) && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(originalFileNameFlag))
                || (StringUtils.hasText(fileCustomMd5) && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(fileCustomMd5Flag)));
    }


    private void validateSoftwareBlackInfo(String rowNumStr) {

        Boolean isDirFlag = SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(directoryFlag);

        if (!isDirFlag && !SoftwareValidateRules.DEFAULT_FLAGS.contains(digitalSignBlackFlag)) {
            // 厂商数字签名不能为空
            LOGGER.error("[{}]黑名单厂商数字签名标记仅支持【是】、【否】两个选项", softwareName);
            errorList.add(
                    LocaleI18nResolver.resolve(
                            SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_BLACK_DIGITAL_SIGN_FLAG_NOT_ALLOW_OTHER_WORDS,
                            new String[] {rowNumStr, softwareName, digitalSignBlackFlag}));
        } else {
            if (!isDirFlag && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(digitalSignBlackFlag) && !StringUtils.hasText(digitalSign)) {
                // 厂商数字签名标记是时，厂商数字签名信息不可为空
                LOGGER.error("[{}]黑名单厂商数字签名标记是时，厂商数字签名信息不可为空", softwareName);
                errorList.add(LocaleI18nResolver.resolve(
                        SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_BLACK_DIGITAL_SIGN_NOT_ALLOW_EMPTY,
                        new String[] {rowNumStr, softwareName, digitalSign}));
            }
            if (digitalSign.length() > SoftwareValidateRules.DIGITAL_SIGN_SIZE) {
                // 厂商数字签名不能超过SoftwareValidateRules.SOFTWARE_DESC_SIZE个字符
                LOGGER.error("厂商数字签名长度不能超过{}个字符", SoftwareValidateRules.DIGITAL_SIGN_SIZE);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_DIGITAL_SIGN_TOO_LENGTH,
                        new String[] {rowNumStr, digitalSign, String.valueOf(SoftwareValidateRules.DIGITAL_SIGN_SIZE)}));
            }
        }
        if (!isDirFlag && !SoftwareValidateRules.DEFAULT_FLAGS.contains(productNameBlackFlag)) {
            // 产品名称不能为空
            LOGGER.error("[{}]黑名单产品名称标记仅支持【是】、【否】两个选项", softwareName);
            errorList.add(
                    LocaleI18nResolver.resolve(
                            SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_BLACK_PRODUCT_NAME_FLAG_NOT_ALLOW_OTHER_WORDS,
                            new String[] {rowNumStr, softwareName, productNameBlackFlag}));
        } else {
            if (!isDirFlag && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(productNameBlackFlag)
                    && !StringUtils.hasText(productName)) {
                // 产品名称标记是时，产品名称信息不可为空
                LOGGER.error("[{}]黑名单产品名称标记是时，产品名称信息不可为空", softwareName);
                errorList.add(LocaleI18nResolver.resolve(
                        SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_BLACK_PRODUCT_NAME_NOT_ALLOW_EMPTY,
                        new String[] {rowNumStr, softwareName}));
            }
            if (productName.length() > SoftwareValidateRules.PRODUCT_NAME_SIZE) {
                // 产品名称不能超过SoftwareValidateRules.SOFTWARE_DESC_SIZE个字符
                LOGGER.error("产品名称长度不能超过{}个字符", SoftwareValidateRules.PRODUCT_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_PRODUCT_NAME_TOO_LENGTH,
                        new String[] {rowNumStr, productName, String.valueOf(SoftwareValidateRules.PRODUCT_NAME_SIZE)}));
            }
        }
        if (!isDirFlag && !SoftwareValidateRules.DEFAULT_FLAGS.contains(processNameBlackFlag)) {
            // 进程名不能为空
            LOGGER.error("[{}]黑名单进程名标记仅支持【是】、【否】两个选项", softwareName);
            errorList.add(
                    LocaleI18nResolver.resolve(
                            SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_BLACK_PROCESS_NAME_FLAG_NOT_ALLOW_OTHER_WORDS,
                            new String[] {rowNumStr, softwareName, processNameBlackFlag}));
        } else {
            if (!isDirFlag && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(processNameBlackFlag) && !StringUtils.hasText(processName)) {
                // 进程名标记是时，进程名信息不可为空
                LOGGER.error("[{}]黑名单进程名标记是时，进程名信息不可为空", softwareName);
                errorList.add(LocaleI18nResolver.resolve(
                        SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_BLACK_PROCESS_NAME_NOT_ALLOW_EMPTY,
                        new String[] {rowNumStr, softwareName}));
            }
            if (processName.length() > SoftwareValidateRules.PROCESS_NAME_SIZE) {
                // 进程名不能超过SoftwareValidateRules.SOFTWARE_DESC_SIZE个字符
                LOGGER.error("进程名长度不能超过{}个字符", SoftwareValidateRules.PROCESS_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_PROCESS_NAME_TOO_LENGTH,
                        new String[] {rowNumStr, processName, String.valueOf(SoftwareValidateRules.PROCESS_NAME_SIZE)}));
            }
        }
        if (!isDirFlag && !SoftwareValidateRules.DEFAULT_FLAGS.contains(originalFileNameBlackFlag)) {
            // 原始文件名不能为空
            LOGGER.error("[{}]黑名单原始文件名标记仅支持【是】、【否】两个选项", softwareName);
            errorList.add(LocaleI18nResolver.resolve(
                    SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_BLACK_ORIGINAL_FILE_NAME_FLAG_NOT_ALLOW_OTHER_WORDS,
                    new String[] {rowNumStr, softwareName, originalFileNameBlackFlag}));
        } else {
            if (!isDirFlag && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(originalFileNameBlackFlag)
                    && !StringUtils.hasText(originalFileName)) {
                // 原始文件名标记是时，原始文件名信息不可为空
                LOGGER.error("[{}]黑名单原始文件名标记是时，原始文件名信息不可为空", softwareName);
                errorList.add(
                        LocaleI18nResolver.resolve(
                                SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_BLACK_ORIGINAL_FILE_NAME_NOT_ALLOW_EMPTY,
                                new String[] {rowNumStr, softwareName}));
            }
            if (originalFileName.length() > SoftwareValidateRules.ORIGINAL_FILE_NAME_SIZE) {
                // 原始文件名不能超过SoftwareValidateRules.SOFTWARE_DESC_SIZE个字符
                LOGGER.error("原始文件名长度不能超过{}个字符", SoftwareValidateRules.ORIGINAL_FILE_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_ORIGINAL_FILE_NAME_TOO_LENGTH,
                        new String[] {rowNumStr, originalFileName, String.valueOf(SoftwareValidateRules.ORIGINAL_FILE_NAME_SIZE)}));
            }
        }
        if (!isDirFlag && !SoftwareValidateRules.DEFAULT_FLAGS.contains(fileCustomMd5BlackFlag)) {
            // 文件特征码不能为空
            LOGGER.error("[{}]黑名单文件特征码标记仅支持【是】、【否】两个选项", softwareName);
            errorList.add(LocaleI18nResolver.resolve(
                    SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_BLACK_FILE_CUSTOM_MD5_NOT_FLAG_ALLOW_OTHER_WORDS,
                    new String[] {rowNumStr, softwareName, fileCustomMd5BlackFlag}));
        } else {
            if (SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(fileCustomMd5BlackFlag) && !StringUtils.hasText(fileCustomMd5)) {
                // 文件特征码标记是时，文件特征码信息不可为空
                LOGGER.error("[{}]黑名单文件特征码标记是时，文件特征码信息不可为空", softwareName);
                errorList.add(LocaleI18nResolver.resolve(
                        SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_BLACK_FILE_CUSTOM_MD5_NOT_ALLOW_EMPTY,
                        new String[] {rowNumStr, softwareName}));
            }
            if (fileCustomMd5.length() > SoftwareValidateRules.FILE_CUSTOM_MD5_SIZE) {
                // 文件特征码不能超过SoftwareValidateRules.SOFTWARE_DESC_SIZE个字符
                LOGGER.error("文件特征码长度不能超过{}个字符", SoftwareValidateRules.FILE_CUSTOM_MD5_SIZE);
                errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_FILE_CUSTOM_MD5_TOO_LENGTH,
                        new String[] {rowNumStr, fileCustomMd5, String.valueOf(SoftwareValidateRules.FILE_CUSTOM_MD5_SIZE)}));
            }
        }
        if (!validateSoftwareBlackFieldMoreThanOne()) {
            LOGGER.error("[{}]软件信息项至少有一项为是，且不为空", softwareName);
            errorList.add(LocaleI18nResolver.resolve(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_INFO_MUST_MORE_THAN_ONE,
                    new String[] {rowNumStr, softwareName}));
        }
    }

    /**
     * 检查软件信息项至少为一 且勾选
     *
     * @return true 校验通过 false 校验不通过
     */
    private boolean validateSoftwareBlackFieldMoreThanOne() {
        // mvp版本不做软件白名单运行查杀，所以不收集安装路径信息
        return (SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(directoryFlag)
                || (StringUtils.hasText(digitalSign) && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(digitalSignFlag))
                || (StringUtils.hasText(productName) && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(productNameFlag))
                || (StringUtils.hasText(processName) && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(processNameFlag))
                || (StringUtils.hasText(originalFileName) && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(originalFileNameFlag))
                || (StringUtils.hasText(fileCustomMd5) && SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(fileCustomMd5Flag)));
    }


}
