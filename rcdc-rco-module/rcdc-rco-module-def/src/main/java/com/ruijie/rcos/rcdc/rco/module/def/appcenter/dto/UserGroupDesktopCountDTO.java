package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/25 17:00
 *
 * @author coderLee23
 */
public class UserGroupDesktopCountDTO {

    private UUID userGroupId;

    private Long notUsedDesktopCount;

    private Long usedDesktopCount;

    public UserGroupDesktopCountDTO(UUID userGroupId, Long notUsedDesktopCount, Long usedDesktopCount) {
        this.userGroupId = userGroupId;
        this.notUsedDesktopCount = notUsedDesktopCount;
        this.usedDesktopCount = usedDesktopCount;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Long getNotUsedDesktopCount() {
        return notUsedDesktopCount;
    }

    public void setNotUsedDesktopCount(Long notUsedDesktopCount) {
        this.notUsedDesktopCount = notUsedDesktopCount;
    }

    public Long getUsedDesktopCount() {
        return usedDesktopCount;
    }

    public void setUsedDesktopCount(Long usedDesktopCount) {
        this.usedDesktopCount = usedDesktopCount;
    }
}
