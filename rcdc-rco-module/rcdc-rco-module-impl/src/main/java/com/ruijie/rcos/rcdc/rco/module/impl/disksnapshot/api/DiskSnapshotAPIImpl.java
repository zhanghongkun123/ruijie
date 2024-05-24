package com.ruijie.rcos.rcdc.rco.module.impl.disksnapshot.api;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDiskSnapshotMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDiskSnapshotDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbDiskDeleteSnapshotDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskSnapshotAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.disksnapshot.DiskSnapshotBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;

/**
 * Description: 磁盘快照API实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年7月22日
 *
 * @author lyb
 */
public class DiskSnapshotAPIImpl implements DiskSnapshotAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskSnapshotAPIImpl.class);

    @Autowired
    private CbbVDIDiskSnapshotMgmtAPI cbbVDIDiskSnapshotMgmtAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Value("${rccpmile.limit.max_snapshots_per_volume:10}")
    private Integer rccpMaxSnapshots;

    private static final String MAX_SNAPSHOT_NUM_KEY = "max_snapshot_num";

    @Override
    public String generateSnapshotNameByDiskName(String diskName) throws BusinessException {
        Assert.hasText(diskName, "diskName must has text");
        int index = 1;
        String snapshotName;
        int max = getMaxSnapshots() + 1;

        while (true) {
            snapshotName = diskName + "_" + index;
            if (Boolean.TRUE.equals(checkNameDuplication(snapshotName))) {
                index++;
            } else {
                return snapshotName;
            }
            if (index > max) {
                LOGGER.error("根据磁盘名称[{}]生成磁盘快照名称[{}]次未成功，放弃此次生成", diskName, max);
                throw new BusinessException(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_NAME_GENERATE_BURST, diskName, Integer.toString(max));
            }
        }
    }

    @Override
    public Boolean checkSnapshotNumberOverByDiskId(UUID diskId) {
        Assert.notNull(diskId, "diskId cannot null");
        List<CbbDiskSnapshotDetailDTO> cbbDiskSnapshotDetailDTOList = cbbVDIDiskSnapshotMgmtAPI.listDiskSnapshotInfoByDiskId(diskId);
        if (cbbDiskSnapshotDetailDTOList.isEmpty()) {
            return false;
        }
        Integer num = cbbDiskSnapshotDetailDTOList.size();
        if (num >= getMaxSnapshots()) {
            LOGGER.info("磁盘【{}】快照数量【{}】达到或超过最大限制数量【{}】", diskId, num, getMaxSnapshots());
            return true;
        }
        return false;
    }

    @Override
    public void deleteBeforeOverDiskSnapshotByDiskId(UUID diskId) throws BusinessException {
        Assert.notNull(diskId, "diskId can not be null");
        List<CbbDiskSnapshotDetailDTO> diskSnapshotDetailDTOList = getBeforeOverDiskSnapshotByDiskId(diskId);
        for (CbbDiskSnapshotDetailDTO diskSnapshotDetailDTO : diskSnapshotDetailDTOList) {
            UUID taskId = UUID.randomUUID();
            CbbDiskDeleteSnapshotDTO cbbDiskDeleteSnapshotDTO = new CbbDiskDeleteSnapshotDTO();
            cbbDiskDeleteSnapshotDTO.setDiskSnapshotId(diskSnapshotDetailDTO.getId());
            cbbDiskDeleteSnapshotDTO.setTaskId(taskId);
            cbbVDIDiskSnapshotMgmtAPI.deleteDiskSnapshot(cbbDiskDeleteSnapshotDTO);
            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            LOGGER.info("删除最早创建磁盘快照【{}】成功, diskId = {}", diskSnapshotDetailDTO.getName(), diskId);
        }
    }

    @Override
    public List<CbbDiskSnapshotDetailDTO> getBeforeOverDiskSnapshotByDiskId(UUID diskId) throws BusinessException {
        Assert.notNull(diskId, "diskId cannot null");
        List<CbbDiskSnapshotDetailDTO> cbbDiskSnapshotDetailDTOList = cbbVDIDiskSnapshotMgmtAPI.listDiskSnapshotInfoByDiskId(diskId);
        if (cbbDiskSnapshotDetailDTOList.isEmpty()) {
            CbbDeskDiskDTO cbbDeskDiskDTO = cbbVDIDeskDiskAPI.getDiskDetail(diskId);
            throw new BusinessException(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_NOT_CREATE, cbbDeskDiskDTO.getName());
        }
        return cbbDiskSnapshotDetailDTOList.stream().sorted(Comparator.comparing(CbbDiskSnapshotDetailDTO::getCreateTime))
                .limit(cbbDiskSnapshotDetailDTOList.size() - getMaxSnapshots() + 1).collect(Collectors.toList());
    }

    @Override
    public Boolean checkNameDuplication(String name) {
        Assert.hasText(name, "name must has text");
        return cbbVDIDiskSnapshotMgmtAPI.isDiskSnapshotNameExists(name);
    }

    @Override
    public Integer getMaxSnapshots() {
        // 去查询数据库
        String maxSnapshotNum = globalParameterAPI.findParameter(MAX_SNAPSHOT_NUM_KEY);
        if (StringUtils.isEmpty(maxSnapshotNum)) {
            // 未查到数据库中配置信息，返回RCCP底层最大限制数
            LOGGER.info("数据库未找到快照最大容量配置KEY[{}]，默认返回快照最大容量为[{}]", MAX_SNAPSHOT_NUM_KEY, rccpMaxSnapshots);
            return rccpMaxSnapshots;
        }

        return Integer.parseInt(maxSnapshotNum);
    }

}
