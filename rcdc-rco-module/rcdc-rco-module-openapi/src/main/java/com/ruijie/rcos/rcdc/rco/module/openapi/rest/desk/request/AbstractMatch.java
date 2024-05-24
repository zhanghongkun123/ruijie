package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import com.ruijie.rcos.sk.pagekit.api.Match;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
public abstract class AbstractMatch extends EqualsHashcodeSupport implements Match {
    private final Type type;

    AbstractMatch(Type type) {
        Assert.notNull(type, "type is not null");
        this.type = type;
    }

    public final Type getType() {
        return this.type;
    }
}
