package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.concurrent.CountDownLatch;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/3/21
 *
 * @author Jarman
 */
public class DesktopRequestDTO extends EqualsHashcodeSupport {

    private CbbDispatcherRequest cbbDispatcherRequest;

    /** 记录关机事件，是否为虚机内部关机 */
    private boolean isDesktopInnerShutdown = true;

    private boolean isDesktopStartFromWeb = false;
    
    private boolean isDesktopRunInTerminal = false;
    
    private String userName;

    private final transient CountDownLatch latch = new CountDownLatch(1);

    public DesktopRequestDTO() {

    }

    public DesktopRequestDTO(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest cannot null");
        this.cbbDispatcherRequest = cbbDispatcherRequest;
    }

    public DesktopRequestDTO(CbbDispatcherRequest cbbDispatcherRequest, @Nullable String userName) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest cannot null");
        this.cbbDispatcherRequest = cbbDispatcherRequest;
        this.userName = userName;
    }

    public DesktopRequestDTO(CbbDispatcherRequest cbbDispatcherRequest, boolean isDesktopInnerShutdown) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest cannot null");
        this.cbbDispatcherRequest = cbbDispatcherRequest;
        this.isDesktopInnerShutdown = isDesktopInnerShutdown;
    }

    public boolean isDesktopInnerShutdown() {
        return isDesktopInnerShutdown;
    }

    public void setDesktopInnerShutdown(boolean desktopInnerShutdown) {
        isDesktopInnerShutdown = desktopInnerShutdown;
    }

    public boolean isDesktopStartFromWeb() {
        return isDesktopStartFromWeb;
    }

    public void setDesktopStartFromWeb(boolean desktopStartFromWeb) {
        isDesktopStartFromWeb = desktopStartFromWeb;
    }
    
    public CbbDispatcherRequest getCbbDispatcherRequest() {
        return cbbDispatcherRequest;
    }

    public void setCbbDispatcherRequest(CbbDispatcherRequest cbbDispatcherRequest) {
        this.cbbDispatcherRequest = cbbDispatcherRequest;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public boolean isDesktopRunInTerminal() {
        return isDesktopRunInTerminal;
    }

    public void setDesktopRunInTerminal(boolean isDesktopRunInTerminal) {
        this.isDesktopRunInTerminal = isDesktopRunInTerminal;
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
