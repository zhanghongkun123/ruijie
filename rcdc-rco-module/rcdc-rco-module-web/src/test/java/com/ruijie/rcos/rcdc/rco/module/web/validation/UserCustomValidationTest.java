package com.ruijie.rcos.rcdc.rco.module.web.validation;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.CreateUserWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import mockit.Tested;

/**
 * Description: 创建用户参数校验
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/04/02
 *
 * @author songxiang
 */

public class UserCustomValidationTest {

    @Tested
    private UserCustomValidation userCustomValidation;

    /**
     * test
     */
    @Test
    public void testCreateUserValidate() {
        CreateUserWebRequest request = new CreateUserWebRequest();
        request.setUserName("admin");
        try {
            userCustomValidation.createUserValidate(request);
            Assert.fail();
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), UserBusinessKey.RCDC_RCO_CREATE_USER_NAME_CONTAIN_DEFAULT);
        }
    }

    /**
     * test
     */
    @Test
    public void testCreateUserValidate2() {
        CreateUserWebRequest request = new CreateUserWebRequest();
        request.setUserName("admin1");
        request.setUserType(IacUserTypeEnum.NORMAL);
        try {
            userCustomValidation.createUserValidate(request);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "用户姓名不能为空");
        }
    }

    /**
     * test
     */
    @Test
    public void testCreateUserValidate3() {
        CreateUserWebRequest request = new CreateUserWebRequest();
        request.setUserName("admin1");
        request.setUserType(IacUserTypeEnum.NORMAL);
        request.setRealName("abc");
        IdLabelEntry labelEntry = new IdLabelEntry();
        labelEntry.setId(UUID.fromString("a4209626-1df1-499f-93ca-de15c3f17fe7"));
        request.setUserGroup(labelEntry);
        try {
            userCustomValidation.createUserValidate(request);
            Assert.fail();
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), UserBusinessKey.RCDC_RCO_USER_GROUP_CAN_NOT_CREATE_IN_DEFAULT);
        }
    }
}
