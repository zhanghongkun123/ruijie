package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.TextName;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: 修改分组信息请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 *
 * @author nt
 */
public class EditTerminalGroupWebRequest implements WebRequest {

    /**
     * 终端分组id
     */
    @NotNull
    @ApiModelProperty(value = "终端分组ID",required = true)
    private UUID id;

    /**
     * 分组名称
     */
    @NotBlank
    @TextShort
    @TextName
    @ApiModelProperty(value = "分组名称",required = true)
    private String groupName;

    /**
     * 父级分组id
     */
    @Nullable
    @ApiModelProperty(value = "父级分组id",required = true)
    private IdLabelEntry parentGroup;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public IdLabelEntry getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(IdLabelEntry parentGroup) {
        this.parentGroup = parentGroup;
    }

    @Override
    public String toString() {
        return "EditTerminalGroupWebRequest [id=" + id + ", groupName=" + groupName + ", parentGroup=" + parentGroup + "]";
    }

}
