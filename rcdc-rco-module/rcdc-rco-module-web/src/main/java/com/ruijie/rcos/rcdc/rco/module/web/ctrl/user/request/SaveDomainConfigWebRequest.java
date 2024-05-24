package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdCoverTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 保存AD域配置的请求参数
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-04-09 15:35
 *
 * @author zql
 */
public class SaveDomainConfigWebRequest extends DomainConfigBaseWebRequest {

    @NotNull
    protected IacAdCoverTypeEnum coverType;

    @NotNull
    protected Boolean enable;

    /**
     * AD域上自动加域
     */
    @Nullable
    protected Boolean autoJoin;

    /**
     * AD域是否开启分页查询
     */
    @Nullable
    protected Boolean enablePageQuery = true;

    @Nullable
    protected String adOu;

    /**
     * AD域用户是否开启自动登录
     */
    @Nullable
    protected Boolean adAutoLogon = true;

    public IacAdCoverTypeEnum getCoverType() {
        return coverType;
    }

    public void setCoverType(IacAdCoverTypeEnum coverType) {
        this.coverType = coverType;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Nullable
    public Boolean getAutoJoin() {
        return autoJoin;
    }

    public void setAutoJoin(@Nullable Boolean autoJoin) {
        this.autoJoin = autoJoin;
    }

    @Nullable
    public Boolean getEnablePageQuery() {
        return enablePageQuery;
    }

    public void setEnablePageQuery(@Nullable Boolean enablePageQuery) {
        this.enablePageQuery = enablePageQuery;
    }

    @Nullable
    public String getAdOu() {
        return adOu;
    }

    public void setAdOu(@Nullable String adOu) {
        this.adOu = adOu;
    }

    @Nullable
    public Boolean getAdAutoLogon() {
        return adAutoLogon;
    }

    public void setAdAutoLogon(@Nullable Boolean adAutoLogon) {
        this.adAutoLogon = adAutoLogon;
    }
}
