package com.ruijie.rcos.rcdc.rco.module.impl.rca.service;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserRcaGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import java.util.UUID;

/**
 * Description: 用户应用分组service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/08/11
 *
 * @author zhengjingyong
 */
public interface UserRcaGroupService {

    /**
     * 根据用户查询关联的应用分组列表
     *
     * @param cbbUserDetailDTO  cbbUserDetailDTO
     * @param request PageSearchRequest
     * @return DefaultPageResponse<UserRcaGroupDTO>
     */
    DefaultPageResponse<UserRcaGroupDTO> pageQueryUserRcaGroup(IacUserDetailDTO cbbUserDetailDTO, PageSearchRequest request);

    /**
     * 根据AD域组查询关联的应用分组列表
     *
     * @param adGroupId  adGroupId
     * @param request PageSearchRequest
     * @return DefaultPageResponse<UserRcaGroupDTO>
     */
    DefaultPageResponse<UserRcaGroupDTO> pageQueryAdGroupRcaGroup(UUID adGroupId, PageSearchRequest request);
}
