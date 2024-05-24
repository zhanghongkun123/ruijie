package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskRemoteAssistResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerDeskRemoteAssistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistantMessageBody;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistantMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistantMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistantRemoteResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ComputerRemoteAssistRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ObtainDeskRemoteAssistConnectionInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.AssistantRemoteResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.VncConnectionInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.AssistantMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.NotifyClientBusinessEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AssistantMessageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.VncProxyService;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月27日
 * 
 * @author ketb
 */
public class ComputerDeskRemoteAssistAPIImpl implements ComputerDeskRemoteAssistAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDeskRemoteAssistAPIImpl.class);

    @Autowired
    private AssistantMessageService assistantMessageService;

    @Autowired
    private VncProxyService vncProxyService;

    @Autowired
    private ComputerBusinessDAO computerDAO;

    @Override
    public AssistantRemoteResponse remoteAssistDesk(ComputerRemoteAssistRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        LOGGER.info("执行远程协助云桌面操作API，云桌面id[{}]", request.getId());
        AssistantMessageDTO dto = new AssistantMessageDTO();
        // 设置autoAgree
        dto.setAutoAgree(request.getAutoAgree());
        AssistantRemoteResponseDTO responseDto = assistantMessageSyncRequest(dto, request.getId(),
                NotifyClientBusinessEnum.REMOTE_ASSIST.getBusinessName());

        LOGGER.info("远程协助小助手应答消息内容[{}]", responseDto.toString());
        responseDto.setDeskId(request.getId());
        AssistantRemoteResponse response = new AssistantRemoteResponse();
        response.setResponseDTO(responseDto);
        return response;
    }

    @Override
    public void cancelRemoteAssistDesk(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        LOGGER.info("执行取消远程协助云桌面操作API，云桌面id[{}]", deskId);
        AssistantMessageDTO dto = new AssistantMessageDTO();
        assistantMessageSyncRequest(dto, deskId, NotifyClientBusinessEnum.CANCEL_REMOTE_ASSIST.getBusinessName());
    }

    @Override
    public CbbDeskRemoteAssistResultDTO obtainDeskRemoteAssistInfo(ObtainDeskRemoteAssistConnectionInfoRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        LOGGER.info("执行获取pc远程协助连接信息操作API，云桌面id[{}]", request.getId());

        VncConnectionInfoDTO vncConnectionInfoDTO = vncProxyService.addVncConfig(request.getId());
        CbbDeskRemoteAssistResultDTO response = new CbbDeskRemoteAssistResultDTO();
        response.setIp(vncConnectionInfoDTO.getIp());
        response.setPort(vncConnectionInfoDTO.getPort());
        response.setToken(vncConnectionInfoDTO.getToken());
        ComputerEntity entity = computerDAO.findComputerEntityById(request.getId());
        if (!StringUtils.isEmpty(entity.getAssistPwd())) {
            response.setPwd(AesUtil.encrypt(entity.getAssistPwd(), Constants.AES_ASSIST_KEY));
        }
        return response;
    }

    @Override
    public void createVncChannelResult(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        LOGGER.info("执行VNC连接成功通知接口API，云桌面id[{}]", deskId);
        AssistantMessageDTO dto = new AssistantMessageDTO();
        AssistantMessageBody body = buildAssistantMessageBody(dto);
        dto.setBody(JSON.toJSONString(body));
        assistantMessageSyncRequest(dto, deskId, NotifyClientBusinessEnum.CREATE_VNC.getBusinessName());
    }

    private AssistantRemoteResponseDTO assistantMessageSyncRequest(AssistantMessageDTO assistantMessageDTO, UUID deskId,
                                                                   String cmdName)
            throws BusinessException {
        assistantMessageDTO.setDeskId(deskId);
        assistantMessageDTO.setBusiness(cmdName);

        return assistantMessageService.syncRequest(assistantMessageDTO);
    }

    private AssistantMessageBody buildAssistantMessageBody(AssistantMessageDTO dto) {
        AssistantMessageBody body = new AssistantMessageBody();
        body.setBusiness(dto.getBusiness());

        AssistantMessageContent content = new AssistantMessageContent();
        content.setCode(AssistantMessageResultTypeEnum.SUCCESS.getCode());
        content.setMessage(AssistantMessageResultTypeEnum.SUCCESS.getMessage());
        body.setContent(content);
        return body;
    }

}
