package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbServerRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PhysicalServerInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;


import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务器管理Service接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月10日
 * @author brq
 */
public interface ServerService {

    /**
     * 获取服务器最新监控信息
     * @param serverRequestDTOList 服务器数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<ServerMonitorInfoDTO> 服务器监控信息
     * @throws BusinessException 业务异常
     */
    List<CbbServerMonitorInfoDTO> getServerMonitorInfo(
            List<CbbServerRequestDTO> serverRequestDTOList, LocalDateTime startTime, LocalDateTime endTime) throws BusinessException;

    /**
     * 获取所有可用物理服务器信息
     * @return 返回
     */
    
    List<PhysicalServerInfoDTO> listAllPhysicalServer();
}
