package com.ruijie.rcos.rcdc.rco.module.impl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchPushInstallPackageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewPushInstallPackageEntity;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/04/05 16:06
 *
 * @author coderLee23
 */
public interface ViewPushInstallPackageService {

    /**
     * 根据查询条件，返回推送安装包列表
     *
     * @param searchPushInstallPackageDTO 查询对象
     * @param pageable 分页参数
     * @return Page<ViewPushInstallPackageEntity>
     */
    Page<ViewPushInstallPackageEntity> pagePushInstallPackage(SearchPushInstallPackageDTO searchPushInstallPackageDTO, Pageable pageable);
}
