package com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/14 11:32
 *
 * @author ketb
 */
public class PromptVersionDTO {

    @JSONField(name = "versions")
    private List<PromptVersionInfoDTO> versionList;

    public List<PromptVersionInfoDTO> getVersionList() {
        return versionList;
    }

    public void setVersionList(List<PromptVersionInfoDTO> versionList) {
        this.versionList = versionList;
    }
}
