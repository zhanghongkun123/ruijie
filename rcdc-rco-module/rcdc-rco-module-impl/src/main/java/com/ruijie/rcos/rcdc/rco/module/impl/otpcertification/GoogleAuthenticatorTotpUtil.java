package com.ruijie.rcos.rcdc.rco.module.impl.otpcertification;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.google.common.io.BaseEncoding;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.UserOtpCertificationConfigDTO;


/**
 * GoogleAuthenticator TOTP 实现工具类
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author lihengjing
 */
public class GoogleAuthenticatorTotpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleAuthenticatorTotpUtil.class);

    private static final int[] DIGITS_POWER
    // 0 1 2 3 4 5 6 7 8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};


    public static final String NEGATIVE = "-";

    /**
     * 验证动态口令是否正确
     *
     * @param userOtpCertificationConfigDTO totp配置
     * @param secretBase32 密钥
     * @param code 待验证的动态口令
     * @return 是否正确
     */
    public static boolean verify(UserOtpCertificationConfigDTO userOtpCertificationConfigDTO, String secretBase32, String code) {
        Assert.notNull(userOtpCertificationConfigDTO, "userOtpCertificationConfigDTO can not be null");
        Assert.notNull(secretBase32, "secretBase32 can not be null");
        Assert.notNull(code, "code can not be null");

        long currentTime = parseTimeDifferent(userOtpCertificationConfigDTO.getTimeDifferent());
        long generateTime = currentTime / 1000L;
        // 判断动态口令和当前时间生成动态口令是否一致
        String generateCurrentCode = generate(secretBase32, userOtpCertificationConfigDTO, generateTime);
        LOGGER.info("计算动态口令的时间为[{}],时间差为[{}]毫秒,服务端生成动态口令为[{}],客户端动态口令为[{}]", generateTime,
                userOtpCertificationConfigDTO.getTimeDifferent(), generateCurrentCode, code);
        return generateCurrentCode.equals(code);
    }

    /**
     * 生成totp协议字符串
     *
     * otpauth://totp/RJ-RCDC-V5.3-LABEL:test@ruijie.com.cn?secret=HXDMVJECJJWSRB3HWIZR4IFUGFTMXBOZ
     * &issuer=RJ-RCDC-V5.3&algorithm=SHA1&digits=6&period=30
     *
     * @param userOtpCertificationConfigDTO
     *        userOtpCertificationConfigDTO.systemName 密钥对应系统名称
     *        userOtpCertificationConfigDTO.userName 密钥拥有者名称
     *        userOtpCertificationConfigDTO.domain 密钥对应使用域名称
     *        userOtpCertificationConfigDTO.secretBase32 密钥
     *        userOtpCertificationConfigDTO.totpConfig totp配置
     * @return 密钥二维码字符串
     */
    public static String generateTotpProtocolString(UserOtpCertificationConfigDTO userOtpCertificationConfigDTO) {
        Assert.notNull(userOtpCertificationConfigDTO, "userOtpCertificationConfigDTO can not be null");
        return "otpauth://totp/" + userOtpCertificationConfigDTO.getSystemName() + ":" + userOtpCertificationConfigDTO.getUserName() + "@"
                + userOtpCertificationConfigDTO.getSystemHost() + "?secret=" + userOtpCertificationConfigDTO.getOtpSecretKey() + "&issuer="
                + userOtpCertificationConfigDTO.getSystemName() + "&algorithm=" + userOtpCertificationConfigDTO.getAlgorithm() + "&digits="
                + userOtpCertificationConfigDTO.getDigits() + "&period=" + userOtpCertificationConfigDTO.getPeriod();
    }

    /**
     * 根据密钥生成动态口令
     *
     * @param secretBase32 base32编码格式的密钥
     * @param userOtpCertificationConfigDTO totp配置
     * @return 实时动态口令
     */
    private static String generate(String secretBase32, UserOtpCertificationConfigDTO userOtpCertificationConfigDTO, long generateTime) {

        String secretHex = HexEncoding.encode(BaseEncoding.base32().decode(secretBase32));

        long period = userOtpCertificationConfigDTO.getPeriod();

        String steps;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            long t = generateTime / period;
            steps = Long.toHexString(t).toUpperCase();
            StringBuilder stepsSb = new StringBuilder(steps);
            while (stepsSb.length() < 16) {
                stepsSb.insert(0, "0");
            }
            steps = stepsSb.toString();
            String returnStr = "";
            switch (userOtpCertificationConfigDTO.getAlgorithm()) {
                case SHA1:
                    returnStr = generateTOTP(secretHex, steps, String.valueOf(userOtpCertificationConfigDTO.getDigits()));
                    break;
                case SHA256:
                    returnStr = generateTOTP256(secretHex, steps, String.valueOf(userOtpCertificationConfigDTO.getDigits()));
                    break;
                case SHA512:
                    returnStr = generateTOTP512(secretHex, steps, String.valueOf(userOtpCertificationConfigDTO.getDigits()));
                    break;
                case MD5:
                    returnStr = generateTOTPMD5(secretHex, steps, String.valueOf(userOtpCertificationConfigDTO.getDigits()));
                    break;
                default:
            }
            return returnStr;
        } catch (final Exception e) {
            LOGGER.error("生成动态口令出错：" + secretBase32, e);
            throw new RuntimeException("生成动态口令出错：" + secretBase32, e);
        }
    }

    /**
     * 此方法为给定的参数集生成SHA256 TOTP动态码。
     *
     * @param key 共享密钥，十六进制编码
     * @param time 时间戳
     * @param returnDigits 要返回的动态码位数
     * @return 10进制 动态码
     */
    private static String generateTOTP256(String key, String time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA256");
    }

    /**
     * 此方法为给定的参数集生成SHA512 TOTP动态码。
     *
     * @param key 共享密钥，十六进制编码
     * @param time 时间戳
     * @param returnDigits 要返回的动态码位数
     * @return 10进制 动态码
     */
    private static String generateTOTP512(String key, String time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA512");
    }


    /**
     * 此方法为给定的参数集生成MD5 TOTP动态码。
     *
     * @param key 共享密钥，十六进制编码
     * @param time 时间戳
     * @param returnDigits 要返回的动态码位数
     * @return 10进制 动态码
     */
    private static String generateTOTPMD5(String key, String time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacMD5");
    }

    /**
     * 此方法为给定的参数集生成SHA1 TOTP动态码。
     *
     * @param key 共享密钥，十六进制编码
     * @param time 时间戳
     * @param returnDigits 要返回的动态码位数
     * @return 10进制 动态码
     */
    private static String generateTOTP(String key, String time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA1");
    }

    /**
     * 此方法为给定的参数集生成 TOTP动态码。
     *
     * @param key 共享密钥，十六进制编码
     * @param time 时间戳
     * @param returnDigits 要返回的动态码位数
     * @param crypto 要使用的加密函数
     * @return 10进制 动态码
     */
    private static String generateTOTP(String key, String time, String returnDigits, String crypto) {
        int codeDigits = Integer.decode(returnDigits).intValue();
        // Using the counter
        // First 8 bytes are for the movingFactor
        // Compliant with base RFC 4226 (HOTP)
        StringBuilder timeSb = new StringBuilder(time);
        while (timeSb.length() < 16) {
            timeSb.insert(0, "0");
        }
        time = timeSb.toString();

        // Get the HEX in a Byte[]
        byte[] msgArr = hexStr2Bytes(time);
        byte[] keyArr = hexStr2Bytes(key);

        byte[] hashArr = hmacSha(crypto, keyArr, msgArr);

        // put selected bytes into result int
        int offset = hashArr[hashArr.length - 1] & 0xf;

        int binary = ((hashArr[offset] & 0x7f) << 24) | ((hashArr[offset + 1] & 0xff) << 16) | ((hashArr[offset + 2] & 0xff) << 8)
                | (hashArr[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];
        String result = Integer.toString(otp);
        StringBuilder resultSb = new StringBuilder(result);
        while (resultSb.length() < codeDigits) {
            resultSb.insert(0, "0");
        }
        result = resultSb.toString();
        return result;
    }

    /**
     * 此方法将十六进制字符串转换为字节[]
     *
     * @param hex: 十六进制字符串
     *
     * @return: 字节数组
     */
    private static byte[] hexStr2Bytes(String hex) {
        // Adding one byte to get the right conversion
        // Values starting with "0" can be converted
        byte[] bArr = new BigInteger("10" + hex, 16).toByteArray();

        // Copy all the REAL bytes, not the "first"
        byte[] retArr = new byte[bArr.length - 1];
        for (int i = 0; i < retArr.length; i++) {
            retArr[i] = bArr[i + 1];
        }
        return retArr;
    }


    /**
     * 此方法使用JCE提供加密算法。HMAC使用crypto hash算法作为参数来计算散列消息认证码
     *
     * @param crypto: 密码算法 (HmacSHA1, HmacSHA256, HmacSHA512)
     * @param keyBytes: 用于HMAC密钥的字节
     * @param text: 要验证的消息或文本
     */
    private static byte[] hmacSha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    /**
     * 生成base32编码的随机密钥
     *
     * @return base32编码的随机密钥
     */
    public static String getRandomSecretBase32() {
        SecureRandom random = new SecureRandom();
        byte[] saltArr = new byte[32];
        random.nextBytes(saltArr);
        return BaseEncoding.base32().encode(saltArr);
    }

    /**
     * 获取加盐之后保存在数据库中的OTP密钥
     * 
     * @param realSecretKey 真实的OTP密钥
     * @return 加盐的OTP密钥
     */
    public static String getFakeSecretKey(String realSecretKey) {
        Assert.notNull(realSecretKey, "realSecretKey can not be null");

        BaseEncoding base32 = BaseEncoding.base32();
        return base32.encode(moveLeftByteArr(base32.decode(realSecretKey), 3));
    }

    /**
     * 获取数据库中解密的真实OTP密钥
     * 
     * @param fakeSecretKey 加盐之后保存在数据库中的密钥
     * @return 真实的OTP密钥
     */
    public static String getRealSecretKey(String fakeSecretKey) {
        Assert.notNull(fakeSecretKey, "fakeSecretKey can not be null");

        BaseEncoding base32 = BaseEncoding.base32();
        return base32.encode(moveRightByteArr(base32.decode(fakeSecretKey), 3));
    }

    private static byte[] moveLeftByteArr(byte[] arr, int distance) {
        if (distance > arr.length) {
            distance = distance % arr.length;
        }
        if (distance < 0) {
            distance = -distance;
        }
        byte[] targetArr = new byte[arr.length];
        System.arraycopy(arr, distance, targetArr, 0, arr.length - distance);
        System.arraycopy(arr, 0, targetArr, arr.length - distance, distance);
        System.arraycopy(targetArr, 0, arr, 0, arr.length);
        return targetArr;
    }

    private static byte[] moveRightByteArr(byte[] arr, int distance) {
        if (distance > arr.length) {
            distance = distance % arr.length;
        }
        if (distance < 0) {
            distance = -distance;
        }
        byte[] targetArr = new byte[arr.length];
        System.arraycopy(arr, 0, targetArr, distance, arr.length - distance);
        System.arraycopy(arr, arr.length - distance, targetArr, 0, distance);
        System.arraycopy(targetArr, 0, arr, 0, arr.length);
        return targetArr;
    }

    /**
     * 动态口令绑定时间和当前系统时间不一致
     *
     * @param timeDifferent 时间差
     * @return
     */
    private static long parseTimeDifferent(String timeDifferent) {
        Long otpCodeBindTime = System.currentTimeMillis();
        if (StringUtils.isBlank(timeDifferent)) {
            return System.currentTimeMillis();
        }

        if (timeDifferent.startsWith(NEGATIVE)) {
            timeDifferent = StringUtils.replace(timeDifferent, NEGATIVE, "");
            return otpCodeBindTime - Long.parseLong(timeDifferent);
        }
        return otpCodeBindTime + Long.parseLong(timeDifferent);
    }

}
