package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.ShineAction;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbImageUpgradeTerminalNotifySPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 通知镜像更新发布后关联在线终端（还原桌面）进行静默下载
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/6 17:22
 *
 * @author yanlin
 */
public class CbbImageUpgradeTerminalNotifySPIImpl implements CbbImageUpgradeTerminalNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbImageUpgradeTerminalNotifySPIImpl.class);

    @Autowired
    private ViewTerminalDAO viewTerminalDAO;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    /**
     * 发送静默下载通知线程
     */
    private static final ThreadExecutor SEND_IMAGE_UPGRADE_EXECUTOR =
            ThreadExecutors.newBuilder("send-image-upgrade-executor").maxThreadNum(30).queueSize(20).build();

    @Override
    public void notifyImageRefOnlineTerminalUpgrade(UUID imageTemplateId) {
        Assert.notNull(imageTemplateId, "imageTemplateId cannot be null");

        // 查询镜像关联终端
        List<ViewTerminalEntity> terminalEntityList = viewTerminalDAO.findByImageId(imageTemplateId);
        terminalEntityList.forEach(entity -> {
            if ((entity.getPattern() == CbbCloudDeskPattern.RECOVERABLE || entity.getPattern() == CbbCloudDeskPattern.APP_LAYER)
                    && entity.getTerminalState() == CbbTerminalStateEnums.ONLINE) {
                // 通知在线终端更新镜像
                LOGGER.info("通知终端[{}]云桌面镜像更新发布成功，可以进行静默下载", entity.getTerminalId());
                SEND_IMAGE_UPGRADE_EXECUTOR.execute(() -> {
                    sendToShine(entity.getTerminalId(), imageTemplateId);
                });

            }
        });
    }

    private void sendToShine(String terminalId, UUID imageId) {
        CbbShineMessageRequest request = CbbShineMessageRequest.create(ShineAction.IMAGE_TEMPLATE_UPGRADE, terminalId);
        request.setContent(new JSONObject());
        try {
            cbbTranspondMessageHandlerAPI.asyncRequest(request, new CbbTerminalCallback() {
                @Override
                public void success(String terminalId, CbbShineMessageResponse response) {
                    Assert.notNull(terminalId, "terminalId cannot be null!");
                    Assert.notNull(response, "response cannot be null!");

                    LOGGER.info("发生镜像更新，通知终端[{}]成功", terminalId);
                }

                @Override
                public void timeout(String terminalId) {
                    Assert.notNull(terminalId, "terminalId cannot be null!");

                    LOGGER.error("通知终端云桌面镜像发布超时，terminalId[{}], 终端重启后会再次检测镜像更新", terminalId);
                }
            });
        } catch (Exception e) {
            LOGGER.error("通知终端[{}]云桌面镜像发布失败，终端重启后再次检测更新", terminalId, e);
        }
    }
}
