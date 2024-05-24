package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rca.module.def.api.request.RcaCreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IDVCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateThirdPartyDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.CreatePoolDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.stereotype.Service;

/**
 * Description: 创建桌面接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/16
 *
 * @author Jarman
 */
@Service
public interface CreateDesktopService {

    /**
     ** 创建云桌面
     *
     * @param request 新增云桌面 request
     * @throws BusinessException 业务异常
     * @return entity
     */
    UserDesktopEntity create(CreateCloudDesktopRequest request) throws BusinessException;

    /**
     * 创建桌面池下云桌面（不含用户信息）
     * @param request 桌面池创建云桌面request
     * @return entity
     * @throws BusinessException 业务异常
     */
    UserDesktopEntity createPoolDesktop(CreatePoolDesktopRequest request) throws BusinessException;

    /**
     * 创建桌派生云主机的云桌面
     * @param rcaCreateCloudDesktopRequest rca应用主机桌面创建参数
     * @return entity
     * @throws BusinessException 业务异常
     */
    UserDesktopEntity createRcaHostDesktop(RcaCreateCloudDesktopRequest rcaCreateCloudDesktopRequest) throws BusinessException;

    /**
     * 创建IDV云桌面
     * @param idvCloudDesktopDTO idvCloudDesktopDTO
     * @throws BusinessException 业务异常
     * @return entity
     */
    UserDesktopEntity createIDV(IDVCloudDesktopDTO idvCloudDesktopDTO) throws BusinessException;

    /**
     * 创建VOI云桌面
     * @param idvCloudDesktopDTO idvCloudDesktopDTO
     * @throws BusinessException 业务异常
     * @return entity
     */
    UserDesktopEntity createVOI(IDVCloudDesktopDTO idvCloudDesktopDTO) throws BusinessException;

    /**
     * 创建第三方云桌面
     * @param request 请求实体
     * @throws BusinessException 业务异常
     */
    void createThirdParty(CreateThirdPartyDesktopRequest request) throws BusinessException;
}

