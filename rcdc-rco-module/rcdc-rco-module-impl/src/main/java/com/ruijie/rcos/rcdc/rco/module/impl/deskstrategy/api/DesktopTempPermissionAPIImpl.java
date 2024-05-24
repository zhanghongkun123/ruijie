package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskStrategyCommonAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBTypeMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetAllUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbClipboardMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUsbStorageDeviceMappingMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbClipBoardSupportTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbDesktopTempPermissionState;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMessageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionCreateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dto.DesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dto.DesktopTempPermissionRelatedInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.service.DesktopTempPermissionService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrinterUpdateNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.*;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.QuitVmToTerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.DesktopTempPermissionServiceTx;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ThreadExecutorsUtils;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.DesktopTempPermissionBusinessKey.*;

/**
 * Description: 临时权限API
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-04-26
 *
 * @author linke
 */
public class DesktopTempPermissionAPIImpl implements DesktopTempPermissionAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopTempPermissionAPIImpl.class);

    private static final String NAME_SPLIT = ",";

    private static final String CONTENT_SPLIT = "，";

    private static final int SQL_IN_MAX_NUM = 500;

    private static final int SINGLE_LOG_MAX_NUM = 20;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private DesktopTempPermissionService desktopTempPermissionService;

    @Autowired
    private DesktopTempPermissionServiceTx desktopTempPermissionServiceTx;

    @Autowired
    private CbbDesktopTempPermissionAPI cbbDesktopTempPermissionAPI;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CloudDesktopViewService cloudDesktopViewService;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private UserMessageAPI userMessageAPI;

    @Autowired
    private CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

    @Autowired
    private QueryUserListService queryUserListService;

    @Autowired
    private UserQueryHelper userQueryHelper;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private AuditPrinterUpdateNotifyService auditPrinterUpdateNotifyService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private HostUserService hostUserService;

    @Autowired
    private CbbDeskStrategyCommonAPI cbbDeskStrategyCommonAPI;

    @Override
    public PageQueryResponse<DesktopTempPermissionDetailDTO> pageQuery(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        PageQueryResponse<CbbDesktopTempPermissionDTO> cbbResp = cbbDesktopTempPermissionAPI.pageQuery(request);

        PageQueryResponse<DesktopTempPermissionDetailDTO> resp = new PageQueryResponse<>();
        resp.setTotal(cbbResp.getTotal());
        resp.setItemArr(convertDesktopTempPermissionArr(cbbResp.getItemArr()));
        return resp;
    }

    private DesktopTempPermissionDetailDTO[] convertDesktopTempPermissionArr(CbbDesktopTempPermissionDTO[] itemArr) {
        if (ArrayUtils.isEmpty(itemArr)) {
            return new DesktopTempPermissionDetailDTO[0];
        }
        return Arrays.stream(itemArr).map(dto -> {
            DesktopTempPermissionDetailDTO detailDTO = new DesktopTempPermissionDetailDTO();
            BeanUtils.copyProperties(dto, detailDTO);
            detailDTO.setDiskMappingType(getDiskMappingEnum(dto.getEnableDiskMapping(), dto.getEnableDiskMappingWriteable()));
            return detailDTO;
        }).toArray(DesktopTempPermissionDetailDTO[]::new);
    }


    @Override
    public CbbDesktopTempPermissionDTO getPermissionByRelatedIdAndType(UUID relatedId, DesktopTempPermissionRelatedType relatedType)
            throws BusinessException {
        Assert.notNull(relatedId, "relatedId must not be null");
        Assert.notNull(relatedType, "relatedType must not be null");
        return desktopTempPermissionService.getPermissionDTOByRelatedObj(relatedId, relatedType);
    }

    @Override
    public DesktopTempPermissionDetailDTO getDetailInfo(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        CbbDesktopTempPermissionDTO permissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(id);
        DesktopTempPermissionDetailDTO permissionDetailDTO = new DesktopTempPermissionDetailDTO();
        BeanUtils.copyProperties(permissionDTO, permissionDetailDTO);

        if (Objects.nonNull(permissionDTO.getEnableDiskMapping())) {
            DiskMappingEnum diskMappingEnum =
                    getDiskMappingEnum(permissionDTO.getEnableDiskMapping(), permissionDTO.getEnableDiskMappingWriteable());
            permissionDetailDTO.setDiskMappingType(diskMappingEnum);
        }

        // 外设策略idLabel封装
        permissionDetailDTO.setUsbTypeList(buildUsbTypeIdLabel(permissionDTO));

        // 获取对象信息（id，name，type）
        List<DesktopTempPermissionRelatedInfoDTO> relatedInfoList = desktopTempPermissionService.listRelatedInfo(id);
        if (CollectionUtils.isEmpty(relatedInfoList)) {
            return permissionDetailDTO;
        }

        Map<DesktopTempPermissionRelatedType, List<DesktopTempPermissionRelatedInfoDTO>> groupRelatedMap =
                relatedInfoList.stream().collect(Collectors.groupingBy(DesktopTempPermissionRelatedInfoDTO::getRelatedType));
        permissionDetailDTO.setUserList(buildIdLabelList(groupRelatedMap.get(DesktopTempPermissionRelatedType.USER)));
        permissionDetailDTO.setDesktopList(buildIdLabelList(groupRelatedMap.get(DesktopTempPermissionRelatedType.DESKTOP)));

        return permissionDetailDTO;
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

    private List<IdLabelEntry> buildIdLabelList(List<DesktopTempPermissionRelatedInfoDTO> relatedInfoList) {
        if (CollectionUtils.isEmpty(relatedInfoList)) {
            return Collections.emptyList();
        }
        //过滤 getRelatedName 为空场景 避免构造失败
        return relatedInfoList.stream().filter(info -> StringUtils.isNotBlank(info.getRelatedName()))
                .map(info -> IdLabelEntry.build(info.getRelatedId(), info.getRelatedName())).collect(Collectors.toList());
    }

    private List<IdLabelEntry> buildUsbTypeIdLabel(CbbDesktopTempPermissionDTO permissionDTO) {
        List<CbbUSBTypeDTO> usbTypeList = listUsbTypeDTO(permissionDTO);
        if (CollectionUtils.isEmpty(usbTypeList)) {
            return new ArrayList<>();
        }
        return usbTypeList.stream().filter(info -> StringUtils.isNotBlank(info.getUsbTypeName()))
                .map(usbType -> IdLabelEntry.build(usbType.getId(), usbType.getUsbTypeName())).collect(Collectors.toList());
    }

    @Override
    public DefaultPageResponse<UserListDTO> pageBindUser(UUID desktopTempPermissionId, PageSearchRequest request) {
        Assert.notNull(desktopTempPermissionId, "desktopTempPermissionId must not be null");
        Assert.notNull(request, "request must not be null");

        Page<RcoViewUserEntity> userPage = queryUserListService.pageUserBindDesktopTempPermission(desktopTempPermissionId, request);

        DefaultPageResponse<UserListDTO> response = new DefaultPageResponse<>();
        response.setTotal(userPage.getTotalElements());

        if (CollectionUtils.isEmpty(userPage.getContent())) {
            response.setItemArr(new UserListDTO[0]);
        } else {
            UserListDTO[] dtoArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(userPage.getContent());
            response.setItemArr(dtoArr);
        }
        return response;
    }

    @Override
    public DefaultPageResponse<CloudDesktopDTO> pageBindDesktop(UUID desktopTempPermissionId, PageSearchRequest request)
            throws BusinessException {
        Assert.notNull(desktopTempPermissionId, "desktopTempPermissionId must not be null");
        Assert.notNull(request, "request must not be null");

        return userDesktopMgmtAPI.pageQueryInDesktopTempPermission(desktopTempPermissionId, request);
    }

    @Override
    public void createDesktopTempPermission(DesktopTempPermissionCreateDTO createDTO) throws BusinessException {
        Assert.notNull(createDTO, "createDTO must not be null");

        // 处理用户、云桌面
        checkAllRelatedObj(createDTO, null);

        UUID permissionId = desktopTempPermissionServiceTx.createDesktopTempPermission(createDTO);

        // 审计日志
        saveCreateAuditLog(permissionId, createDTO);

        // 通知对应桌面
        sendEffectMessageByPermissionId(permissionId);

        // 创建临时权限后，创建用户消息
        createUserMessageByCreatePermission(permissionId);
    }

    @Override
    public void deleteById(UUID id, Boolean isBySystem) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(isBySystem, "isBySystem must not be null");

        CbbDesktopTempPermissionDTO permission = cbbDesktopTempPermissionAPI.getDesktopTempPermission(id);
        LOGGER.info("删除临时权限[{}], isBySystem:{}", JSON.toJSONString(permission), isBySystem);
         //创建 临时策略DTO   存储 临时策略信息 ，以及USB 外设信息 用于后续判断比较变更，推送信息 ，由于协议不支持热重载USB外设
        DesktopTempPermissionDTO desktopTempPermissionDTO = new DesktopTempPermissionDTO();
        desktopTempPermissionDTO.setPermission(permission);
        desktopTempPermissionDTO.setUsbTypeList(permission.getUsbTypeIdList());
        //状态为删除中 后面根据删除状态 进行判断
        desktopTempPermissionDTO.setState(CbbDesktopTempPermissionState.DELETING);
        // 必须先取旧的关联对象
        List<DesktopTempPermissionRelatedInfoDTO> oldRelatedList = desktopTempPermissionService.listRelatedInfo(id);

        desktopTempPermissionServiceTx.deleteById(id);

        // 审计日志
        saveDeleteAuditLog(permission, oldRelatedList, isBySystem);

        // 通知对应桌面
        sendMessageByDeleteRelatedObjects(null, oldRelatedList, desktopTempPermissionDTO);

        if (isBySystem) {
            // 管理员手动删除临时权限后，创建用户消息
            createUserMessageByDeletePermission(permission, oldRelatedList);
        }
    }

    @Override
    public void updateDesktopTempPermission(DesktopTempPermissionUpdateDTO updateDTO) throws BusinessException {
        Assert.notNull(updateDTO, "updateDTO must not be null");
        Assert.notNull(updateDTO.getId(), "id must not be null");

        // 检查用户、云桌面
        checkAllRelatedObj(updateDTO, updateDTO.getId());

        // 必须先取旧的关联对象
        List<DesktopTempPermissionRelatedInfoDTO> oldRelatedList = desktopTempPermissionService.listRelatedInfo(updateDTO.getId());

        // 必须先取旧的
        CbbDesktopTempPermissionDTO oldPermissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(updateDTO.getId());

        desktopTempPermissionServiceTx.updateDesktopTempPermission(updateDTO);

        // 保存编辑的审计日志
        saveUpdateAuditLog(updateDTO, oldRelatedList);

        // 通知对应桌面
        sendMessageByUpdatePermission(updateDTO, oldPermissionDTO, oldRelatedList);

        // 更新临时权限，计算新增和删除的对象，创建用户消息
        createUserMessageByUpdatePermission(updateDTO, oldRelatedList);
    }

    /**
     * 校验对象是否存在
     *
     * @param dto 参数
     * @throws BusinessException 业务异常
     */
    private void checkAllRelatedObj(DesktopTempPermissionCreateDTO dto, UUID permissionId) throws BusinessException {
        checkRelatedObj(dto.getUserIdList(), DesktopTempPermissionRelatedType.USER, permissionId);
        checkRelatedObj(dto.getDesktopIdList(), DesktopTempPermissionRelatedType.DESKTOP, permissionId);
    }

    private void checkRelatedObj(List<UUID> relatedIdList, DesktopTempPermissionRelatedType relatedType, UUID permissionId)
            throws BusinessException {
        if (CollectionUtils.isEmpty(relatedIdList)) {
            return;
        }
        List<List<UUID>> tempIdList = Lists.partition(relatedIdList, SQL_IN_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            desktopTempPermissionService.checkRelatedObject(idList, relatedType, permissionId);
        }
    }

    /**
     * 创建临时权限，记录审计日志
     *
     * @param permissionId permissionId
     * @param createDTO    createDTO
     */
    private void saveCreateAuditLog(UUID permissionId, DesktopTempPermissionCreateDTO createDTO) throws BusinessException {
        CbbDesktopTempPermissionDTO permission = cbbDesktopTempPermissionAPI.getDesktopTempPermission(permissionId);
        // 保存内容审计日志
        String content = buildPermissionContent(permission);
        auditLogAPI.recordLog(RCO_DESK_TEMP_PERMISSION_CREATE_AUDIT_LOG, permission.getName(), content, permission.getReason());

        // 保存开通对象绑定临时权限的审计日志
        saveRelatedObjCreateLog(createDTO);
    }

    /**
     * 编辑临时权限，记录审计日志
     *
     * @param updateDTO      updateDTO
     * @param oldRelatedList oldRelatedList
     * @throws BusinessException BusinessException
     */
    private void saveUpdateAuditLog(DesktopTempPermissionUpdateDTO updateDTO, List<DesktopTempPermissionRelatedInfoDTO> oldRelatedList)
            throws BusinessException {
        CbbDesktopTempPermissionDTO permission = cbbDesktopTempPermissionAPI.getDesktopTempPermission(updateDTO.getId());
        // 保存内容审计日志
        String content = buildPermissionContent(permission);
        auditLogAPI.recordLog(RCO_DESK_TEMP_PERMISSION_UPDATE_AUDIT_LOG, permission.getName(), content, permission.getReason());

        // 保存开通对象绑定临时权限的审计日志
        saveRelatedObjUpdateLog(updateDTO, oldRelatedList);
    }

    private void saveDeleteAuditLog(CbbDesktopTempPermissionDTO permission, List<DesktopTempPermissionRelatedInfoDTO> oldRelatedList,
                                    boolean isBySystem) throws BusinessException {
        // 保存内容审计日志
        String content = buildPermissionContent(permission);
        if (isBySystem) {
            auditLogAPI.recordLog(RCO_DESK_TEMP_PERMISSION_DELETE_AUDIT_LOG, permission.getName(), content, permission.getReason());
        } else {
            auditLogAPI.recordLog(RCO_DESK_TEMP_PERMISSION_DELETE_BY_SYSTEM_AUDIT_LOG, permission.getName(), content, permission.getReason());
        }

        // 保存开通对象的审计日志
        Map<DesktopTempPermissionRelatedType, List<DesktopTempPermissionRelatedInfoDTO>> oldRelatedMap =
                oldRelatedList.stream().collect(Collectors.groupingBy(DesktopTempPermissionRelatedInfoDTO::getRelatedType));

        List<DesktopTempPermissionRelatedInfoDTO> relatedList = oldRelatedMap.get(DesktopTempPermissionRelatedType.USER);
        if (CollectionUtils.isNotEmpty(relatedList)) {
            List<UUID> idList = relatedList.stream().map(DesktopTempPermissionRelatedInfoDTO::getRelatedId).collect(Collectors.toList());
            saveRelatedUserLog(idList, permission.getName(), false);
        }

        relatedList = oldRelatedMap.get(DesktopTempPermissionRelatedType.DESKTOP);
        if (CollectionUtils.isNotEmpty(relatedList)) {
            List<UUID> idList = relatedList.stream().map(DesktopTempPermissionRelatedInfoDTO::getRelatedId).collect(Collectors.toList());
            saveRelatedDesktopLog(idList, permission.getName(), false);
        }
    }

    /**
     * 保存关联对象增删的审计日志
     *
     * @param createDTO createDTO
     * @throws BusinessException error
     */
    private void saveRelatedObjCreateLog(DesktopTempPermissionCreateDTO createDTO) throws BusinessException {
        saveRelatedUserLog(createDTO.getUserIdList(), createDTO.getName(), true);
        saveRelatedDesktopLog(createDTO.getDesktopIdList(), createDTO.getName(), true);
    }

    /**
     * 保存关联对象增删的审计日志
     *
     * @param updateDTO      updateDTO
     * @param oldRelatedList oldRelatedList
     * @throws BusinessException error
     */
    private void saveRelatedObjUpdateLog(DesktopTempPermissionUpdateDTO updateDTO, List<DesktopTempPermissionRelatedInfoDTO> oldRelatedList)
            throws BusinessException {
        Map<DesktopTempPermissionRelatedType, List<DesktopTempPermissionRelatedInfoDTO>> oldRelatedMap =
                oldRelatedList.stream().collect(Collectors.groupingBy(DesktopTempPermissionRelatedInfoDTO::getRelatedType));

        List<UUID> deleteUserIdList = computeDeleteOrAddObj(oldRelatedMap.get(DesktopTempPermissionRelatedType.USER),
                updateDTO.getUserIdList(), false);
        List<UUID> addUserIdList = computeDeleteOrAddObj(oldRelatedMap.get(DesktopTempPermissionRelatedType.USER),
                updateDTO.getUserIdList(), true);

        List<UUID> deleteDesktopIdList = computeDeleteOrAddObj(oldRelatedMap.get(DesktopTempPermissionRelatedType.DESKTOP),
                updateDTO.getDesktopIdList(), false);
        List<UUID> addDesktopIdList = computeDeleteOrAddObj(oldRelatedMap.get(DesktopTempPermissionRelatedType.DESKTOP),
                updateDTO.getDesktopIdList(), true);

        saveRelatedUserLog(addUserIdList, updateDTO.getName(), true);
        saveRelatedUserLog(deleteUserIdList, updateDTO.getName(), false);
        saveRelatedDesktopLog(addDesktopIdList, updateDTO.getName(), true);
        saveRelatedDesktopLog(deleteDesktopIdList, updateDTO.getName(), false);
    }

    private void saveRelatedUserLog(List<UUID> userIdList, String permissionName, boolean isAdd) throws BusinessException {
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        String key = isAdd ? RCO_DESK_TEMP_PERMISSION_ADD_USER_LOG : RCO_DESK_TEMP_PERMISSION_DELETE_USER_LOG;
        List<List<UUID>> tempIdList = Lists.partition(userIdList, SINGLE_LOG_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            List<IacUserDetailDTO> userList = userService.listUserByIds(idList);
            List<String> nameList = userList.stream().map(IacUserDetailDTO::getUserName).collect(Collectors.toList());
            saveLog(permissionName, nameList, key);
        }
    }

    private void saveRelatedDesktopLog(List<UUID> desktopIdList, String permissionName, boolean isAdd) {
        if (CollectionUtils.isEmpty(desktopIdList)) {
            return;
        }
        String key = isAdd ? RCO_DESK_TEMP_PERMISSION_ADD_DESKTOP_LOG : RCO_DESK_TEMP_PERMISSION_DELETE_DESKTOP_LOG;
        List<List<UUID>> tempIdList = Lists.partition(desktopIdList, SINGLE_LOG_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.listDesktopByDesktopIds(idList);
            List<String> nameList = desktopList.stream().map(CloudDesktopDTO::getDesktopName).collect(Collectors.toList());
            saveLog(permissionName, nameList, key);
        }
    }

    private void saveLog(String permissionName, List<String> relatedNameList, String key) {

        auditLogAPI.recordLog(key, permissionName, StringUtils.join(relatedNameList, NAME_SPLIT));
    }

    /**
     * 编辑临时权限，实时生效消息通知
     *
     * @param updateDTO        updateDTO
     * @param oldPermissionDTO 变更前的临时权限
     * @param oldRelatedList   变更前的临时权限对象关联记录列表
     */
    private void sendMessageByUpdatePermission(DesktopTempPermissionUpdateDTO updateDTO, CbbDesktopTempPermissionDTO oldPermissionDTO,
            List<DesktopTempPermissionRelatedInfoDTO> oldRelatedList) throws BusinessException {

        // 判断 USB 设备是否有变化
        updateDTO.convertUsbChange(oldPermissionDTO);
        // 针对现在临时权限绑定的对象
        sendEffectMessageByPermissionIdAndUpdate(updateDTO.getId(), updateDTO);

        if (oldPermissionDTO.getState() != CbbDesktopTempPermissionState.IN_EFFECT) {
            // 原来未生效不需要处理
            return;
        }
        CbbDesktopTempPermissionDTO currentDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(updateDTO.getId());
        if (currentDTO.getState() != CbbDesktopTempPermissionState.IN_EFFECT) {
            // “生效中”变为“未生效”，旧的开通对象都需要通知云桌面使用默认的配置
            sendMessageByDeleteRelatedObjects(null, oldRelatedList, null);
            // 1:(新外设有值  修改为未生效 )，2:(新外设没有值 但是发生了变化)通知所有退出
            if (CollectionUtils.isNotEmpty(updateDTO.getUsbTypeIdList()) || updateDTO.getEnableUsbChange()) {
                buildDeskListAndSendMessage(oldRelatedList);
            }
        } else {
            // “生效中”变为“生效中”，通知本次被删除的关联对象的云桌面使用默认的配置
            sendMessageByDeleteRelatedObjects(updateDTO, oldRelatedList, null);
            // 外设有变化 ，通知所有退出 否则在上面只对删除的通知退出
            if (updateDTO.getEnableUsbChange()) {
                buildDeskListAndSendMessage(oldRelatedList);
            }
        }

    }

    private void buildDeskListAndSendMessage(List<DesktopTempPermissionRelatedInfoDTO> oldRelatedList) {
        Map<DesktopTempPermissionRelatedType, List<DesktopTempPermissionRelatedInfoDTO>> oldRelatedMap =
                oldRelatedList.stream().collect(Collectors.groupingBy(DesktopTempPermissionRelatedInfoDTO::getRelatedType));
        List<UUID> deskList = convertMapToList(oldRelatedMap, DesktopTempPermissionRelatedType.DESKTOP);
        List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.getAllRunningVDIDesktopByDeskIdIn(deskList);
        sendTempPermissionMessageToShine(desktopList, false);
        List<UUID> userDeskList = convertMapToList(oldRelatedMap, DesktopTempPermissionRelatedType.USER);
        List<CloudDesktopDTO> userdesktopList = userDesktopMgmtAPI.getAllRunningVDIDesktopByUserIdList(userDeskList);
        sendTempPermissionMessageToShine(userdesktopList, true);
    }

    private List<UUID> convertMapToList(Map<DesktopTempPermissionRelatedType, List<DesktopTempPermissionRelatedInfoDTO>> oldRelatedMap,
            DesktopTempPermissionRelatedType desktopTempPermissionRelatedType) {
        List<UUID> deskList = new ArrayList<>();
        if (Objects.nonNull(oldRelatedMap.get(desktopTempPermissionRelatedType))) {
            deskList = oldRelatedMap.get(desktopTempPermissionRelatedType).stream().map(DesktopTempPermissionRelatedInfoDTO::getRelatedId)
                    .collect(Collectors.toList());
        }
        return deskList;
    }

    /**
     * 删除关联对象时，触发发送配置信息给桌面
     *
     * @param updateDTO      updateDTO，为null时，表示oldRelatedList都是要使用默认的配置
     * @param oldRelatedList oldRelatedList
     * @param desktopTempPermissionDTO  desktopTempPermissionDTO
     */
    private void sendMessageByDeleteRelatedObjects(DesktopTempPermissionUpdateDTO updateDTO,
                                                   List<DesktopTempPermissionRelatedInfoDTO> oldRelatedList,
                                                   DesktopTempPermissionDTO desktopTempPermissionDTO) {

        Map<DesktopTempPermissionRelatedType, List<DesktopTempPermissionRelatedInfoDTO>> oldRelatedMap =
                oldRelatedList.stream().collect(Collectors.groupingBy(DesktopTempPermissionRelatedInfoDTO::getRelatedType));
        // 过滤出删掉的桌面ID列表和用户ID列表
        List<UUID> deleteDesktopIdList = computeDeleteOrAddObj(oldRelatedMap.get(DesktopTempPermissionRelatedType.DESKTOP),
                Objects.nonNull(updateDTO) ? updateDTO.getDesktopIdList() : null, false);
        List<UUID> deleteUserIdList = computeDeleteOrAddObj(oldRelatedMap.get(DesktopTempPermissionRelatedType.USER),
                Objects.nonNull(updateDTO) ? updateDTO.getUserIdList() : null, false);
        // 缓存云桌面策略，避免多次查询
        Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap = new ConcurrentHashMap<>();
        for (UUID desktopId : deleteDesktopIdList) {
            // 临时权限不再关联这个桌面，需要触发发送配置的动作，因为要去生效云桌面策略的配置，或者其他临时权限
            ThreadExecutorsUtils.executeDesktopTempPermissionMessage(() ->
                    sendMessageByDeleteRelatedObj(desktopId, DesktopTempPermissionRelatedType.DESKTOP, deskStrategyMap, desktopTempPermissionDTO));
        }

        for (UUID userId : deleteUserIdList) {
            // 临时权限不再关联这个这个用户，需要触发发送配置的动作，因为要去生效云桌面策略的配置，或者其他临时权限
            ThreadExecutorsUtils.executeDesktopTempPermissionMessage(() ->
                    sendMessageByDeleteRelatedObj(userId, DesktopTempPermissionRelatedType.USER, deskStrategyMap, desktopTempPermissionDTO));
        }

        // 外设没有变化 ，并且外设不为空，（在删除临时权限的场景 主要进行删除通知，getEnableUsbChange 为false
        if (Objects.nonNull(updateDTO) && !updateDTO.getEnableUsbChange() && CollectionUtils.isNotEmpty(updateDTO.getUsbTypeIdList())) {
            // 通知被删除的 桌面退出
            List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.getAllRunningVDIDesktopByDeskIdIn(deleteDesktopIdList);
            sendTempPermissionMessageToShine(desktopList, false);
            List<CloudDesktopDTO> userdesktopList = userDesktopMgmtAPI.getAllRunningVDIDesktopByUserIdList(deleteUserIdList);
            sendTempPermissionMessageToShine(userdesktopList, true);
        }
    }

    private void sendTempPermissionMessageToShine(List<CloudDesktopDTO> desktopIdList,boolean isByUser) {
        for (CloudDesktopDTO desktopDTO : desktopIdList) {
            ThreadExecutorsUtils.executeDesktopTempPermissionMessage(() -> {
                if (isByUser
                        && desktopTempPermissionService.existInEffectPermission(desktopDTO.getCbbId(), DesktopTempPermissionRelatedType.DESKTOP)) {
                    // 因为用户被关联临时权限需要发送通知给用户下的桌面时，需要检查这个桌面是不是有直接关联的生效中的临时权限，有就不需要发了
                    LOGGER.info("桌面[{}]存在优先级更高的生效中的临时权限，无需发送消息", desktopDTO.getCbbId());
                    return;
                }
                notifyTerminalCloseEST(desktopDTO);
            });
        }
    }

    private void sendMessageByDeleteRelatedObj(UUID relatedId, DesktopTempPermissionRelatedType type,
            Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap, DesktopTempPermissionDTO desktopTempPermissionDTO) {
        if (type == DesktopTempPermissionRelatedType.DESKTOP) {
            try {
                CloudDesktopDetailDTO desktopDetail = userDesktopMgmtAPI.getDesktopDetailById(relatedId);
                CloudDesktopDTO cloudDesktopDTO = desktopDetail.convertMinCloudDesktopDTO();
                sendMessageByDeleteDesktop(cloudDesktopDTO.getUserId(), cloudDesktopDTO, deskStrategyMap, desktopTempPermissionDTO);
            } catch (Exception e) {
                LOGGER.error("临时权限去除关联关系时查询桌面信息桌面[{}]异常", relatedId, e);
            }
        } else if (type == DesktopTempPermissionRelatedType.USER) {
            doSendMessageByDeleteRelatedObjByUser(relatedId, deskStrategyMap, desktopTempPermissionDTO);
        }
    }

    private void doSendMessageByDeleteRelatedObjByUser(UUID userId, Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap,
                                                       DesktopTempPermissionDTO desktopTempPermissionDTO) {
        List<CloudDesktopDTO> cloudDesktopList = userDesktopMgmtAPI.getAllDesktopByUserId(userId);
        cloudDesktopList = cloudDesktopList.stream()
                .filter(item -> Objects.equals(item.getDesktopState(), CbbCloudDeskState.RUNNING.name())).collect(Collectors.toList());
        // 这里是该用户关联的单会话桌面处理
        for (CloudDesktopDTO cloudDesktop : cloudDesktopList) {
            try {
                sendMessageByDeleteDesktop(userId, cloudDesktop, deskStrategyMap, desktopTempPermissionDTO);
            } catch (Exception e) {
                LOGGER.error("临时权限去除关联关系时，发送配置信息给桌面[{}]异常", cloudDesktop.getCbbId(), e);
            }
        }

        // 该用户关联的多会话桌面处理
        List<HostUserEntity> hostUserList = hostUserService.findByUserId(userId);
        hostUserList = hostUserList.stream().filter(item -> StringUtils.isNotBlank(item.getTerminalId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hostUserList)) {
            return;
        }
        List<UUID> hostDeskIdList = hostUserList.stream().map(HostUserEntity::getDesktopId).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hostDeskIdList)) {
            return;
        }
        Map<UUID, HostUserEntity> deskIdTerminalIdMap = hostUserList.stream().collect(
                Collectors.toMap(HostUserEntity::getDesktopId, t -> t, (v1, v2) -> v1));
        List<CloudDesktopDTO> hostDeskList = userDesktopMgmtAPI.listDesktopByDesktopIds(hostDeskIdList);
        hostDeskList = hostDeskList.stream()
                .filter(item -> Objects.equals(item.getDesktopState(), CbbCloudDeskState.RUNNING.name())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hostDeskList)) {
            return;
        }
        List<UUID> effectIdList;
        CbbDesktopTempPermissionDTO tempPermissionDTO;
        String terminalId;
        for (CloudDesktopDTO cloudDesktop : hostDeskList) {
            if (!deskIdTerminalIdMap.containsKey(cloudDesktop.getCbbId())) {
                continue;
            }
            terminalId = deskIdTerminalIdMap.get(cloudDesktop.getCbbId()).getTerminalId();
            try {
                effectIdList = desktopTempPermissionService.listInEffectPermissionId(cloudDesktop.getCbbId(),
                        DesktopTempPermissionRelatedType.DESKTOP);
                if (CollectionUtils.isNotEmpty(effectIdList)) {
                    // 多会话桌面有桌面级别生效的临时权限，只要给这个用户的terminalId发送消息就可以
                    tempPermissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(effectIdList.get(0));
                    sendTempPermissionMessageToDesktopByTerminalId(cloudDesktop.getCbbId(), terminalId, tempPermissionDTO, deskStrategyMap);
                    continue;
                }

                // 然后查询该用户直接生效的临时权限
                effectIdList = desktopTempPermissionService.listInEffectPermissionId(userId, DesktopTempPermissionRelatedType.USER);
                if (CollectionUtils.isNotEmpty(effectIdList)) {
                    tempPermissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(effectIdList.get(0));
                    sendTempPermissionMessageToDesktopByTerminalId(cloudDesktop.getCbbId(), terminalId, tempPermissionDTO, deskStrategyMap);
                    continue;
                }

                // 发送云桌面策略的配置信息给桌面
                sendTempPermissionMessageToDesktopByTerminalId(cloudDesktop.getCbbId(), terminalId, null, deskStrategyMap);
            } catch (Exception e) {
                LOGGER.error("临时权限去除关联关系时，发送配置信息给桌面[{}]异常", cloudDesktop.getCbbId(), e);
            }
        }
    }

    private void sendMessageByDeleteDesktop(UUID userId, CloudDesktopDTO cbbDeskInfo, Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap,
            DesktopTempPermissionDTO desktopTempPermissionDTO) throws BusinessException {

        if (!CbbCloudDeskState.RUNNING.toString().equals(cbbDeskInfo.getDesktopState())) {
            LOGGER.info("临时权限去除桌面关联关系时，桌面[{}]不处于运行中状态，不需要发送配置", cbbDeskInfo.getCbbId());
            return;
        }

        CbbDeskStrategyNotifyDTO strategy = getCbbDeskStrategyNotifyDTO(cbbDeskInfo.getCbbId(), cbbDeskInfo.getDesktopStrategyId(), deskStrategyMap);
        if (Objects.isNull(strategy)) {
            return;
        }

        // 存在直接绑定桌面且生效的临时权限，发送配置信息给桌面
        List<UUID> effectIdList =
                desktopTempPermissionService.listInEffectPermissionId(cbbDeskInfo.getCbbId(), DesktopTempPermissionRelatedType.DESKTOP);
        if (CollectionUtils.isNotEmpty(effectIdList)) {
            CbbDesktopTempPermissionDTO tempPermissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(effectIdList.get(0));
            if (cbbDeskInfo.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                sendTempPermissionMessageToMultiDesktop(cbbDeskInfo.getCbbId(), tempPermissionDTO, deskStrategyMap);
            } else {
                sendTempPermissionMessageToDesktop(cbbDeskInfo.getCbbId(), tempPermissionDTO, deskStrategyMap);
            }
            return;
        }

        // 查桌面正在使用的用户，触发发送配置信息
        if (cbbDeskInfo.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            // 多会话桌面
            List<HostUserEntity> hostUserEntityList = hostUserService.findByDeskId(cbbDeskInfo.getCbbId());
            hostUserEntityList = hostUserEntityList.stream().filter(item -> StringUtils.isNotBlank(item.getTerminalId())
                            && (Objects.isNull(userId) || Objects.equals(item.getUserId(), userId))).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(hostUserEntityList)) {
                LOGGER.info("桌面[{}]无关联的用户使用中的终端", cbbDeskInfo.getCbbId());
                return;
            }
            for (HostUserEntity hostUserEntity : hostUserEntityList) {
                if (StringUtils.isBlank(hostUserEntity.getTerminalId()) || Objects.isNull(hostUserEntity.getUserId())) {
                    continue;
                }
                effectIdList = desktopTempPermissionService.listInEffectPermissionId(hostUserEntity.getUserId(), DesktopTempPermissionRelatedType.USER);
                if (CollectionUtils.isNotEmpty(effectIdList)) {
                    CbbDesktopTempPermissionDTO tempPermissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(effectIdList.get(0));
                    sendTempPermissionMessageToDesktopByTerminalId(cbbDeskInfo.getCbbId(), hostUserEntity.getTerminalId(), tempPermissionDTO,
                            deskStrategyMap);
                } else {
                    sendTempPermissionMessageToDesktopByTerminalId(cbbDeskInfo.getCbbId(), hostUserEntity.getTerminalId(), null,
                            deskStrategyMap);
                }
            }
            return;
        }

        // 单会话桌面
        if (Objects.isNull(userId)) {
            LOGGER.info("临时权限去除桌面关联关系时，桌面[{}]无关联用户，不需要发送配置", cbbDeskInfo.getCbbId());
            return;
        }
        // 存在直接绑定用户且生效的临时权限，发送配置信息给桌面
        effectIdList = desktopTempPermissionService.listInEffectPermissionId(userId, DesktopTempPermissionRelatedType.USER);
        if (CollectionUtils.isNotEmpty(effectIdList)) {
            CbbDesktopTempPermissionDTO tempPermissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(effectIdList.get(0));
            sendTempPermissionMessageToDesktop(cbbDeskInfo.getCbbId(), tempPermissionDTO, deskStrategyMap);
            return;
        }
        // 发送云桌面策略的配置信息给桌面
        sendTempPermissionMessageToDesktop(cbbDeskInfo.getCbbId(), null, deskStrategyMap);

        //由于要通知SHINE 原先临时权限的退出处理逻辑
        //临时权限为空，或者非删除操作 直不进行处理 或者临时权限的USB 外设为空
        if (Objects.isNull(desktopTempPermissionDTO) || CbbDesktopTempPermissionState.DELETING != desktopTempPermissionDTO.getState()
                || CollectionUtils.isEmpty(desktopTempPermissionDTO.getUsbTypeList())) {
            return;
        }
        //通知SHINE 断开EST 连接
        notifyTerminalCloseEST(cbbDeskInfo);
    }

    /**
     *通知SHINE 断开EST 连接
     * @param cbbDeskInfo 桌面信息
     */
    private void notifyTerminalCloseEST(CloudDesktopDTO cbbDeskInfo) {
        // 进行通知SHINE 退出EST窗口
        String terminalId = cbbDeskInfo.getTerminalId();
        // 终端ID 为空 直接跳过
        if (StringUtils.isBlank(terminalId)) {
            return;
        }
        CbbTerminalBasicInfoDTO currentTerminalInfo;
        try {
            currentTerminalInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        } catch (BusinessException e) {
            LOGGER.error("临时权限获取终端信息失败，terminalId：" + terminalId, e);
            return;
        }
        // 发送给在线终端
        if (currentTerminalInfo.getState() == CbbTerminalStateEnums.ONLINE) {
            QuitVmToTerminalDTO quitVmToTerminalDTO = new QuitVmToTerminalDTO();
            quitVmToTerminalDTO.setEndTime(new Date());
            quitVmToTerminalDTO.setDesktopId(cbbDeskInfo.getId());
            // 外设策略到期给终端
            notifyTerminalDesktopIsRobbed(cbbDeskInfo.getId(), terminalId, quitVmToTerminalDTO);
        }
    }

    /**
     * 通知已运行云桌面的终端云桌面 在外设策略到期 终端退出
     */
    private void notifyTerminalDesktopIsRobbed(UUID desktopId, String lastLoginTerminalId, QuitVmToTerminalDTO quitVmToTerminalDTO) {
        LOGGER.debug("发送外设策略到期退出给终端[{}]，桌面ID=[{}]", lastLoginTerminalId, desktopId);
        try {
            CbbShineMessageRequest messageRequest = CbbShineMessageRequest.create(ShineAction.TEMP_PERMISSION_EXPIRE_NOTIFY, lastLoginTerminalId);
            messageRequest.setContent(quitVmToTerminalDTO);
            cbbTranspondMessageHandlerAPI.request(messageRequest);
        } catch (Exception e) {
            LOGGER.error("发送外设策略到期退出消息给终端[{}]，桌面ID=[{}]", lastLoginTerminalId, desktopId, e);
        }
    }

    private void sendEffectMessageByPermissionIdAndUpdate(UUID permissionId,DesktopTempPermissionUpdateDTO updateDTO) throws BusinessException {
        Assert.notNull(permissionId, "permissionId must not be null");

        CbbDesktopTempPermissionDTO permissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(permissionId);

        long current = System.currentTimeMillis();
        if (permissionDTO.getStartTime().getTime() > current || permissionDTO.getEndTime().getTime() < current) {
            LOGGER.info("临时权限[{}]不在生效时间内，无需发送通知给桌面", permissionDTO.getName());
            return;
        }
        // 桌面优先级最高，直接查询
        List<UUID> desktopIdList = desktopTempPermissionService.listDesktopIdByRelatedDesktopAndState(permissionId, CbbCloudDeskState.RUNNING);

        // 用户关联的桌面需要去除上面已有的桌面，同时要去除已有优先级更高的临时权限的桌面
        List<UUID> userDesktopIdList = desktopTempPermissionService.listDesktopIdByRelatedUserAndDeskState(permissionId, CbbCloudDeskState.RUNNING);
        userDesktopIdList = userDesktopIdList.stream().filter(id -> Objects.nonNull(id) && !desktopIdList.contains(id)).collect(Collectors.toList());
        Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap = new ConcurrentHashMap<>();
        sendTempPermissionMessageToDesktopsByRelatedDeskType(desktopIdList, permissionDTO, deskStrategyMap);
        sendTempPermissionMessageToDesktopsByRelatedDeskType(userDesktopIdList, permissionDTO, deskStrategyMap);

        // 多会话
        List<UUID> userIdList = desktopTempPermissionService.listRelatedIdByPermissionIdAndRelatedType(permissionId,
                DesktopTempPermissionRelatedType.USER);
        sendTempPermissionMessageToMultiDesktopsByRelatedUserType(userIdList, permissionDTO, deskStrategyMap);
    }

    /**
     * 发送生效消息给运行中的桌面
     *
     * @param permissionId permissionId
     */
    @Override
    public void sendEffectMessageByPermissionId(UUID permissionId) throws BusinessException {
        Assert.notNull(permissionId, "permissionId must not be null");

        CbbDesktopTempPermissionDTO permissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(permissionId);

        long current = System.currentTimeMillis();
        if (permissionDTO.getStartTime().getTime() > current || permissionDTO.getEndTime().getTime() < current) {
            LOGGER.info("临时权限[{}]不在生效时间内，无需发送通知给桌面", permissionDTO.getName());
            return;
        }
        // 桌面优先级最高，直接查询
        List<UUID> desktopIdList = desktopTempPermissionService.listDesktopIdByRelatedDesktopAndState(permissionId, CbbCloudDeskState.RUNNING);

        // 用户关联的桌面需要去除上面已有的桌面，同时要去除已有优先级更高的临时权限的桌面
        List<UUID> userDesktopIdList = desktopTempPermissionService.listDesktopIdByRelatedUserAndDeskState(permissionId, CbbCloudDeskState.RUNNING);
        userDesktopIdList = userDesktopIdList.stream().filter(id -> Objects.nonNull(id) && !desktopIdList.contains(id)).collect(Collectors.toList());
        Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap = new ConcurrentHashMap<>();
        sendTempPermissionMessageToDesktopsByRelatedDeskType(desktopIdList, permissionDTO, deskStrategyMap);
        sendTempPermissionMessageToDesktopsByRelatedDeskType(userDesktopIdList, permissionDTO, deskStrategyMap);

        // 多会话
        List<UUID> userIdList = desktopTempPermissionService.listRelatedIdByPermissionIdAndRelatedType(permissionId,
                DesktopTempPermissionRelatedType.USER);
        sendTempPermissionMessageToMultiDesktopsByRelatedUserType(userIdList, permissionDTO, deskStrategyMap);
    }

    @Override
    public void createUserExpireNoticeMsg(UUID permissionId) throws BusinessException {
        Assert.notNull(permissionId, "permissionId must not be null");

        CbbDesktopTempPermissionDTO permissionDTO = cbbDesktopTempPermissionAPI.getDesktopTempPermission(permissionId);
        List<DesktopTempPermissionRelatedInfoDTO> relatedInfoList = desktopTempPermissionService.listRelatedInfo(permissionId);
        if (CollectionUtils.isEmpty(relatedInfoList)) {
            LOGGER.info("临时权限[{}]无关联对象，无需创建到期用户消息通知", permissionDTO.getName());
            return;
        }

        // 找出已发送的对象，下面用来过滤
        List<UUID> hadSendUserIdList = computeUserIdToCreateUserMsg(relatedInfoList, DesktopTempPermissionRelatedType.USER, true);
        List<UUID> hadSendDeskIdList = computeUserIdToCreateUserMsg(relatedInfoList, DesktopTempPermissionRelatedType.DESKTOP, true);
        Set<UUID> hadSendIdSet = Sets.newHashSet(hadSendUserIdList);
        hadSendIdSet.addAll(userDesktopService.findUserIdByDesktopIdList(hadSendDeskIdList));

        // 找出未发送的对象
        List<UUID> unSendUserIdList = computeUserIdToCreateUserMsg(relatedInfoList, DesktopTempPermissionRelatedType.USER, false);
        List<UUID> unSendDeskIdList = computeUserIdToCreateUserMsg(relatedInfoList, DesktopTempPermissionRelatedType.DESKTOP, false);
        if (CollectionUtils.isNotEmpty(unSendDeskIdList)) {
            unSendUserIdList.addAll(userDesktopService.findUserIdByDesktopIdList(unSendDeskIdList));
        }
        unSendUserIdList = unSendUserIdList.stream().filter(item -> !hadSendIdSet.contains(item)).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(unSendUserIdList)) {
            LOGGER.info("临时权限[{}]无需要创建到期用户消息通知的关联对象", permissionDTO.getName());
            return;
        }

        // 创建用户消息
        String title = LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_ADD_USER_MSG_TITLE_EXPIRE);
        String content = LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_USER_MSG_WILL_STOP,
                DATE_FORMAT.format(permissionDTO.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
        content = content + "\n" + LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_USER_MSG_WILL_STOP_USB_TIP);
        if (createUserMessage(unSendUserIdList, title, content, permissionDTO.getName())) {
            LOGGER.info("临时权限[{}]已创建到期用户消息通知", permissionDTO.getName());
            // 修改hasSendExpireNotice值为true
            desktopTempPermissionService.updateHasSendExpireNotice(permissionId, true);
            return;
        }
        LOGGER.error("临时权限[{}]创建到期用户消息通知失败", permissionDTO.getName());
    }

    private void sendTempPermissionMessageToDesktopsByRelatedDeskType(List<UUID> desktopIdList, CbbDesktopTempPermissionDTO permissionDTO,
                                                                      Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap) {
        for (UUID deskId : desktopIdList) {
            ThreadExecutorsUtils.executeDesktopTempPermissionMessage(() -> {
                try {
                    CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
                    if (cbbDeskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                        // 多会话根据terminalId发送给OC
                        sendTempPermissionMessageToMultiDesktop(deskId, permissionDTO, deskStrategyMap);
                        return;
                    }
                    sendTempPermissionMessageToDesktop(deskId, permissionDTO, deskStrategyMap);
                } catch (Exception e) {
                    LOGGER.error("桌面[{}]发送临时权限消息[{}]异常", deskId, JSON.toJSONString(permissionDTO), e);
                }
            });
        }
    }

    private void sendTempPermissionMessageToMultiDesktopsByRelatedUserType(List<UUID> userIdList, CbbDesktopTempPermissionDTO permissionDTO,
                                                                           Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<HostUserEntity> hostUserEntityList = hostUserService.findByUserIds(userIdList);
        hostUserEntityList = hostUserEntityList.stream()
                .filter(item -> StringUtils.isNotBlank(item.getTerminalId()) && Objects.nonNull(item.getDesktopId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hostUserEntityList)) {
            return;
        }
        Map<UUID, List<HostUserEntity>> hostUserListMap = hostUserEntityList.stream().collect(Collectors.groupingBy(HostUserEntity::getDesktopId));
        for (Map.Entry<UUID, List<HostUserEntity>> entry : hostUserListMap.entrySet()) {
            UUID deskId = entry.getKey();
            if (CollectionUtils.isEmpty(entry.getValue())) {
                continue;
            }
            if (desktopTempPermissionService.existInEffectPermission(deskId, DesktopTempPermissionRelatedType.DESKTOP)) {
                // 因为用户被关联临时权限需要发送通知给用户下的桌面时，需要检查这个桌面是不是有直接关联的生效中的临时权限，有就不需要发了
                LOGGER.info("桌面[{}]存在优先级更高的生效中的临时权限，无需发送消息", deskId);
                return;
            }
            for (HostUserEntity hostUserEntity : entry.getValue()) {
                ThreadExecutorsUtils.executeDesktopTempPermissionMessage(() -> {
                    try {
                        sendTempPermissionMessageToDesktopByTerminalId(deskId, hostUserEntity.getTerminalId(), permissionDTO, deskStrategyMap);
                    } catch (Exception e) {
                        LOGGER.error("桌面[{}]发送临时权限消息[{}]异常", deskId, JSON.toJSONString(permissionDTO), e);
                    }
                });
            }
        }
    }

    private void sendTempPermissionMessageToMultiDesktop(UUID desktopId, CbbDesktopTempPermissionDTO permissionDTO,
                                                    Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap) {
        List<String> terminalIdList = hostUserService.listTerminalIdByDeskId(desktopId);
        if (CollectionUtils.isEmpty(terminalIdList)) {
            return;
        }
        for (String terminalId : terminalIdList) {
            sendDiskMappingToDesktop(desktopId, terminalId, permissionDTO, deskStrategyMap);
            sendClipBoardModeToDesktop(desktopId, terminalId, permissionDTO, deskStrategyMap);
        }
    }

    private void sendTempPermissionMessageToDesktopByTerminalId(UUID desktopId, String terminalId, CbbDesktopTempPermissionDTO permissionDTO,
                                                    Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap) {
        sendDiskMappingToDesktop(desktopId, terminalId, permissionDTO, deskStrategyMap);
        sendClipBoardModeToDesktop(desktopId, terminalId, permissionDTO, deskStrategyMap);
    }

    private void sendTempPermissionMessageToDesktop(UUID desktopId, CbbDesktopTempPermissionDTO permissionDTO,
                                                    Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap) {
        sendDiskMappingToDesktop(desktopId, null, permissionDTO, deskStrategyMap);
        sendClipBoardModeToDesktop(desktopId, null, permissionDTO, deskStrategyMap);
    }

    /**
     * 发送磁盘映射消息
     *
     * @param deskId          桌面id
     * @param terminalId      终端标识
     * @param permissionDTO   permissionDTO
     * @param deskStrategyMap deskStrategyMap
     */
    private void sendDiskMappingToDesktop(UUID deskId, @Nullable String terminalId, CbbDesktopTempPermissionDTO permissionDTO,
                                          Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap) {
        CbbDeskStrategyNotifyDTO deskStrategyVDIDTO = getCbbDeskStrategyNotifyDTO(deskId, null, deskStrategyMap);
        if (Objects.isNull(deskStrategyVDIDTO)) {
            return;
        }
        CbbDiskMappingDTO cbbDiskMappingDTO = new CbbDiskMappingDTO();
        if (Objects.nonNull(permissionDTO) && Objects.nonNull(permissionDTO.getEnableDiskMapping())) {
            cbbDiskMappingDTO.setEnableDiskMapping(permissionDTO.getEnableDiskMapping());
            cbbDiskMappingDTO.setEnableDiskMappingWriteable(permissionDTO.getEnableDiskMappingWriteable());
        } else {
            cbbDiskMappingDTO.setEnableDiskMapping(deskStrategyVDIDTO.getEnableDiskMapping());
            cbbDiskMappingDTO.setEnableDiskMappingWriteable(deskStrategyVDIDTO.getEnableDiskMappingWriteable());
        }
        // USB存储设备映射模式
        CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode =
                Objects.nonNull(permissionDTO) && Objects.nonNull(permissionDTO.getUsbStorageDeviceMappingMode())
                ? permissionDTO.getUsbStorageDeviceMappingMode() : deskStrategyVDIDTO.getUsbStorageDeviceMappingMode();
        usbStorageDeviceMappingMode.convertToDiskMapping(cbbDiskMappingDTO);
        // 受到临时权限影响 不显示GT 弹出
        cbbDiskMappingDTO.setEnableMessageDisplay(Boolean.FALSE);
        cbbDiskMappingDTO.setEnableNetDiskMapping(deskStrategyVDIDTO.getEnableNetDiskMapping());
        cbbDiskMappingDTO.setEnableCDRomMapping(deskStrategyVDIDTO.getEnableCDRomMapping());
        cbbDiskMappingDTO.setEnableNetDiskMappingWriteable(deskStrategyVDIDTO.getEnableNetDiskMappingWriteable());
        cbbDiskMappingDTO.setEnableCDRomMappingWriteable(deskStrategyVDIDTO.getEnableCDRomMappingWriteable());
        cbbDiskMappingDTO.setEnableLanAutoDetection(deskStrategyVDIDTO.getEnableLanAutoDetection());

        try {
            if (StringUtils.isNotBlank(terminalId)) {
                cbbVDIDeskStrategyMgmtAPI.sendDiskMappingToDesktopByTerminalId(deskId, terminalId, cbbDiskMappingDTO);
            } else {
                cbbVDIDeskStrategyMgmtAPI.sendDiskMappingToDesktop(deskId, cbbDiskMappingDTO);
            }
        } catch (Exception e) {
            LOGGER.error("发送磁盘映射消息通知桌面[{}]异常, cbbDiskMappingDTO[{}]", deskId, JSON.toJSONString(cbbDiskMappingDTO), e);
        }
    }

    /**
     * 发送剪切板模式消息
     *
     * @param deskId          桌面id
     * @param terminalId      终端标识
     * @param permissionDTO   permissionDTO
     * @param deskStrategyMap deskStrategyMap
     */
    private void sendClipBoardModeToDesktop(UUID deskId, @Nullable String terminalId, CbbDesktopTempPermissionDTO permissionDTO,
                                            Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap) {
        ClipboardModeDTO clipboardModeDTO = new ClipboardModeDTO();
        if (Objects.nonNull(permissionDTO) && Objects.nonNull(permissionDTO.getClipBoardMode())) {
            clipboardModeDTO.setClipBoardMode(permissionDTO.getClipBoardMode());
            clipboardModeDTO.setClipBoardSupportTypeArr(permissionDTO.getClipBoardSupportTypeArr());
        } else {
            CbbDeskStrategyNotifyDTO deskStrategyVDI = getCbbDeskStrategyNotifyDTO(deskId, null, deskStrategyMap);
            if (Objects.isNull(deskStrategyVDI)) {
                return;
            }
            clipboardModeDTO.setClipBoardMode(deskStrategyVDI.getClipBoardMode());
            clipboardModeDTO.setClipBoardSupportTypeArr(deskStrategyVDI.getClipBoardSupportTypeArr());
        }

        try {
            if (StringUtils.isNotBlank(terminalId)) {
                cbbVDIDeskStrategyMgmtAPI.sendClipBoardModeToDesktopByTerminalId(deskId, terminalId, clipboardModeDTO);
            } else {
                cbbVDIDeskStrategyMgmtAPI.sendClipBoardModeToDesktop(deskId, clipboardModeDTO);
            }
        } catch (Exception e) {
            LOGGER.error("发送发送剪切板模式消息通知桌面[{}]异常, clipboardModeDTO[{}]", deskId, JSON.toJSONString(clipboardModeDTO), e);
        }
    }

    private CbbDeskStrategyNotifyDTO getCbbDeskStrategyNotifyDTO(UUID desktopId, UUID strategyId,
                                                                 Map<UUID, CbbDeskStrategyNotifyDTO> deskStrategyMap) {
        if (Objects.isNull(strategyId)) {
            strategyId = cbbVDIDeskMgmtAPI.getDeskStrategyIdByDeskId(desktopId);
        }
        if (Objects.isNull(strategyId)) {
            // null
            return null;
        }
        CbbDeskStrategyNotifyDTO deskStrategyNotifyDTO = deskStrategyMap.get(strategyId);
        if (Objects.nonNull(deskStrategyNotifyDTO)) {
            return deskStrategyNotifyDTO;
        }
        try {
            deskStrategyNotifyDTO = cbbDeskStrategyCommonAPI.getDeskStrategyNeedNotifyInfo(strategyId);
            deskStrategyMap.put(strategyId, deskStrategyNotifyDTO);
            return deskStrategyNotifyDTO;
        } catch (BusinessException e) {
            LOGGER.error("发送磁盘映射消息通知桌面[{}]时，查询云桌面策略[{}]异常", desktopId, strategyId, e);
            // null
            return null;
        }
    }

    /**
     * 创建临时权限后，创建用户消息
     *
     * @param permissionId 临时权限ID
     */
    private void createUserMessageByCreatePermission(UUID permissionId) throws BusinessException {
        CbbDesktopTempPermissionDTO permission = cbbDesktopTempPermissionAPI.getDesktopTempPermission(permissionId);
        List<UUID> userIdList = desktopTempPermissionService.listAllUserIdByPermissionId(permissionId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }

        String title = LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_ADD_USER_MSG_TITLE_ADD);

        String permissionContent = buildPermissionContent(permission);
        // 您申请的临时权限已开通，{0}。
        String content = LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_USER_MSG_OPEN, permissionContent);

        // 添加usb外设策略会导致强制注销VDI云桌面的会话的提示
        if (CollectionUtils.isNotEmpty(permission.getUsbTypeIdList())) {
            content = content + "\n" + LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_USER_MSG_USB_TIP);
        }

        // 创建用户消息
        createUserMessage(userIdList, title, content, permission.getName());
    }

    /**
     * 更新临时权限，计算新增和删除的对象，创建用户消息
     *
     * @param updateDTO      updateDTO
     * @param oldRelatedList oldRelatedList
     */
    private void createUserMessageByUpdatePermission(DesktopTempPermissionUpdateDTO updateDTO,
                                                     List<DesktopTempPermissionRelatedInfoDTO> oldRelatedList) throws BusinessException {

        Map<DesktopTempPermissionRelatedType, List<DesktopTempPermissionRelatedInfoDTO>> oldRelatedMap =
                oldRelatedList.stream().collect(Collectors.groupingBy(DesktopTempPermissionRelatedInfoDTO::getRelatedType));

        List<UUID> addUserIdList = computeDeleteOrAddObj(oldRelatedMap.get(DesktopTempPermissionRelatedType.USER),
                updateDTO.getUserIdList(), true);
        List<UUID> addDesktopIdList = computeDeleteOrAddObj(oldRelatedMap.get(DesktopTempPermissionRelatedType.DESKTOP),
                updateDTO.getDesktopIdList(), true);
        if (CollectionUtils.isNotEmpty(addDesktopIdList)) {
            addUserIdList.addAll(userDesktopService.findUserIdByDesktopIdList(addDesktopIdList));
        }
        if (CollectionUtils.isNotEmpty(addUserIdList)) {
            addUserIdList = addUserIdList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }
        CbbDesktopTempPermissionDTO permission = cbbDesktopTempPermissionAPI.getDesktopTempPermission(updateDTO.getId());
        // 创建用户消息
        String title = LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_ADD_USER_MSG_TITLE_ADD);
        String permissionContent = buildPermissionContent(permission);
        String content = LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_USER_MSG_OPEN, permissionContent);
        createUserMessage(addUserIdList, title, content, permission.getName());

        List<UUID> deleteUserIdList = computeDeleteOrAddObj(oldRelatedMap.get(DesktopTempPermissionRelatedType.USER),
                updateDTO.getUserIdList(), false);
        List<UUID> deleteDesktopIdList = computeDeleteOrAddObj(oldRelatedMap.get(DesktopTempPermissionRelatedType.DESKTOP),
                updateDTO.getDesktopIdList(), false);

        if (CollectionUtils.isNotEmpty(deleteDesktopIdList)) {
            deleteUserIdList.addAll(userDesktopService.findUserIdByDesktopIdList(deleteDesktopIdList));
        }
        if (CollectionUtils.isNotEmpty(deleteUserIdList)) {
            deleteUserIdList = deleteUserIdList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }

        title = LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_ADD_USER_MSG_TITLE_DELETE);
        content = LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_USER_MSG_STOP);
        createUserMessage(deleteUserIdList, title, content, permission.getName());
    }

    /**
     * 过虑出需要新增或者删除的关联对象ID
     *
     * @param oldList      oldList
     * @param updateIdList updateIdList
     * @param isAdd        isAdd
     * @return List<UUID>
     */
    private List<UUID> computeDeleteOrAddObj(List<DesktopTempPermissionRelatedInfoDTO> oldList, List<UUID> updateIdList, boolean isAdd) {
        if (isAdd) {
            // 筛选待新增的
            if (CollectionUtils.isEmpty(updateIdList)) {
                return new ArrayList<>();
            }
            if (CollectionUtils.isEmpty(oldList)) {
                return updateIdList;
            }
            Set<UUID> oldIdSet = oldList.stream().map(DesktopTempPermissionRelatedInfoDTO::getRelatedId).collect(Collectors.toSet());
            return updateIdList.stream().filter(id -> !oldIdSet.contains(id)).collect(Collectors.toList());
        }
        // 筛选待删除的
        if (CollectionUtils.isEmpty(oldList)) {
            return new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(updateIdList)) {
            return oldList.stream().map(DesktopTempPermissionRelatedInfoDTO::getRelatedId).collect(Collectors.toList());
        }
        Set<UUID> updateIdSet = Sets.newHashSet(updateIdList);
        return oldList.stream().map(DesktopTempPermissionRelatedInfoDTO::getRelatedId)
                .filter(item -> !updateIdSet.contains(item)).collect(Collectors.toList());
    }

    /**
     * 删除临时权限，创建用户消息
     *
     * @param permission     permission
     * @param oldRelatedList oldRelatedList
     */
    private void createUserMessageByDeletePermission(CbbDesktopTempPermissionDTO permission,
                                                     List<DesktopTempPermissionRelatedInfoDTO> oldRelatedList) {
        Map<DesktopTempPermissionRelatedType, List<DesktopTempPermissionRelatedInfoDTO>> oldRelatedMap =
                oldRelatedList.stream().collect(Collectors.groupingBy(DesktopTempPermissionRelatedInfoDTO::getRelatedType));

        List<DesktopTempPermissionRelatedInfoDTO> relatedList = oldRelatedMap.get(DesktopTempPermissionRelatedType.USER);
        List<UUID> userIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(relatedList)) {
            userIdList = relatedList.stream().map(DesktopTempPermissionRelatedInfoDTO::getRelatedId).collect(Collectors.toList());
        }

        relatedList = oldRelatedMap.get(DesktopTempPermissionRelatedType.DESKTOP);
        if (CollectionUtils.isNotEmpty(relatedList)) {
            List<UUID> desktopIdList = relatedList.stream().map(DesktopTempPermissionRelatedInfoDTO::getRelatedId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(desktopIdList)) {
                userIdList.addAll(userDesktopService.findUserIdByDesktopIdList(desktopIdList));
            }
        }
        userIdList = userIdList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        String title = LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_ADD_USER_MSG_TITLE_DELETE);
        String content = LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_USER_MSG_STOP);
        createUserMessage(userIdList, title, content, permission.getName());
    }

    private boolean createUserMessage(List<UUID> idList, String title, String content, String name) {
        if (CollectionUtils.isEmpty(idList)) {
            return true;
        }
        CreateUserMessageRequest userRequest = new CreateUserMessageRequest(idList.toArray(new UUID[0]), title, content);
        try {
            userMessageAPI.createUserMessage(userRequest);
            return true;
        } catch (Exception e) {
            LOGGER.error("临时权限[{}]，创建用户消息失败，userRequest：{}", name, JSON.toJSONString(userRequest));
            return false;
        }
    }

    private List<UUID> computeUserIdToCreateUserMsg(List<DesktopTempPermissionRelatedInfoDTO> relatedInfoList, DesktopTempPermissionRelatedType type,
                                                    boolean hasSend) {
        if (hasSend) {
            return relatedInfoList.stream().filter(item -> Boolean.TRUE.equals(item.getHasSendExpireNotice()) && item.getRelatedType() == type)
                    .map(DesktopTempPermissionRelatedInfoDTO::getRelatedId).collect(Collectors.toList());
        }
        return relatedInfoList.stream().filter(item -> !Boolean.TRUE.equals(item.getHasSendExpireNotice()) && item.getRelatedType() == type)
                .map(DesktopTempPermissionRelatedInfoDTO::getRelatedId).collect(Collectors.toList());
    }

    /**
     * 权限内容
     * 开通权限：{0}
     *
     * @param permission permission
     * @return 开通权限：{0}
     */
    private String buildPermissionContent(CbbDesktopTempPermissionDTO permission) {
        // 开通权限：剪切板[VDI虚机向PC桌面传输，仅拷贝5个字符；PC桌面向VDI虚机传输，仅拷贝10个字符]，本地磁盘映射[读写]，
        // 外设策略[{0}]，开通时间：2023-05-15 14:22:44，结束时间：2023-05-16 14:24:07。
        List<String> valueList = new ArrayList<>();
        if (Objects.nonNull(permission.getClipBoardMode())) {
            // 剪切板[{0}]
            String label = buildClipBoardLabel(permission);
            if (StringUtils.isNotBlank(label)) {
                valueList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD, label));
            }
        }

        if (Objects.nonNull(permission.getEnableDiskMapping())) {
            // 本地磁盘映射[{0}]
            String diskMappingLabel = buildDiskMappingLabel(permission);
            if (StringUtils.isNotBlank(diskMappingLabel)) {
                valueList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_DISK_MAPPING, diskMappingLabel));
            }
        }

        // USB存储设备映射
        if (Objects.nonNull(permission.getUsbStorageDeviceMappingMode())) {
            String usbStorageDeviceLabel = buildUsbStorageDeviceMapping(permission);
            if (StringUtils.isNotBlank(usbStorageDeviceLabel)) {
                valueList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_USB_STORAGE_DEVICE_MAPPING, usbStorageDeviceLabel));
            }
        }

        if (CollectionUtils.isNotEmpty(permission.getUsbTypeIdList())) {
            // 外设策略[{0}]
            String label = buildUsbTypeLabel(permission);
            if (StringUtils.isNotBlank(label)) {
                valueList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_USB, label));
            }
        }

        valueList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_EFFECT_TIME,
                DATE_FORMAT.format(permission.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()),
                DATE_FORMAT.format(permission.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())));

        return LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT, permission.getName(), StringUtils.join(valueList, CONTENT_SPLIT));
    }

    private String buildClipBoardLabel(CbbDesktopTempPermissionDTO permission) {
        CbbClipBoardSupportTypeDTO[] typeArr = permission.getClipBoardSupportTypeArr();
        if (ArrayUtils.isEmpty(typeArr)) {
            return "";
        }
        List<String> keyList = new ArrayList<>();
        List<String> vmToHostList = new ArrayList<>();
        List<String> hostToVmList = new ArrayList<>();
        for (CbbClipBoardSupportTypeDTO type : typeArr) {
            if (CbbClipboardMode.VM_TO_HOST == type.getMode()) {
                buildTypeLabel(type.getType(),vmToHostList,type.getVmToHostCharLimit());
            }
            if (CbbClipboardMode.HOST_TO_VM == type.getMode()) {
                buildTypeLabel(type.getType(),hostToVmList,type.getHostToVmCharLimit());
            }
            if (CbbClipboardMode.NO_LIMIT == type.getMode()) {
                buildTypeLabel(type.getType(),vmToHostList,type.getVmToHostCharLimit());
                buildTypeLabel(type.getType(),hostToVmList,type.getHostToVmCharLimit());
            }
        }
        if (vmToHostList.size() == 0) {
            vmToHostList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_FORBIDDEN));
        }
        if (hostToVmList.size() == 0) {
            hostToVmList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_FORBIDDEN));
        }

        // VDI虚机向PC桌面传输
        if (CollectionUtils.isNotEmpty(vmToHostList)) {
            keyList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_VM_TO_HOST,
                    String.join(CONTENT_SPLIT,vmToHostList)));
        }
        // PC桌面向VDI虚机传输
        if (CollectionUtils.isNotEmpty(hostToVmList)) {
            keyList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_HOST_TO_VM,
                    String.join(CONTENT_SPLIT,hostToVmList)));
        }

        return String.join(CONTENT_SPLIT,keyList);
    }

    private void buildTypeLabel(CbbClipBoardSupportTypeEnum type,List<String> strList,Integer charLimit) {
        if (type == CbbClipBoardSupportTypeEnum.FILE) {
            strList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_VM_TO_HOST_LIMIT_FILE));
        }
        if (type == CbbClipBoardSupportTypeEnum.TEXT) {
            strList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_VM_TO_HOST_LIMIT_TEXT));
            if (Objects.nonNull(charLimit) && charLimit == 0) {
                strList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_TEXT_CHAR_LIMIT,
                        LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_TEXT_CHAR_LIMIT_NOLIMIT)));
            }
            if (Objects.nonNull(charLimit) && charLimit > 0) {
                strList.add(LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_CLIP_BOARD_TEXT_CHAR_LIMIT,String.valueOf(charLimit)));
            }
        }
    }
    
    private String buildDiskMappingLabel(CbbDesktopTempPermissionDTO permission) {
        if (Objects.isNull(permission.getEnableDiskMapping()) || !permission.getEnableDiskMapping()) {
            // 关闭
            return LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_DISK_MAPPING_CLOSE);
        }
        if (Boolean.TRUE.equals(permission.getEnableDiskMappingWriteable())) {
            // 读写
            return LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_DISK_MAPPING_READ_WRITE);
        }
        // 只读
        return LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_DISK_MAPPING_READ);
    }

    private String buildUsbStorageDeviceMapping(CbbDesktopTempPermissionDTO permission) {
        CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingEnum = permission.getUsbStorageDeviceMappingMode();
        if (Objects.isNull(usbStorageDeviceMappingEnum) || usbStorageDeviceMappingEnum == CbbUsbStorageDeviceMappingMode.CLOSED) {
            // USB存储设备映射:关闭
            return LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_USB_STORAGE_DEVICE_MAPPING_CLOSE);
        }
        if (usbStorageDeviceMappingEnum == CbbUsbStorageDeviceMappingMode.READ_WRITE) {
            // USB存储设备映射:读写
            return LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_USB_STORAGE_DEVICE_MAPPING_READ_WRITE);
        }
        // USB存储设备映射:只读
        return LocaleI18nResolver.resolve(RCO_DESK_TEMP_PERMISSION_CONTENT_USB_STORAGE_DEVICE_MAPPING_READ);
    }


    /**
     * 获取外设策略名称列表
     *
     * @param permission permission
     * @return String
     */
    private String buildUsbTypeLabel(CbbDesktopTempPermissionDTO permission) {
        List<CbbUSBTypeDTO> usbTypeList = listUsbTypeDTO(permission);
        if (CollectionUtils.isEmpty(usbTypeList)) {
            return "";
        }
        List<String> usbTypeNameList = usbTypeList.stream().map(CbbUSBTypeDTO::getUsbTypeName).collect(Collectors.toList());
        return StringUtils.join(usbTypeNameList, CONTENT_SPLIT);
    }

    private List<CbbUSBTypeDTO> listUsbTypeDTO(CbbDesktopTempPermissionDTO permission) {
        List<UUID> usbTypeIdList = permission.getUsbTypeIdList();
        if (CollectionUtils.isEmpty(usbTypeIdList)) {
            return Collections.emptyList();
        }
        CbbGetAllUSBTypeDTO useReq = new CbbGetAllUSBTypeDTO();
        CbbUSBTypeDTO[] usbArr;
        try {
            usbArr = cbbUSBTypeMgmtAPI.getAllUSBType(useReq);
        } catch (BusinessException e) {
            LOGGER.error("拼装临时权限[{}]内容时，获取usb外设类型异常", permission.getName(), e);
            return Collections.emptyList();
        }
        if (ArrayUtils.isEmpty(usbArr)) {
            return Collections.emptyList();
        }
        return Arrays.stream(usbArr).filter(item -> usbTypeIdList.contains(item.getId())).collect(Collectors.toList());
    }


    @Override
    public void deleteDesktopTempPermissionByUserId(UUID desktopTempPermissionId) {
        Assert.notNull(desktopTempPermissionId, "desktopTempPermissionId must not be null");

        desktopTempPermissionService.deleteDesktopTempPermissionByUserId(desktopTempPermissionId);
    }

    @Override
    public void deleteDesktopTempPermissionByDeskId(UUID desktopTempPermissionId) {
        Assert.notNull(desktopTempPermissionId, "desktopTempPermissionId must not be null");

        desktopTempPermissionService.deleteDesktopTempPermissionByDeskId(desktopTempPermissionId);
    }
}
