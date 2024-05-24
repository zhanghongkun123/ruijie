package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen;

import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import java.util.UUID;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/29 14:50
 *
 * @author zhangyichi
 */
public class ListDesktopByUserGroupPageWebRequest extends PageWebRequest {

    @Nullable
    private UUID groupId;

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }
}
