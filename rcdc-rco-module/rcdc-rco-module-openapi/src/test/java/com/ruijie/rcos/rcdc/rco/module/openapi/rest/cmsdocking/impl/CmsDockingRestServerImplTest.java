package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.impl;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsComponentAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.InfeTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.GetInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.LoginAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.LoginUserRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.VerifAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.LoginAdminResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.LoginUserResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.VerifAdminResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.enums.AuthTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request.GetInfoRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request.VerifAdminRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request.VerifUserRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.response.DefaultRestServerResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.response.VerifAdminRestServerResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.response.VerifUserRestServerResponse;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;

/**
 * @author luojianmo
 * @Description:
 * @Company: Ruijie Co., Ltd.
 * @CreateTime: 2020-02-17 15:27
 */
@RunWith(SkyEngineRunner.class)
public class CmsDockingRestServerImplTest {

    @Tested
    private CmsDockingRestServerImpl cmsDockingRestServer;

    @Injectable
    private IacUserMgmtAPI cbbUserAPI;

    @Injectable
    private CmsDockingAPI cmsDockingAPI;

    @Injectable
    private CmsComponentAPI cmsComponentAPI;

    @Test
    public void testVerifUserByAuthTypeIsUser() throws Exception{
        VerifUserRestServerRequest request = new VerifUserRestServerRequest();
        request.setAuthType(AuthTypeEnum.USER);
        request.setUserName("userName");
        request.setPassword("password");
        LoginUserResponse verifUserResponse = new LoginUserResponse();
        verifUserResponse.setAuthCode(CommonMessageCode.CODE_ERR_OTHER);
        verifUserResponse.setName("name");
        verifUserResponse.setState(IacUserStateEnum.ENABLE);
        verifUserResponse.setUserGroupId(UUID.randomUUID());
        new Expectations() {
            {
                cmsDockingAPI.loginUser((LoginUserRequest)any);
                result = verifUserResponse;
            }
        };

        VerifUserRestServerResponse verifUserRestServerResponse = cmsDockingRestServer.verifUser(request);
        Assert.assertEquals(verifUserResponse.getAuthCode(), verifUserRestServerResponse.getAuthCode());
        Assert.assertEquals(verifUserResponse.getName(), verifUserRestServerResponse.getName());
        Assert.assertEquals(verifUserResponse.getState(), verifUserRestServerResponse.getState());
        Assert.assertEquals(verifUserResponse.getUserGroupId(), verifUserRestServerResponse.getUserGroupId());

        new Verifications() {
            {
                cmsDockingAPI.loginUser((LoginUserRequest)any);
                times = 1;
            }
        };
    }

    @Test
    public void testVerifUserByAuthTypeIsUserException() throws Exception{
        VerifUserRestServerRequest request = new VerifUserRestServerRequest();
        request.setAuthType(AuthTypeEnum.USER);
        request.setUserName("userName");
        request.setPassword("password");

        new Expectations() {
            {
                cmsDockingAPI.loginUser((LoginUserRequest)any);
                result = new Exception("error");
            }
        };

        VerifUserRestServerResponse verifUserRestServerResponse = cmsDockingRestServer.verifUser(request);
        Assert.assertEquals(CommonMessageCode.CODE_ERR_OTHER, verifUserRestServerResponse.getAuthCode());
        Assert.assertEquals(null, verifUserRestServerResponse.getName());
        Assert.assertEquals(null, verifUserRestServerResponse.getState());
        Assert.assertEquals(null, verifUserRestServerResponse.getUserGroupId());

        new Verifications() {
            {
                cmsDockingAPI.loginUser((LoginUserRequest)any);
                times = 1;
            }
        };
    }

    @Test
    public void testVerifUserByAuthTypeIsAdmin() throws Exception{
        VerifUserRestServerRequest request = new VerifUserRestServerRequest();
        request.setAuthType(AuthTypeEnum.ADMIN);
        request.setUserName("userName");
        request.setPassword("password");
        LoginAdminResponse loginAdminResponse = new LoginAdminResponse();
        loginAdminResponse.setAuthCode(CommonMessageCode.SUCCESS);

        new Expectations() {
            {
                cmsDockingAPI.loginAdmin((LoginAdminRequest)any);
                result = loginAdminResponse;
            }
        };

        VerifUserRestServerResponse verifUserRestServerResponse = cmsDockingRestServer.verifUser(request);
        Assert.assertEquals(loginAdminResponse.getAuthCode(), verifUserRestServerResponse.getAuthCode());

        new Verifications() {
            {
                cmsDockingAPI.loginUser((LoginUserRequest)any);
                times = 0;

                cmsDockingAPI.loginAdmin((LoginAdminRequest)any);
                times = 1;
            }
        };
    }

