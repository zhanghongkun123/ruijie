package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DeskCreateMode;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaHostDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaHostDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.constants.DesktopPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaHostDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcaHostDesktopViewService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 派生应用主机云桌面管理实现类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月11日
 *
 * @author liuwc
 */
public class RcaHostDesktopMgmtAPIImpl implements RcaHostDesktopMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaHostDesktopMgmtAPIImpl.class);

    @Autowired
    private RcaHostDesktopViewService rcaHostDesktopViewService;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Override
    public DefaultPageResponse<RcaHostDesktopDTO> pageQuery(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Page<ViewRcaHostDesktopEntity> page = rcaHostDesktopViewService.pageQuery(request);

        return buildPageQueryResult(page);
    }

    @Override
    public int getMaxIndexNumWhenAddDesktop(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        List<RcaHostDTO> desktopInfoList = listNormalDeskInfoByDesktopPoolId(desktopPoolId);
        if (CollectionUtils.isEmpty(desktopInfoList)) {
            return 0;
        }
        int size = desktopInfoList.size();
        Integer max = desktopInfoList.stream().map(desktop -> {
            String name = desktop.getName();
            if (StringUtils.isEmpty(name)) {
                return size;
            }
            try {
                return Integer.parseInt(name.substring(name.lastIndexOf(DesktopPoolConstants.DESKTOP_NAME_SEPARATOR) + 1));
            } catch (Exception e) {
                LOGGER.error("getMaxIndexNumWhenAddDesktop转换数字异常", e);
                return desktopInfoList.size();
            }
        }).reduce(Integer::max).orElse(size);
        LOGGER.info("应用池[{}]中派生云主机云桌面名称最大后缀：{}", desktopPoolId, max);
        return max;
    }


    @Override
    public List<RcaHostDesktopDTO> listByHostIdIn(List<UUID> hostIdList) throws BusinessException {
        Assert.notEmpty(hostIdList, "hostIdList must not be empty");

        List<ViewRcaHostDesktopEntity> rcaHostDesktopEntityList = rcaHostDesktopViewService.listByHostIdIn(hostIdList);
        if (CollectionUtils.isEmpty(rcaHostDesktopEntityList)) {
            return new ArrayList<>();
        }

        List<RcaHostDesktopDTO> rcaHostDesktopDTOList = queryCloudDesktopService.convertRcaHostPageInfoList(rcaHostDesktopEntityList);
        for (RcaHostDesktopDTO cloudDesktopDTO : rcaHostDesktopDTOList) {
            // 设置镜像数据盘的信息
            setImageDiskInfoDTOList(cloudDesktopDTO);
        }
        return rcaHostDesktopDTOList;
    }

    private DefaultPageResponse<RcaHostDesktopDTO> buildPageQueryResult(Page<ViewRcaHostDesktopEntity> page) throws BusinessException {

        DefaultPageResponse<RcaHostDesktopDTO> response = queryCloudDesktopService.convertRcaHostPageInfoList(page);

        for (RcaHostDesktopDTO cloudDesktopDTO : response.getItemArr()) {
            // 设置镜像数据盘的信息
            setImageDiskInfoDTOList(cloudDesktopDTO);
        }
        return response;
    }

    /**
     * 设置镜像数据盘的信息（云桌面列表）
     *
     * @param cloudDesktopDTO 云桌面DTO
     * @throws BusinessException
     */
    private void setImageDiskInfoDTOList(RcaHostDesktopDTO cloudDesktopDTO) throws BusinessException {
        CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(cloudDesktopDTO.getDesktopCategory());
        DeskCreateMode deskCreateMode = DeskCreateMode.valueOf(cloudDesktopDTO.getDeskCreateMode());

        if (cloudDesktopDTO.getSystemDisk() == null) {
            return;
        }

        List<CbbImageDiskInfoDTO> imageDiskInfoList;
        try {
            imageDiskInfoList = cbbImageTemplateMgmtAPI.getPublishedImageDiskInfoList(cloudDesktopDTO.getImageId());
            cloudDesktopDTO.setImageDiskInfoDTOList(imageDiskInfoList);
        } catch (BusinessException e) {
            if (deskType == CbbCloudDeskType.VDI && deskCreateMode == DeskCreateMode.FULL_CLONE) {
                // 异常只可能在镜像模板不存在的时候抛出，当全量克隆的时候允许镜像模板不存在(不抛出异常)
                return;
            }
            throw e;
        }
    }

    List<RcaHostDTO> listNormalDeskInfoByDesktopPoolId(UUID appPoolId) {
        Assert.notNull(appPoolId, "appPoolId must not be null");
        List<ViewRcaHostDesktopEntity> rcaHostEntityList = rcaHostDesktopViewService.findAllByPoolId(appPoolId);
        rcaHostEntityList = rcaHostEntityList.stream().filter(this::checkNotNormalStateDesktop).collect(Collectors.toList());
        return convert2PoolDesktopInfoDTOList(rcaHostEntityList);
    }

    private boolean checkNotNormalStateDesktop(ViewRcaHostDesktopEntity rcaHost) {
        String state = rcaHost.getDeskState();
        try {
            CbbCloudDeskState cbbCloudDeskState = CbbCloudDeskState.valueOf(state);
            return Boolean.FALSE.equals(rcaHost.getIsDelete())
                    && cbbCloudDeskState != CbbCloudDeskState.RECYCLE_BIN
                    && cbbCloudDeskState != CbbCloudDeskState.DELETING
                    && cbbCloudDeskState != CbbCloudDeskState.CREATING
                    && cbbCloudDeskState != CbbCloudDeskState.COMPLETE_DELETING;
        } catch (Exception e) {
            LOGGER.error("派生桌面视图-云桌面状态转换失败，state={}", state, e);
            return false;

        }
    }

    private List<RcaHostDTO> convert2PoolDesktopInfoDTOList(List<ViewRcaHostDesktopEntity> hostDesktopEntityList) {
        if (org.springframework.util.CollectionUtils.isEmpty(hostDesktopEntityList)) {
            return Collections.emptyList();
        }
        return hostDesktopEntityList.stream().map(this::convert2RcaHostDTO).collect(Collectors.toList());
    }

    private RcaHostDTO convert2RcaHostDTO(ViewRcaHostDesktopEntity entity) {
        RcaHostDTO rcaHostDTO = new RcaHostDTO();
        BeanUtils.copyProperties(entity, rcaHostDTO);
        return rcaHostDTO;
    }

}
