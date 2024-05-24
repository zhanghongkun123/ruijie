-- 短信网关全局配置
INSERT INTO t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES('036c872e-11ce-41da-9c01-e52750dcfce6', 'sms_gateway_config', '{"enable":false,"countryCode":"86","platformType":"HTTP","httpConfig":null}', 
NULL, now(), now(), 0) ON CONFLICT(id) DO NOTHING;

-- 短信密码找回全局配置
INSERT INTO t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES('a6af4723-7ba3-4ba5-81a0-1e8f3023a9c1', 'sms_recover_pwd_config', '{"enable":false,"interval":60,"period":1,
"numberLimit":0,"maxErrorNumber":3,"smsTemplate":"【锐捷网络】您正在使用短信验证码找回密码，验证码为：<VERIFY_CODE>，验证码有效期为<MINUTE>分钟。"}',
NULL, now(), now(), 0) ON CONFLICT(id) DO NOTHING;

INSERT INTO t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES ('8c6c9979-d16a-4442-9711-9e02c46b0b58', 'disconnect_rcenter_duration', '3600000', '3600000', now(), now(), '0');