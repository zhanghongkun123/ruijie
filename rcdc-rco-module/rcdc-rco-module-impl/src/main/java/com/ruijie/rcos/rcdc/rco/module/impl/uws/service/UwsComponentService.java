package com.ruijie.rcos.rcdc.rco.module.impl.uws.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;

/**
 * Description: uws 组件服务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-12-27 16:43:00
 *
 * @author zjy
 */
public interface UwsComponentService {

    /**
     * 初始化cm app
     */
    void initCmApp();

    /**
     * 获取UWS组件启用情况标识
     *
     * @return FindParameterResponse响应
     */
    FindParameterResponse getUwsComponentFlag();

    /**
     * UWS组件的文件是否存在
     * @return 是否存在
     */
    Boolean isExistUwsPackageFile();
}