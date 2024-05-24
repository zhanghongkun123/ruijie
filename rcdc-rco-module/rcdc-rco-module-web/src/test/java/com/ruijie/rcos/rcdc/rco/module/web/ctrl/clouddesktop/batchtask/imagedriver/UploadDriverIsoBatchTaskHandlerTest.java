package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.imagedriver;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUploadImageDriverDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/23
 *
 * @author XiaoJiaXin
 */
@RunWith(SkyEngineRunner.class)
public class UploadDriverIsoBatchTaskHandlerTest {

    @Tested
    private UploadDriverIsoBatchTaskHandler handler;

    @Injectable
    private ProgrammaticOptLogRecorder optLogRecorder;

    @Injectable
    private CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI;

    @Injectable
    private ChunkUploadFile file;

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
        CbbDeskNetworkDTO cbbDeskNetworkDTO = new CbbDeskNetworkDTO();
        cbbDeskNetworkDTO.setDeskNetworkName("name");
        DtoResponse<CbbDeskNetworkDTO> resp = DtoResponse.success(cbbDeskNetworkDTO);
        new Expectations() {
            {
                cbbImageDriverMgmtAPI.uploadImageDriverFile((CbbUploadImageDriverDTO) any);
            }
        };
        BatchTaskItem defaultBatchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), "itemName");
        handler.processItem(defaultBatchTaskItem);

        new Verifications() {
            {
                cbbImageDriverMgmtAPI.uploadImageDriverFile((CbbUploadImageDriverDTO) any);
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
        CbbDeskNetworkDTO cbbDeskNetworkDTO = new CbbDeskNetworkDTO();
        cbbDeskNetworkDTO.setDeskNetworkName("name");
        DtoResponse<CbbDeskNetworkDTO> resp = DtoResponse.success(cbbDeskNetworkDTO);
        new Expectations() {
            {
                cbbImageDriverMgmtAPI.uploadImageDriverFile((CbbUploadImageDriverDTO) any);
                result = new BusinessException("test");
            }
        };
        BatchTaskItem defaultBatchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), "itemName");
        handler.processItem(defaultBatchTaskItem);

        new Verifications() {
            {
                cbbImageDriverMgmtAPI.uploadImageDriverFile((CbbUploadImageDriverDTO) any);
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
        Assert.assertEquals(batchTaskFinishResult.getMsgKey(), CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_SUCCESS);
        batchTaskFinishResult = handler.onFinish(0, 1);
        Assert.assertEquals(batchTaskFinishResult.getStatus(), BatchTaskStatus.FAILURE);
        Assert.assertEquals(batchTaskFinishResult.getMsgKey(), CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_FAIL);
    }
}
