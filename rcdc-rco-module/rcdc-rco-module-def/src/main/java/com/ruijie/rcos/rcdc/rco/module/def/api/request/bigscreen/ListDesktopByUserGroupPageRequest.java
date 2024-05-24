package com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import java.util.UUID;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/29 14:43
 *
 * @author zhangyichi
 */
public class ListDesktopByUserGroupPageRequest extends PageSearchRequest {

    @Nullable
    private UUID groupId;

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }
}
