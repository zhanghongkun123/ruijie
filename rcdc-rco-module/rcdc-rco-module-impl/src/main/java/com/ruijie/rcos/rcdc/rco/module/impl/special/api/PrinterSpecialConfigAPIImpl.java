package com.ruijie.rcos.rcdc.rco.module.impl.special.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterSpecialConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.special.dto.SpecialConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.PrinterConfigBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.special.dao.PrinterSpecialConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.special.entity.PrinterSpecialConfigEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * Description: 打印机特殊配置API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/8
 *
 * @author zwf
 */
public class PrinterSpecialConfigAPIImpl extends AbstractSpecialConfigAPIImpl implements PrinterSpecialConfigAPI {
    @Autowired
    private PrinterSpecialConfigDAO printerSpecialConfigDAO;

    @Override
    public void importPrinterSpecialConfig(ChunkUploadFile file) throws BusinessException {
        Assert.notNull(file, "file can not be null");

        SpecialConfigDTO printerSpecialConfig = parseSpecialConfigFile(file);
        List<PrinterSpecialConfigEntity> specialConfigEntityList = printerSpecialConfigDAO.findAll();

        PrinterSpecialConfigEntity specialConfigEntity = new PrinterSpecialConfigEntity();
        if (specialConfigEntityList.isEmpty()) {
            BeanUtils.copyProperties(printerSpecialConfig, specialConfigEntity);
        } else {
            PrinterSpecialConfigEntity oldPrinterSpecialConfig = specialConfigEntityList.get(0);
            Long configVersion = printerSpecialConfig.getConfigVersion();
            Long oldConfigVersion = oldPrinterSpecialConfig.getConfigVersion();
            if (configVersion <= oldConfigVersion) {
                throw new BusinessException(PrinterConfigBusinessKey.RCDC_RCO_PRINTER_SPECIAL_CONFIG_VERSION_OLDER,
                        String.valueOf(oldConfigVersion), String.valueOf(configVersion));
            }
            oldPrinterSpecialConfig.setConfigContent(printerSpecialConfig.getConfigContent());
            oldPrinterSpecialConfig.setConfigMd5(printerSpecialConfig.getConfigMd5());
            oldPrinterSpecialConfig.setConfigVersion(printerSpecialConfig.getConfigVersion());
            oldPrinterSpecialConfig.setFileName(printerSpecialConfig.getFileName());
            BeanUtils.copyProperties(oldPrinterSpecialConfig, specialConfigEntity);
        }
        specialConfigEntity.setCreateTime(new Date());
        printerSpecialConfigDAO.save(specialConfigEntity);
    }

    @Override
    public SpecialConfigDTO getPrinterSpecialConfig() {
        SpecialConfigDTO specialConfigDTO = new SpecialConfigDTO();
        List<PrinterSpecialConfigEntity> specialConfigEntityList = printerSpecialConfigDAO.findAll();
        if (!specialConfigEntityList.isEmpty()) {
            BeanUtils.copyProperties(specialConfigEntityList.get(0), specialConfigDTO);
        }
        return specialConfigDTO;
    }

}
