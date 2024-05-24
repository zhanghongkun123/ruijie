package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;

/**
 *
 * Description: 池桌面详细页面DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月12日
 *
 * @author linke
 */
public class DesktopPoolDetailDTO extends DesktopPoolDTO {

    /**
     * 运行中数量
     */
    private Integer running = 0;

    /**
     * 未分配数量
     */
    private Integer free = 0;

    /**
     * 关机数量
     */
    private Integer close = 0;

    /**
     * 报障数量
     */
    private Integer fault = 0;

    /**
     * 关联的用户数量
     */
    private Integer bindUserNum = 0;

    private Boolean enableOpenDesktopRedirect;

    private ExtraDiskDTO[] extraDiskArr;

    public Integer getRunning() {
        return running;
    }

    public void setRunning(Integer running) {
        this.running = running;
    }

    public Integer getFree() {
        return free;
    }

    public void setFree(Integer free) {
        this.free = free;
    }

    public Integer getClose() {
        return close;
    }

    public void setClose(Integer close) {
        this.close = close;
    }

    public Integer getFault() {
        return fault;
    }

    public void setFault(Integer fault) {
        this.fault = fault;
    }

    public Integer getBindUserNum() {
        return bindUserNum;
    }

    public void setBindUserNum(Integer bindUserNum) {
        this.bindUserNum = bindUserNum;
    }

    public Boolean getEnableOpenDesktopRedirect() {
        return enableOpenDesktopRedirect;
    }

    public void setEnableOpenDesktopRedirect(Boolean enableOpenDesktopRedirect) {
        this.enableOpenDesktopRedirect = enableOpenDesktopRedirect;
    }

    public ExtraDiskDTO[] getExtraDiskArr() {
        return extraDiskArr;
    }

    public void setExtraDiskArr(ExtraDiskDTO[] extraDiskArr) {
        this.extraDiskArr = extraDiskArr;
    }
}
