package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserMessageDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserMessageService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Description: 用户消息定时任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/26
 *
 * @author Jarman
 */
@Service
@Quartz(scheduleTypeCode = "user_user_message", scheduleName = BusinessKey.RCDC_USER_QUARTZ_USER_MESSAGE, cron = "0 0/1 * * * ?")
public class UserMessageQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMessageQuartzTask.class);

    private static final ExecutorService SEND_USER_MESSAGE_THREAD_POOL =
            ThreadExecutors.newBuilder("userMessageHandlerThread").maxThreadNum(20).queueSize(1).build();

    private static final int MESSAGE_SEND_INTERVAL = 200;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private UserMessageDAO userMessageDAO;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        // 每60秒执行检查运行中的云桌面是否有未读消息，并尝试发送
        List<ViewUserDesktopEntity> runningDesktopList = viewDesktopDetailDAO.findAllByDeskStateAndIsDelete(CbbCloudDeskState.RUNNING.name(), false);
        if (CollectionUtils.isEmpty(runningDesktopList)) {
            LOGGER.debug("[用户消息发送]当前没有正在运行的云桌面");
            return;
        }
        LOGGER.debug("[用户消息发送]当前正在运行的云桌面个数为：{}", runningDesktopList.size());
        Set<UUID> userSet = new HashSet<>();
        for (ViewUserDesktopEntity desktopEntity : runningDesktopList) {
            if (Objects.isNull(desktopEntity.getUserId())) {
                // 无用户不处理
                continue;
            }
            // 过滤掉idv公用桌面
            if (StringUtils.equals(desktopEntity.getDesktopType(), CbbCloudDeskType.IDV.name()) && desktopEntity.getUserId() == null) {
                continue;
            }

            UUID userId = desktopEntity.getUserId();
            // 发送过的用户不再重复发送
            if (userSet.contains(userId)) {
                continue;
            }
            userSet.add(userId);

            List<UserMessageUserEntity> entityList = userMessageService.findNeedSendMessage(userId);
            if (CollectionUtils.isEmpty(entityList)) {
                continue;
            }

            SEND_USER_MESSAGE_THREAD_POOL.execute(() -> sendUserMessage(entityList));
        }
    }

    private void sendUserMessage(List<UserMessageUserEntity> entityList) {
        // 遍历用户消息
        List<UUID> idList = new ArrayList<>();
        entityList.stream().forEach(userMessage ->
            idList.add(userMessage.getMessageId()));
        List<UserMessageEntity> userMessageList = userMessageDAO.findByMessageIds(idList);
        if (userMessageList.size() > 1) {
            Collections.sort(userMessageList, (o1,o2) -> {
                Assert.notNull(o1, "o1 is not be null");
                Assert.notNull(o2, "o2 is not be null");
                return o1.getCreateTime().compareTo(o2.getCreateTime());
            });
        }

        UserMessageUserEntity userMessageUserEntity = entityList.get(0);
        for (UserMessageEntity userMessageEntity : userMessageList) {
            LOGGER.debug(userMessageEntity.getTitle() + "+" + userMessageEntity.getContent());
            sendMessage(userMessageEntity, userMessageUserEntity);
            try {
                Thread.sleep(MESSAGE_SEND_INTERVAL);
            } catch (InterruptedException e) {
                LOGGER.error("发送用户消息间隔等待异常：", e);
            }
        }
    }

    private void sendMessage(UserMessageEntity userMessageEntity, UserMessageUserEntity userMessageUserEntity) {
        UserMessageDTO userMessageDTO = new UserMessageDTO();
        userMessageDTO.setTitle(userMessageEntity.getTitle());
        userMessageDTO.setContent(userMessageEntity.getContent());
        userMessageDTO.setCreateTime(userMessageEntity.getCreateTime());
        userMessageDTO.setId(userMessageEntity.getId());
        try {
            if (userMessageUserEntity.getDesktopId() == null) {
                userMessageService.sendMessageToGuestTool(userMessageUserEntity, userMessageDTO);
            } else {
                userMessageService.sendDesktopMessageToGuestTool(userMessageUserEntity.getDesktopId(), userMessageUserEntity, userMessageDTO);
            }
        } catch (Exception e) {
            LOGGER.error("向用户[" + userMessageUserEntity.getUserId() + "]发送消息失败", e);
        }
    }
}
