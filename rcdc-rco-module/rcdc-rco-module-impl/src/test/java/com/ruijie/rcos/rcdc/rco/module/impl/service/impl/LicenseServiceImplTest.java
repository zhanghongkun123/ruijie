package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.Maps;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbWindowsLicenseAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.license.LicenseInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.LicenseGlobal;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoGlobalParameterDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.LicenseActiveDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.LicenseActiveResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.LicenseNumDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLicenseMgmtAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * test
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月20日
 *
 * @author lin
 */
@RunWith(SkyEngineRunner.class)
public class LicenseServiceImplTest {

    @Tested
    private LicenseServiceImpl licenseService;

    @Injectable
    private CbbDeskLicenseMgmtAPI cbbLicenseMgmtAPI;

    @Injectable
    private CbbWindowsLicenseAPI cbbWindowsLicenseAPI;

    @Injectable
    private Map<String, LicenseNumDTO> licenseMap = Maps.newConcurrentMap();

    @Injectable
    private Map<UUID, Long> tempLicenseMap = Maps.newConcurrentMap();

    @Injectable
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Injectable
    private CbbTerminalLicenseMgmtAPI cbbTerminalLicenseMgmtAPI;

    @Injectable
    private RcoGlobalParameterDAO rcoGlobalParameterDAO;

    private static final int COUNT_ZERO = 0;

    private static final String WIN_10_64 = "WIN_10_64";

    private static final String WIN_10_32 = "WIN_10_32";

    private static final String LICENSE_TYPE_TEMPORARY = "TEMPORARY";

    private static final String LICENSE_STATUS_EXPIRE = "EXPIRED";


    /**
     * checkLoginToken 测试方法
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testCheckLoginToken() throws BusinessException {


        new Expectations() {
            {

            }
        };

        new Verifications() {
            {

            }
        };
    }


    /**
     * matchWindowsLicense 测试方法
     *
     * @throws BusinessException ex
     */
    @Test
    public void testMatchWindowsLicense() throws BusinessException {
        String osProType = "test";
        LicenseActiveDTO licenseActiveDTO = mockWindowsLicense(osProType, null);
        Set<String> windowsLicenseOsProTypeSet = Collections.singleton(osProType);

        new Expectations(LicenseGlobal.class) {
            {
                LicenseGlobal.getWindowsLicenseOsProTypeSet();
                result = windowsLicenseOsProTypeSet;
            }
        };

        LicenseActiveResultDTO response = licenseService.matchWindowsLicense(licenseActiveDTO);

        new Verifications() {
            {
                LicenseGlobal.getWindowsLicenseOsProTypeSet();
                times = 1;

                assertEquals(licenseActiveDTO.getOsProType(), response.getLicenseOsProType());
            }
        };
    }

    /**
     * matchWindowsLicense 测试方法1
     *
     * @throws BusinessException ex
     */
    @Test
    public void testMatchWindowsLicense1() throws BusinessException {
        String osProType = "test";
        String windowsOsProType = "win10Test";
        final LicenseActiveDTO licenseActiveDTO = mockWindowsLicense(osProType, "noWindows10");
        Set<String> windowsLicenseOsProTypeSet = Collections.singleton("win10Test");

        new Expectations(LicenseGlobal.class) {
            {
                LicenseGlobal.getWindowsLicenseOsProTypeSet();
                result = windowsLicenseOsProTypeSet;
            }
        };

        LicenseActiveResultDTO response = licenseService.matchWindowsLicense(licenseActiveDTO);

        new Verifications() {
            {
                LicenseGlobal.getWindowsLicenseOsProTypeSet();
                times = 1;

                assertNull(response.getLicenseOsProType());
            }
        };

        LicenseActiveDTO licenseActiveDTO1 = mockWindowsLicense(osProType, "WIN_10_32");
        LicenseActiveResultDTO response1 = licenseService.matchWindowsLicense(licenseActiveDTO1);

        new Verifications() {
            {
                LicenseGlobal.getWindowsLicenseOsProTypeSet();
                times = 3;

                assertEquals(windowsOsProType, response1.getLicenseOsProType());
            }
        };

        licenseActiveDTO1 = mockWindowsLicense(osProType, "WIN_10_64");
        licenseService.matchWindowsLicense(licenseActiveDTO1);
    }

    /**
     * activeSuccess 测试方法
     *
     * @throws BusinessException ex
     */
    @Test
    public void testActiveSuccess() throws BusinessException {
        licenseService.activeSuccess();

        new Verifications() {
            {
                LicenseGlobal.activeWindowsActiveNum();
                times = 1;
            }
        };
    }

    private LicenseInfoDTO[] mockPerlicense() {
        LicenseInfoDTO[] infoArr = new LicenseInfoDTO[2];
        LicenseInfoDTO info1 = new LicenseInfoDTO();
        info1.setFeatureId("RG-CML-Desktop-VDI");
        info1.setFeatureType("PERPETUAL");
        info1.setLicenseNum(100);
        info1.setId(UUID.randomUUID());
        infoArr[0] = info1;

        LicenseInfoDTO info2 = new LicenseInfoDTO();
        info2.setFeatureId("RG-CMWinPro-EDU");
        info2.setFeatureType("PERPETUAL");
        info2.setLicenseNum(100);
        info2.setId(UUID.randomUUID());
        infoArr[1] = info2;
        return infoArr;
    }

    private LicenseInfoDTO[] mockTemplicense() {
        LicenseInfoDTO[] infoArr = new LicenseInfoDTO[1];
        LicenseInfoDTO info1 = new LicenseInfoDTO();
        info1.setFeatureId("RG-CML-Desktop-VDI");
        info1.setFeatureType("TEMPORARY");
        info1.setFeatureStatus("AVALIABLE");
        info1.setId(UUID.randomUUID());
        info1.setTrialRemainder(100L);
        infoArr[0] = info1;

        return infoArr;
    }

    private LicenseActiveDTO mockWindowsLicense(String osProType, String osType) {
        LicenseActiveDTO licenseActiveDTO = new LicenseActiveDTO();
        licenseActiveDTO.setOsProType(osProType);
        licenseActiveDTO.setOsType(osType);
        return licenseActiveDTO;
    }

}
