package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.mock;

import org.springframework.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Description: 阳光资管加解密工具类
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2023/8/22 19:46
 *
 * @author zjy
 */
public class MockThirdPartyShineAssestMgmtDesUtil {

    private static final String DEFAULT_KEY = "GM@147hyf&";

    private static Cipher encryptCipher = null;

    private static Cipher decryptCipher = null;

    private static final char CHAR_A = 'A';

    private static final int RADIX = 16;

    private static final char[] CODE_SEQUENCE = {'D','1','O','R','4','P','3','Q','G','I','7','T','V','8','6','M',
        'C','2','U','5','W','L','9','Z','F','H','J','N','Y','0','S','E','B','K','X','A'};

    /**
     * 字节数组转16进制字符串
     * @param byteArr 字节数组
     * @return 16进制字符串
     */
    public static String byteArr2HexStr(byte[] byteArr) {
        Assert.notNull(byteArr, "byteArr can not be null");

        int iLen = byteArr.length;
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = byteArr[i];
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            if (intTmp < RADIX) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, RADIX));
        }
        return sb.toString();
    }


    /**
     * 16进制字符串转字节数组
     * @param hexStr 16进制字符串
     * @return 字节数组
     */
    public static byte[] hexStr2ByteArr(String hexStr) {
        Assert.hasText(hexStr, "hexStr can not be blank");

        byte[] byteArr = hexStr.getBytes();
        int iLen = byteArr.length;

        byte[] outArr = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(byteArr, i, 2);
            outArr[i / 2] = (byte) Integer.parseInt(strTmp, RADIX);
        }
        return outArr;
    }

    /**
     * 字节数组加密
     * @param byteArr 字节数组
     * @return 加密后的字节数组
     * @throws Exception ex
     */
    public static byte[] encrypt(byte[] byteArr) throws Exception {
        Assert.notNull(byteArr, "byteArr can not be null");

        Key key = getKey(DEFAULT_KEY.getBytes());
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        return encryptCipher.doFinal(byteArr);
    }

    /**
     * 加密字符串
     * @param str 字符串
     * @return 加密后的字符串
     * @throws Exception ex
     */
    public static String encrypt(String str) throws Exception {
        Assert.hasText(str, "str can not be blank");
        return byteArr2HexStr(encrypt(str.getBytes()));
    }

    /**
     * 解密字节数组
     * @param byteArr 字节数组
     * @return 解密后的字节数组
     * @throws Exception ex
     */
    public static byte[] decrypt(byte[] byteArr) throws Exception {
        Assert.notNull(byteArr, "byteArr can not be null");

        Key key = getKey(DEFAULT_KEY.getBytes());
        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        return decryptCipher.doFinal(byteArr);
    }

    /**
     * 解密
     * @param str 字符串
     * @return 解密后的字符串
     * @throws Exception ex
     */
    public static String decrypt(String str) throws Exception {
        Assert.hasText(str, "str can not be blank");

        return new String(decrypt(hexStr2ByteArr(str)));
    }


    private static Key getKey(byte[] bTmpArr) {
        byte[] bArr = new byte[8];

        for (int i = 0; i < bTmpArr.length && i < bArr.length; i++) {
            bArr[i] = bTmpArr[i];
        }

        return new SecretKeySpec(bArr, "DES");
    }

    /**
     * 变更字符串
     * @param str 字符串
     * @return 变更后的字符串
     * @throws Exception ex
     */
    public static String changeStr(String str) throws Exception {
        Assert.hasText(str, "str can not be blank");

        char[] idCharArr = str.toCharArray();
        for (int j = 0; j < idCharArr.length; j++) {
            for (int i = 0; i < CODE_SEQUENCE.length; i++) {
                if (idCharArr[j] == CHAR_A) {
                    idCharArr[j] = CODE_SEQUENCE[0];
                    break;
                } else if (idCharArr[j] == CODE_SEQUENCE[i]) {
                    idCharArr[j] = CODE_SEQUENCE[i + 1];
                    break;
                }
            }
        }
        return String.valueOf(idCharArr);
    }
}
