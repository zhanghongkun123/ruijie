package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserImportEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacImportUserAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.CreateUserBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.ImportUserBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportUserDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.ImportUserBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.ImportUserWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportUserHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 导入用户
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/19
 *
 * @author Jarman
 */
@Api(tags = "用户导入功能")
@Controller
@RequestMapping("/rco/user")
@EnableCustomValidate(enable = false)
public class ImportUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportUserController.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private IacImportUserAPI cbbImportUserAPI;

    @Autowired
    private ImportUserHandler importUserHandler;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private MailMgmtAPI mailMgmtAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private DataSyncAPI dataSyncAPI;

    /** 模板文件名称 */
    private static final String TEMPLATE_NAME = "templates";

    /** 纯IDV模式下模板文件名称 */
    private static final String TEMPLATE_NAME_ONLY_FOR_IDV = "templates_only_for_idv";

    /** 模板文件类型 */
    private static final String TEMPLATE_TYPE = "zip";

    /** 用户模板文件名称 */
    private static final String USER_TEMPLATE_NAME = "user_model";

    /** 用户模板文件名称 */
    private static final String USER_TEMPLATE_NAME_ONLY_FOR_IDV = "user_model_only_for_idv";

    /** 用户模板文件类型 */
    private static final String USER_TEMPLATE_TYPE = "xlsx";

    /** 模板文件路径 */
    private static final String TEMPLATE_PATH = "template/";

    private static final String SYMBOL_SPOT = ".";

    private static final String IMPORTUSERTHREADPOOL = "importUserThreadPool";

    /**
     * 导入用户数据
     *
     * @param file 导入的文件对象
     * @param builder 批人数处理
     * @return 返回WebResponse 对象
     * @throws IOException IO异常
     * @throws BusinessException 业务异常
     */
    @ApiOperation("导入用户数据")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "importUser", method = RequestMethod.POST)
    @EnableAuthority
    public ImportUserWebResponse importUser(ChunkUploadFile file, BatchTaskBuilder builder) throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        Assert.notNull(builder, "builder is not null");

        List<ImportUserDTO> importUserList = importUserHandler.getImportDataList(file);
        for (ImportUserDTO importUserDTO : importUserList) {
            importUserDTO.setIsEdit(IacUserImportEnum.CREATE);
        }
        importUserHandler.validate(importUserList);
        try {
            // 开始导入用户，如果已存在导入任务或是AD域用户在同步中则不允许导入
            cbbImportUserAPI.startImportUser();
            // 校验通过，开始执行批量任务
            ImportUserDTO[] userArr = importUserList.toArray(new ImportUserDTO[importUserList.size()]);
            final Iterator<CreateUserBatchTaskItem> iterator =
                    Stream.of(userArr).map(user -> CreateUserBatchTaskItem.builder().itemId(UUID.randomUUID()) //
                            .itemName(UserBusinessKey.RCDC_RCO_IMPORT_USER_ITEM_NAME, new String[] {}).itemUser(user).build()).iterator();
            ImportUserBatchTaskHandlerRequest request = new ImportUserBatchTaskHandlerRequest();
            request.setBatchTaskItemIterator(iterator);
            request.setAuditLogAPI(auditLogAPI);
            request.setCbbUserAPI(cbbUserAPI);
            request.setCbbUserGroupAPI(cbbUserGroupAPI);
            request.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
            request.setUserDesktopConfigAPI(userDesktopConfigAPI);
            request.setCbbImportUserAPI(cbbImportUserAPI);
            request.setMailMgmtAPI(mailMgmtAPI);
            request.setUserInfoAPI(userInfoAPI);
            request.setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI);
            request.setUserDesktopOperateAPI(userDesktopOperateAPI);
            request.setUserIdentityConfigAPI(userIdentityConfigAPI);
            request.setUserMgmtAPI(userMgmtAPI);
            request.setCertificationStrategyParameterAPI(certificationStrategyParameterAPI);
            request.setDataSyncAPI(dataSyncAPI);
            ImportUserBatchTaskHandler handler = new ImportUserBatchTaskHandler(request);
            final UUID uniqueTaskThreadPoolId = UUID.nameUUIDFromBytes(IMPORTUSERTHREADPOOL.getBytes());
            BatchTaskSubmitResult batchTaskSubmitResult =
                    builder.setTaskName(UserBusinessKey.RCDC_RCO_IMPORT_USER_TASK_NAME).setTaskDesc(UserBusinessKey.RCDC_RCO_IMPORT_USER_TASK_DESC)
                            .enableParallel().registerHandler(handler).enablePerformanceMode(uniqueTaskThreadPoolId, 30).start();
            ImportUserWebResponse result = new ImportUserWebResponse();
            result.setStatus(WebResponse.Status.SUCCESS);
            result.setContent(batchTaskSubmitResult);
            return result;
        } catch (BusinessException e) {
            cbbImportUserAPI.finishImportUser();
            throw e;
        }
    }

    /**
     * 编辑用户数据
     *
     * @param file 导入的文件对象
     * @param builder 批人数处理
     * @return 返回WebResponse 对象
     * @throws IOException IO异常
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑用户数据")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "editUser", method = RequestMethod.POST)

    @EnableAuthority
    public ImportUserWebResponse importEditUser(ChunkUploadFile file, BatchTaskBuilder builder) throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        Assert.notNull(builder, "builder is not null");

        List<ImportUserDTO> importUserList = importUserHandler.getImportDataList(file);
        for (ImportUserDTO importUserDTO : importUserList) {
            importUserDTO.setIsEdit(IacUserImportEnum.EDIT);
        }
        importUserHandler.importValidateExpire(importUserList);
        try {
            // 开始导入用户，如果已存在导入任务或是AD域用户在同步中则不允许导入
            cbbImportUserAPI.startImportUser();
            // 校验通过，开始执行批量任务
            ImportUserDTO[] userArr = importUserList.toArray(new ImportUserDTO[importUserList.size()]);
            final Iterator<CreateUserBatchTaskItem> iterator =
                    Stream.of(userArr).map(user -> CreateUserBatchTaskItem.builder().itemId(UUID.randomUUID()) //
                            .itemName(UserBusinessKey.RCDC_RCO_IMPORT_USER_ITEM_NAME, new String[] {}).itemUser(user).build()).iterator();
            ImportUserBatchTaskHandlerRequest request = new ImportUserBatchTaskHandlerRequest();
            request.setBatchTaskItemIterator(iterator);
            request.setAuditLogAPI(auditLogAPI);
            request.setCbbUserAPI(cbbUserAPI);
            request.setCbbUserGroupAPI(cbbUserGroupAPI);
            request.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
            request.setUserDesktopConfigAPI(userDesktopConfigAPI);
            request.setCbbImportUserAPI(cbbImportUserAPI);
            request.setMailMgmtAPI(mailMgmtAPI);
            request.setUserInfoAPI(userInfoAPI);
            request.setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI);
            request.setUserDesktopOperateAPI(userDesktopOperateAPI);
            request.setUserIdentityConfigAPI(userIdentityConfigAPI);
            request.setUserMgmtAPI(userMgmtAPI);
            request.setDataSyncAPI(dataSyncAPI);
            ImportUserBatchTaskHandler handler = new ImportUserBatchTaskHandler(request);
            final UUID uniqueTaskThreadPoolId = UUID.nameUUIDFromBytes(IMPORTUSERTHREADPOOL.getBytes());
            BatchTaskSubmitResult batchTaskSubmitResult =
                    builder.setTaskName(UserBusinessKey.RCDC_RCO_IMPORT_USER_TASK_NAME).setTaskDesc(UserBusinessKey.RCDC_RCO_IMPORT_USER_TASK_DESC)
                            .enableParallel().registerHandler(handler).enablePerformanceMode(uniqueTaskThreadPoolId, 30).start();
            ImportUserWebResponse result = new ImportUserWebResponse();
            result.setStatus(WebResponse.Status.SUCCESS);
            result.setContent(batchTaskSubmitResult);
            return result;
        } catch (BusinessException e) {
            cbbImportUserAPI.finishImportUser();
            throw e;
        }
    }

    /**
     * 用户模板下载
     *
     * @param request 请求参数
     * @return 返回结果
     * @throws IOException 异常
     */
    @ApiOperation("用户模板下载")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "downloadTemplate", method = RequestMethod.GET)
    @EnableAuthority
    public DownloadWebResponse downloadTemplate(EmptyDownloadWebRequest request) throws IOException {
        String resourceName = USER_TEMPLATE_NAME;
        if (serverModelAPI.isIdvModel() || serverModelAPI.isMiniModel()) {
            resourceName = USER_TEMPLATE_NAME_ONLY_FOR_IDV;
        }
        InputStream inputStream =
                this.getClass().getClassLoader().getResourceAsStream(TEMPLATE_PATH + resourceName + SYMBOL_SPOT + USER_TEMPLATE_TYPE);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setInputStream(inputStream, (long) inputStream.available()).setName(resourceName, USER_TEMPLATE_TYPE).build();
    }

    /**
     * 用户模板和用户组模板打包下载
     *
     * @param request 请求参数
     * @return 返回结果
     * @throws IOException 异常
     */
    @ApiOperation("用户模板和用户组模板打包下载")
    @RequestMapping(value = "downloadTemplatePackage", method = RequestMethod.GET)
    public DownloadWebResponse downloadTemplatePackage(EmptyDownloadWebRequest request) throws IOException {
        String resourceName = TEMPLATE_NAME;
        if (serverModelAPI.isIdvModel() || serverModelAPI.isMiniModel()) {
            resourceName = TEMPLATE_NAME_ONLY_FOR_IDV;
        }
        LOGGER.debug("download template<{}>", resourceName);
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(TEMPLATE_PATH + resourceName + SYMBOL_SPOT + TEMPLATE_TYPE);
        byte[] templateByteArr = IOUtils.toByteArray(inputStream);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setInputStream(new ByteArrayInputStream(templateByteArr), templateByteArr.length).setName(resourceName, TEMPLATE_TYPE).build();
    }
}
