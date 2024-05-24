package com.ruijie.rcos.rcdc.rco.module.impl.init;

import static com.ruijie.rcos.rcdc.rco.module.def.constants.UserConstants.THIRD_PARTY_AUTH_PLATFORM_CONFIG;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyAuthPlatformConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyHttpConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyHttpPairRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyResultParserConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyUserSyncConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.thiraparty.InputType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.thiraparty.MessageType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.thiraparty.ThirdPartyAuthOriginEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.thiraparty.ThirdPartyUserSyncCoverTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.thiraparty.ThirdPartyUserSyncTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoThirdPartyUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.ShineAssestMgmtConfigDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;

/**
 * 金融版本升级-阳光资管平台配置自动迁移至第三方配置
 *
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/19 17:07
 *
 * @author zjy
 */
@Service
public class ShineAssestMgmtConfigInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShineAssestMgmtConfigInitializer.class);

    /**
     * 金融版本第三方用户数据源信息全局表key值
     */
    private static final String OLD_THIRD_PARTY_SOURCE_KEY = "third_party_user_data_source";

    /**
     * 金融版本第三方用户数据源信息全局表备份key值
     */
    private static final String OLD_THIRD_PARTY_SOURCE_KEY_BAK = "third_party_user_data_source_bak";

    private static final String AUTH_URI = "/uap/outerAuth/apiAuthCenter";

    private static final Integer AUTH_TIMEOUT = 30;

    private static final String SYNC_USER_TIME = "03:00:00";

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private RcoThirdPartyUserMgmtAPI rcoThirdPartyUserMgmtAPI;

    @Override
    public void safeInit() {
        LOGGER.info("开始升级金融版本-阳光资管配置-->企金通用第三方配置");

        String oldThirdPartySource = "";
        try {
            oldThirdPartySource = globalParameterAPI.findParameter(OLD_THIRD_PARTY_SOURCE_KEY);
        } catch (Exception e) {
            LOGGER.error("读取到旧版阳光资管配置异常，结束升级", e);
            return;
        }

        if (StringUtils.isBlank(oldThirdPartySource)) {
            LOGGER.info("阳光资管配置为空，结束升级");
            return;
        }


        ShineAssestMgmtConfigDTO oldConfig = JSONObject.parseObject(oldThirdPartySource, ShineAssestMgmtConfigDTO.class);
        if (oldConfig.getEnable() == null) {
            LOGGER.info("阳光资管配置中开关为空，结束升级");
            return;
        }

        if (StringUtils.isBlank(oldConfig.getIp())) {
            LOGGER.info("阳光资管配置中ip地址为空，结束升级");
            return;
        }

        if (oldConfig.getPort() == null) {
            LOGGER.info("阳光资管配置中port为空，结束升级");
            return;
        }

        if (StringUtils.isBlank(oldConfig.getAppId())) {
            LOGGER.info("阳光资管配置中AppId为空，结束升级");
            return;
        }

        if (StringUtils.isBlank(oldConfig.getAppSecret())) {
            LOGGER.info("阳光资管配置中AppSecret为空，结束升级");
            return;
        }

        BaseThirdPartyAuthPlatformConfigDTO configDTO = new BaseThirdPartyAuthPlatformConfigDTO();
        try {
            oldConfigConvert(oldConfig, configDTO);

            LOGGER.info("阳光资管配置升级完成");
        } catch (Exception ex) {
            LOGGER.error("阳光资管配置升级失败，失败原因：", ex);
            return;
        }

        try {
            LOGGER.info("开始创建阳光资管用户同步定时任务");
            rcoThirdPartyUserMgmtAPI.createOrUpdateSyncSchedule(configDTO);
        } catch (Exception e) {
            LOGGER.error("创建阳光资管用户同步定时任务发生异常，ex：", e);
        }

    }

    private void oldConfigConvert(ShineAssestMgmtConfigDTO oldConfig, BaseThirdPartyAuthPlatformConfigDTO configDTO) {
        configDTO.setAuthOrigin(ThirdPartyAuthOriginEnum.SHINE_ASSET_MGMT);
        configDTO.setThirdPartyEnable(oldConfig.getEnable());

        // http 配置
        BaseThirdPartyHttpConfigDTO httpConfigDTO = new BaseThirdPartyHttpConfigDTO();
        String url = "http://" + oldConfig.getIp() + ":" + oldConfig.getPort() + AUTH_URI;
        httpConfigDTO.setUrl(url);
        List<BaseThirdPartyHttpPairRequest> headList = new ArrayList<>();
        headList.add(new BaseThirdPartyHttpPairRequest("Content-Encoding", "UTF-8", InputType.TEXT));
        headList.add(new BaseThirdPartyHttpPairRequest("Content-Type", "application/json;charset=utf-8", InputType.TEXT));
        httpConfigDTO.setHttpHeaderList(headList);
        httpConfigDTO.setHttpMethod(HttpMethod.POST);
        httpConfigDTO.setTimeout(AUTH_TIMEOUT);
        JSONObject postData = new JSONObject();
        postData.put("appId", oldConfig.getAppId());
        postData.put("appSecret", oldConfig.getAppSecret());
        postData.put("userCode", "$$USERNAME$$");
        postData.put("password", "$$PASSWORD$$");
        postData.put("vcType", "");
        httpConfigDTO.setHttpBody(postData.toJSONString());
        configDTO.setHttpConfig(httpConfigDTO);

        // 认证结果解析器
        BaseThirdPartyResultParserConfigDTO resultParserConfigDTO = new BaseThirdPartyResultParserConfigDTO();
        resultParserConfigDTO.setParserEnable(Boolean.TRUE);
        resultParserConfigDTO.setMessageType(MessageType.JSON);
        resultParserConfigDTO.setSuccessKey("code");
        resultParserConfigDTO.setSuccessValue("000000");
        configDTO.setResultParserConfig(resultParserConfigDTO);

        // 定时同步配置
        BaseThirdPartyUserSyncConfigDTO userSyncConfigDTO = new BaseThirdPartyUserSyncConfigDTO();
        userSyncConfigDTO.setUserSyncCoverType(ThirdPartyUserSyncCoverTypeEnum.THIRD);
        userSyncConfigDTO.setUserSyncEnable(Boolean.TRUE);
        userSyncConfigDTO.setUserSyncTimeout(AUTH_TIMEOUT);
        userSyncConfigDTO.setUserSyncType(ThirdPartyUserSyncTypeEnum.TIMER);
        userSyncConfigDTO.setUserSyncTime(SYNC_USER_TIME);
        configDTO.setUserSyncConfig(userSyncConfigDTO);

        globalParameterAPI.updateParameter(THIRD_PARTY_AUTH_PLATFORM_CONFIG, JSON.toJSONString(configDTO));
        globalParameterAPI.updateParameter(OLD_THIRD_PARTY_SOURCE_KEY, "");
        globalParameterAPI.updateParameter(OLD_THIRD_PARTY_SOURCE_KEY_BAK, JSON.toJSONString(oldConfig));
    }
}
