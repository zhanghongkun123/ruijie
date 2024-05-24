package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDeskSnapshotChangeSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api.DeskSnapshotActionTcpApi;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.est.EstCommonActionNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.EstSubActionCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionSubResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbSessionManagerAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.tcp.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.common.dto.CommonConstants.PROTOCOL_VERSION;


/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/8 10:45
 *
 * @author zhangsiming
 */
public class CbbDeskSnapshotChangeSPIImpl implements CbbDeskSnapshotChangeSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbDeskSnapshotChangeSPIImpl.class);

    @Autowired
    private EstCommonActionNotifyService estCommonActionNotifyService;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private CbbSessionManagerAPI cbbSessionManagerAPI;

    @Autowired
    private DeskSnapshotActionTcpApi deskSnapshotActionTcpApi;


    @Override
    public void onDeskSnapshotChange(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");
        //找到终端
        UserDesktopEntity userDesktopEntity = userDesktopService.findByDeskId(deskId);
        if (userDesktopEntity != null && userDesktopEntity.getHasTerminalRunning()) {
            try {
                String terminalId = userDesktopEntity.getTerminalId();
                Session session = cbbSessionManagerAPI.getSessionByAlias(terminalId);
                if (session != null && session.getAttribute(PROTOCOL_VERSION) != null) {
                    LOGGER.info("桌面[{}]下发桌面列表变更消息", deskId);
                    deskSnapshotActionTcpApi.snapshotRefreshList(terminalId,
                            generateRefreshSuccessResult(EstSubActionCode.EST_SNAPSHOT_REFRESH_LIST, deskId));
                } else {
                    //这里暂时统一使用创建快照的code
                    estCommonActionNotifyService.responseToEst(userDesktopEntity.getTerminalId(),
                            EstSubActionCode.EST_SNAPSHOT_REFRESH_LIST,
                            generateSuccessResult(EstSubActionCode.EST_SNAPSHOT_REFRESH_LIST, deskId));
                }
            } catch (BusinessException e) {
                LOGGER.error("桌面[{}]快照变化通知异常：{}", deskId, e.getMessage(), e);
            }
        }
    }

    private String generateSuccessResult(String subAction, UUID deskId) {
        EstCommonActionResponse<String> response = new EstCommonActionResponse<>();
        response.setSubAction(subAction);
        response.setData(EstCommonActionSubResponse.success());
        response.setDeskId(deskId);
        return JSON.toJSONString(response, SerializerFeature.WriteDateUseDateFormat);
    }

    private EstCommonActionResponse<String> generateRefreshSuccessResult(String subAction, UUID deskId) {
        EstCommonActionResponse<String> response = new EstCommonActionResponse<>();
        response.setSubAction(subAction);
        response.setData(EstCommonActionSubResponse.success());
        response.setDeskId(deskId);
        return response;
    }
}
