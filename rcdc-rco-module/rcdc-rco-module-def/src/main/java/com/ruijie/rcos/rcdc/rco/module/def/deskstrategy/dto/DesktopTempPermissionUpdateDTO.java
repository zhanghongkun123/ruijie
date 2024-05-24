package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDesktopTempPermissionDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 编辑云桌面临时权限DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-05-08
 *
 * @author linke
 */
public class DesktopTempPermissionUpdateDTO extends DesktopTempPermissionCreateDTO {

    @NotNull
    private UUID id;

    @Nullable
    private Date updateTime;


    /**
     * USB 外设是否变化 ,默认无变化
     */
    private Boolean enableUsbChange = false;


    /**
     * USB 设备是否变化
     * 
     * @param oldPermissionDTO 旧权限参数
     */
    public void convertUsbChange(CbbDesktopTempPermissionDTO oldPermissionDTO) {
        Assert.notNull(oldPermissionDTO, "oldPermissionDTO is not null");
        // 新旧 USB 设备都为空 外设没有变化
        if (CollectionUtils.isEmpty(oldPermissionDTO.getUsbTypeIdList()) && CollectionUtils.isEmpty(this.getUsbTypeIdList())) {
            this.enableUsbChange = false;
        } else if (CollectionUtils.isEmpty(oldPermissionDTO.getUsbTypeIdList()) != CollectionUtils.isEmpty(this.getUsbTypeIdList())) {
            // 一方为空 一方不为空 则外设发生变化
            this.enableUsbChange = true;
        }

        Set<UUID> oldList = new HashSet<>(oldPermissionDTO.getUsbTypeIdList());

        Set<UUID> newList = new HashSet<>(this.getUsbTypeIdList());
        if (oldList.size() != newList.size()) {
            // 长度变化 则外设发生变化
            this.enableUsbChange = true;
        } else {
            //将旧集合 添加新集合元素
            newList.addAll(oldList);
            //如果 新旧还是不一样 ，则有变化
            if (oldList.size() != newList.size()) {
                this.enableUsbChange = true;
            } else {
                this.enableUsbChange = false;
            }
        }

    }



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nullable
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(@Nullable Date updateTime) {
        this.updateTime = updateTime;
    }


    public Boolean getEnableUsbChange() {
        return enableUsbChange;
    }

    public void setEnableUsbChange(Boolean enableUsbChange) {
        this.enableUsbChange = enableUsbChange;
    }
}
