package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportUserDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 导入的用户数据处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/21
 *
 * @author Jarman
 */
@Service
public class ImportUserHandler extends AbstractImportTemplateHandler<ImportUserDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportUserHandler.class);

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    /**
     * 数据起始行数
     */
    private static final int USER_DATA_START_ROW = 3;

    private static final String EMAIL_SPLIT_CHAR = "；";

    @Override
    protected List<ImportUserDTO> doReadData(Sheet sheet) throws BusinessException {
        try {
            return getImportUserDTOList(sheet);
        } catch (Exception e) {
            LOGGER.error("解析用户模板失败", e);
            if (!(e instanceof BusinessException)) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IMPORT_USER_ANALYSIS_FAIL, e);
            }
            throw e;
        }
    }

    private List<ImportUserDTO> getImportUserDTOList(Sheet sheet) throws BusinessException {
        boolean isVDIModel = serverModelAPI.isVdiModel();
        boolean isUserTemplate = isVDIModel ? ExcelUtils.isUserTemplate(sheet) : ExcelUtils.isUserTemplateOnlyForIDV(sheet);
        if (!isUserTemplate) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_TEMPLATE_IS_INVALIDATE);
        }

        Row tittleRow = sheet.getRow(2);
        validateUserTemplate(tittleRow, UserExcelField.GROUP_NAME.getIndex(), UserExcelField.GROUP_NAME.getHeader());
        validateUserTemplate(tittleRow, UserExcelField.NAME.getIndex(), UserExcelField.NAME.getHeader());
        validateUserTemplate(tittleRow, UserExcelField.REAL_NAME.getIndex(), UserExcelField.REAL_NAME.getHeader());
        validateUserTemplate(tittleRow, UserExcelField.PHONE.getIndex(), UserExcelField.PHONE.getHeader());
        validateUserTemplate(tittleRow, UserExcelField.EMAIL.getIndex(), UserExcelField.EMAIL.getHeader());
        validateUserTemplate(tittleRow, UserExcelField.USER_STATE.getIndex(), UserExcelField.USER_STATE.getHeader());
        validateUserTemplate(tittleRow, UserExcelField.OPTCERTIFICATION.getIndex(), UserExcelField.OPTCERTIFICATION.getHeader());
        validateUserTemplate(tittleRow, UserExcelField.PASSWORD.getIndex(), UserExcelField.PASSWORD.getHeader());
        validateUserTemplate(tittleRow, UserExcelField.ACCOUNTEXPIREDATE.getIndex(), UserExcelField.ACCOUNTEXPIREDATE.getHeader());
        validateUserTemplate(tittleRow, UserExcelField.INVALIDETIME.getIndex(), UserExcelField.INVALIDETIME.getHeader());
        validateUserTemplate(tittleRow, UserExcelField.DESCRIPTION.getIndex(), UserExcelField.DESCRIPTION.getHeader());
        if (isVDIModel) {
            validateUserTemplate(tittleRow, UserExcelField.HARDWARECERTIFICATION.getIndex(), UserExcelField.HARDWARECERTIFICATION.getHeader());
            validateUserTemplate(tittleRow, UserExcelField.SMS_CERTIFICATION.getIndex(), UserExcelField.SMS_CERTIFICATION.getHeader());
            validateUserTemplate(tittleRow, UserExcelField.RADIUSCERTIFICATION.getIndex(), UserExcelField.RADIUSCERTIFICATION.getHeader());
            validateUserTemplate(tittleRow, UserExcelField.ACCOUNTPASSWORDCERTIFICATION.getIndex(),
                    UserExcelField.ACCOUNTPASSWORDCERTIFICATION.getHeader());
            validateUserTemplate(tittleRow, UserExcelField.WORKWEIXINCERTIFICATION.getIndex(),
                    UserExcelField.WORKWEIXINCERTIFICATION.getHeader());
            validateUserTemplate(tittleRow, UserExcelField.FEISHUCERTIFICATION.getIndex(), UserExcelField.FEISHUCERTIFICATION.getHeader());
            validateUserTemplate(tittleRow, UserExcelField.DINGDINGCERTIFICATION.getIndex(), UserExcelField.DINGDINGCERTIFICATION.getHeader());
            validateUserTemplate(tittleRow, UserExcelField.OAUTH2CERTIFICATION.getIndex(), UserExcelField.OAUTH2CERTIFICATION.getHeader());
        }
        int rowNum = sheet.getPhysicalNumberOfRows();
        List<ImportUserDTO> userList = new ArrayList<>(rowNum);
        for (int i = USER_DATA_START_ROW; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            // 忽略掉空行
            if (ExcelUtils.isNullRow(row)) {
                break;
            }
            ImportUserDTO importUserDTO = new ImportUserDTO();
            importUserDTO.setRowNum(i);
            String groupName = ExcelUtils.getCellValue(row, UserExcelField.GROUP_NAME);
            String userName = ExcelUtils.getCellValue(row, UserExcelField.NAME);
            String realName = ExcelUtils.getCellValue(row, UserExcelField.REAL_NAME);
            String phoneNum = ExcelUtils.getCellValue(row, UserExcelField.PHONE);
            String email = ExcelUtils.getCellValue(row, UserExcelField.EMAIL);
            String userState = ExcelUtils.getCellValue(row, UserExcelField.USER_STATE);
            String openOtpCertification = ExcelUtils.getCellValue(row, UserExcelField.OPTCERTIFICATION);
            String password = ExcelUtils.getCellValue(row, UserExcelField.PASSWORD);
            String expireDate = ExcelUtils.getCellValue(row, UserExcelField.ACCOUNTEXPIREDATE);
            String invalidTime = ExcelUtils.getCellValue(row, UserExcelField.INVALIDETIME);
            String description = ExcelUtils.getCellValue(row, UserExcelField.DESCRIPTION);

            if (isVDIModel) {
                String openHardwareCertification = ExcelUtils.getCellValue(row, UserExcelField.HARDWARECERTIFICATION);
                String maxHardwareNum = ExcelUtils.getCellValue(row, UserExcelField.MAX_HARDWARE_NUM);
                String openSmsCertification = ExcelUtils.getCellValue(row, UserExcelField.SMS_CERTIFICATION);
                String openRadiusCertification = ExcelUtils.getCellValue(row, UserExcelField.RADIUSCERTIFICATION);
                // 动态口令与Radius只能二选一
                validateCertification(openOtpCertification, openRadiusCertification);
                importUserDTO.setOpenHardwareCertification(openHardwareCertification);
                importUserDTO.setMaxHardwareNum(maxHardwareNum);
                importUserDTO.setOpenRadiusCertification(openRadiusCertification);
                importUserDTO.setOpenSmsCertification(openSmsCertification);
                importUserDTO.setOpenAccountPasswordCertification(ExcelUtils.getCellValue(row, UserExcelField.ACCOUNTPASSWORDCERTIFICATION));
                importUserDTO.setOpenWorkWeixinCertification(ExcelUtils.getCellValue(row, UserExcelField.WORKWEIXINCERTIFICATION));
                importUserDTO.setOpenFeishuCertification(ExcelUtils.getCellValue(row, UserExcelField.FEISHUCERTIFICATION));
                importUserDTO.setOpenDingdingCertification(ExcelUtils.getCellValue(row, UserExcelField.DINGDINGCERTIFICATION));
                importUserDTO.setOpenOauth2Certification(ExcelUtils.getCellValue(row, UserExcelField.OAUTH2CERTIFICATION));
            }

            importUserDTO.setGroupNames(groupName);
            importUserDTO.setUserName(userName);
            importUserDTO.setRealName(realName);
            importUserDTO.setPhoneNum(phoneNum);
            importUserDTO.setState(userState);
            importUserDTO.setEmail(email);
            importUserDTO.setOpenOtpCertification(openOtpCertification);
            importUserDTO.setPassword(password);
            importUserDTO.setAccountExpireDate(expireDate);
            importUserDTO.setInvalidTime(invalidTime);
            importUserDTO.setDescription(description);
            userList.add(importUserDTO);
        }
        // 如果导入数据为空，返回提示信息
        if (CollectionUtils.isEmpty(userList)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IMPORT_NOT_USER_DATA);
        }
        return userList;
    }

    private void validateCertification(String openOtpCertification, String openRadiusCertification) throws BusinessException {
        if (openOtpCertification == null || openRadiusCertification == null) {
            return;
        }
        if (StringUtils.equals(openOtpCertification.trim(), LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE)) &&
                StringUtils.equals(openRadiusCertification.trim(), LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE)))  {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IMPORT_OPT_RADIUS_CANT_OPEN_MEANWHILE);
        }
    }

    private static void validateUserTemplate(Row row, int index, String columnName) throws BusinessException {
        if (row == null) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_TEMPLATE_IS_INVALIDATE);
        }

        Cell cell = CellUtil.getCell(row, index);
        cell.setCellType(CellType.STRING);
        if (!cell.getStringCellValue().startsWith(columnName)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_TEMPLATE_IS_INVALIDATE);
        }
    }

    /**
     * 校验数据行
     *
     * @param importUserList 导入的数据行
     * @throws BusinessException 业务异常
     */
    public void validate(List<ImportUserDTO> importUserList) throws BusinessException {
        Assert.notNull(importUserList, "importUserList不能为null");

        if (importUserList.size() > UserValidateRules.ALLOW_MAX_ROW) {
            LOGGER.error("导入的数据超出{}行", UserValidateRules.ALLOW_MAX_ROW);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IMPORT_ROW_OVER,
                    new String[] {String.valueOf(UserValidateRules.ALLOW_MAX_ROW)});
        }
        boolean isVdiModel = serverModelAPI.isVdiModel();
        // 记录校验不通过的提示信息,String[] 替换国际化文件中的占位符
        List<String> errorList = Lists.newArrayList();
        for (ImportUserDTO userDTO : importUserList) {
            UserRowValidator userRowValidator = new UserRowValidator(userDTO, errorList);
            userRowValidator.validateRowData(isVdiModel);
        }
        if (!errorList.isEmpty()) {
            String errorStr = String.join(EMAIL_SPLIT_CHAR, errorList);
            LOGGER.error("校验数据行，发现异常<{}>", errorStr);
            throw new BusinessException(UserBusinessKey.RCO_IMPORT_USER_PARSE_VALIDATE_FAIL, errorStr);
        }
    }

    /**
     * 校验过期时间是否大于当前时间
     *
     * @param importUserList 导入数据
     * @throws BusinessException 业务异常
     */
    public void importValidateExpire(List<ImportUserDTO> importUserList) throws BusinessException {
        Assert.notNull(importUserList, "importUserList不能为null");

        // 记录校验不通过的提示信息,String[] 替换国际化文件中的占位符
        List<String> errorList = Lists.newArrayList();
        for (ImportUserDTO userDTO : importUserList) {
            UserRowValidator userRowValidator = new UserRowValidator(userDTO, errorList);
            userRowValidator.validateUpdateRowData();
        }
        if (!errorList.isEmpty()) {
            String errorStr = String.join(EMAIL_SPLIT_CHAR, errorList);
            LOGGER.error("校验数据行，发现异常<{}>", errorStr);
            throw new BusinessException(UserBusinessKey.RCO_IMPORT_USER_PARSE_VALIDATE_FAIL, errorStr);
        }
    }
}
