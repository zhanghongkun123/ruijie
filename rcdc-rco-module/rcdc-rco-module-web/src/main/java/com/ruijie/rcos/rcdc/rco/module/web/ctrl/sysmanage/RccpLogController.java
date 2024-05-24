package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccpLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.rccplog.dto.RccpLogCollectStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.rccplog.enums.RccpLogCollectState;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.rccplog.CreateRccpLogWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.rccplog.DownloadRccpLogWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/14 16:23
 *
 * @author ketb
 */
@Controller
@RequestMapping("rco/maintenance/rccpLog")
public class RccpLogController {

    private static final String RCCP_LOG_PATH = "/external_share/log/";

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RccpLogAPI rccpLogAPI;

    /**
     * 创建调试日志压缩文件接口
     *
     * @param webRequest web请求
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/collectLog")
    public DefaultWebResponse createDebugLog(CreateRccpLogWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "请求参数不能为空");

        rccpLogAPI.collectRccpLog();
        return DefaultWebResponse.Builder.success();
    }

    /**
     * 查询日志收集状态
     * @return 状态
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/getState")
    public DefaultWebResponse getLogInfo() throws BusinessException {
        RccpLogCollectStateDTO response = rccpLogAPI.getCollectRccpLogState();

        if (RccpLogCollectState.DONE == response.getState()) {
            auditLogAPI.recordLog(RccpLogBussinessKey.RCDC_COLLECT_RCCPLOG_SUCCESS, new String[] {});
        }
        if (RccpLogCollectState.FAULT == response.getState()) {
            auditLogAPI.recordLog(RccpLogBussinessKey.RCDC_COLLECT_RCCPLOG_ERROR, new String[] {});
        }

        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 下载日志
     * @param downloadWebRequest 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/downloadLog")
    public DownloadWebResponse downloadRccpLog(DownloadRccpLogWebRequest downloadWebRequest) throws BusinessException {
        Assert.notNull(downloadWebRequest, "downloadWebRequest not be null");

        String logFileName = downloadWebRequest.getLogFileName();
        int lastIndex = logFileName.lastIndexOf('.');
        String logFileNameWithoutSuffix = logFileName.substring(0, lastIndex);
        String suffix = logFileName.substring(lastIndex + 1);
        String path = RCCP_LOG_PATH + downloadWebRequest.getLogFileName();
        File logFile = new File(path);
        if (!logFile.exists()) {
            throw new BusinessException(RccpLogBussinessKey.RCDC_COLLECT_RCCPLOG_FAIL_LOGFILE_NOT_EXIST);
        }

        return new DownloadWebResponse.Builder()
                .setContentType("application/octet-stream")
                .setName(logFileNameWithoutSuffix, suffix)
                .setFile(logFile)
                .build();
    }
}
