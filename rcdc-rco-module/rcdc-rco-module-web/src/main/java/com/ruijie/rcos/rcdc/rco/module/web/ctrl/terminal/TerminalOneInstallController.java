package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationPacketAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientCompressionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AppClientCompressionDTO;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.RcoBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.EditOneClickInstallBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.OneClickInstallWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response.OneClickInstallWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.UserOrGroupBatchStateCache;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Description: 终端一键安装
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/17
 *
 * @author TD
 */
@Api("APP一键安装")
@Controller
@RequestMapping("/rco/app/oneClickInstall")
public class TerminalOneInstallController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalOneInstallController.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private ClientCompressionAPI clientCompressionAPI;

    @Autowired
    private BaseApplicationPacketAPI baseApplicationPacketAPI;

    @Autowired
    private RcoGlobalParameterAPI globalParameterAPI;


    /**
     * 获取一键安装配置信息
     *
     * @return 一键安装配置信息
     */
    @ApiOperation("获取一键安装配置信息")
    @RequestMapping(value = "/windows/detail", method = RequestMethod.POST)
    public CommonWebResponse<OneClickInstallWebResponse> getOneClickInstallConfig() {
        OneClickInstallWebResponse webResponse = new OneClickInstallWebResponse();
        try {
            AppClientCompressionDTO appClientCompression = clientCompressionAPI.getAppClientCompressionConfig();
            BeanUtils.copyProperties(appClientCompression, webResponse);
            return CommonWebResponse.success(webResponse);
        } catch (BusinessException e) {
            LOGGER.error("获取一键安装功能信息失败", e);
            return CommonWebResponse.fail(RcoBusinessKey.RCDC_RCO_GET_ONE_CLICK_INSTALL_CONFIG_FAIL);
        }
    }

    /**
     * 修改一键安装配置信息
     *
     * @param request 修改的配置信息
     * @param builder 批处理任务
     * @return 修改结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("修改一键安装配置信息")
    @RequestMapping(value = "/windows/edit", method = RequestMethod.POST)
    public CommonWebResponse<BatchTaskSubmitResult> editOneClickInstallConfig(OneClickInstallWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "OneClickInstallWebRequest is not null");
        Assert.notNull(builder, "BatchTaskBuilder is not null");
        // 只允许一个批量应用用户或用户组策略任务在执行
        if (!UserOrGroupBatchStateCache.STATE.addSyncTask(Constants.ONE_CLICK_INSTALL)) {
            throw new BusinessException(RcoBusinessKey.RCDC_RCO_UPDATE_ONE_CLICK_INSTALL_BATCH_TASK_SYNCHRONIZE);
        }
        try {
            final Iterator<DefaultBatchTaskItem> iterator =
                    Stream.of(UUID.randomUUID())
                            .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                    .itemName(LocaleI18nResolver.resolve(RcoBusinessKey.RCDC_RCO_ASYNC_UPDATE_ONE_CLICK_INSTALL_NAME)).build())
                            .iterator();
            AppClientCompressionDTO compressionDTO = new AppClientCompressionDTO();
            BeanUtils.copyProperties(request, compressionDTO);
            EditOneClickInstallBatchTaskHandler batchTaskHandler =
                    new EditOneClickInstallBatchTaskHandler(iterator, auditLogAPI, globalParameterAPI, baseApplicationPacketAPI, compressionDTO);
            BatchTaskSubmitResult result = builder.setTaskName(RcoBusinessKey.RCDC_RCO_ASYNC_UPDATE_ONE_CLICK_INSTALL_NAME)
                    .setTaskDesc(RcoBusinessKey.RCDC_RCO_ASYNC_UPDATE_ONE_CLICK_INSTALL_DESC) //
                    .registerHandler(batchTaskHandler).start();
            return CommonWebResponse.success(result);
        } catch (BusinessException e) {
            LOGGER.error("修改一键安装配置任务失败", e);
            UserOrGroupBatchStateCache.STATE.removeSyncTask(Constants.ONE_CLICK_INSTALL);
            return CommonWebResponse.fail(RcoBusinessKey.RCDC_RCO_UPDATE_ONE_CLICK_INSTALL_FAIL_LOG, new String[] {e.getI18nMessage()});
        }
    }
}
