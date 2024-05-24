package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.special.dto.SpecialConfigDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

/**
 * Description: 打印机特殊配置API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/8
 *
 * @author zwf
 */
public interface PrinterSpecialConfigAPI {

    /**
     * 导入特殊配置文件
     *
     * @param file       文件
     * @throws BusinessException 异常处理
     */
    void importPrinterSpecialConfig(ChunkUploadFile file) throws BusinessException;

    /**
     * 获取特殊配置文件信息
     *
     * @return 特殊配置
     */
    SpecialConfigDTO getPrinterSpecialConfig();
}
