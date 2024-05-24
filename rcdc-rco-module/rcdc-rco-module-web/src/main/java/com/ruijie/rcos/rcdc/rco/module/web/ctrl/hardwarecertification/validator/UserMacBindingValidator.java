package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.validator;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.HardwareCertificationBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.dto.ImportUserMacBindingDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.enums.UserMacBindingHeader;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Description: 导入用户-mac绑定关系参数校验
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/23 19:25
 *
 * @author yxq
 */
public class UserMacBindingValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMacBindingValidator.class);

    private final ImportUserMacBindingDTO importUserMacBindingDTO;

    /**
     * 记录校验数据不符合规范的国际化描述
     */
    private final List<String> errorList;

    private final String rowNum;

    public UserMacBindingValidator(ImportUserMacBindingDTO importUserMacBindingDTO, List<String> errorList) {
        this.importUserMacBindingDTO = importUserMacBindingDTO;
        this.errorList = errorList;
        this.rowNum = String.valueOf(importUserMacBindingDTO.getRowNum() + 1);
    }


    /**
     * 进行参数校验
     */
    public void validate() {
        validateUserName(rowNum, importUserMacBindingDTO.getUserName());
        validateTerminalMac(rowNum, importUserMacBindingDTO.getTerminalMac());
    }

    private void validateTerminalMac(String rowNum, String terminalMac) {
        if (StringUtils.isBlank(terminalMac)) {
            LOGGER.error("第[{}]行mac地址不能为空", rowNum);
            errorList.add(LocaleI18nResolver.resolve(HardwareCertificationBusinessKey.RCDC_IMPORT_USER_MAC_NOT_ALLOW_EMPTY, rowNum,
                    UserMacBindingHeader.TERMINAL_MAC.getHeader()));
        }
    }

    private void validateUserName(String rowNum, String userName) {
        if (StringUtils.isBlank(userName)) {
            LOGGER.error("第[{}]行用户名不能为空", rowNum);
            errorList.add(LocaleI18nResolver.resolve(HardwareCertificationBusinessKey.RCDC_IMPORT_USER_MAC_NOT_ALLOW_EMPTY, rowNum,
                    UserMacBindingHeader.USER_NAME.getHeader()));
        }
    }
}
