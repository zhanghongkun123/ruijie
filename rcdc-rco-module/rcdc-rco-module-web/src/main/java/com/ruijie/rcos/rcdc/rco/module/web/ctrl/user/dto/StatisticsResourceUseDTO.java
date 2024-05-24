package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;

import com.ruijie.rcos.base.alarm.module.def.dto.CpuDTO;
import com.ruijie.rcos.base.alarm.module.def.dto.MemoryDTO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/22
 *
 * @author Jarman
 */
public class StatisticsResourceUseDTO {

    private CpuDTO cpu;

    private MemoryDTO memory;

    private DiskUseDTO disk;

    public CpuDTO getCpu() {
        return cpu;
    }

    public void setCpu(CpuDTO cpu) {
        this.cpu = cpu;
    }

    public MemoryDTO getMemory() {
        return memory;
    }

    public void setMemory(MemoryDTO memory) {
        this.memory = memory;
    }

    public DiskUseDTO getDisk() {
        return disk;
    }

    public void setDisk(DiskUseDTO disk) {
        this.disk = disk;
    }

    /**
     * 硬盘使用情况
     */
    public static class DiskUseDTO {

        private Long used;

        private Long total;

        public Long getUsed() {
            return used;
        }

        public void setUsed(Long used) {
            this.used = used;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }
    }
}

