package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco;

import com.ruijie.rcos.rcdc.rco.module.def.api.AdminFunctionListCustomAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FunctionListCustomRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FunctionListCustomResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.request.FunctionListCustomWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/14 17:30
 *
 * @author brq
 */
@RunWith(JMockit.class)
public class AdminFunctionListCustomControllerTest {

    @Tested
    private AdminFunctionListCustomController adminFunctionListCustomController;

    @Injectable
    private AdminFunctionListCustomAPI adminFunctionListCustomAPI;

    @Test
    public void testGetFunctionListCustom(@Mocked SessionContext sessionContext) throws BusinessException {
        FunctionListCustomWebRequest webRequest = new FunctionListCustomWebRequest();
        webRequest.setFunctionType("USER_LIST");
        FunctionListCustomResponse response = new FunctionListCustomResponse();
        new Expectations() {
            {
                adminFunctionListCustomAPI.getFunctionListOfColumnMsg((FunctionListCustomRequest) any);
                result = response;
            }
        };
        adminFunctionListCustomController.getFunctionListCustom(webRequest, sessionContext);
        new Verifications() {
            {
                adminFunctionListCustomAPI.getFunctionListOfColumnMsg((FunctionListCustomRequest) any);
                times = 1;
            }
        };
    }

    @Test
    public void testSaveFunctionListCustom(@Mocked SessionContext sessionContext) throws BusinessException {
        FunctionListCustomWebRequest webRequest = new FunctionListCustomWebRequest();
        webRequest.setFunctionType("USER_LIST");

        new Expectations() {
            {
                adminFunctionListCustomAPI.saveFunctionListOfColumnMsg((FunctionListCustomRequest) any);
                result = null;
            }
        };
        adminFunctionListCustomController.saveFunctionListCustom(webRequest, sessionContext);
        new Verifications() {
            {
                adminFunctionListCustomAPI.saveFunctionListOfColumnMsg((FunctionListCustomRequest) any);
                times = 1;
            }
        };
    }
}
