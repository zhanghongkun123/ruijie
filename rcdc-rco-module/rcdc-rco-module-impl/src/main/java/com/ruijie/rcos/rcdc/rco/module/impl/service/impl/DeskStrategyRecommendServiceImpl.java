package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBTypeMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetAllUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbClipBoardSupportTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskStrategyRecommendDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskStrategyRecommendQueryRequest;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DeskStrategyRecommendDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DeskStrategyRecommendEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DeskStrategyRecommendService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/4/3 <br>
 *
 * @author yyz
 */
@Service
public class DeskStrategyRecommendServiceImpl implements DeskStrategyRecommendService {

    @Autowired
    private DeskStrategyRecommendDAO deskStrategyRecommendDAO;

    @Autowired
    private CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Override
    public DefaultPageResponse<DeskStrategyRecommendDTO> pageQuery(DeskStrategyRecommendQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        Page<DeskStrategyRecommendEntity> page = deskStrategyRecommendDAO.findAll(PageRequest.of(request.getPage(), request.getLimit()));
        UUID[] usbTypeAllIdArr = getUsbTypeAllIdArr();
        boolean isVDIModel = serverModelAPI.isVdiModel();
        DeskStrategyRecommendDTO[] deskStrategyRecommendDTOArr = page
                .getContent()//
                .stream()//
                .map(deskStrategyRecommendEntity -> {
                    DeskStrategyRecommendDTO deskStrategyRecommendDTO = buildDeskStrategyRecommendDTO(deskStrategyRecommendEntity);
                    deskStrategyRecommendDTO.setUsbTypeIdArr(usbTypeAllIdArr);
                    return deskStrategyRecommendDTO;
                }).filter(deskStrategyRecommendEntity ->
                    (isVDIModel || deskStrategyRecommendEntity.getStrategyType() != CbbStrategyType.VDI)
                ).toArray(DeskStrategyRecommendDTO[]::new);

        DefaultPageResponse<DeskStrategyRecommendDTO> pageResponse = new DefaultPageResponse<>();
        pageResponse.setItemArr(deskStrategyRecommendDTOArr);
        pageResponse.setTotal(page.getTotalElements());

        return pageResponse;
    }

    @Override
    public DefaultResponse deskStrategyRecommendEdit(UUID[] uuidArr) {
        Assert.notNull(uuidArr, "uuidArr is not null");

        List<DeskStrategyRecommendEntity> recommendEntityList = deskStrategyRecommendDAO.findAllById(Lists.newArrayList(uuidArr));
        recommendEntityList.forEach(value -> {
            int version = value.getVersion();
            deskStrategyRecommendDAO.updateIsShowById(false, version, value.getId());
        });
        return DefaultResponse.Builder.success();
    }

    @Override
    public DeskStrategyRecommendDTO deskStrategyRecommendDetail(UUID strategyRecommendId) throws BusinessException {
        Assert.notNull(strategyRecommendId, "strategyRecommendId is not null");
        DeskStrategyRecommendDTO deskStrategyRecommendDTO = new DeskStrategyRecommendDTO();
        DeskStrategyRecommendEntity deskStrategyRecommendEntity = deskStrategyRecommendDAO.getOne(strategyRecommendId);
        if (null != deskStrategyRecommendEntity) {
            deskStrategyRecommendDTO = buildDeskStrategyRecommendDTO(deskStrategyRecommendEntity);
            UUID[] usbTypeIdArr = getUsbTypeAllIdArr();
            deskStrategyRecommendDTO.setUsbTypeIdArr(usbTypeIdArr);
        }
        return deskStrategyRecommendDTO;
    }

