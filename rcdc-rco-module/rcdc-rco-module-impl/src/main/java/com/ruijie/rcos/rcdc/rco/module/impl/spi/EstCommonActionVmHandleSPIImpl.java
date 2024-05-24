package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rco.module.impl.est.EstCommonActionDispatchService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * EST跟RCDC透传接口 handle处理类
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年3月31日
 *
 * @author lihengjing
 */
@DispatcherImplemetion(ShineAction.RCDC_TRANSPARENT_EST_COMMON_ACTION)
public class EstCommonActionVmHandleSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstCommonActionVmHandleSPIImpl.class);

    @Autowired
    private EstCommonActionDispatchService estCommonActionDispatchService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");

        LOGGER.info("接收到EST终端透传的消息，请求参数为:{}", JSON.toJSONString(request));
        CbbDispatcherDTO dispatcherDTO = new CbbDispatcherDTO();
        BeanUtils.copyProperties(request, dispatcherDTO);

        estCommonActionDispatchService.dispatchCommonAction(dispatcherDTO);
    }

}
