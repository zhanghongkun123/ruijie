package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.api;

import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportAuditApplyExcelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ExportExcelCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.ExportExcelStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.response.GetExportExcelResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.cache.ExportAuditApplyExcelCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dto.ExportAuditApplyExcelDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ExportUtils;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Description: 导出文件流转审计申请单报表API实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/2
 *
 * @author WuShengQiang
 */
public class ExportAuditApplyExcelAPIImpl implements ExportAuditApplyExcelAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportAuditApplyExcelAPIImpl.class);

    private static final String FILE_PREFIX = "export_apply_";

    private static final String FILE_POSTFIX = ".xlsx";

    private static final String EXPORT_APPLY_THREAD_NAME = "exportApplyInfo";

    private static final int DEFAULT_EXPORT_PAGE_SIZE = 20000;

    private static final String DEFAULT_EXCEL_SHEET_NAME = "Sheet1";

    @Autowired
    private ExportAuditApplyExcelCacheManager cacheMgt;

    @Autowired
    private AuditApplyService auditApplyService;

    @Override
    public void exportDataAsync(PageQueryBuilderFactory.RequestBuilder builder, String userId) throws BusinessException {
        Assert.notNull(builder, "builder is not null");
        Assert.hasText(userId, "userId is not null");
        // 根据搜索条件和最大条数查询列表
        ExportExcelCacheDTO cache = cacheMgt.getCache(userId);

        if (Objects.nonNull(cache) && ExportExcelStateEnum.DOING.equals(cache.getState())) {
            throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_DOING);
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
                    String exportFilePath = queryAndExport(builder, tmpFileName);
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
            throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_FILE_NOT_EXIST);
        }
        String exportFilePath = cache.getExportFilePath();
        File file = new File(exportFilePath);
        if (!file.exists()) {
            throw new BusinessException(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_FILE_NOT_EXIST);
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

    private String queryAndExport(PageQueryBuilderFactory.RequestBuilder builder, String fileName) throws Exception {
        int page = 0;
        int rowIndex = 0;
        builder.setPageLimit(page, DEFAULT_EXPORT_PAGE_SIZE);
        PageQueryResponse<ExportAuditApplyExcelDTO> pageResult = getExportApplyList(builder);
        LOGGER.info("准备导出申请单报表，导出条目为[{}]", pageResult.getTotal());
        List<List> exList = new ArrayList<>();
        exList.add(ExportUtils.generateHeader(ExportAuditApplyExcelDTO.class));

        String exportFilePath = Constants.EXPORT_TMP_DIRECTORY + File.separator + fileName;
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_EXPORT_PAGE_SIZE)) {
            Sheet sheet = workbook.createSheet(DEFAULT_EXCEL_SHEET_NAME);
            checkDirectory();

            while (pageResult.getItemArr().length > 0) {
                Arrays.stream(pageResult.getItemArr()).forEach(item -> {
                    List dataList = new ArrayList();
                    Class clz = item.getClass();
                    Field[] fieldArr = clz.getDeclaredFields();
                    for (Field field : fieldArr) {
                        field.setAccessible(true);
                        ExcelHead annotation = field.getAnnotation(ExcelHead.class);
                        if (Objects.isNull(annotation)) {
                            continue;
                        }
                        String header = annotation.value();
                        if (StringUtils.isNotBlank(header)) {
                            try {
                                dataList.add(field.get(item));
                            } catch (IllegalAccessException e) {
                                LOGGER.info("反射excel字段失败，field为{}", field.getName());
                            }
                        }
                    }
                    exList.add(dataList);
                });
                writerData(exList, sheet, rowIndex);
                rowIndex += exList.size();
                builder.setPageLimit(++page, DEFAULT_EXPORT_PAGE_SIZE);
                pageResult = getExportApplyList(builder);
                exList.clear();
            }

            FileOutputStream os = new FileOutputStream(exportFilePath);
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            LOGGER.error("导出申请单异常", e);
            throw e;
        }
        return exportFilePath;
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

    private PageQueryResponse<ExportAuditApplyExcelDTO> getExportApplyList(PageQueryBuilderFactory.RequestBuilder request)
            throws BusinessException {

        PageQueryResponse<AuditApplyDTO> pageQueryResponse = auditApplyService.pageQueryForExport(request.build());
        AuditApplyDTO[] itemArr = pageQueryResponse.getItemArr();
        PageQueryResponse<ExportAuditApplyExcelDTO> result = new PageQueryResponse<>();
        result.setTotal(pageQueryResponse.getTotal());
        result.setItemArr(new ExportAuditApplyExcelDTO[0]);
        if (ArrayUtils.isNotEmpty(itemArr)) {
            result.setItemArr(Arrays.stream(itemArr).map(ExportAuditApplyExcelDTO::new).toArray(ExportAuditApplyExcelDTO[]::new));
        }
        return result;
    }

    private static void writerData(List<List> list, Sheet sheet, int rowIndex) {
        // 循环遍历数据，逐行写入Sheet中
        for (List childList : list) {
            // 创建行
            Row row = sheet.createRow(rowIndex++);
            // 创建单元格
            sheet.setColumnWidth((short) 0, (short) (35.7 * 100));// n为列高的像素数
            sheet.setColumnWidth((short) 1, (short) (35.7 * 100));
            sheet.setColumnWidth((short) 2, (short) (35.7 * 120));
            sheet.setColumnWidth((short) 3, (short) (35.7 * 120));
            sheet.setColumnWidth((short) 4, (short) (35.7 * 120));
            sheet.setColumnWidth((short) 5, (short) (35.7 * 120));
            sheet.setColumnWidth((short) 6, (short) (35.7 * 120));
            sheet.setColumnWidth((short) 7, (short) (35.7 * 120));
            Cell cell;
            for (int j = 0; j < childList.size(); j++) {
                String value = String.valueOf(childList.get(j));
                // 设置第i行第j列为Cells[j]单元格
                cell = row.createCell(j);
                // 设置单元格的值
                cell.setCellValue(new XSSFRichTextString(value));
            }
        }
    }
}
