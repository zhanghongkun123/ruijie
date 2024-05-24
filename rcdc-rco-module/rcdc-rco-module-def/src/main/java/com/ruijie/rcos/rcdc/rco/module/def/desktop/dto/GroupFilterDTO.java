package com.ruijie.rcos.rcdc.rco.module.def.desktop.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserGroupVO;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/18
 *
 * @author songxiang
 */
public class GroupFilterDTO {


    private List<UserGroupVO> userGroupVOList;

    private UUID filterGroupId;

    private Boolean enableFilterAdGroup;

    private Boolean enableFilterLdapGroup;

    private Boolean enableFilterDefaultGroup;

    private Boolean enableFilterThirdPartyGroup;

    public List<UserGroupVO> getUserGroupVOList() {
        return userGroupVOList;
    }

    public void setUserGroupVOList(List<UserGroupVO> userGroupVOList) {
        this.userGroupVOList = userGroupVOList;
    }

    public UUID getFilterGroupId() {
        return filterGroupId;
    }

    public void setFilterGroupId(UUID filterGroupId) {
        this.filterGroupId = filterGroupId;
    }

    public Boolean getEnableFilterAdGroup() {
        return enableFilterAdGroup;
    }

    public void setEnableFilterAdGroup(Boolean enableFilterAdGroup) {
        this.enableFilterAdGroup = enableFilterAdGroup;
    }

    public Boolean getEnableFilterLdapGroup() {
        return enableFilterLdapGroup;
    }

    public void setEnableFilterLdapGroup(Boolean enableFilterLdapGroup) {
        this.enableFilterLdapGroup = enableFilterLdapGroup;
    }

    public Boolean getEnableFilterDefaultGroup() {
        return enableFilterDefaultGroup;
    }

    public void setEnableFilterDefaultGroup(Boolean enableFilterDefaultGroup) {
        this.enableFilterDefaultGroup = enableFilterDefaultGroup;
    }

    public Boolean getEnableFilterThirdPartyGroup() {
        return enableFilterThirdPartyGroup;
    }

    public void setEnableFilterThirdPartyGroup(Boolean enableFilterThirdPartyGroup) {
        this.enableFilterThirdPartyGroup = enableFilterThirdPartyGroup;
    }
}
