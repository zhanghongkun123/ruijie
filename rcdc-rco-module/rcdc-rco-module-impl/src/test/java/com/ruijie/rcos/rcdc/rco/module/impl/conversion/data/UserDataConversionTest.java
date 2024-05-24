package com.ruijie.rcos.rcdc.rco.module.impl.conversion.data;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
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
public class UserDataConversionTest {

    @Tested
    private UserDataConversion userDataConversion;

    @Injectable
    private IacUserMgmtAPI cbbUserAPI;

    /**
     * 测试获取idArr方法
     */
    @Test
    public void testGetIdArrayFromScheduleData() {
        UUID[] userArr = new UUID[] {UUID.randomUUID()};
        ScheduleDataDTO<UUID, String> from = new ScheduleDataDTO<>();
        from.setUserArr(userArr);
        Assert.assertEquals(userDataConversion.getIdArrayFromScheduleData(from), userArr);

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

        userDataConversion.setDataToScheduleDataDTO(to, idLabelEntryList);
        Assert.assertEquals(to.getUserArr().length, idLabelEntryList.size());
        Assert.assertEquals(to.getUserArr()[0], idLabelEntryList.get(0));

    }

    /**
     * 测试obtainIdLabel方法
     * 
     * @throws Exception 异常
     */
    @Test
    public void testObtainIdLabelEntryBy() throws Exception {
        UUID id = UUID.randomUUID();
        ThrowExceptionTester.throwIllegalArgumentException(() -> userDataConversion.obtainIdLabelEntryBy(null), "id can not be null");

        IacUserDetailDTO userDetail = new IacUserDetailDTO();
        userDetail.setUserName("name");
        userDetail.setId(id);


        new Expectations() {
            {
                cbbUserAPI.getUserDetail((UUID) any);
                result = userDetail;
            }
        };
        IdLabelEntry idLabelEntry = userDataConversion.obtainIdLabelEntryBy(id);
        Assert.assertEquals(idLabelEntry.getId(), userDetail.getId());
        Assert.assertEquals(idLabelEntry.getLabel(), userDetail.getUserName());
        new Verifications() {
            {
                cbbUserAPI.getUserDetail((UUID) any);
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
        ThrowExceptionTester.throwIllegalArgumentException(() -> userDataConversion.isSupport(null), "from can not be null");
        Assert.assertFalse(userDataConversion.isSupport(from));
        from.setUserArr(new UUID[] {UUID.randomUUID()});
        Assert.assertTrue(userDataConversion.isSupport(from));
    }



}
