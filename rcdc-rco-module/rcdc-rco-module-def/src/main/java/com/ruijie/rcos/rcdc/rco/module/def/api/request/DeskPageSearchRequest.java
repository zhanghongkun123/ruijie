package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DeskCreateMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月8日
 *
 * @author artom
 */
public class DeskPageSearchRequest extends PageSearchRequest {

    private final static String SCHEDULE_TYPE_CODE = "scheduleTypeCode";

    private final static String IS_OPEN_DESK_MAINTENANCE = "isOpenDeskMaintenance";

    private static final String USER_ID = "userId";

    private String scheduleTypeCode;

    /**
     *
     * 是否有全部权限
     *
     */
    private Boolean hasAllPermission;

    /**
     * 权限id 超级管理员可以为空
     */
    private UUID adminId;

    /**
     *
     * 镜像用途
     *
     */
    private ImageUsageTypeEnum imageUsage;

    public DeskPageSearchRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    /**
     * 镜像模板ID查询条件
     */
    private static final String SEARCH_KEY_OF_IMAGE_TEMPLATE_ID = "imageTemplateId";

    /**
     * 镜像多版本根镜像模板id查询条件
     */
    private static final String SEARCH_KEY_OF_ROOT_IMAGE_ID = "rootImageId";

    private static final String USER_STATE = "userState";

    private static final String USER_INVALID = "userInvalid";

    private static final String STRATEGY_TYPE = "strategyType";

