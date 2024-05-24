package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserMessageUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.UserMessageStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.GuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 用户消息状态处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/2
 *
 * @author Jarman
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_SMS)
public class UserMessageStateHciGuesttoolChannelSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMessageStateHciGuesttoolChannelSPIImpl.class);

    @Autowired
    private UserDesktopDAO desktopDAO;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserMessageUserDAO userMessageUserDAO;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot null");

        final CbbGuesttoolMessageDTO dto = request.getDto();

        LOGGER.info("接收到GT用户消息状态，desktopId={},body={}", dto.getDeskId(), dto.getBody());
        GuestToolMsgDTO guestToolMsgDTO = parseGuestToolMsg(dto.getBody(), GuestToolMsgDTO.class);
        Assert.notNull(guestToolMsgDTO.getContent(), "content不能为null");
        Assert.notNull(guestToolMsgDTO.getContent().getId(), "消息id不能为null");
        Assert.notNull(guestToolMsgDTO.getContent().getStatus(), "消息状态不能为null");
        UUID cbbDeskId = dto.getDeskId();
        UserDesktopEntity userDesktopEntity = desktopDAO.findByCbbDesktopId(cbbDeskId);
        if (userDesktopEntity == null) {
            throw new IllegalStateException("找不到cbbDeskId=" + cbbDeskId + "数据");
        }

        UUID userId = userDesktopEntity.getUserId();
        if (Objects.nonNull(guestToolMsgDTO.getContent().getMultiSessionId())) {
            userId = guestToolMsgDTO.getContent().getMultiSessionId();
        }
        if (Objects.isNull(userId)) {
            throw new IllegalStateException("处理用户消息状态userId为空");
        }
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
        if (userDetail == null) {
            throw new IllegalStateException("不存在 userId=" + userDesktopEntity.getUserId() + "的用户数据");
        }
        UUID msgId = guestToolMsgDTO.getContent().getId();
        UserMessageStateEnum state = guestToolMsgDTO.getContent().getStatus();
        // 修改消息状态
        updateState(state,userDetail,dto.getDeskId(),msgId);

        return new CbbGuesttoolMessageDTO();
    }
    
    private void updateState(UserMessageStateEnum state, IacUserDetailDTO userDetail, UUID deskId, UUID msgId) {
        // 乐观锁重试
        while (true) {
            try {
                UserMessageUserEntity userMessageUserEntity = getUserMessageUserEntity(userDetail, deskId, msgId);
                userMessageUserEntity.setState(state);
                userMessageUserDAO.save(userMessageUserEntity);
                break;
            } catch (ObjectOptimisticLockingFailureException e) {
                LOGGER.error("更新用户消息发生乐观锁异常,休眠200毫秒后重试", e);
                sleep(200);
            }
        }
    }
    
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("休眠异常", e);
        }
    }

    private UserMessageUserEntity getUserMessageUserEntity(IacUserDetailDTO userDetail, UUID desktopId, UUID msgId) {
        UserMessageUserEntity userMessageUserEntity = userMessageUserDAO.findByMessageIdAndDesktopId(msgId, desktopId);
        
        if (userMessageUserEntity == null) {
            if (userDetail.getUserType() == IacUserTypeEnum.VISITOR) {
                throw new IllegalStateException("不存在消息id=" + msgId + ",userId=" + userDetail.getId() + "的用户消息数据");
            }
            userMessageUserEntity = userMessageUserDAO.findUserMessageUserEntityByMessageIdAndUserId(msgId, userDetail.getId());
        }

        if (userMessageUserEntity == null) {
            throw new IllegalStateException("不存在消息id=" + msgId + ",userId=" + userDetail.getId() + "的用户消息数据");
        }
        return userMessageUserEntity;
    }

    private <T> T parseGuestToolMsg(String msgBody, Class<T> clz) {
        T bodyMsg;
        try {
            bodyMsg = JSON.parseObject(msgBody, clz);
        } catch (Exception e) {
            throw new IllegalArgumentException("guest tool报文格式错误.data:" + msgBody, e);
        }
        return bodyMsg;
    }
}
