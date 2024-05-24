package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseDurationEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月24日
 *
 * @author nt
 */
public class UserPageSearchRequest extends PageSearchRequest {
    /**
     * 用户id
     */
    public static final String ID = "id";

    /**
     * 用户组id
     */
    public static final String GROUP_ID = "groupId";

    /**
     * desktopId
     */
    public static final String DESKTOP_ID = "desktopId";

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
     * 短信认证
     */
    public static final String OPEN_SMS_CERTIFICATION = "openSmsCertification";

    /**
     * 硬件特征码
     */
    public static final String OPEN_HARDWARE_CERTIFICATION = "openHardwareCertification";

    /**
     * 动态应用组id
     */
    public static final String APP_GROUP_ID = "appGroupId";

    public static final String INVALID = "invalid";

    /**
     * 是否开启Radius
     */
    public static final String OPEN_RADIUS_CERTIFICATION = "openRadiusCertification";

    /**
     * 是否开启第三方认证
     */
    public static final String OPEN_THIRD_PARTY_CERTIFICATION = "openThirdPartyCertification";

    /**
     * 授权持续类型
     */
    public static final String LICENSE_DURATION = "licenseDuration";

    public UserPageSearchRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr, "exactMatchArr must not be null");
        MatchEqual[] matchEqualArr = new MatchEqual[exactMatchArr.length];

        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual = null;
            switch (exactMatch.getName()) {
                case ID:
                case GROUP_ID:
                case APP_GROUP_ID:
                case DESKTOP_ID:
                    String[] valueArr = exactMatch.getValueArr();
                    UUID[] idArr = new UUID[valueArr.length];
                    for (int j = 0; j < valueArr.length; j++) {
                        idArr[j] = UUID.fromString(valueArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), idArr);
                    break;
                case OPEN_CAS_CERTIFICATION:
                case OPEN_ACCOUNT_PASSWORD_CERTIFICATION:
                case OPEN_OTP_CERTIFICATION:
                case HAS_BIND_OTP:
                case OPEN_SMS_CERTIFICATION:
                case OPEN_HARDWARE_CERTIFICATION:
                case OPEN_RADIUS_CERTIFICATION:
                case OPEN_THIRD_PARTY_CERTIFICATION:
                    String[] otpArr = exactMatch.getValueArr();
                    Boolean[] hasOtpArr = new Boolean[otpArr.length];
                    for (int j = 0; j < otpArr.length; j++) {
                        hasOtpArr[j] = Boolean.parseBoolean(otpArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), hasOtpArr);
                    break;
                case USER_TYPE:
                    String[] userTypeStrArr = exactMatch.getValueArr();
                    IacUserTypeEnum[] userTypeArr = new IacUserTypeEnum[userTypeStrArr.length];
                    for (int j = 0; j < userTypeStrArr.length; j++) {
                        userTypeArr[j] = IacUserTypeEnum.valueOf(userTypeStrArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), userTypeArr);
                    break;
                case USER_STATE:
                    String[] stateStrArr = exactMatch.getValueArr();
                    IacUserStateEnum[] stateArr = new IacUserStateEnum[stateStrArr.length];
                    for (int j = 0; j < stateStrArr.length; j++) {
                        stateArr[j] = IacUserStateEnum.valueOf(stateStrArr[j]);
                    }
                    matchEqual = new MatchEqual(STATE, stateArr);
                    break;
                case INVALID:
                    String[] invalidStrArr = exactMatch.getValueArr();
                    Boolean[] invalidArr = new Boolean[invalidStrArr.length];
                    for (int j = 0; j < invalidStrArr.length; j++) {
                        invalidArr[j] = Boolean.parseBoolean(invalidStrArr[j]);
                    }
                    matchEqual = new MatchEqual(INVALID, invalidArr);
                    break;
                case LICENSE_DURATION:
                    String[] licenseDurationStrArr = exactMatch.getValueArr();
                    CbbLicenseDurationEnum[] licenseDurationArr = new CbbLicenseDurationEnum[licenseDurationStrArr.length];
                    for (int j = 0; j < licenseDurationStrArr.length; j++) {
                        licenseDurationArr[j] = CbbLicenseDurationEnum.valueOf(licenseDurationStrArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), licenseDurationArr);
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    break;
            }
            matchEqualArr[i] = matchEqual;
        }

        return matchEqualArr;
    }

}
