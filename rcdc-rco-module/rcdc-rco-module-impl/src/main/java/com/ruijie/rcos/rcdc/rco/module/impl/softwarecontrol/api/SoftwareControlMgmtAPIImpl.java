package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.api;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareStrategyNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.RcoGlobalParameterEnum;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareGroupTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareRelationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareStrategyRelatedTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request.SoftwareStrategyBindRelationRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.ListRequestHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.SoftwareStrategyCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolConfigService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.SoftwareService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.SoftwareStrategyRelatedSoftwareViewService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.SoftwareStrategyViewService;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.cache.SoftwareImportStateCache;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.constant.SoftwareControlBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.*;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.*;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.service.SoftStrategyRelatedSoftwareService;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.tx.SoftwareControlServiceTx;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public class SoftwareControlMgmtAPIImpl implements SoftwareControlMgmtAPI {


    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareControlMgmtAPIImpl.class);

    @Autowired
    private RcoSoftwareGroupDAO rcoSoftwareGroupDAO;

    @Autowired
    private RcoSoftwareDAO rcoSoftwareDAO;

    @Autowired
    private RcoSoftwareStrategyDetailDAO rcoSoftwareStrategyDetailDAO;

    @Autowired
    private RcoSoftwareStrategyDAO rcoSoftwareStrategyDAO;

    @Autowired
    private ViewRcoSoftwareStrategyCountDAO viewRcoSoftwareStrategyCountDAO;

    @Autowired
    private SoftwareControlServiceTx softwareControlServiceTx;

    @Autowired
    private RcoSoftwareStrategyRelationDAO rcoSoftwareStrategyRelationDAO;

    @Autowired
    private SoftwareStrategyNotifyAPI softwareStrategyNotifyAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private SoftwareService softwareService;

    @Autowired
    private SoftwareStrategyRelatedSoftwareViewService softwareStrategyRelatedSoftwareViewService;

    @Autowired
    private SoftStrategyRelatedSoftwareService softStrategyRelatedSoftwareService;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private SoftwareStrategyViewService softwareStrategyViewService;

    @Autowired
    private DesktopPoolConfigService desktopPoolConfigService;

    public static final Object IMPORT_SOFTWARE_LOCK = new Object();

    private static final int DEFAULT_PAGE_SIZE = 200;

    private static final int DEFAULT_QUERY_PARAM_SIZE = 50;

    private static final ExecutorService SOFTWARE_THREAD_POOL =
            ThreadExecutors.newBuilder("SoftwareSingleThreadPool").maxThreadNum(10).queueSize(10).build();


    @Override
    public void isImportingSoftware() throws BusinessException {
        if (SoftwareImportStateCache.STATE.isImporting()) {
            LOGGER.info("检测存在软件导入的任务");
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_HAS_IMPORT_SOFTWARE);
        }
    }

    @Override
    public void startAddSoftwareData() {
        LOGGER.info("receive start import software message");

        synchronized (IMPORT_SOFTWARE_LOCK) {
            // 验证软件数据是否正在创建：
            SoftwareImportStateCache.STATE.addTask();
        }
    }

    @Override
    public void finishAddSoftwareData() {
        LOGGER.info("receive finish import software message");
        synchronized (IMPORT_SOFTWARE_LOCK) {
            SoftwareImportStateCache.STATE.removeTask();
        }
    }

    /**
     * @param idList        软件id列表
     * @param sourceGroupId 源软件组
     * @param targetGroupId 目标软件组
     * @throws BusinessException 抛出异常
     */
    @Override
    public void moveSoftware(List<UUID> idList, UUID sourceGroupId, UUID targetGroupId) throws BusinessException {
        Assert.notNull(idList, "idList is null");
        Assert.notNull(sourceGroupId, "sourceGroupId is null");
        Assert.notNull(targetGroupId, "targetGroupId is null");

        softwareControlServiceTx.moveSoftware(idList, targetGroupId);
    }

    /**
     * 根据策略获取当前策略下所有的软件清单
     *
     * @param strategyId
     * @return
     */
    @Override
    public List<SoftwareDTO> findAllByStrategyId(UUID strategyId) {
        Assert.notNull(strategyId, "request is null");

        List<RcoSoftwareStrategyDetailEntity> softwareStrategyDetailEntityList = rcoSoftwareStrategyDetailDAO.findByStrategyId(strategyId);
        if (softwareStrategyDetailEntityList.isEmpty()) {
            LOGGER.info("获取软件策略下属的软件列表为空，strategyId:{} ", strategyId);
            return Collections.EMPTY_LIST;
        }

        //获取策略下的软件、软件组
        List<UUID> softwareIdList = new ArrayList<>();
        List<UUID> softwareGroupIdList = new ArrayList<>();
        for (RcoSoftwareStrategyDetailEntity entity : softwareStrategyDetailEntityList) {
            if (SoftwareStrategyRelatedTypeEnum.GROUP == entity.getRelatedType()) {
                softwareGroupIdList.add(entity.getRelatedId());
            } else if (SoftwareStrategyRelatedTypeEnum.SOFTWARE == entity.getRelatedType()) {
                softwareIdList.add(entity.getRelatedId());
            }
        }

        List<SoftwareDTO> softwareDTOList = new ArrayList<>();
        //查询组下的所有软件
        if (!softwareGroupIdList.isEmpty()) {
            List<RcoSoftwareEntity> softwareEntityList = rcoSoftwareDAO.findByGroupIdIn(softwareGroupIdList);
            softwareEntityList.stream().forEach((entity) -> {
                if (!entity.getDirectoryFlag()) {
                    SoftwareDTO softwareDTO = new SoftwareDTO();
                    BeanUtils.copyProperties(entity, softwareDTO);
                    softwareDTOList.add(softwareDTO);
                }
            });
        }

        //查询所有软件,排除绿色软件目录
        if (!softwareIdList.isEmpty()) {
            List<List<UUID>> softwareSubIdList = ListRequestHelper.subList(softwareIdList, DEFAULT_QUERY_PARAM_SIZE);
            for (List<UUID> idList : softwareSubIdList) {
                List<RcoSoftwareEntity> softwareEntityList = rcoSoftwareDAO.findByIdInOrParentIdIn(idList, idList);
                softwareEntityList.stream().forEach((entity) -> {
                    if (!entity.getDirectoryFlag()) {
                        SoftwareDTO softwareDTO = new SoftwareDTO();
                        BeanUtils.copyProperties(entity, softwareDTO);
                        softwareDTOList.add(softwareDTO);
                    }
                });
            }
        }
        return softwareDTOList;
    }

    /**
     * 通知使用该软控分组策略的桌面
     *
     * @param softwareGroupId
     */
    @Override
    public void updateSoftwareGroupNotNotifyDesk(UUID softwareGroupId) {
        Assert.notNull(softwareGroupId, "softwareGroupId is null");

        if (softwareGroupId != null) {
            softwareStrategyNotifyAPI.updateSoftwareGroupNotifyDesk(softwareGroupId);
        }
    }

    /**
     * 批量添加软件
     *
     * @param softwareIdSet
     * @param softwareStrategyIdSet
     */
    @Override
    public void addSoftwareStrategyRelation(Set<UUID> softwareIdSet, Set<UUID> softwareStrategyIdSet) throws BusinessException {
        Assert.notNull(softwareIdSet, "softwareIdList is null");
        Assert.notNull(softwareStrategyIdSet, "softwareStrategyIdList is null");

        Integer softwareCount = rcoSoftwareDAO.countByIdIn(softwareIdSet);
        if (softwareIdSet.size() != softwareCount) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_COUNT_ERROR);
        }

        Integer softwareStrategyCount = rcoSoftwareStrategyDAO.countByIdIn(softwareStrategyIdSet);
        if (softwareStrategyIdSet.size() != softwareStrategyCount) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_COUNT_ERROR);
        }
        List<SoftwareStrategyDTO> softwareStrategyDTOList = findSoftwareStrategyByIdIn(softwareStrategyIdSet);
        softwareStrategyDTOList.forEach((dto) -> {
            //建立策略跟软件的关系
            softwareControlServiceTx.batchAddSoftwareStrategyDetail(dto.getId(), softwareIdSet);
            //记录审计日志、并通知软控策略变更
            List<SoftwareDTO> softwareDTOList = findSoftwareByIdIn(softwareIdSet);
            //将软件列表名称按10个做合并,减少审计记日志记录次数
            //若软件名称长度修改，需要变更方法里的个数
            List<String> joinNameList = joinSoftwareName(softwareDTOList);
            joinNameList.forEach((nameArr) -> {
                auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_SETTING_STRATEGY,
                        nameArr, dto.getName());
            });
            softwareStrategyNotifyAPI.updateSoftwareStrategyNotifyDesk(dto.getId());
        });
    }


    @Override
    public Boolean checkSoftwareStrategyNameDuplication(@Nullable UUID id, String name) {
        Assert.hasText(name, "name cannot null");
        RcoSoftwareStrategyEntity softwareStrategyEntity = rcoSoftwareStrategyDAO.findByName(name);
        if (softwareStrategyEntity != null && !softwareStrategyEntity.getId().equals(id)) {
            LOGGER.info("检查软件策略名称是否重复: [{}] 重复了", name);
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkSoftwareGroupNameDuplication(@Nullable UUID id, String name, SoftwareGroupTypeEnum groupType) {
        Assert.hasText(name, "name cannot null");
        Assert.notNull(groupType, "groupType cannot null");
        RcoSoftwareGroupEntity softwareGroupEntity = rcoSoftwareGroupDAO.findByNameAndGroupType(name, groupType);
        if (softwareGroupEntity != null && !softwareGroupEntity.getId().equals(id)) {
            LOGGER.info("检查软件分组名称是否重复: [{}] 重复了", name);
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkSoftwareNameDuplication(UUID id, String name) {
        Assert.hasText(name, "name cannot null");
        Assert.notNull(id, "id cannot null");
        RcoSoftwareEntity softwareEntity = rcoSoftwareDAO.findByNameAndTopLevelFile(name, Boolean.TRUE);
        if (softwareEntity != null && !softwareEntity.getId().equals(id)) {
            LOGGER.info("检查软件名称是否重复: [{}] 重复了", name);
            return true;
        }
        return false;
    }

    @Override
    public SoftwareDTO checkSoftwareNameDuplication(String name) {
        Assert.hasText(name, "name cannot null");
        RcoSoftwareEntity softwareEntity = rcoSoftwareDAO.findByNameAndTopLevelFile(name, Boolean.TRUE);
        if (softwareEntity != null) {
            LOGGER.info("检查软件名称是否重复: [{}] 重复了", name);
            SoftwareDTO softwareDTO = new SoftwareDTO();
            BeanUtils.copyProperties(softwareEntity, softwareDTO);
            return softwareDTO;
        }
        // 若无名称重复，则返回null
        return null;
    }

    /**
     * 软件同类型重名检查
     *
     * @param md5 软件md5值
     * @return 是否重名
     */
    @Override
    public SoftwareDTO checkSoftwareMd5Duplication(String md5) {
        Assert.hasText(md5, "md5 cannot null");
        RcoSoftwareEntity softwareEntity = rcoSoftwareDAO.findByFileCustomMd5AndTopLevelFile(md5, Boolean.TRUE);
        if (softwareEntity != null) {
            LOGGER.info("检查软件md5是否重复: [{}] 重复了", md5);
            SoftwareDTO softwareDTO = new SoftwareDTO();
            BeanUtils.copyProperties(softwareEntity, softwareDTO);
            return softwareDTO;
        }
        // 若无md5重复，则返回null
        return null;
    }

    @Override
    public void createSoftwareStrategy(SoftwareStrategyDTO softwareStrategyDTO) throws BusinessException {
        Assert.notNull(softwareStrategyDTO, "softwareStrategyDTO cannot null");
        String strategyName = softwareStrategyDTO.getName();
        Boolean hasDuplication = checkSoftwareStrategyNameDuplication(softwareStrategyDTO.getId(), strategyName);
        if (BooleanUtils.isTrue(hasDuplication)) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_NAME_EXIST_ERROR, strategyName);
        }
        softwareControlServiceTx.createSoftwareStrategy(softwareStrategyDTO);
    }

    @Override
    public void createSoftwareGroup(SoftwareGroupDTO softwareGroupDTO) throws BusinessException {
        Assert.notNull(softwareGroupDTO, "softwareGroupDTO cannot null");

        Boolean hasDuplication =
                checkSoftwareGroupNameDuplication(softwareGroupDTO.getId(), softwareGroupDTO.getName(), softwareGroupDTO.getGroupType());
        if (BooleanUtils.isTrue(hasDuplication)) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_NAME_EXIST_ERROR, softwareGroupDTO.getName());
        }

        long softwareGroupCount = rcoSoftwareGroupDAO.count();
        if (Constants.SOFTWARE_MAX_COUNT_LIMIT <= softwareGroupCount) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_GROUP_COUNT_LIMIT_ERROR,
                    String.valueOf(Constants.SOFTWARE_MAX_COUNT_LIMIT));
        }
        RcoSoftwareGroupEntity rcoSoftwareGroupEntity = new RcoSoftwareGroupEntity();
        BeanUtils.copyProperties(softwareGroupDTO, rcoSoftwareGroupEntity);
        rcoSoftwareGroupDAO.save(rcoSoftwareGroupEntity);
    }

    @Override
    public void createSoftware(SoftwareDTO softwareDTO) {
        Assert.notNull(softwareDTO, "softwareDTO cannot null");
        RcoSoftwareEntity rcoSoftwareEntity = new RcoSoftwareEntity();
        BeanUtils.copyProperties(softwareDTO, rcoSoftwareEntity);
        rcoSoftwareEntity.setNameSearch(rcoSoftwareEntity.getName().toLowerCase());
        rcoSoftwareDAO.save(rcoSoftwareEntity);
    }

    @Override
    public void bindRelation(SoftwareStrategyBindRelationRequest request) {
        Assert.notNull(request, "request 不能为空");
        softwareControlServiceTx.bindRelation(request);
    }

    @Override
    public void updateRelationBindStrategy(SoftwareRelationTypeEnum relationType, UUID relationId, UUID newStrategyId) {
        Assert.notNull(relationType, "relationType 不能为空");
        Assert.notNull(relationId, "relationId 不能为空");
        Assert.notNull(newStrategyId, "newStrategyId 不能为空");

        softwareControlServiceTx.updateRelationBindStrategy(relationType, relationId, newStrategyId);
    }

    @Override
    public void deleteBindRelation(SoftwareRelationTypeEnum relationType, UUID relationId) {
        Assert.notNull(relationType, "relationType 不能为空");
        Assert.notNull(relationId, "relationId 不能为空");
        softwareControlServiceTx.deleteBindRelation(relationType, relationId);
    }

    @Override
    public SoftwareStrategyDTO getStrategyByRelation(SoftwareRelationTypeEnum relationType, UUID relationId) throws BusinessException {
        Assert.notNull(relationType, "relationType cannot null");
        Assert.notNull(relationId, "relationId cannot null");
        RcoSoftwareStrategyRelationEntity relationEntity = rcoSoftwareStrategyRelationDAO.findByRelationTypeAndRelationId(relationType, relationId);

        if (Objects.isNull(relationEntity)) {
            // 没有就返回null
            return null;
        }

        return this.findSoftwareStrategyWrapperById(relationEntity.getSoftwareStrategyId());
    }

    @Override
    public SoftwareStrategyDTO findSoftwareStrategyWrapperById(UUID id) throws BusinessException {
        Assert.notNull(id, "id cannot null");
        RcoSoftwareStrategyEntity softwareStrategyEntity = findSoftwareStrategyEntity(id);
        SoftwareStrategyDTO softwareStrategyDTO = new SoftwareStrategyDTO();
        BeanUtils.copyProperties(softwareStrategyEntity, softwareStrategyDTO);
        ViewRcoSoftwareStrategyCountEntity softwareStrategyCountEntity = findSoftwareStrategyCountEntity(id);
        softwareStrategyDTO.setCount(softwareStrategyCountEntity.getCount() == null ? 0L : softwareStrategyCountEntity.getCount());

        // 获取策略关联的分组或软件
        List<RcoSoftwareStrategyDetailEntity> strategyDetailList = rcoSoftwareStrategyDetailDAO.findByStrategyId(id);

        List<UUID> softwareIdList = new ArrayList<>();
        List<UUID> groupIdList = new ArrayList<>();
        strategyDetailList.stream().forEach(strategyDetailEntity -> {
            if (SoftwareStrategyRelatedTypeEnum.GROUP == strategyDetailEntity.getRelatedType()) {
                groupIdList.add(strategyDetailEntity.getRelatedId());
            } else {
                softwareIdList.add(strategyDetailEntity.getRelatedId());
            }
        });

        List<RcoSoftwareGroupEntity> softwareGroupEntityList = rcoSoftwareGroupDAO.findAllById(groupIdList);
        List<RcoSoftwareEntity> softwareEntityList = rcoSoftwareDAO.findAllById(softwareIdList);
        List<SoftwareStrategyDetailDTO> strategyDetailDTOList = new ArrayList<>();
        for (RcoSoftwareGroupEntity softwareGroupEntity : softwareGroupEntityList) {
            SoftwareStrategyDetailDTO softwareStrategyDetailDTO = new SoftwareStrategyDetailDTO();
            softwareStrategyDetailDTO.setId(softwareGroupEntity.getId());
            softwareStrategyDetailDTO.setLabel(softwareGroupEntity.getName());
            strategyDetailDTOList.add(softwareStrategyDetailDTO);
        }
        for (RcoSoftwareEntity softwareEntity : softwareEntityList) {
            SoftwareStrategyDetailDTO softwareStrategyDetailDTO = new SoftwareStrategyDetailDTO();
            softwareStrategyDetailDTO.setId(softwareEntity.getId());
            softwareStrategyDetailDTO.setLabel(softwareEntity.getName());
            softwareStrategyDetailDTO.setGroupId(softwareEntity.getGroupId());
            strategyDetailDTOList.add(softwareStrategyDetailDTO);
        }

        softwareStrategyDTO.setSoftwareGroupArr(strategyDetailDTOList.toArray(new SoftwareStrategyDetailDTO[strategyDetailDTOList.size()]));

        return softwareStrategyDTO;
    }

    /**
     * 通过软件策略Id查找软件策略
     *
     * @param id 软件策略Id
     * @return 软件策略对象
     * @throws BusinessException 异常抛出
     */
    @Override
    public SoftwareStrategyDTO findSoftwareStrategyById(UUID id) throws BusinessException {
        Assert.notNull(id, "id cannot null");
        SoftwareStrategyDTO softwareStrategyDTO = SoftwareStrategyCacheManager.getSoftwareStrategy(id);
        if (softwareStrategyDTO == null) {
            softwareStrategyDTO = new SoftwareStrategyDTO();
            RcoSoftwareStrategyEntity rcoSoftwareStrategyEntity = findSoftwareStrategyEntity(id);
            BeanUtils.copyProperties(rcoSoftwareStrategyEntity, softwareStrategyDTO);
            SoftwareStrategyCacheManager.addSoftwareStrategy(id, softwareStrategyDTO);
        }
        SoftwareStrategyDTO newSoftwareStrategyDTO = new SoftwareStrategyDTO();
        BeanUtils.copyProperties(softwareStrategyDTO, newSoftwareStrategyDTO);
        return newSoftwareStrategyDTO;
    }

    @Override
    public SoftwareGroupDTO findSoftwareGroupById(UUID id) throws BusinessException {
        Assert.notNull(id, "id cannot null");
        RcoSoftwareGroupEntity rcoSoftwareGroupEntity = findSoftwareGroupEntity(id);
        SoftwareGroupDTO softwareGroupDTO = new SoftwareGroupDTO();
        BeanUtils.copyProperties(rcoSoftwareGroupEntity, softwareGroupDTO);
        return softwareGroupDTO;
    }

    @Override
    public SoftwareDTO findSoftwareById(UUID id) throws BusinessException {
        Assert.notNull(id, "id cannot null");
        RcoSoftwareEntity rcoSoftwareEntity = findSoftwareEntity(id);
        SoftwareDTO softwareDTO = new SoftwareDTO();
        BeanUtils.copyProperties(rcoSoftwareEntity, softwareDTO);
        return softwareDTO;
    }

    @Override
    public List<SoftwareDTO> findSoftwareByIdIn(Iterable<UUID> ids) {
        Assert.notNull(ids, "ids cannot null");
        List<RcoSoftwareEntity> rcoSoftwareEntityList = rcoSoftwareDAO.findByIdIn(ids);
        List<SoftwareDTO> softwareDTOList = new ArrayList<>(rcoSoftwareEntityList.size());

        rcoSoftwareEntityList.forEach((entity) -> {
            SoftwareDTO softwareDTO = new SoftwareDTO();
            BeanUtils.copyProperties(entity, softwareDTO);
            softwareDTOList.add(softwareDTO);
        });
        return softwareDTOList;
    }

    @Override
    public void editSoftwareStrategy(SoftwareStrategyDTO softwareStrategyDTO) throws BusinessException {
        Assert.notNull(softwareStrategyDTO, "softwareStrategyDTO 不能为空");
        String strategyName = softwareStrategyDTO.getName();
        Boolean hasDuplication = checkSoftwareStrategyNameDuplication(softwareStrategyDTO.getId(), strategyName);
        if (BooleanUtils.isTrue(hasDuplication)) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_NAME_EXIST_ERROR, strategyName);
        }

        softwareControlServiceTx.editSoftwareStrategy(softwareStrategyDTO);
        SoftwareStrategyCacheManager.deleteSoftwareStrategyCache(softwareStrategyDTO.getId());
        softwareStrategyNotifyAPI.updateSoftwareStrategyNotifyDesk(softwareStrategyDTO.getId());
    }

    @Override
    public void editSoftwareGroup(SoftwareGroupDTO softwareGroupDTO) throws BusinessException {
        Assert.notNull(softwareGroupDTO, "softwareGroupDTO 不能为空");
        //判断修改的策略名是否已经被使用了
        Boolean hasDuplication =
                checkSoftwareGroupNameDuplication(softwareGroupDTO.getId(), softwareGroupDTO.getName(), softwareGroupDTO.getGroupType());
        if (BooleanUtils.isTrue(hasDuplication)) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_NAME_EXIST_ERROR, softwareGroupDTO.getName());
        }

        LockableExecutor.executeWithTryLock(softwareGroupDTO.getId().toString(), () -> {
            RcoSoftwareGroupEntity entity = findSoftwareGroupEntity(softwareGroupDTO.getId());
            entity.setName(softwareGroupDTO.getName());
            entity.setDescription(softwareGroupDTO.getDescription());
            entity.setGroupType(softwareGroupDTO.getGroupType());
            rcoSoftwareGroupDAO.save(entity);
        }, 1);

    }

    @Override
    public void editSoftware(SoftwareDTO softwareDTO) throws BusinessException {
        Assert.notNull(softwareDTO, "softwareDTO 不能为空");
        LockableExecutor.executeWithTryLock(softwareDTO.getId().toString(), () -> {
            RcoSoftwareEntity entity = findSoftwareEntity(softwareDTO.getId());
            if (softwareDTO.getDirectoryFlag() == null) {
                softwareDTO.setDirectoryFlag(entity.getDirectoryFlag());
            }
            if (softwareDTO.getParentId() == null) {
                softwareDTO.setParentId(entity.getParentId());
            }
            softwareDTO.setTopLevelFile(entity.getTopLevelFile());
            BeanUtils.copyProperties(softwareDTO, entity);
            entity.setNameSearch(entity.getName().toLowerCase());
            rcoSoftwareDAO.save(entity);
        }, 1);
        softwareStrategyNotifyAPI.updateSoftwareNotifyDesk(Arrays.asList(softwareDTO.getId()));
    }

    @Override
    public void deleteSoftwareStrategy(UUID id) throws BusinessException {
        Assert.notNull(id, "id 不能为空");
        List<RcoDeskInfoEntity> rcoDeskInfoEntityList = rcoDeskInfoDAO.findBySoftwareStrategyId(id);
        if (!rcoDeskInfoEntityList.isEmpty()) {
            LOGGER.error("软件策略被云桌面引用不允许删除, 软件策略id[{}]", id);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_USED);
        }

        if (desktopPoolConfigService.existsBySoftwareStrategyId(id)) {
            LOGGER.error("软件策略被桌面池引用不允许删除, 软件策略id[{}]", id);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_USED);
        }

        RcoSoftwareStrategyEntity entity = findSoftwareStrategyEntity(id);
        softwareControlServiceTx.deleteSoftwareStrategy(entity);
        SoftwareStrategyCacheManager.deleteSoftwareStrategyCache(id);
        auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_DELETE, entity.getName());
    }

    @Override
    public void deleteSoftwareGroup(UUID id) throws BusinessException {
        Assert.notNull(id, "id 不能为空");
        RcoSoftwareGroupEntity entity = findSoftwareGroupEntity(id);
        softwareControlServiceTx.deleteSoftwareGroup(entity);
        auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_DELETE, entity.getName());
    }

    @Override
    public void deleteSoftware(UUID id) throws BusinessException {
        Assert.notNull(id, "id 不能为空");
        List<RcoSoftwareStrategyDetailEntity> rcoSoftwareStrategyDetailEntityList =
                rcoSoftwareStrategyDetailDAO.findByRelatedIdAndRelatedType(id, SoftwareStrategyRelatedTypeEnum.SOFTWARE);
        softwareControlServiceTx.deleteSoftware(id);
        for (RcoSoftwareStrategyDetailEntity element : rcoSoftwareStrategyDetailEntityList) {
            softwareStrategyNotifyAPI.updateSoftwareStrategyNotifyDesk(element.getStrategyId());
        }

    }

    @Override
    public void deleteSoftwareFromSoftwareStrategyDetail(UUID strategyId, List<UUID> softwareIds) throws BusinessException {
        Assert.notNull(strategyId, "strategyId 不能为空");
        Assert.notNull(softwareIds, "softwareIds 不能为空");

        SoftwareStrategyDTO softwareStrategyDTO = findSoftwareStrategyById(strategyId);
        List<SoftwareDTO> softwareDTOList = findSoftwareByIdIn(softwareIds);
        rcoSoftwareStrategyDetailDAO.deleteByStrategyIdAndRelatedIdIn(strategyId, softwareIds);

        List<String> joinSoftwareNameList = joinSoftwareName(softwareDTOList);
        joinSoftwareNameList.forEach((joinSoftwareName) -> {
            auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_REMOVE,
                    joinSoftwareName, softwareStrategyDTO.getName());
        });
        softwareStrategyNotifyAPI.updateSoftwareStrategyNotifyDesk(strategyId);
    }

    @Override
    public List<SoftwareGroupDTO> findAllSoftwareGroup() throws BusinessException {
        List<RcoSoftwareGroupEntity> softwareGroupEntityList = rcoSoftwareGroupDAO.findAll();
        List<SoftwareGroupDTO> softwareGroupDTOList = new ArrayList<>();
        for (RcoSoftwareGroupEntity groupEntity : softwareGroupEntityList) {
            SoftwareGroupDTO softwareGroupDTO = new SoftwareGroupDTO();
            softwareGroupDTO.setId(groupEntity.getId());
            softwareGroupDTO.setName(groupEntity.getName());
            softwareGroupDTO.setGroupType(groupEntity.getGroupType());
            softwareGroupDTOList.add(softwareGroupDTO);
        }
        return softwareGroupDTOList;
    }

    @Override
    public List<SoftwareDTO> findAllSoftwareForWeb() throws BusinessException {
        List<RcoSoftwareEntity> softwareEntityList = findAllSoftwareInside();
        List<SoftwareDTO> softwareDTOList = new ArrayList<>();
        for (RcoSoftwareEntity softwareEntity : softwareEntityList) {
            if (softwareEntity.getParentId() == null) {
                SoftwareDTO softwareDTO = new SoftwareDTO();
                BeanUtils.copyProperties(softwareEntity, softwareDTO);
                softwareDTOList.add(softwareDTO);
            }
        }
        return softwareDTOList;
    }

    @Override
    public List<SoftwareDTO> findAllSoftwareForGT() throws BusinessException {
        List<RcoSoftwareEntity> softwareEntityList = findAllSoftwareInside();
        List<SoftwareDTO> softwareDTOList = new ArrayList<>();
        for (RcoSoftwareEntity softwareEntity : softwareEntityList) {
            if (!softwareEntity.getDirectoryFlag()) {
                SoftwareDTO softwareDTO = new SoftwareDTO();
                BeanUtils.copyProperties(softwareEntity, softwareDTO);
                softwareDTOList.add(softwareDTO);
            }
        }
        return softwareDTOList;
    }

    /**
     * @return
     */
    private List<RcoSoftwareEntity> findAllSoftwareInside() {
        int page = 0;
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        Page<RcoSoftwareEntity> softwareEntityPage = rcoSoftwareDAO.findAll(pageable);
        List<RcoSoftwareEntity> softwareEntityList = new ArrayList<>(softwareEntityPage.getTotalPages());
        while (!softwareEntityPage.getContent().isEmpty()) {
            softwareEntityList.addAll(softwareEntityPage.getContent());
            page++;
            pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
            softwareEntityPage = rcoSoftwareDAO.findAll(pageable);
        }

        return softwareEntityList;
    }

    private RcoSoftwareGroupEntity findSoftwareGroupEntity(UUID id) throws BusinessException {
        Optional<RcoSoftwareGroupEntity> entityOptional = rcoSoftwareGroupDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_NOT_EXIST, id);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_NOT_EXIST, id.toString());
        }

        return entityOptional.get();
    }

    private RcoSoftwareEntity findSoftwareEntity(UUID id) throws BusinessException {
        Optional<RcoSoftwareEntity> entityOptional = rcoSoftwareDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_NOT_EXIST, id);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_NOT_EXIST, id.toString());
        }

        return entityOptional.get();
    }

    private RcoSoftwareStrategyEntity findSoftwareStrategyEntity(UUID id) throws BusinessException {
        Optional<RcoSoftwareStrategyEntity> entityOptional = rcoSoftwareStrategyDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_NOT_EXIST, id);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_NOT_EXIST, id.toString());
        }

        return entityOptional.get();
    }

    private ViewRcoSoftwareStrategyCountEntity findSoftwareStrategyCountEntity(UUID id) throws BusinessException {
        Optional<ViewRcoSoftwareStrategyCountEntity> entityOptional = viewRcoSoftwareStrategyCountDAO.findById(id);
        if (!entityOptional.isPresent()) {
            LOGGER.error(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_NOT_EXIST, id);
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_STRATEGY_NOT_EXIST, id.toString());
        }

        return entityOptional.get();
    }

    @Override
    public UUID getSoftwareGroupIdIfNotExistCreate(SoftwareGroupDTO softwareGroupDTO) {
        Assert.notNull(softwareGroupDTO, "softwareGroupDTO 不能为空");
        return softwareControlServiceTx.getSoftwareGroupIdIfNotExistCreate(softwareGroupDTO);
    }

    @Override
    public DefaultPageResponse<SoftRelatedSoftStrategyDTO> pageQuery(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "Param [request] must not be null");

        Page<ViewSoftRelatedSoftStrategyEntity> page = softStrategyRelatedSoftwareService.pageQuery(request);
        if (page.getTotalElements() == 0) {
            DefaultPageResponse<SoftRelatedSoftStrategyDTO> response = new DefaultPageResponse<>();
            response.setTotal(0);
            response.setItemArr(new SoftRelatedSoftStrategyDTO[0]);
            return response;
        }

        DefaultPageResponse<SoftRelatedSoftStrategyDTO> resp = new DefaultPageResponse<>();
        List<ViewSoftRelatedSoftStrategyEntity> viewList = page.getContent();
        List<SoftRelatedSoftStrategyDTO> resultList = new ArrayList<>();
        for (ViewSoftRelatedSoftStrategyEntity software : viewList) {
            SoftRelatedSoftStrategyDTO softwareDTO = new SoftRelatedSoftStrategyDTO();
            BeanUtils.copyProperties(software, softwareDTO);
            resultList.add(softwareDTO);
        }
        SoftRelatedSoftStrategyDTO[] softDtoArr = resultList.toArray(new SoftRelatedSoftStrategyDTO[resultList.size()]);
        resp.setItemArr(softDtoArr);
        resp.setTotal(page.getTotalElements());

        return resp;
    }

    @Override
    public DefaultPageResponse<SoftRelatedSoftStrategyRelatedDTO> pageQueryForRelated(PageSearchRequest request, @Nullable UUID softwareId)
            throws BusinessException {

        Assert.notNull(request, "Param [request] must not be null");
        Page<ViewRcoSoftwareStrategyCountEntity> softwareStrategyCountEntityPage = softwareStrategyViewService.pageQuery(request);

        DefaultPageResponse<SoftRelatedSoftStrategyRelatedDTO> response = new DefaultPageResponse<>();
        response.setTotal(softwareStrategyCountEntityPage.getTotalElements());
        response.setItemArr(new SoftRelatedSoftStrategyRelatedDTO[0]);

        if (CollectionUtils.isNotEmpty(softwareStrategyCountEntityPage.getContent())) {

            Map<UUID, RcoSoftwareStrategyDetailEntity> rcoSoftwareStrategyDetailMap = new HashMap<>();
            if (softwareId != null) {
                List<RcoSoftwareStrategyDetailEntity> softwareStrategyDetailEntityList =
                        rcoSoftwareStrategyDetailDAO.findByRelatedIdAndRelatedType(softwareId, SoftwareStrategyRelatedTypeEnum.SOFTWARE);
                rcoSoftwareStrategyDetailMap = softwareStrategyDetailEntityList.stream().
                                collect(Collectors.toMap(RcoSoftwareStrategyDetailEntity::getStrategyId, value -> value));
            }
            List<SoftRelatedSoftStrategyRelatedDTO> resultList = new ArrayList<>();
            for (ViewRcoSoftwareStrategyCountEntity entity : softwareStrategyCountEntityPage.getContent()) {
                SoftRelatedSoftStrategyRelatedDTO softRelatedSoftStrategyRelatedDTO = new SoftRelatedSoftStrategyRelatedDTO();
                BeanUtils.copyProperties(entity, softRelatedSoftStrategyRelatedDTO);
                softRelatedSoftStrategyRelatedDTO.
                        setRelated(rcoSoftwareStrategyDetailMap.get(entity.getId()) == null ? false : true);
                resultList.add(softRelatedSoftStrategyRelatedDTO);
            }
            response.setItemArr(resultList.toArray(new SoftRelatedSoftStrategyRelatedDTO[0]));
        }

        return response;
    }

    /**
     * 根据软件分组ids返回分组列表
     *
     * @param groupIdList
     * @return 分组列表
     */
    @Override
    public List<SoftwareGroupDTO> findSoftwareGroupByIdIn(List<UUID> groupIdList) {
        Assert.notNull(groupIdList, "Param [groupIdList] must not be null");
        List<SoftwareGroupDTO> softwareGroupDTOList = new ArrayList<>();
        List<RcoSoftwareGroupEntity> entityList = rcoSoftwareGroupDAO.findByIdIn(groupIdList);
        entityList.forEach((entity) -> {
            SoftwareGroupDTO softwareGroupDTO = new SoftwareGroupDTO();
            BeanUtils.copyProperties(entity, softwareGroupDTO);
            softwareGroupDTOList.add(softwareGroupDTO);
        });

        return softwareGroupDTOList;
    }

    /**
     * 通过软件策略Id查找软件策略
     *
     * @param softwareStrategyIdList 软件策略Id
     * @return 软件策略对象
     * @throws BusinessException 异常抛出
     */
    @Override
    public List<SoftwareStrategyDTO> findSoftwareStrategyByIdIn(Iterable<UUID> softwareStrategyIdList) {
        Assert.notNull(softwareStrategyIdList, "Param [softwareStrategyIdList] must not be null");
        List<SoftwareStrategyDTO> softwareStrategyDTOList = new ArrayList<>();
        List<RcoSoftwareStrategyEntity> entityList = rcoSoftwareStrategyDAO.findByIdIn(softwareStrategyIdList);
        entityList.forEach((entity) -> {
            SoftwareStrategyDTO softwareStrategyDTO = new SoftwareStrategyDTO();
            BeanUtils.copyProperties(entity, softwareStrategyDTO);
            softwareStrategyDTOList.add(softwareStrategyDTO);
        });

        return softwareStrategyDTOList;
    }

    /**
     * 查询软件分页列表
     *
     * @param request search request
     * @return view page
     */
    @Override
    public DefaultPageResponse<SoftwareDTO> softwarePageQuery(PageSearchRequest request) {
        Assert.notNull(request, "Param [request] must not be null");
        Page<RcoSoftwareEntity> page = softwareService.pageQuery(request);
        DefaultPageResponse<SoftwareDTO> resp = new DefaultPageResponse<>();
        resp.setTotal(page.getTotalElements());

        List<SoftwareDTO> softwareDTOList = new ArrayList<>();
        page.get().forEach(entity -> {
            SoftwareDTO dto = new SoftwareDTO();
            BeanUtils.copyProperties(entity, dto);
            softwareDTOList.add(dto);
        });
        resp.setItemArr(softwareDTOList.toArray(new SoftwareDTO[0]));
        return resp;
    }

    /**
     * @param request search request
     * @return view page
     */
    @Override
    public DefaultPageResponse<SoftwareStrategyRelatedSoftwareDTO> softwareStrategyRelatedSoftwarePageQuery(PageSearchRequest request) {
        Assert.notNull(request, "Param [request] must not be null");
        Page<ViewRcoSoftwareStrategyRelatedSoftwareEntity> page = softwareStrategyRelatedSoftwareViewService.pageQuery(request);
        DefaultPageResponse<SoftwareStrategyRelatedSoftwareDTO> resp = new DefaultPageResponse<>();
        resp.setTotal(page.getTotalElements());

        List<SoftwareStrategyRelatedSoftwareDTO> softwareDTOList = new ArrayList<>();
        page.get().forEach(entity -> {
            SoftwareStrategyRelatedSoftwareDTO dto = new SoftwareStrategyRelatedSoftwareDTO();
            BeanUtils.copyProperties(entity, dto);
            softwareDTOList.add(dto);
        });
        resp.setItemArr(softwareDTOList.toArray(new SoftwareStrategyRelatedSoftwareDTO[0]));
        return resp;
    }

    /**
     * 获取软件白名单全局总开关
     *
     * @return
     */
    @Override
    public Boolean getGlobalSoftwareStrategy() {
        FindParameterRequest request = new FindParameterRequest(RcoGlobalParameterEnum.ENABLE_SOFTWARE_STRATEGY.name());
        FindParameterResponse findParameterResponse = rcoGlobalParameterAPI.findParameter(request);
        return BooleanUtils.toBoolean(findParameterResponse.getValue());
    }

    @Override
    public long count() {
        return rcoSoftwareDAO.countByParentIdIsNull();
    }

    /**
     * 合并软件列表的文件名称 目前合并10个
     *
     * @param softwareDTOList
     * @return
     */
    private List<String> joinSoftwareName(List<SoftwareDTO> softwareDTOList) {
        List<String> joinNameList = new ArrayList<>();
        List<List<SoftwareDTO>> subSoftwareDTOList = ListRequestHelper.subList(softwareDTOList, Constants.SOFTWARE_NAME_MAX_SIZE);
        subSoftwareDTOList.forEach((list) -> {
            StringBuilder sb = new StringBuilder();
            list.forEach((element) -> {
                sb.append(element.getName()).append(Constants.COMMA_SEPARATION_CHARACTER);
            });
            joinNameList.add(sb.substring(0, sb.length() - 1));
        });

        return joinNameList;
    }

}
