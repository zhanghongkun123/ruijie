package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.PageRequest;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.request.systemlog.BaseCreateSystemLogRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.test.GetSetTester;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月08日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class AbstractCloudDeskQuartzTaskTest {
    @Tested
    private AbstractCloudDeskQuartzTask abstractCloudDeskQuartzTask;

    @Injectable
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Injectable
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    /**
     * 测试校验方法
     * 
     * @param cloudDeskQuartzData 数据
     * @throws BusinessException 业务异常
     */
    @Test
    public void testValidate(@Injectable AbstractCloudDeskQuartzTask.CloudDeskQuartzData cloudDeskQuartzData) throws BusinessException {
        new Expectations(JSON.class) {
            {
                JSON.parseObject(anyString, AbstractCloudDeskQuartzTask.CloudDeskQuartzData.class);
                result = cloudDeskQuartzData;
                cloudDeskQuartzData.getDeskArr();
                returns(new UUID[] {UUID.randomUUID()}, new UUID[] {UUID.randomUUID()}, new UUID[] {UUID.randomUUID()}, new UUID[0], new UUID[0],
                        new UUID[0]);
                cloudDeskQuartzData.getUserArr();
                returns(new UUID[] {UUID.randomUUID()}, new UUID[] {UUID.randomUUID()}, new UUID[0], new UUID[] {UUID.randomUUID()},
                        new UUID[] {UUID.randomUUID()}, new UUID[0]);

                cloudDeskQuartzData.getUserGroupArr();
                returns(new UUID[] {UUID.randomUUID()}, new UUID[0], new UUID[] {UUID.randomUUID()}, new UUID[] {UUID.randomUUID()}, new UUID[0],
                        new UUID[0]);
            }
        };
        abstractCloudDeskQuartzTask.validate("");
        abstractCloudDeskQuartzTask.validate("");
        abstractCloudDeskQuartzTask.validate("");
        abstractCloudDeskQuartzTask.validate("");
        abstractCloudDeskQuartzTask.validate("");
        try {
            abstractCloudDeskQuartzTask.validate("");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "用户组、用户、云桌面不能同时为空");
        }

        new Verifications() {
            {
                JSON.parseObject(anyString, AbstractCloudDeskQuartzTask.CloudDeskQuartzData.class);
                times = 6;
            }
        };
    }

    /**
     * 测试分页查询方法
     * 
     * @param cloudDesktopDTOResponse 分页查询响应结果
     * @throws BusinessException 业务异常
     */
    @Test
    public void testQueryByPage(@Injectable DefaultPageResponse<CloudDesktopDTO> cloudDesktopDTOResponse) throws BusinessException {

        CloudDesktopDTO cloudDesktopDTO1 = new CloudDesktopDTO();
        cloudDesktopDTO1.setId(UUID.randomUUID());

        CloudDesktopDTO cloudDesktopDTO2 = new CloudDesktopDTO();
        cloudDesktopDTO2.setId(UUID.randomUUID());

        new Expectations() {
            {
                userDesktopMgmtAPI.pageQuery((PageSearchRequest) any);
                result = cloudDesktopDTOResponse;
                cloudDesktopDTOResponse.getItemArr();
                result = new CloudDesktopDTO[] {cloudDesktopDTO1, cloudDesktopDTO2};
            }
        };
        PageRequest pageRequest = PageRequest.of(0, 1000);
        List<UUID> resultList = abstractCloudDeskQuartzTask.queryByPage(pageRequest);
        Assert.assertTrue(resultList.contains(cloudDesktopDTO1.getId()));
        Assert.assertTrue(resultList.contains(cloudDesktopDTO2.getId()));
        Assert.assertTrue(resultList.size() == 2);
        new Verifications() {
            {
                userDesktopMgmtAPI.pageQuery((PageSearchRequest) any);
                times = 1;
                cloudDesktopDTOResponse.getItemArr();
                times = 1;
            }
        };
    }

    /**
     * 测试execute方法
     * 
     * @param quartzTaskContext 定时任务上下文
     * @param logger 日志对象
     * @throws Exception 异常
     */
    @Test
    public void testExecute(@Mocked QuartzTaskContext quartzTaskContext, @Mocked Logger logger) throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> abstractCloudDeskQuartzTask.execute(null), "quartzTaskContext can not be null");
        AbstractCloudDeskQuartzTask.CloudDeskQuartzData cloudDeskQuartzData = new AbstractCloudDeskQuartzTask.CloudDeskQuartzData();
        cloudDeskQuartzData.setDeskArr(new UUID[] {UUID.randomUUID()});
        new Expectations(abstractCloudDeskQuartzTask) {
            {
                quartzTaskContext.getByType(AbstractCloudDeskQuartzTask.CloudDeskQuartzData.class);
                result = cloudDeskQuartzData;
                abstractCloudDeskQuartzTask.queryAll();
                result = Sets.newHashSet(UUID.randomUUID());
            }
        };
        abstractCloudDeskQuartzTask.execute(quartzTaskContext);
        new Verifications() {
            {
                quartzTaskContext.getByType(AbstractCloudDeskQuartzTask.CloudDeskQuartzData.class);
                times = 1;
                abstractCloudDeskQuartzTask.queryAll();
                times = 1;
//                logger.debug("云桌面操作定时任务执行结束");
//                times = 1;
            }
        };
    }


    /**
     * 测试execute方法
     *
     * @param quartzTaskContext 定时任务上下文
     * @param logger 日志对象
     * @throws Exception 异常
     */
    @Test
    public void testExecuteWhileIsDebug(@Mocked QuartzTaskContext quartzTaskContext, @Mocked Logger logger) throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> abstractCloudDeskQuartzTask.execute(null), "quartzTaskContext can not be null");
        AbstractCloudDeskQuartzTask.CloudDeskQuartzData cloudDeskQuartzData = new AbstractCloudDeskQuartzTask.CloudDeskQuartzData();
        cloudDeskQuartzData.setUserGroupArr(new UUID[] {UUID.randomUUID()});
        new MockUp<Logger>() {
            @Mock
            public boolean isDebugEnabled() {
                return true;
            }
        };
        new Expectations(abstractCloudDeskQuartzTask) {
            {
                quartzTaskContext.getByType(AbstractCloudDeskQuartzTask.CloudDeskQuartzData.class);
                result = cloudDeskQuartzData;
                abstractCloudDeskQuartzTask.queryAll();
                result = Sets.newHashSet(UUID.randomUUID());
            }
        };
        abstractCloudDeskQuartzTask.execute(quartzTaskContext);
        new Verifications() {
            {
//                logger.debug("云桌面定时任务配置涉及所有用户组id[{}]", JSON.toJSONString(cloudDeskQuartzData.getUserGroupArr()));
//                times = 1;
                quartzTaskContext.getByType(AbstractCloudDeskQuartzTask.CloudDeskQuartzData.class);
                times = 1;
                abstractCloudDeskQuartzTask.queryAll();
                times = 1;
//                logger.debug("云桌面操作定时任务执行结束");
//                times = 1;
            }
        };
    }

    /**
     * 测试实体
     */
    @Test
    public void testPojo() {
        GetSetTester getSetTester = new GetSetTester(AbstractCloudDeskQuartzTask.CloudDeskQuartzData.class);
        getSetTester.runTest();
        Assert.assertTrue(true);
    }

    /**
     * 测试添加系统日志方法
     */
    @Test
    public void testAddSystemLog() {
        String businessKey = "key";
        String[] argArr = new String[] {"arg"};
        abstractCloudDeskQuartzTask.addSystemLog(businessKey, argArr);
        new Verifications() {
            {
                baseSystemLogMgmtAPI.createSystemLog((BaseCreateSystemLogRequest) any);
                times = 1;
            }
        };
    }



}
