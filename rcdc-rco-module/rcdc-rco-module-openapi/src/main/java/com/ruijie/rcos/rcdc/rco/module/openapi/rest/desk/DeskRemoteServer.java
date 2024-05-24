package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AsyncTaskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.StartDeskRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import org.springframework.lang.Nullable;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.UUID;

/**
 * Description: 新版远程桌面服务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-17 15:38:00
 *
 * @author zjy
 */
@OpenAPI
@Path("/v2/desk")
public interface DeskRemoteServer {

    /**
     * 启动桌面
     *
     * @param deskId 桌面id
     * @param request 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 13:52
     * @Author zjy
     **/
    @PUT
    @Path("/{desk_id}/start")
    AsyncTaskResponse start(@PathParam("desk_id") @NotNull UUID deskId, @Nullable StartDeskRequest request) throws BusinessException;

    /**
     * 关闭桌面
     *
     * @param deskId 桌面id
     * @param force 是否强制
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 13:53
     * @Author zjy
     **/
    @PUT
    @Path("/{desk_id}/shutdown/{force}")
    AsyncTaskResponse shutdown(@PathParam("desk_id") @NotNull UUID deskId, @PathParam("force") @NotNull Boolean force) throws BusinessException;

    /**
     * 重启桌面
     *
     * @param deskId 桌面id
     * @Date 2022/1/18 13:54
     * @Author zjy
     * @throws BusinessException 业务异常
     **/
    @PUT
    @Path("/{desk_id}/restart")
    void restart(@PathParam("desk_id") @NotNull UUID deskId) throws BusinessException;


}
