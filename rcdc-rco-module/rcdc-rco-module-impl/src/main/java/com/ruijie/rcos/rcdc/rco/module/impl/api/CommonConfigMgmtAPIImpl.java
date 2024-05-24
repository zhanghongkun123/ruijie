package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.CommonConfigMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CommonConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.EditCommonConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.BigScreenDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CommonConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CommonConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.CommonConfigTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Description: 大屏配置管理API实现
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/24 12:45
 *
 * @author zhangyichi
 */
public class CommonConfigMgmtAPIImpl implements CommonConfigMgmtAPI {

    @Autowired
    CommonConfigDAO commonConfigDAO;

    @Autowired
    CommonConfigTx commonConfigTx;

    @Override
    public BigScreenDetailResponse getConfigParam(DefaultRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        List<CommonConfigEntity> entityList = commonConfigDAO.findAll();
        if (CollectionUtils.isEmpty(entityList)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_BIGSCREEN_COMMON_CONFIG_NOT_FOUND);
        }

        List<CommonConfigDTO> dtoList = Lists.newArrayList();
        for (CommonConfigEntity entity : entityList) {
            CommonConfigDTO dto = new CommonConfigDTO();
            BeanUtils.copyProperties(entity, dto);
            dtoList.add(dto);
        }

        BigScreenDetailResponse response = new BigScreenDetailResponse();
        response.setConfigArr(dtoList.toArray(new CommonConfigDTO[]{}));
        return response;
    }

    @Override
    public DefaultResponse editConfigParam(EditCommonConfigRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        commonConfigTx.edit(request.getConfigArr());
        return DefaultResponse.Builder.success();
    }
}
