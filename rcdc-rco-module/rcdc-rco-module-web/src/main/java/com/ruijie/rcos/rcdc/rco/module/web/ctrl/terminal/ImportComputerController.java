package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;


import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbThirdPartyDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.CreateComputerBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.ImportComputerBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.validation.ComputerValidation;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.ImportComputerBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.ImportUserWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportComputerHandler;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Description: PC导入
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/18
 *
 * @author zqj
 */
@Api(tags = "PC终端")
@Controller
@RequestMapping("/rco/computer")
@EnableCustomValidate(validateClass = ComputerValidation.class)
public class ImportComputerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportComputerController.class);

    /** 用户模板文件名称 */
    private static final String USER_TEMPLATE_NAME = "computer_model";

    /** 用户模板文件类型 */
    private static final String USER_TEMPLATE_TYPE = "xlsx";

    /** 模板文件路径 */
    private static final String TEMPLATE_PATH = "template/";

    private static final String SYMBOL_SPOT = ".";

    private static final String IMPORT_COMPUTER_THREAD_POOL = "import_computer_thread_pool";

    @Autowired
    private ComputerBusinessAPI computerBusinessAPI;

    @Autowired
    private ImportComputerHandler importComputerHandler;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    /**
     * pc终端模板下载
     *
     * @param request 请求参数
     * @return 返回结果
     * @throws IOException 异常
     */
    @ApiOperation("用户PC终端下载")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20)})
    @RequestMapping(value = "downloadTemplate",method = RequestMethod.GET)
    @EnableAuthority
    public DownloadWebResponse downloadTemplate(EmptyDownloadWebRequest request) throws IOException {
        InputStream inputStream =
                this.getClass().getClassLoader().getResourceAsStream(TEMPLATE_PATH + USER_TEMPLATE_NAME + SYMBOL_SPOT + USER_TEMPLATE_TYPE);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setInputStream(inputStream, inputStream.available()).setName(USER_TEMPLATE_NAME, USER_TEMPLATE_TYPE).build();
    }

    /**
     * 导入pc数据
     *
     * @param file 导入的文件对象
     * @param builder 批人数处理
     * @return 返回WebResponse 对象
     * @throws IOException IO异常
     * @throws BusinessException 业务异常
     */
    @ApiOperation("导入PC终端数据")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "importComputer", method = RequestMethod.POST)
    @EnableAuthority
    public ImportUserWebResponse importComputer(ChunkUploadFile file, BatchTaskBuilder builder) throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        Assert.notNull(builder, "builder is not null");

        List<ImportComputerDTO> importUserList = importComputerHandler.parseTemplateUserData(file);
        importComputerHandler.validate(importUserList);
        try {
            // 校验通过，开始执行批量任务
            ImportComputerDTO[] computerArr = importUserList.toArray(new ImportComputerDTO[0]);
            final Iterator<CreateComputerBatchTaskItem> iterator =
                    Stream.of(computerArr).map(computer -> CreateComputerBatchTaskItem.builder().itemId(UUID.randomUUID()) //
                            .itemName(ComputerBusinessKey.RCDC_RCO_IMPORT_COMPUTER_ITEM_NAME, new String[] {})
                            .itemComputer(computer).build()).iterator();
            ImportComputerBatchTaskHandlerRequest  request = new ImportComputerBatchTaskHandlerRequest();
            request.setBatchTaskItemIterator(iterator);
            request.setAuditLogAPI(auditLogAPI);
            request.setComputerBusinessAPI(computerBusinessAPI);
            request.setCbbTerminalGroupMgmtAPI(cbbTerminalGroupMgmtAPI);
            request.setCbbUserAPI(cbbUserAPI);
            request.setCbbThirdPartyDeskStrategyMgmtAPI(cbbThirdPartyDeskStrategyMgmtAPI);
            ImportComputerBatchTaskHandler handler = new ImportComputerBatchTaskHandler(request);
            final UUID uniqueTaskThreadPoolId = UUID.nameUUIDFromBytes(IMPORT_COMPUTER_THREAD_POOL.getBytes());
            BatchTaskSubmitResult batchTaskSubmitResult =
                    builder.setTaskName(ComputerBusinessKey.RCDC_RCO_IMPORT_COMPUTER_TASK_NAME).
                            setTaskDesc(ComputerBusinessKey.RCDC_RCO_IMPORT_COMPUTER_TASK_DESC)
                            .enableParallel().registerHandler(handler).enablePerformanceMode(uniqueTaskThreadPoolId, 30).start();
            ImportUserWebResponse result = new ImportUserWebResponse();
            result.setStatus(WebResponse.Status.SUCCESS);
            result.setContent(batchTaskSubmitResult);
            return result;
        } catch (Exception e) {
            LOGGER.error("导入PC终端失败", e);
            throw e;
        }
    }


}
