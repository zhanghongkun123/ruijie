package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUsbStorageDeviceMappingMode;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbClipBoardSupportTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbClipboardMode;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 创建云桌面临时权限Request
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-04-26
 *
 * @author linke
 */
public class CreateDesktopTempPermissionRequest implements WebRequest {

    @ApiModelProperty(value = "临时权限名称", required = true)
    @NotBlank
    @TextShort
    private String name;

    @ApiModelProperty(value = "生效开始时间", required = true)
    @NotNull
    private Date startTime;

    @ApiModelProperty(value = "生效结束时间", required = true)
    @NotNull
    private Date endTime;

    @ApiModelProperty(value = "开通原因", required = true)
    @TextMedium
    private String reason;

    @ApiModelProperty(value = "剪切板", required = true)
    @Nullable
    private CbbClipboardMode clipBoardMode;

    @ApiModelProperty(value = "支持传输的类型")
    @Nullable
    @Size(min = 1, max = 2)
    private CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr;

    @ApiModelProperty("磁盘映射枚举，关闭、可读、读写")
    @Nullable
    private DiskMappingEnum diskMappingType;

    @ApiModelProperty(value = "USB存储设备映射")
    @Nullable
    private CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode;

    @ApiModelProperty(value = "外设策略", required = true)
    @Nullable
    private List<UUID> usbTypeIdList;

    @ApiModelProperty(value = "选择的桌面ID列表")
    @Nullable
    private List<UUID> desktopIdList;

    @ApiModelProperty(value = "选择的用户ID列表")
    @Nullable
    private List<UUID> userIdList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Nullable
    public CbbClipboardMode getClipBoardMode() {
        return clipBoardMode;
    }

    public void setClipBoardMode(@Nullable CbbClipboardMode clipBoardMode) {
        this.clipBoardMode = clipBoardMode;
    }

    @Nullable
    public CbbClipBoardSupportTypeDTO[] getClipBoardSupportTypeArr() {
        return clipBoardSupportTypeArr;
    }

    public void setClipBoardSupportTypeArr(@Nullable CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr) {
        this.clipBoardSupportTypeArr = clipBoardSupportTypeArr;
    }

    @Nullable
    public DiskMappingEnum getDiskMappingType() {
        return diskMappingType;
    }

    public void setDiskMappingType(@Nullable DiskMappingEnum diskMappingType) {
        this.diskMappingType = diskMappingType;
    }
    
    @Nullable
    public CbbUsbStorageDeviceMappingMode getUsbStorageDeviceMappingMode() {
        return usbStorageDeviceMappingMode;
    }

    public void setUsbStorageDeviceMappingMode(@Nullable CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode) {
        this.usbStorageDeviceMappingMode = usbStorageDeviceMappingMode;
    }

    @Nullable
    public List<UUID> getUsbTypeIdList() {
        return usbTypeIdList;
    }

    public void setUsbTypeIdList(@Nullable List<UUID> usbTypeIdList) {
        this.usbTypeIdList = usbTypeIdList;
    }

    @Nullable
    public List<UUID> getDesktopIdList() {
        return desktopIdList;
    }

    public void setDesktopIdList(@Nullable List<UUID> desktopIdList) {
        this.desktopIdList = desktopIdList;
    }

    @Nullable
    public List<UUID> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(@Nullable List<UUID> userIdList) {
        this.userIdList = userIdList;
    }
}
