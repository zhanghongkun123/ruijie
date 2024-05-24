package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.printer.dto.PrinterConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.printer.dto.PrinterConfigMigrateItemDTO;
import com.ruijie.rcos.rcdc.rco.module.def.special.dto.SpecialConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.printer.request.EditPrinterConfigRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/14
 *
 * @author chenjiehui
 */
public interface PrinterManageServiceAPI {

    /**
     * 开启打印机配置
     */
    void enablePrinterConfig();

    /**
     * 关闭打印机配置
     */
    void disablePrinterConfig();

    /**
     * 获取打印机配置开启状态
     * @return configStatus "true" "false"
     */
    String getPrinterConfigStatus();

    /**
     * @param id id
     * 获取打印机配置详情
     * @return PrinterConfigDTO
     * @throws BusinessException /
     */
    PrinterConfigDTO getPrinterConfigById(UUID id) throws BusinessException;

    /**
     * @param id 打印机id
     * 删除指定的打印机配置
     */
    void deletePrinterConfigById(UUID id);

    /**
     * 编辑指定的打印机配置
     * @param request 请求
     * @throws BusinessException 业务异常
     */
    void editPrinterConfigById(EditPrinterConfigRequest request) throws BusinessException;

    /**
     * 分页获取打印机配置列表
     * @param request 请求
     * @return PrinterConfigDTO
     */
    DefaultPageResponse<PrinterConfigDTO> list(PageSearchRequest request);

    /**
     * 导入特殊配置
     * @param file 文件
     * @throws BusinessException /
     */
    void importSpecialConfig(ChunkUploadFile file) throws BusinessException;


    /**
     * 获取打印机特殊配置信息
     * @return PrinterSpecailConfigDTO
     */
    SpecialConfigDTO getPrinterSpecialConfig();

    /**
     * 校验配置名是否重复
     * @param editPrinterConfigRequest /
     * @return boolean
     */
    boolean checkConfigName(EditPrinterConfigRequest editPrinterConfigRequest);


    /**
     * 用于迁移打印机配置
     * @param printerConfigMigrateItemDTO 迁移的printer配置
     * @throws Exception 异常
     */
    void migratePrinterConfig(PrinterConfigMigrateItemDTO printerConfigMigrateItemDTO) throws Exception;

}
