package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月09日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class UserGroupDataConversionTest {
    @Tested
    private UserGroupDataConversion userGroupDataConversion;

    @Injectable
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    /**
     * 测试获取idArr方法
     */
    @Test
    public void testGetIdArrayFromScheduleData() {
        UUID[] userGroupArr = new UUID[] {UUID.randomUUID()};
        ScheduleDataDTO<UUID, String> from = new ScheduleDataDTO<>();
        from.setUserGroupArr(userGroupArr);
        Assert.assertEquals(userGroupDataConversion.getIdArrayFromScheduleData(from), userGroupArr);

    }

    /**
     * 测试设置数据方法
     *
     * @param idLabelEntry idLabelEntry
     */
    @Test
    public void testSetDataToScheduleDataDTO(@Injectable IdLabelEntry idLabelEntry) {
        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> to = new ScheduleDataDTO<>();
        List<IdLabelEntry> idLabelEntryList = Arrays.asList(idLabelEntry);

        userGroupDataConversion.setDataToScheduleDataDTO(to, idLabelEntryList);
        Assert.assertEquals(to.getUserGroupArr().length, idLabelEntryList.size());
        Assert.assertEquals(to.getUserGroupArr()[0], idLabelEntryList.get(0));

    }

    /**
     * 测试obtainIdLabel方法
     *
     * @throws Exception 异常
     */
    @Test
    public void testObtainIdLabelEntryBy() throws Exception {
        UUID id = UUID.randomUUID();
        ThrowExceptionTester.throwIllegalArgumentException(() -> userGroupDataConversion.obtainIdLabelEntryBy(null), "id can not be null");

        IacUserGroupDetailDTO response = new IacUserGroupDetailDTO();
        response.setName("name");
        response.setId(id);


        new Expectations() {
            {
                cbbUserGroupAPI.getUserGroupDetail((UUID) any);
                result = response;
            }
        };
        IdLabelEntry idLabelEntry = userGroupDataConversion.obtainIdLabelEntryBy(id);
        Assert.assertEquals(idLabelEntry.getId(), response.getId());
        Assert.assertEquals(idLabelEntry.getLabel(), response.getName());
        new Verifications() {
            {
                cbbUserGroupAPI.getUserGroupDetail((UUID) any);
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
        ThrowExceptionTester.throwIllegalArgumentException(() -> userGroupDataConversion.isSupport(null), "from can not be null");
        Assert.assertFalse(userGroupDataConversion.isSupport(from));
        from.setUserGroupArr(new UUID[] {UUID.randomUUID()});
        Assert.assertTrue(userGroupDataConversion.isSupport(from));
    }


}