package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportCloudDesktopDTO;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import mockit.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(SkyEngineRunner.class)
public class ExportUtilsTest {

    @Tested
    private ExportUtils exportUtils;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Injectable
    private LocaleI18nResolver i18nResolver;

    @Test
    public void testGenerateExcel() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("data is not null");
        exportUtils.generateExcel(null, "",null);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("outputFilePath is not null");
        exportUtils.generateExcel(Lists.newArrayList(), null,null);


        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("outputFilePath is not null");
        exportUtils.generateExcel(Lists.newArrayList(), "",null);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("headerClass is not null");
        exportUtils.generateExcel(Lists.newArrayList(), "test",null);
        Assert.assertTrue(true);
    }

    @Test
    public void testGenerateExcelSuccess() throws Exception {
        new MockUp<LocaleI18nResolver>() {
            @Mock
            public String resolve(String key, String... args) {
                return key;
            }
        };
        exportUtils.generateExcel(generateExportData(), "test",ExportCloudDesktopDTO.class);
        Assert.assertTrue(true);
    }

    @Test
    public void testGenerateExcelSuccess2() throws Exception {
        new MockUp<LocaleI18nResolver>() {
            @Mock
            public String resolve(String key, String... args) {
                return key;
            }
        };

        exportUtils.generateExcel(new ArrayList<>(), "test",ExportCloudDesktopDTO.class);
        Assert.assertTrue(true);
    }



    public static List<ExportCloudDesktopDTO> generateExportData() {
        List<ExportCloudDesktopDTO> resultList = Lists.newArrayList();
        String json = "{\n" + "                \"cbbId\": \"8f82e979-76b2-4383-9203-68844391d0ee\",\n" + "                \"computerName\": null,\n"
                + "                \"cpu\": 2,\n" + "                \"createTime\": 1600139198755,\n" + "                \"deleteTime\": null,\n"
                + "                \"desktopCategory\": \"VDI\",\n" + "                \"desktopIp\": \"172.28.84.38\",\n"
                + "                \"desktopMac\": \"52:54:00:41:60:72\",\n" + "                \"desktopName\": \"22222\",\n"
                + "                \"desktopRole\": \"NORMAL\",\n" + "                \"desktopState\": \"CLOSE\",\n"
                + "                \"desktopType\": \"PERSONAL\",\n" + "                \"faultDescription\": null,\n"
                + "                \"faultState\": false,\n" + "                \"faultTime\": null,\n"
                + "                \"id\": \"8f82e979-76b2-4383-9203-68844391d0ee\",\n" + "                \"imageName\": \"vdi-test\",\n"
                + "                \"isWindowsOsActive\": false,\n" + "                \"latestLoginTime\": null,\n"
                + "                \"memory\": 2,\n" + "                \"osName\": \"WIN_10_64\",\n" + "                \"personDisk\": 60,\n"
                + "                \"physicalServerId\": null,\n" + "                \"physicalServerIp\": null,\n"
                + "                \"realName\": \"222222\",\n" + "                \"systemDisk\": 100,\n"
                + "                \"terminalGroup\": null,\n" + "                \"terminalIp\": null,\n"
                + "                \"terminalName\": null,\n" + "                \"terminalPlatform\": null,\n"
                + "                \"userGroup\": \"office\",\n" + "                \"userGroupId\": \"fb9ece6d-d5f2-4423-9a7f-6645cdba57fa\",\n"
                + "                \"userId\": \"ada408de-002d-42d7-bf44-4d537f071720\",\n" + "                \"userName\": \"22222\",\n"
                + "                \"userType\": \"NORMAL\",\n" + "                \"windowsOsActive\": false\n" + "            }";
        IntStream.range(0, 100).forEach(index -> {
            ExportCloudDesktopDTO dto = JSON.parseObject(json, ExportCloudDesktopDTO.class);
            resultList.add(dto);
        });
        return resultList;
    }
}
