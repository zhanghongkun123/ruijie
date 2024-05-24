package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.response;

import java.util.Arrays;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AbstractPageQueryResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 分页查询结果子类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/24 20:54
 *
 * @author lyb
 */
public class AbstractDeskPageQueryResponse extends AbstractPageQueryResponse<DesktopItem, CloudDesktopDTO> {


    public AbstractDeskPageQueryResponse() {
        super();
    }

    /**
     * 子类实现，将业务层DTO转换为对外输出
     * 
     * @param defaultPageResponse 业务层分页响应类
     * @return 对外输出内容集合
     */
    @Override
    public DesktopItem[] convertItems(DefaultPageResponse<CloudDesktopDTO> defaultPageResponse) {
        Assert.notNull(defaultPageResponse, "response must not be null");

        if (defaultPageResponse.getItemArr() != null) {
            CloudDesktopDTO[] dtoArr = defaultPageResponse.getItemArr();
            return Arrays.stream(dtoArr).map(DesktopItem::new).toArray(DesktopItem[]::new);
        }
        return new DesktopItem[0];
    }
}
