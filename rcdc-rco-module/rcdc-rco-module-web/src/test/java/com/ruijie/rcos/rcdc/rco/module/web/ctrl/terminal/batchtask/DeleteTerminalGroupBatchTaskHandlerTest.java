package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WifiWhitelistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleGroupPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.IdvTerminalGroupDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.TerminalGroupHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbDeleteTerminalGroupDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/19
 *
 * @author nt
 */
@RunWith(SkyEngineRunner.class)
public class DeleteTerminalGroupBatchTaskHandlerTest {

    @Tested
    private DeleteTerminalGroupBatchTaskHandler handler;

    @Injectable
    private ProgrammaticOptLogRecorder optLogRecorder;

    @Injectable
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    @Injectable
    private UserTerminalGroupMgmtAPI userTerminalGroupMgmtAPI;

    @Injectable
    private PermissionHelper permissionHelper;

    @Injectable
    private TerminalGroupHelper terminalGroupHelper;

    @Injectable
    private SessionContext sessionContext;

    @Injectable
    private DeleteTerminalGroupBatchTaskItem mockBatchTaskItem;

    @Injectable
    private WifiWhitelistAPI wifiWhitelistAPI;

    @Injectable
    private UserTerminalMgmtAPI userTerminalMgmtAPI;
    /**
     * 初始化加载
     */
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

