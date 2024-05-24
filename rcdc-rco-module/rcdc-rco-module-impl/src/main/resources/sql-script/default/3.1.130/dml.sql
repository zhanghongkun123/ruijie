-- 是否开启系统盘自动扩容
INSERT INTO t_rco_global_parameter ( "id", "param_key", "param_value", "default_value", "create_time", "update_time", "version" )
VALUES( 'f7903c7a-0307-3d20-04b2-729095ae6f02', 'enable_full_system_disk', 'false', 'false', now(), now(), 0 );


update t_rco_software set file_custom_md5_black_flag = true;
-- 初始化软件白名单全局开关
delete from  t_rco_global_parameter where id = 'ed0dad6e-6f75-46ca-8d93-0dd1c055bd8b';
INSERT INTO "t_rco_global_parameter" ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES ('ed0dad6e-6f75-46ca-8d93-0dd1c055bd8b', 'ENABLE_SOFTWARE_STRATEGY', 'false', 'false', now(), now(), 0);

update t_rco_software_group set group_type = 'DEFAULT' where name = '未分组';