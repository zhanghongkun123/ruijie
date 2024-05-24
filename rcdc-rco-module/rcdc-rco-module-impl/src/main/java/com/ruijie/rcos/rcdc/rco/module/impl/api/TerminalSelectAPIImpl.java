package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDriverDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryImageTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalDriverConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalSelectAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ProductDriverDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.TerminalSelectPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.terminaldriver.response.TerminalDriverConfigResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalModelAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalModelDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 终端镜像选取接口实现类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/4
 *
 * @author songxiang
 */
public class TerminalSelectAPIImpl implements TerminalSelectAPI {

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbTerminalModelAPI cbbTerminalModelAPI;

    @Autowired
    private TerminalDriverConfigAPI terminalDriverConfigAPI;
    
    private static final String SELF_DEVELOP_TERMINAL_PRODUCT_ID = "800";

    private static final String SELF_DEVELOP_TERMINAL_PRODUCT_TYPE = "RG-";

    @Override
    public ProductDriverDTO[] listSortedTerminalModel(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId must be not null");

        List<CbbQueryImageTypeDTO> cbbQueryImageTypeDTOList = cbbImageTemplateMgmtAPI.queryImageType(imageId);

        if (CollectionUtils.isEmpty(cbbQueryImageTypeDTOList)) {
            throw new BusinessException(BusinessKey.RCDC_IMAGE_TEMPLATE_NOT_EXIST);
        }

        CbbTerminalModelDTO[] terminalModelArr =
                cbbTerminalModelAPI.listTerminalModel(getPlatformByImageType(cbbQueryImageTypeDTOList.get(0).getCbbImageType()));

        if (cbbQueryImageTypeDTOList.get(0).getCbbImageType() == CbbImageType.VOI) {
            // TCI镜像驱动安装， 终端型号查询， 非自研的统一归并为其他终端
            terminalModelArr = Arrays.stream(terminalModelArr).filter(item -> item.getProductId().startsWith(SELF_DEVELOP_TERMINAL_PRODUCT_ID)
                    || item.getProductModel().startsWith(SELF_DEVELOP_TERMINAL_PRODUCT_TYPE)).toArray(CbbTerminalModelDTO[]::new);
        }
        
        // 获取该镜像模板已安装的驱动:
        CbbImageTemplateDriverDTO[] driverInstallDTOArr = cbbImageTemplateMgmtAPI.findImageTemplateDriverInfos(imageId);
        return buildProductDriverDTO(terminalModelArr, driverInstallDTOArr);
    }

    private ProductDriverDTO[] buildProductDriverDTO(CbbTerminalModelDTO[] terminalModelDTOArr, CbbImageTemplateDriverDTO[] driverInstallDTOArr) {
        return Stream.of(terminalModelDTOArr).map(terminalModelDTO -> {
            ProductDriverDTO productDriverDTO = new ProductDriverDTO();
            BeanUtils.copyProperties(terminalModelDTO, productDriverDTO);
            String driverType = terminalModelDTO.getCpuType();
            long count = Stream.of(driverInstallDTOArr).filter(driverInstallDTO -> driverInstallDTO.getDriverType().equals(driverType)).count();
            productDriverDTO.setInstall(count > 0);
            return productDriverDTO;
        }).sorted(Comparator.comparing(ProductDriverDTO::getInstall, Comparator.reverseOrder())).toArray(ProductDriverDTO[]::new);
    }

    @Override
    public DefaultPageResponse<TerminalDTO> listSelectableTerminal(TerminalSelectPageSearchRequest request) {
        Assert.notNull(request, "request must be not null");
        // 获取所有与productId的IDV终端
        return userTerminalMgmtAPI.pageQuery(request);
    }

    @Override
    public ProductDriverDTO[] listSortedHardwareVersion(UUID imageId, String productModel) throws BusinessException {
        Assert.notNull(imageId, "imageId must be not null");
        Assert.notNull(productModel, "productModel must be not null");

        List<CbbQueryImageTypeDTO> cbbQueryImageTypeDTOList = cbbImageTemplateMgmtAPI.queryImageType(imageId);
        if (CollectionUtils.isEmpty(cbbQueryImageTypeDTOList)) {
            throw new BusinessException(BusinessKey.RCDC_IMAGE_TEMPLATE_NOT_EXIST);
        }

        CbbImageType imageType = cbbQueryImageTypeDTOList.get(0).getCbbImageType();
        if (CbbImageType.IDV != imageType) {
            return new ProductDriverDTO[0];
        }

        CbbTerminalModelDTO[] terminalModelArr =
                cbbTerminalModelAPI.listHardwareVersion(getPlatformByImageType(imageType), productModel);
        CbbImageTemplateDriverDTO[] driverInstallDTOArr = cbbImageTemplateMgmtAPI.findImageTemplateDriverInfos(imageId);

        // 轮训每个终端型号硬件版本对应的驱动是否安装
        return Stream.of(terminalModelArr).map(terminalModelDTO -> {
            ProductDriverDTO productDriverDTO = new ProductDriverDTO();
            BeanUtils.copyProperties(terminalModelDTO, productDriverDTO);
            String driverType = Objects.nonNull(terminalModelDTO.getCpuType()) ? terminalModelDTO.getCpuType() : "";
            String hardwareVersion = Objects.nonNull(terminalModelDTO.getHardwareVersion()) ? terminalModelDTO.getHardwareVersion() : "";
            long count = Stream.of(driverInstallDTOArr).filter(driverInstallDTO -> Objects.nonNull(driverInstallDTO.getHardwareVersion()))
                    .filter(driverInstallDTO -> driverInstallDTO.getDriverType().equals(driverType)
                    && driverInstallDTO.getHardwareVersion().equals(hardwareVersion)).count();
            productDriverDTO.setInstall(count > 0);
            return productDriverDTO;
        }).sorted(Comparator.comparing(ProductDriverDTO::getInstall, Comparator.reverseOrder())).toArray(ProductDriverDTO[]::new);
    }

    private CbbTerminalPlatformEnums[] getPlatformByImageType(CbbImageType cbbImageType) throws BusinessException {
        switch (cbbImageType) {
            case VOI:
                return new CbbTerminalPlatformEnums[] {CbbTerminalPlatformEnums.VOI, CbbTerminalPlatformEnums.IDV};
            case IDV:
                return new CbbTerminalPlatformEnums[] {CbbTerminalPlatformEnums.IDV};
            default:
                throw new BusinessException(BusinessKey.RCDC_RCO_NOT_FIND_PLATFORM_BY_IMAGE_TYPE);
        }

    }
}
