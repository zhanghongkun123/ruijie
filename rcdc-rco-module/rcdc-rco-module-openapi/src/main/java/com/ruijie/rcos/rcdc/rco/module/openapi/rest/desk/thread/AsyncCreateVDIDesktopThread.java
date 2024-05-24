package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.CreateVDIDesktopRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.UUID;

/**
 * Description: 异步线程处理创建云桌面请求
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/12 16:43
 *
 * @author xiejian
 */
public class AsyncCreateVDIDesktopThread extends AbstractAsyncDesktopMgmtThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncCreateVDIDesktopThread.class);

    private UUID userId;

    private CreateVDIDesktopRequest request;

    public AsyncCreateVDIDesktopThread(UUID userId, AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                       UserDesktopMgmtAPI userDesktopMgmtAPI, CreateVDIDesktopRequest request) throws BusinessException {
        super(userId, action, openApiTaskInfoAPI, userDesktopMgmtAPI);
        setUserId(userId);
        setRequest(request);
    }

    @Override
    public void run() {
        CreateCloudDesktopRequest createCloudDesktopRequest = new CreateCloudDesktopRequest();
        createCloudDesktopRequest.setUserId(userId);
        createCloudDesktopRequest.setDesktopImageId(request.getImageId());
        createCloudDesktopRequest.setNetworkId(request.getNetworkId());
        createCloudDesktopRequest.setStrategyId(request.getDeskStrategyId());
        createCloudDesktopRequest.setCustomTaskId(customTaskId);
        createCloudDesktopRequest.setClusterId(request.getClusterId());
        createCloudDesktopRequest.setPlatformId(request.getPlatformId());
        try {
            CbbDeskSpecDTO cbbDeskSpecDTO = buildDeskSpec(request);
            createCloudDesktopRequest.setDeskSpec(cbbDeskSpecDTO);
            CreateDesktopResponse response = userDesktopMgmtAPI.create(createCloudDesktopRequest);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.DESK_ID, response.getId());
            jsonObject.put(Constant.DESK_NAME, response.getDesktopName());
            resourceList.add(jsonObject);
            saveTaskSuccess();
        } catch (BusinessException e) {
            LOGGER.error("AsyncCreateVDIDesktop  businessException!", e);
            saveTaskException(e);
        } catch (Exception e) {
            LOGGER.error("AsyncCreateVDIDesktop  exception!", e);
            saveTaskUnknownException(e);
        }
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setRequest(CreateVDIDesktopRequest request) {
        this.request = request;
    }

}
