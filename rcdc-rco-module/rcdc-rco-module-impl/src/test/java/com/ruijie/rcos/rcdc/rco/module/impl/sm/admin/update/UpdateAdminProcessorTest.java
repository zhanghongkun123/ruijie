package com.ruijie.rcos.rcdc.rco.module.impl.sm.admin.update;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacUpdateAdminRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminRequest;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年02月05日
 *
 * @author xiejian
 */
@RunWith(JMockit.class)
public class UpdateAdminProcessorTest {

    @Tested
    private UpdateAdminProcessor processor;

    @Injectable
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Injectable
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Mocked
    private StateTaskHandle.StateProcessContext stateProcessContext;

    @Test
    public void testDoProcess() throws Exception {

        UpdateAdminRequest updateAdminRequest = new UpdateAdminRequest();
        IdLabelEntry[] idLabelEntryArr = new IdLabelEntry[1];
        idLabelEntryArr[0] = new IdLabelEntry();
        idLabelEntryArr[0].setId(UUID.randomUUID());
        idLabelEntryArr[0].setLabel("Test");
        updateAdminRequest.setRoleId(UUID.randomUUID());

        UUID[] roleIdArr = new UUID[1];
        roleIdArr[0] = UUID.randomUUID();
        IacAdminDTO dto = new IacAdminDTO();
        dto.setRoleIdArr(roleIdArr);

        List<IacRoleDTO> dtoList = new ArrayList<>();
        IacRoleDTO baseRoleDTO = new IacRoleDTO();
        baseRoleDTO.setHasDefault(false);
        dtoList.add(baseRoleDTO);

        new Expectations() {
            {
                stateProcessContext.get(UpdateAdminContextKey.UPDATE_ADMIN_REQUEST, UpdateAdminRequest.class);
                result = updateAdminRequest;
                baseAdminMgmtAPI.getAdmin((UUID) any);
                result = dto;
                baseRoleMgmtAPI.getRoleAllByRoleIds((UUID[]) any);
                result = dtoList;
                baseAdminMgmtAPI.updateAdmin((IacUpdateAdminRequest) any);
                result = null;
            }
        };

        processor.doProcess(stateProcessContext);

        new Verifications() {
            {
                baseAdminMgmtAPI.getAdmin((UUID) any);
                times = 2;
                baseAdminMgmtAPI.updateAdmin((IacUpdateAdminRequest) any);
                times = 1;
            }
        };
    }

    @Test
    public void testUndoProcess() throws Exception {

        UpdateAdminRequest updateAdminRequest = new UpdateAdminRequest();
        IdLabelEntry[] idLabelEntryArr = new IdLabelEntry[1];
        idLabelEntryArr[0] = new IdLabelEntry();
        idLabelEntryArr[0].setId(UUID.randomUUID());
        idLabelEntryArr[0].setLabel("Test");
        updateAdminRequest.setRoleId(UUID.randomUUID());

        UUID[] roleIdArr = new UUID[1];
        roleIdArr[0] = UUID.randomUUID();
        IacAdminDTO dto = new IacAdminDTO();
        dto.setRoleIdArr(roleIdArr);

        new Expectations() {
            {
                stateProcessContext.get(UpdateAdminContextKey.ADMIN_INFO_CACHE, UpdateAdminRequest.class);
                result = updateAdminRequest;
                baseAdminMgmtAPI.updateAdmin((IacUpdateAdminRequest) any);
                result = null;
            }
        };

        processor.undoProcess(stateProcessContext);

        new Verifications() {
            {
                baseAdminMgmtAPI.updateAdmin((IacUpdateAdminRequest) any);
                times = 1;
            }
        };
    }

}
