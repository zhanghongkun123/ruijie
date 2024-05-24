package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGtLogCollectStateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.GtLogCollectState;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.gtlog.DownloadGtLogWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;

/**
 * Description: GT日志收集controller
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/22 14:15
 *
 * @author zhangyichi
 */
@Controller
@RequestMapping("/rco/clouddesktop/GTlog")
public class GuestToolLogCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolLogCtrl.class);

    @Autowired
    private CbbGuestToolLogAPI cbbGuestToolLogAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    /**
     * 请求收集GT日志
     * @param request 云桌面ID
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("请求收集GT日志")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping("/collect")
    @EnableAuthority
    public DefaultWebResponse collect(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        try {
            cbbGuestToolLogAPI.collectLog(request.getId());
        } catch (BusinessException e) {
            CloudDesktopDetailDTO detail = userDesktopMgmtAPI.getDesktopDetailById(request.getId());
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_COLLECT_LOG_ERROR, detail.getDesktopName(), e.getI18nMessage());
            throw e;
        }

        return DefaultWebResponse.Builder.success();
    }

    /**
     * 获取日志收集状态
     * @param request 云桌面ID
     * @return 日志收集状态响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/getState")
    public DefaultWebResponse getState(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        CloudDesktopDetailDTO detail = userDesktopMgmtAPI.getDesktopDetailById(request.getId());
        CbbGtLogCollectStateDTO response;
        try {
            response = cbbGuestToolLogAPI.getLogCollectState(request.getId());
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_COLLECT_LOG_ERROR, detail.getDesktopName(), e.getI18nMessage());
            throw e;
        }

        if (GtLogCollectState.DONE == response.getState()) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_COLLECT_LOG_SUCCESS, detail.getDesktopName());
        }
        if (GtLogCollectState.FAULT == response.getState()) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_COLLECT_LOG_ERROR, detail.getDesktopName(), response.getMessage());
        }
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 下载日志
     * @param request 日志下载请求
     * @return 下载响应
     * @throws BusinessException 业务异常
     * @throws IOException IO异常
     */
    @RequestMapping("/downloadLog")
    public DownloadWebResponse download(DownloadGtLogWebRequest request) throws BusinessException, IOException {
        Assert.notNull(request, "request cannot be null!");

        String logFilePath =
                cbbGuestToolLogAPI.getLogFilePath(request.getLogFileName());
        String logFileName = request.getLogFileName();
        int lastIndex = logFileName.lastIndexOf('.');
        String logFileNameWithoutSuffix = buildLogFileName(logFileName.substring(0, lastIndex));
        String suffix = logFileName.substring(lastIndex + 1);
        File file = new File(logFilePath);

        return new DownloadWebResponse.Builder()
                .setContentType("application/octet-stream")
                .setName(logFileNameWithoutSuffix, suffix)
                .setFile(file)
                .build();
    }

    private String buildLogFileName(String logFileName) {
        return new StringBuilder(logFileName).insert(logFileName.lastIndexOf('_'), "_deskIp").toString();
    }
}
