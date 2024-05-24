package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbComponentIndependentUpgradeTaskDTO;
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
public class ComponentIndependentUpgradeTaskDetailContentVO {

    private CbbSystemUpgradeTaskTerminalDTO[] terminalArr;

    private Long total;

    private Integer waitNum;

    private Integer upgradingNum;

    private Integer successNum;

    private Integer failNum;

    private Integer unsupportNum;

    private Integer undoNum;

    private CbbComponentIndependentUpgradeTaskDTO upgradeTask;

    private IdLabelEntry[] terminalGroupArr;

    private PackageComponentVO[] packageComponentArr;

    public CbbSystemUpgradeTaskTerminalDTO[] getTerminalArr() {
        return terminalArr;
    }

    public void setTerminalArr(CbbSystemUpgradeTaskTerminalDTO[] terminalArr) {
        this.terminalArr = terminalArr;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public CbbComponentIndependentUpgradeTaskDTO getUpgradeTask() {
        return upgradeTask;
    }

    public void setUpgradeTask(CbbComponentIndependentUpgradeTaskDTO upgradeTask) {
        this.upgradeTask = upgradeTask;
    }

    public Integer getWaitNum() {
        return waitNum == null ? 0 : waitNum;
    }

    public void setWaitNum(Integer waitNum) {
        this.waitNum = waitNum;
    }

    public Integer getUpgradingNum() {
        return upgradingNum == null ? 0 : upgradingNum;
    }

    public void setUpgradingNum(Integer upgradingNum) {
        this.upgradingNum = upgradingNum;
    }

    public Integer getSuccessNum() {
        return successNum == null ? 0 : successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public Integer getFailNum() {
        return failNum == null ? 0 : failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public Integer getUnsupportNum() {
        return unsupportNum == null ? 0 : unsupportNum;
    }

    public void setUnsupportNum(Integer unsupportNum) {
        this.unsupportNum = unsupportNum;
    }

    public Integer getUndoNum() {
        return undoNum == null ? 0 : undoNum;
    }

    public void setUndoNum(Integer undoNum) {
        this.undoNum = undoNum;
    }

    public IdLabelEntry[] getTerminalGroupArr() {
        return terminalGroupArr;
    }

    public void setTerminalGroupArr(IdLabelEntry[] terminalGroupArr) {
        this.terminalGroupArr = terminalGroupArr;
    }

    public PackageComponentVO[] getPackageComponentArr() {
        return packageComponentArr;
    }

    public void setPackageComponentArr(PackageComponentVO[] packageComponentArr) {
        this.packageComponentArr = packageComponentArr;
    }
}