    @Test
    public void testVerifUserByAuthTypeIsAdminException() throws Exception{
        VerifUserRestServerRequest request = new VerifUserRestServerRequest();
        request.setAuthType(AuthTypeEnum.ADMIN);
        request.setUserName("userName");
        request.setPassword("password");

        new Expectations() {
            {
                cmsDockingAPI.loginAdmin((LoginAdminRequest)any);
                result = new Exception("error");
            }
        };

        VerifUserRestServerResponse verifUserRestServerResponse = cmsDockingRestServer.verifUser(request);
        Assert.assertEquals(CommonMessageCode.CODE_ERR_OTHER, verifUserRestServerResponse.getAuthCode());

        new Verifications() {
            {
                cmsDockingAPI.loginAdmin((LoginAdminRequest)any);
                times = 1;
            }
        };
    }

    @Test
    public void testVerifUserByAuthTypeIsNull() throws Exception{
        VerifUserRestServerRequest request = new VerifUserRestServerRequest();
        request.setUserName("userName");
        request.setPassword("password");


        VerifUserRestServerResponse verifUserRestServerResponse = cmsDockingRestServer.verifUser(request);
        Assert.assertEquals(CommonMessageCode.CODE_ERR_OTHER, verifUserRestServerResponse.getAuthCode());

        new Verifications() {
            {
                cmsDockingAPI.loginUser((LoginUserRequest)any);
                times = 0;

                cmsDockingAPI.loginAdmin((LoginAdminRequest)any);
                times = 0;
            }
        };
    }

    @Test
    public void testVerifAdmin() throws Exception{
        final UUID token = UUID.randomUUID();
        VerifAdminRestServerRequest request = new VerifAdminRestServerRequest();
        request.setToken(token.toString());

        VerifAdminResponse verifAdminResponse = new VerifAdminResponse();
        verifAdminResponse.setAdminName("adminName");
        verifAdminResponse.setAuthCode(CommonMessageCode.SUCCESS);
        verifAdminResponse.setPassword("password");

        new Expectations() {
            {
                cmsDockingAPI.verifAdmin((VerifAdminRequest)any);
                result = verifAdminResponse;
            }
        };

        VerifAdminRestServerResponse serverResponse = cmsDockingRestServer.verifAdmin(request);
        Assert.assertEquals(verifAdminResponse.getAuthCode(), serverResponse.getAuthCode());
        Assert.assertEquals(verifAdminResponse.getAdminName(), serverResponse.getAdminName());
        Assert.assertEquals(verifAdminResponse.getPassword(), serverResponse.getPassword());

        new Verifications() {
            {
                cmsDockingAPI.verifAdmin((VerifAdminRequest)any);
                times = 1;
            }
        };
    }

    @Test
    public void testVerifAdminByException() throws Exception{
        final UUID token = UUID.randomUUID();
        VerifAdminRestServerRequest request = new VerifAdminRestServerRequest();
        request.setToken(token.toString());

        new Expectations() {
            {
                cmsDockingAPI.verifAdmin((VerifAdminRequest)any);
                result = new Exception("error");
            }
        };

        VerifAdminRestServerResponse serverResponse = cmsDockingRestServer.verifAdmin(request);
        Assert.assertEquals(CommonMessageCode.CODE_ERR_OTHER, serverResponse.getAuthCode());


        new Verifications() {
            {
                cmsDockingAPI.verifAdmin((VerifAdminRequest)any);
                times = 1;
            }
        };
    }

    @Test
    public void testGetInfo() throws Exception{
        GetInfoRestServerRequest request = new GetInfoRestServerRequest();
        request.setInfo(InfeTypeEnum.ALL);

        GetInfoResponse getInfoResponse = new GetInfoResponse();
        getInfoResponse.setAuthCode(CommonMessageCode.SUCCESS);

        new Expectations() {
            {
                cmsDockingAPI.getInfo((GetInfoRequest)any);
                result = getInfoResponse;
            }
        };

        DefaultRestServerResponse response = cmsDockingRestServer.getInfo(request);
        Assert.assertEquals(CommonMessageCode.SUCCESS, response.getAuthCode());


        new Verifications() {
            {
                cmsDockingAPI.getInfo((GetInfoRequest)any);
                times = 1;
            }
        };

    }

    @Test
    public void testGetInfoByException() throws Exception{
        GetInfoRestServerRequest request = new GetInfoRestServerRequest();
        request.setInfo(InfeTypeEnum.ALL);


        new Expectations() {
            {
                cmsDockingAPI.getInfo((GetInfoRequest)any);
                result = new Exception("error");
            }
        };

        DefaultRestServerResponse response = cmsDockingRestServer.getInfo(request);
        Assert.assertEquals(CommonMessageCode.CODE_ERR_OTHER, response.getAuthCode());


        new Verifications() {
            {
                cmsDockingAPI.getInfo((GetInfoRequest)any);
                times = 1;
            }
        };

    }

}
