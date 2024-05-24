package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.TextMedium;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/20
 *
 * @author Jarman
 */
public class CheckDuplicationGroupNameWebRequest implements WebRequest {

    @Nullable
    private UUID parent;

    @NotBlank
    @TextMedium
    @TextName
    private String groupName;

    @Nullable
    private UUID id;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Nullable
    public UUID getParent() {
        return parent;
    }

    public void setParent(@Nullable UUID parent) {
        this.parent = parent;
    }

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }
}
