package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 桌面状态回滚等场景的状态变更通知（不全）
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年4月9日
 *
 * @author zhengjingyong
 */
@Service
public class DeskStateUpdateHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStateUpdateHandler.class);

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.STATE_UPDATE == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) throws BusinessException {
        try {
            if (!request.getIsSuccess()) {
                LOGGER.error("收到云桌面[{}] 状态修改失败消息，错误信息：[{}]", request.getDeskId(), request.getErrorMsg());
                return;
            }

            if (request.getDeskState() == null) {
                LOGGER.error("收到云桌面[{}] 状态信息为空", request.getDeskId());
                return;
            }

            LOGGER.info("收到云桌面修改状态消息, 桌面id[{}]，状态：[{}]", request.getDeskId(), request.getDeskState().name());
            uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), request.getDeskState());
            rcaHostAPI.notifyDesktopStateUpdate(request.getDeskId(), request.getDeskState());
        } catch (Exception ex) {
            LOGGER.error("处理spi通知发生异常，异常信息：", ex);
        }
    }
}
