package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.mock;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 金融版本-阳光资管-第三方mock用户返回值
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/19 17:23
 * @author zjy
 */
public class ThirdPartyMockUserResponse {

    public static final int SUCCESS_CODE = 000000;

    public static final int NORMAL_STATUS = 1;

    public static final int CANCELLED_STATUS = 2;

    private Integer code;

    private String msg;

    @JSONField(name = "data")
    private List<ThirdPartyDepartmentUserData> dataList;

    /**
     * 金融版本-阳光资管-第三方mock用户分组
     * Description: Function Description
     * Copyright: Copyright (c) 2020
     * Company: Ruijie Co., Ltd.
     * Create Time: 2023/9/19 17:23
     * @author zjy
     */
    public static class ThirdPartyDepartmentUserData {
        private List<ThirdPartyUserData> userList;

        private String vcOrgName;

        public List<ThirdPartyUserData> getUserList() {
            return userList;
        }

        public void setUserList(List<ThirdPartyUserData> userList) {
            this.userList = userList;
        }

        public String getVcOrgName() {
            return vcOrgName;
        }

        public void setVcOrgName(String vcOrgName) {
            this.vcOrgName = vcOrgName;
        }
    }

    /**
     * 金融版本-阳光资管-第三方mock用户
     * Description: Function Description
     * Copyright: Copyright (c) 2020
     * Company: Ruijie Co., Ltd.
     * Create Time: 2023/9/19 17:23
     * @author zjy
     */
    public static class ThirdPartyUserData {

        private String userName;

        private String userCode;

        private String userEmail;

        private Integer userStatus;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public Integer getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(Integer userStatus) {
            this.userStatus = userStatus;
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ThirdPartyDepartmentUserData> getDataList() {
        return dataList;
    }

    public void setDataList(List<ThirdPartyDepartmentUserData> dataList) {
        this.dataList = dataList;
    }
}
