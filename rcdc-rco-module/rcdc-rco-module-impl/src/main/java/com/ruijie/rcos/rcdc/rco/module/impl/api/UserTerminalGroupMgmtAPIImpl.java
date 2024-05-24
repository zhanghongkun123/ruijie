package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVOIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVOIDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WifiWhitelistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvCreateTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvEditTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.IdvTerminalGroupDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.response.GetWifiWhitelistResponse;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DeskTopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.IdvTerminalGroupDeskConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.IdvTerminalGroupDeskConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.IdvTerminalGroupService;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.constant.SoftwareControlBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/26 11:40
 *
 * @author conghaifeng
 */
public class UserTerminalGroupMgmtAPIImpl implements UserTerminalGroupMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTerminalGroupMgmtAPIImpl.class);

    @Autowired
    private IdvTerminalGroupService idvTerminalGroupService;

    @Autowired
    private IdvTerminalGroupDeskConfigDAO idvTerminalGroupDeskConfigDAO;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;


    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private WifiWhitelistAPI wifiWhitelistAPI;

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    /**
     * @param request 创建idv|voi终端组请求
     * @throws BusinessException 业务异常
     * @description 创建idv|voi终端组
     */
    @Override
    public UUID createIdvTerminalGroup(IdvCreateTerminalGroupRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        DeskTopConfigDTO dto = new DeskTopConfigDTO();
        BeanUtils.copyProperties(request, dto);
        // 校验云桌面配置合法性
        checkGroupDesktopConfig(dto);
        return idvTerminalGroupService.saveIdvTerminalGroup(request);
    }

    /**
     * @param request 编辑idv|voi终端组请求
     * @throws BusinessException 业务异常
     * @description 编辑idv|voi终端组
     */
    @Override
    public void editIdvTerminalGroup(IdvEditTerminalGroupRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        DeskTopConfigDTO dto = new DeskTopConfigDTO();
        BeanUtils.copyProperties(request, dto);
        // 校验云桌面配置合法性
        checkGroupDesktopConfig(dto);
        idvTerminalGroupService.editIdvTerminalGroup(request);
    }

    private void checkGroupDesktopConfig(DeskTopConfigDTO dto) throws BusinessException {
        // IDV云桌面配置信息与VOI云桌面配置信息 都为空关闭，直接返回
        if (isConfigAllClosed(dto)) {
            return;
        }
        // IDV云桌面配置信息或者VOI云桌面配置信息 有填写 都必须填写完整
        // IDV云桌面配置信息不为空
        if (!isIdvConfigClosed(dto)) {
            // 进行校验 IDV镜像 与IDV策略都填写 传入VDI类型占位
            if (!isIdvConfigCompleted(dto)) {
                throw new BusinessException(
                        com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_TERMINAL_IDV_TERMINAL_GROUP_CONFIG_NOT_COMPLETED,
                        CbbTerminalPlatformEnums.IDV.name(), dto.getGroupName());
            }
            // idv镜像模板是否存在
            checkAndGetImageById(CbbTerminalPlatformEnums.IDV, dto.getCbbIdvDesktopImageId());
            // idv云桌面策略模板是否存在
            checkAndGetIdvStrategyById(CbbTerminalPlatformEnums.IDV.name(), dto.getCbbIdvDesktopStrategyId());
            // 检测IDV云桌面策略的系统盘是否大于镜像模板最小限制
            checkSystemDiskSize(dto);
            //检测IDV的软控策略
            checkSoftwareStrategy(dto.getIdvSoftwareStrategyId());

        }

        // VOI云桌面配置信息 不为空 进行校验
        if (!isVoiConfigClosed(dto)) {
            // 进行校验 VOI镜像 与VOI策略都填写  传入VOI类型占位
            if (!isVoiConfigCompleted(dto)) {
                throw new BusinessException(
                        com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_TERMINAL_IDV_TERMINAL_GROUP_CONFIG_NOT_COMPLETED,
                        CbbTerminalPlatformEnums.VOI.name(), dto.getGroupName());
            }
            // VOI镜像模板是否存在
            checkAndGetImageById(CbbTerminalPlatformEnums.VOI, dto.getCbbVoiDesktopImageId());
            // VOI云桌面策略模板是否存在
            checkAndGetVoiStrategyById(CbbTerminalPlatformEnums.VOI.name(), dto.getCbbVoiDesktopStrategyId());
            // 检测VOI云桌面策略的系统盘是否大于镜像模板最小限制
            checkVoiSystemDiskSize(dto);
            //检测VOI的软控策略
            checkSoftwareStrategy(dto.getVoiSoftwareStrategyId());
        }

    }


    /**
     * IDV云桌面配置信息与VOI云桌面配置信息 是否都为空
     * 
     * @param dto
     * @return
     */
    private boolean isConfigAllClosed(DeskTopConfigDTO dto) {
        // IDV镜像
        UUID imageId = dto.getCbbIdvDesktopImageId();
        // IDV云桌面策略
        UUID strategyId = dto.getCbbIdvDesktopStrategyId();
        // VOI镜像
        UUID voiImageId = dto.getCbbVoiDesktopImageId();
        // VOI云桌面策略
        UUID voiStrategyId = dto.getCbbVoiDesktopStrategyId();
        // IDV云桌面配置信息与VOI云桌面配置信息 是否都为空
        return strategyId == null && imageId == null && voiImageId == null && voiStrategyId == null;
    }

    /**
     * IDV云桌面配置信息都为空
     * 
     * @param dto
     * @return
     */
    private boolean isIdvConfigClosed(DeskTopConfigDTO dto) {
        // IDV镜像
        UUID imageId = dto.getCbbIdvDesktopImageId();
        // IDV云桌面策略
        UUID strategyId = dto.getCbbIdvDesktopStrategyId();
        // IDV云桌面配置信息是否都为空
        return strategyId == null && imageId == null;
    }

    /**
     * VOI云桌面配置信息 都为空，
     * 
     * @param dto
     * @return
     */
    private boolean isVoiConfigClosed(DeskTopConfigDTO dto) {
        // VOI镜像
        UUID imageId = dto.getCbbVoiDesktopImageId();
        // VOI云桌面策略
        UUID strategyId = dto.getCbbVoiDesktopStrategyId();
        // VOI云桌面配置信息是否都为空
        return strategyId == null && imageId == null;
    }

    /**
     * IDV云桌面配置信息不为空
     * 
     * @param dto
     * @return
     */
    private boolean isIdvConfigCompleted(DeskTopConfigDTO dto) {
        // IDV镜像
        UUID imageId = dto.getCbbIdvDesktopImageId();
        // IDV云桌面策略
        UUID strategyId = dto.getCbbIdvDesktopStrategyId();
        // IDV云桌面配置信息是否都为空
        return strategyId != null && imageId != null;
    }

    /**
     * VOI云桌面配置信息不为空
     * 
     * @param dto
     * @return
     */
    private boolean isVoiConfigCompleted(DeskTopConfigDTO dto) {
        // IDV镜像
        UUID imageId = dto.getCbbVoiDesktopImageId();
        // IDV云桌面策略
        UUID strategyId = dto.getCbbVoiDesktopStrategyId();
        // IDV云桌面配置信息是否都为空
        return strategyId != null && imageId != null;
    }

    /**
     * 检测IDV云桌面策略的系统盘是否大于镜像模板最小限制
     * 
     * @param dto
     * @throws BusinessException
     */
    private void checkSystemDiskSize(DeskTopConfigDTO dto) throws BusinessException {
        UUID imageId = dto.getCbbIdvDesktopImageId();
        CbbImageTemplateDetailDTO imageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        Integer imageSystemDisk = imageTemplateDetailDTO.getSystemDisk();

        UUID strategyId = dto.getCbbIdvDesktopStrategyId();
        CbbDeskStrategyIDVDTO deskStrategyIDVDTO = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(strategyId);
        Integer strategySystemDisk = deskStrategyIDVDTO.getSystemSize();

        if (Boolean.FALSE.equals(deskStrategyIDVDTO.getEnableFullSystemDisk()) && imageSystemDisk > strategySystemDisk) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_SYSTEM_DISK_SIZE_OUT_RANGE, deskStrategyIDVDTO.getName(),
                    imageTemplateDetailDTO.getImageName());
        }
    }

    /**
     * 检测VOI云桌面策略的系统盘是否大于镜像模板最小限制
     * 
     * @param dto
     * @throws BusinessException
     */
    private void checkVoiSystemDiskSize(DeskTopConfigDTO dto) throws BusinessException {
        //获取镜像信息
        UUID imageId = dto.getCbbVoiDesktopImageId();
        CbbImageTemplateDetailDTO imageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        Integer imageSystemDisk = imageTemplateDetailDTO.getSystemDisk();
        //获 桌面策略信息
        UUID strategyId = dto.getCbbVoiDesktopStrategyId();
        CbbDeskStrategyVOIDTO deskStrategyDTO = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(strategyId);
        Integer strategySystemDisk = deskStrategyDTO.getSystemSize();

        if (Boolean.FALSE.equals(deskStrategyDTO.getEnableFullSystemDisk()) && imageSystemDisk > strategySystemDisk) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_SYSTEM_DISK_SIZE_OUT_RANGE, deskStrategyDTO.getName(),
                    imageTemplateDetailDTO.getImageName());
        }
    }

    /**
     * @param id 删除idv|voi终端组请求
     * @throws BusinessException 业务异常
     * @description 删除idv|voi终端组
     */
    @Override
    public void deleteTerminalGroupDesktopConfig(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not be null");

        idvTerminalGroupService.deleteTerminalGroupDesktopConfig(id);
    }

    /**
     *
     * @param softwareStrategyId
     */
    private void checkSoftwareStrategy(UUID softwareStrategyId) throws BusinessException {
        if (softwareStrategyId != null) {
            softwareControlMgmtAPI.findSoftwareStrategyById(softwareStrategyId);
        }
    }

    /**
     * @param groupId 获取idv|voi终端组详细信息请求
     * @return IdvTerminalGroupDetailResponse idv|voi终端组详细信息
     * @throws BusinessException 业务异常
     * @description idv|voi终端组详细信息
     */
    @Override
    public IdvTerminalGroupDetailResponse idvTerminalGroupDetail(UUID groupId) throws BusinessException {
        Assert.notNull(groupId, "groupId can not be null");

        // 获取终端组详细信息
        CbbTerminalGroupDetailDTO terminalGroupDTO = cbbTerminalGroupMgmtAPI.loadById(groupId);

        return getIdvTerminalGroupDetailResponse(terminalGroupDTO);
    }

    @Override
    public Boolean hasImageBindTerminalGroup(UUID imageId) {
        Assert.notNull(imageId, "imageId can not be null");
        int count = idvTerminalGroupDeskConfigDAO.countByCbbIdvDesktopImageId(imageId);
        return count > 0;
    }

    private IdvTerminalGroupDetailResponse getIdvTerminalGroupDetailResponse(CbbTerminalGroupDetailDTO terminalGroupDTO) throws BusinessException {
        IdvTerminalGroupDetailResponse detailResponse = buildIdvTerminalGroupDetailResponse(terminalGroupDTO);
        // 获取根据桌面配置信息 应该是集合
        List<IdvTerminalGroupDeskConfigEntity> configList =
                idvTerminalGroupDeskConfigDAO.findTerminalGroupDeskConfigEntityByCbbTerminalGroupId(detailResponse.getId());

        for (int i = 0; i < configList.size(); i++) {
            IdvTerminalGroupDeskConfigEntity entity = configList.get(i);
            //桌面配置为空 跳过
            if ( entity == null) {
                continue;
            }
            // 桌面类型是IDV
            if (entity.getCbbIdvDesktopImageId() != null && CbbTerminalPlatformEnums.IDV == entity.getDeskType()) {
                //构造IDV响应参数
                buildIDVResponse( detailResponse,  entity);
            } else if ( entity.getCbbIdvDesktopImageId() != null && CbbTerminalPlatformEnums.VOI == entity.getDeskType()) {
                // 桌面类型是VOI 构造VOI 响应参数
                buildVOIResponse( detailResponse,  entity);
            }
        }
        // 无线策略
        GetWifiWhitelistResponse whitelistResp = wifiWhitelistAPI.getWifiWhitelistByTerminalGroupId(detailResponse.getId());
        WifiWhitelistConfigDTO wifiWhitelistConfigDTO = new WifiWhitelistConfigDTO();
        if (Objects.nonNull(whitelistResp)) {
            wifiWhitelistConfigDTO.setWifiWhiteList(whitelistResp.getWifiWhiteList());
        }
        detailResponse.setWifiWhitelistConfigDTO(wifiWhitelistConfigDTO);
        return detailResponse;
    }

    /**
     * 构造IDV响应返回参数
     * @param detailResponse
     * @param entity
     */
    private void buildIDVResponse(IdvTerminalGroupDetailResponse detailResponse, IdvTerminalGroupDeskConfigEntity entity) throws BusinessException {
        // 镜像
        String imageName = getImageName(entity.getCbbIdvDesktopImageId());
        detailResponse.setIdvDesktopImageName(imageName);
        detailResponse.setIdvDesktopImageId(entity.getCbbIdvDesktopImageId());
        // 桌面策略
        String strategyName = getStrategyName(entity.getCbbIdvDesktopStrategyId());
        detailResponse.setIdvDesktopStrategyName(strategyName);
        detailResponse.setIdvDesktopStrategyId(entity.getCbbIdvDesktopStrategyId());
        //软控策略
        detailResponse.setIdvSoftwareStrategyId(entity.getSoftwareStrategyId());
        if (entity.getSoftwareStrategyId() != null) {
            String softwareStrategyName = getSoftwareStrategyName(entity.getSoftwareStrategyId());
            detailResponse.setIdvSoftwareStrategyName(softwareStrategyName);
        }
        // 用户配置策略
        UUID userProfileStrategyId = entity.getUserProfileStrategyId();
        if (userProfileStrategyId != null) {
            String userProfileStrategyName = getUserProfileStrategyName(userProfileStrategyId);
            detailResponse.setIdvUserProfileStrategyId(userProfileStrategyId);
            detailResponse.setIdvUserProfileStrategyName(userProfileStrategyName);
        }
    }

    /**
     * 构造VOI响应返回参数
     * @param detailResponse
     * @param entity
     */
    private void buildVOIResponse(IdvTerminalGroupDetailResponse detailResponse, IdvTerminalGroupDeskConfigEntity entity) throws BusinessException {
        // VOI镜像
        String imageName = getImageName(entity.getCbbIdvDesktopImageId());
        detailResponse.setVoiDesktopImageName(imageName);
        detailResponse.setVoiDesktopImageId(entity.getCbbIdvDesktopImageId());
        // VOI桌面策略
        String strategyName = getVOIStrategyName(entity.getCbbIdvDesktopStrategyId());
        detailResponse.setVoiDesktopStrategyName(strategyName);
        detailResponse.setVoiDesktopStrategyId(entity.getCbbIdvDesktopStrategyId());
        //软控策略
        detailResponse.setVoiSoftwareStrategyId(entity.getSoftwareStrategyId());
        if (entity.getSoftwareStrategyId() != null) {
            String softwareStrategyName = getSoftwareStrategyName(entity.getSoftwareStrategyId());
            detailResponse.setVoiSoftwareStrategyName(softwareStrategyName);
        }
        // 用户配置策略
        UUID userProfileStrategyId = entity.getUserProfileStrategyId();
        if (userProfileStrategyId != null) {
            String userProfileStrategyName = getUserProfileStrategyName(userProfileStrategyId);
            detailResponse.setVoiUserProfileStrategyId(userProfileStrategyId);
            detailResponse.setVoiUserProfileStrategyName(userProfileStrategyName);
        }
    }

    private IdvTerminalGroupDetailResponse buildIdvTerminalGroupDetailResponse(CbbTerminalGroupDetailDTO terminalGroupDTO) {
        IdvTerminalGroupDetailResponse detailResponse = new IdvTerminalGroupDetailResponse();
        detailResponse.setId(terminalGroupDTO.getId());
        detailResponse.setName(terminalGroupDTO.getGroupName());
        detailResponse.setParentId(terminalGroupDTO.getParentGroupId());
        detailResponse.setParentName(terminalGroupDTO.getParentGroupName());
        return detailResponse;
    }

    /**
     * 镜像
     * 
     * @param imageId
     * @return
     */
    private String getImageName(UUID imageId) {
        CbbImageTemplateDetailDTO dto = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        return dto.getImageName();
    }

    /**
     * 查询用户配置策略
     *
     * @param strategyId 策略ID
     * @return 策略名称
     * @throws BusinessException 业务异常
     */
    private String getUserProfileStrategyName(UUID strategyId) throws BusinessException {
        UserProfileStrategyDTO userProfileStrategyDTO = userProfileMgmtAPI.findUserProfileStrategyById(strategyId);
        return userProfileStrategyDTO.getName();
    }

    /**
     * 策略
     * 
     * @param strategyId
     * @return
     * @throws BusinessException
     */
    private String getStrategyName(UUID strategyId) throws BusinessException {
        CbbDeskStrategyIDVDTO dto = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(strategyId);
        return dto.getName();
    }

    /**
     * 软件策略名
     * @param softwareStrategyId
     * @return
     * @throws BusinessException
     */
    private String getSoftwareStrategyName(UUID softwareStrategyId) {
        String strategyName = null;
        try {
            SoftwareStrategyDTO softwareStrategyDTO = softwareControlMgmtAPI.findSoftwareStrategyById(softwareStrategyId);
            strategyName = softwareStrategyDTO.getName();
        } catch (BusinessException e) {
            LOGGER.error(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_NOT_EXIST, softwareStrategyId);
        }
        return strategyName;
    }

    /**
     * VOI策略
     *
     * @param strategyId
     * @return
     * @throws BusinessException
     */
    private String getVOIStrategyName(UUID strategyId) throws BusinessException {
        CbbDeskStrategyVOIDTO dto = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(strategyId);
        return dto.getName();
    }

    /**
     * 检查IDV 策略是否存在
     * 
     * @param type
     * @param id
     * @return
     * @throws BusinessException
     */
    private CbbDeskStrategyIDVDTO checkAndGetIdvStrategyById(String type, UUID id) throws BusinessException {
        // 针对IDV 查询验证
        Assert.notNull(id, "strategyId can not be null");
        CbbDeskStrategyIDVDTO dto = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(id);
        if (dto == null) {
            // 提示语小写
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IDV_STRATEGY_NOT_FOUND, type.toLowerCase(), id.toString());
        }
        return dto;
    }

    /**
     * 检查VOI策略是否存在
     * 
     * @param type
     * @param id
     * @return
     * @throws BusinessException
     */
    private CbbDeskStrategyVOIDTO checkAndGetVoiStrategyById(String type, UUID id) throws BusinessException {
        // 针对VOI的查询验证
        Assert.notNull(id, "strategyId can not be null");
        CbbDeskStrategyVOIDTO dto = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(id);
        if (dto == null) {
            // 提示语小写
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IDV_STRATEGY_NOT_FOUND, type.toLowerCase(), id.toString());
        }
        return dto;
    }

    /**
     * 校验镜像模板是否存在
     * 
     * @param type
     * @param id
     * @return
     * @throws BusinessException
     */
    private CbbImageTemplateDetailDTO checkAndGetImageById(CbbTerminalPlatformEnums type, UUID id) throws BusinessException {
        // 针对IDV VOI的区别查询验证
        Assert.notNull(id, "imageId can not be null");
        CbbImageTemplateDetailDTO dto = cbbImageTemplateMgmtAPI.getImageTemplateDetail(id);
        // 对象为空 抛出异常 传入IDV类型 占位 异常
        if (dto == null) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_CLOUDDESKTOP_IDV_IMAGE_TEMPLATE_NOT_FOUND,
                    type.name().toLowerCase(), id.toString());

        }
        // 类型不对 抛出异常 传入具VOI类型 占位 异常
        if (!type.name().equals(dto.getCbbImageType().name())) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_CLOUDDESKTOP_IDV_IMAGE_TEMPLATE_NOT_FOUND,
                    type.name().toLowerCase(), id.toString());

        }
        return dto;
    }

}
