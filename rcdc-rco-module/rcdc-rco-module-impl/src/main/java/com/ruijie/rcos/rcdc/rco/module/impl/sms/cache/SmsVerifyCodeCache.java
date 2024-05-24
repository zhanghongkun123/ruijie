package com.ruijie.rcos.rcdc.rco.module.impl.sms.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsPwdRecoverDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.SmsBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 短信验证码缓存
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/15
 * 
 * @author TD
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SmsVerifyCodeCache {

    /**
     * 授权token缓存器：设置5分钟后过期
     */
    private static final Cache<String, UUID> AUTH_TOKEN_CACHE =
            CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).maximumSize(10000).build();

    /**
     * 短信验证码缓存器：设置720分钟后过期，
     */
    private static final Cache<String, SmsCacheExpireValue<String>> SMS_VERIFY_CODE_CACHE =
            CacheBuilder.newBuilder().expireAfterWrite(720, TimeUnit.MINUTES).maximumSize(10000).build();

    /**
     * 获取指定key的验证码
     * @param key 数据key
     * @return 验证码
     * @throws BusinessException 业务异常
     */
    public String getSmsVerifyCode(String key) throws BusinessException {
        Assert.hasText(key, "getSmsVerifyCode key can be not null");
        SmsCacheExpireValue<String> expireValue = Optional.ofNullable(SMS_VERIFY_CODE_CACHE.getIfPresent(key))
                .orElseThrow(() -> new BusinessException(SmsBusinessKey.RCDC_RCO_SMS_VERIFY_CODE_EXPIRED_ERROR));
        // 判断指定key的二维码是否失效
        if (expireValue.isExpired()) {
            // 已失效，直接清理
            SMS_VERIFY_CODE_CACHE.invalidate(key);
            throw new BusinessException(SmsBusinessKey.RCDC_RCO_SMS_VERIFY_CODE_EXPIRED_ERROR);
        }
        return expireValue.getValue();
    }

    /**
     * 获取指定key的验证码数据
     * @param key 数据key
     * @return 验证码数据
     * @throws BusinessException 业务异常
     */
    public SmsCacheExpireValue<String> getSmsVerifyData(String key) throws BusinessException {
        Assert.hasText(key, "getSmsVerifyCode key can be not null");
        SmsCacheExpireValue<String> expireValue = Optional.ofNullable(SMS_VERIFY_CODE_CACHE.getIfPresent(key))
                .orElseThrow(() -> new BusinessException(SmsBusinessKey.RCDC_RCO_SMS_VERIFY_CODE_EXPIRED_ERROR));
        // 判断指定key的二维码是否失效
        if (expireValue.isExpired()) {
            // 已失效，直接清理
            SMS_VERIFY_CODE_CACHE.invalidate(key);
            throw new BusinessException(SmsBusinessKey.RCDC_RCO_SMS_VERIFY_CODE_EXPIRED_ERROR);
        }
        return expireValue;
    }

    /**
     * 缓存指定key数据
     * @param key key
     * @param smsVerifyCode 值
     * @param phone 手机号
     * @param pwdRecoverDTO 短信策略
     */
    public void putSmsVerifyCode(String key, String smsVerifyCode, String phone, SmsPwdRecoverDTO pwdRecoverDTO) {
        Assert.hasText(key,"key can be bot empty");
        Assert.hasText(smsVerifyCode,"value can be bot empty");
        Assert.hasText(phone,"phone can be bot empty");
        Assert.notNull(pwdRecoverDTO,"pwdRecoverDTO can be bot null");
        long currentTimeMillis = System.currentTimeMillis();
        SmsCacheExpireValue<String> cacheValue = new SmsCacheExpireValue<>(smsVerifyCode, 
                currentTimeMillis + TimeUnit.MINUTES.toMillis(pwdRecoverDTO.getPeriod()), 
                currentTimeMillis + TimeUnit.SECONDS.toMillis(pwdRecoverDTO.getInterval()), phone, pwdRecoverDTO.getMaxErrorNumber());
        SMS_VERIFY_CODE_CACHE.put(key, cacheValue);
    }

    /**
     * 将指定key的短信验证码失效
     * @param key key
     */
    public void invalidateSmsVerifyCode(String key) {
        Assert.hasText(key,"invalidateSmsVerifyCode key can be bot empty");
        SMS_VERIFY_CODE_CACHE.invalidate(key);
    }

    /**
     * 设置token
     * @param key key
     * @param token 值
     */
    public void putToken(String key, UUID token) {
        Assert.notNull(key,"putToken key can be bot null");
        Assert.notNull(token,"putToken token can be bot null");
        AUTH_TOKEN_CACHE.put(key, token);
    }

    /**
     * 获取token
     * @param key key
     * @return token 值
     */
    public UUID getToken(String key) {
        Assert.notNull(key,"getToken key can be bot null");
        return AUTH_TOKEN_CACHE.getIfPresent(key);
    }

    /**
     * 将指定key的凭证失效
     * @param key key
     */
    public void invalidateToken(String key) {
        Assert.hasText(key,"invalidateToken key can be bot empty");
        AUTH_TOKEN_CACHE.invalidate(key);
    }

    /**
     * 是否可发送短信验证码
     * @param key key
     * @return boolean
     */
    public boolean isSendSmsVerifyCode(String key) {
        Assert.notNull(key,"isSendSmsVerifyCode key can be bot null");
        SmsCacheExpireValue<String> expireValue = SMS_VERIFY_CODE_CACHE.getIfPresent(key);
        return Objects.isNull(expireValue) || expireValue.enableRetry();
    }

    /**
     * 删除指定缓存数据
     * @param key key
     */
    public void removeSmsVerifyCode(String key) {
        Assert.hasText(key, "removeSmsVerifyCode key can be not null");
        SMS_VERIFY_CODE_CACHE.invalidate(key);
    }
    
    /**
     * 带失效时间的短信验证码缓存
     * @param <T> 泛型参数
     */
    public static class SmsCacheExpireValue<T> {

        /**
         * 缓存值
         */
        private final T value;

        /**
         * 失效时间
         */
        private final long expireTime;

        /**
         * 重试时间
         */
        private final long retryTime;

        /**
         * 发送短信的手机号码
         */
        private final String phone;

        /**
         * 错误次数
         */
        private final AtomicInteger errorNumber = new AtomicInteger(1);

        /**
         * 最大错误次数
         */
        private final int maxErrorNumber;

        SmsCacheExpireValue(T value, long expireTime, long retryTime, String phone, int maxErrorNumber) {
            this.value = value;
            this.expireTime = expireTime;
            this.retryTime = retryTime;
            this.phone = phone;
            this.maxErrorNumber = maxErrorNumber;
        }

        public T getValue() {
            return value;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public long getRetryTime() {
            return retryTime;
        }

        public String getPhone() {
            return phone;
        }

        /**
         * 判断当时数据是否失效
         *
         * @return boolean
         */
        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }

        /**
         * 判断当前数据是否可重试
         *
         * @return boolean
         */
        public boolean enableRetry() {
            return System.currentTimeMillis() > retryTime;
        }

        public int getErrorNumber() {
            return errorNumber.get();
        }

        public int getMaxErrorNumber() {
            return maxErrorNumber;
        }

        /**
         * 错误次数+1
         * @return 返回对应的错误次数
         */
        public int incrementAndGet() {
            return this.errorNumber.getAndIncrement();
        }
    }
    
}
