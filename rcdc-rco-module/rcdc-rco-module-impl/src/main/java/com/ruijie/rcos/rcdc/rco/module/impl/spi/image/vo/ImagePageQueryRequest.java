package com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo;

import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.Sort;

/**
 * Description: 查询所有 VOI 镜像信息，由于未提供接口，直接将分页大小设置成 1000 查询
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.04.25
 *
 * @author linhj
 */
public class ImagePageQueryRequest implements PageQueryRequest {

    @Override
    public int getLimit() {
        return 1000;
    }

    @Override
    public int getPage() {
        return 0;
    }

    @Override
    public Match[] getMatchArr() {
        return new Match[0];
    }

    @Override
    public Sort[] getSortArr() {
        return new Sort[0];
    }
}