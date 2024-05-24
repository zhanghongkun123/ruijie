package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AsyncTaskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.CreateDeskStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.UpdateDeskStrategyRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.lang.Nullable;

import javax.ws.rs.*;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
@OpenAPI
@Path("/v1/desk_strategy")
public interface DeskStrategyServer {

    /**
     * 桌面策略分页查询
     * @param pageQueryRequest request
     * @return 分页信息
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("")
    @ApiAction("pagequery")
    PageQueryResponse pageQuery(PageQueryServerRequest pageQueryRequest) throws BusinessException;

    /**
     * 云桌面策略创建
     * @param request 创建参数
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/create")
    void createDeskStrategy(CreateDeskStrategyRequest request) throws BusinessException;

    /**
     * 云桌面策略修改
     * @param request 修改参数
     * @return AsyncTaskResponse 
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("/edit")
    AsyncTaskResponse updateDeskStrategy(UpdateDeskStrategyRequest request) throws BusinessException;

    /**
     * 删除云桌面策略
     * @param deskStrategyId 云桌面策略ID
     * @param manageDataRequest 统一管理信息
     * @throws BusinessException 业务异常
     */
    @DELETE
    @Path("/delete/{desk_strategy_id}")
    void deleteDeskStrategy(@PathParam("desk_strategy_id") @NotNull UUID deskStrategyId, 
                            @Nullable UnifiedManageDataRequest manageDataRequest) throws BusinessException;

    /**
     * 云桌面策略同步
     * @param request 同步参数
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("/sync")
    void syncDeskStrategy(UpdateDeskStrategyRequest request) throws BusinessException;
}
