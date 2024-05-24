package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageHealthStateEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageProtocolTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月23日
 *
 * @author zhanghongkun
 */
@ApiModel("外置存储响应体")
public class ExternalStorageVO implements Serializable {

    @ApiModelProperty(value = "外置存储Id")
    private UUID id;

    @ApiModelProperty(value = "外置存储名称")
    private String name;

    /**
     * 允许值: "HEALTH", "WARNING", "CREATING", "TROUBLE", "UNKNOWN"
     */
    @ApiModelProperty(value = "外置存储状态")
    private ExternalStorageHealthStateEnum state;

    @ApiModelProperty(value = "总容量")
    private Long totalCapacity;

    @ApiModelProperty(value = "已使用容量")
    private Long usedCapacity;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "外置存储类型")
    private ExternalStorageProtocolTypeEnum protocolType;

    public ExternalStorageProtocolTypeEnum getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ExternalStorageProtocolTypeEnum protocolType) {
        this.protocolType = protocolType;
    }

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

    public ExternalStorageHealthStateEnum getState() {
        return state;
    }

    public void setState(ExternalStorageHealthStateEnum state) {
        this.state = state;
    }

    public Long getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Long totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Long getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(Long usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
