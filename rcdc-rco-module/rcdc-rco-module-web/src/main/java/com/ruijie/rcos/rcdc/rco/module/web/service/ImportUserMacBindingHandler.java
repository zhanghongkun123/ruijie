package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.HardwareCertificationBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.constants.HardwareConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.dto.ImportUserMacBindingDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.validator.UserMacBindingValidator;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.enums.UserMacBindingHeader;
import com.ruijie.rcos.rcdc.rco.module.web.service.userprofile.PathExcelField;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/23 14:48
 *
 * @author yxq
 */

@Service
public class ImportUserMacBindingHandler extends AbstractImportTemplateHandler<ImportUserMacBindingDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportUserMacBindingHandler.class);

    private static final String ERROR_SPLIT_CHAR = "；";

    @Override
    protected List<ImportUserMacBindingDTO> doReadData(Sheet sheet) throws BusinessException {
        try {
            return getImportUserMacBindingDTOList(sheet);
        } catch (Exception e) {
            LOGGER.error("解析绑定信息失败", e);
            if (!(e instanceof BusinessException)) {
                throw new BusinessException(HardwareCertificationBusinessKey.RCDC_IMPORT_USER_MAC_FAIL, e);
            }
            throw e;
        }
    }

    private List<ImportUserMacBindingDTO> getImportUserMacBindingDTOList(Sheet sheet) throws BusinessException {
        if (!ExcelUtils.isUserMacBindingTemplate(sheet)) {
            throw new BusinessException(HardwareCertificationBusinessKey.RCDC_IMPORT_USER_MAC_TEMPLATE_IS_INVALIDATE);
        }

        int rowNum = sheet.getPhysicalNumberOfRows();
        List<ImportUserMacBindingDTO> pathList = new ArrayList<>(rowNum);
        for (int i = 3; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            // 忽略掉空行
            if (ExcelUtils.isNullRow(row, PathExcelField.values())) {
                break;
            }
            ImportUserMacBindingDTO importDto = new ImportUserMacBindingDTO();
            importDto.setRowNum(i);
            importDto.setUserName(ExcelUtils.getCellValue(row, UserMacBindingHeader.USER_NAME));
            importDto.setTerminalMac(ExcelUtils.getCellValue(row, UserMacBindingHeader.TERMINAL_MAC));

            pathList.add(importDto);
        }
        // 如果导入数据为空，返回提示信息
        if (CollectionUtils.isEmpty(pathList)) {
            throw new BusinessException(HardwareCertificationBusinessKey.RCDC_IMPORT_USER_MAC_NOT_BINDING_DATA);
        }
        return pathList;
    }

    /**
     * 参数校验
     *
     * @param importDataList importDataList
     * @throws BusinessException 业务异常
     */
    public void validate(List<ImportUserMacBindingDTO> importDataList) throws BusinessException {
        Assert.notNull(importDataList, "importDataList must not be null");
        if (importDataList.size() > HardwareConstants.ALLOW_MAX_ROW) {
            LOGGER.error("导入的路径数据超出{}行", HardwareConstants.ALLOW_MAX_ROW);
            throw new BusinessException(HardwareCertificationBusinessKey.RCDC_IMPORT_USER_MAC_OVER_MAC_LIMIT,
                    String.valueOf(HardwareConstants.ALLOW_MAX_ROW));
        }
        // 记录校验不通过的提示信息
        List<String> errorList = Lists.newArrayList();
        for (ImportUserMacBindingDTO pathDTO : importDataList) {
            UserMacBindingValidator pathRowValidator = new UserMacBindingValidator(pathDTO, errorList);
            pathRowValidator.validate();
        }
        if (!errorList.isEmpty()) {
            String errorStr = String.join(ERROR_SPLIT_CHAR, errorList);
            LOGGER.error("校验数据行，发现异常<{}>", errorStr);
            throw new BusinessException(HardwareCertificationBusinessKey.RCDC_IMPORT_USER_MAC_VALIDATE_ERROR, errorStr);
        }
    }
}
