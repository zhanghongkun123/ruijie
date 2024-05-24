package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.Assert;

import java.io.*;
import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/23 18:24
 * @param <T> 泛型参数
 *
 * @author yxq
 */
public abstract class AbstractImportTemplateHandler<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImportTemplateHandler.class);

    /**
     * 获取要导入的数据
     *
     * @param file excel file
     * @return 返回数据列表
     * @throws BusinessException 业务异常
     * @throws IOException       IO异常
     */
    public List<T> getImportDataList(ChunkUploadFile file) throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        Sheet sheet = getSheet(file);
        return doReadData(sheet);
    }

    /**
     * 从sheet获取数据列表
     *
     * @param sheet sheet
     * @return 返回数据列表
     * @throws BusinessException 业务异常
     */
    protected abstract List<T> doReadData(Sheet sheet) throws BusinessException;

    private Sheet getSheet(ChunkUploadFile file) throws BusinessException, IOException {
        File uploadFile;
        try {
            uploadFile = new File(file.getFilePath());
        } catch (Exception e) {
            LOGGER.error("创建文件失败<{}>", file.getFilePath(), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IMPORT_FAIL, e);
        }
        if (!uploadFile.exists() || !uploadFile.isFile()) {
            LOGGER.error("文件不存在或格式有误<{}>", file.getFilePath());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IMPORT_FAIL);
        }
        Sheet sheet;
        try (InputStream inputStream = new FileInputStream(uploadFile)) {
            sheet = ExcelUtils.parseExcelFile(inputStream);
        } catch (FileNotFoundException e) {
            LOGGER.error("解析文件失败<{}>", file.getFilePath(), e);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_IMPORT_FAIL, e);
        } catch (IOException e) {
            LOGGER.error("解析文件失败<{}>", file.getFilePath(), e);
            throw e;
        }
        return sheet;
    }
}
