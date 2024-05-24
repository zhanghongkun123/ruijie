package com.ruijie.rcos.rcdc.rco.module.web.ctrl.printer;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyTciNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterManageServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.printer.dto.PrinterConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.printer.request.EditPrinterConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.printer.batchtask.BatchDeletePrinterConfigTaskHandle;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.printer.request.PrinterPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.CheckDuplicationDTO;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

import io.swagger.annotations.ApiOperation;


/**
 * Description: 打印机配置管理
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/10
 *
 * @author chenjiehui
 */
@Controller
@RequestMapping("rco/printer")
public class PrinterManageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrinterManageController.class);

    @Autowired
    private PrinterManageServiceAPI printerManageServiceAPI;

    @Autowired
    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 获取打印机配置开关状态
     * @return 打印机配置开关状态
     */
    @ApiOperation("获取打印机配置开关")
    @RequestMapping(value = "openStatus", method = RequestMethod.POST)
    public CommonWebResponse openStatus() {
        return CommonWebResponse.success(printerManageServiceAPI.getPrinterConfigStatus());
    }

    /**
     * 获取打印机配置列表
     * @param request 请求
     * @return 打印机配置列表
     */
    @ApiOperation("获取打印机配置列表")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @NoAuthUrl
    public CommonWebResponse list(PageWebRequest request) {
        Assert.notNull(request, "request can not be null");
        PrinterPageSearchRequest printerPageSearchRequest = new PrinterPageSearchRequest(request);
        DefaultPageResponse<PrinterConfigDTO> response = printerManageServiceAPI.list(printerPageSearchRequest);
        return CommonWebResponse.success(response);
    }

    /**
     * 获取打印机配置详情
     * @param request 请求
     * @return 打印机配置详情
     * @throws BusinessException /
     */
    @ApiOperation("获取打印机配置详情")
    @RequestMapping(value = "getInfo", method = RequestMethod.POST)
    public CommonWebResponse getInfo(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        return CommonWebResponse.success(printerManageServiceAPI.getPrinterConfigById(request.getId()));
    }


    /**
     * 编辑打印机配置详情
     * @param request 请求
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑打印机配置详情")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public CommonWebResponse edit(EditPrinterConfigRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        printerManageServiceAPI.editPrinterConfigById(request);
        PrinterConfigDTO printerConfig = printerManageServiceAPI.getPrinterConfigById(request.getId());
        auditLogAPI.recordLog(PrinterBusinessKey.RCDC_RCO_CONFIG_PRINTER_SUCCESS, printerConfig.getConfigName());
        return CommonWebResponse.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS,  new String[] {});
    }

    /**
     * 删除打印机配置
     * @param request 请求
     * @param builder /
     * @return /
     * @throws BusinessException /
     */
    @ApiOperation("删除打印机配置")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonWebResponse delete(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException  {
        Assert.notNull(request, "request can not be null");
        Assert.notEmpty(request.getIdArr(), "id不能为空");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");
        UUID[] idArr = request.getIdArr();

        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr).distinct()
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(PrinterBusinessKey.RCDC_RCO_PRINTER_CONFIG_DELETE_ITEM_NAME, new String[]{}).build()).iterator();
        final BatchDeletePrinterConfigTaskHandle handler = new BatchDeletePrinterConfigTaskHandle(iterator,
                auditLogAPI, this.printerManageServiceAPI);
        BatchTaskSubmitResult result = startDeleteBatchTask(idArr, handler, builder);
        return CommonWebResponse.success(result);
    }

    /**
     *
     * @param idArr id数组
     * @param handler /
     * @param builder /
     * @return /
     * @throws BusinessException
     */
    private BatchTaskSubmitResult startDeleteBatchTask(UUID[] idArr, BatchDeletePrinterConfigTaskHandle handler, BatchTaskBuilder builder)
            throws BusinessException {

        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setIsBatch(false);
            PrinterConfigDTO printerConfigDTO = printerManageServiceAPI.getPrinterConfigById(idArr[0]);
            handler.setPrinterConfigName(printerConfigDTO.getConfigName());
            result = builder.setTaskName(PrinterBusinessKey.RCDC_RCO_PRINTER_CONFIG_DELETE_SINGLE_TASK_NAME, new String[]{})
                            .setTaskDesc(PrinterBusinessKey.RCDC_RCO_PRINTER_CONFIG_DELETE_SINGLE_TASK_DESC,
                                    new String[]{printerConfigDTO.getConfigName()})
                            .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(PrinterBusinessKey.RCDC_RCO_PRINTER_CONFIG_DELETE_BATCH_TASK_NAME, new String[]{})
                            .setTaskDesc(PrinterBusinessKey.RCDC_RCO_PRINTER_CONFIG_DELETE_BATCH_TASK_DESC, new String[]{})
                            .enableParallel().registerHandler(handler).start();
        }

        return result;
    }


    /**
     * 开启打印机配置
     * @return /
     */
    @ApiOperation("开启打印机配置")
    @RequestMapping(value = "enable", method = RequestMethod.POST)
    public CommonWebResponse enable() {
        printerManageServiceAPI.enablePrinterConfig();
        deskStrategyTciNotifyAPI.notifyFetchStartParams();
        auditLogAPI.recordLog(PrinterBusinessKey.RCDC_RCO_PRINTER_ENABLE_SUCCESS);
        return CommonWebResponse.success();
    }

    /**
     * 关闭打印机配置
     * @return /
     */
    @ApiOperation("关闭打印机配置")
    @RequestMapping(value = "disable", method = RequestMethod.POST)
    public CommonWebResponse disable() {
        printerManageServiceAPI.disablePrinterConfig();
        deskStrategyTciNotifyAPI.notifyFetchStartParams();
        auditLogAPI.recordLog(PrinterBusinessKey.RCDC_RCO_PRINTER_DISABLE_SUCCESS);
        return CommonWebResponse.success();
    }

    /**
     * 校验配置名
     * @param editPrinterConfigRequest 配置参数
     * @return /
     */
    @ApiOperation("打印机配置名校验是否重复")
    @RequestMapping(value = "/configName/check", method = RequestMethod.POST)
    public CommonWebResponse checkConfigName(EditPrinterConfigRequest editPrinterConfigRequest) {
        Assert.notNull(editPrinterConfigRequest, "request can not be null");
        boolean hasDuplication = printerManageServiceAPI.checkConfigName(editPrinterConfigRequest);
        return CommonWebResponse.success(new CheckDuplicationDTO(hasDuplication));
    }


    /**
     * 导入特殊打印机配置
     * @param file 文件
     * @return CommonWebResponse
     * @throws BusinessException /
     */
    @ApiOperation("导入特殊打印机配置")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public CommonWebResponse importSpecialConfig(ChunkUploadFile file) throws BusinessException {
        Assert.notNull(file, "ChunkUploadFile 不能为null");
        printerManageServiceAPI.importSpecialConfig(file);
        auditLogAPI.recordLog(PrinterBusinessKey.RCDC_RCO_PRINTER_CONFIGURATION_IMPORT_SUCCESS);
        return CommonWebResponse.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
    }

    /**
     * 获取打印机特殊配置信息
     * @return CommonWebResponse
     */
    @ApiOperation("获取特殊打印机配置信息")
    @RequestMapping(value = "specialConfig/getInfo", method = RequestMethod.POST)
    public CommonWebResponse getSpecialConfigInfo() {
        return CommonWebResponse.success(printerManageServiceAPI.getPrinterSpecialConfig());
    }



}
