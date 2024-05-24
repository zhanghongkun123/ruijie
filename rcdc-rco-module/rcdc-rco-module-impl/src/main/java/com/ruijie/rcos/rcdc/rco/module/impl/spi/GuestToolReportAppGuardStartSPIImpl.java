package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeSubTaskService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: GT上报启动完成消息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/26 11:00
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_APP_GUARD_START_END)
class GuestToolReportAppGuardStartSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolReportAppGuardStartSPIImpl.class);

    @Autowired
    private DistributeSubTaskService distributeSubTaskService;


    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        CbbGuesttoolMessageDTO cbbGuesttoolMessageDTO = request.getDto();
        UUID deskId = cbbGuesttoolMessageDTO.getDeskId();
        LOGGER.info("app guard在虚机内就绪！，deskId[{}]", deskId);
        // 方案采用轮询机制，会自动识别云桌面运行状态，下发任务，失败会暂存任务，重试，因此此处不在处理
        List<DistributeSubTaskDTO> subTaskDTOList = distributeSubTaskService.findByTargetId(deskId.toString());

        subTaskDTOList.forEach(subTaskDTO -> {
            if (FileDistributionTaskStatus.RUNNING.equals(subTaskDTO.getStatus())) {
                LOGGER.info("GT在虚机内就绪！重置桌面id：{}的子任务：{}", deskId, JSON.toJSON(subTaskDTOList));
                distributeSubTaskService.changeRunningSubTaskToWaiting(subTaskDTO);
            }
        });
        return cbbGuesttoolMessageDTO;
    }
}
