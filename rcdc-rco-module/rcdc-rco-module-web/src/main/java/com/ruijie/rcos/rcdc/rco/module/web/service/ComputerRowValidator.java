package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.ComputerBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportComputerDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Description: 导入的PC数据行校验类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/19
 *
 * @author zqj
 */
public class ComputerRowValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerRowValidator.class);


    /**
     * 分组层级分隔符
     */
    private static final String GROUP_SPILT = "/";


    private static final int DESCRIPTION_MAX_LENGTH = 128;


    /**
     * 数据行号
     */
    private Integer rowNum;

    /**
     * 组名，有多级
     */
    private List<String> groupNameList = Lists.newArrayList();

    /**
     * 记录校验数据不符合规范的国际化描述
     */
    List<String> errorList;

    private String name;

    private String groupNames;

    private String userName;

    private String ip;

    private String remark;

    private String deskStrategy;

    protected ComputerRowValidator(ImportComputerDTO computerDTO, List<String> errorList) {
        Assert.notNull(computerDTO, "行不能为null");
        Assert.notNull(errorList, "行不能为null");
        this.errorList = errorList;
        // row中的行数初始行为0，这里补偿+1
        this.rowNum = computerDTO.getRowNum() + 1;
        this.groupNames = computerDTO.getGroupNames();
        this.setGroupNameList(groupNames);
        this.userName = computerDTO.getUserName();
        this.name = computerDTO.getName();
        this.remark = computerDTO.getRemark();
        this.deskStrategy = computerDTO.getDeskStrategy();
        this.ip = computerDTO.getIp();
    }

    private void setGroupNameList(String userGroupName) {
        if (StringUtils.hasText(userGroupName)) {
            String[] groupNameArr = userGroupName.split(GROUP_SPILT);
            groupNameList.addAll(Arrays.asList(groupNameArr));
        }
    }

    /**
     * 行内容数据校验
     *
     * @throws BusinessException 业务异常
     */
    public void validateRowData() throws BusinessException {
        String rowNumStr = String.valueOf(rowNum);

        validateUserGroup(rowNumStr);

        validateIp(rowNumStr);

        validateDescription(rowNumStr);

    }

    private void validateIp(String rowNumStr) {
        if (StringUtils.isEmpty(ip)) {
            LOGGER.error("第{}行IP不能为空", rowNumStr);
            errorList.add(LocaleI18nResolver.resolve(ComputerBusinessKey.RCDC_RCO_COMPUTER_IMPORT_IP_NOT_EMPTY,
                    rowNumStr));
        } else if (!ip.matches(ComputerValidateRules.IP)) {
            // 终端ip格式错误
            LOGGER.error("第{}行IP[{}]格式错误", rowNumStr, ip);
            errorList.add(LocaleI18nResolver.resolve(ComputerBusinessKey.RCDC_RCO_COMPUTER_IMPORT_IP_INCORRECT,
                    rowNumStr, ip));
        }
    }


    private void validateUserGroup(String rowNumStr) {
        if (CollectionUtils.isEmpty(groupNameList)) {
            LOGGER.debug("第[{}]行数据分组为空，不校验", rowNumStr);
            return;
        }
        // 分组中间不能有空字符串，即父分组不能为空字符串
        for (String name : groupNameList) {
            String nameNotSpace = name.trim();
            if (nameNotSpace.equals("")) {
                LOGGER.error("第{}行终端组父分组不能为空", rowNumStr);
                errorList.add(LocaleI18nResolver.resolve(ComputerBusinessKey.RCDC_RCO_COMPUTER_IMPORT_USERGROUP_NOT_EMPTY,
                        rowNumStr, groupNames));
                break;
            }
        }
        for (String name : groupNameList) {
            String nameNotSpace = name.trim();
            if (nameNotSpace.equals("")) {
                continue;
            }
            if (!name.matches(ComputerValidateRules.GROUP_NAME)) {
                // 终端组名格式错误
                LOGGER.error("第{}行终端组名[{}]格式错误", rowNumStr, userName);
                errorList.add(LocaleI18nResolver.resolve(ComputerBusinessKey.RCDC_RCO_COMPUTER_IMPORT_GROUPNAME_INCORRECT,
                        rowNumStr, name));
            }
            if (name.length() > ComputerValidateRules.GROUP_NAME_SIZE) {
                // 终端组名不能超过UserValidateRules.USER_GROUP_NAME_SIZE个字符
                LOGGER.error("第{}行终端组名长度不能超过{}个字符", rowNumStr, ComputerValidateRules.GROUP_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(ComputerBusinessKey.RCDC_RCO_COMPUTER_IMPORT_GROUPNAME_TOO_LENGTH,
                        rowNumStr, name, String.valueOf(ComputerValidateRules.GROUP_NAME_SIZE)));
            }
            if (ComputerValidateRules.DEFAULT_USER_GROUP_NAMES.contains(name)) {
                // 终端组名不能含有保留字
                LOGGER.error("第{}行终端组名[{}]不能包含保留字段{}", rowNumStr, ComputerValidateRules.DEFAULT_USER_GROUP_NAMES.toString());
                errorList.add(
                        LocaleI18nResolver.resolve(ComputerBusinessKey.RCDC_RCO_COMPUTER_IMPORT_GROUPNAME_NOT_ALLOW_RESERVED,
                                rowNumStr, name));
            }
        }
    }


    private void validateDescription(String rowNumStr) {
        if (StringUtils.isEmpty(remark)) {
            return;
        }
        if (remark.length() > DESCRIPTION_MAX_LENGTH) {
            LOGGER.error("第[{}]行描述信息长度超过限制", rowNumStr);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_DESCRIPTION_VALIDATE_FAIL));
        }
    }
}
