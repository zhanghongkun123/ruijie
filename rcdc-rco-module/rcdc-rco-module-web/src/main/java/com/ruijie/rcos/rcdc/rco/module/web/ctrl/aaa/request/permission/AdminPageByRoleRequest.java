package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.permission;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/20 10:27
 *
 * @author linrenjian
 */
public class AdminPageByRoleRequest extends DefaultPageRequest implements WebRequest {

    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull
    private UUID roleId;

    /**
     * 根据业务需要进行排序。如果为空，则默认按照后台createTime Desc进行排序。
     */
    @ApiModelProperty(value = "排序")
    @Nullable
    private Sort[] sortArr;

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    @Nullable
    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(@Nullable Sort[] sortArr) {
        this.sortArr = sortArr;
    }
}
