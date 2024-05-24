package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.ExportSoftwareAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.ExportSoftwareDataStateEnums;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request.SoftwarePageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.service.SoftwareService;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.cache.ExportSoftwareDataCacheMgt;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.constant.SoftwareControlBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoSoftwareDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoSoftwareGroupDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.ViewRcoSoftwareGroupCountDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareGroupEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.ViewRcoSoftwareGroupCountEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ExportUtils;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.constants.SoftwareControlConstants.SOFTWARE_IMPORT_SOFTWARE_NULL_NAME;

/**
 * Description: 导出软件信息API实现类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/18
 *
 * @author lihengjing
 */

public class ExportSoftwareAPIImpl implements ExportSoftwareAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportSoftwareAPIImpl.class);


    @Autowired
    private ExportSoftwareDataCacheMgt cacheMgt;

    @Autowired
    private RcoSoftwareDAO rcoSoftwareDAO;

    @Autowired
    private RcoSoftwareGroupDAO rcoSoftwareGroupDAO;

    @Autowired
    private ViewRcoSoftwareGroupCountDAO viewRcoSoftwareGroupCountDAO;

    private static final String FILE_PREFIX = "export_software_";

    private static final String FILE_POSTFIX = ".xlsx";

    private static final String EXPORT_SOFTWARE_THREAD_NAME = "exportSoftwareInfo";


    // 数据库查询页宽

    private static final int PAGE_SIZE = 20000;


    @Override
    public DefaultResponse exportDataAsync(ExportSoftwareRequest request) {
        Assert.notNull(request, "request is null");
        String userId = request.getUserId();
        Assert.notNull(userId, "userId is null");
        com.ruijie.rcos.sk.webmvc.api.vo.Sort[] sortArr = request.getSortArr();
        Assert.notNull(sortArr, "sortArr is null");
        String key = request.genExportSoftwareKey();
        ExportSoftwareFileInfoDTO cache = cacheMgt.getCache(key);
        if (Objects.nonNull(cache) && ExportSoftwareDataStateEnums.DOING.equals(cache.getState())) {
            LOGGER.info("导出任务正在运行，不要重复进行导出操作");
        } else {
            String tmpFileName = getTmpFileName(key);
            // 清空旧缓存
            cacheMgt.deleteCache(key);
            // 在删除一次，此处是为了防止缓存不存在而目录文件中已有文件，确保只有一个文件
            deleteOldFile(tmpFileName);
            ExportSoftwareFileInfoDTO newCache = new ExportSoftwareFileInfoDTO();
            cacheMgt.save(key, newCache);
            // 开启线程导出excel
            ThreadExecutors.submit(EXPORT_SOFTWARE_THREAD_NAME, () -> {
                try {
                    List<ExportSoftwareDTO> exportSoftwareList = getExportSoftwareList(request);
                    LOGGER.info("准备导出软件信息数据，导出条目为{}", exportSoftwareList.size());
                    String exportFilePath = exportAndGetResultPath(exportSoftwareList, tmpFileName);
                    newCache.setState(ExportSoftwareDataStateEnums.DONE);
                    newCache.setCreateTimestamp(System.currentTimeMillis());
                    newCache.setExportFilePath(exportFilePath);
                    newCache.setFileName(tmpFileName);
                    LOGGER.info("导出软件信息成功，导出路径是{}", exportFilePath);
                    cacheMgt.save(key, newCache);
                } catch (Exception e) {
                    LOGGER.error("导出文件出错，错误原因是{}", e);
                    cacheMgt.updateState(key, ExportSoftwareDataStateEnums.FAULT);
                }
            });
        }
        return DefaultResponse.Builder.success();
    }


    @Override
    public GetExportSoftwareCacheResponse getExportDataCache(ExportSoftwareRequest request) {
        Assert.notNull(request, "request is null");
        String userId = request.getUserId();
        Assert.notNull(userId, "userId is null");
        String key = request.genExportSoftwareKey();
        ExportSoftwareFileInfoDTO cache = cacheMgt.getCache(key);
        ExportSoftwareCacheDTO dto = new ExportSoftwareCacheDTO();
        if (Objects.nonNull(cache)) {
            dto.setState(cache.getState());
            dto.setExportFilePath(cache.getExportFilePath());
            dto.setFileName(cache.getFileName());
        } else {
            dto.setState(ExportSoftwareDataStateEnums.DOING);
        }
        GetExportSoftwareCacheResponse response = new GetExportSoftwareCacheResponse(dto);
        return response;
    }

    @Override
    public GetExportSoftwareFileResponse getExportFile(ExportSoftwareRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");

        String key = request.genExportSoftwareKey();
        ExportSoftwareFileInfoDTO cache = cacheMgt.getCache(key);
        if (Objects.isNull(cache)) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_EXPORT_SOFTWARE_DATA_NOT_EXIST);
        }
        String exportFilePath = cache.getExportFilePath();
        File file = new File(exportFilePath);
        if (!file.exists()) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_EXPORT_SOFTWARE_DATA_NOT_EXIST);
        }
        GetExportSoftwareFileResponse response = new GetExportSoftwareFileResponse(file);
        return response;
    }

    /**
     * 删除旧的缓存
     *
     * @param fileName
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
     * @param userId
     * @return 文件名
     */
    private String getTmpFileName(String userId) {
        String fileName = StringUtils.join(FILE_PREFIX, userId, FILE_POSTFIX);
        return fileName;
    }

    /**
     * 导出excel并且获取excel存放路径
     *
     * @param fileName 文件名
     * @return 路径
     */
    private String exportAndGetResultPath(List<ExportSoftwareDTO> dataList, String fileName) throws Exception {

        Assert.notNull(fileName, "fileName is null");
        String tmpFilePath = Constants.EXPORT_TMP_DIRECTORY + File.separator + fileName;
        checkDirectory();
        ExportUtils.generateExcel(dataList, tmpFilePath, ExportSoftwareDTO.class);
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

    private List<ExportSoftwareDTO> getExportSoftwareList(ExportSoftwareRequest request) throws BusinessException {
        List<Sort.Order> orderList = new ArrayList<>();
        Arrays.stream(request.getSortArr()).forEach(sortVO -> {
            Sort.Direction directionEnum = Sort.Direction.fromString(sortVO.getDirection().name());
            Sort.Order order = new Sort.Order(directionEnum, sortVO.getSortField());
            orderList.add(order);
        });

        List<RcoSoftwareEntity> rcoSoftwareEntityList;
        UUID softwareGroupId = request.getSoftwareGroupId();
        if (softwareGroupId != null) {
            rcoSoftwareEntityList = rcoSoftwareDAO.findByGroupId(softwareGroupId, Sort.by(orderList));
        } else {
            rcoSoftwareEntityList = rcoSoftwareDAO.findAll(Sort.by(orderList));
        }

        List<ExportSoftwareDTO> resultList = new ArrayList<>();
        if (!rcoSoftwareEntityList.isEmpty()) {
            //构造绿色软件的映射关系
            Map<UUID, List<RcoSoftwareEntity>> greenSoftwareMap = genGreenSoftwareMapping(rcoSoftwareEntityList);
            List<RcoSoftwareGroupEntity> rcoSoftwareGroupEntityList = rcoSoftwareGroupDAO.findAll();
            Map<UUID, RcoSoftwareGroupEntity> softwareGroupEntityMap = new HashMap<>();
            if (!rcoSoftwareEntityList.isEmpty()) {
                softwareGroupEntityMap = rcoSoftwareGroupEntityList.stream().collect(Collectors.toMap(RcoSoftwareGroupEntity::getId, o -> o));
            }
            Map<UUID, RcoSoftwareGroupEntity> finalSoftwareGroupEntityMap = softwareGroupEntityMap;

            rcoSoftwareEntityList.forEach(item -> {
                //先处理安装软件或者绿色软件目录
                if (item.getParentId() == null) {
                    SoftwareDTO softwareDTO = new SoftwareDTO();
                    BeanUtils.copyProperties(item, softwareDTO);
                    SoftwareGroupDTO softwareGroupDTO = new SoftwareGroupDTO();
                    RcoSoftwareGroupEntity softwareGroupEntity = finalSoftwareGroupEntityMap.get(item.getGroupId());
                    if (softwareGroupEntity != null) {
                        BeanUtils.copyProperties(softwareGroupEntity, softwareGroupDTO);
                    } else {
                        softwareGroupDTO = null;
                    }

                    ExportSoftwareDTO exportSoftwareDTO = new ExportSoftwareDTO(softwareDTO, softwareGroupDTO);
                    resultList.add(exportSoftwareDTO);

                    //获取绿色软件目录下的软件列表
                    List<RcoSoftwareEntity> childrenSoftwareEntityList = greenSoftwareMap.get(item.getId());
                    if (childrenSoftwareEntityList != null && !childrenSoftwareEntityList.isEmpty()) {
                        for (RcoSoftwareEntity child : childrenSoftwareEntityList) {
                            SoftwareDTO softwareTempDTO = new SoftwareDTO();
                            BeanUtils.copyProperties(child, softwareTempDTO);
                            ExportSoftwareDTO exportSoftwareTempDTO = new ExportSoftwareDTO(softwareTempDTO, softwareGroupDTO);
                            resultList.add(exportSoftwareTempDTO);
                        }
                    }
                }
            });
        }
        return resultList;
    }

    /**
     * 构造绿色软件中，目录跟目录里软件列表的映射关系
     *
     * @param rcoSoftwareEntityList
     * @return
     */
    private Map<UUID, List<RcoSoftwareEntity>> genGreenSoftwareMapping(List<RcoSoftwareEntity> rcoSoftwareEntityList) {
        Map<UUID, List<RcoSoftwareEntity>> greenSoftwareMap = new HashMap<>();
        if (!rcoSoftwareEntityList.isEmpty()) {
            rcoSoftwareEntityList.stream().forEach(entity -> {
                UUID parentId = entity.getParentId();
                if (parentId != null) {
                    List<RcoSoftwareEntity> softwareEntityList = greenSoftwareMap.get(parentId);
                    if (softwareEntityList == null) {
                        softwareEntityList = new ArrayList<>();
                    }
                    softwareEntityList.add(entity);
                    greenSoftwareMap.put(parentId, softwareEntityList);
                }
            });
        }
        return greenSoftwareMap;
    }
}
