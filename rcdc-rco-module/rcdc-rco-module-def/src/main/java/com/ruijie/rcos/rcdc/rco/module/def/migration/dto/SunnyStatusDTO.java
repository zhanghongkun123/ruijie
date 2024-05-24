package com.ruijie.rcos.rcdc.rco.module.def.migration.dto;


 /**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/1
 *
 * @author chenl
 */
public class SunnyStatusDTO {

    /**
     * true/false,   (sunny安装结果)
     */
    private Boolean sunnyInstall;

    /**
     * true/false,  （是否需要安装sunny）
     */
    private Boolean needInstallSunny;

    /**
     * "1.0.0, （gt版本）,
     */
    private String guesttoolVersion;

    /**
     * true/false (scsi控制器安装结果)
     */
    private Boolean scsiControllerInstall;

    public Boolean getSunnyInstall() {
        return sunnyInstall;
    }

    public void setSunnyInstall(Boolean sunnyInstall) {
        this.sunnyInstall = sunnyInstall;
    }

    public Boolean getNeedInstallSunny() {
        return needInstallSunny;
    }

    public void setNeedInstallSunny(Boolean needInstallSunny) {
        this.needInstallSunny = needInstallSunny;
    }

    public String getGuesttoolVersion() {
        return guesttoolVersion;
    }

    public void setGuesttoolVersion(String guesttoolVersion) {
        this.guesttoolVersion = guesttoolVersion;
    }

    public Boolean getScsiControllerInstall() {
        return scsiControllerInstall;
    }

    public void setScsiControllerInstall(Boolean scsiControllerInstall) {
        this.scsiControllerInstall = scsiControllerInstall;
    }
}
