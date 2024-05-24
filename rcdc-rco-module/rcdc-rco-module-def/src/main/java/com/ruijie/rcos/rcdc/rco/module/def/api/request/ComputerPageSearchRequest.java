package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/6 11:22
 *
 * @author ketb
 */
public class ComputerPageSearchRequest extends PageSearchRequest {

    public static final String DESKTOP_POOL_ID = "desktopPoolId";

    public static final String SESSION_TYPE = "sessionType";

    private UUID desktopPoolId;

    private CbbDesktopSessionType sessionType;

    public ComputerPageSearchRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr, "exactMatchArr must not be null");
        List<MatchEqual> matchEqualList = new ArrayList<>();
        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual = null;
            switch (exactMatch.getName()) {
                case DESKTOP_POOL_ID:
                    desktopPoolId = covertUUID(exactMatch.getValueArr());
                    continue;
                case SESSION_TYPE:
                    sessionType = CbbDesktopSessionType.valueOf(exactMatch.getValueArr()[0]);
                    continue;
                case "terminalGroupId":
                    String[] groupValueArr = exactMatch.getValueArr();
                    UUID[] groupIdArr = new UUID[groupValueArr.length];
                    for (int j = 0; j < groupValueArr.length; j++) {
                        groupIdArr[j] = UUID.fromString(groupValueArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), groupIdArr);
                    break;
                case "id":
                    String[] valueArr = exactMatch.getValueArr();
                    UUID[] idArr = new UUID[valueArr.length];
                    for (int j = 0; j < valueArr.length; j++) {
                        idArr[j] = UUID.fromString(valueArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), idArr);
                    break;
                case "state":
                    String[] stateStrArr = exactMatch.getValueArr();
                    ComputerStateEnum[] stateArr = new ComputerStateEnum[stateStrArr.length];
                    for (int j = 0; j < stateStrArr.length; j++) {
                        stateArr[j] = ComputerStateEnum.valueOf(stateStrArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), stateArr);
                    break;
                case "faultState":
                    boolean isFaultState = Boolean.parseBoolean(exactMatch.getValueArr()[0]);
                    matchEqual = new MatchEqual(exactMatch.getName(), new Object[]{isFaultState});
                    break;
                case "platform":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            new Object[]{CbbTerminalPlatformEnums.valueOf(exactMatch.getValueArr()[0])});
                    break;
                case "type":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            new Object[]{ComputerTypeEnum.valueOf(exactMatch.getValueArr()[0])});
                    break;
                case "name":
                    matchEqual = new MatchEqual(exactMatch.getName(), new Object[]{exactMatch.getValueArr()[0]});
                    break;
                case "ip":
                    matchEqual = new MatchEqual(exactMatch.getName(), new Object[]{exactMatch.getValueArr()[0]});
                    break;
                case "alias":
                    matchEqual = new MatchEqual(exactMatch.getName(), new Object[]{exactMatch.getValueArr()[0]});
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    break;
            }
            if (!Objects.equals(matchEqual.getName(), SESSION_TYPE)) {
                matchEqualList.add(matchEqual);
            }
        }

        return matchEqualList.toArray(new MatchEqual[0]);
    }

    private UUID covertUUID(Object[] valueArr) {
        return ArrayUtils.isNotEmpty(valueArr) ? UUID.fromString(String.valueOf(valueArr[0])) : null;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    @Override
    protected Sort[] sortConditionConvert(Sort sort) {
        Assert.notNull(sort, "sort can not be null");

        switch (sort.getSortField()) {
            case "name":
                sort.setSortField("name");
                break;
            case "alias":
                sort.setSortField("alias");
                break;
            case "ip":
                sort.setSortField("ip");
                break;
            case "mac":
                sort.setSortField("mac");
                break;
            case "state":
                sort.setSortField("state");
                break;
            case "faultState":
                sort.setSortField("faultState");
                break;
            case "faultTime":
                sort.setSortField("faultTime");
                break;
            case "os":
                sort.setSortField("os");
                break;
            default:
                break;
        }

        return super.sortConditionConvert(sort);
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }
}
