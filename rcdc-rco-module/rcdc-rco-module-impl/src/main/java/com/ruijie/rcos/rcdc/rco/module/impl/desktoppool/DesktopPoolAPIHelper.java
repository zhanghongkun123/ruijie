package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdGroupEntityDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbCheckDeskPoolNameDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbCreateDeskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.VDIDesktopValidateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.ImageDeskSpecGpuCheckParamDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.StrategyHardwareDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.EditPoolDeskSpecRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UpdateDesktopPoolRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.AdGroupPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.DesktopPoolBindGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.DesktopPoolDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.PoolSessionConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.PoolStrategyConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolConfigService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ObtainUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Description: DesktopPoolAPIHelper
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/24
 *
 * @author linke
 */
@Service("rcoDesktopPoolAPIHelper")
public class DesktopPoolAPIHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolAPIHelper.class);

    /**
     * 推送用户数据到rccm,每次处理1千条
     */
    private static final int PUSH_USER_MAX_NUM = 1000;

    private static final String NAME_SPLIT = ",";

    private static final int SQL_IN_MAX_NUM = 500;

    private static final int SINGLE_LOG_MAX_NUM = 20;

    private static final int NO_SIZE = 0;

    private static final int NOTIFY_GT_ASSIGN_HAS_MULTI_USER = 1;

    /**
     * 分配100个线程数处理，gt通知使用
     */
    private static final ExecutorService GT_THREAD_POOL = ThreadExecutors.newBuilder("desktopPoolAPIHelper-noticeGT")
            .maxThreadNum(100).queueSize(1000).build();

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserService userService;

    @Autowired
    private DesktopPoolUserService desktopPoolUserService;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private AdGroupPoolService adGroupPoolService;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private UserProfileValidateAPI userProfileValidateAPI;

    @Autowired
    private DesktopPoolConfigService desktopPoolConfigService;

    @Autowired
    private ComputerBusinessAPI computerBusinessAPI;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    /**
     * 检查用户、用户组是否存在，且不能为访客用户
     *
     * @param bindObjectDTO UpdatePoolBindObjectDTO
     * @throws BusinessException BusinessException
     */
    public void checkUserAndGroupExist(UpdatePoolBindObjectDTO bindObjectDTO) throws BusinessException {
        Assert.notNull(bindObjectDTO, "bindObjectDTO can not be null");

        List<UUID> addUserIdList = bindObjectDTO.getAddUserByIdList();
        if (CollectionUtils.isNotEmpty(addUserIdList)) {
            List<List<UUID>> tempIdList = Lists.partition(addUserIdList, SQL_IN_MAX_NUM);
            for (List<UUID> idList : tempIdList) {
                if (!userService.checkAllUserExistByIds(idList)) {
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UPDATE_BIND_USER_NULL);
                }
                if (userService.checkAnyVisitor(idList)) {
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UPDATE_BIND_VISITOR_FAIL);
                }
            }
        }

        List<UUID> groupIdList = bindObjectDTO.getSelectedGroupIdList();
        if (CollectionUtils.isNotEmpty(groupIdList)) {
            List<List<UUID>> tempIdList = Lists.partition(groupIdList, SQL_IN_MAX_NUM);
            for (List<UUID> idList : tempIdList) {
                if (!userService.checkAllUserGroupExistByIds(idList)) {
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UPDATE_BIND_GROUP_NULL);
                }
            }
        }
    }

    /**
     * 检查AD安全组是否存在
     *
     * @param bindObjectDTO UpdatePoolBindObjectDTO
     * @throws BusinessException BusinessException
     */
    public void checkAdGroupExist(UpdatePoolBindObjectDTO bindObjectDTO) throws BusinessException {
        Assert.notNull(bindObjectDTO, "checkAdGroupExist bindObjectDTO can not be null");
        List<UUID> adGroupIdList = bindObjectDTO.getSelectedAdGroupIdList();
        if (CollectionUtils.isNotEmpty(adGroupIdList)) {
            List<List<UUID>> tempIdList = Lists.partition(adGroupIdList.stream().distinct().collect(Collectors.toList()), SQL_IN_MAX_NUM);
            for (List<UUID> idList : tempIdList) {
                if (!adGroupPoolService.checkAllAdGroupExistByIds(idList)) {
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UPDATE_BIND_AD_GROUP_NULL);
                }
            }
        }
    }

    /**
     * 根据桌面池ID删除RCCM用户数据
     *
     * @param desktopPoolId 桌面池ID
     * @throws BusinessException 业务异常
     */
    public void deleteRccmUserByDesktopPoolId(UUID desktopPoolId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        if (!rccmManageAPI.isReadyToPush()) {
            return;
        }
        List<DesktopPoolUserDTO> desktopPoolUserList = desktopPoolUserService.listDesktopPoolUser(desktopPoolId, null);
        List<UUID> userIdList = new ArrayList<>();
        List<UUID> groupIdList = new ArrayList<>();
        desktopPoolUserList.forEach(desktopPoolUserDTO -> {
            if (IacConfigRelatedType.USER == desktopPoolUserDTO.getRelatedType()) {
                userIdList.add(desktopPoolUserDTO.getRelatedId());
            } else if (IacConfigRelatedType.USERGROUP == desktopPoolUserDTO.getRelatedType()) {
                groupIdList.add(desktopPoolUserDTO.getRelatedId());
            }
        });
        deleteUserToRccm(userIdList);

        if (CollectionUtils.isEmpty(groupIdList)) {
            return;
        }
        // 组下用户可能会超过1000,需要分多次查询处理
        for (UUID groupId : groupIdList) {
            IacPageResponse<IacUserDetailDTO> pageResult = userService.pageQueryByGroupId(groupId, 0);
            // 总页数
            int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
            for (int page = 0; page < totalPage; page++) {
                // 前面已查过，不重复查
                if (page == 0) {
                    deleteUserFromRccm(pageResult.getItemArr());
                    continue;
                }
                pageResult = userService.pageQueryByGroupId(groupId, page);
                if (pageResult.getTotal() == 0) {
                    break;
                }
                deleteUserFromRccm(pageResult.getItemArr());
            }
        }
    }

    private void deleteUserFromRccm(IacUserDetailDTO[] userDetailDTOArr) {
        List<UUID> userIdList = Arrays.stream(userDetailDTOArr)//
                .filter(item -> item.getUserType() != IacUserTypeEnum.VISITOR)//
                .map(IacUserDetailDTO::getId)//
                .collect(Collectors.toList());
        deleteUserToRccm(userIdList);
    }

    /**
     * 修改分配用户时，处理用户是否需要推送到rccm
     *
     * @param updateDTO UpdatePoolBindObjectDTO
     * @throws BusinessException 业务异常
     */
    public void pushUserToRccmWhenUpdateBindUser(UpdatePoolBindObjectDTO updateDTO) throws BusinessException {
        Assert.notNull(updateDTO, "updateDTO can not be null");
        if (!rccmManageAPI.isReadyToPush()) {
            return;
        }
        // 新增总用户 = 新增组的所有用户 + 新增用户(上一步已经把ExceptList的用户添加到addUserByIdList字段了)
        List<UUID> selectedGroupIdList = updateDTO.getSelectedGroupIdList();
        List<UUID> addUserByIdList = updateDTO.getAddUserByIdList();

        List<UUID> addUserIdList = getUserIdListByGroupIds(selectedGroupIdList);
        if (CollectionUtils.isNotEmpty(addUserByIdList)) {
            addUserIdList.addAll(addUserByIdList);
        }
        // 推送新增用户数据到RCCM
        this.pushUserToRccm(addUserIdList);

        // 推送安全组数据到RCCM，后面补充

        // 删除总用户，之前的步骤已汇总过了
        List<UUID> deleteUserByIdList = updateDTO.getDeleteUserByIdList();
        if (CollectionUtils.isEmpty(deleteUserByIdList)) {
            return;
        }
        // 推送删除的用户 = 删除总用户 - 新增总用户
        Set<UUID> addUserIdSet = new HashSet<>(addUserIdList);
        List<UUID> deleteUserList = deleteUserByIdList.stream().filter(item -> !addUserIdSet.contains(item)).collect(Collectors.toList());
        // 推送删除用户数据到RCCM
        this.deleteUserToRccm(deleteUserList);
    }

    private List<UUID> getUserIdListByGroupIds(List<UUID> selectedGroupIdList) throws BusinessException {
        List<UUID> userIdList = new ArrayList<>();
        if (CollectionUtils.isEmpty(selectedGroupIdList)) {
            return userIdList;
        }
        // 组下用户可能会超过1000，按组分页查询
        for (UUID groupId : selectedGroupIdList) {
            IacPageResponse<IacUserDetailDTO> pageResult = userService.pageQueryByGroupId(groupId, 0);
            // 总页数
            int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
            for (int page = 0; page < totalPage; page++) {
                // 前面已查过，不重复查
                if (page == 0) {
                    userIdList.addAll(filterUserIdList(pageResult.getItemArr()));
                    continue;
                }
                pageResult = userService.pageQueryByGroupId(groupId, page);
                if (pageResult.getTotal() == 0) {
                    break;
                }
                userIdList.addAll(filterUserIdList(pageResult.getItemArr()));
            }
        }
        return userIdList;
    }

    private List<UUID> filterUserIdList(IacUserDetailDTO[] userDetailDTOArr) {
        return Arrays.stream(userDetailDTOArr).filter(item -> item.getUserType() != IacUserTypeEnum.VISITOR)//
                .map(IacUserDetailDTO::getId)//
                .collect(Collectors.toList());
    }

    private void pushUserToRccm(List<UUID> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<List<UUID>> tempIdList = Lists.partition(userIdList, PUSH_USER_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            List<RcoViewUserEntity> userInfoByIdList = userService.getUserInfoByIdList(idList);
            List<String> userNameList = userInfoByIdList.stream().map(RcoViewUserEntity::getUserName).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userNameList)) {
                continue;
            }
            rccmManageAPI.pushUserToRccm(userNameList, true);
        }
    }

    /**
     * 推送删除用户数据到RCCM
     *
     * @param userIdList 用户ID列表
     */
    private void deleteUserToRccm(List<UUID> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<List<UUID>> tempIdList = Lists.partition(userIdList, PUSH_USER_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            List<RcoViewUserEntity> userInfoByIdList = userService.getUserInfoByIdList(idList);
            rccmManageAPI.delRccmUserCLuster(userInfoByIdList.stream().map(RcoViewUserEntity::getUserName).collect(Collectors.toList()), false);
        }
    }

    /**
     * 保存分配桌面的审计日志
     *
     * @param desktopPoolDTO    desktopPoolDTO
     * @param poolBindGroupDTO  poolBindGroupDTO
     * @param bindObjectDTO     bindObjectDTO
     * @throws BusinessException BusinessException
     */
    public void saveUpdateBindObjLog(CbbDesktopPoolDTO desktopPoolDTO, DesktopPoolBindGroupDTO poolBindGroupDTO,
                                     UpdatePoolBindObjectDTO bindObjectDTO) throws BusinessException {
        Assert.notNull(desktopPoolDTO, "desktopPoolDTO can not be null");
        Assert.notNull(poolBindGroupDTO, "poolBindGroupDTO can not be null");
        Assert.notNull(bindObjectDTO, "bindObjectDTO can not be null");

        List<IacUserGroupDetailDTO> groupList;
        List<UUID> addGroupIdList = poolBindGroupDTO.getAddGroupIdList();
        if (CollectionUtils.isNotEmpty(addGroupIdList)) {
            groupList = userService.listUserGroupByIds(addGroupIdList);
            List<String> nameList = groupList.stream().map(IacUserGroupDetailDTO::getName).collect(Collectors.toList());
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ADD_BIND_GROUP_LOG, desktopPoolDTO.getName(),
                    StringUtils.join(nameList, NAME_SPLIT));
        }

        List<UUID> deleteGroupIdList = poolBindGroupDTO.getDeleteGroupIdList();
        if (CollectionUtils.isNotEmpty(deleteGroupIdList)) {
            groupList = userService.listUserGroupByIds(deleteGroupIdList);
            List<String> nameList = groupList.stream().map(IacUserGroupDetailDTO::getName).collect(Collectors.toList());
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DELETE_BIND_GROUP_LOG, desktopPoolDTO.getName(),
                    StringUtils.join(nameList, NAME_SPLIT));
        }
        // 记录新增安全组的审计日志
        savePoolUpdateAdGroupLog(desktopPoolDTO.getName(),
                poolBindGroupDTO.getAddAdGroupIdList(), DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ADD_BIND_AD_GROUP_LOG);

        // 记录删除安全组的审计日志
        savePoolUpdateAdGroupLog(desktopPoolDTO.getName(),
                poolBindGroupDTO.getDeleteAdGroupIdList(), DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DELETE_BIND_AD_GROUP_LOG);

        List<UUID> addUserIdList = bindObjectDTO.getAddUserByIdList();
        saveDesktopPoolAddUserLog(desktopPoolDTO, addUserIdList);

        List<UUID> deleteUserIdList = bindObjectDTO.getDeleteUserByIdList();
        // 判断用户所在的组是否被加进来了，如果加进来了，这里就不用记录删除这个用户的记录
        saveDesktopPoolDeleteUserLog(desktopPoolDTO, deleteUserIdList, bindObjectDTO);
    }

    /**
     * 过滤出未绑定用户且策略一致且未绑定磁盘的桌面
     *
     * @param desktopPoolId    池桌面ID
     * @param poolDeskInfoList 桌面列表
     * @return List<PoolDesktopInfoDTO> 云桌面列表
     * @throws BusinessException 业务异常
     */
    public List<PoolDesktopInfoDTO> filterPoolDeskNotUserAndAccordWithList(UUID desktopPoolId, List<PoolDesktopInfoDTO> poolDeskInfoList)
            throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(poolDeskInfoList, "poolDeskInfoList can not be null");
        DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolService.getDesktopPoolBasicById(desktopPoolId);
        // 过滤出未绑定的且策略，镜像模板都和池配置相等的桌面
        List<PoolDesktopInfoDTO> filterResultList = new ArrayList<>();
        for (PoolDesktopInfoDTO poolDeskInfo : poolDeskInfoList) {
            if (Objects.nonNull(poolDeskInfo.getUserId()) || Boolean.TRUE.equals(poolDeskInfo.getIsOpenDeskMaintenance())) {
                continue;
            }
            if (checkDeskConfigEqualsPool(poolDeskInfo, desktopPoolBasicDTO)) {
                filterResultList.add(poolDeskInfo);
            }
        }
        // 获取已绑定POOL_DATA类型磁盘桌面
        Set<UUID> faultDiskDesktopSet = userDiskMgmtAPI.getFaultDiskDesktopSet(desktopPoolId);
        // 过滤掉已绑定磁盘的桌面
        return filterResultList.stream().filter(desk -> !faultDiskDesktopSet.contains(desk.getDeskId())).collect(Collectors.toList());
    }

    /**
     * 判断桌面的配置是否需要和池一致，需要强一致就检查配置是否一致，静态池桌面无需和桌面池保持一致就可分配给用户
     *
     * @param desktopDTO          desktopDTO
     * @param desktopPoolBasicDTO desktopPoolBasicDTO
     * @return true：目前已一致或无需强一致；false：需要强一致且目前不一致
     * @throws BusinessException BusinessException
     */
    public boolean checkDeskConfigEqualsPool(PoolDesktopInfoDTO desktopDTO, DesktopPoolBasicDTO desktopPoolBasicDTO) throws BusinessException {
        Assert.notNull(desktopDTO, "desktopDTO can not be null");
        Assert.notNull(desktopPoolBasicDTO, "desktopPoolBasicDTO can not be null");

        if (desktopPoolBasicDTO.getPoolModel() == CbbDesktopPoolModel.STATIC) {
            // 静态不一致可分配
            return true;
        }

        // 云桌面策略、网络策略、镜像模板、软件管控策略、个性化配置策略是否和桌面池一致
        if (desktopPoolBasicDTO.getPoolType() == CbbDesktopPoolType.VDI) {
            if (!Objects.equals(desktopPoolBasicDTO.getStrategyId(), desktopDTO.getStrategyId())
                    || !Objects.equals(desktopPoolBasicDTO.getNetworkId(), desktopDTO.getNetworkId())
                    || !Objects.equals(desktopPoolBasicDTO.getImageTemplateId(), desktopDTO.getImageTemplateId())
                    || !Objects.equals(desktopPoolBasicDTO.getSoftwareStrategyId(), desktopDTO.getSoftwareStrategyId())
                    || !Objects.equals(desktopPoolBasicDTO.getUserProfileStrategyId(), desktopDTO.getUserProfileStrategyId())) {
                return false;
            }
            // 检查桌面硬件信息是否和桌面池规格一致
            return isDesktopHardwareEqualsPoolDeskSpec(desktopDTO, desktopPoolBasicDTO.getDeskSpecId());
        } else {
            return Objects.equals(desktopPoolBasicDTO.getStrategyId(), desktopDTO.getStrategyId())
                    && Objects.equals(desktopPoolBasicDTO.getSoftwareStrategyId(), desktopDTO.getSoftwareStrategyId())
                    && Objects.equals(desktopPoolBasicDTO.getUserProfileStrategyId(), desktopDTO.getUserProfileStrategyId());
        }
    }

    /**
     * 比对桌面硬件配置是否和池配置的规格一致
     *
     * @param desktopInfo desktopInfo
     * @param poolSpecId  池配置的规格ID
     * @return true一致；false不一致
     * @throws BusinessException BusinessException
     */
    public boolean isDesktopHardwareEqualsPoolDeskSpec(PoolDesktopInfoDTO desktopInfo, UUID poolSpecId) throws BusinessException {
        Assert.notNull(desktopInfo, "desktopInfo can not be null");
        Assert.notNull(poolSpecId, "poolSpecId can not be null");

        CbbDeskSpecDTO cbbDeskSpecDTO = cbbDeskSpecAPI.getById(poolSpecId);

        StrategyHardwareDTO deskHardware = new StrategyHardwareDTO();
        deskHardware.setCpu(desktopInfo.getCpu());
        deskHardware.setMemory(desktopInfo.getMemory());
        deskHardware.setPersonSize(Objects.nonNull(desktopInfo.getPersonSize()) ? desktopInfo.getPersonSize() : NO_SIZE);
        deskHardware.setSystemSize(desktopInfo.getSystemSize());
        deskHardware.setVgpuInfoDTO(desktopInfo.getVgpuInfoDTO());
        deskHardware.setEnableHyperVisorImprove(desktopInfo.getEnableHyperVisorImprove());
        return deskSpecAPI.specHardwareEquals(deskHardware, cbbDeskSpecDTO);
    }

    /**
     * 保存添加了用户的日志
     *
     * @param desktopPoolDTO desktopPoolDTO
     * @param addUserIdList  addUserIdList
     * @throws BusinessException BusinessException
     */
    public void saveDesktopPoolAddUserLog(CbbDesktopPoolDTO desktopPoolDTO, List<UUID> addUserIdList) throws BusinessException {
        Assert.notNull(desktopPoolDTO, "desktopPoolDTO can not be null");
        Assert.notNull(addUserIdList, "addUserIdList can not be null");
        if (CollectionUtils.isEmpty(addUserIdList)) {
            return;
        }
        List<List<UUID>> tempIdList = Lists.partition(addUserIdList, SQL_IN_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            List<IacUserDetailDTO> userList = userService.listUserByIds(idList);
            saveUpdateUserBindSysLog(desktopPoolDTO, userList, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ADD_BIND_USER_LOG);
        }
    }

    private void saveDesktopPoolDeleteUserLog(CbbDesktopPoolDTO desktopPoolDTO, List<UUID> deleteUserIdList, UpdatePoolBindObjectDTO bindObjectDTO)
            throws BusinessException {
        if (CollectionUtils.isEmpty(deleteUserIdList)) {
            return;
        }
        Set<UUID> groupIdSet = new HashSet<>(Optional.ofNullable(bindObjectDTO.getSelectedGroupIdList()).orElse(Collections.emptyList()));
        // 判断用户所在的组是否被加进来了，如果加进来了，这里就不用记录删除这个用户的记录
        List<IacUserDetailDTO> userList = Lists.newArrayList();
        List<List<UUID>> tempIdList = Lists.partition(deleteUserIdList, SQL_IN_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            List<IacUserDetailDTO> tempUserList = userService.listUserByIds(idList);
            tempUserList = tempUserList.stream().filter(item -> !groupIdSet.contains(item.getGroupId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(tempUserList)) {
                continue;
            }
            userList.addAll(tempUserList);
            if (userList.size() > SQL_IN_MAX_NUM) {
                saveUpdateUserBindSysLog(desktopPoolDTO, userList, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DELETE_BIND_USER_LOG);
                userList.clear();
            }
        }
        if (CollectionUtils.isNotEmpty(userList)) {
            saveUpdateUserBindSysLog(desktopPoolDTO, userList, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DELETE_BIND_USER_LOG);
        }
    }

    private void saveUpdateUserBindSysLog(CbbDesktopPoolDTO desktopPoolDTO, List<IacUserDetailDTO> userList, String key) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        List<List<IacUserDetailDTO>> tempUserList = Lists.partition(userList, SINGLE_LOG_MAX_NUM);
        for (List<IacUserDetailDTO> subUserList : tempUserList) {
            List<String> nameList = subUserList.stream().map(IacUserDetailDTO::getUserName).collect(Collectors.toList());
            auditLogAPI.recordLog(key, desktopPoolDTO.getName(), StringUtils.join(nameList, NAME_SPLIT));
        }
    }

    /**
     * 用户登录预启动的池桌面时,向GT发送用户信息
     *
     * @param desktopEntity 云桌面信息
     * @param userId 用户ID
     */
    public void asyncNotifyGuestToolUserInfo(ViewUserDesktopEntity desktopEntity, UUID userId) {
        GT_THREAD_POOL.execute(() -> {
            CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
            messageDTO.setCmdId(Integer.valueOf(GuestToolCmdId.NOTIFY_GT_CMD_ID_USER_INFO));
            messageDTO.setPortId(GuestToolCmdId.NOTIFY_GT_PORT_ID_USER_INFO);
            messageDTO.setDeskId(desktopEntity.getCbbDesktopId());
            ObtainUserInfoDTO body = new ObtainUserInfoDTO();
            body.setCode(CommonMessageCode.SUCCESS);

            try {
                IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
                ObtainUserInfoDTO.Content userContent = new ObtainUserInfoDTO.Content();
                userContent.setUserType(userDetail.getUserType());
                userContent.setUserName(userDetail.getUserName());
                userContent.setPassword(userDetail.getPassword());
                userContent.setChangeUserName(Boolean.FALSE);
                userContent.setAutoLogin(Boolean.FALSE);
                userContent.setUserGroup(CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue());

                if (IacUserTypeEnum.VISITOR != userDetail.getUserType() && IacUserTypeEnum.AD != userDetail.getUserType()) {
                    CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(desktopEntity.getCbbStrategyId());
                    userContent.setChangeUserName(deskStrategy.getDesktopSyncLoginAccount() == null ?
                            Boolean.FALSE : deskStrategy.getDesktopSyncLoginAccount());
                    userContent.setAutoLogin(deskStrategy.getDesktopSyncLoginPassword() == null ?
                            Boolean.FALSE : deskStrategy.getDesktopSyncLoginPassword());
                    userContent.setUserGroup(deskStrategy.getDesktopSyncLoginAccountPermission() == null ?
                            CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue() :
                            deskStrategy.getDesktopSyncLoginAccountPermission().getValue());
                }
                body.setContent(userContent);
                messageDTO.setBody(JSON.toJSONString(body));
                guestToolMessageAPI.asyncRequest(messageDTO);
            } catch (Exception e) {
                LOGGER.error("用户登录预启动的池桌面[{}]时,向GT发送用户[{}]信息出现异常：", desktopEntity.getCbbDesktopId(), userId, e);
            }
        });
    }

    /**
     * 保存池分配安全组日志
     * @param name 池名称
     * @param updateAdGroupList 修改安全组集合
     * @param key 国际化KEY
     * @throws BusinessException BusinessException
     */
    public void savePoolUpdateAdGroupLog(String name, List<UUID> updateAdGroupList, String key) throws BusinessException {
        Assert.hasText(name, "name can be not null");
        Assert.notNull(updateAdGroupList, "updateAdGroupList can be not null");
        Assert.hasText(key, "key can be not null");
        if (CollectionUtils.isEmpty(updateAdGroupList)) {
            return;
        }
        List<List<UUID>> tempIdList = Lists.partition(updateAdGroupList, SQL_IN_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            List<IacAdGroupEntityDTO> adGroupList = adGroupPoolService.listAdGroupByIds(idList);
            saveUpdateAdGroupBindSysLog(name, adGroupList, IacAdGroupEntityDTO::getName, key);
        }
    }

    /**
     * 推送AD域安全组数据到rccm
     *
     * @param countDesktopPoolUser 数量
     */
    public void pushAdGroupToRccm(Integer countDesktopPoolUser) {
        Assert.notNull(countDesktopPoolUser, "countDesktopPoolUser can be not null");
        rccmManageAPI.pushAdGroupToRccm();
    }

    private <T> void saveUpdateAdGroupBindSysLog(String name, List<T> userList, Function<T, String> function, String key) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        List<List<T>> tempUserList = Lists.partition(userList, SINGLE_LOG_MAX_NUM);
        for (List<T> subUserList : tempUserList) {
            List<String> nameList = subUserList.stream().map(function).collect(Collectors.toList());
            auditLogAPI.recordLog(key, name, StringUtils.join(nameList, NAME_SPLIT));
        }
    }

    /**
     * 校验编辑桌面池云桌面策略的参数
     *
     * @param desktopPoolDTO desktopPoolDTO
     * @param newStrategyId  strategyId 云桌面策略
     * @throws BusinessException BusinessException
     */
    public void validateEditDesktopPoolStrategy(CbbDesktopPoolDTO desktopPoolDTO, UUID newStrategyId) throws BusinessException {
        Assert.notNull(desktopPoolDTO, "desktopPoolDTO is null");
        Assert.notNull(newStrategyId, "newStrategyId is null");

        if (desktopPoolDTO.getPoolState() != CbbDesktopPoolState.AVAILABLE) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UNAVAILABLE_UPDATE_FORBID, desktopPoolDTO.getName());
        }
        CbbDeskStrategyDTO newDeskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(newStrategyId);
        if (BooleanUtils.isTrue(newDeskStrategy.getOpenDesktopRedirect())) {
            CbbDeskSpecDTO deskSpecDTO = cbbDeskSpecAPI.getById(desktopPoolDTO.getDeskSpecId());
            if (Objects.isNull(deskSpecDTO.getPersonSize()) || deskSpecDTO.getPersonSize() <= 0) {
                throw new BusinessException(BusinessKey.RCDC_RCO_CREATE_USER_CONFIG_VDI_DESK_REDIRECT_MUST_PERSON_DISK);
            }
        }

        if (desktopPoolDTO.getPoolModel() != CbbDesktopPoolModel.STATIC) {
            DesktopPoolConfigDTO poolConfigDTO = desktopPoolConfigService.queryByDesktopPoolId(desktopPoolDTO.getId());
            if (Objects.nonNull(poolConfigDTO.getUserProfileStrategyId())) {
                if (Objects.equals(newDeskStrategy.getPattern(), CbbCloudDeskPattern.PERSONAL)) {
                    // 个性桌面不需要个性配置策略
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_PERSONAL_NOT_SUPPORT_USER_PROFILE);
                }
            }
        }
    }

    /**
     * 校验编辑桌面池镜像模板的参数
     *
     * @param desktopPoolDTO  desktopPoolDTO
     * @param imageTemplateId imageTemplateId 云桌面镜像模板
     * @throws BusinessException BusinessException
     */
    public void validateEditDesktopPoolImageTemplateId(CbbDesktopPoolDTO desktopPoolDTO, UUID imageTemplateId) throws BusinessException {
        Assert.notNull(desktopPoolDTO, "desktopPoolDTO is null");
        Assert.notNull(imageTemplateId, "imageTemplateId is null");

        if (desktopPoolDTO.getPoolState() != CbbDesktopPoolState.AVAILABLE) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UNAVAILABLE_UPDATE_FORBID, desktopPoolDTO.getName());
        }

        // 校验镜像模板是否支持这个池模式
        CbbImageTemplateDetailDTO imageTemplate = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
        if (imageTemplate.getCbbImageType() != CbbImageType.VDI) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_MUST_VDI);
        }

        // 动态池校验个人盘
        if (desktopPoolDTO.getPoolModel() == CbbDesktopPoolModel.DYNAMIC) {
            boolean isNormalImageTemplate = imageTemplate.getImageRoleType() == ImageRoleType.TEMPLATE && //
                    !Boolean.TRUE.equals(imageTemplate.getEnableMultipleVersion());
            // 动态池镜像模板必须支持还原VDI
            if (isNormalImageTemplate && imageTemplate.getClouldDeskopNumOfPersonal() > 0) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DYNAMIC_IMAGE_HAS_PERSONAL_VDI);
            }
        }

        // 校验VDI桌面策略选择
        VDIDesktopValidateDTO validateDTO = new VDIDesktopValidateDTO();
        validateDTO.setImageId(imageTemplateId);
        validateDTO.setNetworkId(desktopPoolDTO.getNetworkId());
        validateDTO.setStrategyId(desktopPoolDTO.getStrategyId());
        validateDTO.setClusterId(desktopPoolDTO.getClusterId());
        validateDTO.setPlatformId(desktopPoolDTO.getPlatformId());
        clusterAPI.validateVDIDesktopConfig(validateDTO);

        // 校验显卡支持情况
        ImageDeskSpecGpuCheckParamDTO checkParamDTO = new ImageDeskSpecGpuCheckParamDTO();
        checkParamDTO.setImageId(imageTemplateId);
        checkParamDTO.setClusterId(desktopPoolDTO.getClusterId());
        checkParamDTO.setImageEditionId(imageTemplate.getLastRecoveryPointId());
        checkParamDTO.setVgpuInfoDTO(cbbDeskSpecAPI.getById(desktopPoolDTO.getDeskSpecId()).getVgpuInfoDTO());
        checkParamDTO.setStrategyId(desktopPoolDTO.getStrategyId());
        deskSpecAPI.checkGpuSupportByImageAndSpec(checkParamDTO);
    }

    /**
     * 校验创建桌面池的参数
     *
     * @param createDeskPoolDTO createDeskPoolDTO
     * @throws BusinessException BusinessException
     */
    public void validateCreateDesktopPoolParam(CbbCreateDeskPoolDTO createDeskPoolDTO) throws BusinessException {
        Assert.notNull(createDeskPoolDTO, "request is null");

        // 校验基础信息
        validateBaseInfo(createDeskPoolDTO.getName(), null);

        // 校验桌面配置参数：桌面数量、维持预启动数、镜像模板、云平台、计算集群、规格
        DesktopPoolDesktopConfigDTO poolDesktopConfig = new DesktopPoolDesktopConfigDTO(createDeskPoolDTO);
        validateDesktopConfig(createDeskPoolDTO.getPoolModel(), poolDesktopConfig);

        // 校验会话配置：最大会话数、空闲桌面自动回收时间（分钟）
        PoolSessionConfigDTO sessionConfig = new PoolSessionConfigDTO(createDeskPoolDTO);
        validateSessionConfig(createDeskPoolDTO.getPoolModel(), sessionConfig);

        // 校验策略配置：云桌面策略、网络策略、软件管控策略、个性化配置策略
        PoolStrategyConfigDTO strategyConfigDTO = new PoolStrategyConfigDTO(createDeskPoolDTO);
        validateStrategyConfig(strategyConfigDTO);
    }

    /**
     * 验证
     * @param cbbCreateDeskPoolDTO cbbCreateDeskPoolDTO
     * @throws BusinessException 业务异常
     */
    public void validateCreateThirdPartyDesktopPoolParam(CbbCreateDeskPoolDTO cbbCreateDeskPoolDTO) throws BusinessException {
        Assert.notNull(cbbCreateDeskPoolDTO, "cbbCreateDeskPoolDTO is not be null");
        // 校验基础信息
        validateBaseInfo(cbbCreateDeskPoolDTO.getName(), null);

        // 校验会话配置：最大会话数、空闲桌面自动回收时间（分钟）
        PoolSessionConfigDTO sessionConfig = new PoolSessionConfigDTO(cbbCreateDeskPoolDTO);
        validateSessionConfig(cbbCreateDeskPoolDTO.getPoolModel(), sessionConfig);

        // 校验策略配置：云桌面策略、网络策略、软件管控策略、个性化配置策略
        PoolStrategyConfigDTO strategyConfigDTO = new PoolStrategyConfigDTO(cbbCreateDeskPoolDTO);
        validateStrategyConfig(strategyConfigDTO);
    }

    /**
     * 校验编辑桌面池的参数
     *
     * @param request             UpdateDesktopPoolRequest
     * @param desktopPoolBasicDTO desktopPoolBasicDTO
     * @throws BusinessException BusinessException
     */
    public void validateUpdateDesktopPoolParam(UpdateDesktopPoolRequest request, DesktopPoolBasicDTO desktopPoolBasicDTO) throws BusinessException {
        Assert.notNull(request, "request is null");
        Assert.notNull(desktopPoolBasicDTO, "desktopPoolBasicDTO is null");

        if (request.getNeedUpdateConfig() && desktopPoolBasicDTO.getDesktopNum() > 0) {
            // 修改桌面池VDI配置，池下桌面总数必须为0才能修改
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_EXIST_DESKTOP_NO_EDIT_VDI_CONFIG);
        }

        CbbDesktopPoolDTO cbbDesktopPoolDTO = request.getCbbDesktopPoolDTO();

        if (desktopPoolBasicDTO.getPoolState() != CbbDesktopPoolState.AVAILABLE) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UNAVAILABLE_UPDATE_FORBID, desktopPoolBasicDTO.getName());
        }

        // 校验基础信息
        validateBaseInfo(cbbDesktopPoolDTO.getName(), desktopPoolBasicDTO.getId());

        // 校验桌面配置参数：桌面数量、维持预启动数、镜像模板、云平台、计算集群、规格
        DesktopPoolDesktopConfigDTO poolDesktopConfig = new DesktopPoolDesktopConfigDTO(request);
        poolDesktopConfig.setDesktopNum(desktopPoolBasicDTO.getDesktopNum());
        validateDesktopConfig(desktopPoolBasicDTO.getPoolModel(), poolDesktopConfig);

        // 校验会话配置：最大会话数、空闲桌面自动回收时间（分钟）
        PoolSessionConfigDTO sessionConfig = new PoolSessionConfigDTO(request);
        validateSessionConfig(desktopPoolBasicDTO.getPoolModel(), sessionConfig);

        // 校验策略配置：云桌面策略、网络策略、软件管控策略、个性化配置策略
        PoolStrategyConfigDTO strategyConfigDTO = new PoolStrategyConfigDTO(request);
        strategyConfigDTO.setPoolModel(desktopPoolBasicDTO.getPoolModel());
        validateStrategyConfig(strategyConfigDTO);
    }

    /**
     * 校验编辑桌面池的参数不涉及配置相关
     *
     * @param updateDesktopPool updateDesktopPool
     * @param desktopPoolBasicDTO desktopPoolBasicDTO
     * @throws BusinessException BusinessException
     */
    public void validateUpdateDesktopPoolWithoutConfig(CbbDesktopPoolDTO updateDesktopPool, DesktopPoolBasicDTO desktopPoolBasicDTO)
            throws BusinessException {
        Assert.notNull(updateDesktopPool, "updateDesktopPool is null");
        Assert.notNull(desktopPoolBasicDTO, "desktopPoolBasicDTO is null");

        validateBaseInfo(updateDesktopPool.getName(), desktopPoolBasicDTO.getId());
        if (desktopPoolBasicDTO.getPoolModel() == CbbDesktopPoolModel.STATIC) {
            if (Optional.ofNullable(updateDesktopPool.getPreStartDesktopNum()).orElse(0) > 0) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_STATIC_NO_PRE_START_NUM);
            }
            if (updateDesktopPool.getSessionType() == CbbDesktopSessionType.SINGLE && Objects.nonNull(updateDesktopPool.getIdleDesktopRecover())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_STATIC_NO_IDLE_RECOVER);
            }
        } else if (desktopPoolBasicDTO.getPoolModel() == CbbDesktopPoolModel.DYNAMIC) {
            if (Objects.isNull(updateDesktopPool.getPreStartDesktopNum())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DYNAMIC_PRE_START_DESKTOP_NOT_NULL);
            }
            if (updateDesktopPool.getPreStartDesktopNum() > desktopPoolBasicDTO.getDesktopNum()) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DYNAMIC_PRE_START_DESKTOP_NUM_LIMIT);
            }
        }
    }

    /**
     * 校验编辑桌面池规格的参数
     *
     * @param request      EditPoolDeskSpecRequest
     * @param poolBasicDTO poolBasicDTO
     * @throws BusinessException BusinessException
     */
    public void validateUpdatePoolDeskSpec(EditPoolDeskSpecRequest request, DesktopPoolBasicDTO poolBasicDTO) throws BusinessException {
        Assert.notNull(request, "request is null");
        Assert.notNull(poolBasicDTO, "poolBasicDTO is null");

        CbbDeskSpecDTO newDeskSpec = request.getDeskSpec();

        if (poolBasicDTO.getPoolState() != CbbDesktopPoolState.AVAILABLE) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UNAVAILABLE_UPDATE_FORBID, poolBasicDTO.getName());
        }

        if (poolBasicDTO.getSystemDisk() > newDeskSpec.getSystemSize()) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_UPDATE_SYSTEM_LESS);
        }

        int poolPersonSize = Optional.ofNullable(poolBasicDTO.getPersonDisk()).orElse(0);
        int specPersonSize = Optional.ofNullable(newDeskSpec.getPersonSize()).orElse(0);
        if (poolPersonSize > specPersonSize) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_UPDATE_PERSON_LESS);
        }

        if (poolPersonSize > 0 && !Objects.equals(newDeskSpec.getPersonDiskStoragePoolId(), poolBasicDTO.getPersonDiskStoragePoolId())) {
            // 已开启本地盘，不允许变更本地盘存储池
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_UNABLE_CHANGE_PERSON_DISK_STORAGE);
        }

        if (poolBasicDTO.getPoolModel() == CbbDesktopPoolModel.DYNAMIC && specPersonSize > 0) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_DYNAMIC_NO_PERSON_DISK);
        }

        if (specPersonSize == 0) {
            if (CollectionUtils.isNotEmpty(newDeskSpec.getExtraDiskList())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_EXTRA_DISK_NO_PERSON_DISK);
            }
            if (Objects.nonNull(newDeskSpec.getPersonDiskStoragePoolId())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_NO_PERSON_DISK_NO_STORAGE);
            }
        }

        // 检查存储池
        List<UUID> storagePoolIdList = new ArrayList<>();
        if (Objects.nonNull(newDeskSpec.getPersonDiskStoragePoolId())) {
            storagePoolIdList.add(newDeskSpec.getPersonDiskStoragePoolId());
        }
        if (CollectionUtils.isNotEmpty(newDeskSpec.getExtraDiskList())) {
            for (CbbAddExtraDiskDTO cbbAddExtraDiskDTO : newDeskSpec.getExtraDiskList()) {
                storagePoolIdList.add(cbbAddExtraDiskDTO.getAssignedStoragePoolId());
            }
        }
        // 显卡
        ImageDeskSpecGpuCheckParamDTO checkParamDTO = new ImageDeskSpecGpuCheckParamDTO();
        checkParamDTO.setImageId(poolBasicDTO.getImageTemplateId());
        checkParamDTO.setClusterId(poolBasicDTO.getClusterId());
        checkParamDTO.setVgpuInfoDTO(newDeskSpec.getVgpuInfoDTO());
        checkParamDTO.setStrategyId(poolBasicDTO.getStrategyId());
        deskSpecAPI.checkGpuSupportByImageAndSpec(checkParamDTO);
        // 存储池
        clusterAPI.validateClusterStoragePoolList(poolBasicDTO.getClusterId(), storagePoolIdList);
    }

    /**
     * 校验基础信息
     *
     * @param name   name
     * @param poolId poolId
     * @throws BusinessException BusinessException
     */
    private void validateBaseInfo(String name, UUID poolId) throws BusinessException {
        // 校验桌面池名称是否重复
        CbbCheckDeskPoolNameDTO cbbCheckDeskPoolNameDTO = new CbbCheckDeskPoolNameDTO();
        cbbCheckDeskPoolNameDTO.setId(poolId);
        cbbCheckDeskPoolNameDTO.setName(name);
        boolean hasDuplicate = cbbDesktopPoolMgmtAPI.checkDeskPoolNameDuplicate(cbbCheckDeskPoolNameDTO);
        if (hasDuplicate) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NAME_EXIST, name);
        }
    }

    /**
     * 校验桌面配置
     *
     * @param poolModel         poolModel
     * @param poolDesktopConfig poolDesktopConfig
     * @throws BusinessException BusinessException
     */
    private void validateDesktopConfig(CbbDesktopPoolModel poolModel, DesktopPoolDesktopConfigDTO poolDesktopConfig) throws BusinessException {

        CbbDeskSpecDTO cbbDeskSpecDTO = poolDesktopConfig.getDeskSpec();
        int specPersonSize = Optional.ofNullable(cbbDeskSpecDTO.getPersonSize()).orElse(0);
        if (specPersonSize == 0 && Objects.nonNull(cbbDeskSpecDTO.getPersonDiskStoragePoolId())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_NO_PERSON_DISK_NO_STORAGE);
        }
        if (poolModel == CbbDesktopPoolModel.STATIC) {
            if (Optional.ofNullable(poolDesktopConfig.getPreStartDesktopNum()).orElse(0) > 0) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_STATIC_NO_PRE_START_NUM);
            }
            if (specPersonSize > 0 && Objects.isNull(cbbDeskSpecDTO.getPersonDiskStoragePoolId())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NO_PERSON_STORAGE_POOL);
            }
            if (specPersonSize > 0 && specPersonSize < 20) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_PERSON_DISK_MORE_THAN_20);
            }
        }
        CbbImageTemplateDetailDTO imageTemplate = null;
        if (poolModel == CbbDesktopPoolModel.DYNAMIC) {
            if (Objects.isNull(poolDesktopConfig.getPreStartDesktopNum())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DYNAMIC_PRE_START_DESKTOP_NOT_NULL);
            }
            if (poolDesktopConfig.getPreStartDesktopNum() > poolDesktopConfig.getDesktopNum()) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DYNAMIC_PRE_START_DESKTOP_NUM_LIMIT);
            }
            if (specPersonSize > 0) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_DYNAMIC_NO_PERSON_DISK);
            }
            if (specPersonSize == 0 && CollectionUtils.isNotEmpty(cbbDeskSpecDTO.getExtraDiskList())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SPEC_EXTRA_DISK_NO_PERSON_DISK);
            }

            imageTemplate = cbbImageTemplateMgmtAPI.getImageTemplateDetail(poolDesktopConfig.getImageTemplateId());
            boolean isNormalImageTemplate = imageTemplate.getImageRoleType() == ImageRoleType.TEMPLATE && //
                    !Boolean.TRUE.equals(imageTemplate.getEnableMultipleVersion());
            // 动态池镜像模板必须支持还原VDI
            if (isNormalImageTemplate && imageTemplate.getClouldDeskopNumOfPersonal() > 0) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DYNAMIC_IMAGE_HAS_PERSONAL_VDI);
            }
        }

        if (Objects.isNull(imageTemplate)) {
            imageTemplate = cbbImageTemplateMgmtAPI.getImageTemplateDetail(poolDesktopConfig.getImageTemplateId());
        }
        if (imageTemplate.getCbbImageType() != CbbImageType.VDI) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IMAGE_MUST_VDI);
        }
        if (imageTemplate.getSystemDisk() > cbbDeskSpecDTO.getSystemSize()) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_CREATE_USER_CONFIG_VDI_SPEC_SYSTEM_SIZE_ERROR, imageTemplate.getImageName());
        }

        // 校验显卡
        ImageDeskSpecGpuCheckParamDTO checkParamDTO = new ImageDeskSpecGpuCheckParamDTO();
        checkParamDTO.setImageId(poolDesktopConfig.getImageTemplateId());
        checkParamDTO.setClusterId(poolDesktopConfig.getClusterId());
        checkParamDTO.setVgpuInfoDTO(cbbDeskSpecDTO.getVgpuInfoDTO());
        checkParamDTO.setStrategyId(poolDesktopConfig.getStrategyId());
        deskSpecAPI.checkGpuSupportByImageAndSpec(checkParamDTO);

        // 校验多计算集群VDI云桌面配置
        VDIDesktopValidateDTO validateDTO = new VDIDesktopValidateDTO();
        BeanUtils.copyProperties(poolDesktopConfig, validateDTO);
        validateDTO.setImageId(poolDesktopConfig.getImageTemplateId());
        validateDTO.setDeskSpec(cbbDeskSpecDTO);
        validateDTO.setPlatformId(poolDesktopConfig.getPlatformId());
        clusterAPI.validateVDIDesktopConfig(validateDTO);
    }

    /**
     * 校验会话配置
     *
     * @param poolModel     poolModel
     * @param sessionConfig sessionConfig
     * @throws BusinessException BusinessException
     */
    private void validateSessionConfig(CbbDesktopPoolModel poolModel, PoolSessionConfigDTO sessionConfig) throws BusinessException {
        if (poolModel == CbbDesktopPoolModel.STATIC && sessionConfig.getSessionType() == CbbDesktopSessionType.SINGLE
                && Objects.nonNull(sessionConfig.getIdleDesktopRecover())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_STATIC_NO_IDLE_RECOVER);
        }
        if (sessionConfig.getSessionType() == CbbDesktopSessionType.SINGLE && Objects.nonNull(sessionConfig.getMaxSession())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_SINGLE_NO_MAX_SESSION);
        }
    }

    /**
     * 校验策略配置
     *
     * @param strategyConfig strategyConfig
     * @throws BusinessException BusinessException
     */
    private void validateStrategyConfig(PoolStrategyConfigDTO strategyConfig) throws BusinessException {
        if (strategyConfig.getPoolType() == CbbDesktopPoolType.VDI) {
            CbbDeskStrategyDTO deskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(strategyConfig.getStrategyId());
            if (BooleanUtils.isTrue(deskStrategyDTO.getOpenDesktopRedirect())
                    && (Objects.isNull(strategyConfig.getPersonSize()) || strategyConfig.getPersonSize() <= 0)) {
                throw new BusinessException(BusinessKey.RCDC_RCO_CREATE_USER_CONFIG_VDI_DESK_REDIRECT_MUST_PERSON_DISK);
            }

            if (strategyConfig.getPoolModel() == CbbDesktopPoolModel.DYNAMIC) {
                if (deskStrategyDTO.getPattern() != CbbCloudDeskPattern.RECOVERABLE) {
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DYNAMIC_MUST_RECOVERABLE);
                }
            }
        }

        if (Objects.nonNull(strategyConfig.getUserProfileStrategyId())) {
            if (strategyConfig.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                userProfileValidateAPI.validateStorageMustFileServer(strategyConfig.getUserProfileStrategyId());
                return;
            }
            userProfileValidateAPI.validateUserProfileStrategyMustStoragePersonal(strategyConfig.getUserProfileStrategyId());
            userProfileValidateAPI.validateUserProfileStrategyImageRefuse(strategyConfig.getImageTemplateId());
        }
    }


    /**
     * 检查PC终端ID和PC终端组ID是否存在
     * @param bindObjectDTO bindObjectDTO
     * @throws BusinessException 业务异常
     */
    public void checkComputerAndComputerGroupExist(UpdatePoolThirdPartyBindObjectDTO bindObjectDTO) throws BusinessException {
        Assert.notNull(bindObjectDTO, "bindObjectDTO can not be null");

        List<UUID> addComputerIdList = bindObjectDTO.getAddComputerByIdList();
        if (CollectionUtils.isNotEmpty(addComputerIdList)) {
            List<List<UUID>> tempIdList = Lists.partition(addComputerIdList, SQL_IN_MAX_NUM);
            for (List<UUID> idList : tempIdList) {
                if (!computerBusinessAPI.checkAllComputerExistByIds(idList)) {
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UPDATE_BIND_COMPUTER_NULL);
                }
            }
        }

        List<UUID> groupIdList = bindObjectDTO.getSelectedGroupIdList();
        if (CollectionUtils.isNotEmpty(groupIdList)) {
            List<List<UUID>> tempIdList = Lists.partition(groupIdList, SQL_IN_MAX_NUM);
            for (List<UUID> idList : tempIdList) {
                if (!cbbTerminalGroupMgmtAPI.checkAllTerminalGroupExistByIds(idList)) {
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_ADD_DESKTOP_TERMINAL_GROUP_NULL);
                }
            }
        }
    }

    /**
     * 保存第三方桌面池分配审计日志
     * @param desktopPoolDTO desktopPoolDTO
     * @param bindObjectDTO bindObjectDTO
     */
    public void saveUpdateThirdPartyBindObjLog(CbbDesktopPoolDTO desktopPoolDTO, UpdatePoolThirdPartyBindObjectDTO bindObjectDTO) {
        Assert.notNull(desktopPoolDTO, "desktopPoolDTO can not be null");
        Assert.notNull(bindObjectDTO, "bindObjectDTO can not be null");
        List<CbbTerminalGroupDetailDTO> groupList;
        List<UUID> addGroupIdList = bindObjectDTO.getAddGroupIdList();
        if (CollectionUtils.isNotEmpty(addGroupIdList)) {
            groupList = cbbTerminalGroupMgmtAPI.findByIdList(addGroupIdList);
            List<String> nameList = groupList.stream().map(CbbTerminalGroupDetailDTO::getGroupName).collect(Collectors.toList());
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ADD_BIND_TERMINAL_GROUP_LOG, desktopPoolDTO.getName(),
                    StringUtils.join(nameList, NAME_SPLIT));
        }

        List<UUID> deleteGroupIdList = bindObjectDTO.getRemoveGroupIdList();
        if (CollectionUtils.isNotEmpty(deleteGroupIdList)) {
            groupList = cbbTerminalGroupMgmtAPI.findByIdList(deleteGroupIdList);
            List<String> nameList = groupList.stream().map(CbbTerminalGroupDetailDTO::getGroupName).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(nameList)) {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DELETE_BIND_TERMINAL_GROUP_LOG, desktopPoolDTO.getName(),
                        StringUtils.join(nameList, NAME_SPLIT));
            }
        }
    }

    private void saveDesktopPoolAddComputerLog(CbbDesktopPoolDTO desktopPoolDTO, List<UUID> addComputerIdList) {
        Assert.notNull(desktopPoolDTO, "desktopPoolDTO can not be null");
        Assert.notNull(addComputerIdList, "addUserIdList can not be null");
        if (CollectionUtils.isEmpty(addComputerIdList)) {
            return;
        }
        List<List<UUID>> tempIdList = Lists.partition(addComputerIdList, SQL_IN_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            List<ComputerDTO> computerInfoList = computerBusinessAPI.getComputerInfoByIdList(idList);
            saveUpdateComputerBindSysLog(desktopPoolDTO, computerInfoList, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ADD_BIND_COMPUTER_LOG);
        }
    }

    private void saveUpdateComputerBindSysLog(CbbDesktopPoolDTO desktopPoolDTO,
                                              List<ComputerDTO> computerInfoList, String key) {
        if (CollectionUtils.isEmpty(computerInfoList)) {
            return;
        }
        List<List<ComputerDTO>> tempUserList = Lists.partition(computerInfoList, SINGLE_LOG_MAX_NUM);
        for (List<ComputerDTO> subUserList : tempUserList) {
            List<String> nameList = subUserList.stream().map(ComputerDTO::getName).collect(Collectors.toList());
            auditLogAPI.recordLog(key, desktopPoolDTO.getName(), StringUtils.join(nameList, NAME_SPLIT));
        }
    }
}
