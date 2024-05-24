package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.RcdcGuestToolCmdKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: GT上报文件分发任务结果
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/24 10:52
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(RcdcGuestToolCmdKey.RCDC_GT_FILE_DISTRIBUTE_TASK_REPORT)
public class DesktopDistributeTaskReportSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopDistributeTaskReportSPIImpl.class);

    private static final String TASK_ID_KEY = "task_id";

    private static final String RESULT_KEY = "result";

    private static final String MESSAGE_KEY = "message";

    private static final String CONTENT_KEY = "content";

    @Autowired
    private FileDistributionTaskManageAPI fileDistributionTaskManageAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        String body = request.getDto().getBody();
        LOGGER.info("收到GT反馈文件分发任务结果[{}]", body);
        try {
            DistributeTaskResultDTO resultDTO = resolveResult(body);
            fileDistributionTaskManageAPI.processSubTaskResult(resultDTO);
        } catch (Exception e) {
            LOGGER.error("文件分发任务结果解析失败", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TASK_REPORT_RESOLVE_FAIL, e);
        }

        return new CbbGuesttoolMessageDTO();
    }

    private DistributeTaskResultDTO resolveResult(String bodyString) {
        JSONObject body = JSON.parseObject(bodyString);
        JSONObject content = body.getJSONObject(CONTENT_KEY);

        String taskIdString = content.getString(TASK_ID_KEY);
        UUID taskId = UUID.fromString(taskIdString);
        String resultString = content.getString(RESULT_KEY);
        DistributeTaskResultDTO.TaskResult result = DistributeTaskResultDTO.TaskResult.valueOf(resultString);
        Assert.notNull(result, "result[" + resultString + "]cannot be resolved!");
        String message = body.getString(MESSAGE_KEY);

        DistributeTaskResultDTO resultDTO = new DistributeTaskResultDTO();
        resultDTO.setTaskId(taskId);
        resultDTO.setResult(result);
        resultDTO.setMessage(message);
        return resultDTO;
    }
}
