package com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCheckCommNameDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbCreateExternalStorageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbEditExternalStorageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbLocalExternalStorageDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageProtocolTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.batchtask.DeleteExternalStorageBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.request.CheckExternalStorageNameWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.request.ExternalStorageCreateRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.request.ExternalStorageDeleteRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.request.ExternalStorageEditRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.extstorage.response.CheckExternalStorageNameWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 文件服务器管理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/9
 *
 * @author TD
 */
@Api(tags = "文件服务器管理")
@Controller
@RequestMapping("/rco/ext/storage")
public class ExternalStorageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalStorageController.class);

    @Autowired
    private CbbExternalStorageMgmtAPI cbbExternalStorageMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 共享目录的正则表达式规则
     */
    private static final String SHARE_NAME_REGEX = "^(/[\\w]+)+/?$";

    /**
     * 查询文件服务器明细
     *
     * @param request 页面请求参数
     * @return CbbLocalExternalStorageDTO
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取文件服务器详情信息")
    @RequestMapping(value = {"/detail", "/getInfo"}, method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"获取文件服务器基本信息"})})
    public CommonWebResponse<CbbLocalExternalStorageDTO> detail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "detail request must not be null");
        Assert.notNull(request.getId(), "request id must not be null");
        CbbLocalExternalStorageDTO externalStorageDTO = cbbExternalStorageMgmtAPI.getExternalStorageDetail(request.getId());
        externalStorageDTO.setPassword(null);
        return CommonWebResponse.success(externalStorageDTO);
    }

    /**
     * 获取文件服务器列表
     *
     * @param request 请求参数
     * @return 外置存储列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取文件服务器列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"获取文件服务器列表"})})
    public CommonWebResponse<DefaultPageResponse<CbbLocalExternalStorageDTO>> list(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "list request must not be null");
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder(request);
        return CommonWebResponse.success(cbbExternalStorageMgmtAPI.pageQueryLocalExternalStorage(requestBuilder.build()));
    }

    /**
     * 校验文件服务器名称重复
     *
     * @param request 请求参数
     * @return 校验结果返回
     * @throws BusinessException 异常信息
     */
    @ApiOperation("校验文件服务器名称是否重复")
    @RequestMapping(value = "/checkNameDuplication", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"校验文件服务器名称是否重复"})})
    public CommonWebResponse<CheckExternalStorageNameWebResponse> checkDiskPoolNameDuplication(CheckExternalStorageNameWebRequest request)
            throws BusinessException {
        Assert.notNull(request, "checkDiskPoolNameDuplication request must not be null");

        CbbCheckCommNameDTO poolNameDTO = new CbbCheckCommNameDTO();
        BeanUtils.copyProperties(request, poolNameDTO);
        Boolean hasDuplicate = cbbExternalStorageMgmtAPI.checkExternalStorageNameDuplicate(poolNameDTO);

        return CommonWebResponse.success(new CheckExternalStorageNameWebResponse(hasDuplicate));
    }

    /**
     * 文件服务器创建
     *
     * @param request 请求参数
     * @param builder 批处理构造类
     * @return 任务信息
     */
    @ApiOperation("文件服务器创建")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"文件服务器创建"})})
    @EnableAuthority
    public CommonWebResponse<Object> create(ExternalStorageCreateRequest request, BatchTaskBuilder builder) {
        Assert.notNull(request, "create request must not be null");
        Assert.notNull(builder, "builder request must not be null");
        LOGGER.info("create ExternalStorageCreateRequest:{}", JSON.toJSONString(request));
        try {
            externalStorageCreateParamValidation(request);
            // 参数复制
            CbbCreateExternalStorageDTO externalStorageDTO = new CbbCreateExternalStorageDTO();
            BeanUtils.copyProperties(request, externalStorageDTO);
            cbbExternalStorageMgmtAPI.createExternalStorage(externalStorageDTO);
            auditLogAPI.recordLog(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_CREATE_ITEM_SUCCESS_DESC, request.getName());
            return CommonWebResponse.success(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_CREATE_ITEM_SUCCESS_DESC,
                    new String[]{request.getName()});
        } catch (Exception e) {
            LOGGER.error("创建文件服务器发生异常，异常原因：", e);
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_CREATE_ITEM_FAIL_DESC, request.getName(), message);
            return CommonWebResponse.fail(ExternalStorageBusinessKey
                    .RCDC_RCO_EXTERNAL_STORAGE_CREATE_ITEM_FAIL_DESC, new String[]{request.getName(), message});
        }
    }

    private void externalStorageCreateParamValidation(ExternalStorageCreateRequest request) throws BusinessException {
        // 校验共享目录格式是否正确
        if (!Pattern.matches(SHARE_NAME_REGEX, request.getShareName())) {
            throw new BusinessException(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_SHARE_NAME_FORMAT_ERROR);
        }
        if (request.getProtocolType() == ExternalStorageProtocolTypeEnum.NFS) {
            return;
        }
        // 外置存储为SAMBA类型：端口、用户名称、用户密码不能为空
        if (Objects.isNull(request.getPort()) || Objects.isNull(request.getUserName()) || Objects.isNull(request.getPassword())) {
            throw new BusinessException(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_CREATE_PARAM_ERROR);
        }
        // 尝试对密码进行解密，防止密码错误
        try {
            AesUtil.descrypt(request.getPassword(), RedLineUtil.getRealAdminRedLine());
        } catch (Exception e) {
            LOGGER.error("创建文件服务器[{}]操作，密码解密失败", request.getName());
            throw new BusinessException(BusinessKey.RCDC_RCO_DECRYPT_FAIL, e);
        }
    }

    /**
     * 文件服务器信息编辑
     *
     * @param webRequest 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("文件服务器信息编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = "文件服务器信息编辑")})
    @EnableAuthority
    public DefaultWebResponse editAuditFileGlobalConfig(ExternalStorageEditRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");

        CbbEditExternalStorageDTO editExternalStorageDTO = new CbbEditExternalStorageDTO();
        BeanUtils.copyProperties(webRequest, editExternalStorageDTO);
        try {
            String extStorageName = cbbExternalStorageMgmtAPI.getExternalStorageDetail(webRequest.getId()).getName();
            cbbExternalStorageMgmtAPI.updateExternalStorage(editExternalStorageDTO);
            auditLogAPI.recordLog(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_EDIT_SUCCESS_LOG, extStorageName);
            return DefaultWebResponse.Builder.success(
                    ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_EDIT_SUCCESS_LOG, new String[]{extStorageName});
        } catch (BusinessException e) {
            LOGGER.error("编辑文件服务器失败，入参对象为：{}，失败原因:", JSON.toJSONString(webRequest), e);
            throw new BusinessException(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_EDIT_FAIL_LOG, e, webRequest.getId().toString(),
                    e.getI18nMessage());
        }
    }

    /**
     * 文件服务器删除
     *
     * @param request 请求参数
     * @param builder 批处理构造类
     * @return 任务信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("文件服务器删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = "文件服务器删除")})
    @EnableAuthority
    public CommonWebResponse<Object> delete(ExternalStorageDeleteRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder request must not be null");
        UUID[] extStorageIdArr = request.getIdArr();
        String taskName = LocaleI18nResolver.resolve(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_TASK_NAME);
        Iterator<DefaultBatchTaskItem> iterator =
                Arrays.stream(extStorageIdArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(taskName).build()).iterator();

        DeleteExternalStorageBatchTaskHandler handler = new DeleteExternalStorageBatchTaskHandler(iterator);
        BatchTaskSubmitResult result;
        if (extStorageIdArr.length == 1) {
            handler.setExtStorageName(cbbExternalStorageMgmtAPI.getExternalStorageDetail(extStorageIdArr[0]).getName());
            result = builder.setTaskName(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_TASK_NAME)
                    .setTaskDesc(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_TASK_DESC).enableParallel()
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_BATCH_TASK_NAME)
                    .setTaskDesc(ExternalStorageBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_DELETE_BATCH_TASK_DESC).enableParallel()
                    .registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }
}
