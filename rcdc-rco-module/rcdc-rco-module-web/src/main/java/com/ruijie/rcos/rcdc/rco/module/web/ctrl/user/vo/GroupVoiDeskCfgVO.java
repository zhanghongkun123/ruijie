package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.imagetemplate.ImageTemplateVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.strategy.DeskStrategyVO;

/**
 * Description: voi 桌面配置vo
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/11 3:58 下午
 *
 * @author linrenjian
 */
public class GroupVoiDeskCfgVO {

    private ImageTemplateVO image;

    private DeskStrategyVO strategy;

    private UserProfileStrategyDTO userProfileStrategy;

    /**
     * 软件管控策略
     */
    private SoftwareStrategyDTO softwareStrategy;

    public ImageTemplateVO getImage() {
        return image;
    }

    public void setImage(ImageTemplateVO image) {
        this.image = image;
    }

    public DeskStrategyVO getStrategy() {
        return strategy;
    }

    public void setStrategy(DeskStrategyVO strategy) {
        this.strategy = strategy;
    }

    public UserProfileStrategyDTO getUserProfileStrategy() {
        return userProfileStrategy;
    }

    public void setUserProfileStrategy(UserProfileStrategyDTO userProfileStrategy) {
        this.userProfileStrategy = userProfileStrategy;
    }

    public SoftwareStrategyDTO getSoftwareStrategy() {
        return softwareStrategy;
    }

    public void setSoftwareStrategy(SoftwareStrategyDTO softwareStrategy) {
        this.softwareStrategy = softwareStrategy;
    }
}
