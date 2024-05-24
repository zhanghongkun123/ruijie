package com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa;

import java.util.List;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月13日
 *
 * @author linrenjian
 */
public class ListImageIdLabelEntryResponse extends DefaultResponse {

    private List<GroupIdLabelEntry> imageIdLabelEntryList;

    public List<GroupIdLabelEntry> getImageIdLabelEntryList() {
        return imageIdLabelEntryList;
    }

    public void setImageIdLabelEntryList(List<GroupIdLabelEntry> imageIdLabelEntryList) {
        this.imageIdLabelEntryList = imageIdLabelEntryList;
    }
}
