package com.ruijie.rcos.rcdc.rco.module.impl.service;

import org.springframework.data.domain.Page;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalWithImageEntity;
import com.ruijie.rcos.sk.pagekit.api.PageQueryAPI;


/**
 * Description: 终端关于镜像信息视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月12日
 *
 * @author ypp
 */
public interface QueryImageTerminalListService extends PageQueryAPI<ViewTerminalWithImageEntity> {

    /**
     * 分页查询终端上关于镜像信息列表
     *
     * @param request 请求
     * @return DefaultPageResponse<ViewTerminalWithImageEntity>
     */
    Page<ViewTerminalWithImageEntity> pageQuery(PageSearchRequest request);


    /**
     *  查找镜像终端信息
     *
     * @param imageId      镜像id
     * @param terminalId   终端id
     * @return 镜像终端信息
     */
    ViewTerminalWithImageEntity getTerminalWithImage(String imageId, String terminalId);

}
