package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportUserGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.UserGroupBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

/**
 * Description: 导入的用户组数据处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/23
 *
 * @author zhangyichi
 */
@Service
public class ImportUserGroupHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportUserGroupHandler.class);

    @Autowired
    private ServerModelAPI serverModelAPI;

    /**
     * 数据起始行数
     */
    private static final int USER_DATA_START_ROW = 3;

    private static final String EMAIL_SPLIT_CHAR = "；";

    /**
     * 获取要导入的用户数据
     *
     * @param file excel file
     * @return 返回用户数据
     * @throws BusinessException 业务异常
     * @throws IOException IO异常
     */
    public List<ImportUserGroupDTO> parseTemplateUserGroupData(ChunkUploadFile file) throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        File uploadFile;
        try {
            uploadFile = new File(file.getFilePath());
        } catch (Exception e) {
            LOGGER.error("创建文件失败<{}>", file.getFilePath(), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_FAIL, e);
        }
        if (!uploadFile.exists() || !uploadFile.isFile()) {
            LOGGER.error("文件不存在或格式有误<{}>", file.getFilePath());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_FAIL);
        }
        Sheet sheet = null;
        try (InputStream inputStream = new FileInputStream(uploadFile)) {
            sheet = ExcelUtils.parseExcelFile(inputStream);
        } catch (FileNotFoundException e) {
            LOGGER.error("解析文件失败<{}>", file.getFilePath(), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_FAIL, e);
        } catch (IOException e) {
            LOGGER.error("解析文件失败<{}>", file.getFilePath(), e);
            throw e;
        }

        try {
            return getImportUserList(sheet);
        } catch (Exception e) {
            LOGGER.error("解析用户组信息失败", e);
            if (!(e instanceof BusinessException)) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_ANALYSIS_FAIL, e);
            }
            throw e;
        }
    }

    private List<ImportUserGroupDTO> getImportUserList(Sheet sheet) throws BusinessException {
        boolean isVDIModel = serverModelAPI.isVdiModel();
        boolean isUserGroupTemplate = isVDIModel ? ExcelUtils.isUserGroupTemplate(sheet) : ExcelUtils.isUserGroupTemplateOnlyForIDV(sheet);
        if (!isUserGroupTemplate) {
            LOGGER.error("导入的用户组模板不正确，请使用下载的模板重新导入");
            throw new BusinessException(UserBusinessKey.RCDC_RCO_IMPORT_USER_GROUP_TEMPLATE_IS_INVALIDATE);
        }

        int rowNum = sheet.getPhysicalNumberOfRows();
        List<ImportUserGroupDTO> userGroupList = new ArrayList<>(rowNum);
        for (int i = USER_DATA_START_ROW; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            // 忽略掉空行
            if (ExcelUtils.isNullRow(row)) {
                break;
            }
            ImportUserGroupDTO importUserGroupDTO = generateImportUserGroupDTO(i, row, isVDIModel);
            userGroupList.add(importUserGroupDTO);
        }
        // 如果导入数据为空，返回提示信息
        if (CollectionUtils.isEmpty(userGroupList)) {
            LOGGER.warn("无用户组数据导入");
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_NOT_GROUP_DATA);
        }
        return userGroupList;
    }

    private ImportUserGroupDTO generateImportUserGroupDTO(int line, Row row, boolean isVDIModel) {
        ImportUserGroupDTO importUserGroupDTO = new ImportUserGroupDTO();
        importUserGroupDTO.setRowNum(line);
        importUserGroupDTO.setGroupNames(ExcelUtils.getCellValue(row, UserExcelField.GROUP_NAME));


        if (isVDIModel) {
            importUserGroupDTO.setVdiImageTemplateName(ExcelUtils.getCellValue(row, UserExcelField.VDI_IMAGE_TEMPLATE_NAME));
            importUserGroupDTO.setVdiStrategyName(ExcelUtils.getCellValue(row, UserExcelField.VDI_STRATEGY_NAME));
            importUserGroupDTO.setVdiNetworkName(ExcelUtils.getCellValue(row, UserExcelField.VDI_NETWORK_NAME));
            importUserGroupDTO.setVdiCpu(ExcelUtils.getCellValue(row, UserExcelField.VDI_CPU));
            importUserGroupDTO.setVdiMemory(ExcelUtils.getCellValue(row, UserExcelField.VDI_MEMORY));
            importUserGroupDTO.setVdiSystemSize(ExcelUtils.getCellValue(row, UserExcelField.VDI_SYSTEM_SIZE));
            importUserGroupDTO.setVdiStoragePoolName(ExcelUtils.getCellValue(row, UserExcelField.VDI_STORAGE_POOL_NAME));
            importUserGroupDTO.setVdiPersonSize(ExcelUtils.getCellValue(row, UserExcelField.VDI_PERSON_SIZE));
            importUserGroupDTO.setVdiPersonDiskStoragePoolName(ExcelUtils.getCellValue(row, UserExcelField.VDI_PERSON_STORAGE_POOL_NAME));
            importUserGroupDTO.setVdiVgpuModel(ExcelUtils.getCellValue(row, UserExcelField.VDI_VGPU_MODEL));
            importUserGroupDTO.setIdvImageTemplateName(ExcelUtils.getCellValue(row, UserExcelField.IDV_IMAGE_TEMPLATE_NAME));
            importUserGroupDTO.setIdvStrategyName(ExcelUtils.getCellValue(row, UserExcelField.IDV_STRATEGY_NAME));
            //VOI 镜像以及策略的设置
            importUserGroupDTO.setVoiImageTemplateName(ExcelUtils.getCellValue(row, UserExcelField.VOI_IMAGE_TEMPLATE_NAME));
            importUserGroupDTO.setVoiStrategyName(ExcelUtils.getCellValue(row, UserExcelField.VOI_STRATEGY_NAME));
            importUserGroupDTO.setVdiClusterName(ExcelUtils.getCellValue(row, UserExcelField.VDI_CLUSTER_NAME));
            importUserGroupDTO.setAccountExpireDate(ExcelUtils.getCellValue(row, UserExcelField.ACCOUNT_EXPIRE_DATE));
            importUserGroupDTO.setInvalidTime(ExcelUtils.getCellValue(row, UserExcelField.INVALID_ETIME));
            importUserGroupDTO.setCloudPlatformName(ExcelUtils.getCellValue(row, UserExcelField.VDI_CLOUD_PLATFORM_NAME));
        } else {
            importUserGroupDTO.setIdvImageTemplateName(ExcelUtils.getCellValue(row, UserExcelField.IDV_IMAGE_TEMPLATE_NAME_ONLY_FOR_IDV));
            importUserGroupDTO.setIdvStrategyName(ExcelUtils.getCellValue(row, UserExcelField.IDV_STRATEGY_NAME_ONLY_FOR_IDV));
            //VOI 镜像以及策略的设置
            importUserGroupDTO.setVoiImageTemplateName(ExcelUtils.getCellValue(row, UserExcelField.VOI_IMAGE_TEMPLATE_NAME_ONLY_FOR_IDV));
            importUserGroupDTO.setVoiStrategyName(ExcelUtils.getCellValue(row, UserExcelField.VOI_STRATEGY_NAME_ONLY_FOR_IDV));
            importUserGroupDTO.setAccountExpireDate(ExcelUtils.getCellValue(row, UserExcelField.ACCOUNT_EXPIRE_DATE_ONLY_FOR_IDV));
            importUserGroupDTO.setInvalidTime(ExcelUtils.getCellValue(row, UserExcelField.INVALID_ETIME_ONLY_FOR_IDV));
        }

        return importUserGroupDTO;
    }

    /**
     * 校验数据行
     *
     * @param importUserGroupList 导入的数据行
     * @throws BusinessException 业务异常
     */
    public void validate(List<ImportUserGroupDTO> importUserGroupList) throws BusinessException {
        Assert.notNull(importUserGroupList, "importUserList不能为null");
        if (importUserGroupList.size() > UserGroupValidateRules.ALLOW_MAX_ROW) {
            LOGGER.error("导入的数据超出{}行", UserGroupValidateRules.ALLOW_MAX_ROW);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_IMPORT_ROW_OVER, String.valueOf(UserGroupValidateRules.ALLOW_MAX_ROW));
        }
        // 记录校验不通过的提示信息,String[] 替换国际化文件中的占位符
        List<String> errorList = Lists.newArrayList();
        for (ImportUserGroupDTO userGroupDTO : importUserGroupList) {
            UserGroupRowValidator userGroupRowValidator = new UserGroupRowValidator(userGroupDTO, errorList);
            userGroupRowValidator.validateRowData();
        }
        if (!errorList.isEmpty()) {
            String errorStr = String.join(EMAIL_SPLIT_CHAR, errorList);
            LOGGER.error("校验数据行，发现异常<{}>", errorStr);
            throw new BusinessException(UserGroupBusinessKey.RCO_IMPORT_USER_GROUP_PARSE_VALIDATE_FAIL, errorStr);
        }
    }
}
