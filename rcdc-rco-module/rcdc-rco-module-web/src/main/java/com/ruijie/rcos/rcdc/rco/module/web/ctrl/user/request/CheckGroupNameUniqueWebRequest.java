package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.UUID;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 检验终端分组名称是否同级唯一请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 * 
 * @author nt
 */
public class CheckGroupNameUniqueWebRequest implements WebRequest {

    /**
     * 分组id
     */
    @Nullable
    private UUID id;
    
    /**
     * 分组名称
     */
    @NotBlank
    @TextShort
    private String groupName;
    
    /**
     * 父级分组
     */
    @Nullable
    private UUID parentGroupId;

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

    public UUID getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(UUID parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

}
