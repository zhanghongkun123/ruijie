INSERT INTO t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES('869cafef-6085-403b-8c91-d74a590a8809', 'audit_file_global_strategy', '{"enableExtStorage":false,"interval":180}', NULL, 
now(), now(), 0) ON CONFLICT(id) DO NOTHING;

INSERT INTO t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES('28912ea2-c640-47d2-9bc9-a927524f75b5', 'audit_file_ftp_config', '{"ftpPort": 2021,"ftpUserName": "auditFileFtp",
"ftpUserPassword": "21Wq_Er>L","ftpPath": "/","fileDir": "/data/audit_file"}', NULL, now(), now(), 0) ON CONFLICT(id) DO NOTHING;



-- 云桌面授权使用情况统计表过期天数
INSERT INTO t_sk_global_parameter(id, param_key, param_value, default_value, create_time, update_time, version)
VALUES ('88e668ad-b866-4980-983f-504947483231', 'desktop_license_stat_retain_day', '400', '400', now(), now(), 0) ON CONFLICT(id) DO NOTHING;