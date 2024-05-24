package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/21
 *
 * @author zqj
 */

public class DeskHostInfoDTO {

    @NotNull
    private UUID hostId;

    @NotNull
    private String hostBusinessType;

    @NotNull
    private UsageInfoDTO usageInfo;

    @NotNull
    private BaseInfoDTO baseInfo;

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public UsageInfoDTO getUsageInfo() {
        return usageInfo;
    }

    public void setUsageInfo(UsageInfoDTO usageInfo) {
        this.usageInfo = usageInfo;
    }

    public BaseInfoDTO getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(BaseInfoDTO baseInfo) {
        this.baseInfo = baseInfo;
    }

    public String getHostBusinessType() {
        return hostBusinessType;
    }

    public void setHostBusinessType(String hostBusinessType) {
        this.hostBusinessType = hostBusinessType;
    }

    /**
     * Description: Function Description
     * Copyright: Copyright (c) 2024
     * Company: Ruijie Co., Ltd.
     * Create Time: 2024/2/21
     *
     * @author zqj
     */
    public static class UsageInfoDTO {

        /**
         * cpu使用情况,0.5
         */
        @NotNull
        private Float cpuUsage;

        /**
         * 内存使用情况，单位MB
         */
        @NotNull
        private Integer memoryUsage;

        /**
         * 系统盘使用情况，单位GB
         */
        @NotNull
        private Integer systemDiskUsage;

        @NotNull
        private Integer daysLeft;

        public Float getCpuUsage() {
            return cpuUsage;
        }

        public void setCpuUsage(Float cpuUsage) {
            this.cpuUsage = cpuUsage;
        }

        public Integer getMemoryUsage() {
            return memoryUsage;
        }

        public void setMemoryUsage(Integer memoryUsage) {
            this.memoryUsage = memoryUsage;
        }

        public Integer getSystemDiskUsage() {
            return systemDiskUsage;
        }

        public void setSystemDiskUsage(Integer systemDiskUsage) {
            this.systemDiskUsage = systemDiskUsage;
        }

        public Integer getDaysLeft() {
            return daysLeft;
        }

        public void setDaysLeft(Integer daysLeft) {
            this.daysLeft = daysLeft;
        }
    }


    /**
     * Description: Function Description
     * Copyright: Copyright (c) 2024
     * Company: Ruijie Co., Ltd.
     * Create Time: 2024/2/21
     *
     * @author zqj
     */
    public static class BaseInfoDTO {

        /**
         * MAC地址
         */
        @NotBlank
        private String macAddress;

        /**
         * 计算机名称
         */
        @NotBlank
        private String computerName;

        /**
         * cpu核数
         */
        @NotNull
        private Integer cpu;

        /**
         * 内存大小，单位MB
         */
        @NotNull
        private Integer memory;

        /**
         * 系统盘大小，单位GB
         */
        @NotNull
        private Integer systemDisk;

        /**
         * HostAgent版本
         */
        @NotNull
        private String version;

        @JSONField(name = "isAD")
        @Nullable
        private Integer isAd;

        @Nullable
        private String domainName;

        @NotNull
        private String os;

        @NotNull
        private String osVersion;

        @NotNull
        private String agentVersion;

        @NotNull
        private Integer personDisk;

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public String getComputerName() {
            return computerName;
        }

        public void setComputerName(String computerName) {
            this.computerName = computerName;
        }

        public Integer getCpu() {
            return cpu;
        }

        public void setCpu(Integer cpu) {
            this.cpu = cpu;
        }

        public Integer getMemory() {
            return memory;
        }

        public void setMemory(Integer memory) {
            this.memory = memory;
        }

        public Integer getSystemDisk() {
            return systemDisk;
        }

        public void setSystemDisk(Integer systemDisk) {
            this.systemDisk = systemDisk;
        }

        @Nullable
        public Integer getIsAd() {
            return isAd;
        }

        public void setIsAd(@Nullable Integer isAd) {
            this.isAd = isAd;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getAgentVersion() {
            return agentVersion;
        }

        public void setAgentVersion(String agentVersion) {
            this.agentVersion = agentVersion;
        }

        public Integer getPersonDisk() {
            return personDisk;
        }

        public void setPersonDisk(Integer personDisk) {
            this.personDisk = personDisk;
        }
    }
}
