INSERT INTO t_rco_global_parameter(id, param_key, param_value, default_value, create_time, update_time, version)
VALUES ('aeb48ba3-0cb3-438b-9f3a-c5f41d7393e3', 'third_party_auth_code_config', '{"enable":false}', '{"enable":false}', now(), now(), 0) ON CONFLICT(param_key) DO NOTHING;
-- 文件导出审计FTP全局配置更新
UPDATE t_rco_global_parameter SET param_value = '{"ftpPort": 2021,"ftpUserName": "auditFileFtp",
"ftpUserPassword": "c2QTOAsDO3IJ","ftpPath": "/","fileDir": "/data/audit_file"}' where id = '28912ea2-c640-47d2-9bc9-a927524f75b5';

-- upm + 还原 + 用户账户同步
INSERT INTO t_rco_user_profile_path VALUES ('eced9399-4290-4189-96d6-4bbde3ac2999', '476e2c79-73fa-4107-8587-400121894234', '用户身份验证凭据',
 '登录Windows系统的用户名和密码', 'SPECIAL', now(), '2023-09-25 10:15:46.207', 0, 'admin', 'f') ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_user_profile_child_path VALUES('0fd0b1e7-b8d4-4995-a84d-94b25bb0b546',  'SYNCHRO', 'REGISTRY_KEY', 'eced9399-4290-4189-96d6-4bbde3ac2999',
0, 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_user_profile_path_detail VALUES ('131f08e7-6709-4516-9750-82e24135a6fd','HKEY_LOCAL_MACHINE\SAM', '0fd0b1e7-b8d4-4995-a84d-94b25bb0b546','eced9399-4290-4189-96d6-4bbde3ac2999', 0, 0) ON CONFLICT(id) DO NOTHING;
INSERT INTO t_rco_user_profile_path_detail VALUES ('adbff77c-8118-4671-ad8c-43d82ddb969e','HKEY_LOCAL_MACHINE\SECURITY', '0fd0b1e7-b8d4-4995-a84d-94b25bb0b546','eced9399-4290-4189-96d6-4bbde3ac2999', 0, 0) ON CONFLICT(id) DO NOTHING;

--dml 迁移S4全局用户自助快照到策略，同时需要兼容企金1.0升级1.1
do $$
begin
    if exists (select * from t_sk_global_parameter where param_key = 'is_user_snapshot_switch')
    then
        update t_cbb_desk_strategy set enable_user_snapshot = (select param_value from t_sk_global_parameter where param_key = 'is_user_snapshot_switch')::bool  where strategy_type = 'VDI' and (pattern = 'PERSONAL' or person_size > 0);
        delete from t_sk_global_parameter where param_key = 'is_user_snapshot_switch';
end if;
end $$
;

-- 解决兼容企金2.0刷机场景，用户相关表迁移到身份中心
do $$
begin
    if exists (SELECT 1 FROM information_schema.tables WHERE table_name = 't_cbb_user')
    then
        -- 第三方类型用户开启第三方认证,
        UPDATE t_rco_user_identity_config t0 SET open_third_party_certification = TRUE, open_account_password_certification = FALSE
        WHERE t0.id IN (SELECT t1.id FROM t_rco_user_identity_config t1, t_cbb_user t2 WHERE t1.related_id = t2.id AND t2.user_type = 'THIRD_PARTY');

        -- 第三方类型用户组开启第三方认证
        UPDATE t_rco_user_identity_config t0 SET open_third_party_certification = TRUE, open_account_password_certification = FALSE
        WHERE t0.id IN (SELECT t1.id FROM t_rco_user_identity_config t1, t_cbb_user_group t2 WHERE t1.related_id = t2.id AND t2.is_third_party_group = true);
    else
        RAISE NOTICE 't_cbb_user does not exist,skip';
end if;
end $$
;
