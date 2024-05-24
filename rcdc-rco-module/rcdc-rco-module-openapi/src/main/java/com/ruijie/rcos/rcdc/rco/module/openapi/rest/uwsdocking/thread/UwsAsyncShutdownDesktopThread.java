package com.ruijie.rcos.rcdc.rco.module.openapi.rest.uwsdocking.thread;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread.AbstractAsyncOperateDesktopThread;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.UUID;

/**
 * Description: uws异步线程关闭云桌面
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/10 10:58
 *
 * @author zjy
 */
public class UwsAsyncShutdownDesktopThread extends AbstractAsyncOperateDesktopThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(UwsAsyncShutdownDesktopThread.class);

    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    private boolean isForce;

    public UwsAsyncShutdownDesktopThread(UUID deskId, AsyncTaskEnum action,
                                         OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                         UserDesktopOperateAPI cloudDesktopOperateAPI,
                                         UserDesktopMgmtAPI cloudDesktopMgmtAPI) throws BusinessException {
        super(deskId, action, openApiTaskInfoAPI, cloudDesktopOperateAPI);
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
    }

    public void setForce(boolean force) {
        isForce = force;
    }

    @Override
    public void run() {
        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(deskId);
            if (CbbCloudDeskType.VDI.name().equalsIgnoreCase(cloudDesktopDetailDTO.getDeskType()) ||
                    CbbCloudDeskType.THIRD.name().equalsIgnoreCase(cloudDesktopDetailDTO.getDeskType())) {
                cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, isForce, customTaskId));
            } else {
                cloudDesktopOperateAPI.shutdownIdv(new CloudDesktopShutdownRequest(deskId, isForce, customTaskId));
            }
            saveTaskSuccess();
            LOGGER.info("uws关闭云桌面正常，deskId:{},customTaskId:{}", deskId, customTaskId);
        } catch (BusinessException e) {
            LOGGER.error("AsyncStartDesktopThread error!", e);
            saveTaskException(e);
        } catch (Exception e) {
            LOGGER.error("AsyncStartDesktopThread error!", e);
            saveTaskUnknownException(e);
        }
    }
}
