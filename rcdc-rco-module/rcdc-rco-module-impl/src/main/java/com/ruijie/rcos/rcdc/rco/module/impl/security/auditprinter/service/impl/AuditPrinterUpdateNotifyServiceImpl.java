package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAuditPrinterConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditPrinterMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrinterStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkFieldMappingValueDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrinterUpdateNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.spi.response.AuditPrinterStrategyResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.utils.AuditPrinterUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.service.WatermarkMessageParser;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
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
public class AuditPrinterUpdateNotifyServiceImpl implements AuditPrinterUpdateNotifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditPrinterUpdateNotifyServiceImpl.class);

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private WatermarkMessageParser<WatermarkDisplayContentDTO> watermarkMessageParser;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private AuditPrinterMgmtAPI auditPrinterMgmtAPI;


    /**
     * JSON转字符串特征
     */
    private static final SerializerFeature[] JSON_FEATURES =
            new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero};

    /**
     * 终端通知处理线程池,分配1个线程数
     */
    private static final ThreadExecutor NOTICE_HANDLER_THREAD_POOL =
            ThreadExecutors.newBuilder(AuditPrinterUpdateNotifyServiceImpl.class.getName()).maxThreadNum(1).queueSize(200).build();


    @Override
    public void notifyAllStrategyDeskAuditPrinter() {
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
            NOTICE_HANDLER_THREAD_POOL.execute(() -> notifyDeskAuditPrinterStrategyAndStrategyId(deskStrategy.getId()));
        }
    }

    @Override
    public void notifyDeskAuditPrinterStrategyAndStrategyId(UUID id) {
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

        sentDeskAuditPrinterStrategy(deskStrategyDTO, desktopList);
    }

    @Override
    public void notifyDeskAuditPrinterStrategyAndDeskId(UUID deskId, CbbDeskStrategyVDIDTO deskStrategyDTO) throws BusinessException {
        Assert.notNull(deskId, "deskId not be null");
        Assert.notNull(deskStrategyDTO, "CbbDeskStrategyVDIDTO not be null");
        CloudDesktopDetailDTO desktopDetail = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        CloudDesktopDTO cloudDesktopDTO = new CloudDesktopDTO();
        BeanUtils.copyProperties(desktopDetail, cloudDesktopDTO);
        // 发送安全打印的信息给指定桌面
        sentDeskAuditPrinterStrategy(Boolean.TRUE.equals(deskStrategyDTO.getEnableAuditPrinter()),
                deskStrategyDTO.getAuditPrinterInfo(), cloudDesktopDTO);
    }

    private void sentDeskAuditPrinterStrategy(CbbDeskStrategyDTO deskStrategyDTO, List<CloudDesktopDTO> desktopList) {
        if (CollectionUtils.isEmpty(desktopList)) {
            LOGGER.info("云桌面策略[{}]没有运行中的桌面，无需发送打印审计配置", deskStrategyDTO.getName());
            return;
        }
        for (CloudDesktopDTO desktopDTO : desktopList) {
            // 发送安全打印的信息给指定桌面
            sentDeskAuditPrinterStrategy(Boolean.TRUE.equals(deskStrategyDTO.getEnableAuditPrinter()), 
                    deskStrategyDTO.getAuditPrinterInfo(), desktopDTO);
        }
    }

    private void sentDeskAuditPrinterStrategy(boolean enableAuditPrinter, 
                                              CbbAuditPrinterConfigDTO auditPrinterInfo, CloudDesktopDTO cloudDesktopDTO) {
        UUID deskId = cloudDesktopDTO.getId();
        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        messageDTO.setCmdId(Integer.parseInt(GuestToolCmdId.RCDC_GT_CMD_ID_AUDIT_PRINTER_GLOBAL_STRATEGY));
        messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_AUDIT_FILE);

        GuesttoolMessageContent guesttoolMessageContent = new GuesttoolMessageContent();
        guesttoolMessageContent.setCode(GuesttoolMessageResultTypeEnum.SUCCESS.getCode());
        guesttoolMessageContent.setMessage(GuesttoolMessageResultTypeEnum.SUCCESS.getMessage());
        
        AuditPrinterStrategyResponse strategyResponse = new AuditPrinterStrategyResponse();
        AuditPrinterStrategyDTO auditPrinterStrategyDTO = new AuditPrinterStrategyDTO();
        if (enableAuditPrinter && Objects.nonNull(auditPrinterInfo)) {
            BeanUtils.copyProperties(auditPrinterInfo, auditPrinterStrategyDTO);
        }
        auditPrinterStrategyDTO.setEnableAuditPrinter(enableAuditPrinter);
        FtpConfigDTO ftpConfigDTO = auditPrinterMgmtAPI.obtainAuditPrinterEncryptFtpInfo();
        strategyResponse.setFtpInfo(ftpConfigDTO);

        messageDTO.setDeskId(deskId);
        // 设置水印信息
        String displayContent = auditPrinterStrategyDTO.getDisplayContent();
        if (BooleanUtils.isTrue(auditPrinterStrategyDTO.getEnableWatermark()) && StringUtils.hasText(displayContent)) {
            WatermarkFieldMappingValueDTO mappingValue =
                    AuditPrinterUtils.getMappingValueDTO(cloudDesktopDTO, displayContent);
            WatermarkDisplayContentDTO displayContentDTO = watermarkMessageParser.parse(mappingValue, displayContent);
            auditPrinterStrategyDTO.setDisplayContent(JSON.toJSONString(displayContentDTO));
        } else {
            auditPrinterStrategyDTO.setDisplayContent(StringUtils.EMPTY);
        }
        strategyResponse.setAuditPrinterStrategy(auditPrinterStrategyDTO);
        guesttoolMessageContent.setContent(strategyResponse);
        messageDTO.setBody(JSON.toJSONString(guesttoolMessageContent, JSON_FEATURES));
        asyncRequest(messageDTO, deskId);
    }

    private void asyncRequest(CbbGuesttoolMessageDTO messageDTO, UUID cbbDesktopId) {
        // 异步发送信息
        try {
            guestToolMessageAPI.asyncRequest(messageDTO);
        } catch (Exception e) {
            LOGGER.error("文件导出审批策略变更，发送变更通知到云桌面[" + cbbDesktopId + "]失败", e);
        }
    }

}
