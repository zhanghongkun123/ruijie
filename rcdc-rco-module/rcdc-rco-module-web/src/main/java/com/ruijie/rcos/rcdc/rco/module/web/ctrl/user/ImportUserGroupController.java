package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacImportUserAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask.CreateUserGroupBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportUserGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.ImportUserGroupWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.batchtask.ImportUserGroupBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.request.ImportUserGroupBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportUserGroupHandler;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportUserGroupService;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse.Status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 导入用户组
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2010/4/23
 *
 * @author zhangyichi
 */
@Api(tags = "用户组导入功能")
@Controller
@RequestMapping("/rco/user/group")
@EnableCustomValidate(enable = false)
public class ImportUserGroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportUserGroupController.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacImportUserAPI cbbImportUserAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private ImportUserGroupService importUserGroupService;

    @Autowired
    private ImportUserGroupHandler importUserGroupHandler;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    /** 用户组模板文件名称 */
    private static final String USER_GROUP_TEMPLATE_NAME = "user_group_model";

    /** 纯IDV模式下用户组模板文件名称 */
    private static final String USER_GROUP_TEMPLATE_NAME_ONLY_FOR_IDV = "user_group_model_only_for_idv";

    /** 用户组模板文件类型 */
    private static final String USER_GROUP_TEMPLATE_TYPE = "xlsx";

    /** 用户组模板文件路径 */
    private static final String USER_GROUP_TEMPLATE_PATH = "template/";

    private static final String SYMBOL_SPOT = ".";

    /**
     * 导入用户组数据
     *
     * @param file 导入的文件对象
     * @param builder 批人数处理
     * @return 返回WebResponse 对象
     * @throws IOException IO异常
     * @throws BusinessException 业务异常
     */
    @ApiOperation("导入用户组数据")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "importUserGroup", method = RequestMethod.POST)

    @EnableAuthority
    public ImportUserGroupWebResponse importUserGroup(ChunkUploadFile file, BatchTaskBuilder builder) throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        Assert.notNull(builder, "builder is not null");

        List<ImportUserGroupDTO> importUserGroupList = importUserGroupHandler.parseTemplateUserGroupData(file);
        importUserGroupHandler.validate(importUserGroupList);
        // 校验通过，开始执行批量任务
        ImportUserGroupDTO[] userGroupArr = importUserGroupList.toArray(new ImportUserGroupDTO[importUserGroupList.size()]);
        final Iterator<CreateUserGroupBatchTaskItem> iterator =
                Stream.of(userGroupArr).map(userGroup -> CreateUserGroupBatchTaskItem.builder().itemId(UUID.randomUUID()) //
                        .itemName(UserBusinessKey.RCDC_RCO_IMPORT_USER_GROUP_TASK_NAME, new String[] {}).itemUser(userGroup).build()).iterator();
        ImportUserGroupBatchTaskHandlerRequest request = new ImportUserGroupBatchTaskHandlerRequest();
        request.setBatchTaskItemIterator(iterator);
        request.setAuditLogAPI(auditLogAPI);
        request.setCbbImportUserAPI(cbbImportUserAPI);
        request.setUserDesktopConfigAPI(userDesktopConfigAPI);
        request.setImportUserGroupService(importUserGroupService);
        request.setCbbDeskSpecAPI(cbbDeskSpecAPI);
        ImportUserGroupBatchTaskHandler handler = new ImportUserGroupBatchTaskHandler(request);

        BatchTaskSubmitResult batchTaskSubmitResult = builder.setTaskName(UserBusinessKey.RCDC_RCO_IMPORT_USER_GROUP_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_IMPORT_USER_GROUP_TASK_DESC).enableParallel().registerHandler(handler).start();

        ImportUserGroupWebResponse result = new ImportUserGroupWebResponse();
        result.setStatus(Status.SUCCESS);
        result.setContent(batchTaskSubmitResult);
        return result;
    }

    /**
     * 用户组模板下载
     *
     * @param request 请求参数
     * @return 返回结果
     * @throws IOException 异常
     */
    @ApiOperation("用户组模板下载")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "downloadTemplate", method = RequestMethod.GET)

    @EnableAuthority
    public DownloadWebResponse downloadTemplate(EmptyDownloadWebRequest request) throws IOException {
        String resourceName = USER_GROUP_TEMPLATE_NAME;
        if (serverModelAPI.isIdvModel() || serverModelAPI.isMiniModel()) {
            resourceName = USER_GROUP_TEMPLATE_NAME_ONLY_FOR_IDV;
        }
        LOGGER.debug("download template<{}>", resourceName);
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(USER_GROUP_TEMPLATE_PATH + resourceName + SYMBOL_SPOT + USER_GROUP_TEMPLATE_TYPE);
        byte[] templateByteArr = IOUtils.toByteArray(inputStream);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setInputStream(new ByteArrayInputStream(templateByteArr), templateByteArr.length)
                .setName(resourceName, USER_GROUP_TEMPLATE_TYPE).build();
    }
}
