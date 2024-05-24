package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;


/**
 * Description: 镜像服务处理类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/2
 *
 * @author zhiweiHong
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private RccmManageService rccmManageService;


    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Override
    public boolean isLockImage(UUID imageId) {
        Assert.notNull(imageId, "imageId can not be null");

        return rccmManageService.isSlave()
                && unifiedManageDataService.existsUnifiedData(imageId, UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE);
    }
}
