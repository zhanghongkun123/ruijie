package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.cache;

import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dto.AuditFileUploadedCacheDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 文件上传完成缓存处理器
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/6
 *
 * @author TD
 */
@Service
public class AuditFileUploadedCacheManager {

    /**
     * 文件上传完成缓存
     */
    private static final Map<UUID, AuditFileUploadedCacheDTO> FILE_UPLOAD_CACHE_MAP = Maps.newConcurrentMap();

    /**
     * 保存申请单上传文件操作
     * @param fileUploadedCacheDTO 操作信息
     */
    public void save(AuditFileUploadedCacheDTO fileUploadedCacheDTO) {
        Assert.notNull(fileUploadedCacheDTO, "fileUploadedCacheDTO can be not null");
        if (FILE_UPLOAD_CACHE_MAP.containsKey(fileUploadedCacheDTO.getFileId())) {
            return;
        }
        FILE_UPLOAD_CACHE_MAP.put(fileUploadedCacheDTO.getFileId(), fileUploadedCacheDTO);
    }

    /**
     * 删除申请单上传文件操作缓存
     * @param fileId 文件单ID
     */
    public void deleteFileUploaded(UUID fileId) {
        Assert.notNull(fileId, "fileId can be not null");
        FILE_UPLOAD_CACHE_MAP.remove(fileId);
    }

    /**
     * 取消指定申请单上传文件操作
     * @param applyId 申请单
     */
    public void cancelApplyIdFileUploaded(UUID applyId) {
        Assert.notNull(applyId, "applyId can be not null");
        FILE_UPLOAD_CACHE_MAP.values().stream().filter(fileUploadedCacheDTO -> Objects.equals(fileUploadedCacheDTO.getApplyId(), applyId))
                .forEach(fileUploadedCacheDTO -> fileUploadedCacheDTO.getFuture().cancel(true));
    }

    /**
     * 取消全部申请单上传文件操作
     * @return Set 已取消的任务申请单集合
     */
    public Set<UUID> cancelAllFileUploaded() {
        return FILE_UPLOAD_CACHE_MAP.values().stream().map(fileUploadedCacheDTO -> {
            fileUploadedCacheDTO.getFuture().cancel(true);
            return fileUploadedCacheDTO.getApplyId();
        }).collect(Collectors.toSet());
    }
}
