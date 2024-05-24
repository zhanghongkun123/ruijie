package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.util;

import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/8 9:40
 *
 * @author coderLee23
 */
public class ZipUtil {

    private ZipUtil() {

    }

    /**
     * 压缩字符串
     *
     * @param content 压缩前字符串
     * @return byte[] 压缩后数组
     * @throws IOException 压缩异常
     */
    public static byte[] deflater(String content) throws IOException {
        Assert.hasText(content, "content must not be null or empty");
        Deflater def = new Deflater(Deflater.BEST_COMPRESSION, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (DeflaterOutputStream dos = new DeflaterOutputStream(byteArrayOutputStream, def)) {
            byte[] bytesArr = content.getBytes(StandardCharsets.UTF_8);
            dos.write(bytesArr);
            dos.flush();
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 解压字节数组
     *
     * @param bytes 字节数组
     * @return string 解压后的字符串
     * @throws IOException ex
     */
    public static String inflater(byte[] bytes) throws IOException {
        Assert.notNull(bytes,"bytes cant be null");
        Assert.isTrue(bytes.length > 0, "bytes length > 0");
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Inflater inf = new Inflater(true);
        try (InflaterInputStream iis = new InflaterInputStream(bis, inf)) {
            int readCount = 0;
            byte[] bufArr = new byte[1024];
            while ((readCount = iis.read(bufArr, 0, bufArr.length)) != -1) {
                bos.write(bufArr, 0, readCount);
            }
            bos.flush();
        }
        return bos.toString(StandardCharsets.UTF_8.name());
    }

}
