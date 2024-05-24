package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.cabinet;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import java.util.UUID;

/**
 * Description: 编辑机柜提交的数据
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/24 15:16
 *
 * @author BaiGuoliang
 */
public class EditCabinetWebRequest extends CreateCabinetWebRequest {

    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
