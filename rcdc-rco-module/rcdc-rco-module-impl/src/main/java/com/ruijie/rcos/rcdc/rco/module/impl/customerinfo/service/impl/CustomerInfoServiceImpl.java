package com.ruijie.rcos.rcdc.rco.module.impl.customerinfo.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto.CustomerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto.PopupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.request.SubmitCustomerInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.customerinfo.service.CustomerInfoService;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CustomerInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CustomerInfoEntity;
import com.ruijie.rcos.sk.base.filesystem.common.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * <br>
 * Description: 客户信息 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/7 <br>
 *
 * @author xwx
 */
@Service
public class CustomerInfoServiceImpl implements CustomerInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerInfoServiceImpl.class);

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private CustomerInfoDAO customerInfoDAO;

    private static String pcaFilePath = "/data/bin/conf/pca.json";

    private static String hasUploadCustomerInfo = "has_upload_customer_info";

    private String pcaJsonContent;

    @Override
    public PopupDTO getNeedPopup() {
        PopupDTO popupDTO = new PopupDTO();
        Boolean isNeedPopup = !Boolean
                .parseBoolean(rcoGlobalParameterAPI.findParameter(new FindParameterRequest(hasUploadCustomerInfo)).getValue());
        popupDTO.setNeedPopup(isNeedPopup);

        return popupDTO;
    }


    @Override
    public String getPcaInfo() {
        if (pcaJsonContent == null) {
            File file = new File(pcaFilePath);
            try {
                pcaJsonContent = FileUtils.readFileToString(file, Charset.defaultCharset());
            } catch (Exception e) {
                LOGGER.error("读取文件失败", e);
            }

        }
        return pcaJsonContent;
    }

    @Override
    public void submitCustomerInfo(SubmitCustomerInfoRequest request) {
        Assert.notNull(request, "request cant be null");
        List<CustomerInfoEntity> customerList = customerInfoDAO.findAll();
        CustomerInfoEntity customerInfo;
        if (!customerList.isEmpty()) {
            customerInfo = customerInfoDAO.findAll().get(0);
        } else {
            customerInfo = new CustomerInfoEntity();
        }

        BeanUtils.copyProperties(request, customerInfo);
        customerInfoDAO.save(customerInfo);
        rcoGlobalParameterAPI.updateParameter(new UpdateParameterRequest(hasUploadCustomerInfo, Boolean.TRUE.toString()));
    }

    @Override
    public CustomerInfoDTO getCurrentCustomerInfo() {
        List<CustomerInfoEntity> customerList = customerInfoDAO.findAll();
        CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO();
        if (customerList.isEmpty()) {
            return customerInfoDTO;
        }
        BeanUtils.copyProperties(customerList.get(0), customerInfoDTO);
        return customerInfoDTO;
    }

}
