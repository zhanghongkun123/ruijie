---记住我功能开关功能
INSERT INTO t_rco_global_parameter(
 id, param_key, param_value, default_value, create_time, update_time, version)
 VALUES ('a1c4524b-1754-479a-8f6e-fa228c4f1c53', 'remember_password', 'true', 'true', now(), now(), 0);
---Windows密码不能为空
INSERT INTO t_rco_global_parameter(
 id, param_key, param_value, default_value, create_time, update_time, version)
 VALUES ('2b941d39-d022-483b-be21-34e32a73bf7d', 'windows_auto_logon', 'false', 'false', now(), now(), 0);
---修改密码功能开关功能
INSERT INTO t_rco_global_parameter(
 id, param_key, param_value, default_value, create_time, update_time, version)
 VALUES ('fc4d9625-91c6-4dc3-ad57-ab978c480778', 'change_password', 'true', 'true', now(), now(), 0);