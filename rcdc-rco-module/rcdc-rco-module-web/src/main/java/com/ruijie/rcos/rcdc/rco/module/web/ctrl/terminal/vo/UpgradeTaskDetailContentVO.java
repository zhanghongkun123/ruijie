package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbSystemUpgradeTaskDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbSystemUpgradeTaskTerminalDTO;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * 
 * Description: 升级任务终端列表VO
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月19日
 * 
 * @author nt
 */
public class UpgradeTaskDetailContentVO {

    private CbbSystemUpgradeTaskTerminalDTO[] terminalArr;

    private long total;

    private int waitNum;

    private int upgradingNum;

    private int successNum;

    private int failNum;

    private int unsupportNum;

    private int undoNum;

    private CbbSystemUpgradeTaskDTO upgradeTask;

    private IdLabelEntry[] terminalGroupArr;

    public CbbSystemUpgradeTaskTerminalDTO[] getTerminalArr() {
        return terminalArr;
    }

    public void setTerminalArr(CbbSystemUpgradeTaskTerminalDTO[] terminalArr) {
        this.terminalArr = terminalArr;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public CbbSystemUpgradeTaskDTO getUpgradeTask() {
        return upgradeTask;
    }

    public void setUpgradeTask(CbbSystemUpgradeTaskDTO upgradeTask) {
        this.upgradeTask = upgradeTask;
    }

    public int getWaitNum() {
        return waitNum;
    }

    public void setWaitNum(int waitNum) {
        this.waitNum = waitNum;
    }

    public int getUpgradingNum() {
        return upgradingNum;
    }

    public void setUpgradingNum(int upgradingNum) {
        this.upgradingNum = upgradingNum;
    }

    public int getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(int successNum) {
        this.successNum = successNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }

    public int getUnsupportNum() {
        return unsupportNum;
    }

    public void setUnsupportNum(int unsupportNum) {
        this.unsupportNum = unsupportNum;
    }

    public int getUndoNum() {
        return undoNum;
    }

    public void setUndoNum(int undoNum) {
        this.undoNum = undoNum;
    }

    public IdLabelEntry[] getTerminalGroupArr() {
        return terminalGroupArr;
    }

    public void setTerminalGroupArr(IdLabelEntry[] terminalGroupArr) {
        this.terminalGroupArr = terminalGroupArr;
    }
}
