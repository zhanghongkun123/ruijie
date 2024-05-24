package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportCloudDesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportCloudDesktopDataStateEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ExportCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCloudDesktopCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCloudDesktopFileResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DesktopType;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.ExportCloudDesktopDataCacheMgt;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewRcaHostDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.ExportCloudDesktopFileInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaHostDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
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
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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

public class ExportCloudDesktopAPIImpl implements ExportCloudDesktopAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportCloudDesktopAPIImpl.class);


    @Autowired
    private ExportCloudDesktopDataCacheMgt cacheMgt;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private ViewRcaHostDesktopDetailDAO viewRcaHostDesktopDetailDAO;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private ServerModelAPI serverModelAPI;


    private static final String FILE_PREFIX = "export";

    private static final String FILE_POSTFIX = ".xlsx";

    // 数据库查询页宽

    private static final int PAGE_SIZE = 20000;


    @Override
    public DefaultResponse exportDataAsync(ExportCloudDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");
        String userId = request.getUserId();
        Assert.notNull(userId, "userId is null");
        com.ruijie.rcos.sk.webmvc.api.vo.Sort[] sortArr = request.getSortArr();
        Assert.notNull(sortArr, "sortArr is null");

        ExportCloudDesktopFileInfoDTO cache = cacheMgt.getCache(userId);
        if (Objects.nonNull(cache) && ExportCloudDesktopDataStateEnums.DOING.equals(cache.getState())) {
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
                throw new BusinessException(BusinessKey.RCDC_RCO_DELETE_FILE_ERROR, e);
            }
            ExportCloudDesktopFileInfoDTO newCache = new ExportCloudDesktopFileInfoDTO();
            cacheMgt.save(userId, newCache);
            // 开启线程导出excel
            ThreadExecutors.submit("exportCloudDesktop", () -> {
                try {
                    List<ExportCloudDesktopDTO> exportCloudDesktopList = getExportCloudDesktopList(request);
                    LOGGER.info("准备导出云桌面数据，导出条目为{}", exportCloudDesktopList.size());
                    String exportFilePath = exportAndGetResultPath(exportCloudDesktopList, null
                            , tmpFileName, request.getImageUsage().name());
                    newCache.setState(ExportCloudDesktopDataStateEnums.DONE);
                    newCache.setCreateTimestamp(System.currentTimeMillis());
                    newCache.setExportFilePath(exportFilePath);
                    newCache.setFileName(tmpFileName);
                    LOGGER.info("导出云桌面成功，导出路径是{}", exportFilePath);
                    cacheMgt.save(userId, newCache);
                } catch (Exception e) {
                    LOGGER.error("导出文件出错，错误原因是{}", e);
                    cacheMgt.updateState(userId, ExportCloudDesktopDataStateEnums.FAULT);
                }
            });
        }
        return DefaultResponse.Builder.success();
    }

    @Override
    public DefaultResponse exportRcaHostDataAsync(ExportCloudDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");
        String userId = request.getUserId();
        Assert.notNull(userId, "userId is null");
        com.ruijie.rcos.sk.webmvc.api.vo.Sort[] sortArr = request.getSortArr();
        Assert.notNull(sortArr, "sortArr is null");

        ExportCloudDesktopFileInfoDTO cache = cacheMgt.getCache(userId);
        if (Objects.nonNull(cache) && ExportCloudDesktopDataStateEnums.DOING.equals(cache.getState())) {
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
                throw new BusinessException(BusinessKey.RCDC_RCO_DELETE_FILE_ERROR, e);
            }
            ExportCloudDesktopFileInfoDTO newCache = new ExportCloudDesktopFileInfoDTO();
            cacheMgt.save(userId, newCache);
            // 开启线程导出excel
            ThreadExecutors.submit("exportCloudDesktop", () -> {
                try {
                    List<ExportRcaHostDesktopDTO> exportRcaHostCloudList = getExportRcaHostCloudDesktopList(request);
                    LOGGER.info("准备导出应用数据，导出条目为{}", exportRcaHostCloudList.size());
                    String exportFilePath = exportAndGetResultPath(null, exportRcaHostCloudList, tmpFileName, request.getImageUsage().name());
                    newCache.setState(ExportCloudDesktopDataStateEnums.DONE);
                    newCache.setCreateTimestamp(System.currentTimeMillis());
                    newCache.setExportFilePath(exportFilePath);
                    newCache.setFileName(tmpFileName);
                    LOGGER.info("导出云桌面成功，导出路径是{}", exportFilePath);
                    cacheMgt.save(userId, newCache);
                } catch (Exception e) {
                    LOGGER.error("导出文件出错，错误原因是{}", e);
                    cacheMgt.updateState(userId, ExportCloudDesktopDataStateEnums.FAULT);
                }
            });
        }
        return DefaultResponse.Builder.success();
    }


    @Override
    public GetExportCloudDesktopCacheResponse getExportDataCache(String userId) {
        Assert.notNull(userId, "userId is null");
        ExportCloudDesktopFileInfoDTO cache = cacheMgt.getCache(userId);
        ExportCloudDesktopCacheDTO dto = new ExportCloudDesktopCacheDTO();
        if (Objects.nonNull(cache)) {
            dto.setState(cache.getState());
            dto.setExportFilePath(cache.getExportFilePath());
            dto.setFileName(cache.getFileName());
        } else {
            dto.setState(ExportCloudDesktopDataStateEnums.DOING);
        }

        return new GetExportCloudDesktopCacheResponse(dto);
    }


    @Override
    public GetExportCloudDesktopFileResponse getExportFile(String userId) throws BusinessException {
        Assert.notNull(userId, "userId is null");
        ExportCloudDesktopFileInfoDTO cache = cacheMgt.getCache(userId);
        if (Objects.isNull(cache)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EXPORT_DATA_NOT_EXIST);
        }
        String exportFilePath = cache.getExportFilePath();
        File file = new File(exportFilePath);
        if (!file.exists()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EXPORT_DATA_NOT_EXIST);
        }

        return new GetExportCloudDesktopFileResponse(file);
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
    private String exportAndGetResultPath(List<ExportCloudDesktopDTO> dataList, List<ExportRcaHostDesktopDTO> exportRcaHostCloudList
            , String fileName, String imageUsage) throws Exception {

        Assert.notNull(fileName, "fileName is null");

        String tmpFilePath = Constants.EXPORT_TMP_DIRECTORY + File.separator + fileName;
        checkDirectory();
        // 如果是VDI服务器模式 正常导出
        if (serverModelAPI.isVdiModel()) {
            if (imageUsage.equals(ImageUsageTypeEnum.APP.name())) {
                // 区分应用主机和桌面主机列表导出
                List<ExportRcaHostDesktopDTO> appDesktopList = new ArrayList();
                exportRcaHostCloudList.forEach(item -> {
                    ExportRcaHostDesktopDTO exportAppCloudDesktopDTO = new ExportRcaHostDesktopDTO();
                    BeanUtils.copyProperties(item, exportAppCloudDesktopDTO);
                    appDesktopList.add(exportAppCloudDesktopDTO);
                });
                ExportUtils.generateExcel(appDesktopList, tmpFilePath, ExportRcaHostDesktopDTO.class);
            } else {
                ExportUtils.generateExcel(dataList, tmpFilePath, ExportCloudDesktopDTO.class);
            }

        } else {
            if (imageUsage.equals(ImageUsageTypeEnum.APP.name())) {
                // 如果不是VDI服务器模式 则使用MINI|IDV模式的导出
                List<ExportRcaHostDesktopDTO> miniAppList = new ArrayList();
                exportRcaHostCloudList.forEach(item -> {
                    ExportRcaHostDesktopDTO exportCloudDesktopDTO = new ExportRcaHostDesktopDTO();
                    BeanUtils.copyProperties(item, exportCloudDesktopDTO);
                    miniAppList.add(exportCloudDesktopDTO);
                });
                ExportUtils.generateExcel(miniAppList, tmpFilePath, ExportRcaHostDesktopDTO.class);
            } else {

                // 如果不是VDI服务器模式 则使用MINI|IDV模式的导出
                List<ExportMiniCloudDesktopDTO> miniList = new ArrayList();
                dataList.forEach(item -> {
                    ExportMiniCloudDesktopDTO exportCloudDesktopDTO = new ExportMiniCloudDesktopDTO();
                    BeanUtils.copyProperties(item, exportCloudDesktopDTO);
                    miniList.add(exportCloudDesktopDTO);
                });
                ExportUtils.generateExcel(miniList, tmpFilePath, ExportMiniCloudDesktopDTO.class);
            }

        }
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

    private List<ExportCloudDesktopDTO> getExportCloudDesktopList(ExportCloudDesktopRequest request) throws BusinessException {
        List<ExportCloudDesktopDTO> resultList = new ArrayList<>();

        // 如果当前用户并没有全部数据权限 并且拥有的数据权限集合为空 这时候返回空集合
        if (!request.getEnableAllGroupPermission() && CollectionUtils.isEmpty(request.getDesktopIdList())) {
            LOGGER.debug("当前用户没有全部数据权限，并且拥有的桌面数据权限集合为空,返回空集合，信息对象为:{}", JSON.toJSONString(request));
            return resultList;
        }

        List<Sort.Order> orderList = new ArrayList<>();
        Arrays.stream(request.getSortArr()).forEach(sortVO -> {
            Sort.Direction directionEnum = Sort.Direction.fromString(sortVO.getDirection().name());
            Sort.Order order = new Sort.Order(directionEnum, sortVO.getSortField());
            orderList.add(order);
        });
        List<ViewUserDesktopEntity> viewUserDesktopEntityList = viewDesktopDetailDAO.findAll(Sort.by(orderList));

        viewUserDesktopEntityList = viewUserDesktopEntityList.stream().filter(item -> {
            // 如果是超级管理员角色|内置系统管理员则查询全部数据 要求不能为删除
            if (request.getEnableAllGroupPermission()) {
                return !item.getIsDelete();
            } else {
                return request.getDesktopIdList().contains(item.getCbbDesktopId()) && !item.getIsDelete();
            }
        }).collect(Collectors.toList());


        if (!viewUserDesktopEntityList.isEmpty()) {
            List<CloudDesktopDTO> cloudDesktopDTOList = queryCloudDesktopService.convertCloudDesktop(viewUserDesktopEntityList);
            cloudDesktopDTOList.forEach(item -> {
                if (DesktopType.APP_LAYER.toString().equals(item.getDesktopType()) && Objects.nonNull(item.getSystemDisk())) {
                    item.setSystemDisk(item.getSystemDisk() + Constants.SYSTEM_DISK_CAPACITY_INCREASE_SIZE);
                }

                // 云桌面IDV和VOI是同一个类型，要靠镜像类型区别
                if (CbbImageType.VOI.name().equals(item.getCbbImageType())) {
                    item.setDesktopCategory("TCI");
                }
                // 虚拟机类型为THIRD_PARTY，转换为“第三方”
                if (CbbCloudDeskType.THIRD.name().equals(item.getDesktopCategory())) {
                    item.setDesktopCategory(Constants.THIRD);
                }
                resultList.add(new ExportCloudDesktopDTO(item));
            });
        }

        return resultList;
    }

    private List<ExportRcaHostDesktopDTO> getExportRcaHostCloudDesktopList(ExportCloudDesktopRequest request) throws BusinessException {
        List<ExportRcaHostDesktopDTO> resultList = new ArrayList<>();

        // 如果当前用户并没有全部数据权限 并且拥有的数据权限集合为空 这时候返回空集合
        if (!request.getEnableAllGroupPermission() && CollectionUtils.isEmpty(request.getDesktopIdList())) {
            LOGGER.debug("当前用户没有全部数据权限，并且拥有的桌面数据权限集合为空,返回空集合，信息对象为:{}", JSON.toJSONString(request));
            return resultList;
        }

        List<Sort.Order> orderList = new ArrayList<>();
        Arrays.stream(request.getSortArr()).forEach(sortVO -> {
            Sort.Direction directionEnum = Sort.Direction.fromString(sortVO.getDirection().name());
            Sort.Order order = new Sort.Order(directionEnum, sortVO.getSortField());
            orderList.add(order);
        });
        List<ViewUserDesktopEntity> viewUserDesktopEntityList = viewDesktopDetailDAO.findAll(Sort.by(orderList));
        viewUserDesktopEntityList = viewUserDesktopEntityList.stream().filter(item -> {
            if (item.getImageUsage() == ImageUsageTypeEnum.DESK) {
                return false;
            }
            // 如果是超级管理员角色|内置系统管理员则查询全部数据 要求不能为删除
            if (request.getEnableAllGroupPermission()) {
                return !item.getIsDelete();
            } else {
                return request.getDesktopIdList().contains(item.getCbbDesktopId()) && !item.getIsDelete();
            }
        }).collect(Collectors.toList());


        if (!viewUserDesktopEntityList.isEmpty()) {
            List<CloudDesktopDTO> cloudDesktopDTOList = queryCloudDesktopService.convertCloudDesktop(viewUserDesktopEntityList);
            List<ViewRcaHostDesktopEntity> viewRcaHostDesktopEntityList = viewRcaHostDesktopDetailDAO.findAll(Sort.by(orderList));
            // 填充对应的应用主机信息
            List<RcaHostDesktopDTO> rcaHostDesktopDTOList =
                    queryCloudDesktopService.convertToRcaHostDesktopList(cloudDesktopDTOList, viewRcaHostDesktopEntityList);

            rcaHostDesktopDTOList.forEach(item -> {
                resultList.add(new ExportRcaHostDesktopDTO(item));
            });
        }

        return resultList;
    }
}
