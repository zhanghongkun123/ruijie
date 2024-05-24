package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DataSyncResult;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserSyncDataDTO;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 10:55
 *
 * @author coderLee23
 */
public interface UserAndGroupDataSyncAPI {

    /**
     * 获取所有用户组同步数据
     * 
     * @return List<UserGroupSyncDataDTO>
     */
    List<UserGroupSyncDataDTO> listUserGroupSyncData();

    /**
     * 分页获取用户同步数据
     * 
     * @param pageable 分页
     * @param userTypeList 同步的用户类型列表
     * @return Page<UserSyncDataDTO>
     */
    Page<UserSyncDataDTO> pageUserSyncData(List<IacUserTypeEnum> userTypeList, Pageable pageable);

    /**
     * 同步用户组数据
     *
     * @param userGroupSyncDataList 用户组全量数据
     * @return DataSyncResult
     */
    DataSyncResult syncUserGroupData(List<JSONObject> userGroupSyncDataList);

    /**
     * 同步用户数据
     *
     * @param userSyncDataJsonList 用户全量数据
     * @return DataSyncResult
     */
    DataSyncResult syncUserData(List<JSONObject> userSyncDataJsonList);

    /**
     * rcdc主动同步用户数据
     *
     * @param userSyncData 同步数据对象
     * @return DataSyncResult
     */
    DataSyncResult activeSyncUserData(JSONObject userSyncData);

    /**
     * rcdc主动同步用户组数据
     *
     * @param userGroupSyncData 同步数据对象
     * @return DataSyncResult
     */
    DataSyncResult activeSyncUserGroupData(JSONObject userGroupSyncData);

}
