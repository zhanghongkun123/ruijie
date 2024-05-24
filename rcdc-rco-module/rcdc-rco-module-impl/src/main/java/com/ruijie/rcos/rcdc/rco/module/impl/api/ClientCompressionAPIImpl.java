package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationPacketAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.request.ApplicationInstallerConfigRequest;
import com.ruijie.rcos.base.upgrade.module.def.enums.PacketProductType;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbSoftClientGlobalStrategyAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.softclient.CbbSoftClientGlobalStrategyDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientCompressionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AppClientCompressionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.TerminalUpdateListCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.AppUpdateListDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.ChangeVipConfigOneClickDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/16
 *
 * @author TD
 */
public class ClientCompressionAPIImpl implements ClientCompressionAPI {

    /**
     * 一键安装KEY
     */
    private static final String APP_ONE_CLICK_INSTALL = "app_one_click_install";

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCompressionAPIImpl.class);

    @Autowired
    private CloudPlatformMgmtAPI cloudPlatformMgmtAPI;

    @Autowired
    private RcoGlobalParameterAPI globalParameterAPI;

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private BaseApplicationPacketAPI applicationPacketAPI;

    @Autowired
    private CbbSoftClientGlobalStrategyAPI softClientGlobalStrategyAPI;


    @Override
    public void createConfiguredInstaller(PacketProductType productType) throws BusinessException {
        Assert.notNull(productType, "productType is null");
        ApplicationInstallerConfigRequest request = new ApplicationInstallerConfigRequest();
        if (productType == PacketProductType.ONE_CLIENT) {
            //oc类型升级包读取web配置的免配置信息
            CbbSoftClientGlobalStrategyDTO globalStrategyDTO = preHandleAppClientConfig();
            if (BooleanUtils.isFalse(globalStrategyDTO.getOpenOneInstall())) {
                LOGGER.info("one click install function not open[{}]");
                return;
            }
            request.setOpenOneInstall(globalStrategyDTO.getOpenOneInstall());
            request.setProductType(PacketProductType.ONE_CLIENT);
            request.setServerIp(globalStrategyDTO.getServerIp());
            request.setProxyIp(globalStrategyDTO.getProxyServerIp());
            request.setProxyPort(globalStrategyDTO.getProxyPort());
        } else {
            //其他类型升级包默认写入vip配置
            request.setOpenOneInstall(true);
            request.setProductType(productType);
            String currentVip = cloudPlatformMgmtAPI.getClusterVirtualIp(new DefaultRequest()).getDto().getClusterVirtualIpIp();
            request.setServerIp(currentVip);
        }

        applicationPacketAPI.configInstaller(request);
    }


    private synchronized CbbSoftClientGlobalStrategyDTO preHandleAppClientConfig() throws BusinessException {
        CbbSoftClientGlobalStrategyDTO globalStrategy = getAppGlobalStrategyConfig();
        ChangeVipConfigOneClickDTO changeVipConfigOneClickDTO = getChangeVipConfigOneClickDTO();

        // 如果没有记录旧的VIP，证明没有修改过服务器VIP，则不需要处理
        if (StringUtils.isBlank(changeVipConfigOneClickDTO.getOldVip())) {
            LOGGER.info("服务器旧VIP信息为空，无需处理一键安装VIP");
            return globalStrategy;
        }

        String oldVip = changeVipConfigOneClickDTO.getOldVip();
        String currentVip = cloudPlatformMgmtAPI.getClusterVirtualIp(new DefaultRequest()).getDto().getClusterVirtualIpIp();
        String oneClickVip = globalStrategy.getServerIp();
        // 如果配置文件中和之前的VIP一致，则需要修改为新的VIP
        if (StringUtils.equals(oneClickVip, oldVip)) {
            LOGGER.info("服务器旧VIP为[{}]，一键安装配置中VIP为[{}]，一键安装VIP应该为当前VIP[{}]", oldVip, oneClickVip, currentVip);
            // 修改数据库配置一键安装VIP、旧VIP为当前VIP
            globalStrategy.setServerIp(currentVip);
            softClientGlobalStrategyAPI.updateGlobalStrategy(globalStrategy);
            changeVipConfigOneClickDTO.setOldVip(currentVip);
            globalParameterService.updateParameter(Constants.CHANGE_VIP_CONFIG_ONE_CLICK_KEY, JSON.toJSONString(changeVipConfigOneClickDTO));
            if (!StringUtils.equals(oldVip, currentVip)) {
                LOGGER.info("服务器VIP从[{}]变化为[{}]，记录审计日志", oldVip, currentVip);
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_AUTO_CONFIG_ONE_CLICK_INSTALL_SUCCESS_LOG, currentVip);
            }
        } else {
            // 服务器旧VIP和一键安装VIP不一致，证明用户可能手动修改过一键安装VIP，继续使用配置文件的VIP
            LOGGER.info("服务器旧VIP为[{}]，一键安装配置中VIP为[{}]，两者不一致，无需修改一键安装VIP", oldVip, oneClickVip);
            // 如果IP有变化，需要告警，没变化则无需任何操作
            if (!StringUtils.equals(oldVip, currentVip)) {
                LOGGER.info("服务器VIP从[{}]变化为[{}]，需要记录告警日志", oldVip, currentVip);
                saveAlarm();
                // 修改旧VIP为当前VIP，防止重复告警
                changeVipConfigOneClickDTO.setOldVip(currentVip);
                globalParameterService.updateParameter(Constants.CHANGE_VIP_CONFIG_ONE_CLICK_KEY, JSON.toJSONString(changeVipConfigOneClickDTO));
            }
        }
        return globalStrategy;
    }

    private void saveAlarm() {
        SaveAlarmRequest request = new SaveAlarmRequest();
        request.setAlarmLevel(AlarmLevel.TIPS);
        request.setAlarmCode(Constants.AUTO_CONFIG_ONE_CLICK_INSTALL_CODE);
        request.setAlarmType(Constants.AUTO_CONFIG_ONE_CLICK_INSTALL_TYPE);
        request.setAlarmName(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_AUTO_CONFIG_ONE_CLICK_INSTALL_NAME));
        request.setAlarmContent(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_AUTO_CONFIG_ONE_CLICK_INSTALL_TIP));
        request.setAlarmTime(new Date());
        request.setEnableSendMail(false);
        baseAlarmAPI.saveAlarm(request);
    }

    private ChangeVipConfigOneClickDTO getChangeVipConfigOneClickDTO() {
        String paramValue = globalParameterService.findParameter(Constants.CHANGE_VIP_CONFIG_ONE_CLICK_KEY);
        if (StringUtils.isBlank(paramValue)) {
            return new ChangeVipConfigOneClickDTO();
        }
        return JSON.parseObject(paramValue, ChangeVipConfigOneClickDTO.class);
    }

    private CbbSoftClientGlobalStrategyDTO getAppGlobalStrategyConfig() throws BusinessException {
        CbbSoftClientGlobalStrategyDTO globalStrategy = softClientGlobalStrategyAPI.getGlobalStrategy();
        // 获取云服务器IP为空，则把集群VIP写入
        if (StringUtils.isEmpty(globalStrategy.getServerIp())) {
            globalStrategy.setServerIp(cloudPlatformMgmtAPI.getClusterVirtualIp(new DefaultRequest()).getDto().getClusterVirtualIpIp());
        }
        return globalStrategy;
    }

    @Override
    public String getCompletePackageName(CbbTerminalTypeEnums terminalType) throws BusinessException {
        Assert.notNull(terminalType, "CbbTerminalTypeEnums is not null");
        AppUpdateListDTO appUpdateList = TerminalUpdateListCacheManager.get(terminalType);
        // 获取updatelist中完整组件的信息，从中获取全量包文件路径
        if (!TerminalUpdateListCacheManager.isCacheReady(terminalType)) {
            LOGGER.error("[{}]软终端updateList缓存未就绪", terminalType.name());
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_APP_UPDATELIST_CACHE_NOT_READY,terminalType.getOsType());
        }

        if (appUpdateList == null || StringUtils.isEmpty(appUpdateList.getCompletePackageName())) {
            LOGGER.error("[{}]软终端updateList信息异常", terminalType.name());
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_COMPONENT_UPDATELIST_CACHE_INCORRECT,terminalType.getOsType());
        }

        return appUpdateList.getCompletePackageName();
    }

    @Override
    public AppClientCompressionDTO getAppClientCompressionConfig() throws BusinessException {
        FindParameterResponse parameter = globalParameterAPI.findParameter(new FindParameterRequest(APP_ONE_CLICK_INSTALL));
        AppClientCompressionDTO appClientCompressionDTO = JSON.parseObject(parameter.getValue(), AppClientCompressionDTO.class);
        // 获取云服务器IP为空，则把集群VIP写入
        if (StringUtils.isEmpty(appClientCompressionDTO.getServerIp())) {
            appClientCompressionDTO.setServerIp(cloudPlatformMgmtAPI.getClusterVirtualIp(new DefaultRequest()).getDto().getClusterVirtualIpIp());
            globalParameterAPI.updateParameter(new UpdateParameterRequest(APP_ONE_CLICK_INSTALL, JSON.toJSONString(appClientCompressionDTO)));
        }
        return appClientCompressionDTO;
    }
}
