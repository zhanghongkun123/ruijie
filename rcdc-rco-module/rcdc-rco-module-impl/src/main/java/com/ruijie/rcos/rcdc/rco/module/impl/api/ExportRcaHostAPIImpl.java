package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rca.module.def.request.ExportThirdPartyRcaHostRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportRcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportCloudDesktopDataStateEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportRcaHostDataStateEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ExportRcaHostRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportRcaHostCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportRcaHostFileResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.ExportRcaHostDataCacheMgt;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewRcaHostDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.ExportRcaHostFileInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ExportUtils;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: 导出云桌面信息API实现类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/16
 *
 * @author zhiweiHong
 */

public class ExportRcaHostAPIImpl implements ExportRcaHostAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportRcaHostAPIImpl.class);


    @Autowired
    private ExportRcaHostDataCacheMgt cacheMgt;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private ViewRcaHostDesktopDetailDAO viewRcaHostDesktopDetailDAO;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;


    private static final String FILE_PREFIX = "export";

    private static final String FILE_POSTFIX = ".xlsx";

    // 数据库查询页宽

    private static final int PAGE_SIZE = 20000;


    @Override
    public DefaultResponse exportDataAsync(ExportRcaHostRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");
        String userId = request.getUserId();
        Assert.notNull(userId, "userId is null");

        ExportRcaHostFileInfoDTO cache = cacheMgt.getCache(userId);
        if (Objects.nonNull(cache) && ExportRcaHostDataStateEnums.DOING.equals(cache.getState())) {
            LOGGER.info("导出任务正在运行，不要重复进行导出操作");
        } else {
            String tmpFileName = getTmpFileName(userId);
            // 清空旧缓存
            cacheMgt.deleteCache(userId);
            // 在删除一次，此处是为了防止缓存不存在而目录文件中已有文件，确保只有一个文件
            try {
                deleteOldFile(tmpFileName);
            } catch (IOException e) {
                LOGGER.error("删除文件异常", e);
                throw new BusinessException(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXPORT_DELETE_FILE_ERROR, e);
            }
            ExportRcaHostFileInfoDTO newCache = new ExportRcaHostFileInfoDTO();
            cacheMgt.save(userId, newCache);
            // 开启线程导出excel
            ThreadExecutors.submit("exportAppHost", () -> {
                try {
                    List<ExportRcaHostDTO> exportRcaHostList = getExportRcaHostList(request);
                    LOGGER.info("准备导出第三方应用主机数据，导出条目为{}", exportRcaHostList.size());
                    String exportFilePath = exportAndGetResultPath(exportRcaHostList, tmpFileName);
                    newCache.setState(ExportRcaHostDataStateEnums.DONE);
                    newCache.setCreateTimestamp(System.currentTimeMillis());
                    newCache.setExportFilePath(exportFilePath);
                    newCache.setFileName(tmpFileName);
                    LOGGER.info("导出第三方应用成功，导出路径是{}", exportFilePath);
                    cacheMgt.save(userId, newCache);
                } catch (Exception e) {
                    LOGGER.error("导出文件出错，错误原因是{}", e);
                    cacheMgt.updateState(userId, ExportRcaHostDataStateEnums.FAULT);
                }
            });
        }
        return DefaultResponse.Builder.success();
    }

    @Override
    public GetExportRcaHostCacheResponse getExportDataCache(String userId) {
        Assert.notNull(userId, "userId is null");
        ExportRcaHostFileInfoDTO cache = cacheMgt.getCache(userId);
        ExportRcaHostCacheDTO dto = new ExportRcaHostCacheDTO();
        if (Objects.nonNull(cache)) {
            dto.setState(cache.getState());
            dto.setExportFilePath(cache.getExportFilePath());
            dto.setFileName(cache.getFileName());
        } else {
            dto.setState(ExportRcaHostDataStateEnums.DOING);
        }

        return new GetExportRcaHostCacheResponse(dto);
    }


    @Override
    public GetExportRcaHostFileResponse getExportFile(String userId) throws BusinessException {
        Assert.notNull(userId, "userId is null");
        ExportRcaHostFileInfoDTO cache = cacheMgt.getCache(userId);
        if (Objects.isNull(cache)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXPORT_DATA_NOT_EXIST);
        }
        String exportFilePath = cache.getExportFilePath();
        File file = new File(exportFilePath);
        if (!file.exists()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXPORT_DATA_NOT_EXIST);
        }

        return new GetExportRcaHostFileResponse(file);
    }

    /**
     * 删除旧的缓存
     *
     * @param fileName
     */
    private void deleteOldFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        if (Files.exists(path)) {
            Files.delete(path);
            LOGGER.info("旧缓存存在，删除文件[{}]", fileName);
        }
    }

    /**
     * 生产文件名
     *
     * @param userId
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
    private String exportAndGetResultPath(List<ExportRcaHostDTO> dataList, String fileName) throws Exception {
        String tmpFilePath = Constants.EXPORT_TMP_DIRECTORY + File.separator + fileName;
        checkDirectory();
        ExportUtils.generateExcel(dataList, tmpFilePath, ExportRcaHostDTO.class);
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

    private List<ExportRcaHostDTO> getExportRcaHostList(ExportRcaHostRequest request) {
        ExportThirdPartyRcaHostRequest exportThirdPartyRcaHostRequest = new ExportThirdPartyRcaHostRequest();
        BeanUtils.copyProperties(request, exportThirdPartyRcaHostRequest);
        List<RcaHostDTO> rcaHostDTOList = rcaHostAPI.findAllExportThirdPartyRcaHostList(exportThirdPartyRcaHostRequest);
        return rcaHostDTOList.stream().map(rcaHostDTO -> new ExportRcaHostDTO(rcaHostDTO)).collect(Collectors.toList());
    }
}
