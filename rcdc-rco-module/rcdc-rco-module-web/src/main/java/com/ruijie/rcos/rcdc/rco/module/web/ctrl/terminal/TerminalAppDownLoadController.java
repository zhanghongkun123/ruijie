package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppTerminalAPI;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ServerModel;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;

/**
 * Description: 终端管理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月24日
 *
 * @author nt
 */
@Controller
@RequestMapping("/rco/terminal/app/")
@EnableCustomValidate(enable = false)
public class TerminalAppDownLoadController {

    @Autowired
    private AppTerminalAPI appTerminalAPI;

    /**
     * 获取终端信息
     *
     * @param request 请求参数
     * @return 文件下载
     * @throws BusinessException 业务异常
     */
    @ServerModel
    @NoAuthUrl
    @RequestMapping(value = "/windows/download")
    public DownloadWebResponse downloadWindowsApp(EmptyDownloadWebRequest request) throws BusinessException {

        return downloadApp(CbbTerminalTypeEnums.APP_WINDOWS);
    }

    /**
     * 获取终端信息
     *
     * @param request 请求参数
     * @return 文件下载
     * @throws BusinessException 业务异常
     */
    @ServerModel
    @NoAuthUrl
    @RequestMapping(value = "/uos/download")
    public DownloadWebResponse downloadUosApp(EmptyDownloadWebRequest request) throws BusinessException {
        return downloadApp(CbbTerminalTypeEnums.APP_UOS);
    }

    /**
     * 获取终端信息
     *
     * @param request 请求参数
     * @return 文件下载
     * @throws BusinessException 业务异常
     */
    @ServerModel
    @NoAuthUrl
    @RequestMapping(value = "/deepin/download")
    public DownloadWebResponse downloadDeepinApp(EmptyDownloadWebRequest request) throws BusinessException {
        return downloadApp(CbbTerminalTypeEnums.APP_DEEPIN);
    }

    /**
     * 获取终端信息
     *
     * @param request 请求参数
     * @return 文件下载
     * @throws BusinessException 业务异常
     */
    @ServerModel
    @NoAuthUrl
    @RequestMapping(value = "/kylin/download")
    public DownloadWebResponse downloadKylinApp(EmptyDownloadWebRequest request) throws BusinessException {
        return downloadApp(CbbTerminalTypeEnums.APP_KYLIN);
    }

    /**
     * 获取终端信息
     *
     * @param request 请求参数
     * @return 文件下载
     * @throws BusinessException 业务异常
     */
    @ServerModel
    @NoAuthUrl
    @RequestMapping(value = "/neokylin/download")
    public DownloadWebResponse downloadNeoKylinApp(EmptyDownloadWebRequest request) throws BusinessException {
        return downloadApp(CbbTerminalTypeEnums.APP_NEOKYLIN);
    }

    private DownloadWebResponse downloadApp(CbbTerminalTypeEnums cbbTerminalTypeEnums) throws BusinessException {
        String downLoadUrl = appTerminalAPI.getAppDownloadUrl(cbbTerminalTypeEnums);
        File file = new File(downLoadUrl);
        String fileNameWithSuffix = file.getName();
        String fileName = fileNameWithSuffix.substring(0, fileNameWithSuffix.lastIndexOf('.'));
        String suffix = fileNameWithSuffix.substring(fileNameWithSuffix.lastIndexOf('.') + 1);

        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setFile(file, false).setName(fileName, suffix).build();
    }

}
