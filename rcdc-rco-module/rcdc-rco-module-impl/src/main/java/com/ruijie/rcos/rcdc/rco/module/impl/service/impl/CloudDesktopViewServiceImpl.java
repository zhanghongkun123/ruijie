package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CountCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopStateNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopUserSessionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.entity.DesktopTempPermissionRelationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.UserDesktopCountInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopUserSessionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopViewService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.specification.DesktopPageQuerySpecification;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * desktop view service
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time:
 *
 * @author artom
 */
@Service
public class CloudDesktopViewServiceImpl extends AbstractPageQueryTemplate<ViewUserDesktopEntity> implements CloudDesktopViewService {

    private final static String PATTERN = "pattern";

    private final static String PERSON_SIZE = "personSize";

    private final static String IMAGE_USAGE = "imageUsage";

    private final static String USER_ID = "userId";

    private final static String DESKTOP_ID = "desktopId";

    private final static String CBB_DESKTOP_ID = "cbbDesktopId";

    /**
     * 定时任务快照和备份的code
     */
    private final static Set<String> SCHEDULE_TYPE_CODE_SET = Sets.newHashSet(ScheduleTypeCodeConstants.CLOUD_DESK_CREATE_SNAPSHOT_TYPR_CODE,
            ScheduleTypeCodeConstants.CLOUD_DESK_CREATE_BACKUP_TYPR_CODE);

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private DesktopUserSessionDAO desktopUserSessionDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("ip", "desktopName", "terminalIp", "userName", "imageTemplateName", "physicalServerIp", "remark", "computerName",
                "terminalName", "userDescription", "realName", "platformName");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("latestLoginTime", Direction.DESC);
    }

    @Override
    protected Page<ViewUserDesktopEntity> find(Specification<ViewUserDesktopEntity> specification, Pageable pageable) {
        return viewDesktopDetailDAO.findAll(specification, pageable);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        entityFieldMapper.mapping("desktopIp", "ip");
    }

    @Override
    public Page<ViewUserDesktopEntity> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "request is null");

        if (request instanceof DeskPageSearchRequest) {
            DeskPageSearchRequest searchRequest = (DeskPageSearchRequest) request;
            DesktopPageQuerySpecification specification = new DesktopPageQuerySpecification(request.getSearchKeyword(), getSearchColumn(),
                    request.getMatchEqualArr(), request.getIsAnd());
            specification.setScheduleTypeCode(searchRequest.getScheduleTypeCode());
            PageQuerySpecification<ViewUserDesktopEntity> pageQuerySpecification = buildSpecification(searchRequest);
            pageQuerySpecification.setEntityClass(ViewUserDesktopEntity.class);
            Page<ViewUserDesktopEntity> page = viewDesktopDetailDAO.findAll(pageQuerySpecification, buildPageable(request));
            fillUserInfoForQueryResult(searchRequest, page);
            return page;
        }

        return super.pageQuery(request, ViewUserDesktopEntity.class);
    }

    private PageQuerySpecification<ViewUserDesktopEntity> buildSpecification(DeskPageSearchRequest searchRequest) {
        return new PageQuerySpecification<ViewUserDesktopEntity>(searchRequest.getSearchKeyword(), getSearchColumn(),
                searchRequest.getMatchEqualArr(), searchRequest.getIsAnd()) {

            @Override
            public Predicate toPredicate(Root<ViewUserDesktopEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Assert.notNull(root, "root is null");
                Assert.notNull(query, "query is null");
                Assert.notNull(cb, "cb is null");

                Predicate predicate = super.toPredicate(root, query, cb);
                Assert.notNull(predicate, "predicate is null");

                String scheduleTypeCode = searchRequest.getScheduleTypeCode();
                if (StringUtils.isNotEmpty(scheduleTypeCode) && SCHEDULE_TYPE_CODE_SET.contains(scheduleTypeCode)) {
                    Expression<String> patternExpression = root.get(PATTERN);
                    Expression<Integer> personSizeExpression = root.get(PERSON_SIZE);
                    // 添加默认过滤条件: where后 and (pattern != 'RECOVERABLE' or (pattern = 'RECOVERABLE' and person_size > 0))
                    Predicate patternPredicate = cb.equal(patternExpression, CbbCloudDeskPattern.RECOVERABLE.name());
                    Predicate personSizePredicate = cb.gt(personSizeExpression, 0);
                    Predicate orPatternPersonSizePredicate = cb.and(patternPredicate, personSizePredicate);
                    predicate.getExpressions().add(cb.or(patternPredicate.not(), orPatternPersonSizePredicate));
                }
                if (Boolean.FALSE.equals(searchRequest.getHasAllPermission()) && searchRequest.getAdminId() != null) {
                    Subquery<AdminDataPermissionEntity> subQuery = query.subquery(AdminDataPermissionEntity.class);
                    Root<AdminDataPermissionEntity> subRoot = subQuery.from(AdminDataPermissionEntity.class);
                    subQuery.select(subRoot);

                    Predicate disjunction = cb.disjunction();
                    disjunction.getExpressions().add(cb.and(cb.equal(root.get(DBConstants.USER_GROUP_ID).as(String.class),
                                    subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                            cb.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.USER_GROUP),
                            cb.equal(subRoot.get(DBConstants.ADMIN_ID), searchRequest.getAdminId())));

                    disjunction.getExpressions().add(cb.and(cb.equal(root.get(DBConstants.TERMINAL_GROUP_ID).as(String.class),
                                    subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                            cb.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.TERMINAL_GROUP),
                            cb.equal(subRoot.get(DBConstants.ADMIN_ID), searchRequest.getAdminId()),
                            cb.equal(root.get(DBConstants.IDV_TERMINAL_MODEL).as(String.class), DBConstants.PUBLIC)));

                    disjunction.getExpressions().add(cb.and(cb.equal(root.get(DBConstants.DESKTOP_POOL_ID).as(String.class),
                                    subRoot.get(DBConstants.PERMISSION_DATA_ID)),
                            cb.equal(subRoot.get(DBConstants.PERMISSION_DATA_TYPE), AdminDataPermissionType.DESKTOP_POOL),
                            cb.equal(subRoot.get(DBConstants.ADMIN_ID), searchRequest.getAdminId())));

                    subQuery.where(disjunction);
                    predicate.getExpressions().add(cb.exists(subQuery));
                }
                if (!CollectionUtils.isEmpty(searchRequest.getUserIdList())) {
                    dealUserIdPredicate(searchRequest.getUserIdList(), root, query, cb, predicate);
                }
                Expression<String> imageUsage = root.get(IMAGE_USAGE);
                // 添加默认过滤条件: where后 and (pattern != 'RECOVERABLE' or (pattern = 'RECOVERABLE' and person_size > 0))
                Predicate patternPredicate = cb.notEqual(imageUsage, ImageUsageTypeEnum.APP);
                Predicate emptyUsagePredicate = cb.isNull(imageUsage);
                Predicate orPatternPersonSizePredicate = cb.or(patternPredicate, emptyUsagePredicate);
                predicate.getExpressions().add(orPatternPersonSizePredicate);
                return predicate;
            }
        };
    }

    private void dealUserIdPredicate(List<UUID> userIdList, Root<ViewUserDesktopEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                     Predicate rootPredicate) {
        Subquery<UUID> hostUserSubQuery = query.subquery(UUID.class);
        // 子查询 查询HostUserEntity
        Root<HostUserEntity> subRoot = hostUserSubQuery.from(HostUserEntity.class);
        // 查询 select HostUserEntity.desktopId from HostUserEntity where userId in (userIdList)
        hostUserSubQuery.select(subRoot.get(DESKTOP_ID));
        Predicate subIdPredicate = subRoot.get(USER_ID).in(userIdList);
        hostUserSubQuery.where(subIdPredicate);

        // userId in  (userIdList)
        Expression<String> userIdExpression = root.get(USER_ID);
        Predicate userIdInPredicate = userIdExpression.in(userIdList);

        // cbbDesktopId in (select HostUserEntity.desktopId from HostUserEntity where userId in (userIdList))
        Expression<String> cbbDesktopIdInExpression = root.get(CBB_DESKTOP_ID);
        Predicate cbbDesktopIdInPredicate = cbbDesktopIdInExpression.in(hostUserSubQuery);
        // (userId in  (userIdList) or cbbDesktopId in (select HostUserEntity.desktopId from HostUserEntity where userId in (userIdList)) )
        rootPredicate.getExpressions().add(cb.or(userIdInPredicate, cbbDesktopIdInPredicate));
    }

    private void fillUserInfoForQueryResult(DeskPageSearchRequest searchRequest, Page<ViewUserDesktopEntity> page) {
        if (CollectionUtils.isEmpty(searchRequest.getUserIdList()) || searchRequest.getUserIdList().size() > 1
                || CollectionUtils.isEmpty(page.getContent())) {
            return;
        }
        List<ViewUserDesktopEntity> desktopEntityList = page.getContent();
        if (desktopEntityList.stream().noneMatch(item -> StringUtils.isBlank(item.getUserName()))) {
            return;
        }
        RcoViewUserEntity userEntity = userService.getUserInfoById(searchRequest.getUserIdList().get(0));
        if (Objects.isNull(userEntity)) {
            return;
        }
        for (ViewUserDesktopEntity desktopEntity : desktopEntityList) {
            if (StringUtils.isNotBlank(desktopEntity.getUserName())) {
                continue;
            }
            desktopEntity.setUserName(userEntity.getUserName());
            desktopEntity.setRealName(userEntity.getRealName());
            desktopEntity.setUserType(userEntity.getUserType().name());
            desktopEntity.setPhoneNum(userEntity.getPhoneNum());
            desktopEntity.setEmail(userEntity.getEmail());
            desktopEntity.setUserGroupName(userEntity.getGroupName());
            desktopEntity.setUserState(userEntity.getState());
            desktopEntity.setUserAccountExpireDate(userEntity.getAccountExpireDate());
            desktopEntity.setUserInvalidRecoverTime(userEntity.getInvalidRecoverTime());
            desktopEntity.setUserInvalidTime(userEntity.getInvalidTime());
            desktopEntity.setUserInvalid(userEntity.getInvalid());
            desktopEntity.setUserCreateTime(userEntity.getCreateTime());
        }
    }

    @Override
    public PageQueryResponse<ViewUserDesktopEntity> pageQuery(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        return viewDesktopDetailDAO.pageQuery(request);
    }

    @Override
    public Page<ViewUserDesktopEntity> pageQueryInDesktopTempPermission(UUID permissionId, PageSearchRequest request) {
        Assert.notNull(permissionId, "permissionId is null");
        Assert.notNull(request, "request is null");

        DeskPageSearchRequest searchRequest = (DeskPageSearchRequest) request;
        DesktopPageQuerySpecification specification = new DesktopPageQuerySpecification(request.getSearchKeyword(), getSearchColumn(),
                request.getMatchEqualArr(), request.getIsAnd());
        specification.setScheduleTypeCode(searchRequest.getScheduleTypeCode());
        specification.setImageUsage(ImageUsageTypeEnum.DESK);
        specification.setDesktopTempPermissionId(permissionId);

        return viewDesktopDetailDAO.findAll(specification, buildPageable(request));
    }

    @Override
    public UserDesktopCountInfo countUserDesktopInfo(List<UUID> userIdList) {
        Assert.notNull(userIdList, "userIdList must no be null");
        List<Object[]> unUseList = viewDesktopDetailDAO.countByUserIdAndIsDelete(userIdList, true);
        UserDesktopCountInfo info = new UserDesktopCountInfo();
        for (Object[] objArr : unUseList) {
            info.putUnUseCount((UUID) objArr[0], ((Long) objArr[1]).intValue());
        }
        List<Object[]> inUseList = viewDesktopDetailDAO.countByUserIdAndIsDelete(userIdList, false);
        for (Object[] objArr : inUseList) {
            info.putInUseCount((UUID) objArr[0], ((Long) objArr[1]).intValue());
        }

        List<UUID> cannotDeleteList = viewDesktopDetailDAO.findCannotDelUserIds(userIdList);
        info.putCannotDelIds(cannotDeleteList);

        List<DesktopUserSessionEntity> sessionEntityList = desktopUserSessionDAO.findByUserIdIn(userIdList);
        List<UUID> sessionUserIdList = sessionEntityList.stream().map(DesktopUserSessionEntity::getUserId).filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(sessionUserIdList)) {
            info.putCannotDelIds(sessionUserIdList);
        }

        return info;
    }

    @Override
    public List<DesktopStateNumDTO> countByDeskState(List<UUID> userGroupUuidList, Boolean isDelete) {
        Assert.notNull(userGroupUuidList, "userGroupUuidList cannot be null.");
        Assert.notNull(isDelete, "isDelete cannot be null.");
        if (CollectionUtils.isEmpty(userGroupUuidList)) {
            // 统计所有
            return viewDesktopDetailDAO.countNumGroupByDesktopState(isDelete);
        }
        return viewDesktopDetailDAO.countNumGroupByDesktopStateIn(userGroupUuidList, isDelete);
    }

    @Override
    public Long findCountByDeskState(CbbCloudDeskState deskState) {
        Assert.notNull(deskState, "deskState cannot be null.");
        return viewDesktopDetailDAO.countByDeskStateAndIsDelete(deskState.toString(), false);
    }

    @Override
    public Long findCount() {
        return viewDesktopDetailDAO.countByIsDelete(false);
    }

    @Override
    public Integer countByCloudDesktop(CountCloudDesktopDTO countCloudDesktopDTO) {
        Assert.notNull(countCloudDesktopDTO, "countCloudDesktopDTO must not be null");
        return viewDesktopDetailDAO.countByDTO(countCloudDesktopDTO);
    }

    @Override
    protected PageQuerySpecification<ViewUserDesktopEntity> buildSpecification(PageSearchRequest request) {
        if (StringUtils.isBlank(request.getSearchKeyword()) && ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            // 允许为空
            return null;
        } else {
            return new DesktopPageQuerySpecification(request.getSearchKeyword(), getSearchColumn(), request.getMatchEqualArr(), request.getIsAnd());
        }
    }

}
