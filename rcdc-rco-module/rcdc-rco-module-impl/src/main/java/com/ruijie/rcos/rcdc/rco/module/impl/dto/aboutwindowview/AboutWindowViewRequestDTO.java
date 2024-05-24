package com.ruijie.rcos.rcdc.rco.module.impl.dto.aboutwindowview;

 /**
 * Description: 查询关于窗口特征码请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/04/06
 *
 * @author zhang.zhiwen
 */
public class AboutWindowViewRequestDTO {

     /**
      * 终端mac地址
      */
    private String terminalId;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
