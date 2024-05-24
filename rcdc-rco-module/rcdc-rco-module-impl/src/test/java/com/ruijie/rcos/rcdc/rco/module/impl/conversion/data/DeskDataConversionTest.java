package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月09日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class DeskDataConversionTest {
    @Tested
    private DeskDataConversion deskDataConversion;

    @Injectable
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    /**
     * 测试获取idArray方法
     */
    @Test
    public void testGetIdArrayFromScheduleData() {
        UUID[] deskArr = new UUID[] {UUID.randomUUID()};
        ScheduleDataDTO<UUID, String> from = new ScheduleDataDTO<>();
        from.setDeskArr(deskArr);
        Assert.assertEquals(deskDataConversion.getIdArrayFromScheduleData(from), deskArr);
    }

    /**
     * 测试设置方法
     * 
     * @param idLabelEntry idLabelEntry
     */
    @Test
    public void testSetDataToScheduleDataDTO(@Injectable IdLabelEntry idLabelEntry) {
        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to = new ScheduleDataDTO<>();
        List<IdLabelEntry> idLabelEntryList = Arrays.asList(idLabelEntry);

        deskDataConversion.setDataToScheduleDataDTO(to, idLabelEntryList);
        Assert.assertEquals(to.getDeskArr().length, idLabelEntryList.size());
        Assert.assertEquals(to.getDeskArr()[0], idLabelEntryList.get(0));
    }

    /**
     * 测试ObtainIdLabelEntry方法
     * 
     * @throws Exception 异常
     */
    @Test
    public void testObtainIdLabelEntryBy() throws Exception {
        UUID id = UUID.randomUUID();
        ThrowExceptionTester.throwIllegalArgumentException(() -> deskDataConversion.obtainIdLabelEntryBy(null), "id can not be null");

        CbbDeskDTO cbbDeskDTO = new CbbDeskDTO();
        cbbDeskDTO.setDeskId(id);
        cbbDeskDTO.setName("name");
        new Expectations() {
            {
                cbbVDIDeskMgmtAPI.getDeskVDI((UUID) any);
                result = cbbDeskDTO;
            }
        };
        IdLabelEntry idLabelEntry = deskDataConversion.obtainIdLabelEntryBy(id);
        Assert.assertEquals(idLabelEntry.getId(), cbbDeskDTO.getDeskId());
        Assert.assertEquals(idLabelEntry.getLabel(), cbbDeskDTO.getName());
        new Verifications() {
            {
                cbbVDIDeskMgmtAPI.getDeskVDI((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 测试是否支持方法
     * 
     * @throws Exception 异常
     */
    @Test
    public void testIsSupport() throws Exception {
        ScheduleDataDTO<UUID, String> from = new ScheduleDataDTO<>();
        ThrowExceptionTester.throwIllegalArgumentException(() -> deskDataConversion.isSupport(null), "from can not be null");
        Assert.assertFalse(deskDataConversion.isSupport(from));
        from.setDeskArr(new UUID[] {UUID.randomUUID()});
        Assert.assertTrue(deskDataConversion.isSupport(from));

    }



}
