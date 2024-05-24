package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportVDIDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.util.CapacityUnitUtils;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Description: 导入的VDI云桌面据行校验类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/23
 *
 * @author zhangyichi
 */
public class ImportVDIDeskRowValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportVDIDeskRowValidator.class);

    private static final String NUMBER_PATTERN = "^[1-9]\\d*$";

    private static final String MEMORY_PATTERN = "^[1-9]([0-9])*([\\.]5)?$";

    private static final int CPU_MIN = 1;

    private static final int CPU_MAX = 32;

    private static final int MEMORY_MIN = 1024;

    private static final int MEMORY_MAX = 262144;

    private static final int DISK_MIN = 20;

    private static final int DISK_MAX = 2048;

    private static final String DISK_ZERO = "0";

    /**
     * 分组层级分隔符
     */
    private static final String GROUP_SPILT = "/";

    /**
     * 数据行号
     */
    private Integer rowNum;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 记录校验数据不符合规范的国际化描述
     */
    List<String> errorList;

    /**
     * VDI镜像模板
     */
    private String vdiImageTemplateName;

    /**
     * VDI云桌面策略
     */
    private String vdiStrategyName;

    /**
     * 网络策略
     */
    private String vdiNetworkName;


    /**
     * vdi存储位置
     */
    private String vdiStoragePoolName;

    /**
     *vdi运行时位置
     */
    private String vdiClusterName;

    /**
     * 云平台
     */
    private String cloudPlatformName;

    private String vdiCpu;

    private String vdiMemory;

    private String vdiSystemSize;

    private String vdiPersonSize;

    private String vdiPersonDiskStoragePoolName;

    private String vdiVgpuModel;

    protected ImportVDIDeskRowValidator(ImportVDIDeskDTO userGroup, List<String> errorList) {
        Assert.notNull(userGroup, "行不能为null");
        Assert.notNull(errorList, "行不能为null");
        this.errorList = errorList;
        // row中的行数初始行为0，这里补偿+1
        this.rowNum = userGroup.getRowNum() + 1;
        this.userName = userGroup.getUserName();
        this.vdiImageTemplateName = userGroup.getVdiImageTemplateName();
        this.vdiStrategyName = userGroup.getVdiStrategyName();
        this.vdiNetworkName = userGroup.getVdiNetworkName();
        this.vdiClusterName = userGroup.getVdiClusterName();
        this.vdiStoragePoolName = userGroup.getVdiStoragePoolName();
        this.cloudPlatformName = userGroup.getCloudPlatformName();
        this.vdiCpu = userGroup.getVdiCpu();
        this.vdiMemory = userGroup.getVdiMemory();
        this.vdiSystemSize = userGroup.getVdiSystemSize();
        this.vdiPersonSize = userGroup.getVdiPersonSize();
        this.vdiPersonDiskStoragePoolName = userGroup.getVdiPersonDiskStoragePoolName();
        this.vdiVgpuModel = userGroup.getVdiVgpuModel();
    }


    /**
     * 行内容数据校验
     */
    public void validateRowData() {
        String rowNumStr = String.valueOf(rowNum);
        //校验用户名
        validateUserName(rowNumStr);
        //验证VDI镜像模板
        validateImageTemplateName(vdiImageTemplateName, rowNumStr);
        //验证VDI策略
        validateStrategyName(vdiStrategyName, rowNumStr);
        //验证网络策略
        validateNetworkName(vdiNetworkName, rowNumStr);
        //vdi运行时位置
        validateClusterName(vdiClusterName, rowNumStr);
        //云平台
        validateCloudPlatformName(cloudPlatformName, rowNumStr);
        // cpu
        validateCPU(vdiCpu, rowNumStr);
        // Memory
        validateMemory(vdiMemory, rowNumStr);
        // SystemSize
        validateSystemSize(vdiSystemSize, rowNumStr);
        validateStoragePoolName(vdiStoragePoolName, rowNumStr);
        // PersonSize
        validatePersonSize(vdiPersonSize, vdiPersonDiskStoragePoolName, rowNumStr);
    }

    private void validateClusterName(String vdiClusterName, String rowNumStr) {
        if (StringUtils.isEmpty(vdiClusterName)) {
            errorList.add(LocaleI18nResolver.resolve
                    (UserBusinessKey.RCDC_RCO_BATCH_IMPORT_CLUSTER_NAME_NOT_ALLOW_EMPTY, rowNumStr));
        }
    }

    private void validateStoragePoolName(String vdiStoragePoolName, String rowNumStr) {
        if (StringUtils.isEmpty(vdiStoragePoolName)) {
            errorList.add(LocaleI18nResolver.resolve
                    (UserBusinessKey.RCDC_RCO_BATCH_IMPORT_GROUP_SYSTEM_STORAGE_NOT_ALLOW_EMPTY, rowNumStr));
        }
    }

    private void validateCloudPlatformName(String cloudPlatformName, String rowNumStr) {
        if (StringUtils.hasText(cloudPlatformName)) {
            if (!ValidatorUtil.isTextName(cloudPlatformName)) {
                LOGGER.error("云平台[{}]格式错误", cloudPlatformName);
                // 云平台名称格式错误
                errorList.add(
                        LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_CLOUD_PLATFORM_NAME_INCORRECT,
                                rowNumStr, cloudPlatformName));

            }
            if (!ValidatorUtil.isNumberInRangeForInteger(0, ImportVDIDeskValidateRules.STRATEGY_NAME_SIZE,
                    cloudPlatformName.length())) {
                LOGGER.error("云平台[{}]不能超过{}个字符", cloudPlatformName, ImportVDIDeskValidateRules.STRATEGY_NAME_SIZE);
                // 云平台名不能超过ImportVDIDeskValidateRules.STRATEGY_NAME_SIZE个字符
                errorList.add(LocaleI18nResolver
                        .resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_CLOUD_PLATFORM_NAME_TOO_LENGTH, rowNumStr, cloudPlatformName, 
                                String.valueOf(ImportVDIDeskValidateRules.STRATEGY_NAME_SIZE)));
            }
        } else {
            // 云平台名称不能为空
            LOGGER.error("云平台名称[{}]不能为空", cloudPlatformName);
            errorList.add(LocaleI18nResolver.resolve
                    (UserBusinessKey.RCDC_RCO_BATCH_IMPORT_CLOUD_PLATFORM_NAME_NOT_ALLOW_EMPTY, rowNumStr));
        }
    }

    private void validateCPU(String vdiCpu, String rowNumStr) {
        if (StringUtils.isEmpty(vdiCpu)) {
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_CPU_NOT_ALLOW_EMPTY, rowNumStr));
            return;
        }
        if (!Pattern.matches(NUMBER_PATTERN, vdiCpu)) {
            LOGGER.error("第{}行CPU[{}]不合法", rowNumStr, vdiCpu);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_CPU_INCORRECT, rowNumStr, vdiCpu));
            return;
        }
        int cpu = Integer.parseInt(vdiCpu);
        if (cpu < CPU_MIN || cpu > CPU_MAX) {
            LOGGER.error("第{}行CPU[{}]不合法", rowNumStr, vdiCpu);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_CPU_INCORRECT, rowNumStr, vdiCpu));
        }
    }

    private void validateMemory(String vdiMemory, String rowNumStr) {
        if (StringUtils.isEmpty(vdiMemory)) {
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_MEMORY_NOT_ALLOW_EMPTY, rowNumStr));
            return;
        }
        if (!Pattern.matches(MEMORY_PATTERN, vdiMemory)) {
            LOGGER.error("第{}行内存[{}]不合法", rowNumStr, vdiCpu);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_MEMORY_INCORRECT, rowNumStr, vdiMemory));
            return;
        }
        int memory = CapacityUnitUtils.gb2Mb(Double.parseDouble(vdiMemory));
        if (memory < MEMORY_MIN || memory > MEMORY_MAX) {
            LOGGER.error("第{}行内存[{}]不合法", rowNumStr, vdiMemory);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_MEMORY_INCORRECT, rowNumStr, vdiMemory));
        }
    }

    private void validateSystemSize(String vdiSystemSize, String rowNumStr) {
        if (StringUtils.isEmpty(vdiSystemSize)) {
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_SYSTEM_SIZE_NOT_ALLOW_EMPTY, rowNumStr));
            return;
        }
        if (!Pattern.matches(NUMBER_PATTERN, vdiSystemSize)) {
            LOGGER.error("第{}行系统盘[{}]不合法", rowNumStr, vdiSystemSize);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_SYSTEM_SIZE_INCORRECT, rowNumStr, vdiSystemSize));
            return;
        }
        int num = Integer.parseInt(vdiSystemSize);
        if (num < DISK_MIN || num > DISK_MAX) {
            LOGGER.error("第{}行系统盘[{}]不合法", rowNumStr, vdiSystemSize);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_SYSTEM_SIZE_INCORRECT, rowNumStr, vdiSystemSize));
        }
    }

    private void validatePersonSize(String vdiPersonSize, String vdiPersonDiskStoragePoolName, String rowNumStr) {
        if (!StringUtils.hasText(vdiPersonSize)) {
            return;
        }
        if (Objects.equals(DISK_ZERO, vdiPersonSize)) {
            return;
        }
        if (!Pattern.matches(NUMBER_PATTERN, vdiPersonSize)) {
            LOGGER.error("第{}行本地盘[{}]不合法", rowNumStr, vdiPersonSize);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_PERSON_SIZE_INCORRECT, rowNumStr, vdiPersonSize));
            return;
        }
        int num = Integer.parseInt(vdiPersonSize);
        if (num < DISK_MIN || num > DISK_MAX) {
            LOGGER.error("第{}行本地盘[{}]不合法", rowNumStr, vdiPersonSize);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_PERSON_SIZE_INCORRECT, rowNumStr, vdiPersonSize));
            return;
        }
        if (!StringUtils.hasText(vdiPersonDiskStoragePoolName)) {
            LOGGER.error("第{}行本地盘[{}]>0，未填写本地盘存储", rowNumStr, vdiPersonSize);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_PERSON_SIZE_MUST_STORAGE_POOL, rowNumStr, vdiPersonSize));
        }
    }

    private static boolean isNotAllFillDesktopConfig(String... args) {
        boolean enable = false;
        for (String arg : args) {
            if (org.apache.commons.lang3.StringUtils.isBlank(arg)) {
                // 如果为空 说明不是全部填写
                enable = true;
            }
        }
        return enable;
    }

    private void validateNetworkName(String vdiNetworkName, String rowNumStr) {
        if (StringUtils.hasText(vdiNetworkName)) {
            if (!ValidatorUtil.isTextName(vdiNetworkName)) {
                LOGGER.error("网络策略[{}]格式错误", vdiNetworkName);
                // 网络策略名格式错误
                errorList.add(
                        LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_NETWORK_NAME_INCORRECT,
                                new String[] {rowNumStr, vdiNetworkName}));

            }
            if (!ValidatorUtil.isNumberInRangeForInteger(0, ImportVDIDeskValidateRules.NETWORK_NAME_SIZE,
                    vdiNetworkName.length())) {
                LOGGER.error("网络策略[{}]不能超过{}个字符", vdiNetworkName, ImportVDIDeskValidateRules.NETWORK_NAME_SIZE);
                // 网络策略名不能超过ImportVDIDeskValidateRules.NETWORK_NAME_SIZE个字符
                errorList.add(LocaleI18nResolver
                        .resolve(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_NETWORK_NAME_TOO_LENGTH, new String[] {
                            rowNumStr, vdiNetworkName, String.valueOf(ImportVDIDeskValidateRules.NETWORK_NAME_SIZE)}));
            }
        } else {
            // 网络策略不能为空
            LOGGER.error("网络策略名[{}]不能为空", vdiNetworkName);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_NETWORK_NAME_NOT_ALLOW_EMPTY,
                    new String[] {rowNumStr, vdiNetworkName}));
        }
    }

    private void validateStrategyName(String strategyName, String rowNumStr) {
        if (StringUtils.hasText(strategyName)) {
            if (!ValidatorUtil.isTextName(strategyName)) {
                LOGGER.error("云桌面策略[{}]格式错误", strategyName);
                // 云桌面策略名格式错误
                errorList.add(
                        LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_STRATEGY_NAME_INCORRECT,
                                new String[] {rowNumStr, strategyName}));

            }
            if (!ValidatorUtil.isNumberInRangeForInteger(0, ImportVDIDeskValidateRules.STRATEGY_NAME_SIZE,
                    strategyName.length())) {
                LOGGER.error("云桌面策略[{}]不能超过{}个字符", strategyName, ImportVDIDeskValidateRules.STRATEGY_NAME_SIZE);
                // 云桌面策略名不能超过ImportVDIDeskValidateRules.STRATEGY_NAME_SIZE个字符
                errorList.add(LocaleI18nResolver
                        .resolve(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_STRATEGY_NAME_TOO_LENGTH, new String[] {
                            rowNumStr, strategyName, String.valueOf(ImportVDIDeskValidateRules.STRATEGY_NAME_SIZE)}));
            }
        } else {
            // 云桌面策略不能为空
            LOGGER.error("云桌面策略名[{}]不能为空", strategyName);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_STRATEGY_NAME_NOT_ALLOW_EMPTY,
                    new String[] {rowNumStr, strategyName}));
        }
    }



    private void validateImageTemplateName(String imageTemplateName, String rowNumStr) {
        if (StringUtils.hasText(imageTemplateName)) {
            if (!ValidatorUtil.isTextName(imageTemplateName)) {
                LOGGER.error("镜像模板[{}]格式错误", imageTemplateName);
                // 镜像模板名格式错误
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_IMAGE_TEMPLATE_NAME_INCORRECT, rowNumStr,
                        imageTemplateName));

            }
            if (imageTemplateName.length() > ImportVDIDeskValidateRules.IMAGE_TEMPLATE_NAME_SIZE) {
                // 镜像模板名不能超过ImportVDIDeskValidateRules.IMAGE_TEMPLATE_NAME_SIZE个字符
                LOGGER.error("镜像模板名长度不能超过{}个字符", ImportVDIDeskValidateRules.IMAGE_TEMPLATE_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_IMAGE_TEMPLATE_NAME_TOO_LENGTH, rowNumStr,
                        imageTemplateName, String.valueOf(ImportVDIDeskValidateRules.IMAGE_TEMPLATE_NAME_SIZE)));
            }
        } else {
            // 镜像模板名不能为空
            LOGGER.error("镜像模板名[{}]不能为空", imageTemplateName);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_IMAGE_TEMPLATE_NAME_NOT_ALLOW_EMPTY, rowNumStr,
                    imageTemplateName));
        }
    }

    /**
     * 校验用户名
     * @param rowNumStr
     */
    private void validateUserName(String rowNumStr) {
        if (StringUtils.hasText(userName)) {
            if (!userName.matches(UserValidateRules.USER_NAME)) {
                LOGGER.error("用户名[{}]格式错误", userName);
                // 用户名格式错误
                errorList
                        .add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_USERNAME_INCORRECT, new String[] {rowNumStr, userName}));
            }
            if (userName.length() > UserValidateRules.USER_NAME_SIZE) {
                // 用户名不能超过UserValidateRules.USER_NAME_SIZE个字符
                LOGGER.error("用户名长度不能超过{}个字符", UserValidateRules.USER_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_USERNAME_TOO_LENGTH,
                        new String[] {rowNumStr, userName, String.valueOf(UserValidateRules.USER_NAME_SIZE)}));
            }
            // 用户名不能使用保留字段 不进行校验 有就导入桌面 没有就不导入
        } else {
            // 用户名不能为空
            LOGGER.error("用户名[{}]不能为空", userName);
            errorList.add(
                    LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_USERNAME_NOT_ALLOW_EMPTY, new String[] {rowNumStr, userName}));
        }
    }
}
