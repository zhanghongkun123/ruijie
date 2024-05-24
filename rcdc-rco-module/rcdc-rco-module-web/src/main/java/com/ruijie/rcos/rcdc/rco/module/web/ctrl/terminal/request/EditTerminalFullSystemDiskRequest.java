package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 编辑终端是否开启系统盘自动扩容请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/27 12:25
 *
 * @author yxq
 */
@ApiModel("编辑终端是否开启系统盘自动扩容请求")
public class EditTerminalFullSystemDiskRequest {

    @ApiModelProperty(value = "ID数组", required = true)
    @NotEmpty
    @Size(min = 1)
    private String[] idArr;

    @ApiModelProperty(value = "是否强制删除本地盘", required = true)
    @NotNull
    private Boolean forceDeleteLocalDisk;

    @ApiModelProperty("是否开启自动扩容")
    @NotNull
    private Boolean enableFullSystemDisk;

    public String[] getIdArr() {
        return idArr;
    }

    public void setIdArr(String[] idArr) {
        this.idArr = idArr;
    }

    public Boolean getForceDeleteLocalDisk() {
        return forceDeleteLocalDisk;
    }

    public void setForceDeleteLocalDisk(Boolean forceDeleteLocalDisk) {
        this.forceDeleteLocalDisk = forceDeleteLocalDisk;
    }

    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }
}
