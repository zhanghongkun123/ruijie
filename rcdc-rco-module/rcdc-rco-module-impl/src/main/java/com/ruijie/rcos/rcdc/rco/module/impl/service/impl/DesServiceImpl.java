package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author luojianmo
 * @Description:
 * @Company: Ruijie Co., Ltd.
 * @CreateTime: 2020-02-23 23:19
 */
@Service
public class DesServiceImpl implements DesService {

    @Override
    public String bCryptEncrypt(String password) {
        Assert.hasText(password, "password cannot empty");
        return BCrypt.hashpw(password, BCrypt.gensalt(Constants.BCRYPY_SALT));
    }
}
