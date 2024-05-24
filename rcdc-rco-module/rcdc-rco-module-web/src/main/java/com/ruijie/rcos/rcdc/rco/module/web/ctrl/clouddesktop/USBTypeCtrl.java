package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBTypeMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetAllUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeCheckDuplicationDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUpdateUSBTypeDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.GetAllUSBTypeDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbtype.CreateUSBTypeWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbtype.DeleteUSBTypeWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbtype.GetAllUSBTypeIdAndNameWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbtype.GetAllUSBTypeWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbtype.GetUSBTypeWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbtype.USBTypeCheckDuplicationWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbtype.UpdateUSBTypeWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.CheckDuplicationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.usbtype.GetAllUSBTypeIdAndNameWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import io.swagger.annotations.ApiOperation;

/**
 * Description: USB类型controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月20日
 *
 * @author Ghang
 */
@Controller
@RequestMapping("/rco/usbType")
public class USBTypeCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(USBTypeCtrl.class);

    @Autowired
    private CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 创建USB类型
     *
     * @param request        请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping("create")
    @EnableAuthority
    public DefaultWebResponse createUSBType(CreateUSBTypeWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");
        CbbCreateUSBTypeDTO apiRequest = new CbbCreateUSBTypeDTO();
        apiRequest.setUsbTypeName(request.getUsbTypeName());
        apiRequest.setNote(request.getNote());
        try {
            cbbUSBTypeMgmtAPI.createUSBType(apiRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_CREATE_SUCCESS,
                    request.getUsbTypeName());
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("创建USB外设类型", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_CREATE_FAIL,
                    request.getUsbTypeName(), e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 更新USB类型
     *
     * @param request        请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping("edit")
    @EnableAuthority
    public DefaultWebResponse updateUSBType(UpdateUSBTypeWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");
        CbbUpdateUSBTypeDTO apiRequest = new CbbUpdateUSBTypeDTO();
        apiRequest.setId(request.getId());
        apiRequest.setUsbTypeName(request.getUsbTypeName());
        apiRequest.setNote(request.getNote());
        try {
            cbbUSBTypeMgmtAPI.updateUSBType(apiRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_UPDATE_SUCCESS,
                    request.getUsbTypeName());
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑USB外设类型", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_UPDATE_FAIL,
                    request.getUsbTypeName(), e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 删除USB类型
     *
     * @param request        请求参数
     * @param builder        批处理
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping("delete")
    @EnableAuthority
    public DefaultWebResponse deleteUSBType(DeleteUSBTypeWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request is null");
        Assert.notNull(builder, "builder is null");
        Assert.notEmpty(request.getIdArr(), "id不能为空");

        final UUID[] idArr = request.getIdArr();
        if (idArr.length == 1) {
            return deleteSingleRecord(idArr[0]);
        } else {
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                    .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                            .itemName(LocaleI18nResolver
                                    .resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_DELETE_ITEM_NAME))
                            .build())
                    .iterator();
            USBTypeDeleteBatchHandler handler =
                    new USBTypeDeleteBatchHandler(cbbUSBTypeMgmtAPI, iterator, auditLogAPI);
            BatchTaskSubmitResult result =
                    builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_DELETE_BATCH_TASK_NAME)
                            .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_DELETE_BATCH_TASK_DESC)
                            .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);
        }
    }

    private DefaultWebResponse deleteSingleRecord(UUID id) throws BusinessException {
        String logName = getUSBTypeName(id);
        try {
            cbbUSBTypeMgmtAPI.deleteUSBType(id);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_DELETE_SUCCESS, logName);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("删除USB外设类型", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_DELETE_FAIL, logName,
                    e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 获取所有USB类型
     *
     * @param request 请求参数
     * @return GetAllUSBTypeWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("list")
    public DefaultWebResponse getAllUSBType(GetAllUSBTypeWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");
        CbbGetAllUSBTypeDTO cbbGetAllUSBTypeRequest = new CbbGetAllUSBTypeDTO();
        cbbGetAllUSBTypeRequest.setSort(request.getSort());
        GetAllUSBTypeDTO allUSBTypeDTO = new GetAllUSBTypeDTO(cbbUSBTypeMgmtAPI.getAllUSBType(cbbGetAllUSBTypeRequest));
        return DefaultWebResponse.Builder.success(allUSBTypeDTO);
    }

    /**
     * 根据ID获取单条USB类型
     *
     * @param request 请求参数
     * @return data 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("detail")
    public DefaultWebResponse getUSBType(GetUSBTypeWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");
        CbbUSBTypeDTO dto = cbbUSBTypeMgmtAPI.getUSBType(request.getId());
        return DefaultWebResponse.Builder.success(dto);
    }

    /**
     * 获取所有USB类型的ID和NAME给前端
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("getAllUSBType")
    public DefaultWebResponse getAllUSBTypeIdAndName(GetAllUSBTypeIdAndNameWebRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is null");
        CbbUSBTypeDTO[] cbbUSBTypeDTOArr = cbbUSBTypeMgmtAPI.getAllUSBType(new CbbGetAllUSBTypeDTO());
        IdLabelEntry[] itemArr = new IdLabelEntry[cbbUSBTypeDTOArr.length];
        for (int i = 0; i < cbbUSBTypeDTOArr.length; i++) {
            IdLabelEntry dto = new IdLabelEntry();
            dto.setId(cbbUSBTypeDTOArr[i].getId());
            dto.setLabel(cbbUSBTypeDTOArr[i].getUsbTypeName());
            itemArr[i] = dto;
        }
        GetAllUSBTypeIdAndNameWebResponse response = new GetAllUSBTypeIdAndNameWebResponse();
        response.setItemArr(itemArr);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 命名唯一性校验
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "checkDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckDuplicationWebResponse> checkDuplication(USBTypeCheckDuplicationWebRequest request) throws BusinessException {
        Assert.notNull(request, "USBTypeCheckDuplicationWebRequest is null");
        CbbUSBTypeCheckDuplicationDTO cbbUSBTypeCheckDuplicationDTO = new CbbUSBTypeCheckDuplicationDTO();
        cbbUSBTypeCheckDuplicationDTO.setUsbTypeName(request.getUsbTypeName());
        CbbUSBTypeDTO usbTypeDTO = cbbUSBTypeMgmtAPI.findByName(cbbUSBTypeCheckDuplicationDTO);
        CheckDuplicationWebResponse response = new CheckDuplicationWebResponse();
        if (request.getId() == null) {
            if (usbTypeDTO != null) {
                response.setHasDuplication(Boolean.TRUE);
            } else {
                response.setHasDuplication(Boolean.FALSE);
            }
        } else {
            if (usbTypeDTO != null && !usbTypeDTO.getId().equals(request.getId())) {
                response.setHasDuplication(Boolean.TRUE);
            } else {
                response.setHasDuplication(Boolean.FALSE);
            }
        }
        return CommonWebResponse.success(response);
    }

    /**
     * Description: usb类型批量删除任务
     * Copyright: Copyright (c) 2019
     * Company: Ruijie Co., Ltd.
     * Create Time: 2019年1月17日
     *
     * @author Ghang
     */
    protected class USBTypeDeleteBatchHandler extends AbstractBatchTaskHandler {

        private BaseAuditLogAPI auditLogAPI;

        private final CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

        protected USBTypeDeleteBatchHandler(CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI,
                                            Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
            super(iterator);
            this.cbbUSBTypeMgmtAPI = cbbUSBTypeMgmtAPI;
            this.auditLogAPI = auditLogAPI;
        }

        @Override
        public BatchTaskFinishResult onFinish(int successCount, int failCount) {
            return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount,
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_DELETE_BATCH_TASK_RESULT);
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
            Assert.notNull(item, "BatchTaskItem is null");
            String logName = getUSBTypeName(item.getItemID());
            try {
                cbbUSBTypeMgmtAPI.deleteUSBType(item.getItemID());
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_DELETE_SUCCESS, logName);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_DELETE_SUCCESS)
                        .msgArgs(new String[]{logName}).build();
            } catch (BusinessException e) {
                LOGGER.error("删除USB外设类型", e);
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_DELETE_FAIL, logName,
                        e.getI18nMessage());
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBTYPE_DELETE_FAIL, e, logName, e.getI18nMessage());
            }
        }
    }

    private String getUSBTypeName(UUID id) {
        String usbTypeName = id.toString();
        try {
            CbbUSBTypeDTO dto = cbbUSBTypeMgmtAPI.getUSBType(id);
            usbTypeName = dto.getUsbTypeName();
        } catch (Exception e) {
            LOGGER.error("获取USB外设类型", e);
        }
        return usbTypeName;
    }
}