    /**
     * buildDeskStrategyRecommendDTO
     * 
     * @param deskStrategyRecommendEntity deskStrategyRecommendEntity
     * @return DeskStrategyRecommendDTO DeskStrategyRecommendDTO
     */
    public DeskStrategyRecommendDTO buildDeskStrategyRecommendDTO(DeskStrategyRecommendEntity deskStrategyRecommendEntity) {
        Assert.notNull(deskStrategyRecommendEntity, "deskStrategyRecommendEntity is not null");

        DeskStrategyRecommendDTO deskStrategyRecommendDTO = new DeskStrategyRecommendDTO();
        deskStrategyRecommendDTO.setId(deskStrategyRecommendEntity.getId());
        deskStrategyRecommendDTO.setStrategyName(deskStrategyRecommendEntity.getName());
        deskStrategyRecommendDTO.setDesktopType(deskStrategyRecommendEntity.getPattern());
        deskStrategyRecommendDTO.setSessionType(deskStrategyRecommendEntity.getSessionType());
        if (deskStrategyRecommendEntity.getCloudDeskType() != CbbStrategyType.VDI) {
            deskStrategyRecommendDTO.setSystemDisk(deskStrategyRecommendEntity.getSystemSize());
        }
        deskStrategyRecommendDTO.setEnableInternet(deskStrategyRecommendEntity.getIsAllowInternet());
        deskStrategyRecommendDTO.setEnableUsbReadOnly(deskStrategyRecommendEntity.getIsOpenUsbReadOnly());
        deskStrategyRecommendDTO.setIsShow(deskStrategyRecommendEntity.getIsShow());
        deskStrategyRecommendDTO.setClipBoardMode(deskStrategyRecommendEntity.getClipBoardMode());
        CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr = StringUtils.isNotBlank(deskStrategyRecommendEntity.getClipBoardSupportType()) ?
                JSONObject.parseArray(deskStrategyRecommendEntity.getClipBoardSupportType(), CbbClipBoardSupportTypeDTO.class)
                        .toArray(new CbbClipBoardSupportTypeDTO[0]) : new CbbClipBoardSupportTypeDTO[] {};
        deskStrategyRecommendDTO.setClipBoardSupportTypeArr(clipBoardSupportTypeArr);
        deskStrategyRecommendDTO.setEnableDoubleScreen(deskStrategyRecommendEntity.getIsOpenDoubleScreen());
        deskStrategyRecommendDTO.setEnableAllowLocalDisk(deskStrategyRecommendEntity.getIsAllowLocalDisk());
        deskStrategyRecommendDTO.setStrategyType(deskStrategyRecommendEntity.getCloudDeskType());
        deskStrategyRecommendDTO.setEnableOpenDesktopRedirect(deskStrategyRecommendEntity.getIsOpenDesktopRedirect());
        deskStrategyRecommendDTO.setCloudDeskType(deskStrategyRecommendEntity.getCloudDeskType().name());
        DiskMappingEnum diskMappingEnum =
                getDiskMappingEnum(deskStrategyRecommendEntity.getEnableDiskMapping(), deskStrategyRecommendEntity.getEnableDiskMappingWriteable());
        deskStrategyRecommendDTO.setDiskMappingType(diskMappingEnum);
        deskStrategyRecommendDTO.setEnableLanAutoDetection(deskStrategyRecommendEntity.getEnableLanAutoDetection());
        deskStrategyRecommendDTO.setUsbStorageDeviceMappingMode(deskStrategyRecommendEntity.getUsbStorageDeviceMappingMode());
        deskStrategyRecommendDTO.setEstProtocolType(deskStrategyRecommendEntity.getEstProtocolType());
        deskStrategyRecommendDTO.setAgreementInfo(StringUtils.isNotBlank(deskStrategyRecommendEntity.getAgreementInfo()) ?
                JSON.parseObject(deskStrategyRecommendEntity.getAgreementInfo(), AgreementDTO.class) : null);
        deskStrategyRecommendDTO.setEnableTransparentEncrypt(deskStrategyRecommendEntity.getEnableTransparentEncrypt());
        return deskStrategyRecommendDTO;
    }

    private DiskMappingEnum getDiskMappingEnum(Boolean enableDiskMapping, Boolean enableDiskMappingWriteable) {
        if (Boolean.TRUE.equals(enableDiskMapping)) {
            // 若开启磁盘映射，并且可读写
            if (Boolean.TRUE.equals(enableDiskMappingWriteable)) {
                return DiskMappingEnum.READ_WRITE;
            } else {
                // 开启磁盘映射，并且只读
                return DiskMappingEnum.READ_ONLY;
            }
        }
        return DiskMappingEnum.CLOSED;
    }

    private UUID[] getUsbTypeAllIdArr() throws BusinessException {
        CbbUSBTypeDTO[] cbbUSBTypeDTOArr = cbbUSBTypeMgmtAPI.getAllUSBType(new CbbGetAllUSBTypeDTO());
        UUID[] usbTypeIdArr = new UUID[cbbUSBTypeDTOArr.length];
        for (int i = 0; i < cbbUSBTypeDTOArr.length; i++) {
            usbTypeIdArr[i] = cbbUSBTypeDTOArr[i].getId();
        }
        return usbTypeIdArr;
    }

}
