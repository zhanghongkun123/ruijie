package com.ruijie.rcos.rcdc.rco.module.web.ctrl.servermodel;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGlobalStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.common.utils.SysInfoUtils;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.maintenance.module.def.annotate.NoBusinessMaintenanceUrl;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainEduLicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.response.ObtainCpuLicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.servermodel.response.HeartBeatWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.servermodel.response.ObtainEduLicenseInfoWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.servermodel.response.ObtainLicenseInfoWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.servermodel.response.ServerModelWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.sk.webmvc.api.annotation.NoMaintenanceUrl;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年9月4日
 *
 * @author wjp
 */
@Api(tags = "部署模式功能")
@Controller
@RequestMapping("/rco/serverModel")
public class ServerModelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerModelController.class);

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private CmsComponentAPI cmsComponentAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private LicenseAPI licenseAPI;

    @Autowired
    private RcaSupportAPI rcaSupportAPI;

    @Autowired
    private CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;

    private static final String HEART_BEAT_FLAG = "142857";

    /**
     * 获取服务器模式和CMS组件启用情况
     *
     * @param webRequest 请求参数
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取服务器模式和CMS组件启用情况")
    @RequestMapping(value = "getServerModel", method = RequestMethod.POST)
    @NoAuthUrl
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<ServerModelWebResponse> getServerModel(DefaultWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        try {
            ServerModelWebResponse serverModelWebResponse = new ServerModelWebResponse();
            serverModelWebResponse.setServerModel(serverModelAPI.getServerModel());
            serverModelWebResponse.setCmsComponent(cmsComponentAPI.getCmsComponent());
            serverModelWebResponse.setUwsComponent(uwsDockingAPI.getUwsComponentFlag());
            //获取服务器CPU 类型
            serverModelWebResponse.setServerClustercpuArch(cbbGlobalStrategyMgmtAPI.getComputerClusterCpuArch());
            serverModelWebResponse.setEnableVirtualApplication(rcaSupportAPI.getVirtualApplicationState());
            LOGGER.debug("获取服务器模式成功。部署模式 = {}, CMS安装情况 = {}", serverModelWebResponse.getServerModel()
                    , serverModelWebResponse.getCmsComponent());
            return CommonWebResponse.success(serverModelWebResponse);
        } catch (BusinessException e) {
            LOGGER.error("获取服务器模式和CMS组件启动情况失败", e.getI18nMessage());
            return CommonWebResponse.fail(ServerModelBusinessKey.RCO_GET_SERVER_MODEL_OR_CMS_CMSCOMPONENT_FAIL, new String[] {e.getI18nMessage()});
        }
    }

    /**
     * 获取服务器CPU证书到期时间
     *
     * @param webRequest 请求参数
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取服务器CPU证书到期时间")
    @RequestMapping(value = "obtainLicenseInfo", method = RequestMethod.POST)
    @NoAuthUrl
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<ObtainLicenseInfoWebResponse> obtainLicenseInfo(DefaultWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        try {
            ObtainCpuLicenseInfoResponse obtainCpuLicenseInfoResponse = licenseAPI.obtainLicenseTrialRemainder();
            ObtainLicenseInfoWebResponse obtainLicenseInfoWebResponse = new ObtainLicenseInfoWebResponse();
            obtainLicenseInfoWebResponse.setTrialRemainder(obtainCpuLicenseInfoResponse.getTrialRemainder());
            obtainLicenseInfoWebResponse.setHasAllExpire(obtainCpuLicenseInfoResponse.isHasAllExpire());
            obtainLicenseInfoWebResponse.setHasPartExpire(obtainCpuLicenseInfoResponse.isHasPartExpire());
            LOGGER.info("获取服务器CPU证书到期时间成功。到期时间 ={}，是否全部授权到期={}，是否部分授权到期={}", obtainLicenseInfoWebResponse.getTrialRemainder(),
                    obtainLicenseInfoWebResponse.isHasAllExpire(), obtainLicenseInfoWebResponse.isHasPartExpire());
            return CommonWebResponse.success(obtainLicenseInfoWebResponse);
        } catch (BusinessException e) {
            LOGGER.error("获取服务器CPU证书到期时间失败", e.getI18nMessage());
            return CommonWebResponse.fail(ServerModelBusinessKey.RCO_OBTAIN_LICENSE_INFO_FAIL, new String[] {e.getI18nMessage()});
        }
    }

    /**
     * 获取服务器教育版证书
     *
     * @param webRequest 请求参数
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取服务器教育版证书")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"教育版显示-获取服务器教育版证书"})})
    @RequestMapping(value = "obtainEduLicense", method = RequestMethod.POST)
    public CommonWebResponse<ObtainEduLicenseInfoWebResponse> obtainEduLicense(DefaultWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        try {
            ObtainEduLicenseInfoResponse obtainEduLicenseInfoResponse = licenseAPI.obtainEduLicense();
            ObtainEduLicenseInfoWebResponse obtainEduLicenseInfoWebResponse = new ObtainEduLicenseInfoWebResponse();
            // 设置CPU数量
            obtainEduLicenseInfoWebResponse.setCpuEduTotal(obtainEduLicenseInfoResponse.getCpuLicenseInfoDTO().getTotal());
            // VOI证书数量
            obtainEduLicenseInfoWebResponse.setVoiEduTotal(obtainEduLicenseInfoResponse.getVoiLicenseInfoDTO().getTotal());
            return CommonWebResponse.success(obtainEduLicenseInfoWebResponse);
        } catch (BusinessException e) {
            LOGGER.error("获取教育版CPU与VOI证书失败", e.getI18nMessage());
            return CommonWebResponse.fail(ServerModelBusinessKey.RCO_OBTAIN_LICENSE_INFO_FAIL, new String[] {e.getI18nMessage()});
        }
    }

    /**
     * 健康检测心跳报文
     *
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "健康检测心跳报文")
    @RequestMapping(value = "heartBeat", method = RequestMethod.POST)
    @NoAuthUrl
    @NoMaintenanceUrl
    public CommonWebResponse<HeartBeatWebResponse> heartBeat() throws BusinessException {
        HeartBeatWebResponse heartBeatWebResponse = new HeartBeatWebResponse();
        heartBeatWebResponse.setResult(HEART_BEAT_FLAG);
        return CommonWebResponse.success(heartBeatWebResponse);
    }
}
