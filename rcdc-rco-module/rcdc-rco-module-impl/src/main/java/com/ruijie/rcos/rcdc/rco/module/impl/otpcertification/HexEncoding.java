package com.ruijie.rcos.rcdc.rco.module.impl.otpcertification;

import org.springframework.util.Assert;

/**
 * HexEncoding 十六进制编码器
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author lihengjing
 */
public class HexEncoding {

    private HexEncoding() {

    }

    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    /**
     * 将提供的数据编码为十六进制字符串
     * @param data 被编码的字符串
     * @return 十六进制字符串
     */
    public static String encode(byte[] data) {
        Assert.notNull(data, "data can not be null");
        StringBuilder result = new StringBuilder(data.length * 2);
        for (byte b : data) {
            result.append(HEX_DIGITS[(b >>> 4) & 0x0f]);
            result.append(HEX_DIGITS[b & 0x0f]);
        }
        return result.toString();
    }

    /**
     * 将提供的十六进制字符串解码为字节数组
     * @param encoded 被解码的字符串
     * @return 解码后的字节数组
     */
    public static byte[] decode(String encoded) {
        Assert.notNull(encoded, "encoded can not be null");
        // IMPLEMENTATION NOTE: Special care is taken to permit odd number of hexadecimal digits.
        int resultLengthBytes = (encoded.length() + 1) / 2;
        byte[] resultArr = new byte[resultLengthBytes];
        int resultOffset = 0;
        int encodedCharOffset = 0;
        if ((encoded.length() % 2) != 0) {
            // Odd number of digits -- the first digit is the lower 4 bits of the first result byte.
            resultArr[resultOffset++] = (byte) getHexadecimalDigitValue(encoded.charAt(encodedCharOffset));
            encodedCharOffset++;
        }
        for (int len = encoded.length(); encodedCharOffset < len; encodedCharOffset += 2) {
            resultArr[resultOffset++] = (byte) ((getHexadecimalDigitValue(encoded.charAt(encodedCharOffset)) << 4)
                    | getHexadecimalDigitValue(encoded.charAt(encodedCharOffset + 1)));
        }
        return resultArr;
    }

    private static int getHexadecimalDigitValue(char c) {
        if ((c >= 'a') && (c <= 'f')) {
            return (c - 'a') + 0x0a;
        } else if ((c >= 'A') && (c <= 'F')) {
            return (c - 'A') + 0x0a;
        } else if ((c >= '0') && (c <= '9')) {
            return c - '0';
        } else {
            throw new IllegalArgumentException("Invalid hexadecimal digit at position : '" + c + "' (0x" + Integer.toHexString(c) + ")");
        }
    }
}
