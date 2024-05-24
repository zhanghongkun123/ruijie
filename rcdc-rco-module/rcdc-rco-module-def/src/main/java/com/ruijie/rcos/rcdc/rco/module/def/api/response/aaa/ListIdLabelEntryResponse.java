package com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.util.List;

/**
 * Description: IdLabelEntry通用响应体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/18
 *
 * @author WuShengQiang
 */
public class ListIdLabelEntryResponse extends DefaultResponse {

    private List<GroupIdLabelEntry> idLabelEntryList;

    public List<GroupIdLabelEntry> getIdLabelEntryList() {
        return idLabelEntryList;
    }

    public void setIdLabelEntryList(List<GroupIdLabelEntry> idLabelEntryList) {
        this.idLabelEntryList = idLabelEntryList;
    }
}
