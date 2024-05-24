package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.rcdc.rco.module.impl.common.AccountLastLoginUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.common.RcoInvalidTimeHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryAppGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.UserDesktopCountInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.CloudDesktopViewServiceImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 用户查询协助类把类型转换等公共方法提取进来
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/07/08
 *
 * @author linke
 */
@Service
public class UserQueryHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserQueryHelper.class);

    /**
     * 分钟转换成毫秒
     */
    private static final int ONE_MINUTE_MILLIS = 60 * 1000;

    private static final Long TIME_CONVERSION_UNIT = 24 * 60 * 60 * 1000L;

    /**
     * 用户永久锁定时的默认值
     */
    private static final int PERMANENT_LOCK = -1;

    private static final String DATE_FORMAT_DAY = "yyyy-MM-dd";

    private static final String DATE_FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 过期时间 0
     */
    public static final long EXPIRE_DATE_ZERO = 0L;

    public static final String ID = "id";

    public static final String GROUP_ID = "groupId";

    public static final String RELATED_ID = "relatedId";

    public static final String DESKTOP_POOL_ID = "desktopPoolId";

    public static final String DISK_POOL_ID = "diskPoolId";

    public static final String RELATED_TYPE = "relatedType";

    private static final Integer INVALID_FLAG = 0;

    public static final String APP_GROUP_MEMBER_ID = "memberId";

    public static final String APP_GROUP_MEMBER_TYPE = "memberType";

    @Autowired
    private CloudDesktopViewServiceImpl cloudDesktopViewService;

    @Autowired
    private UserRecycleBinMgmtAPI userRecycleBinMgmtAPI;

    @Autowired
    private CertificationStrategyService certificationStrategyService;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private RcoInvalidTimeHelper invalidTimeUtil;

    /**
     * RcoViewUserEntity类转换成UserListDTO
     *
     * @param entityList List<RcoViewUserEntity> entityList
     * @return UserListDTO[]
     */
    public UserListDTO[] convertRcoViewUserEntity2UserListDTO(List<RcoViewUserEntity> entityList) {
        Assert.notNull(entityList, "entityList can not be null");
        UserListDTO[] dtoArr = new UserListDTO[entityList.size()];
        if (CollectionUtils.isEmpty(entityList)) {
            return dtoArr;
        }
        // 计算获取解锁时间，公式当前时间减去配置锁定时间
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyService.getPwdStrategyParameter();
        Date realUnLockTime = new Date(System.currentTimeMillis() - (long) ONE_MINUTE_MILLIS * pwdStrategyDTO.getUserLockTime());

        List<UUID> userIdList = entityList.stream().map(RcoViewUserEntity::getId).collect(Collectors.toList());
        UserDesktopCountInfo userDesktopCountInfo = getUserDesktopCountInfo(entityList);
        List<CloudDesktopDTO> recycleUserList = userRecycleBinMgmtAPI.getAllDesktopByUserIdList(userIdList);
        Map<UUID, List<CloudDesktopDTO>> userIdToDesktopMap = recycleUserList.stream().collect(Collectors.groupingBy(CloudDesktopDTO::getUserId));

        List<CloudDesktopDTO> emptyList = Lists.newLinkedList();
        for (int i = 0; i < entityList.size(); i++) {
            RcoViewUserEntity entity = entityList.get(i);
            UserListDTO dto = new UserListDTO();
            BeanUtils.copyProperties(entity, dto);
            dto.setUserState(entity.getState());
            dto.setCanDelete(userDesktopCountInfo.isUserCanDelete(entity.getId()));
            boolean hasRecycleBin = CollectionUtils.isNotEmpty(userIdToDesktopMap.getOrDefault(dto.getId(), emptyList));
            dto.setHasRecycleBin(hasRecycleBin);
            dealAccountExpireDate(entity, dto);
            boolean isLock = checkUserIsLock(pwdStrategyDTO, dto, realUnLockTime);
            dto.setLock(isLock);
            dto.setUserDescription(entity.getUserDescription());
            if (ObjectUtils.isEmpty(entity.getInvalidTime())) {
                dto.setInvalidTime(INVALID_FLAG);
            }
            //以数据库为准
            dto.setInvalid(entity.getInvalid());

            if (ObjectUtils.isNotEmpty(entity.getId())) {
                // FIXME:songxiang 感觉这个字段没有什么意义，因为前面有一个dealAccountExpireDate() accountExpireDateStr跟他是一样的处理
                String expireDateFormat = invalidTimeUtil.expireDateFormat(entity.getUserType(), entity.getAccountExpireDate());
                dto.setAccountExpireDate(expireDateFormat);
            }
            dtoArr[i] = dto;
        }
        return dtoArr;
    }

    private void dealAccountExpireDate(RcoViewUserEntity entity, UserListDTO dto) {
        if (Objects.isNull(entity.getAccountExpireDate())) {
            return;
        }
        if (entity.getAccountExpireDate() == EXPIRE_DATE_ZERO) {
            dto.setAccountExpireDateStr(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE));
            return;
        }
        Date accountExpireDate;
        if (entity.getUserType() == IacUserTypeEnum.AD) {
            accountExpireDate = DateUtil.adDomainTimestampToDate(entity.getAccountExpireDate());
        } else {
            accountExpireDate = new Date(entity.getAccountExpireDate());
        }
        dto.setAccountExpireDateStr(new SimpleDateFormat(DATE_FORMAT_TIME).format(accountExpireDate));
    }

    /**
     * 判断用户是否上锁
     *
     * @param pwdStrategyDTO pwdStrategyDTO
     * @param userListDTO    userListDTO
     * @param realUnLockTime realUnLockTime
     * @return boolean
     */
    private boolean checkUserIsLock(PwdStrategyDTO pwdStrategyDTO, UserListDTO userListDTO, Date realUnLockTime) {
        // 如果没有开启防爆功能，则用户都处于未锁定状态
        if (Boolean.FALSE.equals(pwdStrategyDTO.getPreventsBruteForce())) {
            return false;
        }

        boolean isLock = Optional.ofNullable(userListDTO.getLock()).orElse(false);
        // 1.基于防暴设置位true,锁定时长不等于null判断是否上锁
        // 2.基于lockTime重新判断isLock字段
        if (Boolean.TRUE.equals(pwdStrategyDTO.getPreventsBruteForce()) && Objects.nonNull(userListDTO.getLockTime())) {
            if (pwdStrategyDTO.getUserLockTime() != PERMANENT_LOCK) {
                // 指定时长锁定
                isLock = userListDTO.getLockTime().after(realUnLockTime);
            } else {
                // 永久锁定
                isLock = true;
            }
        }
        return isLock;
    }

    private UserDesktopCountInfo getUserDesktopCountInfo(List<RcoViewUserEntity> entityList) {
        List<UUID> userIdList = entityList.stream().map(RcoViewUserEntity::getId).collect(Collectors.toList());
        return cloudDesktopViewService.countUserDesktopInfo(userIdList);
    }

    /**
     * 根据参数判断是否是查询全部数据
     *
     * @param request PageSearchRequest
     * @return true查全部
     */
    public static boolean isQueryAll(PageSearchRequest request) {
        Assert.notNull(request, "request can not be null");
        if (ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            return true;
        }
        return Stream.of(request.getMatchEqualArr()).noneMatch(matchEqual -> Objects.equals(matchEqual.getName(), GROUP_ID));
    }

    /**
     * 构建查询
     *
     * @param pageQuery   查询参数
     * @param relatedType relatedType
     * @return Subquery 子查询
     */
    public static Subquery<UUID> buildPoolUserSubQuery(PageQueryPoolDTO pageQuery, IacConfigRelatedType relatedType) {
        Assert.notNull(pageQuery, "pageQuery can not be null");
        Assert.notNull(relatedType, "relatedType can not be null");
        // 子查询结果类型UUID
        Subquery<UUID> subQuery = pageQuery.getQuery().subquery(UUID.class);
        // 子查询 查询DesktopPoolUserEntity
        Root<?> subRoot = subQuery.from(pageQuery.getSubTable());
        // 查询的DesktopPoolUserEntity.relatedId
        subQuery.select(subRoot.get(RELATED_ID));
        // 查询条件
        Predicate subPredicate = pageQuery.getBuilder().equal(subRoot.get(pageQuery.getPoolIdColumnName()), pageQuery.getPoolId());
        Predicate subPredicate2 = pageQuery.getBuilder().equal(subRoot.get(RELATED_TYPE), relatedType);
        // 子查询条件
        subQuery.where(subPredicate, subPredicate2);
        return subQuery;
    }

    /**
     * 构建子条件查询：查询不属于指定池ID的用户Id列表
     *
     * @param pageQuery   查询参数
     * @param relatedType relatedType
     * @return Subquery 子查询
     */
    public static Subquery<UUID> buildPoolUserSubQueryNotEqualPoolId(PageQueryPoolDTO pageQuery, IacConfigRelatedType relatedType) {
        Assert.notNull(pageQuery, "pageQuery can not be null");
        Assert.notNull(relatedType, "relatedType can not be null");
        // 子查询结果类型UUID
        Subquery<UUID> subQuery = pageQuery.getQuery().subquery(UUID.class);
        // 子查询 查询DesktopPoolUserEntity
        Root<?> subRoot = subQuery.from(pageQuery.getSubTable());
        // 查询的DesktopPoolUserEntity.relatedId
        subQuery.select(subRoot.get(RELATED_ID));
        // 查询条件
        Predicate subPredicate = pageQuery.getBuilder().notEqual(subRoot.get(pageQuery.getPoolIdColumnName()), pageQuery.getPoolId());
        Predicate subPredicate2 = pageQuery.getBuilder().equal(subRoot.get(RELATED_TYPE), relatedType);
        // 子查询条件
        subQuery.where(subPredicate, subPredicate2);
        return subQuery;
    }

    /**
     * 应用分组构建查询
     *
     * @param pageQuery  查询参数
     * @param memberType memberType
     * @return Subquery 子查询
     */
    public static Subquery<UUID> buildAppGroupUserSubQuery(PageQueryAppGroupDTO pageQuery, RcaEnum.GroupMemberType memberType) {
        Assert.notNull(pageQuery, "pageQuery can not be null");
        Assert.notNull(memberType, "memberType can not be null");
        // 子查询结果类型UUID
        Subquery<UUID> subQuery = pageQuery.getQuery().subquery(UUID.class);
        // 子查询 查询DesktopPoolUserEntity
        Root<?> subRoot = subQuery.from(pageQuery.getSubTable());
        // 查询的DesktopPoolUserEntity.relatedId
        subQuery.select(subRoot.get(APP_GROUP_MEMBER_ID));
        // 查询条件
        Predicate subPredicate = pageQuery.getBuilder().equal(subRoot.get(pageQuery.getAppGroupIdColumnName()), pageQuery.getAppGroupId());
        Predicate subPredicate2 = pageQuery.getBuilder().equal(subRoot.get(APP_GROUP_MEMBER_TYPE), memberType);
        // 子查询条件
        subQuery.where(subPredicate, subPredicate2);
        return subQuery;
    }

    /**
     * 合并数组
     *
     * @param leftArr  leftArr
     * @param rightArr rightArr
     * @return UserListDTO[]
     */
    public static UserListDTO[] mergeUserListDTOArr(UserListDTO[] leftArr, UserListDTO[] rightArr) {
        Assert.notNull(leftArr, "leftArr can not be null");
        Assert.notNull(rightArr, "rightArr can not be null");

        UserListDTO[] finalUserArr = new UserListDTO[leftArr.length + rightArr.length];
        System.arraycopy(leftArr, 0, finalUserArr, 0, leftArr.length);
        System.arraycopy(rightArr, 0, finalUserArr, leftArr.length, rightArr.length);
        return finalUserArr;
    }

    private void validateIsInvalidAndSet(UserListDTO dto, RcoViewUserEntity entity) {

        List<CloudDesktopDTO> dtoList = userDesktopMgmtAPI.findByUserId(entity.getId());
        Date invalidRecoverTime = entity.getInvalidRecoverTime();
        Long lastLoginTime = invalidRecoverTime == null ? AccountLastLoginUtil.offerLastLoginTime(entity.getId(), dtoList) : invalidRecoverTime.getTime();
        //判断最后一次登录时间+失效时间是否超过当前时间
        if (lastLoginTime + entity.getInvalidTime() * TIME_CONVERSION_UNIT < new Date().getTime()) {
            dto.setInvalid(true);
        } else {
            dto.setInvalid(false);
        }
    }

    private String dealInvalidDescription(RcoViewUserEntity entity) {

        Integer invalidTime = entity.getInvalidTime();
        if (ObjectUtils.isEmpty(invalidTime)) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_FOREVER_INVALID);
        }

        Date invalidRecoverTime = entity.getInvalidRecoverTime();
        List<CloudDesktopDTO> dtoList = userDesktopMgmtAPI.findByUserId(entity.getId());
        //恢复失效时间不为空则以恢复时间为准
        if (ObjectUtils.isNotEmpty(invalidRecoverTime)) {
            return AccountLastLoginUtil.setInvalidDescription(invalidRecoverTime.getTime(), invalidTime);
        } else {
            //以最后一次登录时间为准
            Long lastLoginTime = AccountLastLoginUtil.offerLastLoginTime(entity.getId(), dtoList);
            return AccountLastLoginUtil.setInvalidDescription(lastLoginTime, invalidTime);
        }
    }
}
