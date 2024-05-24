package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description: 云桌面查询附带分配信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/30
 *
 * @author linke
 */
public class DesktopAssignmentPageSearchRequest extends DeskPageSearchRequest {

    public static final String QUERY_SOURCE = "querySource";

    private final static String DESKTOP_TEMP_PERMISSION_ID = "desktopTempPermissionId";

    private static final String STRATEGY_TYPE = "desktopCategory";

    private UUID desktopTempPermissionId;

    private String querySource;

    private String strategyType;

    public DesktopAssignmentPageSearchRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr, "exactMatchArr must not be null");

        MatchEqual[] matchEqualArr = super.exactMatchConvert(exactMatchArr);

        List<MatchEqual> matchEqualList = new ArrayList<>();
        for (MatchEqual matchEqual : matchEqualArr) {
            switch (matchEqual.getName()) {
                case DESKTOP_TEMP_PERMISSION_ID:
                    desktopTempPermissionId = covertUUID(matchEqual.getValueArr());
                    continue;
                case QUERY_SOURCE:
                    querySource = covertString(matchEqual.getValueArr());
                    continue;
                case STRATEGY_TYPE:
                    strategyType = covertString(matchEqual.getValueArr());
                    continue;
                default:
                    matchEqualList.add(matchEqual);
            }
        }
        return matchEqualList.toArray(new MatchEqual[0]);
    }

    private UUID covertUUID(Object[] valueArr) {
        return ArrayUtils.isNotEmpty(valueArr) ? UUID.fromString(String.valueOf(valueArr[0])) : null;
    }

    private String covertString(Object[] valueArr) {
        return ArrayUtils.isNotEmpty(valueArr) ? String.valueOf(valueArr[0]) : null;
    }

    public UUID getDesktopTempPermissionId() {
        return desktopTempPermissionId;
    }

    public void setDesktopTempPermissionId(UUID desktopTempPermissionId) {
        this.desktopTempPermissionId = desktopTempPermissionId;
    }

    public String getQuerySource() {
        return querySource;
    }

    public void setQuerySource(String querySource) {
        this.querySource = querySource;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }

}
