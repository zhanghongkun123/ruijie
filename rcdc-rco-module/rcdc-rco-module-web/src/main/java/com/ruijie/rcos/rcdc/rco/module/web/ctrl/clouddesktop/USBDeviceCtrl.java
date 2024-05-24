package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBDeviceMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.usbdevice.CbbUSBDevicePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateUSBDeviceDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetUSBDeviceResultDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBDeviceDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUpdateUSBDeviceDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbdevice.CreateUSBDeviceWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbdevice.DeleteUSBDeviceBatchWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbdevice.GetUSBDeviceByUSBTypeWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbdevice.GetUSBDeviceWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbdevice.UpdateUSBDeviceWebRequest;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

/**
 * Description: USB设备controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月26日
 *
 * @author Ghang
 */
@Controller
@RequestMapping("/rco/usbDevice")
public class USBDeviceCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(USBDeviceCtrl.class);

    private static final int INT_1 = 1;

    private static final int INT_2 = 2;

    private static final int INT_3 = 3;

    private static final int INT_4 = 4;

    private static final String BLANK_FLAG = "-";

    private static final String USB_TYPE_ID = "usbTypeId";

    @Autowired
    private CbbUSBDeviceMgmtAPI cbbUSBDeviceMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 创建USB设备
     *
     * @param request        請求参数
     * @return DefaultWebResponse 返回值
     * @throws BusinessException 异常
     */
    @RequestMapping("create")
    public DefaultWebResponse createUSBDevice(CreateUSBDeviceWebRequest request) throws BusinessException {
        Assert.notNull(request, "CreateUSBDeviceWebRequest is null");
        CbbCreateUSBDeviceDTO apiRequest = new CbbCreateUSBDeviceDTO();
        apiRequest.setFirm(request.getFirm());
        apiRequest.setFirmFlag(request.getFirmFlag());
        apiRequest.setNote(request.getNote());
        apiRequest.setProduct(request.getProduct());
        apiRequest.setProductFlag(request.getProductFlag());
        apiRequest.setUsbTypeId(request.getUsbTypeId());
        try {
            cbbUSBDeviceMgmtAPI.createUSBDevice(apiRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_CREATE_SUCCESS,
                    fillData(request.getFirmFlag()), fillData(request.getProductFlag()));
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_CREATE_FAIL, e,
                    fillData(request.getFirmFlag()), fillData(request.getProductFlag()), e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }

    }

    /**
     * 更新USB设备信息
     *
     * @param request        請求参数
     * @return DefaultWebResponse 返回值
     * @throws BusinessException 异常
     */
    @RequestMapping("edit")
    public DefaultWebResponse updateUSBDevice(UpdateUSBDeviceWebRequest request) throws BusinessException {
        Assert.notNull(request, "UpdateUSBDeviceWebRequest is null");
        CbbUpdateUSBDeviceDTO apiRequest = new CbbUpdateUSBDeviceDTO();
        apiRequest.setId(request.getId());
        apiRequest.setNote(request.getNote());
        apiRequest.setUsbTypeId(request.getUsbTypeId());
        CbbGetUSBDeviceResultDTO response = getUSBDeviceDetail(request.getId());
        try {
            cbbUSBDeviceMgmtAPI.updateUSBDevice(apiRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_UPDATE_SUCCESS,
                    Objects.requireNonNull(response).getFirmFlag(), response.getProductFlag());
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            if (response == null) {
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_NOT_EXIST_UPDATE_FAIL, e,
                        request.getId().toString(), e.getI18nMessage());
            } else {
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_UPDATE_FAIL, e,
                        response.getFirmFlag(), response.getProductFlag(), e.getI18nMessage());
            }
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }


    /**
     * *根据USB设备类型ID获取USB设备
     *
     * @param request 請求参数
     * @return DefaultWebResponse 返回值
     * @throws BusinessException 异常
     */
    @RequestMapping("list")
    public DefaultWebResponse getUSBDevicePageByUSBType(GetUSBDeviceByUSBTypeWebRequest request) throws BusinessException {
        Assert.notNull(request, "GetUSBDeviceByUSBTypeWebRequest is null");
        CbbUSBDevicePageRequest apiRequest = new CbbUSBDevicePageRequest();
        ExactMatch[] itemArr = request.getExactMatchArr();
        UUID usbTypeId = getUsbTypeId(itemArr);
        apiRequest.setUsbTypeId(usbTypeId);
        apiRequest.setLimit(request.getLimit());
        apiRequest.setPage(request.getPage());
        DefaultPageResponse<CbbUSBDeviceDTO> response = cbbUSBDeviceMgmtAPI.pageQueryUSBDevice(apiRequest);
        return DefaultWebResponse.Builder.success(response);
    }

    private UUID getUsbTypeId(ExactMatch[] itemArr) throws BusinessException {
        if (Objects.isNull(itemArr)) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_EXACTMATCH_ARR_NOT_EMPTY, new String[]{});
        }

        for (ExactMatch item : itemArr) {
            if (Objects.equals(item.getName(), USB_TYPE_ID) && StringUtils.isNotEmpty(item.getValueArr()[0])) {
                return UUID.fromString(item.getValueArr()[0]);
            }
        }
        throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_ID_NOT_EMPTY, new String[]{});
    }

    /**
     * 根据ID获取USB设备详情。
     *
     * @param request 請求参数
     * @return DefaultWebResponse 返回值
     * @throws BusinessException 异常
     */
    @RequestMapping("detail")
    public DefaultWebResponse getUSBDevice(GetUSBDeviceWebRequest request) throws BusinessException {
        Assert.notNull(request, "GetUSBDeviceWebRequest is null");
        CbbGetUSBDeviceResultDTO response = cbbUSBDeviceMgmtAPI.getUSBDevice(request.getId());
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 批量删除USB设备
     *
     * @param request        請求参数
     * @param builder        构建器
     * @return DefaultWebResponse 返回值
     * @throws BusinessException 异常
     */
    @RequestMapping("delete")
    public DefaultWebResponse deleteUSBDeviceBatch(DeleteUSBDeviceBatchWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "DeleteUSBDeviceBatchWebRequest is null");
        Assert.notNull(builder, "builder is null");

        final UUID[] idArr = request.getIdArr();
        if (idArr.length == 1) {
            return deleteSingleRecord(idArr[0]);
        } else {
            final Iterator<DefaultBatchTaskItem> iterator =
                    Stream.of(idArr).distinct()
                            .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(LocaleI18nResolver.resolve(
                                            CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTDEVICE_BATCH_DELETE_ITEM_NAME))
                                    .build())
                            .iterator();
            USBDeviceBatchDeleteHandler handler =
                    new USBDeviceBatchDeleteHandler(auditLogAPI, iterator, cbbUSBDeviceMgmtAPI);
            BatchTaskSubmitResult result =
                    builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_BATCH_DELETE_TASK_NAME)
                            .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_BATCH_DELETE_TASK_DESC)
                            .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);
        }
    }

    private DefaultWebResponse deleteSingleRecord(UUID id) throws BusinessException {
        CbbGetUSBDeviceResultDTO response = getUSBDeviceDetail(id);
        try {
            cbbUSBDeviceMgmtAPI.deleteUSBDevice(id);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_DELETE_SUCCESS,
                    Objects.requireNonNull(response).getFirmFlag(), response.getProductFlag());
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("删除USB外设", e);
            if (response == null) {
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_DELETE_FAIL, e,
                        id.toString(), BLANK_FLAG, e.getI18nMessage());
            } else {
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_DELETE_FAIL, e,
                        response.getFirmFlag(), response.getProductFlag(), e.getI18nMessage());
            }

            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 填充字符串为6位
     *
     * @param data
     * @return
     */
    private String fillData(String data) {
        if (Objects.equals(data, "-1")) {
            return data;
        }

        if (data.length() == INT_1) {
            data = "0x000" + data;
        }

        if (data.length() == INT_2) {
            data = "0x00" + data;
        }

        if (data.length() == INT_3) {
            data = "0x0" + data;
        }

        if (data.length() == INT_4) {
            data = "0x" + data;
        }
        return data;
    }

    /**
     * Description: USB批处理handler
     * Copyright: Copyright (c) 2019
     * Company: Ruijie Co., Ltd.
     * Create Time: 2019年1月18日
     *
     * @author Ghang
     */
    protected class USBDeviceBatchDeleteHandler extends AbstractBatchTaskHandler {

        private BaseAuditLogAPI auditLogAPI;

        private final CbbUSBDeviceMgmtAPI cbbUSBDeviceMgmtAPI;

        protected USBDeviceBatchDeleteHandler(BaseAuditLogAPI auditLogAPI,
                                              Iterator<? extends BatchTaskItem> iterator, CbbUSBDeviceMgmtAPI cbbUSBDeviceMgmtAPI) {
            super(iterator);
            this.cbbUSBDeviceMgmtAPI = cbbUSBDeviceMgmtAPI;
            this.auditLogAPI = auditLogAPI;
        }

        @Override
        public BatchTaskFinishResult onFinish(int successCount, int failCount) {
            return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount,
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_BATCH_DELETE_TASK_RESULT);
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
            CbbGetUSBDeviceResultDTO response = getUSBDeviceDetail(item.getItemID());
            try {
                cbbUSBDeviceMgmtAPI.deleteUSBDevice(item.getItemID());
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_DELETE_SUCCESS,
                        Objects.requireNonNull(response).getFirmFlag(), response.getProductFlag());
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_DELETE_SUCCESS)
                        .msgArgs(new String[]{response.getFirmFlag(), response.getProductFlag()}).build();
            } catch (BusinessException e) {
                LOGGER.error("删除USB外设", e);
                if (response == null) {
                    auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_NOT_EXIST_DELETE_FAIL,
                            item.getItemID().toString(), e.getI18nMessage());
                    throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_DELETE_FAIL, e, item.getItemID().toString(),
                            BLANK_FLAG, e.getI18nMessage());
                } else {
                    auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_DELETE_FAIL,
                            response.getFirmFlag(), response.getProductFlag(), e.getI18nMessage());
                    throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_DELETE_FAIL, e, response.getFirmFlag(),
                            response.getProductFlag(),e.getI18nMessage());
                }
            }
        }
    }

    private CbbGetUSBDeviceResultDTO getUSBDeviceDetail(UUID id) {
        CbbGetUSBDeviceResultDTO response = null;
        try {
            response = cbbUSBDeviceMgmtAPI.getUSBDevice(id);
        } catch (Exception e) {
            LOGGER.error("查询USB外设", e);
        }
        return response;
    }
}
