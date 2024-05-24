package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.SoftwareControlBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.dto.ImportSoftwareDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;


/**
 * Description: 导入的软件数据处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/14
 *
 * @author lihengjing
 */
@Service
public class ImportSoftwareHandler extends AbstractImportTemplateHandler<ImportSoftwareDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportSoftwareHandler.class);

    /**
     * 数据起始行数
     */
    private static final int SOFTWARE_DATA_START_ROW = 1;

    private static final String EMAIL_SPLIT_CHAR = "；";

    @Override
    protected List<ImportSoftwareDTO> doReadData(Sheet sheet) throws BusinessException {
        try {
            return getImportSoftwareDTOList(sheet);
        } catch (Exception e) {
            LOGGER.error("解析软件信息模板失败", e);
            if (!(e instanceof BusinessException)) {
                throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_IMPORT_SOFTWARE_PARSE_FAIL, e);
            }
            throw e;
        }
    }

    private List<ImportSoftwareDTO> getImportSoftwareDTOList(Sheet sheet) throws BusinessException {
        int rowNum = sheet.getPhysicalNumberOfRows();
        List<ImportSoftwareDTO> softwareList = new ArrayList<>(rowNum);
        for (int i = SOFTWARE_DATA_START_ROW; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            // 忽略掉空行
            if (ExcelUtils.isNullRow(row, SoftwareExcelField.values())) {
                break;
            }

            ImportSoftwareDTO importSoftwareDTO = new ImportSoftwareDTO();
            importSoftwareDTO.setRowNum(i);
            String softwareGroupName = ExcelUtils.getCellValue(row, SoftwareExcelField.SOFTWARE_GROUP_NAME);
            String softwareGroupType = ExcelUtils.getCellValue(row, SoftwareExcelField.SOFTWARE_GROUP_TYPE);
            String softwareGroupDesc = ExcelUtils.getCellValue(row, SoftwareExcelField.SOFTWARE_GROUP_DESC);
            String softwareName = ExcelUtils.getCellValue(row, SoftwareExcelField.SOFTWARE_NAME);
            String softwareDesc = ExcelUtils.getCellValue(row, SoftwareExcelField.SOFTWARE_DESC);
            String digitalSign = ExcelUtils.getCellValue(row, SoftwareExcelField.DIGITAL_SIGN);
            String digitalSignFlag = ExcelUtils.getCellValue(row, SoftwareExcelField.DIGITAL_SIGN_FLAG);
            String productName = ExcelUtils.getCellValue(row, SoftwareExcelField.PRODUCT_NAME);
            String productNameFlag = ExcelUtils.getCellValue(row, SoftwareExcelField.PRODUCT_NAME_FLAG);
            String processName = ExcelUtils.getCellValue(row, SoftwareExcelField.PROCESS_NAME);
            String processNameFlag = ExcelUtils.getCellValue(row, SoftwareExcelField.PROCESS_NAME_FLAG);
            String originalFileName = ExcelUtils.getCellValue(row, SoftwareExcelField.ORIGINAL_FILE_NAME);
            String originalFileNameFlag = ExcelUtils.getCellValue(row, SoftwareExcelField.ORIGINAL_FILE_NAME_FLAG);
            String fileCustomMd5 = ExcelUtils.getCellValue(row, SoftwareExcelField.FILE_CUSTOM_MD5);
            String fileCustomMd5Flag = ExcelUtils.getCellValue(row, SoftwareExcelField.FILE_CUSTOM_MD5_FLAG);

            String directory = ExcelUtils.getCellValue(row, SoftwareExcelField.FILE_IS_DIRECTORY);
            String parentId = ExcelUtils.getCellValue(row, SoftwareExcelField.FILE_PARENT_ID);
            String id = ExcelUtils.getCellValue(row, SoftwareExcelField.FILE_ID);

            String digitalSignBlackFlag = ExcelUtils.getCellValue(row, SoftwareExcelField.DIGITAL_SIGN_BLACK_FLAG);
            String productNameBlackFlag = ExcelUtils.getCellValue(row, SoftwareExcelField.PRODUCT_NAME_BLACK_FLAG);
            String processNameBlackFlag = ExcelUtils.getCellValue(row, SoftwareExcelField.PROCESS_NAME_BLACK_FLAG);
            String originalFileNameBlackFlag = ExcelUtils.getCellValue(row, SoftwareExcelField.ORIGINAL_FILE_NAME_BLACK_FLAG);
            String fileCustomMd5BlackFlag = ExcelUtils.getCellValue(row, SoftwareExcelField.FILE_CUSTOM_MD5_BLACK_FLAG);

            importSoftwareDTO.setSoftwareGroupName(softwareGroupName);
            importSoftwareDTO.setSoftwareGroupType(softwareGroupType);
            importSoftwareDTO.setSoftwareGroupDesc(softwareGroupDesc);
            importSoftwareDTO.setSoftwareName(softwareName);
            importSoftwareDTO.setSoftwareDesc(softwareDesc);
            importSoftwareDTO.setDigitalSign(digitalSign);
            importSoftwareDTO.setDigitalSignFlag(digitalSignFlag);
            importSoftwareDTO.setProductName(productName);
            importSoftwareDTO.setProductNameFlag(productNameFlag);
            importSoftwareDTO.setProcessName(processName);
            importSoftwareDTO.setProcessNameFlag(processNameFlag);
            importSoftwareDTO.setOriginalFileName(originalFileName);
            importSoftwareDTO.setOriginalFileNameFlag(originalFileNameFlag);
            importSoftwareDTO.setFileCustomMd5(fileCustomMd5);
            importSoftwareDTO.setFileCustomMd5Flag(fileCustomMd5Flag);

            importSoftwareDTO.setDirectoryFlag(directory);
            importSoftwareDTO.setParentId(StringUtils.isBlank(parentId) ? null : parentId);
            importSoftwareDTO.setId(StringUtils.isBlank(id) ? null : id);

            importSoftwareDTO.setDigitalSignBlackFlag(digitalSignBlackFlag);
            importSoftwareDTO.setProductNameBlackFlag(productNameBlackFlag);
            importSoftwareDTO.setProcessNameBlackFlag(processNameBlackFlag);
            importSoftwareDTO.setOriginalFileNameBlackFlag(originalFileNameBlackFlag);
            importSoftwareDTO.setFileCustomMd5BlackFlag(fileCustomMd5BlackFlag);

            softwareList.add(importSoftwareDTO);
        }
        // 如果导入数据为空，返回提示信息
        if (CollectionUtils.isEmpty(softwareList)) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_NOT_SOFTWARE_DATA);
        }
        LOGGER.info("导入的软件信息为：{}", JSON.toJSONString(softwareList));
        return softwareList;
    }

    /**
     * 校验数据行
     *
     * @param importSoftwareList 导入的数据行
     * @throws BusinessException 业务异常
     */
    public void validate(List<ImportSoftwareDTO> importSoftwareList) throws BusinessException {
        Assert.notNull(importSoftwareList, "importSoftwareList不能为null");

        if (importSoftwareList.size() > SoftwareValidateRules.ALLOW_MAX_ROW) {
            LOGGER.error("导入的数据超出{}行", SoftwareValidateRules.ALLOW_MAX_ROW);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_ROW_OVER,
                    new String[] {String.valueOf(SoftwareValidateRules.ALLOW_MAX_ROW)});
        }
        // 记录校验不通过的提示信息,String[] 替换国际化文件中的占位符
        List<String> errorList = Lists.newArrayList();
        for (ImportSoftwareDTO softwareDTO : importSoftwareList) {
            SoftwareRowValidator softwareRowValidator = new SoftwareRowValidator(softwareDTO, errorList);
            softwareRowValidator.validateRowData();
        }
        if (!errorList.isEmpty()) {
            String errorStr = String.join(EMAIL_SPLIT_CHAR, errorList);
            LOGGER.error("校验数据行，发现异常<{}>", errorStr);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_IMPORT_SOFTWARE_PARSE_VALIDATE_FAIL, errorStr);
        }
    }
}
