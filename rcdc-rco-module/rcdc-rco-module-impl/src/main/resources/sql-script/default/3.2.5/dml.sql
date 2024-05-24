/**增加邮箱服务器配置*/
INSERT INTO t_rco_global_parameter (id, param_key, param_value, default_value, create_time, update_time,version)
VALUES ('7768cb9f-df2b-420a-81c4-395953919730','server_mail_config', '{"enableSendMail":false,"fromMailAccount":"","loginName":"","loginPassword":"","serverPort":"","serverAddress":""}', '{"enableSendMail":false,"fromMailAccount":"","loginName":"","loginPassword":"","serverPort":"","serverAddress":""}', now(), now(), 0) ON CONFLICT (param_key) DO NOTHING;
