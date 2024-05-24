package com.ruijie.rcos.rcdc.rco.module.impl.desktoploginlog.service.impl;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopOnlineLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoploginlog.dto.DesktopOnlineLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoploginlog.service.DesktopOnlineLogService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopOnlineLogEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description: 云桌面登录登出记录日志Service实现类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/20 11:06
 *
 * @author linke
 */
@Service
public class DesktopOnlineLogServicImpl implements DesktopOnlineLogService {

    @Autowired
    private DesktopOnlineLogDAO desktopOnlineLogDAO;

    @Override
    public void saveDesktopOnlineLog(DesktopOnlineLogDTO desktopOnlineLogDTO) {
        Assert.notNull(desktopOnlineLogDTO, "desktopOnlineLogDTO must not be null");

        DesktopOnlineLogEntity onlineLogEntity = new DesktopOnlineLogEntity();
        BeanUtils.copyProperties(desktopOnlineLogDTO, onlineLogEntity);

        desktopOnlineLogDAO.save(onlineLogEntity);
    }

    @Override
    public Integer countByOperationTimeLessThan(Date overdueTime) {
        Assert.notNull(overdueTime, "overdueTime must not be null");
        return desktopOnlineLogDAO.countByOperationTimeLessThan(overdueTime);
    }

    @Override
    public void deleteByOperationTimeLessThan(Date overdueTime) {
        Assert.notNull(overdueTime, "overdueTime must not be null");
        desktopOnlineLogDAO.deleteByOperationTimeLessThan(overdueTime);
    }

    @Override
    public Long countCurrentOnlineDesktop() {
        return desktopOnlineLogDAO.countCurrentOnlineDesktop();
    }
}
