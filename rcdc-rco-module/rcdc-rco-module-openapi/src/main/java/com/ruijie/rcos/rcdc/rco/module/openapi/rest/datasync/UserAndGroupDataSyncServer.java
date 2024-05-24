package com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DataSyncResult;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.request.PageReq;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.response.RcdcUserGroupSyncDataResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.response.RcdcUserSyncDataResponse;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 12:03
 *
 * @author coderLee23
 */
@OpenAPI
@Path("/v1/dataSync")
public interface UserAndGroupDataSyncServer {

    /**
     * 获取用户全量同步数据
     * 
     * @return RcdcUserGroupSyncDataResponse
     */
    @POST
    @Path("/userGroup")
    RcdcUserGroupSyncDataResponse listUserGroupData();

    /**
     * 分页获取用户同步数据
     * 
     * @param pageReq 分页数据
     * @return RcdcUserSyncDataResponse
     */
    @POST
    @Path("/user")
    RcdcUserSyncDataResponse pageUserData(@NotNull PageReq pageReq);

    /**
     * 同步用户组数据
     * 
     * @param userGroupSyncDataList 用户组全量数据
     * @return DataSyncResult
     */
    @POST
    @Path("/syncUserGroup")
    DataSyncResult syncUserGroupData(@NotEmpty List<JSONObject> userGroupSyncDataList);

    /**
     * 同步用户数据
     *
     * @param userSyncDataJsonList 用户全量数据
     * @return DataSyncResult
     */
    @POST
    @Path("/syncUser")
    DataSyncResult syncUserData(@NotNull List<JSONObject> userSyncDataJsonList);

    /**
     * rcdc主动同步用户数据
     *
     * @param userSyncData 同步数据对象
     * @return DataSyncResult
     */
    @POST
    @Path("/activeSyncUser")
    DataSyncResult activeSyncUserData(@NotNull JSONObject userSyncData);

    /**
     * rcdc主动同步用户组数据
     *
     * @param userGroupSyncData 同步数据对象
     * @return DataSyncResult
     */
    @POST
    @Path("/activeSyncUserGroup")
    DataSyncResult activeSyncUserGroupData(@NotNull JSONObject userGroupSyncData);

}
