package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

/**
 * Description: 导出用户请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月31日
 *
 * @author tangxu
 */

public class ExportUserSearchRequest extends PageSearchRequest {

    @Nullable
    private UUID userId;

    @Nullable
    private Integer size;

    @Nullable
    private ExactMatch[] exactMatchArr;

    /**
     * 用户id
     */
    public static final String ID = "id";

    /**
     * 用户组id
     */
    public static final String GROUP_ID = "groupId";

    /**
     * 用户类型
     */
    public static final String USER_TYPE = "userType";

    /**
     * 用户状态
     */
    public static final String USER_STATE = "userState";

    /**
     * 是否开启本地密码认证
     */
    public static final String OPEN_ACCOUNT_PASSWORD_CERTIFICATION = "openAccountPasswordCertification";

    /**
     * 状态
     */
    public static final String STATE = "state";

    /**
     * 动态应用组id
     */
    public static final String APP_GROUP_ID = "appGroupId";

    /**
     * 转换查询条件
     */
    public void convertExactMatch() {
        MatchEqual[] matchEqualArr = new MatchEqual[exactMatchArr.length];

        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual;
            switch (exactMatch.getName()) {
                case ID:
                case GROUP_ID:
                case APP_GROUP_ID:
                    String[] valueArr = exactMatch.getValueArr();
                    UUID[] idArr = new UUID[valueArr.length];
                    for (int j = 0; j < valueArr.length; j++) {
                        idArr[j] = UUID.fromString(valueArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), idArr);
                    break;
                case USER_TYPE:
                    String[] userTypeStrArr = exactMatch.getValueArr();
                    IacUserTypeEnum[] userTypeArr = new IacUserTypeEnum[userTypeStrArr.length];
                    for (int j = 0; j < userTypeStrArr.length; j++) {
                        userTypeArr[j] = IacUserTypeEnum.valueOf(userTypeStrArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), userTypeArr);
                    break;
                case USER_STATE:
                    String[] stateStrArr = exactMatch.getValueArr();
                    IacUserStateEnum[] stateArr = new IacUserStateEnum[stateStrArr.length];
                    for (int j = 0; j < stateStrArr.length; j++) {
                        stateArr[j] = IacUserStateEnum.valueOf(stateStrArr[j]);
                    }
                    matchEqual = new MatchEqual(STATE, stateArr);
                    break;
                case OPEN_ACCOUNT_PASSWORD_CERTIFICATION:
                    String[] openArr = exactMatch.getValueArr();
                    Boolean[] openBoolArr = new Boolean[openArr.length];
                    for (int j = 0; j < openArr.length; j++) {
                        openBoolArr[j] = Boolean.valueOf(openArr[j]);
                    }
                    matchEqual = new MatchEqual(exactMatch.getName(), openBoolArr);
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    break;
            }
            matchEqualArr[i] = matchEqual;
        }

        this.setMatchEqualArr(matchEqualArr);
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * 构造key
     *
     * @return 返回key
     */
    public String getExportKey() {
        return this.getUserId().toString();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Nullable
    public ExactMatch[] getExactMatchArr() {
        return exactMatchArr;
    }

    public void setExactMatchArr(@Nullable ExactMatch[] exactMatchArr) {
        this.exactMatchArr = exactMatchArr;
    }
}
