package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.List;
import java.util.UUID;

/**
 * Description: 获取异常云桌面API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/23 9:45
 *
 * @author zhangyichi
 */
public class AbnormalDesktopListResponse extends DefaultResponse {

    private List<UUID> idList;

    private Integer total;

    public List<UUID> getIdList() {
        return idList;
    }

    public void setIdList(List<UUID> idList) {
        this.idList = idList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
