package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/27 20:18
 *
 * @author zhangsiming
 */
public class RcaStrategyRelatedObjectRequest extends PageSearchRequest {

    /**
     * 用户类型
     */
    public static final String USER_TYPE = "userType";

    /**
     * 用户状态
     */
    public static final String USER_STATE = "userState";


    @NotNull
    private UUID strategyId;

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public RcaStrategyRelatedObjectRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr, "exactMatchArr must not be null");
        List<MatchEqual> matchEqualList = Lists.newArrayList();
        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual = null;

            switch (exactMatch.getName()) {
                case "strategyId":
                    String[] valueArr = exactMatch.getValueArr();
                    setStrategyId(UUID.fromString(valueArr[0]));
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
            if (matchEqual != null) {
                matchEqualList.add(matchEqual);
            }
        }
        return matchEqualList.toArray(new MatchEqual[0]);
    }
}
