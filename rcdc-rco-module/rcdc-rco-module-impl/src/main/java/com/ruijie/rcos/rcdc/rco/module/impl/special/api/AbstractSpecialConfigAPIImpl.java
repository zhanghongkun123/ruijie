package com.ruijie.rcos.rcdc.rco.module.impl.special.api;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.special.dto.SpecialConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.PrinterConfigBusinessKey;
import com.ruijie.rcos.sk.base.crypto.Md5Builder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

import java.io.*;

/**
 * Description: 特殊配置实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/28
 *
 * @author zwf
 */
public abstract class AbstractSpecialConfigAPIImpl {
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpecialConfigAPIImpl.class);

    public static final String SPECIAL_CONFIG_HASK_KEY = "ruijie.com";

    /**
     * 解析 conf 文件
     *
     * @param file
     * @return
     */
    protected SpecialConfigDTO parseSpecialConfigFile(ChunkUploadFile file) throws BusinessException {
        File uploadFile;
        uploadFile = new File(file.getFilePath());
        StringBuilder stringBuilder = new StringBuilder();
        SpecialConfigDTO specailConfigDTO = new SpecialConfigDTO();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(uploadFile));
             BufferedReader buffReader = new BufferedReader(reader)) {
            String line = "";
            while ((line = buffReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("导入配置文件[{}]失败，失败原因", file.getFileName(), e);
            throw new BusinessException(PrinterConfigBusinessKey.RCDC_RCO_PRINTER_CONFIG_IMPORT_FAILED, e);
        }
        JSONObject json;
        try {
            json = JSONObject.parseObject(stringBuilder.toString());
        } catch (RuntimeException e) {
            LOGGER.error("转化为JSON失败，待转化对象：{}，失败原因", stringBuilder.toString(), e);
            throw new BusinessException(PrinterConfigBusinessKey.RCDC_RCO_PRINTER_SPECIAL_CONFIG_CONTENT_FORMAT_INCORRECT, e);
        }

        if (!json.containsKey("hash") || !json.containsKey("ver") || !json.containsKey("content")) {
            throw new BusinessException(PrinterConfigBusinessKey.RCDC_RCO_PRINTER_SPECIAL_CONFIG_FORMAT_INCORRECT);
        }

        String hash = json.getString("hash");
        Long version = Long.valueOf(json.getString("ver"));
        String content = json.getString("content");
        if (!hash.equals(Md5Builder.computeTextMd5(content + SPECIAL_CONFIG_HASK_KEY))) {
            throw new BusinessException(PrinterConfigBusinessKey.RCDC_RCO_PRINTER_SPECIAL_CONFIG_HASH_INCORRECT);
        }

        specailConfigDTO.setFileName(file.getFileName());
        specailConfigDTO.setConfigMd5(hash);
        specailConfigDTO.setConfigVersion(version);
        specailConfigDTO.setConfigContent(content);

        return specailConfigDTO;
    }

}
