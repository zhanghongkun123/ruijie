package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.sysmanage.module.def.api.BaseLicenseMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.request.license.BaseCreateLicenseRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImportLicenseDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.license.module.def.api.CbbLicenseCenterAPI;
import com.ruijie.rcos.rcdc.license.module.def.dto.CbbAuthInfoDTO;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseCategoryEnum;
import com.ruijie.rcos.rcdc.license.module.def.rest.request.TempLicCreateRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.AutoTrialLicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.LicenseFeatureIdEnums;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.NeedAuthMniServerEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.LicenseService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLicenseMgmtAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月10日
 * 
 * @author zouqi
 */
public class AutoTrialLicenseAPIImpl implements AutoTrialLicenseAPI {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoTrialLicenseAPIImpl.class);

    /** 自动生成试用授权时间30天 */
    private static final long TRIAL_DURATION = TimeUnit.DAYS.toSeconds(30);

    /** mini服务器环境自动生成试用授权时间30天 */
    private static final long TRIAL_DURATION_MINI = TimeUnit.DAYS.toSeconds(30);

    /** 自动生成的VDI并发临时授权的授权码信息，暂定该字符串，无特殊含义*/
    private static final String TEMP_VDI_LICENSE_CODE = "auto-crate-vdi-temp-license";

    /** 自动生成的IDV临时授权的授权码信息，暂定该字符串，无特殊含义*/
    private static final String TEMP_IDV_LICENSE_CODE = "auto-crate-idv-temp-license";

    /** 自动生成的VOI临时授权的授权码信息，暂定该字符串，无特殊含义*/
    private static final String TEMP_VOI_LICENSE_CODE = "auto-crate-tci-temp-license";


    /** 自动生成的VDI并发临时授权数量，目前默认设置为1000*/
    private static final Integer TEMP_VDI_LICENSE_NUMBER = 1000;

    /** globalParameter表的key */
    private static final String TRIAL_KEY = "auto_trial_license";

    /** rco_globalParameter表的key */
    private static final String IDV_TRIAL_KEY = "idv_auto_trial_license";

    /** rco_globalParameter表的key */
    private static final String VOI_TRIAL_KEY = "voi_auto_trial_license";

    /** globalParameter表的value */
    private static final String CAN_LICENSE_VALUE = "can_license";

    /** globalParameter表的value */
    private static final String NOT_LICENSE_VALUE = "not_license";


    private static final String SERVER_MODEL = "server_model";


    private static final int NO_AUTH_LIMIT = -1;

    /**
     * featureId 的前缀，后面加上CPU数量
     */
    private static final String FEATURE_ID_PREFIX = "RG-CCP-DCP-Lic-";

    /**
     * 产品类型编码
     */
    private static final String LICENSE_CODE = "RG-CCP-DCP-LIC-100000022100592";

    /**
     * 硬件序列号
     */
    private static final String DEV_ID = "无";

    /**
     * MINI授权全局表KEY
     */
    private static final String AUTH_PARAM_KEY = "need_auth_mini_server";

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private CbbTerminalLicenseMgmtAPI cbbTerminalLicenseMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Autowired
    private CbbDeskLicenseMgmtAPI cbbDeskLicenseMgmtAPI;

    @Autowired
    private BaseLicenseMgmtAPI baseLicenseMgmtAPI;

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private CbbLicenseCenterAPI cbbLicenseCenterAPI;

    @Override
    public synchronized void autoTrialLicense() throws BusinessException {

        String value = globalParameterAPI.findParameter(TRIAL_KEY);

        LOGGER.info("接收到自动授权请求，当前是否允许授权值为[{}]", value);
        if (Objects.equals(CAN_LICENSE_VALUE, value)) {
            autoTemporarilyLicense();
            // 更新global表，变为不可授权的状态
            globalParameterAPI.updateParameter(TRIAL_KEY, NOT_LICENSE_VALUE);
        }
    }

    @Override
    public synchronized void idvAutoTrialLicense() throws BusinessException {
        String value = globalParameterService.findParameter(IDV_TRIAL_KEY);
        LOGGER.info("接收到IDV临时自动授权请求，当前是否允许授权值为[{}]", value);
        if (Objects.equals(CAN_LICENSE_VALUE, value)) {
            //自动导入IDV临时授权
            idvAutoTemporarilyLicense();
            // 更新global表，变为不可授权的状态
            globalParameterService.updateParameter(IDV_TRIAL_KEY, NOT_LICENSE_VALUE);
        }
    }

    @Override
    public synchronized void voiAutoTrialLicense() throws BusinessException {
        String value = globalParameterService.findParameter(VOI_TRIAL_KEY);
        LOGGER.info("接收到VOI临时自动授权请求，当前是否允许授权值为[{}]", value);
        if (Objects.equals(CAN_LICENSE_VALUE, value)) {
            //自动导入VOI临时授权
            voiAutoTemporarilyLicense();
            // 更新global表，变为不可授权的状态
            globalParameterService.updateParameter(VOI_TRIAL_KEY, NOT_LICENSE_VALUE);
        }
    }

    @Override
    public synchronized void systemUpVoiAutoTrialLicense() throws BusinessException {
        //查询VOI 是否能进入自动临时授权
        String value = globalParameterService.findParameter(VOI_TRIAL_KEY);
        // 由历史版本系统升级到5.3R1P1时候 由于旧系统已经走过配置向导 不能再重新走 故只能在升级入口再做VOI临时授权
        LOGGER.info("接收到系统升级后VOI临时自动授权请求，当前是否允许授权值为[{}]", value);
        if (Objects.equals(CAN_LICENSE_VALUE, value)) {
            // 查询VOI 证书
            List<CbbAuthInfoDTO> authInfoDTOList = licenseService.getLicenseUsageSnapshot(CbbLicenseCategoryEnum.VOI.name());
            int totalNum = 0;
            for (CbbAuthInfoDTO authInfoDTO : authInfoDTOList) {
                if (authInfoDTO.getTotal() == -1) {
                    totalNum = -1;
                } else {
                    if (totalNum >= 0) {
                        totalNum += authInfoDTO.getTotal();
                    }
                }
            }
            LOGGER.info("通过CBB查询VOI证书授权证书CbbTerminalLicenseNumDTO，[{}]", JSON.toJSONString(authInfoDTOList));
            // 如果 数据库中 终端证书授权数量 数量等于-1 或者 0 说明未导入VOI证书，可以进行VOI临时授权
            if (totalNum == -1 || totalNum == 0) {
                // 自动导入VOI临时授权
                voiAutoTemporarilyLicense();
            }
            // 更新global表，变为不可授权的状态
            globalParameterService.updateParameter(VOI_TRIAL_KEY, NOT_LICENSE_VALUE);
        }
    }

    /**
     * 自动生成一个临时授权
     * 
     * @throws BusinessException
     */
    private void autoTemporarilyLicense() throws BusinessException {
        TempLicCreateRequest tempLicCreateRequest = new TempLicCreateRequest();
        tempLicCreateRequest.setFeatureId(LicenseFeatureIdEnums.CML_DESKTOP.getFeatureId());
        tempLicCreateRequest.setTrialDuration(TRIAL_DURATION);
        tempLicCreateRequest.setLicenseCode(TEMP_VDI_LICENSE_CODE);
        tempLicCreateRequest.setLicenseNum(TEMP_VDI_LICENSE_NUMBER);
        cbbLicenseCenterAPI.createTempLic(tempLicCreateRequest);
    }

    /**
     * 自动导入IDV 临时证书
     * @throws BusinessException
     */
    private void idvAutoTemporarilyLicense() throws BusinessException {
        TempLicCreateRequest tempLicCreateRequest = new TempLicCreateRequest();
        tempLicCreateRequest.setFeatureId(LicenseFeatureIdEnums.IDV_TERMINAL.getFeatureId());
        tempLicCreateRequest.setTrialDuration(TRIAL_DURATION);
        tempLicCreateRequest.setLicenseNum(TEMP_VDI_LICENSE_NUMBER);

        // 判断是否是mini服务器环境，mini:300天；超融合、纯IDV:30天
        String serverModelValue = globalParameterService.findParameter(SERVER_MODEL);
        LOGGER.info("当前服务器模式为：serverModel = [{}]", serverModelValue);
        if (ServerModelEnum.MINI_SERVER_MODEL.getName().equals(serverModelValue)) {
            tempLicCreateRequest.setTrialDuration(TRIAL_DURATION_MINI);
        }

        tempLicCreateRequest.setLicenseCode(TEMP_IDV_LICENSE_CODE);

        LOGGER.info("调用RCCP接口创建IDV终端临时授权：tempLicCreateRequest = [{}]", JSON.toJSONString(tempLicCreateRequest));
        cbbLicenseCenterAPI.createTempLic(tempLicCreateRequest);
    }

    /**
     * 自动导入 VOI 临时证书
     * @throws BusinessException
     */
    private void voiAutoTemporarilyLicense() throws BusinessException {
        LOGGER.info("自动导入VOI 非教育场景 LIC临时证书");
        voiAutoVOILICTemporarilyLicense();
        // 只需要导入一个VOI 临时证书即可
    }

    /**
     * 自动导入VOI 非教育场景 LIC临时证书
     * 
     * @throws BusinessException
     */
    private void voiAutoVOILICTemporarilyLicense() throws BusinessException {
        TempLicCreateRequest tempLicCreateRequest = new TempLicCreateRequest();
        // VOI 授权名称
        tempLicCreateRequest.setFeatureId(LicenseFeatureIdEnums.RG_CDC_VOI_LIC.getFeatureId());
        // 通用自动生成试用授权时间30天
        tempLicCreateRequest.setTrialDuration(TRIAL_DURATION);
        // 自动生成的VOI并发临时授权数量，目前默认设置为1000
        tempLicCreateRequest.setLicenseNum(TEMP_VDI_LICENSE_NUMBER);
        // 判断是否是mini服务器环境，mini:300天；超融合、纯VOI:30天
        String serverModelValue = globalParameterService.findParameter(SERVER_MODEL);
        LOGGER.info("进行VOI授权当前服务器模式为：serverModel = [{}]", serverModelValue);
        if (ServerModelEnum.MINI_SERVER_MODEL.getName().equals(serverModelValue)) {
            tempLicCreateRequest.setTrialDuration(TRIAL_DURATION_MINI);
        }
        // 设置VOI授权码
        tempLicCreateRequest.setLicenseCode(TEMP_VOI_LICENSE_CODE);
        LOGGER.info("调用RCCP接口创建VOI终端临时授权：tempLicCreateRequest = [{}]", JSON.toJSONString(tempLicCreateRequest));
        cbbLicenseCenterAPI.createTempLic(tempLicCreateRequest);
    }

    @Override
    public synchronized void generateMiniInternalLicense() throws BusinessException {
        LOGGER.info("开始执行生成MINI服务器内置证书方法");

        boolean isMiniModel = serverModelAPI.isMiniModel();
        // 若当前服务器类型不为MINI，则无需生成内置证书
        if (!isMiniModel) {
            LOGGER.info("当前服务器并非MINI服务器，无需生成内置证书");
            return;
        }

        if (!needAuth()) {
            LOGGER.info("MINI服务器已授权，无需重复生成内置证书");
            return;
        }

        DefaultPageResponse<PhysicalServerDTO> physicalServerDtoDefaultPageResponse =
                cbbPhysicalServerMgmtAPI.listPhysicalServer(new PageSearchRequest());
        for (PhysicalServerDTO physicalServerDTO : physicalServerDtoDefaultPageResponse.getItemArr()) {
            String productName = physicalServerDTO.getProductName();
            NeedAuthMniServerEnum needAuthMniServerEnum = NeedAuthMniServerEnum.getByProductName(productName);
            if (needAuthMniServerEnum == null) {
                LOGGER.info("服务器型号[{}]不在需要授权的型号中，无需生成内置证书", productName);
                break;
            }

            // 查询服务器CPU数量
            Integer physicsCpuNum = physicalServerDTO.getCpuInfo().getPhysicsCpuNum();
            // 生成内置证书
            BaseCreateLicenseRequest createLicenseRequest = buildCreateLicenseRequest(physicsCpuNum);
            LOGGER.info("MINI服务器[{}]的CPU数量为[{}]，构造的创建证书请求为：[{}]", physicalServerDTO.getHostName(), physicsCpuNum,
                    JSON.toJSONString(createLicenseRequest));
            String license = baseLicenseMgmtAPI.createLicense(createLicenseRequest);
            LOGGER.info("MINI服务器[{}]生成的内置证书为：[{}]", physicalServerDTO.getHostName(), license);

            CbbImportLicenseDTO cbbImportLicenseDTO = new CbbImportLicenseDTO();
            cbbImportLicenseDTO.setFileContent(license);
            cbbImportLicenseDTO.setFileName(LICENSE_CODE);
            LOGGER.info("MINI服务器[{}]，构造的导入证书请求为：[{}]", physicalServerDTO.getHostName(), JSON.toJSONString(cbbImportLicenseDTO));

            // 导入证书
            cbbDeskLicenseMgmtAPI.importLicense(cbbImportLicenseDTO);

            LOGGER.info("MINI服务器导入证书成功");
        }
        // 修改全局表
        globalParameterService.updateParameter(AUTH_PARAM_KEY, Boolean.FALSE.toString());
    }

    /**
     * 是否需要进行MINI服务器授权
     *
     * @return 是否需要授权
     */
    private Boolean needAuth() {
        String parameter = globalParameterService.findParameter(AUTH_PARAM_KEY);
        LOGGER.info("全局表中是否需要进行MINI授权的值为：[{}]", parameter);

        return Boolean.parseBoolean(parameter);
    }

    private BaseCreateLicenseRequest buildCreateLicenseRequest(Integer cpuNum) {
        BaseCreateLicenseRequest request = new BaseCreateLicenseRequest();
        request.setDevSn(DEV_ID);
        String featureId = FEATURE_ID_PREFIX + cpuNum;
        request.setFeatureId(featureId);
        request.setLicenseCode(LICENSE_CODE);

        return request;
    }
}