    private List<UUID> userIdList;

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr, "exactMatchArr must not be null");

        // 获取查询条件中是否包含镜像模板ID
        boolean hasSearchByImageTemplateId = Arrays.stream(exactMatchArr) //
                .anyMatch(exactMatch -> StringUtils.equals(exactMatch.getName(), SEARCH_KEY_OF_IMAGE_TEMPLATE_ID)
                    || StringUtils.equals(exactMatch.getName(), SEARCH_KEY_OF_ROOT_IMAGE_ID));

        // 对条件匹配数组对象进行组装
        return assembleMatchEqualArr(exactMatchArr, hasSearchByImageTemplateId);
    }

    private MatchEqual[] assembleMatchEqualArr(ExactMatch[] exactMatchArr, boolean hasSearchByImageTemplateId) {
        List<MatchEqual> matchEqualList = new ArrayList<>();

        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual = null;
            switch (exactMatch.getName()) {
                case "cbbNetworkId":
                case "cbbStrategyId":
                case "imageTemplateId":
                case "softwareStrategyId":
                case "id":
                case "physicalServerId":
                case "userProfileStrategyId":
                case "desktopPoolId":
                case "rootImageId":
                case "platformId":
                case "rcaHostId":
                case "rcaPoolId":
                    matchEqual = new MatchEqual(exactMatch.getName(), Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
                    break;
                case USER_ID:
                    userIdList = Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).collect(Collectors.toList());
                    break;
                // 前端属性和后端定义不一致，前端desktoptype:个性/还原,而用的是pattern, 前端修订量大，暂时不改
                case "desktopType":
                    matchEqual = new MatchEqual("pattern", exactMatch.getValueArr());
                    break;
                case "desktopCategory":
                    // 历史遗留导致t_cbb_desk_info.desk_type，TCI桌面也记录为IDV，所以无法正常使用t_cbb_desk_info.desk_type查询，使用策略类型来替代
                    matchEqual = new MatchEqual(STRATEGY_TYPE, exactMatch.getValueArr());
                    break;
                case "desktopState":
                    matchEqual = new MatchEqual("deskState", exactMatch.getValueArr());
                    break;
                case "isDelete":
                    boolean isDelete = Boolean.parseBoolean(exactMatch.getValueArr()[0]);
                    matchEqual = new MatchEqual("isDelete", new Object[] {isDelete});
                    break;
                case "hasLogin":
                    boolean hasLogin = Boolean.parseBoolean(exactMatch.getValueArr()[0]);
                    matchEqual = new MatchEqual("hasLogin", new Object[] {hasLogin});
                    break;
                case "isWindowsOsActive":
                    boolean isWindowsOsActive = Boolean.parseBoolean(exactMatch.getValueArr()[0]);
                    matchEqual = new MatchEqual("isWindowsOsActive", new Object[] {isWindowsOsActive});
                    break;
                case "faultState":
                    boolean isFaultState = Boolean.parseBoolean(exactMatch.getValueArr()[0]);
                    matchEqual = new MatchEqual("faultState", new Object[] {isFaultState});
                    break;
                case "downloadState":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(DownloadStateEnum::valueOf).toArray(DownloadStateEnum[]::new));
                    break;
                case "mode":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(UserProfilePathModeEnum::valueOf).toArray(UserProfilePathModeEnum[]::new));
                    break;
                case "type":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(UserProfilePathTypeEnum::valueOf).toArray(UserProfilePathTypeEnum[]::new));
                    break;
                case "imageUsage":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(ImageUsageTypeEnum::valueOf).toArray(ImageUsageTypeEnum[]::new));
                    break;
                case SCHEDULE_TYPE_CODE:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        // 不加入条件，特殊处理
                        this.scheduleTypeCode = exactMatch.getValueArr()[0];
                    }
                    break;
                case USER_STATE:
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(IacUserStateEnum::valueOf).toArray(IacUserStateEnum[]::new));
                    break;
                case IS_OPEN_DESK_MAINTENANCE:
                case USER_INVALID:
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(Boolean::parseBoolean).toArray(Boolean[]::new));
                    break;
                case "showRootPwd":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(Boolean::parseBoolean).toArray(Boolean[]::new));
                    break;
                case "rcaHostSessionType":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(RcaEnum.HostSessionType::valueOf).toArray(RcaEnum.HostSessionType[]::new));
                    break;
                case "rcaPoolType":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(RcaEnum.PoolType::valueOf).toArray(RcaEnum.PoolType[]::new));
                    break;
                case "platformType":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(CloudPlatformType::valueOf).toArray(CloudPlatformType[]::new));
                    break;
                case "platformStatus":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(CloudPlatformStatus::valueOf).toArray(CloudPlatformStatus[]::new));
                    break;
                case "sessionType":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(CbbDesktopSessionType::valueOf).toArray(CbbDesktopSessionType[]::new));
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    break;
            }
            if (Objects.nonNull(matchEqual)) {
                matchEqualList.add(matchEqual);
            }
        }

        // 查询条件包含镜像模板id字段，则在所有查询条件拼接完成后，补充拼接查询不包含完整克隆创建方式的条件
        if (hasSearchByImageTemplateId) {
            matchEqualList.add(new MatchEqual("deskCreateMode", new Object[] {DeskCreateMode.LINK_CLONE.name(), DeskCreateMode.OTHER.name()}));
        }
        return matchEqualList.toArray(new MatchEqual[0]);
    }

    private void initParam(ExactMatch[] exactMatchArr) {
        if (Objects.isNull(exactMatchArr)) {
            return;
        }
        for (ExactMatch exactMatch : exactMatchArr) {
            if (Objects.equals(exactMatch.getName(), SCHEDULE_TYPE_CODE)) {
                if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                    this.scheduleTypeCode = exactMatch.getValueArr()[0];
                }
            }
        }
    }

    public Boolean getHasAllPermission() {
        return hasAllPermission;
    }

    public void setHasAllPermission(Boolean hasAllPermission) {
        this.hasAllPermission = hasAllPermission;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public String getScheduleTypeCode() {
        return scheduleTypeCode;
    }

    public void setScheduleTypeCode(String scheduleTypeCode) {
        this.scheduleTypeCode = scheduleTypeCode;
    }

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }

    public List<UUID> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<UUID> userIdList) {
        this.userIdList = userIdList;
    }
}
