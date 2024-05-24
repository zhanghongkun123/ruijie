package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileCleanGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileFailCleanRequestEntity;

import java.util.List;
import java.util.UUID;

/**
 * Description: UPM辅助工作类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年09月05日
 *
 * @author zwf
 */
public interface UserProfileHelpService {
    /**
     * 构建下发的清理配置的请求
     * @param desktopId 云桌面ID
     * @param messageDTO 原始请求
     * @param failCleanEntityList 缓存的清理命令
     */
    void buildGuestToolMessageForCleanRequest(UUID desktopId,CbbGuesttoolMessageDTO messageDTO,
                                              List<UserProfileFailCleanRequestEntity> failCleanEntityList);

    /**
     * 构建清理配置的命令对象
     *
     * @param failCleanEntityList 缓存的清理命令
     * @return 命令对象
     */
    UserProfileCleanGuestToolMsgDTO buildCleanGuestToolMsgDTO(List<UserProfileFailCleanRequestEntity> failCleanEntityList);
}