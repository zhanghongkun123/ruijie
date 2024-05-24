package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 分页查询桌面池信息参数
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 14:39
 *
 * @author linke
 */
public class DesktopPoolPageRequest extends PageSearchRequest {

    private static final String USER_ID = "userId";

    private static final String AD_GROUP_ID = "adGroupId";

    private static final String POOL_MODEL = "poolModel";

    private static final String POOL_STATE = "poolState";

    private static final String ID = "id";

    private static final String IMAGE_TEMPLATE_ID = "imageTemplateId";

    private static final String ROOT_IMAGE_ID = "rootImageId";

    private static final String STRATEGY_ID = "strategyId";

    private static final String NETWORK_ID = "networkId";

    private static final String IS_OPEN_MAINTENANCE = "isOpenMaintenance";

    private static final String DESKTOP_ID = "desktopId";

    private static final String PLATFORM_ID = "platformId";

    private static final String POOL_TYPE = "poolType";

    private static final String SESSION_TYPE = "sessionType";

    private static final String PLATFORM_STATUS = "platformStatus";

    private static final String PLATFORM_TYPE = "platformType";

    private UUID userId;

    private UUID adGroupId;

    private UUID desktopId;

    public DesktopPoolPageRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr, "exactMatchArr must not be null");
        // 对条件匹配数组对象进行组装
        List<MatchEqual> matchEqualList = assembleMatchEqualArr(exactMatchArr);
        return matchEqualList.toArray(new MatchEqual[0]);
    }

    private List<MatchEqual> assembleMatchEqualArr(ExactMatch[] exactMatchArr) {
        List<MatchEqual> matchEqualList = Lists.newArrayList();
        for (ExactMatch exactMatch : exactMatchArr) {
            MatchEqual matchEqual;
            switch (exactMatch.getName()) {
                case USER_ID:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        userId = UUID.fromString(exactMatch.getValueArr()[0]);
                    }
                    break;
                case AD_GROUP_ID:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        adGroupId = UUID.fromString(exactMatch.getValueArr()[0]);
                    }
                    break;
                case POOL_MODEL:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(CbbDesktopPoolModel::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case POOL_TYPE:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(CbbDesktopPoolType::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case SESSION_TYPE:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(CbbDesktopSessionType::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case POOL_STATE:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(CbbDesktopPoolState::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case ID:
                case IMAGE_TEMPLATE_ID:
                case ROOT_IMAGE_ID:
                case STRATEGY_ID:
                case NETWORK_ID:
                case PLATFORM_ID:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case IS_OPEN_MAINTENANCE:
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr())
                            .map(Boolean::valueOf).toArray());
                    matchEqualList.add(matchEqual);
                    break;
                case DESKTOP_ID:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        desktopId = UUID.fromString(exactMatch.getValueArr()[0]);
                    }
                    break;
                case PLATFORM_TYPE:
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(CloudPlatformType::valueOf).toArray(CloudPlatformType[]::new));
                    matchEqualList.add(matchEqual);
                    break;
                case PLATFORM_STATUS:
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(CloudPlatformStatus::valueOf).toArray(CloudPlatformStatus[]::new));
                    matchEqualList.add(matchEqual);
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    matchEqualList.add(matchEqual);
                    break;
            }
        }
        return matchEqualList;
    }

    /**
     * 是否需要处理canUsed信息
     *
     * @return true需要处理，false不需要
     */
    public boolean getNeedDealCanUsed() {
        return Objects.nonNull(desktopId);
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getAdGroupId() {
        return adGroupId;
    }

    public void setAdGroupId(UUID adGroupId) {
        this.adGroupId = adGroupId;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }
}
