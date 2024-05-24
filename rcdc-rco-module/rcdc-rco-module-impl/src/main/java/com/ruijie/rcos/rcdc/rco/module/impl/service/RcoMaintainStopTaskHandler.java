package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.maintenance.module.def.dto.CbbTaskDTO;

/**
 * 
 * Description: RCO任务终止接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/20
 *
 * @author zhiweiHong
 */
public interface RcoMaintainStopTaskHandler {

    /**
     * 是否支持处理任务
     *
     * @param cbbTaskDTO cbbTaskDTO
     * @return true 支持 false 不知此
     */
    boolean isSupport(CbbTaskDTO cbbTaskDTO);

    /**
     * 任务执行接口
     *
     * @param cbbTaskDTO cbbTaskDTO
     */
    void handle(CbbTaskDTO cbbTaskDTO);
}
