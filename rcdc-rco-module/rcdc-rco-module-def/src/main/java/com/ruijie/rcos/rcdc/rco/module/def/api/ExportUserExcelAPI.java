package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportUserCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportUserFileResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 导出用户Excel
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/01/11
 *
 * @author guoyongxin
 */
public interface ExportUserExcelAPI {

    /**
     * 异步导出用户信息
     * @param userId 用户标识
     * @param userPageSearchRequest 导出用户列表的查询条件
     */
    void exportUserDataAsync(UserPageSearchRequest userPageSearchRequest, UUID userId);

    /**
     * 获取对应用户的导出用户任务状态
     * @param userId 用户ID
     * @return GetExportUserCacheResponse 响应对象
     */
    GetExportUserCacheResponse getExportDataCache(String userId);

    /**
     * 获取对应用户导出用户文件信息
     * @param userId 用户ID
     * @return GetExportUserFileResponse 响应对象
     * @throws BusinessException 业务异常
     */
    GetExportUserFileResponse getExportFile(String userId) throws BusinessException;

    /**
     * 清空导出目录与文件
     */
    void deleteOldFile();
}
