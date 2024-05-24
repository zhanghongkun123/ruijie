package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbCreateIDVDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbCreateThirdPartyDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbCreateVDIDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbCreateDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyThirdPartyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVOIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopRole;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.RcaCreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CloudDeskComputerNameConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IDVCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateThirdPartyDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.CreatePoolDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.CreatingDesktopNumCache;
import com.ruijie.rcos.rcdc.rco.module.impl.computername.ComputerNameBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.CreateUserDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CreateDesktopHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CreateDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PublicBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/16
 *
 * @author Jarman
 */
@Service
public class CreateDesktopServiceImpl implements CreateDesktopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDesktopServiceImpl.class);

    /**
     * 截取终端mac地址后8位（包含:号）
     */
    private static final int SUBNUM = 8;

    private static final String BLANK_FLAG = "-";

    private static final String DESK_MAC_SEPARATE_FLAG = ":";

    private static final int MAC_LENGTH = 3;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private CreatingDesktopNumCache creatingDesktopNumCache;

    @Autowired
    private CreateDesktopHelper createDesktopHelper;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private CloudDeskComputerNameConfigAPI cloudDeskComputerNameConfigAPI;

    private Map<UUID, Lock> createUserLockMap = Maps.newConcurrentMap();

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    @Autowired
    private CbbThirdPartyDeskMgmtAPI cbbThirdPartyDeskMgmtAPI;

    @Override
    public UserDesktopEntity create(CreateCloudDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request is null.");
        UUID userId = request.getUserId();
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);

        if (userDetail == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_USER_ENTITY_IS_NOT_EXIST,
                    new String[] {userId.toString()});
        }
        CbbCreateVDIDeskDTO cbbCreateVDIDeskDTO;
        try {
            getCreateLock(userDetail.getId()).lock();

            // 创建云桌面请求参数校验
            createDesktopHelper.validateCreateDeskRequestParams(request, userDetail);

            String desktopName = createDesktopHelper.generateDesktopNameForCreating(userDetail);

            //获取当前策略信息
            CbbDeskStrategyVDIDTO deskStrategyVDI = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(request.getStrategyId());
            UUID deskId = UUID.randomUUID();

            preSaveUserDesktopRelation(userDetail, deskId, deskStrategyVDI, desktopName , request);

            //构建请求参数
            cbbCreateVDIDeskDTO = buildCbbCreateVDIDeskDTO(request, deskStrategyVDI , deskId,  desktopName);
        } finally {
            getCreateLock(userDetail.getId()).unlock();
        }
        createCbbDesktop(cbbCreateVDIDeskDTO, true);
        return responseUserDesktopEntity(cbbCreateVDIDeskDTO);
    }

    private void preSaveUserDesktopRelation(IacUserDetailDTO userDetail, UUID deskId,
                                              CbbDeskStrategyVDIDTO deskStrategyVDI ,
                                              String desktopName, CreateCloudDesktopRequest request) {

        CreateUserDesktopDTO createUserDesktopDTO = new CreateUserDesktopDTO();
        createUserDesktopDTO.setDesktopName(desktopName);
        createUserDesktopDTO.setCbbDesktopId(deskId);
        createUserDesktopDTO.setDesktopType(CbbCloudDeskType.VDI);
        createUserDesktopDTO.setUserId(userDetail.getId());
        createUserDesktopDTO.setEnableFullSystemDisk(deskStrategyVDI.getEnableFullSystemDisk());
        // 在调用创建接口之前先预存用户-桌面关系表记录
        saveDesktopEntity(createUserDesktopDTO);

        // 保存 办公个性云桌面配置信息 t_rco_desk_info（软件管控策略信息和用户配置信息）
        saveRcoDeskInfoEntity(deskId, request.getSoftwareStrategyId(), request.getUserProfileStrategyId());
    }

    private CbbCreateVDIDeskDTO buildCbbCreateVDIDeskDTO(CreateCloudDesktopRequest request,
                                                         CbbDeskStrategyVDIDTO deskStrategyVDI,
                                                         UUID deskId , String desktopName) {
        final CbbCreateVDIDeskDTO cbbCreateVDIDeskDTO = new CbbCreateVDIDeskDTO();
        cbbCreateVDIDeskDTO.setDeskId(deskId);
        cbbCreateVDIDeskDTO.setImageTemplateId(request.getDesktopImageId());
        cbbCreateVDIDeskDTO.setIp(request.getDesktopIp());
        cbbCreateVDIDeskDTO.setName(desktopName);
        cbbCreateVDIDeskDTO.setNetworkId(request.getNetworkId());
        cbbCreateVDIDeskDTO.setStrategyId(request.getStrategyId());
        cbbCreateVDIDeskDTO.setDesktopRole(request.getDesktopRole());
        if (Objects.nonNull(request.getCustomTaskId())) {
            cbbCreateVDIDeskDTO.setCustomTaskId(request.getCustomTaskId());
        }
        cbbCreateVDIDeskDTO.setBatchTaskItem(request.getBatchTaskItem());
        cbbCreateVDIDeskDTO.setClusterId(request.getClusterId());
        cbbCreateVDIDeskDTO.setPlatformId(request.getPlatformId());
        cbbCreateVDIDeskDTO.setCreateDeskSpecDTO(buildCbbCreateDeskSpecDTO(request.getDeskSpec()));

        cbbCreateVDIDeskDTO.setDeskCreateMode(deskStrategyVDI.getDeskCreateMode());
        cbbCreateVDIDeskDTO.setPattern(deskStrategyVDI.getPattern());

        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isBlank(deskStrategyVDI.getRemark())) {
            cbbCreateVDIDeskDTO.setRemark(null);
        } else {
            // 设置云桌面标签备注
            cbbCreateVDIDeskDTO.setRemark(deskStrategyVDI.getRemark());
        }
        return cbbCreateVDIDeskDTO;
    }

    @Override
    public UserDesktopEntity createPoolDesktop(CreatePoolDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");

        CbbCreateVDIDeskDTO cbbCreateVDIDeskDTO = null;
        // 创建云桌面请求参数校验
        createDesktopHelper.validateCreateDeskRequestParams(request);
        String desktopName = request.getDesktopName();
        cbbCreateVDIDeskDTO = buildPoolCbbCreateVDIDeskDTO(request);

        //获取当前策略信息
        CbbDeskStrategyVDIDTO deskStrategyVDI = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(request.getStrategyId());
        cbbCreateVDIDeskDTO.setDeskCreateMode(deskStrategyVDI.getDeskCreateMode());
        cbbCreateVDIDeskDTO.setPattern(deskStrategyVDI.getPattern());

        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isBlank(deskStrategyVDI.getRemark())) {
            cbbCreateVDIDeskDTO.setRemark(null);
        } else {
            // 设置云桌面标签备注
            cbbCreateVDIDeskDTO.setRemark(deskStrategyVDI.getRemark());
        }
        cbbCreateVDIDeskDTO.setSessionType(deskStrategyVDI.getSessionTypeEnum());
        cbbCreateVDIDeskDTO.setBatchTaskItem(request.getItem());
        CreateUserDesktopDTO createUserDesktopDTO = new CreateUserDesktopDTO();
        createUserDesktopDTO.setCbbDesktopId(cbbCreateVDIDeskDTO.getDeskId());
        createUserDesktopDTO.setDesktopName(desktopName);
        createUserDesktopDTO.setDesktopType(CbbCloudDeskType.VDI);
        createUserDesktopDTO.setEnableFullSystemDisk(deskStrategyVDI.getEnableFullSystemDisk());


        // 在调用创建接口之前先预存用户-桌面关系表记录
        saveDesktopEntity(createUserDesktopDTO);

        // 创建桌面对应的spec记录
        // 保存 办公个性云桌面配置信息 t_rco_desk_info（软件管控策略信息）
        saveRcoDeskInfoEntity(cbbCreateVDIDeskDTO.getDeskId(), request.getSoftwareStrategyId(), request.getUserProfileStrategyId());

        createCbbDesktop(cbbCreateVDIDeskDTO, true);
        return responseUserDesktopEntity(cbbCreateVDIDeskDTO);
    }

    private CbbCreateVDIDeskDTO buildPoolCbbCreateVDIDeskDTO(CreatePoolDesktopRequest request) {
        final CbbCreateVDIDeskDTO cbbCreateVDIDeskDTO = new CbbCreateVDIDeskDTO();
        cbbCreateVDIDeskDTO.setDeskId(request.getDesktopId());
        cbbCreateVDIDeskDTO.setImageTemplateId(request.getImageTemplateId());
        cbbCreateVDIDeskDTO.setName(request.getDesktopName());
        cbbCreateVDIDeskDTO.setNetworkId(request.getNetworkId());
        cbbCreateVDIDeskDTO.setStrategyId(request.getStrategyId());
        cbbCreateVDIDeskDTO.setDesktopRole(request.getDesktopRole());
        cbbCreateVDIDeskDTO.setDesktopPoolType(request.getPoolDeskType());
        cbbCreateVDIDeskDTO.setDesktopPoolId(request.getPoolId());
        cbbCreateVDIDeskDTO.setClusterId(request.getClusterId());
        cbbCreateVDIDeskDTO.setCreateDeskSpecDTO(buildCbbCreateDeskSpecDTO(request.getDeskSpec()));
        cbbCreateVDIDeskDTO.setPlatformId(request.getPlatformId());
        cbbCreateVDIDeskDTO.setEnableCustom(false);
        return cbbCreateVDIDeskDTO;
    }

    @Override
    public UserDesktopEntity createRcaHostDesktop(RcaCreateCloudDesktopRequest rcaCreateCloudDesktopRequest) throws BusinessException {
        Assert.notNull(rcaCreateCloudDesktopRequest, "rcaCreateCloudDesktopRequest not be null");

        CreatePoolDesktopRequest rcoRequest = new CreatePoolDesktopRequest();
        BeanUtils.copyProperties(rcaCreateCloudDesktopRequest, rcoRequest);
        rcoRequest.setImageTemplateId(rcaCreateCloudDesktopRequest.getDesktopImageId());
        rcoRequest.setClusterId(rcaCreateCloudDesktopRequest.getClusterId());
        rcoRequest.setDeskSpec(rcaCreateCloudDesktopRequest.getCbbDeskSpecDTO());
        // 应用池和桌面池的id冲突，copy的时候会拷贝过来
        rcoRequest.setPoolId(null);
        // 创建云桌面请求参数校验
        createDesktopHelper.validateCreateDeskRequestParams(rcoRequest);

        // 创建云桌面dto
        CbbCreateVDIDeskDTO cbbCreateVDIDeskDTO = buildCreateRcaHostVDIDeskDTO(rcaCreateCloudDesktopRequest);
        // 云应用桌面都是跟随池，无独立规格属性
        cbbCreateVDIDeskDTO.setEnableCustom(false);
        //获取当前策略信息
        RcaMainStrategyDesktopDTO rcaMainStrategyDesktopDTO
                = rcaMainStrategyAPI.getDesktopStrategyOfRcaMainStrategy(rcaCreateCloudDesktopRequest.getStrategyId());

        cbbCreateVDIDeskDTO.setDeskCreateMode(rcaMainStrategyDesktopDTO.getDeskCreateMode());
        cbbCreateVDIDeskDTO.setPattern(rcaMainStrategyDesktopDTO.getPattern());

        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isBlank(rcaMainStrategyDesktopDTO.getRemark())) {
            cbbCreateVDIDeskDTO.setRemark(null);
        } else {
            // 设置云桌面标签备注
            cbbCreateVDIDeskDTO.setRemark(rcaMainStrategyDesktopDTO.getRemark());
        }

        CreateUserDesktopDTO createUserDesktopDTO = new CreateUserDesktopDTO();
        createUserDesktopDTO.setCbbDesktopId(cbbCreateVDIDeskDTO.getDeskId());
        createUserDesktopDTO.setDesktopName(rcoRequest.getDesktopName());
        createUserDesktopDTO.setDesktopType(CbbCloudDeskType.VDI);
        createUserDesktopDTO.setEnableFullSystemDisk(false);

        // 在调用创建接口之前先预存用户-桌面关系表记录
        saveDesktopEntity(createUserDesktopDTO);

        createCbbDesktop(cbbCreateVDIDeskDTO, false);
        return responseUserDesktopEntity(cbbCreateVDIDeskDTO);
    }

    private UserDesktopEntity responseUserDesktopEntity(CbbCreateVDIDeskDTO cbbCreateVDIDeskDTO) {
        // 构造返回的对象信息
        UserDesktopEntity deskEnt = new UserDesktopEntity();
        deskEnt.setCbbDesktopId(cbbCreateVDIDeskDTO.getDeskId());
        deskEnt.setDesktopName(cbbCreateVDIDeskDTO.getName());
        return deskEnt;
    }

    private CbbCreateVDIDeskDTO buildCreateRcaHostVDIDeskDTO(RcaCreateCloudDesktopRequest createCloudDesktopRequest) {
        // 字段可能缺失
        CbbCreateVDIDeskDTO cbbCreateVDIDeskDTO = new CbbCreateVDIDeskDTO();
        cbbCreateVDIDeskDTO.setDeskId(createCloudDesktopRequest.getDesktopId());
        cbbCreateVDIDeskDTO.setImageTemplateId(createCloudDesktopRequest.getDesktopImageId());
        cbbCreateVDIDeskDTO.setIp(createCloudDesktopRequest.getDesktopIp());
        cbbCreateVDIDeskDTO.setName(createCloudDesktopRequest.getDesktopName());
        cbbCreateVDIDeskDTO.setNetworkId(createCloudDesktopRequest.getNetworkId());
        cbbCreateVDIDeskDTO.setStrategyId(createCloudDesktopRequest.getStrategyId());
        cbbCreateVDIDeskDTO.setDesktopRole(DesktopRole.NORMAL);
        cbbCreateVDIDeskDTO.setBatchTaskItem(createCloudDesktopRequest.getBatchTaskItem());
        cbbCreateVDIDeskDTO.setClusterId(createCloudDesktopRequest.getClusterId());
        cbbCreateVDIDeskDTO.setPlatformId(createCloudDesktopRequest.getPlatformId());
        // 独立规格
        cbbCreateVDIDeskDTO.setCreateDeskSpecDTO(buildCbbCreateDeskSpecDTO(createCloudDesktopRequest.getCbbDeskSpecDTO()));
        return cbbCreateVDIDeskDTO;
    }

    private CbbCreateDeskSpecDTO buildCbbCreateDeskSpecDTO(CbbDeskSpecDTO deskSpecDTO) {
        if (Objects.isNull(deskSpecDTO)) {
            // 无就null
            return null;
        }
        return new CbbCreateDeskSpecDTO(deskSpecDTO);
    }

    @Override
    public UserDesktopEntity createIDV(IDVCloudDesktopDTO idvCloudDesktopDTO) throws BusinessException {
        Assert.notNull(idvCloudDesktopDTO, "idvCloudDesktopDTO is null.");
        LOGGER.info("创建IDV云桌面请求DTO,idvCloudDesktopDTO={}", JSONObject.toJSONString(idvCloudDesktopDTO));

        UserDesktopEntity deskEnt = new UserDesktopEntity();
        // 判断IDV终端是否已存在绑定的云桌面
        UserTerminalEntity userTerminalEntity =
                userTerminalDAO.findFirstByTerminalId(idvCloudDesktopDTO.getTerminalId());
        LOGGER.info("查看当前终端是否已经存在绑定的云桌面，userTerminalEntity={}", JSONObject.toJSONString(userTerminalEntity));
        if (null != userTerminalEntity && userTerminalEntity.getBindDeskId() != null) {
            deskEnt.setCbbDesktopId(userTerminalEntity.getBindDeskId());
            LOGGER.warn("当前IDV终端已经存在绑定的云桌面，userTerminalEntity={}", JSONObject.toJSONString(userTerminalEntity));
            return deskEnt;
        }
        //校验创建IDV云桌面参数  策略为IDV云桌面策略，镜像不为空
        createDesktopHelper.validateCreateIDVDeskRequestParams(idvCloudDesktopDTO.getStrategyId(),
                idvCloudDesktopDTO.getImageId());

        idvCloudDesktopDTO.setDeskId(UUID.randomUUID());
        if (IdvTerminalModeEnums.PERSONAL == idvCloudDesktopDTO.getIdvTerminalMode()) {
            // 个人模式
            UUID userId = idvCloudDesktopDTO.getUserId();
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
            if (userDetail == null) {
                throw new BusinessException(BusinessKey.RCDC_USER_USER_ENTITY_IS_NOT_EXIST,
                        new String[] {userId.toString()});
            }
            String desktopName = createDesktopHelper.generateDesktopName(userDetail);
            idvCloudDesktopDTO.setDesktopName(desktopName);
        } else {
            // 公用终端设置默认名称
            String terminalMac = idvCloudDesktopDTO.getTerminalMac();
            String subTerminalId = terminalMac.substring(terminalMac.length() - SUBNUM);
            //设置桌面名称  字典不区分IDV|VOI
            idvCloudDesktopDTO
                    .setDesktopName(PublicBusinessKey.DEFAULT_PUBLIC_IDV_DESK_NAME + "[" + subTerminalId + "]");
        }
        CbbDeskStrategyIDVDTO deskStrategyIDV = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(idvCloudDesktopDTO.getStrategyId());
        CreateUserDesktopDTO createUserDesktopDTO = buildCommonCreateUserDesktopDTO(idvCloudDesktopDTO);
        createUserDesktopDTO.setEnableFullSystemDisk(deskStrategyIDV.getEnableFullSystemDisk());

        // 创建IDV云桌面接口调用
        CbbCreateIDVDeskDTO cbbCreateIDVDeskDTO = buildCbbCreateIDVDeskDTO(idvCloudDesktopDTO);
        LOGGER.info("请求云桌面组件接口，创建IDV云桌面,cbbCreateIDVDeskDTO={}", JSONObject.toJSONString(cbbCreateIDVDeskDTO));

        checkAndCleanIdvDesktopInfo(createUserDesktopDTO);

        cbbIDVDeskMgmtAPI.createDeskIDV(cbbCreateIDVDeskDTO);
        //在创建桌面后再 生成用户和云桌面的关联关系
        saveDesktopEntity(createUserDesktopDTO);
        updateIDVDeskComputerName(idvCloudDesktopDTO.getDeskId(), idvCloudDesktopDTO.getStrategyId());

        // 保存 办公个性云桌面配置信息 t_rco_desk_info（软件管控策略信息）
        saveRcoDeskInfoEntity(idvCloudDesktopDTO.getDeskId(), idvCloudDesktopDTO.getSoftwareStrategyId(),
                idvCloudDesktopDTO.getUserProfileStrategyId());
        deskEnt.setDesktopName(idvCloudDesktopDTO.getDesktopName());
        deskEnt.setCbbDesktopId(idvCloudDesktopDTO.getDeskId());
        return deskEnt;
    }

    @Override
    public UserDesktopEntity createVOI(IDVCloudDesktopDTO voiCloudDesktopDTO) throws BusinessException {
        Assert.notNull(voiCloudDesktopDTO, "voiCloudDesktopDTO is null.");
        LOGGER.info("创建VOI云桌面请求DTO,voiCloudDesktopDTO={}", JSONObject.toJSONString(voiCloudDesktopDTO));

        UserDesktopEntity deskEnt = new UserDesktopEntity();
        // 判断VOI终端是否已存在绑定的云桌面  一个终端只能存在一个云桌面绑定
        UserTerminalEntity userTerminalEntity =
                userTerminalDAO.findFirstByTerminalId(voiCloudDesktopDTO.getTerminalId());
        LOGGER.info("查看当前终端是否已经存在绑定的云桌面，userTerminalEntity={}", JSONObject.toJSONString(userTerminalEntity));
        if (null != userTerminalEntity && userTerminalEntity.getBindDeskId() != null) {
            deskEnt.setCbbDesktopId(userTerminalEntity.getBindDeskId());
            LOGGER.warn("当前VOI终端已经存在绑定的云桌面，userTerminalEntity={}", JSONObject.toJSONString(userTerminalEntity));
            return deskEnt;
        }
        //校验VOI参数  桌面策略是否为VOI，镜像是否存在
        createDesktopHelper.validateCreateVOIDeskRequestParams(voiCloudDesktopDTO.getStrategyId(),
                voiCloudDesktopDTO.getImageId());

        voiCloudDesktopDTO.setDeskId(UUID.randomUUID());
        if (IdvTerminalModeEnums.PERSONAL == voiCloudDesktopDTO.getIdvTerminalMode()) {
            // 个人模式
            UUID userId = voiCloudDesktopDTO.getUserId();
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
            if (userDetail == null) {
                throw new BusinessException(BusinessKey.RCDC_USER_USER_ENTITY_IS_NOT_EXIST,
                        new String[] {userId.toString()});
            }
            String desktopName = createDesktopHelper.generateDesktopName(userDetail);
            voiCloudDesktopDTO.setDesktopName(desktopName);
        } else {
            // 公用终端设置默认名称
            String terminalMac = voiCloudDesktopDTO.getTerminalMac();
            String subTerminalId = terminalMac.substring(terminalMac.length() - SUBNUM);
            //置桌面名称  字典不区分IDV|VOI
            voiCloudDesktopDTO
                    .setDesktopName(PublicBusinessKey.DEFAULT_PUBLIC_IDV_DESK_NAME + "[" + subTerminalId + "]");
        }

        CreateUserDesktopDTO createUserDesktopDTO = buildCommonCreateUserDesktopDTO(voiCloudDesktopDTO);
        CbbDeskStrategyVOIDTO deskStrategyVOI = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(voiCloudDesktopDTO.getStrategyId());
        createUserDesktopDTO.setEnableFullSystemDisk(deskStrategyVOI.getEnableFullSystemDisk());


        // 构造VOI云桌面DTO   复用原有IDV方法
        CbbCreateIDVDeskDTO cbbCreateVOIDeskDTO = buildCbbCreateIDVDeskDTO(voiCloudDesktopDTO);
        LOGGER.info("请求云桌面组件接口，创建VOI云桌面,cbbCreateIDVDeskDTO={}", JSONObject.toJSONString(cbbCreateVOIDeskDTO));

        checkAndCleanIdvDesktopInfo(createUserDesktopDTO);

        cbbIDVDeskMgmtAPI.createDeskIDV(cbbCreateVOIDeskDTO);
        // VOI 生成用户和云桌面的关联关系 沿用IDV 不需要修改  由镜像类型判断是否为VOI CBB 没有提供接口
        saveDesktopEntity(createUserDesktopDTO);
        // 更新VOI桌面名称 再确认下
        updateIDVDeskComputerName(voiCloudDesktopDTO.getDeskId(), voiCloudDesktopDTO.getStrategyId());

        // 保存 办公个性云桌面配置信息 t_rco_desk_info（软件管控策略信息）
        saveRcoDeskInfoEntity(voiCloudDesktopDTO.getDeskId(), voiCloudDesktopDTO.getSoftwareStrategyId(),
                voiCloudDesktopDTO.getUserProfileStrategyId());
        deskEnt.setDesktopName(voiCloudDesktopDTO.getDesktopName());
        deskEnt.setCbbDesktopId(voiCloudDesktopDTO.getDeskId());
        return deskEnt;
    }

    @Override
    public void createThirdParty(CreateThirdPartyDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request is null.");
        CbbCreateThirdPartyDeskDTO thirdPartyDeskDTO = new CbbCreateThirdPartyDeskDTO();
        try {
            getCreateLock(request.getDeskId()).lock();
            // 检查已创建云桌面个数，如果超出指定创建个数则抛异常
            UUID userId = request.getUserId();
            if (userId != null) {
                IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);

                if (userDetail == null) {
                    throw new BusinessException(BusinessKey.RCDC_USER_USER_ENTITY_IS_NOT_EXIST,
                            userId.toString());
                }
                createDesktopHelper.checkCreatedDesktopNum(userId, userDetail.getUserType());
            }

            buildThirdPartyDeskDTO(request, thirdPartyDeskDTO);

            CreateUserDesktopDTO createUserDesktopDTO = new CreateUserDesktopDTO();
            createUserDesktopDTO.setCbbDesktopId(thirdPartyDeskDTO.getDeskId());
            createUserDesktopDTO.setDesktopName(request.getDesktopName());
            createUserDesktopDTO.setDesktopType(CbbCloudDeskType.THIRD);
            createUserDesktopDTO.setEnableFullSystemDisk(Boolean.FALSE);
            createUserDesktopDTO.setUserId(userId);
            // 在调用创建接口之前先预存用户-桌面关系表记录
            saveDesktopEntity(createUserDesktopDTO);
            // 保存 办公个性云桌面配置信息 t_rco_desk_info（软件管控策略信息和用户配置信息）
            saveRcoDeskInfoEntity(thirdPartyDeskDTO.getDeskId(), request.getSoftwareStrategyId(), request.getUserProfileStrategyId());

            createCbbThirdPartyDesktop(userId, thirdPartyDeskDTO);

        } finally {
            getCreateLock(request.getDeskId()).unlock();
        }
    }

    private void buildThirdPartyDeskDTO(CreateThirdPartyDesktopRequest request, CbbCreateThirdPartyDeskDTO thirdPartyDeskDTO) throws BusinessException {
        thirdPartyDeskDTO.setDeskId(request.getDeskId());
        thirdPartyDeskDTO.setIp(request.getDesktopIp());
        thirdPartyDeskDTO.setName(request.getDesktopName());
        thirdPartyDeskDTO.setComputerName(request.getDesktopName());
        thirdPartyDeskDTO.setStrategyId(request.getStrategyId());
        thirdPartyDeskDTO.setDesktopRole(DesktopRole.NORMAL);
        thirdPartyDeskDTO.setDesktopPoolId(request.getPoolId());
        thirdPartyDeskDTO.setDesktopPoolType(request.getPoolDeskType());
        thirdPartyDeskDTO.setOsName(request.getOsName());
        thirdPartyDeskDTO.setGuestToolVersion(request.getAgentVersion());

        CbbDeskStrategyThirdPartyDTO deskStrategyThirdParty = cbbThirdPartyDeskStrategyMgmtAPI.getDeskStrategyThirdParty(request.getStrategyId());
        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isBlank(deskStrategyThirdParty.getRemark())) {
            thirdPartyDeskDTO.setRemark(null);
        } else {
            // 设置云桌面标签备注
            thirdPartyDeskDTO.setRemark(deskStrategyThirdParty.getRemark());
        }
        thirdPartyDeskDTO.setSessionType(deskStrategyThirdParty.getSessionType());
    }

    private void createCbbThirdPartyDesktop(UUID userId, CbbCreateThirdPartyDeskDTO cbbCreateVDIDeskDTO) throws BusinessException {
        try {
            if (userId != null) {
                // 创建云桌面前，缓存中新增1个创建中的云桌面个数
                creatingDesktopNumCache.increaseDesktopNum(userId);
            }
            cbbThirdPartyDeskMgmtAPI.createDeskThirdParty(cbbCreateVDIDeskDTO);
        } finally {
            // 创建云桌面不管成功还是失败，都要减去1个正在创建中的云桌面个数
            if (userId != null) {
                creatingDesktopNumCache.reduceDesktopNum(userId);
            }
        }
    }

    private CbbCreateIDVDeskDTO buildCbbCreateIDVDeskDTO(IDVCloudDesktopDTO idvCloudDesktopDTO) {
        CbbCreateIDVDeskDTO cbbCreateIDVDeskDTO = new CbbCreateIDVDeskDTO();
        cbbCreateIDVDeskDTO.setDeskId(idvCloudDesktopDTO.getDeskId());
        cbbCreateIDVDeskDTO.setName(idvCloudDesktopDTO.getDesktopName());
        cbbCreateIDVDeskDTO.setImageTemplateId(idvCloudDesktopDTO.getImageId());
        // 创建桌面时，初始化为绑定的镜像id
        cbbCreateIDVDeskDTO.setRealUseImageId(idvCloudDesktopDTO.getImageId());
        cbbCreateIDVDeskDTO.setStrategyId(idvCloudDesktopDTO.getStrategyId());
        cbbCreateIDVDeskDTO.setDesktopRole(DesktopRole.NORMAL);
        return cbbCreateIDVDeskDTO;
    }

    private void createCbbDesktop(CbbCreateVDIDeskDTO cbbCreateVDIDeskDTO, boolean isDesktop) throws BusinessException {
        LOGGER.info("调用CBB接口创建桌面[{}]", JSON.toJSONString(cbbCreateVDIDeskDTO));
        cbbVDIDeskMgmtAPI.createDeskVDI(cbbCreateVDIDeskDTO);
        if (isDesktop) {
            updateVDIDeskComputerName(cbbCreateVDIDeskDTO.getDeskId(), cbbCreateVDIDeskDTO.getStrategyId());
        } else {
            updateRcaHostVDIDeskComputerName(cbbCreateVDIDeskDTO);
        }
    }

    private CreateUserDesktopDTO buildCommonCreateUserDesktopDTO(IDVCloudDesktopDTO cloudDesktopDTO) throws BusinessException {
        CreateUserDesktopDTO createUserDesktopDTO = new CreateUserDesktopDTO();
        createUserDesktopDTO.setCbbDesktopId(cloudDesktopDTO.getDeskId());
        createUserDesktopDTO.setDesktopName(cloudDesktopDTO.getDesktopName());
        createUserDesktopDTO.setDesktopType(CbbCloudDeskType.IDV);
        createUserDesktopDTO.setUserId(cloudDesktopDTO.getUserId());
        createUserDesktopDTO.setTerminalId(cloudDesktopDTO.getTerminalId());

        return createUserDesktopDTO;
    }



    private UserDesktopEntity saveDesktopEntity(CreateUserDesktopDTO createUserDesktopDTO) {
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(createUserDesktopDTO.getCbbDesktopId());
        if (userDesktopEntity != null) {
            LOGGER.warn("桌面信息已存在，无需再次添加，cbb桌面id[{}]，桌面名称[{}]",
                    createUserDesktopDTO.getCbbDesktopId(), userDesktopEntity.getDesktopName());
            return userDesktopEntity;
        }
        UserDesktopEntity deskEnt = new UserDesktopEntity();
        BeanUtils.copyProperties(createUserDesktopDTO, deskEnt);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("保存用户和桌面的关系数据，deskEnt={}", JSONObject.toJSONString(deskEnt));
        }

        return createDesktopHelper.saveUserDestopIfNotExistByCbbDesktopId(deskEnt);
    }

    /**
     * 保存桌面跟软控策略、用户配置的关系
     * @param cbbDeskId
     * @param softwareStrategyId
     */
    private void saveRcoDeskInfoEntity(UUID cbbDeskId, UUID softwareStrategyId, UUID userProfileStrategyId) {
        RcoDeskInfoEntity rcoDeskInfoEntity = new RcoDeskInfoEntity();
        rcoDeskInfoEntity.setDeskId(cbbDeskId);
        rcoDeskInfoEntity.setSoftwareStrategyId(softwareStrategyId);
        rcoDeskInfoEntity.setUserProfileStrategyId(userProfileStrategyId);
        rcoDeskInfoEntity.setUpdateTime(new Date());
        rcoDeskInfoEntity.setCreateTime(new Date());
        rcoDeskInfoDAO.save(rcoDeskInfoEntity);
        LOGGER.info("保存桌面跟软控策略的关系数据，deskId={}, softwareStrategyId", cbbDeskId, softwareStrategyId);
    }

    private synchronized Lock getCreateLock(UUID userId) {
        Lock lock = createUserLockMap.get(userId);
        if (lock == null) {
            lock = new ReentrantLock();
            createUserLockMap.put(userId, lock);
        }
        return lock;
    }

    private void updateIDVDeskComputerName(UUID deskId, UUID deskStrategyId) throws BusinessException {
        try {
            String computerNamePrefix = obtainComputerName(deskStrategyId);
            if (StringUtils.isNotBlank(computerNamePrefix)) {
                UserDesktopEntity userDesk = userDesktopDAO.findByCbbDesktopId(deskId);
                // IDV场景下使用终端MAC的后六位，虚机MAC也是由Shine通过终端MAC来生成的
                String computerName = computerNamePrefix + obtainDeskMac(userDesk.getTerminalId());
                cbbIDVDeskMgmtAPI.updateIDVDeskComputerName(deskId, computerName);
            }
        } catch (BusinessException e) {
            LOGGER.error("更新云桌面[{}]计算机名数据失败", deskId, e);
        }
    }

    private void updateVDIDeskComputerName(UUID deskId, UUID deskStrategyId) throws BusinessException {
        try {
            String computerNamePrefix = obtainComputerName(deskStrategyId);
            if (StringUtils.isNotBlank(computerNamePrefix)) {
                CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
                String computerName = computerNamePrefix + obtainDeskMac(cbbDeskDTO.getDeskMac());
                cbbVDIDeskMgmtAPI.updateVDIDeskComputerName(deskId, computerName);
            }
        } catch (BusinessException e) {
            LOGGER.error("更新云桌面计算机名数据失败", e);
        }
    }

    // 应用主机的计算机名称来源不一致，需要额外处理
    private void updateRcaHostVDIDeskComputerName(CbbCreateVDIDeskDTO cbbCreateVDIDeskDTO) throws BusinessException {
        try {
            UUID cbbDeskStrategyId = cbbCreateVDIDeskDTO.getStrategyId();
            UUID deskId = cbbCreateVDIDeskDTO.getDeskId();
            RcaMainStrategyDesktopDTO rcaMainStrategyDesktopDTO = rcaMainStrategyAPI.getDesktopStrategyOfRcaMainStrategy(cbbDeskStrategyId);
            String computerNamePrefix = rcaMainStrategyDesktopDTO.getComputerName();
            if (StringUtils.isNotBlank(computerNamePrefix)) {
                CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
                String computerName = computerNamePrefix + obtainDeskMac(cbbDeskDTO.getDeskMac());
                cbbVDIDeskMgmtAPI.updateVDIDeskComputerName(deskId, computerName);
            }
        } catch (Exception e) {
            LOGGER.error("rca_host-更新云桌面计算机名数据失败", e);
        }
    }

    private String obtainComputerName(UUID deskStrategyId) {
        try {
            return cloudDeskComputerNameConfigAPI.findCloudDeskComputerName(deskStrategyId);
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面计算机名数据失败", e);
        }
        return StringUtils.EMPTY;
    }

    private String obtainDeskMac(String deskMac) throws BusinessException {
        if (StringUtils.isBlank(deskMac)) {
            throw new BusinessException(ComputerNameBusinessKey.RCDC_RCO_CLOUD_DESK_DESKMAC_NOT_EXIT);
        }
        String[] deskMacArr = deskMac.split(DESK_MAC_SEPARATE_FLAG);
        if (deskMacArr.length < MAC_LENGTH) {
            LOGGER.error("MAC地址[{}]格式不正确", deskMac);
            throw new BusinessException(ComputerNameBusinessKey.RCDC_RCO_CLOUD_DESK_DESKMAC_FORMAT_ERROR);
        }
        return BLANK_FLAG + deskMacArr[deskMacArr.length - 3] + deskMacArr[deskMacArr.length - 2]
                + deskMacArr[deskMacArr.length - 1];
    }

    private void  checkAndCleanIdvDesktopInfo(CreateUserDesktopDTO createUserDesktopDTO) {
        //清理脏数据。
        //fix bug:666845规避方案，避免初始化终端后清理数据异常造成残留，对功能产生影响.
        if (createUserDesktopDTO.getDesktopType() != CbbCloudDeskType.IDV) {
            return;
        }
        String terminalId = createUserDesktopDTO.getTerminalId();
        try {
            //rco_desk_info
            List<UserDesktopEntity> userDesktopEntityList = userDesktopDAO.findByTerminalId(terminalId);
            List<UUID> desktopIdList = userDesktopEntityList.stream().map(UserDesktopEntity::getCbbDesktopId).collect(Collectors.toList());

            if (userDesktopEntityList.size() > 0) {
                LOGGER.warn("检测到创建桌面前数据表中有桌面数据残留，桌面信息:[{}], terminalId:[{}]", JSON.toJSONString(userDesktopEntityList), terminalId);
                List<String> terminalIdList = new ArrayList<>();
                terminalIdList.add(terminalId);
                userDesktopDAO.deleteByTerminalIdIn(terminalIdList);
            }

            //cbb_desk_info
            if (desktopIdList.size() > 0) {
                LOGGER.warn("检测到创建桌面前数据表中有桌面数据残留，桌面信息:[{}], terminalId:[{}]", JSON.toJSONString(userDesktopEntityList), terminalId);
                for (UUID deskId : desktopIdList) {
                    cbbIDVDeskMgmtAPI.deleteDeskIDV(deskId);
                }
            }
        } catch (BusinessException e) {
            LOGGER.error("清理残留数据失败:", e);
        }
    }
}
