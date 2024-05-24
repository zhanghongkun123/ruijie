package com.ruijie.rcos.rcdc.rco.module.impl.printer.api;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterSpecialConfigAPI;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterManageServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.printer.dto.PrinterConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.printer.dto.PrinterConfigMigrateItemDTO;
import com.ruijie.rcos.rcdc.rco.module.def.special.dto.SpecialConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.printer.request.EditPrinterConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.PrinterConfigBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.dao.PrinterConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.entity.PrinterConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.service.QueryPrinterConfigServiceImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/14
 *
 * @author chenjiehui
 */
public class PrinterManageServiceAPIImpl implements PrinterManageServiceAPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(PrinterManageServiceAPIImpl.class);

    public static final String SPECIAL_CONFIG_HASK_KEY = "ruijie.com";

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private PrinterConfigDAO printerConfigDAO;

    @Autowired
    private QueryPrinterConfigServiceImpl queryPrinterConfigService;

    @Autowired
    private PrinterSpecialConfigAPI specialConfigAPI;

    @Override
    public void enablePrinterConfig() {
        globalParameterService.updateParameter(PrinterConfigBusinessKey.RCO_PRINTER_MANAGE_ENABLE, Boolean.TRUE.toString());
    }

    @Override
    public void disablePrinterConfig() {
        globalParameterService.updateParameter(PrinterConfigBusinessKey.RCO_PRINTER_MANAGE_ENABLE, Boolean.FALSE.toString());
    }

    @Override
    public String getPrinterConfigStatus() {
        return globalParameterService.findParameter(PrinterConfigBusinessKey.RCO_PRINTER_MANAGE_ENABLE);
    }

    @Override
    public PrinterConfigDTO getPrinterConfigById(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        Optional<PrinterConfigEntity> entityOptional = printerConfigDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error("printer config info is not exist, id[{}]", id);
            throw new BusinessException(PrinterConfigBusinessKey.RCDC_RCO_PRINTER_CONFIG_NOT_EXIST);
        }
        PrinterConfigDTO printerConfigDTO = new PrinterConfigDTO();
        BeanUtils.copyProperties(entityOptional.get(), printerConfigDTO);

        return printerConfigDTO;
    }

    @Override
    public void deletePrinterConfigById(UUID id) {
        Assert.notNull(id, "id can not be null");
        printerConfigDAO.deleteById(id);
    }

    @Override
    public void editPrinterConfigById(EditPrinterConfigRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        PrinterConfigEntity printerConfigEntity = printerConfigDAO.findByConfigName(request.getConfigName());
        if (printerConfigEntity != null && !printerConfigEntity.getId().equals(request.getId())) {
            throw new BusinessException(PrinterConfigBusinessKey.RCDC_RCO_PRINTER_CONFIG_ALREADY_EXIST);
        }
        Optional<PrinterConfigEntity> entityOptional = printerConfigDAO.findById(request.getId());
        if (!entityOptional.isPresent()) {
            throw new BusinessException(PrinterConfigBusinessKey.RCDC_RCO_PRINTER_CONFIG_NOT_EXIST);
        }

        printerConfigDAO.updatePrinterConfigById(request.getConfigName(), request.getConfigDescription(), request.getId());
    }

    @Override
    public DefaultPageResponse<PrinterConfigDTO> list(PageSearchRequest request) {
        Assert.notNull(request, "request can not be null");
        Page<PrinterConfigEntity> printerConfigEntitys = queryPrinterConfigService.pageQuery(request, PrinterConfigEntity.class);
        List<PrinterConfigEntity> printerConfigEntityList = printerConfigEntitys.getContent();

        PrinterConfigDTO[] printerConfigDTOArr = printerConfigEntityList.stream().map(o -> {
            PrinterConfigDTO dto = new PrinterConfigDTO();
            BeanUtils.copyProperties(o, dto);
            return dto;
        }).collect(Collectors.toList()).toArray(new PrinterConfigDTO[] {});

        DefaultPageResponse<PrinterConfigDTO> response = new DefaultPageResponse<>();
        response.setTotal(printerConfigEntitys.getTotalElements());
        response.setItemArr(printerConfigDTOArr);
        return response;

    }

    @Override
    public void importSpecialConfig(ChunkUploadFile file) throws BusinessException {
        Assert.notNull(file, "file can not be null");

        specialConfigAPI.importPrinterSpecialConfig(file);
    }

    @Override
    public SpecialConfigDTO getPrinterSpecialConfig() {
        return specialConfigAPI.getPrinterSpecialConfig();
    }

    @Override
    public boolean checkConfigName(EditPrinterConfigRequest editPrinterConfigRequest) {
        Assert.notNull(editPrinterConfigRequest, "request can not be null");
        PrinterConfigEntity printerConfig = printerConfigDAO.findByConfigName(editPrinterConfigRequest.getConfigName());
        return (printerConfig != null && !printerConfig.getId().equals(editPrinterConfigRequest.getId()));
    }

    @Override
    public void migratePrinterConfig(PrinterConfigMigrateItemDTO printerConfigMigrateItemDTO) throws Exception {
        Assert.notNull(printerConfigMigrateItemDTO, "printerConfigMigrateDTO can not be null");
        PrinterConfigEntity printerConfig = printerConfigDAO.findByConfigName(printerConfigMigrateItemDTO.getConfigName());
        if (printerConfig == null) {
            PrinterConfigEntity printerConfigEntity = new PrinterConfigEntity();
            BeanUtils.copyProperties(printerConfigMigrateItemDTO, printerConfigEntity);
            printerConfigEntity.setCreateTime(new Date());
            printerConfigEntity.setUpdateTime(new Date());
            printerConfigDAO.save(printerConfigEntity);
        } else {
            BeanUtils.copyProperties(printerConfigMigrateItemDTO, printerConfig, "id", "configName");
            printerConfig.setUpdateTime(new Date());
            printerConfigDAO.save(printerConfig);
        }
    }
}
