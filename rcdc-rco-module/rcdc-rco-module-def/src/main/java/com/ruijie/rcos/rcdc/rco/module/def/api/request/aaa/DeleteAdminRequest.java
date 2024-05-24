package com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoAdminDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月07日
 *
 * @author xiejian
 */
public class DeleteAdminRequest {

    @Size(min = 1)
    @NotNull
    private RcoAdminDTO[] adminIdArr;

    public RcoAdminDTO[] getAdminIdArr() {
        return adminIdArr;
    }

    public void setAdminIdArr(RcoAdminDTO[] adminIdArr) {
        this.adminIdArr = adminIdArr;
    }
}
