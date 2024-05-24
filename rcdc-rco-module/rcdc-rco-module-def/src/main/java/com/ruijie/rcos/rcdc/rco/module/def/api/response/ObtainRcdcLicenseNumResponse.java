package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/26
 *
 * @author nt
 */
public class ObtainRcdcLicenseNumResponse extends DefaultResponse {
    
    private int licenseNum;
    
    private int usedNum;

    private int vdiCloudDesktopUsedLicenseNum;

    private int idvTerminalUsedLicenseNum;

    private int tciTerminalUsedLicenseNum;

    private int rcaUsedLicenseNum;

    public ObtainRcdcLicenseNumResponse() {
        
    }
    
    public ObtainRcdcLicenseNumResponse(int licenseNum,int usedNum) {
        this.licenseNum = licenseNum;
        this.usedNum = usedNum;
    }

    public int getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(int licenseNum) {
        this.licenseNum = licenseNum;
    }

    public int getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(int usedNum) {
        this.usedNum = usedNum;
    }

    public int getVdiCloudDesktopUsedLicenseNum() {
        return vdiCloudDesktopUsedLicenseNum;
    }

    public void setVdiCloudDesktopUsedLicenseNum(int vdiCloudDesktopUsedLicenseNum) {
        this.vdiCloudDesktopUsedLicenseNum = vdiCloudDesktopUsedLicenseNum;
    }

    public int getIdvTerminalUsedLicenseNum() {
        return idvTerminalUsedLicenseNum;
    }

    public void setIdvTerminalUsedLicenseNum(int idvTerminalUsedLicenseNum) {
        this.idvTerminalUsedLicenseNum = idvTerminalUsedLicenseNum;
    }

    public int getTciTerminalUsedLicenseNum() {
        return tciTerminalUsedLicenseNum;
    }

    public void setTciTerminalUsedLicenseNum(int tciTerminalUsedLicenseNum) {
        this.tciTerminalUsedLicenseNum = tciTerminalUsedLicenseNum;
    }


    public int getRcaUsedLicenseNum() {
        return rcaUsedLicenseNum;
    }

    public void setRcaUsedLicenseNum(int rcaUsedLicenseNum) {
        this.rcaUsedLicenseNum = rcaUsedLicenseNum;
    }
}
