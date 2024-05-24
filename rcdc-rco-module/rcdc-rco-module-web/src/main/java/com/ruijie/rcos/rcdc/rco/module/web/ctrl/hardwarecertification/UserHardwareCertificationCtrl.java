package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.HardwareCertificationBusinessKey.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.common.utils.MacUtils;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.RcoViewUserHardwareCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.UserMacBindingDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacUserHardwareCertificationStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.bactchtask.CreateUserTerminalBindingBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.bactchtask.DeleteUserCertificationTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.dto.ImportUserMacBindingDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.request.CreateUserMacBindingWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.request.UserHardwareCertificationPageRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportUserMacBindingHandler;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdArrRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * Description: 用户硬件特征码管理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
@Api(tags = "用户硬件特征码管理")
@Controller
@RequestMapping("/rco/user/hardwareCertification")
public class UserHardwareCertificationCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHardwareCertificationCtrl.class);

    /** 模板文件名称 */
    private static final String TEMPLATE_NAME = "user_mac_binding_model";

    /** 模板文件类型 */
    private static final String TEMPLATE_TYPE = "xlsx";

    /** 模板文件路径 */
    private static final String TEMPLATE_PATH = "template/";

    private static final String SYMBOL_SPOT = ".";

    @Autowired
    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private ImportUserMacBindingHandler importUserMacBindingHandler;

    /**
     * 分页获取用户硬件特征码信息
     *
     * @param request 请求参数
     * @param sessionContext session信息
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "分页获取用户硬件特征码信息")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分页获取用户硬件特征码信息"})})
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<RcoViewUserHardwareCertificationDTO>> getPageDeskStrategy(PageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "PageWebRequest can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        PageSearchRequest apiRequest = new UserHardwareCertificationPageRequest(request);

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            List<String> userGroupIdStrList = getPermissionUserGroupIdList(sessionContext.getUserId());
            String userGroupId = getUserGroupId(request);
            if (StringUtils.isNotEmpty(userGroupId) && !userGroupIdStrList.contains(userGroupId)) {
                DefaultPageResponse<RcoViewUserHardwareCertificationDTO> response = new DefaultPageResponse<>();
                response.setItemArr(new RcoViewUserHardwareCertificationDTO[] {});
                return CommonWebResponse.success(response);
            }
            if (StringUtils.isEmpty(userGroupId)) {
                appendUserGroupIdMatchEqual(apiRequest, userGroupIdStrList);
            }
        }

        DefaultPageResponse<RcoViewUserHardwareCertificationDTO> pageResponse = userHardwareCertificationAPI.pageQuery(apiRequest);
        return CommonWebResponse.success(pageResponse);
    }

    /**
     * 删除用户硬件特征码记录
     *
     * @param request 请求参数
     * @param builder BatchTaskBuilder
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "删除用户硬件特征码记录")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"删除用户硬件特征码记录"})})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonWebResponse delete(IdArrRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "delete()的request can not be null");
        Assert.notNull(request.getIdArr(), "idArr can not be null");

        if (request.getIdArr() == null || request.getIdArr().length == 0) {
            LOGGER.info("删除接口ID参数为空");
            return CommonWebResponse.success(RCDC_HARDWARE_CERTIFICATION_DELETE_SUCCESS, new String[] {});
        }
        UUID[] idArr = request.getIdArr();
        return idArr.length == 1 ? deleteSingleCertification(idArr[0]) : batchDeleteCertification(idArr, builder);
    }

    private CommonWebResponse<?> deleteSingleCertification(UUID id) throws BusinessException {
        RcoViewUserHardwareCertificationDTO certification = userHardwareCertificationAPI.getById(id);
        String userName = certification.getUserName();
        String upperCaseMac = certification.getUpperCaseMac();
        String i18nFeatureCode = certification.getI18nFeatureCode();
        try {
            userHardwareCertificationAPI.deleteById(id);
            auditLogAPI.recordLog(RCDC_HARDWARE_CERTIFICATION_DELETE_SUCCESS_LOG, userName, upperCaseMac, i18nFeatureCode);
            return CommonWebResponse.success(RCDC_HARDWARE_CERTIFICATION_DELETE_SUCCESS, new String[]{});
        } catch (Exception e) {
            LOGGER.error("删除用户硬件特征码审批记录异常，用户[{}]，MAC[{}]，特征码[{}]，异常：{}", userName, upperCaseMac, i18nFeatureCode, e);
            auditLogAPI.recordLog(RCDC_HARDWARE_CERTIFICATION_DELETE_FAIL, userName, upperCaseMac, i18nFeatureCode);
            return CommonWebResponse.fail(RCDC_HARDWARE_CERTIFICATION_DELETE_ERROR, new String[]{});
        }
    }

    private CommonWebResponse<?> batchDeleteCertification(UUID[] idArr, BatchTaskBuilder builder)
            throws BusinessException {
        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> taskItemIterator = Stream.of(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(RCDC_HARDWARE_CERTIFICATION_DELETE_ITEM_NAME, new String[] {}).build())
                .iterator();

        DeleteUserCertificationTaskHandler handler = new DeleteUserCertificationTaskHandler(taskItemIterator, auditLogAPI);
        BatchTaskSubmitResult result = builder.setTaskName(RCDC_HARDWARE_CERTIFICATION_BATCH_DELETE_ITEM_NAME)
                .setTaskDesc(RCDC_HARDWARE_CERTIFICATION_BATCH_DELETE_TASK_DESC).enableParallel().registerHandler(handler).start();

        return CommonWebResponse.success(result);
    }

    /**
     * 批准用户硬件特征码
     *
     * @param request 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "批准用户硬件特征码记录")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"批准用户硬件特征码记录"})})
    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    public CommonWebResponse approve(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getId(), "id can not be null");

        RcoViewUserHardwareCertificationDTO certificationDTO = userHardwareCertificationAPI.getById(request.getId());
        try {
            userHardwareCertificationAPI.checkNumAndUpdateState(request.getId(), IacUserHardwareCertificationStateEnum.APPROVED);
            auditLogAPI.recordLog(RCDC_HARDWARE_CERTIFICATION_APPROVE_SUCCESS_LOG, certificationDTO.getUserName(),
                    certificationDTO.getUpperCaseMac(), certificationDTO.getI18nFeatureCode());
            return CommonWebResponse.success(RCDC_HARDWARE_CERTIFICATION_APPROVE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RCDC_HARDWARE_CERTIFICATION_APPROVE_FAIL, certificationDTO.getUserName(),
                    certificationDTO.getUpperCaseMac(), certificationDTO.getI18nFeatureCode(), e.getI18nMessage());
            return CommonWebResponse.fail(RCDC_HARDWARE_CERTIFICATION_APPROVE_ERROR, new String[]{e.getI18nMessage()});
        }
    }

    /**
     * 驳回用户硬件特征码
     *
     * @param request 请求参数
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "驳回用户硬件特征码记录")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"驳回用户硬件特征码记录"})})
    @RequestMapping(value = "/reject", method = RequestMethod.POST)
    public CommonWebResponse reject(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getId(), "id can not be null");

        RcoViewUserHardwareCertificationDTO certificationDTO = userHardwareCertificationAPI.getById(request.getId());
        try {
            userHardwareCertificationAPI.checkNumAndUpdateState(request.getId(), IacUserHardwareCertificationStateEnum.REJECTED);
            auditLogAPI.recordLog(RCDC_HARDWARE_CERTIFICATION_REJECT_SUCCESS_LOG, certificationDTO.getUserName(),
                    certificationDTO.getUpperCaseMac(), certificationDTO.getI18nFeatureCode());
            return CommonWebResponse.success(RCDC_HARDWARE_CERTIFICATION_REJECT_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RCDC_HARDWARE_CERTIFICATION_REJECT_FAIL, certificationDTO.getUserName(), certificationDTO.getUpperCaseMac(),
                    certificationDTO.getI18nFeatureCode());
            return CommonWebResponse.fail(RCDC_HARDWARE_CERTIFICATION_REJECT_ERROR, new String[]{e.getI18nMessage()});
        }
    }


    /**
     * 获取管理的组
     *
     * @param adminId adminId
     * @return List<String>
     * @throws BusinessException 业务异常
     */
    private List<String> getPermissionUserGroupIdList(UUID adminId) throws BusinessException {
        ListUserGroupIdRequest listUserGroupIdRequest = new ListUserGroupIdRequest();
        listUserGroupIdRequest.setAdminId(adminId);
        ListUserGroupIdResponse listUserGroupIdResponse = adminDataPermissionAPI.listUserGroupIdByAdminId(listUserGroupIdRequest);
        return listUserGroupIdResponse.getUserGroupIdList();
    }

    /**
     * 获取组
     *
     * @param request request
     * @return String
     */
    private String getUserGroupId(PageWebRequest request) {
        if (ArrayUtils.isNotEmpty(request.getExactMatchArr())) {
            for (ExactMatch exactMatch : request.getExactMatchArr()) {
                if (exactMatch.getName().equals("userGroupId") && exactMatch.getValueArr().length > 0) {
                    return exactMatch.getValueArr()[0];
                }
            }
        }
        return "";
    }

    /**
     * 添加用户组的条件
     *
     * @param request request
     * @param userGroupIdStrList userGroupIdStrList
     */
    private void appendUserGroupIdMatchEqual(PageSearchRequest request, List<String> userGroupIdStrList) {
        UUID[] uuidArr = userGroupIdStrList.stream().filter(idStr -> !idStr.equals(UserGroupHelper.USER_GROUP_ROOT_ID)).map(UUID::fromString)
                .toArray(UUID[]::new);
        request.appendCustomMatchEqual(new MatchEqual("userGroupId", uuidArr));
    }

    /**
     * 创建用户-终端绑定关系
     *
     * @param request 请求
     * @param builder 批处理
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "创建用户-终端绑定关系")
    @ApiVersions({@ApiVersion(value = Version.V3_2_271, descriptions = {"创建用户-终端绑定关系"})})
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public DefaultWebResponse create(CreateUserMacBindingWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        List<UserMacBindingDTO> userTerminalBindingList = request.getUserTerminalBindingList();
        Assert.isTrue(!CollectionUtils.isEmpty(userTerminalBindingList), "builder can not be null");
        Assert.notNull(builder, "builder can not be null");

        return batchCreateBinding(userTerminalBindingList, builder, false);
    }



    /**
     * 用户-mac绑定模板下载
     *
     * @param request 请求参数
     * @return 返回结果
     * @throws IOException 异常
     */
    @ApiOperation("用户-mac绑定模板下载")
    @ApiVersions({@ApiVersion(value = Version.V3_2_271, descriptions = {"提供下载功能"})})
    @RequestMapping(value = "downloadTemplate", method = RequestMethod.GET)
    public DownloadWebResponse downloadTemplate(EmptyDownloadWebRequest request) throws IOException {
        InputStream inputStream =
                this.getClass().getClassLoader().getResourceAsStream(TEMPLATE_PATH + TEMPLATE_NAME + SYMBOL_SPOT + TEMPLATE_TYPE);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        Assert.notNull(inputStream, "download template must exists");
        return builder.setInputStream(inputStream, inputStream.available()).setName(TEMPLATE_NAME, TEMPLATE_TYPE).build();
    }

    /**
     * 导入用户-mac绑定关系
     *
     * @param file 导入的文件对象
     * @param builder 批人数处理
     * @return 返回WebResponse 对象
     * @throws BusinessException 业务异常
     * @throws IOException IO异常
     */
    @ApiOperation("导入用户-mac绑定关系")
    @ApiVersions({@ApiVersion(value = Version.V3_2_271, descriptions = {"新增接口"})})
    @RequestMapping(value = "importTemplate", method = RequestMethod.POST)
    public DefaultWebResponse importUser(ChunkUploadFile file, BatchTaskBuilder builder) throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        Assert.notNull(builder, "builder is not null");

        List<ImportUserMacBindingDTO> importDataList = importUserMacBindingHandler.getImportDataList(file);
        importUserMacBindingHandler.validate(importDataList);
        List<UserMacBindingDTO> userMacBindingDTOList = importDataList.stream().map(dto -> {
            UserMacBindingDTO userMacBindingDTO = new UserMacBindingDTO();
            BeanUtils.copyProperties(dto, userMacBindingDTO);
            return userMacBindingDTO;
        }).collect(Collectors.toList());
        return batchCreateBinding(userMacBindingDTOList, builder, true);
    }

    private DefaultWebResponse batchCreateBinding(List<UserMacBindingDTO> userMacBindingDTOList, BatchTaskBuilder builder, boolean isImport)
            throws BusinessException {
        // 去重
        userMacBindingDTOList = userMacBindingDTOList.stream().distinct().collect(Collectors.toList());
        String taskName;
        String itemName;
        if (isImport) {
            itemName = HardwareCertificationBusinessKey.RCDC_HARDWARE_CERTIFICATION_IMPORT_BINDING_ITEM_NAME;
            taskName = HardwareCertificationBusinessKey.RCDC_HARDWARE_CERTIFICATION_IMPORT_BINDING_TASK_NAME;
        } else {
            itemName = HardwareCertificationBusinessKey.RCDC_HARDWARE_CERTIFICATION_CREATE_BINDING_ITEM_NAME;
            taskName = HardwareCertificationBusinessKey.RCDC_HARDWARE_CERTIFICATION_CREATE_BINDING_TASK_NAME;
        }

        final Iterator<I18nBatchTaskItem<UserMacBindingDTO>> iterator =
                userMacBindingDTOList.stream().map(userHardwareDTO -> I18nBatchTaskItem.Builder.build
                                (UUID.randomUUID(), itemName, userHardwareDTO, userHardwareDTO.getUserName(),
                                        MacUtils.toUpperCase(userHardwareDTO.getTerminalMac())))
                        .iterator();
        CreateUserTerminalBindingBatchHandler handler = new CreateUserTerminalBindingBatchHandler(iterator);
        handler.setUserHardwareCertificationAPI(userHardwareCertificationAPI);
        BatchTaskSubmitResult result = I18nBatchTaskBuilder.wrap(builder)
                .setAuditLogAPI(auditLogAPI)
                .setTaskName(taskName)
                .registerHandler(handler)
                .start();
        return DefaultWebResponse.Builder.success(result);
    }
}
