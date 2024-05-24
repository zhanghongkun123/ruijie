package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.spi;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullNumberAsZero;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.enums.AuditApplyNotifyActionResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.enums.CreateAuditApplyResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.spi.request.CreateAuditApplyRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.AuditPrinterBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.common.SecurityBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: GT请求RCDC文件流转审计全局策略
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_CREATE_AUDIT_APPLY)
public class GuestToolCreateAuditApplySPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolCreateAuditApplySPIImpl.class);

    /**
     * JSON转字符串特征
     */
    private static final SerializerFeature[] JSON_FEATURES =
            new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero};

    /**
     * 报文最大长度
     */
    private static final int CONTENT_MAX_LENGTH = 20000;


    @Autowired
    private AuditApplyMgmtAPI auditApplyMgmtAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {

        Assert.notNull(request, "request can not be null");
        CbbGuesttoolMessageDTO requestDto = request.getDto();
        Assert.notNull(requestDto, "requestDto can not be null");
        UUID deskId = requestDto.getDeskId();
        Assert.notNull(deskId, "deskId can not be null");
        LOGGER.debug("[CMDID=7011]创建安全审计申请单，请求消息： {}", JSONObject.toJSONString(request));

        CbbGuesttoolMessageDTO response = new CbbGuesttoolMessageDTO();
        response.setPortId(requestDto.getPortId());
        response.setCmdId(requestDto.getCmdId());
        response.setDeskId(requestDto.getDeskId());

        GuesttoolMessageContent requestBody = JSONObject.parseObject(requestDto.getBody(), GuesttoolMessageContent.class);

        GuesttoolMessageContent responseContent = new GuesttoolMessageContent();

        if (requestBody == null || requestBody.getContent() == null) {
            // 设置错误码
            responseContent.setCode(CreateAuditApplyResultTypeEnum.APPLY_INFO_ERROR.getCode());
            responseContent.setMessage(CreateAuditApplyResultTypeEnum.APPLY_INFO_ERROR.getI18nMessage());
            response.setBody(JSON.toJSONString(responseContent, JSON_FEATURES));
            LOGGER.error("[CMDID=7011]创建安全审计申请单【失败】，请求消息内容不正确，请求消息：{}，响应结果：{}", 
                    JSONObject.toJSONString(request), JSON.toJSONString(response));
            return response;
        }
        
        // 判断创建的申请单内容长度
        int applyLength = JSON.toJSONString(requestBody.getContent()).getBytes().length;
        if (applyLength > CONTENT_MAX_LENGTH) {
            // 设置错误码
            responseContent.setCode(CreateAuditApplyResultTypeEnum.APPLY_CREATE_LENGTH_LIMIT.getCode());
            responseContent.setMessage(CreateAuditApplyResultTypeEnum.APPLY_CREATE_LENGTH_LIMIT.getI18nMessage());
            response.setBody(JSON.toJSONString(responseContent, JSON_FEATURES));
            LOGGER.error("[CMDID=7011]创建安全审计申请单【失败】，请求内容总长度：{}，超出20000要求客户端进行压缩，请求消息：{}，响应结果：{}",
                    applyLength, JSONObject.toJSONString(request), JSON.toJSONString(response));
            return response;
        }

        CreateAuditApplyRequest requestContent = JSONObject.parseObject(JSON.toJSONString(requestBody.getContent()), CreateAuditApplyRequest.class);

        AuditApplyDetailDTO auditApplyDetailDTO = new AuditApplyDetailDTO();
        BeanUtils.copyProperties(requestContent, auditApplyDetailDTO);

        List<AuditFileDTO> fileList = requestContent.getFileList();
        if (CollectionUtils.isEmpty(requestContent.getFileList())) {
            // 设置错误码
            responseContent.setCode(CreateAuditApplyResultTypeEnum.APPLY_INFO_ERROR.getCode());
            responseContent.setMessage(CreateAuditApplyResultTypeEnum.APPLY_INFO_ERROR.getI18nMessage());
            response.setBody(JSON.toJSONString(responseContent, JSON_FEATURES));
            LOGGER.error("[CMDID=7011]创建安全审计申请单【失败】，文件列表不允许为空，请求消息：{}，响应结果：{}",
                    JSONObject.toJSONString(request), JSON.toJSONString(response));
            return response;
        } else {
            auditApplyDetailDTO.setAuditFileList(fileList);
        }

        try {
            AuditApplyDetailDTO auditFileApplyDetailDTO = auditApplyMgmtAPI.createAuditApply(deskId, auditApplyDetailDTO);
            responseContent.setCode(CreateAuditApplyResultTypeEnum.SUCCESS.getCode());
            responseContent.setContent(auditFileApplyDetailDTO);
            response.setBody(JSON.toJSONString(responseContent, JSON_FEATURES));
            LOGGER.info("[CMDID=7011]创建安全审计申请单【成功】，请求消息：{}，响应结果：{}", JSON.toJSONString(request), JSON.toJSONString(response));
            return response;
        } catch (BusinessException e) {
            // 异常处理
            exceptionHandler(responseContent, request, e);
            responseContent.setMessage(e.getI18nMessage());
            response.setBody(JSON.toJSONString(responseContent, JSON_FEATURES));
            LOGGER.error("[CMDID=7011]创建安全审计申请单【失败】，请求消息：{}，响应结果：{}", JSON.toJSONString(request), JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            LOGGER.error("[CMDID=7011]创建安全审计申请单【失败】，请求消息：{}，过程中出现未知异常：{}，异常信息：{}", 
                    JSON.toJSONString(request), e.getMessage(), ExceptionUtils.getStackTrace(e));
            responseContent.setCode(CreateAuditApplyResultTypeEnum.SERVER_INNER_ERROR.getCode());
            responseContent.setMessage(CreateAuditApplyResultTypeEnum.SERVER_INNER_ERROR.getI18nMessage());
            response.setBody(JSON.toJSONString(responseContent, JSON_FEATURES));
            LOGGER.error("[CMDID=7011]创建安全审计申请单【失败】，请求消息：{}，响应结果：{}", JSON.toJSONString(request), JSON.toJSONString(response));
            return response;
        }

    }

    private void exceptionHandler(GuesttoolMessageContent responseContent, CbbGuestToolSPIReceiveRequest request, BusinessException e) {
        switch (e.getKey()) {
            case AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_PARAMETER_ILLEGAL:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.APPLY_INFO_ERROR.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_SINGLE_FILE_SIZE_LIMIT:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.SINGLE_FILE_SIZE_LIMIT.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_SINGLE_APPLY_FILE_NUM_LIMIT:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.SINGLE_APLLY_FILE_NUM_LIMIT.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_ONE_DAY_FILE_NUM_LIMIT:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.ONE_DAY_FILE_NUM_LIMIT.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_ONE_DAY_FILE_SIZE_LIMIT:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.ONE_DAY_FILE_SIZE_LIMIT.getCode());
                break;
            case AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINT_APPLY_ONE_DAY_PRINT_PAGE_NUM_LIMIT:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.ONE_DAY_PRINT_PAGE_NUM_LIMIT.getCode());
                break;
            case AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_TYPE_ILLEGAL:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.APPLY_TYPE_ERROR.getCode());
                break;
            case AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINTER_ALREADY_EXIST:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.PRINTER_ALREADY_EXIST.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_FTP_DIR_NO_SPACE:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.FTP_DIR_NO_SPACE.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_GLOBAL_NOT_OPEN_EXTERNAL_STORAGE:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.NOT_OPEN_EXT_STORAGE.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_STATE_NOT_AVAILABLE:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.EXT_STORAGE_NOT_AVAILABLE.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_STORAGE_SPACE_NOT_ENOUGH:
                responseContent.setCode(CreateAuditApplyResultTypeEnum.EXT_STORAGE_SPACE_NOT_ENOUGH.getCode());
                break;
            case AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINTER_DISABLE:
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_DISABLE:
            case SecurityBusinessKey.RCDC_RCO_SECURITY_GLOBAL_CONFIG_WORK_TIME_ERROR:
            default:
                responseContent.setCode(AuditApplyNotifyActionResultTypeEnum.SERVER_INNER_ERROR.getCode());
                LOGGER.error("[CMDID=7011]创建安全审计申请单【失败】，过程中出现未知异常：{}，请求消息：{}，异常信息：{}",
                        e.getI18nMessage(), JSON.toJSONString(request), ExceptionUtils.getStackTrace(e));
                break;
        }
    }
}
