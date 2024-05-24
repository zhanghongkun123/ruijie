package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CheckDeleteDeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.IdvTerminalGroupDeskConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserGroupDesktopConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.IdvTerminalGroupDeskConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserGroupDesktopConfigEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;


/**
 * Description: 检查云桌面策略是否可以删除API实现类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/8 18:46
 *
 * @author yxq
 */
public class CheckDeleteDeskStrategyAPIImpl implements CheckDeleteDeskStrategyAPI {

    /**
     * 允许名称之和的最大长度，
     */
    public static final int MAX_NAME_LENGTH = 39;

    /**
     * 分隔符
     */
    public static final String NAME_SEPARATOR = "、";

    @Autowired
    private IdvTerminalGroupDeskConfigDAO idvTerminalGroupDeskConfigDAO;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private UserGroupDesktopConfigDAO userGroupDesktopConfigDAO;

    @Autowired
    private UserDesktopConfigDAO userDesktopConfigDAO;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Override
    public void checkCanDeleteIDVOrVOIDeskStrategy(UUID strategyId, String strategyName) throws BusinessException {
        Assert.notNull(strategyId, "strategyId must not null");
        Assert.notNull(strategyName, "strategyName must not null");

        List<IdvTerminalGroupDeskConfigEntity> idvTerminalGroupDeskConfigEntityList =
                idvTerminalGroupDeskConfigDAO.findByCbbIdvDesktopStrategyId(strategyId);
        if (!CollectionUtils.isEmpty(idvTerminalGroupDeskConfigEntityList)) {
            handleExistsTerminalGroup(idvTerminalGroupDeskConfigEntityList, strategyName);
        }

        List<UserGroupDesktopConfigEntity> configEntityList = userGroupDesktopConfigDAO.findByStrategyId(strategyId);
        if (!CollectionUtils.isEmpty(configEntityList)) {
            handleExistsUserGroup(configEntityList, strategyName);
        }

        List<UserDesktopConfigEntity> userDesktopConfigEntityList = userDesktopConfigDAO.findByStrategyId(strategyId);
        if (!CollectionUtils.isEmpty(userDesktopConfigEntityList)) {
            handleExistsUser(userDesktopConfigEntityList, strategyName);
        }
    }

    @Override
    public void checkCanDeleteVDIDeskStrategy(UUID strategyId, String strategyName) throws BusinessException {
        Assert.notNull(strategyId, "strategyId must not null");
        Assert.notNull(strategyName, "strategyName must not null");

        if (viewDesktopDetailDAO.findFirstByCbbStrategyId(strategyId) != null) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_HAS_VM, strategyName);
        }

        List<UserGroupDesktopConfigEntity> configEntityList = userGroupDesktopConfigDAO.findByStrategyId(strategyId);
        if (!CollectionUtils.isEmpty(configEntityList)) {
            handleExistsUserGroup(configEntityList, strategyName);
        }
    }

    @Override
    public void checkCanDeleteThirdPartyDeskStrategy(UUID strategyId, String strategyName) throws BusinessException {
        Assert.notNull(strategyId, "strategyId must not null");
        Assert.notNull(strategyName, "strategyName must not null");

        List<UserGroupDesktopConfigEntity> configEntityList = userGroupDesktopConfigDAO.findByStrategyId(strategyId);
        if (!CollectionUtils.isEmpty(configEntityList)) {
            handleExistsUserGroup(configEntityList, strategyName);
        }

        List<UserDesktopConfigEntity> userDesktopConfigEntityList = userDesktopConfigDAO.findByStrategyId(strategyId);
        if (!CollectionUtils.isEmpty(userDesktopConfigEntityList)) {
            handleExistsUser(userDesktopConfigEntityList, strategyName);
        }
    }

    protected void handleExistsTerminalGroup(List<IdvTerminalGroupDeskConfigEntity> idvTerminalGroupDeskConfigEntityList, String strategyName)
            throws BusinessException {
        List<UUID> terminalGroupIdList = idvTerminalGroupDeskConfigEntityList.stream().map(IdvTerminalGroupDeskConfigEntity::getCbbTerminalGroupId)
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        int size = terminalGroupIdList.size();

        for (int i = 0; i < size; i++) {
            CbbTerminalGroupDetailDTO terminalGroupDTO = cbbTerminalGroupMgmtAPI.loadById(terminalGroupIdList.get(i));
            // 如果加上下一个名称
            if (terminalGroupDTO.getGroupName().length() + sb.length() > MAX_NAME_LENGTH) {
                throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_MANY_IDV_GROUP, strategyName, sb.toString());
            }

            if (i != 0) {
                sb.append(NAME_SEPARATOR);
            }
            sb.append(terminalGroupDTO.getGroupName());
        }
        throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_IDV_GROUP, strategyName, sb.toString());
    }

    protected void handleExistsUser(List<UserDesktopConfigEntity> userDesktopConfigEntityList, String strategyName) throws BusinessException {
        List<UUID> userIdList = userDesktopConfigEntityList.stream().map(UserDesktopConfigEntity::getUserId).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        int size = userIdList.size();

        for (int i = 0; i < size; i++) {
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userIdList.get(i));
            if (userDetail.getUserName().length() + sb.length() > MAX_NAME_LENGTH) {
                throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_MANY_USER, strategyName, sb.toString());
            }

            if (i != 0) {
                sb.append(NAME_SEPARATOR);
            }
            sb.append(userDetail.getUserName());
        }
        throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_USER, strategyName, sb.toString());
    }

    protected void handleExistsUserGroup(List<UserGroupDesktopConfigEntity> configEntityList, String strategyName) throws BusinessException {

        List<UUID> userGroupIdList = configEntityList.stream().map(UserGroupDesktopConfigEntity::getGroupId).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        int size = userGroupIdList.size();

        for (int i = 0; i < size; i++) {
            IacUserGroupDetailDTO groupDetail = cbbUserGroupAPI.getUserGroupDetail(userGroupIdList.get(i));
            if (groupDetail.getName().length() + sb.length() > MAX_NAME_LENGTH) {
                throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_MANY_GROUP, strategyName, sb.toString());
            }

            if (i != 0) {
                sb.append(NAME_SEPARATOR);
            }
            sb.append(groupDetail.getName());
        }
        throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_RELATIVE_GROUP, strategyName, sb.toString());
    }
}
