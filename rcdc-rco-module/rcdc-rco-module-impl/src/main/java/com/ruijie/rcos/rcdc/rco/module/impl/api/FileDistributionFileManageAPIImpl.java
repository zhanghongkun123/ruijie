package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionFileManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution.CreateDistributeFileRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution.EditDistributeFileRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DistributeFileDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DistributeFileEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/11 15:19
 *
 * @author zhangyichi
 */
public class FileDistributionFileManageAPIImpl implements FileDistributionFileManageAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDistributionFileManageAPIImpl.class);

    private static final List<FileDistributionTaskStatus> NOT_ALLOW_DELETE_FILE_TASK_STATUS_LIST =
            Arrays.asList(FileDistributionTaskStatus.RUNNING, FileDistributionTaskStatus.WAITING);

    @Autowired
    DistributeFileDAO distributeFileDAO;

    @Autowired
    private FileDistributionTaskManageAPI fileDistributionTaskManageAPI;

    @Override
    public UUID createFile(CreateDistributeFileRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        File sourceFile = new File(request.getSourcePath());
        File targetFile = new File(request.getTargetPath());
        DistributeFileDTO fileDTO = request.getFileDTO();

        // 校验文件
        validateFile(fileDTO, sourceFile);

        // 移动文件
        moveFile(sourceFile, targetFile);

        // 文件入库
        DistributeFileEntity fileEntity = new DistributeFileEntity();
        BeanCopyUtil.copy(fileDTO, fileEntity);
        distributeFileDAO.save(fileEntity);
        LOGGER.info("完成上传文件，文件名：{}", fileDTO.getFileName());

        return fileEntity.getId();
    }

    private void validateFile(DistributeFileDTO fileDTO, File tmpFile) throws BusinessException {
        // 若文件不存在，则抛出异常。
        if (!tmpFile.exists()) {
            LOGGER.error("已上传文件{}不存在", fileDTO.getFileName());
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_UPLOADED_FILE_NOT_EXIST, fileDTO.getFileName());
        }

        // 数据库中安装包若已经存在，则并抛出异常。
        DistributeFileEntity fileEntity = distributeFileDAO.findByFileName(fileDTO.getFileName());
        if (fileEntity != null) {
            LOGGER.error("文件{}已存在", fileDTO.getFileName());
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TARGET_FILE_EXIST, fileDTO.getFileName());
        }
    }

    @Override
    public DistributeFileDTO findByName(String fileName) {
        Assert.hasText(fileName, "fileName cannot be blank!");

        DistributeFileEntity fileEntity = distributeFileDAO.findByFileName(fileName);
        if (fileEntity == null) {
            // 如果不存在则返回null
            return null;
        }
        DistributeFileDTO fileDTO = new DistributeFileDTO();
        BeanCopyUtil.copy(fileEntity, fileDTO);
        return fileDTO;
    }

    @Override
    public DistributeFileDTO findById(UUID fileId) {
        Assert.notNull(fileId, "fileId cannot be null!");

        Optional<DistributeFileEntity> optional = distributeFileDAO.findById(fileId);
        if (!optional.isPresent()) {
            // 不存在则返回null
            return null;
        }
        DistributeFileEntity fileEntity = optional.get();
        DistributeFileDTO fileDTO = new DistributeFileDTO();
        BeanCopyUtil.copy(fileEntity, fileDTO);
        return fileDTO;
    }

    @Override
    public void editFile(EditDistributeFileRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        Optional<DistributeFileEntity> optional = distributeFileDAO.findById(request.getId());
        if (!optional.isPresent()) {
            LOGGER.error("分发文件不存在，ID[{}]", request.getId());
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_NOT_EXIST);
        }
        DistributeFileEntity fileEntity = optional.get();
        fileEntity.setDescription(request.getDescription());
        distributeFileDAO.save(fileEntity);
    }

    @Override
    public void deleteFile(UUID fileId, String saveDir) throws BusinessException {
        Assert.notNull(fileId, "fileId cannot be null!");
        Assert.hasText(saveDir, "saveDir cannot be blank!");

        DistributeFileDTO fileDTO = findById(fileId);
        if (fileDTO == null) {
            LOGGER.info("文件[{}]不存在或已删除", fileId);
            return;
        }

        checkFileAllowDelete(fileId);

        try {
            Files.deleteIfExists(Paths.get(saveDir + File.separator + fileDTO.getFileName()));
            distributeFileDAO.deleteById(fileId);
        } catch (Exception e) {
            LOGGER.error("删除文件[" + fileDTO.getFileName() + "]失败", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_DELETE_FAIL, e, fileDTO.getFileName());
        }
    }

    @Override
    public void existsUsed(UUID fileId) throws BusinessException {
        Assert.notNull(fileId, "fileId cannot be null!");
        checkFileAllowDelete(fileId);
    }

    private void moveFile(File sourceFile, File targetFile) throws BusinessException {
        LOGGER.info("开始移动分发文件：{}", sourceFile.getName());
        long fileSize = sourceFile.length();
        try {
            Files.move(sourceFile.toPath(), targetFile.toPath());
            targetFile.setReadable(true, false);
            targetFile.setExecutable(true, false);
        } catch (FileAlreadyExistsException e) {
            LOGGER.error("移动文件失败，文件已存在", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_MOVE_EXISTS, e);
        } catch (SecurityException e) {
            LOGGER.error("移动文件失败，权限不足", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_MOVE_SECURITY, e);
        } catch (IOException e) {
            LOGGER.error("移动文件失败", e);
            deleteSaveFile(targetFile);
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_MOVE_ERROR, e);
        }

        long resultFileSize = targetFile.length();
        // 如果安装包大小和前端上传的大小不一样，则表示文件上传过程中出错，则抛出异常。
        if (resultFileSize != fileSize) {
            LOGGER.error("文件移动出错，文件名：{}", sourceFile.getName());
            deleteSaveFile(targetFile);
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_SIZE_UNEQUAL, sourceFile.getName(), String.valueOf(fileSize),
                    String.valueOf(resultFileSize));
        }
        LOGGER.info("分发文件[{}]移动完成", sourceFile.getName());
    }

    private void deleteSaveFile(File file) {
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            LOGGER.error("移动文件异常，残留文件删除失败");
        }
    }

    private void checkFileAllowDelete(UUID fileId) throws BusinessException {
        List<DistributeTaskDTO> taskList =
                fileDistributionTaskManageAPI.findTaskByParameterContent(fileId.toString());
        if (CollectionUtils.isEmpty(taskList)) {
            return;
        }

        boolean hasRunningSubTask = taskList.stream().anyMatch(taskDTO -> fileDistributionTaskManageAPI.countSubTaskWithState(
                taskDTO.getId(), NOT_ALLOW_DELETE_FILE_TASK_STATUS_LIST) > 0);
        if (hasRunningSubTask) {
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_FILE_NOT_ALLOW_DELETE);
        }
    }
}
