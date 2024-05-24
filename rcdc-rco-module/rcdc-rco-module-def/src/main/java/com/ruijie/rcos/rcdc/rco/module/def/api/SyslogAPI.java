package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SyslogScheduleConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SyslogScheduleDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: syslog API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 10:26
 *
 * @author yxq
 */
public interface SyslogAPI {

    /**
     * 获得syslog定时任务配置前台展示详情
     *
     * @return 响应
     * @throws BusinessException 业务异常
     */
    SyslogScheduleDTO findSyslogScheduleDetail();

    /**
     * 修改syslog定时发送周期、周期间隔配置
     *
     * @param syslogScheduleDTO syslog定时发送周期、周期间隔配置
     * @throws BusinessException 业务异常
     */
    void editSyslogSchedule(SyslogScheduleDTO syslogScheduleDTO) throws BusinessException;

    /**
     * 获取syslog定时任务发送数据库中的配置信息（后台获取配置用这个接口）
     * 
     * @return syslog定时任务发送数据库中的配置信息
     */
    SyslogScheduleConfigDTO getSyslogScheduleConfig();
}
