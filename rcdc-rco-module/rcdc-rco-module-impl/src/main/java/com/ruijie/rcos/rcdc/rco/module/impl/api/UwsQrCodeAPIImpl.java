package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbConfirmQrCodeMobileReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbGetQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeMobileReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CbbQueryQrCodeReqDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.rcdc.rco.module.impl.uws.service.UwsQrCodeService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月16日
 * 2334
 * 
 * @author xgx
 */
public class UwsQrCodeAPIImpl implements UwsQrCodeAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(UwsQrCodeAPIImpl.class);

    @Autowired
    private List<UwsQrCodeService> qrCodeServiceList;

    @Override
    public CbbQrCodeDTO getQrCode(CbbGetQrCodeReqDTO cbbGetQrCodeReqDTO) throws BusinessException {
        Assert.notNull(cbbGetQrCodeReqDTO, "cbbGetQrCodeReqDTO can not be null");
        LOGGER.info("获取二维码二维码类型[{}]", cbbGetQrCodeReqDTO.getQrCodeType());
        return getTargetQrCodeService(cbbGetQrCodeReqDTO.getQrCodeType()).getQrCode(cbbGetQrCodeReqDTO);
    }



    @Override
    public CbbQrCodeDTO refreshQrCode(CbbQrCodeReqDTO oldQrCodeReqDTO) throws BusinessException {
        Assert.notNull(oldQrCodeReqDTO, "oldQrCodeReqDTO can not be null");

        LOGGER.info("刷新二维码[{}]二维码类型[{}]", oldQrCodeReqDTO.getQrCode(), oldQrCodeReqDTO.getQrCodeType());
        return getTargetQrCodeService(oldQrCodeReqDTO.getQrCodeType()).refreshQrCode(oldQrCodeReqDTO);
    }

    @Override
    public CbbQrCodeDTO queryQrCode(CbbQueryQrCodeReqDTO queryQrCodeReqDTO) throws BusinessException {
        Assert.notNull(queryQrCodeReqDTO, "queryQrCodeReqDTO can not be null");

        LOGGER.info("查询二维码[{}]信息，二维码类型[{}]", queryQrCodeReqDTO.getQrCode(), queryQrCodeReqDTO.getQrCodeType());
        return getTargetQrCodeService(queryQrCodeReqDTO.getQrCodeType()).queryQrCode(queryQrCodeReqDTO);
    }

    @Override
    public CbbQrCodeDTO qrLogin(CbbQrCodeReqDTO qrCodeReqDTO) throws BusinessException {
        Assert.notNull(qrCodeReqDTO, "qrCodeReqDTO can not be null");

        LOGGER.info("二维码登录，二维码[{}]，二维码类型[{}]", qrCodeReqDTO.getQrCode(), qrCodeReqDTO.getQrCodeType());
        return getTargetQrCodeService(qrCodeReqDTO.getQrCodeType()).qrLogin(qrCodeReqDTO);
    }

    @Override
    public void scanQrCode(CbbQrCodeMobileReqDTO qrCodeMobileReq) throws BusinessException {
        Assert.notNull(qrCodeMobileReq, "qrCodeMobileReq can not be null");

        LOGGER.info("扫描二维码[{}]", qrCodeMobileReq.getQrCode());
        getTargetQrCodeService(qrCodeMobileReq.getQrCodeType()).scanQrCode(qrCodeMobileReq);
    }

    @Override
    public void confirmQrLogin(CbbConfirmQrCodeMobileReqDTO confirmQrCodeMobileReqDTO) throws BusinessException {
        Assert.notNull(confirmQrCodeMobileReqDTO, "confirmQrCodeMobileReqDTO can not be null");

        LOGGER.info("确认扫码[{}]，二维码类型[{}]", confirmQrCodeMobileReqDTO.getQrCode(), confirmQrCodeMobileReqDTO.getQrCodeType());
        getTargetQrCodeService(confirmQrCodeMobileReqDTO.getQrCodeType()).confirmQrLogin(confirmQrCodeMobileReqDTO);
    }

    @Override
    public void cancelQrLogin(CbbQrCodeMobileReqDTO qrCodeMobileReq) throws BusinessException {
        Assert.notNull(qrCodeMobileReq, "qrCodeMobileReq can not be null");

        LOGGER.info("取消扫码[{}]，二维码类型[{}]", qrCodeMobileReq.getQrCode(), qrCodeMobileReq.getQrCodeType());
        getTargetQrCodeService(qrCodeMobileReq.getQrCodeType()).cancelQrLogin(qrCodeMobileReq);

    }

    @Override
    public void saveQrCodeConfig(CbbQrCodeConfigDTO qrCodeConfigReqDTO) throws BusinessException {
        Assert.notNull(qrCodeConfigReqDTO, "qrCodeConfigReqDTO can not be null");
        LOGGER.info("更新二维码配置，二维码类型[{}],二维码开关[{}]", qrCodeConfigReqDTO.getQrCodeType(), qrCodeConfigReqDTO.getOpenSwitch());
        getTargetQrCodeService(qrCodeConfigReqDTO.getQrCodeType()).saveQrCodeConfig(qrCodeConfigReqDTO);
    }

    @Override
    public CbbQrCodeConfigDTO getQrCodeConfig(CbbQrCodeType qrCodeType) throws BusinessException {
        Assert.notNull(qrCodeType, "qrCodeType can not be null");
        LOGGER.info("查询二维码配置， 二维码类型[{}]", qrCodeType);
        return getTargetQrCodeService(qrCodeType).getQrCodeConfig();
    }

    @Override
    public List<CbbQrCodeType> getSupportQrCodeTypeList() throws BusinessException {
        List<CbbQrCodeConfigDTO> qrCodeConfigDTOList = new ArrayList<>();
        for (UwsQrCodeService qrCodeService : qrCodeServiceList) {
            final CbbQrCodeConfigDTO qrCodeConfig = qrCodeService.getQrCodeConfig();
            qrCodeConfigDTOList.add(qrCodeConfig);
        }
        return qrCodeConfigDTOList.stream()
                .filter(item -> item.getOpenSwitch() == Boolean.TRUE) //
                .sorted(Comparator.comparingInt(CbbQrCodeConfigDTO::getOrder)) //
                .map(item -> item.getQrCodeType()).collect(Collectors.toList());
    }

    private UwsQrCodeService getTargetQrCodeService(CbbQrCodeType qrCodeType) throws BusinessException {
        return qrCodeServiceList.stream() //
                .filter(item -> item.isSupport(qrCodeType)) //
                .findFirst() //
                .orElseThrow(() -> new BusinessException(BusinessKey.RCDC_UWS_UN_SUPPORT_QR_CODE_TYPE, qrCodeType.toString()));
    }
}
