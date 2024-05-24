package com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa;

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
public class ListTerminalGroupIdResponse extends DefaultResponse {

    private List<String> terminalGroupIdList;

    public List<String> getTerminalGroupIdList() {
        return terminalGroupIdList;
    }

    public void setTerminalGroupIdList(List<String> terminalGroupIdList) {
        this.terminalGroupIdList = terminalGroupIdList;
    }
}
