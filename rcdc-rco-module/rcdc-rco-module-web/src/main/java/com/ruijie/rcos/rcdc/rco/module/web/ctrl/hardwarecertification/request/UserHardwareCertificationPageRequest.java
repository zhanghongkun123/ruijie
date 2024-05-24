package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacUserHardwareCertificationStateEnum;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Description: 用户硬件特征码分页查询请求参数
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
public class UserHardwareCertificationPageRequest extends PageSearchRequest {
    private static final String REQUEST_FIELD_STATE = "state";

    private static final String REQUEST_FIELD_INIT_STATE = "initState";

    public UserHardwareCertificationPageRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr, "exactMatchArr must not be null");

        List<MatchEqual> matchEqualList = new ArrayList<>();
        for (ExactMatch exactMatch : exactMatchArr) {
            MatchEqual matchEqual = null;
            if (REQUEST_FIELD_STATE.equals(exactMatch.getName())) {
                matchEqual = createStateMatchEqual(REQUEST_FIELD_STATE, exactMatch);
            } else if (REQUEST_FIELD_INIT_STATE.equals(exactMatch.getName())) {
                matchEqual = createStateMatchEqual(REQUEST_FIELD_INIT_STATE, exactMatch);
            } else {
                matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
            }
            matchEqualList.add(matchEqual);
        }

        MatchEqual[] matchEqualArr;
        if (matchEqualList.stream().anyMatch(item -> REQUEST_FIELD_STATE.equals(item.getName()))) {
            matchEqualList = matchEqualList.stream().filter(item -> !REQUEST_FIELD_INIT_STATE.equals(item.getName())).collect(Collectors.toList());
            matchEqualArr = new MatchEqual[matchEqualList.size()];
            return matchEqualList.toArray(matchEqualArr);
        }
        if (matchEqualList.stream().anyMatch(item -> REQUEST_FIELD_INIT_STATE.equals(item.getName()))) {
            matchEqualList.forEach(item -> {
                if (REQUEST_FIELD_INIT_STATE.equals(item.getName())) {
                    item.setName(REQUEST_FIELD_STATE);
                }
            });
            matchEqualArr = new MatchEqual[matchEqualList.size()];
            return matchEqualList.toArray(matchEqualArr);
        }

        matchEqualArr = new MatchEqual[matchEqualList.size()];
        return matchEqualList.toArray(matchEqualArr);
    }

    private MatchEqual createStateMatchEqual(String name, ExactMatch exactMatch) {
        String[] stateStrArr = exactMatch.getValueArr();
        IacUserHardwareCertificationStateEnum[] stateArr = new IacUserHardwareCertificationStateEnum[stateStrArr.length];
        for (int j = 0; j < stateStrArr.length; j++) {
            stateArr[j] = IacUserHardwareCertificationStateEnum.valueOf(stateStrArr[j]);
        }
        return new MatchEqual(name, stateArr);
    }
}
