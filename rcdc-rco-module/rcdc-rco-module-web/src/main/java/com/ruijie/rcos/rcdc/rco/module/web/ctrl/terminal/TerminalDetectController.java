package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.StartTerminalDetectBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.TerminalIdBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.StartBatDetectWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.TerminalDetectPageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.TerminalDetectListContentVO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalDetectAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbDetectResultDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalDetectDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbDetectDateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.CbbTerminalDetectPageRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 终端检测
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/15
 *
 * @author nt
 */
@Api(tags = "VDI终端检测")
@Controller
@RequestMapping("/rco/terminal/detect")
@EnableCustomValidate(enable = false)
public class TerminalDetectController {

    @Autowired
    private CbbTerminalOperatorAPI operatorAPI;

    @Autowired
    private CbbTerminalDetectAPI recordAPI;

    @Autowired
    private CbbTerminalDetectAPI terminalDetectAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 发起终端检测
     *
     * @param request 请求参数
     * @param builder 批任务
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("终端检测")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限-终端编辑"})})
    @EnableAuthority
    @RequestMapping(value = "start")
    public DefaultWebResponse startDetect(StartBatDetectWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        String[] terminalIdArr = request.getIdArr();

        final Iterator<TerminalIdBatchTaskItem> iterator = Stream.of(terminalIdArr).map(this::buildTerminalIdBatchTaskItem).iterator();
        StartTerminalDetectBatchTaskHandler handler =
                new StartTerminalDetectBatchTaskHandler(operatorAPI, terminalDetectAPI, iterator, auditLogAPI);

        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_START_TERMINAL_DETECT_TASK_NAME, new String[] {})
                .setTaskDesc(TerminalBusinessKey.RCDC_START_TERMINAL_DETECT_TASK_DESC, new String[] {}) //
                .registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    private TerminalIdBatchTaskItem buildTerminalIdBatchTaskItem(String id) {
        return new TerminalIdBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_START_TERMINAL_DETECT_ITEM_NAME), id);
    }

    /**
     * 获取终端检测分页列表
     *
     * @param request 请求参数
     * @return 检测分页列表
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "list")
    public DefaultWebResponse list(TerminalDetectPageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbTerminalDetectPageRequest pageRequest = buildCbbTerminalDetectPageRequest(request);
        DefaultPageResponse<CbbTerminalDetectDTO> listDetectResp = recordAPI.pageQuery(pageRequest);

        CbbDetectResultDTO detectResultResp = recordAPI.getDetectResult(CbbDetectDateEnums.TODAY);

        TerminalDetectListContentVO contentVO = new TerminalDetectListContentVO();
        contentVO.setItemArr(listDetectResp.getItemArr());
        contentVO.setTotal(listDetectResp.getTotal());
        contentVO.setResult(detectResultResp.getResult());
        contentVO.setThreshold(detectResultResp.getThreshold());

        return DefaultWebResponse.Builder.success(contentVO);
    }

    private CbbTerminalDetectPageRequest buildCbbTerminalDetectPageRequest(TerminalDetectPageWebRequest request) {
        CbbTerminalDetectPageRequest pageRequest = new CbbTerminalDetectPageRequest();
        pageRequest.setLimit(request.getLimit());
        pageRequest.setPage(request.getPage());
        pageRequest.setStartTime(request.getStartTime());
        pageRequest.setEndTime(request.getEndTime());
        return pageRequest;
    }
}
