package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.sk.pagekit.kernel.request.criteria.DefaultExactMatch;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月24日
 * 
 * @author nt
 */
public class UserPageSearchRequest {
    /**
     * 用户id
     */
    public static final String ID = "id";

    /**
     * 用户组id
     */
    public static final String GROUP_ID = "groupId";

    /**
     * 动态口令绑定
     */
    public static final String HAS_BIND_OTP = "hasBindOtp";

    /**
     * 动态口令开启
     */
    public static final String OPEN_OTP_CERTIFICATION = "openOtpCertification";

    /**
     * 用户类型
     */
    public static final String USER_TYPE = "userType";

    /**
     * 用户状态
     */
    public static final String USER_STATE = "userState";

    /**
     * 状态
     */
    public static final String STATE = "state";

    /**
     * 账号密码登录
     */
    public static final String OPEN_ACCOUNT_PASSWORD_CERTIFICATION = "openAccountPasswordCertification";

    /**
     * CAS登录
     */
    public static final String OPEN_CAS_CERTIFICATION = "openCasCertification";

    /**
     * 动态应用组id
     */
    public static final String APP_GROUP_ID = "appGroupId";

    /**
     * 分页查询条件类型转换
     * @param matchArr 请求查询条件
     * @return  转换后类型
     */
    public static MatchEqual[] matchConvert(DefaultExactMatch[] matchArr) {
        Assert.notNull(matchArr, "matchArr must not be null");
        MatchEqual[] matchEqualArr = new MatchEqual[matchArr.length];

        for (int i = 0; i < matchArr.length; i++) {
            DefaultExactMatch match = matchArr[i];
            MatchEqual matchEqual = null;
            switch (match.getFieldName()) {
                case ID:
                case GROUP_ID:
                case APP_GROUP_ID:
                    Object[] valueArr = match.getValueArr();
                    UUID[] idArr = new UUID[valueArr.length];
                    for (int j = 0; j < valueArr.length; j++) {
                        idArr[j] = UUID.fromString((String) valueArr[j]);
                    }
                    matchEqual = new MatchEqual(match.getFieldName(), idArr);
                    break;
                case OPEN_CAS_CERTIFICATION:
                case OPEN_ACCOUNT_PASSWORD_CERTIFICATION:
                case OPEN_OTP_CERTIFICATION:
                case HAS_BIND_OTP:
                    Object[] otpArr = match.getValueArr();
                    Boolean[] hasOtpArr = new Boolean[otpArr.length];
                    for (int j = 0; j < otpArr.length; j++) {
                        hasOtpArr[j] = (Boolean) otpArr[j];
                    }
                    matchEqual = new MatchEqual(match.getFieldName(), hasOtpArr);
                    break;
                case USER_TYPE:
                    Object[] userTypeStrArr = match.getValueArr();
                    IacUserTypeEnum[] userTypeArr = new IacUserTypeEnum[userTypeStrArr.length];
                    for (int j = 0; j < userTypeStrArr.length; j++) {
                        userTypeArr[j] = IacUserTypeEnum.valueOf((String) userTypeStrArr[j]);
                    }
                    matchEqual = new MatchEqual(match.getFieldName(), userTypeArr);
                    break;
                case USER_STATE:
                    Object[] stateStrArr = match.getValueArr();
                    IacUserStateEnum[] stateArr = new IacUserStateEnum[stateStrArr.length];
                    for (int j = 0; j < stateStrArr.length; j++) {
                        stateArr[j] = IacUserStateEnum.valueOf((String) stateStrArr[j]);
                    }
                    matchEqual = new MatchEqual(STATE, stateArr);
                    break;
                default:
                    matchEqual = new MatchEqual(match.getFieldName(), match.getValueArr());
                    break;
            }
            matchEqualArr[i] = matchEqual;
        }
        return matchEqualArr;
    }

}
