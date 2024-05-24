package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.UUID;

/**
 * Description: 异步线程启动云桌面
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/13 10:58
 *
 * @author lyb
 */
public class AsyncStartDesktopThread extends AbstractAsyncOperateDesktopThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncStartDesktopThread.class);

    private UserDiskMgmtAPI userDiskMgmtAPI;

    /**
     * 支持跨CPU厂商启动（异构场景, 仅唤醒桌面需要）
     */
    private Boolean supportCrossCpuVendor = false;

    public AsyncStartDesktopThread(UUID deskId, AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                   UserDesktopOperateAPI cloudDesktopOperateAPI) throws BusinessException {
        super(deskId, action, openApiTaskInfoAPI, cloudDesktopOperateAPI);
    }

    public void setUserDiskMgmtAPI(UserDiskMgmtAPI userDiskMgmtAPI) {
        this.userDiskMgmtAPI = userDiskMgmtAPI;
    }

    @Override
    public void run() {
        try {
            CloudDesktopStartRequest request = new CloudDesktopStartRequest(deskId, customTaskId, CbbCloudDeskState.RUNNING);
            request.setSupportCrossCpuVendor(this.supportCrossCpuVendor);
            cloudDesktopOperateAPI.start(request);
            saveTaskSuccess();
            LOGGER.info("openapi启动云桌面正常，deskId:{},customTaskId:{}", deskId, customTaskId);
        } catch (BusinessException e) {
            LOGGER.error("AsyncStartDesktopThread error!", e);
            if (com.ruijie.rcos.rcdc.hciadapter.module.def.BusinessKey.
                    RCDC_HCIADAPTER_VM_WAKE_ERROR_BY_RESOURCE_INSUFFICIENTLY.equals(e.getKey())) {
                saveTaskException(new BusinessException(BusinessKey.RCDC_RCO_VM_WAKE_ERROR_BY_RESOURCE_INSUFFICIENTLY, e));
            } else {
                saveTaskException(e);
            }
            userDiskMgmtAPI.unbindUserAndDesktopAndDiskRelation(deskId);
        } catch (Exception e) {
            LOGGER.error("AsyncStartDesktopThread error!", e);
            saveTaskUnknownException(e);
            userDiskMgmtAPI.unbindUserAndDesktopAndDiskRelation(deskId);
        }
    }

    public UserDiskMgmtAPI getUserDiskMgmtAPI() {
        return userDiskMgmtAPI;
    }

    public Boolean getSupportCrossCpuVendor() {
        return supportCrossCpuVendor;
    }

    public void setSupportCrossCpuVendor(Boolean supportCrossCpuVendor) {
        this.supportCrossCpuVendor = supportCrossCpuVendor;
    }
}
