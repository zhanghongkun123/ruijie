package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desknetwork.CbbListDeskIpPoolRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbNetworkStrategyMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DeskNetworkIpPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.NetworkMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.DeleteDeskNetworkBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.desknetwork.DeleteSingleDeskNetworkBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.desknetwork.EditDeskNetworkBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.NetworkDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.NetworkInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desknetwork.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.CheckDuplicationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.VirtualSwitchWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.networkstrategy.dto.RcoNetworkStrategyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.networkstrategy.vo.NetworkStrategyDetailVO;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.api.match.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.PageResponseContent;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 网络策略管理controller层
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月11日
 *
 * @author huangxiaodan
 */
@Api(tags = "云桌面网络策略")
@Controller
@RequestMapping("/rco/clouddesktop")
public class DeskNetworkCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskNetworkCtrl.class);

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ComputerClusterServerMgmtAPI computerClusterAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private NetworkMgmtAPI networkMgmtAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    private static final String DEFAULT_GATEWAY = "192.168.0.1";

    private static final String DEFAULT_IP_CIDR = "192.168.0.0/16";

    /**
     * 镜像模板类型key
     */
    private static final String IMAGE_TYPE = "cbbImageType";

    private static final String SORT_FIELD_CREATE_TIME = "createTime";

    private static final String IMAGE_TEMPLATE_TYPE = "TCI";

    /**
     * 创建网络策略
     *
     * @param createWebRequest 前端请求报文
     * @param sessionContext session上下文
     * @return 创建结果
     * @throws BusinessException 异常
     */
    @ApiOperation("创建网络策略")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "deskNetwork/create", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse createDeskNetwork(CreateDeskNetworkWebRequest createWebRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(createWebRequest, "createDeskNetworkWebRequest can not be null");

        LOGGER.info("rcv createDeskNetwork msg: {}", JSONObject.toJSONString(createWebRequest));
        CbbDeskNetworkDTO cbbCreateRequest = new CbbDeskNetworkDTO();
        cbbCreateRequest.setId(UUID.randomUUID());
        cbbCreateRequest.setDeskNetworkName(createWebRequest.getDeskNetworkName());
        cbbCreateRequest.setVswitchId(createWebRequest.getVswitchId());
        cbbCreateRequest.setNetworkMode(createWebRequest.getNetworkMode());
        cbbCreateRequest.setClusterId(createWebRequest.getClusterId());
        cbbCreateRequest.setPlatformId(createWebRequest.getPlatformId());
        // 从会话从获取用户名
        cbbCreateRequest.setCreatorUserName(sessionContext.getUserName());
        CbbDeskNetworkDnsDTO cbbNetworkDnsDTO = new CbbDeskNetworkDnsDTO();
        cbbNetworkDnsDTO.setDnsPrimary(createWebRequest.getDns().getDnsPrimary());
        cbbNetworkDnsDTO.setDnsSecondary(createWebRequest.getDns().getDnsSecondary());
        cbbCreateRequest.setDns(cbbNetworkDnsDTO);
        if (CbbNetworkStrategyMode.DHCP != createWebRequest.getNetworkMode()) {
            Assert.notNull(createWebRequest.getNetworkConfig(), "networkConfig can not be null");
            CbbDeskNetworkConfigDTO cbbNetworkConfigDTO = new CbbDeskNetworkConfigDTO();
            cbbNetworkConfigDTO.setGateway(createWebRequest.getNetworkConfig().getGateway());
            cbbNetworkConfigDTO.setIpCidr(createWebRequest.getNetworkConfig().getIpCidr());
            cbbNetworkConfigDTO.setIpPoolArr(createWebRequest.getNetworkConfig().getIpPoolArr());
            cbbCreateRequest.setNetworkConfig(cbbNetworkConfigDTO);
        }
        LOGGER.info("cbbCreateDeskNetworkRequest: {}", JSONObject.toJSONString(cbbCreateRequest));
        try {
            cbbNetworkMgmtAPI.createDeskNetwork(cbbCreateRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_CREATE_SUCCESS, cbbCreateRequest.getDeskNetworkName());
            return CommonWebResponse.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            LOGGER.error("网络策略创建失败，label: " + createWebRequest.getDeskNetworkName(), e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_CREATE_FAIL, e, cbbCreateRequest.getDeskNetworkName(),
                    e.getI18nMessage());

            throw e;
        } catch (Exception e) {
            Throwable throwable = e.getCause();
            if (throwable instanceof BusinessException) {
                BusinessException ex = (BusinessException) throwable;
                LOGGER.error("网络策略创建失败，Exception, label: " + createWebRequest.getDeskNetworkName(), e);
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_CREATE_FAIL, e, cbbCreateRequest.getDeskNetworkName(),
                        ex.getI18nMessage());
            }

            throw e;
        }
    }

    /**
     * 分页查询网络策略
     *
     * @param pageQueryRequest 前端请求信息
     * @return 网络策略列表
     * @throws BusinessException 业务异常
     * @throws NoSuchFieldException NoSuchFieldException
     * @throws IllegalAccessException IllegalAccessException
     */
    @ApiOperation("分页查询网络策略")
    @RequestMapping(value = "deskNetwork/list", method = RequestMethod.POST)
    public CommonWebResponse<PageResponseContent<NetworkStrategyDetailVO>> listDeskNetwork(PageQueryRequest pageQueryRequest)
            throws BusinessException, NoSuchFieldException, IllegalAccessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest can not be null");

        LOGGER.debug("rcv listDeskNetwork msg: {}", JSONObject.toJSONString(pageQueryRequest));
        Match[] matchArr = pageQueryRequest.getMatchArr();
        UUID[] desktopPoolIdArr = null;
        Integer[] desktopPoolCapacityArr = null;
        UUID[] imageTemplateIdArr = null;
        UUID[] storagePoolIdArr = null;
        UUID[] clusterIdArr = null;
        CbbImageType[] imageTypeArr = null;
        List<Match> matchList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(matchArr)) {
            for (Match match : matchArr) {
                if (match.getType() == Match.Type.EXACT && StringUtils.equals("desktopPoolId", ((ExactMatch) match).getFieldName())) {
                    desktopPoolIdArr =
                            Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
                    continue;
                }
                if (match.getType() == Match.Type.EXACT && StringUtils.equals("desktopPoolCapacity", ((ExactMatch) match).getFieldName())) {
                    desktopPoolCapacityArr = Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> (int) item).toArray(Integer[]::new);
                    continue;
                }
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(Constants.IMAGE_TEMPLATE_ID, ((ExactMatch) match).getFieldName())) {
                    imageTemplateIdArr =
                            Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
                    continue;
                }
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(Constants.STORAGE_POOL_ID, ((ExactMatch) match).getFieldName())) {
                    storagePoolIdArr =
                            Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
                    continue;
                }
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(Constants.CLUSTER_ID, ((ExactMatch) match).getFieldName())) {
                    clusterIdArr =
                            Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> UUID.fromString(item.toString())).toArray(UUID[]::new);
                    continue;
                }
                if (match.getType() == Match.Type.EXACT && StringUtils.equals(IMAGE_TYPE, ((ExactMatch) match).getFieldName())) {
                    imageTypeArr = Arrays.stream(((ExactMatch) match).getValueArr()).map(item -> CbbImageType.valueOf(item.toString()))
                            .filter(cbbImageType -> cbbImageType != CbbImageType.VDI).toArray(CbbImageType[]::new);
                    continue;
                }
                matchList.add(match);
            }
        }

        Field matchArrField = pageQueryRequest.getClass().getDeclaredField("matchArr");
        matchArrField.setAccessible(true);
        matchArrField.set(pageQueryRequest, matchList.toArray(new Match[matchList.size()]));

        PageQueryResponse<CbbDeskNetworkBasicInfoDTO> response = cbbNetworkMgmtAPI.pageQuery(pageQueryRequest);
        if (response.getItemArr() == null || response.getItemArr().length == 0) {
            return CommonWebResponse.success();
        }
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList().stream()
                .collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfoDTO -> clusterInfoDTO));
        NetworkStrategyDetailVO[] desktopNetworkDetailVOArr = this.buildNetworkStrategyVOArr(response.getItemArr(), clusterInfoAllMap);

        if (desktopPoolIdArr != null && desktopPoolIdArr.length > 0) {
            getNetworkStrategyByDesktopPoolId(desktopNetworkDetailVOArr, desktopPoolIdArr);
        }
        if (desktopPoolCapacityArr != null && desktopPoolCapacityArr.length > 0) {
            getNetworkStrategyByDesktopPoolCapacity(desktopNetworkDetailVOArr, desktopPoolCapacityArr);
        }
        // 筛选过滤网络策略
        getNetworkStrategyByIds(desktopNetworkDetailVOArr, imageTemplateIdArr, storagePoolIdArr, clusterIdArr, imageTypeArr);
        PageResponseContent<NetworkStrategyDetailVO> pageResponseContent = new PageResponseContent<>(desktopNetworkDetailVOArr, response.getTotal());
        return CommonWebResponse.success(pageResponseContent);
    }

    private void getNetworkStrategyByIds(NetworkStrategyDetailVO[] desktopNetworkDetailVOArr, UUID[] imageTemplateIdArr, UUID[] storagePoolIdArr,
            UUID[] clusterIdArr, CbbImageType[] imageTypeArr) throws BusinessException {
        if (imageTemplateIdArr != null && imageTemplateIdArr.length > 0) {
            Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList().stream()
                    .collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfoDTO -> clusterInfoDTO));
            getNetworkStrategyByImageTemplateId(desktopNetworkDetailVOArr, imageTemplateIdArr, clusterInfoAllMap);
        }

        if (storagePoolIdArr != null && storagePoolIdArr.length > 0) {
            getNetworkStrategyByStoragePoolId(desktopNetworkDetailVOArr, storagePoolIdArr);
        }

        if (clusterIdArr != null && clusterIdArr.length > 0) {
            getNetworkStrategyByClusterId(desktopNetworkDetailVOArr, clusterIdArr);
        }

        if (imageTypeArr != null && imageTypeArr.length > 0) {
            getNetworkStrategyByImageType(desktopNetworkDetailVOArr, imageTypeArr);
        }
    }

    private void getNetworkStrategyByImageTemplateId(NetworkStrategyDetailVO[] desktopNetworkDetailVOArr, 
                                                     UUID[] imageTemplateIdArr, Map<UUID, ClusterInfoDTO> clusterInfoAllMap) {
        for (UUID imageTemplateId : imageTemplateIdArr) {
            CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
            ClusterInfoDTO clusterInfo = imageTemplateDetail.getClusterInfo();
            if (imageTemplateDetail.getCbbImageType() != CbbImageType.VDI || Objects.isNull(clusterInfo)) {
                continue;
            }
            Set<String> imageArchSet = clusterInfoAllMap.getOrDefault(clusterInfo.getId(), new ClusterInfoDTO()).getArchSet();

            Arrays.stream(desktopNetworkDetailVOArr).forEach(networkStrategyDetailVO -> {
                ClusterInfoDTO clusterInfoDTO = networkStrategyDetailVO.getClusterInfoDTO();
                if (Objects.isNull(clusterInfoDTO) || CollectionUtils.isEmpty(imageArchSet) ||
                        SetUtils.intersection(imageArchSet, Optional.ofNullable(clusterInfoAllMap.get(clusterInfoDTO.getId()))
                                .map(ClusterInfoDTO::getArchSet).orElse(new HashSet<>())).isEmpty()) {
                    networkStrategyDetailVO.setCanUsed(Boolean.FALSE);
                    networkStrategyDetailVO
                            .setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NETWORK_AND_IMAGE_TEMPLATE_CLUSTER_CPU_NOT_AGREEMENT));
                }
            });
        }
    }

    private void getNetworkStrategyByStoragePoolId(NetworkStrategyDetailVO[] desktopNetworkDetailVOArr, UUID[] storagePoolIdArr)
            throws BusinessException {
        for (UUID storagePoolId : storagePoolIdArr) {
            Set<UUID> clusterIdSet = clusterAPI.queryClusterToSetByStoragePoolId(storagePoolId);
            Arrays.stream(desktopNetworkDetailVOArr).forEach(networkStrategyDetailVO -> {
                ClusterInfoDTO clusterInfoDTO = networkStrategyDetailVO.getClusterInfoDTO();
                if (Objects.isNull(clusterInfoDTO) || !clusterIdSet.contains(clusterInfoDTO.getId())) {
                    networkStrategyDetailVO.setCanUsed(Boolean.FALSE);
                    networkStrategyDetailVO
                            .setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NETWORK_AND_STORAGE_POOL_CLUSTER_NOT_AGREEMENT));
                }
            });
        }
    }

    private void getNetworkStrategyByClusterId(NetworkStrategyDetailVO[] desktopNetworkDetailVOArr, UUID[] clusterIdArr) {
        for (UUID clusterId : clusterIdArr) {
            Arrays.stream(desktopNetworkDetailVOArr).forEach(networkStrategyDetailVO -> {
                ClusterInfoDTO clusterInfoDTO = networkStrategyDetailVO.getClusterInfoDTO();
                if (Objects.isNull(clusterInfoDTO) || !Objects.equals(clusterId, clusterInfoDTO.getId())) {
                    networkStrategyDetailVO.setCanUsed(Boolean.FALSE);
                    networkStrategyDetailVO.setCanUsedMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NETWORK_AND_CLUSTER_NOT_AGREEMENT));
                }
            });
        }
    }

    private void getNetworkStrategyByImageType(NetworkStrategyDetailVO[] desktopNetworkDetailVOArr, CbbImageType[] imageTypeArr)
            throws BusinessException {
        for (CbbImageType imageType : imageTypeArr) {
            PlatformComputerClusterDTO computeCluster = computerClusterAPI.getDefaultComputeCluster();

            Arrays.stream(desktopNetworkDetailVOArr).forEach(networkStrategyDetailVO -> {
                ClusterInfoDTO clusterInfoDTO = networkStrategyDetailVO.getClusterInfoDTO();
                if (Objects.isNull(clusterInfoDTO) || !Objects.equals(clusterInfoDTO.getId(), computeCluster.getId())) {
                    String imageTypeName = imageType == CbbImageType.VOI ? IMAGE_TEMPLATE_TYPE : imageType.name();
                    networkStrategyDetailVO.setCanUsed(Boolean.FALSE);
                    networkStrategyDetailVO.setCanUsedMessage(
                            LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_NETWORK_CLUSTER_NOT_IDV_VDI_IMAGE_TEMPLATE, imageTypeName));
                }
            });
        }
    }

    /**
     * 编辑桌面池筛选网络策略场景，校验网络策略中的剩余ip是否足够
     */
    private void getNetworkStrategyByDesktopPoolId(NetworkStrategyDetailVO[] desktopNetworkDetailVOArr, UUID[] desktopPoolIdArr)
            throws BusinessException {
        for (UUID desktopPoolId : desktopPoolIdArr) {
            DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(desktopPoolId);
            UUID networkId = desktopPoolBasicDTO.getNetworkId();

            Arrays.stream(desktopNetworkDetailVOArr).forEach(networkStrategyDetailVO -> {
                if (networkId.equals(networkStrategyDetailVO.getCbbDeskNetworkBasicInfoDTO().getId())) {
                    LOGGER.info("[{}]为桌面池[{}]当前配置的网络策略，无需检查容量能否支持当前桌面池", networkStrategyDetailVO.getCbbDeskNetworkBasicInfoDTO().getDeskNetworkName(),
                            desktopPoolId);
                    return;
                }
                getNetworkStrategyBySupportCapacity(networkStrategyDetailVO, desktopPoolBasicDTO.getDesktopNum());
            });
        }
    }

    /**
     * 创建桌面池筛选网络策略场景，校验网络策略中的剩余ip是否足够
     */
    private void getNetworkStrategyByDesktopPoolCapacity(NetworkStrategyDetailVO[] desktopNetworkDetailVOArr, Integer[] desktopPoolCapacityArr) {

        Arrays.stream(desktopPoolCapacityArr).forEach(desktopPoolCapacity -> {
            Arrays.stream(desktopNetworkDetailVOArr).forEach(networkStrategyDetailVO -> {
                getNetworkStrategyBySupportCapacity(networkStrategyDetailVO, desktopPoolCapacity);
            });
        });
    }

    /**
     * 检查网络策略能否支持指定的容量
     * 
     * @param networkStrategyDetailVO networkStrategyDetailVO
     * @param supportCapacity 需要支持的容量
     */
    private void getNetworkStrategyBySupportCapacity(NetworkStrategyDetailVO networkStrategyDetailVO, int supportCapacity) {

        CbbDeskNetworkBasicInfoDTO deskNetworkBasicInfoDTO = networkStrategyDetailVO.getCbbDeskNetworkBasicInfoDTO();
        if (deskNetworkBasicInfoDTO.getNetworkMode() == CbbNetworkStrategyMode.DHCP) {
            // 网络策略模式是DHCP，认为ip是充足的
            return;
        }

        // 剩余的ip数量
        int remainingIpNum = deskNetworkBasicInfoDTO.getTotalCount() - deskNetworkBasicInfoDTO.getRefCount();
        // 剩余ip数量不能小于需要支持的容量
        if (remainingIpNum < supportCapacity) {
            networkStrategyDetailVO.setCanUsed(Boolean.FALSE);
            networkStrategyDetailVO.setCanUsedMessage(
                    LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_NETWORK_STRATEGY_REMAINING_IP_LESS_THAN_POOL_CAPACITY));
        }
    }

    private NetworkStrategyDetailVO[] buildNetworkStrategyVOArr(CbbDeskNetworkBasicInfoDTO[] basicInfoDTOArr,
            Map<UUID, ClusterInfoDTO> clusterInfoAllMap) {
        List<NetworkStrategyDetailVO> networkStrategyList = Lists.newArrayList();
        for (CbbDeskNetworkBasicInfoDTO dto : basicInfoDTOArr) {
            RcoNetworkStrategyDetailDTO rcoNetworkStrategyDetailDTO = new RcoNetworkStrategyDetailDTO();
            List<UserGroupDesktopConfigDTO> desktopConfigList = userDesktopConfigAPI.getUserGroupDesktopConfigList(dto.getId());
            rcoNetworkStrategyDetailDTO.setBindUserGroup(!CollectionUtils.isEmpty(desktopConfigList));
            try {
                CbbDeskNetworkDetailDTO cbbDeskNetworkDetailDTO = cbbNetworkMgmtAPI.getDeskNetwork(dto.getId());
                rcoNetworkStrategyDetailDTO.setIpPoolArr(cbbDeskNetworkDetailDTO.getIpPoolArr());
            } catch (BusinessException e) {
                LOGGER.error("获取网络策略[{}]信息失败", dto.getId(), e);
            }
            NetworkStrategyDetailVO networkStrategyDetailVO = new NetworkStrategyDetailVO();
            networkStrategyDetailVO.setCbbDeskNetworkBasicInfoDTO(dto);
            networkStrategyDetailVO.setRcoNetworkStrategyDetailDTO(rcoNetworkStrategyDetailDTO);
            UUID clusterId = dto.getClusterId();
            if (Objects.nonNull(clusterId) && clusterInfoAllMap.containsKey(clusterId)) {
                ClusterInfoDTO clusterInfo = new ClusterInfoDTO();
                BeanUtils.copyProperties(clusterInfoAllMap.get(clusterId), clusterInfo);
                networkStrategyDetailVO.setClusterInfoDTO(clusterInfo);
            }
            networkStrategyList.add(networkStrategyDetailVO);
        }
        return networkStrategyList.toArray(new NetworkStrategyDetailVO[0]);
    }

    /**
     * 编辑网络策略回填数据
     *
     * @param webRequest 前端请求信息
     * @return 网络策略列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑网络策略回填数据")
    @RequestMapping(value = "deskNetwork/detail", method = RequestMethod.POST)
    public CommonWebResponse<NetworkDetailDTO> detailDeskNetwork(DetailDeskNetworkWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "DetailDeskNetworkWebRequest can not be null");
        LOGGER.info("rcv detailDeskNetwork msg: {}", JSONObject.toJSONString(webRequest));
        CbbDeskNetworkDetailDTO deskNetworkDTO = cbbNetworkMgmtAPI.getDeskNetwork(webRequest.getId());
        NetworkDetailDTO response = generateDetailDeskNetwokResponse(deskNetworkDTO);
        return CommonWebResponse.success(response);
    }

    /**
     * 获取网络策略详情，用于编辑网络回填
     *
     * @param cbbDeskNetworkDTO 网络策略信息
     * @return 网络详情
     */
    private NetworkDetailDTO generateDetailDeskNetwokResponse(CbbDeskNetworkDetailDTO cbbDeskNetworkDTO) throws BusinessException {
        NetworkDetailDTO response = new NetworkDetailDTO();
        response.setDeskNetworkId(cbbDeskNetworkDTO.getId());
        response.setDeskNetworkName(cbbDeskNetworkDTO.getDeskNetworkName());
        response.setNetworkType(cbbDeskNetworkDTO.getNetworkType());
        response.setVlan(cbbDeskNetworkDTO.getVlan());
        response.setNetworkMode(cbbDeskNetworkDTO.getNetworkMode());
        response.setRefCountByImageTemplate(cbbDeskNetworkDTO.getRefCountByImageTemplate());
        response.setRefCountByDesk(cbbDeskNetworkDTO.getRefCountByDesk());

        CbbDeskNetworkDnsDTO cbbNetworkDnsDTO = new CbbDeskNetworkDnsDTO();
        cbbNetworkDnsDTO.setDnsPrimary(cbbDeskNetworkDTO.getDnsPrimary());
        cbbNetworkDnsDTO.setDnsSecondary(cbbDeskNetworkDTO.getDnsSecondary());
        response.setDns(cbbNetworkDnsDTO);

        CbbDeskNetworkConfigDTO networkConfig = new CbbDeskNetworkConfigDTO();
        networkConfig.setGateway(cbbDeskNetworkDTO.getGateway());
        networkConfig.setIpCidr(cbbDeskNetworkDTO.getIpCidr());
        networkConfig.setIpPoolArr(cbbDeskNetworkDTO.getIpPoolArr());

        if (StringUtils.isEmpty(networkConfig.getGateway())) {
            networkConfig.setGateway(DEFAULT_GATEWAY);
        }
        if (StringUtils.isEmpty(networkConfig.getIpCidr())) {
            networkConfig.setIpCidr(DEFAULT_IP_CIDR);
        }
        if (ArrayUtils.isEmpty(networkConfig.getIpPoolArr())) {
            CbbDeskNetworkIpPoolDTO networkIpPoolDTO = new CbbDeskNetworkIpPoolDTO();
            networkIpPoolDTO.setIpStart(StringUtils.EMPTY);
            networkIpPoolDTO.setIpEnd(StringUtils.EMPTY);
            networkIpPoolDTO.setIpPoolType(DeskNetworkIpPoolType.BUSINESS);
            networkIpPoolDTO.setRefCount(0);
            networkIpPoolDTO.setTotalCount(0);
            CbbDeskNetworkIpPoolDTO[] ipArr = {networkIpPoolDTO};
            networkConfig.setIpPoolArr(ipArr);
        }
        response.setNetworkConfig(networkConfig);
        response.setVswitch(cbbDeskNetworkDTO.getCbbVswitchDTO());
        response.setPlatformId(cbbDeskNetworkDTO.getPlatformId());
        response.setPlatformName(cbbDeskNetworkDTO.getPlatformName());
        response.setPlatformType(cbbDeskNetworkDTO.getPlatformType());
        if (ObjectUtils.isNotEmpty(cbbDeskNetworkDTO.getClusterId())) {
            response.setClusterInfo(clusterAPI.queryAvailableClusterById(cbbDeskNetworkDTO.getClusterId()));
        }

        return response;
    }

    /**
     * 网络策略基本信息
     *
     * @param webRequest 前端请求信息
     * @return 网络策略列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("网络策略基本信息")
    @RequestMapping(value = "deskNetwork/getInfo", method = RequestMethod.POST)
    public CommonWebResponse<NetworkInfoDTO> getDeskNetworkInfo(DeskNetworkInfoWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "DeskNetworkInfoWebRequest can not be null");
        LOGGER.info("rcv DeskNetworkInfoWebRequest msg: {}", JSONObject.toJSONString(webRequest));
        CbbDeskNetworkDetailDTO deskNetworkDTO = cbbNetworkMgmtAPI.getDeskNetwork(webRequest.getId());
        NetworkInfoDTO response = generateDeskNetwokInfoResponse(deskNetworkDTO);

        return CommonWebResponse.success(response);
    }

    private NetworkInfoDTO generateDeskNetwokInfoResponse(CbbDeskNetworkDetailDTO cbbDeskNetworkDTO) throws BusinessException {
        NetworkInfoDTO response = new NetworkInfoDTO();
        response.setDeskNetworkId(cbbDeskNetworkDTO.getId());
        response.setDeskNetworkName(cbbDeskNetworkDTO.getDeskNetworkName());
        response.setDnsPrimary(cbbDeskNetworkDTO.getDnsPrimary());
        response.setDnsSecondary(cbbDeskNetworkDTO.getDnsSecondary());
        response.setNetworkType(cbbDeskNetworkDTO.getNetworkType());
        response.setVlan(cbbDeskNetworkDTO.getVlan());
        response.setGateway(cbbDeskNetworkDTO.getGateway());
        response.setIpCidr(cbbDeskNetworkDTO.getIpCidr());
        response.setCreateTime(cbbDeskNetworkDTO.getCreateTime());
        response.setNetworkMode(cbbDeskNetworkDTO.getNetworkMode());
        CbbDeskNetworkIpPoolUseDTO cbbDeskNetworkIpPoolUseDTO = new CbbDeskNetworkIpPoolUseDTO();
        cbbDeskNetworkIpPoolUseDTO.setRefCount(cbbDeskNetworkDTO.getRefCount());
        cbbDeskNetworkIpPoolUseDTO.setTotalCount(cbbDeskNetworkDTO.getTotalCount());
        response.setIpPoolUse(cbbDeskNetworkIpPoolUseDTO);
        response.setIpPoolArr(cbbDeskNetworkDTO.getIpPoolArr());
        response.setVswitch(cbbDeskNetworkDTO.getCbbVswitchDTO());
        response.setPlatformId(cbbDeskNetworkDTO.getPlatformId());
        response.setPlatformName(cbbDeskNetworkDTO.getPlatformName());
        response.setPlatformType(cbbDeskNetworkDTO.getPlatformType());
        response.setPlatformStatus(cbbDeskNetworkDTO.getPlatformStatus());
        // 管理员名称
        response.setCreatorUserName(cbbDeskNetworkDTO.getCreatorUserName());
        response.setRefCountByDesk(cbbDeskNetworkDTO.getRefCountByDesk());
        response.setRefCountByImageTemplate(cbbDeskNetworkDTO.getRefCountByImageTemplate());
        response.setBindUserGroupCount(userDesktopConfigAPI.getUserGroupDesktopConfigList(cbbDeskNetworkDTO.getId()).size());
        // 计算集群信息
        if (ObjectUtils.isNotEmpty(cbbDeskNetworkDTO.getClusterId())) {
            response.setClusterInfo(clusterAPI.queryAvailableClusterById(cbbDeskNetworkDTO.getClusterId()));
        }
        return response;
    }

    /**
     * 获取交换机列表
     *
     * @param webRequest 前端请求信息
     * @return VLAN ID范围
     * @throws BusinessException 业务异常
     */
    @RequestMapping("deskNetwork/getVswitchs")
    public PageQueryResponse<VirtualSwitchWebResponse> getVswitchs(PageQueryRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(webRequest);
        // 如果前端传递没有排序字段，默认用createTime排序
        if (ArrayUtils.isEmpty(webRequest.getSortArr())) {
            builder.desc(SORT_FIELD_CREATE_TIME);
        }
        PageQueryResponse<CbbVswitchDTO> pageQueryResponse = cbbNetworkMgmtAPI.pageVSwitch(builder.build());
        PageQueryResponse<VirtualSwitchWebResponse> virtualSwitchWebResponse = new PageQueryResponse<>();
        if (ArrayUtils.isEmpty(pageQueryResponse.getItemArr())) {
            virtualSwitchWebResponse.setItemArr(new VirtualSwitchWebResponse[0]);
            return virtualSwitchWebResponse;
        }
        // 获取全部计算集群信息
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList().stream()
                .collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfo -> clusterInfo, (clusterInfo1, clusterInfo2) -> clusterInfo2));
        virtualSwitchWebResponse.setTotal(pageQueryResponse.getTotal());
        virtualSwitchWebResponse.setItemArr(Arrays.stream(pageQueryResponse.getItemArr()).map(cbbVswitchDTO -> {
            VirtualSwitchWebResponse response = new VirtualSwitchWebResponse();
            BeanUtils.copyProperties(cbbVswitchDTO, response);
            UUID clusterId = cbbVswitchDTO.getClusterId();
            if (Objects.nonNull(clusterId) && clusterInfoAllMap.containsKey(clusterId)) {
                ClusterInfoDTO clusterInfoDTO = new ClusterInfoDTO();
                BeanUtils.copyProperties(clusterInfoAllMap.get(clusterId), clusterInfoDTO);
                response.setClusterInfoDTO(clusterInfoDTO);
            }
            return response;
        }).toArray(VirtualSwitchWebResponse[]::new));

        return virtualSwitchWebResponse;
    }


    /**
     * 校验网络名称是否重名(要过滤自身)
     *
     * @param webRequest 前端请求信息
     * @return 网络策略列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("校验网络名称是否重名(要过滤自身)")
    @RequestMapping(value = "deskNetwork/checkDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckDuplicationWebResponse> checkDuplication(CheckNetworkDuplicationWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "DetailDeskNetworkWebRequest can not be null");
        LOGGER.info("rcv checkDuplication msg: {}", JSONObject.toJSONString(webRequest));
        CbbDeskNetworkDetailDTO dto = cbbNetworkMgmtAPI.getNetworkByName(webRequest.getDeskNetworkName());
        CheckDuplicationWebResponse webResp = new CheckDuplicationWebResponse();
        webResp.setHasDuplication(false);
        if (dto != null && dto.getId() != null && !Objects.equals(dto.getId(), webRequest.getNetworkId())) {
            webResp.setHasDuplication(true);
        }
        return CommonWebResponse.success(webResp);
    }

    /**
     * 编辑网络策略
     *
     * @param editWebRequest 前端编辑请求
     * @param builder builder
     * @return 编辑结果
     * @throws BusinessException 异常
     */

    @ApiOperation("编辑网络策略")
    @RequestMapping(value = "deskNetwork/edit", method = RequestMethod.POST)

    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> editNetwork(EditDeskNetworkWebRequest editWebRequest, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(editWebRequest, "editDeskNetworkWebRequest can not be null");
        Assert.notNull(builder, "builder can not be null");

        LOGGER.info("rcv editDeskNetwork msg: {}", JSONObject.toJSONString(editWebRequest));

        DefaultBatchTaskItem batchTaskItem = DefaultBatchTaskItem.builder().itemId(editWebRequest.getId())
                .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_ITEM_NAME)).build();
        EditDeskNetworkBatchHandler handler = new EditDeskNetworkBatchHandler(cbbNetworkMgmtAPI, editWebRequest, batchTaskItem, auditLogAPI);
        BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_EDIT_TASK_DESC).registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    /**
     * 批量删除网络策略
     *
     * @param deleteDeskNetworkWebRequest 删除虚拟案例
     * @param batchTaskBuilder 批量删除
     * @return 删除结果
     * @throws BusinessException 异常
     */
    @ApiOperation("批量删除网络策略")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "deskNetwork/delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> deleteDeskNetwork(DeleteDeskNetworkBatchWebRequest deleteDeskNetworkWebRequest,
            BatchTaskBuilder batchTaskBuilder) throws BusinessException {
        Assert.notNull(deleteDeskNetworkWebRequest, "deleteDeskNetworkWebRequest can not be null");
        Assert.notNull(batchTaskBuilder, "batchTaskBuilder can not be null");

        LOGGER.info("rcv deleteNetwork msg: {}", JSONObject.toJSONString(deleteDeskNetworkWebRequest));
        final UUID[] idArr = deleteDeskNetworkWebRequest.getIdArr();
        Boolean shouldOnlyDeleteDataFromDb = deleteDeskNetworkWebRequest.getShouldOnlyDeleteDataFromDb();
        String prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);

        if (idArr.length == 1) {
            // 删除单个网络
            return deleteSingleNetwork(idArr[0], batchTaskBuilder, shouldOnlyDeleteDataFromDb, prefix);
        } else {
            // 删除多个网络
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)//
                    .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_BATCH_DELETE_ITEM_NAME, prefix)).build())//
                    .iterator();

            DeleteDeskNetworkBatchHandler deskNetworkBatchDeleteHandler =
                    new DeleteDeskNetworkBatchHandler(iterator, auditLogAPI, cbbNetworkMgmtAPI, shouldOnlyDeleteDataFromDb);
            BatchTaskSubmitResult result =
                    batchTaskBuilder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_BATCH_DELETE_TASK_NAME, prefix)
                            .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_BATCH_DELETE_TASK_DESC, prefix)
                            .registerHandler(deskNetworkBatchDeleteHandler).start();

            return CommonWebResponse.success(result);
        }
    }

    private CommonWebResponse<BatchTaskSubmitResult> deleteSingleNetwork(UUID id, BatchTaskBuilder builder, Boolean shouldOnlyDeleteDataFromDb, String prefix)
            throws BusinessException {
        DefaultBatchTaskItem batchTaskItem = DefaultBatchTaskItem.builder().itemId(id)
                .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_ITEM_NAME, prefix)).build();
        DeleteSingleDeskNetworkBatchHandler handler =
                new DeleteSingleDeskNetworkBatchHandler(cbbNetworkMgmtAPI, batchTaskItem, auditLogAPI, shouldOnlyDeleteDataFromDb);
        BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_TASK_NAME, prefix)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_VIRTUAL_NETWORK_DELETE_SINGLE_TASK_DESC, prefix).registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    /**
     * 分页查询IP池
     *
     * @param webRequest 查询请求
     * @return 返回列表
     */
    @RequestMapping("deskIpPool/list")
    public DefaultWebResponse listDeskIpPool(PageWebRequest webRequest) {
        Assert.notNull(webRequest, "PageWebRequest can not be null");

        LOGGER.info("rcv listDeskIpPool msg: {}", JSONObject.toJSONString(webRequest));
        ListDeskIpPoolWebRequest pageWebRequest = generateListIpPoolRequest(webRequest);
        CbbListDeskIpPoolRequest apiRequest = new CbbListDeskIpPoolRequest();
        BeanUtils.copyProperties(pageWebRequest, apiRequest);
        DefaultPageResponse<CbbDeskNetworkIpPoolDTO> response = cbbNetworkMgmtAPI.pageQueryDeskIpPool(apiRequest);
        PageResponseContent<CbbDeskNetworkIpPoolDTO> pageResponseContent = new PageResponseContent<>(response.getItemArr(), response.getTotal());

        return DefaultWebResponse.Builder.success(pageResponseContent);
    }

    private ListDeskIpPoolWebRequest generateListIpPoolRequest(PageWebRequest webRequest) {
        ListDeskIpPoolWebRequest cbbListIpPoolRequest = new ListDeskIpPoolWebRequest();
        cbbListIpPoolRequest.setPage(webRequest.getPage());
        cbbListIpPoolRequest.setLimit(webRequest.getLimit());

        final Map<String, String[]> exactMatchMap = webRequest.toExactMatchMap();
        final String[] valueArr = exactMatchMap.get("id");

        Assert.state(valueArr != null, "查询条件有误");
        LOGGER.info("generateListIpPoolRequest, valueArr:{}", JSONObject.toJSONString(valueArr));
        if (valueArr != null) {
            cbbListIpPoolRequest.setDeskNetworkId(UUID.fromString(valueArr[0]));
        }

        LOGGER.info("generateListIpPoolRequest: {}", JSONObject.toJSONString(cbbListIpPoolRequest));
        return cbbListIpPoolRequest;
    }

    /**
     * 获取VLAN ID范围，该接口前端有调用，若要删除需要通知前端同步修订。当前cbb层已不再同步vlan信息并保存数据库
     *
     * @param webRequest 前端请求信息
     * @return VLAN ID范围
     * @throws BusinessException 业务异常
     */
    @Deprecated
    @RequestMapping("deskNetwork/getVlanIdRange")
    public DefaultWebResponse getVlanIdRange(VlanIdRangeWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "vlanIdRangeWebRequest can not be null");

        Assert.notNull(webRequest, "vlanIdRangeWebRequest can not be null");

        LOGGER.info("rcv vlanIdRangeWebRequest msg: {}", JSONObject.toJSONString(webRequest));
        String vlanIdRange = cbbNetworkMgmtAPI.getVlanIdRange();

        if (StringUtils.isEmpty(vlanIdRange)) {
            return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", Collections.emptyList()));

        }

        JSONArray jsonArray = JSON.parseArray(vlanIdRange);
        List<Integer> vlanIdList = jsonArray.toJavaList(Integer.class);
        vlanIdList.sort(Comparator.comparing(Integer::intValue));

        return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", vlanIdList));
    }

}
