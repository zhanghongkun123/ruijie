package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.EditCommonConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.BigScreenDetailResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;


/**
 * 大屏配置管理API
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月9日
 *
 * @author bairuqiang
 */
public interface CommonConfigMgmtAPI {

    /**
     * 获取大屏配置参数
     *
     * @param request 空白请求
     * @return 配置参数数组
     * @throws BusinessException 业务异常
     */
    
    BigScreenDetailResponse getConfigParam(DefaultRequest request) throws BusinessException;

    /**
     * 修改大屏配置
     *
     * @param request 需要修改的配置项（数组）
     * @return 修改结果
     * @throws BusinessException 业务异常
     */
    
    DefaultResponse editConfigParam(EditCommonConfigRequest request) throws BusinessException;
}