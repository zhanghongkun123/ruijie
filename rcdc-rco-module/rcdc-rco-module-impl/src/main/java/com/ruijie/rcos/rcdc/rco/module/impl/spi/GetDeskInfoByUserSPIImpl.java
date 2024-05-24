package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rca.module.def.dto.UserDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.ViewUserDesktopDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaClientMode;
import com.ruijie.rcos.rcdc.rca.module.def.spi.GetDeskInfoByUserSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * Description: 获取桌面信息实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/2 15:22
 *
 * @author gaoxueyuan
 */
public class GetDeskInfoByUserSPIImpl implements GetDeskInfoByUserSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetDeskInfoByUserSPIImpl.class);

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Override
    public List<UserDesktopInfoDTO> getDeskInfoByUserId(UUID userId) throws BusinessException {
        Assert.notNull(userId, "userId不能为空");


        return Lists.newArrayList();
    }

    @Override
    public ViewUserDesktopDTO getUserDesktopByUserId(UUID userId) {
        Assert.notNull(userId, "userId cannot be null");


        return new ViewUserDesktopDTO();
    }

    @Override
    public List<ViewUserDesktopDTO> getUserAppVmByUserIdAndDeskType(UUID userId, CbbCloudDeskType deskType) {
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(deskType, "deskType cannot be null");

        return Lists.newArrayList();
    }

    @Override
    public ViewUserDesktopDTO getUserCLouDDesktopByUserId(UUID userId) {
        Assert.notNull(userId, "userId cannot be null");
        List<ViewUserDesktopEntity> viewUserDesktopEntityList =
                viewDesktopDetailDAO.findByUserIdAndIsDelete(userId, Boolean.FALSE);
        ViewUserDesktopDTO viewUserDesktopDTO = new ViewUserDesktopDTO();
        if (CollectionUtils.isEmpty(viewUserDesktopEntityList)) {
            return viewUserDesktopDTO;
        }
        return viewUserDesktopDTO;
    }

    @Override
    public ViewUserDesktopDTO getUserDesktopByDesktopId(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId cannot be null");

        return new ViewUserDesktopDTO();
    }

    @Override
    public List<ViewUserDesktopDTO> getUserDesktopByImageId(UUID imageId) {
        Assert.notNull(imageId, "imageId cannot be null");

        return Lists.newArrayList();
    }

    @Override
    public ViewUserDesktopDTO getDesktopByTerminalGroupId(UUID terminalGroupId, RcaClientMode rcaClientMode) {
        Assert.notNull(terminalGroupId, "terminalGroupId cannot be null");
        Assert.notNull(rcaClientMode, "rcaClientMode cannot be null");

        return new ViewUserDesktopDTO();
    }

    @Override
    public ViewUserDesktopDTO getCloudDockDesktopByTerminalGroupId(UUID terminalGroupId, RcaClientMode rcaClientMode) {
        Assert.notNull(terminalGroupId, "terminalGroupId cannot be null");
        Assert.notNull(rcaClientMode, "rcaClientMode cannot be null");


        return new ViewUserDesktopDTO();
    }

    @Override
    public ViewUserDesktopDTO getUserDesktopByTerminalId(String terminalId) {
        Assert.notNull(terminalId, "terminalId cannot be null");

        return new ViewUserDesktopDTO();
    }
}
