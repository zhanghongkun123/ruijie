package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import java.io.File;
import java.io.IOException;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCollectLogStateEnums;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.TerminalIdWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.TerminalLogDownLoadWebRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLogAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalCollectLogStatusDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalLogFileInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/3
 *
 * @author Jarman
 */
@Api(tags = "终端操作")
@Controller
@RequestMapping("/rco/terminal/soft")
@EnableCustomValidate(enable = false)
public class SoftTerminalOperateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftTerminalOperateController.class);

    private static final String TAR_GZ_SUFFIX = "tar.gz";

    private static final String SYMBOL_SPOT = ".";

    @Autowired
    private CbbTerminalLogAPI cbbTerminalLogAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;


    /**
     * 收集日志
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("收集日志")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，收集日志"})})
    @RequestMapping(value = "collectLog", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse collectLog(TerminalIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为null");

        cbbTerminalLogAPI.collectLog(request.getTerminalId());

        return DefaultWebResponse.Builder.success();
    }

    /**
     * 获取终端收集日志状态
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取终端收集日志状态")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，获取终端收集日志状态"})})
    @RequestMapping(value = "getCollectLog", method = RequestMethod.POST)
    public DefaultWebResponse getCollectLog(TerminalIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        CbbTerminalCollectLogStatusDTO response;
        TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(request.getTerminalId());
        try {
            response = cbbTerminalLogAPI.getCollectLog(request.getTerminalId());
        } catch (BusinessException e) {
            LOGGER.error("获取终端[{}]收集日志状态失败", terminalDTO.getTerminalName(), e);
            throw new BusinessException(TerminalBusinessKey.RCDC_SOFT_TERMINAL_COLLECT_LOG_FAIL_LOG, e, terminalDTO.getTerminalName(),
                    e.getI18nMessage());
        }

        if (CbbCollectLogStateEnums.DONE == response.getState()) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_SOFT_TERMINAL_COLLECT_LOG_SUCCESS_LOG, terminalDTO.getTerminalName());
        } else if (CbbCollectLogStateEnums.FAULT == response.getState()) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_SOFT_TERMINAL_COLLECT_LOG_RESULT_FAIL_LOG, terminalDTO.getTerminalName());
        }

        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 下载终端收集日志状态
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     * @throws IOException       io异常
     */
    @ApiOperation("下载终端收集日志")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，下载终端收集日志"})})
    @RequestMapping(value = "downloadLog", method = RequestMethod.GET)
    public DownloadWebResponse downloadLog(TerminalLogDownLoadWebRequest request) throws BusinessException, IOException {
        Assert.notNull(request, "request不能为null");

        CbbTerminalLogFileInfoDTO response = cbbTerminalLogAPI.getTerminalLogFileInfo(request.getLogName());

        // logFilePath=@String[/opt/ftp/terminal/log/20201209213115_172.20.113.184_58696cff3bdd_shine.tar.gz],
        // logFileName=@String[20201209213115_172.20.113.184_58696cff3bdd_shine.tar],
        // suffix=@String[gz]
        final File file = new File(response.getLogFilePath());
        return new DownloadWebResponse.Builder().setContentType("application/octet-stream").setName(response.getLogFileName(), response.getSuffix())
                .setFile(file).build();
    }

    private File getShineLog(CbbTerminalLogFileInfoDTO response) throws BusinessException {
        File shineLog = new File(response.getLogFilePath());
        if (shineLog.length() == 0) {
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_SHINE_ERROR_GET_SHINE_LOG_FAIL);
        }
        return shineLog;
    }

}
