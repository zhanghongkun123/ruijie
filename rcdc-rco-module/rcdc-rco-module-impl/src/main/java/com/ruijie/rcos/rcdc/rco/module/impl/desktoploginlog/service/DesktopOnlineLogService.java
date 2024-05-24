package com.ruijie.rcos.rcdc.rco.module.impl.desktoploginlog.service;

import com.ruijie.rcos.rcdc.rco.module.impl.desktoploginlog.dto.DesktopOnlineLogDTO;

import java.util.Date;

/**
 * Description: 云桌面登录登出记录日志Service
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/20 11:06
 *
 * @author linke
 */
public interface DesktopOnlineLogService {

    /**
     * 保存登录登出日志
     *
     * @param desktopOnlineLogDTO 登录登出日志实例
     */
    void saveDesktopOnlineLog(DesktopOnlineLogDTO desktopOnlineLogDTO);

    /**
     ** 查询超期的操作日志条数
     *
     * @param overdueTime 日志时间
     * @return  数量
     */
    Integer countByOperationTimeLessThan(Date overdueTime);

    /**
     ** 删除超期的操作日志条数
     * @param overdueTime 日志时间
     */
    void deleteByOperationTimeLessThan(Date overdueTime);

    /**
     ** 查询当前在线使用的云桌面数量
     *
     * @return  数量
     */
    Long countCurrentOnlineDesktop();
}
