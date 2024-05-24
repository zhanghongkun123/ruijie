package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDiskMappingDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.ClipboardModeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUsbStorageDeviceMappingMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDesktopTempPermissionSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.service.DesktopTempPermissionService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

/**
 * Description: 剪切板配置SPI
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/17
 *
 * @author linke
 */
public class CbbDesktopTempPermissionSPIImpl implements CbbDesktopTempPermissionSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbDesktopTempPermissionSPIImpl.class);

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private CbbDesktopTempPermissionAPI cbbDesktopTempPermissionAPI;

    @Autowired
    private DesktopTempPermissionService desktopTempPermissionService;

    @Autowired
    private DesktopTempPermissionAPI desktopTempPermissionAPI;

    @Autowired
    private TerminalService terminalService;

    @Override
    public ClipboardModeDTO getClipBoardConfig(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId must not be null");
        return buildClipboardModeDTO(getTempPermissionByFunction(desktopId, CbbDesktopTempPermissionDTO::getClipBoardMode));
    }

    @Override
    public ClipboardModeDTO getClipBoardConfig(String terminalId, UUID desktopId) {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(desktopId, "desktopId must not be null");

        UUID userId = getUserIdByTerminalId(terminalId);
        if (Objects.isNull(userId)) {
            LOGGER.warn("终端[{}]无用户信息", terminalId);
            return getClipBoardConfig(desktopId);
        }
        return buildClipboardModeDTO(getTempPermissionByFunction(desktopId, userId, CbbDesktopTempPermissionDTO::getClipBoardMode));
    }

    private ClipboardModeDTO buildClipboardModeDTO(CbbDesktopTempPermissionDTO tempPermissionDTO) {
        if (Objects.isNull(tempPermissionDTO)) {
            // 未启用功能，返回null
            return null;
        }
        ClipboardModeDTO clipboardModeDTO = new ClipboardModeDTO();
        clipboardModeDTO.setClipBoardMode(tempPermissionDTO.getClipBoardMode());
        clipboardModeDTO.setClipBoardSupportTypeArr(tempPermissionDTO.getClipBoardSupportTypeArr());
        return clipboardModeDTO;
    }

    @Override
    public CbbDiskMappingDTO getDiskMappingConfig(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId must not be null");
        return buildDiskMappingConfig(getTempPermissionByFunction(desktopId, CbbDesktopTempPermissionDTO::getId));
    }

    @Override
    public CbbDiskMappingDTO getDiskMappingConfig(String terminalId, UUID desktopId) {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(desktopId, "desktopId must not be null");

        UUID userId = getUserIdByTerminalId(terminalId);
        if (Objects.isNull(userId)) {
            LOGGER.warn("终端[{}]无用户信息", terminalId);
            return getDiskMappingConfig(desktopId);
        }

        return buildDiskMappingConfig(getTempPermissionByFunction(desktopId, userId, CbbDesktopTempPermissionDTO::getId));
    }

    private CbbDiskMappingDTO buildDiskMappingConfig(CbbDesktopTempPermissionDTO tempPermissionDTO) {
        if (Objects.isNull(tempPermissionDTO)) {
            // 未启用功能，返回null
            return null;
        }
        CbbDiskMappingDTO cbbDiskMappingDTO = new CbbDiskMappingDTO();
        if (Objects.nonNull(tempPermissionDTO.getEnableDiskMapping())) {
            cbbDiskMappingDTO.setEnableDiskMapping(tempPermissionDTO.getEnableDiskMapping());
            cbbDiskMappingDTO.setEnableDiskMappingWriteable(tempPermissionDTO.getEnableDiskMappingWriteable());
            //为临时权限的磁盘映射 受到临时权限影响 不显示GT的弹出
            cbbDiskMappingDTO.setEnableMessageDisplay(Boolean.FALSE);
        }
        // USB存储设备
        CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode = tempPermissionDTO.getUsbStorageDeviceMappingMode();
        if (Objects.nonNull(usbStorageDeviceMappingMode)) {
            usbStorageDeviceMappingMode.convertToDiskMapping(cbbDiskMappingDTO);
            //为临时权限的磁盘映射 受到临时权限影响 不显示GT的弹出
            cbbDiskMappingDTO.setEnableMessageDisplay(Boolean.FALSE);
        }
        return cbbDiskMappingDTO;
    }

    @Override
    public List<UUID> getUsbTypeIdList(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId must not be null");

        return buildUsbTypeIdList(getTempPermissionByFunction(desktopId, CbbDesktopTempPermissionDTO::getUsbTypeIdList));
    }

    @Override
    public List<UUID> getUsbTypeIdList(String terminalId, UUID desktopId) {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(desktopId, "desktopId must not be null");

        UUID userId = getUserIdByTerminalId(terminalId);
        if (Objects.isNull(userId)) {
            LOGGER.warn("终端[{}]无用户信息", terminalId);
            return getUsbTypeIdList(desktopId);
        }

        return buildUsbTypeIdList(getTempPermissionByFunction(desktopId, userId, CbbDesktopTempPermissionDTO::getUsbTypeIdList));
    }

    private List<UUID> buildUsbTypeIdList(CbbDesktopTempPermissionDTO tempPermissionDTO) {
        if (Objects.isNull(tempPermissionDTO) || Objects.isNull(tempPermissionDTO.getUsbTypeIdList())) {
            // 未启用功能，返回空的
            return new ArrayList<>();
        }

        return new ArrayList<>(tempPermissionDTO.getUsbTypeIdList());
    }

    private UUID getUserIdByTerminalId(String terminalId) {
        ViewTerminalEntity viewTerminalEntity;
        try {
            viewTerminalEntity = terminalService.getViewByTerminalId(terminalId);
        } catch (Exception e) {
            LOGGER.error("获取终端[{}]信息失败", terminalId, e);
            // null
            return null;
        }
        return viewTerminalEntity.getUserId();
    }

    private CbbDesktopTempPermissionDTO getTempPermissionByFunction(UUID desktopId, Function<CbbDesktopTempPermissionDTO, ?> enableFun) {
        return getTempPermissionByFunction(desktopId, null, enableFun);
    }

    private CbbDesktopTempPermissionDTO getTempPermissionByFunction(UUID desktopId, UUID userId, Function<CbbDesktopTempPermissionDTO, ?> enableFun) {
        List<UUID> effectIdList = desktopTempPermissionService.listInEffectPermissionId(desktopId, DesktopTempPermissionRelatedType.DESKTOP);
        CbbDesktopTempPermissionDTO tempPermissionDTO;
        if (CollectionUtils.isNotEmpty(effectIdList)) {
            try {
                tempPermissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(effectIdList.get(0));
            } catch (BusinessException e) {
                LOGGER.error("查询临时权限[{}]异常", effectIdList.get(0), e);
                // null
                return null;
            }
            if (Objects.nonNull(enableFun.apply(tempPermissionDTO))) {
                return tempPermissionDTO;
            }
        }

        if (Objects.isNull(userId)) {
            UserDesktopEntity userDesktop = userDesktopService.findByDeskId(desktopId);
            userId = Objects.isNull(userDesktop) ? null : userDesktop.getUserId();
        }
        if (Objects.isNull(userId)) {
            // null
            return null;
        }

        effectIdList = desktopTempPermissionService.listInEffectPermissionId(userId, DesktopTempPermissionRelatedType.USER);
        if (CollectionUtils.isEmpty(effectIdList)) {
            // null
            return null;
        }

        try {
            tempPermissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(effectIdList.get(0));
        } catch (BusinessException e) {
            LOGGER.error("查询临时权限[{}]异常", effectIdList.get(0), e);
            // null
            return null;
        }
        // 未启用功能，返回null
        return Objects.nonNull(enableFun.apply(tempPermissionDTO)) ? tempPermissionDTO : null;
    }
}
