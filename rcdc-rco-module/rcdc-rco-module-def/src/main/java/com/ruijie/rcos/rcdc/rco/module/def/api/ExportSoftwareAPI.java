package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.ExportSoftwareRequest;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.GetExportSoftwareCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.GetExportSoftwareFileResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: 导出软件信息API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/16
 *
 * @author lihengjing
 */

public interface ExportSoftwareAPI {
    /**
     * 异步导出软件信息数据
     *
     * @param request ExportCloudDesktopRequest
     * @return DefaultResponse
     */
    DefaultResponse exportDataAsync(ExportSoftwareRequest request);

    /**
     * 获得软件信息数据导出任务的情况
     *
     * @param request ExportSoftwareRequest
     * @return result
     */
    GetExportSoftwareCacheResponse getExportDataCache(ExportSoftwareRequest request);

    /**
     * 获得导出的软件信息excel文件
     *
     * @param request ExportSoftwareRequest
     * @return result
     * @throws BusinessException 业务异常
     */
    GetExportSoftwareFileResponse getExportFile(ExportSoftwareRequest request) throws BusinessException;

}
