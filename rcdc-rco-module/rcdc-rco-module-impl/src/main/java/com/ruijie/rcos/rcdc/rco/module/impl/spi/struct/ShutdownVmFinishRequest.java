package com.ruijie.rcos.rcdc.rco.module.impl.spi.struct;

import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月7日
 * 
 * @author artom
 */
public class ShutdownVmFinishRequest {
    
    /** 主动监测到关机 */
    public static final int MODE_AUTO = 0;

    /** 用户浮动条触发关机 */
    public static final int MODE_MANUAL = 1;
    
    private int code = CommonMessageCode.SUCCESS;
    
    private ShutdownDetail content;
    
    
    public ShutdownVmFinishRequest() {
        content = new ShutdownDetail(MODE_AUTO);
    }
    
    public ShutdownVmFinishRequest(int mode, String deskId) {
        content = new ShutdownDetail(mode, deskId);
    }
    
    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }

    
    public ShutdownDetail getContent() {
        return content;
    }

    public void setContent(ShutdownDetail content) {
        this.content = content;
    }

    /**
     * 
     * @author artom
     *
     */
    public class ShutdownDetail {
        private int mode = MODE_AUTO;

        /**
         * 桌面ID
         */
        private String deskId;

        public ShutdownDetail(int mode) {
            this.mode = mode;
        }

        public ShutdownDetail(int mode, String deskId) {
            this.mode = mode;
            this.deskId = deskId;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public String getDeskId() {
            return deskId;
        }

        public void setDeskId(String deskId) {
            this.deskId = deskId;
        }

        @Override
        public String toString() {
            return "ShutdownDetail [model=" + mode + ",deskId=" + deskId + "]";
        }
    }

}
