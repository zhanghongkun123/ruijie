package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseMaintenanceModeNotifySPI;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.IacThirdPartyCertificationConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacThirdPartyCertificationConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.thirdparty.ServerConfig;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.RadiusAuthenticatorEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAgreementDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbEstConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbAgreementTemplateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbEstDisplayModeEnum;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementConfigRequestDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.rcdc.rca.module.def.util.CommonStrategyHelper;
import com.ruijie.rcos.rcdc.rco.module.def.api.EstConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.OldThirdPartyCertificationConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月22日
 *
 * @author zhanghongkun
 */
@DispatcherImplemetion("deskStrategyMaintenanceModeNotifySPIImpl")
public class DeskStrategyMaintenanceModeNotifySPIImpl implements BaseMaintenanceModeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStrategyMaintenanceModeNotifySPIImpl.class);

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private IacThirdPartyCertificationAPI thirdPartyCertificationAPI;

    @Autowired
    private EstConfigAPI estConfigAPI;

    public static final String DEFAULT_ENABLE_AGREEMENT_AGENCY = "true";


    @Override
    public Boolean beforeEnteringMaintenance(String s, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterEnteringMaintenance(String s, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterUnderMaintenance(String s, BaseUpgradeDTO baseUpgradeDTO) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterMaintenanceEnd(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        Assert.hasText(dispatchKey, "dispatchKey must not be null or empty");
        Assert.notNull(baseUpgradeDTO, "baseUpgradeDTO must not be null");
        if (baseUpgradeDTO.getType() == BaseUpgradeDTO.UpgradeType.ONLINE) {
            LOGGER.info("当前为热补丁升级，无需执行");
            return Boolean.TRUE;
        }

        LOGGER.info("进行版本从5.4升级到企金1.1后的处理，5.4已经开启Radius的配置进行默认值赋值");
        FindParameterRequest findParameterRequest = new FindParameterRequest(Constants.THIRD_PARTY_AUTH_CODE_CONFIG);
        FindParameterResponse findParameterResponse = rcoGlobalParameterAPI.findParameter(findParameterRequest);
        IacThirdPartyCertificationConfigResponse configDTO = thirdPartyCertificationAPI.getThirdPartyCertificationConfig(SubSystem.CDC);
        OldThirdPartyCertificationConfigDTO oldConfig = new OldThirdPartyCertificationConfigDTO();
        if (StringUtils.isNotBlank(findParameterResponse.getValue())) {
            oldConfig = JSON.parseObject(findParameterResponse.getValue(), OldThirdPartyCertificationConfigDTO.class);
        }
        // 升级前Radius开着，且服务器名称为空，证明是从低版本升级上来的
        if (configDTO.getEnable() && configDTO.getServerName() == null) {
            configDTO.setServerName(Constants.DEFAULT_RADIUS_SERVER_NAME);
            configDTO.setAuthenticator(RadiusAuthenticatorEnum.PAP);
            configDTO.setTimeout(Constants.DEFAULT_RADIUS_SERVER_TIMEOUT);
            configDTO.setRetries(Constants.DEFAULT_RADIUS_SERVER_RETRIES);
            ServerConfig serverConfig = new ServerConfig(oldConfig.getIp(), oldConfig.getPort());
            configDTO.setServerList(Collections.singletonList(serverConfig));
            configDTO.setSecret(AesUtil.encrypt(oldConfig.getSecret(), RedLineUtil.getRealAdminRedLine()));
        }
        IacThirdPartyCertificationConfigRequest configRequest = new IacThirdPartyCertificationConfigRequest();
        BeanUtils.copyProperties(configDTO, configRequest);
        configRequest.setServerList(configDTO.getServerList());
        thirdPartyCertificationAPI.updateThirdPartyCertificationConfig(configRequest, SubSystem.CDC);

        LOGGER.info("进行旧版本升级到企金2.0后的处理，将全局策略中的EST协议配置转换到云桌面策略中");
        List<CbbDeskStrategyDTO> cbbDeskStrategyDTOList = cbbVDIDeskStrategyMgmtAPI.listDeskStrategyVDI();
        if (CollectionUtils.isNotEmpty(cbbDeskStrategyDTOList)) {
            CbbEstConfigDTO lanConfig = estConfigAPI.getEstLanConfig();
            CbbEstConfigDTO wanConfig = estConfigAPI.getEstWanConfig();

            for (CbbDeskStrategyDTO deskStrategyDTO : cbbDeskStrategyDTOList) {
                CbbAgreementDTO agreementInfo = deskStrategyDTO.getAgreementInfo();
                if (CbbEstProtocolType.EST == deskStrategyDTO.getEstProtocolType() && Objects.isNull(agreementInfo)) {
                    AgreementDTO agreementDTO = new AgreementDTO();
                    agreementDTO.setLanEstConfig(buildAgreementConfigRequest(lanConfig, true));
                    agreementDTO.setWanEstConfig(buildAgreementConfigRequest(wanConfig, false));
                    agreementInfo = CommonStrategyHelper.convertAgreementConfig(CbbEstProtocolType.EST, agreementDTO);
                    cbbVDIDeskStrategyMgmtAPI.updateDeskStrategyAgreement(deskStrategyDTO.getId(), agreementInfo);
                }
            }
        }

        return Boolean.TRUE;
    }

    private AgreementConfigRequestDTO buildAgreementConfigRequest(CbbEstConfigDTO cbbEstConfigDTO, boolean isLan) {
        AgreementConfigRequestDTO configRequest = new AgreementConfigRequestDTO();
        BeanUtils.copyProperties(cbbEstConfigDTO, configRequest);
        if (cbbEstConfigDTO.getWebAdvanceSettingInfo() != null) {
            configRequest.setWebAdvanceSettingInfo(JSON.toJSONString(cbbEstConfigDTO.getWebAdvanceSettingInfo()));
        }

        // 设为自定义模版,TemplateId默认流畅优先
        configRequest.setEnableCustomTemplate(true);
        if (CbbEstDisplayModeEnum.CLEAR_FIRST == cbbEstConfigDTO.getName()) {
            configRequest.setTemplateId(isLan ? CbbAgreementTemplateEnum.LAN_CLEAR_FIRST.getId() :
                    CbbAgreementTemplateEnum.WAN_CLEAR_FIRST.getId());
        } else {
            configRequest.setTemplateId(isLan ? CbbAgreementTemplateEnum.LAN_FLUENCY_FIRST.getId() :
                    CbbAgreementTemplateEnum.WAN_FLUENCY_FIRST.getId());
        }
        return configRequest;
    }
}
