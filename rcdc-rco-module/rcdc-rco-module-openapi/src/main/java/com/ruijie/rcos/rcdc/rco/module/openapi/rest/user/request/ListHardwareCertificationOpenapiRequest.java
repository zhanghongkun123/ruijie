package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacUserHardwareCertificationStateEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.sk.pagekit.kernel.request.criteria.DefaultExactMatch;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月23日
 *
 * @author zdc
 */
public class ListHardwareCertificationOpenapiRequest extends PageQueryServerRequest {

    private static final String REQUEST_FIELD_STATE = "state";

    private static final String REQUEST_FIELD_INIT_STATE = "initState";

    @Nullable
    private String adminName;

    @Nullable
    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(@Nullable String adminName) {
        this.adminName = adminName;
    }

    @Override
    protected MatchEqual[] toMatchEqual(DefaultExactMatch[] matchArr) {
        Assert.notNull(matchArr, "matchArr must not be null");

        List<MatchEqual> matchEqualList = new ArrayList<>();
        for (DefaultExactMatch defaultExactMatch : matchArr) {
            MatchEqual matchEqual = null;
            if (REQUEST_FIELD_STATE.equals(defaultExactMatch.getFieldName())) {
                matchEqual = createStateMatchEqual(REQUEST_FIELD_STATE, defaultExactMatch);
            } else if (REQUEST_FIELD_INIT_STATE.equals(defaultExactMatch.getFieldName())) {
                matchEqual = createStateMatchEqual(REQUEST_FIELD_INIT_STATE, defaultExactMatch);
            } else {
                matchEqual = new MatchEqual(defaultExactMatch.getFieldName(), defaultExactMatch.getValueArr());
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

    private MatchEqual createStateMatchEqual(String name, DefaultExactMatch defaultExactMatch) {
        Object[] stateObjectArr = defaultExactMatch.getValueArr();
        IacUserHardwareCertificationStateEnum[] stateArr = new IacUserHardwareCertificationStateEnum[stateObjectArr.length];
        for (int j = 0; j < stateObjectArr.length; j++) {
            stateArr[j] = IacUserHardwareCertificationStateEnum.valueOf((String)stateObjectArr[j]);
        }
        return new MatchEqual(name, stateArr);
    }
}
