package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request;

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
 * Description: 磁盘关联用户的请求类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/29
 *
 * @author TD
 */
public class DiskPoolRealBindUserPageRequest extends UserPageSearchRequest {

    private static final String DISK_POOL_ID = "diskPoolId";

    public DiskPoolRealBindUserPageRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    private UUID diskPoolId;

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr , "exactMatchArr must not be null");
        MatchEqual[] matchEqualArr = super.exactMatchConvert(exactMatchArr);
        List<MatchEqual> matchEqualList = Lists.newArrayList();
        for (MatchEqual matchEqual : matchEqualArr) {
            if (Objects.equals(matchEqual.getName(), DISK_POOL_ID)) {
                Object[] valueArr = matchEqual.getValueArr();
                if (ArrayUtils.isEmpty(valueArr)) {
                    continue;
                }
                diskPoolId = UUID.fromString(valueArr[0].toString());
                continue;
            }
            matchEqualList.add(matchEqual);
        }
        return matchEqualList.toArray(new MatchEqual[0]);
    }

    public UUID getDiskPoolId() {
        return diskPoolId;
    }

    public void setDiskPoolId(UUID diskPoolId) {
        this.diskPoolId = diskPoolId;
    }
}

