package com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto;

import com.alibaba.fastjson.util.TypeUtils;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.RelateTypeEnum;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

/**
 * Description: 统计连接失败DTO类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/21 13:50
 *
 * @author yxq
 */
public class StatisticDesktopConnectFaultDTO {

    /**
     * 连接失败的数量
     */
    private Integer count;

    /**
     * 关联的桌面池、桌面组id
     */
    private UUID relatedId;

    /**
     * 关联的类型：桌面池
     */
    private RelateTypeEnum relatedType;

    /**
     * 桌面池类型：
     */
    private DesktopPoolType desktopPoolType;

    /**
     * 将数据库查询的结果转换成dto
     *
     * @param objectArr 数据库查询的结果
     * @return StatisticConnectFaultDTO
     */
    public static StatisticDesktopConnectFaultDTO convertFor(Object[] objectArr) {
        Assert.notNull(objectArr, "objectArr must not be null");
        Assert.isTrue(objectArr.length == 4, "objectArr length must 4");

        StatisticDesktopConnectFaultDTO connectFaultDTO = new StatisticDesktopConnectFaultDTO();
        connectFaultDTO.setCount(TypeUtils.castToInt(objectArr[0]));
        connectFaultDTO.setRelatedId(Optional.ofNullable(objectArr[1]).map(obj -> UUID.fromString((String) obj)).orElse(null));
        connectFaultDTO.setRelatedType(RelateTypeEnum.valueOf((String) objectArr[2]));
        connectFaultDTO.setDesktopPoolType(DesktopPoolType.valueOf((String) objectArr[3]));
        return connectFaultDTO;
    }

    public RelateTypeEnum getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(RelateTypeEnum relatedType) {
        this.relatedType = relatedType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(DesktopPoolType desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }
}
