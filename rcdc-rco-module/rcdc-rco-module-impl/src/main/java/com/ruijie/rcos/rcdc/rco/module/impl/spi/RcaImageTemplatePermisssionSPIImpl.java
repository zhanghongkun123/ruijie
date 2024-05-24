package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.LocalImageTemplatePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rca.module.def.spi.RcaImageTemplatePermissionSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListImageIdRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * Description: 获取管理员镜像权限
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/16 下午3:10
 *
 * @author yanlin
 */
public class RcaImageTemplatePermisssionSPIImpl implements RcaImageTemplatePermissionSPI {

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Override
    public List<String> getImagePermissionInfo(UUID userId,
                                               LocalImageTemplatePageRequest pageSearchRequest) throws BusinessException {
        Assert.notNull(userId, "sessionContext cannot be null");
        Assert.notNull(pageSearchRequest, "pageSearchRequest cannot be null");

        ListImageIdRequest listImageIdRequest = new ListImageIdRequest();
        listImageIdRequest.setAdminId(userId);
        List<String> imageIdStrList = adminDataPermissionAPI.listImageIdByAdminId(listImageIdRequest).getImageIdList();
        if (!CollectionUtils.isEmpty(imageIdStrList)) {
            // 添加镜像ID筛选条件
            UUID[] imageIdArr = imageIdStrList.stream().map(UUID::fromString).toArray(UUID[]::new);
            pageSearchRequest.appendCustomMatchEqual(new MatchEqual("id", imageIdArr));
        }

        return imageIdStrList;
    }
}
