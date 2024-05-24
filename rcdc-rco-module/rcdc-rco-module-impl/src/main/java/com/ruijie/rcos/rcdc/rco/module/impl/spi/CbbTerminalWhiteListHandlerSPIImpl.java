package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskStartLicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.enums.TerminalWhiteTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.SystemBusinessMappingDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AuthorizationTerminalWhiteSnService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalWhiteListHandlerSPI;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 终端白名单SPI接口实现类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/7 18:07
 *
 * @author conghaifeng
 */
public class CbbTerminalWhiteListHandlerSPIImpl implements CbbTerminalWhiteListHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbTerminalWhiteListHandlerSPIImpl.class);

    @Autowired
    private AuthorizationTerminalWhiteSnService authorizationWhiteListTerminalService;

    @Autowired
    private CbbDeskStartLicenseAPI cbbDeskStartLicenseAPI;

    @Autowired
    private SystemBusinessMappingDAO systemBusinessMappingDAO;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;

    /**
     * 检验终端是否在白名单中
     *
     * @param terminalBasicInfo 终端信息
     * @return boolean 是否在终端白名单中
     */
    @Override
    public boolean checkWhiteList(CbbShineTerminalBasicInfo terminalBasicInfo) {
        Assert.notNull(terminalBasicInfo, "terminalBasicInfo can not be null");
        String productType = terminalBasicInfo.getProductType();
        if (StringUtils.isBlank(productType)) {
            return Boolean.FALSE;
        }
        return cbbTerminalConfigAPI.isValidateTerminal(productType);
    }

    /**
     * 获取产品类型白名单
     *
     * @return List<String>
     */
    @Override
    public List<String> getProductTypeWhiteList() {
        return Stream.of(TerminalWhiteTypeEnum.values()).map(TerminalWhiteTypeEnum::getTerminalType).collect(Collectors.toList());
    }


    @Override
    public boolean enableValidateTerminalSn() {
        return true;
    }

    @Override
    public boolean isExistFourUpgradeSix(String terminalId) {
        Assert.notNull(terminalId, "terminalId can not be null");
        return systemBusinessMappingDAO.existsBySrcId(terminalId);
    }

    @Override
    public boolean isSkipLicense(CbbShineTerminalBasicInfo terminalBasicInfo) {
        Assert.notNull(terminalBasicInfo, "terminalBasicInfo can not be null");
        // 终端可部署为瘦终端，也可部署为胖终端名单，此类终端在check_upgrade不进行授权，部署时再授权
        return cbbTerminalConfigAPI.hasTerminalSupportVdiAndIdv(terminalBasicInfo.getProductType());
    }
}
