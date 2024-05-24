--配置扫码认证策略
INSERT INTO t_rco_global_parameter ( "id", "param_key", "param_value", "default_value", "create_time", "update_time", "version" )
VALUES  ('c3630190-ccf5-4271-8de8-e3f7748639a7', 'cas_scan_code_auth', '{"applyName":"CAS认证","applyServicePrefix":"","applyPrompt":"请使用顺丰APP进行扫码","applyAuthCode":"","applyOpen":false,"sslConfig":null}',
	'{"applyName":"CAS认证","applyServicePrefix":"","applyPrompt":"请使用顺丰APP进行扫码","applyAuthCode":"","applyOpen":false,"sslConfig":null}', now( ), now( ), 0 );

--新增主要策略，兼容以前版本
--针对用户组
-- 解决兼容企金2.0刷机场景，用户相关表迁移到身份中心
do $$
begin
    if exists (SELECT 1 FROM information_schema.tables WHERE table_name = 't_cbb_user')
    then
        INSERT INTO t_rco_user_identity_config (id, related_type, related_id, login_identity_level, version, open_account_password_certification)
        SELECT md5(random()::text || clock_timestamp()::text)::uuid ,
        		 'USERGROUP', g."id", 'AUTO_LOGIN', 0, TRUE
        FROM t_cbb_user_group g
        WHERE NOT EXISTS
            (SELECT *
            FROM t_rco_user_identity_config i
            WHERE i.related_id = g."id"
            		AND i.related_type = 'USERGROUP');
        --针对用户
        INSERT INTO t_rco_user_identity_config (id, related_type, related_id, login_identity_level, version, open_account_password_certification)
        SELECT md5(random()::text || clock_timestamp()::text)::uuid,
        		 'USER', u.id, 'AUTO_LOGIN', 0, true
        FROM t_cbb_user u
        WHERE NOT EXISTS
            (SELECT *
            FROM t_rco_user_identity_config i
            WHERE i.related_id = u."id"
            		AND related_type = 'USER') AND u.user_type != 'VISITOR';
    else
        RAISE NOTICE 't_cbb_user does not exist,skip';
end if;
end $$
;

--所有用户或用户组：默认开启账号密码认证策略
UPDATE t_rco_user_identity_config SET open_account_password_certification = true;
---终端工作模式支持
INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
VALUES ('ccec31ac-af98-45a6-bc7c-9ad2a9e5923d', 'RG-CT5300C-CS', 'IDV,VOI', 'product', true, now(), now(),0, 'RG-CT5',null);

INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
VALUES ('b45d9e69-16cd-457c-b4fe-97ce84e12aa8', 'RG-CT3100C-G2', 'VDI', 'product', true, now(), now(),0, 'RG-CT3',null);

INSERT INTO t_rco_terminal_work_mode_mapping (id, support_mode, working_mode, judge_basis, enable_state, create_time, update_time, version,match_rule,sub_match_rule)
VALUES ('d2bd6b4a-ad54-425d-982f-715a5e35e12a', 'RG-CT3100-G2', 'VDI', 'product', true, now(), now(),0, 'RG-CT3',null);

-- 定时发送syslog cron表达式
INSERT INTO
    t_rco_global_parameter("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES
	( '13f60a6a-d7aa-40b9-bd6d-d8ec34d36a9d', 'send_syslog_schedule_cron', '0 0 3 * * ?', '0 0 3 * * ?', now(), now(), 0 );

-- 当天是否需要发送syslog
INSERT INTO
    t_rco_global_parameter("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES
	( '70432a53-431d-487c-a60b-c6903ea48dda', 'need_send_syslog', 'true', 'true', now(), now(), 0 );