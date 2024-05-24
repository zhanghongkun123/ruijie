package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/19
 *
 * @author nt
 */
@RunWith(SkyEngineRunner.class)
public class DeleteTerminalGroupBatchTaskItemTest {

    /**
     * test
     */
    @Test
    public void testDeleteTerminalGroupBatchTaskItem() {
        UUID itemId = UUID.randomUUID();
        DeleteTerminalGroupBatchTaskItem taskItem = new DeleteTerminalGroupBatchTaskItem(itemId, "123", null);

        assertEquals(itemId, taskItem.getItemID());
        assertEquals("123", taskItem.getItemName());
        assertEquals(null, taskItem.getMoveGroupId());

        UUID moveGroupId = UUID.randomUUID();
        taskItem.setMoveGroupId(moveGroupId);
        assertEquals(moveGroupId, taskItem.getMoveGroupId());
    }

}
