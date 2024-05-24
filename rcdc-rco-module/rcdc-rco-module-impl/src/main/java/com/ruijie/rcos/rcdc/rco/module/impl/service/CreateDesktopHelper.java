package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainConfigDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVOIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbNetworkStrategyMode;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.VDIDesktopValidateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.ImageDeskSpecGpuCheckParamDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.CreatePoolDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.CreatingDesktopNumCache;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.UNDERLINE;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/19
 *
 * @author Jarman
 */
@Service
public class CreateDesktopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDesktopHelper.class);

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private IacAdMgmtAPI cbbAdMgmtAPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private CreatingDesktopNumCache creatingDesktopNumCache;

    /**
     * 检查用户创建的桌面数
     *
     * @param userId   userId
     * @param userType userType
     * @throws BusinessException BusinessException
     */
    public void checkCreatedDesktopNum(UUID userId, IacUserTypeEnum userType) throws BusinessException {
        Assert.notNull(userId, "userId cannot null");
        Assert.notNull(userType, "userType cannot null");
        long count = userService.countUserDesktopNumContainCreatingNum(userId);
        // 访客用户
        if (IacUserTypeEnum.VISITOR == userType && count >= Constants.VISITOR_USER_DESKTOP_MAX_NUM) {
            LOGGER.info("访客用户[{}]已创建[{}]个云桌面，不允许再创建", userId, count);
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_VISITOR_USER_VM_MAX,
                    String.valueOf(Constants.VISITOR_USER_DESKTOP_MAX_NUM));
        }
    }

    /**
     * 校验创建桌面参数
     *
     * @param request    request
     * @param userDetail userEntity
     * @throws BusinessException BusinessException
     */
    public void validateCreateDeskRequestParams(CreateCloudDesktopRequest request, IacUserDetailDTO userDetail) throws BusinessException {
        Assert.notNull(request, "request cannot null");
        Assert.notNull(userDetail, "userEntity cannot null");

        checkVisitorLimit(userDetail.getId(), userDetail.getUserType());

        // 检查规格
        checkDeskSpec(request.getDeskSpec());
        // 检查网络策略
        checkDesktopNetwork(request.getNetworkId(), userDetail);
        // 检查云桌面策略
        checkDesktopStrategy(request.getStrategyId(), request.getDeskSpec());
        // 检查镜像模板
        checkDesktopImage(request.getDesktopImageId());
        // 检查镜像系统是否支持策略配置的显卡
        checkDesktopImageSupportGpu(request.getDesktopImageId(), request.getClusterId(), request.getDeskSpec(), request.getStrategyId());
        // 校验VDI桌面策略选择
        VDIDesktopValidateDTO validateDTO = new VDIDesktopValidateDTO();
        validateDTO.setClusterId(request.getClusterId());
        validateDTO.setImageId(request.getDesktopImageId());
        validateDTO.setNetworkId(request.getNetworkId());
        validateDTO.setStrategyId(request.getStrategyId());
        validateDTO.setPlatformId(request.getPlatformId());
        validateDTO.setDeskSpec(request.getDeskSpec());
        clusterAPI.validateVDIDesktopConfig(validateDTO);
    }

    private void checkVisitorLimit(UUID userId, IacUserTypeEnum userType) throws BusinessException {
        long count = userDesktopDAO.countByUserCreatingDesktop(userId);
        // 访客用户
        if (IacUserTypeEnum.VISITOR == userType && count >= Constants.VISITOR_USER_DESKTOP_MAX_NUM) {
            LOGGER.info("访客用户[{}]已创建[{}]个云桌面，不允许再创建", userId, count);
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_VISITOR_USER_VM_MAX,
                    String.valueOf(Constants.VISITOR_USER_DESKTOP_MAX_NUM));
        }
    }

    /**
     * 验证桌面池创建桌面参数
     *
     * @param request 桌面池创建桌面request
     * @throws BusinessException 业务异常
     */
    public void validateCreateDeskRequestParams(CreatePoolDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot null");
        // 检查规格
        checkDeskSpec(request.getDeskSpec());
        // 检查网络策略
        checkDesktopNetwork(request.getNetworkId());
        // 检查云桌面策略
        checkDesktopStrategy(request.getStrategyId(), request.getDeskSpec());
        // 检查镜像模板
        checkDesktopImage(request.getImageTemplateId());

        // 检查镜像系统是否支持策略配置的显卡
        checkDesktopImageSupportGpu(request.getImageTemplateId(), request.getClusterId(), request.getDeskSpec(), request.getStrategyId());

        // 校验VDI桌面策略选择
        VDIDesktopValidateDTO validateDTO = new VDIDesktopValidateDTO();
        validateDTO.setClusterId(request.getClusterId());
        validateDTO.setImageId(request.getImageTemplateId());
        validateDTO.setNetworkId(request.getNetworkId());
        validateDTO.setStrategyId(request.getStrategyId());
        validateDTO.setPlatformId(request.getPlatformId());
        validateDTO.setDeskSpec(request.getDeskSpec());
        clusterAPI.validateVDIDesktopConfig(validateDTO);
    }

    /**
     * 校验创建IDV云桌面参数
     *
     * @param strategyId strategyId
     * @param imageId    imageId
     * @throws BusinessException BusinessException
     */
    public void validateCreateIDVDeskRequestParams(UUID strategyId, UUID imageId) throws BusinessException {
        Assert.notNull(strategyId, "strategyId cannot null");
        Assert.notNull(imageId, "imageId cannot null");

        // 检查云桌面策略
        checkIDVDesktopStrategy(strategyId);
        // 检查镜像模板
        checkDesktopImage(imageId);
    }

    /**
     * 校验创建VOI云桌面参数
     *
     * @param strategyId strategyId
     * @param imageId    imageId
     * @throws BusinessException BusinessException
     */
    public void validateCreateVOIDeskRequestParams(UUID strategyId, UUID imageId) throws BusinessException {
        Assert.notNull(strategyId, "strategyId cannot null");
        Assert.notNull(imageId, "imageId cannot null");

        // 检查VOI云桌面策略
        checkVOIDesktopStrategy(strategyId);
        // 检查镜像模板
        checkDesktopImage(imageId);
    }

    private void checkDeskSpec(CbbDeskSpecDTO deskSpec) throws BusinessException {
        int specPersonSize = Optional.ofNullable(deskSpec.getPersonSize()).orElse(0);
        if (specPersonSize == 0) {
            if (!CollectionUtils.isEmpty(deskSpec.getExtraDiskList())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_EXTRA_DISK_NO_PERSON_DISK);
            }
            if (Objects.nonNull(deskSpec.getPersonDiskStoragePoolId())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_NO_PERSON_DISK_NO_STORAGE);
            }
        }
    }

    private void checkDesktopNetwork(UUID networkId, IacUserDetailDTO userDetail) throws BusinessException {
        if (userDetail.getUserType() == IacUserTypeEnum.AD) {
            validateAdUserNetworkConfig(networkId);
        }
        CbbDeskNetworkDetailDTO deskNetworkDTO = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
        if (deskNetworkDTO == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_NOT_FOUND, networkId.toString());
        }
    }

    private void checkDesktopNetwork(UUID networkId) throws BusinessException {
        CbbDeskNetworkDetailDTO deskNetworkDTO = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
        if (deskNetworkDTO == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_NOT_FOUND, networkId.toString());
        }
    }

    private void validateAdUserNetworkConfig(UUID networkId) throws BusinessException {
        IacDomainConfigDetailDTO adConfig = cbbAdMgmtAPI.getAdConfig();
        if (ObjectUtils.isEmpty(adConfig)) {
            return;
        }
        if (!adConfig.getAutoJoin()) {
            return;
        }
        // 开启自动加域，需要校验dns是否为空
        CbbDeskNetworkDetailDTO deskNetworkDTO = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
        if (deskNetworkDTO == null) {
            return;
        }

        // DHCP模式不校验dns是否为空
        if (!CbbNetworkStrategyMode.DHCP.equals(deskNetworkDTO.getNetworkMode()) && StringUtils.isBlank(deskNetworkDTO.getDnsPrimary())) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_NETWORK_AD_AUTO_JOIN_MUST_HAS_DNS, deskNetworkDTO.getDeskNetworkName());
        }
    }

    private void checkDesktopStrategy(UUID strategyId, CbbDeskSpecDTO deskSpec) throws BusinessException {
        CbbDeskStrategyVDIDTO deskStrategyVDIDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(strategyId);
        if (deskStrategyVDIDTO == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_VDI_NOT_FOUND, strategyId.toString());
        }
        if (Objects.nonNull(deskSpec) && BooleanUtils.isTrue(deskStrategyVDIDTO.getOpenDesktopRedirect())
                && (Objects.isNull(deskSpec.getPersonSize()) || deskSpec.getPersonSize() <= 0)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CREATE_USER_CONFIG_VDI_DESK_REDIRECT_MUST_PERSON_DISK);
        }
    }

    /**
     * 检测IDV云桌面是否存在
     *
     * @param strategyId 策略ID
     * @throws BusinessException 业务异常
     */
    private void checkIDVDesktopStrategy(UUID strategyId) throws BusinessException {
        CbbDeskStrategyIDVDTO deskStrategyIDVDTO = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(strategyId);
        if (deskStrategyIDVDTO == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_IDV_NOT_FOUND, strategyId.toString());
        }
    }


    /**
     * 校验VOI云桌面策略
     *
     * @param strategyId 策略ID
     * @throws BusinessException 业务异常
     */
    private void checkVOIDesktopStrategy(UUID strategyId) throws BusinessException {
        //VOI 策略校验
        LOGGER.info("VOI 桌面策略ID校验:{}",strategyId);
        CbbDeskStrategyVOIDTO deskStrategyVOIDTO = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(strategyId);
        if (deskStrategyVOIDTO == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_VOI_NOT_FOUND, strategyId.toString());
        }
    }

    private void checkDesktopImage(UUID imageId) throws BusinessException {
        boolean isExit = cbbImageTemplateMgmtAPI.checkImageTemplateExist(imageId);
        if (!isExit) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_NOT_FOUND, imageId.toString());
        }
    }

    /**
     * 生成桌面名称
     *
     * @param userDetail userEntity
     * @return 桌面名
     */
    public String generateDesktopName(IacUserDetailDTO userDetail) {
        Assert.notNull(userDetail, "UserEntity cannot null");
        List<String> desktopNameList = viewDesktopDetailDAO.findDesktopNamesByUserId(userDetail.getId());
        String userName = userDetail.getUserName();
        if (CollectionUtils.isEmpty(desktopNameList)) {
            return userName;
        }
        String genName;
        int creatingDesktopNum = userService.getCreatingDesktopNum(userDetail.getId());
        // 第二个桌面序号从1开始
        int index = creatingDesktopNum + 1;
        while (true) {
            genName = userName + "_" + index;
            if (desktopNameList.contains(genName)) {
                index++;
            } else {
                break;
            }
        }
        return genName;
    }

    /**
     * 生成桌面名称
     *
     * @param userDetail userEntity
     * @return 桌面名
     */
    public String generateDesktopNameForCreating(IacUserDetailDTO userDetail) {
        Assert.notNull(userDetail, "UserEntity cannot null");

        List<String> desktopNameList = userDesktopDAO.findDesktopNameByUserId(userDetail.getId());

        return generateUserDesktopName(userDetail, desktopNameList);

    }

    private String generateUserDesktopName(IacUserDetailDTO userDetail, List<String> desktopNameList) {

        String userName = userDetail.getUserName();
        if (CollectionUtils.isEmpty(desktopNameList)) {
            return userName;
        }

        int index = 1;
        String userDesktopName;
        userDesktopName = userName + UNDERLINE + index;
        while (desktopNameList.contains(userDesktopName)) {
            userDesktopName = userName + UNDERLINE + index++;
        }

        return userDesktopName;
    }


    /**
     * 当用户桌面信息不存在时保存用户桌面信息（同步）
     *
     * @param userDesktopEntity userDesktopEntity
     * @return 如果已存在，则返回已存在的数据，如果不存在则返回插入的数据
     */
    public synchronized UserDesktopEntity saveUserDestopIfNotExistByCbbDesktopId(UserDesktopEntity userDesktopEntity) {
        Assert.notNull(userDesktopEntity, "userDesktopEntity不能为空");
        UserDesktopEntity entity = userDesktopDAO.findByCbbDesktopId(userDesktopEntity.getCbbDesktopId());
        if (entity != null) {
            LOGGER.warn("桌面信息已存在，无需再次添加，userDesktopEntity:{},entity:{}", JSON.toJSONString(userDesktopEntity), JSON.toJSONString(entity));
            return entity;
        }
        userDesktopDAO.saveAndFlush(userDesktopEntity);
        return userDesktopEntity;
    }

    private void checkDesktopImageSupportGpu(UUID imageId, UUID clusterId, CbbDeskSpecDTO deskSpecDTO, UUID strategyId) throws BusinessException {
        ImageDeskSpecGpuCheckParamDTO checkParamDTO = new ImageDeskSpecGpuCheckParamDTO();
        checkParamDTO.setImageId(imageId);
        checkParamDTO.setClusterId(clusterId);
        checkParamDTO.setVgpuInfoDTO(deskSpecDTO.getVgpuInfoDTO());
        checkParamDTO.setStrategyId(strategyId);
        deskSpecAPI.checkGpuSupportByImageAndSpec(checkParamDTO);
    }
}
