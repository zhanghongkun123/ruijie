package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import java.util.UUID;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.IdLabelStringEntry;

/**
 * 
 * Description: 终端分组详情VO
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 * 
 * @author nt
 */
public class TerminalGroupDetailVO {
    

    private UUID id; 

    private String groupName;
    
    /**
     * 父级分组
     */
    private IdLabelStringEntry parentGroup;
    

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

    public IdLabelStringEntry getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(IdLabelStringEntry parentGroup) {
        this.parentGroup = parentGroup;
    }

}
