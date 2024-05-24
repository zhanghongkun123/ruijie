package com.ruijie.rcos.rcdc.rco.module.impl.cmc.service;

import com.ruijie.rcos.rcdc.rco.module.impl.cmc.contant.CmcConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesksoftUseRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesksoftUseRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.DesksoftOperateDTO;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:  GuestTool 上报软件信息消费具体实现
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.11.05
 *
 * @author LinHJ
 */
@Service
public class DeskSoftwareInfoConsumeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskSoftwareInfoConsumeService.class);

    @Autowired
    private DesksoftUseRecordDAO desksoftUseRecordDAO;

    /**
     * 业务处理
     *
     * @param desksoftOperateDTOList 需要处理的数据
     */
    public void doRun(List<DesksoftOperateDTO> desksoftOperateDTOList) {

        Assert.notNull(desksoftOperateDTOList, "desksoftOperateDTOList must not be null");

        // 过滤操作次数为空或者小于零的记录
        Map<String, List<DesksoftOperateDTO>> collectMap = desksoftOperateDTOList.stream().filter(dto ->
                dto.getOperateTimes() != null && dto.getOperateTimes() > 0).collect(Collectors.groupingBy(this::fetchGroupTypeKey));

        collectMap.keySet().forEach(fetchType -> {
            try {
                // 软件名称 + 软件版本做唯一标识
                final String[] splitArr = fetchType.split(CmcConstants.SPLIT_VERSION);
                final String name = splitArr[0];
                final String softVersion = splitArr[1];

                // 实际业务处理
                List<DesksoftOperateDTO> simpleSoftList = collectMap.get(fetchType);
                if (simpleSoftList == null || simpleSoftList.size() == 0) {
                    LOGGER.warn("key {} 对应数据为空，无需处理", fetchType);
                    return;
                }
                int count = (int) simpleSoftList.stream().mapToLong(DesksoftOperateDTO::getOperateTimes).sum();

                // 并发场景实现分布式锁
                LockableExecutor.executeWithTryLock(CmcConstants.LOCK_DESKSOFT_MSG + fetchType, () -> {
                    long updateTime = LocalDate.now().toEpochDay();
                    List<DesksoftUseRecordEntity> entityList = desksoftUseRecordDAO.findByNameAndUpdateTimeEquals(name, updateTime)
                            .stream().filter(entity -> Objects.equals(entity.getSoftVersion(), softVersion)).collect(Collectors.toList());
                    if (entityList.isEmpty()) {
                        insert(simpleSoftList.get(0), name, updateTime, count);
                    } else {
                        // 理论上仅会存在一条记录的情况
                        for (DesksoftUseRecordEntity entity : entityList) {
                            desksoftUseRecordDAO.increaseOperateTimes(entity.getOperateTimes() + count,
                                    name, softVersion, entity.getVersion(), updateTime);
                        }
                    }
                }, CmcConstants.WAIT_SOFT_TIMES);

            } catch (Exception ex) {
                LOGGER.error("处理 CMC 数据入库异常，过滤", ex);
            }
        });
    }

    /**
     * 插入数据，默认操作次数为 0
     *
     * @param desksoftOperateDTO dto
     * @param name               名称
     * @param updateTime         时间
     * @param operateTimes       操作次数
     */
    private void insert(DesksoftOperateDTO desksoftOperateDTO, String name, long updateTime, long operateTimes) {

        DesksoftUseRecordEntity entity = new DesksoftUseRecordEntity();
        BeanUtils.copyProperties(desksoftOperateDTO, entity);
        entity.setName(name);
        entity.setOperateTimes((int) operateTimes);
        entity.setUpdateTime(updateTime);
        desksoftUseRecordDAO.save(entity);
    }

    private String fetchGroupTypeKey(DesksoftOperateDTO desksoftOperateDTO) {
        return obtainSoftwareName(desksoftOperateDTO) + CmcConstants.SPLIT_VERSION + desksoftOperateDTO.getSoftVersion();
    }

    /**
     * 名称作为唯一标识
     *
     * @param desksoftOperateDTO 处理对象
     * @return 名称
     */
    private String obtainSoftwareName(DesksoftOperateDTO desksoftOperateDTO) {

        if (!StringUtils.isEmpty(desksoftOperateDTO.getProductName())) {
            return desksoftOperateDTO.getProductName();
        } else if (!StringUtils.isEmpty(desksoftOperateDTO.getFileDescription())) {
            return desksoftOperateDTO.getFileDescription();
        } else if (!StringUtils.isEmpty(desksoftOperateDTO.getOriFileName())) {
            return desksoftOperateDTO.getOriFileName();
        }
        return "UNKNOWN";
    }
}
