package com.ruijie.rcos.rcdc.rco.module.def.api;

/**
 * Description: ServerModelAPI
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年9月4日
 *
 * @author wjp
 */
public interface ServerModelAPI {

    /**
     * 获取服务器模式
     *
     * @return 响应
     */
    String getServerModel();

    /**
     * 是否是VDI部署模式
     *
     * @return 响应
     */
    boolean isVdiModel();

    /**
     * 是否是IDV部署模式
     *
     * @return 响应
     */
    boolean isIdvModel();

    /**
     * 是否是MINI部署模式
     *
     * @return 响应
     */
    boolean isMiniModel();

    /**
     * 是否还未获取到部署模式
     *
     * @return 响应
     */
    boolean isInitModel();


}
