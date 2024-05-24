package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.service;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.AttachDiskMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.UserDiskPoolEntity;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 用户与磁盘池服务接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/15
 *
 * @author TD
 */
public interface UserDiskPoolService {

    /**
     * 根据用户查询关联的磁盘池信息
     *
     * @param cbbUserDetailDTO 用户信息类
     * @param request          分页查询请求
     * @param isAddGroup       是否添加组查询
     * @return 用户关联磁盘池信息
     */
    DefaultPageResponse<UserDiskPoolEntity> pageQueryUserDiskPool(IacUserDetailDTO cbbUserDetailDTO, PageSearchRequest request, Boolean isAddGroup);

    /**
     * 卸载桌面磁盘
     *
     * @param desktopId 桌面ID
     * @param diskId    磁盘ID
     * @throws BusinessException 业务异常
     */
    void deactivateDisk(UUID desktopId, UUID diskId) throws BusinessException;

    /**
     * 构造失败的磁盘返回消息(找不到云桌面关联磁盘)
     *
     * @param deskId 云桌面ID
     * @param code   错误码
     * @return 返回消息
     */
    GuesttoolMessageContent buildFailMessage(UUID deskId, Integer code);

    /**
     * 为添加磁盘信息增加相关用户信息
     *
     * @param attachDiskMessageDTO 添加磁盘信息
     * @param userId               用户ID
     */
    void setUserInfoForAttachDiskMessage(AttachDiskMessageDTO attachDiskMessageDTO, @Nullable UUID userId);

    /**
     * 根据用户查询关联的磁盘池信息
     *
     * @param adGroupId adGroupId
     * @param request          分页查询请求
     * @return 用户关联磁盘池信息
     */
    DefaultPageResponse<UserDiskPoolEntity> pageQueryAdGroupDiskPool(UUID adGroupId, PageSearchRequest request);
}
