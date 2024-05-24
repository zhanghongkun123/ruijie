package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.utils;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.externalstorage.ExternalStorageDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo.ExternalStorageVO;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Date;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月27日
 *
 * @author zhanghongkun
 */
public class BackupUtils {

    /**
     * DTO转VO
     *
     * @param storageDTO DTO
     * @return 返回VO
     */
    public static ExternalStorageVO convertExternalStorageVO(ExternalStorageDTO storageDTO) {
        Assert.notNull(storageDTO, "storageDTO不能为空");

        ExternalStorageVO vo = new ExternalStorageVO();
        vo.setId(storageDTO.getExtStorageId());
        vo.setName(storageDTO.getName());
        vo.setState(storageDTO.getState());
        vo.setTotalCapacity(storageDTO.getTotalCapacity());
        vo.setUsedCapacity(storageDTO.getUsedCapacity());
        vo.setProtocolType(storageDTO.getProtocolType());
        Instant instant = Instant.ofEpochMilli(storageDTO.getDateCreated());
        vo.setCreateTime(Date.from(instant));
        return vo;

    }

}
