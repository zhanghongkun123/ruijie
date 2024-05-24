package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.imagedriver;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageDriverDTO;
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
public class DeleteDriverIsoBatchHandlerTest {

    @Tested
    DeleteDriverIsoBatchHandler handler;

    @Injectable
    private ProgrammaticOptLogRecorder optLogRecorder;

    @Injectable
    private CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI;

    @Injectable
    private BatchTaskItem item;

    @Mocked
    private LocaleI18nResolver localeI18nResolver;

    /**
     * 测试ProcessItem正常
     * @throws BusinessException 业务异常
     */
    @Test
    public void testProcessItem() throws BusinessException {
        CbbImageDriverDTO driverDTO = new CbbImageDriverDTO();
        driverDTO.setDriverName("name");

        new Expectations() {
            {
                cbbImageDriverMgmtAPI.findImageDriverInfo((UUID) any);
                result = driverDTO;
                cbbImageDriverMgmtAPI.deleteImageDriverFile((UUID) any);
            }
        };
        BatchTaskItem defaultBatchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), "itemName");
        handler.processItem(defaultBatchTaskItem);

        new Verifications() {
            {
                cbbImageDriverMgmtAPI.findImageDriverInfo((UUID) any);
                times = 1;
                cbbImageDriverMgmtAPI.deleteImageDriverFile((UUID) any);
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
                cbbImageDriverMgmtAPI.findImageDriverInfo((UUID) any);
                result = new BusinessException("error");
            }
        };
        BatchTaskItem defaultBatchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), "itemName");
        handler.processItem(defaultBatchTaskItem);

        new Verifications() {
            {
                cbbImageDriverMgmtAPI.findImageDriverInfo((UUID) any);
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
        Assert.assertEquals(batchTaskFinishResult.getMsgKey(), CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_SUCCESS_RESULT);
        batchTaskFinishResult = handler.onFinish(0, 1);
        Assert.assertEquals(batchTaskFinishResult.getStatus(), BatchTaskStatus.FAILURE);
        Assert.assertEquals(batchTaskFinishResult.getMsgKey(), CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_FAIL_RESULT);
    }
}
