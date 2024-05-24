package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.FunctionListCustomRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.AdminFunctionListRelationDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminFunctionListCustomEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/1 17:07
 * @author brq
 */
@RunWith(JMockit.class)
public class AdminFunctionListCustomAPIImplTest {
    @Tested
    private AdminFunctionListCustomAPIImpl adminFunctionListCustomAPI;

    @Injectable
    private AdminFunctionListRelationDAO adminFunctionListRelationDAO;

    /**
     * test
     * @throws BusinessException ex
     */
    @Test
    public void testGetFunctionListOfColumnMsg() throws BusinessException {
        FunctionListCustomRequest request = new FunctionListCustomRequest();
        AdminFunctionListCustomEntity entity = new AdminFunctionListCustomEntity();
        String[] strArr = {"aa", "bb"};
        entity.setColumnText(strArr.toString());
        new Expectations() {
            {
                adminFunctionListRelationDAO.findByAdminIdAndFunctionType((UUID)any, (String)any);
                result = entity;
            }
        };
        adminFunctionListCustomAPI.getFunctionListOfColumnMsg(request);
        new Verifications() {
            {
                adminFunctionListRelationDAO.findByAdminIdAndFunctionType((UUID)any, (String)any);
                times = 1;
            }
        };
    }

    /**
     * test
     * @throws BusinessException ex
     */
    @Test
    public void testGetFunctionListOfColumnMsgWhileEntityIsNull() throws BusinessException {
        FunctionListCustomRequest request = new FunctionListCustomRequest();
        AdminFunctionListCustomEntity entity = null;
        new Expectations() {
            {
                adminFunctionListRelationDAO.findByAdminIdAndFunctionType((UUID)any, (String)any);
                result = entity;
            }
        };
        adminFunctionListCustomAPI.getFunctionListOfColumnMsg(request);
        new Verifications() {
            {
                adminFunctionListRelationDAO.findByAdminIdAndFunctionType((UUID)any, (String)any);
                times = 1;
            }
        };
    }

    /**
     * test
     * @throws BusinessException ex
     */
    @Test
    public void testSaveFunctionListOfColumnMsg() throws BusinessException {
        FunctionListCustomRequest request = new FunctionListCustomRequest();
        AdminFunctionListCustomEntity entity = new AdminFunctionListCustomEntity();
        String[] strArr = {"aa", "bb"};
        entity.setColumnText(strArr.toString());

        new Expectations() {
            {
                adminFunctionListRelationDAO.findByAdminIdAndFunctionType(request.getAdminId(), request.getFunctionType());
                result = entity;
                adminFunctionListRelationDAO.save((AdminFunctionListCustomEntity)any);
            }
        };
        adminFunctionListCustomAPI.saveFunctionListOfColumnMsg(request);
        new Verifications() {
            {
                adminFunctionListRelationDAO.findByAdminIdAndFunctionType(request.getAdminId(), request.getFunctionType());
                times = 1;
                adminFunctionListRelationDAO.save((AdminFunctionListCustomEntity)any);
                times = 1;
            }
        };
    }

    /**
     * test
     * @throws BusinessException ex
     */
    @Test
    public void testSaveFunctionListOfColumnMsgWhileHasId() throws BusinessException {
        FunctionListCustomRequest request = new FunctionListCustomRequest();
        AdminFunctionListCustomEntity entity = new AdminFunctionListCustomEntity();
        String[] strArr = {"aa", "bb"};
        entity.setColumnText(strArr.toString());
        new Expectations() {
            {
                adminFunctionListRelationDAO.findByAdminIdAndFunctionType(request.getAdminId(), request.getFunctionType());
                result = entity;
                adminFunctionListRelationDAO.save((AdminFunctionListCustomEntity)any);
            }
        };
        adminFunctionListCustomAPI.saveFunctionListOfColumnMsg(request);
        new Verifications() {
            {
                adminFunctionListRelationDAO.findByAdminIdAndFunctionType(request.getAdminId(), request.getFunctionType());
                times = 1;
                adminFunctionListRelationDAO.save((AdminFunctionListCustomEntity)any);
                times = 1;
            }
        };
    }
}
