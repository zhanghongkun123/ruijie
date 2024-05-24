package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import java.util.List;
import java.util.UUID;

/**
 * 派生应用主机云桌面管理API接口
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月11日
 *
 * @author liuwc
 */
public interface RcaHostDesktopMgmtAPI {

    /**
     * * 分页查询云桌面
     *
     * @param request 页面请求
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<RcaHostDesktopDTO> pageQuery(PageSearchRequest request) throws BusinessException;

    /**
     * 计算云桌面名称最大后缀
     *
     * @param appPoolId 应用池ID
     * @return 最大后缀数字
     */
    int getMaxIndexNumWhenAddDesktop(UUID appPoolId);


    /**
     * 根据主机列表查询主机桌面详情
     *
     * @param hostIdList 主机列表
     * @return 主机桌面详情列表
     * @throws BusinessException 业务异常
     */
    List<RcaHostDesktopDTO> listByHostIdIn(List<UUID> hostIdList) throws BusinessException;
}
