package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportVDIDeskDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 导入的VDI桌面处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/23
 *
 * @author linrenjian
 */
@Service
public class ImportVDIDeskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportVDIDeskHandler.class);

    @Autowired
    private ServerModelAPI serverModelAPI;

    /**
     * 数据起始行数
     */
    private static final int USER_DATA_START_ROW = 3;

    private static final String EMAIL_SPLIT_CHAR = "；";

    /**
     * 获取要导入的VDI桌面数据
     *
     * @param file excel file
     * @return 返回用户数据
     * @throws BusinessException 业务异常
     * @throws IOException IO异常
     */
    public List<ImportVDIDeskDTO> parseTemplateUserGroupData(ChunkUploadFile file) throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        File uploadFile;
        try {
            uploadFile = new File(file.getFilePath());
        } catch (Exception e) {
            LOGGER.error("创建文件失败<{}>", file.getFilePath(), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_VDI_DESK_IMPORT_FAIL, e);
        }
        if (!uploadFile.exists() || !uploadFile.isFile()) {
            LOGGER.error("文件不存在或格式有误<{}>", file.getFilePath());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_VDI_DESK_IMPORT_FAIL);
        }
        Sheet sheet = null;
        try (InputStream inputStream = new FileInputStream(uploadFile)) {
            sheet = ExcelUtils.parseExcelFile(inputStream);
        } catch (FileNotFoundException e) {
            LOGGER.error("解析文件失败<{}>", file.getFilePath(), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_VDI_DESK_IMPORT_FAIL, e);
        } catch (IOException e) {
            LOGGER.error("解析文件失败<{}>", file.getFilePath(), e);
            throw e;
        }
        
        try {
            return getImporVDIDeskList(sheet);
        } catch (Exception e) {
            LOGGER.error("解析导入个性化配置信息失败", e);
            if (!(e instanceof BusinessException)) {
                throw new BusinessException(UserBusinessKey.RCO_IMPORT_VDI_DESK_FAIL, e);
            }
            throw e;
        }

    }

    /**
     * 从sheet获取要导入的VDI桌面数据
     * @param sheet
     * @return
     * @throws BusinessException
     */
    private List<ImportVDIDeskDTO> getImporVDIDeskList(Sheet sheet) throws BusinessException {
        boolean isVDIModel = serverModelAPI.isVdiModel();
        if (!isVDIModel) {
            LOGGER.error("当前服务器工作模式不支持VDI云桌面导入");
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_TEMPLATE_NOT_ALLOW);
        }
        boolean isVDIDeskTemplate = ExcelUtils.isVDIDeskTemplate(sheet);
        if (!isVDIDeskTemplate) {
            LOGGER.error("导入的VDI云桌面模板不正确，请使用下载的模板重新导入");
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_TEMPLATE_IS_INVALIDATE);
        }

        int rowNum = sheet.getPhysicalNumberOfRows();
        List<ImportVDIDeskDTO> importVDIDeskDTOList = new ArrayList<>(rowNum);
        for (int i = USER_DATA_START_ROW; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            // 忽略掉空行
            if (ExcelUtils.isNullRow(row)) {
                break;
            }
            ImportVDIDeskDTO importVDIDeskDTO = generateImporVDIDeskDTO(i, row);
            importVDIDeskDTOList.add(importVDIDeskDTO);
        }
        // 如果导入数据为空，返回提示信息
        if (CollectionUtils.isEmpty(importVDIDeskDTOList)) {
            LOGGER.warn("无VDI云桌面数据导入");
            throw new BusinessException(UserBusinessKey.RCDC_RCO_VDI_DESK_IMPORT_NOT_DESK_DATA);
        }
        return importVDIDeskDTOList;
    }

    private ImportVDIDeskDTO generateImporVDIDeskDTO(int line, Row row) {
        ImportVDIDeskDTO importVDIDeskDTO = new ImportVDIDeskDTO();
        importVDIDeskDTO.setRowNum(line);
        //用户名
        importVDIDeskDTO.setUserName(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.USERNAME));
        //云平台
        importVDIDeskDTO.setCloudPlatformName(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_CLOUD_PLATFORM_NAME));
        //VDI镜像模板
        importVDIDeskDTO.setVdiImageTemplateName(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_IMAGE_TEMPLATE_NAME));
        //VDI策略
        importVDIDeskDTO.setVdiStrategyName(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_STRATEGY_NAME));
        //VDI网络策略
        importVDIDeskDTO.setVdiNetworkName(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_NETWORK_NAME));
        //vdi运行时位置
        importVDIDeskDTO.setVdiClusterName(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_CLUSTER_NAME));
        //vdi系统盘存储位置
        importVDIDeskDTO.setVdiStoragePoolName(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_STORAGE_POOL_NAME));
        // vdi cpu
        importVDIDeskDTO.setVdiCpu(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_CPU));
        // vdi memory
        importVDIDeskDTO.setVdiMemory(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_MEMORY));
        // vdi系统盘大小
        importVDIDeskDTO.setVdiSystemSize(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_SYSTEM_SIZE));
        // vdi本地盘大小
        importVDIDeskDTO.setVdiPersonSize(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_PERSON_SIZE));
        // vdi本地盘存储位置
        importVDIDeskDTO.setVdiPersonDiskStoragePoolName(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_PERSON_STORAGE_POOL_NAME));
        // vdi显卡配置
        importVDIDeskDTO.setVdiVgpuModel(ExcelUtils.getCellValue(row, ImportVDIDeskExcelField.VDI_VGPU_MODEL));
        return importVDIDeskDTO;
    }

    /**
     * 校验数据行
     *
     * @param importVDIDeskDTOList 导入的数据行
     * @throws BusinessException 业务异常
     */
    public void validate(List<ImportVDIDeskDTO> importVDIDeskDTOList) throws BusinessException {
        Assert.notNull(importVDIDeskDTOList, "importVDIDeskDTOList 不能为null");
        if (importVDIDeskDTOList.size() > ImportVDIDeskValidateRules.ALLOW_MAX_ROW) {
            LOGGER.error("导入的数据超出{}行", ImportVDIDeskValidateRules.ALLOW_MAX_ROW);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_ROW_OVER,
                    String.valueOf(ImportVDIDeskValidateRules.ALLOW_MAX_ROW));
        }
        // 记录校验不通过的提示信息,String[] 替换国际化文件中的占位符
        List<String> errorList = Lists.newArrayList();
        for (ImportVDIDeskDTO importVDIDeskDTO : importVDIDeskDTOList) {
            ImportVDIDeskRowValidator importVDIDeskRowValidator = new ImportVDIDeskRowValidator(importVDIDeskDTO, errorList);
            importVDIDeskRowValidator.validateRowData();
        }
        if (!errorList.isEmpty()) {
            String errorStr = String.join(EMAIL_SPLIT_CHAR, errorList);
            LOGGER.error("校验数据行，发现异常<{}>", errorStr);
            throw new BusinessException(UserBusinessKey.RCO_IMPORT_VDI_DESK_PARSE_VALIDATE_FAIL, errorStr);
        }
    }
}
