package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLogoAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbUploadLogoDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/23
 *
 * @author XiaoJiaXin
 */
public class UploadTerminalLogoBatchTaskHandlerTest {


    @Tested
    private UploadTerminalLogoBatchTaskHandler handler;

    @Injectable
    private ProgrammaticOptLogRecorder optLogRecorder;

    @Injectable
    private ChunkUploadFile file;

    @Injectable
    private CbbTerminalLogoAPI terminalLogoAPI;

    @Mocked
    private LocaleI18nResolver localeI18nResolver;

    @Injectable
    BatchTaskItem batchTaskItem;

    /**
     * 测试ProcessItem正常
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testProcessItem() throws BusinessException {
        new MockUp<ChunkUploadFile>() {
            @Mock
            public String getFilePath() {
                return "filePath";
            }
            @Mock
            public String getFileName() {
                return "fileName";
            }
            @Mock
            public String getFileMD5() {
                return "MD5";
            }
        };
        new Expectations() {
            {
                terminalLogoAPI.uploadLogo((CbbUploadLogoDTO) any);
            }
        };
        BatchTaskItem defaultBatchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), "itemName");
        handler.processItem(defaultBatchTaskItem);

        new Verifications() {
            {
                terminalLogoAPI.uploadLogo((CbbUploadLogoDTO) any);
                times = 1;
            }
        };
    }

    /**
     * 测试processItemFail
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testProcessItemFail() throws BusinessException {
        new MockUp<ChunkUploadFile>() {
            @Mock
            public String getFilePath() {
                return "filePath";
            }
            @Mock
            public String getFileName() {
                return "fileName";
            }
            @Mock
            public String getFileMD5() {
                return "MD5";
            }
        };
        new Expectations() {
            {
                terminalLogoAPI.uploadLogo((CbbUploadLogoDTO) any);
                result = new BusinessException("test");
            }
        };
        BatchTaskItem defaultBatchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), "itemName");
        handler.processItem(defaultBatchTaskItem);

        new Verifications() {
            {
                terminalLogoAPI.uploadLogo((CbbUploadLogoDTO) any);
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
        Assert.assertEquals(batchTaskFinishResult.getMsgKey(), TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_LOGO_TASK_SUCCESS);
        batchTaskFinishResult = handler.onFinish(0, 1);
        Assert.assertEquals(batchTaskFinishResult.getStatus(), BatchTaskStatus.FAILURE);
        Assert.assertEquals(batchTaskFinishResult.getMsgKey(), TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_LOGO_TASK_FAIL);
    }
}
