package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

/**
 * 
 * Description: 删除终端分组请求参数
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年04月23日
 * 
 * @author nt
 */
public class DelUserGroupWebRequest implements WebRequest {

    /**
     * 分组id数组集合
     */
    @NotEmpty
    private UUID[] idArr;

    @Nullable
    private UUID moveGroupId;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    public UUID getMoveGroupId() {
        return moveGroupId;
    }

    public void setMoveGroupId(UUID moveGroupId) {
        this.moveGroupId = moveGroupId;
    }
}
