package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.CheckDuplicateNameRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.platform.CloudPlatformManageBaseRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.platform.EditCloudPlatformBaseInfoRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformFeatureInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.CheckDuplicationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.batchtask.AddCloudPlatformBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.batchtask.EditCloudPlatformBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.batchtask.RemoveCloudPlatformBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.request.CheckDuplicateNameWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.request.CloudPlatformManageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.request.EditCloudPlatformBaseInfoWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.request.EditCloudPlatformManageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.response.TestConnectWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.vo.CloudPlatformVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 云平台配置管理WEB接口
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/8
 *
 * @author WuShengQiang
 */
@Api(tags = "云平台配置管理")
@Controller
@RequestMapping("/rco/cloudPlatform")
public class CloudPlatformController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudPlatformController.class);

    private static final String PLATFORM_PASSWORD = "password";

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 添加云平台
     *
     * @param request 添加云平台入参
     * @param builder 批处理对象
     * @param sessionContext session
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("添加云平台")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"添加云平台"})})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonWebResponse<BatchTaskSubmitResult> add(CloudPlatformManageWebRequest request, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");
        LOGGER.info("添加云平台入参:{}", JSON.toJSONString(request));
        // 非VDI服务器不支持添加
        if (!serverModelAPI.isVdiModel()) {
            throw new BusinessException(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_SERVER_MODEL_NOT_SUPPORT);
        }

        BatchTaskItem batchTaskItem =
                new DefaultBatchTaskItem(UUID.randomUUID(), LocaleI18nResolver.resolve(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_ADD_TASK_NAME));
        AddCloudPlatformBatchTaskHandler handler = new AddCloudPlatformBatchTaskHandler(request, batchTaskItem);
        BatchTaskSubmitResult result = builder.setTaskName(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_ADD_TASK_NAME)
                .setTaskDesc(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_ADD_TASK_DESC, request.getName()).registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    /**
     * 编辑云平台
     *
     * @param request 编辑云平台入参
     * @param builder 批处理对象
     * @param sessionContext session
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑云平台")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"编辑云平台"})})
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonWebResponse<?> edit(EditCloudPlatformManageWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");
        LOGGER.info("编辑云平台入参:{}", JSON.toJSONString(request));
        BatchTaskItem batchTaskItem =
                new DefaultBatchTaskItem(UUID.randomUUID(), LocaleI18nResolver.resolve(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EDIT_TASK_NAME));
        EditCloudPlatformBatchTaskHandler handler = new EditCloudPlatformBatchTaskHandler(request, batchTaskItem);
        BatchTaskSubmitResult result = builder.setTaskName(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EDIT_TASK_NAME)
                .setTaskDesc(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EDIT_TASK_DESC, request.getName()).setUniqueId(request.getId())
                .registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    /**
     * 编辑云平台
     *
     * @param request 编辑云平台入参
     * @param builder 批处理对象
     * @param sessionContext session
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑云平台基本信息")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"编辑云平台基本信息"})})
    @RequestMapping(value = "/editBaseInfo", method = RequestMethod.POST)
    public CommonWebResponse<?> editBaseInfo(EditCloudPlatformBaseInfoWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");
        LOGGER.info("编辑云平台基本信息入参:{}", JSON.toJSONString(request));

        try {
            EditCloudPlatformBaseInfoRequest editCloudPlatformBaseInfoRequest = new EditCloudPlatformBaseInfoRequest();
            editCloudPlatformBaseInfoRequest.setId(request.getId());
            editCloudPlatformBaseInfoRequest.setName(request.getName());
            editCloudPlatformBaseInfoRequest.setDescription(request.getDescription());

            cloudPlatformManageAPI.editBaseInfo(editCloudPlatformBaseInfoRequest);
            auditLogAPI.recordLog(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EDIT_BASE_INFO_SUCCESS, request.getName());
            return CommonWebResponse.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            auditLogAPI.recordLog(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EDIT_BASE_INFO_FAIL, e, request.getName(), exceptionMsg);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, exceptionMsg);
        }
    }

    /**
     * 删除云平台
     *
     * @param request 云平台ID
     * @param builder 批处理
     * @param sessionContext session
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除云平台")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"删除云平台"})})
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonWebResponse<?> remove(IdWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext cannot be null");
        LOGGER.info("删除云平台 ID:{}", request.getId());
        CloudPlatformDTO cloudPlatformDTO = cloudPlatformManageAPI.getInfoById(request.getId());
        BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(request.getId(), cloudPlatformDTO.getName());
        RemoveCloudPlatformBatchTaskHandler handler = new RemoveCloudPlatformBatchTaskHandler(batchTaskItem);
        BatchTaskSubmitResult result = builder.setTaskName(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_REMOVE_TASK_NAME)
                .setTaskDesc(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_REMOVE_TASK_DESC, cloudPlatformDTO.getName()).setUniqueId(request.getId())
                .registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    /**
     * 云平台连通性检查
     *
     * @param request 添加云平台入参
     * @return true：正常连通， false：不连通
     * @throws BusinessException 业务异常
     */
    @ApiOperation("云平台连通性检查")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"云平台连通性检查"})})
    @RequestMapping(value = "/testConnect", method = RequestMethod.POST)
    public CommonWebResponse<TestConnectWebResponse> testConnect(CloudPlatformManageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        LOGGER.info("测试云平台连通性入参:{}", JSON.toJSONString(request));
        TestConnectWebResponse response = new TestConnectWebResponse();
        CloudPlatformManageBaseRequest cloudPlatformManageRequest = new CloudPlatformManageBaseRequest();
        BeanUtils.copyProperties(request, cloudPlatformManageRequest);
        response.setCanConnect(cloudPlatformManageAPI.testConnected(cloudPlatformManageRequest));

        return CommonWebResponse.success(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_TEST_CONNECTED_SUCCESS, response);
    }

    /**
     * 查询云平台信息列表
     *
     * @param request 分页查询对象
     * @param sessionContext session
     * @return 云平台信息列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("云平台列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"云平台列表"})})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonWebResponse<PageQueryResponse<CloudPlatformVO>> list(PageQueryRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        PageQueryResponse<CloudPlatformDTO> pageQueryResponse = cloudPlatformManageAPI.pageQuery(request);
        CloudPlatformDTO[] cloudPlatformDTOArr = Optional.ofNullable(pageQueryResponse.getItemArr()).orElse(new CloudPlatformDTO[0]);
        CloudPlatformVO[] cloudPlatformVOArr = Stream.of(cloudPlatformDTOArr) //
                .map(cloudPlatform -> buildCloudPlatformVO(cloudPlatform)).toArray(CloudPlatformVO[]::new);

        return CommonWebResponse.success(new PageQueryResponse<>(cloudPlatformVOArr, pageQueryResponse.getTotal()));
    }

    /**
     * 查询云平台详情
     *
     * @param request 云平台ID
     * @return 云平台详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询云平台详情")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"查询云平台详情"})})
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    public CommonWebResponse<CloudPlatformVO> getInfo(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        CloudPlatformDTO cloudPlatformDTO = cloudPlatformManageAPI.getInfoById(request.getId());
        CloudPlatformVO cloudPlatformVO = buildCloudPlatformVO(cloudPlatformDTO);


        return CommonWebResponse.success(cloudPlatformVO);
    }

    private static CloudPlatformVO buildCloudPlatformVO(CloudPlatformDTO cloudPlatformDTO) {
        CloudPlatformVO cloudPlatformVO = new CloudPlatformVO();
        cloudPlatformVO.setId(cloudPlatformDTO.getId());
        cloudPlatformVO.setName(cloudPlatformDTO.getName());
        cloudPlatformVO.setDescription(cloudPlatformDTO.getDescription());
        cloudPlatformVO.setType(cloudPlatformDTO.getType());
        JSONObject extendConfig = JSON.parseObject(cloudPlatformDTO.getExtendConfig());
        extendConfig.remove(PLATFORM_PASSWORD);
        cloudPlatformVO.setExtendConfig(extendConfig);
        cloudPlatformVO.setStatus(cloudPlatformDTO.getStatus());
        cloudPlatformVO.setShouldDefault(cloudPlatformDTO.getShouldDefault());
        cloudPlatformVO.setConnectMode(cloudPlatformDTO.getConnectMode());
        cloudPlatformVO.setCreateTime(cloudPlatformDTO.getCreateTime());
        return cloudPlatformVO;
    }

    /**
     * 云平台重名检查
     *
     * @param request 云平台重名检查入参
     * @return true:已存在 false:不存在
     */
    @ApiOperation("云平台重名检查")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"云平台重名检查"})})
    @RequestMapping(value = "/checkDuplicate", method = RequestMethod.POST)
    public CommonWebResponse<CheckDuplicationWebResponse> checkDuplicateName(CheckDuplicateNameWebRequest request) {
        Assert.notNull(request, "request must not be null");
        CheckDuplicationWebResponse webResponse = new CheckDuplicationWebResponse();
        CheckDuplicateNameRequest checkDuplicateNameRequest = new CheckDuplicateNameRequest();
        BeanUtils.copyProperties(request, checkDuplicateNameRequest);
        webResponse.setHasDuplication(cloudPlatformManageAPI.checkDuplicateName(checkDuplicateNameRequest));
        return CommonWebResponse.success(webResponse);
    }

    /**
     * 获取动态表单
     *
     * @return 表单
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取动态表单")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"获取动态表单"})})
    @RequestMapping(value = "/form/getInfo", method = RequestMethod.POST)
    public CommonWebResponse<String> getPlatformTypeForms() throws BusinessException {
        return CommonWebResponse.success(cloudPlatformManageAPI.getPlatformTypeForms());
    }


    /**
     * 获取云平台的特性支持
     *
     * @return 表单
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取云平台的特性支持")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"获取云平台的特性支持"})})
    @RequestMapping(value = "/feature", method = RequestMethod.POST)
    public CommonWebResponse<List<CloudPlatformFeatureInfoDTO>> getPlatformFeature() throws BusinessException {
        return CommonWebResponse.success(cloudPlatformManageAPI.getAllPlatformFeature());
    }

}
