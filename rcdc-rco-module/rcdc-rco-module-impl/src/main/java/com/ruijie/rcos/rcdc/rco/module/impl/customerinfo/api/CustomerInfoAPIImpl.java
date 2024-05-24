package com.ruijie.rcos.rcdc.rco.module.impl.customerinfo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.CustomerInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto.CustomerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto.PopupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.request.SubmitCustomerInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.customerinfo.service.CustomerInfoService;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * <br>
 * Description: 客户信息 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/7 <br>
 *
 * @author xwx
 */
public class CustomerInfoAPIImpl implements CustomerInfoAPI {

    @Autowired
    private CustomerInfoService customerInfoService;

    @Override
    public PopupDTO getNeedPopup() {
        return customerInfoService.getNeedPopup();
    }


    @Override
    public String getPcaInfo() {
        return customerInfoService.getPcaInfo();
    }

    @Override
    public void submitCustomerInfo(SubmitCustomerInfoRequest request) throws BusinessException {
        Assert.notNull(request, "request cant be null");
        customerInfoService.submitCustomerInfo(request);
    }

    @Override
    public CustomerInfoDTO getCurrentCustomerInfo() {
        return customerInfoService.getCurrentCustomerInfo();
    }
}
