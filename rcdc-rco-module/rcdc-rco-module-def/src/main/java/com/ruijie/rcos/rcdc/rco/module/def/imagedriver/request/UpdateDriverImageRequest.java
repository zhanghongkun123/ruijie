package com.ruijie.rcos.rcdc.rco.module.def.imagedriver.request;

import java.util.Date;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.annotation.TextName;

/**
 * <br>
 * Description: 终端列表查询请求 <br>
 * Copyright: Copyright (c) 2022 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2022/07/13 <br>
 *
 * @author ypp
 */

public class UpdateDriverImageRequest {

    @NotNull
    private UUID id;

    @TextMedium
    private String note;


    /**
     * 驱动程序类型
     */
    @Nullable
    private String driverProgramType;

    /**
     * 驱动厂商
     */
    @Nullable
    @Size(max = 512)
    private String provider;

    /**
     * 硬件标识，多个以","分隔
     */
    @Nullable
    private String[] hardwareIdArr;

    /**
     * 操作系统，多个以","分隔
     */
    @Nullable
    private String[] operatingSystemArr;


    @Nullable
    @Size(max = 512)
    private String driverVersion;

    /**
     * 发布时间
     */
    @Nullable
    private Date releaseTime;

    @Nullable
    @TextName
    @Size(max = 64)
    private String driverName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Nullable
    public String getDriverProgramType() {
        return driverProgramType;
    }

    public void setDriverProgramType(@Nullable String driverProgramType) {
        this.driverProgramType = driverProgramType;
    }

    @Nullable
    public String[] getHardwareIdArr() {
        return hardwareIdArr;
    }

    public void setHardwareIdArr(@Nullable String[] hardwareIdArr) {
        this.hardwareIdArr = hardwareIdArr;
    }

    @Nullable
    public String[] getOperatingSystemArr() {
        return operatingSystemArr;
    }

    public void setOperatingSystemArr(@Nullable String[] operatingSystemArr) {
        this.operatingSystemArr = operatingSystemArr;
    }

    @Nullable
    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(@Nullable String driverVersion) {
        this.driverVersion = driverVersion;
    }

    @Nullable
    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(@Nullable Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Nullable
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(@Nullable String driverName) {
        this.driverName = driverName;
    }


    @Nullable
    public String getProvider() {
        return provider;
    }

    public void setProvider(@Nullable String provider) {
        this.provider = provider;
    }
}
