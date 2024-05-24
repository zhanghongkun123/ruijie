package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import java.util.UUID;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 删除终端分组请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 * 
 * @author nt
 */
public class DelTerminalGroupWebRequest implements WebRequest {

    /**
     * 分组id数组集合
     */
    @NotEmpty
    @ApiModelProperty(value = "删除ID集合", required = true)
    private UUID[] idArr;

    @Nullable
    @ApiModelProperty("移动分组ID")
    private UUID moveGroupId;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    @Nullable
    public UUID getMoveGroupId() {
        return moveGroupId;
    }

    public void setMoveGroupId(@Nullable UUID moveGroupId) {
        this.moveGroupId = moveGroupId;
    }
}
