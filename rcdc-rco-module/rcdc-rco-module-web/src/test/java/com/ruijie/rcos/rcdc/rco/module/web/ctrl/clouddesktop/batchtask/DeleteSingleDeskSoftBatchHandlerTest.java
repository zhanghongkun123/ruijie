package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDTO;
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
public class DeleteSingleDeskSoftBatchHandlerTest {

    @Tested
    private DeleteSingleDeskSoftBatchHandler handler;

    @Injectable
    private ProgrammaticOptLogRecorder optLogRecorder;

    @Injectable
    private CbbDeskSoftMgmtAPI deskSoftMgmtAPI;

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
                deskSoftMgmtAPI.deleteDeskSoft((UUID) any);
            }
        };
        BatchTaskItem defaultBatchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), "itemName");
        handler.processItem(defaultBatchTaskItem);

        new Verifications() {
            {
                deskSoftMgmtAPI.deleteDeskSoft((UUID) any);
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
                deskSoftMgmtAPI.deleteDeskSoft((UUID) any);
                result = new BusinessException("test");
            }
        };
        BatchTaskItem defaultBatchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), "itemName");
        handler.processItem(defaultBatchTaskItem);

        new Verifications() {
            {
                deskSoftMgmtAPI.deleteDeskSoft((UUID) any);
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
        Assert.assertEquals(batchTaskFinishResult.getMsgKey(), CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_SUCCESS_RESULT);
        batchTaskFinishResult = handler.onFinish(0, 1);
        Assert.assertEquals(batchTaskFinishResult.getStatus(), BatchTaskStatus.FAILURE);
        Assert.assertEquals(batchTaskFinishResult.getMsgKey(), CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DESKSOFT_DELETE_ITEM_FAIL_RESULT);
    }

}
