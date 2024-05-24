package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.TextName;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * 
 * Description: 创建终端分组前端 请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 * 
 * @author nt
 */
public class CreateTerminalGroupWebRequest implements WebRequest {

    /**
     * 分组名称
     */
    @NotBlank
    @TextShort
    @TextName
    @ApiModelProperty(value = "终端分组名称")
    private String groupName;
    
    /**
     * 父级分组id
     */
    @Nullable
    private IdLabelEntry parentGroup;

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

}
