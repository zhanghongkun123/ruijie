package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CloudDesktopDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import mockit.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月08日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class CloudDeskShutDownQuartzTaskTest {
    @Tested
    private CloudDeskShutDownQuartzTask cloudDeskShutDownQuartzTask;

    @Injectable
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Injectable
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Injectable
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Injectable
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Mocked
    private Logger logger;
    
    /**
     * 测试注解
     */
    @Test
    public void testQuartz() {
        Quartz quartz = (Quartz) AnnotationUtils.findAnnotation(CloudDeskShutDownQuartzTask.class, Quartz.class);
        boolean enableBlockInMaintenanceMode = quartz.blockInMaintenanceMode();
        Assert.assertTrue(enableBlockInMaintenanceMode);
    }

    /**
     * 测试执行操作方法当状态发生变更时
     * 
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteOperatorWhileStateChange() throws BusinessException {
        CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopState(CbbCloudDeskState.CLOSE.name());

        UUID deskId = UUID.randomUUID();

        new Expectations() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = cloudDesktopDetailDTO;
            }
        };
        cloudDeskShutDownQuartzTask.executeOperator(deskId, Sets.newHashSet(), Lists.newArrayList(), Lists.newArrayList());
        new Verifications() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                times = 1;
                userDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, anyBoolean));
                times = 0;
            }
        };
    }

    /**
     * 测试执行操作方法当抛业务异常时
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteOperatorWhileThrowBusinessException() throws BusinessException {
        UUID deskId = UUID.randomUUID();

        CloudDesktopDetailResponse cloudDesktopDetailResponse = new CloudDesktopDetailResponse();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopState(CbbCloudDeskState.RUNNING.name());
        cloudDesktopDetailDTO.setId(deskId);
        cloudDesktopDetailResponse.setCloudDesktopDetailDTO(cloudDesktopDetailDTO);

        new MockUp<LocaleI18nResolver>() {
            @Mock
            public String resolve(String key, String... args) {
                return "string";
            }
        };

        BusinessException e = new BusinessException("xx");
        new Expectations(cloudDeskShutDownQuartzTask) {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = e;
                cloudDeskShutDownQuartzTask.addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_FAIL_SYSTEM_LOG, (String[]) any);

            }
        };
        cloudDeskShutDownQuartzTask.executeOperator(deskId, Sets.newHashSet(), Lists.newArrayList(), Lists.newArrayList(deskId));
        new Verifications() {
            {
                cloudDeskShutDownQuartzTask.addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_FAIL_SYSTEM_LOG, (String[]) any);
                times = 1;
                userDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, anyBoolean));
                times = 0;
            }
        };

    }


    /**
     * 测试执行操作方法当不被配置包含时
     * 
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteOperatorWhileConfigNotContain() throws BusinessException {
        CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopState(CbbCloudDeskState.RUNNING.name());


        UUID deskId = UUID.randomUUID();

        new MockUp<LocaleI18nResolver>() {
            @Mock
            public String resolve(String key, String... args) {
                return "string";
            }
        };

        new Expectations() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = cloudDesktopDetailDTO;
            }
        };
        cloudDeskShutDownQuartzTask.executeOperator(deskId, Sets.newHashSet(), Lists.newArrayList(), Lists.newArrayList());
        cloudDeskShutDownQuartzTask.executeOperator(deskId, Sets.newHashSet(), Lists.newArrayList(), Lists.newArrayList());
        new Verifications() {
            {
                userDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, anyBoolean));
                times = 0;
            }
        };
    }

    /**
     * 测试执行操作方法当异常时
     * 
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteOperatorWhileException() throws BusinessException {
        UUID deskId = UUID.randomUUID();

        CloudDesktopDetailResponse cloudDesktopDetailResponse = new CloudDesktopDetailResponse();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopState(CbbCloudDeskState.RUNNING.name());
        cloudDesktopDetailDTO.setId(deskId);
        cloudDesktopDetailResponse.setCloudDesktopDetailDTO(cloudDesktopDetailDTO);

        Exception e = new Exception();
        new Expectations(cloudDeskShutDownQuartzTask) {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = e;
            }
        };
        cloudDeskShutDownQuartzTask.executeOperator(deskId, Sets.newHashSet(), Lists.newArrayList(), Lists.newArrayList(deskId));
        new Verifications() {
            {
                userDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, anyBoolean));
                times = 0;
            }
        };

    }

    /**
     * 测试执行操作方法
     * 
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteOperator() throws BusinessException {
        UUID deskId = UUID.randomUUID();

        CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopState(CbbCloudDeskState.RUNNING.name());
        cloudDesktopDetailDTO.setId(deskId);
        cloudDesktopDetailDTO.setDesktopCategory("VDI");

        new Expectations(cloudDeskShutDownQuartzTask) {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = cloudDesktopDetailDTO;
                cloudDeskShutDownQuartzTask.addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_SUCCESS_SYSTEM_LOG, (String[]) any);
            }
        };
        cloudDeskShutDownQuartzTask.executeOperator(deskId, Sets.newHashSet(), Lists.newArrayList(), Lists.newArrayList(deskId));
        new Verifications() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                times = 1;
                cloudDeskShutDownQuartzTask.addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_SUCCESS_SYSTEM_LOG, (String[]) any);
                times = 1;
                userDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, anyBoolean));
                times = 1;
            }
        };

    }

    /**
     * 测试执行操作方法
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteOperatorWhileMatchGroupId() throws BusinessException {
        UUID deskId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopState(CbbCloudDeskState.RUNNING.name());
        cloudDesktopDetailDTO.setId(deskId);
        cloudDesktopDetailDTO.setUserGroupId(groupId);
        cloudDesktopDetailDTO.setDesktopCategory("IDV");

        new Expectations(cloudDeskShutDownQuartzTask) {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = cloudDesktopDetailDTO;
                cloudDeskShutDownQuartzTask.addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_SUCCESS_SYSTEM_LOG, (String[]) any);
            }
        };
        cloudDeskShutDownQuartzTask.executeOperator(deskId, Sets.newHashSet(groupId), Lists.newArrayList(), Lists.newArrayList());
        new Verifications() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                times = 1;
                cloudDeskShutDownQuartzTask.addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_SUCCESS_SYSTEM_LOG, (String[]) any);
                times = 1;
            }
        };

    }

    /**
     * 测试执行操作方法
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteOperatorWhileUserId() throws BusinessException {
        UUID deskId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopState(CbbCloudDeskState.RUNNING.name());
        cloudDesktopDetailDTO.setId(deskId);
        cloudDesktopDetailDTO.setUserId(userId);
        cloudDesktopDetailDTO.setDesktopCategory("VDI");

        new Expectations(cloudDeskShutDownQuartzTask) {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = cloudDesktopDetailDTO;
                cloudDeskShutDownQuartzTask.addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_SUCCESS_SYSTEM_LOG, (String[]) any);
            }
        };
        cloudDeskShutDownQuartzTask.executeOperator(deskId, Sets.newHashSet(), Lists.newArrayList(userId), Lists.newArrayList());
        new Verifications() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                times = 1;
                cloudDeskShutDownQuartzTask.addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_SUCCESS_SYSTEM_LOG, (String[]) any);
                times = 1;
                userDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, anyBoolean));
                times = 1;
            }
        };

    }

    /**
     * 测试获取线程池方法
     * 
     * @throws IllegalAccessException 异常
     */
    @Test
    public void testGetThreadExecutor() throws IllegalAccessException {
        ThreadExecutor threadExecutor = (ThreadExecutor) FieldUtils.readField(cloudDeskShutDownQuartzTask, "THREAD_EXECUTOR", true);
        Assert.assertTrue(threadExecutor == cloudDeskShutDownQuartzTask.getThreadExecutor());
    }

    /**
     * 测试获取待处理状态方法
     */
    @Test
    public void testGetNeedHandleState() {
        String[] stateArr = cloudDeskShutDownQuartzTask.getNeedHandleState();
        Assert.assertTrue(stateArr.length == 1);
        Assert.assertEquals(CbbCloudDeskState.RUNNING.name(), stateArr[0]);
    }

    /**
     * 测试执行操作方法，桌面类型不支持
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteOperatorDeskNotSupport() throws BusinessException {
        UUID deskId = UUID.randomUUID();

        CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopState(CbbCloudDeskState.RUNNING.name());
        cloudDesktopDetailDTO.setId(deskId);

        new Expectations(cloudDeskShutDownQuartzTask) {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                result = cloudDesktopDetailDTO;
            }
        };
        cloudDeskShutDownQuartzTask.executeOperator(deskId, Sets.newHashSet(), Lists.newArrayList(), Lists.newArrayList(deskId));
        new Verifications() {
            {
                userDesktopMgmtAPI.getDesktopDetailById((UUID) any);
                times = 1;
                cloudDeskShutDownQuartzTask.addSystemLog(BusinessKey.RCDC_RCO_QUARTZ_SHUT_DOWN_CLOUD_DESK_SUCCESS_SYSTEM_LOG, (String[]) any);
                times = 0;
                userDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, anyBoolean));
                times = 0;
            }
        };

    }

}