    /**
     * test
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItem() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> handler.processItem(null), "taskItem不能为null");

        UUID groupId = UUID.randomUUID();

        CbbTerminalGroupDetailDTO terminalGroupDTO = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO.setId(groupId);
        terminalGroupDTO.setGroupName("123");
        List<CbbTerminalGroupDetailDTO> dtoList = Lists.newArrayList(terminalGroupDTO);

        IdvTerminalGroupDetailResponse detailResponse = new IdvTerminalGroupDetailResponse();
        detailResponse.setId(groupId);
        detailResponse.setName("123");

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = groupId;

                terminalGroupMgmtAPI.listTerminalGroup();
                result = dtoList;

                permissionHelper.deleteAdminGroupPermissionList((List) any, RoleGroupPermissionType.TERMINAL_GROUP);

                terminalGroupMgmtAPI.deleteTerminalGroup((CbbDeleteTerminalGroupDTO) any);
            }
        };

        handler.processItem(mockBatchTaskItem);

        new Verifications() {
            {
                terminalGroupMgmtAPI.listTerminalGroup();
                times = 1;

                permissionHelper.deleteAdminGroupPermissionList((List) any, RoleGroupPermissionType.TERMINAL_GROUP);
                times = 1;

                terminalGroupMgmtAPI.deleteTerminalGroup((CbbDeleteTerminalGroupDTO) any);
                times = 1;
            }
        };
    }

    /**
     * test
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItemHasIdvSetting1() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> handler.processItem(null), "taskItem不能为null");

        UUID groupId = UUID.randomUUID();

        CbbTerminalGroupDetailDTO terminalGroupDTO = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO.setId(groupId);
        terminalGroupDTO.setGroupName("123");
        List<CbbTerminalGroupDetailDTO> dtoList = Lists.newArrayList(terminalGroupDTO);

        IdvTerminalGroupDetailResponse detailResponse = new IdvTerminalGroupDetailResponse();
        detailResponse.setId(groupId);
        detailResponse.setName("123");
        detailResponse.setIdvDesktopImageId(UUID.randomUUID());

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = groupId;

                terminalGroupMgmtAPI.listTerminalGroup();
                result = dtoList;

                permissionHelper.deleteAdminGroupPermissionList((List) any, RoleGroupPermissionType.TERMINAL_GROUP);

                userTerminalGroupMgmtAPI.deleteTerminalGroupDesktopConfig((UUID) any);
            }
        };

        handler.processItem(mockBatchTaskItem);

        new Verifications() {
            {
                terminalGroupMgmtAPI.listTerminalGroup();
                times = 1;

                permissionHelper.deleteAdminGroupPermissionList((List) any, RoleGroupPermissionType.TERMINAL_GROUP);
                times = 1;

                terminalGroupMgmtAPI.deleteTerminalGroup((CbbDeleteTerminalGroupDTO) any);
                times = 1;

                userTerminalGroupMgmtAPI.deleteTerminalGroupDesktopConfig((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * test
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItemHasIdvSetting2() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> handler.processItem(null), "taskItem不能为null");

        UUID groupId = UUID.randomUUID();

        CbbTerminalGroupDetailDTO terminalGroupDTO = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO.setId(groupId);
        terminalGroupDTO.setGroupName("123");
        List<CbbTerminalGroupDetailDTO> dtoList = Lists.newArrayList(terminalGroupDTO);

        IdvTerminalGroupDetailResponse detailResponse = new IdvTerminalGroupDetailResponse();
        detailResponse.setId(groupId);
        detailResponse.setName("123");
        detailResponse.setIdvDesktopStrategyId(UUID.randomUUID());

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = groupId;

                terminalGroupMgmtAPI.listTerminalGroup();
                result = dtoList;

                permissionHelper.deleteAdminGroupPermissionList((List) any, RoleGroupPermissionType.TERMINAL_GROUP);

                userTerminalGroupMgmtAPI.deleteTerminalGroupDesktopConfig((UUID) any);
            }
        };

        handler.processItem(mockBatchTaskItem);

        new Verifications() {
            {
                terminalGroupMgmtAPI.listTerminalGroup();
                times = 1;

                permissionHelper.deleteAdminGroupPermissionList((List) any, RoleGroupPermissionType.TERMINAL_GROUP);
                times = 1;

                terminalGroupMgmtAPI.deleteTerminalGroup((CbbDeleteTerminalGroupDTO) any);
                times = 1;

                userTerminalGroupMgmtAPI.deleteTerminalGroupDesktopConfig((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 异常
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItemWhenException() throws Exception {

        UUID groupId = UUID.randomUUID();

        CbbTerminalGroupDetailDTO terminalGroupDTO = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO.setId(groupId);
        terminalGroupDTO.setGroupName("123");
        List<CbbTerminalGroupDetailDTO> dtoList = Lists.newArrayList(terminalGroupDTO);

        IdvTerminalGroupDetailResponse detailResponse = new IdvTerminalGroupDetailResponse();
        detailResponse.setId(groupId);
        detailResponse.setName("123");

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = groupId;

                terminalGroupMgmtAPI.listTerminalGroup();
                result = dtoList;

                terminalGroupMgmtAPI.deleteTerminalGroup((CbbDeleteTerminalGroupDTO) any);
                result = new BusinessException("error");
            }
        };

        try {
            handler.processItem(mockBatchTaskItem);
        } catch (BusinessException e) {
            Assert.assertEquals(e.getMessage(), "error");
        }

        new Verifications() {
            {
                terminalGroupMgmtAPI.listTerminalGroup();
                times = 1;

                permissionHelper.deleteAdminGroupPermissionList((List) any, RoleGroupPermissionType.TERMINAL_GROUP);
                times = 0;

                terminalGroupMgmtAPI.deleteTerminalGroup((CbbDeleteTerminalGroupDTO) any);
                times = 1;
            }
        };
    }

    /**
     * test
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItemHasChildGroup() throws Exception {

        UUID groupId = UUID.randomUUID();

        CbbTerminalGroupDetailDTO terminalGroupDTO = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO.setId(groupId);
        terminalGroupDTO.setGroupName("123");

        CbbTerminalGroupDetailDTO terminalGroupDTO2 = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO2.setId(UUID.randomUUID());
        terminalGroupDTO2.setGroupName("456");
        terminalGroupDTO2.setParentGroupId(groupId);

        CbbTerminalGroupDetailDTO terminalGroupDTO3 = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO3.setId(UUID.randomUUID());
        terminalGroupDTO3.setGroupName("789");
        terminalGroupDTO3.setParentGroupId(terminalGroupDTO2.getId());

        List<CbbTerminalGroupDetailDTO> dtoList = Lists.newArrayList(terminalGroupDTO, terminalGroupDTO2, terminalGroupDTO3);

        IdvTerminalGroupDetailResponse detailResponse = new IdvTerminalGroupDetailResponse();
        detailResponse.setId(groupId);
        detailResponse.setName("123");

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = groupId;

                terminalGroupMgmtAPI.listTerminalGroup();
                result = dtoList;

                permissionHelper.deleteAdminGroupPermissionList((List) any, RoleGroupPermissionType.TERMINAL_GROUP);

                terminalGroupMgmtAPI.deleteTerminalGroup((CbbDeleteTerminalGroupDTO) any);
            }
        };

        handler.processItem(mockBatchTaskItem);

        new Verifications() {
            {
                terminalGroupMgmtAPI.listTerminalGroup();
                times = 1;

                permissionHelper.deleteAdminGroupPermissionList((List) any, RoleGroupPermissionType.TERMINAL_GROUP);
                times = 1;

                terminalGroupMgmtAPI.deleteTerminalGroup((CbbDeleteTerminalGroupDTO) any);
                times = 1;
            }
        };
    }

    /**
     * test
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItemHasChildGroupAndMove2DefaultGroup() throws Exception {

        UUID groupId = UUID.randomUUID();

        CbbTerminalGroupDetailDTO terminalGroupDTO = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO.setId(groupId);
        terminalGroupDTO.setGroupName("123");

        CbbTerminalGroupDetailDTO terminalGroupDTO2 = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO2.setId(UUID.randomUUID());
        terminalGroupDTO2.setGroupName("456");
        terminalGroupDTO2.setParentGroupId(groupId);

        CbbTerminalGroupDetailDTO terminalGroupDTO3 = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO3.setId(UUID.randomUUID());
        terminalGroupDTO3.setGroupName("789");
        terminalGroupDTO3.setParentGroupId(terminalGroupDTO2.getId());

        CbbTerminalGroupDetailDTO TerminalGroupDTO4 = new CbbTerminalGroupDetailDTO();
        TerminalGroupDTO4.setId(UUID.randomUUID());
        TerminalGroupDTO4.setGroupName("aaa");
        TerminalGroupDTO4.setParentGroupId(terminalGroupDTO2.getId());

        IdvTerminalGroupDetailResponse detailResponse = new IdvTerminalGroupDetailResponse();
        detailResponse.setId(groupId);
        detailResponse.setName("123");

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = groupId;

                mockBatchTaskItem.getMoveGroupId();
                result = CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID;

                terminalGroupMgmtAPI.listTerminalGroup();
                result = Lists.newArrayList(terminalGroupDTO, terminalGroupDTO2, terminalGroupDTO3);

                permissionHelper.deleteAdminGroupPermissionList((List) any, RoleGroupPermissionType.TERMINAL_GROUP);

                terminalGroupMgmtAPI.deleteTerminalGroup((CbbDeleteTerminalGroupDTO) any);
            }
        };

        handler.processItem(mockBatchTaskItem);

        new Verifications() {
            {
                terminalGroupMgmtAPI.listTerminalGroup();
                times = 1;

                permissionHelper.deleteAdminGroupPermissionList((List) any, RoleGroupPermissionType.TERMINAL_GROUP);
                times = 1;

                terminalGroupMgmtAPI.deleteTerminalGroup((CbbDeleteTerminalGroupDTO) any);
                times = 1;
            }
        };
    }

    /**
     * testOnFinishWhenSuccess
     */
    @Test
    public void testOnFinishWhenSuccess() {

        int sucCount = 1;
        int failCount = 0;

        BatchTaskFinishResult batchTaskFinishResult = handler.onFinish(sucCount, failCount);
        Assert.assertTrue(batchTaskFinishResult.getStatus() == BatchTaskStatus.SUCCESS);
    }

    /**
     * testOnFinishWhenFail
     */
    @Test
    public void testOnFinishWhenFail() {

        int sucCount = 0;
        int failCount = 1;

        BatchTaskFinishResult batchTaskFinishResult = handler.onFinish(sucCount, failCount);
        Assert.assertTrue(batchTaskFinishResult.getStatus() == BatchTaskStatus.FAILURE);
    }


}
