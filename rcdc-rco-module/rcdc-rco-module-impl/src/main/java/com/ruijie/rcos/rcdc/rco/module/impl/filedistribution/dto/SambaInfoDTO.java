package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description: Samba挂盘信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/23 10:32
 *
 * @author zhangyichi
 */
public class SambaInfoDTO {

    //盘符
    @JSONField(name = "disk_symbol")
    private String diskSymbol;

    //闪电云盘,
    @JSONField(name = "disk_name")
    private String diskName;

    @JSONField(name = "samba_path")
    private String sambaPath;

    @JSONField(name = "samba_port")
    private Integer sambaPort;

    @JSONField(name = "samba_user_name")
    private String sambaUserName;

    @JSONField(name = "samba_password")
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
