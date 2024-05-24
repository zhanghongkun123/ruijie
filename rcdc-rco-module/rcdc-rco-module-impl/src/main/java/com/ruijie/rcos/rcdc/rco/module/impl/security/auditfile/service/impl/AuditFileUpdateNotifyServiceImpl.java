package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditFileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.AuditFileUpdateNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.spi.request.AuditFileStrategyResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullNumberAsZero;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 16:58
 *
 * @author chenl
 */
@Service
public class AuditFileUpdateNotifyServiceImpl implements AuditFileUpdateNotifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditFileUpdateNotifyServiceImpl.class);

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private AuditFileMgmtAPI auditFileMgmtAPI;


    /**
     * JSON转字符串特征
     */
    private static final SerializerFeature[] JSON_FEATURES =
            new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero};

    /**
     * 终端通知处理线程池,分配1个线程数
     */
    private static final ThreadExecutor NOTICE_HANDLER_THREAD_POOL =
            ThreadExecutors.newBuilder(AuditFileUpdateNotifyServiceImpl.class.getName()).maxThreadNum(1).queueSize(200).build();

    @Override
    public void notifyAllStrategyDeskAuditFile() {
        //  查询所有云桌面策略
        List<CbbDeskStrategyDTO> deskStrategyList;
        try {
            deskStrategyList = cbbVDIDeskStrategyMgmtAPI.listDeskStrategyVDI();
        } catch (Exception e) {
            LOGGER.error("获取策略列表失败", e);
            return;
        }

        if (CollectionUtils.isEmpty(deskStrategyList)) {
            return;
        }

        for (CbbDeskStrategyDTO deskStrategy : deskStrategyList) {
            // 向在线终端发送策略变更消息
            NOTICE_HANDLER_THREAD_POOL.execute(() -> notifyDeskAuditFileStrategyAndStrategyId(deskStrategy.getId()));
        }
    }

    @Override
    public void notifyDeskAuditFileStrategyAndStrategyId(UUID id) {
        Assert.notNull(id, "deskStrategyId not be null");
        CbbDeskStrategyDTO deskStrategyDTO;
        try {
            deskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(id);
        } catch (Exception e) {
            LOGGER.error("获取策略[{}]数据失败", id, e);
            return;
        }

        // 获取云桌面策略关联的桌面列表
        List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.getAllDesktopByStrategyId(id);
        desktopList = desktopList.stream().filter(desktop -> Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.RUNNING.name()))
                .collect(Collectors.toList());

        sentDeskAuditFileStrategy(deskStrategyDTO, desktopList);
    }

    private void sentDeskAuditFileStrategy(CbbDeskStrategyDTO deskStrategyDTO, List<CloudDesktopDTO> desktopList) {
        if (CollectionUtils.isEmpty(desktopList)) {
            LOGGER.info("云桌面策略[{}]没有运行中的桌面，无需发送文件审计配置", deskStrategyDTO.getName());
            return;
        }
        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        messageDTO.setCmdId(Integer.parseInt(GuestToolCmdId.RCDC_GT_CMD_ID_AUDIT_FILE_GLOBAL_STRATEGY));
        messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_AUDIT_FILE);

        GuesttoolMessageContent guesttoolMessageContent = new GuesttoolMessageContent();
        guesttoolMessageContent.setCode(GuesttoolMessageResultTypeEnum.SUCCESS.getCode());
        guesttoolMessageContent.setMessage(GuesttoolMessageResultTypeEnum.SUCCESS.getMessage());

        boolean enableAuditFile = Boolean.TRUE.equals(deskStrategyDTO.getEnableAuditFile());
        AuditFileStrategyDTO auditFileStrategyDTO = new AuditFileStrategyDTO();
        if (enableAuditFile && Objects.nonNull(deskStrategyDTO.getAuditFileInfo())) {
            BeanUtils.copyProperties(deskStrategyDTO.getAuditFileInfo(), auditFileStrategyDTO);
        }
        auditFileStrategyDTO.setEnableAuditFile(enableAuditFile);

        FtpConfigDTO ftpConfigDTO = auditFileMgmtAPI.obtainAuditFileEncryptFtpInfo();
        AuditFileStrategyResponse strategyResponse = new AuditFileStrategyResponse();
        strategyResponse.setFtpInfo(ftpConfigDTO);
        strategyResponse.setAuditFileStrategy(auditFileStrategyDTO);
        guesttoolMessageContent.setContent(strategyResponse);

        for (CloudDesktopDTO desktopDTO : desktopList) {
            UUID deskId = desktopDTO.getId();
            messageDTO.setDeskId(deskId);
            messageDTO.setBody(JSON.toJSONString(guesttoolMessageContent, JSON_FEATURES));
            asyncRequest(messageDTO, deskId);
        }
    }

    private void asyncRequest(CbbGuesttoolMessageDTO messageDTO, UUID cbbDesktopId) {
        // 异步发送信息
        try {
            guestToolMessageAPI.asyncRequest(messageDTO);
        } catch (Exception e) {
            LOGGER.error("文件流转审计策略变更，发送变更通知到云桌面[" + cbbDesktopId + "]失败", e);
        }
    }
}
