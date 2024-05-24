package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserGroupOperNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperSyncNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserLoginNoticeDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月31日
 *
 * @author jarman
 */
public interface UserEventNotifyAPI {


    /**
     * 域用户被禁用
     *
     * @param userIdList 用户id列表
     */
    void domainUserDisabled(@NotNull List<UUID> userIdList);


    /**
     * 域用户权限表更
     *
     * @param userId 用户id
     */
    void domainUserAuthorityChanged(@NotNull UUID userId);


    /**
     * 域用户同步成功执行
     *
     * @param userId userId
     * @throws BusinessException 异常
     */
    void domainUserSyncFinish(@NotNull UUID userId) throws BusinessException;


    /**
     * CMS对接，增量同步用户给CMS（增、删、改）
     *
     * @param userOperNotifyDTO 参数
     */
    void userChanged(@NotNull UserOperNotifyDTO userOperNotifyDTO);

    /**
     * 户组织架构发生变更（增、删、改、全量同步）
     *
     * @param userGroupOperNotifyDTO 参数
     */
    void userGroupChanged(@NotNull UserGroupOperNotifyDTO userGroupOperNotifyDTO);

    /**
     * 用户操作成功后联动增删改与用户对应的关系信息
     *
     * @param userOperSyncNotifyDTO 入参
     */
    void syncNotifyUserChanged(@NotNull UserOperSyncNotifyDTO userOperSyncNotifyDTO);


    /**
     * 用户登成功事件
     *
     * @param userLoginNotifyDTO 参数
     * @return DtoResponse 结果数据
     */
    DtoResponse userLoginSuccess(@NotNull UserLoginNoticeDTO userLoginNotifyDTO);


    /**
     * 域用户组权限变更通知
     *
     * @param groupId 用户组id
     * @param adUserAuthority 权限
     */
    void notifyUserGroupAuthorityChanged(@NotNull UUID groupId, @NotNull IacAdUserAuthorityEnum adUserAuthority);

    /**
     * 域用户权限变更通知
     *
     * @param userId 用户id
     * @param adUserAuthority 权限
     */
    void notifyUserAuthorityChanged(@NotNull UUID userId, @NotNull IacAdUserAuthorityEnum adUserAuthority);

}
