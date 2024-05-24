package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.entity.ViewAuditPrintLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrintLogService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrinterService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.AuditPrinterMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportPrintLogExcelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ExportExcelCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.ExportExcelStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.response.GetExportExcelResponse;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.ViewAuditPrintLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.cache.ExportPrintLogExcelCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.AuditPrinterBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.dto.ExportPrintLogExcelDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ExportUtils;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: 导出文件流转审计申请单报表API实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/2
 *
 * @author WuShengQiang
 */
public class ExportPrintLogExcelAPIImpl implements ExportPrintLogExcelAPI {


    private static final Logger LOGGER = LoggerFactory.getLogger(ExportPrintLogExcelAPIImpl.class);

    private static final String FILE_PREFIX = "export_print_log_";

    private static final String FILE_POSTFIX = ".xlsx";

    private static final String EXPORT_APPLY_THREAD_NAME = "exportPrintLog";

    @Autowired
    private ExportPrintLogExcelCacheManager cacheMgt;

    @Autowired
    private AuditPrintLogService auditPrintLogService;

    @Override
    public void exportDataAsync(PageQueryBuilderFactory.RequestBuilder builder, String userId) throws BusinessException {
        Assert.notNull(builder, "builder is not null");
        Assert.hasText(userId, "userId is not null");
        // 根据搜索条件和最大条数查询列表
        ExportExcelCacheDTO cache = cacheMgt.getCache(userId);

        if (Objects.nonNull(cache) && ExportExcelStateEnum.DOING.equals(cache.getState())) {
            throw new BusinessException(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_DOING);
        } else {
            String tmpFileName = getTmpFileName(userId);
            // 清空旧缓存
            cacheMgt.deleteCache(userId);
            // 在删除一次，此处是为了防止缓存不存在而目录文件中已有文件，确保只有一个文件
            deleteOldFile(tmpFileName);
            ExportExcelCacheDTO newCache = new ExportExcelCacheDTO();
            cacheMgt.save(userId, newCache);
            // 开启线程导出excel
            ThreadExecutors.submit(EXPORT_APPLY_THREAD_NAME, () -> {
                try {
                    List<ExportPrintLogExcelDTO> exportPrintLogList = getExportPrintLogList(builder);
                    LOGGER.info("准备导出申请单报表，导出条目为[{}]", exportPrintLogList.size());
                    String exportFilePath = exportAndGetResultPath(exportPrintLogList, tmpFileName);
                    newCache.setState(ExportExcelStateEnum.DONE);
                    newCache.setCreateTimestamp(System.currentTimeMillis());
                    newCache.setExportFilePath(exportFilePath);
                    newCache.setFileName(tmpFileName);
                    cacheMgt.save(userId, newCache);
                    LOGGER.info("导出申请单报表成功，导出路径是:{}", exportFilePath);
                } catch (Exception e) {
                    LOGGER.error("导出申请单报表出现异常:", e);
                    cacheMgt.updateState(userId, ExportExcelStateEnum.FAULT);
                }
            });
        }
    }

    @Override
    public ExportExcelCacheDTO getExportDataCache(String userId) {
        Assert.hasText(userId, "userId is not null");
        ExportExcelCacheDTO cache = cacheMgt.getCache(userId);
        // 缓存为空时,可能正在进行中
        if (Objects.isNull(cache)) {
            cache = new ExportExcelCacheDTO();
            cache.setState(ExportExcelStateEnum.DOING);
        }
        return cache;
    }

    @Override
    public GetExportExcelResponse getExportFile(String userId) throws BusinessException {
        Assert.hasText(userId, "userId is not null");
        ExportExcelCacheDTO cache = cacheMgt.getCache(userId);
        if (Objects.isNull(cache)) {
            throw new BusinessException(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_NOT_EXIST);
        }
        String exportFilePath = cache.getExportFilePath();
        File file = new File(exportFilePath);
        if (!file.exists()) {
            throw new BusinessException(AuditPrinterBusinessKey.RCDC_RCO_EXPORT_PRINT_LOG_EXCEL_NOT_EXIST);
        }
        return new GetExportExcelResponse(file);
    }

    /**
     * 删除旧的缓存
     *
     * @param fileName 文件名
     */
    private void deleteOldFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 生产文件名
     *
     * @param userId 用户ID
     * @return 文件名
     */
    private String getTmpFileName(String userId) {
        return StringUtils.join(FILE_PREFIX, userId, FILE_POSTFIX);
    }

    /**
     * 导出excel并且获取excel存放路径
     *
     * @param fileName 文件名
     * @return 路径
     */
    private String exportAndGetResultPath(List<ExportPrintLogExcelDTO> dataList, String fileName) throws Exception {
        Assert.notNull(fileName, "fileName is null");
        String tmpFilePath = Constants.EXPORT_TMP_DIRECTORY + File.separator + fileName;
        checkDirectory();
        ExportUtils.generateExcel(dataList, tmpFilePath, ExportPrintLogExcelDTO.class);
        return tmpFilePath;
    }

    /**
     * 检查目录是否存在
     */
    private void checkDirectory() {
        File file = new File(Constants.EXPORT_TMP_DIRECTORY);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
    }

    private List<ExportPrintLogExcelDTO> getExportPrintLogList(PageQueryBuilderFactory.RequestBuilder request) throws BusinessException {
        request.setPageLimit(NumberUtils.INTEGER_ZERO, Integer.MAX_VALUE);
        PageQueryResponse<ViewAuditPrintLogDTO> pageQueryResponse = auditPrintLogService.pageQuery(request.build());
        ViewAuditPrintLogDTO[] itemArr = pageQueryResponse.getItemArr();
        List<ExportPrintLogExcelDTO> resultList = new ArrayList<>();
        if (itemArr != null && itemArr.length > 0) {
            Arrays.stream(itemArr).forEach(viewAuditPrintLogDTO -> {
                ExportPrintLogExcelDTO exportPrintLogExcelDTO = new ExportPrintLogExcelDTO(viewAuditPrintLogDTO);
                resultList.add(exportPrintLogExcelDTO);
            });
        }
        return resultList;
    }

}
