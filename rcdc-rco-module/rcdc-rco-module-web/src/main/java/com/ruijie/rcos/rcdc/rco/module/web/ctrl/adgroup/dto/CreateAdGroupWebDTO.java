package com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 创建Ad域组请求体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-09-26
 *
 * @author zqj
 */
public class CreateAdGroupWebDTO implements WebRequest {


    @NotBlank
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @Nullable
    @ApiModelProperty(value = "email")
    private String email;

    @NotBlank
    @ApiModelProperty(value = "域", required = true)
    private String domain;

    @Nullable
    @ApiModelProperty(value = "描述")
    private String remark;

    @Nullable
    @ApiModelProperty(value = "所在文件夹")
    private String ou;

    @NotBlank
    @ApiModelProperty(value = "objectGuid", required = true)
    private String objectGuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public String getObjectGuid() {
        return objectGuid;
    }

    public void setObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
    }
}
