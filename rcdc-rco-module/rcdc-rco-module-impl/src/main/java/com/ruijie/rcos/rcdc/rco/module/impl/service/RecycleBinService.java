package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Recyclebin service
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月7日
 * 
 * @author artom
 */
public interface RecycleBinService {

    /**
     * *删除
     * 
     * @param cbbDesktopId entity id
     * @throws BusinessException 业务异常
     */
    void deleteDeskCompletely(UUID cbbDesktopId) throws BusinessException;

    /**
     * *恢复
     * 
     * @param cbbDesktopId cbb桌面id
     * @throws BusinessException 业务异常
     */
    void recover(UUID cbbDesktopId) throws BusinessException;
    
    /**
     * *指定用户恢复
     * 
     * @param cbbDesktopId cbb桌面id
     * @param userId 指定用户id
     * @throws BusinessException 业务异常
     */
    void recoverByAssignUserId(UUID cbbDesktopId, UUID userId) throws BusinessException;

    /**
     * *指定桌面池恢复
     *
     * @param deskId              cbb桌面id
     * @param userIdList          桌面关联用户id列表
     * @param assignDesktopPoolId 指定桌面池id
     * @throws BusinessException 业务异常
     */
    void recoverByAssignDesktopPoolId(UUID deskId, @Nullable List<UUID> userIdList, UUID assignDesktopPoolId) throws BusinessException;
}
