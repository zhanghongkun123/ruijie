package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserLoginRecordExportDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserLoginRecordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportDataSessionStateEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportDataStateEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ExportRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserLoginRecordPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportFileResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DesktopType;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.ExportDataCacheMgt;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.ExportFileInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ExportExcelUtils;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_USER_RECORD_EXPORT_TASK_FAIL;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * Description: 导出数据
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/04/12
 *
 * @author guoyongxin
 */
public class ExportAPIImpl implements ExportAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportAPIImpl.class);

    @Autowired
    private ExportDataCacheMgt cacheMgt;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    private static final String EXCEL_SUFFIX = ".xlsx";

    private static final String ZIP_SUFFIX = ".zip";

    private static final String EXPORT_USER_RECORD_THREAD_NAME = "export_user_record_thread_name";

    private static final String EXPORT_USER_RECORD = "export_user_record";

    private static final String SPLIT_UNDERLINE = "_";

    private static final String TOTAL_EXPORT = "导出/总数: ";

    private static final String EXPORT_USER_USE_INFO_FIRST_INFO = "user_login_record";

    private static final String EXPORT_USER_USE_INFO_SHEET_NAME = "用户报表";

    private static final String EXPORT_USER_RECORD_TMP_DIRECTORY = File.separator + "opt";

    private static final Long CLEAR_OLD_FILE_MILLS = 1 * 24 * 60 * 60 * 1000L;

    /**
     * 分配50个线程数处理数据导出
     */
    private static final ExecutorService THREAD_POOL = ThreadExecutors.newBuilder("export")
            .maxThreadNum(3).queueSize(50000).addStartupLineNumberToThreadName().build();

    @Override
    public DefaultResponse exportDataAsync(ExportRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        String userId = request.getUserId();
        Assert.notNull(userId, "userId must not be null");
        Integer totalCount = request.getTotalCount();
        Assert.notNull(totalCount, "totalCount must not be null");
        Sort[] sortArr = request.getSortArr();
        Assert.notNull(sortArr, "sortArr must not be null");
        LOGGER.info("用户使用信息导出请求:{}", JSON.toJSONString(request));
        Integer exportCount = Optional.ofNullable(request.getExportTotalCount()).orElse(totalCount);
        request.setExportTotalCount(exportCount);

        if (totalCount == 0) {
            LOGGER.error("导出数据不能为空");
            throw new BusinessException(BusinessKey.RCDC_RCO_EXPORT_TASK_NOT_EMPTY);
        }

        ExportFileInfoDTO cache = cacheMgt.getCache(userId);
        if (Objects.nonNull(cache) && ExportDataStateEnums.DOING.equals(cache.getState())) {
            LOGGER.info("用户[{}]正在导出数据，请勿重复操作", userId);
        } else {
            // 清空旧缓存
            cacheMgt.deleteCache(userId);
            String excelFileDir = getExcelFileDir(userId);
            String excelFileZip = getZipFilePath(userId);

            deleteOldFile(excelFileDir);
            deleteOldFile(excelFileZip);

            checkDirectory(excelFileDir);

            ExportFileInfoDTO newCache = new ExportFileInfoDTO();
            newCache.setTaskStartTime(System.currentTimeMillis());
            cacheMgt.save(userId, newCache);

            ThreadExecutors.submit(EXPORT_USER_RECORD_THREAD_NAME, () -> {
                try {
                    batchExportExcel(request, excelFileDir, excelFileZip);
                } catch (Exception e) {
                    LOGGER.error("用户[{}]创建导出用户使用信息失败", userId, e);
                }
            });
        }
        return DefaultResponse.Builder.success();
    }

    private void batchExportExcel(ExportRequest request, String excelFileDir, String excelFileZip) {
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        int runCount = (int) Math.ceil(request.getExportTotalCount() * 1.0 / request.getSheetSize());
        String userId = request.getUserId();
        LOGGER.info("用户[{}]开始导出数据，数据总数量为[{}], 导出数据数量为[{}]", userId, request.getTotalCount(), request.getExportTotalCount());
        for (int i = 0; i < runCount; i++) {
            final int page = i;
            try {
                updateTaskState(failCount, userId);
                StringBuilder sb = new StringBuilder();
                String tmpFilePath = sb.append(excelFileDir).append(File.separator).append(getExcelFileName(String.valueOf(page))).toString();
                Date curStartTime = new Date();
                List<UserLoginRecordExportDTO> exportList = getExportList(request, page);
                LOGGER.info("用户[{}]当前查询数据库完毕，序号为: [{}]，文件总数为[{}], 耗时[{} ms]，开始写入数据至Excel",
                        userId, page, runCount, new Date().getTime() - curStartTime.getTime());
                writeDataToExcelFile(exportList, tmpFilePath, request.getTotalCount());
                if (runCount == 1) {
                    updateTaskFinish(userId, tmpFilePath);
                    return;
                }
                LOGGER.info("用户[{}]导出用户报表数据子任务成功，耗时{}ms", userId,  new Date().getTime() - curStartTime.getTime());
                if (successCount.incrementAndGet() == runCount) {
                    zipExcelFile(excelFileDir, excelFileZip);
                    updateTaskFinish(userId, excelFileZip);
                }
            } catch (Exception e) {
                failCount.incrementAndGet();
                cacheMgt.updateState(userId, ExportDataStateEnums.FAULT);
                LOGGER.error("用户[{}]导出用户使用信息第[{}]个任务失败", userId, page, e);
                return;
            }
        }
    }

    private void updateTaskState(AtomicInteger failCount, String userId) throws BusinessException {
        if (failCount.intValue() > 0) {
            cacheMgt.updateState(userId, ExportDataStateEnums.FAULT);
            throw new BusinessException(RCDC_RCO_USER_RECORD_EXPORT_TASK_FAIL, userId);
        }
        cacheMgt.updateState(userId, ExportDataStateEnums.DOING);
    }

    private void updateTaskFinish(String userId, String exportFilePath) {
        ExportFileInfoDTO cache = cacheMgt.getCache(userId);
        cache.setExportFilePath(exportFilePath);
        cache.setState(ExportDataStateEnums.DONE);
        cache.setCreateTimestamp(System.currentTimeMillis());
        cacheMgt.save(userId, cache);
        LOGGER.info("用户[{}]导出用户使用信息报表数据导出成功，耗时{}ms", userId, new Date().getTime() - cacheMgt.getCache(userId).getTaskStartTime());
    }

    private List<UserLoginRecordExportDTO> getExportList(ExportRequest request, Integer page) {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(page, "page must not be null");

        UserLoginRecordPageRequest pageReq = generatePageReq(request, page);
        List<UserLoginRecordDTO> dataList = getOriginalList(pageReq);
        List<UserLoginRecordExportDTO> resultList = transferOriginalToExport(dataList);
        return resultList;
    }

    private List<UserLoginRecordDTO> getOriginalList(UserLoginRecordPageRequest request) {
        Assert.notNull(request, "request is not null");

        DefaultPageResponse<UserLoginRecordDTO> pageResponse = userLoginRecordService.pageQuery(request);
        List<UserLoginRecordDTO> dataList = Arrays.asList(pageResponse.getItemArr());
        return dataList;
    }

    private List<UserLoginRecordExportDTO> transferOriginalToExport(List<UserLoginRecordDTO> originalList) {
        Assert.notNull(originalList, "originalList is not null");

        List<UserLoginRecordExportDTO> dataList = new ArrayList<>();
        for (UserLoginRecordDTO entity : originalList) {
            UserLoginRecordExportDTO userLoginRecordExportDTO = new UserLoginRecordExportDTO();
            BeanUtils.copyProperties(entity, userLoginRecordExportDTO);
            userLoginRecordExportDTO.setAuthDuration(DateUtil.millisecondsConvertToHMS(entity.getAuthDuration(), true));
            userLoginRecordExportDTO.setConnectDuration(DateUtil.millisecondsConvertToHMS(entity.getConnectDuration(), true));
            userLoginRecordExportDTO.setUseDuration(DateUtil.millisecondsConvertToHMS(entity.getUseDuration(), false));
            userLoginRecordExportDTO.setLoginTime(entity.getLoginTime() == null ? "" :
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getLoginTime()));
            userLoginRecordExportDTO.setConnectTime(entity.getConnectTime() == null ? "" :
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getConnectTime()));
            userLoginRecordExportDTO.setLogoutTime(entity.getLogoutTime() == null ? "" :
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getLogoutTime()));
            userLoginRecordExportDTO.setSessionState(parseSessionState(entity.getSessionState()));
            if (StringUtils.isEmpty(entity.getDeskType()) && StringUtils.isEmpty(entity.getDeskStrategyPattern())) {
                userLoginRecordExportDTO.setDeskType("");
            } else if (Objects.equals(CbbCloudDeskType.THIRD.name(), entity.getDeskType())) {
                userLoginRecordExportDTO.setDeskType(Constants.THIRD);
            } else {
                userLoginRecordExportDTO.setDeskType(entity.getDeskType() + '-' + parseStrategyPattern(entity.getDeskStrategyPattern()));
            }
            dataList.add(userLoginRecordExportDTO);
        }
        return dataList;
    }

    private UserLoginRecordPageRequest generatePageReq(ExportRequest request, Integer page) {
        Assert.notNull(request, "request is null.");

        UserLoginRecordPageRequest pageReq = new UserLoginRecordPageRequest(request);
        pageReq.setStartTime(request.getStartTime());
        pageReq.setEndTime(request.getEndTime());
        pageReq.setSearchKeyword(request.getSearchKeyword());
        pageReq.setPage(page);
        pageReq.setLimit(request.getSheetSize());
        pageReq.setSortArr(request.getSortArr());
        return pageReq;
    }

    private String parseSessionState(String sessionState) {
        if (StringUtils.isEmpty(sessionState)) {
            return "";
        }
        ExportDataSessionStateEnums cbbSessionState = ExportDataSessionStateEnums.valueOf(sessionState);
        String businessKey = null;
        switch (cbbSessionState) {
            case CONNECTED:
                businessKey = com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_EXPORT_SESSION_STATE_CONNECTED;
                break;
            case CONNECTING:
                businessKey = com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_EXPORT_SESSION_STATE_CONNECTING;
                break;
            case DISCONNECT:
                businessKey = com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_EXPORT_SESSION_STATE_DISCONNECT;
                break;
            default:
                LOGGER.error("can not find state in cbbSessionState, now is {}", cbbSessionState);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String parseStrategyPattern(String strategyPattern) {
        if (StringUtils.isEmpty(strategyPattern)) {
            return "";
        }
        DesktopType cbbStrategyPattern = DesktopType.valueOf(strategyPattern);
        String businessKey = null;
        switch (cbbStrategyPattern) {
            case PERSONAL:
                businessKey = com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_DESKTOP_TYPE_PERSONAL;
                break;
            case RECOVERABLE:
                businessKey = com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_DESKTOP_TYPE_RECOVERABLE;
                break;
            case APP_LAYER:
                businessKey = com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_DESKTOP_TYPE_APP_LAYER;
                break;
            default:
                LOGGER.error("can not find strategyPattern in DesktopType, now is {}", cbbStrategyPattern);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private void writeDataToExcelFile(List<UserLoginRecordExportDTO> exportList, String tmpFilePath, Integer totalCount) throws Exception {
        Date startTime = new Date();
        try {
            String message = TOTAL_EXPORT + exportList.size() + "/" + totalCount;
            ExportExcelUtils.generateTotalExcel(exportList, tmpFilePath, UserLoginRecordExportDTO.class,
                    EXPORT_USER_USE_INFO_SHEET_NAME, new String[]{EXPORT_USER_USE_INFO_FIRST_INFO, message});
            LOGGER.info("写入Excel文件[{}]成功，耗时[{} ms]", tmpFilePath, new Date().getTime() - startTime.getTime());
        } catch (Exception e) {
            LOGGER.error("写入Excel临时文件[{}]错误", tmpFilePath, e);
            throw e;
        }
    }

    private void zipExcelFile(String excelFileDir, String excelFileZip) throws IOException {
        //压缩多个excel到zip
        LOGGER.info("准备压缩导出用户信息数据文件[{}]", excelFileDir);
        Date startTime = new Date();
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(excelFileZip));
            File excelFile = new File(excelFileDir);
            List<File> fileList = Arrays.asList(excelFile.listFiles());
            for (File file : fileList) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    out.putNextEntry(new ZipEntry(file.getName()));
                    byte[] bufferByteArr = new byte[1024 * 1024];
                    int len;
                    // 读入需要下载的文件的内容，打包到zip文件
                    while ((len = fis.read(bufferByteArr)) > 0) {
                        out.write(bufferByteArr, 0, len);
                    }
                    out.closeEntry();
                }
            }
            Date endTime = new Date();
            LOGGER.info("压缩Excel文件[{}]成功，耗时[{} ms]", excelFileZip, endTime.getTime() - startTime.getTime());
        } catch (Exception e) {
            LOGGER.error("压缩Excel临时文件[{}]错误", excelFileZip, e);
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("压缩Excel临时文件[{}]错误", excelFileZip, e);
                }
            }
        }
    }

    private String getExcelFileName(String index) {
        return join(EXPORT_USER_RECORD, SPLIT_UNDERLINE, index, EXCEL_SUFFIX);
    }

    private String getZipFilePath(String randomDir) {
        return join(EXPORT_USER_RECORD_TMP_DIRECTORY, File.separator, EXPORT_USER_RECORD, File.separator, randomDir, ZIP_SUFFIX);
    }

    private String getExcelFileDir(String randomDir) {
        return join(EXPORT_USER_RECORD_TMP_DIRECTORY, File.separator, EXPORT_USER_RECORD, File.separator, randomDir, File.separator);
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

    /**
     * 删除旧的缓存
     *
     * @param fileName 文件名
     */
    private void deleteOldFile(String fileName) {
        try {
            File f = new File(fileName);
            if (f.isDirectory()) {
                File[] fileArr = f.listFiles();
                for (File subFile : fileArr) {
                    Files.deleteIfExists(Paths.get(subFile.getAbsolutePath()));
                }
            }
            Files.deleteIfExists(Paths.get(fileName));
        } catch (Exception e) {
            LOGGER.error("删除文件[{}]异常", fileName, e);
        }
    }

    @Override
    public GetExportCacheResponse getExportDataCache(String userId) {
        Assert.notNull(userId, "userId is null");
        ExportFileInfoDTO cache = cacheMgt.getCache(userId);
        ExportCacheDTO dto = new ExportCacheDTO();
        if (Objects.nonNull(cache)) {
            dto.setState(cache.getState());
            dto.setExportFilePath(cache.getExportFilePath());
            dto.setFileName(cache.getFileName());
        } else {
            dto.setState(ExportDataStateEnums.DOING);
        }

        return new GetExportCacheResponse(dto);
    }

    @Override
    public GetExportFileResponse getExportFile(String userId) throws BusinessException {
        Assert.notNull(userId, "userId is null");
        ExportFileInfoDTO cache = cacheMgt.getCache(userId);
        if (Objects.isNull(cache)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EXPORT_EXCEL_DATA_NOT_EXIST);
        }
        String exportFilePath = cache.getExportFilePath();
        File file = new File(exportFilePath);
        if (!file.exists()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EXPORT_EXCEL_FILE_NOT_EXIST);
        }

        return new GetExportFileResponse(file);
    }

    @Override
    public void clearOldFile() {
        String filePath = join(EXPORT_USER_RECORD_TMP_DIRECTORY, File.separator, EXPORT_USER_RECORD);
        Path path = Paths.get(filePath);
        long cutoff = System.currentTimeMillis() - CLEAR_OLD_FILE_MILLS;
        try {
            Files.walkFileTree(path,new SimpleFileVisitor<Path>() {
                //遍历删除文件
                public FileVisitResult visitFile(Path file, @Nullable BasicFileAttributes attrs) throws IOException {
                    Assert.notNull(file, "file must not be null");
                    if (file.toFile().lastModified() < cutoff) {
                        Files.delete(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                //遍历删除目录
                public FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) throws IOException {
                    Assert.notNull(dir, "dir must not be null");
                    if (dir.toFile().listFiles().length == 0) {
                        Files.delete(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            LOGGER.error("清除导出的用户使用信息失败", e);
        }
    }
}