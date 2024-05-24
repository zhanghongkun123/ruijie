package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.ExportUserProfilePathAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.ExportUserProfilePathDataStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.ImportUserProfilePathType;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.request.ExportUserProfilePathRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.cache.ExportPathCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileChildPathDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfilePathDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileChildPathEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfilePathEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileMainPathDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfilePathGroupDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfilePathMainEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfilePathGroupEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ExportUtils;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 导出路径实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/27
 *
 * @author WuShengQiang
 */
public class ExportUserProfilePathAPIImpl implements ExportUserProfilePathAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportUserProfilePathAPIImpl.class);

    private static final String FILE_PREFIX = "export_path_";

    private static final String FILE_POSTFIX = ".xlsx";

    private static final String EXPORT_PATH_THREAD_NAME = "exportPathInfo";

    @Autowired
    private ExportPathCacheManager cacheMgt;

    @Autowired
    private UserProfileMainPathDAO rcoUserProfilePathDAO;

    @Autowired
    private UserProfilePathGroupDAO rcoUserProfilePathGroupDAO;

    @Autowired
    private UserProfileChildPathDAO userProfileChildPathDAO;

    @Autowired
    private UserProfilePathDAO pathDAO;

    @Override
    public void exportDataAsync(ExportUserProfilePathRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");
        String userId = request.getUserId();
        Assert.notNull(userId, "userId is null");
        com.ruijie.rcos.sk.webmvc.api.vo.Sort[] sortArr = request.getSortArr();
        Assert.notNull(sortArr, "sortArr is null");
        String key = request.genExportPathKey();
        ExportUserProfilePathCacheDTO cache = cacheMgt.getCache(key);

        if (Objects.nonNull(cache) && ExportUserProfilePathDataStateEnum.DOING.equals(cache.getState())) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_EXPORT_PATH_DOING);
        } else {
            String tmpFileName = getTmpFileName(key);
            // 清空旧缓存
            cacheMgt.deleteCache(key);
            // 在删除一次，此处是为了防止缓存不存在而目录文件中已有文件，确保只有一个文件
            deleteOldFile(tmpFileName);
            ExportUserProfilePathCacheDTO newCache = new ExportUserProfilePathCacheDTO();
            cacheMgt.save(key, newCache);
            // 开启线程导出excel
            ThreadExecutors.submit(EXPORT_PATH_THREAD_NAME, () -> {
                try {
                    List<ExportPathDTO> exportPathList = getExportPathList(request);
                    LOGGER.info("准备导出路径信息数据，导出条目为{}", exportPathList.size());
                    String exportFilePath = exportAndGetResultPath(exportPathList, tmpFileName);
                    newCache.setState(ExportUserProfilePathDataStateEnum.DONE);
                    newCache.setCreateTimestamp(System.currentTimeMillis());
                    newCache.setExportFilePath(exportFilePath);
                    newCache.setFileName(tmpFileName);
                    LOGGER.info("导出路径信息成功，导出路径是:{}", exportFilePath);
                    cacheMgt.save(key, newCache);
                } catch (Exception e) {
                    LOGGER.error("导出文件出错，错误原因是:", e);
                    cacheMgt.updateState(key, ExportUserProfilePathDataStateEnum.FAULT);
                }
            });
        }
    }

    @Override
    public ExportUserProfilePathCacheResponse getExportDataCache(ExportUserProfilePathRequest request) {
        Assert.notNull(request, "request is null");
        String userId = request.getUserId();
        Assert.notNull(userId, "userId is null");
        ExportUserProfilePathCacheDTO cache = cacheMgt.getCache(request.genExportPathKey());
        // 缓存为空时,可能正在进行中
        if (Objects.isNull(cache)) {
            cache = new ExportUserProfilePathCacheDTO();
            cache.setState(ExportUserProfilePathDataStateEnum.DOING);
        }
        return new ExportUserProfilePathCacheResponse(cache);
    }

    @Override
    public ExportUserProfilePathFileResponse getExportFile(ExportUserProfilePathRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");
        String userId = request.getUserId();
        Assert.notNull(userId, "userId is null");

        ExportUserProfilePathCacheDTO cache = cacheMgt.getCache(request.genExportPathKey());
        if (Objects.isNull(cache)) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_EXPORT_PATH_FILE_NOT_EXIST);
        }
        String exportFilePath = cache.getExportFilePath();
        File file = new File(exportFilePath);
        if (!file.exists()) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_EXPORT_PATH_FILE_NOT_EXIST);
        }
        return new ExportUserProfilePathFileResponse(file);
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
    private String exportAndGetResultPath(List<ExportPathDTO> dataList, String fileName) throws Exception {
        Assert.notNull(fileName, "fileName is null");
        String tmpFilePath = Constants.EXPORT_TMP_DIRECTORY + File.separator + fileName;
        checkDirectory();
        ExportUtils.generateExcel(dataList, tmpFilePath, ExportPathDTO.class);
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

    private List<ExportPathDTO> getExportPathList(ExportUserProfilePathRequest request) throws BusinessException {
        List<Sort.Order> orderList = new ArrayList<>();
        Arrays.stream(request.getSortArr()).forEach(sortVO -> {
            Sort.Direction directionEnum = Sort.Direction.fromString(sortVO.getDirection().name());
            Sort.Order order = new Sort.Order(directionEnum, sortVO.getSortField());
            orderList.add(order);
        });
        List<UserProfilePathMainEntity> rcoUserProfilePathEntityList;
        UUID groupId = request.getGroupId();
        if (groupId != null) {
            rcoUserProfilePathEntityList = rcoUserProfilePathDAO.findByGroupId(groupId, Sort.by(orderList));
        } else {
            rcoUserProfilePathEntityList = rcoUserProfilePathDAO.findAll(Sort.by(orderList));
        }
        List<ExportPathDTO> resultList = new ArrayList<>();
        if (!rcoUserProfilePathEntityList.isEmpty()) {
            // 查询所有分组对象,转换成map
            List<UserProfilePathGroupEntity> pathGroupEntityList = rcoUserProfilePathGroupDAO.findAll();
            Map<UUID, UserProfilePathGroupEntity> finalSoftwareGroupEntityMap =
                    pathGroupEntityList.stream().collect(Collectors.toMap(UserProfilePathGroupEntity::getId, o -> o));

            rcoUserProfilePathEntityList.forEach(item -> {
                if (item.getImportUserProfilePathType() == ImportUserProfilePathType.NORMAL) {
                    UserProfilePathDTO pathDTO = new UserProfilePathDTO();
                    BeanUtils.copyProperties(item, pathDTO);
                    // 从map中取出分组信息
                    UserProfilePathGroupEntity pathGroupEntity = finalSoftwareGroupEntityMap.get(item.getGroupId());
                    UserProfilePathGroupDTO pathGroupDTO = null;
                    if (pathGroupEntity != null) {
                        pathGroupDTO = new UserProfilePathGroupDTO();
                        BeanUtils.copyProperties(pathGroupEntity, pathGroupDTO);
                    }
                    resultList.addAll(createExportPathFromUserProfile(pathDTO, pathGroupDTO));
                }
            });
        }
        return resultList;
    }

    private List<ExportPathDTO> createExportPathFromUserProfile(UserProfilePathDTO pathDTO,  UserProfilePathGroupDTO pathGroupDTO) {
        List<ExportPathDTO> exportPathDTOList = new ArrayList<>();

        String groupName = "";
        String groupDesc = "";
        if (pathGroupDTO != null) {
            groupName = pathGroupDTO.getName();
            groupDesc = pathGroupDTO.getDescription();
        }

        List<UserProfileChildPathEntity> childPathEntityList = userProfileChildPathDAO.findByUserProfilePathId(pathDTO.getId());
        List<UserProfilePathEntity> pathEntityList = pathDAO.findByUserProfilePathId(pathDTO.getId());
        for (UserProfileChildPathEntity childPath : childPathEntityList) {
            for (UserProfilePathEntity path : pathEntityList) {
                if (childPath.getId().equals(path.getUserProfileChildPathId())) {
                    ExportPathDTO exportPathDTO = new ExportPathDTO();
                    exportPathDTO.setPath(path.getPath());
                    exportPathDTO.setPathName(pathDTO.getName());
                    exportPathDTO.setPathDesc(pathDTO.getDescription());
                    exportPathDTO.setMode(childPath.getMode().getText());
                    exportPathDTO.setType(childPath.getType().getText());
                    exportPathDTO.setGroupName(groupName);
                    exportPathDTO.setGroupDesc(groupDesc);
                    exportPathDTO.formatFor();
                    exportPathDTOList.add(exportPathDTO);
                }
            }
        }

        return exportPathDTOList;
    }

}