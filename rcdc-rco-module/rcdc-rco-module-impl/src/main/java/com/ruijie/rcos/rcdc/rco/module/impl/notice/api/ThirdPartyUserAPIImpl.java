package com.ruijie.rcos.rcdc.rco.module.impl.notice.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyUserAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyAuthPlatformConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyHttpConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyUserSyncConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.request.BaseThirdPartyGetUserRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.request.BaseThirdPartyWrapAuthRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.response.BaseThirdPartyWrapAuthResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.util.ThirdPartyConverterUtils;
import com.ruijie.rcos.rcdc.rco.module.common.utils.HttpsClient;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.ThirdUserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.ThirdPartyUserAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineAssertMgmtUserResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.thirdparty.utils.ThirdPartyShineAssertMgmtSignUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.thirdparty.utils.ThirdPartyShineAssestMgmtDesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月31日
 *
 * @author jarman
 */
public class ThirdPartyUserAPIImpl implements ThirdPartyUserAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyUserAPIImpl.class);

    private static final Integer SUNSHINE_ASSET_MANAGEMENT_RANDOM_BOUND = 99999;

    private static final String GET_USER_URL = "/uap/outerAuth/newApiOrgStructure";

    private static final String GET_USER_URL_MOCK = "/rcdc/rco/user/thirdPartyMock/getAllUser";

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private IacThirdPartyUserAPI cbbThirdPartyUserAPI;



    @Override
    public BaseThirdPartyWrapAuthResponse wrapAuthRequest(BaseThirdPartyWrapAuthRequest wrapAuthRequest) throws BusinessException {
        Assert.notNull(wrapAuthRequest, "wrapAuthRequest cannot be null");

        Map<String, Object> headerMap = wrapAuthRequest.getHeaderMap();
        JSONObject httpBodyJson = null;
        try {
            httpBodyJson = JSONObject.parseObject(wrapAuthRequest.getHttpBody());
        } catch (Exception ex) {
            LOGGER.error("请求体json解析异常");
            return new BaseThirdPartyWrapAuthResponse(headerMap, wrapAuthRequest.getHttpBody());
        }

        try {
            httpBodyJson.put("userCode", ThirdPartyShineAssestMgmtDesUtil.encrypt(httpBodyJson.getString("userCode")));
            httpBodyJson.put("password", ThirdPartyShineAssestMgmtDesUtil.encrypt(httpBodyJson.getString("password")));
            httpBodyJson.put("clientIp", globalParameterAPI.findParameter(Constants.VIP_PARAM_KEY));
            httpBodyJson.put("sysUnicode", String.valueOf(new Random().nextInt(SUNSHINE_ASSET_MANAGEMENT_RANDOM_BOUND)));
            httpBodyJson.put("sign", ThirdPartyShineAssertMgmtSignUtil.getSign(httpBodyJson));
        } catch (Exception e) {
            LOGGER.error("阳光资管参数处理过程中出现异常, 请求参数：[{}] ex：", httpBodyJson.toJSONString(), e);
            throw new BusinessException(BusinessKey.RCDC_RCO_THIRD_PARTY_SHINE_ASSERT_ENCRYPT_ERROR, e);
        }
        return new BaseThirdPartyWrapAuthResponse(headerMap, httpBodyJson.toJSONString());
    }

    @Override
    public Map<String, List<BaseThirdPartyUserDTO>> getUserList(BaseThirdPartyGetUserRequest getUserRequest) throws BusinessException {
        Assert.notNull(getUserRequest, "getUserRequest not be null");

        BaseThirdPartyAuthPlatformConfigDTO thirdPartyConfig = cbbThirdPartyUserAPI.getThirdPartyConfig();
        if (thirdPartyConfig == null || !thirdPartyConfig.getThirdPartyEnable()) {
            return Maps.newHashMap();
        }

        BaseThirdPartyHttpConfigDTO httpConfig = thirdPartyConfig.getHttpConfig();
        BaseThirdPartyUserSyncConfigDTO userSyncConfig = thirdPartyConfig.getUserSyncConfig();

        URL url = null;
        try {
            url = new URL(httpConfig.getUrl());
        } catch (MalformedURLException e) {
            LOGGER.error("请求地址存在异常，url 为 [{}]", httpConfig.getUrl(), e);
            throw new BusinessException(ThirdUserBusinessKey.RCDC_USER_THIRD_PARTY_GET_USER_URL_INVALID, e);
        }

        // 获取httpclient
        HttpsClient httpClient = null;
        if (url.getProtocol().equals("http")) {
            httpClient =
                    new HttpsClient(userSyncConfig.getUserSyncTimeout(), userSyncConfig.getUserSyncTimeout(), userSyncConfig.getUserSyncTimeout());
        } else {
            try {
                httpClient = new HttpsClient(null, userSyncConfig.getUserSyncTimeout(), userSyncConfig.getUserSyncTimeout(),
                        userSyncConfig.getUserSyncTimeout());
            } catch (Exception e) {
                LOGGER.error("获取httpClient发生异常，ex:", e);
                throw new BusinessException(ThirdUserBusinessKey.RCDC_USER_THIRD_PARTY_GET_USER_URL_INVALID, e);
            }
        }

        JSONObject httpBodyJson = null;
        try {
            httpBodyJson = JSONObject.parseObject(httpConfig.getHttpBody());
        } catch (Exception e) {
            LOGGER.error("请求体json解析异常");
            throw new BusinessException(ThirdUserBusinessKey.RCDC_USER_THIRD_PARTY_GET_USER_BODY_INVALID, e);
        }

        try {
            httpBodyJson.put("sysUnicode", String.valueOf(new Random().nextInt(SUNSHINE_ASSET_MANAGEMENT_RANDOM_BOUND)));
            httpBodyJson.put("vcType", StringUtils.EMPTY);
            httpBodyJson.put("sign", ThirdPartyShineAssertMgmtSignUtil.getSign(httpBodyJson));
        } catch (Exception e) {
            LOGGER.error("阳光资管同步用户参数处理过程中出现异常, ex：", e);
            throw new BusinessException(ThirdUserBusinessKey.RCDC_USER_THIRD_PARTY_GET_USER_BODY_INVALID, e);
        }

        String protocol = url.getProtocol();
        String host = url.getHost();
        int port = url.getPort();
        String userInfo = url.getUserInfo();
        // xxxaaa 兼容处理，可以同时适配 Mock 接口和正式接口
        String getUserUrl =
                protocol + "://" + host + (port == -1 ? "" : ":" + port) + (httpConfig.getUrl().contains("/rcdc") ? GET_USER_URL_MOCK : GET_USER_URL);
        // 向第三方服务器发送请求信息
        String thirdPartyUserInfoStr = null;
        try {
            if (HttpMethod.POST == httpConfig.getHttpMethod()) {
                thirdPartyUserInfoStr = httpClient.doPost(getUserUrl, ThirdPartyConverterUtils.getRequestMap(httpConfig.getHttpHeaderList()),
                        httpBodyJson.toJSONString());
            } else if (HttpMethod.GET == httpConfig.getHttpMethod()) {
                thirdPartyUserInfoStr = httpClient.doGet(httpConfig.getUrl(), ThirdPartyConverterUtils.getRequestMap(httpConfig.getHttpHeaderList()),
                        httpBodyJson.toJSONString());
            }
            // xxxaaa 后期删除
            LOGGER.debug("请求第三方用户信息返回结果：[{}]", thirdPartyUserInfoStr);
        } catch (Exception ex) {
            LOGGER.error("请求第三方用户信息发生异常，ex:", ex);
            throw new BusinessException(ThirdUserBusinessKey.RCDC_USER_THIRD_PARTY_GET_USER_ERROR, ex);
        }

        // xxxaaa 兼容处理，可以同时适配 Mock 接口和正式接口
        if (JSONObject.parseObject(thirdPartyUserInfoStr).containsKey("content")) {
            String userInfoStr = JSONObject.parseObject(thirdPartyUserInfoStr).getString("content");
            thirdPartyUserInfoStr = StringUtils.isEmpty(userInfoStr) ? "[]" : userInfoStr;
        }
        ShineAssertMgmtUserResponse userResponse = JSONObject.parseObject(thirdPartyUserInfoStr, ShineAssertMgmtUserResponse.class);
        if (ShineAssertMgmtUserResponse.SUCCESS_CODE != userResponse.getCode()) {
            LOGGER.error("请求第三方用户信息发生异常, 错误码为【{}】:", userResponse.getCode());
            throw new BusinessException(ThirdUserBusinessKey.RCDC_USER_THIRD_PARTY_GET_USER_ERROR, userResponse.getMsg());
        }

        List<ShineAssertMgmtUserResponse.ThirdPartyDepartmentUserData> dataList = userResponse.getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            return Maps.newHashMap();
        }

        Map<String, List<BaseThirdPartyUserDTO>> groupWithUserMap = new HashMap<>();
        for (ShineAssertMgmtUserResponse.ThirdPartyDepartmentUserData thirdPartyDepartmentUserData : dataList) {
            String vcOrgName = thirdPartyDepartmentUserData.getVcOrgName();
            List<BaseThirdPartyUserDTO> thirdPartyUserList = groupWithUserMap.get(vcOrgName);
            if (CollectionUtils.isEmpty(thirdPartyUserList)) {
                thirdPartyUserList = new ArrayList<>();
            }

            for (ShineAssertMgmtUserResponse.ThirdPartyUserData thirdPartyUserData : thirdPartyDepartmentUserData.getUserList()) {
                BaseThirdPartyUserDTO cbbThirdPartyUserDTO = new BaseThirdPartyUserDTO();
                cbbThirdPartyUserDTO.setUserName(thirdPartyUserData.getUserCode());
                cbbThirdPartyUserDTO.setUserRealName(thirdPartyUserData.getUserName());
                cbbThirdPartyUserDTO.setUserEmail(thirdPartyUserData.getUserEmail());
                cbbThirdPartyUserDTO
                        .setState(ShineAssertMgmtUserResponse.NORMAL_STATUS == thirdPartyUserData.getUserStatus() ? IacUserStateEnum.ENABLE
                                : IacUserStateEnum.DISABLE);
                thirdPartyUserList.add(cbbThirdPartyUserDTO);
            }
            groupWithUserMap.put(vcOrgName, thirdPartyUserList);
        }

        return groupWithUserMap;
    }
}
