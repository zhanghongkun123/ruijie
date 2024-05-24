package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDeskOperateNotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.dto.OpenApiTaskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.exception.OpenAPIDesktopBindTerminalException;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.SaveOpenApiTaskInfoRequest;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Description: 接收桌面事件消息处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/8
 *
 * @author Jarman
 */
public class DesktopOperateSPIImpl implements CbbDeskOperateNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopOperateSPIImpl.class);

    @Autowired
    private DesktopOperateHandler desktopOperateHandler;

    @Autowired
    private OpenApiTaskInfoAPI openApiTaskInfoAPI;

    private static final ExecutorService DESK_RCO_EVENT_THREAD =
            ThreadExecutors.newBuilder("rco_event_spi").maxThreadNum(40).queueSize(1).build();

    @Override
    public void afterDeskOperateSuccess(CbbDeskOperateNotifyRequest request) {
        Assert.notNull(request, "request must not be null");
        CbbCloudDeskOperateType operateType = request.getOperateType();
        if (operateType == null) {
            throw new IllegalArgumentException("接收桌面操作事件消息，操作类型不能为null");
        }

        DESK_RCO_EVENT_THREAD.execute(() -> {
            try {
                desktopOperateHandler.execute(request);
            } catch (Exception e) {
                LOGGER.error("接收桌面状态消息事件处理发生异常", e);
                // 仅作为 Openapi 接口启动云桌面接口
                if (e instanceof OpenAPIDesktopBindTerminalException) {
                    List<OpenApiTaskInfoDTO> dtoList = openApiTaskInfoAPI.findByBusinessIdAndTaskState(request.getDeskId(),
                            OpenApiTaskState.RUNNING.name());
                    if (dtoList.isEmpty()) {
                        return;
                    }
                    OpenApiTaskInfoDTO dto = dtoList.get(0);
                    SaveOpenApiTaskInfoRequest openApiTaskInfoRequest = new SaveOpenApiTaskInfoRequest();
                    openApiTaskInfoRequest.setAction(dto.getAction());
                    openApiTaskInfoRequest.setTaskId(dto.getTaskId());
                    openApiTaskInfoRequest.setBusinessId(dto.getBusinessId());
                    openApiTaskInfoRequest.setExceptionName(e.getClass().getName());
                    openApiTaskInfoRequest.setExceptionMessage("云桌面已启动");
                    openApiTaskInfoRequest.setTaskState(OpenApiTaskState.FINISHED.name());
                    openApiTaskInfoAPI.save(openApiTaskInfoRequest);
                }
            }
        });

    }
}
