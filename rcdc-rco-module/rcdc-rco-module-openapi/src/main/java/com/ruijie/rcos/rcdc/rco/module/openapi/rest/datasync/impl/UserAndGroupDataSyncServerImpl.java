package com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserAndGroupDataSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DataSyncResult;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.UserAndGroupDataSyncServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.request.PageReq;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.response.PageResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.response.RcdcUserGroupSyncDataResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.response.RcdcUserSyncDataResponse;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 12:16
 *
 * @author coderLee23
 */
@Service
public class UserAndGroupDataSyncServerImpl implements UserAndGroupDataSyncServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAndGroupDataSyncServerImpl.class);

    @Autowired
    private UserAndGroupDataSyncAPI userAndGroupDataSyncAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    private static final List<IacUserTypeEnum> USER_TYPE_LIST =
            Arrays.asList(IacUserTypeEnum.NORMAL, IacUserTypeEnum.AD, IacUserTypeEnum.LDAP, IacUserTypeEnum.THIRD_PARTY);


    @Override
    public RcdcUserGroupSyncDataResponse listUserGroupData() {

        RccmServerConfigDTO rccmServerConfig = rccmManageAPI.getRccmServerConfig();
        Assert.notNull(rccmServerConfig, "rccmServerConfig must not be null");
        UUID clusterId = rccmServerConfig.getClusterId();
        List<UserGroupSyncDataDTO> userGroupSyncDataList = userAndGroupDataSyncAPI.listUserGroupSyncData();

        RcdcUserGroupSyncDataResponse rcdcUserGroupSyncDataResponse = new RcdcUserGroupSyncDataResponse();
        rcdcUserGroupSyncDataResponse.setClusterId(clusterId);
        rcdcUserGroupSyncDataResponse.setUserGroupSyncDataList(userGroupSyncDataList);

        return rcdcUserGroupSyncDataResponse;
    }

    @Override
    public RcdcUserSyncDataResponse pageUserData(PageReq pageReq) {
        Assert.notNull(pageReq, "pageReq must not be null");

        RccmServerConfigDTO rccmServerConfig = rccmManageAPI.getRccmServerConfig();
        Assert.notNull(rccmServerConfig, "rccmServerConfig must not be null");
        UUID clusterId = rccmServerConfig.getClusterId();

        Pageable pageable = PageRequest.of(pageReq.getPage(), pageReq.getSize());
        // 不同步访客用户
        Page<UserSyncDataDTO> userSyncDataPage = userAndGroupDataSyncAPI.pageUserSyncData(USER_TYPE_LIST, pageable);

        RcdcUserSyncDataResponse rcdcUserSyncDataResponse = new RcdcUserSyncDataResponse();
        rcdcUserSyncDataResponse.setClusterId(clusterId);

        PageResponse<UserSyncDataDTO> pageResponse = new PageResponse<>();
        pageResponse.setContentList(userSyncDataPage.getContent());
        pageResponse.setTotal(userSyncDataPage.getTotalElements());
        pageResponse.setHasNext(userSyncDataPage.hasNext());

        rcdcUserSyncDataResponse.setUserSyncDataPage(pageResponse);

        return rcdcUserSyncDataResponse;
    }

    @Override
    public DataSyncResult syncUserGroupData(List<JSONObject> userGroupSyncDataList) {
        Assert.notEmpty(userGroupSyncDataList, "userGroupSyncDataList must not be empty");
        return userAndGroupDataSyncAPI.syncUserGroupData(userGroupSyncDataList);
    }

    @Override
    public DataSyncResult syncUserData(List<JSONObject> userSyncDataJsonList) {
        Assert.notEmpty(userSyncDataJsonList, "userSyncDataJsonList must not be empty");
        return userAndGroupDataSyncAPI.syncUserData(userSyncDataJsonList);
    }

    @Override
    public DataSyncResult activeSyncUserData(JSONObject userSyncData) {
        Assert.notNull(userSyncData, "userSyncData must not be null");
        return userAndGroupDataSyncAPI.activeSyncUserData(userSyncData);
    }

    @Override
    public DataSyncResult activeSyncUserGroupData(JSONObject userGroupSyncData) {
        Assert.notNull(userGroupSyncData, "userGroupSyncData must not be null");
        return userAndGroupDataSyncAPI.activeSyncUserGroupData(userGroupSyncData);
    }

}
