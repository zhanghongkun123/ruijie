package com.ruijie.rcos.rcdc.rco.module.openapi.rest.notify;

import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserGroupOperNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperSyncNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserLoginNoticeDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.notify.dto.UserIdListDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;

/**
 * Description: 接收来自身份中心用户相关事件通知(后面mq支持持久化后，考虑切换成mq)
 * <p>
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024-01-31
 *
 * @author jarman
 */
@OpenAPI
@Path("/v1/user/eventNotify")
public interface UserEventNotifyServer {

    /**
     * 域用户被禁用
     * 
     * @param userIdListDTO 用户id列表
     */
    @POST
    @Path("/domainUserDisabled")
    void domainUserDisabled(@NotNull UserIdListDTO userIdListDTO);


    /**
     * 域用户权限表更
     * 
     * @param userId 用户id
     */
    @POST
    @Path("/domainUserAuthorityChanged")
    @Deprecated
    void domainUserAuthorityChanged(@NotNull UUID userId);


    /**
     * 域用户同步成功执行
     *
     * @param userId userId
     * @throws BusinessException 异常
     */
    @POST
    @Path("/domainUserSyncFinish")
    void domainUserSyncFinish(@NotNull UUID userId) throws BusinessException;


    /**
     * CMS对接，增量同步用户给CMS（增、删、改）
     *
     * @param userOperNotifyDTO 参数
     */
    @POST
    @Path("/userChanged")
    void userChanged(@NotNull UserOperNotifyDTO userOperNotifyDTO);

    /**
     * 户组织架构发生变更（增、删、改、全量同步）
     * 
     * @param userGroupOperNotifyDTO 参数
     */
    @POST
    @Path("/userGroupChanged")
    void userGroupChanged(@NotNull UserGroupOperNotifyDTO userGroupOperNotifyDTO);

    /**
     * 用户操作成功后联动增删改与用户对应的关系信息
     *
     * @param userOperSyncNotifyDTO 入参
     */
    @POST
    @Path("/syncNotifyUserChanged")
    void syncNotifyUserChanged(@NotNull UserOperSyncNotifyDTO userOperSyncNotifyDTO);


    /**
     * 用户登成功事件
     * 
     * @Deprecated 从代码看，用户组件未调用该接口，但不排除其他版本可能有用到，这里先完整保留
     * @param userLoginNotifyDTO 参数
     * @return DtoResponse 返回结果
     */
    @POST
    @Path("/userLoginSuccess")
    @Deprecated
    DtoResponse userLoginSuccess(@NotNull UserLoginNoticeDTO userLoginNotifyDTO);
}
