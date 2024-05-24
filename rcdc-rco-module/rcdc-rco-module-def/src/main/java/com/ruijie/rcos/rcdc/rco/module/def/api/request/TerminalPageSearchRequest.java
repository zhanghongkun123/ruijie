package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNicWorkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalWakeUpStatus;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月23日
 * 
 * @author nt
 */
public class TerminalPageSearchRequest extends PageSearchRequest {
    public TerminalPageSearchRequest(PageWebRequest webRequest) {
        super(webRequest);
    }
    
    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr , "exactMatchArr must not be null");
        MatchEqual[] matchEqualArr = new MatchEqual[exactMatchArr.length];

        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual = null;
            switch (exactMatch.getName()) {
                case "terminalGroupId":
                    String[] valueArr = exactMatch.getValueArr();
                    UUID[] idArr = new UUID[valueArr.length];
                    for (int j = 0; j < valueArr.length; j++) {
                        idArr[j] = UUID.fromString(valueArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), idArr);
                    break;
                case "id":
                    // 终端比较特殊，以terminalId作为唯一标识
                    matchEqual = new MatchEqual("terminalId", exactMatch.getValueArr());
                    break;
                case "hasLogin":
                    boolean hasLogin = Boolean.parseBoolean(exactMatch.getValueArr()[0]);
                    matchEqual = new MatchEqual("hasLogin", new Object[] {hasLogin});
                    break;
                case "terminalState":
                    String[] stateStrArr = exactMatch.getValueArr();
                    CbbTerminalStateEnums[] stateArr = new CbbTerminalStateEnums[stateStrArr.length];
                    for (int j = 0; j < stateStrArr.length; j++) {
                        stateArr[j] = CbbTerminalStateEnums.valueOf(stateStrArr[j]);
                    }

                    matchEqual = new MatchEqual("terminalState", stateArr);
                    break;
                case "platform":
                    Object[] platformValueArr = Stream.of(exactMatch.getValueArr()).map(CbbTerminalPlatformEnums::valueOf).toArray();
                    matchEqual = new MatchEqual(exactMatch.getName(), platformValueArr);
                    break;
                case "idvTerminalMode":
                    IdvTerminalModeEnums[] idvTerminalModeEnumsArr = Arrays.stream(exactMatch.getValueArr())
                            .map(IdvTerminalModeEnums::valueOf)
                            .toArray(IdvTerminalModeEnums[]::new);
                    matchEqual = new MatchEqual(exactMatch.getName(), idvTerminalModeEnumsArr);
                    break;
                case "pattern":
                    CbbCloudDeskPattern[] cbbCloudDeskPatternArr = Arrays.stream(exactMatch.getValueArr())
                            .map(CbbCloudDeskPattern::valueOf)
                            .toArray(CbbCloudDeskPattern[]::new);
                    matchEqual = new MatchEqual(exactMatch.getName(), cbbCloudDeskPatternArr);
                    break;
                case "downloadState":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(DownloadStateEnum::valueOf).toArray(DownloadStateEnum[]::new));
                    break;
                case "nicWorkMode":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Stream.of(exactMatch.getValueArr()).map(CbbNicWorkModeEnums::valueOf).toArray());
                    break;
                case "enableFullSystemDisk":
                    matchEqual = new MatchEqual(exactMatch.getName(), Stream.of(exactMatch.getValueArr()).map(Boolean::parseBoolean).toArray());
                    break;
                case "wakeUpStatus":
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(CbbTerminalWakeUpStatus::valueOf).toArray(CbbTerminalWakeUpStatus[]::new));
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    break;
            }
            matchEqualArr[i] = matchEqual;
        }
        
        return matchEqualArr;
    }

    @Override
    protected Sort[] sortConditionConvert(Sort sort) {
        Assert.notNull(sort, "sort can not be null");

        switch (sort.getSortField()) {
            case "lastOnlineTime":
                Sort appendSort = new Sort();
                appendSort.setSortField("hasOnline");
                appendSort.setDirection(sort.getDirection());

                // 最后上线时间大的在线时间更短， 排序顺序应相反
                Sort.Direction queryDirection =
                        sort.getDirection() == Sort.Direction.ASC ? Sort.Direction.DESC : Sort.Direction.ASC;
                sort.setDirection(queryDirection);
                return new Sort[] {appendSort, sort};
            case "mac":
                sort.setSortField("macAddr");
                break;
            default:
                break;
        }

        return super.sortConditionConvert(sort);
    }
}
