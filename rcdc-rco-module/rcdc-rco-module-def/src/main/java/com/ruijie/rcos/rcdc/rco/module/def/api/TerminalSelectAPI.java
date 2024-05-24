package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ProductDriverDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.TerminalSelectPageSearchRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 终端查询服务
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/4
 *
 * @author songxiang
 */
public interface TerminalSelectAPI {

    /**
     * 根据镜像ID获取标记好的驱动产品型号列表,并标记是否安装
     * 
     * @param imageId 镜像ID
     * @return 产品驱动列表:
     * @throws BusinessException 业务异常
     */
    ProductDriverDTO[] listSortedTerminalModel(UUID imageId) throws BusinessException;

    /**
     * 根据镜像ID以及产品型号,并标记是否安装
     *
     * @param imageId 镜像ID
     * @param productModel 镜像ID
     * @return 产品驱动列表:
     * @throws BusinessException 业务异常
     */
    ProductDriverDTO[] listSortedHardwareVersion(UUID imageId, String productModel) throws BusinessException;

    /**
     * 获取满足驱动型号的终端列表，并标记优先级
     * @param request 分页请求
     * @return 所有在线的终端列表
     */
    DefaultPageResponse<TerminalDTO> listSelectableTerminal(TerminalSelectPageSearchRequest request) ;
}
