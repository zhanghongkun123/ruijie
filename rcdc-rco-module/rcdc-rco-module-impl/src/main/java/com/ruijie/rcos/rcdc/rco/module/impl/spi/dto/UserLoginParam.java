package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月23日
 *
 * @author jarman
 */
public class UserLoginParam {

    @NotNull
    private LoginBusinessService loginBusinessService;

    @NotNull
    private ShineLoginDTO loginRequestData;

    @NotNull
    private CbbDispatcherRequest dispatcherRequest;

    @Nullable
    private IacUserDetailDTO userDetailDTO;

    public UserLoginParam() {
        
    }

    public UserLoginParam(CbbDispatcherRequest dispatcherRequest, ShineLoginDTO loginRequestData, IacUserDetailDTO userDetailDTO,
            LoginBusinessService loginBusinessService) {
        this.loginBusinessService = loginBusinessService;
        this.loginRequestData = loginRequestData;
        this.dispatcherRequest = dispatcherRequest;
        this.userDetailDTO = userDetailDTO;
    }

    public LoginBusinessService getLoginBusinessService() {
        return loginBusinessService;
    }

    public void setLoginBusinessService(LoginBusinessService loginBusinessService) {
        this.loginBusinessService = loginBusinessService;
    }

    public ShineLoginDTO getLoginRequestData() {
        return loginRequestData;
    }

    public void setLoginRequestData(ShineLoginDTO loginRequestData) {
        this.loginRequestData = loginRequestData;
    }

    public CbbDispatcherRequest getDispatcherRequest() {
        return dispatcherRequest;
    }

    public void setDispatcherRequest(CbbDispatcherRequest dispatcherRequest) {
        this.dispatcherRequest = dispatcherRequest;
    }

    public IacUserDetailDTO getUserDetailDTO() {
        return userDetailDTO;
    }

    public void setUserDetailDTO(IacUserDetailDTO userDetailDTO) {
        this.userDetailDTO = userDetailDTO;
    }
}
