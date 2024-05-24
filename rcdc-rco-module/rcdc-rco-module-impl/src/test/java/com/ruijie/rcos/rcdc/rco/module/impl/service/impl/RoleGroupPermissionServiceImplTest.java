package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleGroupPermissionType;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RoleGroupPermissionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RoleGroupPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AdminDataPermissionService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月08日
 *
 * @author xiejian
 */
@RunWith(JMockit.class)
public class RoleGroupPermissionServiceImplTest {

    @Tested
    private AdminDataPermissionService roleGroupPermissionServiceImpl;

    @Injectable
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Injectable
    private IacUserGroupMgmtAPI userGroupAPI;

    @Injectable
    private RoleGroupPermissionDAO roleGroupPermissionDAO;

    @Injectable
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    /**
     * 测试 listUserGroupIdByAdminId
     * 测试 listTerminalGroupIdByAdminId
     */
    @Test
    public void testListUserAndTerminalGroupIdByAdminId() throws BusinessException {
        final IacAdminDTO dto = new IacAdminDTO();
        UUID[] roleIdArr = new UUID[1];
        roleIdArr[0] = UUID.randomUUID();
        dto.setRoleIdArr(roleIdArr);

        List<RoleGroupPermissionEntity> entityList = new ArrayList<>();
        RoleGroupPermissionEntity entity = new RoleGroupPermissionEntity();
        entity.setGroupId(UUID.randomUUID().toString());
        entityList.add(entity);

        new Expectations() {
            {
                baseAdminMgmtAPI.getAdmin((UUID) any);
                result = dto;
                roleGroupPermissionDAO.findByGroupTypeAndRoleIdIn((RoleGroupPermissionType) any, (ArrayList) any);
                result = entityList;
            }
        };

        roleGroupPermissionServiceImpl.listUserGroupIdByAdminId(UUID.randomUUID());
        roleGroupPermissionServiceImpl.listTerminalGroupIdByAdminId(UUID.randomUUID());

        new Verifications() {
            {
                baseAdminMgmtAPI.getAdmin((UUID) any);
                times = 2;
                roleGroupPermissionDAO.findByGroupTypeAndRoleIdIn((RoleGroupPermissionType) any, (ArrayList) any);
                times = 2;
            }
        };
    }



    /**
     * 测试 listTerminalGroupIdLabelEntryByAdminId
     */
    @Test
    public void testListTerminalGroupIdLabelEntryByAdminId() throws BusinessException {
        UUID userGroupId = UUID.randomUUID();

        CbbTerminalGroupDetailDTO dto = new CbbTerminalGroupDetailDTO();
        dto.setId(userGroupId);
        dto.setGroupName("Test");

        List<CbbTerminalGroupDetailDTO> dtoList = new ArrayList<>();
        dtoList.add(dto);

        List<RoleGroupPermissionEntity> entityList = new ArrayList<>();
        RoleGroupPermissionEntity entity = new RoleGroupPermissionEntity();
        entity.setGroupId(userGroupId.toString());
        entityList.add(entity);

        new Expectations() {
            {
                terminalGroupMgmtAPI.listTerminalGroup();
                result = dtoList;
                roleGroupPermissionDAO.findByGroupTypeAndRoleIdIn((RoleGroupPermissionType) any, (ArrayList) any);
                result = entityList;
            }
        };

        roleGroupPermissionServiceImpl.listTerminalGroupIdLabelEntryByAdminId(UUID.randomUUID());

        new Verifications() {
            {
                terminalGroupMgmtAPI.listTerminalGroup();
                times = 1;
                roleGroupPermissionDAO.findByGroupTypeAndRoleIdIn((RoleGroupPermissionType) any, (ArrayList) any);
                times = 1;
            }
        };
    }


    /**
     * 测试 listUserGroupIdLabelEntryByAdminId
     */
    @Test
    public void testListUserGroupIdLabelEntryByAdminId() throws BusinessException {
        UUID userGroupId = UUID.randomUUID();

        IacUserGroupDetailDTO dto = new IacUserGroupDetailDTO();
        dto.setId(userGroupId);
        dto.setName("Test");

        IacUserGroupDetailDTO[] dtoArr = new IacUserGroupDetailDTO[1];
        dtoArr[0] = dto;

        List<RoleGroupPermissionEntity> entityList = new ArrayList<>();
        RoleGroupPermissionEntity entity = new RoleGroupPermissionEntity();
        entity.setGroupId(userGroupId.toString());
        entityList.add(entity);

        new Expectations() {
            {
                userGroupAPI.getAllUserGroup();
                result = dtoArr;
                roleGroupPermissionDAO.findByGroupTypeAndRoleIdIn((RoleGroupPermissionType) any, (ArrayList) any);
                result = entityList;
            }
        };

        roleGroupPermissionServiceImpl.listUserGroupIdLabelEntryByAdminId(UUID.randomUUID());

        new Verifications() {
            {
                userGroupAPI.getAllUserGroup();
                times = 1;
                roleGroupPermissionDAO.findByGroupTypeAndRoleIdIn((RoleGroupPermissionType) any, (ArrayList) any);
                times = 1;
            }
        };
    }

    /**
     * 测试 listRoleGroupPermissionByRoleId
     */
    @Test
    public void testListRoleGroupPermissionByRoleId() {
        roleGroupPermissionServiceImpl.listAdminDataPermisssionByAdminId(UUID.randomUUID());

        new Verifications() {
            {
                roleGroupPermissionDAO.findByRoleId((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 测试 saveRoleGroupPermissionList
     */
    @Test
    public void testSaveRoleGroupPermissionList() {
        List<AdminDataPermissionEntity> entityList = new ArrayList<>();
        entityList.add(new AdminDataPermissionEntity());
        roleGroupPermissionServiceImpl.saveAdminDataPermisssionList(entityList);

        new Verifications() {
            {
                roleGroupPermissionDAO.saveAll((List<RoleGroupPermissionEntity>) any);
                times = 1;
            }
        };
    }

    /**
     * 测试 deleteRoleGroupPermissionByRoleId
     */
    @Test
    public void testDeleteRoleGroupPermissionByRoleId() {
        UUID roleId = UUID.randomUUID();
        roleGroupPermissionServiceImpl.deleteAdminDataPermisssionByAdminId(roleId);

        new Verifications() {
            {
                roleGroupPermissionDAO.deleteByRoleId(roleId);
                times = 1;
            }
        };
    }
}
