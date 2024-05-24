package com.ruijie.rcos.rcdc.rco.module.def.utils;

import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Description: 安全红线工具类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/06/08
 *
 * @author heruiyuan
 */
public class RedLineUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RedLineUtil.class);

    /**
     * 密钥再加密
     */
    private static final String RED_LINE_ADMIN = "QRqMTE1wbqsO8XzrJvgQQGxUAFu+1K2zoGsOtB1JoDQ+p2hPWjO4lr1HTv5E0+9i";

    private static final String RED_LINE_USER = "O8kqZdxOnXOpmob1/fa18MfrUKFKvjY9sTpW7UQt5EE57b1S7QsJLBd8lZvXQ6wz";

    private static final String RED_LINE_SHINEFTP = "9URB9Tw9E8LjCuG0gE/Dzm7loo/jtRPJdodXXG31sWpn6jIPvHF1AXtMLjMGn/cc";
    
    private static final String RED_LINE_AUDIT = "WtwAyt6QiceJJ/T84R3uHFbh3RrvnzVOeQ49jD3KDnfDSfpkRk51uJOavLgHcU/N";

    /**
     * 密钥解密
     */
    private static final String RED_LINE = "73ACD9A5972130B7";

    /**
     * 获取解密后真实AdminKey
     *
     * @return ADMINPASSWORDKEY
     */
    public static String getRealAdminRedLine() {
        return AesUtil.descrypt(RED_LINE_ADMIN, RED_LINE);
    }

    /**
     * 获取解密后真实UserKey
     *
     * @return USERPASSWORD_KEY
     */
    public static String getRealUserRedLine() {
        return AesUtil.descrypt(RED_LINE_USER, RED_LINE);
    }

    /**
     * 获取解密后真实ShineFtpKey
     *
     * @return SHINEFTPPASSWORD
     */
    public static String getRealShineFtpRedLine() {
        return AesUtil.descrypt(RED_LINE_SHINEFTP, RED_LINE);
    }

    /**
     * 获取解密后真实AuditFtpKey
     *
     * @return AUDITFILEENCRYPT
     */
    public static String getAuditFtpRedLine() {
        return AesUtil.descrypt(RED_LINE_AUDIT, RED_LINE);
    }

    /**
     * 将USERPASSWORD_KEY加密的密文转换为ADMINPASSWORDKEY加密的密文
     *
     * @param userKeyPwd USERPASSWORD_KEY加密的密文
     * @return ADMINPASSWORDKEY加密的密文
     */
    public static String convertUserKey2AdminKey(String userKeyPwd) {
        Assert.hasText(userKeyPwd, "userKeyPwd must has text");

        return AesUtil.encrypt(AesUtil.descrypt(userKeyPwd, getRealUserRedLine()), getRealAdminRedLine());
    }

    /**
     * 把身份中心保存的用户密文转为用rco密文
     * 
     * @param iacEncPwd 身份中心加密的密文
     * @return rco密文
     */
    public static String convertIacUserPwdToRcoPwd(@Nullable String iacEncPwd) {
        if (StringUtils.isBlank(iacEncPwd)) {
            LOGGER.error("传的密码为空值");
            return "";
        }
        // 先解密成明文
        String plaintext = AesUtil.descrypt(iacEncPwd, com.ruijie.rcos.gss.sdk.iac.module.def.util.RedLineUtil.getRealInnerUserRedLine());
        // 用rco的key加密
        return AesUtil.encrypt(plaintext, getRealUserRedLine());
    }


    /**
     * 解密身份中心用户密码
     * 
     * @param rcoEncPwd rco密文
     * @return 用户密码明文
     */
    public static String decryptRcoUserPwd(String rcoEncPwd) {
        Assert.hasText(rcoEncPwd, "rcoEncPwd must has text");

        return AesUtil.descrypt(rcoEncPwd, getRealUserRedLine());
    }
}

