package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年02月25日
 *
 * @author dongquanyi
 */
public interface CloudDesktopExportDAOCustom {

    /**
     * @param page 页数
     * @return 列表
     */
    List<ViewUserDesktopEntity> findAllByExport(Pageable page);
}
