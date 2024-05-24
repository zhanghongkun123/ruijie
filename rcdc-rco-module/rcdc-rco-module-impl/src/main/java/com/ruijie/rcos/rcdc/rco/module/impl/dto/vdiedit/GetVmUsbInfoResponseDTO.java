package com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit;

import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/30 13:37
 *
 * @author zhangyichi
 */
public class GetVmUsbInfoResponseDTO {

    private String usbConf;

    private USBFilterInfo usbFilter;

    public String getUsbConf() {
        return usbConf;
    }

    public void setUsbConf(String usbConf) {
        this.usbConf = usbConf;
    }

    public USBFilterInfo getUsbFilter() {
        return usbFilter;
    }

    public void setUsbFilter(USBFilterInfo usbFilter) {
        this.usbFilter = usbFilter;
    }

    /**
     * USB设备权限信息
     */
    public class USBFilterInfo {

        private final Integer allowOther = 1;

        private final String defaultUnAllowStr = StringUtils.EMPTY;

        private int defaultAllow;

        private String allowInfo;

        private int allowLength;

        private String unAllowInfo;

        private int unAllowLength;

        public USBFilterInfo() {
            // 默认允许其它USB设备
            this.defaultAllow = allowOther;
            // 默认不禁止设备
            setUnAllowInfo(defaultUnAllowStr);
        }

        public int getDefaultAllow() {
            return defaultAllow;
        }

        public void setDefaultAllow(int defaultAllow) {
            this.defaultAllow = defaultAllow;
        }

        public String getAllowInfo() {
            return allowInfo;
        }

        /**
         * 设置允许设备列表，并设置长度
         * @param allowInfo 允许设备信息
         */
        public void setAllowInfo(String allowInfo) {
            Assert.notNull(allowInfo, "allowStr cannot be null!");

            this.allowInfo = allowInfo;
            this.allowLength = allowInfo.length();
        }

        public int getAllowLength() {
            return allowLength;
        }

        public void setAllowLength(int allowLength) {
            this.allowLength = allowLength;
        }

        public String getUnAllowInfo() {
            return unAllowInfo;
        }

        /**
         * 设置禁止设备列表，并设置长度
         * @param unAllowInfo 禁止设备信息
         */
        public void setUnAllowInfo(String unAllowInfo) {
            Assert.notNull(unAllowInfo, "unAllowStr cannot be null!");
            this.unAllowInfo = unAllowInfo;
            this.unAllowLength = unAllowInfo.length();
        }

        public int getUnAllowLength() {
            return unAllowLength;
        }

        public void setUnAllowLength(int unAllowLength) {
            this.unAllowLength = unAllowLength;
        }
    }

    @Override
    public String toString() {
        return "GetVmUsbInfoResponseDTO{" +
                "usbConf='" + usbConf + '\'' +
                ", usbFilter=" + usbFilter +
                '}';
    }
}
