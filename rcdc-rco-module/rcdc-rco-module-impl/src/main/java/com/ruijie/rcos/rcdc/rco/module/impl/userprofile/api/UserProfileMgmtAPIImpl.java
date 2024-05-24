package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileStrategyNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.service.tree.TreeBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.*;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PathTree;
import com.ruijie.rcos.rcdc.rco.module.def.utils.UserProfileHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.IdvTerminalGroupDeskConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserGroupDesktopConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.IdvTerminalGroupDeskConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserGroupDesktopConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserProfileStrategyRelatedViewService;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.cache.UserProfileImportStateCache;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.cache.UserProfileStrategyCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.*;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.*;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.service.ViewUserProfileChildService;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.tx.UserProfileServiceTx;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.crypto.Md5Builder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static java.util.stream.Collectors.toList;

;

/**
 * Description: 用户配置路径管理API实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public class UserProfileMgmtAPIImpl implements UserProfileMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileMgmtAPIImpl.class);

    /**
     * 获取锁的等待时间
     */
    private static final int WAIT_TIME = 1;

    /**
     * 最少子路径数目
     */
    private static final int MIN_CHILD_PATH_NUM = 1;

    /**
     * 前一位
     */
    private static final int AHEAD = 1;

    /**
     * 策略关联的最大路径数量
     */
    private static final int RELATED_PATH_SIZE = 200;

    /**
     * 导入任务锁对象
     */
    public static final Object IMPORT_PATH_LOCK = new Object();

    /**
     * 允许名称之和的最大长度，
     */
    public static final int MAX_NAME_LENGTH = 39;

    /**
     * 分隔符
     */
    public static final String NAME_SEPARATOR = "、";

    /**
     * 反斜杠
     */
    private static final String BACKSLASH = "\\";

    /**
     * 用于切割的反斜杠
     */
    private static final String CUTTING_BACKSLASH = "\\\\";

    /**
     * 匹配 C:\
     */
    private static final String DISC_REG = "(?i)c:\\\\";

    /**
     * 匹配 HKEY_LOCAL_MACHINE
     */
    private static final String REGISTRY_MACHINE_REG = "(?i)HKEY_LOCAL_MACHINE";

    /**
     * 匹配 HKEY_LOCAL_MACHINE\system\CurrentControlSet
     */
    private static final String REGISTRY_MACHINE_CURRENTCONTROLSET_REG = "(?i)HKEY_LOCAL_MACHINE\\\\system\\\\CurrentControlSet";

    /**
     * 匹配 HKEY_CURRENT_USER\Software\Classes
     */
    private static final String REGISTRY_USER_CLASSES_REG = "(?i)HKEY_CURRENT_USER\\\\Software\\\\Classes";

    /**
     * 匹配 HKEY_CURRENT_USER\Software
     */
    private static final String REGISTRY_USER_REG = "(?i)HKEY_CURRENT_USER";

    /**
     * 百分号
     */
    private static final String PERCENT = "%";

    private static final List<String> FILE_SERVER_ONLY_SUPPORT_LIST = Lists.newArrayList( "HKEY_CURRENT_USER", "%USERPROFILE%", "%APPDATA%",
            "%HOMEPATH%", "%LOCALAPPDATA%", "%TEMP%", "%TMP%");

    /**
     * JSON转字符串特征,List字段如果为null,输出为[],而非null 字符类型字段如果为null,输出为”“,而非null 数值字段如果为null,输出为0,而非null
     */
    private static final SerializerFeature[] JSON_FEATURES = new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty,
        WriteNullNumberAsZero};

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private UserProfilePathGroupDAO rcoUserProfilePathGroupDAO;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserProfileServiceTx userProfileServiceTx;

    @Autowired
    private UserProfileMainPathDAO rcoUserProfilePathDAO;

    @Autowired
    private UserProfileStrategyRelatedDAO rcoUserProfileStrategyRelatedDAO;

    @Autowired
    private UserProfileStrategyDAO rcoUserProfileStrategyDAO;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private ViewRcoUserProfileStrategyCountDAO viewRcoUserProfileStrategyCountDAO;

    @Autowired
    private UserProfileStrategyRelatedViewService userProfileStrategyRelatedViewService;

    @Autowired
    private ViewRcoUserProfileStrategyRelatedDAO viewRcoUserProfileStrategyRelatedDAO;

    @Autowired
    private ViewUserProfileChildService viewUserProfileChildService;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

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
    private UserProfileStrategyNotifyAPI userProfileStrategyNotifyAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserProfileChildPathDAO userProfileChildPathDAO;

    @Autowired
    private UserProfilePathDAO pathDAO;

    @Autowired
    private UserProfileFailCleanRequestDAO userProfileFailCleanDAO;

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Override
    public void isImportingUserProfilePath() throws BusinessException {
        if (UserProfileImportStateCache.STATE.isImporting()) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_HAS_IMPORT_PATH);
        }
    }

    @Override
    public void createUserProfilePathGroup(UserProfilePathGroupDTO userProfilePathGroupDTO) throws BusinessException {
        Assert.notNull(userProfilePathGroupDTO, "userProfilePathGroupDTO cannot null");
        LOGGER.info("创建用户配置路径组,入参对象:{}", JSON.toJSONString(userProfilePathGroupDTO));
        if (checkUserProfilePathGroupNameDuplication(null, userProfilePathGroupDTO.getName())) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_GROUP_NAME_DUPLICATION);
        }
        UserProfilePathGroupEntity rcoUserProfilePathGroupEntity = new UserProfilePathGroupEntity();
        BeanUtils.copyProperties(userProfilePathGroupDTO, rcoUserProfilePathGroupEntity);
        rcoUserProfilePathGroupEntity.setCreateTime(new Date());
        rcoUserProfilePathGroupEntity.setUpdateTime(new Date());
        rcoUserProfilePathGroupDAO.save(rcoUserProfilePathGroupEntity);
    }

    @Override
    public void editUserProfilePathGroup(UserProfilePathGroupDTO userProfilePathGroupDTO) throws BusinessException {
        Assert.notNull(userProfilePathGroupDTO, "userProfilePathGroupDTO 不能为空");
        LOGGER.info("编辑用户配置路径组,入参对象:{}", JSON.toJSONString(userProfilePathGroupDTO));
        if (checkUserProfilePathGroupNameDuplication(userProfilePathGroupDTO.getId(), userProfilePathGroupDTO.getName())) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_GROUP_NAME_DUPLICATION);
        }
        LockableExecutor.executeWithTryLock(userProfilePathGroupDTO.getId().toString(), () -> {
            UserProfilePathGroupEntity entity = findUserProfilePathGroupEntity(userProfilePathGroupDTO.getId());
            entity.setName(userProfilePathGroupDTO.getName());
            entity.setDescription(userProfilePathGroupDTO.getDescription());
            entity.setUpdateTime(new Date());
            rcoUserProfilePathGroupDAO.save(entity);
        }, WAIT_TIME);
    }

    @Override
    public void deleteUserProfilePathGroup(UUID id) throws BusinessException {
        Assert.notNull(id, "id 不能为空");
        UserProfilePathGroupEntity entity = findUserProfilePathGroupEntity(id);
        userProfileServiceTx.deleteUserProfilePathGroup(entity);
        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_GROUP_DELETE, entity.getName());
    }

    @Override
    public UserProfilePathGroupDTO findUserProfilePathGroupById(UUID id) throws BusinessException {
        Assert.notNull(id, "id cannot null");
        UserProfilePathGroupEntity entity = findUserProfilePathGroupEntity(id);
        UserProfilePathGroupDTO userProfilePathGroupDTO = new UserProfilePathGroupDTO();
        BeanUtils.copyProperties(entity, userProfilePathGroupDTO);
        return userProfilePathGroupDTO;
    }

    @Override
    public Boolean checkUserProfilePathGroupNameDuplication(@Nullable UUID id, String name) {
        Assert.hasText(name, "name cannot null");
        UserProfilePathGroupEntity softwareGroupEntity = rcoUserProfilePathGroupDAO.findByName(name);
        return (softwareGroupEntity != null && !softwareGroupEntity.getId().equals(id));
    }

    @Override
    public void moveUserProfilePath(List<UUID> idList, UUID targetGroupId) throws BusinessException {
        Assert.notNull(idList, "idList is null");
        Assert.notNull(targetGroupId, "targetGroupId is null");
        userProfileServiceTx.moveUserProfilePath(idList, targetGroupId);
    }

    @Override
    public List<UserProfilePathDTO> findUserProfilePathByIdIn(List<UUID> ids) throws BusinessException {
        Assert.notNull(ids, "ids cannot null");
        List<UserProfilePathMainEntity> userProfilePathEntityList = rcoUserProfilePathDAO.findByIdIn(ids);
        List<UserProfilePathDTO> userProfilePathDTOList = new ArrayList<>(userProfilePathEntityList.size());
        userProfilePathEntityList.forEach((entity) -> {
            UserProfilePathDTO userProfilePathDTO = new UserProfilePathDTO();
            BeanUtils.copyProperties(entity, userProfilePathDTO);
            userProfilePathDTOList.add(userProfilePathDTO);
        });
        return userProfilePathDTOList;
    }

    @Override
    public void createUserProfilePath(UserProfilePathDTO userProfilePathDTO) throws BusinessException {
        Assert.notNull(userProfilePathDTO, "userProfilePathDTO cannot null");
        LOGGER.info("创建用户配置路径,入参对象:{}", JSON.toJSONString(userProfilePathDTO));
        if (checkUserProfilePathNameDuplication(null, userProfilePathDTO.getName())) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_NAME_DUPLICATION, userProfilePathDTO.getName());
        }
        UserProfileHelper.pathValidate(userProfilePathDTO);
        UserProfilePathMainEntity userProfilePathEntity = new UserProfilePathMainEntity();
        BeanUtils.copyProperties(userProfilePathDTO, userProfilePathEntity);
        userProfilePathEntity.setDefault(false);
        userProfilePathEntity.setCreateTime(new Date());
        userProfilePathEntity.setUpdateTime(new Date());
        userProfilePathEntity.setImportUserProfilePathType(ImportUserProfilePathType.NORMAL);
        UserProfilePathMainEntity entity = rcoUserProfilePathDAO.save(userProfilePathEntity);

        createUserProfileChildPath(entity.getId(), userProfilePathDTO.getChildPathArr());
    }

    @Override
    public void importSpecialUserProfilePath(UserProfilePathDTO userProfilePathDTO) throws BusinessException {
        Assert.notNull(userProfilePathDTO, "userProfilePathDTO cannot null");
        LOGGER.debug("导入特殊用户配置路径,入参对象:{}", JSON.toJSONString(userProfilePathDTO));

        checkImportSpecialUserProfileName(userProfilePathDTO.getName());
        UserProfilePathMainEntity userProfilePathEntity = new UserProfilePathMainEntity();
        userProfilePathEntity.setGroupId(userProfilePathDTO.getGroupId());
        userProfilePathEntity.setName(userProfilePathDTO.getName());
        userProfilePathEntity.setDescription(userProfilePathDTO.getDescription());
        userProfilePathEntity.setDefault(false);
        userProfilePathEntity.setCreateTime(new Date());
        userProfilePathEntity.setUpdateTime(new Date());
        userProfilePathEntity.setImportUserProfilePathType(ImportUserProfilePathType.SPECIAL);
        UserProfilePathMainEntity entity = rcoUserProfilePathDAO.save(userProfilePathEntity);

        createUserProfileChildPath(entity.getId(), userProfilePathDTO.getChildPathArr());
    }

    private void checkImportSpecialUserProfileName(String name) throws BusinessException {
        UserProfilePathMainEntity userProfilePathEntity = rcoUserProfilePathDAO.findByName(name);
        if (userProfilePathEntity == null) {
            LOGGER.debug("找不到[{}]对应的个性化配置, 确定无名字冲突", name);
            return;
        }

        if (userProfilePathEntity.getImportUserProfilePathType() == ImportUserProfilePathType.NORMAL) {
            LOGGER.error("个性化配置[{}]与一般配置存在冲突，特殊配置导入失败", name);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_NAME_DUPLICATION, name);
        }

        //特殊配置重名，则会覆盖
        if (userProfilePathEntity.getImportUserProfilePathType() == ImportUserProfilePathType.SPECIAL) {
            LOGGER.debug("个性化配置[{}]与特殊配置存在冲突，将会覆盖同名的特殊配置", name);
            deleteUserProfilePath(userProfilePathEntity.getId());
        }
    }

    @Override
    public void editUserProfilePath(UserProfilePathDTO userProfilePathDTO) throws BusinessException {
        Assert.notNull(userProfilePathDTO, "userProfilePathDTO 不能为空");
        LOGGER.info("编辑用户配置路径,入参对象:{}", JSON.toJSONString(userProfilePathDTO));
        if (checkUserProfilePathNameDuplication(userProfilePathDTO.getId(), userProfilePathDTO.getName())) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_NAME_DUPLICATION, userProfilePathDTO.getName());
        }
        UserProfileHelper.pathValidate(userProfilePathDTO);
        LockableExecutor.executeWithTryLock(userProfilePathDTO.getId().toString(), () -> {
            UserProfilePathMainEntity entity = findUserProfilePathEntity(userProfilePathDTO.getId());
            if (entity.getDefault()) {
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_IS_DEFAULT_NOT_EDIT);
            }
            if (entity.getImportUserProfilePathType() == ImportUserProfilePathType.SPECIAL) {
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_IS_DEFAULT_NOT_EDIT);
            }
            entity.setName(userProfilePathDTO.getName());
            entity.setGroupId(userProfilePathDTO.getGroupId());
            entity.setDescription(userProfilePathDTO.getDescription());
            entity.setUpdateTime(new Date());
            rcoUserProfilePathDAO.save(entity);
            updateUserProfileChildPath(userProfilePathDTO);
        }, WAIT_TIME);
        // 清理旧的关于个性化配置的缓存
        userProfileStrategyNotifyAPI.updatePathNotifyDesk(userProfilePathDTO.getId());
    }

    private void updateUserProfileChildPath(UserProfilePathDTO userProfilePathDTO) {
        UUID userProfilePathId = userProfilePathDTO.getId();
        userProfileChildPathDAO.deleteByUserProfilePathId(userProfilePathId);
        pathDAO.deleteByUserProfilePathId(userProfilePathId);

        createUserProfileChildPath(userProfilePathId, userProfilePathDTO.getChildPathArr());
    }


    private void createUserProfileChildPath(UUID userProfilePathId, UserProfileChildPathDTO[] childPathDTOArr) {
        List<UserProfilePathEntity> pathEntityList = new ArrayList<>();

        for (int i = 0; i < childPathDTOArr.length; i++) {
            UserProfileChildPathDTO childPathDTO = childPathDTOArr[i];
            UserProfileChildPathEntity childPathEntity = new UserProfileChildPathEntity();
            childPathEntity.setUserProfilePathId(userProfilePathId);
            childPathEntity.setMode(childPathDTO.getMode());
            childPathEntity.setType(childPathDTO.getType());
            childPathEntity.setIndex(i);
            UserProfileChildPathEntity childEntity = userProfileChildPathDAO.save(childPathEntity);
            String[] pathArr = childPathDTO.getPathArr();
            boolean isRoute = childPathDTO.isRoute();
            for (int j = 0; j < pathArr.length; j++) {
                UserProfilePathEntity pathEntity = new UserProfilePathEntity();
                pathEntity.setPath(trimPath(pathArr[j], isRoute));
                pathEntity.setIndex(j);
                pathEntity.setUserProfilePathId(userProfilePathId);
                pathEntity.setUserProfileChildPathId(childEntity.getId());
                pathEntityList.add(pathEntity);
            }
        }

        pathDAO.saveAll(pathEntityList);
    }

    private String trimPath(String path, boolean isRoute) {
        //文件和文件夹的路径，要能够自动去除目录前后出现的空格；
        if (isRoute) {
            String[] pathArr = path.split(CUTTING_BACKSLASH);
            StringBuilder trimString = new StringBuilder();
            for (int i = 0; i < pathArr.length; i++) {
                trimString.append(pathArr[i].trim());
                if (i < pathArr.length - AHEAD) {
                    trimString.append(BACKSLASH);
                }
            }

            return trimString.toString();
        }

        return path;
    }

    @Override
    public UserProfilePathDTO findUserProfilePathById(UUID id) throws BusinessException {
        Assert.notNull(id, "id cannot null");
        UserProfilePathMainEntity userProfilePathEntity = findUserProfilePathEntity(id);
        UserProfilePathDTO userProfilePathDTO = new UserProfilePathDTO();
        BeanUtils.copyProperties(userProfilePathEntity, userProfilePathDTO);
        UserProfilePathGroupEntity groupEntity = findUserProfilePathGroupEntity(userProfilePathDTO.getGroupId());
        userProfilePathDTO.setGroupName(groupEntity.getName());
        userProfilePathDTO.setChildPathArr(findUserProfileChildPathById(id));
        return userProfilePathDTO;
    }

    private UserProfileChildPathDTO[] findUserProfileChildPathById(UUID userProfilePathId) {
        List<UserProfileChildPathEntity> childPathEntityList = userProfileChildPathDAO.findByUserProfilePathIdOrderByIndex(userProfilePathId);
        List<UserProfilePathEntity> pathEntityList = pathDAO.findByUserProfilePathId(userProfilePathId);

        UserProfileChildPathDTO[] childPathDTOArr = new UserProfileChildPathDTO[childPathEntityList.size()];
        for (int i = 0; i < childPathEntityList.size(); i++) {
            UserProfileChildPathEntity childPathEntity = childPathEntityList.get(i);
            UserProfileChildPathDTO childPathDTO = new UserProfileChildPathDTO();
            childPathDTO.setMode(childPathEntity.getMode());
            childPathDTO.setType(childPathEntity.getType());
            childPathDTO.setId(childPathEntity.getId());
            List<UserProfilePathEntity> pathEntityFilterList = new ArrayList<>();
            for (UserProfilePathEntity pathEntity : pathEntityList) {
                if (childPathEntity.getId().equals(pathEntity.getUserProfileChildPathId())) {
                    pathEntityFilterList.add(pathEntity);
                }
            }
            childPathDTO.setPathArr(changeToPathArr(pathEntityFilterList));

            childPathDTOArr[i] = childPathDTO;
        }

        return childPathDTOArr;
    }

    private String[] changeToPathArr(List<UserProfilePathEntity> pathEntityFilterList) {
        UserProfilePathEntity[] pathEntityArr = pathEntityFilterList.toArray(new UserProfilePathEntity[pathEntityFilterList.size()]);

        //冒泡排序
        for (int j = 0; j < pathEntityArr.length - 1; j++) {
            for (int k = 0; k < pathEntityArr.length - 1 - j; k++) {
                UserProfilePathEntity ahead = pathEntityArr[k];
                UserProfilePathEntity next = pathEntityArr[k + 1];
                if (ahead.getIndex() > next.getIndex()) {
                    pathEntityArr[k] = next;
                    pathEntityArr[k + 1] = ahead;
                }
            }
        }

        String[] pathArr = new String[pathEntityArr.length];
        for (int i = 0; i < pathArr.length; i++) {
            pathArr[i] = pathEntityArr[i].getPath();
        }

        return pathArr;
    }

    @Override
    public DefaultPageResponse<UserProfileChildPathInfoDTO> pageQuery(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        Page<ViewUserProfileChildPathEntity> page = viewUserProfileChildService.pageQuery(request);
        DefaultPageResponse<UserProfileChildPathInfoDTO> response = convertPageInfo(page);

        return response;
    }

    private DefaultPageResponse<UserProfileChildPathInfoDTO> convertPageInfo(Page<ViewUserProfileChildPathEntity> page) {
        Assert.notNull(page, "Param [page] must not be null");

        if (page.getTotalElements() == 0) {
            DefaultPageResponse<UserProfileChildPathInfoDTO> response = new DefaultPageResponse<>();
            response.setTotal(0);
            response.setItemArr(new UserProfileChildPathInfoDTO[0]);
            return response;
        }
        DefaultPageResponse<UserProfileChildPathInfoDTO> resp = new DefaultPageResponse<>();
        List<ViewUserProfileChildPathEntity> viewList = page.getContent();
        List<UserProfileChildPathInfoDTO> cloudDesktopList = convertShowUserProfilePath(viewList);
        UserProfileChildPathInfoDTO[] desktopArr = cloudDesktopList.toArray(new UserProfileChildPathInfoDTO[cloudDesktopList.size()]);
        resp.setItemArr(desktopArr);
        resp.setTotal(page.getTotalElements());
        return resp;
    }

    private List<UserProfileChildPathInfoDTO> convertShowUserProfilePath(List<ViewUserProfileChildPathEntity> viewList) {
        List<UserProfileChildPathInfoDTO> showUserProfilePathDTOList = new ArrayList<>();
        for (ViewUserProfileChildPathEntity entity : viewList) {
            UserProfileChildPathInfoDTO dto = new UserProfileChildPathInfoDTO();
            BeanUtils.copyProperties(entity, dto);
            showUserProfilePathDTOList.add(dto);
        }

        return showUserProfilePathDTOList;
    }

    @Override
    public void deleteUserProfilePath(UUID id) throws BusinessException {
        Assert.notNull(id, "id 不能为空");
        UserProfilePathMainEntity entity = findUserProfilePathEntity(id);
        if (entity.getDefault()) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_IS_DEFAULT_NOT_EDIT);
        }
        if (entity.getImportUserProfilePathType() == ImportUserProfilePathType.SPECIAL) {
            LOGGER.error("个性化配置[{}]是特殊配置，不能编辑与删除", entity.getName());
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_IS_SPECIAL_NOT_EDIT, entity.getName());
        }
        List<UserProfileStrategyRelatedEntity> relatedList = rcoUserProfileStrategyRelatedDAO.findByRelatedIdAndRelatedType(id,
                UserProfileRelatedTypeEnum.PATH);
        userProfileServiceTx.deleteUserProfilePath(id);

        // 通知所有云桌面
        List<UUID> strategyIdList = relatedList.stream().map(UserProfileStrategyRelatedEntity::getStrategyId).collect(Collectors.toList());
        if (!strategyIdList.isEmpty()) {
            UserProfileStrategyCacheManager.deleteCaches(strategyIdList);
        }
    }

    @Override
    public Boolean checkUserProfilePathNameDuplication(@Nullable UUID id, String name) {
        Assert.hasText(name, "name cannot null");
        UserProfilePathMainEntity userProfilePathEntity = rcoUserProfilePathDAO.findByName(name);
        return (userProfilePathEntity != null && !userProfilePathEntity.getId().equals(id));
    }

    @Override
    public void createUserProfileStrategy(UserProfileStrategyDTO userProfileStrategyDTO) throws BusinessException {
        Assert.notNull(userProfileStrategyDTO, "userProfileStrategyDTO cannot null");
        LOGGER.info("创建用户配置策略,入参对象:{}", JSON.toJSONString(userProfileStrategyDTO));
        if (checkUserProfileStrategyNameDuplication(null, userProfileStrategyDTO.getName())) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_NAME_DUPLICATION);
        }
        checkRelatedPathSize(userProfileStrategyDTO);
        userProfileServiceTx.createUserProfileStrategy(userProfileStrategyDTO);
    }

    @Override
    public void editUserProfileStrategy(UserProfileStrategyDTO userProfileStrategyDTO) throws BusinessException {
        Assert.notNull(userProfileStrategyDTO, "userProfileStrategyDTO 不能为空");
        LOGGER.info("编辑用户配置策略,入参对象:{}", JSON.toJSONString(userProfileStrategyDTO));
        if (checkUserProfileStrategyNameDuplication(userProfileStrategyDTO.getId(), userProfileStrategyDTO.getName())) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_NAME_DUPLICATION);
        }
        checkRelatedPathSize(userProfileStrategyDTO);
        userProfileServiceTx.editUserProfileStrategy(userProfileStrategyDTO);
        // 通知所有云桌面
        userProfileStrategyNotifyAPI.updateStrategyNotifyDesk(userProfileStrategyDTO.getId());
    }

    @Override
    public void deleteUserProfileStrategy(UUID strategyId) throws BusinessException {
        Assert.notNull(strategyId, "strategyId 不能为空");
        List<RcoDeskInfoEntity> rcoDeskInfoEntityList = rcoDeskInfoDAO.findByUserProfileStrategyId(strategyId);
        if (!rcoDeskInfoEntityList.isEmpty()) {
            LOGGER.error("用户配置策略被云桌面引用不允许删除, 用户配置策略id[{}]", strategyId);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_USED);
        }
        UserProfileStrategyEntity entity = findUserProfileStrategyEntity(strategyId);
        String strategyName = entity.getName();
        List<IdvTerminalGroupDeskConfigEntity> idvTerminalGroupDeskConfigEntityList =
                idvTerminalGroupDeskConfigDAO.findByUserProfileStrategyId(strategyId);
        if (!CollectionUtils.isEmpty(idvTerminalGroupDeskConfigEntityList)) {
            handleExistsTerminalGroup(idvTerminalGroupDeskConfigEntityList, strategyName);
        }

        List<UserGroupDesktopConfigEntity> configEntityList = userGroupDesktopConfigDAO.findByUserProfileStrategyId(strategyId);
        if (!CollectionUtils.isEmpty(configEntityList)) {
            handleExistsUserGroup(configEntityList, strategyName);
        }

        List<UserDesktopConfigEntity> userDesktopConfigEntityList = userDesktopConfigDAO.findByUserProfileStrategyId(strategyId);
        if (!CollectionUtils.isEmpty(userDesktopConfigEntityList)) {
            handleExistsUser(userDesktopConfigEntityList, strategyName);
        }

        List<DesktopPoolBasicDTO> desktopPoolList = desktopPoolService.listByUserProfileStrategyId(strategyId);
        if (!CollectionUtils.isEmpty(desktopPoolList)) {
            handleExistsDesktopPool(desktopPoolList, strategyName);
        }

        userProfileServiceTx.deleteUserProfileStrategy(entity);
        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_LOG, entity.getName());
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
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_RELATIVE_MANY_IDV_GROUP, strategyName,
                        sb.toString());
            }

            if (i != 0) {
                sb.append(NAME_SEPARATOR);
            }
            sb.append(terminalGroupDTO.getGroupName());
        }
        throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_RELATIVE_IDV_GROUP, strategyName, sb.toString());
    }

    protected void handleExistsUserGroup(List<UserGroupDesktopConfigEntity> configEntityList, String strategyName) throws BusinessException {
        List<UUID> userGroupIdList = configEntityList.stream().map(UserGroupDesktopConfigEntity::getGroupId).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        int size = userGroupIdList.size();

        for (int i = 0; i < size; i++) {
            IacUserGroupDetailDTO groupDetail = cbbUserGroupAPI.getUserGroupDetail(userGroupIdList.get(i));
            if (groupDetail.getName().length() + sb.length() > MAX_NAME_LENGTH) {
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_RELATIVE_MANY_GROUP, strategyName,
                        sb.toString());
            }

            if (i != 0) {
                sb.append(NAME_SEPARATOR);
            }
            sb.append(groupDetail.getName());
        }
        throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_RELATIVE_GROUP, strategyName, sb.toString());
    }

    protected void handleExistsUser(List<UserDesktopConfigEntity> userDesktopConfigEntityList, String strategyName) throws BusinessException {
        List<UUID> userIdList = userDesktopConfigEntityList.stream().map(UserDesktopConfigEntity::getUserId).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        int size = userIdList.size();

        for (int i = 0; i < size; i++) {
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userIdList.get(i));
            if (userDetail.getUserName().length() + sb.length() > MAX_NAME_LENGTH) {
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_RELATIVE_MANY_USER, strategyName,
                        sb.toString());
            }

            if (i != 0) {
                sb.append(NAME_SEPARATOR);
            }
            sb.append(userDetail.getUserName());
        }
        throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_RELATIVE_USER, strategyName, sb.toString());
    }

    private void handleExistsDesktopPool(List<DesktopPoolBasicDTO> desktopPoolList, String strategyName) throws BusinessException {
        StringBuilder sb = new StringBuilder();
        int size = desktopPoolList.size();

        for (int i = 0; i < size; i++) {
            DesktopPoolBasicDTO dto = desktopPoolList.get(i);
            if (dto.getName().length() + sb.length() > MAX_NAME_LENGTH) {
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_RELATIVE_DESKTOP_POOL, strategyName,
                        sb.toString());
            }

            if (i != 0) {
                sb.append(NAME_SEPARATOR);
            }
            sb.append(dto.getName());
        }
        throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_RELATIVE_DESKTOP_POOL, strategyName, sb.toString());
    }

    @Override
    public UserProfileStrategyDTO findUserProfileStrategyWrapperById(UUID id) throws BusinessException {
        Assert.notNull(id, "id cannot null");
        UserProfileStrategyEntity userProfileStrategyEntity = findUserProfileStrategyEntity(id);
        UserProfileStrategyDTO userProfileStrategyDTO = new UserProfileStrategyDTO();
        BeanUtils.copyProperties(userProfileStrategyEntity, userProfileStrategyDTO);
        ViewRcoUserProfileStrategyCountEntity userProfileStrategyCountEntity = findUserProfileStrategyCountEntity(id);
        userProfileStrategyDTO.setCount(userProfileStrategyCountEntity.getCount() == null ? 0L : userProfileStrategyCountEntity.getCount());
        userProfileStrategyDTO.setDeskCount(userProfileStrategyCountEntity.getDeskCount() == null ? 0L :
                userProfileStrategyCountEntity.getDeskCount());

        // 获取策略关联的分组或路径
        List<UserProfileStrategyRelatedEntity> strategyRelatedEntityList = rcoUserProfileStrategyRelatedDAO.findByStrategyId(id);
        List<UUID> softwareIdList = new ArrayList<>();
        List<UUID> groupIdList = new ArrayList<>();
        strategyRelatedEntityList.stream().forEach(strategyRelatedEntity -> {
            if (UserProfileRelatedTypeEnum.GROUP == strategyRelatedEntity.getRelatedType()) {
                groupIdList.add(strategyRelatedEntity.getRelatedId());
            } else {
                softwareIdList.add(strategyRelatedEntity.getRelatedId());
            }
        });

        List<UserProfilePathGroupEntity> groupEntityList = rcoUserProfilePathGroupDAO.findAllById(groupIdList);
        List<UserProfilePathMainEntity> pathEntityList = rcoUserProfilePathDAO.findAllById(softwareIdList);
        List<UserProfileStrategyRelatedDTO> strategyDetailDTOList = new ArrayList<>();
        for (UserProfilePathGroupEntity groupEntity : groupEntityList) {
            UserProfileStrategyRelatedDTO strategyDetailDTO = new UserProfileStrategyRelatedDTO();
            strategyDetailDTO.setId(groupEntity.getId());
            strategyDetailDTO.setLabel(groupEntity.getName());
            strategyDetailDTOList.add(strategyDetailDTO);
        }
        for (UserProfilePathMainEntity pathEntity : pathEntityList) {
            UserProfileStrategyRelatedDTO strategyDetailDTO = new UserProfileStrategyRelatedDTO();
            strategyDetailDTO.setId(pathEntity.getId());
            strategyDetailDTO.setLabel(pathEntity.getName());
            strategyDetailDTO.setGroupId(pathEntity.getGroupId());
            strategyDetailDTOList.add(strategyDetailDTO);
        }

        userProfileStrategyDTO.setPathArr(strategyDetailDTOList.toArray(new UserProfileStrategyRelatedDTO[0]));
        return userProfileStrategyDTO;
    }

    @Override
    public Boolean checkUserProfileStrategyNameDuplication(@Nullable UUID id, String name) {
        Assert.hasText(name, "name cannot null");
        UserProfileStrategyEntity userProfileStrategyEntity = rcoUserProfileStrategyDAO.findByName(name);
        return (userProfileStrategyEntity != null && !userProfileStrategyEntity.getId().equals(id));
    }

    @Override
    public List<UserProfilePathDTO> findAllUserProfilePathForWeb() throws BusinessException {
        List<UserProfilePathMainEntity> pathEntityList = rcoUserProfilePathDAO.findAll();
        List<UserProfilePathDTO> pathDTOList = new ArrayList<>(pathEntityList.size());
        for (UserProfilePathMainEntity pathEntity : pathEntityList) {
            UserProfilePathDTO pathDTO = new UserProfilePathDTO();
            BeanUtils.copyProperties(pathEntity, pathDTO);
            pathDTOList.add(pathDTO);
        }
        return pathDTOList;
    }

    @Override
    public List<UserProfilePathGroupDTO> findAllUserProfilePathGroup() throws BusinessException {
        List<UserProfilePathGroupEntity> groupEntityList = rcoUserProfilePathGroupDAO.findAll();
        List<UserProfilePathGroupDTO> groupDTOList = new ArrayList<>(groupEntityList.size());
        for (UserProfilePathGroupEntity groupEntity : groupEntityList) {
            UserProfilePathGroupDTO groupDTO = new UserProfilePathGroupDTO();
            BeanUtils.copyProperties(groupEntity, groupDTO);
            groupDTOList.add(groupDTO);
        }
        return groupDTOList;
    }

    @Override
    public List<UserProfileTreeDTO> listUserProfileTree(@Nullable UserProfileStrategyStorageTypeEnum storageType) {
        List<UserProfilePathMainEntity> pathEntityList = rcoUserProfilePathDAO.findAll();
        List<UserProfilePathGroupEntity> groupEntityList = rcoUserProfilePathGroupDAO.findAll();
        Set<UUID> notSupportIdSet = null;
        if (storageType == UserProfileStrategyStorageTypeEnum.FILE_SERVER) {
            List<UserProfilePathEntity> allPathEntityList = pathDAO.findAll();
            if (!CollectionUtils.isEmpty(allPathEntityList)) {
                notSupportIdSet = allPathEntityList.stream().filter(entity -> StringUtils.isNotBlank(entity.getPath()))
                        .filter(entity -> FILE_SERVER_ONLY_SUPPORT_LIST.stream().noneMatch(item -> entity.getPath().toUpperCase().startsWith(item)))
                        .map(UserProfilePathEntity::getUserProfilePathId).collect(Collectors.toSet());
            }
        }
        List<UserProfileTreeDTO> treeList = new ArrayList<>();
        for (UserProfilePathMainEntity profilePathMainEntity : pathEntityList) {
            UserProfileTreeDTO treeDTO = new UserProfileTreeDTO();
            treeDTO.setId(profilePathMainEntity.getId().toString());
            treeDTO.setLabel(profilePathMainEntity.getName());
            treeDTO.setParentId(profilePathMainEntity.getGroupId().toString());
            if (storageType == UserProfileStrategyStorageTypeEnum.FILE_SERVER) {
                checkCanUsed(treeDTO, profilePathMainEntity, notSupportIdSet);
            }
            treeList.add(treeDTO);
        }
        for (UserProfilePathGroupEntity groupEntity : groupEntityList) {
            UserProfileTreeDTO treeDTO = new UserProfileTreeDTO();
            treeDTO.setId(groupEntity.getId().toString());
            treeDTO.setLabel(groupEntity.getName());
            treeList.add(treeDTO);
        }
        treeList = new TreeBuilder<>(treeList).build();
        return treeList;
    }

    private void checkCanUsed(UserProfileTreeDTO treeDTO, UserProfilePathMainEntity profilePathMainEntity, Set<UUID> notSupportIdSet) {
        if (CollectionUtils.isEmpty(notSupportIdSet)) {
            return;
        }
        if (notSupportIdSet.contains(profilePathMainEntity.getId())) {
            treeDTO.setCanUsed(false);
            treeDTO.setCanUsedMessage(LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_ONLY_SUPPORT_FILE_SERVER));
        }
    }

    @Override
    public DefaultPageResponse<UserProfileStrategyRelatedDetailDTO> userProfileStrategyRelatedPageQuery(PageSearchRequest request)
            throws BusinessException {
        Assert.notNull(request, "Param [request] must not be null");
        Page<ViewRcoUserProfileStrategyRelatedEntity> page = userProfileStrategyRelatedViewService.pageQuery(request);
        DefaultPageResponse<UserProfileStrategyRelatedDetailDTO> resp = new DefaultPageResponse<>();
        resp.setTotal(page.getTotalElements());
        List<UserProfileStrategyRelatedDetailDTO> relatedDetailDTOList = new ArrayList<>();
        page.get().forEach(entity -> {
            UserProfileStrategyRelatedDetailDTO dto = new UserProfileStrategyRelatedDetailDTO();
            BeanUtils.copyProperties(entity, dto);
            dto.setGroupName(getUserProfileGroupName(entity.getGroupId()));
            relatedDetailDTOList.add(dto);
        });
        resp.setItemArr(relatedDetailDTOList.toArray(new UserProfileStrategyRelatedDetailDTO[0]));
        return resp;
    }

    private String getUserProfileGroupName(UUID userProfilePathGroupId) {
        UserProfilePathGroupEntity groupEntity = rcoUserProfilePathGroupDAO.getOne(userProfilePathGroupId);

        return groupEntity == null ? StringUtils.EMPTY : groupEntity.getName();
    }

    @Override
    public void deletePathFromStrategyRelated(UUID strategyId, UUID id) throws BusinessException {
        Assert.notNull(strategyId, "strategyId 不能为空");
        Assert.notNull(id, "id 不能为空");
        UserProfileStrategyEntity userProfileStrategyEntity = findUserProfileStrategyEntity(strategyId);
        UserProfilePathMainEntity userProfilePathEntity = findUserProfilePathEntity(id);
        rcoUserProfileStrategyRelatedDAO.deleteByStrategyIdAndRelatedId(strategyId, id);
        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_REMOVE,
                userProfilePathEntity.getName(), userProfileStrategyEntity.getName());
        // 通知所有云桌面
        userProfileStrategyNotifyAPI.updateStrategyNotifyDesk(strategyId);
    }

    @Override
    public UserProfileStrategyDTO findUserProfileStrategyById(UUID userProfileStrategyId) throws BusinessException {
        Assert.notNull(userProfileStrategyId, "userProfileStrategyId cannot null");
        UserProfileStrategyEntity userProfileStrategyEntity = findUserProfileStrategyEntity(userProfileStrategyId);
        UserProfileStrategyDTO userProfileStrategyDTO = new UserProfileStrategyDTO();
        BeanUtils.copyProperties(userProfileStrategyEntity, userProfileStrategyDTO);
        return userProfileStrategyDTO;
    }

    @Override
    public PathTree getEffectiveUserProfilePathTree(UUID userProfileStrategyId) throws BusinessException {
        Assert.notNull(userProfileStrategyId, "userProfileStrategyId cannot null");
        List<UserProfilePathDetailDTO> effectivePathList = getEffectiveUserProfilePath(userProfileStrategyId);
        PathTree forest = new PathTree();
        PathTree current = forest;
        // 1.遍历路径对象
        for (UserProfilePathDetailDTO pathDTO : effectivePathList) {
            // 2.遍历路径分隔后的目录
            String[] pathArr = pathDTO.getPath().split("\\\\");
            for (String path : pathArr) {
                // 返回的current为每个子目录的对象
                current = current.child(path);
            }
            // 路径的最后一个节点赋值路径对象,非空判断防止覆盖父目录的对象
            if (current.getData() == null) {
                current.setData(pathDTO);
            }
            // 一个路径处理后,重新将当前目录指定为初始目录对象
            current = forest;
        }
        return forest;
    }

    @Override
    public List<UserProfilePathDetailDTO> getEffectiveUserProfilePathByStrategyId(UUID userProfileStrategyId) {
        Assert.notNull(userProfileStrategyId, "userProfileStrategyId cannot null");

        List<ViewRcoUserProfileStrategyRelatedEntity> strategyRelatedEntityList =
                viewRcoUserProfileStrategyRelatedDAO.findByStrategyId(userProfileStrategyId);
        List<UserProfilePathDetailDTO> userProfilePathDTOList = new ArrayList<>();
        strategyRelatedEntityList.forEach(entity -> {
            userProfilePathDTOList.addAll(getEffectiveUserProfilePathByPathId(entity.getId()));
        });

        // 去重
        return userProfilePathDTOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(
                Comparator.comparing(o -> o.getMode().name() + o.getType().name() + o.getPath()))), ArrayList::new));
    }

    @Override
    public List<UserProfilePathDetailDTO> getEffectiveUserProfilePathByPathId(UUID userProfilePathId) {
        Assert.notNull(userProfilePathId, "userProfilePathId cannot null");

        List<UserProfilePathDetailDTO> pathDetailDTOList = getUserProfilePathDetailByPathId(userProfilePathId);

        if (CollectionUtils.isEmpty(pathDetailDTOList)) {
            return pathDetailDTOList;
        }

        // 去重
        return pathDetailDTOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(
                Comparator.comparing(o -> o.getMode().name() + o.getType().name() + o.getPath()))), ArrayList::new));
    }

    @Override
    public List<UserProfilePathDetailDTO> getEffectiveUserProfilePathByChildPathId(UUID userProfileChildPathId) {
        Assert.notNull(userProfileChildPathId, "userProfileChildPathId cannot null");
        return getUserProfilePathDetailByChildPathId(userProfileChildPathId);
    }

    private List<UserProfilePathDetailDTO> getUserProfilePathDetailByChildPathId(UUID userProfileChildPathId) {
        UserProfilePathEntity path = pathDAO.getOne(userProfileChildPathId);
        UserProfilePathMainEntity userProfilePath = rcoUserProfilePathDAO.getOne(path.getUserProfilePathId());
        UserProfileChildPathEntity childPathEntity = userProfileChildPathDAO.getOne(path.getUserProfileChildPathId());

        List<UserProfilePathDetailDTO> pathDetailDTOList = new ArrayList<>();
        UserProfilePathDetailDTO detailDTO = new UserProfilePathDetailDTO();
        BeanUtils.copyProperties(userProfilePath, detailDTO);
        detailDTO.setPath(path.getPath());
        detailDTO.setMode(childPathEntity.getMode());
        detailDTO.setType(childPathEntity.getType());
        pathDetailDTOList.add(detailDTO);

        return pathDetailDTOList;
    }

    @Override
    public List<UserProfilePathDetailDTO> getEffectiveUserProfilePath(UUID userProfileStrategyId) throws BusinessException {
        Assert.notNull(userProfileStrategyId, "userProfileStrategyId cannot null");
        List<ViewRcoUserProfileStrategyRelatedEntity> strategyRelatedEntityList =
                viewRcoUserProfileStrategyRelatedDAO.findByStrategyId(userProfileStrategyId);
        List<UserProfilePathDetailDTO> userProfilePathDTOList = new ArrayList<>();
        strategyRelatedEntityList.forEach(entity -> {
            userProfilePathDTOList.addAll(getUserProfilePathDetail(entity));
        });

        return getEffectiveUserProfilePath(userProfilePathDTOList);
    }


    private List<UserProfilePathDetailDTO> getUserProfilePathDetailByPathId(UUID userProfilePathId) {
        UserProfilePathMainEntity userProfilePath = rcoUserProfilePathDAO.getOne(userProfilePathId);

        List<UserProfileChildPathEntity> childPathEntityList = userProfileChildPathDAO.findByUserProfilePathId(userProfilePathId);
        Map<UUID, UserProfileChildPathEntity> childPathMap = new HashMap<>();
        for (UserProfileChildPathEntity childPath : childPathEntityList) {
            childPathMap.put(childPath.getId(), childPath);
        }

        List<UserProfilePathDetailDTO> pathDetailDTOList = new ArrayList<>();

        List<UserProfilePathEntity> pathEntityList = pathDAO.findByUserProfilePathId(userProfilePathId);
        for (UserProfilePathEntity path : pathEntityList) {
            UserProfilePathDetailDTO detailDTO = new UserProfilePathDetailDTO();
            BeanUtils.copyProperties(userProfilePath, detailDTO);
            detailDTO.setPath(path.getPath());
            UserProfileChildPathEntity childPath = childPathMap.get(path.getUserProfileChildPathId());
            if (childPath != null) {
                detailDTO.setMode(childPath.getMode());
                detailDTO.setType(childPath.getType());
            }

            pathDetailDTOList.add(detailDTO);
        }

        return pathDetailDTOList;
    }

    private List<UserProfilePathDetailDTO> getEffectiveUserProfilePath(List<UserProfilePathDetailDTO> userProfilePathDTOList) {
        if (CollectionUtils.isEmpty(userProfilePathDTOList)) {
            return userProfilePathDTOList;
        }

        // 去重
        userProfilePathDTOList = userProfilePathDTOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(
                Comparator.comparing(o -> o.getMode().name() + o.getType().name() + o.getPathLowerCase()))), ArrayList::new));
        // 排序规则:mode(同步在前面)然后小写的path(父级在前面)
        userProfilePathDTOList = userProfilePathDTOList.stream().sorted(Comparator.comparing(UserProfilePathDetailDTO::getModeName,
                Comparator.reverseOrder()).thenComparing(UserProfilePathDetailDTO::getPathLowerCase)).collect(toList());

        // 存在同步的环境变量
        boolean isSynchroVariable = userProfilePathDTOList.stream().anyMatch(item -> UserProfilePathModeEnum.SYNCHRO == item.getMode()
                && item.getPathLowerCase().startsWith(PERCENT));

        List<UserProfilePathDetailDTO> effectivePathList = new ArrayList<>();
        a:
        for (UserProfilePathDetailDTO pathDTO : userProfilePathDTOList) {
            String pathLowerCase = pathDTO.getPathLowerCase() + BACKSLASH;
            pathDTO.setPathLowerCase(pathLowerCase);
            if (effectivePathList.isEmpty() && UserProfilePathModeEnum.SYNCHRO == pathDTO.getMode()) {
                LOGGER.info("首次遍历,生效路径为空,添加第一条同步路径:{}", pathLowerCase);
                effectivePathList.add(pathDTO);
                continue;
            }

            // 是否是新的父路径
            boolean isNewParent = true;
            // 生效路径循环
            for (int i = effectivePathList.size() - 1; i >= 0; i--) {
                UserProfilePathDetailDTO effectivePathDTO = effectivePathList.get(i);
                String effectivePathLowerCase = effectivePathDTO.getPathLowerCase();
                // 判断第二个路径是否是生效路径的平级还是子级,需区分类型
                if (pathLowerCase.startsWith(effectivePathLowerCase)) {
                    isNewParent = false;
                    //1.相同路径
                    if (pathLowerCase.length() == effectivePathLowerCase.length()) {
                        if (pathDTO.getType() == effectivePathDTO.getType()) {
                            // 1.1类型一样,配置方式不一样,说明肯定有排除,两个都要删除
                            // 因为同步的路径在前面已经处理了,这里不会再出现子级同步的情况,只有子级排除,所以这里的删除不会对后来数据判断造成影响
                            if (pathDTO.getMode() != effectivePathDTO.getMode()) {
                                effectivePathList.remove(i);
                                LOGGER.info("类型相同,路径相同,配置方式不一样,两个都要删除 路径:{},生效路径:{}", pathLowerCase, effectivePathLowerCase);
                            }
                            // 类型一样,配置方式一样,说明是完全相同的路径,直接舍弃并退出内循环,避免碰到爷级
                            continue a;
                        } else {
                            // 1.2类型不一样,配置方式一样,优先保留目录
                            if (pathDTO.getMode() == effectivePathDTO.getMode()) {
                                if (UserProfilePathTypeEnum.FOLDER == pathDTO.getType() ||
                                        UserProfilePathTypeEnum.REGISTRY_KEY == pathDTO.getType()) {
                                    LOGGER.info("类型不一样,配置方式一样,优先使用目录 添加路径:{},删除路径ID:{}", pathLowerCase, effectivePathDTO.getId());
                                    effectivePathList.remove(i);
                                    effectivePathList.add(pathDTO);
                                }
                                // 1.3类型不一样,配置方式一样,不是目录,直接舍弃并退出内循环 (如果有进入上一步的添加删除操作,这里也要退出内循环,避免碰到父级后又重复添加到生效路径中)
                                continue a;
                            }
                            //1.4两个都不一样会继续内循环,寻找更上一级
                        }
                        // 2.存在上下级的路径
                    } else {
                        if (UserProfilePathTypeEnum.DOCUMENT == effectivePathDTO.getType() ||
                                UserProfilePathTypeEnum.REGISTRY_VALUE == effectivePathDTO.getType()) {
                            // 2.1父级是文件/注册表值,本级不受限制
                            isNewParent = true;
                        } else {
                            // 2.2父级是文件夹/注册表,本级判断是否保留排除
                            // 这里父级处理后需要直接退出内循环,避免碰到同步的爷级
                            if (UserProfilePathModeEnum.EXCLUDE == effectivePathDTO.getMode()) {
                                LOGGER.info("父级是排除,子级也是排除,直接舍弃并退出内循环 路径:{}", pathLowerCase);
                            } else if (UserProfilePathModeEnum.EXCLUDE == pathDTO.getMode()) {
                                effectivePathList.add(pathDTO);
                                LOGGER.info("父级是同步,子级是排除才添加 类型:{} 路径:{}", pathDTO.getType().getText(), pathLowerCase);
                            }
                            continue a;
                        }
                    }
                    // 文件夹/注册表项时,后面的排除路径是父级时,需要删除前面所有同步子级
                } else if ((UserProfilePathTypeEnum.FOLDER == pathDTO.getType() || UserProfilePathTypeEnum.REGISTRY_KEY == pathDTO.getType())
                        && effectivePathLowerCase.startsWith(pathLowerCase)) {
                    effectivePathList.remove(i);
                    LOGGER.info("文件夹/注册表项,后面的排除路径是父级时,需要删除前面所有生效的同步子级路径 路径:{} 删除路径:{}", pathLowerCase, effectivePathLowerCase);
                }
            }
            // 新的父级路径
            if (isNewParent) {
                switch (pathDTO.getType()) {
                    case REGISTRY_KEY:
                    case REGISTRY_VALUE:
                        if (UserProfilePathModeEnum.SYNCHRO == pathDTO.getMode()) {
                            LOGGER.info("新的父级是同步才添加 类型:{} 路径:{}", pathDTO.getType().getText(), pathLowerCase);
                            effectivePathList.add(pathDTO);
                        }
                        break;
                    case DOCUMENT:
                    case FOLDER:
                        // 存在同步的环境变量,新的父级都要保留
                        if (isSynchroVariable) {
                            LOGGER.info("存在同步的环境变量,新的父级都要保留 类型:{} 路径:{}", pathDTO.getType().getText(), pathLowerCase);
                            effectivePathList.add(pathDTO);
                        } else {
                            if (UserProfilePathModeEnum.SYNCHRO == pathDTO.getMode()) {
                                LOGGER.info("不存在同步的环境变量,新的父级是同步才添加 类型:{} 路径:{}", pathDTO.getType().getText(), pathLowerCase);
                                effectivePathList.add(pathDTO);
                                // 不存在同步的环境变量,新的父级是排除的环境变量也要添加
                            } else if (pathLowerCase.startsWith(PERCENT)) {
                                LOGGER.info("不存在同步的环境变量,新的父级是排除的环境变量也要添加 类型:{} 路径:{}", pathDTO.getType().getText(), pathLowerCase);
                                effectivePathList.add(pathDTO);
                            }
                        }
                        break;
                    default:
                        LOGGER.warn("未知的路径类型:{} 路径:{} ID:{}", pathDTO.getType().getText(), pathLowerCase, pathDTO.getId());
                        break;
                }
            }
        }

        // 不存在同步的环境变量时,同时也不存在同步的(文件夹/文件),需要删除环境变量
        if (!isSynchroVariable && !CollectionUtils.isEmpty(effectivePathList)) {
            boolean isExistSynchroFolder = effectivePathList.stream().anyMatch(item -> UserProfilePathModeEnum.SYNCHRO == item.getMode()
                    && (UserProfilePathTypeEnum.FOLDER == item.getType() || UserProfilePathTypeEnum.DOCUMENT == item.getType()));
            if (!isExistSynchroFolder) {
                for (int i = effectivePathList.size() - 1; i >= 0; i--) {
                    UserProfilePathDetailDTO pathDTO = effectivePathList.get(i);
                    // 此处只会存在排除的环境变量
                    if (pathDTO.getPathLowerCase().startsWith(PERCENT)) {
                        effectivePathList.remove(i);
                        LOGGER.info("不存在同步的环境变量,同时也不存在同步的(文件夹/文件),需要删除环境变量 类型:{} 配置方式:{} 路径:{}", pathDTO.getType().getText(),
                                pathDTO.getModeName(), pathDTO.getPathLowerCase());
                    }
                }
            }
        }
        return effectivePathList;
    }

    private List<UserProfilePathDetailDTO> getUserProfilePathDetail(ViewRcoUserProfileStrategyRelatedEntity relatedEntity) {
        List<UserProfileChildPathEntity> childPathEntityList = userProfileChildPathDAO.
                findByUserProfilePathIdOrderByIndex(relatedEntity.getId());
        List<UserProfilePathEntity> pathEntityList = pathDAO.findByUserProfilePathId(relatedEntity.getId());

        List<UserProfilePathDetailDTO> pathDetailDTOList = new ArrayList<>();
        for (UserProfileChildPathEntity childPath : childPathEntityList) {
            for (UserProfilePathEntity path : pathEntityList) {
                if (childPath.getId().equals(path.getUserProfileChildPathId())) {
                    UserProfilePathDetailDTO detailDTO = new UserProfilePathDetailDTO();
                    BeanUtils.copyProperties(relatedEntity, detailDTO);
                    detailDTO.setPathLowerCase(path.getPath().toLowerCase());
                    detailDTO.setPath(path.getPath());
                    detailDTO.setMode(childPath.getMode());
                    detailDTO.setType(childPath.getType());
                    pathDetailDTOList.add(detailDTO);
                }
            }
        }

        return pathDetailDTOList;
    }

    @Override
    public void isImportingPath() throws BusinessException {
        if (UserProfileImportStateCache.STATE.isImporting()) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_PATH_HAS_IMPORT);
        }
    }

    @Override
    public void startAddPathData() {
        synchronized (IMPORT_PATH_LOCK) {
            UserProfileImportStateCache.STATE.addTask();
        }
    }

    @Override
    public void finishAddPathData() {
        synchronized (IMPORT_PATH_LOCK) {
            UserProfileImportStateCache.STATE.removeTask();
        }
    }

    @Override
    public UUID getPathGroupIdIfNotExistCreate(UserProfilePathGroupDTO groupDTO) {
        Assert.notNull(groupDTO, "groupDTO 不能为空");
        return userProfileServiceTx.getPathGroupIdIfNotExistCreate(groupDTO);
    }

    @Override
    public PageQueryResponse<UserProfileStrategyDTO> pageUserProfileStrategyQuery(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "Param [pageQueryRequest] must not be null");
        PageQueryResponse<UserProfileStrategyViewDTO> pageQueryResponse = UserProfileMgmtAPI.super.pageQuery(pageQueryRequest);
        PageQueryResponse<UserProfileStrategyDTO> strategyResponse = new PageQueryResponse<>();
        strategyResponse.setTotal(pageQueryResponse.getTotal());

        if (pageQueryResponse.getItemArr() == null || pageQueryResponse.getItemArr().length == 0) {
            strategyResponse.setItemArr(new UserProfileStrategyDTO[] {});
            return strategyResponse;
        }

        UserProfileStrategyDTO[] strategyArr = Arrays.stream(pageQueryResponse.getItemArr()).map(viewStrategy -> {
            UserProfileStrategyDTO strategyDTO = new UserProfileStrategyDTO();
            BeanUtils.copyProperties(viewStrategy, strategyDTO);
            return strategyDTO;
        }).toArray(UserProfileStrategyDTO[]::new);
        strategyResponse.setItemArr(strategyArr);
        return strategyResponse;
    }

    @Override
    public String getStrategyUsedMessageByImageId(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId must not be null");
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        CbbOsType osType = imageTemplateDetail.getOsType();
        if (!CbbOsType.isWin7UpOS(osType)) {
            return LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DISABLE_IMAGE_TEMPLATE);
        }
        return StringUtils.EMPTY;
    }

    @Override
    public String getStrategyUsedMessageByStrategyId(UUID strategyId) throws BusinessException {
        Assert.notNull(strategyId, "strategyId must not be null");
        CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(strategyId);
        if (CbbCloudDeskPattern.RECOVERABLE != deskStrategy.getPattern() && CbbStrategyType.THIRD != deskStrategy.getStrategyType()) {
            return LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DISABLE_DESK_STRATEGY);
        }
        return StringUtils.EMPTY;
    }

    @Override
    public void getGuestToolStrategyPath(UserProfileStrategyGuestToolMsgDTO messageDTO, List<UserProfilePathDetailDTO> userProfilePathDTOList,
            UUID userProfileStrategyId) throws BusinessException {
        Assert.notNull(messageDTO, "messageDTO must not be null");
        Assert.notNull(userProfilePathDTOList, "userProfilePathDTOList must not be null");
        Assert.notNull(userProfileStrategyId, "userProfileStrategyId must not be null");

        UserProfileStrategyGuestToolMsgDTO.BodyMessage bodyMessage = new UserProfileStrategyGuestToolMsgDTO.BodyMessage();
        List<String> configDirToSaveList = new ArrayList<>();
        List<String> configDirNoSaveList = new ArrayList<>();
        List<String> configFileToSaveList = new ArrayList<>();
        List<String> configFileNoSaveList = new ArrayList<>();
        List<String> configKeyToSaveList = new ArrayList<>();
        List<String> configKeyNoSaveList = new ArrayList<>();
        List<String> configValueToSaveList = new ArrayList<>();
        List<String> configValueNoSaveList = new ArrayList<>();
        List<JSONObject> extraConfigList = new ArrayList<>();
        for (UserProfilePathDetailDTO pathDTO : userProfilePathDTOList) {
            // 根据应用分层要求按固定格式转换
            String path = pathDTO.getPath().replaceAll(DISC_REG, "")
                    .replaceAll(REGISTRY_MACHINE_CURRENTCONTROLSET_REG, "\\\\REGISTRY\\\\MACHINE\\\\system\\\\ControlSet001")
                    .replaceAll(REGISTRY_MACHINE_REG, "\\\\REGISTRY\\\\MACHINE")
                    .replaceAll(REGISTRY_USER_CLASSES_REG, "\\\\REGISTRY\\\\user\\\\S-1-5-21-????????*-????????*-????????*-????_classes")
                    .replaceAll(REGISTRY_USER_REG, "\\\\REGISTRY\\\\user\\\\S-1-5-21-????????*-????????*-????????*-????");

            UserProfilePathTypeEnum type = pathDTO.getType();
            // 同步
            if (UserProfilePathModeEnum.SYNCHRO == pathDTO.getMode()) {
                switch (type) {
                    case FOLDER:
                        configDirToSaveList.add(path);
                        break;
                    case DOCUMENT:
                        configFileToSaveList.add(path);
                        break;
                    case REGISTRY_KEY:
                        configKeyToSaveList.add(path);
                        break;
                    case REGISTRY_VALUE:
                        configValueToSaveList.add(path);
                        break;
                    default:
                        LOGGER.error("未知的路径类型，路径DTO:{}", JSON.toJSONString(pathDTO));
                        break;
                }
            } else {
                // 排除
                switch (type) {
                    case FOLDER:
                        configDirNoSaveList.add(path);
                        break;
                    case DOCUMENT:
                        configFileNoSaveList.add(path);
                        break;
                    case REGISTRY_KEY:
                        configKeyNoSaveList.add(path);
                        break;
                    case REGISTRY_VALUE:
                        configValueNoSaveList.add(path);
                        break;
                    default:
                        LOGGER.error("未知的路径类型，路径DTO:{}", JSON.toJSONString(pathDTO));
                        break;
                }
            }
        }
        bodyMessage.setConfigDirToSaveList(configDirToSaveList);
        bodyMessage.setConfigDirNoSaveList(configDirNoSaveList);
        bodyMessage.setConfigFileToSaveList(configFileToSaveList);
        bodyMessage.setConfigFileNoSaveList(configFileNoSaveList);
        bodyMessage.setConfigKeyToSaveList(configKeyToSaveList);
        bodyMessage.setConfigKeyNoSaveList(configKeyNoSaveList);
        bodyMessage.setConfigValueToSaveList(configValueToSaveList);
        bodyMessage.setConfigValueNoSaveList(configValueNoSaveList);
        String contentString = JSON.toJSONString(bodyMessage, JSON_FEATURES);

        List<ViewRcoUserProfileStrategyRelatedEntity> strategyRelatedEntityList =
                viewRcoUserProfileStrategyRelatedDAO.findByStrategyId(userProfileStrategyId);
        // 勾选的常用配置有额外配置参数需要传递
        if (!CollectionUtils.isEmpty(strategyRelatedEntityList)) {

            List<String> extraConfigInfoList = strategyRelatedEntityList.stream().map(ViewRcoUserProfileStrategyRelatedEntity::getExtraConfigInfo)
                    .filter(StringUtils::isNotBlank).collect(toList());

            if (!CollectionUtils.isEmpty(extraConfigInfoList)) {
                for (String config : extraConfigInfoList) {
                    JSONObject jsonObject = JSONObject.parseObject(config);
                    extraConfigList.add(jsonObject);
                }

                bodyMessage.setExtraConfigList(extraConfigList);
            }
        }

        String md5 = Md5Builder.computeTextMd5(contentString);
        bodyMessage.setMd5(md5);
        messageDTO.setContent(bodyMessage);
    }

    @Override
    public void getGuestToolCleanPath(UserProfileCleanGuestToolMsgDTO messageDTO,
                                                                 List<UserProfilePathDetailDTO> userProfilePathDTOList) {
        Assert.notNull(messageDTO, "messageDTO must not be null");
        Assert.notNull(userProfilePathDTOList, "userProfilePathDTOList must not be null");

        LOGGER.debug("将要被清理的路径对象{}", JSON.toJSONString(userProfilePathDTOList));

        UserProfileCleanGuestToolMsgDTO.BodyMessage bodyMessage = new UserProfileCleanGuestToolMsgDTO.BodyMessage();
        List<String> configDirList = new ArrayList<>();
        List<String> configFileList = new ArrayList<>();
        List<String> configKeyList = new ArrayList<>();
        List<String> configValueList = new ArrayList<>();
        for (UserProfilePathDetailDTO pathDTO : userProfilePathDTOList) {
            // 根据应用分层要求按固定格式转换
            String path = pathDTO.getPath().replaceAll(DISC_REG, "")
                    .replaceAll(REGISTRY_MACHINE_CURRENTCONTROLSET_REG, "\\\\REGISTRY\\\\MACHINE\\\\system\\\\ControlSet001")
                    .replaceAll(REGISTRY_MACHINE_REG, "\\\\REGISTRY\\\\MACHINE")
                    .replaceAll(REGISTRY_USER_CLASSES_REG, "\\\\REGISTRY\\\\user\\\\S-1-5-21-????????*-????????*-????????*-????_classes")
                    .replaceAll(REGISTRY_USER_REG, "\\\\REGISTRY\\\\user\\\\S-1-5-21-????????*-????????*-????????*-????");

            UserProfilePathTypeEnum type = pathDTO.getType();
            switch (type) {
                case FOLDER:
                    configDirList.add(path);
                    break;
                case DOCUMENT:
                    configFileList.add(path);
                    break;
                case REGISTRY_KEY:
                    configKeyList.add(path);
                    break;
                case REGISTRY_VALUE:
                    configValueList.add(path);
                    break;
                default:
                    LOGGER.error("未知的路径类型，路径DTO:{}", JSON.toJSONString(pathDTO));
                    break;
            }
        }
        bodyMessage.setConfigDirList(configDirList);
        bodyMessage.setConfigFileList(configFileList);
        bodyMessage.setConfigKeyList(configKeyList);
        bodyMessage.setConfigValueList(configValueList);
        String contentString = JSON.toJSONString(bodyMessage, JSON_FEATURES);
        String md5 = Md5Builder.computeTextMd5(contentString);
        bodyMessage.setMd5(md5);
        messageDTO.setContent(bodyMessage);
    }

    @Override
    public void deleteDeskRelatedUserProfileStrategy(UUID deskStrategyId) {
        Assert.notNull(deskStrategyId, "deskStrategyId must not be null");
        LOGGER.info("删除云桌面策略:[{}]关联的所有用户配置策略记录", deskStrategyId);
        List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.getAllDesktopByStrategyId(deskStrategyId);
        desktopList.forEach(cloudDesktopDTO -> {
            if (cloudDesktopDTO.getUserProfileStrategyId() != null) {
                LOGGER.info("删除云桌面策略:[{}]关联的云桌面:[{}]与用户配置策略:[{}]记录", deskStrategyId, cloudDesktopDTO.getId(),
                        cloudDesktopDTO.getUserProfileStrategyId());

                // 清理个性化策略ID
                Optional.ofNullable(rcoDeskInfoDAO.findByDeskId(cloudDesktopDTO.getId())).ifPresent((rcoDeskInfoEntity) -> {
                    rcoDeskInfoEntity.setUserProfileStrategyId(null);
                    rcoDeskInfoDAO.save(rcoDeskInfoEntity);
                });
            }
        });

        List<IdvTerminalGroupDeskConfigEntity> terminalGroupDeskConfigList =
                idvTerminalGroupDeskConfigDAO.findByCbbIdvDesktopStrategyId(deskStrategyId);
        terminalGroupDeskConfigList.forEach(terminalGroupDeskConfigEntity -> {
            if (terminalGroupDeskConfigEntity.getUserProfileStrategyId() != null) {
                LOGGER.info("更新云桌面策略:[{}]关联的终端组配置:[{}]记录中的用户配置策略ID为空", deskStrategyId, terminalGroupDeskConfigEntity.getId());
                terminalGroupDeskConfigEntity.setUserProfileStrategyId(null);
                idvTerminalGroupDeskConfigDAO.save(terminalGroupDeskConfigEntity);
            }
        });

        List<UserGroupDesktopConfigEntity> userGroupDesktopConfigList = userGroupDesktopConfigDAO.findByStrategyId(deskStrategyId);
        userGroupDesktopConfigList.forEach(userGroupDesktopConfigEntity -> {
            if (userGroupDesktopConfigEntity.getUserProfileStrategyId() != null) {
                LOGGER.info("更新云桌面策略:[{}]关联的用户组配置:[{}]记录中的用户配置策略ID为空", deskStrategyId, userGroupDesktopConfigEntity.getId());
                userGroupDesktopConfigEntity.setUserProfileStrategyId(null);
                userGroupDesktopConfigDAO.save(userGroupDesktopConfigEntity);
            }
        });

        List<UserDesktopConfigEntity> userDesktopConfigList = userDesktopConfigDAO.findByStrategyId(deskStrategyId);
        userDesktopConfigList.forEach(userDesktopConfigEntity -> {
            if (userDesktopConfigEntity.getUserProfileStrategyId() != null) {
                LOGGER.info("更新云桌面策略:[{}]关联的用户组配置:[{}]记录中的用户配置策略ID为空", deskStrategyId, userDesktopConfigEntity.getId());
                userDesktopConfigEntity.setUserProfileStrategyId(null);
                userDesktopConfigDAO.save(userDesktopConfigEntity);
            }
        });
    }

    private UserProfilePathGroupEntity findUserProfilePathGroupEntity(UUID id) throws BusinessException {
        Optional<UserProfilePathGroupEntity> entityOptional = rcoUserProfilePathGroupDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_GROUP_NOT_EXIST, id);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_GROUP_NOT_EXIST, id.toString());
        }
        return entityOptional.get();
    }

    private UserProfilePathMainEntity findUserProfilePathEntity(UUID id) throws BusinessException {
        Optional<UserProfilePathMainEntity> entityOptional = rcoUserProfilePathDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error("路径ID[{}]对应的路径不存在", id);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_NOT_EXIST);
        }
        return entityOptional.get();
    }

    private UserProfileStrategyEntity findUserProfileStrategyEntity(UUID id) throws BusinessException {
        Optional<UserProfileStrategyEntity> entityOptional = rcoUserProfileStrategyDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error("ID[{}]对应的用户配置策略不存在", id);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_NOT_EXIST);
        }
        return entityOptional.get();
    }

    private ViewRcoUserProfileStrategyCountEntity findUserProfileStrategyCountEntity(UUID id) throws BusinessException {
        Optional<ViewRcoUserProfileStrategyCountEntity> entityOptional = viewRcoUserProfileStrategyCountDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error("ID[{}]对应的用户配置策略不存在", id);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_NOT_EXIST);
        }
        return entityOptional.get();
    }

    private void checkRelatedPathSize(UserProfileStrategyDTO userProfileStrategyDTO) throws BusinessException {
        if (userProfileStrategyDTO.getPathArr().length > RELATED_PATH_SIZE) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_RELATED_PATH_SIZE, userProfileStrategyDTO.getName(),
                    String.valueOf(RELATED_PATH_SIZE));
        }
    }

    @Override
    public void sendCleanUserProfilePathMessage(UserProfileCleanGuestToolMsgDTO guestToolMsgDTO, UUID deskId) throws BusinessException {
        Assert.notNull(guestToolMsgDTO, "guestToolMsgDTO must not be null");
        Assert.notNull(deskId, "deskId must not be null");

        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        messageDTO.setCmdId(GuestToolCmdId.NOTIFY_GT_CMD_ID_CLEAN_USER_PROFILE_PATH);
        messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_USER_PROFILE_STRATEGY);
        messageDTO.setBody(JSON.toJSONString(guestToolMsgDTO, JSON_FEATURES));
        messageDTO.setDeskId(deskId);

        guestToolMessageAPI.asyncRequest(messageDTO);
    }

    @Override
    public void saveFailCleanRequest(UserProfileCleanGuestToolMsgDTO guestToolMsgDTO, UUID deskId) {
        Assert.notNull(guestToolMsgDTO, "guestToolMsgDTO must not be null");
        Assert.notNull(deskId, "deskId must not be null");

        if (guestToolMsgDTO.getContent() == null) {
            return;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("下发失败的路径清理消息为[{}]", JSON.toJSONString(guestToolMsgDTO, JSON_FEATURES));
        }
        List<UserProfileFailCleanRequestEntity> failCleanEntityList = new ArrayList<>();

        List<String> configDirList = guestToolMsgDTO.getContent().getConfigDirList();
        if (!CollectionUtils.isEmpty(configDirList)) {
            for (String configDir : configDirList) {
                failCleanEntityList.add(buildFailCleanRequest(configDir, deskId, UserProfilePathTypeEnum.FOLDER));
            }
        }

        List<String> configFileList = guestToolMsgDTO.getContent().getConfigFileList();
        if (!CollectionUtils.isEmpty(configFileList)) {
            for (String configFile : configFileList) {
                failCleanEntityList.add(buildFailCleanRequest(configFile, deskId, UserProfilePathTypeEnum.DOCUMENT));
            }
        }

        List<String> configKeyList = guestToolMsgDTO.getContent().getConfigKeyList();
        if (!CollectionUtils.isEmpty(configKeyList)) {
            for (String configKey : configKeyList) {
                failCleanEntityList.add(buildFailCleanRequest(configKey, deskId, UserProfilePathTypeEnum.REGISTRY_KEY));
            }
        }


        List<String> configValueList = guestToolMsgDTO.getContent().getConfigValueList();
        if (!CollectionUtils.isEmpty(configValueList)) {
            for (String configValue : configValueList) {
                failCleanEntityList.add(buildFailCleanRequest(configValue, deskId, UserProfilePathTypeEnum.REGISTRY_VALUE));
            }
        }

        if (!failCleanEntityList.isEmpty()) {
            //删除旧的失败请求
            userProfileFailCleanDAO.deleteByDesktopId(deskId);
            //增加新的失败请求
            userProfileFailCleanDAO.saveAll(failCleanEntityList);
        }
    }

    @Override
    public void deleteFailCleanRequestById(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId must not be null");

        userProfileFailCleanDAO.deleteByDesktopId(desktopId);
    }

    @Override
    public void deleteFailCleanRequestByIdArr(UUID[] desktopIdArr) {
        Assert.notNull(desktopIdArr, "desktopIdArr must not be null");

        userProfileFailCleanDAO.deleteByDesktopIdIn(desktopIdArr);
    }

    private UserProfileFailCleanRequestEntity buildFailCleanRequest(String path, UUID deskId, UserProfilePathTypeEnum type) {
        UserProfileFailCleanRequestEntity failClean = new UserProfileFailCleanRequestEntity();
        failClean.setPath(path);
        failClean.setDesktopId(deskId);
        failClean.setType(type);

        return failClean;
    }

    @Override
    public void deleteChildPath(UUID userProfileDetailId) throws BusinessException {
        Assert.notNull(userProfileDetailId, "userProfileDetailId must not be null");

        UserProfilePathEntity path = pathDAO.getOne(userProfileDetailId);
        if (path == null) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_NOT_EXIST_PATH);
        }

        if (pathDAO.countByUserProfilePathId(path.getUserProfilePathId()) <= MIN_CHILD_PATH_NUM) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_LAST_PATH , path.getPath());
        }
        pathDAO.deleteById(userProfileDetailId);

        if (pathDAO.countByUserProfileChildPathId(path.getUserProfileChildPathId()) == 0) {
            userProfileChildPathDAO.deleteById(path.getUserProfileChildPathId());
        }

        // 删除原先个性化配置的缓存
        userProfileStrategyNotifyAPI.updatePathNotifyDesk(path.getUserProfilePathId());
        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_CHILD_PATH_DELETE, path.getPath());
    }

    @Override
    public int getPathCountOfStrategy(UUID[] userProfilePathIdArr) {
        Assert.notNull(userProfilePathIdArr, "userProfilePathIdArr must not be null");

        return pathDAO.countByUserProfilePathIdArr(userProfilePathIdArr);
    }

    @Override
    public String findStrategyNameByStrategyId(UUID strategyId) {
        Assert.notNull(strategyId, "strategy must not be null");

        return rcoUserProfileStrategyDAO.findNameById(strategyId);
    }

    @Override
    public UUID findUserProfilePathIdByChildPath(UUID userProfileDetailId) {
        Assert.notNull(userProfileDetailId, "userProfileDetailId must not be null");

        UserProfilePathEntity path = pathDAO.getOne(userProfileDetailId);
        return path.getUserProfilePathId();
    }
}
