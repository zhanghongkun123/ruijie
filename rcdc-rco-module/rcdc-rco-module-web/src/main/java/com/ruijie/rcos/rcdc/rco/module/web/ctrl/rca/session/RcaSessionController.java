package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.session;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.ViewHostSessionDTO;
import com.ruijie.rcos.rcdc.rca.module.def.request.PageSearchRcaSessionRequest;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.session.handler.DestroyHostSessionBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdArrRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/15 11:06
 *
 * @author zhangsiming
 */

@Api(tags = "云应用会话管理")
@Controller
@RequestMapping("/rca/session")
public class RcaSessionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaSessionController.class);

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 获取应用主机当前会话列表
     *
     * @param request 请求参数
     * @return 结果 用户列表
     * @throws BusinessException 异常
     */
    @ApiOperation("获取应用主机当前会话列表")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"获取应用主机当前会话列表"})})
    public DefaultWebResponse getHostSessionList(PageSearchRcaSessionRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        PageQueryResponse<ViewHostSessionDTO> response = rcaHostSessionAPI.pageQueryHostSession(request, Boolean.TRUE);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 注销应用主机会话
     *
     * @param request        应用主机会话记录ID列表
     * @param optLogRecorder optLogRecorder
     * @param builder        builder
     * @return the default web response
     * @throws BusinessException BusinessException
     */
    @ApiOperation("注销应用主机会话")
    @RequestMapping(value = "destroy", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"注销应用主机会话"})})
    @EnableAuthority
    public DefaultWebResponse destroyHostSession(IdArrRequest request, ProgrammaticOptLogRecorder optLogRecorder, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(builder, "builder不能为null");
        Assert.notNull(optLogRecorder, "optLogRecorder不能为null");
        UUID[] idArr = request.getIdArr();
        // 批量注销
        final Iterator<? extends I18nBatchTaskItem<?>> iterator = Stream.of(idArr) //
                .map(id -> I18nBatchTaskItem.Builder.build(id, RcaBusinessKey.RCDC_RCA_APP_HOST_DESTROY_SESSION_TASK_ITEM_NAME))//
                .iterator();

        DestroyHostSessionBatchTaskHandler handler =
                new DestroyHostSessionBatchTaskHandler(iterator, rcaHostSessionAPI);
        BatchTaskSubmitResult result = I18nBatchTaskBuilder.wrap(builder).setAuditLogAPI(auditLogAPI)
                .setTaskName(RcaBusinessKey.RCDC_RCA_APP_HOST_DESTROY_SESSION_TASK_ITEM_NAME)
                .enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

}
