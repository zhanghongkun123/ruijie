package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacPermissionMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacLoginAdminRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/24 11:40
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class AdminLoginHandlerSPIImplTest {

    @Tested
    private AdminLoginHandlerSPIImpl adminLoginHandlerSPI;

    @Injectable
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Injectable
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Injectable
    private AdminLoginOnTerminalCacheManager cacheManager;

    @Injectable
    private BaseAuditLogAPI auditLogAPI;

    @Injectable
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Injectable
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Injectable
    private DesService desService;

    @Injectable
    private AdminDataPermissionAPI roleGroupPermissionAPI;

    @Injectable
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Injectable
    private IacPermissionMgmtAPI basePermissionMgmtAPI;

    @Injectable
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;



    /**
     * 维护模式
     * @throws BusinessException 异常
     */
    @Test
    public void testDispatchException() throws BusinessException {
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData("{\"adminName\":\"admin\",\"password\":\"pass\",\"action\":\"test\"}");

        adminLoginHandlerSPI.dispatch(request);

        new Verifications() {
            {
                CbbResponseShineMessage message;
                messageHandlerAPI.response(message = withCapture());
                Assert.assertEquals(-2, message.getCode().intValue());
            }
        };
    }

    /**
     * 审计管理员
     * @throws BusinessException 异常
     */
    @Test
    public void testDispatchAudAdmin() throws BusinessException {
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData("{\"adminName\":\"audadmin\",\"password\":\"pass\",\"action\":\"test\"}");

        UUID uuid = UUID.randomUUID();
        IacAdminDTO adminDTO = new IacAdminDTO();
        adminDTO.setRoleIdArr(new UUID[] {uuid});

        IacRoleDTO baseRoleDTO = new IacRoleDTO();
        baseRoleDTO.setRoleName("审计管理员");
        List<IacRoleDTO> dtoList = new ArrayList<IacRoleDTO>();
        dtoList.add(baseRoleDTO);

        new Expectations() {
            {
                maintenanceModeMgmtAPI.getMaintenanceMode();
                result = SystemMaintenanceState.NORMAL;
                baseAdminMgmtAPI.loginAdmin((IacLoginAdminRequest) any);
                result = adminDTO;
                baseRoleMgmtAPI.getRoleAllByRoleIds((UUID[]) any);
                result = dtoList;
            }
        };
        adminLoginHandlerSPI.dispatch(request);

        new Verifications() {
            {
                CbbResponseShineMessage message;
                messageHandlerAPI.response(message = withCapture());
                Assert.assertEquals(-5, message.getCode().intValue());
            }
        };
    }

    /**
     * 测试getMenuNameArr方法，入参为空数组
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    public void testGetMenuNameArrUUIDArrNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getMenuNameArrMethod = adminLoginHandlerSPI.getClass().getDeclaredMethod("getMenuNameArr",  UUID[].class);
        getMenuNameArrMethod.setAccessible(true);

        String[] resultArr = (String[]) getMenuNameArrMethod.invoke(adminLoginHandlerSPI, (Object) new UUID[]{});

        Assert.assertEquals(resultArr, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    /**
     * 测试getMenuNameArr方法，入参不为空数组
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    public void testGetMenuNameArr() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, BusinessException {
        Method getMenuNameArrMethod = adminLoginHandlerSPI.getClass().getDeclaredMethod("getMenuNameArr",  UUID[].class);
        getMenuNameArrMethod.setAccessible(true);


        IacPermissionDTO basePermissionDTO = new IacPermissionDTO();
        basePermissionDTO.setPermissionCode("imageTemplate");
        List<IacPermissionDTO> dtoList = new ArrayList<>();
        dtoList.add(basePermissionDTO);

        new Expectations() {
            {
                basePermissionMgmtAPI.listPermissionByAdminIdAndSource((UUID) any, SubSystem.CDC);
                result = dtoList;
            }
        };

        String[] resultArr = (String[]) getMenuNameArrMethod.invoke(adminLoginHandlerSPI, (Object) new UUID[]{UUID.randomUUID(),
            UUID.randomUUID()});

        Assert.assertTrue(resultArr.length == 1);
        Assert.assertTrue(resultArr[0].equals("imageTemplate"));
    }
}