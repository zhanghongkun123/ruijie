package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbLocalExternalStorageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户配置策略DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/14
 *
 * @author WuShengQiang
 */
public class UserProfileStrategyDTO {

    /**
     * 策略ID
     */
    private UUID id;

    /**
     * 策略名称
     */
    private String name;

    /**
     * 存储位置(本地/UNC路径)
     */
    private UserProfileStrategyStorageTypeEnum storageType;

    /**
     * 磁盘路径
     */
    private String diskPath;

    /**
     * 磁盘容量 单位:GB
     */
    private Integer diskSize;

    /**
     * 策略关联的路径对象列表
     */
    private UserProfileStrategyRelatedDTO[] pathArr;

    /**
     * 策略路径数量
     */
    private Long count;

    /**
     * 关联云桌面数量
     */
    private Long deskCount;

    /**
     * 描述
     **/
    private String description;

    /**
     * 创建人
     */
    private String creatorUserName;

    private Date createTime;

    /**
     * 默认true:可用 false:不可用
     */
    private Boolean canUsed = true;

    /**
     * 不能使用提示消息
     */
    private String canUsedMessage;

    /**
     * 文件服务器id
     */
    @Nullable
    private UUID externalStorageId;

    /**
     * 外置存储-文件服务器信息
     */
    private CbbLocalExternalStorageDTO externalStorageDTO;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserProfileStrategyStorageTypeEnum getStorageType() {
        return storageType;
    }

    public void setStorageType(UserProfileStrategyStorageTypeEnum storageType) {
        this.storageType = storageType;
    }

    public String getDiskPath() {
        return diskPath;
    }

    public void setDiskPath(String diskPath) {
        this.diskPath = diskPath;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    public UserProfileStrategyRelatedDTO[] getPathArr() {
        return pathArr;
    }

    public void setPathArr(UserProfileStrategyRelatedDTO[] pathArr) {
        this.pathArr = pathArr;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getDeskCount() {
        return deskCount;
    }

    public void setDeskCount(Long deskCount) {
        this.deskCount = deskCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getCanUsed() {
        return canUsed;
    }

    public void setCanUsed(Boolean canUsed) {
        this.canUsed = canUsed;
    }

    public String getCanUsedMessage() {
        return canUsedMessage;
    }

    public void setCanUsedMessage(String canUsedMessage) {
        this.canUsedMessage = canUsedMessage;
    }

    @Nullable
    public UUID getExternalStorageId() {
        return externalStorageId;
    }

    public void setExternalStorageId(@Nullable UUID externalStorageId) {
        this.externalStorageId = externalStorageId;
    }

    public CbbLocalExternalStorageDTO getExternalStorageDTO() {
        return externalStorageDTO;
    }

    public void setExternalStorageDTO(CbbLocalExternalStorageDTO externalStorageDTO) {
        this.externalStorageDTO = externalStorageDTO;
    }
}