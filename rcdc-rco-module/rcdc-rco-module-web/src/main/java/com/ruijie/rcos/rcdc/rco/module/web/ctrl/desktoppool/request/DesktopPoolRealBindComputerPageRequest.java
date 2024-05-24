package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserPageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 分页查询桌面池关联的所有用户（用户组下的用户+分配的用户）
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 14:39
 *
 * @author linke
 */
public class DesktopPoolRealBindComputerPageRequest extends UserPageSearchRequest {

    private static final String DESKTOP_POOL_ID = "desktopPoolId";

    private static final String TERMINAL_GROUP_ID = "terminalGroupId";

    public DesktopPoolRealBindComputerPageRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    private UUID desktopPoolId;

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr , "exactMatchArr must not be null");
        MatchEqual[] matchEqualArr = super.exactMatchConvert(exactMatchArr);
        List<MatchEqual> matchEqualList = Lists.newArrayList();
        for (MatchEqual matchEqual : matchEqualArr) {
            if (DESKTOP_POOL_ID.equals(matchEqual.getName())) {
                if (ArrayUtils.isNotEmpty(matchEqual.getValueArr())) {
                    desktopPoolId = UUID.fromString(matchEqual.getValueArr()[0].toString());
                }
                matchEqual = buildUUidMatchEqual(matchEqual);
            }
            if (TERMINAL_GROUP_ID.equals(matchEqual.getName())) {
                matchEqual = buildUUidMatchEqual(matchEqual);
            }
            matchEqualList.add(matchEqual);
        }
        return matchEqualList.toArray(new MatchEqual[0]);
    }

    private static MatchEqual buildUUidMatchEqual(MatchEqual matchEqual) {
        Object[] valueArr = matchEqual.getValueArr();
        UUID[] idArr = new UUID[valueArr.length];
        for (int j = 0; j < valueArr.length; j++) {
            idArr[j] = UUID.fromString(valueArr[j].toString());
        }
        matchEqual = new MatchEqual(matchEqual.getName(), idArr);
        return matchEqual;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }
}
