package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.slave.prepare;

import com.ruijie.rcos.rcdc.rco.module.common.resolver.ContextDtoResolver;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.SlavePrepareImageSyncDTO;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 创建空白镜像上下文
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
@Service
public class PrepareStageContextResolver implements ContextDtoResolver<SlavePrepareImageSyncDTO> {

    private static final String IS_FIRST_CREATE = "is_first_create";

    /**
     * 设置是否第一次创建
     *
     * @param context       context
     * @param isFirstCreate isFirstCreate
     */
    public void setIsFirstCreateFlag(StateTaskHandle.StateProcessContext context, Boolean isFirstCreate) {
        Assert.notNull(context, "context can not be null ");
        Assert.notNull(isFirstCreate, "isFirst can not be null ");
        context.put(IS_FIRST_CREATE, isFirstCreate);
    }


    /**
     * 获取第一次创建标志
     *
     * @param context context
     * @return true or false
     */
    public boolean isFirstCreateFlag(StateTaskHandle.StateProcessContext context) {
        Assert.notNull(context, "context can not be null ");
        return BooleanUtils.isTrue(context.get(IS_FIRST_CREATE, Boolean.class));
    }

}
