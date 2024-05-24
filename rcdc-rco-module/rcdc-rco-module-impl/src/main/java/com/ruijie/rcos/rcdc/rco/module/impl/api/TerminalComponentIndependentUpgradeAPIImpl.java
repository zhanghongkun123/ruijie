package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalComponentIndependentUpgradeAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalComponentIndependentUpgradeAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalComponentIndependentUpgradePackageAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbIndependentUpgradeableTerminalListDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalComponentIndependentUpgradePackageInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.MatchEqual;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Description: 组件独立升级业务API实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/1
 *
 * @author lyb
 */
public class TerminalComponentIndependentUpgradeAPIImpl implements TerminalComponentIndependentUpgradeAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalComponentIndependentUpgradeAPIImpl.class);

    private static final String PARAM_FIELD_PACKAGE_ID = "packageId";

    private static final String PARAM_FIELD_GROUP_ID = "groupId";

    private static final String PARAM_FIELD_TERMINAL_GROUP_ID_ARR = "terminalGroupIdArr";

    private static final String PARAM_PRODUCT_TYPE_3120 = "RG-CT3120";

    private static final String PARAM_PRODUCT_TYPE_3100_G2 = "RG-CT3100-G2";

    private static final String PARAM_PRODUCT_TYPE_3100C_G2 = "RG-CT3100C-G2";

    private static final String PARAM_PRODUCT_TYPE_3100L_G2 = "RG-CT3100L-G2";

    private static final String[] PARAM_PRODUCT_TYPE_FOR_OLD_VDI_ARR = new String[] {"RG-Rain300W", "RG-Rain400W", "RG-Rain400W V2"};

    private static final String PARAM_PRODUCT_TYPE_FOR_400W_V2 = "RG-Rain400W V2";

    private static final String PARAM_PRODUCT_TYPE_FOR_400W = "RG-Rain400W";

    private static final String PARAM_PRODUCT_TYPE_FOR_300W = "RG-Rain300W";

    private static final String TERMINAL_OS_TYPE = "terminalOsType";

    private static final String TERMINAL_NAME = "terminalName";

    private static final String CPU_ARCH = "cpuArch";

    private static final String PLATFORM = "platform";

    private static final String PRODUCT_TYPE = "productType";

    private static final String UPGRADE_CPU_TYPE = "upgradeCpuType";

    private static final String IP = "ip";

    @Autowired
    private CbbTerminalComponentIndependentUpgradePackageAPI componentIndependentUpgradePackageAPI;

    @Autowired
    private CbbTerminalComponentIndependentUpgradeAPI componentIndependentUpgradeAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Override
    public PageQueryResponse<CbbIndependentUpgradeableTerminalListDTO> findUpgradeableTerminals(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request not be null");
        PageQueryBuilderFactory.RequestBuilder requestBuilder =
                pageQueryBuilderFactory.newRequestBuilder().setPageLimit(request.getPage(), request.getLimit());
        UUID packageId = getPackageId(request);
        UUID groupId = getGroupId(request);
        Object[] groupIdArr = terminalGroupIdArr(request);
        CbbTerminalComponentIndependentUpgradePackageInfoDTO packageInfoDTO =
                componentIndependentUpgradePackageAPI.findPackageInfoByPackageId(packageId);
        if (CbbTerminalPlatformEnums.convert(packageInfoDTO.getPackageType().getPlatform()) == CbbTerminalPlatformEnums.IDV) {
            requestBuilder.composite(criteriaBuilder -> {
                List<Match> matchList = Lists.newArrayList();
                // 如果后续办公也有voi，这里需要添加voi的查询
                Object[] platformArr = new Object[2];
                platformArr[0] = CbbTerminalPlatformEnums.IDV;
                platformArr[1] = CbbTerminalPlatformEnums.VOI;
                matchList.add(criteriaBuilder.in("platform", platformArr));
                matchList.add(criteriaBuilder.eq("productType", PARAM_PRODUCT_TYPE_3120));
                matchList.add(criteriaBuilder.eq("productType", PARAM_PRODUCT_TYPE_3100_G2));
                matchList.add(criteriaBuilder.eq("productType", PARAM_PRODUCT_TYPE_3100C_G2));
                matchList.add(criteriaBuilder.eq("productType", PARAM_PRODUCT_TYPE_3100L_G2));
                LOGGER.info("matchList:{}", JSON.toJSONString(matchList));
                return criteriaBuilder.or(matchList.toArray(new Match[0]));
            });
        }
        // VOI 由于 升级包没有VOI 类型 所以不需要单独处理
        if (CbbTerminalPlatformEnums.convert(packageInfoDTO.getPackageType().getPlatform()) == CbbTerminalPlatformEnums.VDI) {
            requestBuilder.composite(criteriaBuilder -> {
                List<Match> matchList = Lists.newArrayList();
                matchList.add(criteriaBuilder.eq("platform", CbbTerminalPlatformEnums.VDI));
                matchList.add(criteriaBuilder.neq("productType", PARAM_PRODUCT_TYPE_3120));
                matchList.add(criteriaBuilder.neq("productType", PARAM_PRODUCT_TYPE_3100_G2));
                matchList.add(criteriaBuilder.neq("productType", PARAM_PRODUCT_TYPE_3100C_G2));
                matchList.add(criteriaBuilder.neq("productType", PARAM_PRODUCT_TYPE_3100L_G2));

                return criteriaBuilder.and(matchList.toArray(new Match[0]));
            });
        }

        // 由于RPM CPU 等终端问题 需要添加额外通用查询
        requestBuilder.composite(criteriaBuilder -> {
            List<Match> matchList = Lists.newArrayList();
            if (Objects.nonNull(packageInfoDTO.getPackageType())) {
                matchList.add(criteriaBuilder.eq(TERMINAL_OS_TYPE, packageInfoDTO.getPackageType().getOsType()));
            }
            // RCDC 为空不赋值 由底层抛出空指针异常
            if (Objects.nonNull(packageInfoDTO.getCpuArch())) {
                matchList.add(criteriaBuilder.eq(CPU_ARCH, packageInfoDTO.getCpuArch()));
            }

            // 非支持所有类型CPU需要添加cpu匹配参数
            if (Objects.nonNull(packageInfoDTO.getSupportCpu()) && !packageInfoDTO.getSupportCpu().equals("ALL")) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("非支持所有类型CPU需要添加cpu匹配参数:[{}]", JSON.toJSONString(packageInfoDTO));
                }

                matchList.add(criteriaBuilder.in(UPGRADE_CPU_TYPE, packageInfoDTO.getSupportCpu().split(",")));
            }
            return criteriaBuilder.and(matchList.toArray(new Match[0]));
        });

        if (StringUtils.isNotBlank(request.getSearchKeyword())) {
            requestBuilder.composite(criteriaBuilder -> {
                List<Match> matchList = Lists.newArrayList();
                matchList.add(criteriaBuilder.like(TERMINAL_NAME, request.getSearchKeyword()));
                matchList.add(criteriaBuilder.like(IP, request.getSearchKeyword()));
                return criteriaBuilder.or(matchList.toArray(new Match[0]));
            });
        }

        // 终端组ID不为空 进行查询
        if (groupId != null) {
            requestBuilder.composite(criteriaBuilder -> {
                List<Match> matchList = Lists.newArrayList();
                matchList.add(criteriaBuilder.eq(PARAM_FIELD_GROUP_ID, groupId));
                return criteriaBuilder.and(matchList.toArray(new Match[0]));
            });
        } else if (ArrayUtils.isNotEmpty(groupIdArr)) {
            // 拥有终端组权限不为空 终端组数组查询
            requestBuilder.composite(criteriaBuilder -> {
                List<Match> matchList = Lists.newArrayList();
                matchList.add(criteriaBuilder.in(PARAM_FIELD_GROUP_ID, groupIdArr));
                return criteriaBuilder.and(matchList.toArray(new Match[0]));
            });
        }

        PageQueryRequest pageQueryRequest = requestBuilder.build();
        return componentIndependentUpgradeAPI.pageQuery(pageQueryRequest);
    }

    private UUID getGroupId(PageSearchRequest request) {
        if (ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            // 若参数未传，则为空
            return null;
        }

        UUID groupId = null;
        List<MatchEqual> matchEqualList = new ArrayList<>(Arrays.asList(request.getMatchEqualArr()));
        for (MatchEqual matchEqual : matchEqualList) {
            if (PARAM_FIELD_GROUP_ID.equals(matchEqual.getName()) && Objects.nonNull(matchEqual.getValueArr())
                    && Objects.nonNull(matchEqual.getValueArr()[0])) {
                groupId = UUID.fromString(matchEqual.getValueArr()[0].toString());
            }
        }
        return groupId;
    }

    private Object[] terminalGroupIdArr(PageSearchRequest request) {
        if (ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            // 若参数未传，则为空
            return null;
        }
        Object[] valueArr = null;
        List<MatchEqual> matchEqualList = new ArrayList<>(Arrays.asList(request.getMatchEqualArr()));
        for (MatchEqual matchEqual : matchEqualList) {
            if (PARAM_FIELD_TERMINAL_GROUP_ID_ARR.equals(matchEqual.getName()) && Objects.nonNull(matchEqual.getValueArr())) {
                valueArr = matchEqual.getValueArr();
            }
        }
        return valueArr;
    }

    private UUID getPackageId(PageSearchRequest request) {
        if (ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            // 若参数未传，则为空
            return null;
        }

        UUID packageId = null;
        List<MatchEqual> matchEqualList = new ArrayList<>(Arrays.asList(request.getMatchEqualArr()));
        for (MatchEqual matchEqual : matchEqualList) {
            if (PARAM_FIELD_PACKAGE_ID.equals(matchEqual.getName()) && Objects.nonNull(matchEqual.getValueArr())
                    && Objects.nonNull(matchEqual.getValueArr()[0])) {
                packageId = UUID.fromString(matchEqual.getValueArr()[0].toString());
            }
        }
        return packageId;
    }
}
