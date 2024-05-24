package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.request;

import com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums.ExpandObjectTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.UUID;

/**
 * Description: 扩容磁盘请求类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13
 *
 * @author TD
 */
public class ExpandDiskWebRequest implements WebRequest {

    @NotNull
    @Range(min = "20", max = "2048")
    @ApiModelProperty(value = "扩容目标大小", required = true)
    private Integer expandCapacity;

    @NotNull
    @ApiModelProperty(value = "扩容类型", required = true)
    private ExpandObjectTypeEnum type;

    /**
     * 磁盘池
     */
    @Nullable
    @ApiModelProperty(value = "磁盘池ID")
    private UUID diskPoolId;

    /**
     * 用户组ID集合
     */
    @Nullable
    @ApiModelProperty(value = "用户组ID集合")
    private UUID[] groupIdArr;

    /**
     * 用户ID集合
     */
    @Nullable
    @ApiModelProperty(value = "用户ID集合")
    private UUID[] userIdArr;

    /**
     * 磁盘ID集合
     */
    @Nullable
    @ApiModelProperty(value = "磁盘ID集合")
    private UUID[] diskIdArr;

    public Integer getExpandCapacity() {
        return expandCapacity;
    }

    public void setExpandCapacity(Integer expandCapacity) {
        this.expandCapacity = expandCapacity;
    }

    public ExpandObjectTypeEnum getType() {
        return type;
    }

    public void setType(ExpandObjectTypeEnum type) {
        this.type = type;
    }

    @Nullable
    public UUID getDiskPoolId() {
        return diskPoolId;
    }

    public void setDiskPoolId(@Nullable UUID diskPoolId) {
        this.diskPoolId = diskPoolId;
    }

    @Nullable
    public UUID[] getGroupIdArr() {
        return groupIdArr;
    }

    public void setGroupIdArr(@Nullable UUID[] groupIdArr) {
        this.groupIdArr = groupIdArr;
    }

    @Nullable
    public UUID[] getUserIdArr() {
        return userIdArr;
    }

    public void setUserIdArr(@Nullable UUID[] userIdArr) {
        this.userIdArr = userIdArr;
    }

    @Nullable
    public UUID[] getDiskIdArr() {
        return diskIdArr;
    }

    public void setDiskIdArr(@Nullable UUID[] diskIdArr) {
        this.diskIdArr = diskIdArr;
    }

    @Override
    public String toString() {
        return "ExpandDiskWebRequest{" +
                "expandCapacity=" + expandCapacity +
                ", type=" + type +
                ", diskPoolId=" + diskPoolId +
                ", groupIdArr=" + Arrays.toString(groupIdArr) +
                ", userIdArr=" + Arrays.toString(userIdArr) +
                ", diskIdArr=" + Arrays.toString(diskIdArr) +
                '}';
    }
}
