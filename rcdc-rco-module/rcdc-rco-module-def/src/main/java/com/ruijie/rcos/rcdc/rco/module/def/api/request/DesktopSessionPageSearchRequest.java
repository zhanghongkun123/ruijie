package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月06日
 *
 * @author wangjie9
 */
public class DesktopSessionPageSearchRequest extends PageSearchRequest {

    /**
     * 用户名称
     */
    public static final String USERNAME = "userName";

    /**
     * 真实姓名
     */
    public static final String REALNAME = "realName";

    /**
     * 桌面名称
     */
    public static final String DESKTOPNAME = "desktopName";

    /**
     * 桌面id
     */
    public static final String DESKID = "deskId";

    /**
     * 终端ip
     */
    public static final String TERMINALIP = "terminalIp";

    /**
     * 桌面池id
     */
    public static final String DESKTOPPOOLID = "desktopPoolId";

    /**
     * 桌面池模式
     */
    public static final String DESKTOPPOOLMODEL = "desktopPoolModel";

    /**
     * 会话状态
     */
    public static final String SESSIONSTATUS = "sessionStatus";


    public DesktopSessionPageSearchRequest(PageWebRequest webRequest) {
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
                case USERNAME:
                    matchEqual = new MatchEqual(USERNAME, exactMatch.getValueArr());
                    break;
                case REALNAME:
                    matchEqual = new MatchEqual(REALNAME, exactMatch.getValueArr());
                    break;
                case DESKTOPNAME:
                    matchEqual = new MatchEqual(DESKTOPNAME, exactMatch.getValueArr());
                    break;
                case TERMINALIP:
                    matchEqual = new MatchEqual(TERMINALIP, exactMatch.getValueArr());
                    break;
                case DESKTOPPOOLID:
                    matchEqual = new MatchEqual(DESKTOPPOOLID, Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
                    break;
                case DESKTOPPOOLMODEL:
                    matchEqual = new MatchEqual(DESKTOPPOOLMODEL, exactMatch.getValueArr());
                    break;
                case DESKID:
                    matchEqual = new MatchEqual(DESKID, Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
                    break;
                case SESSIONSTATUS:
                    matchEqual = new MatchEqual(SESSIONSTATUS, exactMatch.getValueArr());
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
