package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import com.ruijie.rcos.rcdc.rco.module.web.util.CapacityUnitUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.constants.UserGroupValidateConstants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportUserGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;

/**
 * Description: 导入的用户组数据行校验类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/23
 *
 * @author zhangyichi
 */
public class UserGroupRowValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupRowValidator.class);

    /**
     * 分组层级分隔符
     */
    private static final String GROUP_SPILT = "/";

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String INVALID_PATTERN = "^[0-9]*$";

    private static final int INVALID_TIME_MAX_VALUE = 1000;

    private static final int INVALID_TIME_MIN_VALUE = 0;

    private static final String MEMORY_PATTERN = "^[1-9]([0-9])*([\\.]5)?$";

    private static final String NUMBER_PATTERN = "^[1-9]\\d*$";

    private static final int CPU_MIN = 1;

    private static final int CPU_MAX = 32;

    private static final int MEMORY_MIN = 1024;

    private static final int MEMORY_MAX = 262144;

    private static final int DISK_MIN = 20;

    private static final int DISK_MAX = 2048;

    private static final String DISK_ZERO = "0";

    /**
     * 数据行号
     */
    private Integer rowNum;

    private String userGroupName;

    /**
     * 用户组名，有多级
     */
    private List<String> userGroupNameList = Lists.newArrayList();

    /**
     * 记录校验数据不符合规范的国际化描述
     */
    List<String> errorList;

    private String vdiStoragePoolName;

    private String vdiClusterName;

    private String vdiImageTemplateName;

    private String vdiStrategyName;

    private String vdiNetworkName;

    private String idvImageTemplateName;

    private String idvStrategyName;

    private String voiImageTemplateName;

    private String voiStrategyName;

    private String accountExpireDate;

    private String invalidTime;

    private String cloudPlatformName;

    private String vdiCpu;

    private String vdiMemory;

    private String vdiSystemSize;

    private String vdiPersonSize;

    private String vdiPersonDiskStoragePoolName;

    private String vdiVgpuModel;

    protected UserGroupRowValidator(ImportUserGroupDTO userGroup, List<String> errorList) {
        Assert.notNull(userGroup, "行不能为null");
        Assert.notNull(errorList, "行不能为null");
        this.errorList = errorList;
        // row中的行数初始行为0，这里补偿+1
        this.rowNum = userGroup.getRowNum() + 1;
        this.userGroupName = userGroup.getGroupNames();
        this.vdiStoragePoolName = userGroup.getVdiStoragePoolName();
        this.vdiClusterName = userGroup.getVdiClusterName();
        this.setUserGroupNameList(userGroupName);
        this.vdiImageTemplateName = userGroup.getVdiImageTemplateName();
        this.vdiStrategyName = userGroup.getVdiStrategyName();
        this.vdiNetworkName = userGroup.getVdiNetworkName();
        this.idvImageTemplateName = userGroup.getIdvImageTemplateName();
        this.idvStrategyName = userGroup.getIdvStrategyName();
        this.voiImageTemplateName = userGroup.getVoiImageTemplateName();
        this.voiStrategyName = userGroup.getVoiStrategyName();
        this.accountExpireDate = userGroup.getAccountExpireDate();
        this.invalidTime = userGroup.getInvalidTime();
        this.cloudPlatformName = userGroup.getCloudPlatformName();
        this.vdiCpu = userGroup.getVdiCpu();
        this.vdiMemory = userGroup.getVdiMemory();
        this.vdiSystemSize = userGroup.getVdiSystemSize();
        this.vdiPersonSize = userGroup.getVdiPersonSize();
        this.vdiPersonDiskStoragePoolName = userGroup.getVdiPersonDiskStoragePoolName();
        this.vdiVgpuModel = userGroup.getVdiVgpuModel();
    }

    private void setUserGroupNameList(String userGroupName) {
        if (StringUtils.hasText(userGroupName)) {
            String[] groupNameArr = userGroupName.split(GROUP_SPILT);
            userGroupNameList.addAll(Arrays.asList(groupNameArr));
        }
    }

    /**
     * 行数据校验
     *
     * @throws BusinessException 业务异常
     */
    public void validateRowData() throws BusinessException {
        String rowNumStr = String.valueOf(rowNum);

        validateUserGroup(rowNumStr);
        // vdi、idv、voi桌面配置信息要么都填，要么都不填
        if (isPartDesktopConfig(cloudPlatformName, vdiImageTemplateName, vdiStrategyName, vdiNetworkName, vdiClusterName, vdiStoragePoolName, vdiCpu,
                vdiMemory, vdiSystemSize)
                || isPartDesktopConfig(idvImageTemplateName, idvStrategyName)
                || isPartDesktopConfig(voiImageTemplateName, voiStrategyName)) {
            // 不允许部分配置
            errorList.add(LocaleI18nResolver.resolve(
                    UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_INCOMPLETE_CONFIG_DESKTOP, new String[]{rowNumStr}));
        }
        validateImageTemplateName(UserCloudDeskTypeEnum.VDI, vdiImageTemplateName, rowNumStr);

        validateStrategyName(vdiStrategyName, rowNumStr);

        validateNetworkName(rowNumStr);

        validateImageTemplateName(UserCloudDeskTypeEnum.IDV, idvImageTemplateName, rowNumStr);

        validateStrategyName(idvStrategyName, rowNumStr);

        validateImageTemplateName(UserCloudDeskTypeEnum.VOI, voiImageTemplateName, rowNumStr);

        validateStrategyName(voiStrategyName, rowNumStr);

        validateExpireDateFormat(accountExpireDate, rowNumStr);

        validateInvalidTime(invalidTime, rowNumStr);

        validateCloudPlatformName(cloudPlatformName, rowNumStr);

        validateCPU(vdiCpu, rowNumStr);
        validateMemory(vdiMemory, rowNumStr);
        validateSystemSize(vdiSystemSize, vdiStoragePoolName, rowNumStr);
        validatePersonSize(vdiPersonSize, vdiPersonDiskStoragePoolName, rowNumStr);
    }

    private void validateVdiClusterName(String vdiImageTemplateName, String vdiClusterName, String rowNumStr) {
        if (StringUtils.hasText(vdiImageTemplateName) && StringUtils.isEmpty(vdiClusterName)) {
            errorList.add(LocaleI18nResolver.resolve
                    (UserBusinessKey.RCDC_RCO_BATCH_IMPORT_CLUSTER_NAME_NOT_ALLOW_EMPTY, rowNumStr));
        }
    }

    private static boolean isPartDesktopConfig(String... args) {
        int emptyCount = 0;
        for (String arg : args) {
            if (StringUtils.isEmpty(arg)) {
                emptyCount++;
            }
        }
        return emptyCount > 0 && emptyCount < args.length;
    }

    private void validateNetworkName(String rowNumStr) {
        if (StringUtils.hasText(vdiNetworkName)) {
            if (!ValidatorUtil.isTextName(vdiNetworkName)) {
                LOGGER.error("网络策略[{}]格式错误", vdiNetworkName);
                // 网络策略名格式错误
                errorList.add(
                        LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_NETWORK_NAME_INCORRECT,
                                new String[]{rowNumStr, vdiNetworkName}));

            }
            if (!ValidatorUtil.isNumberInRangeForInteger(0, UserGroupValidateRules.NETWORK_NAME_SIZE,
                    vdiNetworkName.length())) {
                LOGGER.error("网络策略[{}]不能超过{}个字符", vdiNetworkName, UserGroupValidateRules.NETWORK_NAME_SIZE);
                // 网络策略名不能超过UserGroupValidateRules.NETWORK_NAME_SIZE个字符
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_NETWORK_NAME_TOO_LENGTH,
                        new String[]{rowNumStr, vdiNetworkName, String.valueOf(UserGroupValidateRules.NETWORK_NAME_SIZE)}));
            }
        }
    }

    private void validateStrategyName(String strategyName, String rowNumStr) {
        if (StringUtils.hasText(strategyName)) {
            if (!ValidatorUtil.isTextName(strategyName)) {
                LOGGER.error("云桌面策略[{}]格式错误", strategyName);
                // 云桌面策略名格式错误
                errorList.add(
                        LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_STRATEGY_NAME_INCORRECT,
                                new String[]{rowNumStr, strategyName}));

            }
            if (!ValidatorUtil.isNumberInRangeForInteger(0, UserGroupValidateRules.STRATEGY_NAME_SIZE,
                    strategyName.length())) {
                LOGGER.error("云桌面策略[{}]不能超过{}个字符", strategyName, UserGroupValidateRules.STRATEGY_NAME_SIZE);
                // 云桌面策略名不能超过UserGroupValidateRules.STRATEGY_NAME_SIZE个字符
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_STRATEGY_NAME_TOO_LENGTH,
                        new String[]{rowNumStr, strategyName, String.valueOf(UserGroupValidateRules.STRATEGY_NAME_SIZE)}));
            }
        }
    }

    private void validateUserGroup(String rowNumStr) {
        if (CollectionUtils.isEmpty(userGroupNameList)) {
            // 用户组名不能为空
            LOGGER.error("第{}行用户组名不能为空", rowNumStr);
            errorList.add(LocaleI18nResolver.resolve(
                    UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_GROUP_NAME_NOT_ALLOW_EMPTY, new String[]{rowNumStr}));
            return;
        }
        // 分组中间不能有空字符串，即父分组不能为空字符串
        for (String name : userGroupNameList) {
            String nameNotSpace = name.trim();
            if (nameNotSpace.equals("")) {
                LOGGER.error("第{}行用户组父分组不能为空", rowNumStr);
                errorList.add(LocaleI18nResolver.resolve(
                        UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_PARENT_GROUP_NOT_ALLOWED_EMPTY,
                        new String[]{rowNumStr, userGroupName}));
                break;
            }
        }
        if (userGroupNameList.size() > UserGroupValidateConstants.MAX_HIERARCHY_NUM) {
            // 用户组层级不能超过系统最多支持的9级
            LOGGER.error("第{}行用户组层级不能超过系统最多支持的{}级", rowNumStr, UserGroupValidateConstants.MAX_HIERARCHY_NUM);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_GROUP_OVER_COUNT,
                    new String[]{rowNumStr, userGroupName,
                            String.valueOf(UserGroupValidateConstants.MAX_HIERARCHY_NUM)}));
        }
        for (String name : userGroupNameList) {
            String nameNotSpace = name.trim();
            if (nameNotSpace.equals("")) {
                continue;
            }
            if (!ValidatorUtil.isTextName(name)) {
                // 用户组名格式错误
                LOGGER.error("用户组名[{}]格式错误", name);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_GROUPNAME_INCORRECT,
                        new String[]{rowNumStr, name}));
            }
            if (!ValidatorUtil.isNumberInRangeForInteger(0, UserGroupValidateRules.USER_GROUP_NAME_SIZE,
                    name.length())) {
                // 用户组名不能超过UserGroupValidateRules.USER_GROUP_NAME_SIZE个字符
                LOGGER.error("用户组名长度不能超过{}个字符", UserGroupValidateRules.USER_GROUP_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(
                        UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_GROUPNAME_TOO_LENGTH,
                        new String[]{rowNumStr, name, String.valueOf(UserGroupValidateRules.USER_GROUP_NAME_SIZE)}));
            }
            if (UserGroupValidateRules.DEFAULT_USER_GROUP_NAMES.contains(name)) {
                // 用户组名不能含有保留字
                LOGGER.error("第{}行用户组名[{}]不能包含保留字段{}", rowNumStr,
                        UserGroupValidateRules.DEFAULT_USER_GROUP_NAMES.toString());
                errorList.add(LocaleI18nResolver.resolve(
                        UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_GROUPNAME_NOT_ALLOW_RESERVED,
                        new String[]{rowNumStr, name}));
            }
        }
    }

    private void validateImageTemplateName(UserCloudDeskTypeEnum userCloudDeskTypeEnum, String imageTemplateName, String rowNumStr) {
        if (StringUtils.hasText(imageTemplateName)) {
            if (!ValidatorUtil.isTextName(imageTemplateName)) {
                LOGGER.error("镜像模板[{}]格式错误", imageTemplateName);
                // 镜像模板名格式错误
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_IMAGE_TEMPLATE_NAME_INCORRECT, rowNumStr,
                        imageTemplateName));

            }

            long allowLength = userCloudDeskTypeEnum == UserCloudDeskTypeEnum.VDI ? UserGroupValidateRules.VDI_IMAGE_TEMPLATE_NAME_SIZE
                    : UserGroupValidateRules.IMAGE_TEMPLATE_NAME_SIZE;

            if (imageTemplateName.length() > allowLength) {
                // 镜像模板名不能超过UserGroupValidateRules.IMAGE_TEMPLATE_NAME_SIZE个字符
                LOGGER.error("镜像模板名长度不能超过{}个字符", allowLength);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_IMAGE_TEMPLATE_NAME_TOO_LENGTH, rowNumStr,
                        imageTemplateName, String.valueOf(allowLength)));
            }
        }
    }

    private void validateExpireDateFormat(String accountExpireDate, String rowNumStr) throws BusinessException {
        if (ObjectUtils.isEmpty(accountExpireDate)) {
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date expireDate = simpleDateFormat.parse(accountExpireDate);
            if (expireDate.getTime() < new Date().getTime()) {
                LOGGER.error("第[{}]行过期时间[{}]小于当前时间[{}]", new String[]{DateUtil.formatDate(expireDate, DATE_FORMAT),
                        DateUtil.formatDate(new Date(), DATE_FORMAT)});
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_VALIDATE_EXPIRE_DATE_INCORRECT, rowNumStr,
                        DateUtil.formatDate(expireDate, DATE_FORMAT), DateUtil.formatDate(new Date(), DATE_FORMAT)));
            }
        } catch (ParseException e) {
            LOGGER.error("第[{}]行过期时间[{}]格式解析错误,e:{}", rowNumStr, accountExpireDate, e);
            errorList.add(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_IMPORT_EXPIRE_FORMAT_ERROR, rowNumStr, accountExpireDate));
        }
    }

    private void validateInvalidTime(String invalidTime, String rowNumStr) throws BusinessException {
        if (ObjectUtils.isEmpty(invalidTime)) {
            return;
        }

        try {
            Integer invalidDate = Integer.parseInt(invalidTime);
            if (invalidDate > INVALID_TIME_MAX_VALUE || invalidDate < INVALID_TIME_MIN_VALUE) {
                LOGGER.error("第[{}]行失效天数[{}]不在规定范围", rowNumStr, invalidTime);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_INVALID_TIME_VALIDATE_FAIL, rowNumStr, invalidTime));
            }
        } catch (NumberFormatException e) {
            LOGGER.error("第[{}]行失效天数[{}]不在规定范围,e:{}", rowNumStr, invalidTime, e);
            errorList.add(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_INVALID_TIME_VALIDATE_RESOLVE_FAIL, rowNumStr, invalidTime));
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
        }
    }

    private void validateCPU(String vdiCpu, String rowNumStr) {
        if (StringUtils.isEmpty(vdiCpu)) {
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

    private void validateSystemSize(String vdiSystemSize, String vdiStoragePoolName, String rowNumStr) {
        if (!StringUtils.hasText(vdiSystemSize)) {
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
            return;
        }
        if (!StringUtils.hasText(vdiStoragePoolName)) {
            LOGGER.error("第{}行系统盘[{}]，必须填写系统盘存储池", rowNumStr, vdiSystemSize);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_HAS_SYSTEM_SIZE_MUST_STORAGE_POOL, rowNumStr));
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
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_BATCH_IMPORT_PERSON_SIZE_MUST_STORAGE_POOL, rowNumStr));
        }
    }
}
