package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import org.springframework.util.Assert;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

/**
 * 授权数量，总数、已用
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月17日
 * 
 * @author lin
 */
public class LicenseNumDTO extends EqualsHashcodeSupport {

    private int licenseNum = 0;
    
    private int activeNum = 0;
    
    public int getLicenseNum() {
        return licenseNum;
    }

    public int getActiveNum() {
        return activeNum;
    }
    
    /**
     * 增加授权总数
     * @param num 数量
     */
    public void addLicenseNum(int num) {
        Assert.notNull(num, "num cannot be null!");
        if (-1 == this.licenseNum) {
            this.licenseNum = num;
            return;
        }
        this.licenseNum += num;
    }
    
    /**
     * 增加授权使用数
     * @param num num
     */
    public void addActiveNum(int num) {
        Assert.notNull(num, "num cannot be null!");
        this.licenseNum += num;
    }

    public void setLicenseNum(int licenseNum) {
        this.licenseNum = licenseNum;
    }

    public void setActiveNum(int activeNum) {
        this.activeNum = activeNum;
    }
    
}
