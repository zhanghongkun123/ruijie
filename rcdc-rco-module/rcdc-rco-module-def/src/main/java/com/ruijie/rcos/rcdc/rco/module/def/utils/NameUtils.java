package com.ruijie.rcos.rcdc.rco.module.def.utils;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/1/12
 *
 * @author chenl
 */
public class NameUtils {

    /**
     * 生成随机的八位字符
     *
     * @return 名称
     */
    public static String generateRandomName() {

        String[] simpleArr = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");

        for (int index = 0; index < 8; index++) {
            String str = uuid.substring(index * 4, index * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(simpleArr[x % 0x3E]);
        }
        return shortBuffer.toString();
    }
}
