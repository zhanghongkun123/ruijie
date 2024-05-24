package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import mockit.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月23日
 *
 * @author zhuangchenwu
 */
@RunWith(SkyEngineRunner.class)
public class PowerOffDesktopBatchTaskHandlerTest {

    @Tested
    private PowerOffDesktopBatchTaskHandler handler;

    @Injectable
    private CloudDesktopWebService cloudDesktopWebService;

    @Injectable
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Injectable
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    @Injectable
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Injectable
    private BatchTaskItem mockBatchTaskItem;

    @Injectable
    private Iterator<? extends BatchTaskItem> mockIterator;

    @Injectable
    private ProgrammaticOptLogRecorder mockOptLogRecorder;

    @Injectable
    private AtomicInteger mockProcessItemCount = new AtomicInteger();


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
     * 关闭IDV云桌面
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItemWhenIDVDeskSuccess() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> handler.processItem(null), "taskItem is not null");

        UUID deskId = UUID.randomUUID();

        CloudDesktopDetailDTO cloudDesktopDetailDTO = getCloudDesktopDetailDTO(CbbCloudDeskType.IDV);

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = deskId;

                cloudDesktopWebService.obtainCloudDesktopResponse(deskId);
                result = cloudDesktopDetailDTO;
            }
        };

        handler.processItem(mockBatchTaskItem);

        new Verifications() {
            {
                cloudDesktopWebService.obtainCloudDesktopResponse(deskId);
                times = 1;

            }
        };
    }

    /**
     * 关闭VDI云桌面
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItemWhenVDIDeskSuccess() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> handler.processItem(null), "taskItem is not null");

        UUID deskId = UUID.randomUUID();

        CloudDesktopDetailDTO cloudDesktopDetailDTO = getCloudDesktopDetailDTO(CbbCloudDeskType.VDI);

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = deskId;

                cloudDesktopWebService.obtainCloudDesktopResponse(deskId);
                result = cloudDesktopDetailDTO;
            }
        };

        handler.processItem(mockBatchTaskItem);

        new Verifications() {
            {
                cloudDesktopWebService.obtainCloudDesktopResponse(deskId);
                times = 1;

                cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, Boolean.TRUE));
                times = 1;
            }
        };
    }

    /**
     * 关闭云桌面异常
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItemWhenDeskException() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> handler.processItem(null), "taskItem is not null");

        UUID deskId = UUID.randomUUID();

        CloudDesktopDetailDTO cloudDesktopDetailDTO = getCloudDesktopDetailDTO(CbbCloudDeskType.VDI);

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = deskId;

                cloudDesktopWebService.obtainCloudDesktopResponse(deskId);
                result = cloudDesktopDetailDTO;

                cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, Boolean.TRUE));
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
                cloudDesktopWebService.obtainCloudDesktopResponse(deskId);
                times = 1;

                cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, Boolean.TRUE));
                times = 1;
            }
        };
    }

    /**
     * testOnFinishWhenSuccess
     */
    @Test
    public void testOnFinishWhenSuccess() {

        int sucCount = 0;
        int failCount = 0;

        AtomicInteger atomicInteger = Deencapsulation.getField(handler, "processItemCount");
        atomicInteger.incrementAndGet();
        BatchTaskFinishResult batchTaskFinishResult = handler.onFinish(sucCount, failCount);
        Assert.assertTrue(batchTaskFinishResult.getStatus() == BatchTaskStatus.FAILURE);
    }

    /**
     * testOnFinishWhenFail
     */
    @Test
    public void testOnFinishWhenFail() {

        int sucCount = 1;
        int failCount = 0;

        AtomicInteger atomicInteger = Deencapsulation.getField(handler, "processItemCount");
        atomicInteger.incrementAndGet();
        BatchTaskFinishResult batchTaskFinishResult = handler.onFinish(sucCount, failCount);
        Assert.assertTrue(batchTaskFinishResult.getStatus() == BatchTaskStatus.SUCCESS);
    }

    /**
     * testOnFinishWhenFail
     */
    @Test
    public void testOnFinish() {

        int sucCount = 1;
        int failCount = 0;

        AtomicInteger atomicInteger = Deencapsulation.getField(handler, "processItemCount");
        BatchTaskFinishResult batchTaskFinishResult = handler.onFinish(sucCount, failCount);
        Assert.assertTrue(batchTaskFinishResult.getStatus() == BatchTaskStatus.SUCCESS);
    }

    private CloudDesktopDetailDTO getCloudDesktopDetailDTO(CbbCloudDeskType type) {
        CloudDesktopDetailDTO cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
        cloudDesktopDetailDTO.setDesktopCategory(type.name());
        cloudDesktopDetailDTO.setUserName("userName");
        cloudDesktopDetailDTO.setDesktopName("deskName");

        return cloudDesktopDetailDTO;
    }

}
