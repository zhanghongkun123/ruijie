package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import mockit.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年6月16日
 *
 * @author zhuangchenwu
 */
@RunWith(SkyEngineRunner.class)
public class RecoverRecycleBinByAssignUserBatchTaskHandlerTest {

    @Tested
    private RecoverRecycleBinByAssignUserBatchTaskHandler handler;

    @Injectable
    private UserRecycleBinMgmtAPI recycleBinMgmtAPI;

    @Injectable
    private BatchTaskItem mockBatchTaskItem;

    @Injectable
    private Iterator<? extends BatchTaskItem> mockIterator;

    @Injectable
    private ProgrammaticOptLogRecorder mockOptLogRecorder;
    
    @Injectable
    private String deskName;
    
    @Injectable
    private UUID assignUserId;

    @Injectable
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

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
     * 恢复桌面成功
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItemWhenSuccess() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> handler.processItem(null), "taskItem can not be null");

        UUID deskId = UUID.randomUUID();

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = deskId;
            }
        };

        BatchTaskItemResult result = handler.processItem(mockBatchTaskItem);
        assertEquals(BatchTaskItemStatus.SUCCESS, result.getItemStatus());
        assertEquals(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_ITEM_SUC_DESC, result.getMsgKey());
    }

    /**
     * 恢复云桌面异常
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItemWhenException() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> handler.processItem(null), "taskItem can not be null");

        UUID deskId = UUID.randomUUID();

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = deskId;

                recycleBinMgmtAPI.recover(deskId, assignUserId);
                result = new BusinessException("error");
            }
        };

        BatchTaskItemResult result = handler.processItem(mockBatchTaskItem);
        assertEquals(BatchTaskItemStatus.FAILURE, result.getItemStatus());
        assertEquals(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_ITEM_FAIL_DESC, result.getMsgKey());
    }

    /**
     * 恢复云桌面异常
     *
     * @throws Exception 异常
     */
    @Test
    public void testProcessItemWhenExceptionAndDeskNameNotNull() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> handler.processItem(null), "taskItem can not be null");

        UUID deskId = UUID.randomUUID();

        new Expectations() {
            {
                mockBatchTaskItem.getItemID();
                result = deskId;

                recycleBinMgmtAPI.recover(deskId, assignUserId);
                result = new BusinessException("error");
            }
        };

        Deencapsulation.setField(handler, "deskName", "deskName");
        BatchTaskItemResult result = handler.processItem(mockBatchTaskItem);
        assertEquals(BatchTaskItemStatus.FAILURE, result.getItemStatus());
        assertEquals(UserBusinessKey.RCDC_RCO_RECYCLEBIN_RECOVER_ASSIGN_USER_ITEM_FAIL_DESC, result.getMsgKey());
    }

    /**
     * testOnFinishWhenFailAndDeskNameNotNull
     */
    @Test
    public void testOnFinishWhenFailAndDeskNameNotNull() {

        int sucCount = 0;
        int failCount = 1;

        Deencapsulation.setField(handler, "deskName", "deskName");
        BatchTaskFinishResult batchTaskFinishResult = handler.onFinish(sucCount, failCount);
        assertTrue(batchTaskFinishResult.getStatus() == BatchTaskStatus.FAILURE);
    }

    /**
     * testOnFinishWhenSuccesslAndDeskNameNotNull
     */
    @Test
    public void testOnFinishWhenSuccesslAndDeskNameNotNull() {

        int sucCount = 1;
        int failCount = 0;

        Deencapsulation.setField(handler, "deskName", "deskName");
        BatchTaskFinishResult batchTaskFinishResult = handler.onFinish(sucCount, failCount);
        assertTrue(batchTaskFinishResult.getStatus() == BatchTaskStatus.SUCCESS);
    }

    /**
     * testOnFinishWhenFailAndDeskNameNull
     */
    @Test
    public void testOnFinishWhenFailAndDeskNameNull() {

        int sucCount = 0;
        int failCount = 1;

        BatchTaskFinishResult batchTaskFinishResult = handler.onFinish(sucCount, failCount);
        assertTrue(batchTaskFinishResult.getStatus() == BatchTaskStatus.FAILURE);
    }

    /**
     * testOnFinishWhenSuccesslAndDeskNameNull
     */
    @Test
    public void testOnFinishWhenSuccesslAndDeskNameNull() {

        int sucCount = 1;
        int failCount = 0;

        BatchTaskFinishResult batchTaskFinishResult = handler.onFinish(sucCount, failCount);
        assertTrue(batchTaskFinishResult.getStatus() == BatchTaskStatus.SUCCESS);
    }

}
