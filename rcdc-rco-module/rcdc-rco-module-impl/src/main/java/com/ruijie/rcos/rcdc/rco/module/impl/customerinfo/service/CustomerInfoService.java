package com.ruijie.rcos.rcdc.rco.module.impl.customerinfo.service;

import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto.CustomerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto.PopupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.request.SubmitCustomerInfoRequest;

/**
 * <br>
 * Description:客户信息 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/7 <br>
 *
 * @author xwx
 */
public interface CustomerInfoService {
    /**
     * 是否需要弹窗
     * @return 是否需要弹窗
     */
    PopupDTO getNeedPopup();

    /**
     * 获取省市区信息
     * @return 省市区信息
     */
    String getPcaInfo();

    /**
     * 提交客户信息
     * @param request 客户信息
     */
    void submitCustomerInfo(SubmitCustomerInfoRequest request);

    /**
     * 获取当前客户信息
     * @return 当前客户信息
     */
    CustomerInfoDTO getCurrentCustomerInfo();
}
