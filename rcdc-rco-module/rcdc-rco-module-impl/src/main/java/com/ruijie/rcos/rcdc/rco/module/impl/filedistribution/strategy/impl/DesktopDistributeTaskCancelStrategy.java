package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.rcdc.rco.module.impl.RcdcGuestToolCmdKey;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto.DistributeTaskCancelGtMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.strategy.DistributeTaskCancelStrategy;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 对象为云桌面的文件分发任务取消策略
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/13
 *
 * @author zdc
 */
@Service
public class DesktopDistributeTaskCancelStrategy implements DistributeTaskCancelStrategy {

    private final static int SUCCESS = 0;

    @Autowired
    private CbbGuestToolMessageAPI cbbGuestToolMessageAPI;

    @Override
    public FileDistributionTargetType targetType() {
        return FileDistributionTargetType.DESKTOP;
    }

    @Override
    public void doCancel(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException {
        Assert.notNull(subTaskDTO,"subTaskDTO must not be null");
        Assert.notNull(parameterDTO,"parameterDTO must not be null");
        sendCancelToGt(subTaskDTO);
    }

    private void sendCancelToGt(DistributeSubTaskDTO subTaskDTO) throws BusinessException {
        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        UUID targetId = resolveTargetId(subTaskDTO);
        messageDTO.setDeskId(targetId);
        messageDTO.setCmdId(RcdcGuestToolCmdKey.RCDC_GT_CMD_ID_FILE_DISTRIBUTE_TASK_CANCEL);
        messageDTO.setPortId(RcdcGuestToolCmdKey.RCDC_GT_PORT_ID_FILE_DISTRIBUTE_TASK);
        messageDTO.setBody(JSON.toJSONString(buildTaskCancelMessageContent(subTaskDTO)));
        cbbGuestToolMessageAPI.asyncRequest(messageDTO);
    }


    private GuesttoolMessageContent buildTaskCancelMessageContent(DistributeSubTaskDTO subTaskDTO) {
        DistributeTaskCancelGtMessageDTO messageDTO = new DistributeTaskCancelGtMessageDTO();
        messageDTO.setTaskId(subTaskDTO.getId());

        GuesttoolMessageContent content = new GuesttoolMessageContent();
        content.setCode(SUCCESS);
        content.setContent(messageDTO);
        return content;
    }

    private UUID resolveTargetId(DistributeSubTaskDTO subTaskDTO) {
        return UUID.fromString(subTaskDTO.getTargetId());
    }

}
