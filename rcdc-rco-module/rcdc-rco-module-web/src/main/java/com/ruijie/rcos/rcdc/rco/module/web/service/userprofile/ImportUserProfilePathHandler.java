package com.ruijie.rcos.rcdc.rco.module.web.service.userprofile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.dto.ImportUserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.dto.UserProfilePathToolDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.AbstractImportTemplateHandler;
import com.ruijie.rcos.rcdc.rco.module.web.service.ExcelUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 导入用户配置路径表格数据处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
@Service
public class ImportUserProfilePathHandler extends AbstractImportTemplateHandler<ImportUserProfilePathDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportUserProfilePathHandler.class);

    private static final String EMAIL_SPLIT_CHAR = "；";


    @Override
    protected List<ImportUserProfilePathDTO> doReadData(Sheet sheet) throws BusinessException {
        try {
            return getImportUserProfilePathDTOList(sheet);
        } catch (Exception e) {
            LOGGER.error("解析导入个性化配置信息失败", e);
            if (!(e instanceof BusinessException)) {
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_FAIL, e);
            }
            throw e;
        }
    }

    private List<ImportUserProfilePathDTO> getImportUserProfilePathDTOList(Sheet sheet) throws BusinessException {
        int rowNum = sheet.getPhysicalNumberOfRows();
        List<ImportUserProfilePathDTO> pathList = new ArrayList<>(rowNum);
        for (int i = 1; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            // 忽略掉空行
            if (ExcelUtils.isNullRow(row, PathExcelField.values())) {
                break;
            }
            ImportUserProfilePathDTO importPathDTO = new ImportUserProfilePathDTO();
            importPathDTO.setRowNum(i);
            importPathDTO.setName(ExcelUtils.getCellValue(row, PathExcelField.PATH_NAME));
            importPathDTO.setDescription(ExcelUtils.getCellValue(row, PathExcelField.PATH_DESC));
            importPathDTO.setGroupName(ExcelUtils.getCellValue(row, PathExcelField.GROUP_NAME));
            importPathDTO.setGroupDescription(ExcelUtils.getCellValue(row, PathExcelField.GROUP_DESC));
            importPathDTO.setMode(ExcelUtils.getCellValue(row, PathExcelField.MODE));
            importPathDTO.setType(ExcelUtils.getCellValue(row, PathExcelField.TYPE));
            importPathDTO.setPath(ExcelUtils.getCellValue(row, PathExcelField.PATH));

            pathList.add(importPathDTO);
        }
        // 如果导入数据为空，返回提示信息
        if (CollectionUtils.isEmpty(pathList)) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_NOT_DATA);
        }
        return pathList;
    }

    /**
     * 校验数据行
     *
     * @param importPathList 导入的数据行
     * @throws BusinessException 业务异常
     */
    public void validate(List<ImportUserProfilePathDTO> importPathList) throws BusinessException {
        Assert.notNull(importPathList, "importPathList不能为null");
        if (importPathList.size() > PathValidateRules.ALLOW_MAX_ROW) {
            LOGGER.error("导入的路径数据超出{}行", PathValidateRules.ALLOW_MAX_ROW);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_PATH_IMPORT_ROW_OVER, String.valueOf(PathValidateRules.ALLOW_MAX_ROW));
        }
        // 记录校验不通过的提示信息,String[] 替换国际化文件中的占位符
        List<String> errorList = Lists.newArrayList();
        for (ImportUserProfilePathDTO pathDTO : importPathList) {
            PathRowValidator pathRowValidator = new PathRowValidator(pathDTO, errorList);
            pathRowValidator.validateRowData();
        }
        if (!errorList.isEmpty()) {
            String errorStr = String.join(EMAIL_SPLIT_CHAR, errorList);
            LOGGER.error("校验数据行，发现异常<{}>", errorStr);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_PARSE_VALIDATE_FAIL, errorStr);
        }
    }

    /**
     * 将导入数据转化为路径对象
     *
     * @param importPathList 导入数据
     * @return 路径对象
     */
    public List<UserProfilePathDTO> changeImportUserProfileToRealUserProfile(List<ImportUserProfilePathDTO> importPathList) {
        Assert.notEmpty(importPathList, "importPathList不能为null");
        LOGGER.debug("待转化的导入用户配置数据：{}", JSON.toJSONString(importPathList));

        Map<String, UserProfilePathToolDTO> userProfilePathDTOMap = new HashMap<>();
        for (ImportUserProfilePathDTO importPath : importPathList) {
            UserProfilePathToolDTO toolDTO = userProfilePathDTOMap.get(importPath.getName());
            UserProfilePathModeEnum mode = UserProfilePathModeEnum.valueOfText(importPath.getMode());
            UserProfilePathTypeEnum type = UserProfilePathTypeEnum.valueOfText(importPath.getType());

            if (toolDTO == null) {
                UserProfilePathToolDTO newToolDTO = new UserProfilePathToolDTO();
                newToolDTO.setName(importPath.getName());
                newToolDTO.setDescription(importPath.getDescription());
                newToolDTO.setGroupName(importPath.getGroupName());
                newToolDTO.setGroupDescribe(importPath.getGroupDescription());
                newToolDTO.addUserProfileChildPath(mode, type, importPath.getPath());

                userProfilePathDTOMap.put(importPath.getName(), newToolDTO);
                continue;
            }

            toolDTO.addUserProfileChildPath(mode, type, importPath.getPath());
        }

        List<UserProfilePathDTO> returnUserProfilePathList = new ArrayList<>();
        for (Map.Entry<String, UserProfilePathToolDTO> map : userProfilePathDTOMap.entrySet()) {
            UserProfilePathToolDTO toolDTO = map.getValue();
            LOGGER.debug("等待转换的对象：{}", JSON.toJSONString(toolDTO));
            returnUserProfilePathList.add(toolDTO.changeToUserProfilePathDTO());
        }

        LOGGER.debug("导入数据经过处理之后形成的对象：{}", JSON.toJSONString(returnUserProfilePathList));

        return returnUserProfilePathList;
    }
}