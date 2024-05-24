-- 初始化短信认证全局配置
delete from  t_rco_global_parameter where id = 'a4105516-acb8-41c7-a291-96448d63f1d4';
INSERT INTO t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES('a4105516-acb8-41c7-a291-96448d63f1d4', 'sms_auth_config', '{"enable":false,"autoBindPhone":true,"interval":60,"period":1,"numberLimit":0,
"maxErrorNumber":3,"smsTemplate":"【锐捷网络】您正在使用短信验证码登录云桌面或绑定用户手机号，验证码为：<VERIFY_CODE>，验证码有效期为<MINUTE>分钟。"}',
NULL, now(), now(), 0) ON CONFLICT(id) DO NOTHING;



INSERT INTO t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES('a4105516-acb8-41c7-a291-96448d63f1d5', 'publish_image_max_time', '7200000',
       '7200000', now(), now(), 0) ON CONFLICT(id) DO NOTHING;


