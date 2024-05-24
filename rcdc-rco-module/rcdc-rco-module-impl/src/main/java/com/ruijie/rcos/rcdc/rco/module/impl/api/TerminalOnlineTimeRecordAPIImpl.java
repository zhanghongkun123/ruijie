package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalOnlineTimeRecordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.TerminalTotalOnlineTimeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.BigScreenPlantTreeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalOnlineTimeRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Description: 统计终端在线时长业务类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/5
 *
 * @author xiao'yong'deng
 */
public class TerminalOnlineTimeRecordAPIImpl implements TerminalOnlineTimeRecordAPI {

    @Autowired
    private TerminalOnlineTimeRecordService onlineTimeRecordService;

    private static final Integer SECODE_TO_HOUR = 60 * 60;

    private static final Float VDI_SAVING_COEFFICIENT = (float) 0.24;

    private static final Float IDV_SAVING_COEFFICIENT = (float) 0.16;

    private static final Float CARBON_EMISSIONS_COEFFICIENT = (float) 0.785;

    private static final Float PLANT_TREES_COEFFICIENT = (float) 18.3;

    private static final Integer KG_TON_RATIO = 1000;

    @Override
    public BigScreenPlantTreeResponse findPlantTree() {
        BigScreenPlantTreeResponse response = new BigScreenPlantTreeResponse();
        List<TerminalTotalOnlineTimeDTO> vdiTotalOnlineTimeList = onlineTimeRecordService.findTotalOnlineTime(Arrays.asList(CbbImageType.VDI.name()));
        List<TerminalTotalOnlineTimeDTO> idvTotalOnlineTimeList = onlineTimeRecordService.findTotalOnlineTime(Arrays.asList(CbbImageType.IDV.name(),
            CbbImageType.VOI.name()));

        Float totalElectricalSaving = countElectricalSaving(vdiTotalOnlineTimeList, idvTotalOnlineTimeList);
        Float carbonEmissions = countCarbonEmissions(totalElectricalSaving);
        Float plantTrees = countPlantTrees(carbonEmissions);
        response.setElectricalSaving(totalElectricalSaving);
        response.setCarbonEmissions(carbonEmissions);
        response.setPlantTrees(plantTrees);
        return response;
    }

    /**
     * 计算节约的电量
     * @param vdiTotalOnlineTime  vdi类型终端在线时长
     * @param idvTotalOnlineTime  idv/voi类型终端在线时长
     * @return
     */
    private float countElectricalSaving(List<TerminalTotalOnlineTimeDTO> vdiTotalOnlineTime, List<TerminalTotalOnlineTimeDTO> idvTotalOnlineTime) {
        Long vdiTime = getOnlineTime(vdiTotalOnlineTime);
        Float vdiTimeHours = vdiTime / SECODE_TO_HOUR.floatValue();
        Long idvTime = getOnlineTime(idvTotalOnlineTime);
        Float idvTimeHours = idvTime / SECODE_TO_HOUR.floatValue();
        return VDI_SAVING_COEFFICIENT * vdiTimeHours  + IDV_SAVING_COEFFICIENT * idvTimeHours;
    }

    /**
     * 统计节约的碳排放量
     * @param totalElectricalSaving 节约电量
     * @return
     */
    private Float countCarbonEmissions(Float totalElectricalSaving) {
        if (totalElectricalSaving == null) {
            return (float) 0;
        }
        return CARBON_EMISSIONS_COEFFICIENT * totalElectricalSaving / KG_TON_RATIO;
    }

    /**
     * 统计植树量
     * @param carbonEmissions 节约碳排放量
     * @return  植树量
     */
    private Float countPlantTrees(Float carbonEmissions) {
        if (carbonEmissions == null) {
            return (float) 0;
        }
        return carbonEmissions * KG_TON_RATIO / PLANT_TREES_COEFFICIENT;
    }

    /**
     *  获取在线总时长
     * @param onlineTime 各个类型终端在线集合
     * @return 在线总时长
     */
    private Long getOnlineTime(List<TerminalTotalOnlineTimeDTO> onlineTime) {
        if (CollectionUtils.isEmpty(onlineTime)) {
            return 0L;
        }
        return onlineTime.stream().mapToLong(TerminalTotalOnlineTimeDTO::getOnlineTotalTime).sum();
    }

}
