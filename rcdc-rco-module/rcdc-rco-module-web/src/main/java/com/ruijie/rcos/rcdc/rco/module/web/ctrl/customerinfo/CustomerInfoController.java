package com.ruijie.rcos.rcdc.rco.module.web.ctrl.customerinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CustomerInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto.PopupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.request.SubmitCustomerInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * <br>
 * Description:客户信息 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/7 <br>
 *
 * @author xwx
 */
@Controller
@RequestMapping("/rco/customer")
public class CustomerInfoController {

    @Autowired
    private CustomerInfoAPI customerInfoAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerInfoController.class);

    /**
     * 是否需要弹窗
     *
     * @return 是否需要弹窗
     */
    @RequestMapping(value = "/popup")
    public DefaultWebResponse getNeedPopup() {
        PopupDTO popupDTO = customerInfoAPI.getNeedPopup();
        return DefaultWebResponse.Builder.success(popupDTO);
    }

    /**
     * 获取省市区信息
     *
     * @return 省市区信息
     */
    @RequestMapping(value = "/pcaInfo")
    public DefaultWebResponse getPcaInfo() {
        String pcaInfo = customerInfoAPI.getPcaInfo();
        return DefaultWebResponse.Builder.success(pcaInfo);
    }


    /**
     * 提交客户信息
     *
     * @param request 客户信息
     * @return 提交结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/customerInfo")
    public DefaultWebResponse submitCustomerInfo(SubmitCustomerInfoRequest request) throws BusinessException {
        Assert.notNull(request, "request cant be null");
        try {
            customerInfoAPI.submitCustomerInfo(request);
            auditLogAPI.recordLog(CustomerBusinessKey.RCDC_RCO_EDIT_CUSTOMER_SUCCESS, request.getCustomerName());
            // 返回操作成功
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException ex) {
            // 返回操作失败
            auditLogAPI.recordLog(CustomerBusinessKey.RCDC_RCO_EDIT_CUSTOMER_FAIL, ex, request.getCustomerName(), ex.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, ex, ex.getI18nMessage());
        }
    }

    /**
     * 获取当前客户信息
     *
     * @return 当前客户信息
     */
    @RequestMapping(value = "/detail")
    public DefaultWebResponse getCurrentCustomerInfo() {

        return DefaultWebResponse.Builder.success(customerInfoAPI.getCurrentCustomerInfo());
    }

}
