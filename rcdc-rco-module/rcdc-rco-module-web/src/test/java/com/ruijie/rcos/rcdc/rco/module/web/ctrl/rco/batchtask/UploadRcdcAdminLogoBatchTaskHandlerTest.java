package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.batchtask;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.ruijie.rcos.rcdc.rco.module.def.api.RcdcThemeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.UploadPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.RcoBusinessKey;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

import mockit.*;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/21 15:43
 *
 * @author conghaifeng
 */
public class UploadRcdcAdminLogoBatchTaskHandlerTest {

    @Tested
    private UploadRcdcAdminLogoBatchTaskHandler handler;

    @Injectable
    private ProgrammaticOptLogRecorder optLogRecorder;

    @Injectable
    private ChunkUploadFile file;

    @Injectable
    private RcdcThemeAPI rcdcThemeAPI;

    @Mocked
    private LocaleI18nResolver localeI18nResolver;

    @Injectable
    BatchTaskItem batchTaskItem;

    /**
     * 测试ProcessItem正常
     * @throws BusinessException 业务异常
     */
    @Test
    public void testProcessItem() throws BusinessException {
        new Expectations() {
            {
                rcdcThemeAPI.uploadPicture((UploadPictureRequest) any);
            }
        };
        BatchTaskItem defaultBatchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), "itemName");
        handler.processItem(defaultBatchTaskItem);

        new Verifications() {
            {
                rcdcThemeAPI.uploadPicture((UploadPictureRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 测试processItemFail
     * @throws BusinessException 业务异常
     */
    @Test
    public void testProcessItemFail() throws BusinessException {
        new Expectations() {
            {
                rcdcThemeAPI.uploadPicture((UploadPictureRequest) any);
                result = new BusinessException("test");
            }
        };
        BatchTaskItem defaultBatchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), "itemName");
        handler.processItem(defaultBatchTaskItem);

        new Verifications() {
            {
                rcdcThemeAPI.uploadPicture((UploadPictureRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 测试OnFinish方法
     */
    @Test
    public void testOnFinish() {
        BatchTaskFinishResult batchTaskFinishResult = handler.onFinish(1, 0);
        Assert.assertEquals(batchTaskFinishResult.getStatus(), BatchTaskStatus.SUCCESS);
        Assert.assertEquals(batchTaskFinishResult.getMsgKey(), RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_ADMIN_LOGO_TASK_SUCCESS);
        batchTaskFinishResult = handler.onFinish(0, 1);
        Assert.assertEquals(batchTaskFinishResult.getStatus(), BatchTaskStatus.FAILURE);
        Assert.assertEquals(batchTaskFinishResult.getMsgKey(), RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_ADMIN_LOGO_TASK_FAIL);
    }

}