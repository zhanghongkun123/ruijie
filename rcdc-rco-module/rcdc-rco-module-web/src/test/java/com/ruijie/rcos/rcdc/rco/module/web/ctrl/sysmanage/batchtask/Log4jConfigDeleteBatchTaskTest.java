package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask;

import com.ruijie.rcos.base.sysmanage.module.def.api.Log4jConfigAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.response.log4j.BaseDeleteLog4jConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.SysmanagerBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Description: 批量删除log4j配置任务handler
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年01月16日
 *
 * @author GuoZhouYue
 */
public class Log4jConfigDeleteBatchTaskTest {

    @Tested
    private Log4jConfigDeleteBatchTask log4jConfigDeleteBatchTask;

    @Injectable
    private Iterator<BatchTaskItem> iterator;

    @Injectable
    private Log4jConfigAPI log4jConfigAPI;

    @Mocked
    @Injectable
    private ProgrammaticOptLogRecorder logRecorder;

    /**
     * test
     * @param batchTaskItem item
     * @param response response 
     * @throws BusinessException exception
     */
    @Test
    public void testProcessItem(@Injectable BatchTaskItem batchTaskItem, @Injectable BaseDeleteLog4jConfigResponse response) 
            throws BusinessException {
        new Expectations() {
            {
                batchTaskItem.getItemID();
                result = UUID.randomUUID();

                log4jConfigAPI.removeConfig((UUID) any);
                result = response;

                response.getLoggerName();
                result = "loggerName";
            }
        };

        BatchTaskItemResult batchTaskItemResult = log4jConfigDeleteBatchTask.processItem(batchTaskItem);

        new Verifications() {
            {
                batchTaskItem.getItemID();
                times = 1;

                log4jConfigAPI.removeConfig((UUID) any);
                times = 1;

                response.getLoggerName();
                times = 2;
            }
        };
        Assert.assertEquals(batchTaskItemResult.getItemStatus(), BatchTaskItemStatus.SUCCESS);
        Assert.assertEquals(batchTaskItemResult.getMsgKey(), SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_GRADE_TASK_ITEM_SUCCESS);
    }

    /**
     * test
     * @param batchTaskItem item
     * @throws BusinessException exception
     */
    @Test
    public void testProcessItemBusizExp(@Injectable BatchTaskItem batchTaskItem) throws BusinessException {
        new Expectations() {
            {
                batchTaskItem.getItemID();
                result = UUID.randomUUID();

                log4jConfigAPI.removeConfig((UUID) any);
                result = new BusinessException("xxx");
            }
        };

        new MockUp<BusinessException>() {
            @Mock
            public String getI18nMessage() {
                return "";
            }
        };

        BatchTaskItemResult batchTaskItemResult = log4jConfigDeleteBatchTask.processItem(batchTaskItem);

        new Verifications() {
            {
                batchTaskItem.getItemID();
                times = 1;

                log4jConfigAPI.removeConfig((UUID) any);
                times = 1;
            }
        };
        Assert.assertEquals(batchTaskItemResult.getItemStatus(), BatchTaskItemStatus.FAILURE);
        Assert.assertEquals(batchTaskItemResult.getMsgKey(), SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_GRADE_TASK_ITEM_FAIL);
    }

    /**
     * testOnFinish
     */
    @Test
    public void testOnFinish() {
        BatchTaskFinishResult result = log4jConfigDeleteBatchTask.onFinish(1, 0);

        assertEquals(BatchTaskStatus.SUCCESS, result.getStatus());
        assertEquals(SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_GRADE_TASK_RESULT, result.getMsgKey());
        assertEquals(2, result.getMsgArgs().length);
        assertEquals("1", result.getMsgArgs()[0]);
        assertEquals("0", result.getMsgArgs()[1]);
    }
}
