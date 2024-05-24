package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchResultDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 搜索返回给前端的数据定义
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/3
 *
 * @author Jarman
 */
public class SearchResultResponse extends DefaultResponse {

    private SearchResultDTO user;

    private SearchResultDTO vdi;

    private SearchResultDTO app;

    private SearchResultDTO idv;

    private SearchResultDTO desktop;

    private SearchResultDTO voi;

    private SearchResultDTO pc;

    public SearchResultDTO getVoi() {
        return voi;
    }

    public void setVoi(SearchResultDTO voi) {
        this.voi = voi;
    }

    public SearchResultDTO getUser() {
        return user;
    }

    public void setUser(SearchResultDTO user) {
        this.user = user;
    }

    public SearchResultDTO getVdi() {
        return vdi;
    }

    public void setVdi(SearchResultDTO vdi) {
        this.vdi = vdi;
    }

    public SearchResultDTO getDesktop() {
        return desktop;
    }

    public void setDesktop(SearchResultDTO desktop) {
        this.desktop = desktop;
    }

    public SearchResultDTO getApp() {
        return app;
    }

    public void setApp(SearchResultDTO app) {
        this.app = app;
    }

    public SearchResultDTO getIdv() {
        return idv;
    }

    public void setIdv(SearchResultDTO idv) {
        this.idv = idv;
    }

    public SearchResultDTO getPc() {
        return pc;
    }

    public void setPc(SearchResultDTO pc) {
        this.pc = pc;
    }
}
