package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.GuestToolNormalMsgDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

/**
 * Description:  GuestTool 上报软件信息抽象实现
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.11.05
 *
 * @author LinHJ
 */
public abstract class AbstractGuestToolMessageSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGuestToolMessageSPIImpl.class);

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {

        Assert.notNull(request, "request must not be null");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("收到 GuestTool 消息 [{}] 请求：{}", getName(), JSON.toJSONString(request));
        }

        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            GuestToolNormalMsgDTO guestToolNormalMsgDTO = parseGuestToolMsg(request.getDto().getBody(), GuestToolNormalMsgDTO.class);
            doReceive(guestToolNormalMsgDTO);

        } catch (Exception ex) {
            LOGGER.error(String.format("接受 GuestTool 消息 [%s] 处理错误", getName()), ex);
        } finally {
            stopWatch.stop();
            LOGGER.info("处理 GuestTool 消息 [{}] 请求成功，{}", getName(), stopWatch.shortSummary());
        }

        return wrapperCbbGuestToolMessageDTO(request.getDto());
    }

    /**
     * 业务标题
     *
     * @return return
     */
    public abstract String getName();

    /**
     * 业务实现
     *
     * @param guestToolNormalMsgDTO 业务对象
     */
    public abstract void doReceive(GuestToolNormalMsgDTO guestToolNormalMsgDTO);

    /**
     * 解析对象
     */
    @SuppressWarnings("SameParameterValue")
    private <T> T parseGuestToolMsg(String msgBody, Class<T> clz) {

        T bodyMsg;
        try {
            bodyMsg = JSON.parseObject(msgBody, clz);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("GuestTool 报文格式错误：%s", msgBody), e);
        }
        return bodyMsg;
    }

    /**
     * 返回已封装的对象
     */
    private CbbGuesttoolMessageDTO wrapperCbbGuestToolMessageDTO(CbbGuesttoolMessageDTO cbbGuesttoolMessageDTO) {

        final CbbGuesttoolMessageDTO responseDto = new CbbGuesttoolMessageDTO();
        responseDto.setCmdId(cbbGuesttoolMessageDTO.getCmdId());
        responseDto.setPortId(cbbGuesttoolMessageDTO.getPortId());
        responseDto.setDeskId(cbbGuesttoolMessageDTO.getDeskId());

        GuesttoolMessageContent messageContent = new GuesttoolMessageContent();
        messageContent.setCode(GuesttoolMessageResultTypeEnum.SUCCESS.getCode());
        messageContent.setMessage(GuesttoolMessageResultTypeEnum.SUCCESS.getMessage());
        messageContent.setContent(StringUtils.EMPTY);

        responseDto.setBody(JSON.toJSONString(messageContent));
        return responseDto;
    }
}
