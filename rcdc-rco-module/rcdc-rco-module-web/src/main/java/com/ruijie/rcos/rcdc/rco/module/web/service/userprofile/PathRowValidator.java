package com.ruijie.rcos.rcdc.rco.module.web.service.userprofile;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.UserProfileHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.dto.ImportUserProfilePathDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Description: 每行路径数据校验
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/26
 *
 * @author WuShengQiang
 */
public class PathRowValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PathRowValidator.class);

    protected PathRowValidator(ImportUserProfilePathDTO pathDTO, List<String> errorList) {
        Assert.notNull(pathDTO, "pathDTO不能为null");
        Assert.notNull(errorList, "errorList不能为null");
        this.errorList = errorList;
        // row中的行数初始行为0，这里补偿+1
        this.rowNum = pathDTO.getRowNum() + 1;
        this.name = pathDTO.getName();
        this.description = pathDTO.getDescription();
        this.groupName = pathDTO.getGroupName();
        this.groupDescription = pathDTO.getGroupDescription();
        this.mode = pathDTO.getMode();
        this.type = pathDTO.getType();
        this.path = pathDTO.getPath();
    }

    /**
     * 记录校验数据不符合规范的国际化描述
     */
    List<String> errorList;

    /**
     * 当前行
     */
    private Integer rowNum;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 组描述
     */
    private String groupDescription;

    /**
     * 路径名称
     */
    private String name;

    /**
     * 配置方式(同步/排除)
     */
    private String mode;

    /**
     * 类型(文件夹/文件/注册表)
     */
    private String type;

    /**
     * 路径
     */
    private String path;

    /**
     * 描述
     **/
    private String description;

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 行内容数据校验
     */
    public void validateRowData() {
        String rowNumStr = String.valueOf(rowNum);
        validatePathGroup(rowNumStr);
        validatePathName(rowNumStr);
        validatePathInfo(rowNumStr);
    }

    private void validatePathGroup(String rowNumStr) {
        if (StringUtils.hasText(groupName)) {
            if (groupName.length() > PathValidateRules.NAME_SIZE) {
                errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_GROUP_NAME_TOO_LENGTH, rowNumStr,
                        String.valueOf(PathValidateRules.NAME_SIZE)));
            }
            if (!ValidatorUtil.isTextName(groupName)) {
                errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_GROUP_NAME_INCORRECT, rowNumStr, groupName));
            }
        }

        if (groupDescription.length() > PathValidateRules.DESC_SIZE) {
            errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_GROUP_DESC_TOO_LENGTH, rowNumStr
                    , String.valueOf(PathValidateRules.DESC_SIZE)));
        }
    }

    private void validatePathName(String rowNumStr) {
        if (StringUtils.hasText(name)) {
            if (name.length() > PathValidateRules.NAME_SIZE) {
                errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_NAME_TOO_LENGTH, rowNumStr,
                        String.valueOf(PathValidateRules.NAME_SIZE)));
            }
            if (!ValidatorUtil.isTextName(name)) {
                errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_NAME_INCORRECT, rowNumStr, name));
            }
        } else {
            errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_NAME_NOT_ALLOW_EMPTY, rowNumStr));
        }
        if (description.length() > PathValidateRules.DESC_SIZE) {
            errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_DESC_TOO_LENGTH, rowNumStr,
                    String.valueOf(PathValidateRules.DESC_SIZE)));
        }
    }

    private void validatePathInfo(String rowNumStr) {
        if (StringUtils.hasText(mode)) {
            if (!UserProfilePathModeEnum.isValid(mode)) {
                errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_MODE_NOT_ALLOW_OTHER_WORDS, rowNumStr, name,
                        mode));
            }
        } else {
            errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_MODE_NOT_ALLOW_EMPTY, rowNumStr));
        }

        UserProfilePathTypeEnum typeEnum = null;
        if (StringUtils.hasText(type)) {
            if (!UserProfilePathTypeEnum.isValid(type)) {
                errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_TYPE_NOT_ALLOW_OTHER_WORDS, rowNumStr, name,
                        type));
            } else {
                typeEnum = UserProfilePathTypeEnum.valueOfText(type);
            }
        } else {
            errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_TYPE_NOT_ALLOW_EMPTY, rowNumStr));
        }

        if (StringUtils.hasText(path)) {
            if (path.length() > PathValidateRules.PATH_SIZE) {
                errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_PATH_TOO_LENGTH, rowNumStr,
                        String.valueOf(PathValidateRules.PATH_SIZE)));
            }
            // 根据不同的类型校验路径格式
            if (typeEnum != null) {
                try {
                    UserProfileHelper.pathValidate(path, typeEnum);
                } catch (BusinessException e) {
                    LOGGER.error("路径[{}]格式错误,原因:{}", path, e.getI18nMessage());
                    errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_PATH_INCORRECT, rowNumStr, path,
                            e.getI18nMessage()));
                }
            }
        } else {
            errorList.add(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_PATH_NOT_ALLOW_EMPTY, rowNumStr));
        }

    }

}