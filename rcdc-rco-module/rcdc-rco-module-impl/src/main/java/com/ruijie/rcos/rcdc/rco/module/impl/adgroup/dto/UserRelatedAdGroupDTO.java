package com.ruijie.rcos.rcdc.rco.module.impl.adgroup.dto;

/**
 * Description: 用户所属安全组DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/10
 *
 * @author TD
 */
public class UserRelatedAdGroupDTO {

    private String dn;

    private String objectGuid;

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getObjectGuid() {
        return objectGuid;
    }

    public void setObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
    }
}
