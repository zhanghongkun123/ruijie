package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.ExportRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportFileResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 导出数据到Excel
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/04/12
 *
 * @author guoyongxin
 */
public interface ExportAPI {

    /**
     * 异步导出数据
     * @param request 导出数据请求体
     * @return DefaultResponse
     * @throws BusinessException 业务异常
     */
    DefaultResponse exportDataAsync(ExportRequest request) throws BusinessException;

    /**
     * 获得数据导出任务的情况
     *
     * @param userId 用户id
     * @return result
     */
    GetExportCacheResponse getExportDataCache(String userId);

    /**
     * 获得导出的excel文件
     *
     * @param userId 用户id
     * @return result
     * @throws BusinessException 业务异常
     */
    GetExportFileResponse getExportFile(String userId) throws BusinessException;

    /**
     * 删除旧文件
     */
    void clearOldFile();
}
