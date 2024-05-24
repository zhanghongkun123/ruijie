package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserPageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 查询临时权限绑定用户列表的参数
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/24
 *
 * @author linke
 */
public class DesktopTempPermissionBindUserRequest extends UserPageSearchRequest {

    private static final String DESKTOP_TEMP_PERMISSION_ID = "desktopTempPermissionId";

    private UUID desktopTempPermissionId;

    public DesktopTempPermissionBindUserRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr , "exactMatchArr must not be null");
        MatchEqual[] matchEqualArr = super.exactMatchConvert(exactMatchArr);
        List<MatchEqual> matchEqualList = Lists.newArrayList();
        for (MatchEqual matchEqual : matchEqualArr) {
            if (Objects.equals(matchEqual.getName(), DESKTOP_TEMP_PERMISSION_ID)) {
                Object[] valueArr = matchEqual.getValueArr();
                if (ArrayUtils.isEmpty(valueArr)) {
                    continue;
                }
                desktopTempPermissionId = UUID.fromString(valueArr[0].toString());
                continue;
            }
            matchEqualList.add(matchEqual);
        }
        return matchEqualList.toArray(new MatchEqual[0]);
    }

    public UUID getDesktopTempPermissionId() {
        return desktopTempPermissionId;
    }

    public void setDesktopTempPermissionId(UUID desktopTempPermissionId) {
        this.desktopTempPermissionId = desktopTempPermissionId;
    }
}
