package com.ruijie.rcos.rcdc.rco.module.impl.servermodel.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: ServerModelService
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020-09-04
 *
 * @author wjp
 */
public interface ServerModelService {

    /**
     * 查询服务器部署模式
     * 
     * @return 服务器部署模式
     * @throws BusinessException 业务异常
     */
    ServerModelEnum getServerModel() throws BusinessException;

    /**
     * 获取服务器部署模式标识
     * 
     * @return FindParameterResponse响应
     */
    FindParameterResponse getServerModelFlag();

    /**
     * 更新服务器部署模式标识
     * 
     * @param serverModel 服务器部署模式
     */
    void updateServerModelFlag(String serverModel);

    /**
     * 初始化服务器部署模式标识
     */
    void initServerModelFlag();
}
