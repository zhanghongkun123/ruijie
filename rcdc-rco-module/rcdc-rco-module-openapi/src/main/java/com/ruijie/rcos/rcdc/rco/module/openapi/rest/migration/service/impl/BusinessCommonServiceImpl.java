package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskPersonalConfigStrategyType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.rco.module.def.api.CloudDeskComputerNameConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyMigrationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyMigrationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.service.BusinessCommonService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.kernel.request.DefaultPageQueryRequest;
import com.ruijie.rcos.sk.pagekit.kernel.request.criteria.DefaultExactMatch;
import com.ruijie.rcos.sk.pagekit.kernel.request.criteria.DefaultFuzzyMatch;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author chenl
 */
@Service
public class BusinessCommonServiceImpl implements BusinessCommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessCommonServiceImpl.class);

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CloudDeskComputerNameConfigAPI cloudDeskComputerNameConfigAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private DeskStrategyMigrationAPI deskStrategyMigrationAPI;


    // 计算机名称前缀
    private final static String PREFIX_COMPUTER_NAME = "PC";

    private final static int QUERY_LIMIT = 20;

    /**
     * 根据策略id缓存对应的软件列表，缓存10分钟
     */
    private static final Cache<UUID, CbbDeskStrategyIDVDTO> DESK_STRATEGY_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();


    /**
     * @param request 旧平台的桌面策略参数
     * @param preName 桌面策略名称前缀
     * @return UUID 桌面策略ID
     * @throws BusinessException 业务异常
     */
    public UUID getRepeatDeskStrategy(CbbDeskStrategyIDVDTO request, String preName) throws BusinessException {
        Assert.notNull(preName, "preName not null");
        Assert.notNull(request, "CbbDeskStrategyIDVDTO not null");
        UUID deskStrategyId = null;
        DefaultPageQueryRequest pageQueryRequest = new DefaultPageQueryRequest();
        pageQueryRequest.setLimit(QUERY_LIMIT);
        List<Match> defaultExactMatchList = new ArrayList<>();
        defaultExactMatchList.add(DefaultExactMatch.eq("systemSize", request.getSystemSize()));
        defaultExactMatchList.add(DefaultExactMatch.eq("strategyType", CbbStrategyType.IDV.name()));
        defaultExactMatchList.add(DefaultExactMatch.eq("isAllowLocalDisk", request.getAllowLocalDisk()));
        defaultExactMatchList.add(DefaultExactMatch.eq("isOpenDesktopRedirect", request.getOpenDesktopRedirect()));
        defaultExactMatchList.add(DefaultExactMatch.eq("pattern", request.getPattern()));
        defaultExactMatchList.add(DefaultFuzzyMatch.likeRight(preName, "name"));
        pageQueryRequest.setMatchArr(defaultExactMatchList.toArray(new Match[0]));
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);
        PageQueryResponse<DeskStrategyMigrationDTO> pageQueryResponse = deskStrategyMigrationAPI.pageDeskStrategyQuery(builder.build());

        if (ArrayUtils.isNotEmpty(pageQueryResponse.getItemArr())) {
            UUID existDeskStrategyId;
            CbbDeskStrategyIDVDTO deskStrategyIDVDTO;
            for (DeskStrategyMigrationDTO deskStrategyDTO : pageQueryResponse.getItemArr()) {
                existDeskStrategyId = deskStrategyDTO.getId();
                deskStrategyIDVDTO = DESK_STRATEGY_CACHE.getIfPresent(existDeskStrategyId);
                if (deskStrategyIDVDTO == null) {
                    deskStrategyIDVDTO = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(existDeskStrategyId);
                    DESK_STRATEGY_CACHE.put(existDeskStrategyId, deskStrategyIDVDTO);
                }
                UUID[] usbTypeIdArr = deskStrategyIDVDTO.getUsbTypeIdArr();
                if (checkDeskStrategyDefault(deskStrategyIDVDTO) && compareUsbTypeIdArr(request.getUsbTypeIdArr(), usbTypeIdArr)) {
                    deskStrategyId = deskStrategyDTO.getId();
                    break;
                }
            }
        }
        return deskStrategyId;
    }

    /**
     * 对比usb外设是否一样
     *
     * @param sourceUsbIdArr
     * @param targetUsbIdArr
     * @return
     */
    private Boolean compareUsbTypeIdArr(UUID[] sourceUsbIdArr, UUID[] targetUsbIdArr) {
        Boolean isSame = true;
        if (sourceUsbIdArr == null && targetUsbIdArr == null) {
            return true;

        } else if ((sourceUsbIdArr == null && targetUsbIdArr != null)
                || (sourceUsbIdArr != null && targetUsbIdArr == null)) {
            return false;

        } else {
            if (sourceUsbIdArr.length == targetUsbIdArr.length) {
                //都是空数组，则返回true
                if (sourceUsbIdArr.length == NumberUtils.INTEGER_ZERO) {
                    return true;
                }
                Map<UUID, UUID> sourceUsbIdMap = Arrays.stream(sourceUsbIdArr).collect(Collectors.toMap(uuid -> uuid, uuid -> uuid));
                for (UUID targetId : targetUsbIdArr) {
                    if (sourceUsbIdMap.get(targetId) == null) {
                        isSame = false;
                        break;
                    }
                }
            } else {
                isSame = false;
            }
        }
        return isSame;
    }

    /**
     * 判断旧平台策略迁移过来后，新平台新建的值没有改动过
     *
     * @param deskStrategyIDVDTO
     * @return
     */
    private Boolean checkDeskStrategyDefault(CbbDeskStrategyIDVDTO deskStrategyIDVDTO) throws BusinessException {
        String computerName = cloudDeskComputerNameConfigAPI.findCloudDeskComputerName(deskStrategyIDVDTO.getId());
        return PREFIX_COMPUTER_NAME.equals(computerName) // 桌面计算机名前缀
                // 对于迁移来说本地盘跟重定向已一样的
//                && (BooleanUtils.toBoolean(deskStrategyIDVDTO.getAllowLocalDisk())
//                  == BooleanUtils.toBoolean(deskStrategyIDVDTO.getOpenDesktopRedirect()))
                && BooleanUtils.isFalse(deskStrategyIDVDTO.getOpenUsbReadOnly()) // USB存储设备只读
                && BooleanUtils.isFalse(deskStrategyIDVDTO.getEnableNested()) // 启用嵌套虚拟化
                && StringUtils.EMPTY.equals(deskStrategyIDVDTO.getAdOu()); //AD域加入OU路径
    }

    /**
     *
     * @param cbbCloudDeskPattern 桌面类型
     * @return CbbDeskPersonalConfigStrategyType
     */
    public CbbDeskPersonalConfigStrategyType getCbbDeskPersonalConfigStrategyType(CbbCloudDeskPattern cbbCloudDeskPattern) {
        if (cbbCloudDeskPattern == CbbCloudDeskPattern.RECOVERABLE || cbbCloudDeskPattern == CbbCloudDeskPattern.PERSONAL) {
            return CbbDeskPersonalConfigStrategyType.USE_DATA_DISK;
        }
        return CbbDeskPersonalConfigStrategyType.NOT_USE;
    }
}
