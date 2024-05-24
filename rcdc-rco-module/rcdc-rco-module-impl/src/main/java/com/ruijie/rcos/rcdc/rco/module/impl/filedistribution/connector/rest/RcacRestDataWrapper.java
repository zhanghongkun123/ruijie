package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest;

import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.data.ConnectorDataWrapper;
import com.ruijie.rcos.sk.connectkit.api.data.InputData;
import com.ruijie.rcos.sk.connectkit.api.data.OutputData;
import com.ruijie.rcos.sk.connectkit.api.invocation.Result;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.Type;

/**
 * Description: RCAC REST接口数据包装器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/2 10:12
 *
 * @author zhangyichi
 */
@Service
public class RcacRestDataWrapper implements ConnectorDataWrapper {

    @Override
    public void wrapInvocation(InputData inputData, OutputData outputData) {
        Assert.notNull(inputData, "inputData cannot be null!");
        Assert.notNull(outputData, "outputData cannot be null!");

        Object content = inputData.getContent();
        outputData.putContent(content);
    }

    @Override
    public Object unwrapContent(InputData inputData) {
        Assert.notNull(inputData, "inputData cannot be null!");
        return inputData.getContent();
    }

    @Override
    public Type getRpcResponseType(Type type) {
        Assert.notNull(type, "type cannot be null!");
        return type;
    }

    @Override
    public Object wrapReturn(Result result) throws BusinessException {
        Assert.notNull(result, "result cannot be null!");
        return result.getValue();
    }
}

