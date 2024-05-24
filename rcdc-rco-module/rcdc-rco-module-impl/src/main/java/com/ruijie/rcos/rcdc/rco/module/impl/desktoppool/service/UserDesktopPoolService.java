package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 用户桌面池service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/08/11
 *
 * @author linke
 */
public interface UserDesktopPoolService {

    /**
     * 根据用户查询关联的桌面池列表
     *
     * @param cbbUserDetailDTO  cbbUserDetailDTO
     * @param request PageSearchRequest
     * @return DefaultPageResponse<DesktopPoolDTO>
     */
    DefaultPageResponse<DesktopPoolDTO> pageQueryUserDesktopPool(IacUserDetailDTO cbbUserDetailDTO, PageSearchRequest request);

    /**
     * 根据AD域组查询关联的桌面池列表
     *
     * @param adGroupId  adGroupId
     * @param request PageSearchRequest
     * @return DefaultPageResponse<DesktopPoolDTO>
     */
    DefaultPageResponse<DesktopPoolDTO> pageQueryAdGroupDesktopPool(UUID adGroupId, PageSearchRequest request);
}
