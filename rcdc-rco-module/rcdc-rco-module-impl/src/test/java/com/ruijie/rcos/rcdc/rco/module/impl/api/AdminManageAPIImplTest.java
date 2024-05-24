package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacIdPasswordEntry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacGetAdminPasswordRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SuperPrivilegeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetAdminPasswordResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SuperPrivilegeResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

/**
 * @author luojianmo
 * @Description:
 * @Company: Ruijie Co., Ltd.
 * @CreateTime: 2020-02-16 21:50
 */
@RunWith(JMockit.class)
public class AdminManageAPIImplTest {

    @Tested
    private AdminManageAPIImpl adminManageAPI;

    @Injectable
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Injectable
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Before
    public void before() {

        new MockUp<LocaleI18nResolver>() {

            /**
             *
             * @param key key
             * @param args args
             * @return key
             */
            @Mock
            public String resolve(String key, String... args) {
                return key;
            }

        };
    }

    @Test
    public void testIsSuperPrivilegeByRoleIsNull() throws BusinessException {
        UUID[] roleIdArr = new UUID[2];
        roleIdArr[0] = UUID.randomUUID();
        roleIdArr[1] = UUID.randomUUID();

        new Expectations() {
            {
                baseRoleMgmtAPI.getRoleAllByRoleIds((UUID[]) any);
                result = null;
            }
        };
        SuperPrivilegeResponse superPrivilegeResponse = adminManageAPI
            .isSuperPrivilege(new SuperPrivilegeRequest(roleIdArr));
        Assert.assertEquals(false, superPrivilegeResponse.isSuperPrivilege());
    }

    @Test
    public void testIsSuperPrivilegeByBaseRoleDTOListIsNull() throws BusinessException {
        UUID[] roleIdArr = new UUID[2];
        roleIdArr[0] = UUID.randomUUID();
        roleIdArr[1] = UUID.randomUUID();

        new Expectations() {
            {
                baseRoleMgmtAPI.getRoleAllByRoleIds((UUID[]) any);
                result = Lists.newArrayList();
            }
        };
        SuperPrivilegeResponse superPrivilegeResponse = adminManageAPI
            .isSuperPrivilege(new SuperPrivilegeRequest(roleIdArr));
        Assert.assertEquals(false, superPrivilegeResponse.isSuperPrivilege());
    }

    @Test
    public void testIsSuperPrivilegeByYes() throws BusinessException {
        UUID[] roleIdArr = new UUID[2];
        roleIdArr[0] = UUID.randomUUID();
        roleIdArr[1] = UUID.randomUUID();
        List<IacRoleDTO> baseRoleDTOList = new ArrayList<>();
        IacRoleDTO baseRoleDTO = new IacRoleDTO();
        baseRoleDTO.setHasSuperPrivilege(true);
        baseRoleDTOList.add(baseRoleDTO);

        new Expectations() {
            {
                baseRoleMgmtAPI.getRoleAllByRoleIds((UUID[]) any);
                result = baseRoleDTOList;
            }
        };
        SuperPrivilegeResponse superPrivilegeResponse = adminManageAPI
            .isSuperPrivilege(new SuperPrivilegeRequest(roleIdArr));
        Assert.assertEquals(true, superPrivilegeResponse.isSuperPrivilege());
    }

    @Test
    public void testIsSuperPrivilegeByNo() throws BusinessException {
        UUID[] roleIdArr = new UUID[2];
        roleIdArr[0] = UUID.randomUUID();
        roleIdArr[1] = UUID.randomUUID();
        List<IacRoleDTO> baseRoleDTOList = new ArrayList<>();
        IacRoleDTO baseRoleDTO = new IacRoleDTO();
        baseRoleDTO.setHasSuperPrivilege(false);
        baseRoleDTOList.add(baseRoleDTO);

        new Expectations() {
            {
                baseRoleMgmtAPI.getRoleAllByRoleIds((UUID[]) any);
                result = baseRoleDTOList;
            }
        };
        SuperPrivilegeResponse superPrivilegeResponse = adminManageAPI
            .isSuperPrivilege(new SuperPrivilegeRequest(roleIdArr));
        Assert.assertEquals(false, superPrivilegeResponse.isSuperPrivilege());
    }

    @Test
    public void testGetAdminPassword() throws Exception {
        final UUID uuid = UUID.randomUUID();

        final String password = "password";
        IdRequest idRequest = new IdRequest();
        idRequest.setId(uuid);

        IacIdPasswordEntry idPasswordEntry = new IacIdPasswordEntry(uuid,
                AesUtil.encrypt(password, RedLineUtil.getRealAdminRedLine()));
        IacIdPasswordEntry[] idPasswordEntryArr = new IacIdPasswordEntry[]{idPasswordEntry};

        new Expectations() {
            {
                baseAdminMgmtAPI.getAdminPassword((IacGetAdminPasswordRequest) any);
                result = idPasswordEntryArr;
            }
        };

        GetAdminPasswordResponse getAdminPasswordResponse = adminManageAPI.getAdminPassword(idRequest);
        Assert.assertEquals(password, getAdminPasswordResponse.getPassword());
    }

    @Test
    public void testGetAdminPasswordByException() throws Exception {
        final UUID uuid = UUID.randomUUID();
        final String password = "password";
        IdRequest idRequest = new IdRequest();
        idRequest.setId(uuid);

        IacIdPasswordEntry idPasswordEntry = new IacIdPasswordEntry(uuid, password);
        IacIdPasswordEntry[] idPasswordEntryArr = new IacIdPasswordEntry[]{idPasswordEntry};

        new Expectations() {
            {
                baseAdminMgmtAPI.getAdminPassword((IacGetAdminPasswordRequest) any);
                result = null;
            }
        };
        try {
            adminManageAPI.getAdminPassword(idRequest);
            Assert.fail();
        } catch (BusinessException e) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_GET_ADMIN_PASSWORD_FAIL, e.getKey());
        }


    }

}
