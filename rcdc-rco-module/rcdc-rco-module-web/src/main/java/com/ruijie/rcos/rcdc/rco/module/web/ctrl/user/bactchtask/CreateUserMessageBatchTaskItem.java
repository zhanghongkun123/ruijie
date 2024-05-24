package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.UUID;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.UserMessageObjTypeEnum;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;

/**
 * 
 * Description: 创建用户消息批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月10日
 * 
 * @author jarman
 */
public class CreateUserMessageBatchTaskItem extends DefaultBatchTaskItem {   
    
    private String title;
    
    private String content;
    
    private UserMessageObjTypeEnum type;
    
    public CreateUserMessageBatchTaskItem(UUID itemId, String itemName) {
        super(itemId, itemName);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserMessageObjTypeEnum getType() {
        return type;
    }

    public void setType(UserMessageObjTypeEnum type) {
        this.type = type;
    }

}
