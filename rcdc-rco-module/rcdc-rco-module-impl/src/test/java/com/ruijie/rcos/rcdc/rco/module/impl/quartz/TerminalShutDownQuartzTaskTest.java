package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.request.systemlog.BaseCreateSystemLogRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月30日
 *
 * @author zql
 */
@RunWith(SkyEngineRunner.class)
public class TerminalShutDownQuartzTaskTest {

    @Tested
    private TerminalShutDownQuartzTask terminalShutDownQuartzTask;

    @Injectable
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Injectable
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Injectable
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Mocked
    private Logger logger;

    /**
     * 测试注解
     */
    @Test
    public void testQuartz() {
        Quartz quartz = (Quartz) AnnotationUtils.findAnnotation(TerminalShutDownQuartzTask.class, Quartz.class);
        boolean enableBlockInMaintenanceMode = quartz.blockInMaintenanceMode();
        Assert.assertTrue(enableBlockInMaintenanceMode);
    }


    /**
     * 测试校验方法
     * 
     * @param quartzData 定时任务数据
     * @throws BusinessException 业务异常
     */
    @Test
    public void testValidate(@Injectable TerminalShutDownQuartzTask.QuartzData quartzData) throws BusinessException {
        String bizData = "xx";
        new Expectations(JSON.class) {
            {
                JSON.parseObject(bizData, TerminalShutDownQuartzTask.QuartzData.class);
                result = quartzData;
                quartzData.getTerminalArr();
                returns(new String[] {"x"}, new String[] {""}, new String[0], new String[0]);
                quartzData.getTerminalGroupArr();
                returns(new UUID[] {UUID.randomUUID()}, new UUID[0], new UUID[] {UUID.randomUUID()}, new UUID[0]);
            }
        };

        terminalShutDownQuartzTask.validate(bizData);
        terminalShutDownQuartzTask.validate(bizData);
        terminalShutDownQuartzTask.validate(bizData);
        try {
            terminalShutDownQuartzTask.validate(bizData);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "终端组与终端不能同时为空");
        }
        new Verifications() {
            {
                ObjectUtils.isEmpty(any);
                times = 8;
                JSON.parseObject(bizData, TerminalShutDownQuartzTask.QuartzData.class);
                times = 4;
                quartzData.getTerminalArr();
                times = 4;
                quartzData.getTerminalGroupArr();
                times = 4;
            }
        };
    }

    /**
     * 测试execute方法当查询返回结果为空时
     *
     * @param quartzTaskContext 定时任务上下文
     * @throws Exception 异常
     */
    @Test
    public void testExecuteWhileQueryNoResult(@Injectable QuartzTaskContext quartzTaskContext) throws Exception {
        TerminalShutDownQuartzTask.QuartzData quartzData = new TerminalShutDownQuartzTask.QuartzData();
        UUID groupId = UUID.randomUUID();
        quartzData.setTerminalArr(new String[] {"one"});
        quartzData.setTerminalGroupArr(new UUID[] {groupId});
        final List<TerminalDTO> dtoList = Lists.newArrayList();
        TerminalDTO one = new TerminalDTO();
        one.setId(UUID.randomUUID().toString());
        one.setTerminalGroupId(groupId);
        dtoList.add(one);

        new Expectations(terminalShutDownQuartzTask) {
            {
                quartzTaskContext.getByType(TerminalShutDownQuartzTask.QuartzData.class);
                result = quartzData;
                terminalShutDownQuartzTask.queryAll();
                result = Sets.newHashSet();
            }
        };
        terminalShutDownQuartzTask.execute(quartzTaskContext);
        new Verifications() {
            {
                terminalShutDownQuartzTask.queryAll();
                times = 1;
            }
        };

    }

    /**
     * 测试execute方法
     *
     * @param quartzTaskContext 定时任务上下文
     * @param threadExecutor 线程池
     * @throws Exception 异常
     */
    @Test
    public void testExecute(@Injectable QuartzTaskContext quartzTaskContext, @Capturing ThreadExecutor threadExecutor) throws Exception {
        TerminalShutDownQuartzTask.QuartzData quartzData = new TerminalShutDownQuartzTask.QuartzData();
        UUID groupId = UUID.randomUUID();
        quartzData.setTerminalArr(new String[] {"one", "two"});
        quartzData.setTerminalGroupArr(new UUID[] {groupId});
        final List<TerminalDTO> dtoList = Lists.newArrayList();
        TerminalDTO one = new TerminalDTO();
        one.setId(UUID.randomUUID().toString());
        one.setTerminalGroupId(groupId);
        dtoList.add(one);
        new MockUp<TerminalShutDownQuartzTask>() {
            @Mock
            public Set<String> queryAll() throws Exception {
                return Sets.newHashSet("2");
            }
        };


        new Expectations() {
            {
                quartzTaskContext.getByType(TerminalShutDownQuartzTask.QuartzData.class);
                result = quartzData;
                threadExecutor.execute((Runnable) any);

            }
        };
        terminalShutDownQuartzTask.execute(quartzTaskContext);
        new Verifications() {
            {
                terminalShutDownQuartzTask.queryAll();
                times = 1;
            }
        };

    }

    /**
     * 测试关闭终端方法
     *
     * @throws NoSuchMethodException 异常
     * @throws InvocationTargetException 异常
     * @throws IllegalAccessException 异常
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteCloseTerminal() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, BusinessException {
        // 利用反射将私有方法暴露进行测试
        Class terminalShutDownQuartzTaskClass = TerminalShutDownQuartzTask.class;
        Method executeCloseTerminalMethod =
                terminalShutDownQuartzTaskClass.getDeclaredMethod("executeCloseTerminal", String.class, List.class, List.class);
        executeCloseTerminalMethod.setAccessible(true);

        UUID groupId = UUID.randomUUID();

        List<UUID> groupIdList = Lists.newArrayList();
        groupIdList.add(groupId);
        List<String> terminalIdList = Lists.newArrayList();
        TerminalDTO terminalDTO = new TerminalDTO();

        terminalDTO.setTerminalGroupId(groupId);
        terminalDTO.setTerminalState(CbbTerminalStateEnums.ONLINE);

        new MockUp<LocaleI18nResolver>() {
            @Mock
            public String resolve(String key, String... args) {
                return "x";
            }
        };
        new Expectations() {
            {
                userTerminalMgmtAPI.getTerminalById(anyString);
                result = terminalDTO;
            }
        };
        executeCloseTerminalMethod.invoke(terminalShutDownQuartzTask, "terminalId", groupIdList, terminalIdList);
        new Verifications() {
            {
                userTerminalMgmtAPI.getTerminalById(anyString);
                times = 1;
                cbbTerminalOperatorAPI.shutdown(anyString);
                times = 1;
                baseSystemLogMgmtAPI.createSystemLog((BaseCreateSystemLogRequest) any);
                times = 1;
            }
        };

    }

    /**
     * 测试关闭终端方法当配置不包含此终端时
     *
     * @throws NoSuchMethodException 异常
     * @throws InvocationTargetException 异常
     * @throws IllegalAccessException 异常
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteCloseTerminalWhileConfigNotContain()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, BusinessException {
        // 利用反射将私有方法暴露进行测试
        Class terminalShutDownQuartzTaskClass = TerminalShutDownQuartzTask.class;
        Method executeCloseTerminalMethod =
                terminalShutDownQuartzTaskClass.getDeclaredMethod("executeCloseTerminal", String.class, List.class, List.class);
        executeCloseTerminalMethod.setAccessible(true);

        UUID groupId = UUID.randomUUID();
        List<UUID> groupIdList = Lists.newArrayList();
        groupIdList.add(groupId);
        List<String> terminalIdList = Lists.newArrayList();
        TerminalDTO terminalDTO = new TerminalDTO();

        terminalDTO.setTerminalGroupId(UUID.randomUUID());
        terminalDTO.setTerminalState(CbbTerminalStateEnums.ONLINE);

        new Expectations() {
            {
                userTerminalMgmtAPI.getTerminalById(anyString);
                result = terminalDTO;
            }
        };
        executeCloseTerminalMethod.invoke(terminalShutDownQuartzTask, "terminalId", groupIdList, terminalIdList);
        executeCloseTerminalMethod.invoke(terminalShutDownQuartzTask, "terminalId", groupIdList, terminalIdList);
        new Verifications() {
            {

                userTerminalMgmtAPI.getTerminalById(anyString);
                times = 2;

            }
        };

    }

    /**
     * 测试关闭终端方法当状态发生变化时
     *
     * @throws NoSuchMethodException 异常
     * @throws InvocationTargetException 异常
     * @throws IllegalAccessException 异常
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteCloseTerminalWhileThrowBusinessException()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, BusinessException {
        // 利用反射将私有方法暴露进行测试
        Class terminalShutDownQuartzTaskClass = TerminalShutDownQuartzTask.class;
        Method executeCloseTerminalMethod =
                terminalShutDownQuartzTaskClass.getDeclaredMethod("executeCloseTerminal", String.class, List.class, List.class);
        executeCloseTerminalMethod.setAccessible(true);

        UUID groupId = UUID.randomUUID();
        List<UUID> groupIdList = Lists.newArrayList();
        groupIdList.add(groupId);
        List<String> terminalIdList = Lists.newArrayList();
        TerminalDTO terminalDTO = new TerminalDTO();

        terminalDTO.setTerminalGroupId(groupId);
        terminalDTO.setTerminalState(CbbTerminalStateEnums.OFFLINE);

        new MockUp<LocaleI18nResolver>() {
            @Mock
            public String resolve(String key, String... args) {

                return "";
            }
        };

        BusinessException e = new BusinessException("x");
        new Expectations() {
            {
                userTerminalMgmtAPI.getTerminalById(anyString);
                result = e;
            }
        };
        executeCloseTerminalMethod.invoke(terminalShutDownQuartzTask, "terminalId", groupIdList, terminalIdList);
        new Verifications() {
            {
                userTerminalMgmtAPI.getTerminalById(anyString);
                times = 1;
            }
        };

    }

    /**
     * 测试关闭终端方法当状态发生变化时
     *
     * @throws NoSuchMethodException 异常
     * @throws InvocationTargetException 异常
     * @throws IllegalAccessException 异常
     * @throws BusinessException 业务异常
     */
    @Test
    public void testExecuteCloseTerminalWhileStateChange()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, BusinessException {
        // 利用反射将私有方法暴露进行测试
        Class terminalShutDownQuartzTaskClass = TerminalShutDownQuartzTask.class;
        Method executeCloseTerminalMethod =
                terminalShutDownQuartzTaskClass.getDeclaredMethod("executeCloseTerminal", String.class, List.class, List.class);
        executeCloseTerminalMethod.setAccessible(true);

        UUID groupId = UUID.randomUUID();
        List<UUID> groupIdList = Lists.newArrayList();
        groupIdList.add(groupId);
        List<String> terminalIdList = Lists.newArrayList();
        TerminalDTO terminalDTO = new TerminalDTO();

        terminalDTO.setTerminalGroupId(groupId);
        terminalDTO.setTerminalState(CbbTerminalStateEnums.OFFLINE);

        new Expectations() {
            {
                userTerminalMgmtAPI.getTerminalById(anyString);
                result = terminalDTO;
            }
        };
        executeCloseTerminalMethod.invoke(terminalShutDownQuartzTask, "terminalId", groupIdList, terminalIdList);
        new Verifications() {
            {
                userTerminalMgmtAPI.getTerminalById(anyString);
                times = 1;
            }
        };

    }

    /**
     * 测试翻页查询方法
     * 
     * @throws BusinessException 业务异常
     */
    @Test
    public void testQueryByPage() throws BusinessException {
        PageRequest pageRequest = PageRequest.of(0, 2);

        DefaultPageResponse<TerminalDTO> terminalListDTODefaultPageResponse = new DefaultPageResponse<>();
        TerminalDTO terminalListDTO1 = new TerminalDTO();
        terminalListDTO1.setId("1");
        TerminalDTO terminalListDTO2 = new TerminalDTO();
        terminalListDTO2.setId("2");
        terminalListDTODefaultPageResponse.setItemArr(new TerminalDTO[] {terminalListDTO1, terminalListDTO2});

        new Expectations() {
            {
                userTerminalMgmtAPI.pageQuery((PageSearchRequest) any);
                result = terminalListDTODefaultPageResponse;
            }
        };
        List<String> resultList = terminalShutDownQuartzTask.queryByPage(pageRequest);
        Assert.assertTrue(resultList.size() == 2);
        Assert.assertEquals(resultList.get(0), terminalListDTO1.getId());
        Assert.assertEquals(resultList.get(1), terminalListDTO2.getId());
        new Verifications() {
            {
                userTerminalMgmtAPI.pageQuery((PageSearchRequest) any);
                times = 1;
            }
        };
    }
}
