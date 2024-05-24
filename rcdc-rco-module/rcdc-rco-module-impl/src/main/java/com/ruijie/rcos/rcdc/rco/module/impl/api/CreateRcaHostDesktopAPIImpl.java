package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAppAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaPoolAppAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.RcaCreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.CreateRcaHostDesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CreateDesktopService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 创建派生云主机桌面信息
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月17日
 *
 * @author liuwc
 */
public class CreateRcaHostDesktopAPIImpl implements CreateRcaHostDesktopAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateRcaHostDesktopAPIImpl.class);

    @Autowired
    private CreateDesktopService createDesktopService;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private RcaHostAppAPI rcaHostAppAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcaPoolAppAPI rcaPoolAppAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Override
    public void create(UUID desktopId, RcaCreateCloudDesktopRequest rcaCreateCloudDesktopRequest) throws BusinessException {
        Assert.notNull(desktopId, "desktopId not be null");
        Assert.notNull(rcaCreateCloudDesktopRequest, "rcaCreateCloudDesktopRequest not be null");

        rcaCreateCloudDesktopRequest.setDesktopId(desktopId);
        LOGGER.info("收到创建派生应用主机的消息={}", JSONObject.toJSONString(rcaCreateCloudDesktopRequest));
        createDesktopService.createRcaHostDesktop(rcaCreateCloudDesktopRequest);
        CbbDeskDTO deskVDI = cbbVDIDeskMgmtAPI.getDeskVDI(desktopId);
        rcaCreateCloudDesktopRequest.setMac(deskVDI.getDeskMac());
        rcaHostAPI.insertVdiHost(rcaCreateCloudDesktopRequest);

        // 需要从镜像模板的主机复制一份应用
        rcaHostAppAPI.copyAppFromImageHost(rcaCreateCloudDesktopRequest.getDesktopImageId(),
                desktopId, rcaCreateCloudDesktopRequest.getPoolId());
    }
}
