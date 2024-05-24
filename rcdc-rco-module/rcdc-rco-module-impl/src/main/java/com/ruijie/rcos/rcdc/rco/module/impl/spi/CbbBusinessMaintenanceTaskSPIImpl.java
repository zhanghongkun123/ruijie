package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.maintenance.module.def.dto.CbbTaskDTO;
import com.ruijie.rcos.rcdc.maintenance.module.def.spi.CbbBusinessMaintenanceTaskSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoMaintainStopTaskHandler;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * 
 * Description: CbbBusinessMaintenanceTaskSPIImpl
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14
 *
 * @author zhiweiHong
 */
public class CbbBusinessMaintenanceTaskSPIImpl implements CbbBusinessMaintenanceTaskSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbBusinessMaintenanceTaskSPIImpl.class);

    @Autowired
    private List<RcoMaintainStopTaskHandler> taskHandlerList;

    @Override
    public List<CbbTaskDTO> getTaskList() {
        return Lists.newArrayList();
    }

    @Override
    public boolean stopTask(CbbTaskDTO task) throws BusinessException {
        Assert.notNull(task, "task can not be null");
        Optional<RcoMaintainStopTaskHandler> handlerOptional = taskHandlerList.stream().filter(handler -> handler.isSupport(task)).findFirst();
        if (handlerOptional.isPresent()) {
            handlerOptional.get().handle(task);
            return true;
        }

        LOGGER.warn("未找到处理类[{}]", JSON.toJSONString(task));
        return false;
    }
}
