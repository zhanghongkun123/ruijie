package com.ruijie.rcos.rcdc.rco.module.impl.desksnapshot.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskSnapshotAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotUserType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSnapshotAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.MaxSnapshotsRangeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDeskSnapshotDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desksnapshot.DeskSnapshotBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDeskSnapshotEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月10日
 *
 * @author luojianmo
 */
public class DeskSnapshotAPIImpl implements DeskSnapshotAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskSnapshotAPIImpl.class);

    @Autowired
    private CbbVDIDeskSnapshotAPI cbbVDIDeskSnapshotAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private ViewDeskSnapshotDAO viewDeskSnapshotDAO;

    private static final int MIN_SNAPSHOT_NUM = 1;

    private static final String MAX_SNAPSHOT_NUM_KEY = "max_snapshot_num";

    @Value("${rccpmile.limit.max_snapshots_per_volume:10}")
    private Integer rccpMaxSnapshots;

    /**
     * 根据云桌面名称生成快照名称最大次数
     */
    private static final Integer MAX = 1000;

    @Override
    public String generateSnapshotNameByDesktopName(String desktopName) throws BusinessException {
        Assert.hasText(desktopName, "deskId cannot null");
        int index = 1;
        String snapshotName;

        while (true) {
            snapshotName = desktopName + "_" + index;
            if (Boolean.TRUE.equals(checkNameDuplication(snapshotName))) {
                index++;
            } else {
                return snapshotName;
            }
            if (index >= MAX) {
                LOGGER.error("根据云桌面名称[{}]生成云桌面快照名称[{}]次未成功，放弃此次生成", desktopName, MAX);
                throw new BusinessException(DeskSnapshotBusinessKey.RCDC_RCO_DESK_SNAPSHOT_NAME_GENERATE_BURST, desktopName, MAX.toString());
            }
        }
    }

    @Override
    public Boolean checkSnapshotNumberOverByDeskId(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId cannot null");
        int size = cbbVDIDeskSnapshotAPI.countDeskSnapshotByDeskId(deskId);
        Integer all = this.getMaxSnapshots();
        if (size >= all) {
            LOGGER.info("云桌面[{}]快照数量[{}]超过最大限制数量[{}]", deskId, size, all);
            return true;
        }

        return false;
    }


    @Override
    public List<CbbDeskSnapshotDTO> getBeforeOverDeskSnapshotByDeskId(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId cannot null");
        List<CbbDeskSnapshotDTO> cbbDeskSnapshotDTOList = cbbVDIDeskSnapshotAPI.listDeskSnapshotInfoByDeskId(deskId);
        if (cbbDeskSnapshotDTOList.isEmpty()) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
            throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_NOT_CREATE, cloudDesktopDetailDTO.getDesktopName());
        }
        return cbbDeskSnapshotDTOList.stream().sorted(Comparator.comparing(CbbDeskSnapshotDTO::getCreateTime))
                .limit(cbbDeskSnapshotDTOList.size() - getMaxSnapshots() + 1).collect(Collectors.toList());
    }

    @Override
    public Boolean checkNameDuplication(String name) {
        Assert.hasText(name, "name cannot null");
        try {
            CbbDeskSnapshotDTO cbbDeskSnapshotDTO = cbbVDIDeskSnapshotAPI.findDeskSnapshotInfoByName(name);
            if (cbbDeskSnapshotDTO != null) {
                LOGGER.info("检查云桌面快照名称是否重复: [{}] 重复了", name);
                return true;
            }
        } catch (BusinessException e) {
            LOGGER.info("检查云桌面快照名称是否重复: ", e.getI18nMessage());
        }
        return false;
    }

    @Override
    public MaxSnapshotsRangeResponse getMaxSnapshotsRange() {
        MaxSnapshotsRangeResponse maxSnapshotsRangeResponse = new MaxSnapshotsRangeResponse();
        //提取所有云桌面策略已配置的用户快照数量最大数量，如果为0则使用MIN_SNAPSHOT_NUM
        int min = MIN_SNAPSHOT_NUM;
        maxSnapshotsRangeResponse.setMaxSnapshot(rccpMaxSnapshots);
        maxSnapshotsRangeResponse.setMinSnapshot(min);
        return maxSnapshotsRangeResponse;
    }

    @Override
    public Integer getMaxSnapshots() {
        // 去查询数据库
        String maxSnapshotNum = globalParameterAPI.findParameter(MAX_SNAPSHOT_NUM_KEY);
        if (StringUtils.isEmpty(maxSnapshotNum)) {
            // 未查到数据库中配置信息，返回RCCP底层最大限制数
            LOGGER.info("数据库未找到快照最大容量配置[{}]，默认返回快照最大容量为[{}]", MAX_SNAPSHOT_NUM_KEY, rccpMaxSnapshots);
            return rccpMaxSnapshots;
        }

        return Integer.parseInt(maxSnapshotNum);
    }

    @Override
    public void editMaxSnapshots(Integer maxSnapshots) {
        Assert.notNull(maxSnapshots, "maxSnapshots cannot null");
        globalParameterAPI.updateParameter(MAX_SNAPSHOT_NUM_KEY, String.valueOf(maxSnapshots));
    }


    @Override
    public CbbDeskSnapshotDTO getFirstDeskSnapshotByDeskIdAndUserId(UUID deskId, UUID userId) throws BusinessException {
        Assert.notNull(deskId, "deskId cannot null");
        Assert.notNull(userId, "userId cannot null");

        List<CbbDeskSnapshotDTO> cbbDeskSnapshotDTOList =
                cbbVDIDeskSnapshotAPI.listDeskSnapshotInfoByDeskIdAndUserId(deskId, userId);
        if (cbbDeskSnapshotDTOList.isEmpty()) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
            throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_NOT_CREATE, cloudDesktopDetailDTO.getDesktopName());
        }
        CbbDeskSnapshotDTO firstDeskSnapshotDTO = null;
        for (CbbDeskSnapshotDTO cbbDeskSnapshotDTO : cbbDeskSnapshotDTOList) {
            if (firstDeskSnapshotDTO == null) {
                firstDeskSnapshotDTO = cbbDeskSnapshotDTO;
            } else {
                if (cbbDeskSnapshotDTO.getCreateTime().before(firstDeskSnapshotDTO.getCreateTime())) {
                    firstDeskSnapshotDTO = cbbDeskSnapshotDTO;
                }
            }
        }
        return firstDeskSnapshotDTO;
    }

    @Override
    public CbbDeskSnapshotDTO getFirstDeskSnapshotByDeskIdAndUserType(UUID deskId, CbbDeskSnapshotUserType userType) throws BusinessException {
        Assert.notNull(deskId, "deskId cannot null");
        Assert.notNull(userType, "userType cannot null");

        List<CbbDeskSnapshotDTO> cbbDeskSnapshotDTOList =
                cbbVDIDeskSnapshotAPI.listDeskSnapshotInfoByDeskIdAndUserType(deskId, userType);
        if (cbbDeskSnapshotDTOList.isEmpty()) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
            throw new BusinessException(DeskSnapshotBusinessKey.RCDC_DESK_SNAPSHOT_NOT_CREATE, cloudDesktopDetailDTO.getDesktopName());
        }
        CbbDeskSnapshotDTO firstDeskSnapshotDTO = null;
        for (CbbDeskSnapshotDTO cbbDeskSnapshotDTO : cbbDeskSnapshotDTOList) {
            if (firstDeskSnapshotDTO == null) {
                firstDeskSnapshotDTO = cbbDeskSnapshotDTO;
            } else {
                if (cbbDeskSnapshotDTO.getCreateTime().before(firstDeskSnapshotDTO.getCreateTime())) {
                    firstDeskSnapshotDTO = cbbDeskSnapshotDTO;
                }
            }
        }
        return firstDeskSnapshotDTO;
    }

    @Override
    public PageQueryResponse<CbbDeskSnapshotDTO> pageQuery(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        PageQueryResponse<ViewDeskSnapshotEntity> deskSnapshotEntityPageQueryResponse = viewDeskSnapshotDAO.pageQuery(request);
        if (deskSnapshotEntityPageQueryResponse == null || ObjectUtils.isEmpty(deskSnapshotEntityPageQueryResponse.getItemArr())) {
            return new PageQueryResponse<>(new CbbDeskSnapshotDTO[0], 0);
        }
        CbbDeskSnapshotDTO[] deskSnapshotDTOArr = Stream.of(deskSnapshotEntityPageQueryResponse.getItemArr())
                .map(viewDeskSnapshotEntity -> {
                    CbbDeskSnapshotDTO cbbDeskSnapshotDTO = new CbbDeskSnapshotDTO();
                    cbbDeskSnapshotDTO.setDeskId(viewDeskSnapshotEntity.getDeskId());
                    cbbDeskSnapshotDTO.setId(viewDeskSnapshotEntity.getId());
                    cbbDeskSnapshotDTO.setName(viewDeskSnapshotEntity.getName());
                    cbbDeskSnapshotDTO.setState(viewDeskSnapshotEntity.getState());
                    cbbDeskSnapshotDTO.setCreateTime(viewDeskSnapshotEntity.getCreateTime());
                    cbbDeskSnapshotDTO.setUserId(viewDeskSnapshotEntity.getUserId());
                    cbbDeskSnapshotDTO.setUserType(viewDeskSnapshotEntity.getUserType());
                    cbbDeskSnapshotDTO.setPlatformId(viewDeskSnapshotEntity.getPlatformId());
                    cbbDeskSnapshotDTO.setPlatformType(viewDeskSnapshotEntity.getPlatformType());
                    cbbDeskSnapshotDTO.setPlatformName(viewDeskSnapshotEntity.getPlatformName());
                    cbbDeskSnapshotDTO.setPlatformStatus(viewDeskSnapshotEntity.getPlatformStatus());
                    return cbbDeskSnapshotDTO;
                }).toArray(CbbDeskSnapshotDTO[]::new);

        return new PageQueryResponse<>(deskSnapshotDTOArr, deskSnapshotEntityPageQueryResponse.getTotal());
    }
}
