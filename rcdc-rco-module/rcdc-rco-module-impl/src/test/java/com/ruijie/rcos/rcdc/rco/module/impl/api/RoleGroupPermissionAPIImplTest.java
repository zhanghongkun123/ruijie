package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleGroupPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeleteRoleGroupPermissionByGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RoleGroupPermissionDAO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月08日
 *
 * @author xiejian
 */
@RunWith(JMockit.class)
public class RoleGroupPermissionAPIImplTest {

    @Tested
    private AdminDataPermissionAPI roleGroupPermissionAPIImpl;



    @Injectable
    private RoleGroupPermissionDAO roleGroupPermissionDAO;

    /**
     * deleteRoleGroupPermissionByGroupId 测试
     */
    @Test
    public void testDeleteRoleGroupPermissionByGroupId() throws BusinessException {
        DeleteRoleGroupPermissionByGroupIdRequest request = new DeleteRoleGroupPermissionByGroupIdRequest();
        request.setGroupId(UUID.randomUUID().toString());
        request.setGroupType(RoleGroupPermissionType.USER_GROUP.name());
        new Expectations() {
            {
                roleGroupPermissionDAO.deleteByGroupIdAndGroupType(anyString, (RoleGroupPermissionType) any);
            }
        };

        roleGroupPermissionAPIImpl.deleteAdminGroupPermissionByGroupId(request);

        new Verifications() {
            {
                roleGroupPermissionDAO.deleteByGroupIdAndGroupType(anyString, (RoleGroupPermissionType) any);
                times = 1;
            }
        };
    }



}
