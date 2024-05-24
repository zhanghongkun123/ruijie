package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.mock;

import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * 第三方认证Mock
 * <p>
 * Description: 第三方认证Mock
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/13 16:24
 *
 * @author zjy
 */
@Api(tags = "第三方认证")
@Controller
@RequestMapping("/rco/user/thirdPartyMock")
public class ThirdPartyMockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyMockController.class);

    private static final String APPID = "appId";

    private static final String APPSECRET = "appSecret";

    private Map<String, ThirdPartyMockUserResponse.ThirdPartyUserData> userInfo = new HashMap<>();

    /**
     * mock认证
     *
     * @param authRequest 请求信息
     * @return AuthResponse 认证结果
     * @throws Exception 业务异常
     */
    @RequestMapping(value = "/auth")
    @NoAuthUrl
    public AuthResponse auth(AuthRequest authRequest) throws Exception {
        Assert.notNull(authRequest, "authRequest not be null");

        if (!APPID.equals(authRequest.getAppId()) || !APPSECRET.equals(authRequest.getAppSecret())) {
            return new AuthResponse("300004", "appId和appSecret校验失败");
        }

        if (StringUtils.isEmpty(authRequest.getUserCode()) || StringUtils.isEmpty(authRequest.getPassword())) {
            return new AuthResponse("300002", "用户名或密码错误");
        }

        if (userInfo.get(authRequest.getUserCode()) == null) {
            return new AuthResponse("300001", "用户未注册");
        }

        if (MockThirdPartyShineAssestMgmtDesUtil.encrypt("successUser").equals(authRequest.getUserCode())) {
            return new AuthResponse("000000", "成功");
        }

        if (MockThirdPartyShineAssestMgmtDesUtil.encrypt("failUser").equals(authRequest.getUserCode())) {
            return new AuthResponse("300002", "用户名或密码错误");
        }

        if (MockThirdPartyShineAssestMgmtDesUtil.encrypt("expiredUser").equals(authRequest.getUserCode())) {
            return new AuthResponse("300003", "用户名或密码错误");
        }

        if (MockThirdPartyShineAssestMgmtDesUtil.encrypt("errorUser").equals(authRequest.getUserCode())) {
            return new AuthResponse("999999", "用户名或密码错误");
        }

        return new AuthResponse("000000", "成功");
    }

    /**
     * mock获取用户
     *
     * @return ThirdPartyMockUserResponse 用户结果
     * @throws Exception 业务异常
     */
    @RequestMapping(value = "/getAllUser")
    @NoAuthUrl
    public ThirdPartyMockUserResponse getAllUser() throws Exception {
        ThirdPartyMockUserResponse response = new ThirdPartyMockUserResponse();
        response.setCode(000000);
        response.setMsg("成功");

        List<ThirdPartyMockUserResponse.ThirdPartyDepartmentUserData> dataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ThirdPartyMockUserResponse.ThirdPartyDepartmentUserData groupData =
                    new ThirdPartyMockUserResponse.ThirdPartyDepartmentUserData();
            List<ThirdPartyMockUserResponse.ThirdPartyUserData> userSameGroupList = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                ThirdPartyMockUserResponse.ThirdPartyUserData userItem = new ThirdPartyMockUserResponse.ThirdPartyUserData();
                userItem.setUserCode("G" + i + "User" + j);
                userItem.setUserName("G" + i + "User" + j);
                userItem.setUserEmail("G" + i + "User" + j + "@qq.com");
                userItem.setUserStatus(new Random().nextInt(2) % 2 + 1);
                userSameGroupList.add(userItem);
                userInfo.put(MockThirdPartyShineAssestMgmtDesUtil.encrypt(userItem.getUserCode()), userItem);
            }
            groupData.setUserList(userSameGroupList);
            groupData.setVcOrgName("thirdPartyGroup" + i);
            dataList.add(groupData);
        }

        ThirdPartyMockUserResponse.ThirdPartyDepartmentUserData customGroup =
                new ThirdPartyMockUserResponse.ThirdPartyDepartmentUserData();
        List<ThirdPartyMockUserResponse.ThirdPartyUserData> customUserList = new ArrayList<>();
        ThirdPartyMockUserResponse.ThirdPartyUserData userItem1 = new ThirdPartyMockUserResponse.ThirdPartyUserData();
        userItem1.setUserCode("successUser");
        userItem1.setUserName("successUser");
        userItem1.setUserEmail("successUser@qq.com");
        userItem1.setUserStatus(1);
        customUserList.add(userItem1);
        userInfo.put(MockThirdPartyShineAssestMgmtDesUtil.encrypt(userItem1.getUserCode()), userItem1);

        ThirdPartyMockUserResponse.ThirdPartyUserData userItem2 = new ThirdPartyMockUserResponse.ThirdPartyUserData();
        userItem2.setUserCode("failUser");
        userItem2.setUserName("failUser");
        userItem2.setUserEmail("failUser@qq.com");
        userItem2.setUserStatus(1);
        customUserList.add(userItem2);
        userInfo.put(MockThirdPartyShineAssestMgmtDesUtil.encrypt(userItem2.getUserCode()), userItem2);

        ThirdPartyMockUserResponse.ThirdPartyUserData userItem3 = new ThirdPartyMockUserResponse.ThirdPartyUserData();
        userItem3.setUserCode("expiredUser");
        userItem3.setUserName("expiredUser");
        userItem3.setUserEmail("expiredUser@qq.com");
        userItem3.setUserStatus(2);
        customUserList.add(userItem3);
        userInfo.put(MockThirdPartyShineAssestMgmtDesUtil.encrypt(userItem3.getUserCode()), userItem3);

        ThirdPartyMockUserResponse.ThirdPartyUserData userItem4 = new ThirdPartyMockUserResponse.ThirdPartyUserData();
        userItem4.setUserCode("errorUser");
        userItem4.setUserName("errorUser");
        userItem4.setUserEmail("errorUser@qq.com");
        userItem4.setUserStatus(1);
        customUserList.add(userItem4);
        userInfo.put(MockThirdPartyShineAssestMgmtDesUtil.encrypt(userItem4.getUserCode()), userItem4);

        customGroup.setVcOrgName("mockGroup");
        customGroup.setUserList(customUserList);
        dataList.add(customGroup);

        response.setDataList(dataList);
        return response;
    }


}
