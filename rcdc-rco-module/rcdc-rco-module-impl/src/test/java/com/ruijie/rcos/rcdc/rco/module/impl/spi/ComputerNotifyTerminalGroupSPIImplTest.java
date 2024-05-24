package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.request.CbbTerminalGroupOperNotifyRequest;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author wjp
 * @Description:
 * @Company: Ruijie Co., Ltd.
 * @CreateTime: 2020-02-17 11:00
 */
@RunWith(SkyEngineRunner.class)
public class ComputerNotifyTerminalGroupSPIImplTest {

    @Tested
    private ComputerNotifyTerminalGroupSPIImpl computerNotifyTerminalGroupSPI;

    @Injectable
    private ComputerBusinessDAO computerBusinessDAO;

    @Test
    public void testNotifyTerminalGroupChangeByException() {
        CbbTerminalGroupOperNotifyRequest terminalGroupOperNotifyRequest = new CbbTerminalGroupOperNotifyRequest();
        terminalGroupOperNotifyRequest.setDeleteIdSet(Collections.singleton(UUID.randomUUID()));
        terminalGroupOperNotifyRequest.setMoveGroupId(UUID.randomUUID());
        new Expectations() {
            {
                computerBusinessDAO.findByTerminalGroupId((UUID) any);
                result = null;
            }
        };
        computerNotifyTerminalGroupSPI.notifyTerminalGroupChange(terminalGroupOperNotifyRequest);

        new Verifications() {
            {
                computerBusinessDAO.save((ComputerEntity) any);
                times = 0;
            }
        };
    }

    @Test
    public void testNotifyTerminalGroupChange() {
        CbbTerminalGroupOperNotifyRequest terminalGroupOperNotifyRequest = new CbbTerminalGroupOperNotifyRequest();
        terminalGroupOperNotifyRequest.setDeleteIdSet(Collections.singleton(UUID.randomUUID()));
        terminalGroupOperNotifyRequest.setMoveGroupId(UUID.randomUUID());
        List<ComputerEntity> computerEntityList = new ArrayList<>();
        ComputerEntity computerEntity = new ComputerEntity();
        computerEntity.setId(UUID.randomUUID());
        computerEntityList.add(computerEntity);
        new Expectations() {
            {
                computerBusinessDAO.findByTerminalGroupId((UUID) any);
                result = computerEntityList;
            }
        };
        computerNotifyTerminalGroupSPI.notifyTerminalGroupChange(terminalGroupOperNotifyRequest);

        new Verifications() {
            {
                computerBusinessDAO.save((ComputerEntity) any);
                times = 1;
            }
        };
    }
}
