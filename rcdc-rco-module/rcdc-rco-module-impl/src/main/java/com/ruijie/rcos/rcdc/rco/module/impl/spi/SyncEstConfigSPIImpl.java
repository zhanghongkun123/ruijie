package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGlobalStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAgreementDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAgreementTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbHestConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbAgreementTemplateEnum;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.EstConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.DeskIdRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.SyncEstConfigDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalOsTypeEnums;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Description: 同步EST配置SPI
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/21 16:52
 *
 * @author yxq
 */
@DispatcherImplemetion(ShineAction.SYNC_EST_CONFIG)
public class SyncEstConfigSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncEstConfigSPIImpl.class);

    @Autowired
    private EstConfigAPI estConfigAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request must not be null");
        String data = request.getData();
        UUID deskId = null;
        try {
            SyncEstConfigDTO syncEstConfigDTO = new SyncEstConfigDTO();
            syncEstConfigDTO.setTimestamp(new Date());
            syncEstConfigDTO.setEstProtocolType(CbbEstProtocolType.EST);

            // 企金2.0 会传桌面ID,获取云桌面策略中的协议配置,旧版本和IDV使用默认EST流畅模版
            if (StringUtils.isNotBlank(data)) {
                DeskIdRequestDTO deskIdRequest = JSON.parseObject(data, DeskIdRequestDTO.class);
                deskId = deskIdRequest.getDeskId();
            }
            CbbDeskStrategyVDIDTO deskStrategyVDI = null;
            if (Objects.nonNull(deskId)) {
                CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
                if (CbbCloudDeskType.VDI == deskDTO.getDeskType()) {
                    deskStrategyVDI = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(deskDTO.getStrategyId());
                }
            }

            if (Objects.isNull(deskStrategyVDI)) {
                setEstFluencyFirstConfig(syncEstConfigDTO);
                response(request, CommonMessageCode.SUCCESS, syncEstConfigDTO);
                return;
            }

            // 安卓终端盒子不支持HEST
            CbbEstProtocolType type = deskStrategyVDI.getEstProtocolType();
            if (CbbEstProtocolType.HEST == type) {
                CbbTerminalBasicInfoDTO terminalBasicInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
                if (CbbTerminalOsTypeEnums.ANDROID == CbbTerminalOsTypeEnums.convert(terminalBasicInfo.getTerminalOsType())) {
                    setEstFluencyFirstConfig(syncEstConfigDTO);
                    response(request, CommonMessageCode.SUCCESS, syncEstConfigDTO);
                    return;
                }
            }

            CbbAgreementDTO agreementInfo = deskStrategyVDI.getAgreementInfo();
            if (Objects.isNull(agreementInfo)) {
                setEstFluencyFirstConfig(syncEstConfigDTO);
            } else {
                syncEstConfigDTO.setEstProtocolType(type);
                syncEstConfigDTO.setLanEstConfig(convertConfig(type, agreementInfo.getLanEstConfig()));
                syncEstConfigDTO.setWanEstConfig(convertConfig(type, agreementInfo.getWanEstConfig()));
            }

            response(request, CommonMessageCode.SUCCESS, syncEstConfigDTO);
        } catch (Exception e) {
            LOGGER.error(String.format("终端[%s]获取EST配置失败，失败原因：", request.getTerminalId()), e);
            response(request, CommonMessageCode.FAIL_CODE, new SyncEstConfigDTO());
        }
    }

    private CbbHestConfigDTO convertConfig(CbbEstProtocolType type, CbbHestConfigDTO config) {
        // transport为RUTP并且启用音视频优化,transport设为2
        if (CbbEstProtocolType.HEST == type && Objects.equals(Constants.INT_1, config.getTransport())
                && Boolean.TRUE.equals(config.getEnableAudioVideoOptimize())) {
            config.setTransport(Constants.INT_2);
        }
        return config;
    }

    private void setEstFluencyFirstConfig(SyncEstConfigDTO syncEstConfigDTO) {
        CbbAgreementTemplateDTO agreementTemplate = cbbGlobalStrategyMgmtAPI.getAgreementTemplate(CbbEstProtocolType.EST);
        if (Objects.nonNull(agreementTemplate)) {
            CbbHestConfigDTO lanConfigDTO = templateFilter(agreementTemplate.getLanTemplateList(),
                    CbbAgreementTemplateEnum.LAN_FLUENCY_FIRST);
            syncEstConfigDTO.setLanEstConfig(lanConfigDTO);
            CbbHestConfigDTO wanConfigDTO = templateFilter(agreementTemplate.getWanTemplateList(),
                    CbbAgreementTemplateEnum.WAN_FLUENCY_FIRST);
            syncEstConfigDTO.setWanEstConfig(wanConfigDTO);
        } else {
            setDefaultEstConfig(syncEstConfigDTO);
        }
    }

    private void setDefaultEstConfig(SyncEstConfigDTO syncEstConfigDTO) {
        syncEstConfigDTO.setLanEstConfig((CbbHestConfigDTO) estConfigAPI.getEstLanConfig());
        syncEstConfigDTO.setWanEstConfig((CbbHestConfigDTO) estConfigAPI.getEstWanConfig());
    }

    private CbbHestConfigDTO templateFilter(List<CbbHestConfigDTO> templateList, CbbAgreementTemplateEnum templateEnum) {
        Optional<CbbHestConfigDTO> first =
                templateList.stream().filter(estConfigDTO -> Objects.equals(templateEnum.getId(), estConfigDTO.getTemplateId())).findFirst();
        return first.orElseGet(CbbHestConfigDTO::new);
    }

    private void response(CbbDispatcherRequest request, int code, SyncEstConfigDTO syncEstConfigDTO) {
        LOGGER.info("终端[{}]获取EST配置信息为：[{}]", request.getTerminalId(), JSON.toJSONString(syncEstConfigDTO));
        try {
            shineMessageHandler.responseContent(request, code, syncEstConfigDTO);
        } catch (Exception e) {
            LOGGER.error(String.format("响应终端[%s]获取EST配置信息失败，失败原因：", request.getTerminalId()), e);
        }
    }
}
