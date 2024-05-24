package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.CreatingDesktopNumCache;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.cache.DesktopPoolCache;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CreateDesktopHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RecycleBinService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UserServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * Recyclebin service
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019
 *
 * @author artom
 */
@Service
public class RecycleBinServiceImpl implements RecycleBinService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecycleBinServiceImpl.class);

    @Autowired
    private CbbDeskRecycleBinMgmtAPI cbbDeskRecycleBinMgmtAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserService userService;

    @Autowired
    private CreatingDesktopNumCache creatingDesktopNumCache;

    @Autowired
    private CreateDesktopHelper createDesktopHelper;

    @Autowired
    private UserServiceTx userServiceTx;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private DesktopPoolUserService desktopPoolUserService;

    @Autowired
    private DesktopPoolCache desktopPoolCache;

    @Autowired
    private HostUserService hostUserService;

    @Override
    public void deleteDeskCompletely(UUID cbbDesktopId) throws BusinessException {
        Assert.notNull(cbbDesktopId, "root is null");
        UserDesktopEntity entity = queryCloudDesktopService.checkAndFindById(cbbDesktopId);
        CbbCloudDeskState state = queryCloudDesktopService.queryState(cbbDesktopId);
        if (state != CbbCloudDeskState.RECYCLE_BIN) {
            throw new BusinessException(BusinessKey.RCDC_USER_RECYCLEBIN_DELETE_STATE_ERR, entity.getDesktopName(),
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_CLOUDDESKTOP_STATE_PRE + state.name().toLowerCase()));
        }
        // 此处调用完CBB删除回收站桌面接口后，对用户-桌面关系表的删除在SPI实现类RecycleBinDeleteDeskHandler中处理
        cbbDeskRecycleBinMgmtAPI.deleteDeskCompletely(cbbDesktopId);
    }

    @Override
    public void recover(UUID cbbDesktopId) throws BusinessException {
        Assert.notNull(cbbDesktopId, "cbbDesktopId must not be null");

        UserDesktopEntity entity = queryCloudDesktopService.checkAndFindById(cbbDesktopId);
        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(cbbDesktopId);
        checkAndUpdateVDIDesktopClusterInfo(cbbDesktopId);

        if (Objects.isNull(cbbDeskDTO.getDesktopPoolType()) || cbbDeskDTO.getDesktopPoolType() == DesktopPoolType.COMMON) {
            // 非池桌面恢复
            recoverCommonDesktop(cbbDesktopId, entity);
            return;
        }

        // 池桌面恢复
        recoverPoolDesktop(cbbDeskDTO, entity);
    }

    private void recoverCommonDesktop(UUID cbbDesktopId, UserDesktopEntity userDesktopEntity) throws BusinessException {
        UUID userId = userDesktopEntity.getUserId();
        IacUserDetailDTO userDetailResponse = cbbUserAPI.getUserDetail(userId);
        if (userDetailResponse == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_RECYCLEBIN_RECOVER_USER_DELETE, userDesktopEntity.getDesktopName());
        }

        // 检查桌面状态
        checkRecoverDeskState(cbbDesktopId, userDesktopEntity);
        synchronized (userService) {
            IacUserTypeEnum userType = userDetailResponse.getUserType();
            // 检查用户已创建的桌面数量
            checkUserDesktopNum(userDesktopEntity, userType);
            creatingDesktopNumCache.increaseDesktopNum(userId);
        }
        try {
            cbbDeskRecycleBinMgmtAPI.recoverDesk(cbbDesktopId, userDesktopEntity.getDesktopName());
        } finally {
            creatingDesktopNumCache.reduceDesktopNum(userId);
        }
    }

    private void checkUserDesktopNum(UserDesktopEntity entity, IacUserTypeEnum userType) throws BusinessException {
        if (userType == IacUserTypeEnum.VISITOR) {
            long desktopNum = userService.countUserDesktopNumContainCreatingNum(entity.getUserId());
            checkRecoverVisitorDesktopNum(desktopNum);
        }
    }

    private void recoverPoolDesktop(CbbDeskDTO cbbDeskDTO, UserDesktopEntity entity) throws BusinessException {
        checkRecoverDeskState(cbbDeskDTO.getDeskId(), entity);

        if (Objects.isNull(cbbDeskDTO.getDesktopPoolId())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_RECYCLE_BIN_DESKTOP_POOL_ID_NULL, cbbDeskDTO.getName());
        }
        UUID desktopPoolId = cbbDeskDTO.getDesktopPoolId();
        CbbDesktopPoolDTO desktopPoolDTO;
        try {
            desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
            if (desktopPoolDTO.getPoolState() == CbbDesktopPoolState.DELETING) {
                throw new BusinessException(BusinessKey.RCDC_RCO_RECYCLE_BIN_DESKTOP_POOL_DELETING, cbbDeskDTO.getName());
            }
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面[{}]的桌面池[{}]信息异常", cbbDeskDTO.getDeskId(), desktopPoolId, e);
            throw new BusinessException(BusinessKey.RCDC_RCO_RECYCLE_BIN_DESKTOP_POOL_NOT_EXISTS, e, cbbDeskDTO.getName());
        }

        List<UUID> userIdList = new ArrayList<>();
        List<HostUserEntity> entityList = hostUserService.findByDeskId(cbbDeskDTO.getDeskId());
        if (CbbDesktopSessionType.MULTIPLE == cbbDeskDTO.getSessionType() && entityList != null) {
            // 多会话桌面全部关联用户
            userIdList = entityList.stream().map(HostUserEntity::getUserId).collect(Collectors.toList());
            List<IacUserDetailDTO> userDTOList;
            if (!CollectionUtils.isEmpty(userIdList)) {
                // 检查关联的用户是否存在
                userDTOList = cbbUserAPI.listUserByUserIds(userIdList);
                if (CollectionUtils.isEmpty(userDTOList)) {
                    throw new BusinessException(BusinessKey.RCDC_USER_RECYCLEBIN_RECOVER_USER_DELETE, entity.getDesktopName());
                }

                // 检查多会话桌面关联的用户是否已在静态池中有其他绑定的桌面
                boolean isInPool = desktopPoolUserService.checkUserIdListBindDesktopInPool(cbbDeskDTO.getDesktopPoolId(), userIdList);
                if (isInPool) {
                    throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_USER_ALREADY_BIND_IN_POOL);
                }
            }
        } else {
            // 检查单会话桌面关联的用户是否已在静态池中有其他绑定的桌面
            if (Objects.nonNull(entity.getUserId())
                    && desktopPoolUserService.checkUserBindDesktopInPool(cbbDeskDTO.getDesktopPoolId(), entity.getUserId())) {
                IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(entity.getUserId());
                if (userDetailDTO == null) {
                    throw new BusinessException(BusinessKey.RCDC_USER_RECYCLEBIN_RECOVER_USER_DELETE, entity.getDesktopName());
                }
                throw new BusinessException(BusinessKey.RCDC_RCO_RECYCLE_BIN_USER_HAD_DESK_IN_POOL, userDetailDTO.getUserName(),
                        desktopPoolDTO.getName());
            }
        }
        userIdList.add(entity.getUserId());

        try {
            desktopPoolCache.checkUserAndIncreaseDesktopNum(desktopPoolId, cbbDeskDTO.getDeskId(), userIdList);
            cbbDeskRecycleBinMgmtAPI.recoverDesk(cbbDeskDTO.getDeskId(), entity.getDesktopName());
        } finally {
            desktopPoolCache.reduceDesktopNum(desktopPoolId, cbbDeskDTO.getDeskId(), userIdList);
        }
    }

    @Override
    public void recoverByAssignUserId(UUID cbbDesktopId, UUID userId) throws BusinessException {
        Assert.notNull(cbbDesktopId, "cbbDesktopId must not be null");
        Assert.notNull(userId, "userId must not be null");

        IacUserDetailDTO userDetailResponse = cbbUserAPI.getUserDetail(userId);

        Assert.notNull(userDetailResponse, "userEntity must not be null, userId = [" + userId + "]");

        UserDesktopEntity userDesktopEntity = queryCloudDesktopService.checkAndFindById(cbbDesktopId);
        checkDesktopBeforeRecoverAssignUser(cbbDesktopId, userDesktopEntity);
        checkAndUpdateVDIDesktopClusterInfo(cbbDesktopId);

        String newDesktopName = null;
        synchronized (userService) {
            IacUserTypeEnum userType = userDetailResponse.getUserType();
            // 检查指定的用户桌面数量
            checkRecoverAssignUserDesktopNum(userDetailResponse, userDesktopEntity, userType);
            // 检查通过后，预生成新的桌面名称
            newDesktopName = obtainNewDesktopName(userDetailResponse, userDesktopEntity);
            creatingDesktopNumCache.increaseDesktopNum(userId);
        }
        // 更新数据库表记录
        CbbDeskDTO cbbDeskDTO = queryCloudDesktopService.getDeskInfo(cbbDesktopId);

        userServiceTx.updateRecoverDeskData(cbbDeskDTO, userDetailResponse, newDesktopName);
        try {
            cbbDeskRecycleBinMgmtAPI.recoverDesk(cbbDesktopId, newDesktopName);
        } finally {
            creatingDesktopNumCache.reduceDesktopNum(userId);
        }
    }

    @Override
    public void recoverByAssignDesktopPoolId(UUID deskId, @Nullable List<UUID> userIdList, UUID assignDesktopPoolId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(assignDesktopPoolId, "assignDesktopPoolId must not be null");
        try {
            desktopPoolCache.checkUserAndIncreaseDesktopNum(assignDesktopPoolId, deskId, userIdList);
            cbbDeskRecycleBinMgmtAPI.recoverDeskAssignDesktopPool(deskId, assignDesktopPoolId, null);
        } finally {
            desktopPoolCache.reduceDesktopNum(assignDesktopPoolId, deskId, userIdList);
        }
    }

    private void checkDesktopBeforeRecoverAssignUser(UUID cbbDesktopId, UserDesktopEntity userDesktopEntity) throws BusinessException {
        // 检查是否是池桌面
        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(cbbDesktopId);
        if (Objects.nonNull(cbbDeskDTO.getDesktopPoolType()) && DesktopPoolType.isPoolDesktop(cbbDeskDTO.getDesktopPoolType())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_RECYCLE_BIN_ASSIGN_USER_ERROR_POOL_DESKTOP, cbbDeskDTO.getName());
        }

        // 检查桌面状态
        checkRecoverDeskState(cbbDesktopId, userDesktopEntity);
    }

    private void checkRecoverAssignUserDesktopNum(IacUserDetailDTO userDetail, UserDesktopEntity userDesktopEntity, IacUserTypeEnum userType)
            throws BusinessException {
        UUID userId = userDetail.getId();
        long normalDesktopNum = userService.countUserDesktopNumContainCreatingNum(userId);
        // 检查非访客用户已创建的桌面数量，不包含回收站云桌面
        if (userType != IacUserTypeEnum.VISITOR) {
            return;
        }
        // 回收站云桌面关联的用户id同指定的访客用户id相同，则按照普通回收站恢复访客用户桌面限制处理
        if (Objects.equals(userId, userDesktopEntity.getUserId())) {
            checkRecoverVisitorDesktopNum(normalDesktopNum);
            return;
        }
        // 访客用户已关联的云桌面数量（包含回收站云桌面）
        long recycleBinDesktopNum = viewDesktopDetailDAO.countByUserIdAndDesktopTypeAndIsDeleteAndDesktopPoolType(userId,
                CbbCloudDeskType.VDI.name(), true, DesktopPoolType.COMMON.name());
        long visitorDesktopNum = normalDesktopNum + recycleBinDesktopNum;
        if (visitorDesktopNum > 0) {
            throw new BusinessException(BusinessKey.RCDC_USER_RECYCLEBIN_RECOVER_TO_VISITOR_HAS_DESKTOP_FORBID, userDetail.getUserName(),
                    userDesktopEntity.getDesktopName());
        }
    }

    private void checkRecoverVisitorDesktopNum(long normalDesktopNum) throws BusinessException {
        // 访客桌面数量最多不能超过10个
        if (normalDesktopNum >= Constants.VISITOR_USER_DESKTOP_MAX_NUM) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_VISITOR_USER_VM_MAX,
                    String.valueOf(Constants.VISITOR_USER_DESKTOP_MAX_NUM));
        }
    }

    private String obtainNewDesktopName(IacUserDetailDTO userDetail, UserDesktopEntity entity) {
        // 指定的用户为自身
        if (Objects.equals(userDetail.getId(), entity.getUserId())) {
            return entity.getDesktopName();
        }
        return createDesktopHelper.generateDesktopName(userDetail);
    }

    private void checkRecoverDeskState(UUID cbbDesktopId, UserDesktopEntity entity) throws BusinessException {
        CbbCloudDeskState state = queryCloudDesktopService.queryState(cbbDesktopId);
        if (state != CbbCloudDeskState.RECYCLE_BIN) {
            throw new BusinessException(BusinessKey.RCDC_USER_RECYCLEBIN_RECOVER_STATE_ERR, entity.getDesktopName(),
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_CLOUDDESKTOP_STATE_PRE + state.name().toLowerCase()));
        }
    }

    /**
     * 回收站的桌面无法向CCP查询计算集群信息,目前与网络策略的计算集群必须一致才能使用
     *
     * @param cbbDesktopId 云桌面ID
     * @throws BusinessException 业务异常
     */
    private void checkAndUpdateVDIDesktopClusterInfo(UUID cbbDesktopId) throws BusinessException {
        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(cbbDesktopId);
        if (Objects.nonNull(cbbDeskDTO.getClusterId())) {
            return;
        }
        CbbDeskNetworkDetailDTO cbbDeskNetworkDetailDTO = cbbNetworkMgmtAPI.getDeskNetwork(cbbDeskDTO.getNetworkId());
        cbbVDIDeskMgmtAPI.updateVDIDesktopClusterInfo(cbbDesktopId, cbbDeskNetworkDetailDTO.getClusterId());
    }

}
