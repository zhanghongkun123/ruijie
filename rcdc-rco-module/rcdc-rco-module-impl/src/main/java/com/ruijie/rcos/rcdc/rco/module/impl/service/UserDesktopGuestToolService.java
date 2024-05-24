package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.dto.UserDesktopGuestToolDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 云桌面GT信息
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年8月30日
 *
 * @author linrenjian
 */

public interface UserDesktopGuestToolService {


    /**
     * 分页查询
     *
     * @param uuidArr 集合
     * @return PageQueryResponse<CbbDesktopTempPermissionDTO>
     * @throws BusinessException 业务异常
     */
    List<UserDesktopGuestToolDTO> findAllOnlineVDIDeskByInDeskId(UUID[] uuidArr) throws BusinessException;

    /**
     * 查询全部的VDI 在线桌面
     *
     * @return PageQueryResponse<CbbDesktopTempPermissionDTO>
     * @throws BusinessException 业务异常
     */
    List<UserDesktopGuestToolDTO> findAllOnlineVDIDesk() throws BusinessException;
}
