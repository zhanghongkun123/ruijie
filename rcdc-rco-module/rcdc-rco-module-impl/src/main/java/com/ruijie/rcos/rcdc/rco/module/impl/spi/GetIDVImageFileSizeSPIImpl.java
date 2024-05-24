package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVOIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbIDVDeskImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.IDVImageFileDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbIDVImageDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.AbstractDeskStrategyLocalModeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GetBandImageInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ConfigWizardForIDVCode;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.IDVTerminalReportConfigWizardDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 *
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/19
 *
 * @author zhiweiHong
 */
@DispatcherImplemetion(Constants.GET_BIND_IMAGE_INFO)
public class GetIDVImageFileSizeSPIImpl extends AbstractIDVConfigWizardSPI implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetIDVImageFileSizeSPIImpl.class);

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    protected CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest不能为null");
        Assert.hasText(request.getData(), "报文data不能为空");

        LOGGER.info("收到镜像编辑:terminalId:{};data:{}", request.getTerminalId(), request.getData());

        // 1、入参校验
        JSONObject dataJson = JSONObject.parseObject(request.getData());
        IDVTerminalReportConfigWizardDTO requestDto = dataJson.toJavaObject(IDVTerminalReportConfigWizardDTO.class);
        // 2、验证终端数据是否合法，获取t_rco_user_terminal表中的终端信息
        if (checkTerminal(request, requestDto)) {
            return;
        }

        boolean hasNotifyShine;
        GetBandImageInfoDTO cloudDesktopDTO = new GetBandImageInfoDTO();
        cloudDesktopDTO.setTerminalId(request.getTerminalId());
        cloudDesktopDTO.setIdvTerminalMode(requestDto.getIdvTerminalMode());
        // 3、判断终端模式：个人模式or公用模式，个人模式需要判断用户信息，公用模式需要判断终端分组信息
        switch (requestDto.getIdvTerminalMode()) {
            case PERSONAL:
                hasNotifyShine = personalModeProcess(request, requestDto, cloudDesktopDTO);
                break;
            case PUBLIC:
                hasNotifyShine = publicModeProcess(request, requestDto, cloudDesktopDTO);
                break;
            default:
                responseErrorMessage(request, ConfigWizardForIDVCode.UN_SUPPORT_TERMINAL_MODE);
                return;
        }

        if (hasNotifyShine) {
            LOGGER.info("前置处理中已经有返回消息给SHINE，无需执行后续逻辑");
            return;
        }

        try {
            // 查询 终端的信息
            CbbTerminalBasicInfoDTO terminalDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询到终端信息{}", JSON.toJSONString(terminalDTO));
            }
            LOGGER.info("查询到终端信息{}", JSON.toJSONString(terminalDTO)); //排查SPI阻塞日志
            // 构建返回信息
            buildGetBandImageInfoDTO(cloudDesktopDTO, terminalDTO.getTerminalPlatform());
            LOGGER.info("查询到云桌面信息2：{}",cloudDesktopDTO);  //排查SPI阻塞日志
            // 响应信息
            responseSuccessMessage(request, cloudDesktopDTO);
        } catch (BusinessException e) {
            LOGGER.error("无法查询到终端信息{}", request.getTerminalId());
            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_EXIST);
        } catch (Exception e) {
            LOGGER.error("获取镜像模板异常,请求信息为：{}", JSON.toJSONString(request), e);
            responseErrorMessage(request, ConfigWizardForIDVCode.CODE_ERR_OTHER);
        }
    }

    private void buildGetBandImageInfoDTO(GetBandImageInfoDTO cloudDesktopDTO, CbbTerminalPlatformEnums platform) throws BusinessException {
        // 镜像模板的信息
        CbbIDVDeskImageTemplateDTO imageTemplateDTO = cbbIDVDeskMgmtAPI.getIDVDeskImageTemplate(cloudDesktopDTO.getImageId());
        Long imageFileSize = getImageTemplateNeedSpace(imageTemplateDTO);
        cloudDesktopDTO.setImageFileSize(imageFileSize);
        cloudDesktopDTO.setImageSystemDiskSize(imageTemplateDTO.getCbbImageTemplateDetailDTO().getSystemDisk());

        int imageDataDiskSize = getImageDataDiskSize(imageTemplateDTO.getImageDiskList());
        cloudDesktopDTO.setImageDataDiskSize(imageDataDiskSize);
        LOGGER.info("查询到cloudDesktopDTO：{}", JSON.toJSONString(cloudDesktopDTO)); // 排查SPI阻塞日志
        // 云桌面策略的信息
        AbstractDeskStrategyLocalModeDTO strategyInfo;
        if (platform == CbbTerminalPlatformEnums.IDV) {
            strategyInfo = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(cloudDesktopDTO.getStrategyId());
        } else {
            strategyInfo = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(cloudDesktopDTO.getStrategyId());
        }
        cloudDesktopDTO.setStrategySystemDiskSize(strategyInfo.getSystemSize());
        cloudDesktopDTO.setEnableFullSystemDisk(strategyInfo.getEnableFullSystemDisk());
        cloudDesktopDTO.setImageRecoveryPointId(imageTemplateDTO.getCbbImageTemplateDetailDTO().getLastRecoveryPointId());

    }

    private int getImageDataDiskSize(List<CbbIDVImageDiskDTO> imageDiskList) {

        if (CollectionUtils.isEmpty(imageDiskList)) {
            return 0;
        }

        Optional<CbbIDVImageDiskDTO> diskOpt = imageDiskList.stream().filter(disk -> {
            if (disk.getImageDiskType() != CbbImageDiskType.DATA) {
                return false;
            }
            IDVImageFileDTO idvImageFileDTO = disk.getIdvImageFileDTO();

            return StringUtils.isNoneBlank( //
                    idvImageFileDTO.getTorrentFilePath(),
                    idvImageFileDTO.getTorrentFileName(),
                    idvImageFileDTO.getTorrentFileMD5());

        }).findFirst();

        if (!diskOpt.isPresent()) {
            return 0;
        }
        CbbIDVImageDiskDTO cbbIDVImageDiskDTO = diskOpt.get();

        Integer diskSize = Optional.of(cbbIDVImageDiskDTO).map(CbbIDVImageDiskDTO::getDiskSize).orElse(0);

        Integer vmDiskSize = Optional.of(cbbIDVImageDiskDTO).map(CbbIDVImageDiskDTO::getVmDiskSize).orElse(0);

        return vmDiskSize == 0 ? diskSize : vmDiskSize;
    }

    @Override
    protected boolean isNeedCheckUserBindRelation(CbbDispatcherRequest request, IacUserDetailDTO cbbUserInfoDTO) {
        return false;
    }


    private Long getImageTemplateNeedSpace(CbbIDVDeskImageTemplateDTO idvDeskImageTemplate) {
        List<CbbIDVImageDiskDTO> imageDiskList = Optional.ofNullable(idvDeskImageTemplate.getImageDiskList()).orElse(new ArrayList<>());
        Long imageFileSize = 0L;
        for (CbbIDVImageDiskDTO idvImageDiskDTO : imageDiskList) {

            IDVImageFileDTO idvImageFileDTO = idvImageDiskDTO.getIdvImageFileDTO();
            if (ObjectUtils.isEmpty(idvImageFileDTO)) {
                continue;
            }

            // 种子文件为空，该镜像没有发布，不计算空间大小
            if (StringUtils.isAnyBlank(idvImageFileDTO.getTorrentFileMD5(), //
                    idvImageFileDTO.getTorrentFileName(), //
                    idvImageFileDTO.getTorrentFilePath())) { //
                continue;
            }

            imageFileSize += Optional.ofNullable(idvImageFileDTO.getFileSize()).orElse(0L);
            if (ObjectUtils.isEmpty(idvImageFileDTO.getBackingFile())) {
                continue;
            }
            imageFileSize += Optional.ofNullable(idvImageFileDTO.getBackingFile().getFileSize()).orElse(0L);
        }
        return imageFileSize;
    }
}
