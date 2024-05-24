package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.request;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/1 上午9:40
 *
 * @author yanlin
 */
public class RcaStrategyBindUserPageSearchRequest extends PageSearchRequest {

    /**
     * 用户类型
     */
    public static final String USER_TYPE = "userType";

    /**
     * 用户状态
     */
    public static final String USER_STATE = "userState";

    public RcaStrategyBindUserPageSearchRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr, "exactMatchArr must not be null");
        MatchEqual[] matchEqualArr = new MatchEqual[exactMatchArr.length];

        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual;

            switch (exactMatch.getName()) {
                case "rcaStrategyId":
                    String[] valueArr = exactMatch.getValueArr();
                    UUID[] idArr = new UUID[valueArr.length];
                    for (int j = 0; j < valueArr.length; j++) {
                        idArr[j] = UUID.fromString(valueArr[j]);
                    }
                    matchEqual = new MatchEqual("rcaStrategyId", idArr);
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
                    matchEqual = new MatchEqual("state", stateArr);
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
