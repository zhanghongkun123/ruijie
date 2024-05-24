package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
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
public class TerminalGroupDataConversionTest {
    @Tested
    private TerminalGroupDataConversion terminalGroupDataConversion;

    @Injectable
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    /**
     * 测试获取idArray方法
     */
    @Test
    public void testGetIdArrayFromScheduleData() {
        UUID[] terminalArr = new UUID[] {UUID.randomUUID()};
        ScheduleDataDTO<UUID, String> from = new ScheduleDataDTO<>();
        from.setTerminalGroupArr(terminalArr);
        Assert.assertEquals(terminalGroupDataConversion.getIdArrayFromScheduleData(from), terminalArr);
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

        terminalGroupDataConversion.setDataToScheduleDataDTO(to, idLabelEntryList);
        Assert.assertEquals(to.getTerminalGroupArr().length, idLabelEntryList.size());
        Assert.assertEquals(to.getTerminalGroupArr()[0], idLabelEntryList.get(0));
    }

    /**
     * 测试ObtainIdLabelEntry方法
     *
     * @throws Exception 异常
     */
    @Test
    public void testObtainIdLabelEntryBy() throws Exception {
        UUID id = UUID.randomUUID();
        ThrowExceptionTester.throwIllegalArgumentException(() -> terminalGroupDataConversion.obtainIdLabelEntryBy(null), "id can not be null");

        CbbTerminalGroupDetailDTO terminalGroupDTO = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO.setId(id);
        terminalGroupDTO.setGroupName("name");
        new Expectations() {
            {
                cbbTerminalGroupMgmtAPI.loadById((UUID) any);
                result = terminalGroupDTO;
            }
        };
        IdLabelEntry idLabelEntry = terminalGroupDataConversion.obtainIdLabelEntryBy(id);
        Assert.assertEquals(idLabelEntry.getId(), terminalGroupDTO.getId());
        Assert.assertEquals(idLabelEntry.getLabel(), terminalGroupDTO.getGroupName());
        new Verifications() {
            {
                cbbTerminalGroupMgmtAPI.loadById((UUID) any);
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
        ThrowExceptionTester.throwIllegalArgumentException(() -> terminalGroupDataConversion.isSupport(null), "from can not be null");
        Assert.assertFalse(terminalGroupDataConversion.isSupport(from));
        from.setTerminalGroupArr(new UUID[] {UUID.randomUUID()});
        Assert.assertTrue(terminalGroupDataConversion.isSupport(from));

    }



}
