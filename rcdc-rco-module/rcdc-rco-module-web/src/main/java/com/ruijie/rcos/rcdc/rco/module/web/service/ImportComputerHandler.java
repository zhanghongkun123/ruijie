package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.ComputerBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportComputerDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 导入的PC终端数据处理器
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/18
 *
 * @author zqj
 */
@Service
public class ImportComputerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportComputerHandler.class);

    /**
     * 数据起始行数
     */
    private static final int USER_DATA_START_ROW = 3;

    private static final String EMAIL_SPLIT_CHAR = "；";

    /**
     * 获取要导入的PC终端数据
     *
     * @param file excel file
     * @return 返回用户数据
     * @throws BusinessException 业务异常
     * @throws IOException IO异常
     */
    public List<ImportComputerDTO> parseTemplateUserData(ChunkUploadFile file) throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        File uploadFile;
        try {
            uploadFile = new File(file.getFilePath());
        } catch (Exception e) {
            LOGGER.error("创建文件失败<{}>", file.getFilePath(), e);
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_IMPORT_FAIL, e);
        }
        if (!uploadFile.exists() || !uploadFile.isFile()) {
            LOGGER.error("文件不存在或格式有误<{}>", file.getFilePath());
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_IMPORT_FAIL);
        }
        Sheet sheet = null;
        try (InputStream inputStream = new FileInputStream(uploadFile)) {
            sheet = ExcelUtils.parseExcelFile(inputStream);
        } catch (FileNotFoundException e) {
            LOGGER.error("解析文件失败<{}>", file.getFilePath(), e);
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_IMPORT_FAIL, e);
        } catch (IOException e) {
            LOGGER.error("解析文件失败<{}>", file.getFilePath(), e);
            throw e;
        }
        // 解析excel数据成List
        try {
            return getImportComputerList(sheet);
        } catch (Exception e) {
            LOGGER.error("解析PC终端信息失败", e);
            if (!(e instanceof BusinessException)) {
                throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_IMPORT_ANALYSIS_FAIL, e);
            }
            throw e;
        }
    }

    private List<ImportComputerDTO> getImportComputerList(Sheet sheet) throws BusinessException {
        boolean isUserTemplate = ExcelUtils.isComputerTemplate(sheet);
        if (!isUserTemplate) {
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_IMPORT_COMPUTER_TEMPLATE_IS_INVALIDATE);
        }

        Row tittleRow = sheet.getRow(2);
        validateUserTemplate(tittleRow, ComputerExcelField.GROUP_NAME.getIndex(), ComputerExcelField.GROUP_NAME.getHeader());
        validateUserTemplate(tittleRow, ComputerExcelField.IP.getIndex(), ComputerExcelField.IP.getHeader());
        validateUserTemplate(tittleRow, ComputerExcelField.USER_NAME.getIndex(), ComputerExcelField.USER_NAME.getHeader());
        validateUserTemplate(tittleRow, ComputerExcelField.DESK_STRATEGY.getIndex(), ComputerExcelField.DESK_STRATEGY.getHeader());
        validateUserTemplate(tittleRow, ComputerExcelField.REMARK.getIndex(), ComputerExcelField.REMARK.getHeader());


        int rowNum = sheet.getPhysicalNumberOfRows();
        List<ImportComputerDTO> computerList = new ArrayList<>(rowNum);
        for (int i = USER_DATA_START_ROW; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            // 忽略掉空行
            if (ExcelUtils.isNullRow(row)) {
                break;
            }
            ImportComputerDTO importComputerDTO = new ImportComputerDTO();
            importComputerDTO.setRowNum(i);
            String groupName = ExcelUtils.getCellValue(row, ComputerExcelField.GROUP_NAME);
            String userName = ExcelUtils.getCellValue(row, ComputerExcelField.USER_NAME);
            String ip = ExcelUtils.getCellValue(row, ComputerExcelField.IP);
            String remark = ExcelUtils.getCellValue(row, ComputerExcelField.REMARK);
            String deskStrategy = ExcelUtils.getCellValue(row, ComputerExcelField.DESK_STRATEGY);

            importComputerDTO.setGroupNames(groupName);
            importComputerDTO.setUserName(userName);
            importComputerDTO.setIp(ip);
            importComputerDTO.setRemark(remark);
            importComputerDTO.setDeskStrategy(deskStrategy);

            computerList.add(importComputerDTO);
        }
        // 如果导入数据为空，返回提示信息
        if (CollectionUtils.isEmpty(computerList)) {
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_USER_IMPORT_NOT_COMPUTER_DATA);
        }
        return computerList;
    }


    private static void validateUserTemplate(Row row, int index, String columnName) throws BusinessException {
        if (row == null) {
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_IMPORT_COMPUTER_TEMPLATE_IS_INVALIDATE);
        }

        Cell cell = CellUtil.getCell(row, index);
        cell.setCellType(CellType.STRING);
        if (!cell.getStringCellValue().startsWith(columnName)) {
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_IMPORT_COMPUTER_TEMPLATE_IS_INVALIDATE);
        }
    }

    /**
     * 校验数据行
     *
     * @param importUserList 导入的数据行
     * @throws BusinessException 业务异常
     */
    public void validate(List<ImportComputerDTO> importUserList) throws BusinessException {
        Assert.notNull(importUserList, "importUserList不能为null");

        // 记录校验不通过的提示信息,String[] 替换国际化文件中的占位符
        List<String> errorList = Lists.newArrayList();
        for (ImportComputerDTO importComputerDTO : importUserList) {
            ComputerRowValidator computerRowValidator = new ComputerRowValidator(importComputerDTO, errorList);
            computerRowValidator.validateRowData();
        }
        if (!errorList.isEmpty()) {
            String errorStr = String.join(EMAIL_SPLIT_CHAR, errorList);
            LOGGER.error("校验数据行，发现异常<{}>", errorStr);
            throw new BusinessException(ComputerBusinessKey.RCO_IMPORT_COMPUTER_PARSE_VALIDATE_FAIL, errorStr);
        }
    }
}
