package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.PublicBusinessKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;

/**
 * Description: UserGroupHelper测试类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/15 6:00 下午
 *
 * @author zhouhuan
 */
@RunWith(SkyEngineRunner.class)
public class TerminalGroupHelperTest {

    @Tested
    private TerminalGroupHelper terminalGroupHelper;

    @Injectable
    private SessionContext sessionContext;

    /**
     * before
     */
    @Before
    public void before() {
        new MockUp<LocaleI18nResolver>() {
            @Mock
            public String resolve(String key, String... args) {
                return key;
            }
        };
    }

    /**
     * testGetErrorTipIsAllGroupPermission
     */
    @Test
    public void testGetErrorTipIsAllGroupPermission() {
        String errorTip = terminalGroupHelper.getErrorTip(sessionContext, new BusinessException("123"), true);

        Assert.assertEquals("123", errorTip);
    }

    /**
     * testGetErrorTipGroupNumExceedLimit
     */
    @Test
    public void testGetErrorTipGroupNumExceedLimit() {
        String errorTip = terminalGroupHelper.getErrorTip(sessionContext, new BusinessException(PublicBusinessKey.RCDC_TERMINALGROUP_GROUP_NUM_EXCEED_LIMIT), false);

        Assert.assertEquals(TerminalBusinessKey.RCDC_TERMINALGROUP_GROUP_NUM_EXCEED_LIMIT_FOR_SYSADMIN, errorTip);
    }

    /**
     * testGetErrorTipGroupNameDuplicate
     */
    @Test
    public void testGetErrorTipGroupNameDuplicate() {
        String errorTip = terminalGroupHelper.getErrorTip(sessionContext, new BusinessException(PublicBusinessKey.RCDC_TERMINALGROUP_GROUP_NAME_DUPLICATE), false);

        Assert.assertEquals(TerminalBusinessKey.RCDC_TERMINALGROUP_GROUP_NAME_DUPLICATE_FOR_SYSADMIN, errorTip);
    }

    /**
     * testGetErrorTipGroupNameDuplicateWithMoveGroup
     */
    @Test
    public void testGetErrorTipGroupNameDuplicateWithMoveGroup() {
        String errorTip = terminalGroupHelper.getErrorTip(sessionContext, new BusinessException(PublicBusinessKey.RCDC_DELETE_TERMINAL_GROUP_SUB_GROUP_HAS_DUPLICATION_WITH_MOVE_GROUP), false);

        Assert.assertEquals(TerminalBusinessKey.RCDC_DELETE_TERMINAL_GROUP_SUB_GROUP_HAS_DUPLICATION_WITH_MOVE_GROUP_FOR_SYSADMIN, errorTip);
    }

    /**
     * testGetErrorTipGroupSubNumExceedLimit
     */
    @Test
    public void testGetErrorTipGroupSubNumExceedLimit() {
        String errorTip = terminalGroupHelper.getErrorTip(sessionContext, new BusinessException(PublicBusinessKey.RCDC_TERMINALGROUP_SUB_GROUP_NUM_EXCEED_LIMIT), false);

        Assert.assertEquals(TerminalBusinessKey.RCDC_TERMINALGROUP_SUB_GROUP_NUM_EXCEED_LIMIT_FOR_SYSADMIN, errorTip);
    }
}
