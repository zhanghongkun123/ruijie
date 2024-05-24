package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskimport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskimport.batchtask.CreateVDIDeskBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskimport.batchtask.ImportVDIDeskBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskimport.batchtask.ImportVDIDeskBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportVDIDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.ImportUserGroupWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportVDIDeskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportVDIDeskService;
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
 * Description: 批量创建桌面虚拟机
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2010/4/23
 *
 * @author linrenjian
 */
@Api(tags = "批量导入桌面虚拟机")
@Controller
@RequestMapping("/rco/user")
@EnableCustomValidate(enable = false)
public class DeskImportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskImportController.class);


    @Autowired
    private ImportVDIDeskHandler importVDIDeskHandler;

    @Autowired
    private ServerModelAPI serverModelAPI;

    /**
     * 用户API
     */
    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    /**
     * 镜像API
     */
    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    /**
     * VDI云桌面策略
     */
    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    /**
     * 网络策略
     */
    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    /**
     * 导入VDI云桌面配置服务
     */
    @Autowired
    private ImportVDIDeskService importVDIDeskService;

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /** 云桌面模板文件名称 */
    private static final String VDI_DESK_TEMPLATE_NAME = "desk_model";

    /**模板文件类型 */
    private static final String TEMPLATE_TYPE = "xlsx";

    /** 模板文件路径 */
    private static final String TEMPLATE_PATH = "template/";

    private static final String SYMBOL_SPOT = ".";

    /**
     * 导入桌面数据
     *
     * @param file 导入的文件对象
     * @param builder 批人数处理
     * @return 返回WebResponse 对象
     * @throws IOException IO异常
     * @throws BusinessException 业务异常
     */
    @ApiOperation("导入VDI云桌面数据")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "importVDIDesk", method = RequestMethod.POST)
    @EnableAuthority
    public ImportUserGroupWebResponse importVDIDesk(ChunkUploadFile file, BatchTaskBuilder builder) throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        Assert.notNull(builder, "builder is not null");
        //获取表格中的数据
        List<ImportVDIDeskDTO> importVDIDeskDTOList = importVDIDeskHandler.parseTemplateUserGroupData(file);
        //校验数据
        importVDIDeskHandler.validate(importVDIDeskDTOList);
        // 校验通过，开始执行批量任务
        ImportVDIDeskDTO[] importVDIDeskDTOArr = importVDIDeskDTOList.toArray(new ImportVDIDeskDTO[0]);
        final Iterator<CreateVDIDeskBatchTaskItem> iterator =
                Stream.of(importVDIDeskDTOArr).map(importVDIDeskDTO -> CreateVDIDeskBatchTaskItem.builder().itemId(UUID.randomUUID()) //
                        .itemName(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_TASK_NAME, new String[] {}).itemUser(importVDIDeskDTO).build()).iterator();
        ImportVDIDeskBatchTaskHandlerRequest request = new ImportVDIDeskBatchTaskHandlerRequest();
        request.setBatchTaskItemIterator(iterator);
        request.setAuditLogAPI(auditLogAPI);
        request.setCbbUserAPI(cbbUserAPI);
        request.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        request.setCbbVDIDeskStrategyMgmtAPI(cbbVDIDeskStrategyMgmtAPI);
        request.setCbbNetworkMgmtAPI(cbbNetworkMgmtAPI);
        request.setImportVDIDeskService(importVDIDeskService);
        request.setCloudDesktopWebService(cloudDesktopWebService);
        //批量导入VDI云桌面
        ImportVDIDeskBatchTaskHandler handler = new ImportVDIDeskBatchTaskHandler(request);

        BatchTaskSubmitResult batchTaskSubmitResult = builder.setTaskName(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_TASK_NAME)
                .setTaskDesc(UserBusinessKey.RCDC_RCO_IMPORT_VDI_DESK_TASK_DESC).enableParallel().registerHandler(handler).start();

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
    @ApiOperation("VDI云桌面模板下载")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "downloadVDIDeskTemplate", method = RequestMethod.GET)
    @EnableAuthority
    public DownloadWebResponse downloadTemplate(EmptyDownloadWebRequest request) throws IOException {
        String resourceName = VDI_DESK_TEMPLATE_NAME;
        LOGGER.debug("download template<{}>", resourceName);
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(TEMPLATE_PATH + resourceName + SYMBOL_SPOT + TEMPLATE_TYPE);
        byte[] templateByteArr = IOUtils.toByteArray(inputStream);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setInputStream(new ByteArrayInputStream(templateByteArr), templateByteArr.length)
                .setName(resourceName, TEMPLATE_TYPE).build();
    }
}
