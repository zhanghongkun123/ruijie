package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacPermissionMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacGetAdminPageRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.role.IacUpdateRoleRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.LocalImageTemplatePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaPeripheralStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaPeripheralStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DefaultAdmin;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.ViewDeskStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolStatisticDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.FunTypes;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.AdminDataPermisssionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AdminDataPermissionService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/21 20:25
 *
 * @author linrenjian
 */
@Service
public class AdminDataPermissionServiceImpl implements AdminDataPermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDataPermissionServiceImpl.class);

    private static final String ID = "id";

    @Autowired
    private AdminDataPermisssionDAO adminDataPermisssionDAO;

    @Autowired
    private IacUserGroupMgmtAPI userGroupAPI;

    @Autowired
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private DiskPoolMgmtAPI diskPoolMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private IacPermissionMgmtAPI basePermissionMgmtAPI;

    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private RcaPeripheralStrategyAPI rcaPeripheralStrategyAPI;

    @Override
    public void save(AdminDataPermissionEntity adminDataPermisssionEntity) {
        Assert.notNull(adminDataPermisssionEntity, "adminDataPermisssionEntity list is not empty");
        adminDataPermisssionDAO.save(adminDataPermisssionEntity);
    }

    @Override
    public void saveAdminDataPermisssionList(List<AdminDataPermissionEntity> list) {
        Assert.notEmpty(list, "AdminDataPermisssionEntity list is not empty");
        adminDataPermisssionDAO.saveAll(list);
    }

    @Override
    public void deleteAdminDataPermisssionByAdminId(UUID adminId) {
        Assert.notNull(adminId, "adminId is not null");
        adminDataPermisssionDAO.deleteByAdminId(adminId);
    }

    @Override
    public int deleteAdminDataPermisssionByPermissionDataId(String permissionDataId) {
        Assert.notNull(permissionDataId, "permissionDataId is not null");
        return adminDataPermisssionDAO.deleteByPermissionDataId(permissionDataId);
    }

    @Override
    public List<AdminDataPermissionEntity> listAdminDataPermisssionByAdminId(UUID adminId) {
        Assert.notNull(adminId, "adminId is not null");
        return adminDataPermisssionDAO.findByAdminId(adminId);
    }


    @Override
    public List<String> listUserGroupIdByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");
        List<AdminDataPermissionEntity> adminDataPermisssionEntityList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.USER_GROUP, adminId);
        return getAdminDataPermissionIds(adminDataPermisssionEntityList);
    }


    @Override
    public List<String> listTerminalGroupIdByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");

        List<AdminDataPermissionEntity> adminDataPermisssionEntityList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.TERMINAL_GROUP, adminId);
        return getAdminDataPermissionIds(adminDataPermisssionEntityList);
    }

    @Override
    public List<String> listImageIdByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");

        List<AdminDataPermissionEntity> adminDataPermisssionEntityList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.IMAGE, adminId);
        return getAdminDataPermissionIds(adminDataPermisssionEntityList);
    }

    @Override
    public List<GroupIdLabelEntry> listUserGroupIdLabelEntryByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");

        List<AdminDataPermissionEntity> adminDataPermisssionEntityList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.USER_GROUP, adminId);
        return getRoleUserGroupPermissionIdLabelEntrys(adminDataPermisssionEntityList);
    }


    private List<GroupIdLabelEntry> getRoleUserGroupPermissionIdLabelEntrys(List<AdminDataPermissionEntity> entityList) throws BusinessException {
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();

        IacUserGroupDetailDTO[] userGroupDTOArr = userGroupAPI.getAllUserGroup();

        entityList.stream().forEach(adminDataPermisssionEntity -> {
            GroupIdLabelEntry entry = new GroupIdLabelEntry();
            entry.setId(adminDataPermisssionEntity.getPermissionDataId());
            for (IacUserGroupDetailDTO dto : userGroupDTOArr) {
                if (dto.getId().toString().equals(adminDataPermisssionEntity.getPermissionDataId())) {
                    entry.setLabel(dto.getName());
                    break;
                }
            }
            idLabelEntryList.add(entry);
        });

        return idLabelEntryList;
    }

    @Override
    public List<GroupIdLabelEntry> listTerminalGroupIdLabelEntryByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");

        List<AdminDataPermissionEntity> adminDataPermisssionEntityList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.TERMINAL_GROUP, adminId);
        return getRoleTerminalGroupPermissionIdLabelEntrys(adminDataPermisssionEntityList);
    }


    private List<GroupIdLabelEntry> getRoleTerminalGroupPermissionIdLabelEntrys(List<AdminDataPermissionEntity> entityList) {
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();

        List<CbbTerminalGroupDetailDTO> terminalGroupDTOList = terminalGroupMgmtAPI.listTerminalGroup();

        entityList.stream().forEach(adminDataPermisssionEntity -> {
            GroupIdLabelEntry entry = new GroupIdLabelEntry();
            entry.setId(adminDataPermisssionEntity.getPermissionDataId());
            for (CbbTerminalGroupDetailDTO dto : terminalGroupDTOList) {
                if (dto.getId().toString().equals(adminDataPermisssionEntity.getPermissionDataId())) {
                    entry.setLabel(dto.getGroupName());
                    break;
                }
            }
            idLabelEntryList.add(entry);
        });

        return idLabelEntryList;
    }

    @Override
    public List<GroupIdLabelEntry> listImageIdLabelEntryByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");

        List<AdminDataPermissionEntity> adminDataPermisssionEntityList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.IMAGE, adminId);
        return getImagePermissionIdLabelEntrys(adminDataPermisssionEntityList);
    }

    @Override
    public List<GroupIdLabelEntry> listImageIdLabelEntryByAdminIdAndImageId(UUID adminId, String permissionDataId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");
        Assert.notNull(permissionDataId, "permissionDataId is not null");

        List<AdminDataPermissionEntity> adminDataPermisssionEntityList = adminDataPermisssionDAO
                .findByPermissionDataTypeAndAdminIdAndPermissionDataId(AdminDataPermissionType.IMAGE, adminId, permissionDataId);
        return getImagePermissionIdLabelEntrys(adminDataPermisssionEntityList);
    }

    /**
     * 构造镜像 lb name
     *
     * @param entityList
     * @return
     */
    private List<GroupIdLabelEntry> getImagePermissionIdLabelEntrys(List<AdminDataPermissionEntity> entityList) throws BusinessException {
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();
        // 如果数据权限ID为空 直接返回空 不需要In
        if (CollectionUtils.isEmpty(entityList)) {
            return idLabelEntryList;
        }
        // 构造查询
        UUID[] uuidArr = entityList.stream().map(AdminDataPermissionEntity::getPermissionDataId).map(UUID::fromString).toArray(UUID[]::new);
        // 查询全部镜像
        List<CbbImageTemplateDetailDTO> allImageList = getAllImageList(uuidArr);
        entityList.forEach(adminDataPermisssionEntity -> {
            GroupIdLabelEntry entry = new GroupIdLabelEntry();
            entry.setId(adminDataPermisssionEntity.getPermissionDataId());
            for (CbbImageTemplateDetailDTO dto : allImageList) {
                if (dto.getId().toString().equals(adminDataPermisssionEntity.getPermissionDataId())) {
                    entry.setLabel(dto.getImageName());
                }
            }
            idLabelEntryList.add(entry);
        });

        return idLabelEntryList;
    }


    @Override
    public void initializeAdminDataPermission() throws BusinessException {

        IacAdminDTO[] itemArr = getAllBaseAdminDTOArr();
        // 过滤掉角色是安全管理员 审计管理员
        itemArr = Arrays.stream(itemArr).filter(baseAdminDTO -> !(Objects.equals(baseAdminDTO.getUserName(), DefaultAdmin.SECADMIN.getName())
                || Objects.equals(baseAdminDTO.getUserName(), DefaultAdmin.AUDADMIN.getName()))).toArray(IacAdminDTO[]::new);
        LOGGER.info("5.4升级初始化赋值镜像权限，查询到管理员信息为{}", JSON.toJSONString(itemArr));
        List<AdminDataPermissionEntity> entityArrayList = getAllNeedAddAdminDataPermissionEntity(itemArr);
        LOGGER.info("5.4升级初始化赋值镜像权限，需要赋值的权限列表为{}", JSON.toJSONString(entityArrayList));
        // 权限集合不为空 进行初始化
        if (CollectionUtils.isNotEmpty(entityArrayList)) {
            saveAdminDataPermissionList(entityArrayList);
        }
    }

    @Override
    public void initDeskStrategyAdminDataPermission() throws BusinessException {
        List<IacAdminDTO> baseAdminDTOList = listNeedDataPermissionAdmin();
        List<AdminDataPermissionEntity> entityArrayList = getAllNeedAddDeskStrategyAdminDataPermissionEntity(baseAdminDTOList);
        LOGGER.info("Ent1.1升级初始化赋值云桌面策略权限，需要赋值的权限列表为{}", JSON.toJSONString(entityArrayList));
        // 权限集合不为空 进行初始化
        if (CollectionUtils.isNotEmpty(entityArrayList)) {
            saveAdminDataPermissionList(entityArrayList);
        }
    }

    @Override
    public void initRcaStrategyAdminDataPermission() throws BusinessException {
        List<IacAdminDTO> baseAdminDTOList = listNeedDataPermissionAdmin();
        List<AdminDataPermissionEntity> entityArrayList = getAllNeedAddRcaStrategyAdminDataPermissionEntity(baseAdminDTOList);
        LOGGER.info("Ent2.0升级初始化赋值云应用及外设策略权限，需要赋值的权限列表为{}", JSON.toJSONString(entityArrayList));
        // 权限集合不为空 进行初始化
        if (CollectionUtils.isNotEmpty(entityArrayList)) {
            saveAdminDataPermissionList(entityArrayList);
        }
    }

    private List<AdminDataPermissionEntity> getAllNeedAddRcaStrategyAdminDataPermissionEntity(List<IacAdminDTO> baseAdminDTOList)
            throws BusinessException {
        if (CollectionUtils.isEmpty(baseAdminDTOList)) {
            return new ArrayList<>();
        }
        // 查询全部的云应用及外设策略
        List<RcaMainStrategyDTO> rcaStrategyList = getAllRcaStrategyList(null);
        List<RcaPeripheralStrategyDTO> peripheralStrategyList = getAllRcaPeripheralStrategyList(null);
        LOGGER.info("Ent2.0升级初始化赋值云应用及外设策略权限，查询全部的云应用策略为{}，查询全部的云应用外设策略为{}", JSON.toJSONString(rcaStrategyList),
                JSON.toJSONString(peripheralStrategyList));

        List<AdminDataPermissionEntity> entityArrayList = new ArrayList<>();
        for (IacAdminDTO baseAdminDTO : baseAdminDTOList) {
            UUID adminId = baseAdminDTO.getId();
            List<String> rcaPermissionList = listByAdminIdAndPermissionType(adminId, AdminDataPermissionType.APP_MAIN_STRATEGY);
            List<String> peripheralPermissionList = listByAdminIdAndPermissionType(adminId, AdminDataPermissionType.APP_PERIPHERAL_STRATEGY);
            if (CollectionUtils.isEmpty(rcaPermissionList)) {
                LOGGER.info("管理员:{} 云应用策略权限为空,进行补偿", adminId);
                rcaStrategyList.forEach(strategy -> {
                    if (Objects.isNull(strategy.getId())) {
                        return;
                    }
                    AdminDataPermissionEntity entity =
                            buildAdminDataPermissionEntity(AdminDataPermissionType.APP_MAIN_STRATEGY, adminId, strategy.getId().toString());
                    entityArrayList.add(entity);
                });
            }
            if (CollectionUtils.isEmpty(peripheralPermissionList)) {
                LOGGER.info("管理员:{} 云应用外设策略权限为空,进行补偿", adminId);
                peripheralStrategyList.forEach(strategy -> {
                    if (Objects.isNull(strategy.getId())) {
                        return;
                    }
                    AdminDataPermissionEntity entity =
                            buildAdminDataPermissionEntity(AdminDataPermissionType.APP_PERIPHERAL_STRATEGY, adminId, strategy.getId().toString());
                    entityArrayList.add(entity);
                });
            }
        }
        return entityArrayList;
    }

    private List<IacAdminDTO> listNeedDataPermissionAdmin() throws BusinessException {
        IacAdminDTO[] baseAdminDTOArr = getAllBaseAdminDTOArr();
        List<IacAdminDTO> baseAdminDTOList = new ArrayList<>();
        if (ArrayUtils.isEmpty(baseAdminDTOArr)) {
            LOGGER.info("升级初始化赋值数据权限，查询到管理员列表为空");
            return baseAdminDTOList;
        }

        // 内置超管角色有所有权限，角色安全管理员和审计管理员无数据权限
        List<IacRoleDTO> noDataPermissionRoleList = listNoNeedDataPermissionRole();
        Set<UUID> noDataRoleIdSet = noDataPermissionRoleList.stream().map(IacRoleDTO::getId).collect(Collectors.toSet());

        // 内置角色中安全管理员和审计管理员无数据权限，超管有所有权限，内置的系统管理员sysadmin有所有权限
        baseAdminDTOList = Arrays.stream(baseAdminDTOArr).filter(baseAdminDTO -> {
            if (Objects.equals(baseAdminDTO.getUserName(), DefaultAdmin.SYSADMIN.getName())) {
                return false;
            }
            if (CollectionUtils.isNotEmpty(noDataRoleIdSet) && ArrayUtils.isNotEmpty(baseAdminDTO.getRoleIdArr())) {
                return Arrays.stream(baseAdminDTO.getRoleIdArr()).noneMatch(noDataRoleIdSet::contains);
            }
            return true;
        }).collect(Collectors.toList());
        LOGGER.info("升级初始化赋值数据权限，查询到需要补偿的管理员信息为{}", JSON.toJSONString(baseAdminDTOList));
        return baseAdminDTOList;
    }

    private List<IacRoleDTO> listNoNeedDataPermissionRole() throws BusinessException {
        List<RoleType> noDataPermissionRoleList = Lists.newArrayList(RoleType.ADMIN, RoleType.SECADMIN, RoleType.AUDADMIN);
        List<IacRoleDTO> noDataRoleList = Lists.newArrayList();
        for (RoleType roleType : noDataPermissionRoleList) {
            if (baseRoleMgmtAPI.validateRoleNameExist(roleType.getName(), SubSystem.CDC)) {
                IacRoleDTO baseRoleDTO = baseRoleMgmtAPI.getRoleByRoleName(roleType.getName(), SubSystem.CDC);
                noDataRoleList.add(baseRoleDTO);
            }
        }
        return noDataRoleList;
    }

    @Override
    public List<String> listByAdminIdAndPermissionType(UUID adminId, AdminDataPermissionType permissionType) {
        Assert.notNull(adminId, "adminId is not null");
        Assert.notNull(permissionType, "permissionType is not null");
        List<AdminDataPermissionEntity> permissionEntityList = adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(permissionType, adminId);
        return getAdminDataPermissionIds(permissionEntityList);
    }

    @Override
    public List<GroupIdLabelEntry> listDesktopPoolIdLabelEntryByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");
        List<AdminDataPermissionEntity> permissionList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.DESKTOP_POOL, adminId);
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();
        // 如果数据权限ID为空 直接返回空
        if (CollectionUtils.isEmpty(permissionList)) {
            return idLabelEntryList;
        }

        // 构造查询
        UUID[] uuidArr = permissionList.stream().map(AdminDataPermissionEntity::getPermissionDataId).map(UUID::fromString).toArray(UUID[]::new);
        // 查询全部桌面池
        List<DesktopPoolDTO> desktopPoolList = getAllDesktopPoolList(uuidArr);
        Map<UUID, DesktopPoolDTO> desktopPoolMap =
                desktopPoolList.stream().collect(Collectors.toMap(DesktopPoolDTO::getId, Function.identity(), (key1, key2) -> key2));

        permissionList.forEach(permissionEntity -> {
            GroupIdLabelEntry entry = new GroupIdLabelEntry();
            entry.setId(permissionEntity.getPermissionDataId());
            DesktopPoolDTO desktopPoolDTO = desktopPoolMap.get(UUID.fromString(permissionEntity.getPermissionDataId()));
            if (desktopPoolDTO != null) {
                entry.setLabel(desktopPoolDTO.getName());
            }
            idLabelEntryList.add(entry);
        });
        return idLabelEntryList;
    }

    @Override
    public List<GroupIdLabelEntry> listDiskPoolIdLabelEntryByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");
        List<AdminDataPermissionEntity> permissionList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.DISK_POOL, adminId);
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();
        // 如果数据权限ID为空 直接返回空
        if (CollectionUtils.isEmpty(permissionList)) {
            return idLabelEntryList;
        }

        // 构造查询
        UUID[] uuidArr = permissionList.stream().map(AdminDataPermissionEntity::getPermissionDataId).map(UUID::fromString).toArray(UUID[]::new);
        // 查询全部桌面池
        List<DiskPoolStatisticDTO> diskPoolList = getAllDiskPoolList(uuidArr);
        Map<UUID, DiskPoolStatisticDTO> diskPoolMap =
                diskPoolList.stream().collect(Collectors.toMap(DiskPoolStatisticDTO::getId, Function.identity(), (key1, key2) -> key2));

        permissionList.forEach(permissionEntity -> {
            GroupIdLabelEntry entry = new GroupIdLabelEntry();
            entry.setId(permissionEntity.getPermissionDataId());
            DiskPoolStatisticDTO diskPoolStatisticDTO = diskPoolMap.get(UUID.fromString(permissionEntity.getPermissionDataId()));
            if (diskPoolStatisticDTO != null) {
                entry.setLabel(diskPoolStatisticDTO.getName());
            }
            idLabelEntryList.add(entry);
        });
        return idLabelEntryList;
    }

    @Override
    public List<GroupIdLabelEntry> listDeskStrategyIdLabelEntryByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");
        List<AdminDataPermissionEntity> permissionList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.DESKTOP_STRATEGY, adminId);
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();
        // 如果数据权限ID为空 直接返回空
        if (CollectionUtils.isEmpty(permissionList)) {
            return idLabelEntryList;
        }

        // 构造查询
        List<UUID> idList = permissionList.stream().map(AdminDataPermissionEntity::getPermissionDataId).map(UUID::fromString)
                .collect(Collectors.toList());
        // 查询全部桌面池
        List<ViewDeskStrategyDTO> deskStrategyList = getAllDeskStrategyList(idList);
        Map<UUID, ViewDeskStrategyDTO> diskPoolMap =
                deskStrategyList.stream().collect(Collectors.toMap(ViewDeskStrategyDTO::getId, Function.identity(), (key1, key2) -> key2));

        permissionList.forEach(permissionEntity -> {
            GroupIdLabelEntry entry = new GroupIdLabelEntry();
            entry.setId(permissionEntity.getPermissionDataId());
            ViewDeskStrategyDTO deskStrategyDTO = diskPoolMap.get(UUID.fromString(permissionEntity.getPermissionDataId()));
            if (Objects.nonNull(deskStrategyDTO)) {
                entry.setLabel(deskStrategyDTO.getStrategyName());
            }
            idLabelEntryList.add(entry);
        });
        return idLabelEntryList;
    }

    @Override
    public List<GroupIdLabelEntry> listAppPoolIdLabelEntryByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");
        List<AdminDataPermissionEntity> permissionList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.APP_POOL, adminId);
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();
        // 如果数据权限ID为空 直接返回空
        if (CollectionUtils.isEmpty(permissionList)) {
            return idLabelEntryList;
        }

        // 构造查询
        List<UUID> poolIdList = permissionList.stream().map(AdminDataPermissionEntity::getPermissionDataId)
                .map(UUID::fromString).collect(Collectors.toList());
        // 查询全部应用池
        List<RcaAppPoolBaseDTO> poolAppDTOList = rcaAppPoolAPI.findAllByIdList(poolIdList);
        Map<UUID, RcaAppPoolBaseDTO> appPoolMap = poolAppDTOList.stream().collect(Collectors.toMap(
                RcaAppPoolBaseDTO::getId, Function.identity(), (key1, key2) -> key2));

        permissionList.forEach(permissionEntity -> {
            GroupIdLabelEntry entry = new GroupIdLabelEntry();
            entry.setId(permissionEntity.getPermissionDataId());
            RcaAppPoolBaseDTO rcaAppPoolBaseDTO = appPoolMap.get(UUID.fromString(permissionEntity.getPermissionDataId()));
            if (rcaAppPoolBaseDTO != null) {
                entry.setLabel(rcaAppPoolBaseDTO.getName());
            }
            idLabelEntryList.add(entry);
        });
        return idLabelEntryList;
    }

    @Override
    public List<GroupIdLabelEntry> listAppMainStrategyIdLabelEntryByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");
        List<AdminDataPermissionEntity> permissionList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.APP_MAIN_STRATEGY, adminId);
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();
        // 如果数据权限ID为空 直接返回空
        if (CollectionUtils.isEmpty(permissionList)) {
            return idLabelEntryList;
        }

        // 构造查询
        List<UUID> strategyIdList = permissionList.stream().map(AdminDataPermissionEntity::getPermissionDataId).distinct()
                .map(UUID::fromString).collect(Collectors.toList());
        // 查询全部桌面池
        List<RcaMainStrategyDTO> rcaMainStrategyDTOList = rcaMainStrategyAPI.listMasterStrategyById(strategyIdList);
        Map<UUID, RcaMainStrategyDTO> mainStrategyMap = rcaMainStrategyDTOList.stream().collect(Collectors.toMap(
                RcaMainStrategyDTO::getId, Function.identity(), (key1, key2) -> key2));

        permissionList.forEach(permissionEntity -> {
            GroupIdLabelEntry entry = new GroupIdLabelEntry();
            entry.setId(permissionEntity.getPermissionDataId());
            RcaMainStrategyDTO rcaMainStrategyDTO = mainStrategyMap.get(UUID.fromString(permissionEntity.getPermissionDataId()));
            if (rcaMainStrategyDTO != null) {
                entry.setLabel(rcaMainStrategyDTO.getName());
            }
            idLabelEntryList.add(entry);
        });
        return idLabelEntryList;
    }

    @Override
    public List<GroupIdLabelEntry> listAppPeripheralStrategyIdLabelEntryByAdminId(UUID adminId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");
        List<AdminDataPermissionEntity> permissionList =
                adminDataPermisssionDAO.findByPermissionDataTypeAndAdminId(AdminDataPermissionType.APP_PERIPHERAL_STRATEGY, adminId);
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();
        // 如果数据权限ID为空 直接返回空
        if (CollectionUtils.isEmpty(permissionList)) {
            return idLabelEntryList;
        }

        // 构造查询
        List<UUID> strategyIdList = permissionList.stream().map(AdminDataPermissionEntity::getPermissionDataId)
                .map(UUID::fromString).collect(Collectors.toList());
        // 查询全部桌面池
        List<RcaPeripheralStrategyDTO> rcaPeripheralStrategyList = rcaPeripheralStrategyAPI.listMasterStrategyById(strategyIdList);
        Map<UUID, RcaPeripheralStrategyDTO> mainStrategyMap = rcaPeripheralStrategyList.stream().collect(Collectors.toMap(
                RcaPeripheralStrategyDTO::getId, Function.identity(), (key1, key2) -> key2));

        permissionList.forEach(permissionEntity -> {
            GroupIdLabelEntry entry = new GroupIdLabelEntry();
            entry.setId(permissionEntity.getPermissionDataId());
            RcaPeripheralStrategyDTO rcaPeripheralStrategyDTO = mainStrategyMap.get(
                    UUID.fromString(permissionEntity.getPermissionDataId()));
            if (rcaPeripheralStrategyDTO != null) {
                entry.setLabel(rcaPeripheralStrategyDTO.getName());
            }
            idLabelEntryList.add(entry);
        });
        return idLabelEntryList;
    }

    @Override
    public Boolean hasDataPermission(UUID adminId, String permissionDataId, AdminDataPermissionType permissionDataType) {
        Assert.notNull(adminId, "adminId must not be null");
        Assert.hasText(permissionDataId, "permissionDataId must not be null");
        Assert.notNull(permissionDataType, "permissionDataType must not be null");
        return adminDataPermisssionDAO.existsByAdminIdAndPermissionDataIdAndPermissionDataType(adminId, permissionDataId, permissionDataType);
    }

    @Override
    public void deleteByPermissionDataIdAndPermissionDataType(String permissionDataId, AdminDataPermissionType permissionDataType) {
        Assert.hasText(permissionDataId, "permissionDataId must not be null");
        Assert.notNull(permissionDataType, "permissionDataType must not be null");
        adminDataPermisssionDAO.deleteByPermissionDataIdAndPermissionDataType(permissionDataId, permissionDataType);
    }

    private List<String> getAdminDataPermissionIds(List<AdminDataPermissionEntity> entityList) {
        return entityList.stream().map(AdminDataPermissionEntity::getPermissionDataId).collect(Collectors.toList());
    }

    private IacAdminDTO[] getAllBaseAdminDTOArr() throws BusinessException {
        // 管理员集合
        List<IacAdminDTO> baseAdminDTOList = new ArrayList<>();
        int page = 0;
        while (true) {
            IacGetAdminPageRequest baseRequest = new IacGetAdminPageRequest();
            baseRequest.setPage(page);
            baseRequest.setLimit(Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT);
            baseRequest.setSubSystem(SubSystem.CDC);
            DefaultPageResponse<IacAdminDTO> response = baseAdminMgmtAPI.getAdminPage(baseRequest);
            page++;
            // 如果分页查询为空集合 直接返回
            if (ArrayUtils.isEmpty(response.getItemArr())) {
                return baseAdminDTOList.toArray(new IacAdminDTO[0]);
            }
            // 不为空就添加进管理员集合
            baseAdminDTOList.addAll(Arrays.asList(response.getItemArr()));
            // 当总数小于需要查询数量 直接返回
            if (response.getTotal() <= page * Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT) {
                return baseAdminDTOList.toArray(new IacAdminDTO[0]);
            }
        }
    }

    /**
     * 初始化的时候增加所有的镜像，权限
     * 5.4之前的版本中默认系统管理员需要都是必须勾选的镜像权限。所以升级上来后也默认拥有全部镜像
     *
     * @param itemArr
     * @return
     * @throws BusinessException
     */
    private List<AdminDataPermissionEntity> getAllNeedAddAdminDataPermissionEntity(IacAdminDTO[] itemArr) throws BusinessException {
        // 查询全部的镜像
        List<CbbImageTemplateDetailDTO> cbbImageTemplateDetailDTOList = getAllImageList(null);
        LOGGER.info("5.4升级初始化赋值镜像权限，查询全部的镜像为{}", JSON.toJSONString(cbbImageTemplateDetailDTOList));
        // 镜像数据权限
        List<AdminDataPermissionEntity> entityArrayList = new ArrayList<>();
        // 如果镜像模板为空直接返回
        if (CollectionUtils.isEmpty(cbbImageTemplateDetailDTOList)) {
            return entityArrayList;
        }
        Arrays.stream(itemArr).forEach(baseAdminDTO -> {

            // 遍历镜像集合
            for (CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO : cbbImageTemplateDetailDTOList) {
                UUID adminId = baseAdminDTO.getId();
                // 构造镜像数据权限
                AdminDataPermissionEntity imageDataPermissionList =
                        buildAdminDataPermissionEntity(AdminDataPermissionType.IMAGE, adminId, cbbImageTemplateDetailDTO.getId().toString());
                entityArrayList.add(imageDataPermissionList);
            }


        });
        return entityArrayList;
    }

    private List<AdminDataPermissionEntity> getAllNeedAddDeskStrategyAdminDataPermissionEntity(List<IacAdminDTO> baseAdminDTOList)
            throws BusinessException {
        if (CollectionUtils.isEmpty(baseAdminDTOList)) {
            return new ArrayList<>();
        }
        // 查询全部的云桌面策略
        List<ViewDeskStrategyDTO> viewDeskStrategyDTOList = getAllDeskStrategyList(null);
        LOGGER.info("Ent1.1升级初始化赋值云桌面策略权限，查询全部的云桌面策略为{}", JSON.toJSONString(viewDeskStrategyDTOList));
        // 云桌面策略数据权限
        List<AdminDataPermissionEntity> entityArrayList = new ArrayList<>();
        // 如果云桌面策略为空直接返回
        if (CollectionUtils.isEmpty(viewDeskStrategyDTOList)) {
            return entityArrayList;
        }
        for (IacAdminDTO baseAdminDTO : baseAdminDTOList) {
            // 遍历镜像集合
            for (ViewDeskStrategyDTO deskStrategyDTO : viewDeskStrategyDTOList) {
                UUID adminId = baseAdminDTO.getId();
                // 构造镜像数据权限
                AdminDataPermissionEntity entity =
                        buildAdminDataPermissionEntity(AdminDataPermissionType.DESKTOP_STRATEGY, adminId, deskStrategyDTO.getId().toString());
                entityArrayList.add(entity);
            }
        }
        return entityArrayList;
    }


    /**
     * 获取权限镜像
     *
     * @return
     * @throws BusinessException
     */
    private List<CbbImageTemplateDetailDTO> getAllImageList(UUID[] uuidArr) throws BusinessException {
        // 镜像集合
        List<CbbImageTemplateDetailDTO> cbbImageTemplateDetailDTOList = new ArrayList<>();
        int page = 0;
        while (true) {
            // 查询最大
            PageWebRequest webRequest = new PageWebRequest();
            webRequest.setPage(page);
            webRequest.setLimit(Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT);
            LocalImageTemplatePageRequest pageSearchRequest = new LocalImageTemplatePageRequest(webRequest);
            // 集合不为空
            if (ArrayUtils.isNotEmpty(uuidArr)) {
                // ID数组
                pageSearchRequest.appendCustomMatchEqual(new MatchEqual(ID, uuidArr));
            }
            // 查询
            DefaultPageResponse<CbbImageTemplateDetailDTO> response = cbbImageTemplateMgmtAPI.pageQueryLocalPageImageTemplate(pageSearchRequest);
            page++;
            // 如果分页查询为空集合 直接返回
            if (ArrayUtils.isEmpty(response.getItemArr())) {
                return cbbImageTemplateDetailDTOList;
            }
            // 不为空就添加进管理员集合
            cbbImageTemplateDetailDTOList.addAll(Arrays.asList(response.getItemArr()));
            // 当总数小于需要查询数量 直接返回
            if (response.getTotal() <= page * Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT) {
                return cbbImageTemplateDetailDTOList;
            }
        }
    }

    private List<ViewDeskStrategyDTO> getAllDeskStrategyList(List<UUID> idList) throws BusinessException {
        // 镜像集合
        List<ViewDeskStrategyDTO> viewDeskStrategyDTOList = new ArrayList<>();
        int page = 0;
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder();
        if (CollectionUtils.isNotEmpty(idList)) {
            // ID数组
            builder.in(ID, idList.toArray());
        }
        while (true) {
            // 查询
            PageQueryResponse<ViewDeskStrategyDTO> response = deskStrategyAPI.pageQuery(builder.setPageLimit(page,
                    Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT).build());
            page++;
            // 如果分页查询为空集合 直接返回
            if (ArrayUtils.isEmpty(response.getItemArr())) {
                return viewDeskStrategyDTOList;
            }
            // 不为空就添加进管理员集合
            viewDeskStrategyDTOList.addAll(Arrays.asList(response.getItemArr()));
            // 当总数小于需要查询数量 直接返回
            if (response.getItemArr().length < Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT) {
                // 当本次查询到的记录小于指定的数量时，后续页数已无记录
                return viewDeskStrategyDTOList;
            }
        }
    }

    private List<RcaMainStrategyDTO> getAllRcaStrategyList(List<UUID> idList) throws BusinessException {
        List<RcaMainStrategyDTO> rcaStrategyList = new ArrayList<>();
        int page = 0;
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder();
        if (CollectionUtils.isNotEmpty(idList)) {
            // ID数组
            builder.in(ID, idList.toArray());
        }
        while (true) {
            // 查询
            PageQueryResponse<RcaMainStrategyDTO> response = rcaMainStrategyAPI.pageQueryMasterStrategy(builder.setPageLimit(page,
                    Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT).build());
            page++;
            // 如果分页查询为空集合 直接返回
            if (ArrayUtils.isEmpty(response.getItemArr())) {
                return rcaStrategyList;
            }
            // 不为空就添加进管理员集合
            rcaStrategyList.addAll(Arrays.asList(response.getItemArr()));
            // 当总数小于需要查询数量 直接返回
            if (response.getItemArr().length < Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT) {
                // 当本次查询到的记录小于指定的数量时，后续页数已无记录
                return rcaStrategyList;
            }
        }
    }

    private List<RcaPeripheralStrategyDTO> getAllRcaPeripheralStrategyList(List<UUID> idList) throws BusinessException {
        List<RcaPeripheralStrategyDTO> peripheralStrategyList = new ArrayList<>();
        int page = 0;
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder();
        if (CollectionUtils.isNotEmpty(idList)) {
            // ID数组
            builder.in(ID, idList.toArray());
        }
        while (true) {
            // 查询
            PageQueryResponse<RcaPeripheralStrategyDTO> response = rcaPeripheralStrategyAPI.pageQueryMasterStrategy(builder.setPageLimit(page,
                    Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT).build());
            page++;
            // 如果分页查询为空集合 直接返回
            if (ArrayUtils.isEmpty(response.getItemArr())) {
                return peripheralStrategyList;
            }
            // 不为空就添加进管理员集合
            peripheralStrategyList.addAll(Arrays.asList(response.getItemArr()));
            // 当总数小于需要查询数量 直接返回
            if (response.getItemArr().length < Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT) {
                // 当本次查询到的记录小于指定的数量时，后续页数已无记录
                return peripheralStrategyList;
            }
        }
    }

    private List<DesktopPoolDTO> getAllDesktopPoolList(UUID[] uuidArr) throws BusinessException {
        // 桌面池集合
        List<DesktopPoolDTO> desktopPoolDTOList = new ArrayList<>();
        int page = 0;
        while (true) {
            // 查询最大
            PageWebRequest webRequest = new PageWebRequest();
            webRequest.setPage(page);
            webRequest.setLimit(Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT);
            PageSearchRequest pageSearchRequest = new PageSearchRequest(webRequest);
            // 集合不为空
            if (ArrayUtils.isNotEmpty(uuidArr)) {
                // ID数组
                pageSearchRequest.appendCustomMatchEqual(new com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual(ID, uuidArr));
            }
            // 查询
            DefaultPageResponse<DesktopPoolDTO> response = desktopPoolMgmtAPI.pageQuery(pageSearchRequest);
            page++;
            // 如果分页查询为空集合 直接返回
            if (ArrayUtils.isEmpty(response.getItemArr())) {
                return desktopPoolDTOList;
            }
            // 不为空就添加进管理员集合
            desktopPoolDTOList.addAll(Arrays.asList(response.getItemArr()));
            // 当总数小于需要查询数量 直接返回
            if (response.getTotal() <= (long) page * Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT) {
                return desktopPoolDTOList;
            }
        }
    }

    private List<DiskPoolStatisticDTO> getAllDiskPoolList(UUID[] uuidArr) throws BusinessException {
        // 磁盘池集合
        List<DiskPoolStatisticDTO> diskPoolDTOList = new ArrayList<>();
        int page = 0;
        while (true) {
            // 查询最大
            PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder();
            requestBuilder.setPageLimit(page, Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT);

            // 集合不为空
            if (ArrayUtils.isNotEmpty(uuidArr)) {
                // ID数组
                requestBuilder.in(ID, uuidArr);
            }
            // 查询
            PageQueryResponse<DiskPoolStatisticDTO> response = diskPoolMgmtAPI.pageDiskPool(requestBuilder.build());
            page++;
            // 如果分页查询为空集合 直接返回
            if (ArrayUtils.isEmpty(response.getItemArr())) {
                return diskPoolDTOList;
            }
            // 不为空就添加进管理员集合
            diskPoolDTOList.addAll(Arrays.asList(response.getItemArr()));
            // 当总数小于需要查询数量 直接返回
            if (response.getTotal() <= (long) page * Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT) {
                return diskPoolDTOList;
            }
        }
    }

    /**
     * 构造 管理员数据权限实体
     *
     * @param permissionDataType
     * @param adminId
     * @param permissionDataId
     * @return
     */
    private AdminDataPermissionEntity buildAdminDataPermissionEntity(AdminDataPermissionType permissionDataType, UUID adminId,
            String permissionDataId) {
        AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
        entity.setId(UUID.randomUUID());
        entity.setPermissionDataId(permissionDataId);
        entity.setAdminId(adminId);
        entity.setPermissionDataType(permissionDataType);
        Date date = new Date();
        entity.setCreateDate(date);
        entity.setUpdateDate(date);
        entity.setVersion(0);
        return entity;
    }

    /**
     * 保存全部数据权限
     *
     * @param list
     */
    private void saveAdminDataPermissionList(List<AdminDataPermissionEntity> list) {
        Assert.notEmpty(list, "AdminDataPermisssionEntity list is not empty");
        adminDataPermisssionDAO.saveAll(list);

    }

    /**
     * 系统管理员菜单补偿
     */
    @Override
    public void addSysadminNewPermission() throws BusinessException {
        String value = globalParameterService.findParameter(Constants.NEED_ATTACH_SYSADMIN_PERMISSION_KEY);
        if (StringUtils.isEmpty(value) || Boolean.FALSE.toString().equals(value)) {
            LOGGER.info("数据库查询的值为[{}]，无需进行sysadmin菜单权限补偿", value);
            return;
        }

        List<IacPermissionDTO> permissionDTOList = basePermissionMgmtAPI.listAllPermission();

        // sysadmin应该拥有的所有权限
        // 不赋值超级管理员菜单 但是要赋值赋值定时任务 告警监控 告警列表等菜单 如果有打 ENABLE_SYS_ADMIN FunTypes.YES 说明可以加入权限
        List<UUID> expectPermissionList = permissionDTOList.stream()
                .filter(basePermissionDTO -> basePermissionDTO.getTags() != null
                        && (!FunTypes.YES.equals(((JSONObject) basePermissionDTO.getTags()).getString(FunTypes.ENABLE_SUPER_ADMIN))
                        || FunTypes.YES.equals(((JSONObject) basePermissionDTO.getTags()).getString(FunTypes.ENABLE_SYS_ADMIN))))
                .map(IacPermissionDTO::getId).collect(Collectors.toList());

        // 如果有的权限为空，则无需处理
        if (CollectionUtils.isEmpty(expectPermissionList)) {
            updateIdentification();
            return;
        }

        try {
            // sysadmin角色信息
            IacRoleDTO baseRoleDTO = baseRoleMgmtAPI.getRoleByRoleName(RoleType.SYSADMIN.getName(), SubSystem.CDC);
            List<UUID> currentPermissionIdList = new ArrayList<>();
            Collections.addAll(currentPermissionIdList, baseRoleDTO.getPermissionIdArr());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sysadmin当前权限为[{}]，需要拥有的所有权限为[{}]", JSON.toJSONString(currentPermissionIdList), //
                        JSON.toJSONString(expectPermissionList));
            }

            boolean shouldUpdatePermission = expectPermissionList.stream().anyMatch(id -> !currentPermissionIdList.contains(id));
            if (!shouldUpdatePermission) {
                LOGGER.info("sysadmin没有需要新增的权限，无需更新权限信息");
                updateIdentification();
                return;
            }

            // 设置权限
            baseRoleDTO.setPermissionIdArr(expectPermissionList.toArray(new UUID[0]));
            IacUpdateRoleRequest baseUpdateRoleRequest = new IacUpdateRoleRequest();
            // 将更新修正的角色信息
            BeanUtils.copyProperties(baseRoleDTO, baseUpdateRoleRequest);
            // 强制修改默认角色
            baseUpdateRoleRequest.setForceDefault(Boolean.TRUE);

            LOGGER.info("修改sysadmin权限信息");
            baseRoleMgmtAPI.updateRole(baseUpdateRoleRequest);

            // 修改标识
            updateIdentification();
        } catch (Exception e) {
            LOGGER.error("补齐sysadmin菜单权限失败，失败原因：", e);
        }
    }

    @Override
    public void deleteByAdminIdAndPermissionDataTypeIn(UUID adminId, List<AdminDataPermissionType> dataPermissionTypeList) {
        Assert.notNull(adminId, "adminId must not be null");
        Assert.notEmpty(dataPermissionTypeList, "dataPermissionTypeList must not be null or empty");
        LOGGER.info("根据adminId {} dataPermissionTypeList {} 删除权限数据", adminId, JSON.toJSONString(dataPermissionTypeList));
        adminDataPermisssionDAO.deleteByAdminIdAndPermissionDataTypeIn(adminId, dataPermissionTypeList);
    }

    private void updateIdentification() {
        globalParameterService.updateParameter(Constants.NEED_ATTACH_SYSADMIN_PERMISSION_KEY, Boolean.FALSE.toString());
    }
}
