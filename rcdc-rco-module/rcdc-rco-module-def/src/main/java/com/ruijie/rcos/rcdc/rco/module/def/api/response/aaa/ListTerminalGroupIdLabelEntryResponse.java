package com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月13日
 *
 * @author xiejian
 */
public class ListTerminalGroupIdLabelEntryResponse extends DefaultResponse {

    private List<GroupIdLabelEntry> terminalGroupIdLabelEntryList;

    public List<GroupIdLabelEntry> getTerminalGroupIdLabelEntryList() {
        return terminalGroupIdLabelEntryList;
    }

    public void setTerminalGroupIdLabelEntryList(
        List<GroupIdLabelEntry> terminalGroupIdLabelEntryList) {
        this.terminalGroupIdLabelEntryList = terminalGroupIdLabelEntryList;
    }
}
