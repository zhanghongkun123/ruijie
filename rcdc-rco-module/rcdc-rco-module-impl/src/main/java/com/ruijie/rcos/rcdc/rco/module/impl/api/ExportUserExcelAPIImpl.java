package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.io.Files;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportUserExcelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportUserExcelDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportUserPageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportUserViewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportUserCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportUserFileResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.ExportUserDataStateEnums;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.ExportUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.ExportUserDataCacheMgt;
import com.ruijie.rcos.rcdc.rco.module.impl.util.FileUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.zip.ZipUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static com.ruijie.rcos.sk.base.util.StringUtils.join;
import static java.nio.file.Files.deleteIfExists;

/**
 * Description: 用户数据导出实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/01/11
 *
 * @author guoyongxin
 */
public class ExportUserExcelAPIImpl implements ExportUserExcelAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportUserExcelAPIImpl.class);

    private static final short DEFAULT_COLUMNS_WIDTH = 7000;

    private static final int DEFAULT_EXPORT_PAGE_SIZE = 20000;

    private static final String SPLIT_UNDERLINE = "_";

    private static final String EXCEL_NAME = "user_template";

    private static final String EXPORT_USER = "export_user";

    private static final String EXCEL_FILE_POSTFIX = ".xlsx";

    private static final String ZIP_FILE_POSTFIX = ".zip";

    private static final String EXPORT_USER_THREAD_NAME = "exportUserThread";

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private ExportUserDataCacheMgt cacheMgt;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    public void exportUserDataAsync(UserPageSearchRequest userPageSearchRequest, UUID userId) {
        Assert.notNull(userPageSearchRequest, "userPageSearchRequest not null");
        Assert.notNull(userId, "userId not null");

        String key = userId.toString();
        ExportUserInfoDTO cache = cacheMgt.getCache(key);
        if (Objects.nonNull(cache) && ExportUserDataStateEnums.DOING.equals(cache.getState())) {
            LOGGER.warn("导出任务{}正在运行，不要重复进行导出操作", key);
            cache.setReDoing(Boolean.TRUE);
            return;
        }

        // 清空旧缓存,由定时器来删除历史数据, 再删除一次，此处是为了防止缓存不存在而目录文件中已有文件，确保目录只有一个
        cacheMgt.deleteCache(key);
        String randomDir = UUID.randomUUID().toString();
        String zipFilePath = this.getZipFilePath(randomDir);
        String exportFileDir = this.getExcelFileDir(randomDir);
        //由定时器来删除
        ExportUserInfoDTO newCache = new ExportUserInfoDTO();
        newCache.setState(ExportUserDataStateEnums.DOING);
        cacheMgt.save(key, newCache);
        // 开启线程导出excel
        ThreadExecutors.submit(EXPORT_USER_THREAD_NAME, () -> {
            try {
                userPageSearchRequest.setPage(0);
                userPageSearchRequest.setLimit(DEFAULT_EXPORT_PAGE_SIZE);

                String excelName = this.getExcelFileName(key, String.valueOf(0));

                ExportUserPageDTO exportUserPageDTO = userDesktopConfigAPI.getExportUserList(userPageSearchRequest, key);
                int totalPages = exportUserPageDTO.getTotalPages();
                long totalElements = exportUserPageDTO.getTotalElements();
                LOGGER.info("准备导出用户信息数据，导出条目为{}", totalElements);

                String exportFilePath = this.exportAndGetResultPath(exportUserPageDTO, exportFileDir, excelName);
                LOGGER.info("导出用户信息成功，导出路径是{}", exportFilePath);

                if (totalPages > 1) {
                    for (int i = 1; i < totalPages; i++) {
                        excelName = this.getExcelFileName(key, String.valueOf(i));
                        userPageSearchRequest.setPage(i);
                        exportUserPageDTO = userDesktopConfigAPI.getExportUserList(userPageSearchRequest, key);
                        exportFilePath = this.exportAndGetResultPath(exportUserPageDTO, exportFileDir, excelName);
                        LOGGER.info("导出用户信息成功，导出路径是{}", exportFilePath);
                    }
                    //压缩多个excel到zip
                    LOGGER.info("准备压缩导出用户信息数据文件，文件数量为{}", totalPages);
                    File exportFilesDir = new File(exportFileDir);
                    File zipFile = new File(zipFilePath);
                    ZipUtil.zipFile(exportFilesDir, zipFile);
                    exportFilePath = zipFilePath;
                }

                newCache.setState(ExportUserDataStateEnums.DONE);
                newCache.setCreateTimestamp(System.currentTimeMillis());
                newCache.setExportFilePath(exportFilePath);
                cacheMgt.save(key, newCache);
                //删除用户组缓存
                cacheMgt.deleteUserGroupCache(key);

                LOGGER.info("导出用户信息数据成功，数量为{}", String.valueOf(totalElements));
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_EXPORT_USER_SUCCESS_LOG, String.valueOf(totalElements));
            } catch (Exception e) {
                LOGGER.error("导出文件任务{}出错，错误原因是{}", key, e.getMessage(), e);
                cacheMgt.updateState(key, ExportUserDataStateEnums.FAULT);
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_EXPORT_USER_FAIL_LOG, e, String.valueOf(e));
            }
        });
    }

    @Override
    public GetExportUserCacheResponse getExportDataCache(String userId) {
        Assert.notNull(userId, "userId is null");
        ExportUserInfoDTO cache = cacheMgt.getCache(userId);
        if (cache == null) {
            cache = new ExportUserInfoDTO();
            cache.setState(ExportUserDataStateEnums.DOING);
        }

        return new GetExportUserCacheResponse(cache);
    }

    @Override
    public GetExportUserFileResponse getExportFile(String userId) throws BusinessException {
        Assert.notNull(userId, "userId is null");
        GetExportUserCacheResponse cache = this.getExportDataCache(userId);
        if (Objects.isNull(cache) || Objects.isNull(cache.getExportUserInfoDTO())) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_EXPORT_USER_DATA_NOT_EXIST);
        }
        String exportFilePath = cache.getExportUserInfoDTO().getExportFilePath();
        File file = new File(exportFilePath);
        if (!file.exists()) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_EXPORT_USER_DATA_NOT_EXIST);
        }
        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_EXPORT_USER_DOWNLOAD_LOG);
        return new GetExportUserFileResponse(file);
    }

    /**
     * 导出excel文件名
     *
     * @param userId
     * @param index
     * @return 文件名
     */
    private String getExcelFileName(String userId, String index) {
        return join(EXPORT_USER, SPLIT_UNDERLINE, userId, SPLIT_UNDERLINE, index, EXCEL_FILE_POSTFIX);
    }

    /**
     * 生成zip文件名
     *
     * @param randomDir
     * @return zip文件名
     */
    private String getZipFilePath(String randomDir) {
        return join(Constants.EXPORT_TMP_DIRECTORY, File.separator, EXPORT_USER, File.separator, randomDir, ZIP_FILE_POSTFIX);
    }

    /**
     * 生成excel文件目录名
     *
     * @param randomDir
     * @return 导出文件目录名
     */
    private String getExcelFileDir(String randomDir) {
        return join(Constants.EXPORT_TMP_DIRECTORY, File.separator, EXPORT_USER, File.separator, randomDir, File.separator);
    }

    /**
     * 删除目录及目录下文件及压缩文件
     * 删除前一天文件与目录，保留当天
     */
    @Override
    public void deleteOldFile() {
        String fileDir = join(Constants.EXPORT_TMP_DIRECTORY, File.separator, EXPORT_USER, File.separator);
        File file = new File(fileDir);
        try {
            if (!file.exists()) {
                return;
            }
            this.deleteSubFile(file, false);
        } catch (IOException e) {
            LOGGER.error(String.format("删除目录或文件失败, 文件[%s]", fileDir), e);
        }
    }

    private void deleteSubFile(File subFile, boolean ifDelCurDir) throws IOException {
        if (!subFile.exists()) {
            return;
        }
        if (subFile.isDirectory()) {
            Date fileDate = FileUtil.getFileDateTime(subFile);
            if (fileDate != null) {
                //只删除一天前数据
                Date now = new Date();
                Date delDate = DateUtils.addDays(now, -1);
                if (fileDate.after(delDate)) {
                    return;
                }
            }

            if (!ArrayUtils.isEmpty(subFile.listFiles())) {
                for (File f : subFile.listFiles()) {
                    if (f.isDirectory()) {
                        deleteSubFile(f, true);
                    } else {
                        deleteIfExists(f.toPath());
                    }
                }
            }
        }
        if (ifDelCurDir) {
            deleteIfExists(subFile.toPath());
        }
    }

    /**
     * 导出excel并且获取excel存放路径
     *
     * @param exportUserPageDTO 导出对象
     * @param fileName  文件名
     * @param exportDir 文件导出目录
     * @return 路径
     */
    private String exportAndGetResultPath(ExportUserPageDTO exportUserPageDTO, String exportDir, String fileName) throws Exception {
        Assert.notNull(exportDir, "exportDir is null");
        Assert.notNull(fileName, "fileName is null");
        this.checkDirectory(exportDir);
        String fullPath = exportDir + fileName;
        File file = new File(fullPath);
        boolean isSuccess = file.createNewFile();
        if (isSuccess) {
            Files.write(this.exportExcel(exportUserPageDTO), file);
        } else {
            LOGGER.error(String.format("创建文件失败, 文件名[%s]", fullPath));
        }

        return fullPath;
    }

    /**
     * 检查目录是否存在
     */
    private void checkDirectory(String dir) {
        File fileDir = new File(dir);
        if (!fileDir.exists() || !fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
    }

    private byte[] exportExcel(ExportUserPageDTO exportUserPageDTO) throws IOException {
        Assert.notNull(exportUserPageDTO, "exportUserPageDTO 不能为null");

        Collections.sort(exportUserPageDTO.getExportUserViewDTOList());

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // 构建用户Sheet
            Sheet userSheet = workbook.createSheet(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_EXPORT_USER_DATA_SHEET_NAME));

            int userSheetCurRow = 0;
            userSheetCurRow = generateExcelTitle(userSheet, userSheetCurRow);
            generateExcelHeaderAndData(exportUserPageDTO.getExportUserViewDTOList(), userSheet, userSheetCurRow);

            // 构建统计Sheet
            Sheet staticsSheet = workbook.createSheet(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_EXPORT_USER_STATIC_SHEET_NAME));
            int staticSheetCurRow = 0;
            Row staticsRow = staticsSheet.createRow(staticSheetCurRow++);
            staticsRow.createCell(0).setCellValue(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_EXPORT_USER_STATIC_TOTAL_NUM));
            staticsRow.createCell(1).setCellValue(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_EXPORT_USER_STATIC_DATA_NUM));

            staticsRow = staticsSheet.createRow(staticSheetCurRow);
            staticsRow.createCell(0).setCellValue(exportUserPageDTO.getTotalElements());
            staticsRow.createCell(1).setCellValue(exportUserPageDTO.getExportUserViewDTOList().size());

            workbook.write(out);

            return out.toByteArray();
        } catch (IOException e) {
            throw new IOException(BusinessKey.RCDC_RCO_EXPORT_USER_STATIC_DATA_FAIL, e);
        }
    }

    private int generateExcelTitle(Sheet sheet, int rowNum) {
        Row rowOne = sheet.createRow(rowNum++);
        rowOne.createCell(0).setCellValue(EXCEL_NAME);
        Row rowTwo = sheet.createRow(rowNum++);
        rowTwo.createCell(0).setCellValue(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_EXPORT_USER_DATA_SHEET_TITLE_MESSAGE));
        return rowNum;
    }

    private List getHeader(Class clazz) {
        List resultList = new ArrayList();
        if (clazz == null) {
            return resultList;
        }

        Field[] fieldArr = clazz.getDeclaredFields();
        for (Field field : fieldArr) {
            field.setAccessible(true);
            ExcelHead annotation = field.getAnnotation(ExcelHead.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            String header = annotation.value();
            header = LocaleI18nResolver.resolve(header);
            resultList.add(header);
        }
        return resultList;
    }

    private void generateExcelHeaderAndData(List<ExportUserViewDTO> exportUserExcelList, Sheet sheet, int rowNum) {
        List<List> exList = new ArrayList<>();
        // 准备字段头
        List headerList = getHeader(ExportUserExcelDTO.class);

        exList.add(headerList);

        // 准备数据
        exportUserExcelList.stream().forEach(item -> {
            List dataList = new ArrayList();
            ExportUserExcelDTO exportUserExcelDTO = new ExportUserExcelDTO(item);
            Class clz = exportUserExcelDTO.getClass();
            Field[] fieldArr = clz.getDeclaredFields();
            for (Field field : fieldArr) {
                field.setAccessible(true);
                ExcelHead annotation = field.getAnnotation(ExcelHead.class);
                if (Objects.isNull(annotation)) {
                    continue;
                }
                String header = annotation.value();
                if (Boolean.FALSE.equals(StringUtils.isNotBlank(header))) {
                    continue;
                }
                try {
                    Object fieldValue = field.get(exportUserExcelDTO);
                    if (fieldValue == null) {
                        dataList.add(StringUtils.EMPTY);
                    } else {
                        dataList.add(fieldValue);
                    }
                } catch (IllegalAccessException e) {
                    LOGGER.info("反射excel字段失败，field为{}", field.getName());
                }
            }
            exList.add(dataList);
        });

        // 设置列宽
        for (int i = 0; i < headerList.size(); i++) {
            sheet.setColumnWidth((short) i, DEFAULT_COLUMNS_WIDTH);
        }

        for (int i = 0; i < exList.size(); i++) {
            List childList = exList.get(i);
            // 创建行
            Row row = sheet.createRow(i + rowNum);
            for (int j = 0; j < childList.size(); j++) {
                String value = String.valueOf(childList.get(j));
                // 设置第i行第j列为Cells[j]单元格
                row.createCell(j).setCellValue(value);
            }
        }
    }
}
