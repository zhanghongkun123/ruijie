package com.ruijie.rcos.rcdc.rco.module.impl.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/30 14:00
 *
 * @author liuwang1
 */
@Service
public class RcoInvalidTimeHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoInvalidTimeHelper.class);

    private static final Long TIME_CONVERSION_UNIT = 24 * 60 * 60 * 1000L;

    public static final long EXPIRE_DATE_ZERO = 0L;

    private static final String DATE_FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";

    private static final String DAY_OF_FORMAT = "yyyy-MM-dd";

    private static final Long SECOND_TO_MILLISECOND = 1000L;


    /**
     * 获取账户是否失效
     *
     * @param dto 用户dto
     * @return 是否失效
     * @throws BusinessException 业务异常
     */
    public Boolean isAccountInvalid(IacUserDetailDTO dto) throws BusinessException {
        Assert.notNull(dto, "dto is not null");

        Integer invalidTime = dto.getInvalidTime();
        //为0表示永不失效
        if (invalidTime == null || invalidTime == 0) {
            return false;
        }
        Date loginOutTime = dto.getLoginOutTime();
        Date createTime = dto.getCreateTime();
        Date invalidRecoverTime = dto.getInvalidRecoverTime();
        Date calDate = null;
        if (ObjectUtils.isNotEmpty(dto.getLastLoginTerminalTime())) {
            calDate = dto.getLastLoginTerminalTime();
        }
        if (ObjectUtils.isNotEmpty(loginOutTime)) {
            calDate = loginOutTime;
        }
        // 登录时间和登出时间都存在拿最新时间来做判断
        if (ObjectUtils.isNotEmpty(loginOutTime) && ObjectUtils.isNotEmpty(dto.getLastLoginTerminalTime())) {
            calDate = dto.getLastLoginTerminalTime().before(loginOutTime) ? loginOutTime : dto.getLastLoginTerminalTime();
        }
        //优先按失效恢复时间判断
        if (ObjectUtils.isNotEmpty(invalidRecoverTime)) {
            return obtainIsInvalid(invalidRecoverTime.getTime(), invalidTime);
            //其次按退出登录时间判断
        } else if (calDate != null) {
            return obtainIsInvalid(calDate.getTime(), invalidTime);
            //按用户创建时间判断
        } else {
            return obtainIsInvalid(createTime.getTime(), invalidTime);
        }

    }

    private Boolean obtainIsInvalid(Long loginOutTime, Integer invalidTime) throws BusinessException {
        Date loginOutDate = new Date(loginOutTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DAY_OF_FORMAT);
        String format = simpleDateFormat.format(loginOutDate);
        try {
            Date parse = simpleDateFormat.parse(format);
            long formatTime = parse.getTime();
            //跟当天的23:59:59比较
            if (formatTime + (invalidTime + 1) * TIME_CONVERSION_UNIT - 1 * SECOND_TO_MILLISECOND < new Date().getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DATE_FORMAT_VALIDATE_ERROR, e);
        }
    }

    /**
     * 处理失效描述
     *
     * @param dto 用户详情dto
     * @return 失效描述
     */
    public String obtainInvalidDescription(IacUserDetailDTO dto) {
        Assert.notNull(dto, "dto is not null");

        Integer invalidTime = dto.getInvalidTime();
        if (invalidTime == null || invalidTime == 0) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_FOREVER_INVALID);
        }
        Date loginOutTime = dto.getLoginOutTime();
        Date createTime = dto.getCreateTime();
        Date invalidRecoverTime = dto.getInvalidRecoverTime();
        //优先按失效恢复时间判断
        if (ObjectUtils.isNotEmpty(invalidRecoverTime)) {
            return AccountLastLoginUtil.setInvalidDescription(invalidRecoverTime.getTime(), invalidTime);
            //其次按退出登录时间判断
        } else if (ObjectUtils.isNotEmpty(loginOutTime)) {
            return AccountLastLoginUtil.setInvalidDescription(loginOutTime.getTime(), invalidTime);
            //按用户创建时间判断
        } else {
            return AccountLastLoginUtil.setInvalidDescription(createTime.getTime(), invalidTime);
        }

    }

    /**
     * 处理过期描述
     *
     * @param dto 用户详情dto
     * @return 过期时间描述
     */
    public String expireDateFormat(IacUserDetailDTO dto) {
        Assert.notNull(dto, "dto is not null");
        if (Objects.isNull(dto.getAccountExpireDate())) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE);
        }
        if (dto.getAccountExpireDate() == EXPIRE_DATE_ZERO) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE);
        }
        Date accountExpireDate;
        if (dto.getUserType() == IacUserTypeEnum.AD) {
            accountExpireDate = DateUtil.adDomainTimestampToDate(dto.getAccountExpireDate());
        } else {
            accountExpireDate = new Date(dto.getAccountExpireDate());
        }
        return new SimpleDateFormat(DATE_FORMAT_TIME).format(accountExpireDate);
    }

    /**
     *  根据用户类型转换时间的格式为标准yyyy-MM-dd HH:mm:ss
     * @param userType 用户类型
     * @param accountExpireDate 过期时间ms
     * @return 标准yyyy-MM-dd HH:mm:ss
     */
    public String expireDateFormat(IacUserTypeEnum userType,@Nullable Long accountExpireDate) {
        Assert.notNull(userType,"userType is not null");
        if (Objects.isNull(accountExpireDate)) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE);
        }
        if (accountExpireDate == EXPIRE_DATE_ZERO) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE);
        }

        Date dateAccountExpireDate;
        if (userType == IacUserTypeEnum.AD) {
            dateAccountExpireDate = DateUtil.adDomainTimestampToDate(accountExpireDate);
        } else {
            dateAccountExpireDate = new Date(accountExpireDate);
        }
        return new SimpleDateFormat(DATE_FORMAT_TIME).format(dateAccountExpireDate);
    }

    /**
     * 处理过期描述
     *
     * @param viewUserEntity 用户视图
     * @return 过期时间描述
     */
    public String expireDateFormat(RcoViewUserEntity viewUserEntity) {
        Assert.notNull(viewUserEntity, "dto is not null");
        if (Objects.isNull(viewUserEntity.getAccountExpireDate())) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE);
        }
        if (viewUserEntity.getAccountExpireDate() == EXPIRE_DATE_ZERO) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE);
        }
        Date accountExpireDate;
        if (viewUserEntity.getUserType() == IacUserTypeEnum.AD) {
            accountExpireDate = DateUtil.adDomainTimestampToDate(viewUserEntity.getAccountExpireDate());
        } else {
            accountExpireDate = new Date(viewUserEntity.getAccountExpireDate());
        }
        return new SimpleDateFormat(DATE_FORMAT_TIME).format(accountExpireDate);
    }

    /**
     * 获取登录状态码
     *
     * @param authCode 登录码
     * @param userDetail 用户信息
     * @return 登录码
     * @throws BusinessException 业务异常
     */
    public Integer obtainLoginStateCode(Integer authCode, @Nullable IacUserDetailDTO userDetail) throws BusinessException {
        Assert.notNull(authCode, "authCode is not null");

        //统一判断用户账号是否失效和过期
        if (CommonMessageCode.SUCCESS == authCode) { //返回成功说明前面对账号的验证都通过，否则直接跳过不用再验证是否失效和过期

            //判断账号是否失效
            Integer accountExpireCode = checkAccountExpireDate(authCode, userDetail);
            if (accountExpireCode != null && CommonMessageCode.SUCCESS != accountExpireCode) {
                return accountExpireCode;
            } else if (Boolean.TRUE.equals(isAccountInvalid(userDetail))) {
                LOGGER.info("用户[{}]账号已失效", userDetail.getUserName());
                return LoginMessageCode.ACCOUNT_INVALID;
            }
        }
        return authCode;
    }

    private Integer checkAccountExpireDate(Integer authCode, IacUserDetailDTO userDetail) {
        //处理过期时间
        if (userDetail.getAccountExpireDate() == null || userDetail.getAccountExpireDate() == 0L) {
            return authCode;
        }
        // 如果是AD域用户，认证成功，直接返回结果
        if (IacUserTypeEnum.AD.equals(userDetail.getUserType())) {
            return authCode;
        }

        Date accountExpireDate = dealExpireDate(userDetail);
        //判断账号是否过期
        if (accountExpireDate.getTime() < new Date().getTime()) {
            LOGGER.info("用户[{}]账号已过期", userDetail.getUserName());
            return LoginMessageCode.AD_ACCOUNT_EXPIRE;
        }
        //正常时返回null
        return null;
    }

    private Date dealExpireDate(IacUserDetailDTO dto) {

        if (IacUserTypeEnum.AD.equals(dto.getUserType())) {
            return DateUtil.adDomainTimestampToDate(dto.getAccountExpireDate());
        } else {
            return new Date(dto.getAccountExpireDate());
        }
    }
}
