package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 清理配置数据的请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/27
 *
 * @author zwf
 */
public class CleanUserProfilePathInfoRequest {
    @NotNull
    private UUID id;

    @NotNull
    private Boolean isAllClean;

    @Nullable
    @Size
    private UUID[] cloudDesktopIdArr;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getIsAllClean() {
        return isAllClean;
    }

    public void setIsAllClean(Boolean isAllClean) {
        this.isAllClean = isAllClean;
    }

    @Nullable
    public UUID[] getCloudDesktopIdArr() {
        return cloudDesktopIdArr;
    }

    public void setCloudDesktopIdArr(@Nullable UUID[] cloudDesktopIdArr) {
        this.cloudDesktopIdArr = cloudDesktopIdArr;
    }
}
