package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbStartRemoteTerminalVmDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.RemoteTerminalEditImageValidSPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageDownloadStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.enums.TerminalInstallDriverStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.imagedriver.request.dto.TerminalInstallDriverStateDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageDownloadStateService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 远程终端编辑镜像校验
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月11日
 *
 * @author ypp
 */

public class RemoteTerminalEditImageValidSPIImpl implements RemoteTerminalEditImageValidSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteTerminalEditImageValidSPIImpl.class);


    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private ImageDownloadStateService imageDownloadStateService;


    @Override
    public boolean validRemoteTerminalEditImage(CbbStartRemoteTerminalVmDTO startRemoteTerminalVmDTO) throws BusinessException {
        Assert.notNull(startRemoteTerminalVmDTO, "startRemoteTerminalVmDTO must not be null");

        // idv重新编辑可以不校验
        if (Boolean.TRUE.equals(startRemoteTerminalVmDTO.getReEdit())) {
            return true;
        }

        validTerminalDownloadImage(startRemoteTerminalVmDTO);

        TerminalInstallDriverStateDTO dto = getTerminalInstallDriverState(startRemoteTerminalVmDTO.getTerminalId());
        String isLimit = dto.getIsLimit();

        if (TerminalInstallDriverStateEnum.SUCCESS.name().equals(isLimit)) {
            return true;
        }

        if (TerminalInstallDriverStateEnum.REMOTE_EDIT_IMAGE.name().equals(isLimit)) {

            if (String.valueOf(startRemoteTerminalVmDTO.getImageId()).equals(dto.getImageId())) {
                return true;
            }
            throw new BusinessException(BusinessKey.RCDC_RCO_EDIT_IMAGE_TERMINAL_REMOTE_EDIT_IMAGE);
        }

        if (TerminalInstallDriverStateEnum.DESK_RUNNING.name().equals(isLimit)
                || TerminalInstallDriverStateEnum.DESK_STARTING.name().equals(isLimit)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EDIT_IMAGE_TERMINAL_DESK_RUNNING);
        }

        if (TerminalInstallDriverStateEnum.LOCAL_EDIT_IMAGE.name().equals(isLimit)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EDIT_IMAGE_TERMINAL_LOCAL_EDIT_IMAGE);
        }

        if (TerminalInstallDriverStateEnum.ERROR.name().equals(isLimit)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EDIT_IMAGE_TERMINAL_ERROR);
        }

        return true;
    }

    private void validTerminalDownloadImage(CbbStartRemoteTerminalVmDTO startRemoteTerminalVmDTO) throws BusinessException {
        if (startRemoteTerminalVmDTO.getCbbImageType() != CbbImageType.VOI) {
            return;
        }

        ImageDownloadStateDTO imageDownloadStateDTO = imageDownloadStateService.getByTerminalId(startRemoteTerminalVmDTO.getTerminalId());

        if (Objects.nonNull(imageDownloadStateDTO) && imageDownloadStateDTO.getDownloadState() == DownloadStateEnum.START) {
            throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_HAS_DOWNLOAD_IMAGE);
        }

    }

    private TerminalInstallDriverStateDTO getTerminalInstallDriverState(String terminalId) {

        Assert.notNull(terminalId, "terminalId is not null");
        AtomicReference<TerminalInstallDriverStateDTO> dto = new AtomicReference<>(new TerminalInstallDriverStateDTO());
        dto.get().setIsLimit(TerminalInstallDriverStateEnum.ERROR.name());

        try {
            CbbShineMessageRequest<?> request = CbbShineMessageRequest.createDefault(ShineAction.IS_LIMIT_EDIT_IMAGE, terminalId);
            CbbShineMessageResponse<?> response = messageHandlerAPI.syncRequest(request);
            Optional.ofNullable(response.getContent()).ifPresent((content) -> {
                LOGGER.info("终端{}响应状态信息 {}", terminalId, content.toString());
                dto.set(JSON.parseObject(content.toString(), TerminalInstallDriverStateDTO.class));
            });
        } catch (Exception ex) {
            LOGGER.error("查询终端{}状态信息失败, ", terminalId, ex);
        }
        return dto.get();
    }
}
