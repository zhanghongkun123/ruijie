package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;


import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdDataTreeNodeDTO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/4
 *
 * @author hs
 */
public class GetAdMappingConfigDTO {

    private IacAdDataTreeNodeDTO[] treeNodeArr;

    public IacAdDataTreeNodeDTO[] getTreeNodeArr() {
        return this.treeNodeArr;
    }

    public void setTreeNodeArr(IacAdDataTreeNodeDTO[] treeNodeArr) {
        this.treeNodeArr = treeNodeArr;
    }
}
