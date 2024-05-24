package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.DeskFaultInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CbbDeskFaultInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CbbDeskFaultInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DeskFaultInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DeskFaultInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DeskFaultInfoService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/4 18:54
 *
 * @author ketb
 */
public class DeskFaultInfoAPIImpl implements DeskFaultInfoAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskFaultInfoAPIImpl.class);

    @Autowired
    private DeskFaultInfoService deskFaultInfoService;

    @Autowired
    private DeskFaultInfoDAO deskFaultInfoDAO;

    @Override
    public CbbDeskFaultInfoResponse findFaultInfoByMac(String mac) {
        Assert.hasText(mac, "mac can not be null");
        DeskFaultInfoEntity deskFaultInfoEntity = deskFaultInfoDAO.findByMac(mac);
        CbbDeskFaultInfoResponse cbbDeskFaultInfoResponse = new CbbDeskFaultInfoResponse();
        if (deskFaultInfoEntity == null) {
            return cbbDeskFaultInfoResponse;
        }
        CbbDeskFaultInfoDTO cbbDeskFaultInfoDTO = new CbbDeskFaultInfoDTO();
        BeanUtils.copyProperties(deskFaultInfoEntity, cbbDeskFaultInfoDTO);
        cbbDeskFaultInfoResponse.setCbbDeskFaultInfoDTO(cbbDeskFaultInfoDTO);
        return cbbDeskFaultInfoResponse;
    }

    @Override
    public CbbDeskFaultInfoResponse findFaultInfoByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");
        DeskFaultInfoEntity deskFaultInfoEntity = deskFaultInfoDAO.findByDeskId(deskId);
        CbbDeskFaultInfoResponse cbbDeskFaultInfoResponse = new CbbDeskFaultInfoResponse();
        if (deskFaultInfoEntity == null) {
            return cbbDeskFaultInfoResponse;
        }
        CbbDeskFaultInfoDTO cbbDeskFaultInfoDTO = new CbbDeskFaultInfoDTO();
        BeanUtils.copyProperties(deskFaultInfoEntity, cbbDeskFaultInfoDTO);
        cbbDeskFaultInfoResponse.setCbbDeskFaultInfoDTO(cbbDeskFaultInfoDTO);
        return cbbDeskFaultInfoResponse;
    }

    @Override
    public DefaultResponse save(CbbDeskFaultInfoDTO cbbDeskFaultInfoDTO) {
        Assert.notNull(cbbDeskFaultInfoDTO, "request can not be null");
        DeskFaultInfoEntity deskFaultInfoEntity = deskFaultInfoDAO.findByDeskId(cbbDeskFaultInfoDTO.getDeskId());
        if (deskFaultInfoEntity == null) {
            deskFaultInfoEntity = new DeskFaultInfoEntity();
        }
        deskFaultInfoEntity.setDeskId(cbbDeskFaultInfoDTO.getDeskId());
        deskFaultInfoEntity.setMac(cbbDeskFaultInfoDTO.getMac());
        deskFaultInfoEntity.setCreateTime(cbbDeskFaultInfoDTO.getCreateTime());
        deskFaultInfoEntity.setFaultDescription(cbbDeskFaultInfoDTO.getFaultDescription());
        deskFaultInfoEntity.setFaultState(cbbDeskFaultInfoDTO.getFaultState());
        deskFaultInfoEntity.setFaultTime(cbbDeskFaultInfoDTO.getFaultTime());
        deskFaultInfoDAO.save(deskFaultInfoEntity);
        return DefaultResponse.Builder.success();
    }

    @Override
    public DefaultResponse relieveFaultForCloudDesk(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");
        deskFaultInfoService.relieveFault(deskId.toString());
        LOGGER.info("relieve desk fault [{}]", deskId);
        return DefaultResponse.Builder.success();
    }

    @Override
    public DefaultPageResponse<CbbDeskFaultInfoDTO> assemblyInfo(UUID[] uuidArr) {
        Assert.notNull(uuidArr, "uuidArr can not be null");
        CbbDeskFaultInfoDTO[] deskArr = deskFaultInfoService.assemblyInfo(uuidArr);
        DefaultPageResponse<CbbDeskFaultInfoDTO> response = new DefaultPageResponse();
        response.setItemArr(deskArr);
        return response;
    }
}
