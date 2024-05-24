package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: Samba挂盘信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/23 10:32
 *
 * @author zhangyichi
 */
public class AppClientSambaInfoDTO {

    //盘符
    @Nullable
    @JSONField(name = "diskSymbol")
    private String diskSymbol;

    @Nullable
    @JSONField(name = "diskName")
    private String diskName;

    @NotBlank
    @JSONField(name = "sambaPath")
    private String sambaPath;

    @NotNull
    @JSONField(name = "sambaPort")
    private Integer sambaPort;

    @NotBlank
    @JSONField(name = "sambaUserName")
    private String sambaUserName;

    @NotBlank
    @JSONField(name = "sambaPassword")
    private String sambaPassword;

    public String getDiskSymbol() {
        return diskSymbol;
    }

    public void setDiskSymbol(String diskSymbol) {
        this.diskSymbol = diskSymbol;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getSambaPath() {
        return sambaPath;
    }

    public void setSambaPath(String sambaPath) {
        this.sambaPath = sambaPath;
    }

    public Integer getSambaPort() {
        return sambaPort;
    }

    public void setSambaPort(Integer sambaPort) {
        this.sambaPort = sambaPort;
    }

    public String getSambaUserName() {
        return sambaUserName;
    }

    public void setSambaUserName(String sambaUserName) {
        this.sambaUserName = sambaUserName;
    }

    public String getSambaPassword() {
        return sambaPassword;
    }

    public void setSambaPassword(String sambaPassword) {
        this.sambaPassword = sambaPassword;
    }
}
