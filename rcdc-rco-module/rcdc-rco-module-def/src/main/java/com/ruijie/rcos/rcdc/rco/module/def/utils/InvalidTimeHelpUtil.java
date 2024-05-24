package com.ruijie.rcos.rcdc.rco.module.def.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/30 18:03
 *
 * @author liuwang1
 */
@Service
public class InvalidTimeHelpUtil {


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
        //优先按失效恢复时间判断
        if (ObjectUtils.isNotEmpty(invalidRecoverTime)) {
            return obtainIsInvalid(invalidRecoverTime.getTime(), invalidTime);
            //其次按退出登录时间判断
        } else if (ObjectUtils.isNotEmpty(loginOutTime)) {
            return obtainIsInvalid(loginOutTime.getTime(), invalidTime);
            //按用户创建时间判断
        } else {
            return obtainIsInvalid(createTime.getTime(), invalidTime);
        }

    }

    /**
     * 获取账户是否失效
     *
     * @param dto 用户dto
     * @return 是否失效
     * @throws BusinessException 业务异常
     */
    public Boolean convertAccountInvalid(UserInfoDTO dto) throws BusinessException {
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
            return this.setInvalidDescription(invalidRecoverTime.getTime(), invalidTime);
            //其次按退出登录时间判断
        } else if (ObjectUtils.isNotEmpty(loginOutTime)) {
            return this.setInvalidDescription(loginOutTime.getTime(), invalidTime);
            //按用户创建时间判断
        } else {
            return this.setInvalidDescription(createTime.getTime(), invalidTime);
        }

    }

    private   String setInvalidDescription(Long lastLoginTime, Integer invalidTime) {
        Assert.notNull(lastLoginTime, "lastLoginTime is not null");
        Assert.notNull(invalidTime, "invalidTime is not null");

        long timeGapValue = new Date().getTime() - lastLoginTime - invalidTime * TIME_CONVERSION_UNIT;
        Long invalidDay;
        if (timeGapValue < EXPIRE_DATE_ZERO) {
            invalidDay = Math.abs(timeGapValue) / TIME_CONVERSION_UNIT;
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_REMAIN_INVALID_DAY, invalidDay.toString());
        } else {
            invalidDay = timeGapValue / TIME_CONVERSION_UNIT;
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ALREADY_INVALID, invalidDay.toString());
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
            accountExpireDate = this.adDomainTimestampToDate(dto.getAccountExpireDate());
        } else {
            accountExpireDate = new Date(dto.getAccountExpireDate());
        }
        return new SimpleDateFormat(DATE_FORMAT_TIME).format(accountExpireDate);
    }

    /**
     * 获取用户过期时间
     *
     * @param expireDate 过期时间
     * @param userType   用户类型
     * @return 过期时间
     */
    public Long expireDateFormat(Long expireDate, IacUserTypeEnum userType) {
        Assert.notNull(expireDate, "expireDate is not null");
        Assert.notNull(userType, "userType is not null");

        if (expireDate == null || expireDate == 0L) {
            return 0L;
        }

        Date accountExpireDate;
        if (userType == IacUserTypeEnum.AD) {
            accountExpireDate = this.adDomainTimestampToDate(expireDate);
        } else {
            accountExpireDate = new Date(expireDate);
        }
        return accountExpireDate.getTime();
    }
    
    /**
     * 获取用户的过期时间
     * @param dto 用户对象
     * @return 过期时间的时间搓
     */
    public long dealExpireDate(IacUserDetailDTO dto) {
        Assert.notNull(dto, "IacUserDetailDTO is not null");
        
        if (dto.getAccountExpireDate() == null) {
            return 0;
        }
        if (IacUserTypeEnum.AD.equals(dto.getUserType())) {
            return adDomainTimestampToDate(dto.getAccountExpireDate()).getTime();
        } else {
            return dto.getAccountExpireDate().longValue();
        }
    }

    private Date adDomainTimestampToDate(Long timestamp) {
        Assert.notNull(timestamp, "timestamp cannot be null!");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1601, Calendar.JANUARY, 1, 0, 0);
        timestamp = timestamp + calendar.getTime().getTime();
        return new Date(timestamp);
    }
}
