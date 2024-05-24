package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.ExportUserProfilePathCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.ExportUserProfilePathFileResponse;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.request.ExportUserProfilePathRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 导出用户配置路径API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public interface ExportUserProfilePathAPI {

    /**
     * 异步导出路径数据到excel
     *
     * @param request 导出路径请求对象
     * @throws BusinessException 业务异常
     */
    void exportDataAsync(ExportUserProfilePathRequest request) throws BusinessException;

    /**
     * 获得软件信息数据导出任务的情况
     *
     * @param request 请求对象
     * @return 响应对象
     */
    ExportUserProfilePathCacheResponse getExportDataCache(ExportUserProfilePathRequest request);

    /**
     * 获得导出的路径信息excel文件
     *
     * @param request 请求对象
     * @return 响应对象
     * @throws BusinessException 业务异常
     */
    ExportUserProfilePathFileResponse getExportFile(ExportUserProfilePathRequest request) throws BusinessException;
}