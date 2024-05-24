package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/10
 *
 * @author wuShengQiang
 */
@ApiModel("云桌面备份名称是否重复请求体")
public class CheckBackupNameDuplicationRequest implements WebRequest {

    @ApiModelProperty(value = "备份名称，通用名称格式", required = true)
    @NotBlank
    @Size(min = 1, max = 64)
    @TextName
    private String name;

    @ApiModelProperty(value = "云桌面ID", required = true)
    @NotNull
    private UUID deskId;

    @ApiModelProperty(value = "云桌面备份ID")
    @Nullable
    private UUID deskBackupId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    @Nullable
    public UUID getDeskBackupId() {
        return deskBackupId;
    }

    public void setDeskBackupId(@Nullable UUID deskBackupId) {
        this.deskBackupId = deskBackupId;
    }
}
