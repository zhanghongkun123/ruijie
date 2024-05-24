package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.ExportRcaHostRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportRcaHostCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportRcaHostFileResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: 导出第三方应用主机API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/16
 *
 * @author lihengjing
 */

public interface ExportRcaHostAPI {
    /**
     * 异步导出软件信息数据
     *
     * @param request ExportCloudDesktopRequest
     * @return DefaultResponse
     * @throws BusinessException 业务异常
     */
    DefaultResponse exportDataAsync(ExportRcaHostRequest request) throws BusinessException;

    /**
     * 获得软件信息数据导出任务的情况
     *
     * @param userId 用户id
     * @return result
     */
    GetExportRcaHostCacheResponse getExportDataCache(String userId);

    /**
     * 获得导出的软件信息excel文件
     *
     * @param userId 用户id
     * @return result
     * @throws BusinessException 业务异常
     */
    GetExportRcaHostFileResponse getExportFile(String userId) throws BusinessException;

}
