package com.ruijie.rcos.rcdc.rco.module.impl.spi.startvmhandler;

import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.StartVmMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.StartVmDispatcherDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Description: 启动云桌面（当前状态处于不可用状态）
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 *
 * @author Jarman
 */
@Service
public class StartVmFromUnavailableStateHandler extends AbstractMessageHandlerTemplate<StartVmDispatcherDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartVmFromUnavailableStateHandler.class);

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Override
    protected boolean isNeedHandleMessage(StartVmDispatcherDTO request) {
        // 不可用状态，包括：关机中、从回收站恢复中、唤醒中、还原中、休眠中、更新配置数据中、故障恢复中
        switch (request.getDeskState()) {
            case SHUTTING:
            case RECOVING:
            case WAKING:
            case RESTORING:
            case SLEEPING:
            case UPDATING:
            case FAILBACKING:
            case SNAPSHOT_CREATING:
            case SNAPSHOT_RESTORING:
//            case BACKUP_CREATING:
            case BACKUP_RESTORING:
            case CREATING:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void processMessage(StartVmDispatcherDTO request) throws BusinessException {
        String terminalId = request.getDispatcherRequest().getTerminalId();
        UUID desktopId = request.getDesktopId();
        LOGGER.info("当前桌面状态处于[{}]状态，terminalId={},desktopId={}", request.getDeskState(), terminalId, desktopId);

        String errMsg = LocaleI18nResolver.resolve(BusinessKey.RCD_RCO_SHINE_START_VM_VM_BUSY,
                LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_CLOUDDESKTOP_STATE_PRE + request.getDeskState().name().toLowerCase()));
        try {
            shineMessageHandler.responseMessage(request.getDispatcherRequest(), StartVmMessageCode.CODE_ERR_STATE, errMsg);
            LOGGER.info("应答报文给终端[{}]成功，应答状态码：{},应答内容：{}", terminalId, StartVmMessageCode.CODE_ERR_STATE, errMsg);
        } catch (Exception e) {
            LOGGER.error("应答报文给终端[" + terminalId + "]失败，应答状态码：" + StartVmMessageCode.CODE_ERR_STATE + "应答内容：" + errMsg, e);
        }
    }
}
