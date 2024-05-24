package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ExportExcelCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.response.GetExportExcelResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;

/**
 * Description: 导出文件导出审批申请单报表API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/2
 *
 * @author WuShengQiang
 */
public interface ExportPrintLogExcelAPI {

    /**
     * 异步导出文件导出审批申请单数据到excel
     *
     * @param builder 分页查询
     * @param userId  用户ID
     * @throws BusinessException 业务异常
     */
    void exportDataAsync(PageQueryBuilderFactory.RequestBuilder builder, String userId) throws BusinessException;

    /**
     * 获取导出报表结果
     *
     * @param userId 用户ID
     * @return 结果DTO
     */
    ExportExcelCacheDTO getExportDataCache(String userId);

    /**
     * 下载报表excel
     *
     * @param userId 用户ID
     * @return 下载信息
     * @throws BusinessException 业务异常
     */
    GetExportExcelResponse getExportFile(String userId) throws BusinessException;

}
