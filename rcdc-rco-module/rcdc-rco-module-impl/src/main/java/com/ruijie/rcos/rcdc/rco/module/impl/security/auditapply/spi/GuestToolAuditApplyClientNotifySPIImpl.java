package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.spi;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullNumberAsZero;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.cache.AuditFileUploadedCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dto.AuditFileUploadedCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.enums.AuditApplyNotifyActionEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.AuditPrinterBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditPrinterMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditFilePrintInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.enums.AuditApplyNotifyActionResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.spi.request.AuditApplyClientNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.util.StopWatch;

/**
 * Description: 安全审计申请单上报变更状态
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/28
 *
 * @author lihengjing
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_AUDIT_APPLY_CLIENT_NOTIFY)
public class GuestToolAuditApplyClientNotifySPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolAuditApplyClientNotifySPIImpl.class);

    private static final Cache<String, AuditApplyClientNotifyRequest> CACHE_MAP;

    /**
     * 缓存1小时
     */
    static {
        CACHE_MAP = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.HOURS).build();
    }

    /**
     * JSON转字符串特征
     */
    private static final SerializerFeature[] JSON_FEATURES =
            new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero};

    /**
     * 自定义处理文件上传完成的线程池
     */
    private static final ThreadExecutor CUSTOM_THREAD_EXECUTOR =
            ThreadExecutors.newBuilder("CopyAuditFileHandler").maxThreadNum(4).queueSize(10000).build();

    /**
     * 处理中
     */
    private static final int IN_PROCESS = -2;

    @Autowired
    private AuditApplyMgmtAPI auditApplyMgmtAPI;

    @Autowired
    private AuditPrinterMgmtAPI auditPrinterMgmtAPI;

    @Autowired
    private AuditApplyService auditApplyService;

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private DesktopPoolUserService desktopPoolUserService;
    
    @Autowired
    private AuditFileUploadedCacheManager auditFileUploadedCacheManager;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        CbbGuesttoolMessageDTO requestDto = request.getDto();
        Assert.notNull(requestDto, "requestDto can not be null");
        UUID deskId = requestDto.getDeskId();
        Assert.notNull(deskId, "deskId can not be null");
        LOGGER.info("[CMDID=7013]安全审计申请单客户端上报变更状态，请求消息：{}", JSON.toJSONString(request));
        GuesttoolMessageContent requestBody = JSON.parseObject(requestDto.getBody(), GuesttoolMessageContent.class);
        CbbGuesttoolMessageDTO responseBody = new CbbGuesttoolMessageDTO();
        responseBody.setPortId(requestDto.getPortId());
        responseBody.setCmdId(requestDto.getCmdId());
        responseBody.setDeskId(requestDto.getDeskId());
        GuesttoolMessageContent body = new GuesttoolMessageContent();

        if (requestBody == null || requestBody.getContent() == null) {
            CbbGuesttoolMessageDTO illegalParamResponse = getIllegalParamDTO(responseBody, body);
            LOGGER.error("[CMDID=7013]安全审计申请单客户端上报变更状态【失败】，请求消息内容不正确，请求消息：{}，响应结果：{}", JSON.toJSONString(request),
                    JSON.toJSONString(illegalParamResponse));
            return illegalParamResponse;
        }
        AuditApplyClientNotifyRequest requestContent =
                JSON.parseObject(JSON.toJSONString(requestBody.getContent()), AuditApplyClientNotifyRequest.class);
        UUID applyId = requestContent.getApplyId();
        UUID fileId = requestContent.getFileId();
        String tempCacheKey = fileId == null ? String.valueOf(applyId) : applyId + "_" + fileId;
        final String cacheKey = requestContent.getAction().name() + ":" + tempCacheKey;
        body.setCode(CommonMessageCode.SUCCESS);
        body.setContent(requestContent);

        // 防止客户端对同一申请单的多次操作
        AuditApplyClientNotifyRequest oldRequest = CACHE_MAP.getIfPresent(cacheKey);
        if (oldRequest != null) {
            responseBody.setBody(JSON.toJSONString(body));
            LOGGER.warn("[CMDID=7013]安全审计申请单客户端上报变更状态已经存在处理的任务，" +
                            "请求消息：{}，响应消息：{}，cacheKey:{}，oldRequest：{}", JSON.toJSONString(request),
                    JSON.toJSONString(requestBody), cacheKey, JSON.toJSONString(oldRequest));
            return responseBody;
        }
        CACHE_MAP.put(cacheKey, requestContent);

        try {
            switch (requestContent.getAction()) {
                // 撤回申请:删除临时文件,修改状态
                case CANCELLED:
                    // 先取消指定申请单文件上传操作
                    auditFileUploadedCacheManager.cancelApplyIdFileUploaded(applyId);
                    auditApplyMgmtAPI.cancelAuditFileApply(deskId, applyId);
                    break;
                    
                // 因网络不通，文件被删除等原因，GT上报结束申请单   
                case FAIL:
                    auditApplyService.validateDiscardAuditApply(applyId);
                    auditApplyService.discardAuditApply(applyId, requestContent.getFailReason());
                    break;
                    
                // 废弃操作客户端处理完成：删除临时文件，修改状态
                case FAIL_SUCCESS:
                    auditApplyService.discardAuditApplyHandler(applyId);
                    break;
                    
                // 上传文件完成:处理文件列表,修改状态
                case UPLOADED:
                    Future<?> future = CUSTOM_THREAD_EXECUTOR.submit(() -> {
                        // 异步执行文件申请上传处理
                        syncHandleAuditFileUploaded(deskId, applyId, fileId, body, cacheKey);
                        // 线程未被取消，则组装发送消息给GT
                        if (!Thread.currentThread().isInterrupted()) {
                            responseBody.setBody(JSON.toJSONString(body, JSON_FEATURES));
                            sendFileUploadedNotify(applyId, deskId, fileId, responseBody);
                        }
                    });
                    auditFileUploadedCacheManager.save(new AuditFileUploadedCacheDTO(applyId, fileId, future));
                    body.setCode(IN_PROCESS);
                    break;

                // 打印文件完成:通知打印结果
                case PRINTED:
                    AuditFilePrintInfoDTO auditFilePrintInfoDTO =
                            JSON.parseObject(JSON.toJSONString(requestBody.getContent()), AuditFilePrintInfoDTO.class);
                    auditPrinterMgmtAPI.handleAuditPrintApplyResult(deskId, auditFilePrintInfoDTO);
                    break;
                default:
                    return getIllegalParamDTO(responseBody, body);
            }
            responseBody.setBody(JSON.toJSONString(body));
            LOGGER.info("[CMDID=7013]安全审计申请单客户端上报变更状态【成功】，请求消息：{}，响应消息：{}", 
                    JSON.toJSONString(request), JSON.toJSONString(responseBody));
            return responseBody;
        } catch (BusinessException e) {
            // 构建返回的错误码
            buildErrorCode(e.getKey(), body);
            body.setMessage(e.getI18nMessage());
            responseBody.setBody(JSON.toJSONString(body, JSON_FEATURES));
            LOGGER.error("[CMDID=7013]安全审计申请单客户端上报变更状态【失败】，" +
                    "请求消息：{}，响应消息：{}", JSON.toJSONString(request), JSON.toJSONString(responseBody));
            return responseBody;
        } catch (Exception e) {
            LOGGER.error("[CMDID=7013]安全审计申请单客户端上报变更状态【失败】，请求消息：{}，" +
                            "过程中出现未知异常：{}，异常信息：{}", JSON.toJSONString(request), e.getMessage(), ExceptionUtils.getStackTrace(e));
            body.setCode(AuditApplyNotifyActionResultTypeEnum.SERVER_INNER_ERROR.getCode());
            body.setMessage(AuditApplyNotifyActionResultTypeEnum.SERVER_INNER_ERROR.getI18nMessage());
            responseBody.setBody(JSON.toJSONString(body, JSON_FEATURES));
            LOGGER.error("[CMDID=7013]安全审计申请单客户端上报变更状态【失败】，" +
                    "请求消息：{}，响应结果：{}", JSON.toJSONString(request), JSON.toJSONString(responseBody));
            return responseBody;
        } finally {
            // 上传文件完成转异步操作，不在这里进行清除缓存
            if (requestContent.getAction() != AuditApplyNotifyActionEnum.UPLOADED) {
                CACHE_MAP.invalidate(cacheKey);
            }
        }
    }
    
    private void syncHandleAuditFileUploaded(UUID deskId, UUID applyId, UUID fileId, GuesttoolMessageContent body, String cacheKey) {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            body.setCode(CommonMessageCode.SUCCESS);
            auditApplyMgmtAPI.handleAuditFileApplyUploaded(deskId, applyId, fileId);
        } catch (BusinessException e) {
            LOGGER.error("执行申请单[{}]上传文件[{}]完成处理出现异常", applyId, fileId, e);
            buildErrorCode(e.getKey(), body);
            body.setMessage(e.getI18nMessage());
        } finally {
            CACHE_MAP.invalidate(cacheKey);
            auditFileUploadedCacheManager.deleteFileUploaded(fileId);
        }
        watch.stop();
        LOGGER.info("结束执行申请单[{}]上传文件[{}]完成处理操作，耗时:[{}s]", applyId, fileId, watch.getTotalTimeSeconds());
    }

    private CbbGuesttoolMessageDTO getIllegalParamDTO(CbbGuesttoolMessageDTO responseBody, GuesttoolMessageContent body) {
        body.setCode(AuditApplyNotifyActionResultTypeEnum.PARAM_ILLEGAL.getCode());
        body.setMessage(AuditApplyNotifyActionResultTypeEnum.PARAM_ILLEGAL.getI18nMessage());
        responseBody.setBody(JSON.toJSONString(body));
        return responseBody;
    }
    
    private void sendFileUploadedNotify(UUID applyId, UUID deskId, UUID fileId, CbbGuesttoolMessageDTO responseBody) {
        try {
            AuditApplyDTO auditApplyDTO = auditApplyService.findAuditApplyById(applyId);
            if (auditApplyDTO.getDesktopPoolType() == DesktopPoolType.DYNAMIC) {
                UUID useDesktopId = desktopPoolUserService.findDeskRunningByDesktopPoolIdAndUserId(
                        auditApplyDTO.getDesktopPoolId(), auditApplyDTO.getUserId());
                if (Objects.isNull(useDesktopId)) {
                    LOGGER.info("用户[{}]申请单[{}]在动态池[{}]无运行中的桌面, 不向GT发送请求", 
                            auditApplyDTO.getUserId(), applyId, auditApplyDTO.getDesktopPoolId());
                    return;
                }
                responseBody.setDeskId(useDesktopId);
            } else {
                CloudDesktopDetailDTO deskDetail = queryCloudDesktopService.queryDeskDetail(deskId);
                // 申请单所属用户与桌面所属不一致或桌面不在线则不发送请求
                if (!Objects.equals(deskDetail.getUserId(), auditApplyDTO.getUserId())
                        || !Objects.equals(CbbCloudDeskState.RUNNING.name(), deskDetail.getDesktopState())) {
                    LOGGER.info("不向非运行中的GT发送申请单[{}]文件上传处理结果，当前云桌面[{}]状态为[{}]",
                            auditApplyDTO.getId(), deskDetail.getDesktopName(), deskDetail.getDesktopState());
                    return;
                }
            }
            guestToolMessageAPI.asyncRequest(responseBody);
        } catch (Exception e) {
            LOGGER.error("向GT发送文件[{}]处理完成消息出现异常：", fileId, e);
        }
    }
    
    private void buildErrorCode(String key, GuesttoolMessageContent body) {
        switch (key) {
            case AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINTER_ALREADY_EXIST:
                body.setCode(AuditApplyNotifyActionResultTypeEnum.SUCCESS.getCode());
                break;
            case AuditPrinterBusinessKey.RCDC_RCO_AUDIT_FILE_PRINT_INFO_PRINTER_NAME_IS_NULL:
            case AuditPrinterBusinessKey.RCDC_RCO_AUDIT_FILE_PRINT_INFO_PRINT_RESULT_MSG_IS_NULL:
            case AuditPrinterBusinessKey.RCDC_RCO_AUDIT_FILE_PRINT_INFO_PRINT_STATE_ERROR:
                body.setCode(AuditApplyNotifyActionResultTypeEnum.PARAM_ILLEGAL.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_DISABLE:
                body.setCode(AuditApplyNotifyActionResultTypeEnum.AUDIT_FILE_DISABLE.getCode());
                break;
            case AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_NOT_EXIST:
                body.setCode(AuditApplyNotifyActionResultTypeEnum.APPLY_NOT_EXIST.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_STATE_NOT_CANCELLED:
                 
                body.setCode(AuditApplyNotifyActionResultTypeEnum.APPLY_STATE_NOT_CANCELLED.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_DELETE_FAIL:
                body.setCode(AuditApplyNotifyActionResultTypeEnum.FILE_DELETE_FAIL.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_TEMP_PATH_NOT_FIND:
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_MD5_NOT_MATCH:
                body.setCode(AuditApplyNotifyActionResultTypeEnum.FILE_MD5_NOT_MATCH.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_STORAGE_SPACE_NOT_ENOUGH:
                body.setCode(AuditApplyNotifyActionResultTypeEnum.STORAGE_SPACE_NOT_ENOUGH.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_GLOBAL_NOT_OPEN_EXTERNAL_STORAGE:
                body.setCode(AuditApplyNotifyActionResultTypeEnum.NOT_OPEN_EXT_STORAGE.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_STATE_NOT_AVAILABLE:
                body.setCode(AuditApplyNotifyActionResultTypeEnum.EXT_STORAGE_NOT_AVAILABLE.getCode());
                break;
            case AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_STORAGE_ERROR:
            default:
                body.setCode(AuditApplyNotifyActionResultTypeEnum.SERVER_INNER_ERROR.getCode());
                break;
        }
    }
}
