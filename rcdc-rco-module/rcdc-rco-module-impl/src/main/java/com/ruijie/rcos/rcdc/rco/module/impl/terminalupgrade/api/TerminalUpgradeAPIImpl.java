package com.ruijie.rcos.rcdc.rco.module.impl.terminalupgrade.api;

import java.util.*;

import com.ruijie.rcos.rcdc.rco.module.def.enums.TerminalSupportVdiAndIdvEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalUpgradeAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalSystemUpgradeAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalSystemUpgradePackageAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalSystemUpgradePackageInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbUpgradeableTerminalListDTO;
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

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/15 11:45
 *
 * @author ketb
 */
public class TerminalUpgradeAPIImpl implements TerminalUpgradeAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalUpgradeAPIImpl.class);

    private static final String PARAM_FIELD_PACKAGE_ID = "packageId";

    private static final String PARAM_FIELD_GROUP_ID = "groupId";

    private static final String PARAM_FIELD_TERMINAL_GROUP_ID_ARR = "terminalGroupIdArr";

    private static final String PARAM_PRODUCT_TYPE_3120 = "RG-CT3120";

    private static final String PARAM_PRODUCT_TYPE_3100_G2 = "RG-CT3100-G2";

    private static final String PARAM_PRODUCT_TYPE_3100C_G2 = "RG-CT3100C-G2";

    private static final String PARAM_PRODUCT_TYPE_3100L_G2 = "RG-CT3100L-G2";

    @Autowired
    private CbbTerminalSystemUpgradePackageAPI cbbTerminalSystemUpgradePackageAPI;

    @Autowired
    private CbbTerminalSystemUpgradeAPI cbbTerminalSystemUpgradeAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;

    @Override
    public PageQueryResponse<CbbUpgradeableTerminalListDTO> findUpgradeableTerminals(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request not be null");
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder()
                .setPageLimit(request.getPage(), request.getLimit());
        //升级包ID
        UUID packageId = getPackageId(request);
        //终端组ID
        UUID groupId = getGroupId(request);
        //终端组集合
        Object[]  groupIdArr = terminalGroupIdArr( request);
        //查询升级包 判断平台类型
        CbbTerminalSystemUpgradePackageInfoDTO packageInfoDTO = cbbTerminalSystemUpgradePackageAPI.findById(packageId);

        LOGGER.info("查询升级包详情信息:[{}]", JSON.toJSONString(packageInfoDTO));

        // 兼容旧版本OTA包
        if (Objects.isNull(packageInfoDTO.getBoot())) {
            LOGGER.info("老版本ota包结合使用platForm字段进行匹配");
            matchPlatForm(requestBuilder, packageInfoDTO.getPackageType().getPlatform());
        }

        // 由于RPM CPU 等终端问题 需要添加额外通用查询
        requestBuilder.composite(criteriaBuilder -> {
            List<Match> matchList = Lists.newArrayList();
            matchList.add(criteriaBuilder.eq("terminalOsType", packageInfoDTO.getPackageType().getOsType()));
            // RCDC 为空不赋值 由底层抛出空指针异常
            if (Objects.nonNull(packageInfoDTO.getCpuArch())) {
                matchList.add(criteriaBuilder.eq("cpuArch", packageInfoDTO.getCpuArch()));
            }

            // 非支持所有类型CPU需要添加cpu匹配参数
            if (packageInfoDTO.getSupportCpu() != null && !packageInfoDTO.getSupportCpu().equals("ALL")) {
                LOGGER.info("非支持所有类型CPU需要添加cpu匹配参数:[{}]", JSON.toJSONString(packageInfoDTO));
                matchList.add(criteriaBuilder.in("upgradeCpuType", packageInfoDTO.getSupportCpu().split(",")));
            }

            // boot类型匹配
            if (Objects.nonNull(packageInfoDTO.getBoot())) {
                LOGGER.info("匹配boot类型:[{}]", packageInfoDTO.getBoot());
                String[] bootsArr = packageInfoDTO.getBoot().split(",");
                matchList.add(criteriaBuilder.in("boot", bootsArr));
            }
            return criteriaBuilder.and(matchList.stream().toArray(Match[]::new));
        });
        //搜索终端名称/IP
        if (StringUtils.isNotEmpty(request.getSearchKeyword())) {
            requestBuilder.composite(criteriaBuilder -> {
                List<Match> matchList = Lists.newArrayList();
                matchList.add(criteriaBuilder.like("terminalName", request.getSearchKeyword()));
                matchList.add(criteriaBuilder.like("ip", request.getSearchKeyword()));
                return criteriaBuilder.or(matchList.stream().toArray(Match[]::new));
            });
        }
        // 终端组ID不为空 进行查询
        if (groupId != null) {
            requestBuilder.composite(criteriaBuilder -> {
                List<Match> matchList = Lists.newArrayList();
                matchList.add(criteriaBuilder.eq(PARAM_FIELD_GROUP_ID, groupId));
                return criteriaBuilder.and(matchList.stream().toArray(Match[]::new));
            });
        } else  if (ArrayUtils.isNotEmpty(groupIdArr)) {
            //拥有终端组权限不为空 终端组数组查询
            requestBuilder.composite(criteriaBuilder -> {
                List<Match> matchList = Lists.newArrayList();
                matchList.add(criteriaBuilder.in(PARAM_FIELD_GROUP_ID, groupIdArr));
                return criteriaBuilder.and(matchList.stream().toArray(Match[]::new));
            });
        }
        PageQueryRequest pageQueryRequest = requestBuilder.build();
        LOGGER.info("查询刷机终端参数：{}", JSON.toJSONString(pageQueryRequest));
        return cbbTerminalSystemUpgradeAPI.pageQuery(pageQueryRequest);
    }


    private void  matchPlatForm(PageQueryBuilderFactory.RequestBuilder requestBuilder, String platForm) {
        if (CbbTerminalPlatformEnums.convert(platForm) == CbbTerminalPlatformEnums.IDV) {
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
                // 胖终端可支持部署成瘦终端使用，刷机包还是用胖终端的刷机包
                for (String terminal : cbbTerminalConfigAPI.getMultiModeProductTypeList()) {
                    matchList.add(criteriaBuilder.eq("productType", terminal));
                }
                // 胖终端部署成VDI模式也允许使用IDV/TCI的iso刷机包
                LOGGER.info("matchList:{}", JSON.toJSONString(matchList));
                return criteriaBuilder.or(matchList.stream().toArray(Match[]::new));
            });
        } else if (CbbTerminalPlatformEnums.convert(platForm) == CbbTerminalPlatformEnums.VDI) {
            requestBuilder.composite(criteriaBuilder -> {
                List<Match> matchList = Lists.newArrayList();
                matchList.add(criteriaBuilder.neq("productType", PARAM_PRODUCT_TYPE_3120));
                matchList.add(criteriaBuilder.neq("productType", PARAM_PRODUCT_TYPE_3100_G2));
                matchList.add(criteriaBuilder.neq("productType", PARAM_PRODUCT_TYPE_3100C_G2));
                matchList.add(criteriaBuilder.neq("productType", PARAM_PRODUCT_TYPE_3100L_G2));
                // 胖终端可支持部署成瘦终端使用，刷机包还是用胖终端的刷机包
                for (String terminal : cbbTerminalConfigAPI.getMultiModeProductTypeList()) {
                    matchList.add(criteriaBuilder.neq("productType", terminal));
                }
                return criteriaBuilder.and(matchList.stream().toArray(Match[]::new));
            });
        }
    }

    /**
     * 获取升级包ID
     * @param request
     * @return
     */
    private UUID getPackageId(PageSearchRequest request) {
        if (ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            // 若参数未传，则为空
            return null;
        }

        UUID packageId = null;
        List<MatchEqual> matchEqualList = new ArrayList<>(Arrays.asList(request.getMatchEqualArr()));
        for (Iterator<MatchEqual> iterator = matchEqualList.listIterator(); iterator.hasNext();) {
            MatchEqual matchEqual = iterator.next();
            if (PARAM_FIELD_PACKAGE_ID.equals(matchEqual.getName()) && matchEqual.getValueArr() != null && matchEqual.getValueArr()[0] != null) {
                packageId = UUID.fromString(matchEqual.getValueArr()[0].toString());
            }
        }
        return packageId;
    }

    /**
     * 获取终端组ID
     * @param request
     * @return
     */
    private UUID getGroupId(PageSearchRequest request) {
        if (ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            // 若参数未传，则为空
            return null;
        }
        UUID groupId = null;
        List<MatchEqual> matchEqualList = new ArrayList<>(Arrays.asList(request.getMatchEqualArr()));
        for (Iterator<MatchEqual> iterator = matchEqualList.listIterator(); iterator.hasNext();) {
            MatchEqual matchEqual = iterator.next();
            LOGGER.info("matchEqual.getName:{}", matchEqual.getName());
            if (PARAM_FIELD_GROUP_ID.equals(matchEqual.getName()) && matchEqual.getValueArr() != null && matchEqual.getValueArr()[0] != null) {
                groupId = UUID.fromString(matchEqual.getValueArr()[0].toString());
            }
        }
        return groupId;
    }

    /**
     * 获取终端组ID
     * @param request
     * @return
     */
    private Object[] terminalGroupIdArr(PageSearchRequest request) {
        if (ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            // 若参数未传，则为空
            return new Object[0];
        }
        Object[] valueArr = null;
        List<MatchEqual> matchEqualList = new ArrayList<>(Arrays.asList(request.getMatchEqualArr()));
        for (Iterator<MatchEqual> iterator = matchEqualList.listIterator(); iterator.hasNext();) {
            MatchEqual matchEqual = iterator.next();
            LOGGER.debug("matchEqual.getName:{}", matchEqual.getName());
            if (PARAM_FIELD_TERMINAL_GROUP_ID_ARR.equals(matchEqual.getName()) && matchEqual.getValueArr() != null) {
                valueArr = matchEqual.getValueArr();
            }
        }
        return valueArr;
    }
}
