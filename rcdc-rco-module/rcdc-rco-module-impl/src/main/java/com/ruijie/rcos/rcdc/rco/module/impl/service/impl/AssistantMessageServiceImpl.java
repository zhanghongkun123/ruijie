package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistantMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistantRemoteResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AssistantMessageService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/23 15:48
 *
 * @author ketb
 */
@Service
public class AssistantMessageServiceImpl implements AssistantMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssistantMessageServiceImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private ComputerBusinessDAO computerDAO;
    
    /** 远程协助接口结果成功标识 */
    private static final int SUCCESS = 0;

    @Override
    public AssistantRemoteResponseDTO syncRequest(AssistantMessageDTO messageDTO) throws BusinessException {
        Assert.notNull(messageDTO, "cbbMessageDTO cannot be null!");

        LOGGER.info("发送PC桌面小助手消息（同步），message[{}]", messageDTO.toString());
        CbbShineMessageRequest shineMessageRequest = buildShineMessageRequest(messageDTO);
        CbbShineMessageResponse shineMessageResponse = null;
        try {
            shineMessageResponse = cbbTranspondMessageHandlerAPI.syncRequest(shineMessageRequest);
            Assert.isTrue(shineMessageResponse.getCode() == SUCCESS, "shineMessageResponse status is fail!");
        } catch (Exception e) {
            LOGGER.error("PC桌面小助手消息发送失败", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_ASSISTANT_MESSAGE_PC_SEND_FAIL, e);
        }
        AssistantRemoteResponseDTO dto = new AssistantRemoteResponseDTO();
        dto.setCode(shineMessageResponse.getCode());
        return dto;
    }

    @Override
    public void asyncRequest(AssistantMessageDTO messageDTO) throws BusinessException {
        Assert.notNull(messageDTO, "cbbMessageDTO cannot be null!");

        LOGGER.info("发送PC桌面小助手消息（异步），message[{}]", messageDTO.toString());
        CbbShineMessageRequest shineMessageRequest = buildShineMessageRequest(messageDTO);
        try {
            cbbTranspondMessageHandlerAPI.asyncRequest(shineMessageRequest, new GtMessageCbbTerminalCallbackImpl());
        } catch (Exception e) {
            LOGGER.error("PC桌面小助手消息发送失败", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_ASSISTANT_MESSAGE_PC_SEND_FAIL, e);
        }
    }

    @Override
    public void broadcastRequest(AssistantMessageDTO messageDTO) throws BusinessException {
        Assert.notNull(messageDTO, "cbbMessageDTO cannot be null!");

        LOGGER.error("不支持PC桌面小助手消息广播，message[{}]", messageDTO.toString());
    }

    private CbbShineMessageRequest buildShineMessageRequest(AssistantMessageDTO messageDTO) throws BusinessException {
        UUID deskId = messageDTO.getDeskId();
        ComputerEntity computerEntity = computerDAO.getOne(deskId);

        if (null == computerEntity) {
            LOGGER.error("不存在PC桌面，ID={}", deskId);
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_ASSISTANT_MESSAGE_PC_DESK_NOT_EXIST, deskId.toString());
        }

        String terminalId = com.ruijie.rcos.rcdc.terminal.module.def.constants.Constants.PC_FLAG + computerEntity.getMac();
        CbbShineMessageRequest shineMessageRequest = CbbShineMessageRequest.create(Constants.RCDC_TO_ASSISTANT_ACTION, terminalId);
        shineMessageRequest.setContent(messageDTO);
        return shineMessageRequest;
    }

    /**
     * 异步消息回调实现类
     */
    private class GtMessageCbbTerminalCallbackImpl implements CbbTerminalCallback {
        @Override
        public void success(String terminalId, CbbShineMessageResponse msg) {
            Assert.notNull(terminalId, "terminalId cannot be null!");
            Assert.notNull(msg, "msg cannot be null!");

            LOGGER.info("PC桌面小助手消息发送成功，terminalId[{}]，信息[{}]", terminalId, msg.toString());
        }

        @Override
        public void timeout(String terminalId) {
            Assert.notNull(terminalId, "terminalId cannot be null!");

            LOGGER.error("PC桌面小助手消息发送超时，terminalId[{}]", terminalId);
        }
    }
}
