package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbEstConfigDTO;

/**
 * Description: EST配置API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/17 15:52
 *
 * @author yxq
 */
public interface EstConfigAPI {

    /**
     * 获取EST广域网配置
     *
     * @return EST配置
     */
    CbbEstConfigDTO getEstWanConfig();


    /**
     * 获取EST广域网配置
     *
     * @return EST配置
     */
    CbbEstConfigDTO getEstLanConfig();

    /**
     * 编辑EST广域网配置
     *
     * @param estConfigDTO EST配置
     */
    void editEstWanConfig(CbbEstConfigDTO estConfigDTO);

    /**
     * 编辑EST广域网配置
     *
     * @param estConfigDTO EST配置
     */
    void editEstLanConfig(CbbEstConfigDTO estConfigDTO);

    /**
     * 重置EST局域网配置
     */
    void resetEstLanConfig();

    /**
     * 重置EST广域网配置
     */
    void resetEstWanConfig();
}
