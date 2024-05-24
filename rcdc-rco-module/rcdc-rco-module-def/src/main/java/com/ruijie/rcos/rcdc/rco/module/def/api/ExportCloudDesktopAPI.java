package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.ExportCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCloudDesktopCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCloudDesktopFileResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 
 * Description: 导出云桌面信息API
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/16
 *
 * @author zhiweiHong
 */

public interface ExportCloudDesktopAPI {
    /**
     * 异步导出云桌面数据
     *
     * @param request ExportCloudDesktopRequest
     * @return DefaultResponse
     * @throws BusinessException 删除文件异常
     */
    DefaultResponse exportDataAsync(ExportCloudDesktopRequest request) throws BusinessException;

    /**
     * 异步导出应用主机的云桌面数据
     *
     * @param request ExportCloudDesktopRequest
     * @return DefaultResponse
     * @throws BusinessException 删除文件异常
     */
    DefaultResponse exportRcaHostDataAsync(ExportCloudDesktopRequest request) throws BusinessException;

    /**
     * 获得云桌面数据导出任务的情况
     *
     * @param userId 用户id
     * @return result
     */
    GetExportCloudDesktopCacheResponse getExportDataCache(String userId);

    /**
     * 获得导出的云桌面excel文件
     *
     * @param userId 用户id
     * @return result
     * @throws BusinessException 业务异常
     */
    GetExportCloudDesktopFileResponse getExportFile(String userId) throws BusinessException;
}
