-- 修复低版本有问题的数据
UPDATE t_rco_user_identity_config SET max_hardware_num = 10, "version" = "version" + 1, update_time = now()
WHERE related_type = 'USER' AND open_hardware_certification IS TRUE AND max_hardware_num IS NULL;