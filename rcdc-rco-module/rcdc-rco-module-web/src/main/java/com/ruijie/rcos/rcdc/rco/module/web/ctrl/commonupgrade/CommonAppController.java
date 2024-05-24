package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade;

import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationPacketAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.request.ApplicationFileQueryRequest;
import com.ruijie.rcos.base.upgrade.module.def.api.request.ApplicationInstallerQueryRequest;
import com.ruijie.rcos.base.upgrade.module.def.api.request.ApplicationVersionQueryRequest;
import com.ruijie.rcos.base.upgrade.module.def.api.response.ApplicationPacketVersionQueryResponse;
import com.ruijie.rcos.base.upgrade.module.def.constant.AppUpgradeConstant;
import com.ruijie.rcos.base.upgrade.module.def.dto.ApplicationPacketFileDTO;
import com.ruijie.rcos.base.upgrade.module.def.dto.ApplicationPacketInstallerDTO;
import com.ruijie.rcos.base.upgrade.module.def.enums.DownloadType;
import com.ruijie.rcos.base.upgrade.module.def.enums.PacketProductType;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbSoftClientGlobalStrategyAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.softclient.CbbSoftClientGlobalStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.common.enums.CommonUpgradeTargetType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.request.GetAppRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.request.GetAppTorrentRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbOneAgentClientIdTransformSPI;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.response.OneAgentClientTransformDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.List;

/**
 * Description: 升级包管理ctrl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年11月21日
 *
 * @author chenl
 */
@Controller
@RequestMapping("/rco/app")
public class CommonAppController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonAppController.class);

    @Autowired
    private BaseApplicationPacketAPI baseApplicationPacketAPI;

    @Autowired
    private CbbSoftClientGlobalStrategyAPI softClientGlobalStrategyAPI;

    @Autowired
    private CbbOneAgentClientIdTransformSPI oneAgentClientIdTransformSPI;

    /**
     * 种子文件下载
     *
     * @param webRequest 请求参数
     * @return 应用详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("种子文件下载")
    @RequestMapping(value = "/torrent/download", method = RequestMethod.GET)
    @NoAuthUrl
    public DownloadWebResponse downloadTorrent(@RequestParam GetAppTorrentRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest cannot be null");

        ApplicationFileQueryRequest request = new ApplicationFileQueryRequest();
        request.setProductType(webRequest.getProductType());
        request.setOsType(webRequest.getOsType());
        request.setArchType(webRequest.getArchType());
        request.setVersion(webRequest.getVersion());
        request.setHash(webRequest.getHash());
        ApplicationPacketFileDTO fileDTO = baseApplicationPacketAPI.getFile(request);
        File file = new File(fileDTO.getTorrentFilePath());
        String fileName = file.getName();
        int lastIndex = fileName.lastIndexOf('.');
        String filename = fileName.substring(0, lastIndex);
        String suffix = fileName.substring(lastIndex + 1);
        return new DownloadWebResponse.Builder().setName(filename, suffix).setFile(file.getAbsoluteFile()).build();
    }


    /**
     * 安装包文件下载
     *
     * @param webRequest 请求参数
     * @return 应用详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("安装包文件下载")
    @RequestMapping(value = "/installer/download", method = RequestMethod.GET)
    @NoAuthUrl
    public DownloadWebResponse downloadInstaller(@RequestParam GetAppRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest cannot be null");

        ApplicationInstallerQueryRequest request = new ApplicationInstallerQueryRequest();
        request.setProductType(webRequest.getProductType());
        request.setArchType(webRequest.getArchType());
        request.setOsType(webRequest.getOsType());
        request.setVersion(webRequest.getVersion());
        request.setType(webRequest.getType());

        if (webRequest.getProductType() == PacketProductType.ONE_AGENT && StringUtils.hasText(webRequest.getId())) {
            OneAgentClientTransformDTO oneAgentClientTransformDTO = oneAgentClientIdTransformSPI.oneAgentClientIdTransform(webRequest.getId());
            if (oneAgentClientTransformDTO.getOneAgentClientType() == CommonUpgradeTargetType.IMAGE) {
                //如果是镜像默认下载最新版本
                request.setVersion(AppUpgradeConstant.LAST_VERSION);
            } else {
                //如果是桌面默认下载升级范围匹配版本
                request.setTargetId(oneAgentClientTransformDTO.getId());
                request.setTargetGroupId(oneAgentClientTransformDTO.getGroupId());
            }
        }

        ApplicationPacketInstallerDTO installer = baseApplicationPacketAPI.getInstaller(request);

        //下载安装包种子文件，只有oc/oa会通过下载器下载，直接返回原始安装包的种子文件
        if (webRequest.getDownloadType() == DownloadType.BT) {
            File downloadFile = new File(installer.getTorrentFilePath());
            return new DownloadWebResponse.Builder().setFile(downloadFile).build();
        }

        //FIXME: 临时规避，如果version=null说明是下载器来下载安装包，默认返回原始安装包，后面下载器会改成bt下载，只会走到上面的逻辑，这里要去掉
        if (StringUtils.isEmpty(webRequest.getVersion())) {
            File downloadFile = new File(installer.getFilePath());
            return new DownloadWebResponse.Builder().setFile(downloadFile).build();
        }

        //OA默认下载免配置安装包
        if (webRequest.getProductType() == PacketProductType.ONE_AGENT) {
            File downloadFile = new File(installer.getConfiguredFilePath());
            return new DownloadWebResponse.Builder().setFile(downloadFile).build();
        }

        //OC若开启免配置则返回免配置安装包，否则返回原始安装包
        CbbSoftClientGlobalStrategyDTO softClientGlobalStrategyDTO = softClientGlobalStrategyAPI.getGlobalStrategy();
        if (Boolean.TRUE.equals(softClientGlobalStrategyDTO.getOpenOneInstall())) {
            File downloadFile = new File(installer.getConfiguredFilePath());
            return new DownloadWebResponse.Builder().setFile(downloadFile).build();
        }

        File downloadFile = new File(installer.getFilePath());
        return new DownloadWebResponse.Builder().setFile(downloadFile).build();
    }

    /**
     * 安装包列表接口
     *
     * @return 应用详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("安装包列表接口")
    @RequestMapping(value = "/installer/list", method = RequestMethod.POST)
    @NoAuthUrl
    public DefaultWebResponse installerList() throws BusinessException {
        ApplicationVersionQueryRequest request = new ApplicationVersionQueryRequest();
        List<ApplicationPacketVersionQueryResponse> applicationPacketVersionResponseList = baseApplicationPacketAPI.listVersion(request);
        return DefaultWebResponse.Builder.success(applicationPacketVersionResponseList);
    }

}
